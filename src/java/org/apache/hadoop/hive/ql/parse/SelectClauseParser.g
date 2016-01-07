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
parser grammar SelectClauseParser;

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

//----------------------- Rules for parsing selectClause -----------------------------
// select a,b,c ... 前缀加入distinct或者all,都是仅仅针对第一个字段有用,其余字段无关
//SELECT /*+ STREAMTABLE(t1) */ 属于hintClause
//TRANSFORM 使用linux或者python脚本,可以忽略,因为我不喜欢这种方式,工作中我个人很少遇见
//总结
/**
SELECT [ /*+ STREAMTABLE(arg1),MAPJOIN(arg1,arg2,arg3),HOLD_DDLTIME(arg1,arg2,arg3) */ ] [ALL | DISTINCT ] selectExpression [ as xxx | as (xxx,xxx) ],selectExpression [ as xxx | as (xxx,xxx) ]
注意
a.arg1参数任意字符串即可
b.STREAMTABLE(arg1),MAPJOIN(arg1,arg2,arg3),HOLD_DDLTIME(arg1,arg2,arg3) 可以有任意几个都可以.不需要三个都存在,多个存在的时候用逗号分隔即可
c.DISTINCT col1,col2 表示按照col1和col2合在一起进行过滤重复，相当于 group by col1,col2
d.as 后面可以定义多个别名
e.selectExpression
  *  tableName.* 、dbName.tableName.* 或者 precedenceOrExpression
 **/
查询语句
格式:
SELECT [hintClause] [ALL|DISTINCT] selectList
SELECT [hintClause] TRANSFORM selectTrfmClause 
selectClause
@init { gParent.msgs.push("select clause"); }
@after { gParent.msgs.pop(); }
    :
    KW_SELECT hintClause? (((KW_ALL | dist=KW_DISTINCT)? selectList) // select all version from nginx limit 10;
                          | (transform=KW_TRANSFORM selectTrfmClause))
     -> {$transform == null && $dist == null}? ^(TOK_SELECT hintClause? selectList)
     -> {$transform == null && $dist != null}? ^(TOK_SELECTDI hintClause? selectList)
     -> ^(TOK_SELECT hintClause? ^(TOK_SELEXPR selectTrfmClause) )
    |
    trfmClause  ->^(TOK_SELECT ^(TOK_SELEXPR trfmClause))
    ;

查询的属性集合,包括使用as 别名语法
格式:selectItem,selectItem..
selectList
@init { gParent.msgs.push("select list"); }
@after { gParent.msgs.pop(); }
    :
    selectItem ( COMMA  selectItem )* -> selectItem+
    ;

//使用linux或者python脚本,可以忽略,因为我不喜欢这种方式,工作中我个人很少遇见
selectTrfmClause
@init { gParent.msgs.push("transform clause"); }
@after { gParent.msgs.pop(); }
    :
    LPAREN selectExpressionList RPAREN
    inSerde=rowFormat inRec=recordWriter
    KW_USING StringLiteral
    ( KW_AS ((LPAREN (aliasList | columnNameTypeList) RPAREN) | (aliasList | columnNameTypeList)))?
    outSerde=rowFormat outRec=recordReader
    -> ^(TOK_TRANSFORM selectExpressionList $inSerde $inRec StringLiteral $outSerde $outRec aliasList? columnNameTypeList?)
    ;

暗示条款以/*+开头,以*/结尾,期间包含hintList
格式:/*+ hintList */
eg:/*+ STREAMTABLE(t1) */
hintClause
@init { gParent.msgs.push("hint clause"); }
@after { gParent.msgs.pop(); }
    :
    DIVIDE STAR PLUS hintList STAR DIVIDE -> ^(TOK_HINTLIST hintList)
    ;
    
由逗号拆分的一组hintItem组成
格式:hintItem,hintItem..
hintList
@init { gParent.msgs.push("hint list"); }
@after { gParent.msgs.pop(); }
    :
    hintItem (COMMA hintItem)* -> hintItem+
    ;

每一个hintItem对象由hintName (hintArgs)或者hintName组成,例如STREAMTABLE(t1,t2)
格式:MAPJOIN|STREAMTABLE|DDLTIME [(String,String...)]
hintItem
@init { gParent.msgs.push("hint item"); }
@after { gParent.msgs.pop(); }
    :
    hintName (LPAREN hintArgs RPAREN)? -> ^(TOK_HINT hintName hintArgs?)
    ;

由MAPJOIN、STREAMTABLE、HOLD_DDLTIME三个组成
格式:MAPJOIN|STREAMTABLE|DDLTIME
hintName
@init { gParent.msgs.push("hint name"); }
@after { gParent.msgs.pop(); }
    :
    KW_MAPJOIN -> TOK_MAPJOIN
    | KW_STREAMTABLE -> TOK_STREAMTABLE
    | KW_HOLD_DDLTIME -> TOK_HOLD_DDLTIME
    ;

暗示条款的参数由逗号拆分的多个参数组成
格式:String,String...
hintArgs
@init { gParent.msgs.push("hint arguments"); }
@after { gParent.msgs.pop(); }
    :
    hintArgName (COMMA hintArgName)* -> ^(TOK_HINTARGLIST hintArgName+)
    ;

暗示条款的每一个参数的name由有效标识符组成即可
格式:String
hintArgName
@init { gParent.msgs.push("hint argument name"); }
@after { gParent.msgs.pop(); }
    :
    identifier
    ;

格式:
a.selectExpression [AS] String 为每一个查询项起一个别名
b.selectExpression [AS] (String,String..) 为每一个查询项起多个别名,但是好像现实环境是不允许的
selectItem 
@init { gParent.msgs.push("selection target"); }
@after { gParent.msgs.pop(); }
    :
    ( selectExpression
      ((KW_AS? identifier) | (KW_AS LPAREN identifier (COMMA identifier)* RPAREN))?
    ) -> ^(TOK_SELEXPR selectExpression identifier*)
    ;
//使用linux或者python脚本,可以忽略,因为我不喜欢这种方式,工作中我个人很少遇见
trfmClause
@init { gParent.msgs.push("transform clause"); }
@after { gParent.msgs.pop(); }
    :
    (   KW_MAP    selectExpressionList
      | KW_REDUCE selectExpressionList )
    inSerde=rowFormat inRec=recordWriter
    KW_USING StringLiteral
    ( KW_AS ((LPAREN (aliasList | columnNameTypeList) RPAREN) | (aliasList | columnNameTypeList)))?
    outSerde=rowFormat outRec=recordReader
    -> ^(TOK_TRANSFORM selectExpressionList $inSerde $inRec StringLiteral $outSerde $outRec aliasList? columnNameTypeList?)
    ;

查询的某一项
格式:tableName.* |dbName.tableName.* |expression
selectExpression
@init { gParent.msgs.push("select expression"); }
@after { gParent.msgs.pop(); }
    :
    expression | tableAllColumns
    ;

查询的多项计划
格式:selectExpression,selectExpression...
selectExpressionList
@init { gParent.msgs.push("select expression list"); }
@after { gParent.msgs.pop(); }
    :
    selectExpression (COMMA selectExpression)* -> ^(TOK_EXPLIST selectExpression+)
    ;

//---------------------- Rules for windowing clauses -------------------------------
数据窗口
格式:WINDOW window_defn,window_defn..
window_clause 
@init { gParent.msgs.push("window_clause"); }
@after { gParent.msgs.pop(); } 
:
  KW_WINDOW window_defn (COMMA window_defn)* -> ^(KW_WINDOW window_defn+)
;  

任意字符串表达式 as @window_specification
格式:
a.String AS String
b.String AS ([String] [partitioningSpec] [window_frame])
window_defn 
@init { gParent.msgs.push("window_defn"); }
@after { gParent.msgs.pop(); } 
:
  Identifier KW_AS window_specification -> ^(TOK_WINDOWDEF Identifier window_specification)
;  

格式:
a.String
b.([String] [partitioningSpec] [window_frame])
window_specification 
@init { gParent.msgs.push("window_specification"); }
@after { gParent.msgs.pop(); } 
:
  (Identifier | ( LPAREN Identifier? partitioningSpec? window_frame? RPAREN)) -> ^(TOK_WINDOWSPEC Identifier? partitioningSpec? window_frame?)
;

格式:
window_range_expression | window_value_expression
window_frame :
 window_range_expression |
 window_value_expression
;

格式:
a.ROWS UNBOUNDED PRECEDING
b.ROWS CURRENT ROW
c.ROWS Number PRECEDING
d.ROWS BETWEEN window_frame_boundary AND window_frame_boundary
window_range_expression 
@init { gParent.msgs.push("window_range_expression"); }
@after { gParent.msgs.pop(); } 
:
 KW_ROWS sb=window_frame_start_boundary -> ^(TOK_WINDOWRANGE $sb) |
 KW_ROWS KW_BETWEEN s=window_frame_boundary KW_AND end=window_frame_boundary -> ^(TOK_WINDOWRANGE $s $end)
;


格式:
a.RANGE UNBOUNDED PRECEDING
b.RANGE CURRENT ROW
c.RANGE Number PRECEDING
d.RANGE BETWEEN window_frame_boundary AND window_frame_boundary
window_value_expression 
@init { gParent.msgs.push("window_value_expression"); }
@after { gParent.msgs.pop(); } 
:
 KW_RANGE sb=window_frame_start_boundary -> ^(TOK_WINDOWVALUES $sb) |
 KW_RANGE KW_BETWEEN s=window_frame_boundary KW_AND end=window_frame_boundary -> ^(TOK_WINDOWVALUES $s $end)
;

格式:
a.UNBOUNDED PRECEDING
b.CURRENT ROW
c.Number PRECEDING
window_frame_start_boundary 
@init { gParent.msgs.push("windowframestartboundary"); }
@after { gParent.msgs.pop(); } 
:
  KW_UNBOUNDED KW_PRECEDING  -> ^(KW_PRECEDING KW_UNBOUNDED) | 
  KW_CURRENT KW_ROW  -> ^(KW_CURRENT) |
  Number KW_PRECEDING -> ^(KW_PRECEDING Number)
;


格式:
a.UNBOUNDED PRECEDING|FOLLOWING //不限制的规则或者不限制根据以下规则
b.CURRENT ROW 
c.Number PRECEDING|FOLLOWING
window_frame_boundary 
@init { gParent.msgs.push("windowframeboundary"); }
@after { gParent.msgs.pop(); } 
:
  KW_UNBOUNDED (r=KW_PRECEDING |r=KW_FOLLOWING)  -> ^($r KW_UNBOUNDED) | 
  KW_CURRENT KW_ROW  -> ^(KW_CURRENT) |
  Number (d=KW_PRECEDING | d=KW_FOLLOWING ) -> ^($d Number)
;   

