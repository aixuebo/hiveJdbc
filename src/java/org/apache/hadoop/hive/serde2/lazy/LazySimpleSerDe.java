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
 * ���������صķ�ʽ,��һ�������������л����ֽ�����,���߽��ֽ����鷴���л���һ�����ݡ�����Ϊ���紫��Ķ����ֽ�����
 *
 * Also LazySimpleSerDe outputs typed columns instead of treating all columns as
 * String like MetadataTypedColumnsetSerDe.
 */
public class LazySimpleSerDe extends AbstractSerDe {

  public static final Log LOG = LogFactory.getLog(LazySimpleSerDe.class
      .getName());

  public static final String SERIALIZATION_EXTEND_NESTING_LEVELS
    = "hive.serialization.extend.nesting.levels";//ֵ��true����false

  public static final byte[] DefaultSeparators = {(byte) 1, (byte) 2, (byte) 3};

  private ObjectInspector cachedObjectInspector;

  private long serializedSize;//���л�һ�е��ֽڴ�С
  private SerDeStats stats;
  private boolean lastOperationSerialize;//true��ʾ�����������л�����
  private boolean lastOperationDeserialize;//true��ʾ�������Ƿ����л�����

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
   *          The string containing a number.�������ֵĲ���
   * @param defaultVal
   *          If the altValue does not represent a number, return the
   *          defaultVal.
   * ���ַ�������������������,127���ڵ�����,��������������쳣,�򷵻ص�һ���ֽڼ���
   * ���������,��byte���Ǹ����ֶ�Ӧ���ֽ�,����altValue=89,����ֵ����89,(char)89,��������Y  
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
   * ���л��ͷ����л��Ĳ�����ϸ��Ϣ
   */
  public static class SerDeParameters {
    byte[] separators = DefaultSeparators;//����1,2,3,������չ�ַ�,Ŀ����Ϊ�˲�ֵ�����,��1λ���field���ֽ�,Ĭ����1,��2λ��ּ��ϵĲ������key,Ĭ��ֵ��002,����λ���map���ֽ�,Ĭ����3
    String nullString;//Ĭ����\\N  ��ʾ�����null��ʱ��,���д������
    Text nullSequence;//��hadoop���л���Ŀ�ֵ,��new Text(nullString)
    TypeInfo rowTypeInfo;//��¼���е�������Ҫ�����Լ��Ϻ��������ͼ���,����columnNames��columnTypes������
    boolean lastColumnTakesRest;//�Ƿ��������һ�����Ե�����ֵ��һֱ���������һ���ֽ�Ϊֹ
    List<String> columnNames;//�������Ƽ���
    List<TypeInfo> columnTypes;//���Ե����ͼ���

    boolean escaped;//�Ƿ���Ҫת��
    byte escapeChar;//ת���ַ�,��ֵ��127���ڵ�����,�����ַ����ͻ�ȡ��һ��char��Ϊת���ַ�
    boolean[] needsEscape;//��Ҫ��ת����ַ�����   ��0-127��byte��,��Ҫת����ַ�����Щ,����true��λ��,������Ҫת����ַ�λ��

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

  //���л��ͷ����л��Ĳ�����ϸ��Ϣ
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

	//��ʼ�����л��ͷ����л��Ĳ�����ϸ��Ϣ
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
    //��β��field�����ϡ�map����
    serdeParams.separators = new byte[8];
    serdeParams.separators[0] = getByte(tbl.getProperty(serdeConstants.FIELD_DELIM,
        tbl.getProperty(serdeConstants.SERIALIZATION_FORMAT)), DefaultSeparators[0]);
    serdeParams.separators[1] = getByte(tbl
        .getProperty(serdeConstants.COLLECTION_DELIM), DefaultSeparators[1]);
    serdeParams.separators[2] = getByte(
        tbl.getProperty(serdeConstants.MAPKEY_DELIM), DefaultSeparators[2]);

    String extendedNesting =
        tbl.getProperty(SERIALIZATION_EXTEND_NESTING_LEVELS);//��ȡbooleanֵ
    if(extendedNesting == null || !extendedNesting.equalsIgnoreCase("true")){
      //use the default smaller set of separators for backward compatibility ������
      for (int i = 3; i < serdeParams.separators.length; i++) {//����Ĭ����չ�ַ�
        serdeParams.separators[i] = (byte) (i + 1);//��3��λ�ÿ�ʼ����4,��4��λ������5 �ȵ�.һֱ������separators����
      }
    } else{//������չ�ַ�
      //If extended nesting is enabled, set the extended set of separator chars

      final int MAX_CTRL_CHARS = 29;
      byte[] extendedSeparators = new byte[MAX_CTRL_CHARS];//��չ����ֽڽ��
      int extendedSeparatorsIdx = 0;

      //get the first 3 separators that have already been set (defaults to 1,2,3)
      for(int i = 0; i < 3; i++){//ǰ�����ֽڲ��仯
        extendedSeparators[extendedSeparatorsIdx++] = serdeParams.separators[i];
      }

      for (byte asciival = 4; asciival <= MAX_CTRL_CHARS; asciival++) {//��ʼ��չ

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

        switch(asciival){//��5���ǲ���Ҫ����չ��
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
          Arrays.copyOfRange(extendedSeparators, 0, extendedSeparatorsIdx);//��չseparators��8������ת����29-5������
    }

    //���ÿ�ֵ
    serdeParams.nullString = tbl.getProperty(
        serdeConstants.SERIALIZATION_NULL_FORMAT, "\\N");
    serdeParams.nullSequence = new Text(serdeParams.nullString);

    //�����Ƿ��������һ�����Ե�����ֵ��һֱ���������һ���ֽ�Ϊֹ
    String lastColumnTakesRestString = tbl
        .getProperty(serdeConstants.SERIALIZATION_LAST_COLUMN_TAKES_REST);
    serdeParams.lastColumnTakesRest = (lastColumnTakesRestString != null && lastColumnTakesRestString
        .equalsIgnoreCase("true"));

    //��Properties�����н���һ�������������name���Ϻ�����type���ͼ���,���ҽ�������õ�SerDeParameters������
    LazyUtils.extractColumnInfo(tbl, serdeParams, serdeName);

    // Create the LazyObject for storing the rows
    serdeParams.rowTypeInfo = TypeInfoFactory.getStructTypeInfo(
        serdeParams.columnNames, serdeParams.columnTypes);

    // Get the escape information
    //ת���ַ���key,value��127���ڵ�����,�����ַ����ͻ�ȡ��һ��char��Ϊת���ַ�,�����key��Ӧ��value���ǿ�,��˵����Ҫת��
    String escapeProperty = tbl.getProperty(serdeConstants.ESCAPE_CHAR);
    serdeParams.escaped = (escapeProperty != null);
    if (serdeParams.escaped) {
      serdeParams.escapeChar = getByte(escapeProperty, (byte) '\\');
    }
    
    //��0-127��byte��,��Ҫת����ַ�����Щ,����true��λ��,������Ҫת����ַ�λ��
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
  //һ���������ݵ��ֽ�����
  LazyStruct cachedLazyStruct;

  // The wrapper for byte array
  //��װһ���������ݵ��ֽ�����
  ByteArrayRef byteArrayRef;

  /**
   * Deserialize a row from the Writable to a LazyObject.
   *
   * @param field
   *          the Writable that contains the data
   * @return The deserialized row Object.
   * @see SerDe#deserialize(Writable)
   * �����л�
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

  Text serializeCache = new Text();//�洢һ�е���������
  ByteStream.Output serializeStream = new ByteStream.Output();//��ʱ�洢һ�����ݵ��ֽ�����

  /**
   * Serialize a row of data.
   * ������л�һ������
   * @param obj
   *          The row object һ�����ݵľ�������
   * @param objInspector
   *          The ObjectInspector for the row object  һ�����ݵ����ݸ�ʽ
   * @return The serialized Writable object
   * @throws IOException
   * @see SerDe#serialize(Object, ObjectInspector)
   * ��һ���������л���Text
   */
  @Override
  public Writable serialize(Object obj, ObjectInspector objInspector)
      throws SerDeException {

    if (objInspector.getCategory() != Category.STRUCT) {//���ݸ�ʽһ����STRUCT��ʽ
      throw new SerDeException(getClass().toString()
          + " can only serialize struct types, but we got: "
          + objInspector.getTypeName());
    }

    // Prepare the field ObjectInspectors
    StructObjectInspector soi = (StructObjectInspector) objInspector;
    List<? extends StructField> fields = soi.getAllStructFieldRefs();//��������
    List<Object> list = soi.getStructFieldsDataAsList(obj);//��ȡ�����Զ�Ӧ��ÿһ��ֵ
    List<? extends StructField> declaredFields = (serdeParams.rowTypeInfo != null &&
            ((StructTypeInfo) serdeParams.rowTypeInfo).getAllStructFieldNames().size() > 0) ? ((StructObjectInspector) getObjectInspector()).getAllStructFieldRefs(): null;


    serializeStream.reset();
    serializedSize = 0;

    // Serialize each field ��ÿһ�����Զ�Ӧ��ֵ�������л����������
    for (int i = 0; i < fields.size(); i++) {//ѭ��ÿһ������
      // Append the separator if needed.
      if (i > 0) {
        serializeStream.write(serdeParams.separators[0]);//����ÿһ������֮��ķָ���
      }
      // Get the field objectInspector and the field object.
      ObjectInspector foi = fields.get(i).getFieldObjectInspector();//��ȡ��������
      Object f = (list == null ? null : list.get(i));//��ȡ�����Զ�Ӧ�ľ���ֵ

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
    // since we cannot directly set the private byte[] field inside Text.��Ϊ���ǲ���ֱ��ʹ��byte���������text��
    serializeCache
        .set(serializeStream.getData(), 0, serializeStream.getCount());//�����е��������д�뵽Text��
    serializedSize = serializeStream.getCount();
    lastOperationSerialize = true;
    lastOperationDeserialize = false;
    return serializeCache;
  }

    /**
     * ���������л�����
     * @param out �����,�����л��Ľ��д�������
     * @param obj Ҫ���л������Եľ����ֵ
     * @param objInspector Ҫ���л�����������
     * @param serdeParams ���л���������
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
   * ���л�һ������
   * @param out
   *          The StringBuilder to store the serialized data.�����
   * @param obj
   *          The object for the current field.�����Ե�ǰ�����ֵ
   * @param objInspector
   *          The ObjectInspector for the current Object.�����Ծ��������
   * @param separators
   *          The separators array.���ò�������
   * @param level
   *          The current level of separator.�ָ����ļ���,���list�����Ԫ����Ȼ��listʱ����
   * @param nullSequence
   *          The byte sequence representing the NULL value.�����null��ʱ��������
   * @param escaped
   *          Whether we need to escape the data when writing out �Ƿ�ת���ַ�ת��
   * @param escapeChar
   *          Which char to use as the escape char, e.g. '\\'  ת���ַ�
   * @param needsEscape
   *          Which chars needs to be escaped. This array should have size of
   *          128. Negative byte values (or byte values >= 128) are never
   *          escaped. ��Ҫת����ַ�
   * @throws IOException
   * @throws SerDeException
   */
  public static void serialize(ByteStream.Output out, Object obj,
      ObjectInspector objInspector, byte[] separators, int level,
      Text nullSequence, boolean escaped, byte escapeChar, boolean[] needsEscape)
      throws IOException, SerDeException {

    if (obj == null) {//�����null,��ֱ��д��null��Ӧ�ľ�������
      out.write(nullSequence.getBytes(), 0, nullSequence.getLength());
      return;
    }

    char separator;
    List<?> list;
    switch (objInspector.getCategory()) {//�ж���������
    case PRIMITIVE:
      LazyUtils.writePrimitiveUTF8(out, obj,
          (PrimitiveObjectInspector) objInspector, escaped, escapeChar,
          needsEscape);//��ֵ������ת�����ֽ�����,�洢��out��
      return;
    case LIST:
      separator = (char) LazyUtils.getSeparator(separators, level);//����user����,ÿһ��user�������������ɸ�ʡ,ÿһ��ʡ��Ӧ���ɸ���.��������һ���ָ����ָ�list��������,û�취���������ڵ�,���ÿһ��list����һ���ָ���,���ȡ����level
      ListObjectInspector loi = (ListObjectInspector) objInspector;//����������list
      list = loi.getList(obj);//�����ֵ
      ObjectInspector eoi = loi.getListElementObjectInspector();//listԪ�ص�����
      if (list == null) {
        out.write(nullSequence.getBytes(), 0, nullSequence.getLength());//���ÿ��ֽ�����
      } else {
        for (int i = 0; i < list.size(); i++) {//ѭ��д��ÿһ��listԪ��
          if (i > 0) {
            out.write(separator);
          }
          serialize(out, list.get(i), eoi, separators, level + 1, nullSequence,
              escaped, escapeChar, needsEscape);//�ݹ�д������
        }
      }
      return;
    case MAP:
      separator = (char) LazyUtils.getSeparator(separators, level);//map��Ԫ�صķָ���
      char keyValueSeparator =
           (char) LazyUtils.getSeparator(separators, level + 1);//key��value�ķָ���

      MapObjectInspector moi = (MapObjectInspector) objInspector;//��ʾMap����
      ObjectInspector koi = moi.getMapKeyObjectInspector();//��ȡmap��key��value����
      ObjectInspector voi = moi.getMapValueObjectInspector();
      Map<?, ?> map = moi.getMap(obj);//��ȡmap�ľ���ֵ
      if (map == null) {
        out.write(nullSequence.getBytes(), 0, nullSequence.getLength());
      } else {
        boolean first = true;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
          if (first) {
            first = false;
          } else {
            out.write(separator);//�ڶ���Ԫ�ؿ�ʼҪ����ָ���
          }
          serialize(out, entry.getKey(), koi, separators, level + 2,
              nullSequence, escaped, escapeChar, needsEscape);//���л�key���ֽ�����
          out.write(keyValueSeparator);//���л�key��value֮��ķָ���
          serialize(out, entry.getValue(), voi, separators, level + 2,
              nullSequence, escaped, escapeChar, needsEscape);//���л�value���ֽ�����
        }
      }
      return;
    case STRUCT:
      separator = (char) LazyUtils.getSeparator(separators, level);//ÿһ������ֵ�ķָ���
      StructObjectInspector soi = (StructObjectInspector) objInspector;
      List<? extends StructField> fields = soi.getAllStructFieldRefs();//���е����Լ���
      list = soi.getStructFieldsDataAsList(obj);//���е�����ֵ
      if (list == null) {
        out.write(nullSequence.getBytes(), 0, nullSequence.getLength());
      } else {
        for (int i = 0; i < list.size(); i++) {
          if (i > 0) {
            out.write(separator);
          }
          serialize(out, list.get(i), fields.get(i).getFieldObjectInspector(),
              separators, level + 1, nullSequence, escaped, escapeChar,
              needsEscape);//���л�ÿһ������ֵ
        }
      }
      return;
    case UNION://��ʱ��ʾ����union�����е�һ��Ԫ��,��obj��union���͵�һ��Ԫ��
      separator = (char) LazyUtils.getSeparator(separators, level);//ÿһ�����͵�ֵ�ķָ���
      UnionObjectInspector uoi = (UnionObjectInspector) objInspector;
      List<? extends ObjectInspector> ois = uoi.getObjectInspectors();//������Щ����
      if (ois == null) {
        out.write(nullSequence.getBytes(), 0, nullSequence.getLength());
      } else {
        LazyUtils.writePrimitiveUTF8(out, new Byte(uoi.getTag(obj)),
            PrimitiveObjectInspectorFactory.javaByteObjectInspector,
            escaped, escapeChar, needsEscape);//�洢��unionԪ�ض�Ӧ�ı��
        out.write(separator);
        serialize(out, uoi.getField(obj), ois.get(uoi.getTag(obj)),//�ֱ��ʾ���������objԪ�����ڵľ����ֵ,��objԪ�����ڵ�����
            separators, level + 1, nullSequence, escaped, escapeChar,
            needsEscape);//�洢��union��Ӧ�ľ����ֵ
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
