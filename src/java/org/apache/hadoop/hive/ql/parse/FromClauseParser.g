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
parser grammar FromClauseParser;

options
{
output=AST;
ASTLabelType=CommonTree;
backtrack=false;
k=3;
}

@members {
  @Override
  public Object recoverFromMismatchedSet(IntStream input,
      RecognitionException re, BitSet follow) throws RecognitionException {
    throw re;
  }
  @Override
  public void displayRecognitionError(String[] tokenNames,
      RecognitionException e) {
    gParent.errors.add(new ParseError(gParent, e, tokenNames));
  }
}

@rulecatch {
catch (RecognitionException e) {
  throw e;
}
}

//-----------------------------------------------------------------------------------

//tableName.*
//dbName.tableName.*
//*
tableAllColumns
    : STAR
        -> ^(TOK_ALLCOLREF)
    | tableName DOT STAR
        -> ^(TOK_ALLCOLREF tableName)
    ;

表示任意字符串,可以表示table名称或者列属性名称或者别名
格式:String
tableOrColumn
@init { gParent.msgs.push("table or column identifier"); }
@after { gParent.msgs.pop(); }
    :
    identifier -> ^(TOK_TABLE_OR_COL identifier)
    ;

多个表达式集合
格式:expression,expression..
expressionList
@init { gParent.msgs.push("expression list"); }
@after { gParent.msgs.pop(); }
    :
    expression (COMMA expression)* -> ^(TOK_EXPLIST expression+)
    ;

定义别名集合
格式:String,String..
aliasList
@init { gParent.msgs.push("alias list"); }
@after { gParent.msgs.pop(); }
    :
    identifier (COMMA identifier)* -> ^(TOK_ALIASLIST identifier+)
    ;

//----------------------- Rules for parsing fromClause ------------------------------
from子句
格式:
a.FROM fromSource joinToken fromSource [ON expression] joinToken fromSource [ON expression]..
b.FROM UNIQUEJOIN [PRESERVE] fromSource (expression,expression..)
fromClause
@init { gParent.msgs.push("from clause"); }
@after { gParent.msgs.pop(); }
    :
    KW_FROM joinSource -> ^(TOK_FROM joinSource)
    ;

格式:
a.fromSource joinToken fromSource [ON expression] joinToken fromSource [ON expression]..
b.UNIQUEJOIN [PRESERVE] fromSource (expression,expression..)
joinSource
@init { gParent.msgs.push("join source"); }
@after { gParent.msgs.pop(); }
    : fromSource ( joinToken^ fromSource (KW_ON! expression)? )*
    | uniqueJoinToken^ uniqueJoinSource (COMMA! uniqueJoinSource)+
    ;

格式:[PRESERVE] fromSource (expression,expression..)
uniqueJoinSource
@init { gParent.msgs.push("join source"); }
@after { gParent.msgs.pop(); }
    : KW_PRESERVE? fromSource uniqueJoinExpr
    ;

格式:(expression,expression..)
uniqueJoinExpr
@init { gParent.msgs.push("unique join expression list"); }
@after { gParent.msgs.pop(); }
    : LPAREN e1+=expression (COMMA e1+=expression)* RPAREN
      -> ^(TOK_EXPLIST $e1*)
    ;

格式:UNIQUEJOIN
uniqueJoinToken
@init { gParent.msgs.push("unique join"); }
@after { gParent.msgs.pop(); }
    : KW_UNIQUEJOIN -> TOK_UNIQUEJOIN;

格式:JOIN | INNER JOIN |CROSS JOIN | LEFT [OUTER] JOIN | RIGHT [OUTER] JOIN | FULL [OUTER] JOIN | LEFT SEMI JOIN
joinToken
@init { gParent.msgs.push("join type specifier"); }
@after { gParent.msgs.pop(); }
    :
      KW_JOIN                      -> TOK_JOIN
    | KW_INNER KW_JOIN             -> TOK_JOIN
    | KW_CROSS KW_JOIN             -> TOK_CROSSJOIN
    | KW_LEFT  (KW_OUTER)? KW_JOIN -> TOK_LEFTOUTERJOIN
    | KW_RIGHT (KW_OUTER)? KW_JOIN -> TOK_RIGHTOUTERJOIN
    | KW_FULL  (KW_OUTER)? KW_JOIN -> TOK_FULLOUTERJOIN
    | KW_LEFT KW_SEMI KW_JOIN      -> TOK_LEFTSEMIJOIN
    ;

格式:LATERAL VIEW [OUTER] function "tableAlias" [AS identifier,identifier..]
lateralView
@init {gParent.msgs.push("lateral view"); }
@after {gParent.msgs.pop(); }
	:
	KW_LATERAL KW_VIEW KW_OUTER function tableAlias (KW_AS identifier (COMMA identifier)*)?
	-> ^(TOK_LATERAL_VIEW_OUTER ^(TOK_SELECT ^(TOK_SELEXPR function identifier* tableAlias)))
	|
	KW_LATERAL KW_VIEW function tableAlias (KW_AS identifier (COMMA identifier)*)?
	-> ^(TOK_LATERAL_VIEW ^(TOK_SELECT ^(TOK_SELEXPR function identifier* tableAlias)))
	;

表示任意字符串,可以表示table名称或者列属性名称或者别名
格式:String
tableAlias
@init {gParent.msgs.push("table alias"); }
@after {gParent.msgs.pop(); }
    :
    identifier -> ^(TOK_TABALIAS identifier)
    ;

属于from后面接的信息
格式:
a.tableName [(keyValueProperty,keyValueProperty,keyProperty,keyProperty)] [tableSample] [ [AS] Identifier ] lateralView lateralView ..直接查询一个表,支持抽样
b.(queryStatementExpression) String lateralView lateralView .. 子查询
c.partitionedTableFunction lateralView lateralView ..
fromSource
@init { gParent.msgs.push("from source"); }
@after { gParent.msgs.pop(); }
    :
    ((Identifier LPAREN)=> partitionedTableFunction | tableSource | subQuerySource) (lateralView^)*
    ;

对table进行抽样提取数据
格式:TABLESAMPLE(BUCKET 数字 OUT OF 数字  [ ON expression,expression.. ] )
tableBucketSample
@init { gParent.msgs.push("table bucket sample specification"); }
@after { gParent.msgs.pop(); }
    :
    KW_TABLESAMPLE LPAREN KW_BUCKET (numerator=Number) KW_OUT KW_OF (denominator=Number) (KW_ON expr+=expression (COMMA expr+=expression)*)? RPAREN 
    -> ^(TOK_TABLEBUCKETSAMPLE $numerator $denominator $expr*)
    ;

对table进行抽样提取数据
格式:
a.TABLESAMPLE (Number PERCENT | ROWS)
b.TABLESAMPLE(ByteLengthLiteral)
注意:
ByteLengthLiteral表示匹配数字+单位,即表示长度信息,即(Digit)+ ('b' | 'B' | 'k' | 'K' | 'm' | 'M' | 'g' | 'G')
splitSample
@init { gParent.msgs.push("table split sample specification"); }
@after { gParent.msgs.pop(); }
    :
    KW_TABLESAMPLE LPAREN  (numerator=Number) (percent=KW_PERCENT|KW_ROWS) RPAREN
    -> {percent != null}? ^(TOK_TABLESPLITSAMPLE TOK_PERCENT $numerator)
    -> ^(TOK_TABLESPLITSAMPLE TOK_ROWCOUNT $numerator)
    |
    KW_TABLESAMPLE LPAREN  (numerator=ByteLengthLiteral) RPAREN
    -> ^(TOK_TABLESPLITSAMPLE TOK_LENGTH $numerator)
    ;

对table进行抽样提取数据
格式:
a.TABLESAMPLE(BUCKET 数字 OUT OF 数字  [ ON expression,expression.. ] )
b.TABLESAMPLE (Number PERCENT | ROWS)
c.TABLESAMPLE(ByteLengthLiteral) 
注意:
ByteLengthLiteral表示匹配数字+单位,即表示长度信息,即(Digit)+ ('b' | 'B' | 'k' | 'K' | 'm' | 'M' | 'g' | 'G')
tableSample
@init { gParent.msgs.push("table sample specification"); }
@after { gParent.msgs.pop(); }
    :
    tableBucketSample |
    splitSample
    ;

格式:
tableName [(keyValueProperty,keyValueProperty,keyProperty,keyProperty)] [tableSample] [ [AS] Identifier ]
tableSource
@init { gParent.msgs.push("table source"); }
@after { gParent.msgs.pop(); }
    : tabname=tableName (props=tableProperties)? (ts=tableSample)? (KW_AS? alias=Identifier)?
    -> ^(TOK_TABREF $tabname $props? $ts? $alias?)
    ;


视图和数据库表命名规则
格式:database.tableName | tableName
tableName
@init { gParent.msgs.push("table name"); }
@after { gParent.msgs.pop(); }
    :
    db=identifier DOT tab=identifier
    -> ^(TOK_TABNAME $db $tab)
    |
    tab=identifier
    -> ^(TOK_TABNAME $tab)
    ;

视图和数据库表命名规则
格式:database.tableName | tableName
viewName
@init { gParent.msgs.push("view name"); }
@after { gParent.msgs.pop(); }
    :
    (db=identifier DOT)? view=identifier
    -> ^(TOK_TABNAME $db? $view)
    ;

格式:(queryStatementExpression) String
subQuerySource
@init { gParent.msgs.push("subquery source"); }
@after { gParent.msgs.pop(); }
    :
    LPAREN queryStatementExpression RPAREN identifier -> ^(TOK_SUBQUERY queryStatementExpression identifier)
    ;

//---------------------- Rules for parsing PTF clauses -----------------------------
格式:
a.PARTITION by expression,expression.. [ORDER BY expression [ASC | DESC],expression [ASC | DESC]..]
b.ORDER BY expression [ASC | DESC],expression [ASC | DESC]..
c.DISTRIBUTE BY expression,expression.. [SORT BY expression [ASC | DESC],expression [ASC | DESC]..]
d.SORT BY expression [ASC | DESC],expression [ASC | DESC]..
e.CLUSTER BY (expression,expression..)
partitioningSpec
@init { gParent.msgs.push("partitioningSpec clause"); }
@after { gParent.msgs.pop(); } 
   :
   partitionByClause orderByClause? -> ^(TOK_PARTITIONINGSPEC partitionByClause orderByClause?) |
   orderByClause -> ^(TOK_PARTITIONINGSPEC orderByClause) |
   distributeByClause sortByClause? -> ^(TOK_PARTITIONINGSPEC distributeByClause sortByClause?) |
   sortByClause -> ^(TOK_PARTITIONINGSPEC sortByClause) |
   clusterByClause -> ^(TOK_PARTITIONINGSPEC clusterByClause)
   ;

格式:
a.(queryStatementExpression) String 子查询
b.tableName [(keyValueProperty,keyValueProperty,keyProperty,keyProperty)] [tableSample] [ [AS] Identifier ] 仅仅查询一个表,并且支持抽样
c.partitionedTableFunction 嵌套查询
partitionTableFunctionSource
@init { gParent.msgs.push("partitionTableFunctionSource clause"); }
@after { gParent.msgs.pop(); } 
   :
   subQuerySource |
   tableSource |
   partitionedTableFunction
   ;

格式:Identifier (ON partitionTableFunctionSource [partitioningSpec] Identifier(expression ),Identifier(expression).. ) alias
partitionedTableFunction
@init { gParent.msgs.push("ptf clause"); }
@after { gParent.msgs.pop(); } 
   :
   name=Identifier
   LPAREN KW_ON ptfsrc=partitionTableFunctionSource partitioningSpec?
     ((Identifier LPAREN expression RPAREN ) => Identifier LPAREN expression RPAREN ( COMMA Identifier LPAREN expression RPAREN)*)? 
   RPAREN alias=Identifier?
   ->   ^(TOK_PTBLFUNCTION $name $alias? partitionTableFunctionSource partitioningSpec? expression*)
   ; 

//----------------------- Rules for parsing whereClause -----------------------------
格式:WHERE expression
whereClause
@init { gParent.msgs.push("where clause"); }
@after { gParent.msgs.pop(); }
    :
    KW_WHERE searchCondition -> ^(TOK_WHERE searchCondition)
    ;

searchCondition
@init { gParent.msgs.push("search condition"); }
@after { gParent.msgs.pop(); }
    :
    expression
    ;

//-----------------------------------------------------------------------------------
