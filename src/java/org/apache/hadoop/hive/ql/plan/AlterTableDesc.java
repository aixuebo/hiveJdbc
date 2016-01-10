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

package org.apache.hadoop.hive.ql.plan;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hive.metastore.api.FieldSchema;
import org.apache.hadoop.hive.metastore.api.Order;
import org.apache.hadoop.hive.ql.exec.Utilities;
import org.apache.hadoop.hive.ql.metadata.Table;
import org.apache.hadoop.hive.ql.parse.ParseUtils;
import org.apache.hadoop.hive.ql.parse.SemanticException;

/**
 * AlterTableDesc.
 * 描述更改表操作的信息
 * 
 * 一、
 * 修改表的属性 String ADD|REPLACE COLUMNS (columnNameTypeList)
 * 为某个table的某个partitions分配HDFS上路径 ,SET LOCATION xxxx
 * 
 * 二、
 * 设置存储的方式是csv、json、还是protobuffer等等吧
格式 SET SERDE "serde_class_name" [WITH SERDEPROPERTIES(key=value,key=value)]
格式 SET SERDEPROPERTIES (key=value,key=value)

三、
设置/取消视图的属性
格式:
a.String SET TBLPROPERTIES (keyValueProperty,keyValueProperty)
b.String UNSET TBLPROPERTIES [IF Exists](keyValueProperty,keyValueProperty)

四、
analyzeAlterTableProtectMode设置protect模式以及类型,但是该类没有sql被启动,因此暂时不会对系统有影响

五、
alterStatementSuffixClusterbySortby格式:
1.NOT CLUSTERED 表示没有CLUSTERED BY 操作
2.NOT SORTED 表示没有SORTED BY 操作
3.CLUSTERED BY (column1,column2) [SORTED BY (column1 desc,column2 desc)] into Number BUCKETS,属于ADDCLUSTERSORTCOLUMN类型

六、
alterTblPartitionStatementSuffixSkewedLocation
a.SET SKEWED LOCATION (key=value,key=value) 是ALTERSKEWEDLOCATION类型
b.SET SKEWED LOCATION ((key1,key2)=value,(key1,key2)=value) 是ALTERSKEWEDLOCATION类型

七、
tableSkewed 属于ADDSKEWEDBY类型
SKEWED BY (属性字符串,属性字符串) on (属性值集合xxx,xxx) [STORED AS DIRECTORIES]
或者SKEWED BY (属性字符串,属性字符串) on (多组属性值集合 (xxx,xxx),(xxx,xxx),(xxx,xxx) ) [STORED AS DIRECTORIES]
create table T (c1 string, c2 string) skewed by (c1) on ('x1') 表示在c1属性的值为x1的时候可能会数据发生偏移,因此在join的时候要先预估一下是否一个表的c1=x1的值能否很少,并且存储在内存中,如果是,则可以进行优化
create table T (c1 string, c2 string) skewed by (c1,c2) on (('x11','x21'),('x12','x22')) 表示在c1,c2属性的值为(x11,x21),或者(x21,x22)的时候可能会数据发生偏移,因此在join的时候要先预估一下是否一个表的(x11,x21),或者(x21,x22)的值能否很少,并且存储在内存中,如果是,则可以进行优化

八、
alterStatementSuffixSkewedby 属于ADDSKEWEDBY类型
"tableName" NOT SKEWED

九、
 alterStatementSuffixBucketNum
 INTO number BUCKETS
 
十、
"oldName" RENAME TO "newName"

十一
 alterStatementSuffixRenameCol 更改表的属性名字,此时属于RENAMECOLUMN类型
 格式:String CHANGE [COLUMN] "oldName" "newName" type [COMMENT String] [FIRST|AFTER String]
   注意:
   1.type表示字段类型
   2.FIRST或者AFTER String
   TOK_ALTERTABLE_RENAMECOL identifier $oldName $newName colType $comment? alterStatementChangeColPosition?

 */
@Explain(displayName = "Alter Table")
public class AlterTableDesc extends DDLDesc implements Serializable {
  private static final long serialVersionUID = 1L;

  /**
   * alterTableTypes.
   * 更改的类型
   */
  public static enum AlterTableTypes {
    RENAME, 
    ADDCOLS,//修改表的属性 String ADD (columnNameTypeList) 
    REPLACECOLS,//修改表的属性 String REPLACE COLUMNS (columnNameTypeList)
    ADDPROPS, DROPPROPS, ADDSERDE, 
    ADDSERDEPROPS,//设置存储的方式是csv、json、还是protobuffer等等吧,格式 SET SERDEPROPERTIES (key=value,key=value)
    ADDFILEFORMAT, 
    ADDCLUSTERSORTCOLUMN, //alterStatementSuffixClusterbySortby
    RENAMECOLUMN,//alterStatementSuffixRenameCol 
    ADDPARTITION,
    TOUCH,//重新写回关于table的partition下的元数据信息
    ARCHIVE,//Hive中的归档移动分区中的文件到Hadoop归档中（HAR），该语句只会减少文件的数量，但不提供压缩。 
    UNARCHIVE,//Hive中的归档移动分区中的文件到Hadoop归档中（HAR），该语句只会减少文件的数量，但不提供压缩。 
    ALTERPROTECTMODE, ALTERPARTITIONPROTECTMODE,
    ALTERLOCATION,//为table下的某个partition分配一个新的HDFS路径
    DROPPARTITION,//删除数据库表的某些分区
    RENAMEPARTITION,//RENAME TO PARTITION (name=value,name=value,name) 
    ADDSKEWEDBY, //tableSkewed
    ALTERSKEWEDLOCATION,//alterTblPartitionStatementSuffixSkewedLocation
    ALTERBUCKETNUM, ALTERPARTITION
  }

  public static enum ProtectModeType {
    NO_DROP, OFFLINE, READ_ONLY, NO_DROP_CASCADE
  }


  AlterTableTypes op;//更改的类型
  String oldName;//更改表前名字
  String newName;//更改表后的名字 "oldName" RENAME TO "newName"
  ArrayList<FieldSchema> newCols;//修改表的属性 String ADD|REPLACE COLUMNS (columnNameTypeList)中用于解释新的属性集合
  
  /**
   * 格式 SET SERDE "serde_class_name" [WITH SERDEPROPERTIES(key=value,key=value)]中的key=value,key=value
   * 格式 SET SERDEPROPERTIES (key=value,key=value) 中的key=value,key=value
   * 格式 String SET TBLPROPERTIES (keyValueProperty,keyValueProperty)
             格式 String UNSET TBLPROPERTIES [IF Exists](keyValueProperty,keyValueProperty)
   */
  HashMap<String, String> props;
  
  String serdeName;//格式 SET SERDE "serde_class_name" [WITH SERDEPROPERTIES(key=value,key=value)]中的serde_class_name
  String inputFormat;//文件写入的形式
  String outputFormat;//文件读出的形式
  String storageHandler;
  
  //CLUSTERED BY (column1,column2) [SORTED BY (column1 desc,column2 desc)] into Number BUCKETS
  //NOT CLUSTERED表示没有CLUSTERED BY 操作
  int numberBuckets;//into Number BUCKETS
  ArrayList<String> bucketColumns;//CLUSTERED BY (column1,column2)
  ArrayList<Order> sortColumns;//SORTED BY (column1 desc,column2 desc)
  boolean isTurnOffSorting = false;//alterStatementSuffixClusterbySortby格式: 解析NOT SORTED 表示没有SORTED BY 操作
  
  String oldColName;
  String newColName;
  String newColType;//String CHANGE [COLUMN] "oldName" "newName" type [COMMENT String] [FIRST|AFTER String] 中用于修改type,即属性类型
  String newColComment;//String CHANGE [COLUMN] "oldName" "newName" type [COMMENT String] [FIRST|AFTER String] 中用于修改备注
  boolean first;//String CHANGE [COLUMN] "oldName" "newName" type [COMMENT String] [FIRST|AFTER String] 用于设置了FIRST
  String afterCol;//String CHANGE [COLUMN] "oldName" "newName" type [COMMENT String] [FIRST|AFTER String] 用于设置了AFTER String
  
  
  boolean expectView;//true 表示操作的表示视图
  HashMap<String, String> partSpec;//确定一个partition,HashMap<String, String> partSpec 是因为确定某个partition可能来源于多个属性,例如log_date log_hour
  private String newLocation;//更改HDFS的路径 
  boolean protectModeEnable;//参见analyzeAlterTableProtectMode中设置prodect模式是否启动/关闭
  ProtectModeType protectModeType;//analyzeAlterTableProtectMode中设置prodect模式
  Map<List<String>, String> skewedLocations;//alterTblPartitionStatementSuffixSkewedLocation中设置解析后的内容
  boolean isTurnOffSkewed = false;//解析SKEWED BY (属性字符串,属性字符串)时候默认是false,但是解析alterStatementSuffixSkewedby String NOT SKEWED时候为true
  boolean isStoredAsSubDirectories = false;//解析SKEWED BY (属性字符串,属性字符串)中的 [STORED AS DIRECTORIES]
  List<String> skewedColNames;//解析SKEWED BY (属性字符串,属性字符串) on (多组属性值集合 (xxx,xxx),(xxx,xxx),(xxx,xxx) ) 中SKEWED BY (属性字符串,属性字符串) 
  List<List<String>> skewedColValues;//解析SKEWED BY (属性字符串,属性字符串) on (多组属性值集合 (xxx,xxx),(xxx,xxx),(xxx,xxx) ) 中on (多组属性值集合 (xxx,xxx),(xxx,xxx),(xxx,xxx) )
  Table table;
  boolean isDropIfExists = false;

  public AlterTableDesc() {
  }

  /**
   * @param tblName
   *          table name
   * @param oldColName
   *          old column name
   * @param newColName
   *          new column name
   * @param newComment
   * @param newType
   * 解析String CHANGE [COLUMN] "oldName" "newName" type [COMMENT String] [FIRST|AFTER String]操作
   */
  public AlterTableDesc(String tblName, String oldColName, String newColName,
      String newType, String newComment, boolean first, String afterCol) {
    super();
    oldName = tblName;
    this.oldColName = oldColName;
    this.newColName = newColName;
    newColType = newType;
    newColComment = newComment;
    this.first = first;
    this.afterCol = afterCol;
    op = AlterTableTypes.RENAMECOLUMN;
  }

  /**
   * @param oldName
   *          old name of the table
   * @param newName
   *          new name of the table
   */
  public AlterTableDesc(String oldName, String newName, boolean expectView) {
    op = AlterTableTypes.RENAME;
    this.oldName = oldName;
    this.newName = newName;
    this.expectView = expectView;
  }

  /**
   * @param name
   *          name of the table
   * @param newCols
   *          new columns to be added
   */
  public AlterTableDesc(String name, List<FieldSchema> newCols,
      AlterTableTypes alterType) {
    op = alterType;
    oldName = name;
    this.newCols = new ArrayList<FieldSchema>(newCols);
  }

  /**
   * @param alterType
   *          type of alter op
   */
  public AlterTableDesc(AlterTableTypes alterType) {
    this(alterType, false);
  }

  /**
   * @param alterType
   *          type of alter op
   */
  public AlterTableDesc(AlterTableTypes alterType, boolean expectView) {
    op = alterType;
    this.expectView = expectView;
  }

  /**
   *
   * @param name
   *          name of the table
   * @param inputFormat
   *          new table input format
   * @param outputFormat
   *          new table output format
   * @param partSpec
   */
  public AlterTableDesc(String name, String inputFormat, String outputFormat,
      String serdeName, String storageHandler, HashMap<String, String> partSpec) {
    super();
    op = AlterTableTypes.ADDFILEFORMAT;
    oldName = name;
    this.inputFormat = inputFormat;
    this.outputFormat = outputFormat;
    this.serdeName = serdeName;
    this.storageHandler = storageHandler;
    this.partSpec = partSpec;
  }

  /**
   * alterStatementSuffixClusterbySortby格式:
解析CLUSTERED BY (column1,column2) [SORTED BY (column1 desc,column2 desc)] into Number BUCKETS
解析NOT CLUSTERED 表示没有CLUSTERED BY 操作
   */
  public AlterTableDesc(String tableName, int numBuckets,
      List<String> bucketCols, List<Order> sortCols, HashMap<String, String> partSpec) {
    oldName = tableName;
    op = AlterTableTypes.ADDCLUSTERSORTCOLUMN;
    numberBuckets = numBuckets;
    bucketColumns = new ArrayList<String>(bucketCols);
    sortColumns = new ArrayList<Order>(sortCols);
    this.partSpec = partSpec;
  }

  /**
   * alterStatementSuffixClusterbySortby格式:
解析NOT SORTED 表示没有SORTED BY 操作
   */
  public AlterTableDesc(String tableName, boolean sortingOff, HashMap<String, String> partSpec) {
    oldName = tableName;
    op = AlterTableTypes.ADDCLUSTERSORTCOLUMN;
    isTurnOffSorting = sortingOff;
    this.partSpec = partSpec;
  }

  /**
   * 为tableName下的某个partition分区分配HDFS路径
   * HashMap<String, String> partSpec 是因为确定某个partition可能来源于多个属性,例如log_date log_hour
   * @param tableName
   * @param newLocation 新分配的HDFS路径
   * @param partSpec
   */
  public AlterTableDesc(String tableName, String newLocation,
      HashMap<String, String> partSpec) {
    op = AlterTableTypes.ALTERLOCATION;
    this.oldName = tableName;
    this.newLocation = newLocation;
    this.partSpec = partSpec;
  }

  public AlterTableDesc(String tableName, Map<List<String>, String> locations,
      HashMap<String, String> partSpec) {
    op = AlterTableTypes.ALTERSKEWEDLOCATION;
    this.oldName = tableName;
    this.skewedLocations = locations;
    this.partSpec = partSpec;
  }

  //解析tableSkewed
  public AlterTableDesc(String tableName, boolean turnOffSkewed,
      List<String> skewedColNames, List<List<String>> skewedColValues) {
    oldName = tableName;
    op = AlterTableTypes.ADDSKEWEDBY;
    this.isTurnOffSkewed = turnOffSkewed;
    this.skewedColNames = new ArrayList<String>(skewedColNames);
    this.skewedColValues = new ArrayList<List<String>>(skewedColValues);
  }

  public AlterTableDesc(String tableName, HashMap<String, String> partSpec, int numBuckets) {
    op = AlterTableTypes.ALTERBUCKETNUM;
    this.oldName = tableName;
    this.partSpec = partSpec;
    this.numberBuckets = numBuckets;
  }

  @Explain(displayName = "new columns")
  public List<String> getNewColsString() {
    return Utilities.getFieldSchemaString(getNewCols());
  }

  @Explain(displayName = "type")
  public String getAlterTableTypeString() {
    switch (op) {
    case RENAME:
      return "rename";
    case ADDCOLS:
      return "add columns";
    case REPLACECOLS:
      return "replace columns";
    }

    return "unknown";
  }

  /**
   * @return the old name of the table
   */
  @Explain(displayName = "old name")
  public String getOldName() {
    return oldName;
  }

  /**
   * @param oldName
   *          the oldName to set
   */
  public void setOldName(String oldName) {
    this.oldName = oldName;
  }

  /**
   * @return the newName
   */
  @Explain(displayName = "new name")
  public String getNewName() {
    return newName;
  }

  /**
   * @param newName
   *          the newName to set
   */
  public void setNewName(String newName) {
    this.newName = newName;
  }

  /**
   * @return the op
   */
  public AlterTableTypes getOp() {
    return op;
  }

  /**
   * @param op
   *          the op to set
   */
  public void setOp(AlterTableTypes op) {
    this.op = op;
  }

  /**
   * @return the newCols
   */
  public ArrayList<FieldSchema> getNewCols() {
    return newCols;
  }

  /**
   * @param newCols
   *          the newCols to set
   */
  public void setNewCols(ArrayList<FieldSchema> newCols) {
    this.newCols = newCols;
  }

  /**
   * @return the serdeName
   */
  @Explain(displayName = "deserializer library")
  public String getSerdeName() {
    return serdeName;
  }

  /**
   * @param serdeName
   *          the serdeName to set
   */
  public void setSerdeName(String serdeName) {
    this.serdeName = serdeName;
  }

  /**
   * @return the props
   */
  @Explain(displayName = "properties")
  public HashMap<String, String> getProps() {
    return props;
  }

  /**
   * @param props
   *          the props to set
   */
  public void setProps(HashMap<String, String> props) {
    this.props = props;
  }

  /**
   * @return the input format
   */
  @Explain(displayName = "input format")
  public String getInputFormat() {
    return inputFormat;
  }

  /**
   * @param inputFormat
   *          the input format to set
   */
  public void setInputFormat(String inputFormat) {
    this.inputFormat = inputFormat;
  }

  /**
   * @return the output format
   */
  @Explain(displayName = "output format")
  public String getOutputFormat() {
    return outputFormat;
  }

  /**
   * @param outputFormat
   *          the output format to set
   */
  public void setOutputFormat(String outputFormat) {
    this.outputFormat = outputFormat;
  }

  /**
   * @return the storage handler
   */
  @Explain(displayName = "storage handler")
  public String getStorageHandler() {
    return storageHandler;
  }

  /**
   * @param storageHandler
   *          the storage handler to set
   */
  public void setStorageHandler(String storageHandler) {
    this.storageHandler = storageHandler;
  }

  /**
   * @return the number of buckets
   */
  public int getNumberBuckets() {
    return numberBuckets;
  }

  /**
   * @param numberBuckets
   *          the number of buckets to set
   */
  public void setNumberBuckets(int numberBuckets) {
    this.numberBuckets = numberBuckets;
  }

  /**
   * @return the bucket columns
   */
  public ArrayList<String> getBucketColumns() {
    return bucketColumns;
  }

  /**
   * @param bucketColumns
   *          the bucket columns to set
   */
  public void setBucketColumns(ArrayList<String> bucketColumns) {
    this.bucketColumns = bucketColumns;
  }

  /**
   * @return the sort columns
   */
  public ArrayList<Order> getSortColumns() {
    return sortColumns;
  }

  /**
   * @param sortColumns
   *          the sort columns to set
   */
  public void setSortColumns(ArrayList<Order> sortColumns) {
    this.sortColumns = sortColumns;
  }

  /**
   * @return old column name
   */
  public String getOldColName() {
    return oldColName;
  }

  /**
   * @param oldColName
   *          the old column name
   */
  public void setOldColName(String oldColName) {
    this.oldColName = oldColName;
  }

  /**
   * @return new column name
   */
  public String getNewColName() {
    return newColName;
  }

  /**
   * @param newColName
   *          the new column name
   */
  public void setNewColName(String newColName) {
    this.newColName = newColName;
  }

  /**
   * @return new column type
   */
  public String getNewColType() {
    return newColType;
  }

  /**
   * @param newType
   *          new column's type
   */
  public void setNewColType(String newType) {
    newColType = newType;
  }

  /**
   * @return new column's comment
   */
  public String getNewColComment() {
    return newColComment;
  }

  /**
   * @param newComment
   *          new column's comment
   */
  public void setNewColComment(String newComment) {
    newColComment = newComment;
  }

  /**
   * @return if the column should be changed to position 0
   */
  public boolean getFirst() {
    return first;
  }

  /**
   * @param first
   *          set the column to position 0
   */
  public void setFirst(boolean first) {
    this.first = first;
  }

  /**
   * @return the column's after position
   */
  public String getAfterCol() {
    return afterCol;
  }

  /**
   * @param afterCol
   *          set the column's after position
   */
  public void setAfterCol(String afterCol) {
    this.afterCol = afterCol;
  }

  /**
   * @return whether to expect a view being altered
   */
  public boolean getExpectView() {
    return expectView;
  }

  /**
   * @param expectView
   *          set whether to expect a view being altered
   */
  public void setExpectView(boolean expectView) {
    this.expectView = expectView;
  }

  /**
   * @return part specification
   */
  public HashMap<String, String> getPartSpec() {
    return partSpec;
  }

  /**
   * @param partSpec
   */
  public void setPartSpec(HashMap<String, String> partSpec) {
    this.partSpec = partSpec;
  }

  /**
   * @return new location
   */
  public String getNewLocation() {
    return newLocation;
  }

  /**
   * @param newLocation new location
   */
  public void setNewLocation(String newLocation) {
    this.newLocation = newLocation;
  }

  public boolean isProtectModeEnable() {
    return protectModeEnable;
  }

  public void setProtectModeEnable(boolean protectModeEnable) {
    this.protectModeEnable = protectModeEnable;
  }

  public ProtectModeType getProtectModeType() {
    return protectModeType;
  }

  public void setProtectModeType(ProtectModeType protectModeType) {
    this.protectModeType = protectModeType;
  }
  /**
   * @return the skewedLocations
   */
  public Map<List<String>, String> getSkewedLocations() {
    return skewedLocations;
  }

  /**
   * @param skewedLocations the skewedLocations to set
   */
  public void setSkewedLocations(Map<List<String>, String> skewedLocations) {
    this.skewedLocations = skewedLocations;
  }

  /**
   * @return isTurnOffSorting
   */
  public boolean isTurnOffSorting() {
    return isTurnOffSorting;
  }

  /**
   * @return the turnOffSkewed
   */
  public boolean isTurnOffSkewed() {
    return isTurnOffSkewed;
  }

  /**
   * @param turnOffSkewed the turnOffSkewed to set
   */
  public void setTurnOffSkewed(boolean turnOffSkewed) {
    this.isTurnOffSkewed = turnOffSkewed;
  }

  /**
   * @return the skewedColNames
   */
  public List<String> getSkewedColNames() {
    return skewedColNames;
  }

  /**
   * @param skewedColNames the skewedColNames to set
   */
  public void setSkewedColNames(List<String> skewedColNames) {
    this.skewedColNames = skewedColNames;
  }

  /**
   * @return the skewedColValues
   */
  public List<List<String>> getSkewedColValues() {
    return skewedColValues;
  }

  /**
   * @param skewedColValues the skewedColValues to set
   */
  public void setSkewedColValues(List<List<String>> skewedColValues) {
    this.skewedColValues = skewedColValues;
  }

  /**
   * Validate alter table description.
   *
   * @throws SemanticException
   */
  public void validate() throws SemanticException {
    if (null != table) {
      /* Validate skewed information. */
      ValidationUtility.validateSkewedInformation(
          ParseUtils.validateColumnNameUniqueness(table.getCols()), this.getSkewedColNames(),
          this.getSkewedColValues());
    }
  }

  /**
   * @return the table
   */
  public Table getTable() {
    return table;
  }

  /**
   * @param table the table to set
   */
  public void setTable(Table table) {
    this.table = table;
  }

  /**
   * @return the isStoredAsSubDirectories
   */
  public boolean isStoredAsSubDirectories() {
    return isStoredAsSubDirectories;
  }

  /**
   * @param isStoredAsSubDirectories the isStoredAsSubDirectories to set
   */
  public void setStoredAsSubDirectories(boolean isStoredAsSubDirectories) {
    this.isStoredAsSubDirectories = isStoredAsSubDirectories;
  }

  /**
   * @param isDropIfExists the isDropIfExists to set
   */
  public void setDropIfExists(boolean isDropIfExists) {
    this.isDropIfExists = isDropIfExists;
  }

  /**
   * @return isDropIfExists
   */
  public boolean getIsDropIfExists() {
    return isDropIfExists;
  }

}
