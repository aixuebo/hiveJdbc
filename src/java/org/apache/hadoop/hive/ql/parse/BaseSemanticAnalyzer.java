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
 * �����ָ�,�ָ��еĲ�ַ��Լ������������
 */
@SuppressWarnings("deprecation")
public abstract class BaseSemanticAnalyzer {
  protected final Hive db;
  protected final HiveConf conf;
  protected List<Task<? extends Serializable>> rootTasks;//����������񼯺�
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
  //����table���Է�����Щ����
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

  //�����зָ�����Ϣ
  class RowFormatParams {
    String fieldDelim = null;//�ָ�ÿһ������
    String fieldEscape = null;//�ָ�����ʱ���õ�ת���ַ�
    String collItemDelim = null;//�ָ�����,��arr����,����map�е�ÿһ��key-value
    String mapKeyDelim = null;//�ָ�map�е�key-value
    String lineDelim = null;//�ָ�ÿ������,������\n

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

  //STORED BY xxxx�洢����, WITH SERDEPROPERTIES (key=value,key=value,key) ,ע��key=value������Ϊxxx�洢�����ṩ�Ĳ�������
  class AnalyzeCreateCommonVars {
    String serde = null;//��ʾSTORED BY���������
    //�洢��������key-value��Ϣ����
    Map<String, String> serdeProps = new HashMap<String, String>();
  }

  /**
   * ����/����ĸ�ʽ������
   */
  class StorageFormat {
    String inputFormat = null;
    String outputFormat = null;
    String storageHandler = null;

    /**
     * 
     * @param child
     * @param shared ȫ�ֵĲ�������
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
      case HiveParser.TOK_TABLEFILEFORMAT://�Զ�������/�����ʽ
        inputFormat = unescapeSQLString(child.getChild(0).getText());
        outputFormat = unescapeSQLString(child.getChild(1).getText());
        storageFormat = true;
        break;
        /**
         * child:[storageHandler����,
         	[
         	 [key,value],
         	 [key,value]
         	]
         ]
         */
      case HiveParser.TOK_STORAGEHANDLER:
        storageHandler = unescapeSQLString(child.getChild(0).getText());
        if (child.getChildCount() == 2) {// ��ʽN��child�ӽڵ�,ÿһ���ӽڵ�������С�ڵ�,��Ƶ�洢key-value
          readProps(
            (ASTNode) (child.getChild(1).getChild(0)),
            shared.serdeProps);
        }
        storageFormat = true;
        break;
      }
      return storageFormat;
    }

    //����Ĭ�ϵ��ļ�����/�������
    protected void fillDefaultStorageFormat(AnalyzeCreateCommonVars shared) {
      if ((inputFormat == null) && (storageHandler == null)) {//���û������,��ȥ����
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
   * �����ָ�,�ָ��еĲ�ַ��Լ������������
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

  //hive��sql������ɺ�,���з����׶�
  public void analyze(ASTNode ast, Context ctx) throws SemanticException {
    initCtx(ctx);
    init();
    analyzeInternal(ast);
  }

  //�Է���������ݽ���У����ȷ��
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

  //ȥ��``�ַ���Ϣ,���ַ�Ӧ�������ݿ������
  public static String stripIdentifierQuotes(String val) {
    if ((val.charAt(0) == '`' && val.charAt(val.length() - 1) == '`')) {
      val = val.substring(1, val.length() - 1);
    }
    return val;
  }

  //ȥ�������ź�˫����
  public static String stripQuotes(String val) {
    return PlanUtils.stripQuotes(val);
  }

  //ת��
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
   * ��ȡ���ݿ�ͱ�����,���ҽ�``ɾ����,����dbName + "." + tableName
   * ���߻�ȡ�����Ե�����,����������
   */
  public static String getUnescapedName(ASTNode tableOrColumnNode) {
    return getUnescapedName(tableOrColumnNode, null);
  }

  /**
   * ��ȡ���ݿ�ͱ�����,���ҽ�``ɾ����,����dbName + "." + tableName
   * ���߻�ȡ�����Ե�����,����������
   * @param tableOrColumnNode�ȴ������Ķ���
   * @param currentDatabase ��ǰʹ�õ����ݿ�
   */
  public static String getUnescapedName(ASTNode tableOrColumnNode, String currentDatabase) {
    if (tableOrColumnNode.getToken().getType() == HiveParser.TOK_TABNAME) {
      // table node
      if (tableOrColumnNode.getChildCount() == 2) {///������������
        String dbName = unescapeIdentifier(tableOrColumnNode.getChild(0).getText());
        String tableName = unescapeIdentifier(tableOrColumnNode.getChild(1).getText());
        return dbName + "." + tableName;
      }
      //����˵����������table,ʹ�õ���Ĭ�ϵ����ݿ�
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
   *   �������ݿ������
   */
  public static String getUnescapedUnqualifiedTableName(ASTNode node) {
    assert node.getChildCount() <= 2;

    if (node.getChildCount() == 2) {//˵���������ݿ�Ҳ����������,��˻�ȡ�ڶ���λ�õı�����
      node = (ASTNode) node.getChild(1);
    }

    return getUnescapedName(node);
  }


  /**
   * Remove the encapsulating "`" pair from the identifier. We allow users to
   * use "`" to escape identifier for table names, column names and aliases, in
   * case that coincide with Hive language keywords.
   * ȥ��``�ַ���Ϣ,���ַ�Ӧ�������ݿ������
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
   *   ��ʽN��child�ӽڵ�,ÿһ���ӽڵ�������С�ڵ�,��Ƶ�洢key-value
   *
   * @param mapProp property map which receives the mappings
   * ��prop�е�ÿһ�����ݶ�����key-value,����洢��map��
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
   * ת��sql
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
      //���˵������ź�˫����
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

      //�����u+4λ����,��ʾunicode,����ת����code�ַ���ƴ��
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

      //�����\+3λ����.��ʾasscii,��Ҳ���д���
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

      //ת��
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

  //��ȡ���е����ݿ����Լ���
  protected List<FieldSchema> getColumns(ASTNode ast) throws SemanticException {
    return getColumns(ast, true);
  }

  /**
   * �Զ���,Ŀǰ��֧��,��Ҫ�Լ�ʵ�ָ÷���,�̳�DDLSemanticAnalyzer�༴��
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
   * ��ȡ���ݿ����Լ���
   * lowerCase:true,��ʾ��name�������ָĳ�Сд
   */
  public static List<FieldSchema> getColumns(ASTNode ast, boolean lowerCase) throws SemanticException {
	  //FieldSchema ��ʾ���ݿ���һ������,����name�����͡�ע��
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

  //������ȡ���ݿ���������,�����������Сд��name
  //����sql:COLUMNS (column1,column2...)
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
   * �������ݿ�������װ�ɵ�Order����,����order by�Ӿ���ʹ��
   * ÿһ��Order�����������name��Сд,�Լ������Ե��������,����������
   * ����SORTED BY (column1 desc,column2 desc) 
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
   * ��ʽ�����ݿ�����Ե�����
   * @param typeNode ���ݿ��������ͽڵ�
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
      buffer.append(unescapeIdentifier(child.getChild(0).getText())).append(":");//����name
      buffer.append(getTypeStringFromAST((ASTNode) child.getChild(1)));//��ʽ�����ݿ�����Ե�����
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
    public String tableName;//table������
    public Table tableHandle;//table����
    public Map<String, String> partSpec; // has to use LinkedHashMap to enforce order�������÷�����˳��,�����������,key������,value�Ƿ���ֵ
    public Partition partHandle;
    public int numDynParts; // number of dynamic partition columns���ٸ���̬��������
    public List<Partition> partitions; // involved partitions in TableScanOperator/FileSinkOperator
    public static enum SpecType {TABLE_ONLY, STATIC_PARTITION, DYNAMIC_PARTITION};//û�з��������ݿ��,��̬����,��̬����
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
        //�Ǵ���table
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

      // get partition metadata if partition specified ���÷�������������ڷ������ԵĻ�
      if (ast.getChildCount() == 2 && ast.getToken().getType() != HiveParser.TOK_CREATETABLE) {
        childIndex = 1;
        ASTNode partspec = (ASTNode) ast.getChild(1);
        partitions = new ArrayList<Partition>();
        // partSpec is a mapping from partition column name to its value.���÷���
        partSpec = new LinkedHashMap<String, String>(partspec.getChildCount());
        //����������Ϣ
        for (int i = 0; i < partspec.getChildCount(); ++i) {
          ASTNode partspec_val = (ASTNode) partspec.getChild(i);
          String val = null;
          String colName = unescapeIdentifier(partspec_val.getChild(0).getText().toLowerCase());
          if (partspec_val.getChildCount() < 2) { // DP in the form of T partition (ds, hr) ��������������name,��û������ֵ,��������ڶ�̬����
            if (allowDynamicPartitionsSpec) {//��������̬��������
              ++numDynParts;
            } else {
              throw new SemanticException(ErrorMsg.INVALID_PARTITION
                                                       .getMsg(" - Dynamic partitions not allowed"));
            }
          } else { // in the form of T partition (ds="2010-03-03") ��̬����,��ȡ�����Զ�Ӧ�ķ���ֵ2010-03-03
            val = stripQuotes(partspec_val.getChild(1).getText());
          }
          //�������Ժͷ���ֵ
          partSpec.put(colName, val);
        }

        // check if the columns specified in the partition() clause are actually partition columns
        //У���table����ķ�����Ϣ�Ƿ�������
        validatePartSpec(tableHandle, partSpec, ast, conf);

        // check if the partition spec is valid
        if (numDynParts > 0) {//˵���Ƕ�̬����
          List<FieldSchema> parts = tableHandle.getPartitionKeys();//�ܷ����ֶ���
          int numStaPart = parts.size() - numDynParts;//�ж��پ�̬����
          //�ϸ�Ҫ��ʱ,Ҫ������Ҫ��һ����̬����,��˲�����numStaPart=0,��numStaPart=0˵��ȫ���Ƕ�̬����
          if (numStaPart == 0 &&
              conf.getVar(HiveConf.ConfVars.DYNAMICPARTITIONINGMODE).equalsIgnoreCase("strict")) {
            throw new SemanticException(ErrorMsg.DYNAMIC_PARTITION_STRICT_MODE.getMsg());
          }

          // check the partitions in partSpec be the same as defined in table schema��������һ����Ҫƥ���
          if (partSpec.keySet().size() != parts.size()) {
            ErrorPartSpec(partSpec, parts);
          }
          
          //������˳�����һ��,�Լ����������Ա���һ��,��������������һ�µľͿ�����
          Iterator<String> itrPsKeys = partSpec.keySet().iterator();
          for (FieldSchema fs: parts) {
            if (!itrPsKeys.next().toLowerCase().equals(fs.getName().toLowerCase())) {
              ErrorPartSpec(partSpec, parts);
            }
          }

          // check if static partition appear after dynamic partitions
          //У����뾲̬��������ǰ��,Ȼ�������ŵ��Ƕ�̬����
          for (FieldSchema fs: parts) {
            if (partSpec.get(fs.getName().toLowerCase()) == null) {
              if (numStaPart > 0) { // found a DP, but there exists ST as subpartition,��̬��������0,˵����̬�����Ѿ����뵽��̬����ǰ����,��˳����쳣
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
        } else {//˵���Ǿ�̬����
          try {
            if (allowPartialPartitionsSpec) {//�����з���ִ��
              partitions = db.getPartitions(tableHandle, partSpec);
            } else {
              // this doesn't create partition.����ÿһ�������Ĵ������
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
      } else {//˵�������ڷ���,�����Ǵ��������
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
   * ��ȡ������Ϣ,���շ��ص�map��,key�Ƿ����ֶ�,value�Ƿ���ֵ
   * ����PARTITION (name=value,name=value,name)
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
   * @param spec specification key-value map�ȴ�У�������,����ds='2008-04-08', min='30',���������ds, hr, min,��У�����ʧ��
   * @return true if the specification is prefix; never returns false, but throws
   * @throws HiveException
   */
  final public boolean isValidPrefixSpec(Table tTable, Map<String, String> spec)
 throws HiveException {

    // TODO - types need to be checked. ��ȡ��table���ܷ����ֶ���
    List<FieldSchema> partCols = tTable.getPartitionKeys();
    if (partCols == null || (partCols.size() == 0)) {//У��û�з��������
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
      if(!itrPsKeys.hasNext()) {//˵���ܷ����Ȳ�����,����ǿ��Ե�
        break;
      }
      
      //�������˳��һ��,��˵��������
      if (!itrPsKeys.next().toLowerCase().equals(
              fs.getName().toLowerCase())) {
        ErrorPartSpec(spec, partCols);
      }
    }

    //���table����ķ���û��������,����������Ȼ��,��˵��Ҳ������
    if(itrPsKeys.hasNext()) {
      ErrorPartSpec(spec, partCols);
    }

    return true;
  }

  /**
   * ��ӡ������Ϣ
   * @param partSpec �����ķ�������
   * @param parts �ܵķ�������
   * @throws SemanticException ��������������ƥ��
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
   *   AST Node is for (1,2,5) ��ȡ1 2 5����ֵ�ļ���
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
   * ����SKEWED BY (�����ַ���,�����ַ���) on (��������ֵ���� (xxx,xxx),(xxx,xxx),(xxx,xxx) ) ��on֮��Ĳ���
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
   * ��ȡҪ�Ż�����ƫ�Ƶ����Լ���.Skewed by���
   * 
tableSkewed
SKEWED BY (�����ַ���,�����ַ���) on (����ֵ����xxx,xxx) [STORED AS DIRECTORIES]
����SKEWED BY (�����ַ���,�����ַ���) on (��������ֵ���� (xxx,xxx),(xxx,xxx),(xxx,xxx) ) [STORED AS DIRECTORIES]
create table T (c1 string, c2 string) skewed by (c1) on ('x1') ��ʾ��c1���Ե�ֵΪx1��ʱ����ܻ����ݷ���ƫ��,�����join��ʱ��Ҫ��Ԥ��һ���Ƿ�һ�����c1=x1��ֵ�ܷ����,���Ҵ洢���ڴ���,�����,����Խ����Ż�
create table T (c1 string, c2 string) skewed by (c1,c2) on (('x11','x21'),('x12','x22')) ��ʾ��c1,c2���Ե�ֵΪ(x11,x21),����(x21,x22)��ʱ����ܻ����ݷ���ƫ��,�����join��ʱ��Ҫ��Ԥ��һ���Ƿ�һ�����(x11,x21),����(x21,x22)��ֵ�ܷ����,���Ҵ洢���ڴ���,�����,����Խ����Ż�
����SKEWED BY (�����ַ���,�����ַ���)����������Լ���
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
   * ΪSkewed by on����,skewedValues��ÿһ��Ԫ�ض�Ҫƥ��skewed by��������Ը���
   * 
tableSkewed
SKEWED BY (�����ַ���,�����ַ���) on (����ֵ����xxx,xxx) [STORED AS DIRECTORIES]
����SKEWED BY (�����ַ���,�����ַ���) on (��������ֵ���� (xxx,xxx),(xxx,xxx),(xxx,xxx) ) [STORED AS DIRECTORIES]
create table T (c1 string, c2 string) skewed by (c1) on ('x1') ��ʾ��c1���Ե�ֵΪx1��ʱ����ܻ����ݷ���ƫ��,�����join��ʱ��Ҫ��Ԥ��һ���Ƿ�һ�����c1=x1��ֵ�ܷ����,���Ҵ洢���ڴ���,�����,����Խ����Ż�
create table T (c1 string, c2 string) skewed by (c1,c2) on (('x11','x21'),('x12','x22')) ��ʾ��c1,c2���Ե�ֵΪ(x11,x21),����(x21,x22)��ʱ����ܻ����ݷ���ƫ��,�����join��ʱ��Ҫ��Ԥ��һ���Ƿ�һ�����(x11,x21),����(x21,x22)��ֵ�ܷ����,���Ҵ洢���ڴ���,�����,����Խ����Ż�

����on (����ֵ����xxx,xxx)����on (��������ֵ���� (xxx,xxx),(xxx,xxx),(xxx,xxx) )�������valueֵ
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
    	  //����һ�����Ա�������ƫ��,��˷��صļ���Ԫ��ÿһ����
        for (String str : getSkewedValueFromASTNode(vAstNode)) {//ѭ�������ֵ
          List<String> sList = new ArrayList<String>(Arrays.asList(str));//����ֵ���õ�list��
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
   * ���� STORED AS DIRECTORIES �Ƿ������
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
   * У���table�ķ������
   * @param tbl
   * @param partSpec ������key-value����
   * @param astNode
   * @param conf
   * @throws SemanticException
   */
  public static void validatePartSpec(Table tbl,
      Map<String, String> partSpec, ASTNode astNode, HiveConf conf) throws SemanticException {

    Map<ASTNode, ExprNodeDesc> astExprNodeMap = new HashMap<ASTNode, ExprNodeDesc>();

    //У������������
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
