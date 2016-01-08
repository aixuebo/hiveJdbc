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

package org.apache.hadoop.hive.ql.parse;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.metastore.api.FieldSchema;
import org.apache.hadoop.hive.metastore.api.Order;
import org.apache.hadoop.hive.ql.Context;
import org.apache.hadoop.hive.ql.ErrorMsg;
import org.apache.hadoop.hive.ql.QueryProperties;
import org.apache.hadoop.hive.ql.exec.ExprNodeEvaluatorFactory;
import org.apache.hadoop.hive.ql.exec.FetchTask;
import org.apache.hadoop.hive.ql.exec.Task;
import org.apache.hadoop.hive.ql.exec.Utilities;
import org.apache.hadoop.hive.ql.hooks.LineageInfo;
import org.apache.hadoop.hive.ql.hooks.ReadEntity;
import org.apache.hadoop.hive.ql.hooks.WriteEntity;
import org.apache.hadoop.hive.ql.io.IgnoreKeyTextOutputFormat;
import org.apache.hadoop.hive.ql.io.RCFileInputFormat;
import org.apache.hadoop.hive.ql.io.RCFileOutputFormat;
import org.apache.hadoop.hive.ql.io.orc.OrcInputFormat;
import org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat;
import org.apache.hadoop.hive.ql.io.orc.OrcSerde;
import org.apache.hadoop.hive.ql.lib.Node;
import org.apache.hadoop.hive.ql.metadata.Hive;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.metadata.InvalidTableException;
import org.apache.hadoop.hive.ql.metadata.Partition;
import org.apache.hadoop.hive.ql.metadata.Table;
import org.apache.hadoop.hive.ql.optimizer.listbucketingpruner.ListBucketingPrunerUtils;
import org.apache.hadoop.hive.ql.plan.ExprNodeDesc;
import org.apache.hadoop.hive.ql.plan.ListBucketingCtx;
import org.apache.hadoop.hive.ql.plan.PlanUtils;
import org.apache.hadoop.hive.ql.session.SessionState.LogHelper;
import org.apache.hadoop.hive.serde.serdeConstants;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorConverters;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfoUtils;
import org.apache.hadoop.mapred.SequenceFileInputFormat;
import org.apache.hadoop.mapred.SequenceFileOutputFormat;
import org.apache.hadoop.mapred.TextInputFormat;

/**
 * BaseSemanticAnalyzer.
 * 基础分隔,分隔行的拆分符以及输入输出对象
 */
@SuppressWarnings("deprecation")
public abstract class BaseSemanticAnalyzer {
  protected final Hive db;
  protected final HiveConf conf;
  protected List<Task<? extends Serializable>> rootTasks;//解析后的任务集合
  protected FetchTask fetchTask;
  protected final Log LOG;
  protected final LogHelper console;

  protected Context ctx;
  protected HashMap<String, String> idToTableNameMap;
  protected QueryProperties queryProperties;

  public static int HIVE_COLUMN_ORDER_ASC = 1;
  public static int HIVE_COLUMN_ORDER_DESC = 0;

  /**
   * ReadEntitites that are passed to the hooks.
   */
  protected HashSet<ReadEntity> inputs;
  /**
   * List of WriteEntities that are passed to the hooks.
   */
  protected HashSet<WriteEntity> outputs;
  /**
   * Lineage information for the query.
   */
  protected LineageInfo linfo;
  protected TableAccessInfo tableAccessInfo;
  //返回table可以访问哪些属性
  protected ColumnAccessInfo columnAccessInfo;

  protected static final String TEXTFILE_INPUT = TextInputFormat.class
      .getName();
  protected static final String TEXTFILE_OUTPUT = IgnoreKeyTextOutputFormat.class
      .getName();
  protected static final String SEQUENCEFILE_INPUT = SequenceFileInputFormat.class
      .getName();
  protected static final String SEQUENCEFILE_OUTPUT = SequenceFileOutputFormat.class
      .getName();
  protected static final String RCFILE_INPUT = RCFileInputFormat.class
      .getName();
  protected static final String RCFILE_OUTPUT = RCFileOutputFormat.class
      .getName();
  protected static final String ORCFILE_INPUT = OrcInputFormat.class
      .getName();
  protected static final String ORCFILE_OUTPUT = OrcOutputFormat.class
      .getName();
  protected static final String ORCFILE_SERDE = OrcSerde.class
      .getName();

  //设置行分隔符信息
  class RowFormatParams {
    String fieldDelim = null;//分隔每一个属性
    String fieldEscape = null;//分隔属性时候用的转义字符
    String collItemDelim = null;//分隔集合,例arr数组,或者map中的每一个key-value
    String mapKeyDelim = null;//分割map中的key-value
    String lineDelim = null;//分隔每行数据,必须是\n

    protected void analyzeRowFormat(AnalyzeCreateCommonVars shared, ASTNode child) throws SemanticException {
      child = (ASTNode) child.getChild(0);
      int numChildRowFormat = child.getChildCount();
      for (int numC = 0; numC < numChildRowFormat; numC++) {
        ASTNode rowChild = (ASTNode) child.getChild(numC);
        switch (rowChild.getToken().getType()) {
        case HiveParser.TOK_TABLEROWFORMATFIELD://fieldDelim
          fieldDelim = unescapeSQLString(rowChild.getChild(0)
              .getText());
          if (rowChild.getChildCount() >= 2) {
            fieldEscape = unescapeSQLString(rowChild
                .getChild(1).getText());
          }
          break;
        case HiveParser.TOK_TABLEROWFORMATCOLLITEMS://collItemDelim
          collItemDelim = unescapeSQLString(rowChild
              .getChild(0).getText());
          break;
        case HiveParser.TOK_TABLEROWFORMATMAPKEYS://mapKeyDelim
          mapKeyDelim = unescapeSQLString(rowChild.getChild(0)
              .getText());
          break;
        case HiveParser.TOK_TABLEROWFORMATLINES://lineDelim
          lineDelim = unescapeSQLString(rowChild.getChild(0)
              .getText());
          if (!lineDelim.equals("\n")
              && !lineDelim.equals("10")) {
            throw new SemanticException(SemanticAnalyzer.generateErrorMessage(rowChild,
                ErrorMsg.LINES_TERMINATED_BY_NON_NEWLINE.getMsg()));
          }
          break;
        default:
          assert false;
        }
      }
    }
  }

  //STORED BY xxxx存储引擎, WITH SERDEPROPERTIES (key=value,key=value,key) ,注意key=value集合是为xxx存储引擎提供的参数集合
  class AnalyzeCreateCommonVars {
    String serde = null;//表示STORED BY后面的属性
    //存储所有属性key-value信息集合
    Map<String, String> serdeProps = new HashMap<String, String>();
  }

  /**
   * 输入/输出的格式化类型
   */
  class StorageFormat {
    String inputFormat = null;
    String outputFormat = null;
    String storageHandler = null;

    /**
     * 
     * @param child
     * @param shared 全局的参数集合
     * @return
     */
    protected boolean fillStorageFormat(ASTNode child, AnalyzeCreateCommonVars shared) {
      boolean storageFormat = false;
      switch(child.getToken().getType()) {
      case HiveParser.TOK_TBLSEQUENCEFILE://SequenceFile
        inputFormat = SEQUENCEFILE_INPUT;
        outputFormat = SEQUENCEFILE_OUTPUT;
        storageFormat = true;
        break;
      case HiveParser.TOK_TBLTEXTFILE://textFile
        inputFormat = TEXTFILE_INPUT;
        outputFormat = TEXTFILE_OUTPUT;
        storageFormat = true;
        break;
      case HiveParser.TOK_TBLRCFILE://RCFile
        inputFormat = RCFILE_INPUT;
        outputFormat = RCFILE_OUTPUT;
        if (shared.serde == null) {
          shared.serde = conf.getVar(HiveConf.ConfVars.HIVEDEFAULTRCFILESERDE);
        }
        storageFormat = true;
        break;
      case HiveParser.TOK_TBLORCFILE://ORC
        inputFormat = ORCFILE_INPUT;
        outputFormat = ORCFILE_OUTPUT;
        shared.serde = ORCFILE_SERDE;
        storageFormat = true;
        break;
      case HiveParser.TOK_TABLEFILEFORMAT://自定义输入/输出格式
        inputFormat = unescapeSQLString(child.getChild(0).getText());
        outputFormat = unescapeSQLString(child.getChild(1).getText());
        storageFormat = true;
        break;
        /**
         * child:[storageHandler类名,
         	[
         	 [key,value],
         	 [key,value]
         	]
         ]
         */
      case HiveParser.TOK_STORAGEHANDLER:
        storageHandler = unescapeSQLString(child.getChild(0).getText());
        if (child.getChildCount() == 2) {// 格式N个child子节点,每一个子节点有两个小节点,分频存储key-value
          readProps(
            (ASTNode) (child.getChild(1).getChild(0)),
            shared.serdeProps);
        }
        storageFormat = true;
        break;
      }
      return storageFormat;
    }

    //设置默认的文件输入/输出类型
    protected void fillDefaultStorageFormat(AnalyzeCreateCommonVars shared) {
      if ((inputFormat == null) && (storageHandler == null)) {//如果没有设置,则去设置
        if ("SequenceFile".equalsIgnoreCase(conf.getVar(HiveConf.ConfVars.HIVEDEFAULTFILEFORMAT))) {
          inputFormat = SEQUENCEFILE_INPUT;
          outputFormat = SEQUENCEFILE_OUTPUT;
        } else if ("RCFile".equalsIgnoreCase(conf.getVar(HiveConf.ConfVars.HIVEDEFAULTFILEFORMAT))) {
          inputFormat = RCFILE_INPUT;
          outputFormat = RCFILE_OUTPUT;
          shared.serde = conf.getVar(HiveConf.ConfVars.HIVEDEFAULTRCFILESERDE);
        } else if ("ORC".equalsIgnoreCase(conf.getVar(HiveConf.ConfVars.HIVEDEFAULTFILEFORMAT))) {
          inputFormat = ORCFILE_INPUT;
          outputFormat = ORCFILE_OUTPUT;
          shared.serde = ORCFILE_SERDE;
        } else {
          inputFormat = TEXTFILE_INPUT;
          outputFormat = TEXTFILE_OUTPUT;
        }
      }
    }

  }

  /**
   * 基础分隔,分隔行的拆分符以及输入输出对象
   */
  public BaseSemanticAnalyzer(HiveConf conf) throws SemanticException {
    try {
      this.conf = conf;
      db = Hive.get(conf);
      rootTasks = new ArrayList<Task<? extends Serializable>>();
      LOG = LogFactory.getLog(this.getClass().getName());
      console = new LogHelper(LOG);
      idToTableNameMap = new HashMap<String, String>();
      inputs = new LinkedHashSet<ReadEntity>();
      outputs = new LinkedHashSet<WriteEntity>();
    } catch (Exception e) {
      throw new SemanticException(e);
    }
  }

  public HashMap<String, String> getIdToTableNameMap() {
    return idToTableNameMap;
  }

  public abstract void analyzeInternal(ASTNode ast) throws SemanticException;
  public void init() {
    //no-op
  }

  public void initCtx(Context ctx) {
    this.ctx = ctx;
  }

  //hive的sql解析完成后,进行分析阶段
  public void analyze(ASTNode ast, Context ctx) throws SemanticException {
    initCtx(ctx);
    init();
    analyzeInternal(ast);
  }

  //对分析后的数据进行校验正确性
  public void validate() throws SemanticException {
    // Implementations may choose to override this
  }

  public List<Task<? extends Serializable>> getRootTasks() {
    return rootTasks;
  }

  /**
   * @return the fetchTask
   */
  public FetchTask getFetchTask() {
    return fetchTask;
  }

  /**
   * @param fetchTask
   *          the fetchTask to set
   */
  public void setFetchTask(FetchTask fetchTask) {
    this.fetchTask = fetchTask;
  }

  protected void reset() {
    rootTasks = new ArrayList<Task<? extends Serializable>>();
  }

  //去除``字符信息,该字符应用于数据库表名字
  public static String stripIdentifierQuotes(String val) {
    if ((val.charAt(0) == '`' && val.charAt(val.length() - 1) == '`')) {
      val = val.substring(1, val.length() - 1);
    }
    return val;
  }

  //去除单引号和双引号
  public static String stripQuotes(String val) {
    return PlanUtils.stripQuotes(val);
  }

  //转码
  public static String charSetString(String charSetName, String charSetString)
      throws SemanticException {
    try {
      // The character set name starts with a _, so strip that
      charSetName = charSetName.substring(1);
      if (charSetString.charAt(0) == '\'') {
        return new String(unescapeSQLString(charSetString).getBytes(),
            charSetName);
      } else // hex input is also supported
      {
        assert charSetString.charAt(0) == '0';
        assert charSetString.charAt(1) == 'x';
        charSetString = charSetString.substring(2);

        byte[] bArray = new byte[charSetString.length() / 2];
        int j = 0;
        for (int i = 0; i < charSetString.length(); i += 2) {
          int val = Character.digit(charSetString.charAt(i), 16) * 16
              + Character.digit(charSetString.charAt(i + 1), 16);
          if (val > 127) {
            val = val - 256;
          }
          bArray[j++] = (byte)val;
        }

        String res = new String(bArray, charSetName);
        return res;
      }
    } catch (UnsupportedEncodingException e) {
      throw new SemanticException(e);
    }
  }

  /**
   * Get dequoted name from a table/column node.
   * @param tableOrColumnNode the table or column node
   * @return for table node, db.tab or tab. for column node column.
   * 获取数据库和表名字,并且将``删除掉,返回dbName + "." + tableName
   * 或者获取列属性的名字,返回列名字
   */
  public static String getUnescapedName(ASTNode tableOrColumnNode) {
    return getUnescapedName(tableOrColumnNode, null);
  }

  /**
   * 获取数据库和表名字,并且将``删除掉,返回dbName + "." + tableName
   * 或者获取列属性的名字,返回列名字
   * @param tableOrColumnNode等待解析的对象
   * @param currentDatabase 当前使用的数据库
   */
  public static String getUnescapedName(ASTNode tableOrColumnNode, String currentDatabase) {
    if (tableOrColumnNode.getToken().getType() == HiveParser.TOK_TABNAME) {
      // table node
      if (tableOrColumnNode.getChildCount() == 2) {///包含两个参数
        String dbName = unescapeIdentifier(tableOrColumnNode.getChild(0).getText());
        String tableName = unescapeIdentifier(tableOrColumnNode.getChild(1).getText());
        return dbName + "." + tableName;
      }
      //否则说明仅仅包含table,使用的是默认的数据库
      String tableName = unescapeIdentifier(tableOrColumnNode.getChild(0).getText());
      if (currentDatabase != null) {
        return currentDatabase + "." + tableName;
      }
      return tableName;
    }
    // column node
    return unescapeIdentifier(tableOrColumnNode.getText());
  }

  /**
   * Get the unqualified name from a table node.
   *
   * This method works for table names qualified with their schema (e.g., "db.table")
   * and table names without schema qualification. In both cases, it returns
   * the table name without the schema.
   *
   * @param node the table node
   * @return the table name without schema qualification
   *         (i.e., if name is "db.table" or "table", returns "table")
   *   返回数据库表名字
   */
  public static String getUnescapedUnqualifiedTableName(ASTNode node) {
    assert node.getChildCount() <= 2;

    if (node.getChildCount() == 2) {//说明包含数据库也包含表名字,因此获取第二个位置的表名字
      node = (ASTNode) node.getChild(1);
    }

    return getUnescapedName(node);
  }


  /**
   * Remove the encapsulating "`" pair from the identifier. We allow users to
   * use "`" to escape identifier for table names, column names and aliases, in
   * case that coincide with Hive language keywords.
   * 去除``字符信息,该字符应用于数据库表名字
   */
  public static String unescapeIdentifier(String val) {
    if (val == null) {
      return null;
    }
    if (val.charAt(0) == '`' && val.charAt(val.length() - 1) == '`') {
      val = val.substring(1, val.length() - 1);
    }
    return val;
  }

  /**
   * Converts parsed key/value properties pairs into a map.
   *
   * @param prop ASTNode parent of the key/value pairs
   *   格式N个child子节点,每一个子节点有两个小节点,分频存储key-value
   *
   * @param mapProp property map which receives the mappings
   * 将prop中的每一个数据都会有key-value,将其存储早map中
   */
  public static void readProps(
    ASTNode prop, Map<String, String> mapProp) {

    for (int propChild = 0; propChild < prop.getChildCount(); propChild++) {
      String key = unescapeSQLString(prop.getChild(propChild).getChild(0)
          .getText());
      String value = null;
      if (prop.getChild(propChild).getChild(1) != null) {
        value = unescapeSQLString(prop.getChild(propChild).getChild(1).getText());
      }
      mapProp.put(key, value);
    }
  }

  private static final int[] multiplier = new int[] {1000, 100, 10, 1};

  /**
   * 转义sql
   */
  @SuppressWarnings("nls")
  public static String unescapeSQLString(String b) {

    Character enclosure = null;

    // Some of the strings can be passed in as unicode. For example, the
    // delimiter can be passed in as \002 - So, we first check if the
    // string is a unicode number, else go back to the old behavior
    StringBuilder sb = new StringBuilder(b.length());
    for (int i = 0; i < b.length(); i++) {

      char currentChar = b.charAt(i);
      //过滤掉单引号和双引号
      if (enclosure == null) {
        if (currentChar == '\'' || b.charAt(i) == '\"') {
          enclosure = currentChar;
        }
        // ignore all other chars outside the enclosure
        continue;
      }

      if (enclosure.equals(currentChar)) {
        enclosure = null;
        continue;
      }

      //如果是u+4位数字,表示unicode,将其转换成code字符串拼接
      if (currentChar == '\\' && (i + 6 < b.length()) && b.charAt(i + 1) == 'u') {
        int code = 0;
        int base = i + 2;
        for (int j = 0; j < 4; j++) {
          int digit = Character.digit(b.charAt(j + base), 16);
          code += digit * multiplier[j];
        }
        sb.append((char)code);
        i += 5;
        continue;
      }

      //如果是\+3位数字.表示asscii,则也进行处理
      if (currentChar == '\\' && (i + 4 < b.length())) {
        char i1 = b.charAt(i + 1);
        char i2 = b.charAt(i + 2);
        char i3 = b.charAt(i + 3);
        if ((i1 >= '0' && i1 <= '1') && (i2 >= '0' && i2 <= '7')
            && (i3 >= '0' && i3 <= '7')) {
          byte bVal = (byte) ((i3 - '0') + ((i2 - '0') * 8) + ((i1 - '0') * 8 * 8));
          byte[] bValArr = new byte[1];
          bValArr[0] = bVal;
          String tmp = new String(bValArr);
          sb.append(tmp);
          i += 3;
          continue;
        }
      }

      //转义
      if (currentChar == '\\' && (i + 2 < b.length())) {
        char n = b.charAt(i + 1);
        switch (n) {
        case '0':
          sb.append("\0");
          break;
        case '\'':
          sb.append("'");
          break;
        case '"':
          sb.append("\"");
          break;
        case 'b':
          sb.append("\b");
          break;
        case 'n':
          sb.append("\n");
          break;
        case 'r':
          sb.append("\r");
          break;
        case 't':
          sb.append("\t");
          break;
        case 'Z':
          sb.append("\u001A");
          break;
        case '\\':
          sb.append("\\");
          break;
        // The following 2 lines are exactly what MySQL does
        case '%':
          sb.append("\\%");
          break;
        case '_':
          sb.append("\\_");
          break;
        default:
          sb.append(n);
        }
        i++;
      } else {
        sb.append(currentChar);
      }
    }
    return sb.toString();
  }

  public HashSet<ReadEntity> getInputs() {
    return inputs;
  }

  public HashSet<WriteEntity> getOutputs() {
    return outputs;
  }

  /**
   * @return the schema for the fields which will be produced
   * when the statement is executed, or null if not known
   */
  public List<FieldSchema> getResultSchema() {
    return null;
  }

  //获取所有的数据库属性集合
  protected List<FieldSchema> getColumns(ASTNode ast) throws SemanticException {
    return getColumns(ast, true);
  }

  /**
   * 自定义,目前不支持,需要自己实现该方法,继承DDLSemanticAnalyzer类即可
   * @param node
   * @throws SemanticException
   */
  protected void handleGenericFileFormat(ASTNode node) throws SemanticException{

  ASTNode child = (ASTNode)node.getChild(0);
  throw new SemanticException("Unrecognized file format in STORED AS clause:"+
         " "+ (child == null ? "" : child.getText()));
  }

  /**
   * Get the list of FieldSchema out of the ASTNode.
   * 获取数据库属性集合
   * lowerCase:true,表示将name属性名字改成小写
   */
  public static List<FieldSchema> getColumns(ASTNode ast, boolean lowerCase) throws SemanticException {
	  //FieldSchema 表示数据库表的一个属性,包含name、类型、注释
    List<FieldSchema> colList = new ArrayList<FieldSchema>();
    int numCh = ast.getChildCount();
    for (int i = 0; i < numCh; i++) {
      FieldSchema col = new FieldSchema();
      ASTNode child = (ASTNode) ast.getChild(i);
      Tree grandChild = child.getChild(0);
      if(grandChild != null) {
        String name = grandChild.getText();
        if(lowerCase) {
          name = name.toLowerCase();
        }
        // child 0 is the name of the column
        col.setName(unescapeIdentifier(name));
        // child 1 is the type of the column
        ASTNode typeChild = (ASTNode) (child.getChild(1));
        col.setType(getTypeStringFromAST(typeChild));

        // child 2 is the optional comment of the column
        if (child.getChildCount() == 3) {
          col.setComment(unescapeSQLString(child.getChild(2).getText()));
        }
      }
      colList.add(col);
    }
    return colList;
  }

  //仅仅获取数据库属性名称,并且输出都是小写的name
  //解析sql:COLUMNS (column1,column2...)
  protected List<String> getColumnNames(ASTNode ast) {
    List<String> colList = new ArrayList<String>();
    int numCh = ast.getChildCount();
    for (int i = 0; i < numCh; i++) {
      ASTNode child = (ASTNode) ast.getChild(i);
      colList.add(unescapeIdentifier(child.getText()).toLowerCase());
    }
    return colList;
  }

  
  /**
   * 返回数据库属性组装成的Order对象,用于order by子句中使用
   * 每一个Order对象包含属性name的小写,以及该属性的排序规则,倒序还是正序
   * 解析SORTED BY (column1 desc,column2 desc) 
   */
  protected List<Order> getColumnNamesOrder(ASTNode ast) {
    List<Order> colList = new ArrayList<Order>();
    int numCh = ast.getChildCount();
    for (int i = 0; i < numCh; i++) {
      ASTNode child = (ASTNode) ast.getChild(i);
      if (child.getToken().getType() == HiveParser.TOK_TABSORTCOLNAMEASC) {
        colList.add(new Order(unescapeIdentifier(child.getChild(0).getText()).toLowerCase(),
            HIVE_COLUMN_ORDER_ASC));
      } else {
        colList.add(new Order(unescapeIdentifier(child.getChild(0).getText()).toLowerCase(),
            HIVE_COLUMN_ORDER_DESC));
      }
    }
    return colList;
  }

  /**
   * 格式化数据库的属性的类型
   * @param typeNode 数据库属性类型节点
   * @return
   * @throws SemanticException
   */
  protected static String getTypeStringFromAST(ASTNode typeNode)
      throws SemanticException {
    switch (typeNode.getType()) {
    case HiveParser.TOK_LIST:
      return serdeConstants.LIST_TYPE_NAME + "<"
          + getTypeStringFromAST((ASTNode) typeNode.getChild(0)) + ">";
    case HiveParser.TOK_MAP:
      return serdeConstants.MAP_TYPE_NAME + "<"
          + getTypeStringFromAST((ASTNode) typeNode.getChild(0)) + ","
          + getTypeStringFromAST((ASTNode) typeNode.getChild(1)) + ">";
    case HiveParser.TOK_STRUCT:
      return getStructTypeStringFromAST(typeNode);
    case HiveParser.TOK_UNIONTYPE:
      return getUnionTypeStringFromAST(typeNode);
    default:
      return DDLSemanticAnalyzer.getTypeName(typeNode);
    }
  }

  private static String getStructTypeStringFromAST(ASTNode typeNode)
      throws SemanticException {
    String typeStr = serdeConstants.STRUCT_TYPE_NAME + "<";
    typeNode = (ASTNode) typeNode.getChild(0);
    int children = typeNode.getChildCount();
    if (children <= 0) {
      throw new SemanticException("empty struct not allowed.");
    }
    StringBuilder buffer = new StringBuilder(typeStr);
    for (int i = 0; i < children; i++) {
      ASTNode child = (ASTNode) typeNode.getChild(i);
      buffer.append(unescapeIdentifier(child.getChild(0).getText())).append(":");//属性name
      buffer.append(getTypeStringFromAST((ASTNode) child.getChild(1)));//格式化数据库的属性的类型
      if (i < children - 1) {
        buffer.append(",");
      }
    }

    buffer.append(">");
    return buffer.toString();
  }

  private static String getUnionTypeStringFromAST(ASTNode typeNode)
      throws SemanticException {
    String typeStr = serdeConstants.UNION_TYPE_NAME + "<";
    typeNode = (ASTNode) typeNode.getChild(0);
    int children = typeNode.getChildCount();
    if (children <= 0) {
      throw new SemanticException("empty union not allowed.");
    }
    StringBuilder buffer = new StringBuilder(typeStr);
    for (int i = 0; i < children; i++) {
      buffer.append(getTypeStringFromAST((ASTNode) typeNode.getChild(i)));
      if (i < children - 1) {
        buffer.append(",");
      }
    }
    buffer.append(">");
    typeStr = buffer.toString();
    return typeStr;
  }

  /**
   * tableSpec.
   */
  public static class tableSpec {
    public String tableName;//table表名字
    public Table tableHandle;//table对象
    public Map<String, String> partSpec; // has to use LinkedHashMap to enforce order按照设置分区的顺序,进行排序分区,key是属性,value是分区值
    public Partition partHandle;
    public int numDynParts; // number of dynamic partition columns多少个动态分区属性
    public List<Partition> partitions; // involved partitions in TableScanOperator/FileSinkOperator
    public static enum SpecType {TABLE_ONLY, STATIC_PARTITION, DYNAMIC_PARTITION};//没有分区的数据库表,静态分区,动态分区
    public SpecType specType;

    public tableSpec(Hive db, HiveConf conf, ASTNode ast)
        throws SemanticException {
      this(db, conf, ast, true, false);
    }

    public tableSpec(Hive db, HiveConf conf, ASTNode ast,
        boolean allowDynamicPartitionsSpec, boolean allowPartialPartitionsSpec)
        throws SemanticException {

      assert (ast.getToken().getType() == HiveParser.TOK_TAB
          || ast.getToken().getType() == HiveParser.TOK_TABLE_PARTITION
          || ast.getToken().getType() == HiveParser.TOK_TABTYPE
          || ast.getToken().getType() == HiveParser.TOK_CREATETABLE);
      int childIndex = 0;
      numDynParts = 0;

      try {
        // get table metadata
        tableName = getUnescapedName((ASTNode)ast.getChild(0));
        boolean testMode = conf.getBoolVar(HiveConf.ConfVars.HIVETESTMODE);
        if (testMode) {
          tableName = conf.getVar(HiveConf.ConfVars.HIVETESTMODEPREFIX)
              + tableName;
        }
        //非创建table
        if (ast.getToken().getType() != HiveParser.TOK_CREATETABLE) {
          tableHandle = db.getTable(tableName);
        }
      } catch (InvalidTableException ite) {
        throw new SemanticException(ErrorMsg.INVALID_TABLE.getMsg(ast
            .getChild(0)), ite);
      } catch (HiveException e) {
        throw new SemanticException(ErrorMsg.GENERIC_ERROR.getMsg(ast
            .getChild(childIndex), e.getMessage()), e);
      }

      // get partition metadata if partition specified 设置分区属性如果存在分区属性的话
      if (ast.getChildCount() == 2 && ast.getToken().getType() != HiveParser.TOK_CREATETABLE) {
        childIndex = 1;
        ASTNode partspec = (ASTNode) ast.getChild(1);
        partitions = new ArrayList<Partition>();
        // partSpec is a mapping from partition column name to its value.设置分区
        partSpec = new LinkedHashMap<String, String>(partspec.getChildCount());
        //解析分区信息
        for (int i = 0; i < partspec.getChildCount(); ++i) {
          ASTNode partspec_val = (ASTNode) partspec.getChild(i);
          String val = null;
          String colName = unescapeIdentifier(partspec_val.getChild(0).getText().toLowerCase());
          if (partspec_val.getChildCount() < 2) { // DP in the form of T partition (ds, hr) 仅仅设置了属性name,而没有设置值,因此是属于动态分区
            if (allowDynamicPartitionsSpec) {//必须允许动态分区存在
              ++numDynParts;
            } else {
              throw new SemanticException(ErrorMsg.INVALID_PARTITION
                                                       .getMsg(" - Dynamic partitions not allowed"));
            }
          } else { // in the form of T partition (ds="2010-03-03") 静态分区,获取该属性对应的分区值2010-03-03
            val = stripQuotes(partspec_val.getChild(1).getText());
          }
          //设置属性和分区值
          partSpec.put(colName, val);
        }

        // check if the columns specified in the partition() clause are actually partition columns
        //校验该table对象的分区信息是否有问题
        validatePartSpec(tableHandle, partSpec, ast, conf);

        // check if the partition spec is valid
        if (numDynParts > 0) {//说明是动态分区
          List<FieldSchema> parts = tableHandle.getPartitionKeys();//总分区字段数
          int numStaPart = parts.size() - numDynParts;//有多少静态分区
          //严格要求时,要求至少要有一个静态分区,因此不允许numStaPart=0,即numStaPart=0说明全都是动态分区
          if (numStaPart == 0 &&
              conf.getVar(HiveConf.ConfVars.DYNAMICPARTITIONINGMODE).equalsIgnoreCase("strict")) {
            throw new SemanticException(ErrorMsg.DYNAMIC_PARTITION_STRICT_MODE.getMsg());
          }

          // check the partitions in partSpec be the same as defined in table schema分区数量一定是要匹配的
          if (partSpec.keySet().size() != parts.size()) {
            ErrorPartSpec(partSpec, parts);
          }
          
          //分区的顺序必须一致,以及分区的属性必须一致,即不仅仅是数量一致的就可以了
          Iterator<String> itrPsKeys = partSpec.keySet().iterator();
          for (FieldSchema fs: parts) {
            if (!itrPsKeys.next().toLowerCase().equals(fs.getName().toLowerCase())) {
              ErrorPartSpec(partSpec, parts);
            }
          }

          // check if static partition appear after dynamic partitions
          //校验必须静态分区在最前面,然后后面跟着的是动态分区
          for (FieldSchema fs: parts) {
            if (partSpec.get(fs.getName().toLowerCase()) == null) {
              if (numStaPart > 0) { // found a DP, but there exists ST as subpartition,静态分区大于0,说明动态分区已经插入到静态分区前面了,因此出现异常
                throw new SemanticException(
                    ErrorMsg.PARTITION_DYN_STA_ORDER.getMsg(ast.getChild(childIndex)));
              }
              break;
            } else {
              --numStaPart;
            }
          }
          partHandle = null;
          specType = SpecType.DYNAMIC_PARTITION;
        } else {//说明是静态分区
          try {
            if (allowPartialPartitionsSpec) {//允许并行分区执行
              partitions = db.getPartitions(tableHandle, partSpec);
            } else {
              // this doesn't create partition.创建每一个分区的处理对象
              partHandle = db.getPartition(tableHandle, partSpec, false);
              if (partHandle == null) {
                // if partSpec doesn't exists in DB, return a delegate one
                // and the actual partition is created in MoveTask
                partHandle = new Partition(tableHandle, partSpec, null);
              } else {
                partitions.add(partHandle);
              }
            }
          } catch (HiveException e) {
            throw new SemanticException(
                ErrorMsg.INVALID_PARTITION.getMsg(ast.getChild(childIndex)), e);
          }
          specType = SpecType.STATIC_PARTITION;
        }
      } else {//说明不存在分区,仅仅是创建表分析
        specType = SpecType.TABLE_ONLY;
      }
    }

    public Map<String, String> getPartSpec() {
      return this.partSpec;
    }

    public void setPartSpec(Map<String, String> partSpec) {
      this.partSpec = partSpec;
    }

    @Override
    public String toString() {
      if (partHandle != null) {
        return partHandle.toString();
      } else {
        return tableHandle.toString();
      }
    }

  }

  /**
   * Gets the lineage information.
   *
   * @return LineageInfo associated with the query.
   */
  public LineageInfo getLineageInfo() {
    return linfo;
  }

  /**
   * Sets the lineage information.
   *
   * @param linfo The LineageInfo structure that is set in the optimization phase.
   */
  public void setLineageInfo(LineageInfo linfo) {
    this.linfo = linfo;
  }

  /**
   * Gets the table access information.
   *
   * @return TableAccessInfo associated with the query.
   */
  public TableAccessInfo getTableAccessInfo() {
    return tableAccessInfo;
  }

  /**
   * Sets the table access information.
   *
   * @param taInfo The TableAccessInfo structure that is set in the optimization phase.
   */
  public void setTableAccessInfo(TableAccessInfo tableAccessInfo) {
    this.tableAccessInfo = tableAccessInfo;
  }

  /**
   * Gets the column access information.
   *
   * @return ColumnAccessInfo associated with the query.
   */
  public ColumnAccessInfo getColumnAccessInfo() {
    return columnAccessInfo;
  }

  /**
   * Sets the column access information.
   *
   * @param columnAccessInfo The ColumnAccessInfo structure that is set immediately after
   * the optimization phase.
   */
  public void setColumnAccessInfo(ColumnAccessInfo columnAccessInfo) {
    this.columnAccessInfo = columnAccessInfo;
  }

  /**
   * 获取分区信息,最终返回到map中,key是分区字段,value是分区值
   * 解析PARTITION (name=value,name=value,name)
   */
  protected HashMap<String, String> extractPartitionSpecs(Tree partspec)
      throws SemanticException {
    HashMap<String, String> partSpec = new LinkedHashMap<String, String>();
    for (int i = 0; i < partspec.getChildCount(); ++i) {
      CommonTree partspec_val = (CommonTree) partspec.getChild(i);
      String val = stripQuotes(partspec_val.getChild(1).getText());
      partSpec.put(partspec_val.getChild(0).getText().toLowerCase(), val);
    }
    return partSpec;
  }

  /**
   * Checks if given specification is proper specification for prefix of
   * partition cols, for table partitioned by ds, hr, min valid ones are
   * (ds='2008-04-08'), (ds='2008-04-08', hr='12'), (ds='2008-04-08', hr='12', min='30')
   * invalid one is for example (ds='2008-04-08', min='30')
   * @param spec specification key-value map等待校验的数据,例如ds='2008-04-08', min='30',如果规则是ds, hr, min,则校验分区失败
   * @return true if the specification is prefix; never returns false, but throws
   * @throws HiveException
   */
  final public boolean isValidPrefixSpec(Table tTable, Map<String, String> spec)
 throws HiveException {

    // TODO - types need to be checked. 获取该table的总分区字段数
    List<FieldSchema> partCols = tTable.getPartitionKeys();
    if (partCols == null || (partCols.size() == 0)) {//校验没有分区的情况
      if (spec != null) {
        throw new HiveException(
            "table is not partitioned but partition spec exists: "
                + spec);
      } else {
        return true;
      }
    }

    if (spec == null) {
      throw new HiveException("partition spec is not specified");
    }

    Iterator<String> itrPsKeys = spec.keySet().iterator();
    for (FieldSchema fs: partCols) {
      if(!itrPsKeys.hasNext()) {//说明总分区比参数多,因此是可以的
        break;
      }
      
      //如果分区顺序不一致,则说明不允许
      if (!itrPsKeys.next().toLowerCase().equals(
              fs.getName().toLowerCase())) {
        ErrorPartSpec(spec, partCols);
      }
    }

    //如果table定义的分区没有内容了,而参数还依然有,则说明也有问题
    if(itrPsKeys.hasNext()) {
      ErrorPartSpec(spec, partCols);
    }

    return true;
  }

  /**
   * 打印错误信息
   * @param partSpec 解析的分区数量
   * @param parts 总的分区数量
   * @throws SemanticException 两个分区数量不匹配
   */
  private static void ErrorPartSpec(Map<String, String> partSpec,
      List<FieldSchema> parts) throws SemanticException {
    StringBuilder sb =
        new StringBuilder(
            "Partition columns in the table schema are: (");
    for (FieldSchema fs : parts) {
      sb.append(fs.getName()).append(", ");
    }
    sb.setLength(sb.length() - 2); // remove the last ", "
    sb.append("), while the partitions specified in the query are: (");

    Iterator<String> itrPsKeys = partSpec.keySet().iterator();
    while (itrPsKeys.hasNext()) {
      sb.append(itrPsKeys.next()).append(", ");
    }
    sb.setLength(sb.length() - 2); // remove the last ", "
    sb.append(").");
    throw new SemanticException(ErrorMsg.PARTSPEC_DIFFER_FROM_SCHEMA
        .getMsg(sb.toString()));
  }

  public Hive getDb() {
    return db;
  }

  public QueryProperties getQueryProperties() {
    return queryProperties;
  }

  /**
   * Construct list bucketing context.
   *
   * @param skewedColNames
   * @param skewedValues
   * @param skewedColValueLocationMaps
   * @param isStoredAsSubDirectories
   * @return
   */
  protected ListBucketingCtx constructListBucketingCtx(List<String> skewedColNames,
      List<List<String>> skewedValues, Map<List<String>, String> skewedColValueLocationMaps,
      boolean isStoredAsSubDirectories, HiveConf conf) {
    ListBucketingCtx lbCtx = new ListBucketingCtx();
    lbCtx.setSkewedColNames(skewedColNames);
    lbCtx.setSkewedColValues(skewedValues);
    lbCtx.setLbLocationMap(skewedColValueLocationMaps);
    lbCtx.setStoredAsSubDirectories(isStoredAsSubDirectories);
    lbCtx.setDefaultKey(ListBucketingPrunerUtils.HIVE_LIST_BUCKETING_DEFAULT_KEY);
    lbCtx.setDefaultDirName(ListBucketingPrunerUtils.HIVE_LIST_BUCKETING_DEFAULT_DIR_NAME);
    return lbCtx;
  }

  /**
   * Given a ASTNode, return list of values.
   *
   * use case:
   *   create table xyz list bucketed (col1) with skew (1,2,5)
   *   AST Node is for (1,2,5) 获取1 2 5三个值的集合
   * @param ast
   * @return
   */
  protected List<String> getSkewedValueFromASTNode(ASTNode ast) {
    List<String> colList = new ArrayList<String>();
    int numCh = ast.getChildCount();
    for (int i = 0; i < numCh; i++) {
      ASTNode child = (ASTNode) ast.getChild(i);
      colList.add(stripQuotes(child.getText()).toLowerCase());
    }
    return colList;
  }

  /**
   * Retrieve skewed values from ASTNode.
   *
   * @param node
   * @return
   * @throws SemanticException
   * 解析SKEWED BY (属性字符串,属性字符串) on (多组属性值集合 (xxx,xxx),(xxx,xxx),(xxx,xxx) ) 中on之后的部分
   */
  protected List<String> getSkewedValuesFromASTNode(Node node) throws SemanticException {
    List<String> result = null;
    Tree leafVNode = ((ASTNode) node).getChild(0);
    if (leafVNode == null) {
      throw new SemanticException(//No skewed values.
          ErrorMsg.SKEWED_TABLE_NO_COLUMN_VALUE.getMsg());
    } else {
      ASTNode lVAstNode = (ASTNode) leafVNode;
      if (lVAstNode.getToken().getType() != HiveParser.TOK_TABCOLVALUE) {
        throw new SemanticException(
            ErrorMsg.SKEWED_TABLE_NO_COLUMN_VALUE.getMsg());
      } else {
        result = new ArrayList<String>(getSkewedValueFromASTNode(lVAstNode));
      }
    }
    return result;
  }

  /**
   * Analyze list bucket column names
   *
   * @param skewedColNames
   * @param child
   * @return
   * @throws SemanticException
   * 获取要优化数据偏移的属性集合.Skewed by语句
   * 
tableSkewed
SKEWED BY (属性字符串,属性字符串) on (属性值集合xxx,xxx) [STORED AS DIRECTORIES]
或者SKEWED BY (属性字符串,属性字符串) on (多组属性值集合 (xxx,xxx),(xxx,xxx),(xxx,xxx) ) [STORED AS DIRECTORIES]
create table T (c1 string, c2 string) skewed by (c1) on ('x1') 表示在c1属性的值为x1的时候可能会数据发生偏移,因此在join的时候要先预估一下是否一个表的c1=x1的值能否很少,并且存储在内存中,如果是,则可以进行优化
create table T (c1 string, c2 string) skewed by (c1,c2) on (('x11','x21'),('x12','x22')) 表示在c1,c2属性的值为(x11,x21),或者(x21,x22)的时候可能会数据发生偏移,因此在join的时候要先预估一下是否一个表的(x11,x21),或者(x21,x22)的值能否很少,并且存储在内存中,如果是,则可以进行优化
返回SKEWED BY (属性字符串,属性字符串)解析后的属性集合
   */
  protected List<String> analyzeSkewedTablDDLColNames(List<String> skewedColNames, ASTNode child)
      throws SemanticException {
  Tree nNode = child.getChild(0);
    if (nNode == null) {
      throw new SemanticException(ErrorMsg.SKEWED_TABLE_NO_COLUMN_NAME.getMsg());
    } else {
      ASTNode nAstNode = (ASTNode) nNode;
      if (nAstNode.getToken().getType() != HiveParser.TOK_TABCOLNAME) {
        throw new SemanticException(ErrorMsg.SKEWED_TABLE_NO_COLUMN_NAME.getMsg());
      } else {
        skewedColNames = getColumnNames(nAstNode);
      }
    }
    return skewedColNames;
  }

  /**
   * Handle skewed values in DDL.
   *
   * It can be used by both skewed by ... on () and set skewed location ().
   *
   * @param skewedValues
   * @param child
   * @throws SemanticException
   * 为Skewed by on解析,skewedValues的每一个元素都要匹配skewed by后面的属性个数
   * 
tableSkewed
SKEWED BY (属性字符串,属性字符串) on (属性值集合xxx,xxx) [STORED AS DIRECTORIES]
或者SKEWED BY (属性字符串,属性字符串) on (多组属性值集合 (xxx,xxx),(xxx,xxx),(xxx,xxx) ) [STORED AS DIRECTORIES]
create table T (c1 string, c2 string) skewed by (c1) on ('x1') 表示在c1属性的值为x1的时候可能会数据发生偏移,因此在join的时候要先预估一下是否一个表的c1=x1的值能否很少,并且存储在内存中,如果是,则可以进行优化
create table T (c1 string, c2 string) skewed by (c1,c2) on (('x11','x21'),('x12','x22')) 表示在c1,c2属性的值为(x11,x21),或者(x21,x22)的时候可能会数据发生偏移,因此在join的时候要先预估一下是否一个表的(x11,x21),或者(x21,x22)的值能否很少,并且存储在内存中,如果是,则可以进行优化

返回on (属性值集合xxx,xxx)或者on (多组属性值集合 (xxx,xxx),(xxx,xxx),(xxx,xxx) )解析后的value值
   */
  protected void analyzeDDLSkewedValues(List<List<String>> skewedValues, ASTNode child)
      throws SemanticException {
  Tree vNode = child.getChild(1);
    if (vNode == null) {
      throw new SemanticException(ErrorMsg.SKEWED_TABLE_NO_COLUMN_VALUE.getMsg());
    }
    ASTNode vAstNode = (ASTNode) vNode;
    switch (vAstNode.getToken().getType()) {
      case HiveParser.TOK_TABCOLVALUE:
    	  //仅仅一个属性被设置了偏移,因此返回的集合元素每一个都
        for (String str : getSkewedValueFromASTNode(vAstNode)) {//循环具体的值
          List<String> sList = new ArrayList<String>(Arrays.asList(str));//将该值设置到list中
          skewedValues.add(sList);
        }
        break;
      case HiveParser.TOK_TABCOLVALUE_PAIR:
        ArrayList<Node> vLNodes = vAstNode.getChildren();
        for (Node node : vLNodes) {
          if ( ((ASTNode) node).getToken().getType() != HiveParser.TOK_TABCOLVALUES) {
            throw new SemanticException(
                ErrorMsg.SKEWED_TABLE_NO_COLUMN_VALUE.getMsg());
          } else {
            skewedValues.add(getSkewedValuesFromASTNode(node));
          }
        }
        break;
      default:
        break;
    }
  }

  /**
   * process stored as directories
   *
   * @param child
   * @return
   * 解析 STORED AS DIRECTORIES 是否被设计了
   */
  protected boolean analyzeStoredAdDirs(ASTNode child) {
    boolean storedAsDirs = false;
    if ((child.getChildCount() == 3)
        && (((ASTNode) child.getChild(2)).getToken().getType()
            == HiveParser.TOK_STOREDASDIRS)) {
      storedAsDirs = true;
    }
    return storedAsDirs;
  }

  private static void getPartExprNodeDesc(ASTNode astNode,
      Map<ASTNode, ExprNodeDesc> astExprNodeMap)
          throws SemanticException, HiveException {

    if ((astNode == null) || (astNode.getChildren() == null) ||
        (astNode.getChildren().size() <= 1)) {
      return;
    }

    TypeCheckCtx typeCheckCtx = new TypeCheckCtx(null);
    for (Node childNode : astNode.getChildren()) {
      ASTNode childASTNode = (ASTNode)childNode;

      if (childASTNode.getType() != HiveParser.TOK_PARTVAL) {
        getPartExprNodeDesc(childASTNode, astExprNodeMap);
      } else {
        if (childASTNode.getChildren().size() <= 1) {
          throw new HiveException("This is dynamic partitioning");
        }

        ASTNode partValASTChild = (ASTNode)childASTNode.getChildren().get(1);
        astExprNodeMap.put((ASTNode)childASTNode.getChildren().get(0),
            TypeCheckProcFactory.genExprNode(partValASTChild, typeCheckCtx).get(partValASTChild));
      }
    }
  }

  /**
   * 校验该table的分区情况
   * @param tbl
   * @param partSpec 分区的key-value集合
   * @param astNode
   * @param conf
   * @throws SemanticException
   */
  public static void validatePartSpec(Table tbl,
      Map<String, String> partSpec, ASTNode astNode, HiveConf conf) throws SemanticException {

    Map<ASTNode, ExprNodeDesc> astExprNodeMap = new HashMap<ASTNode, ExprNodeDesc>();

    //校验分区必须存在
    Utilities.validatePartSpec(tbl, partSpec);

    if (HiveConf.getBoolVar(conf, HiveConf.ConfVars.HIVE_TYPE_CHECK_ON_INSERT)) {
      try {
        getPartExprNodeDesc(astNode, astExprNodeMap);
      } catch (HiveException e) {
        return;
      }
      List<FieldSchema> parts = tbl.getPartitionKeys();
      Map<String, String> partCols = new HashMap<String, String>(parts.size());
      for (FieldSchema col : parts) {
        partCols.put(col.getName(), col.getType().toLowerCase());
      }
      for (Entry<ASTNode, ExprNodeDesc> astExprNodePair : astExprNodeMap.entrySet()) {

        String astKeyName = astExprNodePair.getKey().toString().toLowerCase();
        if (astExprNodePair.getKey().getType() == HiveParser.Identifier) {
          astKeyName = stripIdentifierQuotes(astKeyName);
        }
        String colType = partCols.get(astKeyName);
        ObjectInspector inputOI = astExprNodePair.getValue().getWritableObjectInspector();

        TypeInfo expectedType =
            TypeInfoUtils.getTypeInfoFromTypeString(colType);
        ObjectInspector outputOI =
            TypeInfoUtils.getStandardWritableObjectInspectorFromTypeInfo(expectedType);
        Object value = null;
        try {
          value =
              ExprNodeEvaluatorFactory.get(astExprNodePair.getValue()).
              evaluate(partSpec.get(astKeyName));
        } catch (HiveException e) {
          throw new SemanticException(e);
        }
        Object convertedValue =
          ObjectInspectorConverters.getConverter(inputOI, outputOI).convert(value);
        if (convertedValue == null) {
          throw new SemanticException(ErrorMsg.PARTITION_SPEC_TYPE_MISMATCH.format(astKeyName,
              inputOI.getTypeName(), outputOI.getTypeName()));
        }
      }
    }
  }
}
