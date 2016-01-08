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

import static org.apache.hadoop.hive.ql.parse.HiveParser.TOK_DATABASELOCATION;
import static org.apache.hadoop.hive.ql.parse.HiveParser.TOK_DATABASEPROPERTIES;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.conf.HiveConf.ConfVars;
import org.apache.hadoop.hive.metastore.MetaStoreUtils;
import org.apache.hadoop.hive.metastore.TableType;
import org.apache.hadoop.hive.metastore.Warehouse;
import org.apache.hadoop.hive.metastore.api.FieldSchema;
import org.apache.hadoop.hive.metastore.api.Index;
import org.apache.hadoop.hive.metastore.api.MetaException;
import org.apache.hadoop.hive.metastore.api.Order;
import org.apache.hadoop.hive.metastore.api.PrincipalType;
import org.apache.hadoop.hive.metastore.api.SkewedInfo;
import org.apache.hadoop.hive.ql.Driver;
import org.apache.hadoop.hive.ql.ErrorMsg;
import org.apache.hadoop.hive.ql.exec.ArchiveUtils;
import org.apache.hadoop.hive.ql.exec.FetchTask;
import org.apache.hadoop.hive.ql.exec.Task;
import org.apache.hadoop.hive.ql.exec.TaskFactory;
import org.apache.hadoop.hive.ql.exec.Utilities;
import org.apache.hadoop.hive.ql.hooks.ReadEntity;
import org.apache.hadoop.hive.ql.hooks.WriteEntity;
import org.apache.hadoop.hive.ql.index.HiveIndex;
import org.apache.hadoop.hive.ql.index.HiveIndex.IndexType;
import org.apache.hadoop.hive.ql.index.HiveIndexHandler;
import org.apache.hadoop.hive.ql.io.IgnoreKeyTextOutputFormat;
import org.apache.hadoop.hive.ql.io.RCFileInputFormat;
import org.apache.hadoop.hive.ql.lib.Node;
import org.apache.hadoop.hive.ql.metadata.Hive;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.metadata.HiveUtils;
import org.apache.hadoop.hive.ql.metadata.Partition;
import org.apache.hadoop.hive.ql.metadata.Table;
import org.apache.hadoop.hive.ql.plan.AddPartitionDesc;
import org.apache.hadoop.hive.ql.plan.AlterDatabaseDesc;
import org.apache.hadoop.hive.ql.plan.AlterIndexDesc;
import org.apache.hadoop.hive.ql.plan.AlterIndexDesc.AlterIndexTypes;
import org.apache.hadoop.hive.ql.plan.AlterTableAlterPartDesc;
import org.apache.hadoop.hive.ql.plan.AlterTableDesc;
import org.apache.hadoop.hive.ql.plan.AlterTableDesc.AlterTableTypes;
import org.apache.hadoop.hive.ql.plan.AlterTableExchangePartition;
import org.apache.hadoop.hive.ql.plan.AlterTableSimpleDesc;
import org.apache.hadoop.hive.ql.plan.CreateDatabaseDesc;
import org.apache.hadoop.hive.ql.plan.CreateIndexDesc;
import org.apache.hadoop.hive.ql.plan.DDLWork;
import org.apache.hadoop.hive.ql.plan.DescDatabaseDesc;
import org.apache.hadoop.hive.ql.plan.DescFunctionDesc;
import org.apache.hadoop.hive.ql.plan.DescTableDesc;
import org.apache.hadoop.hive.ql.plan.DropDatabaseDesc;
import org.apache.hadoop.hive.ql.plan.DropIndexDesc;
import org.apache.hadoop.hive.ql.plan.DropTableDesc;
import org.apache.hadoop.hive.ql.plan.FetchWork;
import org.apache.hadoop.hive.ql.plan.GrantDesc;
import org.apache.hadoop.hive.ql.plan.GrantRevokeRoleDDL;
import org.apache.hadoop.hive.ql.plan.ListBucketingCtx;
import org.apache.hadoop.hive.ql.plan.LoadTableDesc;
import org.apache.hadoop.hive.ql.plan.LockTableDesc;
import org.apache.hadoop.hive.ql.plan.MoveWork;
import org.apache.hadoop.hive.ql.plan.MsckDesc;
import org.apache.hadoop.hive.ql.plan.PartitionSpec;
import org.apache.hadoop.hive.ql.plan.PlanUtils;
import org.apache.hadoop.hive.ql.plan.PrincipalDesc;
import org.apache.hadoop.hive.ql.plan.PrivilegeDesc;
import org.apache.hadoop.hive.ql.plan.PrivilegeObjectDesc;
import org.apache.hadoop.hive.ql.plan.RenamePartitionDesc;
import org.apache.hadoop.hive.ql.plan.RevokeDesc;
import org.apache.hadoop.hive.ql.plan.RoleDDLDesc;
import org.apache.hadoop.hive.ql.plan.ShowColumnsDesc;
import org.apache.hadoop.hive.ql.plan.ShowCreateTableDesc;
import org.apache.hadoop.hive.ql.plan.ShowDatabasesDesc;
import org.apache.hadoop.hive.ql.plan.ShowFunctionsDesc;
import org.apache.hadoop.hive.ql.plan.ShowGrantDesc;
import org.apache.hadoop.hive.ql.plan.ShowIndexesDesc;
import org.apache.hadoop.hive.ql.plan.ShowLocksDesc;
import org.apache.hadoop.hive.ql.plan.ShowPartitionsDesc;
import org.apache.hadoop.hive.ql.plan.ShowTableStatusDesc;
import org.apache.hadoop.hive.ql.plan.ShowTablesDesc;
import org.apache.hadoop.hive.ql.plan.ShowTblPropertiesDesc;
import org.apache.hadoop.hive.ql.plan.StatsWork;
import org.apache.hadoop.hive.ql.plan.SwitchDatabaseDesc;
import org.apache.hadoop.hive.ql.plan.TableDesc;
import org.apache.hadoop.hive.ql.plan.TruncateTableDesc;
import org.apache.hadoop.hive.ql.plan.UnlockTableDesc;
import org.apache.hadoop.hive.ql.security.authorization.Privilege;
import org.apache.hadoop.hive.ql.security.authorization.PrivilegeRegistry;
import org.apache.hadoop.hive.ql.session.SessionState;
import org.apache.hadoop.hive.serde.serdeConstants;
import org.apache.hadoop.hive.serde2.lazy.LazySimpleSerDe;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector.PrimitiveCategory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorUtils;
import org.apache.hadoop.hive.serde2.typeinfo.VarcharTypeParams;
import org.apache.hadoop.mapred.InputFormat;
import org.apache.hadoop.mapred.TextInputFormat;

/**
 * DDLSemanticAnalyzer.
 *
 */
public class DDLSemanticAnalyzer extends BaseSemanticAnalyzer {
  private static final Log LOG = LogFactory.getLog(DDLSemanticAnalyzer.class);
  //属性所属类型映射
  private static final Map<Integer, String> TokenToTypeName = new HashMap<Integer, String>();

  private final Set<String> reservedPartitionValues;//预先保留的partition名字集合,这些名字的partition是不允许被命名的
  static {
    TokenToTypeName.put(HiveParser.TOK_BOOLEAN, serdeConstants.BOOLEAN_TYPE_NAME);
    TokenToTypeName.put(HiveParser.TOK_TINYINT, serdeConstants.TINYINT_TYPE_NAME);
    TokenToTypeName.put(HiveParser.TOK_SMALLINT, serdeConstants.SMALLINT_TYPE_NAME);
    TokenToTypeName.put(HiveParser.TOK_INT, serdeConstants.INT_TYPE_NAME);
    TokenToTypeName.put(HiveParser.TOK_BIGINT, serdeConstants.BIGINT_TYPE_NAME);
    TokenToTypeName.put(HiveParser.TOK_FLOAT, serdeConstants.FLOAT_TYPE_NAME);
    TokenToTypeName.put(HiveParser.TOK_DOUBLE, serdeConstants.DOUBLE_TYPE_NAME);
    TokenToTypeName.put(HiveParser.TOK_STRING, serdeConstants.STRING_TYPE_NAME);
    TokenToTypeName.put(HiveParser.TOK_VARCHAR, serdeConstants.VARCHAR_TYPE_NAME);
    TokenToTypeName.put(HiveParser.TOK_BINARY, serdeConstants.BINARY_TYPE_NAME);
    TokenToTypeName.put(HiveParser.TOK_DATE, serdeConstants.DATE_TYPE_NAME);
    TokenToTypeName.put(HiveParser.TOK_DATETIME, serdeConstants.DATETIME_TYPE_NAME);
    TokenToTypeName.put(HiveParser.TOK_TIMESTAMP, serdeConstants.TIMESTAMP_TYPE_NAME);
    TokenToTypeName.put(HiveParser.TOK_DECIMAL, serdeConstants.DECIMAL_TYPE_NAME);
  }

  /**
   * 返回该field属性节点对应的类型值,例如String类型被返回
   */
  public static String getTypeName(ASTNode node) throws SemanticException {
    int token = node.getType();
    String typeName;

    // datetime type isn't currently supported 当前不支持datetime类型
    if (token == HiveParser.TOK_DATETIME) {
      throw new SemanticException(ErrorMsg.UNSUPPORTED_TYPE.getMsg());
    }

    switch (token) {
    case HiveParser.TOK_VARCHAR://记录varchar的字节长度
      PrimitiveCategory primitiveCategory = PrimitiveCategory.VARCHAR;
      typeName = TokenToTypeName.get(token);
      //解析verchar节点的长度限制,返回VarcharTypeParams对象
      VarcharTypeParams varcharParams = ParseUtils.getVarcharParams(typeName, node);
      typeName = PrimitiveObjectInspectorUtils.getTypeEntryFromTypeSpecs(
          primitiveCategory, varcharParams).toString();
      break;
    default:
      typeName = TokenToTypeName.get(token);
    }
    return typeName;
  }

  //描述一个数据库表名字以及对应的partition信息集合
  static class TablePartition {
    String tableName;
    HashMap<String, String> partSpec = null;

    public TablePartition() {
    }

    /**
     * 解析tablename和partition集合
     * @param tblPart
     * @throws SemanticException
     */
    public TablePartition(ASTNode tblPart) throws SemanticException {
      tableName = unescapeIdentifier(tblPart.getChild(0).getText());
      if (tblPart.getChildCount() > 1) {
        ASTNode part = (ASTNode) tblPart.getChild(1);
        if (part.getToken().getType() == HiveParser.TOK_PARTSPEC) {
          this.partSpec = DDLSemanticAnalyzer.getPartSpec(part);
        }
      }
    }
  }

  public DDLSemanticAnalyzer(HiveConf conf) throws SemanticException {
    super(conf);
    //设置预先保留的partition名字集合,这些名字的partition是不允许被命名的
    reservedPartitionValues = new HashSet<String>();
    // Partition can't have this name 不允许partition的命名规则
    reservedPartitionValues.add(HiveConf.getVar(conf, ConfVars.DEFAULTPARTITIONNAME));
    reservedPartitionValues.add(HiveConf.getVar(conf, ConfVars.DEFAULT_ZOOKEEPER_PARTITION_NAME));
    // Partition value can't end in this suffix partition不能使用的后缀
    reservedPartitionValues.add(HiveConf.getVar(conf, ConfVars.METASTORE_INT_ORIGINAL));
    reservedPartitionValues.add(HiveConf.getVar(conf, ConfVars.METASTORE_INT_ARCHIVED));
    reservedPartitionValues.add(HiveConf.getVar(conf, ConfVars.METASTORE_INT_EXTRACTED));
  }

  @Override
  public void analyzeInternal(ASTNode ast) throws SemanticException {

    switch (ast.getToken().getType()) {
    case HiveParser.TOK_ALTERTABLE_PARTITION: {
    	/**
    	 * 解析alterTblPartitionStatement
		a.tableName [PARTITION (name=value,name=value,name)] alterTblPartitionStatementSuffix
		b.String PARTITION COLUMN (columnNameType)
其中:alterTblPartitionStatementSuffix
一、alterStatementSuffixLocation
SET LOCATION xxxx
二、alterStatementSuffixFileFormat
1.SET FILEFORMAT SEQUENCEFILE
2.SET FILEFORMAT TEXTFILE
3.SET FILEFORMAT RCFILE
4.SET FILEFORMAT ORCFILE
5.SET FILEFORMAT INPUTFORMAT string OUTPUTFORMAT string [INPUTDRIVER string OUTPUTDRIVER string]
6.SET FILEFORMAT xxxx 属于TOK_FILEFORMAT_GENERIC类型自定义格式
三、alterStatementSuffixProtectMode
1.ENABLE OFFLINE
2.ENABLE NO_DROP [CASCADE]
3.ENABLE READONLY
4.DISABLE OFFLINE
5.DISABLE NO_DROP [CASCADE]
6.DISABLE READONLY
四、alterStatementSuffixMergeFiles
CONCATENATE
五、alterStatementSuffixSerdeProperties
1.SET SERDE string [WITH SERDEPROPERTIES(key=value,key=value)]
2.SET SERDEPROPERTIES (key=value,key=value)
六、alterStatementSuffixRenamePart
RENAME TO PARTITION (name=value,name=value,name)
七、alterStatementSuffixBucketNum
INTO number BUCKETS
八、alterTblPartitionStatementSuffixSkewedLocation 
SET SKEWED LOCATION (key=value,key=value)
九、alterStatementSuffixClusterbySortby
1.NOT CLUSTERED
2.NOT SORTED
3.CLUSTERED BY (column1,column2) [SORTED BY (column1 desc,column2 desc)] into Number BUCKETS
    	 */
    
      /**
       * 解析tableName [PARTITION (name=value,name=value,name)]
       */
      ASTNode tablePart = (ASTNode) ast.getChild(0);
      TablePartition tblPart = new TablePartition(tablePart);
      String tableName = tblPart.tableName;
      HashMap<String, String> partSpec = tblPart.partSpec;
      ast = (ASTNode) ast.getChild(1);
      if (ast.getToken().getType() == HiveParser.TOK_ALTERTABLE_FILEFORMAT) {//设置文件格式
    	 /**
    	  * 设置文件格式
1.SET FILEFORMAT SEQUENCEFILE
2.SET FILEFORMAT TEXTFILE
3.SET FILEFORMAT RCFILE
4.SET FILEFORMAT ORCFILE
5.SET FILEFORMAT INPUTFORMAT string OUTPUTFORMAT string [INPUTDRIVER string OUTPUTDRIVER string]
6.SET FILEFORMAT xxxx 属于TOK_FILEFORMAT_GENERIC类型自定义格式
    	  */
        analyzeAlterTableFileFormat(ast, tableName, partSpec);
      }else if (ast.getToken().getType() == HiveParser.TOK_ALTERTABLE_LOCATION) {
        analyzeAlterTableLocation(ast, tableName, partSpec);//解析SET LOCATION xxxx
      }else if (ast.getToken().getType() == HiveParser.TOK_ALTERTABLE_SERIALIZER) {
    	  /**
设置存储的方式是csv、json、还是protobuffer等等吧
格式 SET SERDE "serde_class_name" [WITH SERDEPROPERTIES(key=value,key=value)]
    	   */
        analyzeAlterTableSerde(ast, tableName, partSpec);
      } else if (ast.getToken().getType() == HiveParser.TOK_ALTERTABLE_SERDEPROPERTIES) {
    	  /**
设置存储的方式是csv、json、还是protobuffer等等吧
格式 SET SERDEPROPERTIES (key=value,key=value)
    	   */
        analyzeAlterTableSerdeProps(ast, tableName, partSpec);
      } else if (ast.getToken().getType() == HiveParser.TOK_ALTERTABLE_RENAMEPART) {
        /**
         * alterStatementSuffixRenamePart
         * RENAME TO PARTITION (name=value,name=value,name)
         */
    	  analyzeAlterTableRenamePart(ast, tableName, partSpec);
      } else if (ast.getToken().getType() == HiveParser.TOK_ALTERTBLPART_SKEWED_LOCATION) {
    	/**
    	 * a.SET SKEWED LOCATION (key=value,key=value)
b.SET SKEWED LOCATION ((key1,key2)=value,(key1,key2)=value)
    	 */
        analyzeAlterTableSkewedLocation(ast, tableName, partSpec);
      } else if (ast.getToken().getType() == HiveParser.TOK_TABLEBUCKETS) {
    	  /**
alterStatementSuffixBucketNum
 INTO number BUCKETS
    	   */
        analyzeAlterTableBucketNum(ast, tableName, partSpec);
      } else if (ast.getToken().getType() == HiveParser.TOK_ALTERTABLE_CLUSTER_SORT) {
    	  /**
    	   * alterStatementSuffixClusterbySortby格式:
1.NOT CLUSTERED
2.NOT SORTED
3.CLUSTERED BY (column1,column2) [SORTED BY (column1 desc,column2 desc)] into Number BUCKETS
    	   */
        analyzeAlterTableClusterSort(ast, tableName, partSpec);
      }
      break;
    }
    case HiveParser.TOK_DROPTABLE:
      analyzeDropTable(ast, false);
      break;
    case HiveParser.TOK_TRUNCATETABLE:
      analyzeTruncateTable(ast);
      break;
    case HiveParser.TOK_CREATEINDEX:
      analyzeCreateIndex(ast);
      break;
    case HiveParser.TOK_DROPINDEX:
      //DROP INDEX [IF EXISTS] "indexName" ON tableName
      analyzeDropIndex(ast);
      break;
    case HiveParser.TOK_DESCTABLE:
      //DESCRIBE | DESC [FORMATTED | EXTENDED | PRETTY] .($ELEM$ | $KEY$ | $VALUE$ | xxx ) [PARTITION (name=value,name=value,name)] 描述表
      ctx.setResFile(new Path(ctx.getLocalTmpFileURI()));
      analyzeDescribeTable(ast);
      break;
    case HiveParser.TOK_SHOWDATABASES:
      ctx.setResFile(new Path(ctx.getLocalTmpFileURI()));
      analyzeShowDatabases(ast);
      break;
    case HiveParser.TOK_SHOWTABLES:
      ctx.setResFile(new Path(ctx.getLocalTmpFileURI()));
      analyzeShowTables(ast);
      break;
    case HiveParser.TOK_SHOWCOLUMNS:
      ctx.setResFile(new Path(ctx.getLocalTmpFileURI()));
      analyzeShowColumns(ast);
      break;
    case HiveParser.TOK_SHOW_TABLESTATUS:
      ctx.setResFile(new Path(ctx.getLocalTmpFileURI()));
      analyzeShowTableStatus(ast);
      break;
    case HiveParser.TOK_SHOW_TBLPROPERTIES:
      ctx.setResFile(new Path(ctx.getLocalTmpFileURI()));
      analyzeShowTableProperties(ast);
      break;
    case HiveParser.TOK_SHOWFUNCTIONS:
      ctx.setResFile(new Path(ctx.getLocalTmpFileURI()));
      analyzeShowFunctions(ast);
      break;
    case HiveParser.TOK_SHOWLOCKS:
      ctx.setResFile(new Path(ctx.getLocalTmpFileURI()));
      analyzeShowLocks(ast);
      break;
    case HiveParser.TOK_DESCFUNCTION:
      ctx.setResFile(new Path(ctx.getLocalTmpFileURI()));
      analyzeDescFunction(ast);
      break;
    case HiveParser.TOK_DESCDATABASE:
      ctx.setResFile(new Path(ctx.getLocalTmpFileURI()));
      analyzeDescDatabase(ast);
      break;
    case HiveParser.TOK_MSCK:
      ctx.setResFile(new Path(ctx.getLocalTmpFileURI()));
      analyzeMetastoreCheck(ast);
      break;
    case HiveParser.TOK_DROPVIEW:
      analyzeDropTable(ast, true);
      break;
    case HiveParser.TOK_ALTERVIEW_PROPERTIES:
    	//String SET TBLPROPERTIES (keyValueProperty,keyValueProperty) 设置视图的属性
      analyzeAlterTableProps(ast, true, false);
      break;
    case HiveParser.TOK_DROPVIEW_PROPERTIES:
    	//String UNSET TBLPROPERTIES [IF Exists](keyValueProperty,keyValueProperty)设置视图的属性
      analyzeAlterTableProps(ast, true, true);
      break;
    case HiveParser.TOK_ALTERVIEW_ADDPARTS:
      // for ALTER VIEW ADD PARTITION, we wrapped the ADD to discriminate
      // view from table; unwrap it now
      analyzeAlterTableAddParts((ASTNode) ast.getChild(0), true);
      break;
    case HiveParser.TOK_ALTERVIEW_DROPPARTS:
      // for ALTER VIEW DROP PARTITION, we wrapped the DROP to discriminate
      // view from table; unwrap it now
      analyzeAlterTableDropParts((ASTNode) ast.getChild(0), true);
      break;
    case HiveParser.TOK_ALTERVIEW_RENAME:
      // for ALTER VIEW RENAME, we wrapped the RENAME to discriminate
      // view from table; unwrap it now
      analyzeAlterTableRename(((ASTNode) ast.getChild(0)), true);
      break;
    case HiveParser.TOK_ALTERTABLE_RENAME:
      analyzeAlterTableRename(ast, false);
      break;
    case HiveParser.TOK_ALTERTABLE_TOUCH:
      analyzeAlterTableTouch(ast);
      break;
    case HiveParser.TOK_ALTERTABLE_ARCHIVE:
      analyzeAlterTableArchive(ast, false);
      break;
    case HiveParser.TOK_ALTERTABLE_UNARCHIVE:
      analyzeAlterTableArchive(ast, true);
      break;
    case HiveParser.TOK_ALTERTABLE_ADDCOLS:
      analyzeAlterTableModifyCols(ast, AlterTableTypes.ADDCOLS);
      break;
    case HiveParser.TOK_ALTERTABLE_REPLACECOLS:
      analyzeAlterTableModifyCols(ast, AlterTableTypes.REPLACECOLS);
      break;
    case HiveParser.TOK_ALTERTABLE_RENAMECOL:
      analyzeAlterTableRenameCol(ast);
      break;
    case HiveParser.TOK_ALTERTABLE_ADDPARTS:
      analyzeAlterTableAddParts(ast, false);
      break;
    case HiveParser.TOK_ALTERTABLE_DROPPARTS:
      analyzeAlterTableDropParts(ast, false);
      break;
    case HiveParser.TOK_ALTERTABLE_PROPERTIES:
    	//String SET TBLPROPERTIES (keyValueProperty,keyValueProperty) 设置数据表的属性信息
      analyzeAlterTableProps(ast, false, false);
      break;
    case HiveParser.TOK_DROPTABLE_PROPERTIES:
    	//String UNSET TBLPROPERTIES [IF Exists](keyValueProperty,keyValueProperty) 取消数据表的属性信息
      analyzeAlterTableProps(ast, false, true);
      break;
    case HiveParser.TOK_ALTERINDEX_REBUILD:
      //"indexName" ON "tableName" [PARTITION (name=value,name=value,name)] REBUILD
      analyzeAlterIndexRebuild(ast);
      break;
    case HiveParser.TOK_ALTERINDEX_PROPERTIES:
      //"indexName" ON "tableName" [PARTITION (name=value,name=value,name)] SET IDXPROPERTIES (key=value,key=value)
      analyzeAlterIndexProps(ast);
      break;
    case HiveParser.TOK_SHOWPARTITIONS:
    	//SHOW PARTITIONS xxx PARTITION (name=value,name=value,name)
      ctx.setResFile(new Path(ctx.getLocalTmpFileURI()));
      analyzeShowPartitions(ast);
      break;
    case HiveParser.TOK_SHOW_CREATETABLE:
      ctx.setResFile(new Path(ctx.getLocalTmpFileURI()));
      analyzeShowCreateTable(ast);
      break;
    case HiveParser.TOK_SHOWINDEXES:
      ctx.setResFile(new Path(ctx.getLocalTmpFileURI()));
      analyzeShowIndexes(ast);
      break;
    case HiveParser.TOK_LOCKTABLE:
      analyzeLockTable(ast);
      break;
    case HiveParser.TOK_UNLOCKTABLE:
      analyzeUnlockTable(ast);
      break;
    case HiveParser.TOK_CREATEDATABASE:
      analyzeCreateDatabase(ast);
      break;
    case HiveParser.TOK_DROPDATABASE:
      analyzeDropDatabase(ast);
      break;
    case HiveParser.TOK_SWITCHDATABASE:
      analyzeSwitchDatabase(ast);
      break;
    case HiveParser.TOK_ALTERDATABASE_PROPERTIES:
      analyzeAlterDatabase(ast);
      break;
    case HiveParser.TOK_CREATEROLE:
      analyzeCreateRole(ast);
      break;
    case HiveParser.TOK_DROPROLE:
      analyzeDropRole(ast);
      break;
    case HiveParser.TOK_SHOW_ROLE_GRANT:
      ctx.setResFile(new Path(ctx.getLocalTmpFileURI()));
      analyzeShowRoleGrant(ast);
      break;
    case HiveParser.TOK_GRANT_ROLE:
      analyzeGrantRevokeRole(true, ast);
      break;
    case HiveParser.TOK_REVOKE_ROLE:
      analyzeGrantRevokeRole(false, ast);
      break;
    case HiveParser.TOK_GRANT:
      analyzeGrant(ast);
      break;
    case HiveParser.TOK_SHOW_GRANT:
      ctx.setResFile(new Path(ctx.getLocalTmpFileURI()));
      analyzeShowGrant(ast);
      break;
    case HiveParser.TOK_REVOKE:
      analyzeRevoke(ast);
      break;
    case HiveParser.TOK_ALTERTABLE_SKEWED:
    	/**
alterStatementSuffixSkewedby
a.String tableSkewed
b.String NOT SKEWED
c.String NOT STORED AS DIRECTORIES
    	 */
      analyzeAltertableSkewedby(ast);
      break;
   case HiveParser.TOK_EXCHANGEPARTITION:
      analyzeExchangePartition(ast);
      break;
    default:
      throw new SemanticException("Unsupported command.");
    }
  }

  /**
   * GRANT ROLE String,... TO principalSpecification时,第一个参数为true,表示将角色集合授权给某些user、group、role
   * REVOKE ROLE String,... FROM principalSpecification时,第一个参数为false,表示将角色集合撤销一些,撤销的是:user、group、role
   * 授权/撤回角色
   */
  private void analyzeGrantRevokeRole(boolean grant, ASTNode ast) {
	  
	//解析USER | GROUP | ROLE String,USER | GROUP | ROLE String...集合
    List<PrincipalDesc> principalDesc = analyzePrincipalListDef(
        (ASTNode) ast.getChild(0));
    
    //解析String,String..角色集合
    List<String> roles = new ArrayList<String>();
    for (int i = 1; i < ast.getChildCount(); i++) {
      roles.add(unescapeIdentifier(ast.getChild(i).getText()));
    }
    
    String roleOwnerName = "";
    if (SessionState.get() != null
        && SessionState.get().getAuthenticator() != null) {
      roleOwnerName = SessionState.get().getAuthenticator().getUserName();
    }
    GrantRevokeRoleDDL grantRevokeRoleDDL = new GrantRevokeRoleDDL(grant,
        roles, principalDesc, roleOwnerName, PrincipalType.USER, true);
    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
        grantRevokeRoleDDL), conf));
  }

  private void analyzeShowGrant(ASTNode ast) throws SemanticException {
    PrivilegeObjectDesc privHiveObj = null;

    ASTNode principal = (ASTNode) ast.getChild(0);
    PrincipalType type = PrincipalType.USER;
    switch (principal.getType()) {
    case HiveParser.TOK_USER:
      type = PrincipalType.USER;
      break;
    case HiveParser.TOK_GROUP:
      type = PrincipalType.GROUP;
      break;
    case HiveParser.TOK_ROLE:
      type = PrincipalType.ROLE;
      break;
    }
    String principalName = unescapeIdentifier(principal.getChild(0).getText());
    PrincipalDesc principalDesc = new PrincipalDesc(principalName, type);
    List<String> cols = null;
    if (ast.getChildCount() > 1) {
      ASTNode child = (ASTNode) ast.getChild(1);
      if (child.getToken().getType() == HiveParser.TOK_PRIV_OBJECT_COL) {
        privHiveObj = new PrivilegeObjectDesc();
        privHiveObj.setObject(unescapeIdentifier(child.getChild(0).getText()));
        if (child.getChildCount() > 1) {
          for (int i = 1; i < child.getChildCount(); i++) {
            ASTNode grandChild = (ASTNode) child.getChild(i);
            if (grandChild.getToken().getType() == HiveParser.TOK_PARTSPEC) {
              privHiveObj.setPartSpec(DDLSemanticAnalyzer.getPartSpec(grandChild));
            } else if (grandChild.getToken().getType() == HiveParser.TOK_TABCOLNAME) {
              cols = getColumnNames((ASTNode) grandChild);
            } else {
              privHiveObj.setTable(child.getChild(i) != null);
            }
          }
        }
      }
    }

    if (privHiveObj == null && cols != null) {
      throw new SemanticException(
          "For user-level privileges, column sets should be null. columns="
              + cols.toString());
    }

    ShowGrantDesc showGrant = new ShowGrantDesc(ctx.getResFile().toString(),
        principalDesc, privHiveObj, cols);
    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
        showGrant), conf));
  }

  private void analyzeGrant(ASTNode ast) throws SemanticException {
    List<PrivilegeDesc> privilegeDesc = analyzePrivilegeListDef(
        (ASTNode) ast.getChild(0));
    List<PrincipalDesc> principalDesc = analyzePrincipalListDef(
        (ASTNode) ast.getChild(1));
    boolean grantOption = false;
    PrivilegeObjectDesc privilegeObj = null;

    if (ast.getChildCount() > 2) {
      for (int i = 2; i < ast.getChildCount(); i++) {
        ASTNode astChild = (ASTNode) ast.getChild(i);
        if (astChild.getType() == HiveParser.TOK_GRANT_WITH_OPTION) {
          grantOption = true;
        } else if (astChild.getType() == HiveParser.TOK_PRIV_OBJECT) {
          privilegeObj = analyzePrivilegeObject(astChild, getOutputs());
        }
      }
    }

    String userName = null;
    if (SessionState.get() != null
        && SessionState.get().getAuthenticator() != null) {
      userName = SessionState.get().getAuthenticator().getUserName();
    }

    GrantDesc grantDesc = new GrantDesc(privilegeObj, privilegeDesc,
        principalDesc, userName, PrincipalType.USER, grantOption);
    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
        grantDesc), conf));
  }

  private void analyzeRevoke(ASTNode ast) throws SemanticException {
    List<PrivilegeDesc> privilegeDesc = analyzePrivilegeListDef(
        (ASTNode) ast.getChild(0));
    List<PrincipalDesc> principalDesc = analyzePrincipalListDef(
        (ASTNode) ast.getChild(1));
    PrivilegeObjectDesc hiveObj = null;
    if (ast.getChildCount() > 2) {
      ASTNode astChild = (ASTNode) ast.getChild(2);
      hiveObj = analyzePrivilegeObject(astChild, getOutputs());
    }

    RevokeDesc revokeDesc = new RevokeDesc(privilegeDesc, principalDesc, hiveObj);
    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
        revokeDesc), conf));
  }

  private PrivilegeObjectDesc analyzePrivilegeObject(ASTNode ast,
      HashSet<WriteEntity> outputs)
      throws SemanticException {
    PrivilegeObjectDesc subject = new PrivilegeObjectDesc();
    subject.setObject(unescapeIdentifier(ast.getChild(0).getText()));
    if (ast.getChildCount() > 1) {
      for (int i = 0; i < ast.getChildCount(); i++) {
        ASTNode astChild = (ASTNode) ast.getChild(i);
        if (astChild.getToken().getType() == HiveParser.TOK_PARTSPEC) {
          subject.setPartSpec(DDLSemanticAnalyzer.getPartSpec(astChild));
        } else {
          subject.setTable(ast.getChild(0) != null);
        }
      }
    }

    if (subject.getTable()) {
      Table tbl = getTable(subject.getObject(), true);
      if (subject.getPartSpec() != null) {
        Partition part = getPartition(tbl, subject.getPartSpec(), true);
        outputs.add(new WriteEntity(part));
      } else {
        outputs.add(new WriteEntity(tbl));
      }
    }

    return subject;
  }

  /**
   * 解析sql:USER | GROUP | ROLE String,USER | GROUP | ROLE String...
   * 返回权限集合
   */
  private List<PrincipalDesc> analyzePrincipalListDef(ASTNode node) {
    List<PrincipalDesc> principalList = new ArrayList<PrincipalDesc>();

    for (int i = 0; i < node.getChildCount(); i++) {
      ASTNode child = (ASTNode) node.getChild(i);
      PrincipalType type = null;//特权类型
      switch (child.getType()) {
      case HiveParser.TOK_USER:
        type = PrincipalType.USER;
        break;
      case HiveParser.TOK_GROUP:
        type = PrincipalType.GROUP;
        break;
      case HiveParser.TOK_ROLE:
        type = PrincipalType.ROLE;
        break;
      }
      String principalName = unescapeIdentifier(child.getChild(0).getText());//特权名称
      PrincipalDesc principalDesc = new PrincipalDesc(principalName, type);
      principalList.add(principalDesc);
    }

    return principalList;
  }

  private List<PrivilegeDesc> analyzePrivilegeListDef(ASTNode node)
      throws SemanticException {
    List<PrivilegeDesc> ret = new ArrayList<PrivilegeDesc>();
    for (int i = 0; i < node.getChildCount(); i++) {
      ASTNode privilegeDef = (ASTNode) node.getChild(i);
      ASTNode privilegeType = (ASTNode) privilegeDef.getChild(0);
      Privilege privObj = PrivilegeRegistry.getPrivilege(privilegeType.getType());

      if (privObj == null) {
        throw new SemanticException("undefined privilege " + privilegeType.getType());
      }
      List<String> cols = null;
      if (privilegeDef.getChildCount() > 1) {
        cols = getColumnNames((ASTNode) privilegeDef.getChild(1));
      }
      PrivilegeDesc privilegeDesc = new PrivilegeDesc(privObj, cols);
      ret.add(privilegeDesc);
    }
    return ret;
  }

  /**
   * CREATE ROLE "roleName" 创建一个角色
   */
  private void analyzeCreateRole(ASTNode ast) {
    String roleName = unescapeIdentifier(ast.getChild(0).getText());//角色名称
    RoleDDLDesc createRoleDesc = new RoleDDLDesc(roleName,
        RoleDDLDesc.RoleOperation.CREATE_ROLE);
    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
        createRoleDesc), conf));
  }

  /**
   * DROP ROLE "roleName" 删除一个角色
   */
  private void analyzeDropRole(ASTNode ast) {
    String roleName = unescapeIdentifier(ast.getChild(0).getText());
    RoleDDLDesc createRoleDesc = new RoleDDLDesc(roleName,
        RoleDDLDesc.RoleOperation.DROP_ROLE);
    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
        createRoleDesc), conf));
  }

  /**
   * 展示某个user、group、role的具体权限
   * 格式:SHOW ROLE GRANT USER | GROUP | ROLE String
   */
  private void analyzeShowRoleGrant(ASTNode ast) {
    ASTNode child = (ASTNode) ast.getChild(0);
    PrincipalType principalType = PrincipalType.USER;
    switch (child.getType()) {
    case HiveParser.TOK_USER:
      principalType = PrincipalType.USER;
      break;
    case HiveParser.TOK_GROUP:
      principalType = PrincipalType.GROUP;
      break;
    case HiveParser.TOK_ROLE:
      principalType = PrincipalType.ROLE;
      break;
    }
    String principalName = unescapeIdentifier(child.getChild(0).getText());//权限名称
    RoleDDLDesc createRoleDesc = new RoleDDLDesc(principalName, principalType,
        RoleDDLDesc.RoleOperation.SHOW_ROLE_GRANT, null);
    createRoleDesc.setResFile(ctx.getResFile().toString());
    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
        createRoleDesc), conf));
  }

  /**
   * 更改数据库的属性
   * String SET DBPROPERTIES (key=value,key=value)
   */
  private void analyzeAlterDatabase(ASTNode ast) throws SemanticException {

    String dbName = unescapeIdentifier(ast.getChild(0).getText());//数据库名字
    Map<String, String> dbProps = null;//属性集合

    //解析属性集合
    for (int i = 1; i < ast.getChildCount(); i++) {
      ASTNode childNode = (ASTNode) ast.getChild(i);
      switch (childNode.getToken().getType()) {
      case HiveParser.TOK_DATABASEPROPERTIES:
        dbProps = DDLSemanticAnalyzer.getProps((ASTNode) childNode.getChild(0));
        break;
      default:
        throw new SemanticException("Unrecognized token in CREATE DATABASE statement");
      }
    }

    // currently alter database command can only change properties
    AlterDatabaseDesc alterDesc = new AlterDatabaseDesc(dbName, null, null, false);
    //设置属性集合
    alterDesc.setDatabaseProperties(dbProps);
    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(), alterDesc),
        conf));

  }

  /**
tableName1 EXCHANGE PARTITION (name=value,name=value,name) WITH TABLE tableName2
  将tableName1的某一个partition的数据交换到另外一个tableName2中
  注意:此时两个数据库表结构一样、分区字段相同
   */
  private void analyzeExchangePartition(ASTNode ast) throws SemanticException {
	  //获取目标和数据源两个数据表
    Table sourceTable =  getTable(getUnescapedName((ASTNode)ast.getChild(0)));
    Table destTable = getTable(getUnescapedName((ASTNode)ast.getChild(2)));

    // Get the partition specs 获取指定partition分区,并且校验partition分区信息
    Map<String, String> partSpecs = getPartSpec((ASTNode) ast.getChild(1));
    //校验分区名字不允许是在保留字中
    validatePartitionValues(partSpecs);
    
    //校验两个数据库表字段相同、分区字段相同
    boolean sameColumns = MetaStoreUtils.compareFieldColumns(
        sourceTable.getAllCols(), destTable.getAllCols());
    boolean samePartitions = MetaStoreUtils.compareFieldColumns(
        sourceTable.getPartitionKeys(), destTable.getPartitionKeys());
    if (!sameColumns || !samePartitions) {
      throw new SemanticException(ErrorMsg.TABLES_INCOMPATIBLE_SCHEMAS.getMsg());
    }
    
    //获取数据源表对应的partition集合
    List<Partition> partitions = getPartitions(sourceTable, partSpecs, true);

    // Verify that the partitions specified are continuous
    // If a subpartition value is specified without specifying a partition's value
    // then we throw an exception
    if (!isPartitionValueContinuous(sourceTable.getPartitionKeys(), partSpecs)) {
      throw new SemanticException(
          ErrorMsg.PARTITION_VALUE_NOT_CONTINUOUS.getMsg(partSpecs.toString()));
    }
    //获取目标数据库表的分区集合
    List<Partition> destPartitions = null;
    try {
      destPartitions = getPartitions(destTable, partSpecs, true);
    } catch (SemanticException ex) {
      // We should expect a semantic exception being throw as this partition
      // should not be present.
    }
    
    
    if (destPartitions != null) {
      // If any destination partition is present then throw a Semantic Exception.
      throw new SemanticException(ErrorMsg.PARTITION_EXISTS.getMsg(destPartitions.toString()));
    }
    AlterTableExchangePartition alterTableExchangePartition =
      new AlterTableExchangePartition(sourceTable, destTable, partSpecs);
    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
      alterTableExchangePartition), conf));
  }

  /**
   * @param partitionKeys the list of partition keys of the table 数据库表中的所有分区字段集合
   * @param partSpecs the partition specs given by the user 用户需要的分区字段集合
   * @return true if no subpartition value is specified without a partition's
   *         value being specified else it returns false
   */
  private boolean isPartitionValueContinuous(List<FieldSchema> partitionKeys,
      Map<String, String> partSpecs) {
    boolean partitionMissing = false;//是否缺失分区字段
    //循环数据库表中所有的字段集合
    for (FieldSchema partitionKey: partitionKeys) {
      if (!partSpecs.containsKey(partitionKey.getName())) {
        partitionMissing = true;
      } else {
        if (partitionMissing) {
          // A subpartition value exists after a missing partition 表示当缺失的partition之后的分区中,存在正确的partition
          // The partition value specified are not continuous, return false
          return false;
        }
      }
    }
    return true;
  }

  /**
   * CREATE DATABASE|SCHEMA [IF NOT Exists] "databaseName" [COMMENT String] [LOCATION String][WITH DBPROPERTIES (key=value,key=value)]
   * 创建一个数据库
   */
  private void analyzeCreateDatabase(ASTNode ast) throws SemanticException {
    String dbName = unescapeIdentifier(ast.getChild(0).getText());//数据库名
    boolean ifNotExists = false;//是否进行校验 IF NOT Exists
    String dbComment = null;//备注
    String dbLocation = null;//数据库存储路径
    Map<String, String> dbProps = null;//Map类型的数据库属性信息

    for (int i = 1; i < ast.getChildCount(); i++) {
      ASTNode childNode = (ASTNode) ast.getChild(i);
      switch (childNode.getToken().getType()) {
      case HiveParser.TOK_IFNOTEXISTS:
        ifNotExists = true;
        break;
      case HiveParser.TOK_DATABASECOMMENT:
        dbComment = unescapeSQLString(childNode.getChild(0).getText());
        break;
      case TOK_DATABASEPROPERTIES:
        dbProps = DDLSemanticAnalyzer.getProps((ASTNode) childNode.getChild(0));
        break;
      case TOK_DATABASELOCATION:
        dbLocation = unescapeSQLString(childNode.getChild(0).getText());
        break;
      default:
        throw new SemanticException("Unrecognized token in CREATE DATABASE statement");
      }
    }

    CreateDatabaseDesc createDatabaseDesc =
        new CreateDatabaseDesc(dbName, dbComment, dbLocation, ifNotExists);
    if (dbProps != null) {//设置数据库的Map属性信息
      createDatabaseDesc.setDatabaseProperties(dbProps);
    }

    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
        createDatabaseDesc), conf));
  }

  /**
   * DROP (DATABASE|SCHEMA) [IF EXISTS] database_name [RESTRICT|CASCADE];
   * 删除一个数据库
   */
  private void analyzeDropDatabase(ASTNode ast) throws SemanticException {
    String dbName = unescapeIdentifier(ast.getChild(0).getText());
    boolean ifExists = false;
    boolean ifCascade = false;

    if (null != ast.getFirstChildWithType(HiveParser.TOK_IFEXISTS)) {
      ifExists = true;
    }

    if (null != ast.getFirstChildWithType(HiveParser.TOK_CASCADE)) {
      ifCascade = true;
    }

    DropDatabaseDesc dropDatabaseDesc = new DropDatabaseDesc(dbName, ifExists, ifCascade);
    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(), dropDatabaseDesc), conf));
  }

  /**
   * use + 字符串 
   * 表示切换数据库操作
   */
  private void analyzeSwitchDatabase(ASTNode ast) {
    String dbName = unescapeIdentifier(ast.getChild(0).getText());//数据库名字
    SwitchDatabaseDesc switchDatabaseDesc = new SwitchDatabaseDesc(dbName);
    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
        switchDatabaseDesc), conf));
  }

  /**
   * DROP TABLE [IF EXISTS] tableName
   * 删除一个表操作
   * @expectView true表示该表是一个视图表,而不是实体表
   */
  private void analyzeDropTable(ASTNode ast, boolean expectView)
      throws SemanticException {
    String tableName = getUnescapedName((ASTNode) ast.getChild(0));
    boolean ifExists = (ast.getFirstChildWithType(HiveParser.TOK_IFEXISTS) != null);
    // we want to signal an error if the table/view doesn't exist and we're
    // configured not to fail silently
    //如果数据块表不存在的话,是否抛异常
    boolean throwException =
        !ifExists && !HiveConf.getBoolVar(conf, ConfVars.DROPIGNORESNONEXISTENT);
    Table tab = getTable(tableName, throwException);
    if (tab != null) {
      inputs.add(new ReadEntity(tab));
      outputs.add(new WriteEntity(tab));
    }

    DropTableDesc dropTblDesc = new DropTableDesc(
        tableName, expectView, ifExists, true);
    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
        dropTblDesc), conf));
  }

  /**
   * 截断表内数据
   * 从表或者表分区删除所有行，不指定分区，将截断表中的所有分区，也可以一次指定多个分区，截断多个分区。
   * TRUNCATE TABLE tableName [PARTITION (name=value,name=value,name)] [COLUMNS (column1,column2...)]
   */
  private void analyzeTruncateTable(ASTNode ast) throws SemanticException {
    ASTNode root = (ASTNode) ast.getChild(0);
    String tableName = getUnescapedName((ASTNode) root.getChild(0));//表名

    Table table = getTable(tableName, true);
    if (table.getTableType() != TableType.MANAGED_TABLE) {//只能是内部表才允许截断表操作
      throw new SemanticException(ErrorMsg.TRUNCATE_FOR_NON_MANAGED_TABLE.format(tableName));
    }
    if (table.isNonNative()) {
      throw new SemanticException(ErrorMsg.TRUNCATE_FOR_NON_NATIVE_TABLE.format(tableName)); //TODO
    }
    
    //语法有问题,table没有分区,但是却sql中设置了PARTITION (name=value,name=value,name),因此异常
    if (!table.isPartitioned() && root.getChildCount() > 1) {
      throw new SemanticException(ErrorMsg.PARTSPEC_FOR_NON_PARTITIONED_TABLE.format(tableName));
    }
    
    //解析PARTITION (name=value,name=value,name),查找到对应的某个partition对象
    Map<String, String> partSpec = getPartSpec((ASTNode) root.getChild(1));
    
    //获取所有的partition数据
    if (partSpec == null) {//没有要查找某个分区
      if (!table.isPartitioned()) {//table没有分区,则设置全部表
        outputs.add(new WriteEntity(table));
      } else {//table有分区,则设置所有table的分区
        for (Partition partition : getPartitions(table, null, false)) {
          outputs.add(new WriteEntity(partition));
        }
      }
    } else {
      if (isFullSpec(table, partSpec)) {
        Partition partition = getPartition(table, partSpec, true);
        outputs.add(new WriteEntity(partition));
      } else {
        for (Partition partition : getPartitions(table, partSpec, false)) {
          outputs.add(new WriteEntity(partition));
        }
      }
    }

    TruncateTableDesc truncateTblDesc = new TruncateTableDesc(tableName, partSpec);

    DDLWork ddlWork = new DDLWork(getInputs(), getOutputs(), truncateTblDesc);
    Task<? extends Serializable> truncateTask = TaskFactory.get(ddlWork, conf);

    // Is this a truncate column command
    //解析sql:COLUMNS (column1,column2...)
    List<String> columnNames = null;
    if (ast.getChildCount() == 2) {
      try {
    	//解析所有的属性名集合
        columnNames = getColumnNames((ASTNode)ast.getChild(1));

        // Throw an error if the table is indexed 带有索引的表是不允许在TRUNCATE命令中执行COLUMNS (column1,column2...)命令的
        List<Index> indexes = db.getIndexes(table.getDbName(), tableName, (short)1);
        if (indexes != null && indexes.size() > 0) {
          throw new SemanticException(ErrorMsg.TRUNCATE_COLUMN_INDEXED_TABLE.getMsg());
        }

        List<String> bucketCols = null;
        Class<? extends InputFormat> inputFormatClass = null;
        boolean isArchived = false;
        Path newTblPartLoc = null;
        Path oldTblPartLoc = null;
        List<FieldSchema> cols = null;
        ListBucketingCtx lbCtx = null;
        boolean isListBucketed = false;
        List<String> listBucketColNames = null;

        if (table.isPartitioned()) {
          Partition part = db.getPartition(table, partSpec, false);

          Path tabPath = table.getPath();
          Path partPath = part.getPartitionPath();

          // if the table is in a different dfs than the partition,
          // replace the partition's dfs with the table's dfs.
          newTblPartLoc = new Path(tabPath.toUri().getScheme(), tabPath.toUri()
              .getAuthority(), partPath.toUri().getPath());

          oldTblPartLoc = partPath;

          cols = part.getCols();
          bucketCols = part.getBucketCols();
          inputFormatClass = part.getInputFormatClass();
          isArchived = ArchiveUtils.isArchived(part);
          lbCtx = constructListBucketingCtx(part.getSkewedColNames(), part.getSkewedColValues(),
              part.getSkewedColValueLocationMaps(), part.isStoredAsSubDirectories(), conf);
          isListBucketed = part.isStoredAsSubDirectories();
          listBucketColNames = part.getSkewedColNames();
        } else {
          // input and output are the same
          oldTblPartLoc = table.getPath();
          newTblPartLoc = table.getPath();
          cols  = table.getCols();
          bucketCols = table.getBucketCols();
          inputFormatClass = table.getInputFormatClass();
          lbCtx = constructListBucketingCtx(table.getSkewedColNames(), table.getSkewedColValues(),
              table.getSkewedColValueLocationMaps(), table.isStoredAsSubDirectories(), conf);
          isListBucketed = table.isStoredAsSubDirectories();
          listBucketColNames = table.getSkewedColNames();
        }

        // throw a HiveException for non-rcfile.
        if (!inputFormatClass.equals(RCFileInputFormat.class)) {
          throw new SemanticException(ErrorMsg.TRUNCATE_COLUMN_NOT_RC.getMsg());
        }

        // throw a HiveException if the table/partition is archived
        if (isArchived) {
          throw new SemanticException(ErrorMsg.TRUNCATE_COLUMN_ARCHIVED.getMsg());
        }

        /**
         * 针对命令中的每一个属性,找到其在table中属性的索引位置,该集合与命令中的属性是一对一关系
         * 并且在查找过程中进行一些校验
         */
        Set<Integer> columnIndexes = new HashSet<Integer>();
        for (String columnName : columnNames) {//循环命令中的属性
          boolean found = false;
          for (int columnIndex = 0; columnIndex < cols.size(); columnIndex++) {//循环该表的所有属性
            if (columnName.equalsIgnoreCase(cols.get(columnIndex).getName())) {//命令中的属性与表在属性一致
              columnIndexes.add(columnIndex);
              found = true;
              break;
            }
          }
          // Throw an exception if the user is trying to truncate a column which doesn't exist
          if (!found) {//如果没有在table中找到指定属性,则抛异常
            throw new SemanticException(ErrorMsg.INVALID_COLUMN.getMsg(columnName));
          }
          // Throw an exception if the table/partition is bucketed on one of the columns
          //truncate时,column中属性不允许是bucketCol中的属性
          for (String bucketCol : bucketCols) {
            if (bucketCol.equalsIgnoreCase(columnName)) {
              throw new SemanticException(ErrorMsg.TRUNCATE_BUCKETED_COLUMN.getMsg(columnName));
            }
          }
          
          if (isListBucketed) {
            for (String listBucketCol : listBucketColNames) {
              if (listBucketCol.equalsIgnoreCase(columnName)) {
                throw new SemanticException(
                    ErrorMsg.TRUNCATE_LIST_BUCKETED_COLUMN.getMsg(columnName));
              }
            }
          }
          
        }

        //设置COLUMNS (column1,column2...) 语法中的属性在table中的属性序号一一对应关系
        truncateTblDesc.setColumnIndexes(new ArrayList<Integer>(columnIndexes));

        //该partition的老路径
        truncateTblDesc.setInputDir(oldTblPartLoc.toString());
        addInputsOutputsAlterTable(tableName, partSpec);

        truncateTblDesc.setLbCtx(lbCtx);

        addInputsOutputsAlterTable(tableName, partSpec);
        ddlWork.setNeedLock(true);
        TableDesc tblDesc = Utilities.getTableDesc(table);
        // Write the output to temporary directory and move it to the final location at the end
        // so the operation is atomic.
        String queryTmpdir = ctx.getExternalTmpFileURI(newTblPartLoc.toUri());
        truncateTblDesc.setOutputDir(queryTmpdir);
        LoadTableDesc ltd = new LoadTableDesc(queryTmpdir, queryTmpdir, tblDesc,
            partSpec == null ? new HashMap<String, String>() : partSpec);
        ltd.setLbCtx(lbCtx);
        Task<MoveWork> moveTsk = TaskFactory.get(new MoveWork(null, null, ltd, null, false),
            conf);
        truncateTask.addDependentTask(moveTsk);

        // Recalculate the HDFS stats if auto gather stats is set
        if (conf.getBoolVar(HiveConf.ConfVars.HIVESTATSAUTOGATHER)) {
          StatsWork statDesc;
          if (oldTblPartLoc.equals(newTblPartLoc)) {
            // If we're merging to the same location, we can avoid some metastore calls
            tableSpec tablepart = new tableSpec(this.db, conf, root);
            statDesc = new StatsWork(tablepart);
          } else {
            statDesc = new StatsWork(ltd);
          }
          statDesc.setNoStatsAggregator(true);
          statDesc.setStatsReliable(conf.getBoolVar(HiveConf.ConfVars.HIVE_STATS_RELIABLE));
          Task<? extends Serializable> statTask = TaskFactory.get(statDesc, conf);
          moveTsk.addDependentTask(statTask);
        }
      } catch (HiveException e) {
        throw new SemanticException(e);
      }
    }

    rootTasks.add(truncateTask);
  }

  private boolean isFullSpec(Table table, Map<String, String> partSpec) {
    for (FieldSchema partCol : table.getPartCols()) {
      if (partSpec.get(partCol.getName()) == null) {
        return false;
      }
    }
    return true;
  }

  /**
创建索引
CREATE INDEX table01_index ON TABLE table01 (column1,column2) AS 'COMPACT';
其他的是可选项

注意:
1.含义,对table01表建立索引,该索引针对column1,column2两个列建立索引,索引名称是table01_index
2.as 后面的内容是COMPACT、aggregate、bitmap、或者class全路径,参见HiveIndex类
3.autoRebuild:  with deferred rebuild 表示延期建立索引
4.indexPropertiesPrefixed: IDXPROPERTIES (key=value,key=value) 表示该index的额外属性信息
5.indexTblName: in table tableName
6.tableRowFormat:表示如何存储一行数据,由以下内容表示
//方式1:ROW FORMAT DELIMITED [FIELDS terminated by xxx [ESCAPED by xx] ] 
//[COLLECTION ITEMS terminated by xxx ]
//[MAP KEYS terminated by xxx ]
//[LINES terminated by xxx ]
//方式2:
//ROW FORMAT SERDE "class全路径" [WHIN SERDEPROPERTIES TBLPROPERTIES (key=value,key=value,key)]

7.tableFileFormat:表示文件存储格式,由以下内容存储
//STORED as SEQUENCEFILE |
//STORED as TEXTFILE |
//STORED as RCFILE |
//STORED as TEXTFILE |
//STORED as INPUTFORMAT xxx OUTPUTFORMAT xxx [INPUTDRIVER xxx OUTPUTDRIVER xxx]
//STORED BY xxxx存储引擎, WITH SERDEPROPERTIES (key=value,key=value,key) ,注意key=value集合是为xxx存储引擎提供的参数集合
//STORED AS xxxx
8.tableLocation:LOCATION xxx 表示存储在HDFS上的路径
9.indexComment:  comment xxx 表示为索引添加备注
   */
  private void analyzeCreateIndex(ASTNode ast) throws SemanticException {
    String indexName = unescapeIdentifier(ast.getChild(0).getText());//索引名称
    String typeName = unescapeSQLString(ast.getChild(1).getText());//索引的引擎,HiveIndex表内的name或者自定义的class全路径
    String tableName = getUnescapedName((ASTNode) ast.getChild(2));//对哪个表建立索引
    List<String> indexedCols = getColumnNames((ASTNode) ast.getChild(3));//对哪些列建立索引

    //获取索引的类型
    IndexType indexType = HiveIndex.getIndexType(typeName);
    if (indexType != null) {
      typeName = indexType.getHandlerClsName();
    } else {
      try {
        Class.forName(typeName);
      } catch (Exception e) {
        throw new SemanticException("class name provided for index handler not found.", e);
      }
    }

    String indexTableName = null;//解析in table tableName语法
    boolean deferredRebuild = false;//true表示延期建立索引
    String location = null;//语法解析LOCATION xxx 表示存储在HDFS上的路径
    Map<String, String> tblProps = null;
    Map<String, String> idxProps = null;//IDXPROPERTIES (key=value,key=value) 表示该index的额外属性信息
    String indexComment = null;//索引的备注信息

    //设置行分隔符信息
    RowFormatParams rowFormatParams = new RowFormatParams();
    
    //解析store as 以什么格式进行存储数据
    StorageFormat storageFormat = new StorageFormat();
    
    //STORED BY xxxx存储引擎, WITH SERDEPROPERTIES (key=value,key=value,key) ,注意key=value集合是为xxx存储引擎提供的参数集合
    AnalyzeCreateCommonVars shared = new AnalyzeCreateCommonVars();

    //循环所有的可选项子节点
    for (int idx = 4; idx < ast.getChildCount(); idx++) {
      ASTNode child = (ASTNode) ast.getChild(idx);
      if (storageFormat.fillStorageFormat(child, shared)) {
        continue;
      }
      switch (child.getToken().getType()) {
      case HiveParser.TOK_TABLEROWFORMAT://创建解析table格式的信息
        rowFormatParams.analyzeRowFormat(shared, child);
        break;
      case HiveParser.TOK_CREATEINDEX_INDEXTBLNAME://解析in table tableName语法
        ASTNode ch = (ASTNode) child.getChild(0);
        indexTableName = getUnescapedName((ASTNode) ch);
        break;
      case HiveParser.TOK_DEFERRED_REBUILDINDEX://解析是否延期建立索引
        deferredRebuild = true;
        break;
      case HiveParser.TOK_TABLELOCATION://语法解析LOCATION xxx 表示存储在HDFS上的路径
        location = unescapeSQLString(child.getChild(0).getText());
        break;
      case HiveParser.TOK_TABLEPROPERTIES:
        tblProps = DDLSemanticAnalyzer.getProps((ASTNode) child.getChild(0));
        break;
      case HiveParser.TOK_INDEXPROPERTIES://IDXPROPERTIES (key=value,key=value) 表示该index的额外属性信息
        idxProps = DDLSemanticAnalyzer.getProps((ASTNode) child.getChild(0));
        break;
      case HiveParser.TOK_TABLESERIALIZER://解析STORED BY xxxx存储引擎, WITH SERDEPROPERTIES (key=value,key=value,key) ,注意key=value集合是为xxx存储引擎提供的参数集合
        child = (ASTNode) child.getChild(0);
        shared.serde = unescapeSQLString(child.getChild(0).getText());
        if (child.getChildCount() == 2) {
          readProps((ASTNode) (child.getChild(1).getChild(0)),
              shared.serdeProps);
        }
        break;
      case HiveParser.TOK_INDEXCOMMENT://解析索引的备注信息
        child = (ASTNode) child.getChild(0);
        indexComment = unescapeSQLString(child.getText());
      }
    }

    storageFormat.fillDefaultStorageFormat(shared);


    CreateIndexDesc crtIndexDesc = new CreateIndexDesc(tableName, indexName,
        indexedCols, indexTableName, deferredRebuild, storageFormat.inputFormat,
        storageFormat.outputFormat,
        storageFormat.storageHandler, typeName, location, idxProps, tblProps,
        shared.serde, shared.serdeProps, rowFormatParams.collItemDelim,
        rowFormatParams.fieldDelim, rowFormatParams.fieldEscape,
        rowFormatParams.lineDelim, rowFormatParams.mapKeyDelim, indexComment);
    Task<?> createIndex =
        TaskFactory.get(new DDLWork(getInputs(), getOutputs(), crtIndexDesc), conf);
    rootTasks.add(createIndex);
  }

  /**
   * 删除一个数据表的一个索引
   * 格式:DROP INDEX [IF EXISTS] "indexName" ON tableName
   */
  private void analyzeDropIndex(ASTNode ast) throws SemanticException {
    String indexName = unescapeIdentifier(ast.getChild(0).getText());
    String tableName = getUnescapedName((ASTNode) ast.getChild(1));
    boolean ifExists = (ast.getFirstChildWithType(HiveParser.TOK_IFEXISTS) != null);
    // we want to signal an error if the index doesn't exist and we're
    // configured not to ignore this
    boolean throwException =
        !ifExists && !HiveConf.getBoolVar(conf, ConfVars.DROPIGNORESNONEXISTENT);
    if (throwException) {
      try {
    	//查找指定table对应的一个索引
        Index idx = db.getIndex(tableName, indexName);
      } catch (HiveException e) {
        throw new SemanticException(ErrorMsg.INVALID_INDEX.getMsg(indexName));
      }
    }

    DropIndexDesc dropIdxDesc = new DropIndexDesc(indexName, tableName);
    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
        dropIdxDesc), conf));
  }

  /**
   * 修改index的属性信息 "indexName" ON "tableName" [PARTITION (name=value,name=value,name)] REBUILD
   */
  private void analyzeAlterIndexRebuild(ASTNode ast) throws SemanticException {
    String baseTableName = unescapeIdentifier(ast.getChild(0).getText());
    String indexName = unescapeIdentifier(ast.getChild(1).getText());
    HashMap<String, String> partSpec = null;
    Tree part = ast.getChild(2);
    if (part != null) {
      partSpec = extractPartitionSpecs(part);
    }
    
    //添加任务
    List<Task<?>> indexBuilder = getIndexBuilderMapRed(baseTableName, indexName, partSpec);
    rootTasks.addAll(indexBuilder);

    // Handle updating index timestamps
    AlterIndexDesc alterIdxDesc = new AlterIndexDesc(AlterIndexTypes.UPDATETIMESTAMP);
    alterIdxDesc.setIndexName(indexName);
    alterIdxDesc.setBaseTableName(baseTableName);
    alterIdxDesc.setDbName(SessionState.get().getCurrentDatabase());
    alterIdxDesc.setSpec(partSpec);

    Task<?> tsTask = TaskFactory.get(new DDLWork(alterIdxDesc), conf);
    for (Task<?> t : indexBuilder) {
      t.addDependentTask(tsTask);
    }
  }

  /**
   * 修改index的属性信息
   * "indexName" ON "tableName" [PARTITION (name=value,name=value,name)] SET IDXPROPERTIES (key=value,key=value)
   */
  private void analyzeAlterIndexProps(ASTNode ast)
      throws SemanticException {

    String baseTableName = getUnescapedName((ASTNode) ast.getChild(0));
    String indexName = unescapeIdentifier(ast.getChild(1).getText());
    HashMap<String, String> mapProp = getProps((ASTNode) (ast.getChild(2))
        .getChild(0));

    AlterIndexDesc alterIdxDesc =
        new AlterIndexDesc(AlterIndexTypes.ADDPROPS);
    alterIdxDesc.setProps(mapProp);
    alterIdxDesc.setIndexName(indexName);
    alterIdxDesc.setBaseTableName(baseTableName);
    alterIdxDesc.setDbName(SessionState.get().getCurrentDatabase());

    rootTasks.add(TaskFactory.get(new DDLWork(alterIdxDesc), conf));
  }

  /**
   * 修改index的属性信息 "indexName" ON "tableName" [PARTITION (name=value,name=value,name)] REBUILD
   * 被analyzeAlterIndexRebuild该方法调用
   */
  private List<Task<?>> getIndexBuilderMapRed(String baseTableName, String indexName,
      HashMap<String, String> partSpec) throws SemanticException {
    try {
      String dbName = SessionState.get().getCurrentDatabase();
      Index index = db.getIndex(dbName, baseTableName, indexName);//找到对应的索引对象
      Table indexTbl = getTable(index.getIndexTableName());//找到索引对应的表
      String baseTblName = index.getOrigTableName();
      Table baseTbl = getTable(baseTblName);

      String handlerCls = index.getIndexHandlerClass();
      HiveIndexHandler handler = HiveUtils.getIndexHandler(conf, handlerCls);

      List<Partition> indexTblPartitions = null;
      List<Partition> baseTblPartitions = null;
      if (indexTbl != null) {
        indexTblPartitions = new ArrayList<Partition>();
        baseTblPartitions = preparePartitions(baseTbl, partSpec,
            indexTbl, db, indexTblPartitions);
      }

      List<Task<?>> ret = handler.generateIndexBuildTaskList(baseTbl,
          index, indexTblPartitions, baseTblPartitions, indexTbl, getInputs(), getOutputs());
      return ret;
    } catch (Exception e) {
      throw new SemanticException(e);
    }
  }

  private List<Partition> preparePartitions(
      org.apache.hadoop.hive.ql.metadata.Table baseTbl,
      HashMap<String, String> partSpec,
      org.apache.hadoop.hive.ql.metadata.Table indexTbl, Hive db,
      List<Partition> indexTblPartitions)
      throws HiveException, MetaException {
    List<Partition> baseTblPartitions = new ArrayList<Partition>();
    if (partSpec != null) {
      // if partspec is specified, then only producing index for that
      // partition
      Partition part = db.getPartition(baseTbl, partSpec, false);
      if (part == null) {
        throw new HiveException("Partition "
            + Warehouse.makePartName(partSpec, false)
            + " does not exist in table "
            + baseTbl.getTableName());
      }
      baseTblPartitions.add(part);
      Partition indexPart = db.getPartition(indexTbl, partSpec, false);
      if (indexPart == null) {
        indexPart = db.createPartition(indexTbl, partSpec);
      }
      indexTblPartitions.add(indexPart);
    } else if (baseTbl.isPartitioned()) {
      // if no partition is specified, create indexes for all partitions one
      // by one.
      baseTblPartitions = db.getPartitions(baseTbl);
      for (Partition basePart : baseTblPartitions) {
        HashMap<String, String> pSpec = basePart.getSpec();
        Partition indexPart = db.getPartition(indexTbl, pSpec, false);
        if (indexPart == null) {
          indexPart = db.createPartition(indexTbl, pSpec);
        }
        indexTblPartitions.add(indexPart);
      }
    }
    return baseTblPartitions;
  }

  private void validateAlterTableType(Table tbl, AlterTableTypes op) throws SemanticException {
    validateAlterTableType(tbl, op, false);
  }

  /**
   * 对表或者视图校验是否允许该alter的类型操作
   * @param tbl 真实的table对象
   * @param op 要进行的alter操作
   * @param expectView,期望该数据库是否是视图
   */
  private void validateAlterTableType(Table tbl, AlterTableTypes op, boolean expectView)
      throws SemanticException {
    if (tbl.isView()) {//真实的table对象是视图
      if (!expectView) {//必须是视图,因为table是视图属性
        throw new SemanticException(ErrorMsg.ALTER_COMMAND_FOR_VIEWS.getMsg());
      }

      switch (op) {
      case ADDPARTITION:
      case DROPPARTITION:
      case RENAMEPARTITION:
      case ADDPROPS:
      case DROPPROPS:
      case RENAME:
        // allow this form
        break;
      default:
        throw new SemanticException(ErrorMsg.ALTER_VIEW_DISALLOWED_OP.getMsg(op.toString()));//视图不允许有该操作
      }
    } else {
      if (expectView) {//是表,不是视图
        throw new SemanticException(ErrorMsg.ALTER_COMMAND_FOR_TABLES.getMsg());
      }
    }
    if (tbl.isNonNative()) {
      throw new SemanticException(ErrorMsg.ALTER_TABLE_NON_NATIVE.getMsg(tbl.getTableName()));
    }
  }

  /**
   * 设置/取消视图或者表的属性
格式:
a.String SET TBLPROPERTIES (keyValueProperty,keyValueProperty)
b.String UNSET TBLPROPERTIES [IF Exists](keyValueProperty,keyValueProperty)
@param expectView true表示操作视图
@param isUnset true表示取消操作,false表示设置操作
   */
  private void analyzeAlterTableProps(ASTNode ast, boolean expectView, boolean isUnset)
      throws SemanticException {

    String tableName = getUnescapedName((ASTNode) ast.getChild(0));
    HashMap<String, String> mapProp = getProps((ASTNode) (ast.getChild(1))
        .getChild(0));
    AlterTableDesc alterTblDesc = null;
    if (isUnset == true) {
      alterTblDesc = new AlterTableDesc(AlterTableTypes.DROPPROPS, expectView);
      if (ast.getChild(2) != null) {
        alterTblDesc.setDropIfExists(true);
      }
    } else {
      alterTblDesc = new AlterTableDesc(AlterTableTypes.ADDPROPS, expectView);
    }
    alterTblDesc.setProps(mapProp);
    alterTblDesc.setOldName(tableName);

    addInputsOutputsAlterTable(tableName, null, alterTblDesc);

    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
        alterTblDesc), conf));
  }

  /**
设置存储的方式是csv、json、还是protobuffer等等吧,格式 SET SERDEPROPERTIES (key=value,key=value)
   */
  private void analyzeAlterTableSerdeProps(ASTNode ast, String tableName,
      HashMap<String, String> partSpec)
      throws SemanticException {
    HashMap<String, String> mapProp = getProps((ASTNode) (ast.getChild(0))
        .getChild(0));
    AlterTableDesc alterTblDesc = new AlterTableDesc(
        AlterTableTypes.ADDSERDEPROPS);
    alterTblDesc.setProps(mapProp);
    alterTblDesc.setOldName(tableName);
    alterTblDesc.setPartSpec(partSpec);

    addInputsOutputsAlterTable(tableName, partSpec, alterTblDesc);
    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
        alterTblDesc), conf));
  }

  /**
设置存储的方式是csv、json、还是protobuffer等等吧
格式 SET SERDE "serde_class_name" [WITH SERDEPROPERTIES(key=value,key=value)]
   */
  private void analyzeAlterTableSerde(ASTNode ast, String tableName,
      HashMap<String, String> partSpec)
      throws SemanticException {

    String serdeName = unescapeSQLString(ast.getChild(0).getText());//类名字
    AlterTableDesc alterTblDesc = new AlterTableDesc(AlterTableTypes.ADDSERDE);
    if (ast.getChildCount() > 1) {//属性信息
      HashMap<String, String> mapProp = getProps((ASTNode) (ast.getChild(1))
          .getChild(0));
      alterTblDesc.setProps(mapProp);
    }
    alterTblDesc.setOldName(tableName);
    alterTblDesc.setSerdeName(serdeName);
    alterTblDesc.setPartSpec(partSpec);

    addInputsOutputsAlterTable(tableName, partSpec, alterTblDesc);
    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
        alterTblDesc), conf));
  }

  /**
设置文件格式
1.SET FILEFORMAT SEQUENCEFILE
2.SET FILEFORMAT TEXTFILE
3.SET FILEFORMAT RCFILE
4.SET FILEFORMAT ORCFILE
5.SET FILEFORMAT INPUTFORMAT string OUTPUTFORMAT string [INPUTDRIVER string OUTPUTDRIVER string]
6.SET FILEFORMAT xxxx 属于TOK_FILEFORMAT_GENERIC类型自定义格式   *
   */
  private void analyzeAlterTableFileFormat(ASTNode ast, String tableName,
      HashMap<String, String> partSpec)
      throws SemanticException {

    String inputFormat = null;
    String outputFormat = null;
    String storageHandler = null;
    String serde = null;
    ASTNode child = (ASTNode) ast.getChild(0);

    switch (child.getToken().getType()) {
    case HiveParser.TOK_TABLEFILEFORMAT:
      inputFormat = unescapeSQLString(((ASTNode) child.getChild(0)).getToken()
          .getText());
      outputFormat = unescapeSQLString(((ASTNode) child.getChild(1)).getToken()
          .getText());
      try {
        Class.forName(inputFormat);
        Class.forName(outputFormat);
      } catch (ClassNotFoundException e) {
        throw new SemanticException(e);
      }
      break;
    case HiveParser.TOK_STORAGEHANDLER:
      storageHandler =
          unescapeSQLString(((ASTNode) child.getChild(1)).getToken().getText());
      try {
        Class.forName(storageHandler);
      } catch (ClassNotFoundException e) {
        throw new SemanticException(e);
      }
      break;
    case HiveParser.TOK_TBLSEQUENCEFILE:
      inputFormat = SEQUENCEFILE_INPUT;
      outputFormat = SEQUENCEFILE_OUTPUT;
      break;
    case HiveParser.TOK_TBLTEXTFILE:
      inputFormat = TEXTFILE_INPUT;
      outputFormat = TEXTFILE_OUTPUT;
      break;
    case HiveParser.TOK_TBLRCFILE:
      inputFormat = RCFILE_INPUT;
      outputFormat = RCFILE_OUTPUT;
      serde = conf.getVar(HiveConf.ConfVars.HIVEDEFAULTRCFILESERDE);
      break;
    case HiveParser.TOK_TBLORCFILE:
      inputFormat = ORCFILE_INPUT;
      outputFormat = ORCFILE_OUTPUT;
      serde = ORCFILE_SERDE;
      break;
    case HiveParser.TOK_FILEFORMAT_GENERIC://自定义,目前不支持,需要自己实现该方法,继承DDLSemanticAnalyzer类即可
      handleGenericFileFormat(child);
      break;
    }

    AlterTableDesc alterTblDesc = new AlterTableDesc(tableName, inputFormat,
        outputFormat, serde, storageHandler, partSpec);

    addInputsOutputsAlterTable(tableName, partSpec, alterTblDesc);
    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
        alterTblDesc), conf));
  }

  private void addInputsOutputsAlterTable(String tableName, Map<String, String> partSpec)
      throws SemanticException {
    addInputsOutputsAlterTable(tableName, partSpec, null);
  }

  /**
   * 当为某个table下的某个partition创建了一个更改命令Alter命令后,调用该方法
   */
  private void addInputsOutputsAlterTable(String tableName, Map<String, String> partSpec,
      AlterTableDesc desc) throws SemanticException {
    Table tab = getTable(tableName, true);
    if (partSpec == null || partSpec.isEmpty()) {
      inputs.add(new ReadEntity(tab));
      outputs.add(new WriteEntity(tab));
    }
    else {
      inputs.add(new ReadEntity(tab));
      if (desc == null || desc.getOp() != AlterTableDesc.AlterTableTypes.ALTERPROTECTMODE) {
        Partition part = getPartition(tab, partSpec, true);
        outputs.add(new WriteEntity(part));
      }
      else {
        for (Partition part : getPartitions(tab, partSpec, true)) {
          outputs.add(new WriteEntity(part));
        }
      }
    }

    if (desc != null) {
      validateAlterTableType(tab, desc.getOp(), desc.getExpectView());

      // validate Unset Non Existed Table Properties
      if (desc.getOp() == AlterTableDesc.AlterTableTypes.DROPPROPS &&
            desc.getIsDropIfExists() == false) {
        Iterator<String> keyItr = desc.getProps().keySet().iterator();
        while (keyItr.hasNext()) {
          String currKey = keyItr.next();
          if (tab.getTTable().getParameters().containsKey(currKey) == false) {
            String errorMsg =
                "The following property " + currKey +
                " does not exist in " + tab.getTableName();
            throw new SemanticException(
              ErrorMsg.ALTER_TBL_UNSET_NON_EXIST_PROPERTY.getMsg(errorMsg));
          }
        }
      }
    }
  }

  /**
   * 解析SET LOCATION xxxx,即HDFS上存储的路径
   * 为某个table的某个partitions分配HDFS上路径 
   */
  private void analyzeAlterTableLocation(ASTNode ast, String tableName,
      HashMap<String, String> partSpec) throws SemanticException {
	//获取HDFS路径
    String newLocation = unescapeSQLString(ast.getChild(0).getText());

    //创建描述本次更改操作的内容
    AlterTableDesc alterTblDesc = new AlterTableDesc(tableName, newLocation, partSpec);

    addInputsOutputsAlterTable(tableName, partSpec, alterTblDesc);
    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
        alterTblDesc), conf));
  }

  /**
   * analyzeAlterTableProtectMode设置protect模式以及类型,但是该类没有sql被启动,因此暂时不会对系统有影响
   */
  private void analyzeAlterTableProtectMode(ASTNode ast, String tableName,
      HashMap<String, String> partSpec)
      throws SemanticException {

    AlterTableDesc alterTblDesc =
        new AlterTableDesc(AlterTableTypes.ALTERPROTECTMODE);

    alterTblDesc.setOldName(tableName);
    alterTblDesc.setPartSpec(partSpec);

    ASTNode child = (ASTNode) ast.getChild(0);

    switch (child.getToken().getType()) {
    case HiveParser.TOK_ENABLE:
      alterTblDesc.setProtectModeEnable(true);
      break;
    case HiveParser.TOK_DISABLE:
      alterTblDesc.setProtectModeEnable(false);
      break;
    default:
      throw new SemanticException(
          "Set Protect mode Syntax parsing error.");
    }

    ASTNode grandChild = (ASTNode) child.getChild(0);
    switch (grandChild.getToken().getType()) {
    case HiveParser.TOK_OFFLINE:
      alterTblDesc.setProtectModeType(AlterTableDesc.ProtectModeType.OFFLINE);
      break;
    case HiveParser.TOK_NO_DROP:
      if (grandChild.getChildCount() > 0) {
        alterTblDesc.setProtectModeType(AlterTableDesc.ProtectModeType.NO_DROP_CASCADE);
      }
      else {
        alterTblDesc.setProtectModeType(AlterTableDesc.ProtectModeType.NO_DROP);
      }
      break;
    case HiveParser.TOK_READONLY:
      throw new SemanticException(
          "Potect mode READONLY is not implemented");
    default:
      throw new SemanticException(
          "Only protect mode NO_DROP or OFFLINE supported");
    }

    addInputsOutputsAlterTable(tableName, partSpec, alterTblDesc);
    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
        alterTblDesc), conf));
  }

  private void analyzeAlterTablePartMergeFiles(ASTNode tablePartAST, ASTNode ast,
      String tableName, HashMap<String, String> partSpec)
      throws SemanticException {
    AlterTablePartMergeFilesDesc mergeDesc = new AlterTablePartMergeFilesDesc(
        tableName, partSpec);

    List<String> inputDir = new ArrayList<String>();
    Path oldTblPartLoc = null;
    Path newTblPartLoc = null;
    Table tblObj = null;
    ListBucketingCtx lbCtx = null;

    try {
      tblObj = getTable(tableName);

      List<String> bucketCols = null;
      Class<? extends InputFormat> inputFormatClass = null;
      boolean isArchived = false;
      boolean checkIndex = HiveConf.getBoolVar(conf,
          HiveConf.ConfVars.HIVE_CONCATENATE_CHECK_INDEX);
      if (checkIndex) {
        List<Index> indexes = db.getIndexes(tblObj.getDbName(), tableName,
            Short.MAX_VALUE);
        if (indexes != null && indexes.size() > 0) {
          throw new SemanticException("can not do merge because source table "
              + tableName + " is indexed.");
        }
      }

      if (tblObj.isPartitioned()) {
        if (partSpec == null) {
          throw new SemanticException("source table " + tableName
              + " is partitioned but no partition desc found.");
        } else {
          Partition part = getPartition(tblObj, partSpec, false);
          if (part == null) {
            throw new SemanticException("source table " + tableName
                + " is partitioned but partition not found.");
          }
          bucketCols = part.getBucketCols();
          inputFormatClass = part.getInputFormatClass();
          isArchived = ArchiveUtils.isArchived(part);

          Path tabPath = tblObj.getPath();
          Path partPath = part.getPartitionPath();

          // if the table is in a different dfs than the partition,
          // replace the partition's dfs with the table's dfs.
          newTblPartLoc = new Path(tabPath.toUri().getScheme(), tabPath.toUri()
              .getAuthority(), partPath.toUri().getPath());

          oldTblPartLoc = partPath;

          lbCtx = constructListBucketingCtx(part.getSkewedColNames(), part.getSkewedColValues(),
              part.getSkewedColValueLocationMaps(), part.isStoredAsSubDirectories(), conf);
        }
      } else {
        inputFormatClass = tblObj.getInputFormatClass();
        bucketCols = tblObj.getBucketCols();

        // input and output are the same
        oldTblPartLoc = tblObj.getPath();
        newTblPartLoc = tblObj.getPath();

        lbCtx = constructListBucketingCtx(tblObj.getSkewedColNames(), tblObj.getSkewedColValues(),
            tblObj.getSkewedColValueLocationMaps(), tblObj.isStoredAsSubDirectories(), conf);
      }

      // throw a HiveException for non-rcfile.
      if (!inputFormatClass.equals(RCFileInputFormat.class)) {
        throw new SemanticException(
            "Only RCFileFormat is supportted right now.");
      }

      // throw a HiveException if the table/partition is bucketized
      if (bucketCols != null && bucketCols.size() > 0) {
        throw new SemanticException(
            "Merge can not perform on bucketized partition/table.");
      }

      // throw a HiveException if the table/partition is archived
      if (isArchived) {
        throw new SemanticException(
            "Merge can not perform on archived partitions.");
      }

      inputDir.add(oldTblPartLoc.toString());

      mergeDesc.setInputDir(inputDir);

      mergeDesc.setLbCtx(lbCtx);

      addInputsOutputsAlterTable(tableName, partSpec);
      DDLWork ddlWork = new DDLWork(getInputs(), getOutputs(), mergeDesc);
      ddlWork.setNeedLock(true);
      Task<? extends Serializable> mergeTask = TaskFactory.get(ddlWork, conf);
      TableDesc tblDesc = Utilities.getTableDesc(tblObj);
      String queryTmpdir = ctx.getExternalTmpFileURI(newTblPartLoc.toUri());
      mergeDesc.setOutputDir(queryTmpdir);
      LoadTableDesc ltd = new LoadTableDesc(queryTmpdir, queryTmpdir, tblDesc,
          partSpec == null ? new HashMap<String, String>() : partSpec);
      ltd.setLbCtx(lbCtx);
      Task<MoveWork> moveTsk = TaskFactory.get(new MoveWork(null, null, ltd, null, false),
          conf);
      mergeTask.addDependentTask(moveTsk);

      if (conf.getBoolVar(HiveConf.ConfVars.HIVESTATSAUTOGATHER)) {
        StatsWork statDesc;
        if (oldTblPartLoc.equals(newTblPartLoc)) {
          // If we're merging to the same location, we can avoid some metastore calls
          tableSpec tablepart = new tableSpec(this.db, conf, tablePartAST);
          statDesc = new StatsWork(tablepart);
        } else {
          statDesc = new StatsWork(ltd);
        }
        statDesc.setNoStatsAggregator(true);
        statDesc.setStatsReliable(conf.getBoolVar(HiveConf.ConfVars.HIVE_STATS_RELIABLE));
        Task<? extends Serializable> statTask = TaskFactory.get(statDesc, conf);
        moveTsk.addDependentTask(statTask);
      }

      rootTasks.add(mergeTask);
    } catch (Exception e) {
      throw new SemanticException(e);
    }
  }

  /**
alterStatementSuffixClusterbySortby格式:
1.NOT CLUSTERED 表示没有CLUSTERED BY 操作
2.NOT SORTED 表示没有SORTED BY 操作
3.CLUSTERED BY (column1,column2) [SORTED BY (column1 desc,column2 desc)] into Number BUCKETS
   */
  private void analyzeAlterTableClusterSort(ASTNode ast, String tableName,
      HashMap<String, String> partSpec) throws SemanticException {
    addInputsOutputsAlterTable(tableName, partSpec);

    AlterTableDesc alterTblDesc;
    switch (ast.getChild(0).getType()) {
    case HiveParser.TOK_NOT_CLUSTERED:
      alterTblDesc = new AlterTableDesc(tableName, -1, new ArrayList<String>(),
          new ArrayList<Order>(), partSpec);
      rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(), alterTblDesc), conf));
      break;
    case HiveParser.TOK_NOT_SORTED:
      alterTblDesc = new AlterTableDesc(tableName, true, partSpec);
      rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(), alterTblDesc), conf));
      break;
    case HiveParser.TOK_TABLEBUCKETS:
      ASTNode buckets = (ASTNode) ast.getChild(0);
      //解析CLUSTERED BY (column1,column2)
      List<String> bucketCols = getColumnNames((ASTNode) buckets.getChild(0));
      //解析SORTED BY (column1 desc,column2 desc) 
      List<Order> sortCols = new ArrayList<Order>();
      //解析into Number BUCKETS
      int numBuckets = -1;
      if (buckets.getChildCount() == 2) {//仅仅有into Number BUCKETS
        numBuckets = (Integer.valueOf(buckets.getChild(1).getText())).intValue();
      } else {//SORT BY和into Number BUCKETS都被设置了
        sortCols = getColumnNamesOrder((ASTNode) buckets.getChild(1));
        numBuckets = (Integer.valueOf(buckets.getChild(2).getText())).intValue();
      }
      if (numBuckets <= 0) {
    	//CLUSTERED BY (column1,column2) [SORTED BY (column1 desc,column2 desc)] into Number BUCKETS语法中必须存在into Number BUCKETS语句,并且Number一定要大于1 
        throw new SemanticException(ErrorMsg.INVALID_BUCKET_NUMBER.getMsg());
      }

      alterTblDesc = new AlterTableDesc(tableName, numBuckets,
          bucketCols, sortCols, partSpec);
      rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
          alterTblDesc), conf));
      break;
    }
  }

  //解析数据库的property属性键值对信息
  static HashMap<String, String> getProps(ASTNode prop) {
    HashMap<String, String> mapProp = new HashMap<String, String>();
    readProps(prop, mapProp);
    return mapProp;
  }

  /**
   * Utility class to resolve QualifiedName
   */
  static class QualifiedNameUtil {

    // delimiter to check DOT delimited qualified names
    static String delimiter = "\\.";

    /**
     * Get the fully qualified name in the ast. e.g. the ast of the form ^(DOT
     * ^(DOT a b) c) will generate a name of the form a.b.c
     *
     * @param ast
     *          The AST from which the qualified name has to be extracted
     * @return String
     * 将节点的所有子节点使用.分隔开
     */
    static public String getFullyQualifiedName(ASTNode ast) {
      if (ast.getChildCount() == 0) {
        return ast.getText();
      } else if (ast.getChildCount() == 2) {
        return getFullyQualifiedName((ASTNode) ast.getChild(0)) + "."
        + getFullyQualifiedName((ASTNode) ast.getChild(1));
      } else if (ast.getChildCount() == 3) {
        return getFullyQualifiedName((ASTNode) ast.getChild(0)) + "."
        + getFullyQualifiedName((ASTNode) ast.getChild(1)) + "."
        + getFullyQualifiedName((ASTNode) ast.getChild(2));
      } else {
        return null;
      }
    }

    // assume the first component of DOT delimited name is tableName
    // get the attemptTableName
    /**
     * 
     * @param db 数据库查询对象
     * @param qualifiedName database.table.column or database.table or database
     * @param isColumn
     * @return
     */
    static public String getAttemptTableName(Hive db, String qualifiedName, boolean isColumn) {
      // check whether the name starts with table
      // DESCRIBE table
      // DESCRIBE table.column
      // DECRIBE table column
      String tableName = qualifiedName.substring(0,
        qualifiedName.indexOf('.') == -1 ?
        qualifiedName.length() : qualifiedName.indexOf('.'));
      try {
        Table tab = db.getTable(tableName);
        if (tab != null) {
          if (isColumn) {
            // if attempt to get columnPath
            // return the whole qualifiedName(table.column or table)
            return qualifiedName;
          } else {
            // if attempt to get tableName
            // return table
            return tableName;
          }
        }
      } catch (HiveException e) {
        // assume the first DOT delimited component is tableName
        // OK if it is not
        // do nothing when having exception
        return null;
      }
      return null;
    }

    // get Database Name 获取数据库名字
    static public String getDBName(Hive db, ASTNode ast) {
      String dbName = null;
      //database.table.column or database.table or database
      String fullyQualifiedName = getFullyQualifiedName(ast);

      // if database.table or database.table.column or table.column
      // first try the first component of the DOT separated name
      if (ast.getChildCount() >= 2) {
    	//获取数据库名字
        dbName = fullyQualifiedName.substring(0,
          fullyQualifiedName.indexOf('.') == -1 ?
          fullyQualifiedName.length() :
          fullyQualifiedName.indexOf('.'));
        try {
          // if the database name is not valid
          // it is table.column
          // return null as dbName 校验数据库是否存在
          if (!db.databaseExists(dbName)) {
            return null;
          }
        } catch (HiveException e) {
          return null;
        }
      } else {
        // in other cases, return null
        // database is not validated if null
        return null;
      }
      return dbName;
    }

    // get Table Name 获取表名字
    static public String getTableName(Hive db, ASTNode ast)
      throws SemanticException {
      String tableName = null;
      //database.table.column or database.table or database
      String fullyQualifiedName = getFullyQualifiedName(ast);

      // assume the first component of DOT delimited name is tableName
      String attemptTableName = getAttemptTableName(db, fullyQualifiedName, false);
      if (attemptTableName != null) {
        return attemptTableName;
      }

      // if the name does not start with table
      // it should start with database
      // DESCRIBE database.table.column
      // DESCRIBE database.table
      // DESCRIBE database.table column
      if (fullyQualifiedName.split(delimiter).length == 3) {
        // if DESCRIBE database.table.column
        // invalid syntax exception
        if (ast.getChildCount() == 2) {
          throw new SemanticException(ErrorMsg.INVALID_TABLE_OR_COLUMN.getMsg(fullyQualifiedName));
        } else {
          // if DESCRIBE database.table column
          // return database.table as tableName
          tableName = fullyQualifiedName.substring(0,
            fullyQualifiedName.lastIndexOf('.'));
        }
      } else if (fullyQualifiedName.split(delimiter).length == 2) {
        // if DESCRIBE database.table
        // return database.table as tableName
        tableName = fullyQualifiedName;
      } else {
        // if fullyQualifiedName only have one component
        // it is an invalid table
        throw new SemanticException(ErrorMsg.INVALID_TABLE.getMsg(fullyQualifiedName));
      }

      return tableName;
    }

    // get column path
    /**
     * 
     * @param db
     * @param parentAst
     * @param ast 解析DESCRIBE | DESC .($ELEM$ | $KEY$ | $VALUE$ | xxx )
     * @param tableName 格式:database.table.column or database.table or database
     * @param partSpec
     * @return
     */
    static public String getColPath(
      Hive db,
      ASTNode parentAst,
      ASTNode ast,
      String tableName,
      Map<String, String> partSpec) {

      // if parent has two children
      // it could be DESCRIBE table key
      // or DESCRIBE table partition
      if (parentAst.getChildCount() == 2 && partSpec == null) {
        // if partitionSpec is null
        // it is DESCRIBE table key
        // return table as columnPath
        return getFullyQualifiedName(parentAst);
      }

      // assume the first component of DOT delimited name is tableName
      String attemptTableName = getAttemptTableName(db, tableName, true);
      if (attemptTableName != null) {
        return attemptTableName;
      }

      // if the name does not start with table
      // it should start with database
      // DESCRIBE database.table
      // DESCRIBE database.table column
      if (tableName.split(delimiter).length == 3) {
        // if DESCRIBE database.table column
        // return table.column as column path
        return tableName.substring(
          tableName.indexOf(".") + 1, tableName.length());
      }

      // in other cases, column path is the same as tableName
      return tableName;
    }

    // get partition metadata 解析 [PARTITION (name=value,name=value,name)] 并且进行了校验
    static public Map<String, String> getPartitionSpec(Hive db, ASTNode ast, String tableName)
      throws SemanticException {
      // if ast has two children
      // it could be DESCRIBE table key
      // or DESCRIBE table partition
      // check whether it is DESCRIBE table partition
      if (ast.getChildCount() == 2) {
        ASTNode partNode = (ASTNode) ast.getChild(1);
        HashMap<String, String> partSpec = null;
        try {
          partSpec = getPartSpec(partNode);
        } catch (SemanticException e) {
          // get exception in resolving partition
          // it could be DESCRIBE table key
          // return null
          // continue processing for DESCRIBE table key
          return null;
        }

        Table tab = null;
        try {
          tab = db.getTable(tableName);
        } catch (HiveException e) {
          // if table not valid
          // throw semantic exception
          throw new SemanticException(ErrorMsg.INVALID_TABLE.getMsg(tableName), e);
        }

        if (partSpec != null) {
          Partition part = null;
          try {
            part = db.getPartition(tab, partSpec, false);
          } catch (HiveException e) {
            // if get exception in finding partition
            // it could be DESCRIBE table key
            // return null
            // continue processing for DESCRIBE table key
            return null;
          }

          // if partition is not found
          // it is DESCRIBE table partition
          // invalid partition exception
          if (part == null) {
            throw new SemanticException(ErrorMsg.INVALID_PARTITION.getMsg(partSpec.toString()));
          }

          // it is DESCRIBE table partition
          // return partition metadata
          return partSpec;
        }
      }

      return null;
    }

  }

  /**
   * Create a FetchTask for a given table and thrift ddl schema.
   *
   * @param tablename
   *          tablename
   * @param schema
   *          thrift ddl 例如partition#string表示String类型的partition字段
   */
  private FetchTask createFetchTask(String schema) {
    Properties prop = new Properties();

    prop.setProperty(serdeConstants.SERIALIZATION_FORMAT, "9");//拆分每一个属性的配置,field.delim的默认值,默认是9,即\t
    prop.setProperty(serdeConstants.SERIALIZATION_NULL_FORMAT, " ");//当null的时候输出空格
    String[] colTypes = schema.split("#");
    prop.setProperty("columns", colTypes[0]);//列名字
    prop.setProperty("columns.types", colTypes[1]);//列类型

    FetchWork fetch = new FetchWork(ctx.getResFile().toString(), new TableDesc(
        LazySimpleSerDe.class, TextInputFormat.class,
        IgnoreKeyTextOutputFormat.class, prop), -1);
    fetch.setSerializationNullFormat(" ");
    return (FetchTask) TaskFactory.get(fetch, conf);
  }

  /**
   * 校验数据库
   */
  private void validateDatabase(String databaseName) throws SemanticException {
    try {
      if (!db.databaseExists(databaseName)) {
        throw new SemanticException(ErrorMsg.DATABASE_NOT_EXISTS.getMsg(databaseName));
      }
    } catch (HiveException e) {
      throw new SemanticException(ErrorMsg.DATABASE_NOT_EXISTS.getMsg(databaseName), e);
    }
  }

  /**
   * 
   * 校验数据库表以及分区
   */
  private void validateTable(String tableName, Map<String, String> partSpec)
      throws SemanticException {
    Table tab = getTable(tableName);
    if (partSpec != null) {
      getPartition(tab, partSpec, true);
    }
  }

  /**
   * 描述表
   * DESCRIBE | DESC [FORMATTED | EXTENDED | PRETTY] .($ELEM$ | $KEY$ | $VALUE$ | xxx ) [PARTITION (name=value,name=value,name)]
   */
  private void analyzeDescribeTable(ASTNode ast) throws SemanticException {
    ASTNode tableTypeExpr = (ASTNode) ast.getChild(0);

    //database.table.column or database.table or database
    String qualifiedName =
      QualifiedNameUtil.getFullyQualifiedName((ASTNode) tableTypeExpr.getChild(0));
    
    //表名字
    String tableName =
      QualifiedNameUtil.getTableName(db, (ASTNode)(tableTypeExpr.getChild(0)));
    
    //数据库名字
    String dbName =
      QualifiedNameUtil.getDBName(db, (ASTNode)(tableTypeExpr.getChild(0)));

    //解析[PARTITION (name=value,name=value,name)]
    Map<String, String> partSpec =
      QualifiedNameUtil.getPartitionSpec(db, tableTypeExpr, tableName);

    String colPath = QualifiedNameUtil.getColPath(
        db, tableTypeExpr, (ASTNode) tableTypeExpr.getChild(0), qualifiedName, partSpec);

    // if database is not the one currently using
    // validate database
    
    //对数据库和数据表进行校验
    if (dbName != null) {
      validateDatabase(dbName);
    }
    if (partSpec != null) {
      validateTable(tableName, partSpec);
    }

    DescTableDesc descTblDesc = new DescTableDesc(
      ctx.getResFile(), tableName, partSpec, colPath);

    //解析[FORMATTED | EXTENDED | PRETTY]
    if (ast.getChildCount() == 2) {
      int descOptions = ast.getChild(1).getType();
      descTblDesc.setFormatted(descOptions == HiveParser.KW_FORMATTED);
      descTblDesc.setExt(descOptions == HiveParser.KW_EXTENDED);
      descTblDesc.setPretty(descOptions == HiveParser.KW_PRETTY);
    }
    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
        descTblDesc), conf));
    setFetchTask(createFetchTask(DescTableDesc.getSchema()));
    LOG.info("analyzeDescribeTable done");
  }

  /**
   * Describe database.
   *
   * @param ast
   * @throws SemanticException
   */
  private void analyzeDescDatabase(ASTNode ast) throws SemanticException {

    boolean isExtended;
    String dbName;

    if (ast.getChildCount() == 1) {
      dbName = stripQuotes(ast.getChild(0).getText());
      isExtended = false;
    } else if (ast.getChildCount() == 2) {
      dbName = stripQuotes(ast.getChild(0).getText());
      isExtended = true;
    } else {
      throw new SemanticException("Unexpected Tokens at DESCRIBE DATABASE");
    }

    DescDatabaseDesc descDbDesc = new DescDatabaseDesc(ctx.getResFile(),
        dbName, isExtended);
    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(), descDbDesc), conf));
    setFetchTask(createFetchTask(descDbDesc.getSchema()));
  }

  //解析table的partion
  //eg:date=20150831,task=share
  //sql:PARTITION (name=value,name=value,name)
  private static HashMap<String, String> getPartSpec(ASTNode partspec)
      throws SemanticException {
    if (partspec == null) {
      return null;
    }
    HashMap<String, String> partSpec = new LinkedHashMap<String, String>();
    for (int i = 0; i < partspec.getChildCount(); ++i) {
      ASTNode partspec_val = (ASTNode) partspec.getChild(i);
      String key = partspec_val.getChild(0).getText();
      String val = null;
      if (partspec_val.getChildCount() > 1) {
        val = stripQuotes(partspec_val.getChild(1).getText());
      }
      partSpec.put(key.toLowerCase(), val);
    }
    return partSpec;
  }

  /**
   * 展示某个表的某个partition信息
   * SHOW PARTITIONS xxx PARTITION (name=value,name=value,name)
   */
  private void analyzeShowPartitions(ASTNode ast) throws SemanticException {
    ShowPartitionsDesc showPartsDesc;
    String tableName = getUnescapedName((ASTNode) ast.getChild(0));
    //虽然这个结果看允许多个partition分区被在sql中,但是最终代码获取了仅仅1个分区,即该sql只能有一个分区语句被展现
    List<Map<String, String>> partSpecs = getPartitionSpecs(ast);
    // We only can have a single partition spec
    assert (partSpecs.size() <= 1);
    Map<String, String> partSpec = null;//最终要show的分区
    if (partSpecs.size() > 0) {
      partSpec = partSpecs.get(0);
    }

    validateTable(tableName, null);

    showPartsDesc = new ShowPartitionsDesc(tableName, ctx.getResFile(), partSpec);
    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
        showPartsDesc), conf));
    setFetchTask(createFetchTask(showPartsDesc.getSchema()));
  }

  private void analyzeShowCreateTable(ASTNode ast) throws SemanticException {
    ShowCreateTableDesc showCreateTblDesc;
    String tableName = getUnescapedName((ASTNode)ast.getChild(0));
    showCreateTblDesc = new ShowCreateTableDesc(tableName, ctx.getResFile().toString());

    Table tab = getTable(tableName);
    if (tab.getTableType() == org.apache.hadoop.hive.metastore.TableType.INDEX_TABLE) {
      throw new SemanticException(ErrorMsg.SHOW_CREATETABLE_INDEX.getMsg(tableName
          + " has table type INDEX_TABLE"));
    }
    inputs.add(new ReadEntity(tab));
    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
        showCreateTblDesc), conf));
    setFetchTask(createFetchTask(showCreateTblDesc.getSchema()));
  }

  private void analyzeShowDatabases(ASTNode ast) throws SemanticException {
    ShowDatabasesDesc showDatabasesDesc;
    if (ast.getChildCount() == 1) {
      String databasePattern = unescapeSQLString(ast.getChild(0).getText());
      showDatabasesDesc = new ShowDatabasesDesc(ctx.getResFile(), databasePattern);
    } else {
      showDatabasesDesc = new ShowDatabasesDesc(ctx.getResFile());
    }
    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(), showDatabasesDesc), conf));
    setFetchTask(createFetchTask(showDatabasesDesc.getSchema()));
  }

  private void analyzeShowTables(ASTNode ast) throws SemanticException {
    ShowTablesDesc showTblsDesc;
    String dbName = SessionState.get().getCurrentDatabase();
    String tableNames = null;

    if (ast.getChildCount() > 3) {
      throw new SemanticException(ErrorMsg.GENERIC_ERROR.getMsg());
    }

    switch (ast.getChildCount()) {
    case 1: // Uses a pattern
      tableNames = unescapeSQLString(ast.getChild(0).getText());
      showTblsDesc = new ShowTablesDesc(ctx.getResFile(), dbName, tableNames);
      break;
    case 2: // Specifies a DB
      assert (ast.getChild(0).getType() == HiveParser.TOK_FROM);
      dbName = unescapeIdentifier(ast.getChild(1).getText());
      validateDatabase(dbName);
      showTblsDesc = new ShowTablesDesc(ctx.getResFile(), dbName);
      break;
    case 3: // Uses a pattern and specifies a DB
      assert (ast.getChild(0).getType() == HiveParser.TOK_FROM);
      dbName = unescapeIdentifier(ast.getChild(1).getText());
      tableNames = unescapeSQLString(ast.getChild(2).getText());
      validateDatabase(dbName);
      showTblsDesc = new ShowTablesDesc(ctx.getResFile(), dbName, tableNames);
      break;
    default: // No pattern or DB
      showTblsDesc = new ShowTablesDesc(ctx.getResFile(), dbName);
      break;
    }

    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
        showTblsDesc), conf));
    setFetchTask(createFetchTask(showTblsDesc.getSchema()));
  }

  private void analyzeShowColumns(ASTNode ast) throws SemanticException {
    ShowColumnsDesc showColumnsDesc;
    String dbName = null;
    String tableName = null;
    switch (ast.getChildCount()) {
    case 1:
      tableName = getUnescapedName((ASTNode) ast.getChild(0));
      break;
    case 2:
      dbName = getUnescapedName((ASTNode) ast.getChild(0));
      tableName = getUnescapedName((ASTNode) ast.getChild(1));
      break;
    default:
      break;
    }

    Table tab = getTable(dbName, tableName, true);
    inputs.add(new ReadEntity(tab));

    showColumnsDesc = new ShowColumnsDesc(ctx.getResFile(), dbName, tableName);
    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
        showColumnsDesc), conf));
    setFetchTask(createFetchTask(showColumnsDesc.getSchema()));
  }

  //SHOW TABLE EXTENDED [ (from | in) db_name ] like tableName [  PARTITION (name=value,name=value,name) ]
  private void analyzeShowTableStatus(ASTNode ast) throws SemanticException {
    ShowTableStatusDesc showTblStatusDesc;
    String tableNames = getUnescapedName((ASTNode) ast.getChild(0));
    String dbName = SessionState.get().getCurrentDatabase();
    int children = ast.getChildCount();
    HashMap<String, String> partSpec = null;
    if (children >= 2) {
      if (children > 3) {
        throw new SemanticException(ErrorMsg.GENERIC_ERROR.getMsg());
      }
      for (int i = 1; i < children; i++) {
        ASTNode child = (ASTNode) ast.getChild(i);
        if (child.getToken().getType() == HiveParser.Identifier) {
          dbName = unescapeIdentifier(child.getText());
        } else if (child.getToken().getType() == HiveParser.TOK_PARTSPEC) {
          partSpec = getPartSpec(child);
        } else {
          throw new SemanticException(ErrorMsg.GENERIC_ERROR.getMsg());
        }
      }
    }

    if (partSpec != null) {
      validateTable(tableNames, partSpec);
    }

    showTblStatusDesc = new ShowTableStatusDesc(ctx.getResFile().toString(), dbName,
        tableNames, partSpec);
    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
        showTblStatusDesc), conf));
    setFetchTask(createFetchTask(showTblStatusDesc.getSchema()));
  }

  private void analyzeShowTableProperties(ASTNode ast) throws SemanticException {
    ShowTblPropertiesDesc showTblPropertiesDesc;
    String tableNames = getUnescapedName((ASTNode) ast.getChild(0));
    String dbName = SessionState.get().getCurrentDatabase();
    String propertyName = null;
    if (ast.getChildCount() > 1) {
      propertyName = unescapeSQLString(ast.getChild(1).getText());
    }

    validateTable(tableNames, null);

    showTblPropertiesDesc = new ShowTblPropertiesDesc(ctx.getResFile().toString(), tableNames,
        propertyName);
    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
        showTblPropertiesDesc), conf));
    setFetchTask(createFetchTask(showTblPropertiesDesc.getSchema()));
  }

  private void analyzeShowIndexes(ASTNode ast) throws SemanticException {
    ShowIndexesDesc showIndexesDesc;
    String tableName = getUnescapedName((ASTNode) ast.getChild(0));
    showIndexesDesc = new ShowIndexesDesc(tableName, ctx.getResFile());

    if (ast.getChildCount() == 2) {
      int descOptions = ast.getChild(1).getType();
      showIndexesDesc.setFormatted(descOptions == HiveParser.KW_FORMATTED);
    }

    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
        showIndexesDesc), conf));
    setFetchTask(createFetchTask(showIndexesDesc.getSchema()));
  }

  /**
   * Add the task according to the parsed command tree. This is used for the CLI
   * command "SHOW FUNCTIONS;".
   *
   * @param ast
   *          The parsed command tree.
   * @throws SemanticException
   *           Parsin failed
   */
  private void analyzeShowFunctions(ASTNode ast) throws SemanticException {
    ShowFunctionsDesc showFuncsDesc;
    if (ast.getChildCount() == 1) {
      String funcNames = stripQuotes(ast.getChild(0).getText());
      showFuncsDesc = new ShowFunctionsDesc(ctx.getResFile(), funcNames);
    } else {
      showFuncsDesc = new ShowFunctionsDesc(ctx.getResFile());
    }
    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
        showFuncsDesc), conf));
    setFetchTask(createFetchTask(showFuncsDesc.getSchema()));
  }

  /**
   * Add the task according to the parsed command tree. This is used for the CLI
   * command "SHOW LOCKS;".
   *
   * @param ast
   *          The parsed command tree.
   * @throws SemanticException
   *           Parsing failed
   */
  private void analyzeShowLocks(ASTNode ast) throws SemanticException {
    String tableName = null;
    HashMap<String, String> partSpec = null;
    boolean isExtended = false;

    if (ast.getChildCount() >= 1) {
      // table for which show locks is being executed
      for (int i = 0; i < ast.getChildCount(); i++) {
        ASTNode child = (ASTNode) ast.getChild(i);
        if (child.getType() == HiveParser.TOK_TABTYPE) {
          ASTNode tableTypeExpr = (ASTNode) child;
          tableName =
            QualifiedNameUtil.getFullyQualifiedName((ASTNode) tableTypeExpr.getChild(0));
          // get partition metadata if partition specified
          if (tableTypeExpr.getChildCount() == 2) {
            ASTNode partspec = (ASTNode) tableTypeExpr.getChild(1);
            partSpec = getPartSpec(partspec);
          }
        } else if (child.getType() == HiveParser.KW_EXTENDED) {
          isExtended = true;
        }
      }
    }

    ShowLocksDesc showLocksDesc = new ShowLocksDesc(ctx.getResFile(), tableName,
        partSpec, isExtended);
    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
        showLocksDesc), conf));
    setFetchTask(createFetchTask(showLocksDesc.getSchema()));

    // Need to initialize the lock manager
    ctx.setNeedLockMgr(true);
  }

  /**
   * Add the task according to the parsed command tree. This is used for the CLI
   * command "LOCK TABLE ..;".
   *
   * @param ast
   *          The parsed command tree.
   * @throws SemanticException
   *           Parsing failed
   */
  private void analyzeLockTable(ASTNode ast)
      throws SemanticException {
    String tableName = getUnescapedName((ASTNode) ast.getChild(0)).toLowerCase();
    String mode = unescapeIdentifier(ast.getChild(1).getText().toUpperCase());
    List<Map<String, String>> partSpecs = getPartitionSpecs(ast);

    // We only can have a single partition spec
    assert (partSpecs.size() <= 1);
    Map<String, String> partSpec = null;
    if (partSpecs.size() > 0) {
      partSpec = partSpecs.get(0);
    }

    LockTableDesc lockTblDesc = new LockTableDesc(tableName, mode, partSpec,
        HiveConf.getVar(conf, ConfVars.HIVEQUERYID));
    lockTblDesc.setQueryStr(this.ctx.getCmd());
    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
        lockTblDesc), conf));

    // Need to initialize the lock manager
    ctx.setNeedLockMgr(true);
  }

  /**
   * Add the task according to the parsed command tree. This is used for the CLI
   * command "UNLOCK TABLE ..;".
   *
   * @param ast
   *          The parsed command tree.
   * @throws SemanticException
   *           Parsing failed
   */
  private void analyzeUnlockTable(ASTNode ast)
      throws SemanticException {
    String tableName = getUnescapedName((ASTNode) ast.getChild(0));
    List<Map<String, String>> partSpecs = getPartitionSpecs(ast);

    // We only can have a single partition spec
    assert (partSpecs.size() <= 1);
    Map<String, String> partSpec = null;
    if (partSpecs.size() > 0) {
      partSpec = partSpecs.get(0);
    }

    UnlockTableDesc unlockTblDesc = new UnlockTableDesc(tableName, partSpec);
    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
        unlockTblDesc), conf));

    // Need to initialize the lock manager
    ctx.setNeedLockMgr(true);
  }

  /**
   * Add the task according to the parsed command tree. This is used for the CLI
   * command "DESCRIBE FUNCTION;".
   *
   * @param ast
   *          The parsed command tree.
   * @throws SemanticException
   *           Parsing failed
   */
  private void analyzeDescFunction(ASTNode ast) throws SemanticException {
    String funcName;
    boolean isExtended;

    if (ast.getChildCount() == 1) {
      funcName = stripQuotes(ast.getChild(0).getText());
      isExtended = false;
    } else if (ast.getChildCount() == 2) {
      funcName = stripQuotes(ast.getChild(0).getText());
      isExtended = true;
    } else {
      throw new SemanticException("Unexpected Tokens at DESCRIBE FUNCTION");
    }

    DescFunctionDesc descFuncDesc = new DescFunctionDesc(ctx.getResFile(),
        funcName, isExtended);
    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
        descFuncDesc), conf));
    setFetchTask(createFetchTask(descFuncDesc.getSchema()));
  }


  private void analyzeAlterTableRename(ASTNode ast, boolean expectView) throws SemanticException {
    String tblName = getUnescapedName((ASTNode) ast.getChild(0));
    AlterTableDesc alterTblDesc = new AlterTableDesc(tblName,
        getUnescapedName((ASTNode) ast.getChild(1)), expectView);

    addInputsOutputsAlterTable(tblName, null, alterTblDesc);
    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
        alterTblDesc), conf));
  }

  private void analyzeAlterTableRenameCol(ASTNode ast) throws SemanticException {
    String tblName = getUnescapedName((ASTNode) ast.getChild(0));
    String newComment = null;
    String newType = null;
    newType = getTypeStringFromAST((ASTNode) ast.getChild(3));
    boolean first = false;
    String flagCol = null;
    ASTNode positionNode = null;
    if (ast.getChildCount() == 6) {
      newComment = unescapeSQLString(ast.getChild(4).getText());
      positionNode = (ASTNode) ast.getChild(5);
    } else if (ast.getChildCount() == 5) {
      if (ast.getChild(4).getType() == HiveParser.StringLiteral) {
        newComment = unescapeSQLString(ast.getChild(4).getText());
      } else {
        positionNode = (ASTNode) ast.getChild(4);
      }
    }

    if (positionNode != null) {
      if (positionNode.getChildCount() == 0) {
        first = true;
      } else {
        flagCol = unescapeIdentifier(positionNode.getChild(0).getText());
      }
    }

    String oldColName = ast.getChild(1).getText();
    String newColName = ast.getChild(2).getText();

    /* Validate the operation of renaming a column name. */
    Table tab = getTable(tblName);

    SkewedInfo skewInfo = tab.getTTable().getSd().getSkewedInfo();
    if ((null != skewInfo)
        && (null != skewInfo.getSkewedColNames())
        && skewInfo.getSkewedColNames().contains(oldColName)) {
      throw new SemanticException(oldColName
          + ErrorMsg.ALTER_TABLE_NOT_ALLOWED_RENAME_SKEWED_COLUMN.getMsg());
    }

    AlterTableDesc alterTblDesc = new AlterTableDesc(tblName,
        unescapeIdentifier(oldColName), unescapeIdentifier(newColName),
        newType, newComment, first, flagCol);
    addInputsOutputsAlterTable(tblName, null, alterTblDesc);

    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
        alterTblDesc), conf));
  }

  /**
   * alterStatementSuffixRenamePart 为table修改新的partition属性
   * RENAME TO PARTITION (name=value,name=value,name)
   * 
   * @param tblName 表示为哪个表进行重新修改分区属性 
   * @param oldPartSpec 老版本的分区属性集合
   */
  private void analyzeAlterTableRenamePart(ASTNode ast, String tblName,
      HashMap<String, String> oldPartSpec) throws SemanticException{
	  //获取新的分区属性集合信息
    Map<String, String> newPartSpec = extractPartitionSpecs((ASTNode) ast.getChild(0));
    if (newPartSpec == null) {
      throw new SemanticException("RENAME PARTITION Missing Destination" + ast);
    }
    //获取数据库表对象
    Table tab = getTable(tblName, true);
    validateAlterTableType(tab, AlterTableTypes.RENAMEPARTITION);
    inputs.add(new ReadEntity(tab));

    //新老版本的分区信息
    List<Map<String, String>> partSpecs = new ArrayList<Map<String, String>>();
    partSpecs.add(oldPartSpec);
    partSpecs.add(newPartSpec);
    addTablePartsOutputs(tblName, partSpecs);
    
    RenamePartitionDesc renamePartitionDesc = new RenamePartitionDesc(
        SessionState.get().getCurrentDatabase(), tblName, oldPartSpec, newPartSpec);
    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
        renamePartitionDesc), conf));
  }

  /**
 alterStatementSuffixBucketNum
 INTO number BUCKETS
   */
  private void analyzeAlterTableBucketNum(ASTNode ast, String tblName,
      HashMap<String, String> partSpec) throws SemanticException {
    Table tab = getTable(tblName, true);
    if (tab.getBucketCols() == null || tab.getBucketCols().isEmpty()) {
      throw new SemanticException(ErrorMsg.ALTER_BUCKETNUM_NONBUCKETIZED_TBL.getMsg());
    }
    validateAlterTableType(tab, AlterTableTypes.ALTERBUCKETNUM);
    inputs.add(new ReadEntity(tab));

    //解析 INTO number BUCKETS
    int bucketNum = Integer.parseInt(ast.getChild(0).getText());
    AlterTableDesc alterBucketNum = new AlterTableDesc(tblName, partSpec, bucketNum);

    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
        alterBucketNum), conf));
  }

  /**
   * 修改表的属性 String ADD|REPLACE COLUMNS (columnNameTypeList)
   */
  private void analyzeAlterTableModifyCols(ASTNode ast,
      AlterTableTypes alterType) throws SemanticException {
    String tblName = getUnescapedName((ASTNode) ast.getChild(0));
    List<FieldSchema> newCols = getColumns((ASTNode) ast.getChild(1));
    
    AlterTableDesc alterTblDesc = new AlterTableDesc(tblName, newCols,
        alterType);

    addInputsOutputsAlterTable(tblName, null, alterTblDesc);
    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
        alterTblDesc), conf));
  }

  /**
删除一个数据库表的某些partition
String DROP [IF Exists] PARTITION(key 符号 value,key 符号 value),PARTITION( key 符号 value,key 符号 value) [IGNORE PROTECTION]
注意:符号 = 、 == 、 <>、 != 、 <= 、< 、 < 、 >=
@param expectView,true表示是一个视图,false表示是一个实体表
   */
  private void analyzeAlterTableDropParts(ASTNode ast, boolean expectView)
      throws SemanticException {

    String tblName = getUnescapedName((ASTNode) ast.getChild(0));
    // get table metadata
    //解析PARTITION(key 符号 value,key 符号 value),PARTITION( key 符号 value,key 符号 value)
    List<PartitionSpec> partSpecs = getFullPartitionSpecs(ast);
    Table tab = getTable(tblName, true);//获取数据库表对象
    
    //校验
    validateAlterTableType(tab, AlterTableTypes.DROPPARTITION, expectView);
    inputs.add(new ReadEntity(tab));

    // Find out if all partition columns are strings. This is needed for JDO
    //校验是否所有的partition属性都是String类型的,false表示有不是String类型的
    boolean stringPartitionColumns = true;
    List<FieldSchema> partCols = tab.getPartCols();

    for (FieldSchema partCol : partCols) {
      if (!partCol.getType().toLowerCase().equals("string")) {
        stringPartitionColumns = false;
        break;
      }
    }

    // Only equality is supported for non-string partition columns
    //删除某一个表的某些分区的时候,如果分区的属性类型不是String的,只允许进行=号操作删除,不允许进行非等号的操作,例如>=等操作
    if (!stringPartitionColumns) {//说明不是String类型的partition属性
      for (PartitionSpec partSpec : partSpecs) {
        if (partSpec.isNonEqualityOperator()) {//true表示所有的操作中有非=号操作,false表示所有的属性都是=号操作
          throw new SemanticException(
              ErrorMsg.DROP_PARTITION_NON_STRING_PARTCOLS_NONEQUALITY.getMsg());
        }
      }
    }

    //获取IGNORE PROTECTION属性
    boolean ignoreProtection = (ast.getFirstChildWithType(HiveParser.TOK_IGNOREPROTECTION) != null);
    if (partSpecs != null) {
      boolean ifExists = (ast.getFirstChildWithType(HiveParser.TOK_IFEXISTS) != null);
      // we want to signal an error if the partition doesn't exist and we're
      // configured not to fail silently
      boolean throwException =
          !ifExists && !HiveConf.getBoolVar(conf, ConfVars.DROPIGNORESNONEXISTENT);
      //为某一个数据库表删除一些partition分区的时候,调用该方法
      addTableDropPartsOutputs(tblName, partSpecs, throwException,
                                stringPartitionColumns, ignoreProtection);
    }
    //生成任务对象
    DropTableDesc dropTblDesc =
        new DropTableDesc(tblName, partSpecs, expectView, stringPartitionColumns, ignoreProtection);

    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
        dropTblDesc), conf));
  }

  private void analyzeAlterTableAlterParts(ASTNode ast)
      throws SemanticException {
    // get table name
    String tblName = getUnescapedName((ASTNode)ast.getChild(0));

    Table tab = null;

    // check if table exists.
    try {
      tab = db.getTable(SessionState.get().getCurrentDatabase(), tblName, true);
      inputs.add(new ReadEntity(tab));
    } catch (HiveException e) {
      throw new SemanticException(ErrorMsg.INVALID_TABLE.getMsg(tblName));
    }

    // validate the DDL is a valid operation on the table.
    validateAlterTableType(tab, AlterTableTypes.ALTERPARTITION, false);

    // Alter table ... partition column ( column newtype) only takes one column at a time.
    // It must have a column name followed with type.
    ASTNode colAst = (ASTNode) ast.getChild(1);
    assert(colAst.getChildCount() == 2);

    FieldSchema newCol = new FieldSchema();

    // get column name
    String name = colAst.getChild(0).getText().toLowerCase();
    newCol.setName(unescapeIdentifier(name));

    // get column type
    ASTNode typeChild = (ASTNode) (colAst.getChild(1));
    newCol.setType(getTypeStringFromAST(typeChild));

    // check if column is defined or not
    boolean fFoundColumn = false;
    for( FieldSchema col : tab.getTTable().getPartitionKeys()) {
      if (col.getName().compareTo(newCol.getName()) == 0) {
        fFoundColumn = true;
      }
    }

    // raise error if we could not find the column
    if (!fFoundColumn) {
      throw new SemanticException(ErrorMsg.INVALID_COLUMN.getMsg(newCol.getName()));
    }

    AlterTableAlterPartDesc alterTblAlterPartDesc =
            new AlterTableAlterPartDesc(SessionState.get().getCurrentDatabase(), tblName, newCol);

    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
            alterTblAlterPartDesc), conf));
  }

    /**
   * Add one or more partitions to a table. Useful when the data has been copied
   * to the right location by some other process.
   *
   * @param ast
   *          The parsed command tree.
   *
   * @param expectView
   *          True for ALTER VIEW, false for ALTER TABLE.
   *
   * @throws SemanticException
   *           Parsing failed
   * 为视图和table添加partition分区
   * String ADD [IF NOT Exists] PARTITION (name=value,name=value,name) [LOCATION String] PARTITION (name=value,name=value,name) [LOCATION String]..
   */
  private void analyzeAlterTableAddParts(CommonTree ast, boolean expectView)
      throws SemanticException {

    // ^(TOK_ALTERTABLE_ADDPARTS identifier ifNotExists? alterStatementSuffixAddPartitionsElement+)
    String tblName = getUnescapedName((ASTNode)ast.getChild(0));
    boolean ifNotExists = ast.getChild(1).getType() == HiveParser.TOK_IFNOTEXISTS;

    Table tab = getTable(tblName, true);
    boolean isView = tab.isView();
    validateAlterTableType(tab, AlterTableTypes.ADDPARTITION, expectView);
    inputs.add(new ReadEntity(tab));

    //表示解析PARTITION (name=value,name=value,name) [LOCATION String] PARTITION (name=value,name=value,name) [LOCATION String]..之后的集合
    List<AddPartitionDesc> partitionDescs = new ArrayList<AddPartitionDesc>();

    int numCh = ast.getChildCount();
    int start = ifNotExists ? 2 : 1;

    /**
     * 解析PARTITION (name=value,name=value,name) [LOCATION String] PARTITION (name=value,name=value,name) [LOCATION String]..
     */
    String currentLocation = null;//存储路径
    Map<String, String> currentPart = null;//添加的分区
    for (int num = start; num < numCh; num++) {
      ASTNode child = (ASTNode) ast.getChild(num);
      switch (child.getToken().getType()) {
      case HiveParser.TOK_PARTSPEC://解析PARTITION (name=value,name=value,name)
        if (currentPart != null) {//说明已经存在一个currentPart了,因此处理上一个currentPart分区数据
          //getPartitionForOutput
          Partition partition = getPartitionForOutput(tab, currentPart);
          if (partition == null || !ifNotExists) {
            AddPartitionDesc addPartitionDesc = new AddPartitionDesc(
              tab.getDbName(), tblName, currentPart,
              currentLocation, ifNotExists, expectView);
            partitionDescs.add(addPartitionDesc);
          }
          currentLocation = null;
        }
        currentPart = getPartSpec(child);//解析PARTITION (name=value,name=value,name)返回Map
        //校验该分区是否在table中符合条件
        validatePartSpec(tab, currentPart, (ASTNode)child, conf);
        break;
      case HiveParser.TOK_PARTITIONLOCATION:
        // if location specified, set in partition 解析LOCATION String
        currentLocation = unescapeSQLString(child.getChild(0).getText());
        break;
      default:
        throw new SemanticException("Unknown child: " + child);
      }
    }

    // add the last one 将最后一个处理一下
    if (currentPart != null) {
      Partition partition = getPartitionForOutput(tab, currentPart);
      if (partition == null || !ifNotExists) {
        AddPartitionDesc addPartitionDesc = new AddPartitionDesc(
          tab.getDbName(), tblName, currentPart,
          currentLocation, ifNotExists, expectView);
        partitionDescs.add(addPartitionDesc);
      }
    }

    //没有分区被添加,则直接返回
    if (partitionDescs.isEmpty()) {
      // nothing to do
      return;
    }

    /**
   * 校验该table的分区
   * 参数分区是否合法
   * 1.table本身没有分区,参数有分区,则非法
   * 2.分区字段总数不一致,非法
   * 3.必须每一个分区字段参数中都存在
     */
    for (AddPartitionDesc addPartitionDesc : partitionDescs) {
      try {
        tab.isValidSpec(addPartitionDesc.getPartSpec());
      } catch (HiveException ex) {
        throw new SemanticException(ErrorMsg.INVALID_PARTITION_SPEC.getMsg(ex.getMessage()));
      }
      rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
          addPartitionDesc), conf));
    }

    /**
     * 如果是视图,要去执行一个sql
     * select * from biao where (key=value and key=value) or (key=value and key=value) ..
     * 注意:每一个PARTITION (name=value,name=value,name)对应一组(key=value and key=value)
     */
    if (isView) {
      // Compile internal query to capture underlying table partition
      // dependencies
      StringBuilder cmd = new StringBuilder();
      cmd.append("SELECT * FROM ");
      cmd.append(HiveUtils.unparseIdentifier(tblName));
      cmd.append(" WHERE ");
      boolean firstOr = true;
      for (AddPartitionDesc partitionDesc : partitionDescs) {
        // Perform this check early so that we get a better error message.
        try {
          // Note that isValidSpec throws an exception (it never
          // actually returns false).
            /**
        	   * 校验该table的分区
        	   * 参数分区是否合法
        	   * 1.table本身没有分区,参数有分区,则非法
        	   * 2.分区字段总数不一致,非法
        	   * 3.必须每一个分区字段参数中都存在
        	     */
          tab.isValidSpec(partitionDesc.getPartSpec());
        } catch (HiveException ex) {
          throw new SemanticException(ErrorMsg.INVALID_PARTITION_SPEC.getMsg(ex.getMessage()));
        }
        if (firstOr) {
          firstOr = false;
        } else {
          cmd.append(" OR ");
        }
        boolean firstAnd = true;
        cmd.append("(");
        for (Map.Entry<String, String> entry : partitionDesc.getPartSpec().entrySet())
        {
          if (firstAnd) {
            firstAnd = false;
          } else {
            cmd.append(" AND ");
          }
          cmd.append(HiveUtils.unparseIdentifier(entry.getKey()));
          cmd.append(" = '");
          cmd.append(HiveUtils.escapeString(entry.getValue()));
          cmd.append("'");
        }
        cmd.append(")");
      }
      Driver driver = new Driver(conf);
      int rc = driver.compile(cmd.toString());
      if (rc != 0) {
        throw new SemanticException(ErrorMsg.NO_VALID_PARTN.getMsg());
      }
      inputs.addAll(driver.getPlan().getInputs());
    }
  }

  //查找table中指定的分区
  private Partition getPartitionForOutput(Table tab, Map<String, String> currentPart)
    throws SemanticException {
	//校验分区名字不允许是在保留字中
    validatePartitionValues(currentPart);
    try {
      //查找指定table对应的分区
      Partition partition = db.getPartition(tab, currentPart, false);
      if (partition != null) {//将分区添加到输出中
        outputs.add(new WriteEntity(partition));
      }
      return partition;
    } catch (HiveException e) {
      LOG.warn("wrong partition spec " + currentPart);
    }
    return null;
  }

  /**
   * Rewrite the metadata for one or more partitions in a table. Useful when
   * an external process modifies files on HDFS and you want the pre/post
   * hooks to be fired for the specified partition.
   *
   * @param ast
   *          The parsed command tree.
   * @throws SemanticException
   *           Parsin failed
   * 重新写回关于table的partition下的元数据信息
   * String TOUCH PARTITION (name=value,name=value,name) PARTITION (name=value,name=value,name)..
   */
  private void analyzeAlterTableTouch(CommonTree ast)
      throws SemanticException {

    String tblName = getUnescapedName((ASTNode)ast.getChild(0));//表名字
    Table tab = getTable(tblName, true);
    validateAlterTableType(tab, AlterTableTypes.TOUCH);
    inputs.add(new ReadEntity(tab));

    // partition name to value
    List<Map<String, String>> partSpecs = getPartitionSpecs(ast);

    if (partSpecs.size() == 0) {
      AlterTableSimpleDesc touchDesc = new AlterTableSimpleDesc(
          SessionState.get().getCurrentDatabase(), tblName, null,
          AlterTableDesc.AlterTableTypes.TOUCH);
      outputs.add(new WriteEntity(tab));
      rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
          touchDesc), conf));
    } else {
      addTablePartsOutputs(tblName, partSpecs);
      for (Map<String, String> partSpec : partSpecs) {
        AlterTableSimpleDesc touchDesc = new AlterTableSimpleDesc(
            SessionState.get().getCurrentDatabase(), tblName, partSpec,
            AlterTableDesc.AlterTableTypes.TOUCH);
        rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
            touchDesc), conf));
      }
    }
  }

  /**
   * String UNARCHIVE PARTITION (name=value,name=value,name) PARTITION (name=value,name=value,name)... 第二个参数为true
   * String ARCHIVE PARTITION (name=value,name=value,name) PARTITION (name=value,name=value,name)...第二个参数为false
   * Hive中的归档移动分区中的文件到Hadoop归档中（HAR），该语句只会减少文件的数量，但不提供压缩。
   */
  private void analyzeAlterTableArchive(CommonTree ast, boolean isUnArchive)
      throws SemanticException {

	  //不支持ARCHIVE PARTITION命令归档partition的数据成hadoop的har文件,抛异常
    if (!conf.getBoolVar(HiveConf.ConfVars.HIVEARCHIVEENABLED)) {
      throw new SemanticException(ErrorMsg.ARCHIVE_METHODS_DISABLED.getMsg());
    }
    
    //针对哪个数据库表的哪些分区进行归档
    String tblName = getUnescapedName((ASTNode) ast.getChild(0));
    // partition name to value
    List<Map<String, String>> partSpecs = getPartitionSpecs(ast);

    Table tab = getTable(tblName, true);
    addTablePartsOutputs(tblName, partSpecs, true);
    validateAlterTableType(tab, AlterTableTypes.ARCHIVE);
    inputs.add(new ReadEntity(tab));

    //ARCHIVE PARTITION命令归档partition的数据成hadoop的har文件,必须要有一个partition,不能大于1
    if (partSpecs.size() > 1) {
      throw new SemanticException(isUnArchive ?
          ErrorMsg.UNARCHIVE_ON_MULI_PARTS.getMsg() :
          ErrorMsg.ARCHIVE_ON_MULI_PARTS.getMsg());
    }
    
    //ARCHIVE PARTITION命令归档partition的数据成hadoop的har文件,必须要有一个partition
    if (partSpecs.size() == 0) {
      throw new SemanticException(ErrorMsg.ARCHIVE_ON_TABLE.getMsg());
    }

    //获取该分区的属性集合,并且进行校验,等待校验的数据,例如ds='2008-04-08', min='30',如果规则是ds, hr, min,则校验分区失败
    Map<String, String> partSpec = partSpecs.get(0);
    try {
      isValidPrefixSpec(tab, partSpec);
    } catch (HiveException e) {
      throw new SemanticException(e.getMessage(), e);
    }
    
    AlterTableSimpleDesc archiveDesc = new AlterTableSimpleDesc(
        SessionState.get().getCurrentDatabase(), tblName, partSpec,
        (isUnArchive ? AlterTableTypes.UNARCHIVE : AlterTableTypes.ARCHIVE));
    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
        archiveDesc), conf));
  }

  /**
   * Verify that the information in the metastore matches up with the data on
   * the fs.
   *
   * @param ast
   *          Query tree.
   * @throws SemanticException
   */
  private void analyzeMetastoreCheck(CommonTree ast) throws SemanticException {
    String tableName = null;
    boolean repair = false;
    if (ast.getChildCount() > 0) {
      repair = ast.getChild(0).getType() == HiveParser.KW_REPAIR;
      if (!repair) {
        tableName = getUnescapedName((ASTNode) ast.getChild(0));
      } else if (ast.getChildCount() > 1) {
        tableName = getUnescapedName((ASTNode) ast.getChild(1));
      }
    }
    List<Map<String, String>> specs = getPartitionSpecs(ast);
    MsckDesc checkDesc = new MsckDesc(tableName, specs, ctx.getResFile(),
        repair);
    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
        checkDesc), conf));
  }

  /**
   * Get the partition specs from the tree.
   *
   * @param ast
   *          Tree to extract partitions from.
   * @return A list of partition name to value mappings.
   * @throws SemanticException
   * 解析PARTITION (name=value,name=value,name) PARTITION (name=value,name=value,name)..
   */
  private List<Map<String, String>> getPartitionSpecs(CommonTree ast)
      throws SemanticException {
    List<Map<String, String>> partSpecs = new ArrayList<Map<String, String>>();
    int childIndex = 0;
    // get partition metadata if partition specified
    for (childIndex = 1; childIndex < ast.getChildCount(); childIndex++) {
      Tree partspec = ast.getChild(childIndex);
      // sanity check
      if (partspec.getType() == HiveParser.TOK_PARTSPEC) {
        partSpecs.add(getPartSpec((ASTNode) partspec));
      }
    }
    return partSpecs;
  }

  /**
   * Get the partition specs from the tree. This stores the full specification
   * with the comparator operator into the output list.
   *
   * @param ast
   *          Tree to extract partitions from.
   * @return A list of PartitionSpec objects which contain the mapping from
   *         key to operator and value.
   * @throws SemanticException
   * 解析 PARTITION(key 符号 value,key 符号 value),PARTITION( key 符号 value,key 符号 value) [IGNORE PROTECTION]
   * 注意:符号 = 、 == 、 <>、 != 、 <= 、< 、 < 、 >= 
   * 即通过范围匹配要删除的数据库表的属性范围
   */
  private List<PartitionSpec> getFullPartitionSpecs(CommonTree ast)
      throws SemanticException {
    List<PartitionSpec> partSpecList = new ArrayList<PartitionSpec>();

    for (int childIndex = 1; childIndex < ast.getChildCount(); childIndex++) {
      Tree partSpecTree = ast.getChild(childIndex);
      if (partSpecTree.getType() == HiveParser.TOK_PARTSPEC) {
        PartitionSpec partSpec = new PartitionSpec();

        for (int i = 0; i < partSpecTree.getChildCount(); ++i) {
          CommonTree partSpecSingleKey = (CommonTree) partSpecTree.getChild(i);
          assert (partSpecSingleKey.getType() == HiveParser.TOK_PARTVAL);
          String key = partSpecSingleKey.getChild(0).getText().toLowerCase();//解析key
          String operator = partSpecSingleKey.getChild(1).getText();//解析操作符号
          String val = partSpecSingleKey.getChild(2).getText();//解析value
          partSpec.addPredicate(key, operator, val);
        }
        partSpecList.add(partSpec);
      }
    }
    return partSpecList;
  }

  /**
   * Certain partition values are are used by hive. e.g. the default partition
   * in dynamic partitioning and the intermediate partition values used in the
   * archiving process. Naturally, prohibit the user from creating partitions
   * with these reserved values. The check that this function is more
   * restrictive than the actual limitation, but it's simpler. Should be okay
   * since the reserved names are fairly long and uncommon.
   * 校验分区名字不允许是在保留字中
   */
  private void validatePartitionValues(Map<String, String> partSpec)
      throws SemanticException {

    for (Entry<String, String> e : partSpec.entrySet()) {
      for (String s : reservedPartitionValues) {
        if (e.getValue().contains(s)) {
          throw new SemanticException(ErrorMsg.RESERVED_PART_VAL.getMsg(
              "(User value: " + e.getValue() + " Reserved substring: " + s + ")"));
        }
      }
    }
  }

  /**
   * Add the table partitions to be modified in the output, so that it is available for the
   * pre-execution hook. If the partition does not exist, no error is thrown.
   */
  private void addTablePartsOutputs(String tblName, List<Map<String, String>> partSpecs)
      throws SemanticException {
    addTablePartsOutputs(tblName, partSpecs, false, false, null);
  }

  /**
   * Add the table partitions to be modified in the output, so that it is available for the
   * pre-execution hook. If the partition does not exist, no error is thrown.
   */
  private void addTablePartsOutputs(String tblName, List<Map<String, String>> partSpecs,
      boolean allowMany)
      throws SemanticException {
    addTablePartsOutputs(tblName, partSpecs, false, allowMany, null);
  }

  /**
   * Add the table partitions to be modified in the output, so that it is available for the
   * pre-execution hook. If the partition does not exist, throw an error if
   * throwIfNonExistent is true, otherwise ignore it.
   */
  private void addTablePartsOutputs(String tblName, List<Map<String, String>> partSpecs,
      boolean throwIfNonExistent, boolean allowMany, ASTNode ast)
      throws SemanticException {
    Table tab = getTable(tblName);

    Iterator<Map<String, String>> i;
    int index;
    for (i = partSpecs.iterator(), index = 1; i.hasNext(); ++index) {
      Map<String, String> partSpec = i.next();
      List<Partition> parts = null;
      if (allowMany) {
        try {
          parts = db.getPartitions(tab, partSpec);
        } catch (HiveException e) {
          LOG.error("Got HiveException during obtaining list of partitions");
        }
      } else {
        parts = new ArrayList<Partition>();
        try {
          Partition p = db.getPartition(tab, partSpec, false);
          if (p != null) {
            parts.add(p);
          }
        } catch (HiveException e) {
          LOG.debug("Wrong specification");
        }
      }
      if (parts.isEmpty()) {
        if (throwIfNonExistent) {
          throw new SemanticException(ErrorMsg.INVALID_PARTITION.getMsg(ast.getChild(index)));
        }
      }
      for (Partition p : parts) {
        outputs.add(new WriteEntity(p));
      }
    }
  }

  /**
   * Add the table partitions to be modified in the output, so that it is available for the
   * pre-execution hook. If the partition does not exist, throw an error if
   * throwIfNonExistent is true, otherwise ignore it.
   * 为某一个数据库表删除一些partition分区的时候,调用该方法
   * tblName 表示要删除的数据库表
   * partSpecs 要对哪些分区进行删除,条件可能是>=等操作
   * throwIfNonExistent true表示没有找到删除的目标分区时,抛异常
   * stringPartitionColumns 删除某一个表的某些分区的时候,如果分区的属性类型不是String的,只允许进行=号操作删除,不允许进行非等号的操作,例如>=等操作
   *   校验是否所有的partition属性都是String类型的,false表示有不是String类型的,true表示所有的都是String类型的partiton名称
   * ignoreProtection,true表示设置了IGNORE PROTECTION属性
   * 
   * String DROP [IF Exists] PARTITION(key 符号 value,key 符号 value),PARTITION( key 符号 value,key 符号 value) [IGNORE PROTECTION]
注意:符号 = 、 == 、 <>、 != 、 <= 、< 、 < 、 >=
   */
  private void addTableDropPartsOutputs(String tblName, List<PartitionSpec> partSpecs,
      boolean throwIfNonExistent, boolean stringPartitionColumns, boolean ignoreProtection)
      throws SemanticException {
    Table tab = getTable(tblName);

    Iterator<PartitionSpec> i;
    int index;
    //寻找满足过滤条件的partition分区集合,进行删除
    for (i = partSpecs.iterator(), index = 1; i.hasNext(); ++index) {
      PartitionSpec partSpec = i.next();
      List<Partition> parts = null;
      if (stringPartitionColumns) {
        try {
        	//在table中查找符合filter条件的partiton集合,filer条件可以允许>=等非=号的符号
          parts = db.getPartitionsByFilter(tab, partSpec.toString());
        } catch (Exception e) {
          throw new SemanticException(ErrorMsg.INVALID_PARTITION.getMsg(partSpec.toString()), e);
        }
      } else {
        try {
        	//只允许查找=号的partition
          parts = db.getPartitions(tab, partSpec.getPartSpecWithoutOperator());
        } catch (Exception e) {
          throw new SemanticException(ErrorMsg.INVALID_PARTITION.getMsg(partSpec.toString()), e);
        }
      }

      //表示没有找到删除的目标分区时,抛异常
      if (parts.isEmpty()) {
        if (throwIfNonExistent) {
          throw new SemanticException(ErrorMsg.INVALID_PARTITION.getMsg(partSpec.toString()));
        }
      }
      
      //删除指定partition
      for (Partition p : parts) {
    	//当partition不可删除,并且ignoreProtection=false,则抛异常,说明该分区不允许删除,则程序停止进行
        if (!ignoreProtection && !p.canDrop()) {
          throw new SemanticException(
            ErrorMsg.DROP_COMMAND_NOT_ALLOWED_FOR_PARTITION.getMsg(p.getCompleteName()));
        }
        outputs.add(new WriteEntity(p));
      }
    }
  }

  /**
   * Analyze alter table's skewed table
   *
   * @param ast
   *          node
   * @throws SemanticException
   * 
alterStatementSuffixSkewedby
a.String tableSkewed
b.String NOT SKEWED
c.String NOT STORED AS DIRECTORIES
   */
  private void analyzeAltertableSkewedby(ASTNode ast) throws SemanticException {
    /**
     * Throw an error if the user tries to use the DDL with
     * hive.internal.ddl.list.bucketing.enable set to false.
     */
    HiveConf hiveConf = SessionState.get().getConf();

    String tableName = getUnescapedName((ASTNode) ast.getChild(0));
    Table tab = getTable(tableName, true);

    inputs.add(new ReadEntity(tab));
    outputs.add(new WriteEntity(tab));

    validateAlterTableType(tab, AlterTableTypes.ADDSKEWEDBY);

    if (ast.getChildCount() == 1) {//仅仅解析alterStatementSuffixSkewedby String NOT SKEWED
      /* Convert a skewed table to non-skewed table. */
      AlterTableDesc alterTblDesc = new AlterTableDesc(tableName, true,
          new ArrayList<String>(), new ArrayList<List<String>>());
      alterTblDesc.setStoredAsSubDirectories(false);
      rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
          alterTblDesc), conf));
    } else {
      switch (((ASTNode) ast.getChild(1)).getToken().getType()) {
      case HiveParser.TOK_TABLESKEWED:
    	/**
SKEWED BY (属性字符串,属性字符串) on (属性值集合xxx,xxx) [STORED AS DIRECTORIES]
或者SKEWED BY (属性字符串,属性字符串) on (多组属性值集合 (xxx,xxx),(xxx,xxx),(xxx,xxx) ) [STORED AS DIRECTORIES]
create table T (c1 string, c2 string) skewed by (c1) on ('x1') 表示在c1属性的值为x1的时候可能会数据发生偏移,因此在join的时候要先预估一下是否一个表的c1=x1的值能否很少,并且存储在内存中,如果是,则可以进行优化
create table T (c1 string, c2 string) skewed by (c1,c2) on (('x11','x21'),('x12','x22')) 表示在c1,c2属性的值为(x11,x21),或者(x21,x22)的时候可能会数据发生偏移,因此在join的时候要先预估一下是否一个表的(x11,x21),或者(x21,x22)的值能否很少,并且存储在内存中,如果是,则可以进行优化
    	 */
        handleAlterTableSkewedBy(ast, tableName, tab);
        break;
      case HiveParser.TOK_STOREDASDIRS:
    	  //解析STORED AS DIRECTORIES
        handleAlterTableDisableStoredAsDirs(tableName, tab);
        break;
      default:
        assert false;
      }
    }
  }

  /**
   * Handle alter table <name> not stored as directories
   *
   * @param tableName
   * @param tab
   * @throws SemanticException
   * 解析STORED AS DIRECTORIES
   */
  private void handleAlterTableDisableStoredAsDirs(String tableName, Table tab)
      throws SemanticException {
  List<String> skewedColNames = tab.getSkewedColNames();
    List<List<String>> skewedColValues = tab.getSkewedColValues();
    if ((skewedColNames == null) || (skewedColNames.size() == 0) || (skewedColValues == null)
        || (skewedColValues.size() == 0)) {
      throw new SemanticException(ErrorMsg.ALTER_TBL_STOREDASDIR_NOT_SKEWED.getMsg(tableName));
    }
    AlterTableDesc alterTblDesc = new AlterTableDesc(tableName, false,
        skewedColNames, skewedColValues);
    alterTblDesc.setStoredAsSubDirectories(false);
    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
        alterTblDesc), conf));
  }

  /**
   * Process "alter table <name> skewed by .. on .. stored as directories
   * @param ast
   * @param tableName
   * @param tab
   * @throws SemanticException
   *
tableSkewed
SKEWED BY (属性字符串,属性字符串) on (属性值集合xxx,xxx) [STORED AS DIRECTORIES]
或者SKEWED BY (属性字符串,属性字符串) on (多组属性值集合 (xxx,xxx),(xxx,xxx),(xxx,xxx) ) [STORED AS DIRECTORIES]
create table T (c1 string, c2 string) skewed by (c1) on ('x1') 表示在c1属性的值为x1的时候可能会数据发生偏移,因此在join的时候要先预估一下是否一个表的c1=x1的值能否很少,并且存储在内存中,如果是,则可以进行优化
create table T (c1 string, c2 string) skewed by (c1,c2) on (('x11','x21'),('x12','x22')) 表示在c1,c2属性的值为(x11,x21),或者(x21,x22)的时候可能会数据发生偏移,因此在join的时候要先预估一下是否一个表的(x11,x21),或者(x21,x22)的值能否很少,并且存储在内存中,如果是,则可以进行优化
   */
  private void handleAlterTableSkewedBy(ASTNode ast, String tableName, Table tab)
      throws SemanticException {
    List<String> skewedColNames = new ArrayList<String>();
    List<List<String>> skewedValues = new ArrayList<List<String>>();
    /* skewed column names. */
    ASTNode skewedNode = (ASTNode) ast.getChild(1);
    //返回SKEWED BY (属性字符串,属性字符串)解析后的属性集合
    skewedColNames = analyzeSkewedTablDDLColNames(skewedColNames, skewedNode);
    /* skewed value. 
     * 返回on (属性值集合xxx,xxx)或者on (多组属性值集合 (xxx,xxx),(xxx,xxx),(xxx,xxx) )解析后的value值 
     **/
    analyzeDDLSkewedValues(skewedValues, skewedNode);
    // stored as directories 解析[STORED AS DIRECTORIES]
    boolean storedAsDirs = analyzeStoredAdDirs(skewedNode);


    AlterTableDesc alterTblDesc = new AlterTableDesc(tableName, false,
        skewedColNames, skewedValues);
    alterTblDesc.setStoredAsSubDirectories(storedAsDirs);
    /**
     * Validate information about skewed table
     */
    alterTblDesc.setTable(tab);
    alterTblDesc.validate();
    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
        alterTblDesc), conf));
  }

  /**
   * Analyze skewed column names
   *
   * @param skewedColNames
   * @param child
   * @return
   * @throws SemanticException
   */
  private List<String> analyzeAlterTableSkewedColNames(List<String> skewedColNames,
      ASTNode child) throws SemanticException {
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
   * Given a ASTNode, return list of values.
   *
   * use case:
   * create table xyz list bucketed (col1) with skew (1,2,5)
   * AST Node is for (1,2,5)
   *
   * @param ast
   * @return
   */
  private List<String> getColumnValues(ASTNode ast) {
    List<String> colList = new ArrayList<String>();
    int numCh = ast.getChildCount();
    for (int i = 0; i < numCh; i++) {
      ASTNode child = (ASTNode) ast.getChild(i);
      colList.add(stripQuotes(child.getText()).toLowerCase());
    }
    return colList;
  }


  /**
   * Analyze alter table's skewed location
   *
   * @param ast
   * @param tableName
   * @param partSpec
   * @throws SemanticException
   * 
alterTblPartitionStatementSuffixSkewedLocation
a.SET SKEWED LOCATION (key=value,key=value)
b.SET SKEWED LOCATION ((key1,key2)=value,(key1,key2)=value)
   */
  private void analyzeAlterTableSkewedLocation(ASTNode ast, String tableName,
      HashMap<String, String> partSpec) throws SemanticException {
    /**
     * Throw an error if the user tries to use the DDL with
     * hive.internal.ddl.list.bucketing.enable set to false.
     */
    HiveConf hiveConf = SessionState.get().getConf();
    /**
     * Retrieve mappings from parser
     */
    Map<List<String>, String> locations = new HashMap<List<String>, String>();
    ArrayList<Node> locNodes = ast.getChildren();
    if (null == locNodes) {
      throw new SemanticException(ErrorMsg.ALTER_TBL_SKEWED_LOC_NO_LOC.getMsg());
    } else {
      for (Node locNode : locNodes) {
        // TOK_SKEWED_LOCATIONS
        ASTNode locAstNode = (ASTNode) locNode;
        ArrayList<Node> locListNodes = locAstNode.getChildren();
        if (null == locListNodes) {
          throw new SemanticException(ErrorMsg.ALTER_TBL_SKEWED_LOC_NO_LOC.getMsg());
        } else {
          for (Node locListNode : locListNodes) {
            // TOK_SKEWED_LOCATION_LIST
            ASTNode locListAstNode = (ASTNode) locListNode;
            ArrayList<Node> locMapNodes = locListAstNode.getChildren();
            if (null == locMapNodes) {
              throw new SemanticException(ErrorMsg.ALTER_TBL_SKEWED_LOC_NO_LOC.getMsg());
            } else {
              for (Node locMapNode : locMapNodes) {
                // TOK_SKEWED_LOCATION_MAP
                ASTNode locMapAstNode = (ASTNode) locMapNode;
                ArrayList<Node> locMapAstNodeMaps = locMapAstNode.getChildren();
                if ((null == locMapAstNodeMaps) || (locMapAstNodeMaps.size() != 2)) {
                  throw new SemanticException(ErrorMsg.ALTER_TBL_SKEWED_LOC_NO_MAP.getMsg());
                } else {
                  List<String> keyList = new LinkedList<String>();
                  ASTNode node = (ASTNode) locMapAstNodeMaps.get(0);
                  if (node.getToken().getType() == HiveParser.TOK_TABCOLVALUES) {
                    keyList = getSkewedValuesFromASTNode(node);
                  } else if (isConstant(node)) {
                    keyList.add(PlanUtils
                        .stripQuotes(node.getText()));
                  } else {
                    throw new SemanticException(ErrorMsg.SKEWED_TABLE_NO_COLUMN_VALUE.getMsg());
                  }
                  String newLocation = PlanUtils
                      .stripQuotes(unescapeSQLString(((ASTNode) locMapAstNodeMaps.get(1))
                          .getText()));
                  validateSkewedLocationString(newLocation);
                  locations.put(keyList, newLocation);
                }
              }
            }
          }
        }
      }
    }
    AlterTableDesc alterTblDesc = new AlterTableDesc(tableName, locations, partSpec);
    addInputsOutputsAlterTable(tableName, partSpec);
    rootTasks.add(TaskFactory.get(new DDLWork(getInputs(), getOutputs(),
        alterTblDesc), conf));
  }

  /**
   * Check if the node is constant.
   *
   * @param node
   * @return
   */
  private boolean isConstant(ASTNode node) {
    boolean result = false;
    switch(node.getToken().getType()) {
      case HiveParser.Number:
        result = true;
        break;
      case HiveParser.StringLiteral:
        result = true;
        break;
      case HiveParser.BigintLiteral:
        result = true;
        break;
      case HiveParser.SmallintLiteral:
        result = true;
        break;
      case HiveParser.TinyintLiteral:
        result = true;
        break;
      case HiveParser.DecimalLiteral:
        result = true;
        break;
      case HiveParser.CharSetName:
        result = true;
        break;
      case HiveParser.KW_TRUE:
      case HiveParser.KW_FALSE:
        result = true;
        break;
      default:
          break;
    }
    return result;
  }

  private void validateSkewedLocationString(String newLocation) throws SemanticException {
    /* Validate location string. */
    try {
      URI locUri = new URI(newLocation);
      if (!locUri.isAbsolute() || locUri.getScheme() == null
          || locUri.getScheme().trim().equals("")) {
        throw new SemanticException(
            newLocation
                + " is not absolute or has no scheme information. "
                + "Please specify a complete absolute uri with scheme information.");
      }
    } catch (URISyntaxException e) {
      throw new SemanticException(e);
    }
  }

  private Table getTable(String tblName) throws SemanticException {
    return getTable(null, tblName, true);
  }

  private Table getTable(String tblName, boolean throwException) throws SemanticException {
    return getTable(SessionState.get().getCurrentDatabase(), tblName, throwException);
  }

  /**
   * 查询数据库,如果查询不到,则根据throwException 是否为true,进行抛异常
   */
  private Table getTable(String database, String tblName, boolean throwException)
      throws SemanticException {
    try {
      Table tab = database == null ? db.getTable(tblName, false)
          : db.getTable(database, tblName, false);
      if (tab == null && throwException) {
        throw new SemanticException(ErrorMsg.INVALID_TABLE.getMsg(tblName));
      }
      return tab;
    } catch (HiveException e) {
      throw new SemanticException(ErrorMsg.INVALID_TABLE.getMsg(tblName));
    }
  }

  //查找table的某一个分区
  private Partition getPartition(Table table, Map<String, String> partSpec, boolean throwException)
      throws SemanticException {
    try {
      Partition partition = db.getPartition(table, partSpec, false);
      if (partition == null && throwException) {
        throw new SemanticException(toMessage(ErrorMsg.INVALID_PARTITION, partSpec));
      }
      return partition;
    } catch (HiveException e) {
      throw new SemanticException(toMessage(ErrorMsg.INVALID_PARTITION, partSpec), e);
    }
  }

  //查找table的所有分区情况
  private List<Partition> getPartitions(Table table, Map<String, String> partSpec,
      boolean throwException) throws SemanticException {
    try {
      List<Partition> partitions = partSpec == null ? db.getPartitions(table) :
          db.getPartitions(table, partSpec);
      if (partitions.isEmpty() && throwException) {
        throw new SemanticException(toMessage(ErrorMsg.INVALID_PARTITION, partSpec));
      }
      return partitions;
    } catch (HiveException e) {
      throw new SemanticException(toMessage(ErrorMsg.INVALID_PARTITION, partSpec), e);
    }
  }

  private String toMessage(ErrorMsg message, Object detail) {
    return detail == null ? message.getMsg() : message.getMsg(detail.toString());
  }
}
