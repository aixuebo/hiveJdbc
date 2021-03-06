/**
   Licensed to the Apache Software Foundation (ASF) under one or more 
   contributor license agreements.  See the NOTICE file distributed with 
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with 
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
parser grammar HiveParser;

options
{
tokenVocab=HiveLexer;
output=AST;
ASTLabelType=CommonTree;
backtrack=false;
k=3;
}
import SelectClauseParser, FromClauseParser, IdentifiersParser;

tokens {
TOK_INSERT;
TOK_QUERY;
TOK_SELECT;
TOK_SELECTDI;
TOK_SELEXPR;
TOK_FROM;
TOK_TAB;
TOK_PARTSPEC;
TOK_PARTVAL;
TOK_DIR;
TOK_LOCAL_DIR;
TOK_TABREF;
TOK_SUBQUERY;
TOK_INSERT_INTO;
TOK_DESTINATION;
TOK_ALLCOLREF;
TOK_TABLE_OR_COL;
TOK_FUNCTION;
TOK_FUNCTIONDI;
TOK_FUNCTIONSTAR;
TOK_WHERE;
TOK_OP_EQ;
TOK_OP_NE;
TOK_OP_LE;
TOK_OP_LT;
TOK_OP_GE;
TOK_OP_GT;
TOK_OP_DIV;
TOK_OP_ADD;
TOK_OP_SUB;
TOK_OP_MUL;
TOK_OP_MOD;
TOK_OP_BITAND;
TOK_OP_BITNOT;
TOK_OP_BITOR;
TOK_OP_BITXOR;
TOK_OP_AND;
TOK_OP_OR;
TOK_OP_NOT;
TOK_OP_LIKE;
TOK_TRUE;
TOK_FALSE;
TOK_TRANSFORM;
TOK_SERDE;
TOK_SERDENAME;
TOK_SERDEPROPS;
TOK_EXPLIST;
TOK_ALIASLIST;
TOK_GROUPBY;
TOK_ROLLUP_GROUPBY;
TOK_CUBE_GROUPBY;
TOK_GROUPING_SETS;
TOK_GROUPING_SETS_EXPRESSION;
TOK_HAVING;
TOK_ORDERBY;
TOK_CLUSTERBY;
TOK_DISTRIBUTEBY;
TOK_SORTBY;
TOK_UNION;
TOK_JOIN;
TOK_LEFTOUTERJOIN;
TOK_RIGHTOUTERJOIN;
TOK_FULLOUTERJOIN;
TOK_UNIQUEJOIN;
TOK_CROSSJOIN;
TOK_LOAD;
TOK_EXPORT;
TOK_IMPORT;
TOK_NULL;
TOK_ISNULL;
TOK_ISNOTNULL;
TOK_TINYINT;
TOK_SMALLINT;
TOK_INT;
TOK_BIGINT;
TOK_BOOLEAN;
TOK_FLOAT;
TOK_DOUBLE;
TOK_DATE;
TOK_DATELITERAL;
TOK_DATETIME;
TOK_TIMESTAMP;
TOK_STRING;
TOK_VARCHAR;
TOK_BINARY;
TOK_DECIMAL;
TOK_LIST;
TOK_STRUCT;
TOK_MAP;
TOK_UNIONTYPE;
TOK_COLTYPELIST;
TOK_CREATEDATABASE;
TOK_CREATETABLE;
TOK_TRUNCATETABLE;
TOK_CREATEINDEX;
TOK_CREATEINDEX_INDEXTBLNAME;
TOK_DEFERRED_REBUILDINDEX;
TOK_DROPINDEX;
TOK_DROPTABLE_PROPERTIES;
TOK_LIKETABLE;
TOK_DESCTABLE;
TOK_DESCFUNCTION;
TOK_ALTERTABLE_PARTITION;
TOK_ALTERTABLE_RENAME;
TOK_ALTERTABLE_ADDCOLS;
TOK_ALTERTABLE_RENAMECOL;
TOK_ALTERTABLE_RENAMEPART;
TOK_ALTERTABLE_REPLACECOLS;
TOK_ALTERTABLE_ADDPARTS;
TOK_ALTERTABLE_DROPPARTS;
TOK_ALTERTABLE_ALTERPARTS;
TOK_ALTERTABLE_ALTERPARTS_PROTECTMODE;
TOK_ALTERTABLE_TOUCH;
TOK_ALTERTABLE_ARCHIVE;
TOK_ALTERTABLE_UNARCHIVE;
TOK_ALTERTABLE_SERDEPROPERTIES;
TOK_ALTERTABLE_SERIALIZER;
TOK_TABLE_PARTITION;
TOK_ALTERTABLE_FILEFORMAT;
TOK_ALTERTABLE_LOCATION;
TOK_ALTERTABLE_PROPERTIES;
TOK_ALTERTABLE_CHANGECOL_AFTER_POSITION;
TOK_ALTERINDEX_REBUILD;
TOK_ALTERINDEX_PROPERTIES;
TOK_MSCK;
TOK_SHOWDATABASES;
TOK_SHOWTABLES;
TOK_SHOWCOLUMNS;
TOK_SHOWFUNCTIONS;
TOK_SHOWPARTITIONS;
TOK_SHOW_CREATETABLE;
TOK_SHOW_TABLESTATUS;
TOK_SHOW_TBLPROPERTIES;
TOK_SHOWLOCKS;
TOK_LOCKTABLE;
TOK_UNLOCKTABLE;
TOK_SWITCHDATABASE;
TOK_DROPDATABASE;
TOK_DROPTABLE;
TOK_DATABASECOMMENT;
TOK_TABCOLLIST;
TOK_TABCOL;
TOK_TABLECOMMENT;
TOK_TABLEPARTCOLS;
TOK_TABLEBUCKETS;
TOK_TABLEROWFORMAT;
TOK_TABLEROWFORMATFIELD;
TOK_TABLEROWFORMATCOLLITEMS;
TOK_TABLEROWFORMATMAPKEYS;
TOK_TABLEROWFORMATLINES;
TOK_TBLORCFILE;
TOK_TBLSEQUENCEFILE;
TOK_TBLTEXTFILE;
TOK_TBLRCFILE;
TOK_TABLEFILEFORMAT;
TOK_FILEFORMAT_GENERIC;
TOK_OFFLINE;
TOK_ENABLE;
TOK_DISABLE;
TOK_READONLY;
TOK_NO_DROP;
TOK_STORAGEHANDLER;
TOK_ALTERTABLE_CLUSTER_SORT;
TOK_NOT_CLUSTERED;
TOK_NOT_SORTED;
TOK_TABCOLNAME;
TOK_TABLELOCATION;
TOK_PARTITIONLOCATION;
TOK_TABLEBUCKETSAMPLE;
TOK_TABLESPLITSAMPLE;
TOK_PERCENT;
TOK_LENGTH;
TOK_ROWCOUNT;
TOK_TMP_FILE;
TOK_TABSORTCOLNAMEASC;
TOK_TABSORTCOLNAMEDESC;
TOK_STRINGLITERALSEQUENCE;
TOK_CHARSETLITERAL;
TOK_CREATEFUNCTION;
TOK_DROPFUNCTION;
TOK_CREATEMACRO;
TOK_DROPMACRO;
TOK_CREATEVIEW;
TOK_DROPVIEW;
TOK_ALTERVIEW_AS;
TOK_ALTERVIEW_PROPERTIES;
TOK_DROPVIEW_PROPERTIES;
TOK_ALTERVIEW_ADDPARTS;
TOK_ALTERVIEW_DROPPARTS;
TOK_ALTERVIEW_RENAME;
TOK_VIEWPARTCOLS;
TOK_EXPLAIN;
TOK_TABLESERIALIZER;
TOK_TABLEPROPERTIES;
TOK_TABLEPROPLIST;
TOK_INDEXPROPERTIES;
TOK_INDEXPROPLIST;
TOK_TABTYPE;
TOK_LIMIT;
TOK_TABLEPROPERTY;
TOK_IFEXISTS;
TOK_IFNOTEXISTS;
TOK_ORREPLACE;
TOK_HINTLIST;
TOK_HINT;
TOK_MAPJOIN;
TOK_STREAMTABLE;
TOK_HOLD_DDLTIME;
TOK_HINTARGLIST;
TOK_USERSCRIPTCOLNAMES;
TOK_USERSCRIPTCOLSCHEMA;
TOK_RECORDREADER;
TOK_RECORDWRITER;
TOK_LEFTSEMIJOIN;
TOK_LATERAL_VIEW;
TOK_LATERAL_VIEW_OUTER;
TOK_TABALIAS;
TOK_ANALYZE;
TOK_CREATEROLE;
TOK_DROPROLE;
TOK_GRANT;
TOK_REVOKE;
TOK_SHOW_GRANT;
TOK_PRIVILEGE_LIST;
TOK_PRIVILEGE;
TOK_PRINCIPAL_NAME;
TOK_USER;
TOK_GROUP;
TOK_ROLE;
TOK_GRANT_WITH_OPTION;
TOK_PRIV_ALL;
TOK_PRIV_ALTER_METADATA;
TOK_PRIV_ALTER_DATA;
TOK_PRIV_DROP;
TOK_PRIV_INDEX;
TOK_PRIV_LOCK;
TOK_PRIV_SELECT;
TOK_PRIV_SHOW_DATABASE;
TOK_PRIV_CREATE;
TOK_PRIV_OBJECT;
TOK_PRIV_OBJECT_COL;
TOK_GRANT_ROLE;
TOK_REVOKE_ROLE;
TOK_SHOW_ROLE_GRANT;
TOK_SHOWINDEXES;
TOK_INDEXCOMMENT;
TOK_DESCDATABASE;
TOK_DATABASEPROPERTIES;
TOK_DATABASELOCATION;
TOK_DBPROPLIST;
TOK_ALTERDATABASE_PROPERTIES;
TOK_ALTERTABLE_ALTERPARTS_MERGEFILES;
TOK_TABNAME;
TOK_TABSRC;
TOK_RESTRICT;
TOK_CASCADE;
TOK_TABLESKEWED;
TOK_TABCOLVALUE;
TOK_TABCOLVALUE_PAIR;
TOK_TABCOLVALUES;
TOK_ALTERTABLE_SKEWED;
TOK_ALTERTBLPART_SKEWED_LOCATION;
TOK_SKEWED_LOCATIONS;
TOK_SKEWED_LOCATION_LIST;
TOK_SKEWED_LOCATION_MAP;
TOK_STOREDASDIRS;
TOK_PARTITIONINGSPEC;
TOK_PTBLFUNCTION;
TOK_WINDOWDEF;
TOK_WINDOWSPEC;
TOK_WINDOWVALUES;
TOK_WINDOWRANGE;
TOK_IGNOREPROTECTION;
TOK_EXCHANGEPARTITION;
}


// Package headers
@header {
package org.apache.hadoop.hive.ql.parse;

import java.util.Collection;
import java.util.HashMap;
}


@members {
  ArrayList<ParseError> errors = new ArrayList<ParseError>();
  Stack msgs = new Stack<String>();

  private static HashMap<String, String> xlateMap;
  static {
    xlateMap = new HashMap<String, String>();

    // Keywords
    xlateMap.put("KW_TRUE", "TRUE");
    xlateMap.put("KW_FALSE", "FALSE");
    xlateMap.put("KW_ALL", "ALL");
    xlateMap.put("KW_AND", "AND");
    xlateMap.put("KW_OR", "OR");
    xlateMap.put("KW_NOT", "NOT");
    xlateMap.put("KW_LIKE", "LIKE");

    xlateMap.put("KW_ASC", "ASC");
    xlateMap.put("KW_DESC", "DESC");
    xlateMap.put("KW_ORDER", "ORDER");
    xlateMap.put("KW_BY", "BY");
    xlateMap.put("KW_GROUP", "GROUP");
    xlateMap.put("KW_WHERE", "WHERE");
    xlateMap.put("KW_FROM", "FROM");
    xlateMap.put("KW_AS", "AS");
    xlateMap.put("KW_SELECT", "SELECT");
    xlateMap.put("KW_DISTINCT", "DISTINCT");
    xlateMap.put("KW_INSERT", "INSERT");
    xlateMap.put("KW_OVERWRITE", "OVERWRITE");
    xlateMap.put("KW_OUTER", "OUTER");
    xlateMap.put("KW_JOIN", "JOIN");
    xlateMap.put("KW_LEFT", "LEFT");
    xlateMap.put("KW_RIGHT", "RIGHT");
    xlateMap.put("KW_FULL", "FULL");
    xlateMap.put("KW_ON", "ON");
    xlateMap.put("KW_PARTITION", "PARTITION");
    xlateMap.put("KW_PARTITIONS", "PARTITIONS");
    xlateMap.put("KW_TABLE", "TABLE");
    xlateMap.put("KW_TABLES", "TABLES");
    xlateMap.put("KW_TBLPROPERTIES", "TBLPROPERTIES");
    xlateMap.put("KW_SHOW", "SHOW");
    xlateMap.put("KW_MSCK", "MSCK");
    xlateMap.put("KW_DIRECTORY", "DIRECTORY");
    xlateMap.put("KW_LOCAL", "LOCAL");
    xlateMap.put("KW_TRANSFORM", "TRANSFORM");
    xlateMap.put("KW_USING", "USING");
    xlateMap.put("KW_CLUSTER", "CLUSTER");
    xlateMap.put("KW_DISTRIBUTE", "DISTRIBUTE");
    xlateMap.put("KW_SORT", "SORT");
    xlateMap.put("KW_UNION", "UNION");
    xlateMap.put("KW_LOAD", "LOAD");
    xlateMap.put("KW_DATA", "DATA");
    xlateMap.put("KW_INPATH", "INPATH");
    xlateMap.put("KW_IS", "IS");
    xlateMap.put("KW_NULL", "NULL");
    xlateMap.put("KW_CREATE", "CREATE");
    xlateMap.put("KW_EXTERNAL", "EXTERNAL");
    xlateMap.put("KW_ALTER", "ALTER");
    xlateMap.put("KW_DESCRIBE", "DESCRIBE");
    xlateMap.put("KW_DROP", "DROP");
    xlateMap.put("KW_REANME", "REANME");
    xlateMap.put("KW_TO", "TO");
    xlateMap.put("KW_COMMENT", "COMMENT");
    xlateMap.put("KW_BOOLEAN", "BOOLEAN");
    xlateMap.put("KW_TINYINT", "TINYINT");
    xlateMap.put("KW_SMALLINT", "SMALLINT");
    xlateMap.put("KW_INT", "INT");
    xlateMap.put("KW_BIGINT", "BIGINT");
    xlateMap.put("KW_FLOAT", "FLOAT");
    xlateMap.put("KW_DOUBLE", "DOUBLE");
    xlateMap.put("KW_DATE", "DATE");
    xlateMap.put("KW_DATETIME", "DATETIME");
    xlateMap.put("KW_TIMESTAMP", "TIMESTAMP");
    xlateMap.put("KW_STRING", "STRING");
    xlateMap.put("KW_BINARY", "BINARY");
    xlateMap.put("KW_ARRAY", "ARRAY");
    xlateMap.put("KW_MAP", "MAP");
    xlateMap.put("KW_REDUCE", "REDUCE");
    xlateMap.put("KW_PARTITIONED", "PARTITIONED");
    xlateMap.put("KW_CLUSTERED", "CLUSTERED");
    xlateMap.put("KW_SORTED", "SORTED");
    xlateMap.put("KW_INTO", "INTO");
    xlateMap.put("KW_BUCKETS", "BUCKETS");
    xlateMap.put("KW_ROW", "ROW");
    xlateMap.put("KW_FORMAT", "FORMAT");
    xlateMap.put("KW_DELIMITED", "DELIMITED");
    xlateMap.put("KW_FIELDS", "FIELDS");
    xlateMap.put("KW_TERMINATED", "TERMINATED");
    xlateMap.put("KW_COLLECTION", "COLLECTION");
    xlateMap.put("KW_ITEMS", "ITEMS");
    xlateMap.put("KW_KEYS", "KEYS");
    xlateMap.put("KW_KEY_TYPE", "\$KEY\$");
    xlateMap.put("KW_LINES", "LINES");
    xlateMap.put("KW_STORED", "STORED");
    xlateMap.put("KW_SEQUENCEFILE", "SEQUENCEFILE");
    xlateMap.put("KW_TEXTFILE", "TEXTFILE");
    xlateMap.put("KW_INPUTFORMAT", "INPUTFORMAT");
    xlateMap.put("KW_OUTPUTFORMAT", "OUTPUTFORMAT");
    xlateMap.put("KW_LOCATION", "LOCATION");
    xlateMap.put("KW_TABLESAMPLE", "TABLESAMPLE");
    xlateMap.put("KW_BUCKET", "BUCKET");
    xlateMap.put("KW_OUT", "OUT");
    xlateMap.put("KW_OF", "OF");
    xlateMap.put("KW_CAST", "CAST");
    xlateMap.put("KW_ADD", "ADD");
    xlateMap.put("KW_REPLACE", "REPLACE");
    xlateMap.put("KW_COLUMNS", "COLUMNS");
    xlateMap.put("KW_RLIKE", "RLIKE");
    xlateMap.put("KW_REGEXP", "REGEXP");
    xlateMap.put("KW_TEMPORARY", "TEMPORARY");
    xlateMap.put("KW_FUNCTION", "FUNCTION");
    xlateMap.put("KW_EXPLAIN", "EXPLAIN");
    xlateMap.put("KW_EXTENDED", "EXTENDED");
    xlateMap.put("KW_SERDE", "SERDE");
    xlateMap.put("KW_WITH", "WITH");
    xlateMap.put("KW_SERDEPROPERTIES", "SERDEPROPERTIES");
    xlateMap.put("KW_LIMIT", "LIMIT");
    xlateMap.put("KW_SET", "SET");
    xlateMap.put("KW_PROPERTIES", "TBLPROPERTIES");
    xlateMap.put("KW_VALUE_TYPE", "\$VALUE\$");
    xlateMap.put("KW_ELEM_TYPE", "\$ELEM\$");

    // Operators
    xlateMap.put("DOT", ".");
    xlateMap.put("COLON", ":");
    xlateMap.put("COMMA", ",");
    xlateMap.put("SEMICOLON", ");");

    xlateMap.put("LPAREN", "(");
    xlateMap.put("RPAREN", ")");
    xlateMap.put("LSQUARE", "[");
    xlateMap.put("RSQUARE", "]");

    xlateMap.put("EQUAL", "=");
    xlateMap.put("NOTEQUAL", "<>");
    xlateMap.put("EQUAL_NS", "<=>");
    xlateMap.put("LESSTHANOREQUALTO", "<=");
    xlateMap.put("LESSTHAN", "<");
    xlateMap.put("GREATERTHANOREQUALTO", ">=");
    xlateMap.put("GREATERTHAN", ">");

    xlateMap.put("DIVIDE", "/");
    xlateMap.put("PLUS", "+");
    xlateMap.put("MINUS", "-");
    xlateMap.put("STAR", "*");
    xlateMap.put("MOD", "\%");

    xlateMap.put("AMPERSAND", "&");
    xlateMap.put("TILDE", "~");
    xlateMap.put("BITWISEOR", "|");
    xlateMap.put("BITWISEXOR", "^");
    xlateMap.put("CharSetLiteral", "\\'");
  }

  public static Collection<String> getKeywords() {
    return xlateMap.values();
  }

  private static String xlate(String name) {

    String ret = xlateMap.get(name);
    if (ret == null) {
      ret = name;
    }

    return ret;
  }

  @Override
  public Object recoverFromMismatchedSet(IntStream input,
      RecognitionException re, BitSet follow) throws RecognitionException {
    throw re;
  }

  @Override
  public void displayRecognitionError(String[] tokenNames,
      RecognitionException e) {
    errors.add(new ParseError(this, e, tokenNames));
  }

  @Override
  public String getErrorHeader(RecognitionException e) {
    String header = null;
    if (e.charPositionInLine < 0 && input.LT(-1) != null) {
      Token t = input.LT(-1);
      header = "line " + t.getLine() + ":" + t.getCharPositionInLine();
    } else {
      header = super.getErrorHeader(e);
    }

    return header;
  }
  
  @Override
  public String getErrorMessage(RecognitionException e, String[] tokenNames) {
    String msg = null;

    // Translate the token names to something that the user can understand
    String[] xlateNames = new String[tokenNames.length];
    for (int i = 0; i < tokenNames.length; ++i) {
      xlateNames[i] = HiveParser.xlate(tokenNames[i]);
    }

    if (e instanceof NoViableAltException) {
      @SuppressWarnings("unused")
      NoViableAltException nvae = (NoViableAltException) e;
      // for development, can add
      // "decision=<<"+nvae.grammarDecisionDescription+">>"
      // and "(decision="+nvae.decisionNumber+") and
      // "state "+nvae.stateNumber
      msg = "cannot recognize input near"
              + (input.LT(1) != null ? " " + getTokenErrorDisplay(input.LT(1)) : "")
              + (input.LT(2) != null ? " " + getTokenErrorDisplay(input.LT(2)) : "")
              + (input.LT(3) != null ? " " + getTokenErrorDisplay(input.LT(3)) : "");
    } else if (e instanceof MismatchedTokenException) {
      MismatchedTokenException mte = (MismatchedTokenException) e;
      msg = super.getErrorMessage(e, xlateNames) + (input.LT(-1) == null ? "":" near '" + input.LT(-1).getText()) + "'";
    } else if (e instanceof FailedPredicateException) {
      FailedPredicateException fpe = (FailedPredicateException) e;
      msg = "Failed to recognize predicate '" + fpe.token.getText() + "'. Failed rule: '" + fpe.ruleName + "'";
    } else {
      msg = super.getErrorMessage(e, xlateNames);
    }

    if (msgs.size() > 0) {
      msg = msg + " in " + msgs.peek();
    }
    return msg;
  }
}

@rulecatch {
catch (RecognitionException e) {
 reportError(e);
  throw e;
}
}

//开始执行规则,分两种,是explain还是仅仅执行的sql语句
// starting rule
statement
	: explainStatement EOF
	| execStatement EOF
	;

格式:EXPLAIN [EXTENDED|FORMATTED|DEPENDENCY|LOGICAL] execStatement
explainStatement
@init { msgs.push("explain statement"); }
@after { msgs.pop(); }
	: KW_EXPLAIN (explainOptions=KW_EXTENDED|explainOptions=KW_FORMATTED|explainOptions=KW_DEPENDENCY|explainOptions=KW_LOGICAL)? execStatement
      -> ^(TOK_EXPLAIN execStatement $explainOptions?)
	;

execStatement
@init { msgs.push("statement"); }
@after { msgs.pop(); }
    : queryStatementExpression
    | loadStatement
    | exportStatement
    | importStatement
    | ddlStatement
    ;

格式:LOAD DATA [LOCAL] INPATH String [OVERWRITE] INTO TABLE tableName [PARTITION (name=value,name=value,name)]
loadStatement
@init { msgs.push("load statement"); }
@after { msgs.pop(); }
    : KW_LOAD KW_DATA (islocal=KW_LOCAL)? KW_INPATH (path=StringLiteral) (isoverwrite=KW_OVERWRITE)? KW_INTO KW_TABLE (tab=tableOrPartition)
    -> ^(TOK_LOAD $path $tab $islocal? $isoverwrite?)
    ;

将表的数据导出到HDFS上
格式: EXPORT TABLE tableName [PARTITION (name=value,name=value,name)] TO "Path"
exportStatement
@init { msgs.push("export statement"); }
@after { msgs.pop(); }
    : KW_EXPORT KW_TABLE (tab=tableOrPartition) KW_TO (path=StringLiteral)
    -> ^(TOK_EXPORT $tab $path)
    ;

将HDFS上的数据导入到表中
格式:IMPORT [EXTERNAL] TABLE [tableName [PARTITION (name=value,name=value,name)]] FROM "PATH" [LOCATION xxx]
注意: [LOCATION xxx] 表示将path的数据导入到表中,并且该表存储的路径是xxx
importStatement
@init { msgs.push("import statement"); }
@after { msgs.pop(); }
	: KW_IMPORT ((ext=KW_EXTERNAL)? KW_TABLE (tab=tableOrPartition))? KW_FROM (path=StringLiteral) tableLocation?
    -> ^(TOK_IMPORT $path $tab? $ext? tableLocation?)
    ;

ddlStatement
@init { msgs.push("ddl statement"); }
@after { msgs.pop(); }
    : createDatabaseStatement
    | switchDatabaseStatement
    | dropDatabaseStatement
    | createTableStatement
    | dropTableStatement
    | truncateTableStatement
    | alterStatement
    | descStatement
    | showStatement
    | metastoreCheck
    | createViewStatement
    | dropViewStatement
    | createFunctionStatement
    | createMacroStatement
    | createIndexStatement
    | dropIndexStatement
    | dropFunctionStatement
    | dropMacroStatement
    | analyzeStatement
    | lockStatement
    | unlockStatement
    | createRoleStatement
    | dropRoleStatement
    | grantPrivileges
    | revokePrivileges
    | showGrants
    | showRoleGrants
    | grantRole
    | revokeRole
    ;

//匹配if exists语句
ifExists
@init { msgs.push("if exists clause"); }
@after { msgs.pop(); }
    : KW_IF KW_EXISTS
    -> ^(TOK_IFEXISTS)
    ;

//执行drop的时候使用的语法
//格式RESTRICT 或者 CASCADE
restrictOrCascade
@init { msgs.push("restrict or cascade clause"); }
@after { msgs.pop(); }
    : KW_RESTRICT
    -> ^(TOK_RESTRICT)
    | KW_CASCADE
    -> ^(TOK_CASCADE)
    ;

//IF NOT EXISTS
ifNotExists
@init { msgs.push("if not exists clause"); }
@after { msgs.pop(); }
    : KW_IF KW_NOT KW_EXISTS
    -> ^(TOK_IFNOTEXISTS)
    ;

//格式 STORED AS DIRECTORIES
storedAsDirs
@init { msgs.push("stored as directories"); }
@after { msgs.pop(); }
    : KW_STORED KW_AS KW_DIRECTORIES
    -> ^(TOK_STOREDASDIRS)
    ;

//匹配 or replace 
orReplace
@init { msgs.push("or replace clause"); }
@after { msgs.pop(); }
    : KW_OR KW_REPLACE
    -> ^(TOK_ORREPLACE)
    ;

//匹配 IGNORE PROTECTION 表示忽略保护模式
ignoreProtection
@init { msgs.push("ignore protection clause"); }
@after { msgs.pop(); }
        : KW_IGNORE KW_PROTECTION
        -> ^(TOK_IGNOREPROTECTION)
        ;

创建一个数据库
格式:CREATE DATABASE|SCHEMA [IF NOT Exists] "databaseName" [COMMENT String] [LOCATION String][WITH DBPROPERTIES (key=value,key=value)]
createDatabaseStatement
@init { msgs.push("create database statement"); }
@after { msgs.pop(); }
    : KW_CREATE (KW_DATABASE|KW_SCHEMA)
        ifNotExists?
        name=identifier
        databaseComment?
        dbLocation?
        (KW_WITH KW_DBPROPERTIES dbprops=dbProperties)?
    -> ^(TOK_CREATEDATABASE $name ifNotExists? dbLocation? databaseComment? $dbprops?)
    ;

//匹配location + 字符串
dbLocation
@init { msgs.push("database location specification"); }
@after { msgs.pop(); }
    :
      KW_LOCATION locn=StringLiteral -> ^(TOK_DATABASELOCATION $locn)
    ;

//存储table的属性键值对信息集合
//格式(key=value,key=value),或者key(此时认为解析成key=null,即不需要value属性值)
dbProperties
@init { msgs.push("dbproperties"); }
@after { msgs.pop(); }
    :
      LPAREN dbPropertiesList RPAREN -> ^(TOK_DATABASEPROPERTIES dbPropertiesList)
    ;

//存储table的属性键值对信息集合
//格式key=value,key=value,或者key(此时认为解析成key=null,即不需要value属性值)
dbPropertiesList
@init { msgs.push("database properties list"); }
@after { msgs.pop(); }
    :
      keyValueProperty (COMMA keyValueProperty)* -> ^(TOK_DBPROPLIST keyValueProperty+)
    ;


//切换数据库 
//格式 use + 字符串
switchDatabaseStatement
@init { msgs.push("switch database statement"); }
@after { msgs.pop(); }
    : KW_USE identifier
    -> ^(TOK_SWITCHDATABASE identifier)
    ;

//DROP (DATABASE|SCHEMA) [IF EXISTS] database_name [RESTRICT|CASCADE];
dropDatabaseStatement
@init { msgs.push("drop database statement"); }
@after { msgs.pop(); }
    : KW_DROP (KW_DATABASE|KW_SCHEMA) ifExists? identifier restrictOrCascade?
    -> ^(TOK_DROPDATABASE identifier ifExists? restrictOrCascade?)
    ;

//添加数据库的备注信息
//COMMENT +备注
databaseComment
@init { msgs.push("database's comment"); }
@after { msgs.pop(); }
    : KW_COMMENT comment=StringLiteral
    -> ^(TOK_DATABASECOMMENT $comment)
    ;

格式
a.CREATE [EXTERNAL] TABLE [IF NOT Exists] tableName LIKE tableName [LOCATION xxx] [TBLPROPERTIES (keyValueProperty,keyValueProperty,keyProperty,keyProperty)]
b.CREATE [EXTERNAL] TABLE [IF NOT Exists] tableName [(columnNameTypeList)] 
  [COMMENT String] //备注
  [PARTITIONED BY (xxx colType COMMENT xxx,xxx colType COMMENT xxx)] //分区
  [CLUSTERED BY (column1,column2) [SORTED BY (column1 desc,column2 desc)] into Number BUCKETS] //为表进行分桶,即设置hadoop的partition类,以及设置每一个reduce中的排序方式
  [tableSkewed] //为表设置偏斜属性
  [tableRowFormat] //解析一行信息
  [tableFileFormat] //数据表的存储方式
  [LOCATION xxx] //存储在HDFS什么路径下
  [TBLPROPERTIES (keyValueProperty,keyValueProperty,keyProperty,keyProperty)] //设置table的属性信息
  [AS    selectClause
   fromClause
   whereClause?
   groupByClause?
   havingClause?
   orderByClause?
   clusterByClause?
   distributeByClause?
   sortByClause?
   window_clause?] //写入sql查询语句,用于创建表
createTableStatement 创建表
@init { msgs.push("create table statement"); }
@after { msgs.pop(); }
    : KW_CREATE (ext=KW_EXTERNAL)? KW_TABLE ifNotExists? name=tableName
      (  like=KW_LIKE likeName=tableName
         tableLocation?
         tablePropertiesPrefixed?
       | (LPAREN columnNameTypeList RPAREN)?
         tableComment?
         tablePartition?
         tableBuckets?
         tableSkewed?
         tableRowFormat?
         tableFileFormat?
         tableLocation?
         tablePropertiesPrefixed?
         (KW_AS selectStatement)?
      )
    -> ^(TOK_CREATETABLE $name $ext? ifNotExists?
         ^(TOK_LIKETABLE $likeName?)
         columnNameTypeList?
         tableComment?
         tablePartition?
         tableBuckets?
         tableSkewed?
         tableRowFormat?
         tableFileFormat?
         tableLocation?
         tablePropertiesPrefixed?
         selectStatement?
        )
    ;

截断表内数据
从表或者表分区删除所有行，不指定分区，将截断表中的所有分区，也可以一次指定多个分区，截断多个分区。
格式:TRUNCATE TABLE tableName [PARTITION (name=value,name=value,name)] [COLUMNS (column1,column2...)]
truncateTableStatement
@init { msgs.push("truncate table statement"); }
@after { msgs.pop(); }
    : KW_TRUNCATE KW_TABLE tablePartitionPrefix (KW_COLUMNS LPAREN columnNameList RPAREN)? -> ^(TOK_TRUNCATETABLE tablePartitionPrefix columnNameList?);

/**
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
createIndexStatement
@init { msgs.push("create index statement");}
@after {msgs.pop();}
    : KW_CREATE KW_INDEX indexName=identifier
      KW_ON KW_TABLE tab=tableName LPAREN indexedCols=columnNameList RPAREN
      KW_AS typeName=StringLiteral
      autoRebuild?
      indexPropertiesPrefixed?
      indexTblName?
      tableRowFormat?
      tableFileFormat?
      tableLocation?
      tablePropertiesPrefixed?
      indexComment?
    ->^(TOK_CREATEINDEX $indexName $typeName $tab $indexedCols
        autoRebuild?
        indexPropertiesPrefixed?
        indexTblName?
        tableRowFormat?
        tableFileFormat?
        tableLocation?
        tablePropertiesPrefixed?
        indexComment?)
    ;

//索引的备注  格式 comment xxx
indexComment
@init { msgs.push("comment on an index");}
@after {msgs.pop();}
        :
                KW_COMMENT comment=StringLiteral  -> ^(TOK_INDEXCOMMENT $comment)
        ;

//索引的可选项  格式  with deferred rebuild
autoRebuild
@init { msgs.push("auto rebuild index");}
@after {msgs.pop();}
    : KW_WITH KW_DEFERRED KW_REBUILD
    ->^(TOK_DEFERRED_REBUILDINDEX)
    ;

//格式 in table tableName
indexTblName
@init { msgs.push("index table name");}
@after {msgs.pop();}
    : KW_IN KW_TABLE indexTbl=tableName
    ->^(TOK_CREATEINDEX_INDEXTBLNAME $indexTbl)
    ;

//格式 IDXPROPERTIES (key=value,key=value)
//表示索引的属性信息
indexPropertiesPrefixed
@init { msgs.push("table properties with prefix"); }
@after { msgs.pop(); }
    :
        KW_IDXPROPERTIES! indexProperties
    ;

//格式 (key=value,key=value)
indexProperties
@init { msgs.push("index properties"); }
@after { msgs.pop(); }
    :
      LPAREN indexPropertiesList RPAREN -> ^(TOK_INDEXPROPERTIES indexPropertiesList)
    ;

//格式: key=value,key=value
indexPropertiesList
@init { msgs.push("index properties list"); }
@after { msgs.pop(); }
    :
      keyValueProperty (COMMA keyValueProperty)* -> ^(TOK_INDEXPROPLIST keyValueProperty+)
    ;

删除一个数据表的一个索引
格式:DROP INDEX [IF EXISTS] "indexName" ON tableName
dropIndexStatement
@init { msgs.push("drop index statement");}
@after {msgs.pop();}
    : KW_DROP KW_INDEX ifExists? indexName=identifier KW_ON tab=tableName
    ->^(TOK_DROPINDEX $indexName $tab ifExists?)
    ;

删除一个数据表
格式:DROP TABLE [IF EXISTS] tableName
dropTableStatement
@init { msgs.push("drop statement"); }
@after { msgs.pop(); }
    : KW_DROP KW_TABLE ifExists? tableName -> ^(TOK_DROPTABLE tableName ifExists?)
    ;

格式:
a.ALTER TABLE alterTableStatementSuffix
b.ALTER VIEW alterViewStatementSuffix
c.ALTER INDEX alterIndexStatementSuffix
d.ALTER DATABASE alterDatabaseStatementSuffix
alterStatement
@init { msgs.push("alter statement"); }
@after { msgs.pop(); }
    : KW_ALTER!
        (
            KW_TABLE! alterTableStatementSuffix
        |
            KW_VIEW! alterViewStatementSuffix
        |
            KW_INDEX! alterIndexStatementSuffix
        |
            KW_DATABASE! alterDatabaseStatementSuffix
        )
    ;


格式:
a."oldName" RENAME TO "newName"
b.String ADD|REPLACE COLUMNS (columnNameTypeList)
c.String CHANGE [COLUMN] "oldName" "newName" type [COMMENT String] [FIRST|AFTER String]
注意:type表示字段类型
d.String DROP [IF Exists] PARTITION(key 符号 value,key 符号 value),PARTITION( key 符号 value,key 符号 value) [IGNORE PROTECTION]
注意:符号 = 、 == 、 <>、 != 、 <= 、< 、 < 、 >=
删除一些partition
e.PARTITION (name=value,name=value,name) [LOCATION String]
f.String TOUCH PARTITION (name=value,name=value,name)
g.String ARCHIVE PARTITION (name=value,name=value,name)
h.String UNARCHIVE PARTITION (name=value,name=value,name)
i.String SET TBLPROPERTIES (keyValueProperty,keyValueProperty)
j.String UNSET TBLPROPERTIES [IF Exists](keyValueProperty,keyValueProperty)
k.tableName [PARTITION (name=value,name=value,name)] alterTblPartitionStatementSuffix
l.String PARTITION COLUMN (columnNameType)
m.String tableSkewed
n.String NOT SKEWED
o.String NOT STORED AS DIRECTORIES
p.tableName EXCHANGE PARTITION (name=value,name=value,name) WITH TABLE tableName
alterTableStatementSuffix
@init { msgs.push("alter table statement"); }
@after { msgs.pop(); }
    : alterStatementSuffixRename
    | alterStatementSuffixAddCol
    | alterStatementSuffixRenameCol
    | alterStatementSuffixDropPartitions
    | alterStatementSuffixAddPartitions
    | alterStatementSuffixTouch
    | alterStatementSuffixArchive
    | alterStatementSuffixUnArchive
    | alterStatementSuffixProperties
    | alterTblPartitionStatement
    | alterStatementSuffixSkewedby
    | alterStatementSuffixExchangePartition
    ;


格式:
a.String SET TBLPROPERTIES (keyValueProperty,keyValueProperty)
b.String UNSET TBLPROPERTIES [IF Exists](keyValueProperty,keyValueProperty)
c."oldName" RENAME TO "newName"
d.PARTITION (name=value,name=value,name) [LOCATION String]
e.String DROP [IF Exists] PARTITION(key 符号 value,key 符号 value),PARTITION( key 符号 value,key 符号 value) [IGNORE PROTECTION]
注意:符号 = 、 == 、 <>、 != 、 <= 、< 、 < 、 >=
表示删除一些partition
f.tableName AS selectStatement
alterViewStatementSuffix
@init { msgs.push("alter view statement"); }
@after { msgs.pop(); }
    : alterViewSuffixProperties
    | alterStatementSuffixRename
        -> ^(TOK_ALTERVIEW_RENAME alterStatementSuffixRename)
    | alterStatementSuffixAddPartitions
        -> ^(TOK_ALTERVIEW_ADDPARTS alterStatementSuffixAddPartitions)
    | alterStatementSuffixDropPartitions
        -> ^(TOK_ALTERVIEW_DROPPARTS alterStatementSuffixDropPartitions)
    | name=tableName KW_AS selectStatement
        -> ^(TOK_ALTERVIEW_AS $name selectStatement)
    ;


修改index的属性信息
格式:
a."indexName" ON "tableName" [PARTITION (name=value,name=value,name)] REBUILD
b."indexName" ON "tableName" [PARTITION (name=value,name=value,name)] SET IDXPROPERTIES (key=value,key=value)
alterIndexStatementSuffix
@init { msgs.push("alter index statement"); }
@after { msgs.pop(); }
    : indexName=identifier
      (KW_ON tableNameId=identifier)
      partitionSpec?
    (
      KW_REBUILD
      ->^(TOK_ALTERINDEX_REBUILD $tableNameId $indexName partitionSpec?)
    |
      KW_SET KW_IDXPROPERTIES
      indexProperties
      ->^(TOK_ALTERINDEX_PROPERTIES $tableNameId $indexName indexProperties)
    )
    ;

设置属性
格式:String SET DBPROPERTIES (key=value,key=value)
alterDatabaseStatementSuffix
@init { msgs.push("alter database statement"); }
@after { msgs.pop(); }
    : alterDatabaseSuffixProperties
    ;

设置属性
格式:String SET DBPROPERTIES (key=value,key=value)
alterDatabaseSuffixProperties
@init { msgs.push("alter database properties statement"); }
@after { msgs.pop(); }
    : name=identifier KW_SET KW_DBPROPERTIES dbProperties
    -> ^(TOK_ALTERDATABASE_PROPERTIES $name dbProperties)
    ;

重命名
格式:"oldName" RENAME TO "newName"
alterStatementSuffixRename
@init { msgs.push("rename statement"); }
@after { msgs.pop(); }
    : oldName=identifier KW_RENAME KW_TO newName=identifier
    -> ^(TOK_ALTERTABLE_RENAME $oldName $newName)
    ;

修改表的属性
格式:String ADD|REPLACE COLUMNS (columnNameTypeList)
alterStatementSuffixAddCol
@init { msgs.push("add column statement"); }
@after { msgs.pop(); }
    : identifier (add=KW_ADD | replace=KW_REPLACE) KW_COLUMNS LPAREN columnNameTypeList RPAREN
    -> {$add != null}? ^(TOK_ALTERTABLE_ADDCOLS identifier columnNameTypeList)
    ->                 ^(TOK_ALTERTABLE_REPLACECOLS identifier columnNameTypeList)
    ;

更改表的属性名字
格式:String CHANGE [COLUMN] "oldName" "newName" type [COMMENT String] [FIRST|AFTER String]
注意:FIRST或者AFTER String
注意:type表示字段类型
alterStatementSuffixRenameCol
@init { msgs.push("rename column name"); }
@after { msgs.pop(); }
    : identifier KW_CHANGE KW_COLUMN? oldName=identifier newName=identifier colType (KW_COMMENT comment=StringLiteral)? alterStatementChangeColPosition?
    ->^(TOK_ALTERTABLE_RENAMECOL identifier $oldName $newName colType $comment? alterStatementChangeColPosition?)
    ;

格式:FIRST|AFTER String
alterStatementChangeColPosition
    : first=KW_FIRST|KW_AFTER afterCol=identifier
    ->{$first != null}? ^(TOK_ALTERTABLE_CHANGECOL_AFTER_POSITION )
    -> ^(TOK_ALTERTABLE_CHANGECOL_AFTER_POSITION $afterCol)
    ;

为视图和table添加partition分区
格式:String ADD [IF NOT Exists] PARTITION (name=value,name=value,name) [LOCATION String] PARTITION (name=value,name=value,name) [LOCATION String]..
alterStatementSuffixAddPartitions
@init { msgs.push("add partition statement"); }
@after { msgs.pop(); }
    : identifier KW_ADD ifNotExists? alterStatementSuffixAddPartitionsElement+
    -> ^(TOK_ALTERTABLE_ADDPARTS identifier ifNotExists? alterStatementSuffixAddPartitionsElement+)
    ;

格式:PARTITION (name=value,name=value,name) [LOCATION String]
alterStatementSuffixAddPartitionsElement
    : partitionSpec partitionLocation?
    ;

格式:String TOUCH PARTITION (name=value,name=value,name) PARTITION (name=value,name=value,name)..
alterStatementSuffixTouch
@init { msgs.push("touch statement"); }
@after { msgs.pop(); }
    : identifier KW_TOUCH (partitionSpec)*
    -> ^(TOK_ALTERTABLE_TOUCH identifier (partitionSpec)*)
    ;

格式:String ARCHIVE PARTITION (name=value,name=value,name) PARTITION (name=value,name=value,name)。。。
alterStatementSuffixArchive
@init { msgs.push("archive statement"); }
@after { msgs.pop(); }
    : identifier KW_ARCHIVE (partitionSpec)*
    -> ^(TOK_ALTERTABLE_ARCHIVE identifier (partitionSpec)*)
    ;

格式:String UNARCHIVE PARTITION (name=value,name=value,name) PARTITION (name=value,name=value,name)。。。
alterStatementSuffixUnArchive
@init { msgs.push("unarchive statement"); }
@after { msgs.pop(); }
    : identifier KW_UNARCHIVE (partitionSpec)*
    -> ^(TOK_ALTERTABLE_UNARCHIVE identifier (partitionSpec)*)
    ;

partitionLocation
@init { msgs.push("partition location"); }
@after { msgs.pop(); }
    :
      KW_LOCATION locn=StringLiteral -> ^(TOK_PARTITIONLOCATION $locn)
    ;

删除一些partition
格式:String DROP [IF Exists] PARTITION(key 符号 value,key 符号 value),PARTITION( key 符号 value,key 符号 value) [IGNORE PROTECTION]
注意:符号 = 、 == 、 <>、 != 、 <= 、< 、 < 、 >=
alterStatementSuffixDropPartitions
@init { msgs.push("drop partition statement"); }
@after { msgs.pop(); }
    : identifier KW_DROP ifExists? dropPartitionSpec (COMMA dropPartitionSpec)* ignoreProtection?
    -> ^(TOK_ALTERTABLE_DROPPARTS identifier dropPartitionSpec+ ifExists? ignoreProtection?)
    ;

设置和取消一些属性
格式:
a.String SET TBLPROPERTIES (keyValueProperty,keyValueProperty)
b.String UNSET TBLPROPERTIES [IF Exists](keyValueProperty,keyValueProperty)
alterStatementSuffixProperties
@init { msgs.push("alter properties statement"); }
@after { msgs.pop(); }
    : name=identifier KW_SET KW_TBLPROPERTIES tableProperties
    -> ^(TOK_ALTERTABLE_PROPERTIES $name tableProperties)
    | name=identifier KW_UNSET KW_TBLPROPERTIES ifExists? tableProperties
    -> ^(TOK_DROPTABLE_PROPERTIES $name tableProperties ifExists?)
    ;

设置和取消一些属性
格式:
a.String SET TBLPROPERTIES (keyValueProperty,keyValueProperty)
b.String UNSET TBLPROPERTIES [IF Exists](keyValueProperty,keyValueProperty)
alterViewSuffixProperties
@init { msgs.push("alter view properties statement"); }
@after { msgs.pop(); }
    : name=identifier KW_SET KW_TBLPROPERTIES tableProperties
    -> ^(TOK_ALTERVIEW_PROPERTIES $name tableProperties)
    | name=identifier KW_UNSET KW_TBLPROPERTIES ifExists? tableProperties
    -> ^(TOK_DROPVIEW_PROPERTIES $name tableProperties ifExists?)
    ;

设置存储的方式是csv、json、还是protobuffer等等吧
格式 
1.SET SERDE "serde_class_name" [WITH SERDEPROPERTIES(key=value,key=value)]
2.SET SERDEPROPERTIES (key=value,key=value)
alterStatementSuffixSerdeProperties
@init { msgs.push("alter serdes statement"); }
@after { msgs.pop(); }
    : KW_SET KW_SERDE serdeName=StringLiteral (KW_WITH KW_SERDEPROPERTIES tableProperties)?
    -> ^(TOK_ALTERTABLE_SERIALIZER $serdeName tableProperties?)
    | KW_SET KW_SERDEPROPERTIES tableProperties
    -> ^(TOK_ALTERTABLE_SERDEPROPERTIES tableProperties)
    ;

//tableName [PARTITION (name=value,name=value,name)]
tablePartitionPrefix
@init {msgs.push("table partition prefix");}
@after {msgs.pop();}
  :name=identifier partitionSpec?
  ->^(TOK_TABLE_PARTITION $name partitionSpec?)
  ;

格式:
a.tableName [PARTITION (name=value,name=value,name)] alterTblPartitionStatementSuffix
b.String PARTITION COLUMN (columnNameType)
alterTblPartitionStatement
@init {msgs.push("alter table partition statement");}
@after {msgs.pop();}
  : tablePartitionPrefix alterTblPartitionStatementSuffix
  -> ^(TOK_ALTERTABLE_PARTITION tablePartitionPrefix alterTblPartitionStatementSuffix)
  |Identifier KW_PARTITION KW_COLUMN LPAREN columnNameType RPAREN
  -> ^(TOK_ALTERTABLE_ALTERPARTS Identifier columnNameType)
  ;


格式:
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
alterTblPartitionStatementSuffix
@init {msgs.push("alter table partition statement suffix");}
@after {msgs.pop();}
  : alterStatementSuffixFileFormat
  | alterStatementSuffixLocation
  | alterStatementSuffixProtectMode
  | alterStatementSuffixMergeFiles
  | alterStatementSuffixSerdeProperties
  | alterStatementSuffixRenamePart
  | alterStatementSuffixBucketNum
  | alterTblPartitionStatementSuffixSkewedLocation
  | alterStatementSuffixClusterbySortby
  ;

设置文件格式
1.SET FILEFORMAT SEQUENCEFILE
2.SET FILEFORMAT TEXTFILE
3.SET FILEFORMAT RCFILE
4.SET FILEFORMAT ORCFILE
5.SET FILEFORMAT INPUTFORMAT string OUTPUTFORMAT string [INPUTDRIVER string OUTPUTDRIVER string]
6.SET FILEFORMAT xxxx 属于TOK_FILEFORMAT_GENERIC类型自定义格式,自定义,目前不支持,需要自己实现该方法,继承DDLSemanticAnalyzer类即可
alterStatementSuffixFileFormat
@init {msgs.push("alter fileformat statement"); }
@after {msgs.pop();}
	: KW_SET KW_FILEFORMAT fileFormat
	-> ^(TOK_ALTERTABLE_FILEFORMAT fileFormat)
	;

格式:
1.NOT CLUSTERED 表示没有CLUSTERED BY 操作
2.NOT SORTED 表示没有SORTED BY 操作
3.CLUSTERED BY (column1,column2) [SORTED BY (column1 desc,column2 desc)] into Number BUCKETS
alterStatementSuffixClusterbySortby
@init {msgs.push("alter partition cluster by sort by statement");}
@after {msgs.pop();}
  : KW_NOT KW_CLUSTERED -> ^(TOK_ALTERTABLE_CLUSTER_SORT TOK_NOT_CLUSTERED)
  | KW_NOT KW_SORTED -> ^(TOK_ALTERTABLE_CLUSTER_SORT TOK_NOT_SORTED)
  | tableBuckets -> ^(TOK_ALTERTABLE_CLUSTER_SORT tableBuckets)
  ;

格式:
a.SET SKEWED LOCATION (key=value,key=value)
b.SET SKEWED LOCATION ((key1,key2)=value,(key1,key2)=value)
alterTblPartitionStatementSuffixSkewedLocation
@init {msgs.push("alter partition skewed location");}
@after {msgs.pop();}
  : KW_SET KW_SKEWED KW_LOCATION skewedLocations
  -> ^(TOK_ALTERTBLPART_SKEWED_LOCATION skewedLocations)
  ;

是skewedLocationsList用括号包裹上,
格式:(skewedLocationMap,skewedLocationMap)
skewedLocations
@init { msgs.push("skewed locations"); }
@after { msgs.pop(); }
    :
      LPAREN skewedLocationsList RPAREN -> ^(TOK_SKEWED_LOCATIONS skewedLocationsList)
    ;

是skewedLocationMap的集合,
格式:skewedLocationMap,skewedLocationMap
skewedLocationsList
@init { msgs.push("skewed locations list"); }
@after { msgs.pop(); }
    :
      skewedLocationMap (COMMA skewedLocationMap)* -> ^(TOK_SKEWED_LOCATION_LIST skewedLocationMap+)
    ;

格式 skewedValueLocationElement = String
格式 即key=value形式
skewedLocationMap
@init { msgs.push("specifying skewed location map"); }
@after { msgs.pop(); }
    :
      key=skewedValueLocationElement EQUAL value=StringLiteral -> ^(TOK_SKEWED_LOCATION_MAP $key $value)
    ;

//格式: SET LOCATION xxxx
alterStatementSuffixLocation
@init {msgs.push("alter location");}
@after {msgs.pop();}
  : KW_SET KW_LOCATION newLoc=StringLiteral
  -> ^(TOK_ALTERTABLE_LOCATION $newLoc)
  ;

格式:
a.String tableSkewed
b.String NOT SKEWED
c.String NOT STORED AS DIRECTORIES
alterStatementSuffixSkewedby
@init {msgs.push("alter skewed by statement");}
@after{msgs.pop();}
	:name=identifier tableSkewed
	->^(TOK_ALTERTABLE_SKEWED $name tableSkewed)
	|
	name=identifier KW_NOT KW_SKEWED
	->^(TOK_ALTERTABLE_SKEWED $name)
	|
	name=identifier KW_NOT storedAsDirs
	->^(TOK_ALTERTABLE_SKEWED $name storedAsDirs)
	;


格式:tableName EXCHANGE PARTITION (name=value,name=value,name) WITH TABLE tableName
alterStatementSuffixExchangePartition
@init {msgs.push("alter exchange partition");}
@after{msgs.pop();}
    : name=tableName KW_EXCHANGE partitionSpec KW_WITH KW_TABLE exchangename=tableName
    -> ^(TOK_EXCHANGEPARTITION $name partitionSpec $exchangename)
    ;

格式
1.ENABLE OFFLINE
2.ENABLE NO_DROP [CASCADE]
3.ENABLE READONLY
4.DISABLE OFFLINE
5.DISABLE NO_DROP [CASCADE]
6.DISABLE READONLY
alterProtectMode
alterStatementSuffixProtectMode
@init { msgs.push("alter partition protect mode statement"); }
@after { msgs.pop(); }
    : alterProtectMode
    -> ^(TOK_ALTERTABLE_ALTERPARTS_PROTECTMODE alterProtectMode)
    ;

为table修改新的partition属性
格式:
RENAME TO PARTITION (name=value,name=value,name)
alterStatementSuffixRenamePart
@init { msgs.push("alter table rename partition statement"); }
@after { msgs.pop(); }
    : KW_RENAME KW_TO partitionSpec
    ->^(TOK_ALTERTABLE_RENAMEPART partitionSpec)
    ;

格式 :CONCATENATE
alterStatementSuffixMergeFiles
@init { msgs.push(""); }
@after { msgs.pop(); }
    : KW_CONCATENATE
    -> ^(TOK_ALTERTABLE_ALTERPARTS_MERGEFILES)
    ;

格式
1.ENABLE OFFLINE
2.ENABLE NO_DROP [CASCADE]
3.ENABLE READONLY
4.DISABLE OFFLINE
5.DISABLE NO_DROP [CASCADE]
6.DISABLE READONLY
alterProtectMode
@init { msgs.push("protect mode specification enable"); }
@after { msgs.pop(); }
    : KW_ENABLE alterProtectModeMode  -> ^(TOK_ENABLE alterProtectModeMode)
    | KW_DISABLE alterProtectModeMode  -> ^(TOK_DISABLE alterProtectModeMode)
    ;

//格式 
1.OFFLINE
2.NO_DROP [CASCADE]
3.READONLY
alterProtectModeMode
@init { msgs.push("protect mode specification enable"); }
@after { msgs.pop(); }
    : KW_OFFLINE  -> ^(TOK_OFFLINE)
    | KW_NO_DROP KW_CASCADE? -> ^(TOK_NO_DROP KW_CASCADE?)
    | KW_READONLY  -> ^(TOK_READONLY)
    ;

格式:
INTO number BUCKETS
alterStatementSuffixBucketNum
@init { msgs.push(""); }
@after { msgs.pop(); }
    : KW_INTO num=Number KW_BUCKETS
    -> ^(TOK_TABLEBUCKETS $num)
    ;

//文件格式
1.SET FILEFORMAT SEQUENCEFILE
2.SET FILEFORMAT TEXTFILE
3.SET FILEFORMAT RCFILE
4.SET FILEFORMAT ORCFILE
5.SET FILEFORMAT INPUTFORMAT string OUTPUTFORMAT string [INPUTDRIVER string OUTPUTDRIVER string]
6.SET FILEFORMAT xxxx 属于TOK_FILEFORMAT_GENERIC类型自定义格式

fileFormat
@init { msgs.push("file format specification"); }
@after { msgs.pop(); }
    : KW_SEQUENCEFILE  -> ^(TOK_TBLSEQUENCEFILE)
    | KW_TEXTFILE  -> ^(TOK_TBLTEXTFILE)
    | KW_RCFILE  -> ^(TOK_TBLRCFILE)
    | KW_ORCFILE -> ^(TOK_TBLORCFILE)
    | KW_INPUTFORMAT inFmt=StringLiteral KW_OUTPUTFORMAT outFmt=StringLiteral (KW_INPUTDRIVER inDriver=StringLiteral KW_OUTPUTDRIVER outDriver=StringLiteral)?
      -> ^(TOK_TABLEFILEFORMAT $inFmt $outFmt $inDriver? $outDriver?)
    | genericSpec=identifier -> ^(TOK_FILEFORMAT_GENERIC $genericSpec)
    ;

//xxx .($ELEM$ | $KEY$ | $VALUE$ | xxx ) .($ELEM$ | $KEY$ | $VALUE$ | xxx )
tabTypeExpr
@init { msgs.push("specifying table types"); }
@after { msgs.pop(); }

   : identifier (DOT^ (KW_ELEM_TYPE | KW_KEY_TYPE | KW_VALUE_TYPE | identifier))*
   ;

//xxx .($ELEM$ | $KEY$ | $VALUE$ | xxx ) .($ELEM$ | $KEY$ | $VALUE$ | xxx )
descTabTypeExpr
@init { msgs.push("specifying describe table types"); }
@after { msgs.pop(); }

   : identifier (DOT^ (KW_ELEM_TYPE | KW_KEY_TYPE | KW_VALUE_TYPE | identifier))* identifier?
   ;

partTypeExpr
@init { msgs.push("specifying table partitions"); }
@after { msgs.pop(); }
    :  tabTypeExpr partitionSpec? -> ^(TOK_TABTYPE tabTypeExpr partitionSpec?)
    ;

格式:.($ELEM$ | $KEY$ | $VALUE$ | xxx ) [PARTITION (name=value,name=value,name)]
descPartTypeExpr
@init { msgs.push("specifying describe table partitions"); }
@after { msgs.pop(); }
    :  descTabTypeExpr partitionSpec? -> ^(TOK_TABTYPE descTabTypeExpr partitionSpec?)
    ;

格式:
a.DESCRIBE | DESC [FORMATTED | EXTENDED | PRETTY] .($ELEM$ | $KEY$ | $VALUE$ | xxx ) [PARTITION (name=value,name=value,name)] 描述表
注意xxx可能的格式是database.table.column or database.table or database
b.DESCRIBE | DESC FUNCTION [EXTENDED] descFuncNames
c.DESCRIBE | DESC DATABASE [EXTENDED] "dbName"
descStatement
@init { msgs.push("describe statement"); }
@after { msgs.pop(); }
    : (KW_DESCRIBE|KW_DESC) (descOptions=KW_FORMATTED|descOptions=KW_EXTENDED|descOptions=KW_PRETTY)? (parttype=descPartTypeExpr) -> ^(TOK_DESCTABLE $parttype $descOptions?)
    | (KW_DESCRIBE|KW_DESC) KW_FUNCTION KW_EXTENDED? (name=descFuncNames) -> ^(TOK_DESCFUNCTION $name KW_EXTENDED?)
    | (KW_DESCRIBE|KW_DESC) KW_DATABASE KW_EXTENDED? (dbName=identifier) -> ^(TOK_DESCDATABASE $dbName KW_EXTENDED?)
    ;

格式:ANALYZE TABLE tableName [ PARTITION (name=value,name=value,name) ] COMPUTE STATISTICS [NOSCAN]  [PARTIALSCAN]  [FOR COLUMNS "column1","column2"]
analyzeStatement
@init { msgs.push("analyze statement"); }
@after { msgs.pop(); }
    : KW_ANALYZE KW_TABLE (parttype=tableOrPartition) KW_COMPUTE KW_STATISTICS ((noscan=KW_NOSCAN) | (partialscan=KW_PARTIALSCAN) | (KW_FOR KW_COLUMNS statsColumnName=columnNameList))? -> ^(TOK_ANALYZE $parttype $noscan? $partialscan? $statsColumnName?)
    ;

表示SHOW 语法
a.SHOW DATABASES|SCHEMAS LIKE "xxx" 模糊查询,一定要带引号
b.SHOW TABLES [(FROM | IN) tableName ] like "xxx"
c.SHOW COLUMNS (FROM | IN) tableName [(FROM | IN) db_name ]
d.SHOW FUNCTIONS [xxx]
e.SHOW PARTITIONS xxx PARTITION (name=value,name=value,name) 展示某个表的某个partition信息
f.SHOW CREATE TABLE tableName 
g.SHOW TABLE EXTENDED [(FROM | IN) db_name ] like tableName [PARTITION (name=value,name=value,name)]
h.SHOW TBLPROPERTIES tblName [(columnName)] 获取该表的某一个自定义属性内容
i.SHOW LOCKS xxx .($ELEM$ | $KEY$ | $VALUE$ | xxx ) .($ELEM$ | $KEY$ | $VALUE$ | xxx )详细看.没看太懂
j.SHOW [FORMATTED](INDEX|INDEXES) ON tableName (FROM | IN) db_name ] 注意:该hive版本目前好像没有解析(FROM | IN) db_name这句语法的代码
showStatement
@init { msgs.push("show statement"); }
@after { msgs.pop(); }
    : KW_SHOW (KW_DATABASES|KW_SCHEMAS) (KW_LIKE showStmtIdentifier)? -> ^(TOK_SHOWDATABASES showStmtIdentifier?)
    | KW_SHOW KW_TABLES ((KW_FROM|KW_IN) db_name=identifier)? (KW_LIKE showStmtIdentifier|showStmtIdentifier)?  -> ^(TOK_SHOWTABLES (TOK_FROM $db_name)? showStmtIdentifier?)
    | KW_SHOW KW_COLUMNS (KW_FROM|KW_IN) tabname=tableName ((KW_FROM|KW_IN) db_name=identifier)? 
    -> ^(TOK_SHOWCOLUMNS $db_name? $tabname)
    | KW_SHOW KW_FUNCTIONS showStmtIdentifier?  -> ^(TOK_SHOWFUNCTIONS showStmtIdentifier?)
    | KW_SHOW KW_PARTITIONS identifier partitionSpec? -> ^(TOK_SHOWPARTITIONS identifier partitionSpec?)
    | KW_SHOW KW_CREATE KW_TABLE tabName=tableName -> ^(TOK_SHOW_CREATETABLE $tabName)
    | KW_SHOW KW_TABLE KW_EXTENDED ((KW_FROM|KW_IN) db_name=identifier)? KW_LIKE showStmtIdentifier partitionSpec?
    -> ^(TOK_SHOW_TABLESTATUS showStmtIdentifier $db_name? partitionSpec?)
    | KW_SHOW KW_TBLPROPERTIES tblName=identifier (LPAREN prptyName=StringLiteral RPAREN)? -> ^(TOK_SHOW_TBLPROPERTIES $tblName $prptyName?)
    | KW_SHOW KW_LOCKS (parttype=partTypeExpr)? (isExtended=KW_EXTENDED)? -> ^(TOK_SHOWLOCKS $parttype? $isExtended?)
    | KW_SHOW (showOptions=KW_FORMATTED)? (KW_INDEX|KW_INDEXES) KW_ON showStmtIdentifier ((KW_FROM|KW_IN) db_name=identifier)?
    -> ^(TOK_SHOWINDEXES showStmtIdentifier $showOptions? $db_name?)
    ;

格式:LOCK TABLE tableName [PARTITION (name=value,name=value,name)] (SHARED | EXCLUSIVE)
lockStatement
@init { msgs.push("lock statement"); }
@after { msgs.pop(); }
    : KW_LOCK KW_TABLE tableName partitionSpec? lockMode -> ^(TOK_LOCKTABLE tableName lockMode partitionSpec?)
    ;

lockMode
@init { msgs.push("lock mode"); }
@after { msgs.pop(); }
    : KW_SHARED | KW_EXCLUSIVE
    ;

格式:UNLOCK TABLE tableName [PARTITION (name=value,name=value,name)]
unlockStatement
@init { msgs.push("unlock statement"); }
@after { msgs.pop(); }
    : KW_UNLOCK KW_TABLE tableName partitionSpec?  -> ^(TOK_UNLOCKTABLE tableName partitionSpec?)
    ;

格式:CREATE ROLE "roleName"
createRoleStatement
@init { msgs.push("create role"); }
@after { msgs.pop(); }
    : KW_CREATE KW_ROLE roleName=identifier
    -> ^(TOK_CREATEROLE $roleName)
    ;

  格式:DROP ROLE "roleName"
dropRoleStatement
@init {msgs.push("drop role");}
@after {msgs.pop();}
    : KW_DROP KW_ROLE roleName=identifier
    -> ^(TOK_DROPROLE $roleName)
    ;

grantPrivileges
@init {msgs.push("grant privileges");}
@after {msgs.pop();}
    : KW_GRANT privList=privilegeList
      privilegeObject?
      KW_TO principalSpecification
      (KW_WITH withOption)?
    -> ^(TOK_GRANT $privList principalSpecification privilegeObject? withOption?)
    ;

revokePrivileges
@init {msgs.push("revoke privileges");}
@afer {msgs.pop();}
    : KW_REVOKE privilegeList privilegeObject? KW_FROM principalSpecification
    -> ^(TOK_REVOKE privilegeList principalSpecification privilegeObject?)
    ;

grantRole
@init {msgs.push("grant role");}
@after {msgs.pop();}
    : KW_GRANT KW_ROLE identifier (COMMA identifier)* KW_TO principalSpecification
    -> ^(TOK_GRANT_ROLE principalSpecification identifier+)
    ;

revokeRole
@init {msgs.push("revoke role");}
@after {msgs.pop();}
    : KW_REVOKE KW_ROLE identifier (COMMA identifier)* KW_FROM principalSpecification
    -> ^(TOK_REVOKE_ROLE principalSpecification identifier+)
    ;

展示某个user、group、role的具体权限
 格式:SHOW ROLE GRANT USER | GROUP | ROLE String
showRoleGrants
@init {msgs.push("show role grants");}
@after {msgs.pop();}
    : KW_SHOW KW_ROLE KW_GRANT principalName
    -> ^(TOK_SHOW_ROLE_GRANT principalName)
    ;

showGrants
@init {msgs.push("show grants");}
@after {msgs.pop();}
    : KW_SHOW KW_GRANT principalName privilegeIncludeColObject?
    -> ^(TOK_SHOW_GRANT principalName privilegeIncludeColObject?)
    ;

privilegeIncludeColObject
@init {msgs.push("privilege object including columns");}
@after {msgs.pop();}
    : KW_ON (table=KW_TABLE|KW_DATABASE) identifier (LPAREN cols=columnNameList RPAREN)? partitionSpec?
    -> ^(TOK_PRIV_OBJECT_COL identifier $table? $cols? partitionSpec?)
    ;

  格式:ON TABLE|DATABASE String [PARTITION (name=value,name=value,name)]
privilegeObject
@init {msgs.push("privilege subject");}
@after {msgs.pop();}
    : KW_ON (table=KW_TABLE|KW_DATABASE) identifier partitionSpec?
    -> ^(TOK_PRIV_OBJECT identifier $table? partitionSpec?)
    ;

格式:privlegeDef,...
privilegeList
@init {msgs.push("grant privilege list");}
@after {msgs.pop();}
    : privlegeDef (COMMA privlegeDef)*
    -> ^(TOK_PRIVILEGE_LIST privlegeDef+)
    ;

格式:ALL | ALTER | UPDATE | CREATE | DROP | INDEX | LOCK | SELECT | SHOW_DATABASE [(column1,column2...)]
privlegeDef
@init {msgs.push("grant privilege");}
@after {msgs.pop();}
    : privilegeType (LPAREN cols=columnNameList RPAREN)?
    -> ^(TOK_PRIVILEGE privilegeType $cols?)
    ;

格式: ALL | ALTER | UPDATE | CREATE | DROP | INDEX | LOCK | SELECT | SHOW_DATABASE
privilegeType
@init {msgs.push("privilege type");}
@after {msgs.pop();}
    : KW_ALL -> ^(TOK_PRIV_ALL)
    | KW_ALTER -> ^(TOK_PRIV_ALTER_METADATA)
    | KW_UPDATE -> ^(TOK_PRIV_ALTER_DATA)
    | KW_CREATE -> ^(TOK_PRIV_CREATE)
    | KW_DROP -> ^(TOK_PRIV_DROP)
    | KW_INDEX -> ^(TOK_PRIV_INDEX)
    | KW_LOCK -> ^(TOK_PRIV_LOCK)
    | KW_SELECT -> ^(TOK_PRIV_SELECT)
    | KW_SHOW_DATABASE -> ^(TOK_PRIV_SHOW_DATABASE)
    ;

  格式: USER | GROUP | ROLE String,USER | GROUP | ROLE String...
principalSpecification
@init { msgs.push("user/group/role name list"); }
@after { msgs.pop(); }
    : principalName (COMMA principalName)* -> ^(TOK_PRINCIPAL_NAME principalName+)
    ;

  格式: USER | GROUP | ROLE String
principalName
@init {msgs.push("user|group|role name");}
@after {msgs.pop();}
    : KW_USER identifier -> ^(TOK_USER identifier)
    | KW_GROUP identifier -> ^(TOK_GROUP identifier)
    | KW_ROLE identifier -> ^(TOK_ROLE identifier)
    ;

格式:GRANT OPTION
withOption
@init {msgs.push("grant with option");}
@after {msgs.pop();}
    : KW_GRANT KW_OPTION
    -> ^(TOK_GRANT_WITH_OPTION)
    ;

格式:MSCK [REPAIR] [TABLE tableName PARTITION (name=value,name=value,name),PARTITION (name=value,name=value,name)...]
metastoreCheck
@init { msgs.push("metastore check statement"); }
@after { msgs.pop(); }
    : KW_MSCK (repair=KW_REPAIR)? (KW_TABLE table=identifier partitionSpec? (COMMA partitionSpec)*)?
    -> ^(TOK_MSCK $repair? ($table partitionSpec*)?)
    ;

创建一个函数
格式:CREATE TEMPORARY FUNCTION xxx as xxx
createFunctionStatement
@init { msgs.push("create function statement"); }
@after { msgs.pop(); }
    : KW_CREATE KW_TEMPORARY KW_FUNCTION identifier KW_AS StringLiteral
    -> ^(TOK_CREATEFUNCTION identifier StringLiteral)
    ;

删除一个自定义函数
格式:DROP TEMPORARY FUNCTION [IF Exists] xxx
dropFunctionStatement
@init { msgs.push("drop temporary function statement"); }
@after { msgs.pop(); }
    : KW_DROP KW_TEMPORARY KW_FUNCTION ifExists? identifier
    -> ^(TOK_DROPFUNCTION identifier ifExists?)
    ;

格式；
a.CREATE TEMPORARY MACRO String() expression
b.CREATE TEMPORARY MACRO String(columnNameTypeList) expression
createMacroStatement
@init { msgs.push("create macro statement"); }
@after { msgs.pop(); }
    : KW_CREATE KW_TEMPORARY KW_MACRO Identifier
      LPAREN columnNameTypeList? RPAREN expression
    -> ^(TOK_CREATEMACRO Identifier columnNameTypeList? expression)
    ;

格式；DROP TEMPORARY MACRO [IF Exists] String
dropMacroStatement
@init { msgs.push("drop macro statement"); }
@after { msgs.pop(); }
    : KW_DROP KW_TEMPORARY KW_MACRO ifExists? Identifier
    -> ^(TOK_DROPMACRO Identifier ifExists?)
    ;

创建一个view视图
格式:
CREATE [OR REPLACE] VIEW [IF NOT Exists] tableName [("columnName1" COMMENT string,"columnName2" COMMENT string)] [COMMENT String]
[PARTITIONED ON (columnName1,columnName2)]
[TBLPROPERTIES (keyValueProperty,keyValueProperty,keyProperty,keyProperty)]
AS selectStatement
createViewStatement
@init {
    msgs.push("create view statement");
}
@after { msgs.pop(); }
    : KW_CREATE (orReplace)? KW_VIEW (ifNotExists)? name=tableName
        (LPAREN columnNameCommentList RPAREN)? tableComment? viewPartition?
        tablePropertiesPrefixed?
        KW_AS
        selectStatement
    -> ^(TOK_CREATEVIEW $name orReplace?
         ifNotExists?
         columnNameCommentList?
         tableComment?
         viewPartition?
         tablePropertiesPrefixed?
         selectStatement
        )
    ;

格式:
PARTITIONED ON (columnName1,columnName2)
viewPartition
@init { msgs.push("view partition specification"); }
@after { msgs.pop(); }
    : KW_PARTITIONED KW_ON LPAREN columnNameList RPAREN
    -> ^(TOK_VIEWPARTCOLS columnNameList)
    ;

删除一个视图
格式:DROP VIEW [IF Exists] viewName
dropViewStatement
@init { msgs.push("drop view statement"); }
@after { msgs.pop(); }
    : KW_DROP KW_VIEW ifExists? viewName -> ^(TOK_DROPVIEW viewName ifExists?)
    ;

//随意字符串,用于show 后面的语句
showStmtIdentifier
@init { msgs.push("identifier for show statement"); }
@after { msgs.pop(); }
    : identifier
    | StringLiteral
    ;

为表设置备注
格式:COMMENT String
tableComment
@init { msgs.push("table's comment"); }
@after { msgs.pop(); }
    :
      KW_COMMENT comment=StringLiteral  -> ^(TOK_TABLECOMMENT $comment)
    ;

定义一个表的分区集合,包括名字 类型 备注
格式:PARTITIONED BY (xxx colType COMMENT xxx,xxx colType COMMENT xxx)
tablePartition
@init { msgs.push("table partition specification"); }
@after { msgs.pop(); }
    : KW_PARTITIONED KW_BY LPAREN columnNameTypeList RPAREN
    -> ^(TOK_TABLEPARTCOLS columnNameTypeList)
    ;

//CLUSTERED BY (column1,column2) [SORTED BY (column1 desc,column2 desc)] into Number BUCKETS
tableBuckets
@init { msgs.push("table buckets specification"); }
@after { msgs.pop(); }
    :
      KW_CLUSTERED KW_BY LPAREN bucketCols=columnNameList RPAREN (KW_SORTED KW_BY LPAREN sortCols=columnNameOrderList RPAREN)? KW_INTO num=Number KW_BUCKETS
    -> ^(TOK_TABLEBUCKETS $bucketCols $sortCols? $num)
    ;

SKEWED BY (属性字符串,属性字符串) on (属性值集合xxx,xxx) [STORED AS DIRECTORIES]
或者SKEWED BY (属性字符串,属性字符串) on (多组属性值集合 (xxx,xxx),(xxx,xxx),(xxx,xxx) ) [STORED AS DIRECTORIES]
create table T (c1 string, c2 string) skewed by (c1) on ('x1') 表示在c1属性的值为x1的时候可能会数据发生偏移,因此在join的时候要先预估一下是否一个表的c1=x1的值能否很少,并且存储在内存中,如果是,则可以进行优化
create table T (c1 string, c2 string) skewed by (c1,c2) on (('x11','x21'),('x12','x22')) 表示在c1,c2属性的值为(x11,x21),或者(x21,x22)的时候可能会数据发生偏移,因此在join的时候要先预估一下是否一个表的(x11,x21),或者(x21,x22)的值能否很少,并且存储在内存中,如果是,则可以进行优化
tableSkewed
@init { msgs.push("table skewed specification"); }
@after { msgs.pop(); }
    :
     KW_SKEWED KW_BY LPAREN skewedCols=columnNameList RPAREN KW_ON LPAREN (skewedValues=skewedValueElement) RPAREN (storedAsDirs)?
    -> ^(TOK_TABLESKEWED $skewedCols $skewedValues storedAsDirs?)
    ;

rowFormat
@init { msgs.push("serde specification"); }
@after { msgs.pop(); }
    : rowFormatSerde -> ^(TOK_SERDE rowFormatSerde)
    | rowFormatDelimited -> ^(TOK_SERDE rowFormatDelimited)
    |   -> ^(TOK_SERDE)
    ;

//RECORDREADER "XXX"
recordReader
@init { msgs.push("record reader specification"); }
@after { msgs.pop(); }
    : KW_RECORDREADER StringLiteral -> ^(TOK_RECORDREADER StringLiteral)
    |   -> ^(TOK_RECORDREADER)
    ;

//RECORDWRITER "XXX"
recordWriter
@init { msgs.push("record writer specification"); }
@after { msgs.pop(); }
    : KW_RECORDWRITER StringLiteral -> ^(TOK_RECORDWRITER StringLiteral)
    |   -> ^(TOK_RECORDWRITER)
    ;

//ROW FORMAT SERDE "class全路径" [WHIN SERDEPROPERTIES TBLPROPERTIES (key=value,key=value,key)]
//注意:key没有等号,表示默认值是null
rowFormatSerde
@init { msgs.push("serde format specification"); }
@after { msgs.pop(); }
    : KW_ROW KW_FORMAT KW_SERDE name=StringLiteral (KW_WITH KW_SERDEPROPERTIES serdeprops=tableProperties)?
    -> ^(TOK_SERDENAME $name $serdeprops?)
    ;

//解析每一行的分隔信息
ROW FORMAT DELIMITED [FIELDS TERMINATED BY xxx [ESCAPED by xx]]
[COLLECTION ITEMS TERMINATED BY xxx ]
[MAP KEYS TERMINATED BY xxx]
[LINES TERMINATED BY xxx]
rowFormatDelimited
@init { msgs.push("serde properties specification"); }
@after { msgs.pop(); }
    :
      KW_ROW KW_FORMAT KW_DELIMITED tableRowFormatFieldIdentifier? tableRowFormatCollItemsIdentifier? tableRowFormatMapKeysIdentifier? tableRowFormatLinesIdentifier?
    -> ^(TOK_SERDEPROPS tableRowFormatFieldIdentifier? tableRowFormatCollItemsIdentifier? tableRowFormatMapKeysIdentifier? tableRowFormatLinesIdentifier?)
    ;

//方式1:ROW FORMAT DELIMITED [FIELDS terminated by xxx [ESCAPED by xx] ] 
//[COLLECTION ITEMS terminated by xxx ]
//[MAP KEYS terminated by xxx ]
//[LINES terminated by xxx ]
//方式2:
//ROW FORMAT SERDE "class全路径" [WHIN SERDEPROPERTIES TBLPROPERTIES (key=value,key=value,key)]
//注意:key没有等号,表示默认值是null
tableRowFormat
@init { msgs.push("table row format specification"); }
@after { msgs.pop(); }
    :
      rowFormatDelimited
    -> ^(TOK_TABLEROWFORMAT rowFormatDelimited)
    | rowFormatSerde
    -> ^(TOK_TABLESERIALIZER rowFormatSerde)
    ;

//解析table的键值对属性值的前缀信息
//格式TBLPROPERTIES (key=value,key=value,key) 注意:key没有等号,表示默认值是null
格式: TBLPROPERTIES (keyValueProperty,keyValueProperty,keyProperty,keyProperty)
tablePropertiesPrefixed
@init { msgs.push("table properties with prefix"); }
@after { msgs.pop(); }
    :
        KW_TBLPROPERTIES! tableProperties
    ;

//存储table的属性键值对信息集合
//格式(key=value,key=value,key)
//(此时认为解析成key=null,即不需要value属性值)
是key=value集合,即格式 (keyValueProperty,keyValueProperty,keyProperty,keyProperty)
tableProperties
@init { msgs.push("table properties"); }
@after { msgs.pop(); }
    :
      LPAREN tablePropertiesList RPAREN -> ^(TOK_TABLEPROPERTIES tablePropertiesList)
    ;

//存储table的属性键值对信息集合
//格式key=value,key=value,或者key(此时认为解析成key=null,即不需要value属性值)
是key=value集合,即格式 keyValueProperty,keyValueProperty,keyProperty,keyProperty
tablePropertiesList
@init { msgs.push("table properties list"); }
@after { msgs.pop(); }
    :
      keyValueProperty (COMMA keyValueProperty)* -> ^(TOK_TABLEPROPLIST keyValueProperty+)
    |
      keyProperty (COMMA keyProperty)* -> ^(TOK_TABLEPROPLIST keyProperty+)
    ;

//解析一个key-value键值对属性
//例如:key = value
格式 String=String
keyValueProperty
@init { msgs.push("specifying key/value property"); }
@after { msgs.pop(); }
    :
      key=StringLiteral EQUAL value=StringLiteral -> ^(TOK_TABLEPROPERTY $key $value)
    ;


//解析一个key键值对属性,并且设置value属性为null
//内容就是一个字符串,代表key
格式 String=null
keyProperty
@init { msgs.push("specifying key property"); }
@after { msgs.pop(); }
    :
      key=StringLiteral -> ^(TOK_TABLEPROPERTY $key TOK_NULL)
    ;

//设置每行的field分隔符
//格式 FIELDS TERMINATED BY xxx [ESCAPED by xx]
tableRowFormatFieldIdentifier
@init { msgs.push("table row format's field separator"); }
@after { msgs.pop(); }
    :
      KW_FIELDS KW_TERMINATED KW_BY fldIdnt=StringLiteral (KW_ESCAPED KW_BY fldEscape=StringLiteral)?
    -> ^(TOK_TABLEROWFORMATFIELD $fldIdnt $fldEscape?)
    ;

//设置每行的集合分隔符
//格式COLLECTION ITEMS terminated by xxx 
tableRowFormatCollItemsIdentifier
@init { msgs.push("table row format's column separator"); }
@after { msgs.pop(); }
    :
      KW_COLLECTION KW_ITEMS KW_TERMINATED KW_BY collIdnt=StringLiteral
    -> ^(TOK_TABLEROWFORMATCOLLITEMS $collIdnt)
    ;

//设置每行的Map中key-value分隔符
//格式 MAP KEYS terminated by xxx 
tableRowFormatMapKeysIdentifier
@init { msgs.push("table row format's map key separator"); }
@after { msgs.pop(); }
    :
      KW_MAP KW_KEYS KW_TERMINATED KW_BY mapKeysIdnt=StringLiteral
    -> ^(TOK_TABLEROWFORMATMAPKEYS $mapKeysIdnt)
    ;

//设置每行的分隔符
//格式 LINES terminated by xxx 
tableRowFormatLinesIdentifier
@init { msgs.push("table row format's line separator"); }
@after { msgs.pop(); }
    :
      KW_LINES KW_TERMINATED KW_BY linesIdnt=StringLiteral
    -> ^(TOK_TABLEROWFORMATLINES $linesIdnt)
    ;

//数据文件格式
//STORED as SEQUENCEFILE |
//STORED as TEXTFILE |
//STORED as RCFILE |
//STORED as TEXTFILE |
//STORED as INPUTFORMAT xxx OUTPUTFORMAT xxx [INPUTDRIVER xxx OUTPUTDRIVER xxx]
//STORED BY xxxx存储引擎, WITH SERDEPROPERTIES (key=value,key=value,key) ,注意key=value集合是为xxx存储引擎提供的参数集合
//STORED AS xxxx
tableFileFormat
@init { msgs.push("table file format specification"); }
@after { msgs.pop(); }
    :
      KW_STORED KW_AS KW_SEQUENCEFILE  -> TOK_TBLSEQUENCEFILE
      | KW_STORED KW_AS KW_TEXTFILE  -> TOK_TBLTEXTFILE
      | KW_STORED KW_AS KW_RCFILE  -> TOK_TBLRCFILE
      | KW_STORED KW_AS KW_ORCFILE -> TOK_TBLORCFILE
      | KW_STORED KW_AS KW_INPUTFORMAT inFmt=StringLiteral KW_OUTPUTFORMAT outFmt=StringLiteral (KW_INPUTDRIVER inDriver=StringLiteral KW_OUTPUTDRIVER outDriver=StringLiteral)?
      -> ^(TOK_TABLEFILEFORMAT $inFmt $outFmt $inDriver? $outDriver?)
      | KW_STORED KW_BY storageHandler=StringLiteral
         (KW_WITH KW_SERDEPROPERTIES serdeprops=tableProperties)?
      -> ^(TOK_STORAGEHANDLER $storageHandler $serdeprops?)
      | KW_STORED KW_AS genericSpec=identifier
      -> ^(TOK_FILEFORMAT_GENERIC $genericSpec)
    ;

//存储路径
//格式:LOCATION xxx
tableLocation
@init { msgs.push("table location specification"); }
@after { msgs.pop(); }
    :
      KW_LOCATION locn=StringLiteral -> ^(TOK_TABLELOCATION $locn)
    ;

//解析column的name、类型集合,用逗号拆分
//格式xxx colType COMMENT xxx,xxx colType COMMENT xxx
columnNameTypeList
@init { msgs.push("column name type list"); }
@after { msgs.pop(); }
    : columnNameType (COMMA columnNameType)* -> ^(TOK_TABCOLLIST columnNameType+)
    ;

//获取属性名字:属性类型的集合,用逗号拆分
//格式 name:type,name:type
columnNameColonTypeList
@init { msgs.push("column name type list"); }
@after { msgs.pop(); }
    : columnNameColonType (COMMA columnNameColonType)* -> ^(TOK_TABCOLLIST columnNameColonType+)
    ;

//获取属性名字集合,用逗号拆分
//格式属性字符串,属性字符串
columnNameList
@init { msgs.push("column name list"); }
@after { msgs.pop(); }
    : columnName (COMMA columnName)* -> ^(TOK_TABCOLNAME columnName+)
    ;

//仅仅获取属性名称
columnName
@init { msgs.push("column name"); }
@after { msgs.pop(); }
    :
      identifier
    ;

//返回多个属性列集合 格式: 字符串 [asc|desc],格式 字符串 [asc|desc]
columnNameOrderList
@init { msgs.push("column name order list"); }
@after { msgs.pop(); }
    : columnNameOrder (COMMA columnNameOrder)* -> ^(TOK_TABCOLNAME columnNameOrder+)
    ;

//返回一个属性对应的value值集合
//格式1 xxx,xxx
//格式2 (xxx,xxx),(xxx,xxx),(xxx,xxx) 多组,每组用()分离
skewedValueElement
@init { msgs.push("skewed value element"); }
@after { msgs.pop(); }
    : 
      skewedColumnValues
     | skewedColumnValuePairList
    ;

//返回多组列值集合,用逗号拆分,每组用()分隔
//格式(常量,常量),(常量,常量)
skewedColumnValuePairList
@init { msgs.push("column value pair list"); }
@after { msgs.pop(); }
    : skewedColumnValuePair (COMMA skewedColumnValuePair)* -> ^(TOK_TABCOLVALUE_PAIR skewedColumnValuePair+)
    ;

//返回列值集合,用逗号拆分
//格式(常量,常量)
skewedColumnValuePair
@init { msgs.push("column value pair"); }
@after { msgs.pop(); }
    : 
      LPAREN colValues=skewedColumnValues RPAREN 
      -> ^(TOK_TABCOLVALUES $colValues)
    ;

//返回列值集合,用逗号拆分
//格式
skewedColumnValues
@init { msgs.push("column values"); }
@after { msgs.pop(); }
    : skewedColumnValue (COMMA skewedColumnValue)* -> ^(TOK_TABCOLVALUE skewedColumnValue+)
    ;
/*
* 返回列值
* 格式
* DATE + 字符串 |
* 字符串  |
* 多个字符串 |
* 匹配long类型,即数字+L |
* 匹配数字+S |
* 匹配数字+Y |
* 匹配数字或者带科学计数法的数字 +BD |
* 匹配以_ +字母/数字'_' | '-' | '.' | ':' 信息为合法信息 + 匹配包含引号的字符串  或者 十六进制的数字,以0X开头 |
* 格式 true 或者false
*/
skewedColumnValue
@init { msgs.push("column value"); }
@after { msgs.pop(); }
    :
      constant
    ;

//元素的属性值
//格式 xxx 或者 (xxx,xxx)
skewedValueLocationElement
@init { msgs.push("skewed value location element"); }
@after { msgs.pop(); }
    : 
      skewedColumnValue
     | skewedColumnValuePair
    ;

//仅仅返回一个列属性,格式 字符串 [asc|desc]
columnNameOrder
@init { msgs.push("column name order"); }
@after { msgs.pop(); }
    : identifier (asc=KW_ASC | desc=KW_DESC)?
    -> {$desc == null}? ^(TOK_TABSORTCOLNAMEASC identifier)
    ->                  ^(TOK_TABSORTCOLNAMEDESC identifier)
    ;

//解析属性名字和注释集合,用逗号拆分，没有类型
//格式 属性名字 COMMENT 注释,属性名字 COMMENT 注释
columnNameCommentList
@init { msgs.push("column name comment list"); }
@after { msgs.pop(); }
    : columnNameComment (COMMA columnNameComment)* -> ^(TOK_TABCOLNAME columnNameComment+)
    ;

//解析属性名字和注释，没有类型
//格式 属性名字 COMMENT 注释
columnNameComment
@init { msgs.push("column name comment"); }
@after { msgs.pop(); }
    : colName=identifier (KW_COMMENT comment=StringLiteral)?
    -> ^(TOK_TABCOL $colName TOK_NULL $comment?)
    ;

//expression [ASC | DESC]
columnRefOrder
@init { msgs.push("column order"); }
@after { msgs.pop(); }
    : expression (asc=KW_ASC | desc=KW_DESC)?
    -> {$desc == null}? ^(TOK_TABSORTCOLNAMEASC expression)
    ->                  ^(TOK_TABSORTCOLNAMEDESC expression)
    ;

//解析column的name、类型、注释,仅仅解析一个属性的
//格式xxx colType COMMENT xxx
columnNameType
@init { msgs.push("column specification"); }
@after { msgs.pop(); }
    : colName=identifier colType (KW_COMMENT comment=StringLiteral)?
    -> {$comment == null}? ^(TOK_TABCOL $colName colType)
    ->                     ^(TOK_TABCOL $colName colType $comment)
    ;

//解析column的name、类型、注释,仅仅解析一个属性的,并且属性name和类型之间用:连接
//格式xxx : colType COMMENT xxx
columnNameColonType
@init { msgs.push("column specification"); }
@after { msgs.pop(); }
    : colName=identifier COLON colType (KW_COMMENT comment=StringLiteral)?
    -> {$comment == null}? ^(TOK_TABCOL $colName colType)
    ->                     ^(TOK_TABCOL $colName colType $comment)
    ;

//解析column属性的类型,仅仅解析一个属性的类型
colType
@init { msgs.push("column type"); }
@after { msgs.pop(); }
    : type
    ;

//column属性的类型集合,用逗号拆分,仅仅包含属性类型
colTypeList
@init { msgs.push("column type list"); }
@after { msgs.pop(); }
    : colType (COMMA colType)* -> ^(TOK_COLTYPELIST colType+)
    ;

//column属性的类型
type
    : primitiveType
    | listType
    | structType
    | mapType
    | unionType;

//必须是基础类型
primitiveType
@init { msgs.push("primitive type specification"); }
@after { msgs.pop(); }
    : KW_TINYINT       ->    TOK_TINYINT
    | KW_SMALLINT      ->    TOK_SMALLINT
    | KW_INT           ->    TOK_INT
    | KW_BIGINT        ->    TOK_BIGINT
    | KW_BOOLEAN       ->    TOK_BOOLEAN
    | KW_FLOAT         ->    TOK_FLOAT
    | KW_DOUBLE        ->    TOK_DOUBLE
    | KW_DATE          ->    TOK_DATE
    | KW_DATETIME      ->    TOK_DATETIME
    | KW_TIMESTAMP     ->    TOK_TIMESTAMP
    | KW_STRING        ->    TOK_STRING
    | KW_BINARY        ->    TOK_BINARY
    | KW_DECIMAL       ->    TOK_DECIMAL
    | KW_VARCHAR LPAREN length=Number RPAREN      ->    ^(TOK_VARCHAR $length)
    ;

//必须是数组类型,
//格式 ARRAY<primitiveType |listType |structType | mapType | unionType >
listType
@init { msgs.push("list type"); }
@after { msgs.pop(); }
    : KW_ARRAY LESSTHAN type GREATERTHAN   -> ^(TOK_LIST type)
    ;

//必须是对象类型
//格式STRUCT<name:type,name:type>
structType
@init { msgs.push("struct type"); }
@after { msgs.pop(); }
    : KW_STRUCT LESSTHAN columnNameColonTypeList GREATERTHAN -> ^(TOK_STRUCT columnNameColonTypeList)
    ;

//Map对象类型
//MAP <primitiveType,primitiveType |listType |structType | mapType | unionType>
mapType
@init { msgs.push("map type"); }
@after { msgs.pop(); }
    : KW_MAP LESSTHAN left=primitiveType COMMA right=type GREATERTHAN
    -> ^(TOK_MAP $left $right)
    ;
//union对象类型
//UNIONTYPE <包含属性类型>
unionType
@init { msgs.push("uniontype type"); }
@after { msgs.pop(); }
    : KW_UNIONTYPE LESSTHAN colTypeList GREATERTHAN -> ^(TOK_UNIONTYPE colTypeList)
    ;

//UNION ALL
queryOperator
@init { msgs.push("query operator"); }
@after { msgs.pop(); }
    : KW_UNION KW_ALL -> ^(TOK_UNION)
    ;

// queryStatement UNION ALL queryStatement UNION ALL queryStatement
queryStatementExpression
    : queryStatement (queryOperator^ queryStatement)*
    ;

queryStatement
    :
    fromClause
    ( b+=body )+ -> ^(TOK_QUERY fromClause body+)
    | regular_body
    ;

regular_body
   :
   insertClause
   selectClause
   fromClause
   whereClause?
   groupByClause?
   havingClause?
   orderByClause?
   clusterByClause?
   distributeByClause?
   sortByClause?
   window_clause?
   limitClause? -> ^(TOK_QUERY fromClause ^(TOK_INSERT insertClause
                     selectClause whereClause? groupByClause? havingClause? orderByClause? clusterByClause?
                     distributeByClause? sortByClause? window_clause? limitClause?))
   |
   selectStatement
   ;

selectStatement
   :
   selectClause
   fromClause
   whereClause?
   groupByClause?
   havingClause?
   orderByClause?
   clusterByClause?
   distributeByClause?
   sortByClause?
   window_clause?
   limitClause? -> ^(TOK_QUERY fromClause ^(TOK_INSERT ^(TOK_DESTINATION ^(TOK_DIR TOK_TMP_FILE))
                     selectClause whereClause? groupByClause? havingClause? orderByClause? clusterByClause?
                     distributeByClause? sortByClause? window_clause? limitClause?))
   ;


body
   :
   insertClause
   selectClause
   lateralView?
   whereClause?
   groupByClause?
   havingClause?
   orderByClause?
   clusterByClause?
   distributeByClause?
   sortByClause?
   window_clause?
   limitClause? -> ^(TOK_INSERT insertClause
                     selectClause lateralView? whereClause? groupByClause? havingClause? orderByClause? clusterByClause?
                     distributeByClause? sortByClause? window_clause? limitClause?)
   |
   selectClause
   lateralView?
   whereClause?
   groupByClause?
   havingClause?
   orderByClause?
   clusterByClause?
   distributeByClause?
   sortByClause?
   window_clause?
   limitClause? -> ^(TOK_INSERT ^(TOK_DESTINATION ^(TOK_DIR TOK_TMP_FILE))
                     selectClause lateralView? whereClause? groupByClause? havingClause? orderByClause? clusterByClause?
                     distributeByClause? sortByClause? window_clause? limitClause?)
   ;

格式:
a.INSERT OVERWRITE LOCAL DIRECTORY "path" [tableRowFormat] [tableFileFormat] [IF NOT Exists]
b.INSERT OVERWRITE DIRECTORY "path" [IF NOT Exists]
c.INSERT OVERWRITE TABLE tableName [ PARTITION (name=value,name=value,name) ] [IF NOT Exists]
d.INSERT INTO TABLE tableName [ PARTITION (name=value,name=value,name) ]
注意:path必须是单引号或者双引号包裹
例如:
insert overwrite local directory '/data11/xuebo/logs/csv/csv' 向本地目录存储
insert overwrite directory '/logs/statistics/report/h_order_5/log_day=${d}' 向HDFS上目录存储
FROM nginx n INSERT OVERWRITE TABLE shareStatis PARTITION (task = 'share', date = '20150905')  SELECT * from where 向一个表中某个分区内插入数据
insertClause
@init { msgs.push("insert clause"); }
@after { msgs.pop(); }
   :
     KW_INSERT KW_OVERWRITE destination ifNotExists? -> ^(TOK_DESTINATION destination ifNotExists?)
   | KW_INSERT KW_INTO KW_TABLE tableOrPartition
       -> ^(TOK_INSERT_INTO tableOrPartition)
   ;

格式:
a.LOCAL DIRECTORY "path" [tableRowFormat] [tableFileFormat]
b.DIRECTORY "path"
c.TABLE tableName [ PARTITION (name=value,name=value,name) ]
注意:path必须是单引号或者双引号包裹
destination
@init { msgs.push("destination specification"); }
@after { msgs.pop(); }
   :
     KW_LOCAL KW_DIRECTORY StringLiteral tableRowFormat? tableFileFormat? -> ^(TOK_LOCAL_DIR StringLiteral tableRowFormat? tableFileFormat?)
   | KW_DIRECTORY StringLiteral -> ^(TOK_DIR StringLiteral)
   | KW_TABLE tableOrPartition -> tableOrPartition
   ;

格式:LIMIT Number
limitClause
@init { msgs.push("limit clause"); }
@after { msgs.pop(); }
   :
   KW_LIMIT num=Number -> ^(TOK_LIMIT $num)
   ;
