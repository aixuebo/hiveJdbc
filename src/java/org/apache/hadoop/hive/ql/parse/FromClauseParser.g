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

��ʾ�����ַ���,���Ա�ʾtable���ƻ������������ƻ��߱���
��ʽ:String
tableOrColumn
@init { gParent.msgs.push("table or column identifier"); }
@after { gParent.msgs.pop(); }
    :
    identifier -> ^(TOK_TABLE_OR_COL identifier)
    ;

������ʽ����
��ʽ:expression,expression..
expressionList
@init { gParent.msgs.push("expression list"); }
@after { gParent.msgs.pop(); }
    :
    expression (COMMA expression)* -> ^(TOK_EXPLIST expression+)
    ;

�����������
��ʽ:String,String..
aliasList
@init { gParent.msgs.push("alias list"); }
@after { gParent.msgs.pop(); }
    :
    identifier (COMMA identifier)* -> ^(TOK_ALIASLIST identifier+)
    ;

//----------------------- Rules for parsing fromClause ------------------------------
from�Ӿ�
��ʽ:
a.FROM fromSource joinToken fromSource [ON expression] joinToken fromSource [ON expression]..
b.FROM UNIQUEJOIN [PRESERVE] fromSource (expression,expression..)
fromClause
@init { gParent.msgs.push("from clause"); }
@after { gParent.msgs.pop(); }
    :
    KW_FROM joinSource -> ^(TOK_FROM joinSource)
    ;

��ʽ:
a.fromSource joinToken fromSource [ON expression] joinToken fromSource [ON expression]..
b.UNIQUEJOIN [PRESERVE] fromSource (expression,expression..)
joinSource
@init { gParent.msgs.push("join source"); }
@after { gParent.msgs.pop(); }
    : fromSource ( joinToken^ fromSource (KW_ON! expression)? )*
    | uniqueJoinToken^ uniqueJoinSource (COMMA! uniqueJoinSource)+
    ;

��ʽ:[PRESERVE] fromSource (expression,expression..)
uniqueJoinSource
@init { gParent.msgs.push("join source"); }
@after { gParent.msgs.pop(); }
    : KW_PRESERVE? fromSource uniqueJoinExpr
    ;

��ʽ:(expression,expression..)
uniqueJoinExpr
@init { gParent.msgs.push("unique join expression list"); }
@after { gParent.msgs.pop(); }
    : LPAREN e1+=expression (COMMA e1+=expression)* RPAREN
      -> ^(TOK_EXPLIST $e1*)
    ;

��ʽ:UNIQUEJOIN
uniqueJoinToken
@init { gParent.msgs.push("unique join"); }
@after { gParent.msgs.pop(); }
    : KW_UNIQUEJOIN -> TOK_UNIQUEJOIN;

��ʽ:JOIN | INNER JOIN |CROSS JOIN | LEFT [OUTER] JOIN | RIGHT [OUTER] JOIN | FULL [OUTER] JOIN | LEFT SEMI JOIN
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

��ʽ:LATERAL VIEW [OUTER] function "tableAlias" [AS identifier,identifier..]
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

��ʾ�����ַ���,���Ա�ʾtable���ƻ������������ƻ��߱���
��ʽ:String
tableAlias
@init {gParent.msgs.push("table alias"); }
@after {gParent.msgs.pop(); }
    :
    identifier -> ^(TOK_TABALIAS identifier)
    ;

����from����ӵ���Ϣ
��ʽ:
a.tableName [(keyValueProperty,keyValueProperty,keyProperty,keyProperty)] [tableSample] [ [AS] Identifier ] lateralView lateralView ..ֱ�Ӳ�ѯһ����,֧�ֳ���
b.(queryStatementExpression) String lateralView lateralView .. �Ӳ�ѯ
c.partitionedTableFunction lateralView lateralView ..
fromSource
@init { gParent.msgs.push("from source"); }
@after { gParent.msgs.pop(); }
    :
    ((Identifier LPAREN)=> partitionedTableFunction | tableSource | subQuerySource) (lateralView^)*
    ;

��table���г�����ȡ����
��ʽ:TABLESAMPLE(BUCKET ���� OUT OF ����  [ ON expression,expression.. ] )
tableBucketSample
@init { gParent.msgs.push("table bucket sample specification"); }
@after { gParent.msgs.pop(); }
    :
    KW_TABLESAMPLE LPAREN KW_BUCKET (numerator=Number) KW_OUT KW_OF (denominator=Number) (KW_ON expr+=expression (COMMA expr+=expression)*)? RPAREN 
    -> ^(TOK_TABLEBUCKETSAMPLE $numerator $denominator $expr*)
    ;

��table���г�����ȡ����
��ʽ:
a.TABLESAMPLE (Number PERCENT | ROWS)
b.TABLESAMPLE(ByteLengthLiteral)
ע��:
ByteLengthLiteral��ʾƥ������+��λ,����ʾ������Ϣ,��(Digit)+ ('b' | 'B' | 'k' | 'K' | 'm' | 'M' | 'g' | 'G')
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

��table���г�����ȡ����
��ʽ:
a.TABLESAMPLE(BUCKET ���� OUT OF ����  [ ON expression,expression.. ] )
b.TABLESAMPLE (Number PERCENT | ROWS)
c.TABLESAMPLE(ByteLengthLiteral) 
ע��:
ByteLengthLiteral��ʾƥ������+��λ,����ʾ������Ϣ,��(Digit)+ ('b' | 'B' | 'k' | 'K' | 'm' | 'M' | 'g' | 'G')
tableSample
@init { gParent.msgs.push("table sample specification"); }
@after { gParent.msgs.pop(); }
    :
    tableBucketSample |
    splitSample
    ;

��ʽ:
tableName [(keyValueProperty,keyValueProperty,keyProperty,keyProperty)] [tableSample] [ [AS] Identifier ]
tableSource
@init { gParent.msgs.push("table source"); }
@after { gParent.msgs.pop(); }
    : tabname=tableName (props=tableProperties)? (ts=tableSample)? (KW_AS? alias=Identifier)?
    -> ^(TOK_TABREF $tabname $props? $ts? $alias?)
    ;


��ͼ�����ݿ����������
��ʽ:database.tableName | tableName
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

��ͼ�����ݿ����������
��ʽ:database.tableName | tableName
viewName
@init { gParent.msgs.push("view name"); }
@after { gParent.msgs.pop(); }
    :
    (db=identifier DOT)? view=identifier
    -> ^(TOK_TABNAME $db? $view)
    ;

��ʽ:(queryStatementExpression) String
subQuerySource
@init { gParent.msgs.push("subquery source"); }
@after { gParent.msgs.pop(); }
    :
    LPAREN queryStatementExpression RPAREN identifier -> ^(TOK_SUBQUERY queryStatementExpression identifier)
    ;

//---------------------- Rules for parsing PTF clauses -----------------------------
��ʽ:
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

��ʽ:
a.(queryStatementExpression) String �Ӳ�ѯ
b.tableName [(keyValueProperty,keyValueProperty,keyProperty,keyProperty)] [tableSample] [ [AS] Identifier ] ������ѯһ����,����֧�ֳ���
c.partitionedTableFunction Ƕ�ײ�ѯ
partitionTableFunctionSource
@init { gParent.msgs.push("partitionTableFunctionSource clause"); }
@after { gParent.msgs.pop(); } 
   :
   subQuerySource |
   tableSource |
   partitionedTableFunction
   ;

��ʽ:Identifier (ON partitionTableFunctionSource [partitioningSpec] Identifier(expression ),Identifier(expression).. ) alias
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
��ʽ:WHERE expression
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
