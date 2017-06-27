/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hadoop.hive.serde2.lazy;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.serde.serdeConstants;
import org.apache.hadoop.hive.serde2.AbstractSerDe;
import org.apache.hadoop.hive.serde2.ByteStream;
import org.apache.hadoop.hive.serde2.SerDe;
import org.apache.hadoop.hive.serde2.SerDeException;
import org.apache.hadoop.hive.serde2.SerDeStats;
import org.apache.hadoop.hive.serde2.objectinspector.ListObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.MapObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector.Category;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.StructField;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.UnionObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.typeinfo.StructTypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfoFactory;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfoUtils;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

/**
 * LazySimpleSerDe can be used to read the same data format as
 * MetadataTypedColumnsetSerDe and TCTLSeparatedProtocol.
 *
 * However, LazySimpleSerDe creates Objects in a lazy way, to provide better
 * performance.
 * 采用懒加载的方式,将一行数据内容序列化成字节数组,或者将字节数组反序列化成一行内容。。因为网络传输的都是字节数组
 *
 * Also LazySimpleSerDe outputs typed columns instead of treating all columns as
 * String like MetadataTypedColumnsetSerDe.
 */
public class LazySimpleSerDe extends AbstractSerDe {

  public static final Log LOG = LogFactory.getLog(LazySimpleSerDe.class
      .getName());

  public static final String SERIALIZATION_EXTEND_NESTING_LEVELS
    = "hive.serialization.extend.nesting.levels";//值是true或者false

  public static final byte[] DefaultSeparators = {(byte) 1, (byte) 2, (byte) 3};

  private ObjectInspector cachedObjectInspector;

  private long serializedSize;//序列化一行的字节大小
  private SerDeStats stats;
  private boolean lastOperationSerialize;//true表示最后处理的是序列化内容
  private boolean lastOperationDeserialize;//true表示最后处理的是反序列化内容

  @Override
  public String toString() {
    return getClass().toString()
        + "["
        + Arrays.asList(serdeParams.separators)
        + ":"
        + ((StructTypeInfo) serdeParams.rowTypeInfo).getAllStructFieldNames()
        + ":"
        + ((StructTypeInfo) serdeParams.rowTypeInfo)
            .getAllStructFieldTypeInfos() + "]";
  }

  public LazySimpleSerDe() throws SerDeException {
  }

  /**
   * Return the byte value of the number string.
   *
   * @param altValue
   *          The string containing a number.包含数字的参数
   * @param defaultVal
   *          If the altValue does not represent a number, return the
   *          defaultVal.
   * 从字符串参数理论上是数字,127以内的数字,如果非数字则抛异常,则返回第一个字节即可
   * 如果是数字,则byte就是该数字对应的字节,例如altValue=89,返回值就是89,(char)89,返回则是Y  
   */
  public static byte getByte(String altValue, byte defaultVal) {
    if (altValue != null && altValue.length() > 0) {
      try {
        return Byte.valueOf(altValue).byteValue();
      } catch (NumberFormatException e) {
        return (byte) altValue.charAt(0);
      }
    }
    return defaultVal;
  }

  /**
   * SerDeParameters.
   * 序列化和反序列化的参数明细信息
   */
  public static class SerDeParameters {
    byte[] separators = DefaultSeparators;//包含1,2,3,其他扩展字符,目的是为了拆分等作用,第1位拆分field的字节,默认是1,第2位拆分集合的拆分配置key,默认值是002,第三位拆分map的字节,默认是3
    String nullString;//默认是\\N  表示如果是null的时候,如何写入数据
    Text nullSequence;//是hadoop序列化后的空值,即new Text(nullString)
    TypeInfo rowTypeInfo;//记录整行的数据需要的属性集合和属性类型集合,即是columnNames和columnTypes的整合
    boolean lastColumnTakesRest;//是否允许最后一个属性的数据值是一直到文章最后一个字节为止
    List<String> columnNames;//属性名称集合
    List<TypeInfo> columnTypes;//属性的类型集合

    boolean escaped;//是否需要转义
    byte escapeChar;//转义字符,该值是127以内的数字,或者字符串就获取第一个char作为转义字符
    boolean[] needsEscape;//需要被转义的字符集合   从0-127的byte中,需要转义的字符有哪些,返回true的位置,就是需要转义的字符位置

    public List<TypeInfo> getColumnTypes() {
      return columnTypes;
    }

    public List<String> getColumnNames() {
      return columnNames;
    }

    public byte[] getSeparators() {
      return separators;
    }

    public String getNullString() {
      return nullString;
    }

    public Text getNullSequence() {
      return nullSequence;
    }

    public TypeInfo getRowTypeInfo() {
      return rowTypeInfo;
    }

    public boolean isLastColumnTakesRest() {
      return lastColumnTakesRest;
    }

    public boolean isEscaped() {
      return escaped;
    }

    public byte getEscapeChar() {
      return escapeChar;
    }

    public boolean[] getNeedsEscape() {
      return needsEscape;
    }
  }

  //序列化和反序列化的参数明细信息
  SerDeParameters serdeParams = null;

  /**
   * Initialize the SerDe given the parameters. serialization.format: separator
   * char or byte code (only supports byte-value up to 127) columns:
   * ","-separated column names columns.types: ",", ":", or ";"-separated column
   * types
   *
   * @see SerDe#initialize(Configuration, Properties)
   */
  @Override
  public void initialize(Configuration job, Properties tbl)
      throws SerDeException {

	//初始化序列化和反序列化的参数明细信息
    serdeParams = LazySimpleSerDe.initSerdeParams(job, tbl, getClass()
        .getName());

    // Create the ObjectInspectors for the fields
    cachedObjectInspector = LazyFactory.createLazyStructInspector(serdeParams
        .getColumnNames(), serdeParams.getColumnTypes(), serdeParams
        .getSeparators(), serdeParams.getNullSequence(), serdeParams
        .isLastColumnTakesRest(), serdeParams.isEscaped(), serdeParams
        .getEscapeChar());

    cachedLazyStruct = (LazyStruct) LazyFactory
        .createLazyObject(cachedObjectInspector);

    LOG.debug(getClass().getName() + " initialized with: columnNames="
        + serdeParams.columnNames + " columnTypes=" + serdeParams.columnTypes
        + " separator=" + Arrays.asList(serdeParams.separators)
        + " nullstring=" + serdeParams.nullString + " lastColumnTakesRest="
        + serdeParams.lastColumnTakesRest);

    serializedSize = 0;
    stats = new SerDeStats();
    lastOperationSerialize = false;
    lastOperationDeserialize = false;

  }

  public static SerDeParameters initSerdeParams(Configuration job,
      Properties tbl, String serdeName) throws SerDeException {
    SerDeParameters serdeParams = new SerDeParameters();
    // Read the separators: We use 8 levels of separators by default,
    // and 24 if SERIALIZATION_EXTEND_NESTING_LEVELS is set to true
    // The levels possible are the set of control chars that we can use as
    // special delimiters, ie they should absent in the data or escaped.
    // To increase this level further, we need to stop relying
    // on single control chars delimiters
    //如何拆分field、集合、map数据
    serdeParams.separators = new byte[8];
    serdeParams.separators[0] = getByte(tbl.getProperty(serdeConstants.FIELD_DELIM,
        tbl.getProperty(serdeConstants.SERIALIZATION_FORMAT)), DefaultSeparators[0]);
    serdeParams.separators[1] = getByte(tbl
        .getProperty(serdeConstants.COLLECTION_DELIM), DefaultSeparators[1]);
    serdeParams.separators[2] = getByte(
        tbl.getProperty(serdeConstants.MAPKEY_DELIM), DefaultSeparators[2]);

    String extendedNesting =
        tbl.getProperty(SERIALIZATION_EXTEND_NESTING_LEVELS);//获取boolean值
    if(extendedNesting == null || !extendedNesting.equalsIgnoreCase("true")){
      //use the default smaller set of separators for backward compatibility 向后兼容
      for (int i = 3; i < serdeParams.separators.length; i++) {//设置默认扩展字符
        serdeParams.separators[i] = (byte) (i + 1);//第3个位置开始设置4,第4个位置设置5 等等.一直设置完separators内容
      }
    } else{//设置扩展字符
      //If extended nesting is enabled, set the extended set of separator chars

      final int MAX_CTRL_CHARS = 29;
      byte[] extendedSeparators = new byte[MAX_CTRL_CHARS];//扩展后的字节结果
      int extendedSeparatorsIdx = 0;

      //get the first 3 separators that have already been set (defaults to 1,2,3)
      for(int i = 0; i < 3; i++){//前三个字节不变化
        extendedSeparators[extendedSeparatorsIdx++] = serdeParams.separators[i];
      }

      for (byte asciival = 4; asciival <= MAX_CTRL_CHARS; asciival++) {//开始扩展

        //use only control chars that are very unlikely to be part of the string
        // the following might/likely to be used in text files for strings
        // 9 (horizontal tab, HT, \t, ^I)
        // 10 (line feed, LF, \n, ^J),
        // 12 (form feed, FF, \f, ^L),
        // 13 (carriage return, CR, \r, ^M),
        // 27 (escape, ESC, \e [GCC only], ^[).

        //reserving the following values for future dynamic level impl
        // 30
        // 31

        switch(asciival){//这5个是不需要被扩展的
        case 9:
        case 10:
        case 12:
        case 13:
        case 27:
          continue;
        }
        extendedSeparators[extendedSeparatorsIdx++] = asciival;
      }

      serdeParams.separators =
          Arrays.copyOfRange(extendedSeparators, 0, extendedSeparatorsIdx);//扩展separators从8个内容转换成29-5个内容
    }

    //设置空值
    serdeParams.nullString = tbl.getProperty(
        serdeConstants.SERIALIZATION_NULL_FORMAT, "\\N");
    serdeParams.nullSequence = new Text(serdeParams.nullString);

    //设置是否允许最后一个属性的数据值是一直到文章最后一个字节为止
    String lastColumnTakesRestString = tbl
        .getProperty(serdeConstants.SERIALIZATION_LAST_COLUMN_TAKES_REST);
    serdeParams.lastColumnTakesRest = (lastColumnTakesRestString != null && lastColumnTakesRestString
        .equalsIgnoreCase("true"));

    //从Properties属性中解析一个表的所有属性name集合和属性type类型集合,并且将结果设置到SerDeParameters对象中
    LazyUtils.extractColumnInfo(tbl, serdeParams, serdeName);

    // Create the LazyObject for storing the rows
    serdeParams.rowTypeInfo = TypeInfoFactory.getStructTypeInfo(
        serdeParams.columnNames, serdeParams.columnTypes);

    // Get the escape information
    //转义字符的key,value是127以内的数字,或者字符串就获取第一个char作为转义字符,如果该key对应的value不是空,则说明需要转义
    String escapeProperty = tbl.getProperty(serdeConstants.ESCAPE_CHAR);
    serdeParams.escaped = (escapeProperty != null);
    if (serdeParams.escaped) {
      serdeParams.escapeChar = getByte(escapeProperty, (byte) '\\');
    }
    
    //从0-127的byte中,需要转义的字符有哪些,返回true的位置,就是需要转义的字符位置
    if (serdeParams.escaped) {
      serdeParams.needsEscape = new boolean[128];
      for (int i = 0; i < 128; i++) {
        serdeParams.needsEscape[i] = false;
      }
      serdeParams.needsEscape[serdeParams.escapeChar] = true;
      for (int i = 0; i < serdeParams.separators.length; i++) {
        serdeParams.needsEscape[serdeParams.separators[i]] = true;
      }
    }

    return serdeParams;
  }

  // The object for storing row data
  //一行数据内容的字节数组
  LazyStruct cachedLazyStruct;

  // The wrapper for byte array
  //包装一行数据内容的字节数组
  ByteArrayRef byteArrayRef;

  /**
   * Deserialize a row from the Writable to a LazyObject.
   *
   * @param field
   *          the Writable that contains the data
   * @return The deserialized row Object.
   * @see SerDe#deserialize(Writable)
   * 反序列化
   */
  @Override
  public Object deserialize(Writable field) throws SerDeException {
    if (byteArrayRef == null) {
      byteArrayRef = new ByteArrayRef();
    }
    if (field instanceof BytesWritable) {
      BytesWritable b = (BytesWritable) field;
      // For backward-compatibility with hadoop 0.17
      byteArrayRef.setData(b.getBytes());
      cachedLazyStruct.init(byteArrayRef, 0, b.getLength());
    } else if (field instanceof Text) {
      Text t = (Text) field;
      byteArrayRef.setData(t.getBytes());
      cachedLazyStruct.init(byteArrayRef, 0, t.getLength());
    } else {
      throw new SerDeException(getClass().toString()
          + ": expects either BytesWritable or Text object!");
    }
    lastOperationSerialize = false;
    lastOperationDeserialize = true;
    return cachedLazyStruct;
  }

  /**
   * Returns the ObjectInspector for the row.
   */
  @Override
  public ObjectInspector getObjectInspector() throws SerDeException {
    return cachedObjectInspector;
  }

  /**
   * Returns the Writable Class after serialization.
   *
   * @see SerDe#getSerializedClass()
   */
  @Override
  public Class<? extends Writable> getSerializedClass() {
    return Text.class;
  }

  Text serializeCache = new Text();//存储一行的数据内容
  ByteStream.Output serializeStream = new ByteStream.Output();//临时存储一行数据的字节数组

  /**
   * Serialize a row of data.
   * 如何序列化一行数据
   * @param obj
   *          The row object 一行数据的具体内容
   * @param objInspector
   *          The ObjectInspector for the row object  一行数据的数据格式
   * @return The serialized Writable object
   * @throws IOException
   * @see SerDe#serialize(Object, ObjectInspector)
   * 将一行数据序列化成Text
   */
  @Override
  public Writable serialize(Object obj, ObjectInspector objInspector)
      throws SerDeException {

    if (objInspector.getCategory() != Category.STRUCT) {//数据格式一定是STRUCT形式
      throw new SerDeException(getClass().toString()
          + " can only serialize struct types, but we got: "
          + objInspector.getTypeName());
    }

    // Prepare the field ObjectInspectors
    StructObjectInspector soi = (StructObjectInspector) objInspector;
    List<? extends StructField> fields = soi.getAllStructFieldRefs();//所有属性
    List<Object> list = soi.getStructFieldsDataAsList(obj);//获取该属性对应的每一个值
    List<? extends StructField> declaredFields = (serdeParams.rowTypeInfo != null &&
            ((StructTypeInfo) serdeParams.rowTypeInfo).getAllStructFieldNames().size() > 0) ? ((StructObjectInspector) getObjectInspector()).getAllStructFieldRefs(): null;


    serializeStream.reset();
    serializedSize = 0;

    // Serialize each field 将每一个属性对应的值进行序列化到输出流中
    for (int i = 0; i < fields.size(); i++) {//循环每一个属性
      // Append the separator if needed.
      if (i > 0) {
        serializeStream.write(serdeParams.separators[0]);//插入每一个属性之间的分隔符
      }
      // Get the field objectInspector and the field object.
      ObjectInspector foi = fields.get(i).getFieldObjectInspector();//获取属性类型
      Object f = (list == null ? null : list.get(i));//获取该属性对应的具体值

      if (declaredFields != null && i >= declaredFields.size()) {
        throw new SerDeException("Error: expecting " + declaredFields.size()
            + " but asking for field " + i + "\n" + "data=" + obj + "\n"
            + "tableType=" + serdeParams.rowTypeInfo.toString() + "\n"
            + "dataType="
            + TypeInfoUtils.getTypeInfoFromObjectInspector(objInspector));
      }

      serializeField(serializeStream, f, foi, serdeParams);
    }

    // TODO: The copy of data is unnecessary, but there is no work-around
    // since we cannot directly set the private byte[] field inside Text.因为我们不能直接使用byte数组代替在text中
    serializeCache
        .set(serializeStream.getData(), 0, serializeStream.getCount());//将该行的输出内容写入到Text中
    serializedSize = serializeStream.getCount();
    lastOperationSerialize = true;
    lastOperationDeserialize = false;
    return serializeCache;
  }

    /**
     * 真正的序列化操作
     * @param out 输出流,将序列化的结果写入该流中
     * @param obj 要序列化的属性的具体的值
     * @param objInspector 要序列化的属性类型
     * @param serdeParams 序列化参数对象
     * @throws SerDeException
     */
  protected void serializeField(ByteStream.Output out, Object obj, ObjectInspector objInspector,
      SerDeParameters serdeParams) throws SerDeException {
    try {
      serialize(out, obj, objInspector, serdeParams.separators, 1, serdeParams.nullSequence,
          serdeParams.escaped, serdeParams.escapeChar, serdeParams.needsEscape);
    } catch (IOException e) {
      throw new SerDeException(e);
    }
  }

  /**
   * Serialize the row into the StringBuilder.
   * 序列化一行数据
   * @param out
   *          The StringBuilder to store the serialized data.输出流
   * @param obj
   *          The object for the current field.该属性当前具体的值
   * @param objInspector
   *          The ObjectInspector for the current Object.该属性具体的类型
   * @param separators
   *          The separators array.配置参数对象
   * @param level
   *          The current level of separator.分隔符的集合,针对list里面的元素依然是list时候处理
   * @param nullSequence
   *          The byte sequence representing the NULL value.如果是null的时候如何输出
   * @param escaped
   *          Whether we need to escape the data when writing out 是否转义字符转义
   * @param escapeChar
   *          Which char to use as the escape char, e.g. '\\'  转义字符
   * @param needsEscape
   *          Which chars needs to be escaped. This array should have size of
   *          128. Negative byte values (or byte values >= 128) are never
   *          escaped. 需要转义的字符
   * @throws IOException
   * @throws SerDeException
   */
  public static void serialize(ByteStream.Output out, Object obj,
      ObjectInspector objInspector, byte[] separators, int level,
      Text nullSequence, boolean escaped, byte escapeChar, boolean[] needsEscape)
      throws IOException, SerDeException {

    if (obj == null) {//如果是null,则直接写入null对应的具体内容
      out.write(nullSequence.getBytes(), 0, nullSequence.getLength());
      return;
    }

    char separator;
    List<?> list;
    switch (objInspector.getCategory()) {//判断属性类型
    case PRIMITIVE:
      LazyUtils.writePrimitiveUTF8(out, obj,
          (PrimitiveObjectInspector) objInspector, escaped, escapeChar,
          needsEscape);//将值的内容转换成字节数组,存储到out中
      return;
    case LIST:
      separator = (char) LazyUtils.getSeparator(separators, level);//比如user集合,每一个user还有生活在若干个省,每一个省对应若干个市.因此如果用一个分隔符分割list是有问题,没办法区别其他节点,因此每一个list就用一个分隔符,因此取决于level
      ListObjectInspector loi = (ListObjectInspector) objInspector;//数据类型是list
      list = loi.getList(obj);//具体的值
      ObjectInspector eoi = loi.getListElementObjectInspector();//list元素的类型
      if (list == null) {
        out.write(nullSequence.getBytes(), 0, nullSequence.getLength());//设置空字节内容
      } else {
        for (int i = 0; i < list.size(); i++) {//循环写入每一个list元素
          if (i > 0) {
            out.write(separator);
          }
          serialize(out, list.get(i), eoi, separators, level + 1, nullSequence,
              escaped, escapeChar, needsEscape);//递归写入数据
        }
      }
      return;
    case MAP:
      separator = (char) LazyUtils.getSeparator(separators, level);//map中元素的分隔符
      char keyValueSeparator =
           (char) LazyUtils.getSeparator(separators, level + 1);//key和value的分隔符

      MapObjectInspector moi = (MapObjectInspector) objInspector;//表示Map类型
      ObjectInspector koi = moi.getMapKeyObjectInspector();//获取map中key和value类型
      ObjectInspector voi = moi.getMapValueObjectInspector();
      Map<?, ?> map = moi.getMap(obj);//获取map的具体值
      if (map == null) {
        out.write(nullSequence.getBytes(), 0, nullSequence.getLength());
      } else {
        boolean first = true;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
          if (first) {
            first = false;
          } else {
            out.write(separator);//第二个元素开始要加入分隔符
          }
          serialize(out, entry.getKey(), koi, separators, level + 2,
              nullSequence, escaped, escapeChar, needsEscape);//序列化key的字节内容
          out.write(keyValueSeparator);//序列化key和value之间的分隔符
          serialize(out, entry.getValue(), voi, separators, level + 2,
              nullSequence, escaped, escapeChar, needsEscape);//序列化value的字节内容
        }
      }
      return;
    case STRUCT:
      separator = (char) LazyUtils.getSeparator(separators, level);//每一个属性值的分隔符
      StructObjectInspector soi = (StructObjectInspector) objInspector;
      List<? extends StructField> fields = soi.getAllStructFieldRefs();//所有的属性集合
      list = soi.getStructFieldsDataAsList(obj);//所有的属性值
      if (list == null) {
        out.write(nullSequence.getBytes(), 0, nullSequence.getLength());
      } else {
        for (int i = 0; i < list.size(); i++) {
          if (i > 0) {
            out.write(separator);
          }
          serialize(out, list.get(i), fields.get(i).getFieldObjectInspector(),
              separators, level + 1, nullSequence, escaped, escapeChar,
              needsEscape);//序列化每一个属性值
        }
      }
      return;
    case UNION://此时表示的是union类型中的一个元素,即obj是union类型的一个元素
      separator = (char) LazyUtils.getSeparator(separators, level);//每一个类型的值的分隔符
      UnionObjectInspector uoi = (UnionObjectInspector) objInspector;
      List<? extends ObjectInspector> ois = uoi.getObjectInspectors();//包含哪些类型
      if (ois == null) {
        out.write(nullSequence.getBytes(), 0, nullSequence.getLength());
      } else {
        LazyUtils.writePrimitiveUTF8(out, new Byte(uoi.getTag(obj)),
            PrimitiveObjectInspectorFactory.javaByteObjectInspector,
            escaped, escapeChar, needsEscape);//存储该union元素对应的编号
        out.write(separator);
        serialize(out, uoi.getField(obj), ois.get(uoi.getTag(obj)),//分别表示输出流、第obj元素所在的具体的值,第obj元素所在的类型
            separators, level + 1, nullSequence, escaped, escapeChar,
            needsEscape);//存储该union对应的具体的值
      }
      return;
    default:
      break;
    }

    throw new RuntimeException("Unknown category type: "
        + objInspector.getCategory());
  }

  /**
   * Returns the statistics after (de)serialization)
   */

  @Override
  public SerDeStats getSerDeStats() {
    // must be different
    assert (lastOperationSerialize != lastOperationDeserialize);

    if (lastOperationSerialize) {
      stats.setRawDataSize(serializedSize);
    } else {
      stats.setRawDataSize(cachedLazyStruct.getRawDataSerializedSize());
    }
    return stats;

  }
}
