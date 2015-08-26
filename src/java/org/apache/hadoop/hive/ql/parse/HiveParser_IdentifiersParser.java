// $ANTLR 3.4 IdentifiersParser.g 2015-01-29 16:46:22

package org.apache.hadoop.hive.ql.parse;

import java.util.Collection;
import java.util.HashMap;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.antlr.runtime.tree.*;


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
@SuppressWarnings({"all", "warnings", "unchecked"})
public class HiveParser_IdentifiersParser extends Parser {
    public static final int EOF=-1;
    public static final int AMPERSAND=4;
    public static final int BITWISEOR=5;
    public static final int BITWISEXOR=6;
    public static final int BigintLiteral=7;
    public static final int ByteLengthLiteral=8;
    public static final int COLON=9;
    public static final int COMMA=10;
    public static final int COMMENT=11;
    public static final int CharSetLiteral=12;
    public static final int CharSetName=13;
    public static final int DIV=14;
    public static final int DIVIDE=15;
    public static final int DOLLAR=16;
    public static final int DOT=17;
    public static final int DecimalLiteral=18;
    public static final int Digit=19;
    public static final int EQUAL=20;
    public static final int EQUAL_NS=21;
    public static final int Exponent=22;
    public static final int GREATERTHAN=23;
    public static final int GREATERTHANOREQUALTO=24;
    public static final int HexDigit=25;
    public static final int Identifier=26;
    public static final int KW_ADD=27;
    public static final int KW_ADMIN=28;
    public static final int KW_AFTER=29;
    public static final int KW_ALL=30;
    public static final int KW_ALTER=31;
    public static final int KW_ANALYZE=32;
    public static final int KW_AND=33;
    public static final int KW_ARCHIVE=34;
    public static final int KW_ARRAY=35;
    public static final int KW_AS=36;
    public static final int KW_ASC=37;
    public static final int KW_AUTHORIZATION=38;
    public static final int KW_BEFORE=39;
    public static final int KW_BETWEEN=40;
    public static final int KW_BIGINT=41;
    public static final int KW_BINARY=42;
    public static final int KW_BOOLEAN=43;
    public static final int KW_BOTH=44;
    public static final int KW_BUCKET=45;
    public static final int KW_BUCKETS=46;
    public static final int KW_BY=47;
    public static final int KW_CASCADE=48;
    public static final int KW_CASE=49;
    public static final int KW_CAST=50;
    public static final int KW_CHANGE=51;
    public static final int KW_CHAR=52;
    public static final int KW_CLUSTER=53;
    public static final int KW_CLUSTERED=54;
    public static final int KW_CLUSTERSTATUS=55;
    public static final int KW_COLLECTION=56;
    public static final int KW_COLUMN=57;
    public static final int KW_COLUMNS=58;
    public static final int KW_COMMENT=59;
    public static final int KW_COMPACT=60;
    public static final int KW_COMPACTIONS=61;
    public static final int KW_COMPUTE=62;
    public static final int KW_CONCATENATE=63;
    public static final int KW_CONF=64;
    public static final int KW_CONTINUE=65;
    public static final int KW_CREATE=66;
    public static final int KW_CROSS=67;
    public static final int KW_CUBE=68;
    public static final int KW_CURRENT=69;
    public static final int KW_CURSOR=70;
    public static final int KW_DATA=71;
    public static final int KW_DATABASE=72;
    public static final int KW_DATABASES=73;
    public static final int KW_DATE=74;
    public static final int KW_DATETIME=75;
    public static final int KW_DBPROPERTIES=76;
    public static final int KW_DECIMAL=77;
    public static final int KW_DEFAULT=78;
    public static final int KW_DEFERRED=79;
    public static final int KW_DEFINED=80;
    public static final int KW_DELETE=81;
    public static final int KW_DELIMITED=82;
    public static final int KW_DEPENDENCY=83;
    public static final int KW_DESC=84;
    public static final int KW_DESCRIBE=85;
    public static final int KW_DIRECTORIES=86;
    public static final int KW_DIRECTORY=87;
    public static final int KW_DISABLE=88;
    public static final int KW_DISTINCT=89;
    public static final int KW_DISTRIBUTE=90;
    public static final int KW_DOUBLE=91;
    public static final int KW_DROP=92;
    public static final int KW_ELEM_TYPE=93;
    public static final int KW_ELSE=94;
    public static final int KW_ENABLE=95;
    public static final int KW_END=96;
    public static final int KW_ESCAPED=97;
    public static final int KW_EXCHANGE=98;
    public static final int KW_EXCLUSIVE=99;
    public static final int KW_EXISTS=100;
    public static final int KW_EXPLAIN=101;
    public static final int KW_EXPORT=102;
    public static final int KW_EXTENDED=103;
    public static final int KW_EXTERNAL=104;
    public static final int KW_FALSE=105;
    public static final int KW_FETCH=106;
    public static final int KW_FIELDS=107;
    public static final int KW_FILE=108;
    public static final int KW_FILEFORMAT=109;
    public static final int KW_FIRST=110;
    public static final int KW_FLOAT=111;
    public static final int KW_FOLLOWING=112;
    public static final int KW_FOR=113;
    public static final int KW_FORMAT=114;
    public static final int KW_FORMATTED=115;
    public static final int KW_FROM=116;
    public static final int KW_FULL=117;
    public static final int KW_FUNCTION=118;
    public static final int KW_FUNCTIONS=119;
    public static final int KW_GRANT=120;
    public static final int KW_GROUP=121;
    public static final int KW_GROUPING=122;
    public static final int KW_HAVING=123;
    public static final int KW_HOLD_DDLTIME=124;
    public static final int KW_IDXPROPERTIES=125;
    public static final int KW_IF=126;
    public static final int KW_IGNORE=127;
    public static final int KW_IMPORT=128;
    public static final int KW_IN=129;
    public static final int KW_INDEX=130;
    public static final int KW_INDEXES=131;
    public static final int KW_INNER=132;
    public static final int KW_INPATH=133;
    public static final int KW_INPUTDRIVER=134;
    public static final int KW_INPUTFORMAT=135;
    public static final int KW_INSERT=136;
    public static final int KW_INT=137;
    public static final int KW_INTERSECT=138;
    public static final int KW_INTO=139;
    public static final int KW_IS=140;
    public static final int KW_ITEMS=141;
    public static final int KW_JAR=142;
    public static final int KW_JOIN=143;
    public static final int KW_KEYS=144;
    public static final int KW_KEY_TYPE=145;
    public static final int KW_LATERAL=146;
    public static final int KW_LEFT=147;
    public static final int KW_LESS=148;
    public static final int KW_LIKE=149;
    public static final int KW_LIMIT=150;
    public static final int KW_LINES=151;
    public static final int KW_LOAD=152;
    public static final int KW_LOCAL=153;
    public static final int KW_LOCATION=154;
    public static final int KW_LOCK=155;
    public static final int KW_LOCKS=156;
    public static final int KW_LOGICAL=157;
    public static final int KW_LONG=158;
    public static final int KW_MACRO=159;
    public static final int KW_MAP=160;
    public static final int KW_MAPJOIN=161;
    public static final int KW_MATERIALIZED=162;
    public static final int KW_MINUS=163;
    public static final int KW_MORE=164;
    public static final int KW_MSCK=165;
    public static final int KW_NONE=166;
    public static final int KW_NOSCAN=167;
    public static final int KW_NOT=168;
    public static final int KW_NO_DROP=169;
    public static final int KW_NULL=170;
    public static final int KW_OF=171;
    public static final int KW_OFFLINE=172;
    public static final int KW_ON=173;
    public static final int KW_OPTION=174;
    public static final int KW_OR=175;
    public static final int KW_ORDER=176;
    public static final int KW_OUT=177;
    public static final int KW_OUTER=178;
    public static final int KW_OUTPUTDRIVER=179;
    public static final int KW_OUTPUTFORMAT=180;
    public static final int KW_OVER=181;
    public static final int KW_OVERWRITE=182;
    public static final int KW_OWNER=183;
    public static final int KW_PARTIALSCAN=184;
    public static final int KW_PARTITION=185;
    public static final int KW_PARTITIONED=186;
    public static final int KW_PARTITIONS=187;
    public static final int KW_PERCENT=188;
    public static final int KW_PLUS=189;
    public static final int KW_PRECEDING=190;
    public static final int KW_PRESERVE=191;
    public static final int KW_PRETTY=192;
    public static final int KW_PRINCIPALS=193;
    public static final int KW_PROCEDURE=194;
    public static final int KW_PROTECTION=195;
    public static final int KW_PURGE=196;
    public static final int KW_RANGE=197;
    public static final int KW_READ=198;
    public static final int KW_READONLY=199;
    public static final int KW_READS=200;
    public static final int KW_REBUILD=201;
    public static final int KW_RECORDREADER=202;
    public static final int KW_RECORDWRITER=203;
    public static final int KW_REDUCE=204;
    public static final int KW_REGEXP=205;
    public static final int KW_RENAME=206;
    public static final int KW_REPAIR=207;
    public static final int KW_REPLACE=208;
    public static final int KW_RESTRICT=209;
    public static final int KW_REVOKE=210;
    public static final int KW_REWRITE=211;
    public static final int KW_RIGHT=212;
    public static final int KW_RLIKE=213;
    public static final int KW_ROLE=214;
    public static final int KW_ROLES=215;
    public static final int KW_ROLLUP=216;
    public static final int KW_ROW=217;
    public static final int KW_ROWS=218;
    public static final int KW_SCHEMA=219;
    public static final int KW_SCHEMAS=220;
    public static final int KW_SELECT=221;
    public static final int KW_SEMI=222;
    public static final int KW_SERDE=223;
    public static final int KW_SERDEPROPERTIES=224;
    public static final int KW_SET=225;
    public static final int KW_SETS=226;
    public static final int KW_SHARED=227;
    public static final int KW_SHOW=228;
    public static final int KW_SHOW_DATABASE=229;
    public static final int KW_SKEWED=230;
    public static final int KW_SMALLINT=231;
    public static final int KW_SORT=232;
    public static final int KW_SORTED=233;
    public static final int KW_SSL=234;
    public static final int KW_STATISTICS=235;
    public static final int KW_STORED=236;
    public static final int KW_STREAMTABLE=237;
    public static final int KW_STRING=238;
    public static final int KW_STRUCT=239;
    public static final int KW_TABLE=240;
    public static final int KW_TABLES=241;
    public static final int KW_TABLESAMPLE=242;
    public static final int KW_TBLPROPERTIES=243;
    public static final int KW_TEMPORARY=244;
    public static final int KW_TERMINATED=245;
    public static final int KW_THEN=246;
    public static final int KW_TIMESTAMP=247;
    public static final int KW_TINYINT=248;
    public static final int KW_TO=249;
    public static final int KW_TOUCH=250;
    public static final int KW_TRANSACTIONS=251;
    public static final int KW_TRANSFORM=252;
    public static final int KW_TRIGGER=253;
    public static final int KW_TRUE=254;
    public static final int KW_TRUNCATE=255;
    public static final int KW_UNARCHIVE=256;
    public static final int KW_UNBOUNDED=257;
    public static final int KW_UNDO=258;
    public static final int KW_UNION=259;
    public static final int KW_UNIONTYPE=260;
    public static final int KW_UNIQUEJOIN=261;
    public static final int KW_UNLOCK=262;
    public static final int KW_UNSET=263;
    public static final int KW_UNSIGNED=264;
    public static final int KW_UPDATE=265;
    public static final int KW_USE=266;
    public static final int KW_USER=267;
    public static final int KW_USING=268;
    public static final int KW_UTC=269;
    public static final int KW_UTCTIMESTAMP=270;
    public static final int KW_VALUES=271;
    public static final int KW_VALUE_TYPE=272;
    public static final int KW_VARCHAR=273;
    public static final int KW_VIEW=274;
    public static final int KW_WHEN=275;
    public static final int KW_WHERE=276;
    public static final int KW_WHILE=277;
    public static final int KW_WINDOW=278;
    public static final int KW_WITH=279;
    public static final int LCURLY=280;
    public static final int LESSTHAN=281;
    public static final int LESSTHANOREQUALTO=282;
    public static final int LPAREN=283;
    public static final int LSQUARE=284;
    public static final int Letter=285;
    public static final int MINUS=286;
    public static final int MOD=287;
    public static final int NOTEQUAL=288;
    public static final int Number=289;
    public static final int PLUS=290;
    public static final int QUESTION=291;
    public static final int QuotedIdentifier=292;
    public static final int RCURLY=293;
    public static final int RPAREN=294;
    public static final int RSQUARE=295;
    public static final int RegexComponent=296;
    public static final int SEMICOLON=297;
    public static final int STAR=298;
    public static final int SmallintLiteral=299;
    public static final int StringLiteral=300;
    public static final int TILDE=301;
    public static final int TinyintLiteral=302;
    public static final int WS=303;
    public static final int TOK_ADMIN_OPTION_FOR=578;
    public static final int TOK_ALIASLIST=579;
    public static final int TOK_ALLCOLREF=580;
    public static final int TOK_ALTERDATABASE_OWNER=581;
    public static final int TOK_ALTERDATABASE_PROPERTIES=582;
    public static final int TOK_ALTERINDEX_PROPERTIES=583;
    public static final int TOK_ALTERINDEX_REBUILD=584;
    public static final int TOK_ALTERTABLE=585;
    public static final int TOK_ALTERTABLE_ADDCOLS=586;
    public static final int TOK_ALTERTABLE_ADDPARTS=587;
    public static final int TOK_ALTERTABLE_ARCHIVE=588;
    public static final int TOK_ALTERTABLE_BUCKETS=589;
    public static final int TOK_ALTERTABLE_CHANGECOL_AFTER_POSITION=590;
    public static final int TOK_ALTERTABLE_CLUSTER_SORT=591;
    public static final int TOK_ALTERTABLE_COMPACT=592;
    public static final int TOK_ALTERTABLE_DROPPARTS=593;
    public static final int TOK_ALTERTABLE_DROPPROPERTIES=594;
    public static final int TOK_ALTERTABLE_EXCHANGEPARTITION=595;
    public static final int TOK_ALTERTABLE_FILEFORMAT=596;
    public static final int TOK_ALTERTABLE_LOCATION=597;
    public static final int TOK_ALTERTABLE_MERGEFILES=598;
    public static final int TOK_ALTERTABLE_PARTCOLTYPE=599;
    public static final int TOK_ALTERTABLE_PROPERTIES=600;
    public static final int TOK_ALTERTABLE_PROTECTMODE=601;
    public static final int TOK_ALTERTABLE_RENAME=602;
    public static final int TOK_ALTERTABLE_RENAMECOL=603;
    public static final int TOK_ALTERTABLE_RENAMEPART=604;
    public static final int TOK_ALTERTABLE_REPLACECOLS=605;
    public static final int TOK_ALTERTABLE_SERDEPROPERTIES=606;
    public static final int TOK_ALTERTABLE_SERIALIZER=607;
    public static final int TOK_ALTERTABLE_SKEWED=608;
    public static final int TOK_ALTERTABLE_SKEWED_LOCATION=609;
    public static final int TOK_ALTERTABLE_TOUCH=610;
    public static final int TOK_ALTERTABLE_UNARCHIVE=611;
    public static final int TOK_ALTERTABLE_UPDATECOLSTATS=612;
    public static final int TOK_ALTERVIEW=613;
    public static final int TOK_ALTERVIEW_ADDPARTS=614;
    public static final int TOK_ALTERVIEW_DROPPARTS=615;
    public static final int TOK_ALTERVIEW_DROPPROPERTIES=616;
    public static final int TOK_ALTERVIEW_PROPERTIES=617;
    public static final int TOK_ALTERVIEW_RENAME=618;
    public static final int TOK_ANALYZE=619;
    public static final int TOK_ANONYMOUS=620;
    public static final int TOK_ARCHIVE=621;
    public static final int TOK_BIGINT=622;
    public static final int TOK_BINARY=623;
    public static final int TOK_BOOLEAN=624;
    public static final int TOK_CASCADE=625;
    public static final int TOK_CHAR=626;
    public static final int TOK_CHARSETLITERAL=627;
    public static final int TOK_CLUSTERBY=628;
    public static final int TOK_COLTYPELIST=629;
    public static final int TOK_COL_NAME=630;
    public static final int TOK_CREATEDATABASE=631;
    public static final int TOK_CREATEFUNCTION=632;
    public static final int TOK_CREATEINDEX=633;
    public static final int TOK_CREATEINDEX_INDEXTBLNAME=634;
    public static final int TOK_CREATEMACRO=635;
    public static final int TOK_CREATEROLE=636;
    public static final int TOK_CREATETABLE=637;
    public static final int TOK_CREATEVIEW=638;
    public static final int TOK_CROSSJOIN=639;
    public static final int TOK_CTE=640;
    public static final int TOK_CUBE_GROUPBY=641;
    public static final int TOK_DATABASECOMMENT=642;
    public static final int TOK_DATABASELOCATION=643;
    public static final int TOK_DATABASEPROPERTIES=644;
    public static final int TOK_DATE=645;
    public static final int TOK_DATELITERAL=646;
    public static final int TOK_DATETIME=647;
    public static final int TOK_DBPROPLIST=648;
    public static final int TOK_DB_TYPE=649;
    public static final int TOK_DECIMAL=650;
    public static final int TOK_DEFERRED_REBUILDINDEX=651;
    public static final int TOK_DELETE_FROM=652;
    public static final int TOK_DESCDATABASE=653;
    public static final int TOK_DESCFUNCTION=654;
    public static final int TOK_DESCTABLE=655;
    public static final int TOK_DESTINATION=656;
    public static final int TOK_DIR=657;
    public static final int TOK_DISABLE=658;
    public static final int TOK_DISTRIBUTEBY=659;
    public static final int TOK_DOUBLE=660;
    public static final int TOK_DROPDATABASE=661;
    public static final int TOK_DROPFUNCTION=662;
    public static final int TOK_DROPINDEX=663;
    public static final int TOK_DROPMACRO=664;
    public static final int TOK_DROPROLE=665;
    public static final int TOK_DROPTABLE=666;
    public static final int TOK_DROPVIEW=667;
    public static final int TOK_ENABLE=668;
    public static final int TOK_EXPLAIN=669;
    public static final int TOK_EXPLAIN_SQ_REWRITE=670;
    public static final int TOK_EXPLIST=671;
    public static final int TOK_EXPORT=672;
    public static final int TOK_FALSE=673;
    public static final int TOK_FILE=674;
    public static final int TOK_FILEFORMAT_GENERIC=675;
    public static final int TOK_FLOAT=676;
    public static final int TOK_FROM=677;
    public static final int TOK_FULLOUTERJOIN=678;
    public static final int TOK_FUNCTION=679;
    public static final int TOK_FUNCTIONDI=680;
    public static final int TOK_FUNCTIONSTAR=681;
    public static final int TOK_GRANT=682;
    public static final int TOK_GRANT_OPTION_FOR=683;
    public static final int TOK_GRANT_ROLE=684;
    public static final int TOK_GRANT_WITH_ADMIN_OPTION=685;
    public static final int TOK_GRANT_WITH_OPTION=686;
    public static final int TOK_GROUP=687;
    public static final int TOK_GROUPBY=688;
    public static final int TOK_GROUPING_SETS=689;
    public static final int TOK_GROUPING_SETS_EXPRESSION=690;
    public static final int TOK_HAVING=691;
    public static final int TOK_HINT=692;
    public static final int TOK_HINTARGLIST=693;
    public static final int TOK_HINTLIST=694;
    public static final int TOK_HOLD_DDLTIME=695;
    public static final int TOK_IFEXISTS=696;
    public static final int TOK_IFNOTEXISTS=697;
    public static final int TOK_IGNOREPROTECTION=698;
    public static final int TOK_IMPORT=699;
    public static final int TOK_INDEXCOMMENT=700;
    public static final int TOK_INDEXPROPERTIES=701;
    public static final int TOK_INDEXPROPLIST=702;
    public static final int TOK_INSERT=703;
    public static final int TOK_INSERT_INTO=704;
    public static final int TOK_INT=705;
    public static final int TOK_ISNOTNULL=706;
    public static final int TOK_ISNULL=707;
    public static final int TOK_JAR=708;
    public static final int TOK_JOIN=709;
    public static final int TOK_LATERAL_VIEW=710;
    public static final int TOK_LATERAL_VIEW_OUTER=711;
    public static final int TOK_LEFTOUTERJOIN=712;
    public static final int TOK_LEFTSEMIJOIN=713;
    public static final int TOK_LENGTH=714;
    public static final int TOK_LIKETABLE=715;
    public static final int TOK_LIMIT=716;
    public static final int TOK_LIST=717;
    public static final int TOK_LOAD=718;
    public static final int TOK_LOCAL_DIR=719;
    public static final int TOK_LOCKDB=720;
    public static final int TOK_LOCKTABLE=721;
    public static final int TOK_MAP=722;
    public static final int TOK_MAPJOIN=723;
    public static final int TOK_MSCK=724;
    public static final int TOK_NOT_CLUSTERED=725;
    public static final int TOK_NOT_SORTED=726;
    public static final int TOK_NO_DROP=727;
    public static final int TOK_NULL=728;
    public static final int TOK_OFFLINE=729;
    public static final int TOK_OP_ADD=730;
    public static final int TOK_OP_AND=731;
    public static final int TOK_OP_BITAND=732;
    public static final int TOK_OP_BITNOT=733;
    public static final int TOK_OP_BITOR=734;
    public static final int TOK_OP_BITXOR=735;
    public static final int TOK_OP_DIV=736;
    public static final int TOK_OP_EQ=737;
    public static final int TOK_OP_GE=738;
    public static final int TOK_OP_GT=739;
    public static final int TOK_OP_LE=740;
    public static final int TOK_OP_LIKE=741;
    public static final int TOK_OP_LT=742;
    public static final int TOK_OP_MOD=743;
    public static final int TOK_OP_MUL=744;
    public static final int TOK_OP_NE=745;
    public static final int TOK_OP_NOT=746;
    public static final int TOK_OP_OR=747;
    public static final int TOK_OP_SUB=748;
    public static final int TOK_ORDERBY=749;
    public static final int TOK_ORREPLACE=750;
    public static final int TOK_PARTITIONINGSPEC=751;
    public static final int TOK_PARTITIONLOCATION=752;
    public static final int TOK_PARTSPEC=753;
    public static final int TOK_PARTVAL=754;
    public static final int TOK_PERCENT=755;
    public static final int TOK_PRINCIPAL_NAME=756;
    public static final int TOK_PRIVILEGE=757;
    public static final int TOK_PRIVILEGE_LIST=758;
    public static final int TOK_PRIV_ALL=759;
    public static final int TOK_PRIV_ALTER_DATA=760;
    public static final int TOK_PRIV_ALTER_METADATA=761;
    public static final int TOK_PRIV_CREATE=762;
    public static final int TOK_PRIV_DELETE=763;
    public static final int TOK_PRIV_DROP=764;
    public static final int TOK_PRIV_INDEX=765;
    public static final int TOK_PRIV_INSERT=766;
    public static final int TOK_PRIV_LOCK=767;
    public static final int TOK_PRIV_OBJECT=768;
    public static final int TOK_PRIV_OBJECT_COL=769;
    public static final int TOK_PRIV_SELECT=770;
    public static final int TOK_PRIV_SHOW_DATABASE=771;
    public static final int TOK_PTBLFUNCTION=772;
    public static final int TOK_QUERY=773;
    public static final int TOK_READONLY=774;
    public static final int TOK_RECORDREADER=775;
    public static final int TOK_RECORDWRITER=776;
    public static final int TOK_RESOURCE_ALL=777;
    public static final int TOK_RESOURCE_LIST=778;
    public static final int TOK_RESOURCE_URI=779;
    public static final int TOK_RESTRICT=780;
    public static final int TOK_REVOKE=781;
    public static final int TOK_REVOKE_ROLE=782;
    public static final int TOK_RIGHTOUTERJOIN=783;
    public static final int TOK_ROLE=784;
    public static final int TOK_ROLLUP_GROUPBY=785;
    public static final int TOK_ROWCOUNT=786;
    public static final int TOK_SELECT=787;
    public static final int TOK_SELECTDI=788;
    public static final int TOK_SELEXPR=789;
    public static final int TOK_SERDE=790;
    public static final int TOK_SERDENAME=791;
    public static final int TOK_SERDEPROPS=792;
    public static final int TOK_SET_COLUMNS_CLAUSE=793;
    public static final int TOK_SHOWCOLUMNS=794;
    public static final int TOK_SHOWCONF=795;
    public static final int TOK_SHOWDATABASES=796;
    public static final int TOK_SHOWDBLOCKS=797;
    public static final int TOK_SHOWFUNCTIONS=798;
    public static final int TOK_SHOWINDEXES=799;
    public static final int TOK_SHOWLOCKS=800;
    public static final int TOK_SHOWPARTITIONS=801;
    public static final int TOK_SHOWTABLES=802;
    public static final int TOK_SHOW_COMPACTIONS=803;
    public static final int TOK_SHOW_CREATETABLE=804;
    public static final int TOK_SHOW_GRANT=805;
    public static final int TOK_SHOW_ROLES=806;
    public static final int TOK_SHOW_ROLE_GRANT=807;
    public static final int TOK_SHOW_ROLE_PRINCIPALS=808;
    public static final int TOK_SHOW_SET_ROLE=809;
    public static final int TOK_SHOW_TABLESTATUS=810;
    public static final int TOK_SHOW_TBLPROPERTIES=811;
    public static final int TOK_SHOW_TRANSACTIONS=812;
    public static final int TOK_SKEWED_LOCATIONS=813;
    public static final int TOK_SKEWED_LOCATION_LIST=814;
    public static final int TOK_SKEWED_LOCATION_MAP=815;
    public static final int TOK_SMALLINT=816;
    public static final int TOK_SORTBY=817;
    public static final int TOK_STORAGEHANDLER=818;
    public static final int TOK_STOREDASDIRS=819;
    public static final int TOK_STREAMTABLE=820;
    public static final int TOK_STRING=821;
    public static final int TOK_STRINGLITERALSEQUENCE=822;
    public static final int TOK_STRUCT=823;
    public static final int TOK_SUBQUERY=824;
    public static final int TOK_SUBQUERY_EXPR=825;
    public static final int TOK_SUBQUERY_OP=826;
    public static final int TOK_SUBQUERY_OP_NOTEXISTS=827;
    public static final int TOK_SUBQUERY_OP_NOTIN=828;
    public static final int TOK_SWITCHDATABASE=829;
    public static final int TOK_TAB=830;
    public static final int TOK_TABALIAS=831;
    public static final int TOK_TABCOL=832;
    public static final int TOK_TABCOLLIST=833;
    public static final int TOK_TABCOLNAME=834;
    public static final int TOK_TABCOLVALUE=835;
    public static final int TOK_TABCOLVALUES=836;
    public static final int TOK_TABCOLVALUE_PAIR=837;
    public static final int TOK_TABLEBUCKETSAMPLE=838;
    public static final int TOK_TABLECOMMENT=839;
    public static final int TOK_TABLEFILEFORMAT=840;
    public static final int TOK_TABLELOCATION=841;
    public static final int TOK_TABLEPARTCOLS=842;
    public static final int TOK_TABLEPROPERTIES=843;
    public static final int TOK_TABLEPROPERTY=844;
    public static final int TOK_TABLEPROPLIST=845;
    public static final int TOK_TABLEROWFORMAT=846;
    public static final int TOK_TABLEROWFORMATCOLLITEMS=847;
    public static final int TOK_TABLEROWFORMATFIELD=848;
    public static final int TOK_TABLEROWFORMATLINES=849;
    public static final int TOK_TABLEROWFORMATMAPKEYS=850;
    public static final int TOK_TABLEROWFORMATNULL=851;
    public static final int TOK_TABLESERIALIZER=852;
    public static final int TOK_TABLESKEWED=853;
    public static final int TOK_TABLESPLITSAMPLE=854;
    public static final int TOK_TABLE_OR_COL=855;
    public static final int TOK_TABLE_PARTITION=856;
    public static final int TOK_TABLE_TYPE=857;
    public static final int TOK_TABNAME=858;
    public static final int TOK_TABREF=859;
    public static final int TOK_TABSORTCOLNAMEASC=860;
    public static final int TOK_TABSORTCOLNAMEDESC=861;
    public static final int TOK_TABSRC=862;
    public static final int TOK_TABTYPE=863;
    public static final int TOK_TEMPORARY=864;
    public static final int TOK_TIMESTAMP=865;
    public static final int TOK_TINYINT=866;
    public static final int TOK_TMP_FILE=867;
    public static final int TOK_TRANSFORM=868;
    public static final int TOK_TRUE=869;
    public static final int TOK_TRUNCATETABLE=870;
    public static final int TOK_UNION=871;
    public static final int TOK_UNIONTYPE=872;
    public static final int TOK_UNIQUEJOIN=873;
    public static final int TOK_UNLOCKDB=874;
    public static final int TOK_UNLOCKTABLE=875;
    public static final int TOK_UPDATE_TABLE=876;
    public static final int TOK_USER=877;
    public static final int TOK_USERSCRIPTCOLNAMES=878;
    public static final int TOK_USERSCRIPTCOLSCHEMA=879;
    public static final int TOK_VALUES_TABLE=880;
    public static final int TOK_VALUE_ROW=881;
    public static final int TOK_VARCHAR=882;
    public static final int TOK_VIEWPARTCOLS=883;
    public static final int TOK_VIRTUAL_TABLE=884;
    public static final int TOK_VIRTUAL_TABREF=885;
    public static final int TOK_WHERE=886;
    public static final int TOK_WINDOWDEF=887;
    public static final int TOK_WINDOWRANGE=888;
    public static final int TOK_WINDOWSPEC=889;
    public static final int TOK_WINDOWVALUES=890;

    // delegates
    public Parser[] getDelegates() {
        return new Parser[] {};
    }

    // delegators
    public HiveParser gHiveParser;
    public HiveParser gParent;


    public HiveParser_IdentifiersParser(TokenStream input, HiveParser gHiveParser) {
        this(input, new RecognizerSharedState(), gHiveParser);
    }
    public HiveParser_IdentifiersParser(TokenStream input, RecognizerSharedState state, HiveParser gHiveParser) {
        super(input, state);
        this.gHiveParser = gHiveParser;
        gParent = gHiveParser;
    }

protected TreeAdaptor adaptor = new CommonTreeAdaptor();

public void setTreeAdaptor(TreeAdaptor adaptor) {
    this.adaptor = adaptor;
}
public TreeAdaptor getTreeAdaptor() {
    return adaptor;
}
    public String[] getTokenNames() { return HiveParser.tokenNames; }
    public String getGrammarFileName() { return "IdentifiersParser.g"; }


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


    public static class groupByClause_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "groupByClause"
    // IdentifiersParser.g:49:1: groupByClause : KW_GROUP KW_BY groupByExpression ( COMMA groupByExpression )* ( (rollup= KW_WITH KW_ROLLUP ) | (cube= KW_WITH KW_CUBE ) )? (sets= KW_GROUPING KW_SETS LPAREN groupingSetExpression ( COMMA groupingSetExpression )* RPAREN )? -> {rollup != null}? ^( TOK_ROLLUP_GROUPBY ( groupByExpression )+ ) -> {cube != null}? ^( TOK_CUBE_GROUPBY ( groupByExpression )+ ) -> {sets != null}? ^( TOK_GROUPING_SETS ( groupByExpression )+ ( groupingSetExpression )+ ) -> ^( TOK_GROUPBY ( groupByExpression )+ ) ;
    public final HiveParser_IdentifiersParser.groupByClause_return groupByClause() throws RecognitionException {
        HiveParser_IdentifiersParser.groupByClause_return retval = new HiveParser_IdentifiersParser.groupByClause_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token rollup=null;
        Token cube=null;
        Token sets=null;
        Token KW_GROUP1=null;
        Token KW_BY2=null;
        Token COMMA4=null;
        Token KW_ROLLUP6=null;
        Token KW_CUBE7=null;
        Token KW_SETS8=null;
        Token LPAREN9=null;
        Token COMMA11=null;
        Token RPAREN13=null;
        HiveParser_IdentifiersParser.groupByExpression_return groupByExpression3 =null;

        HiveParser_IdentifiersParser.groupByExpression_return groupByExpression5 =null;

        HiveParser_IdentifiersParser.groupingSetExpression_return groupingSetExpression10 =null;

        HiveParser_IdentifiersParser.groupingSetExpression_return groupingSetExpression12 =null;


        CommonTree rollup_tree=null;
        CommonTree cube_tree=null;
        CommonTree sets_tree=null;
        CommonTree KW_GROUP1_tree=null;
        CommonTree KW_BY2_tree=null;
        CommonTree COMMA4_tree=null;
        CommonTree KW_ROLLUP6_tree=null;
        CommonTree KW_CUBE7_tree=null;
        CommonTree KW_SETS8_tree=null;
        CommonTree LPAREN9_tree=null;
        CommonTree COMMA11_tree=null;
        CommonTree RPAREN13_tree=null;
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_KW_GROUPING=new RewriteRuleTokenStream(adaptor,"token KW_GROUPING");
        RewriteRuleTokenStream stream_KW_CUBE=new RewriteRuleTokenStream(adaptor,"token KW_CUBE");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_KW_ROLLUP=new RewriteRuleTokenStream(adaptor,"token KW_ROLLUP");
        RewriteRuleTokenStream stream_KW_WITH=new RewriteRuleTokenStream(adaptor,"token KW_WITH");
        RewriteRuleTokenStream stream_KW_GROUP=new RewriteRuleTokenStream(adaptor,"token KW_GROUP");
        RewriteRuleTokenStream stream_KW_SETS=new RewriteRuleTokenStream(adaptor,"token KW_SETS");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleTokenStream stream_KW_BY=new RewriteRuleTokenStream(adaptor,"token KW_BY");
        RewriteRuleSubtreeStream stream_groupingSetExpression=new RewriteRuleSubtreeStream(adaptor,"rule groupingSetExpression");
        RewriteRuleSubtreeStream stream_groupByExpression=new RewriteRuleSubtreeStream(adaptor,"rule groupByExpression");
         gParent.pushMsg("group by clause", state); 
        try {
            // IdentifiersParser.g:52:5: ( KW_GROUP KW_BY groupByExpression ( COMMA groupByExpression )* ( (rollup= KW_WITH KW_ROLLUP ) | (cube= KW_WITH KW_CUBE ) )? (sets= KW_GROUPING KW_SETS LPAREN groupingSetExpression ( COMMA groupingSetExpression )* RPAREN )? -> {rollup != null}? ^( TOK_ROLLUP_GROUPBY ( groupByExpression )+ ) -> {cube != null}? ^( TOK_CUBE_GROUPBY ( groupByExpression )+ ) -> {sets != null}? ^( TOK_GROUPING_SETS ( groupByExpression )+ ( groupingSetExpression )+ ) -> ^( TOK_GROUPBY ( groupByExpression )+ ) )
            // IdentifiersParser.g:53:5: KW_GROUP KW_BY groupByExpression ( COMMA groupByExpression )* ( (rollup= KW_WITH KW_ROLLUP ) | (cube= KW_WITH KW_CUBE ) )? (sets= KW_GROUPING KW_SETS LPAREN groupingSetExpression ( COMMA groupingSetExpression )* RPAREN )?
            {
            KW_GROUP1=(Token)match(input,KW_GROUP,FOLLOW_KW_GROUP_in_groupByClause72); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KW_GROUP.add(KW_GROUP1);


            KW_BY2=(Token)match(input,KW_BY,FOLLOW_KW_BY_in_groupByClause74); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KW_BY.add(KW_BY2);


            pushFollow(FOLLOW_groupByExpression_in_groupByClause80);
            groupByExpression3=groupByExpression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_groupByExpression.add(groupByExpression3.getTree());

            // IdentifiersParser.g:55:5: ( COMMA groupByExpression )*
            loop1:
            do {
                int alt1=2;
                switch ( input.LA(1) ) {
                case COMMA:
                    {
                    alt1=1;
                    }
                    break;

                }

                switch (alt1) {
            	case 1 :
            	    // IdentifiersParser.g:55:7: COMMA groupByExpression
            	    {
            	    COMMA4=(Token)match(input,COMMA,FOLLOW_COMMA_in_groupByClause88); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA4);


            	    pushFollow(FOLLOW_groupByExpression_in_groupByClause90);
            	    groupByExpression5=groupByExpression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_groupByExpression.add(groupByExpression5.getTree());

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            // IdentifiersParser.g:56:5: ( (rollup= KW_WITH KW_ROLLUP ) | (cube= KW_WITH KW_CUBE ) )?
            int alt2=3;
            switch ( input.LA(1) ) {
                case KW_WITH:
                    {
                    switch ( input.LA(2) ) {
                        case KW_ROLLUP:
                            {
                            alt2=1;
                            }
                            break;
                        case KW_CUBE:
                            {
                            alt2=2;
                            }
                            break;
                    }

                    }
                    break;
            }

            switch (alt2) {
                case 1 :
                    // IdentifiersParser.g:56:6: (rollup= KW_WITH KW_ROLLUP )
                    {
                    // IdentifiersParser.g:56:6: (rollup= KW_WITH KW_ROLLUP )
                    // IdentifiersParser.g:56:7: rollup= KW_WITH KW_ROLLUP
                    {
                    rollup=(Token)match(input,KW_WITH,FOLLOW_KW_WITH_in_groupByClause103); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_KW_WITH.add(rollup);


                    KW_ROLLUP6=(Token)match(input,KW_ROLLUP,FOLLOW_KW_ROLLUP_in_groupByClause105); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_KW_ROLLUP.add(KW_ROLLUP6);


                    }


                    }
                    break;
                case 2 :
                    // IdentifiersParser.g:56:35: (cube= KW_WITH KW_CUBE )
                    {
                    // IdentifiersParser.g:56:35: (cube= KW_WITH KW_CUBE )
                    // IdentifiersParser.g:56:36: cube= KW_WITH KW_CUBE
                    {
                    cube=(Token)match(input,KW_WITH,FOLLOW_KW_WITH_in_groupByClause113); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_KW_WITH.add(cube);


                    KW_CUBE7=(Token)match(input,KW_CUBE,FOLLOW_KW_CUBE_in_groupByClause115); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_KW_CUBE.add(KW_CUBE7);


                    }


                    }
                    break;

            }


            // IdentifiersParser.g:57:5: (sets= KW_GROUPING KW_SETS LPAREN groupingSetExpression ( COMMA groupingSetExpression )* RPAREN )?
            int alt4=2;
            switch ( input.LA(1) ) {
                case KW_GROUPING:
                    {
                    alt4=1;
                    }
                    break;
            }

            switch (alt4) {
                case 1 :
                    // IdentifiersParser.g:57:6: sets= KW_GROUPING KW_SETS LPAREN groupingSetExpression ( COMMA groupingSetExpression )* RPAREN
                    {
                    sets=(Token)match(input,KW_GROUPING,FOLLOW_KW_GROUPING_in_groupByClause128); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_KW_GROUPING.add(sets);


                    KW_SETS8=(Token)match(input,KW_SETS,FOLLOW_KW_SETS_in_groupByClause130); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_KW_SETS.add(KW_SETS8);


                    LPAREN9=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_groupByClause137); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN9);


                    pushFollow(FOLLOW_groupingSetExpression_in_groupByClause139);
                    groupingSetExpression10=groupingSetExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_groupingSetExpression.add(groupingSetExpression10.getTree());

                    // IdentifiersParser.g:58:34: ( COMMA groupingSetExpression )*
                    loop3:
                    do {
                        int alt3=2;
                        switch ( input.LA(1) ) {
                        case COMMA:
                            {
                            alt3=1;
                            }
                            break;

                        }

                        switch (alt3) {
                    	case 1 :
                    	    // IdentifiersParser.g:58:36: COMMA groupingSetExpression
                    	    {
                    	    COMMA11=(Token)match(input,COMMA,FOLLOW_COMMA_in_groupByClause143); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA11);


                    	    pushFollow(FOLLOW_groupingSetExpression_in_groupByClause145);
                    	    groupingSetExpression12=groupingSetExpression();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_groupingSetExpression.add(groupingSetExpression12.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop3;
                        }
                    } while (true);


                    RPAREN13=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_groupByClause150); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN13);


                    }
                    break;

            }


            // AST REWRITE
            // elements: groupByExpression, groupingSetExpression, groupByExpression, groupByExpression, groupByExpression
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 59:5: -> {rollup != null}? ^( TOK_ROLLUP_GROUPBY ( groupByExpression )+ )
            if (rollup != null) {
                // IdentifiersParser.g:59:26: ^( TOK_ROLLUP_GROUPBY ( groupByExpression )+ )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(TOK_ROLLUP_GROUPBY, "TOK_ROLLUP_GROUPBY")
                , root_1);

                if ( !(stream_groupByExpression.hasNext()) ) {
                    throw new RewriteEarlyExitException();
                }
                while ( stream_groupByExpression.hasNext() ) {
                    adaptor.addChild(root_1, stream_groupByExpression.nextTree());

                }
                stream_groupByExpression.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            else // 60:5: -> {cube != null}? ^( TOK_CUBE_GROUPBY ( groupByExpression )+ )
            if (cube != null) {
                // IdentifiersParser.g:60:24: ^( TOK_CUBE_GROUPBY ( groupByExpression )+ )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(TOK_CUBE_GROUPBY, "TOK_CUBE_GROUPBY")
                , root_1);

                if ( !(stream_groupByExpression.hasNext()) ) {
                    throw new RewriteEarlyExitException();
                }
                while ( stream_groupByExpression.hasNext() ) {
                    adaptor.addChild(root_1, stream_groupByExpression.nextTree());

                }
                stream_groupByExpression.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            else // 61:5: -> {sets != null}? ^( TOK_GROUPING_SETS ( groupByExpression )+ ( groupingSetExpression )+ )
            if (sets != null) {
                // IdentifiersParser.g:61:24: ^( TOK_GROUPING_SETS ( groupByExpression )+ ( groupingSetExpression )+ )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(TOK_GROUPING_SETS, "TOK_GROUPING_SETS")
                , root_1);

                if ( !(stream_groupByExpression.hasNext()) ) {
                    throw new RewriteEarlyExitException();
                }
                while ( stream_groupByExpression.hasNext() ) {
                    adaptor.addChild(root_1, stream_groupByExpression.nextTree());

                }
                stream_groupByExpression.reset();

                if ( !(stream_groupingSetExpression.hasNext()) ) {
                    throw new RewriteEarlyExitException();
                }
                while ( stream_groupingSetExpression.hasNext() ) {
                    adaptor.addChild(root_1, stream_groupingSetExpression.nextTree());

                }
                stream_groupingSetExpression.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            else // 62:5: -> ^( TOK_GROUPBY ( groupByExpression )+ )
            {
                // IdentifiersParser.g:62:8: ^( TOK_GROUPBY ( groupByExpression )+ )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(TOK_GROUPBY, "TOK_GROUPBY")
                , root_1);

                if ( !(stream_groupByExpression.hasNext()) ) {
                    throw new RewriteEarlyExitException();
                }
                while ( stream_groupByExpression.hasNext() ) {
                    adaptor.addChild(root_1, stream_groupByExpression.nextTree());

                }
                stream_groupByExpression.reset();

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
            if ( state.backtracking==0 ) { gParent.popMsg(state); }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "groupByClause"


    public static class groupingSetExpression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "groupingSetExpression"
    // IdentifiersParser.g:65:1: groupingSetExpression : ( groupByExpression -> ^( TOK_GROUPING_SETS_EXPRESSION groupByExpression ) | LPAREN groupByExpression ( COMMA groupByExpression )* RPAREN -> ^( TOK_GROUPING_SETS_EXPRESSION ( groupByExpression )+ ) | LPAREN RPAREN -> ^( TOK_GROUPING_SETS_EXPRESSION ) );
    public final HiveParser_IdentifiersParser.groupingSetExpression_return groupingSetExpression() throws RecognitionException {
        HiveParser_IdentifiersParser.groupingSetExpression_return retval = new HiveParser_IdentifiersParser.groupingSetExpression_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token LPAREN15=null;
        Token COMMA17=null;
        Token RPAREN19=null;
        Token LPAREN20=null;
        Token RPAREN21=null;
        HiveParser_IdentifiersParser.groupByExpression_return groupByExpression14 =null;

        HiveParser_IdentifiersParser.groupByExpression_return groupByExpression16 =null;

        HiveParser_IdentifiersParser.groupByExpression_return groupByExpression18 =null;


        CommonTree LPAREN15_tree=null;
        CommonTree COMMA17_tree=null;
        CommonTree RPAREN19_tree=null;
        CommonTree LPAREN20_tree=null;
        CommonTree RPAREN21_tree=null;
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleSubtreeStream stream_groupByExpression=new RewriteRuleSubtreeStream(adaptor,"rule groupByExpression");
        gParent.pushMsg("grouping set expression", state); 
        try {
            // IdentifiersParser.g:68:4: ( groupByExpression -> ^( TOK_GROUPING_SETS_EXPRESSION groupByExpression ) | LPAREN groupByExpression ( COMMA groupByExpression )* RPAREN -> ^( TOK_GROUPING_SETS_EXPRESSION ( groupByExpression )+ ) | LPAREN RPAREN -> ^( TOK_GROUPING_SETS_EXPRESSION ) )
            int alt6=3;
            alt6 = dfa6.predict(input);
            switch (alt6) {
                case 1 :
                    // IdentifiersParser.g:69:4: groupByExpression
                    {
                    pushFollow(FOLLOW_groupByExpression_in_groupingSetExpression244);
                    groupByExpression14=groupByExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_groupByExpression.add(groupByExpression14.getTree());

                    // AST REWRITE
                    // elements: groupByExpression
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 70:4: -> ^( TOK_GROUPING_SETS_EXPRESSION groupByExpression )
                    {
                        // IdentifiersParser.g:70:7: ^( TOK_GROUPING_SETS_EXPRESSION groupByExpression )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(TOK_GROUPING_SETS_EXPRESSION, "TOK_GROUPING_SETS_EXPRESSION")
                        , root_1);

                        adaptor.addChild(root_1, stream_groupByExpression.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 2 :
                    // IdentifiersParser.g:72:4: LPAREN groupByExpression ( COMMA groupByExpression )* RPAREN
                    {
                    LPAREN15=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_groupingSetExpression265); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN15);


                    pushFollow(FOLLOW_groupByExpression_in_groupingSetExpression271);
                    groupByExpression16=groupByExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_groupByExpression.add(groupByExpression16.getTree());

                    // IdentifiersParser.g:73:22: ( COMMA groupByExpression )*
                    loop5:
                    do {
                        int alt5=2;
                        switch ( input.LA(1) ) {
                        case COMMA:
                            {
                            alt5=1;
                            }
                            break;

                        }

                        switch (alt5) {
                    	case 1 :
                    	    // IdentifiersParser.g:73:23: COMMA groupByExpression
                    	    {
                    	    COMMA17=(Token)match(input,COMMA,FOLLOW_COMMA_in_groupingSetExpression274); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA17);


                    	    pushFollow(FOLLOW_groupByExpression_in_groupingSetExpression276);
                    	    groupByExpression18=groupByExpression();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_groupByExpression.add(groupByExpression18.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop5;
                        }
                    } while (true);


                    RPAREN19=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_groupingSetExpression283); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN19);


                    // AST REWRITE
                    // elements: groupByExpression
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 75:4: -> ^( TOK_GROUPING_SETS_EXPRESSION ( groupByExpression )+ )
                    {
                        // IdentifiersParser.g:75:7: ^( TOK_GROUPING_SETS_EXPRESSION ( groupByExpression )+ )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(TOK_GROUPING_SETS_EXPRESSION, "TOK_GROUPING_SETS_EXPRESSION")
                        , root_1);

                        if ( !(stream_groupByExpression.hasNext()) ) {
                            throw new RewriteEarlyExitException();
                        }
                        while ( stream_groupByExpression.hasNext() ) {
                            adaptor.addChild(root_1, stream_groupByExpression.nextTree());

                        }
                        stream_groupByExpression.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 3 :
                    // IdentifiersParser.g:77:4: LPAREN RPAREN
                    {
                    LPAREN20=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_groupingSetExpression305); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN20);


                    RPAREN21=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_groupingSetExpression310); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN21);


                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 79:4: -> ^( TOK_GROUPING_SETS_EXPRESSION )
                    {
                        // IdentifiersParser.g:79:7: ^( TOK_GROUPING_SETS_EXPRESSION )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(TOK_GROUPING_SETS_EXPRESSION, "TOK_GROUPING_SETS_EXPRESSION")
                        , root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
            if ( state.backtracking==0 ) {gParent.popMsg(state); }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "groupingSetExpression"


    public static class groupByExpression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "groupByExpression"
    // IdentifiersParser.g:83:1: groupByExpression : expression ;
    public final HiveParser_IdentifiersParser.groupByExpression_return groupByExpression() throws RecognitionException {
        HiveParser_IdentifiersParser.groupByExpression_return retval = new HiveParser_IdentifiersParser.groupByExpression_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        HiveParser_IdentifiersParser.expression_return expression22 =null;



         gParent.pushMsg("group by expression", state); 
        try {
            // IdentifiersParser.g:86:5: ( expression )
            // IdentifiersParser.g:87:5: expression
            {
            root_0 = (CommonTree)adaptor.nil();


            pushFollow(FOLLOW_expression_in_groupByExpression350);
            expression22=expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, expression22.getTree());

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
            if ( state.backtracking==0 ) { gParent.popMsg(state); }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "groupByExpression"


    public static class havingClause_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "havingClause"
    // IdentifiersParser.g:90:1: havingClause : KW_HAVING havingCondition -> ^( TOK_HAVING havingCondition ) ;
    public final HiveParser_IdentifiersParser.havingClause_return havingClause() throws RecognitionException {
        HiveParser_IdentifiersParser.havingClause_return retval = new HiveParser_IdentifiersParser.havingClause_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token KW_HAVING23=null;
        HiveParser_IdentifiersParser.havingCondition_return havingCondition24 =null;


        CommonTree KW_HAVING23_tree=null;
        RewriteRuleTokenStream stream_KW_HAVING=new RewriteRuleTokenStream(adaptor,"token KW_HAVING");
        RewriteRuleSubtreeStream stream_havingCondition=new RewriteRuleSubtreeStream(adaptor,"rule havingCondition");
         gParent.pushMsg("having clause", state); 
        try {
            // IdentifiersParser.g:93:5: ( KW_HAVING havingCondition -> ^( TOK_HAVING havingCondition ) )
            // IdentifiersParser.g:94:5: KW_HAVING havingCondition
            {
            KW_HAVING23=(Token)match(input,KW_HAVING,FOLLOW_KW_HAVING_in_havingClause381); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KW_HAVING.add(KW_HAVING23);


            pushFollow(FOLLOW_havingCondition_in_havingClause383);
            havingCondition24=havingCondition();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_havingCondition.add(havingCondition24.getTree());

            // AST REWRITE
            // elements: havingCondition
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 94:31: -> ^( TOK_HAVING havingCondition )
            {
                // IdentifiersParser.g:94:34: ^( TOK_HAVING havingCondition )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(TOK_HAVING, "TOK_HAVING")
                , root_1);

                adaptor.addChild(root_1, stream_havingCondition.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
            if ( state.backtracking==0 ) { gParent.popMsg(state); }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "havingClause"


    public static class havingCondition_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "havingCondition"
    // IdentifiersParser.g:97:1: havingCondition : expression ;
    public final HiveParser_IdentifiersParser.havingCondition_return havingCondition() throws RecognitionException {
        HiveParser_IdentifiersParser.havingCondition_return retval = new HiveParser_IdentifiersParser.havingCondition_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        HiveParser_IdentifiersParser.expression_return expression25 =null;



         gParent.pushMsg("having condition", state); 
        try {
            // IdentifiersParser.g:100:5: ( expression )
            // IdentifiersParser.g:101:5: expression
            {
            root_0 = (CommonTree)adaptor.nil();


            pushFollow(FOLLOW_expression_in_havingCondition422);
            expression25=expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, expression25.getTree());

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
            if ( state.backtracking==0 ) { gParent.popMsg(state); }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "havingCondition"


    public static class orderByClause_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "orderByClause"
    // IdentifiersParser.g:105:1: orderByClause : KW_ORDER KW_BY columnRefOrder ( COMMA columnRefOrder )* -> ^( TOK_ORDERBY ( columnRefOrder )+ ) ;
    public final HiveParser_IdentifiersParser.orderByClause_return orderByClause() throws RecognitionException {
        HiveParser_IdentifiersParser.orderByClause_return retval = new HiveParser_IdentifiersParser.orderByClause_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token KW_ORDER26=null;
        Token KW_BY27=null;
        Token COMMA29=null;
        HiveParser.columnRefOrder_return columnRefOrder28 =null;

        HiveParser.columnRefOrder_return columnRefOrder30 =null;


        CommonTree KW_ORDER26_tree=null;
        CommonTree KW_BY27_tree=null;
        CommonTree COMMA29_tree=null;
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_KW_ORDER=new RewriteRuleTokenStream(adaptor,"token KW_ORDER");
        RewriteRuleTokenStream stream_KW_BY=new RewriteRuleTokenStream(adaptor,"token KW_BY");
        RewriteRuleSubtreeStream stream_columnRefOrder=new RewriteRuleSubtreeStream(adaptor,"rule columnRefOrder");
         gParent.pushMsg("order by clause", state); 
        try {
            // IdentifiersParser.g:108:5: ( KW_ORDER KW_BY columnRefOrder ( COMMA columnRefOrder )* -> ^( TOK_ORDERBY ( columnRefOrder )+ ) )
            // IdentifiersParser.g:109:5: KW_ORDER KW_BY columnRefOrder ( COMMA columnRefOrder )*
            {
            KW_ORDER26=(Token)match(input,KW_ORDER,FOLLOW_KW_ORDER_in_orderByClause454); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KW_ORDER.add(KW_ORDER26);


            KW_BY27=(Token)match(input,KW_BY,FOLLOW_KW_BY_in_orderByClause456); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KW_BY.add(KW_BY27);


            pushFollow(FOLLOW_columnRefOrder_in_orderByClause458);
            columnRefOrder28=gHiveParser.columnRefOrder();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_columnRefOrder.add(columnRefOrder28.getTree());

            // IdentifiersParser.g:109:35: ( COMMA columnRefOrder )*
            loop7:
            do {
                int alt7=2;
                switch ( input.LA(1) ) {
                case COMMA:
                    {
                    alt7=1;
                    }
                    break;

                }

                switch (alt7) {
            	case 1 :
            	    // IdentifiersParser.g:109:37: COMMA columnRefOrder
            	    {
            	    COMMA29=(Token)match(input,COMMA,FOLLOW_COMMA_in_orderByClause462); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA29);


            	    pushFollow(FOLLOW_columnRefOrder_in_orderByClause464);
            	    columnRefOrder30=gHiveParser.columnRefOrder();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_columnRefOrder.add(columnRefOrder30.getTree());

            	    }
            	    break;

            	default :
            	    break loop7;
                }
            } while (true);


            // AST REWRITE
            // elements: columnRefOrder
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 109:60: -> ^( TOK_ORDERBY ( columnRefOrder )+ )
            {
                // IdentifiersParser.g:109:63: ^( TOK_ORDERBY ( columnRefOrder )+ )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(TOK_ORDERBY, "TOK_ORDERBY")
                , root_1);

                if ( !(stream_columnRefOrder.hasNext()) ) {
                    throw new RewriteEarlyExitException();
                }
                while ( stream_columnRefOrder.hasNext() ) {
                    adaptor.addChild(root_1, stream_columnRefOrder.nextTree());

                }
                stream_columnRefOrder.reset();

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
            if ( state.backtracking==0 ) { gParent.popMsg(state); }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "orderByClause"


    public static class clusterByClause_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "clusterByClause"
    // IdentifiersParser.g:112:1: clusterByClause : ( KW_CLUSTER KW_BY LPAREN expression ( COMMA expression )* RPAREN -> ^( TOK_CLUSTERBY ( expression )+ ) | KW_CLUSTER KW_BY expression ( ( COMMA )=> COMMA expression )* -> ^( TOK_CLUSTERBY ( expression )+ ) );
    public final HiveParser_IdentifiersParser.clusterByClause_return clusterByClause() throws RecognitionException {
        HiveParser_IdentifiersParser.clusterByClause_return retval = new HiveParser_IdentifiersParser.clusterByClause_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token KW_CLUSTER31=null;
        Token KW_BY32=null;
        Token LPAREN33=null;
        Token COMMA35=null;
        Token RPAREN37=null;
        Token KW_CLUSTER38=null;
        Token KW_BY39=null;
        Token COMMA41=null;
        HiveParser_IdentifiersParser.expression_return expression34 =null;

        HiveParser_IdentifiersParser.expression_return expression36 =null;

        HiveParser_IdentifiersParser.expression_return expression40 =null;

        HiveParser_IdentifiersParser.expression_return expression42 =null;


        CommonTree KW_CLUSTER31_tree=null;
        CommonTree KW_BY32_tree=null;
        CommonTree LPAREN33_tree=null;
        CommonTree COMMA35_tree=null;
        CommonTree RPAREN37_tree=null;
        CommonTree KW_CLUSTER38_tree=null;
        CommonTree KW_BY39_tree=null;
        CommonTree COMMA41_tree=null;
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_KW_CLUSTER=new RewriteRuleTokenStream(adaptor,"token KW_CLUSTER");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleTokenStream stream_KW_BY=new RewriteRuleTokenStream(adaptor,"token KW_BY");
        RewriteRuleSubtreeStream stream_expression=new RewriteRuleSubtreeStream(adaptor,"rule expression");
         gParent.pushMsg("cluster by clause", state); 
        try {
            // IdentifiersParser.g:115:5: ( KW_CLUSTER KW_BY LPAREN expression ( COMMA expression )* RPAREN -> ^( TOK_CLUSTERBY ( expression )+ ) | KW_CLUSTER KW_BY expression ( ( COMMA )=> COMMA expression )* -> ^( TOK_CLUSTERBY ( expression )+ ) )
            int alt10=2;
            switch ( input.LA(1) ) {
            case KW_CLUSTER:
                {
                switch ( input.LA(2) ) {
                case KW_BY:
                    {
                    switch ( input.LA(3) ) {
                    case LPAREN:
                        {
                        alt10=1;
                        }
                        break;
                    case BigintLiteral:
                    case CharSetName:
                    case DecimalLiteral:
                    case Identifier:
                    case KW_ADD:
                    case KW_ADMIN:
                    case KW_AFTER:
                    case KW_ALL:
                    case KW_ALTER:
                    case KW_ANALYZE:
                    case KW_ARCHIVE:
                    case KW_ARRAY:
                    case KW_AS:
                    case KW_ASC:
                    case KW_AUTHORIZATION:
                    case KW_BEFORE:
                    case KW_BETWEEN:
                    case KW_BIGINT:
                    case KW_BINARY:
                    case KW_BOOLEAN:
                    case KW_BOTH:
                    case KW_BUCKET:
                    case KW_BUCKETS:
                    case KW_BY:
                    case KW_CASCADE:
                    case KW_CASE:
                    case KW_CAST:
                    case KW_CHANGE:
                    case KW_CLUSTER:
                    case KW_CLUSTERED:
                    case KW_CLUSTERSTATUS:
                    case KW_COLLECTION:
                    case KW_COLUMNS:
                    case KW_COMMENT:
                    case KW_COMPACT:
                    case KW_COMPACTIONS:
                    case KW_COMPUTE:
                    case KW_CONCATENATE:
                    case KW_CONTINUE:
                    case KW_CREATE:
                    case KW_CUBE:
                    case KW_CURSOR:
                    case KW_DATA:
                    case KW_DATABASES:
                    case KW_DATE:
                    case KW_DATETIME:
                    case KW_DBPROPERTIES:
                    case KW_DECIMAL:
                    case KW_DEFAULT:
                    case KW_DEFERRED:
                    case KW_DEFINED:
                    case KW_DELETE:
                    case KW_DELIMITED:
                    case KW_DEPENDENCY:
                    case KW_DESC:
                    case KW_DESCRIBE:
                    case KW_DIRECTORIES:
                    case KW_DIRECTORY:
                    case KW_DISABLE:
                    case KW_DISTRIBUTE:
                    case KW_DOUBLE:
                    case KW_DROP:
                    case KW_ELEM_TYPE:
                    case KW_ENABLE:
                    case KW_ESCAPED:
                    case KW_EXCLUSIVE:
                    case KW_EXISTS:
                    case KW_EXPLAIN:
                    case KW_EXPORT:
                    case KW_EXTERNAL:
                    case KW_FALSE:
                    case KW_FETCH:
                    case KW_FIELDS:
                    case KW_FILE:
                    case KW_FILEFORMAT:
                    case KW_FIRST:
                    case KW_FLOAT:
                    case KW_FOR:
                    case KW_FORMAT:
                    case KW_FORMATTED:
                    case KW_FULL:
                    case KW_FUNCTIONS:
                    case KW_GRANT:
                    case KW_GROUP:
                    case KW_GROUPING:
                    case KW_HOLD_DDLTIME:
                    case KW_IDXPROPERTIES:
                    case KW_IF:
                    case KW_IGNORE:
                    case KW_IMPORT:
                    case KW_IN:
                    case KW_INDEX:
                    case KW_INDEXES:
                    case KW_INNER:
                    case KW_INPATH:
                    case KW_INPUTDRIVER:
                    case KW_INPUTFORMAT:
                    case KW_INSERT:
                    case KW_INT:
                    case KW_INTERSECT:
                    case KW_INTO:
                    case KW_IS:
                    case KW_ITEMS:
                    case KW_JAR:
                    case KW_KEYS:
                    case KW_KEY_TYPE:
                    case KW_LATERAL:
                    case KW_LEFT:
                    case KW_LIKE:
                    case KW_LIMIT:
                    case KW_LINES:
                    case KW_LOAD:
                    case KW_LOCAL:
                    case KW_LOCATION:
                    case KW_LOCK:
                    case KW_LOCKS:
                    case KW_LOGICAL:
                    case KW_LONG:
                    case KW_MAP:
                    case KW_MAPJOIN:
                    case KW_MATERIALIZED:
                    case KW_MINUS:
                    case KW_MSCK:
                    case KW_NONE:
                    case KW_NOSCAN:
                    case KW_NOT:
                    case KW_NO_DROP:
                    case KW_NULL:
                    case KW_OF:
                    case KW_OFFLINE:
                    case KW_OPTION:
                    case KW_ORDER:
                    case KW_OUT:
                    case KW_OUTER:
                    case KW_OUTPUTDRIVER:
                    case KW_OUTPUTFORMAT:
                    case KW_OVERWRITE:
                    case KW_OWNER:
                    case KW_PARTITION:
                    case KW_PARTITIONED:
                    case KW_PARTITIONS:
                    case KW_PERCENT:
                    case KW_PLUS:
                    case KW_PRETTY:
                    case KW_PRINCIPALS:
                    case KW_PROCEDURE:
                    case KW_PROTECTION:
                    case KW_PURGE:
                    case KW_RANGE:
                    case KW_READ:
                    case KW_READONLY:
                    case KW_READS:
                    case KW_REBUILD:
                    case KW_RECORDREADER:
                    case KW_RECORDWRITER:
                    case KW_REGEXP:
                    case KW_RENAME:
                    case KW_REPAIR:
                    case KW_REPLACE:
                    case KW_RESTRICT:
                    case KW_REVOKE:
                    case KW_REWRITE:
                    case KW_RIGHT:
                    case KW_RLIKE:
                    case KW_ROLE:
                    case KW_ROLES:
                    case KW_ROLLUP:
                    case KW_ROW:
                    case KW_ROWS:
                    case KW_SCHEMA:
                    case KW_SCHEMAS:
                    case KW_SEMI:
                    case KW_SERDE:
                    case KW_SERDEPROPERTIES:
                    case KW_SET:
                    case KW_SETS:
                    case KW_SHARED:
                    case KW_SHOW:
                    case KW_SHOW_DATABASE:
                    case KW_SKEWED:
                    case KW_SMALLINT:
                    case KW_SORT:
                    case KW_SORTED:
                    case KW_SSL:
                    case KW_STATISTICS:
                    case KW_STORED:
                    case KW_STREAMTABLE:
                    case KW_STRING:
                    case KW_STRUCT:
                    case KW_TABLE:
                    case KW_TABLES:
                    case KW_TBLPROPERTIES:
                    case KW_TEMPORARY:
                    case KW_TERMINATED:
                    case KW_TIMESTAMP:
                    case KW_TINYINT:
                    case KW_TO:
                    case KW_TOUCH:
                    case KW_TRANSACTIONS:
                    case KW_TRIGGER:
                    case KW_TRUE:
                    case KW_TRUNCATE:
                    case KW_UNARCHIVE:
                    case KW_UNDO:
                    case KW_UNION:
                    case KW_UNIONTYPE:
                    case KW_UNLOCK:
                    case KW_UNSET:
                    case KW_UNSIGNED:
                    case KW_UPDATE:
                    case KW_USE:
                    case KW_USER:
                    case KW_USING:
                    case KW_UTC:
                    case KW_UTCTIMESTAMP:
                    case KW_VALUES:
                    case KW_VALUE_TYPE:
                    case KW_VIEW:
                    case KW_WHILE:
                    case KW_WITH:
                    case MINUS:
                    case Number:
                    case PLUS:
                    case SmallintLiteral:
                    case StringLiteral:
                    case TILDE:
                    case TinyintLiteral:
                        {
                        alt10=2;
                        }
                        break;
                    default:
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 10, 2, input);

                        throw nvae;

                    }

                    }
                    break;
                default:
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 10, 1, input);

                    throw nvae;

                }

                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;

            }

            switch (alt10) {
                case 1 :
                    // IdentifiersParser.g:116:5: KW_CLUSTER KW_BY LPAREN expression ( COMMA expression )* RPAREN
                    {
                    KW_CLUSTER31=(Token)match(input,KW_CLUSTER,FOLLOW_KW_CLUSTER_in_clusterByClause506); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_KW_CLUSTER.add(KW_CLUSTER31);


                    KW_BY32=(Token)match(input,KW_BY,FOLLOW_KW_BY_in_clusterByClause508); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_KW_BY.add(KW_BY32);


                    LPAREN33=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_clusterByClause514); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN33);


                    pushFollow(FOLLOW_expression_in_clusterByClause516);
                    expression34=expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_expression.add(expression34.getTree());

                    // IdentifiersParser.g:117:23: ( COMMA expression )*
                    loop8:
                    do {
                        int alt8=2;
                        switch ( input.LA(1) ) {
                        case COMMA:
                            {
                            alt8=1;
                            }
                            break;

                        }

                        switch (alt8) {
                    	case 1 :
                    	    // IdentifiersParser.g:117:24: COMMA expression
                    	    {
                    	    COMMA35=(Token)match(input,COMMA,FOLLOW_COMMA_in_clusterByClause519); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA35);


                    	    pushFollow(FOLLOW_expression_in_clusterByClause521);
                    	    expression36=expression();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_expression.add(expression36.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop8;
                        }
                    } while (true);


                    RPAREN37=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_clusterByClause525); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN37);


                    // AST REWRITE
                    // elements: expression
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 117:50: -> ^( TOK_CLUSTERBY ( expression )+ )
                    {
                        // IdentifiersParser.g:117:53: ^( TOK_CLUSTERBY ( expression )+ )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(TOK_CLUSTERBY, "TOK_CLUSTERBY")
                        , root_1);

                        if ( !(stream_expression.hasNext()) ) {
                            throw new RewriteEarlyExitException();
                        }
                        while ( stream_expression.hasNext() ) {
                            adaptor.addChild(root_1, stream_expression.nextTree());

                        }
                        stream_expression.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 2 :
                    // IdentifiersParser.g:119:5: KW_CLUSTER KW_BY expression ( ( COMMA )=> COMMA expression )*
                    {
                    KW_CLUSTER38=(Token)match(input,KW_CLUSTER,FOLLOW_KW_CLUSTER_in_clusterByClause546); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_KW_CLUSTER.add(KW_CLUSTER38);


                    KW_BY39=(Token)match(input,KW_BY,FOLLOW_KW_BY_in_clusterByClause548); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_KW_BY.add(KW_BY39);


                    pushFollow(FOLLOW_expression_in_clusterByClause554);
                    expression40=expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_expression.add(expression40.getTree());

                    // IdentifiersParser.g:121:5: ( ( COMMA )=> COMMA expression )*
                    loop9:
                    do {
                        int alt9=2;
                        int LA9_0 = input.LA(1);

                        if ( (LA9_0==COMMA) && (synpred1_IdentifiersParser())) {
                            alt9=1;
                        }


                        switch (alt9) {
                    	case 1 :
                    	    // IdentifiersParser.g:121:7: ( COMMA )=> COMMA expression
                    	    {
                    	    COMMA41=(Token)match(input,COMMA,FOLLOW_COMMA_in_clusterByClause566); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA41);


                    	    pushFollow(FOLLOW_expression_in_clusterByClause568);
                    	    expression42=expression();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_expression.add(expression42.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop9;
                        }
                    } while (true);


                    // AST REWRITE
                    // elements: expression
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 121:36: -> ^( TOK_CLUSTERBY ( expression )+ )
                    {
                        // IdentifiersParser.g:121:39: ^( TOK_CLUSTERBY ( expression )+ )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(TOK_CLUSTERBY, "TOK_CLUSTERBY")
                        , root_1);

                        if ( !(stream_expression.hasNext()) ) {
                            throw new RewriteEarlyExitException();
                        }
                        while ( stream_expression.hasNext() ) {
                            adaptor.addChild(root_1, stream_expression.nextTree());

                        }
                        stream_expression.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
            if ( state.backtracking==0 ) { gParent.popMsg(state); }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "clusterByClause"


    public static class partitionByClause_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "partitionByClause"
    // IdentifiersParser.g:124:1: partitionByClause : ( KW_PARTITION KW_BY LPAREN expression ( COMMA expression )* RPAREN -> ^( TOK_DISTRIBUTEBY ( expression )+ ) | KW_PARTITION KW_BY expression ( ( COMMA )=> COMMA expression )* -> ^( TOK_DISTRIBUTEBY ( expression )+ ) );
    public final HiveParser_IdentifiersParser.partitionByClause_return partitionByClause() throws RecognitionException {
        HiveParser_IdentifiersParser.partitionByClause_return retval = new HiveParser_IdentifiersParser.partitionByClause_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token KW_PARTITION43=null;
        Token KW_BY44=null;
        Token LPAREN45=null;
        Token COMMA47=null;
        Token RPAREN49=null;
        Token KW_PARTITION50=null;
        Token KW_BY51=null;
        Token COMMA53=null;
        HiveParser_IdentifiersParser.expression_return expression46 =null;

        HiveParser_IdentifiersParser.expression_return expression48 =null;

        HiveParser_IdentifiersParser.expression_return expression52 =null;

        HiveParser_IdentifiersParser.expression_return expression54 =null;


        CommonTree KW_PARTITION43_tree=null;
        CommonTree KW_BY44_tree=null;
        CommonTree LPAREN45_tree=null;
        CommonTree COMMA47_tree=null;
        CommonTree RPAREN49_tree=null;
        CommonTree KW_PARTITION50_tree=null;
        CommonTree KW_BY51_tree=null;
        CommonTree COMMA53_tree=null;
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_KW_PARTITION=new RewriteRuleTokenStream(adaptor,"token KW_PARTITION");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleTokenStream stream_KW_BY=new RewriteRuleTokenStream(adaptor,"token KW_BY");
        RewriteRuleSubtreeStream stream_expression=new RewriteRuleSubtreeStream(adaptor,"rule expression");
         gParent.pushMsg("partition by clause", state); 
        try {
            // IdentifiersParser.g:127:5: ( KW_PARTITION KW_BY LPAREN expression ( COMMA expression )* RPAREN -> ^( TOK_DISTRIBUTEBY ( expression )+ ) | KW_PARTITION KW_BY expression ( ( COMMA )=> COMMA expression )* -> ^( TOK_DISTRIBUTEBY ( expression )+ ) )
            int alt13=2;
            switch ( input.LA(1) ) {
            case KW_PARTITION:
                {
                switch ( input.LA(2) ) {
                case KW_BY:
                    {
                    switch ( input.LA(3) ) {
                    case LPAREN:
                        {
                        alt13=1;
                        }
                        break;
                    case BigintLiteral:
                    case CharSetName:
                    case DecimalLiteral:
                    case Identifier:
                    case KW_ADD:
                    case KW_ADMIN:
                    case KW_AFTER:
                    case KW_ALL:
                    case KW_ALTER:
                    case KW_ANALYZE:
                    case KW_ARCHIVE:
                    case KW_ARRAY:
                    case KW_AS:
                    case KW_ASC:
                    case KW_AUTHORIZATION:
                    case KW_BEFORE:
                    case KW_BETWEEN:
                    case KW_BIGINT:
                    case KW_BINARY:
                    case KW_BOOLEAN:
                    case KW_BOTH:
                    case KW_BUCKET:
                    case KW_BUCKETS:
                    case KW_BY:
                    case KW_CASCADE:
                    case KW_CASE:
                    case KW_CAST:
                    case KW_CHANGE:
                    case KW_CLUSTER:
                    case KW_CLUSTERED:
                    case KW_CLUSTERSTATUS:
                    case KW_COLLECTION:
                    case KW_COLUMNS:
                    case KW_COMMENT:
                    case KW_COMPACT:
                    case KW_COMPACTIONS:
                    case KW_COMPUTE:
                    case KW_CONCATENATE:
                    case KW_CONTINUE:
                    case KW_CREATE:
                    case KW_CUBE:
                    case KW_CURSOR:
                    case KW_DATA:
                    case KW_DATABASES:
                    case KW_DATE:
                    case KW_DATETIME:
                    case KW_DBPROPERTIES:
                    case KW_DECIMAL:
                    case KW_DEFAULT:
                    case KW_DEFERRED:
                    case KW_DEFINED:
                    case KW_DELETE:
                    case KW_DELIMITED:
                    case KW_DEPENDENCY:
                    case KW_DESC:
                    case KW_DESCRIBE:
                    case KW_DIRECTORIES:
                    case KW_DIRECTORY:
                    case KW_DISABLE:
                    case KW_DISTRIBUTE:
                    case KW_DOUBLE:
                    case KW_DROP:
                    case KW_ELEM_TYPE:
                    case KW_ENABLE:
                    case KW_ESCAPED:
                    case KW_EXCLUSIVE:
                    case KW_EXISTS:
                    case KW_EXPLAIN:
                    case KW_EXPORT:
                    case KW_EXTERNAL:
                    case KW_FALSE:
                    case KW_FETCH:
                    case KW_FIELDS:
                    case KW_FILE:
                    case KW_FILEFORMAT:
                    case KW_FIRST:
                    case KW_FLOAT:
                    case KW_FOR:
                    case KW_FORMAT:
                    case KW_FORMATTED:
                    case KW_FULL:
                    case KW_FUNCTIONS:
                    case KW_GRANT:
                    case KW_GROUP:
                    case KW_GROUPING:
                    case KW_HOLD_DDLTIME:
                    case KW_IDXPROPERTIES:
                    case KW_IF:
                    case KW_IGNORE:
                    case KW_IMPORT:
                    case KW_IN:
                    case KW_INDEX:
                    case KW_INDEXES:
                    case KW_INNER:
                    case KW_INPATH:
                    case KW_INPUTDRIVER:
                    case KW_INPUTFORMAT:
                    case KW_INSERT:
                    case KW_INT:
                    case KW_INTERSECT:
                    case KW_INTO:
                    case KW_IS:
                    case KW_ITEMS:
                    case KW_JAR:
                    case KW_KEYS:
                    case KW_KEY_TYPE:
                    case KW_LATERAL:
                    case KW_LEFT:
                    case KW_LIKE:
                    case KW_LIMIT:
                    case KW_LINES:
                    case KW_LOAD:
                    case KW_LOCAL:
                    case KW_LOCATION:
                    case KW_LOCK:
                    case KW_LOCKS:
                    case KW_LOGICAL:
                    case KW_LONG:
                    case KW_MAP:
                    case KW_MAPJOIN:
                    case KW_MATERIALIZED:
                    case KW_MINUS:
                    case KW_MSCK:
                    case KW_NONE:
                    case KW_NOSCAN:
                    case KW_NOT:
                    case KW_NO_DROP:
                    case KW_NULL:
                    case KW_OF:
                    case KW_OFFLINE:
                    case KW_OPTION:
                    case KW_ORDER:
                    case KW_OUT:
                    case KW_OUTER:
                    case KW_OUTPUTDRIVER:
                    case KW_OUTPUTFORMAT:
                    case KW_OVERWRITE:
                    case KW_OWNER:
                    case KW_PARTITION:
                    case KW_PARTITIONED:
                    case KW_PARTITIONS:
                    case KW_PERCENT:
                    case KW_PLUS:
                    case KW_PRETTY:
                    case KW_PRINCIPALS:
                    case KW_PROCEDURE:
                    case KW_PROTECTION:
                    case KW_PURGE:
                    case KW_RANGE:
                    case KW_READ:
                    case KW_READONLY:
                    case KW_READS:
                    case KW_REBUILD:
                    case KW_RECORDREADER:
                    case KW_RECORDWRITER:
                    case KW_REGEXP:
                    case KW_RENAME:
                    case KW_REPAIR:
                    case KW_REPLACE:
                    case KW_RESTRICT:
                    case KW_REVOKE:
                    case KW_REWRITE:
                    case KW_RIGHT:
                    case KW_RLIKE:
                    case KW_ROLE:
                    case KW_ROLES:
                    case KW_ROLLUP:
                    case KW_ROW:
                    case KW_ROWS:
                    case KW_SCHEMA:
                    case KW_SCHEMAS:
                    case KW_SEMI:
                    case KW_SERDE:
                    case KW_SERDEPROPERTIES:
                    case KW_SET:
                    case KW_SETS:
                    case KW_SHARED:
                    case KW_SHOW:
                    case KW_SHOW_DATABASE:
                    case KW_SKEWED:
                    case KW_SMALLINT:
                    case KW_SORT:
                    case KW_SORTED:
                    case KW_SSL:
                    case KW_STATISTICS:
                    case KW_STORED:
                    case KW_STREAMTABLE:
                    case KW_STRING:
                    case KW_STRUCT:
                    case KW_TABLE:
                    case KW_TABLES:
                    case KW_TBLPROPERTIES:
                    case KW_TEMPORARY:
                    case KW_TERMINATED:
                    case KW_TIMESTAMP:
                    case KW_TINYINT:
                    case KW_TO:
                    case KW_TOUCH:
                    case KW_TRANSACTIONS:
                    case KW_TRIGGER:
                    case KW_TRUE:
                    case KW_TRUNCATE:
                    case KW_UNARCHIVE:
                    case KW_UNDO:
                    case KW_UNION:
                    case KW_UNIONTYPE:
                    case KW_UNLOCK:
                    case KW_UNSET:
                    case KW_UNSIGNED:
                    case KW_UPDATE:
                    case KW_USE:
                    case KW_USER:
                    case KW_USING:
                    case KW_UTC:
                    case KW_UTCTIMESTAMP:
                    case KW_VALUES:
                    case KW_VALUE_TYPE:
                    case KW_VIEW:
                    case KW_WHILE:
                    case KW_WITH:
                    case MINUS:
                    case Number:
                    case PLUS:
                    case SmallintLiteral:
                    case StringLiteral:
                    case TILDE:
                    case TinyintLiteral:
                        {
                        alt13=2;
                        }
                        break;
                    default:
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 13, 2, input);

                        throw nvae;

                    }

                    }
                    break;
                default:
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 13, 1, input);

                    throw nvae;

                }

                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 13, 0, input);

                throw nvae;

            }

            switch (alt13) {
                case 1 :
                    // IdentifiersParser.g:128:5: KW_PARTITION KW_BY LPAREN expression ( COMMA expression )* RPAREN
                    {
                    KW_PARTITION43=(Token)match(input,KW_PARTITION,FOLLOW_KW_PARTITION_in_partitionByClause612); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_KW_PARTITION.add(KW_PARTITION43);


                    KW_BY44=(Token)match(input,KW_BY,FOLLOW_KW_BY_in_partitionByClause614); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_KW_BY.add(KW_BY44);


                    LPAREN45=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_partitionByClause620); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN45);


                    pushFollow(FOLLOW_expression_in_partitionByClause622);
                    expression46=expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_expression.add(expression46.getTree());

                    // IdentifiersParser.g:129:23: ( COMMA expression )*
                    loop11:
                    do {
                        int alt11=2;
                        switch ( input.LA(1) ) {
                        case COMMA:
                            {
                            alt11=1;
                            }
                            break;

                        }

                        switch (alt11) {
                    	case 1 :
                    	    // IdentifiersParser.g:129:24: COMMA expression
                    	    {
                    	    COMMA47=(Token)match(input,COMMA,FOLLOW_COMMA_in_partitionByClause625); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA47);


                    	    pushFollow(FOLLOW_expression_in_partitionByClause627);
                    	    expression48=expression();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_expression.add(expression48.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop11;
                        }
                    } while (true);


                    RPAREN49=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_partitionByClause631); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN49);


                    // AST REWRITE
                    // elements: expression
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 129:50: -> ^( TOK_DISTRIBUTEBY ( expression )+ )
                    {
                        // IdentifiersParser.g:129:53: ^( TOK_DISTRIBUTEBY ( expression )+ )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(TOK_DISTRIBUTEBY, "TOK_DISTRIBUTEBY")
                        , root_1);

                        if ( !(stream_expression.hasNext()) ) {
                            throw new RewriteEarlyExitException();
                        }
                        while ( stream_expression.hasNext() ) {
                            adaptor.addChild(root_1, stream_expression.nextTree());

                        }
                        stream_expression.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 2 :
                    // IdentifiersParser.g:131:5: KW_PARTITION KW_BY expression ( ( COMMA )=> COMMA expression )*
                    {
                    KW_PARTITION50=(Token)match(input,KW_PARTITION,FOLLOW_KW_PARTITION_in_partitionByClause652); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_KW_PARTITION.add(KW_PARTITION50);


                    KW_BY51=(Token)match(input,KW_BY,FOLLOW_KW_BY_in_partitionByClause654); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_KW_BY.add(KW_BY51);


                    pushFollow(FOLLOW_expression_in_partitionByClause660);
                    expression52=expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_expression.add(expression52.getTree());

                    // IdentifiersParser.g:132:16: ( ( COMMA )=> COMMA expression )*
                    loop12:
                    do {
                        int alt12=2;
                        int LA12_0 = input.LA(1);

                        if ( (LA12_0==COMMA) && (synpred2_IdentifiersParser())) {
                            alt12=1;
                        }


                        switch (alt12) {
                    	case 1 :
                    	    // IdentifiersParser.g:132:17: ( COMMA )=> COMMA expression
                    	    {
                    	    COMMA53=(Token)match(input,COMMA,FOLLOW_COMMA_in_partitionByClause668); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA53);


                    	    pushFollow(FOLLOW_expression_in_partitionByClause670);
                    	    expression54=expression();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_expression.add(expression54.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop12;
                        }
                    } while (true);


                    // AST REWRITE
                    // elements: expression
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 132:46: -> ^( TOK_DISTRIBUTEBY ( expression )+ )
                    {
                        // IdentifiersParser.g:132:49: ^( TOK_DISTRIBUTEBY ( expression )+ )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(TOK_DISTRIBUTEBY, "TOK_DISTRIBUTEBY")
                        , root_1);

                        if ( !(stream_expression.hasNext()) ) {
                            throw new RewriteEarlyExitException();
                        }
                        while ( stream_expression.hasNext() ) {
                            adaptor.addChild(root_1, stream_expression.nextTree());

                        }
                        stream_expression.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
            if ( state.backtracking==0 ) { gParent.popMsg(state); }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "partitionByClause"


    public static class distributeByClause_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "distributeByClause"
    // IdentifiersParser.g:135:1: distributeByClause : ( KW_DISTRIBUTE KW_BY LPAREN expression ( COMMA expression )* RPAREN -> ^( TOK_DISTRIBUTEBY ( expression )+ ) | KW_DISTRIBUTE KW_BY expression ( ( COMMA )=> COMMA expression )* -> ^( TOK_DISTRIBUTEBY ( expression )+ ) );
    public final HiveParser_IdentifiersParser.distributeByClause_return distributeByClause() throws RecognitionException {
        HiveParser_IdentifiersParser.distributeByClause_return retval = new HiveParser_IdentifiersParser.distributeByClause_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token KW_DISTRIBUTE55=null;
        Token KW_BY56=null;
        Token LPAREN57=null;
        Token COMMA59=null;
        Token RPAREN61=null;
        Token KW_DISTRIBUTE62=null;
        Token KW_BY63=null;
        Token COMMA65=null;
        HiveParser_IdentifiersParser.expression_return expression58 =null;

        HiveParser_IdentifiersParser.expression_return expression60 =null;

        HiveParser_IdentifiersParser.expression_return expression64 =null;

        HiveParser_IdentifiersParser.expression_return expression66 =null;


        CommonTree KW_DISTRIBUTE55_tree=null;
        CommonTree KW_BY56_tree=null;
        CommonTree LPAREN57_tree=null;
        CommonTree COMMA59_tree=null;
        CommonTree RPAREN61_tree=null;
        CommonTree KW_DISTRIBUTE62_tree=null;
        CommonTree KW_BY63_tree=null;
        CommonTree COMMA65_tree=null;
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_KW_DISTRIBUTE=new RewriteRuleTokenStream(adaptor,"token KW_DISTRIBUTE");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleTokenStream stream_KW_BY=new RewriteRuleTokenStream(adaptor,"token KW_BY");
        RewriteRuleSubtreeStream stream_expression=new RewriteRuleSubtreeStream(adaptor,"rule expression");
         gParent.pushMsg("distribute by clause", state); 
        try {
            // IdentifiersParser.g:138:5: ( KW_DISTRIBUTE KW_BY LPAREN expression ( COMMA expression )* RPAREN -> ^( TOK_DISTRIBUTEBY ( expression )+ ) | KW_DISTRIBUTE KW_BY expression ( ( COMMA )=> COMMA expression )* -> ^( TOK_DISTRIBUTEBY ( expression )+ ) )
            int alt16=2;
            switch ( input.LA(1) ) {
            case KW_DISTRIBUTE:
                {
                switch ( input.LA(2) ) {
                case KW_BY:
                    {
                    switch ( input.LA(3) ) {
                    case LPAREN:
                        {
                        alt16=1;
                        }
                        break;
                    case BigintLiteral:
                    case CharSetName:
                    case DecimalLiteral:
                    case Identifier:
                    case KW_ADD:
                    case KW_ADMIN:
                    case KW_AFTER:
                    case KW_ALL:
                    case KW_ALTER:
                    case KW_ANALYZE:
                    case KW_ARCHIVE:
                    case KW_ARRAY:
                    case KW_AS:
                    case KW_ASC:
                    case KW_AUTHORIZATION:
                    case KW_BEFORE:
                    case KW_BETWEEN:
                    case KW_BIGINT:
                    case KW_BINARY:
                    case KW_BOOLEAN:
                    case KW_BOTH:
                    case KW_BUCKET:
                    case KW_BUCKETS:
                    case KW_BY:
                    case KW_CASCADE:
                    case KW_CASE:
                    case KW_CAST:
                    case KW_CHANGE:
                    case KW_CLUSTER:
                    case KW_CLUSTERED:
                    case KW_CLUSTERSTATUS:
                    case KW_COLLECTION:
                    case KW_COLUMNS:
                    case KW_COMMENT:
                    case KW_COMPACT:
                    case KW_COMPACTIONS:
                    case KW_COMPUTE:
                    case KW_CONCATENATE:
                    case KW_CONTINUE:
                    case KW_CREATE:
                    case KW_CUBE:
                    case KW_CURSOR:
                    case KW_DATA:
                    case KW_DATABASES:
                    case KW_DATE:
                    case KW_DATETIME:
                    case KW_DBPROPERTIES:
                    case KW_DECIMAL:
                    case KW_DEFAULT:
                    case KW_DEFERRED:
                    case KW_DEFINED:
                    case KW_DELETE:
                    case KW_DELIMITED:
                    case KW_DEPENDENCY:
                    case KW_DESC:
                    case KW_DESCRIBE:
                    case KW_DIRECTORIES:
                    case KW_DIRECTORY:
                    case KW_DISABLE:
                    case KW_DISTRIBUTE:
                    case KW_DOUBLE:
                    case KW_DROP:
                    case KW_ELEM_TYPE:
                    case KW_ENABLE:
                    case KW_ESCAPED:
                    case KW_EXCLUSIVE:
                    case KW_EXISTS:
                    case KW_EXPLAIN:
                    case KW_EXPORT:
                    case KW_EXTERNAL:
                    case KW_FALSE:
                    case KW_FETCH:
                    case KW_FIELDS:
                    case KW_FILE:
                    case KW_FILEFORMAT:
                    case KW_FIRST:
                    case KW_FLOAT:
                    case KW_FOR:
                    case KW_FORMAT:
                    case KW_FORMATTED:
                    case KW_FULL:
                    case KW_FUNCTIONS:
                    case KW_GRANT:
                    case KW_GROUP:
                    case KW_GROUPING:
                    case KW_HOLD_DDLTIME:
                    case KW_IDXPROPERTIES:
                    case KW_IF:
                    case KW_IGNORE:
                    case KW_IMPORT:
                    case KW_IN:
                    case KW_INDEX:
                    case KW_INDEXES:
                    case KW_INNER:
                    case KW_INPATH:
                    case KW_INPUTDRIVER:
                    case KW_INPUTFORMAT:
                    case KW_INSERT:
                    case KW_INT:
                    case KW_INTERSECT:
                    case KW_INTO:
                    case KW_IS:
                    case KW_ITEMS:
                    case KW_JAR:
                    case KW_KEYS:
                    case KW_KEY_TYPE:
                    case KW_LATERAL:
                    case KW_LEFT:
                    case KW_LIKE:
                    case KW_LIMIT:
                    case KW_LINES:
                    case KW_LOAD:
                    case KW_LOCAL:
                    case KW_LOCATION:
                    case KW_LOCK:
                    case KW_LOCKS:
                    case KW_LOGICAL:
                    case KW_LONG:
                    case KW_MAP:
                    case KW_MAPJOIN:
                    case KW_MATERIALIZED:
                    case KW_MINUS:
                    case KW_MSCK:
                    case KW_NONE:
                    case KW_NOSCAN:
                    case KW_NOT:
                    case KW_NO_DROP:
                    case KW_NULL:
                    case KW_OF:
                    case KW_OFFLINE:
                    case KW_OPTION:
                    case KW_ORDER:
                    case KW_OUT:
                    case KW_OUTER:
                    case KW_OUTPUTDRIVER:
                    case KW_OUTPUTFORMAT:
                    case KW_OVERWRITE:
                    case KW_OWNER:
                    case KW_PARTITION:
                    case KW_PARTITIONED:
                    case KW_PARTITIONS:
                    case KW_PERCENT:
                    case KW_PLUS:
                    case KW_PRETTY:
                    case KW_PRINCIPALS:
                    case KW_PROCEDURE:
                    case KW_PROTECTION:
                    case KW_PURGE:
                    case KW_RANGE:
                    case KW_READ:
                    case KW_READONLY:
                    case KW_READS:
                    case KW_REBUILD:
                    case KW_RECORDREADER:
                    case KW_RECORDWRITER:
                    case KW_REGEXP:
                    case KW_RENAME:
                    case KW_REPAIR:
                    case KW_REPLACE:
                    case KW_RESTRICT:
                    case KW_REVOKE:
                    case KW_REWRITE:
                    case KW_RIGHT:
                    case KW_RLIKE:
                    case KW_ROLE:
                    case KW_ROLES:
                    case KW_ROLLUP:
                    case KW_ROW:
                    case KW_ROWS:
                    case KW_SCHEMA:
                    case KW_SCHEMAS:
                    case KW_SEMI:
                    case KW_SERDE:
                    case KW_SERDEPROPERTIES:
                    case KW_SET:
                    case KW_SETS:
                    case KW_SHARED:
                    case KW_SHOW:
                    case KW_SHOW_DATABASE:
                    case KW_SKEWED:
                    case KW_SMALLINT:
                    case KW_SORT:
                    case KW_SORTED:
                    case KW_SSL:
                    case KW_STATISTICS:
                    case KW_STORED:
                    case KW_STREAMTABLE:
                    case KW_STRING:
                    case KW_STRUCT:
                    case KW_TABLE:
                    case KW_TABLES:
                    case KW_TBLPROPERTIES:
                    case KW_TEMPORARY:
                    case KW_TERMINATED:
                    case KW_TIMESTAMP:
                    case KW_TINYINT:
                    case KW_TO:
                    case KW_TOUCH:
                    case KW_TRANSACTIONS:
                    case KW_TRIGGER:
                    case KW_TRUE:
                    case KW_TRUNCATE:
                    case KW_UNARCHIVE:
                    case KW_UNDO:
                    case KW_UNION:
                    case KW_UNIONTYPE:
                    case KW_UNLOCK:
                    case KW_UNSET:
                    case KW_UNSIGNED:
                    case KW_UPDATE:
                    case KW_USE:
                    case KW_USER:
                    case KW_USING:
                    case KW_UTC:
                    case KW_UTCTIMESTAMP:
                    case KW_VALUES:
                    case KW_VALUE_TYPE:
                    case KW_VIEW:
                    case KW_WHILE:
                    case KW_WITH:
                    case MINUS:
                    case Number:
                    case PLUS:
                    case SmallintLiteral:
                    case StringLiteral:
                    case TILDE:
                    case TinyintLiteral:
                        {
                        alt16=2;
                        }
                        break;
                    default:
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 16, 2, input);

                        throw nvae;

                    }

                    }
                    break;
                default:
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 16, 1, input);

                    throw nvae;

                }

                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 16, 0, input);

                throw nvae;

            }

            switch (alt16) {
                case 1 :
                    // IdentifiersParser.g:139:5: KW_DISTRIBUTE KW_BY LPAREN expression ( COMMA expression )* RPAREN
                    {
                    KW_DISTRIBUTE55=(Token)match(input,KW_DISTRIBUTE,FOLLOW_KW_DISTRIBUTE_in_distributeByClause712); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_KW_DISTRIBUTE.add(KW_DISTRIBUTE55);


                    KW_BY56=(Token)match(input,KW_BY,FOLLOW_KW_BY_in_distributeByClause714); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_KW_BY.add(KW_BY56);


                    LPAREN57=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_distributeByClause720); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN57);


                    pushFollow(FOLLOW_expression_in_distributeByClause722);
                    expression58=expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_expression.add(expression58.getTree());

                    // IdentifiersParser.g:140:23: ( COMMA expression )*
                    loop14:
                    do {
                        int alt14=2;
                        switch ( input.LA(1) ) {
                        case COMMA:
                            {
                            alt14=1;
                            }
                            break;

                        }

                        switch (alt14) {
                    	case 1 :
                    	    // IdentifiersParser.g:140:24: COMMA expression
                    	    {
                    	    COMMA59=(Token)match(input,COMMA,FOLLOW_COMMA_in_distributeByClause725); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA59);


                    	    pushFollow(FOLLOW_expression_in_distributeByClause727);
                    	    expression60=expression();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_expression.add(expression60.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop14;
                        }
                    } while (true);


                    RPAREN61=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_distributeByClause731); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN61);


                    // AST REWRITE
                    // elements: expression
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 140:50: -> ^( TOK_DISTRIBUTEBY ( expression )+ )
                    {
                        // IdentifiersParser.g:140:53: ^( TOK_DISTRIBUTEBY ( expression )+ )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(TOK_DISTRIBUTEBY, "TOK_DISTRIBUTEBY")
                        , root_1);

                        if ( !(stream_expression.hasNext()) ) {
                            throw new RewriteEarlyExitException();
                        }
                        while ( stream_expression.hasNext() ) {
                            adaptor.addChild(root_1, stream_expression.nextTree());

                        }
                        stream_expression.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 2 :
                    // IdentifiersParser.g:142:5: KW_DISTRIBUTE KW_BY expression ( ( COMMA )=> COMMA expression )*
                    {
                    KW_DISTRIBUTE62=(Token)match(input,KW_DISTRIBUTE,FOLLOW_KW_DISTRIBUTE_in_distributeByClause752); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_KW_DISTRIBUTE.add(KW_DISTRIBUTE62);


                    KW_BY63=(Token)match(input,KW_BY,FOLLOW_KW_BY_in_distributeByClause754); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_KW_BY.add(KW_BY63);


                    pushFollow(FOLLOW_expression_in_distributeByClause760);
                    expression64=expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_expression.add(expression64.getTree());

                    // IdentifiersParser.g:143:16: ( ( COMMA )=> COMMA expression )*
                    loop15:
                    do {
                        int alt15=2;
                        int LA15_0 = input.LA(1);

                        if ( (LA15_0==COMMA) && (synpred3_IdentifiersParser())) {
                            alt15=1;
                        }


                        switch (alt15) {
                    	case 1 :
                    	    // IdentifiersParser.g:143:17: ( COMMA )=> COMMA expression
                    	    {
                    	    COMMA65=(Token)match(input,COMMA,FOLLOW_COMMA_in_distributeByClause768); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA65);


                    	    pushFollow(FOLLOW_expression_in_distributeByClause770);
                    	    expression66=expression();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_expression.add(expression66.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop15;
                        }
                    } while (true);


                    // AST REWRITE
                    // elements: expression
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 143:46: -> ^( TOK_DISTRIBUTEBY ( expression )+ )
                    {
                        // IdentifiersParser.g:143:49: ^( TOK_DISTRIBUTEBY ( expression )+ )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(TOK_DISTRIBUTEBY, "TOK_DISTRIBUTEBY")
                        , root_1);

                        if ( !(stream_expression.hasNext()) ) {
                            throw new RewriteEarlyExitException();
                        }
                        while ( stream_expression.hasNext() ) {
                            adaptor.addChild(root_1, stream_expression.nextTree());

                        }
                        stream_expression.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
            if ( state.backtracking==0 ) { gParent.popMsg(state); }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "distributeByClause"


    public static class sortByClause_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "sortByClause"
    // IdentifiersParser.g:146:1: sortByClause : ( KW_SORT KW_BY LPAREN columnRefOrder ( COMMA columnRefOrder )* RPAREN -> ^( TOK_SORTBY ( columnRefOrder )+ ) | KW_SORT KW_BY columnRefOrder ( ( COMMA )=> COMMA columnRefOrder )* -> ^( TOK_SORTBY ( columnRefOrder )+ ) );
    public final HiveParser_IdentifiersParser.sortByClause_return sortByClause() throws RecognitionException {
        HiveParser_IdentifiersParser.sortByClause_return retval = new HiveParser_IdentifiersParser.sortByClause_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token KW_SORT67=null;
        Token KW_BY68=null;
        Token LPAREN69=null;
        Token COMMA71=null;
        Token RPAREN73=null;
        Token KW_SORT74=null;
        Token KW_BY75=null;
        Token COMMA77=null;
        HiveParser.columnRefOrder_return columnRefOrder70 =null;

        HiveParser.columnRefOrder_return columnRefOrder72 =null;

        HiveParser.columnRefOrder_return columnRefOrder76 =null;

        HiveParser.columnRefOrder_return columnRefOrder78 =null;


        CommonTree KW_SORT67_tree=null;
        CommonTree KW_BY68_tree=null;
        CommonTree LPAREN69_tree=null;
        CommonTree COMMA71_tree=null;
        CommonTree RPAREN73_tree=null;
        CommonTree KW_SORT74_tree=null;
        CommonTree KW_BY75_tree=null;
        CommonTree COMMA77_tree=null;
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleTokenStream stream_KW_SORT=new RewriteRuleTokenStream(adaptor,"token KW_SORT");
        RewriteRuleTokenStream stream_KW_BY=new RewriteRuleTokenStream(adaptor,"token KW_BY");
        RewriteRuleSubtreeStream stream_columnRefOrder=new RewriteRuleSubtreeStream(adaptor,"rule columnRefOrder");
         gParent.pushMsg("sort by clause", state); 
        try {
            // IdentifiersParser.g:149:5: ( KW_SORT KW_BY LPAREN columnRefOrder ( COMMA columnRefOrder )* RPAREN -> ^( TOK_SORTBY ( columnRefOrder )+ ) | KW_SORT KW_BY columnRefOrder ( ( COMMA )=> COMMA columnRefOrder )* -> ^( TOK_SORTBY ( columnRefOrder )+ ) )
            int alt19=2;
            switch ( input.LA(1) ) {
            case KW_SORT:
                {
                switch ( input.LA(2) ) {
                case KW_BY:
                    {
                    switch ( input.LA(3) ) {
                    case LPAREN:
                        {
                        alt19=1;
                        }
                        break;
                    case BigintLiteral:
                    case CharSetName:
                    case DecimalLiteral:
                    case Identifier:
                    case KW_ADD:
                    case KW_ADMIN:
                    case KW_AFTER:
                    case KW_ALL:
                    case KW_ALTER:
                    case KW_ANALYZE:
                    case KW_ARCHIVE:
                    case KW_ARRAY:
                    case KW_AS:
                    case KW_ASC:
                    case KW_AUTHORIZATION:
                    case KW_BEFORE:
                    case KW_BETWEEN:
                    case KW_BIGINT:
                    case KW_BINARY:
                    case KW_BOOLEAN:
                    case KW_BOTH:
                    case KW_BUCKET:
                    case KW_BUCKETS:
                    case KW_BY:
                    case KW_CASCADE:
                    case KW_CASE:
                    case KW_CAST:
                    case KW_CHANGE:
                    case KW_CLUSTER:
                    case KW_CLUSTERED:
                    case KW_CLUSTERSTATUS:
                    case KW_COLLECTION:
                    case KW_COLUMNS:
                    case KW_COMMENT:
                    case KW_COMPACT:
                    case KW_COMPACTIONS:
                    case KW_COMPUTE:
                    case KW_CONCATENATE:
                    case KW_CONTINUE:
                    case KW_CREATE:
                    case KW_CUBE:
                    case KW_CURSOR:
                    case KW_DATA:
                    case KW_DATABASES:
                    case KW_DATE:
                    case KW_DATETIME:
                    case KW_DBPROPERTIES:
                    case KW_DECIMAL:
                    case KW_DEFAULT:
                    case KW_DEFERRED:
                    case KW_DEFINED:
                    case KW_DELETE:
                    case KW_DELIMITED:
                    case KW_DEPENDENCY:
                    case KW_DESC:
                    case KW_DESCRIBE:
                    case KW_DIRECTORIES:
                    case KW_DIRECTORY:
                    case KW_DISABLE:
                    case KW_DISTRIBUTE:
                    case KW_DOUBLE:
                    case KW_DROP:
                    case KW_ELEM_TYPE:
                    case KW_ENABLE:
                    case KW_ESCAPED:
                    case KW_EXCLUSIVE:
                    case KW_EXISTS:
                    case KW_EXPLAIN:
                    case KW_EXPORT:
                    case KW_EXTERNAL:
                    case KW_FALSE:
                    case KW_FETCH:
                    case KW_FIELDS:
                    case KW_FILE:
                    case KW_FILEFORMAT:
                    case KW_FIRST:
                    case KW_FLOAT:
                    case KW_FOR:
                    case KW_FORMAT:
                    case KW_FORMATTED:
                    case KW_FULL:
                    case KW_FUNCTIONS:
                    case KW_GRANT:
                    case KW_GROUP:
                    case KW_GROUPING:
                    case KW_HOLD_DDLTIME:
                    case KW_IDXPROPERTIES:
                    case KW_IF:
                    case KW_IGNORE:
                    case KW_IMPORT:
                    case KW_IN:
                    case KW_INDEX:
                    case KW_INDEXES:
                    case KW_INNER:
                    case KW_INPATH:
                    case KW_INPUTDRIVER:
                    case KW_INPUTFORMAT:
                    case KW_INSERT:
                    case KW_INT:
                    case KW_INTERSECT:
                    case KW_INTO:
                    case KW_IS:
                    case KW_ITEMS:
                    case KW_JAR:
                    case KW_KEYS:
                    case KW_KEY_TYPE:
                    case KW_LATERAL:
                    case KW_LEFT:
                    case KW_LIKE:
                    case KW_LIMIT:
                    case KW_LINES:
                    case KW_LOAD:
                    case KW_LOCAL:
                    case KW_LOCATION:
                    case KW_LOCK:
                    case KW_LOCKS:
                    case KW_LOGICAL:
                    case KW_LONG:
                    case KW_MAP:
                    case KW_MAPJOIN:
                    case KW_MATERIALIZED:
                    case KW_MINUS:
                    case KW_MSCK:
                    case KW_NONE:
                    case KW_NOSCAN:
                    case KW_NOT:
                    case KW_NO_DROP:
                    case KW_NULL:
                    case KW_OF:
                    case KW_OFFLINE:
                    case KW_OPTION:
                    case KW_ORDER:
                    case KW_OUT:
                    case KW_OUTER:
                    case KW_OUTPUTDRIVER:
                    case KW_OUTPUTFORMAT:
                    case KW_OVERWRITE:
                    case KW_OWNER:
                    case KW_PARTITION:
                    case KW_PARTITIONED:
                    case KW_PARTITIONS:
                    case KW_PERCENT:
                    case KW_PLUS:
                    case KW_PRETTY:
                    case KW_PRINCIPALS:
                    case KW_PROCEDURE:
                    case KW_PROTECTION:
                    case KW_PURGE:
                    case KW_RANGE:
                    case KW_READ:
                    case KW_READONLY:
                    case KW_READS:
                    case KW_REBUILD:
                    case KW_RECORDREADER:
                    case KW_RECORDWRITER:
                    case KW_REGEXP:
                    case KW_RENAME:
                    case KW_REPAIR:
                    case KW_REPLACE:
                    case KW_RESTRICT:
                    case KW_REVOKE:
                    case KW_REWRITE:
                    case KW_RIGHT:
                    case KW_RLIKE:
                    case KW_ROLE:
                    case KW_ROLES:
                    case KW_ROLLUP:
                    case KW_ROW:
                    case KW_ROWS:
                    case KW_SCHEMA:
                    case KW_SCHEMAS:
                    case KW_SEMI:
                    case KW_SERDE:
                    case KW_SERDEPROPERTIES:
                    case KW_SET:
                    case KW_SETS:
                    case KW_SHARED:
                    case KW_SHOW:
                    case KW_SHOW_DATABASE:
                    case KW_SKEWED:
                    case KW_SMALLINT:
                    case KW_SORT:
                    case KW_SORTED:
                    case KW_SSL:
                    case KW_STATISTICS:
                    case KW_STORED:
                    case KW_STREAMTABLE:
                    case KW_STRING:
                    case KW_STRUCT:
                    case KW_TABLE:
                    case KW_TABLES:
                    case KW_TBLPROPERTIES:
                    case KW_TEMPORARY:
                    case KW_TERMINATED:
                    case KW_TIMESTAMP:
                    case KW_TINYINT:
                    case KW_TO:
                    case KW_TOUCH:
                    case KW_TRANSACTIONS:
                    case KW_TRIGGER:
                    case KW_TRUE:
                    case KW_TRUNCATE:
                    case KW_UNARCHIVE:
                    case KW_UNDO:
                    case KW_UNION:
                    case KW_UNIONTYPE:
                    case KW_UNLOCK:
                    case KW_UNSET:
                    case KW_UNSIGNED:
                    case KW_UPDATE:
                    case KW_USE:
                    case KW_USER:
                    case KW_USING:
                    case KW_UTC:
                    case KW_UTCTIMESTAMP:
                    case KW_VALUES:
                    case KW_VALUE_TYPE:
                    case KW_VIEW:
                    case KW_WHILE:
                    case KW_WITH:
                    case MINUS:
                    case Number:
                    case PLUS:
                    case SmallintLiteral:
                    case StringLiteral:
                    case TILDE:
                    case TinyintLiteral:
                        {
                        alt19=2;
                        }
                        break;
                    default:
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 19, 2, input);

                        throw nvae;

                    }

                    }
                    break;
                default:
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 19, 1, input);

                    throw nvae;

                }

                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 19, 0, input);

                throw nvae;

            }

            switch (alt19) {
                case 1 :
                    // IdentifiersParser.g:150:5: KW_SORT KW_BY LPAREN columnRefOrder ( COMMA columnRefOrder )* RPAREN
                    {
                    KW_SORT67=(Token)match(input,KW_SORT,FOLLOW_KW_SORT_in_sortByClause812); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_KW_SORT.add(KW_SORT67);


                    KW_BY68=(Token)match(input,KW_BY,FOLLOW_KW_BY_in_sortByClause814); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_KW_BY.add(KW_BY68);


                    LPAREN69=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_sortByClause820); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN69);


                    pushFollow(FOLLOW_columnRefOrder_in_sortByClause822);
                    columnRefOrder70=gHiveParser.columnRefOrder();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_columnRefOrder.add(columnRefOrder70.getTree());

                    // IdentifiersParser.g:152:5: ( COMMA columnRefOrder )*
                    loop17:
                    do {
                        int alt17=2;
                        switch ( input.LA(1) ) {
                        case COMMA:
                            {
                            alt17=1;
                            }
                            break;

                        }

                        switch (alt17) {
                    	case 1 :
                    	    // IdentifiersParser.g:152:7: COMMA columnRefOrder
                    	    {
                    	    COMMA71=(Token)match(input,COMMA,FOLLOW_COMMA_in_sortByClause830); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA71);


                    	    pushFollow(FOLLOW_columnRefOrder_in_sortByClause832);
                    	    columnRefOrder72=gHiveParser.columnRefOrder();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_columnRefOrder.add(columnRefOrder72.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop17;
                        }
                    } while (true);


                    RPAREN73=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_sortByClause836); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN73);


                    // AST REWRITE
                    // elements: columnRefOrder
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 152:37: -> ^( TOK_SORTBY ( columnRefOrder )+ )
                    {
                        // IdentifiersParser.g:152:40: ^( TOK_SORTBY ( columnRefOrder )+ )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(TOK_SORTBY, "TOK_SORTBY")
                        , root_1);

                        if ( !(stream_columnRefOrder.hasNext()) ) {
                            throw new RewriteEarlyExitException();
                        }
                        while ( stream_columnRefOrder.hasNext() ) {
                            adaptor.addChild(root_1, stream_columnRefOrder.nextTree());

                        }
                        stream_columnRefOrder.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 2 :
                    // IdentifiersParser.g:154:5: KW_SORT KW_BY columnRefOrder ( ( COMMA )=> COMMA columnRefOrder )*
                    {
                    KW_SORT74=(Token)match(input,KW_SORT,FOLLOW_KW_SORT_in_sortByClause857); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_KW_SORT.add(KW_SORT74);


                    KW_BY75=(Token)match(input,KW_BY,FOLLOW_KW_BY_in_sortByClause859); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_KW_BY.add(KW_BY75);


                    pushFollow(FOLLOW_columnRefOrder_in_sortByClause865);
                    columnRefOrder76=gHiveParser.columnRefOrder();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_columnRefOrder.add(columnRefOrder76.getTree());

                    // IdentifiersParser.g:156:5: ( ( COMMA )=> COMMA columnRefOrder )*
                    loop18:
                    do {
                        int alt18=2;
                        int LA18_0 = input.LA(1);

                        if ( (LA18_0==COMMA) && (synpred4_IdentifiersParser())) {
                            alt18=1;
                        }


                        switch (alt18) {
                    	case 1 :
                    	    // IdentifiersParser.g:156:7: ( COMMA )=> COMMA columnRefOrder
                    	    {
                    	    COMMA77=(Token)match(input,COMMA,FOLLOW_COMMA_in_sortByClause878); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA77);


                    	    pushFollow(FOLLOW_columnRefOrder_in_sortByClause880);
                    	    columnRefOrder78=gHiveParser.columnRefOrder();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_columnRefOrder.add(columnRefOrder78.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop18;
                        }
                    } while (true);


                    // AST REWRITE
                    // elements: columnRefOrder
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 156:40: -> ^( TOK_SORTBY ( columnRefOrder )+ )
                    {
                        // IdentifiersParser.g:156:43: ^( TOK_SORTBY ( columnRefOrder )+ )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(TOK_SORTBY, "TOK_SORTBY")
                        , root_1);

                        if ( !(stream_columnRefOrder.hasNext()) ) {
                            throw new RewriteEarlyExitException();
                        }
                        while ( stream_columnRefOrder.hasNext() ) {
                            adaptor.addChild(root_1, stream_columnRefOrder.nextTree());

                        }
                        stream_columnRefOrder.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
            if ( state.backtracking==0 ) { gParent.popMsg(state); }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "sortByClause"


    public static class function_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "function"
    // IdentifiersParser.g:160:1: function : functionName LPAREN ( (star= STAR ) | (dist= KW_DISTINCT )? ( selectExpression ( COMMA selectExpression )* )? ) RPAREN ( KW_OVER ws= window_specification )? -> {$star != null}? ^( TOK_FUNCTIONSTAR functionName ( $ws)? ) -> {$dist == null}? ^( TOK_FUNCTION functionName ( ( selectExpression )+ )? ( $ws)? ) -> ^( TOK_FUNCTIONDI functionName ( ( selectExpression )+ )? ) ;
    public final HiveParser_IdentifiersParser.function_return function() throws RecognitionException {
        HiveParser_IdentifiersParser.function_return retval = new HiveParser_IdentifiersParser.function_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token star=null;
        Token dist=null;
        Token LPAREN80=null;
        Token COMMA82=null;
        Token RPAREN84=null;
        Token KW_OVER85=null;
        HiveParser_SelectClauseParser.window_specification_return ws =null;

        HiveParser_IdentifiersParser.functionName_return functionName79 =null;

        HiveParser_SelectClauseParser.selectExpression_return selectExpression81 =null;

        HiveParser_SelectClauseParser.selectExpression_return selectExpression83 =null;


        CommonTree star_tree=null;
        CommonTree dist_tree=null;
        CommonTree LPAREN80_tree=null;
        CommonTree COMMA82_tree=null;
        CommonTree RPAREN84_tree=null;
        CommonTree KW_OVER85_tree=null;
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
        RewriteRuleTokenStream stream_KW_OVER=new RewriteRuleTokenStream(adaptor,"token KW_OVER");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_KW_DISTINCT=new RewriteRuleTokenStream(adaptor,"token KW_DISTINCT");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleSubtreeStream stream_window_specification=new RewriteRuleSubtreeStream(adaptor,"rule window_specification");
        RewriteRuleSubtreeStream stream_selectExpression=new RewriteRuleSubtreeStream(adaptor,"rule selectExpression");
        RewriteRuleSubtreeStream stream_functionName=new RewriteRuleSubtreeStream(adaptor,"rule functionName");
         gParent.pushMsg("function specification", state); 
        try {
            // IdentifiersParser.g:163:5: ( functionName LPAREN ( (star= STAR ) | (dist= KW_DISTINCT )? ( selectExpression ( COMMA selectExpression )* )? ) RPAREN ( KW_OVER ws= window_specification )? -> {$star != null}? ^( TOK_FUNCTIONSTAR functionName ( $ws)? ) -> {$dist == null}? ^( TOK_FUNCTION functionName ( ( selectExpression )+ )? ( $ws)? ) -> ^( TOK_FUNCTIONDI functionName ( ( selectExpression )+ )? ) )
            // IdentifiersParser.g:164:5: functionName LPAREN ( (star= STAR ) | (dist= KW_DISTINCT )? ( selectExpression ( COMMA selectExpression )* )? ) RPAREN ( KW_OVER ws= window_specification )?
            {
            pushFollow(FOLLOW_functionName_in_function923);
            functionName79=functionName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_functionName.add(functionName79.getTree());

            LPAREN80=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_function929); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN80);


            // IdentifiersParser.g:166:7: ( (star= STAR ) | (dist= KW_DISTINCT )? ( selectExpression ( COMMA selectExpression )* )? )
            int alt23=2;
            switch ( input.LA(1) ) {
            case STAR:
                {
                alt23=1;
                }
                break;
            case BigintLiteral:
            case CharSetName:
            case DecimalLiteral:
            case Identifier:
            case KW_ADD:
            case KW_ADMIN:
            case KW_AFTER:
            case KW_ALL:
            case KW_ALTER:
            case KW_ANALYZE:
            case KW_ARCHIVE:
            case KW_ARRAY:
            case KW_AS:
            case KW_ASC:
            case KW_AUTHORIZATION:
            case KW_BEFORE:
            case KW_BETWEEN:
            case KW_BIGINT:
            case KW_BINARY:
            case KW_BOOLEAN:
            case KW_BOTH:
            case KW_BUCKET:
            case KW_BUCKETS:
            case KW_BY:
            case KW_CASCADE:
            case KW_CASE:
            case KW_CAST:
            case KW_CHANGE:
            case KW_CLUSTER:
            case KW_CLUSTERED:
            case KW_CLUSTERSTATUS:
            case KW_COLLECTION:
            case KW_COLUMNS:
            case KW_COMMENT:
            case KW_COMPACT:
            case KW_COMPACTIONS:
            case KW_COMPUTE:
            case KW_CONCATENATE:
            case KW_CONTINUE:
            case KW_CREATE:
            case KW_CUBE:
            case KW_CURSOR:
            case KW_DATA:
            case KW_DATABASES:
            case KW_DATE:
            case KW_DATETIME:
            case KW_DBPROPERTIES:
            case KW_DECIMAL:
            case KW_DEFAULT:
            case KW_DEFERRED:
            case KW_DEFINED:
            case KW_DELETE:
            case KW_DELIMITED:
            case KW_DEPENDENCY:
            case KW_DESC:
            case KW_DESCRIBE:
            case KW_DIRECTORIES:
            case KW_DIRECTORY:
            case KW_DISABLE:
            case KW_DISTINCT:
            case KW_DISTRIBUTE:
            case KW_DOUBLE:
            case KW_DROP:
            case KW_ELEM_TYPE:
            case KW_ENABLE:
            case KW_ESCAPED:
            case KW_EXCLUSIVE:
            case KW_EXISTS:
            case KW_EXPLAIN:
            case KW_EXPORT:
            case KW_EXTERNAL:
            case KW_FALSE:
            case KW_FETCH:
            case KW_FIELDS:
            case KW_FILE:
            case KW_FILEFORMAT:
            case KW_FIRST:
            case KW_FLOAT:
            case KW_FOR:
            case KW_FORMAT:
            case KW_FORMATTED:
            case KW_FULL:
            case KW_FUNCTIONS:
            case KW_GRANT:
            case KW_GROUP:
            case KW_GROUPING:
            case KW_HOLD_DDLTIME:
            case KW_IDXPROPERTIES:
            case KW_IF:
            case KW_IGNORE:
            case KW_IMPORT:
            case KW_IN:
            case KW_INDEX:
            case KW_INDEXES:
            case KW_INNER:
            case KW_INPATH:
            case KW_INPUTDRIVER:
            case KW_INPUTFORMAT:
            case KW_INSERT:
            case KW_INT:
            case KW_INTERSECT:
            case KW_INTO:
            case KW_IS:
            case KW_ITEMS:
            case KW_JAR:
            case KW_KEYS:
            case KW_KEY_TYPE:
            case KW_LATERAL:
            case KW_LEFT:
            case KW_LIKE:
            case KW_LIMIT:
            case KW_LINES:
            case KW_LOAD:
            case KW_LOCAL:
            case KW_LOCATION:
            case KW_LOCK:
            case KW_LOCKS:
            case KW_LOGICAL:
            case KW_LONG:
            case KW_MAP:
            case KW_MAPJOIN:
            case KW_MATERIALIZED:
            case KW_MINUS:
            case KW_MSCK:
            case KW_NONE:
            case KW_NOSCAN:
            case KW_NOT:
            case KW_NO_DROP:
            case KW_NULL:
            case KW_OF:
            case KW_OFFLINE:
            case KW_OPTION:
            case KW_ORDER:
            case KW_OUT:
            case KW_OUTER:
            case KW_OUTPUTDRIVER:
            case KW_OUTPUTFORMAT:
            case KW_OVERWRITE:
            case KW_OWNER:
            case KW_PARTITION:
            case KW_PARTITIONED:
            case KW_PARTITIONS:
            case KW_PERCENT:
            case KW_PLUS:
            case KW_PRETTY:
            case KW_PRINCIPALS:
            case KW_PROCEDURE:
            case KW_PROTECTION:
            case KW_PURGE:
            case KW_RANGE:
            case KW_READ:
            case KW_READONLY:
            case KW_READS:
            case KW_REBUILD:
            case KW_RECORDREADER:
            case KW_RECORDWRITER:
            case KW_REGEXP:
            case KW_RENAME:
            case KW_REPAIR:
            case KW_REPLACE:
            case KW_RESTRICT:
            case KW_REVOKE:
            case KW_REWRITE:
            case KW_RIGHT:
            case KW_RLIKE:
            case KW_ROLE:
            case KW_ROLES:
            case KW_ROLLUP:
            case KW_ROW:
            case KW_ROWS:
            case KW_SCHEMA:
            case KW_SCHEMAS:
            case KW_SEMI:
            case KW_SERDE:
            case KW_SERDEPROPERTIES:
            case KW_SET:
            case KW_SETS:
            case KW_SHARED:
            case KW_SHOW:
            case KW_SHOW_DATABASE:
            case KW_SKEWED:
            case KW_SMALLINT:
            case KW_SORT:
            case KW_SORTED:
            case KW_SSL:
            case KW_STATISTICS:
            case KW_STORED:
            case KW_STREAMTABLE:
            case KW_STRING:
            case KW_STRUCT:
            case KW_TABLE:
            case KW_TABLES:
            case KW_TBLPROPERTIES:
            case KW_TEMPORARY:
            case KW_TERMINATED:
            case KW_TIMESTAMP:
            case KW_TINYINT:
            case KW_TO:
            case KW_TOUCH:
            case KW_TRANSACTIONS:
            case KW_TRIGGER:
            case KW_TRUE:
            case KW_TRUNCATE:
            case KW_UNARCHIVE:
            case KW_UNDO:
            case KW_UNION:
            case KW_UNIONTYPE:
            case KW_UNLOCK:
            case KW_UNSET:
            case KW_UNSIGNED:
            case KW_UPDATE:
            case KW_USE:
            case KW_USER:
            case KW_USING:
            case KW_UTC:
            case KW_UTCTIMESTAMP:
            case KW_VALUES:
            case KW_VALUE_TYPE:
            case KW_VIEW:
            case KW_WHILE:
            case KW_WITH:
            case LPAREN:
            case MINUS:
            case Number:
            case PLUS:
            case RPAREN:
            case SmallintLiteral:
            case StringLiteral:
            case TILDE:
            case TinyintLiteral:
                {
                alt23=2;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 23, 0, input);

                throw nvae;

            }

            switch (alt23) {
                case 1 :
                    // IdentifiersParser.g:167:9: (star= STAR )
                    {
                    // IdentifiersParser.g:167:9: (star= STAR )
                    // IdentifiersParser.g:167:10: star= STAR
                    {
                    star=(Token)match(input,STAR,FOLLOW_STAR_in_function950); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_STAR.add(star);


                    }


                    }
                    break;
                case 2 :
                    // IdentifiersParser.g:168:11: (dist= KW_DISTINCT )? ( selectExpression ( COMMA selectExpression )* )?
                    {
                    // IdentifiersParser.g:168:11: (dist= KW_DISTINCT )?
                    int alt20=2;
                    switch ( input.LA(1) ) {
                        case KW_DISTINCT:
                            {
                            alt20=1;
                            }
                            break;
                    }

                    switch (alt20) {
                        case 1 :
                            // IdentifiersParser.g:168:12: dist= KW_DISTINCT
                            {
                            dist=(Token)match(input,KW_DISTINCT,FOLLOW_KW_DISTINCT_in_function966); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_KW_DISTINCT.add(dist);


                            }
                            break;

                    }


                    // IdentifiersParser.g:168:31: ( selectExpression ( COMMA selectExpression )* )?
                    int alt22=2;
                    switch ( input.LA(1) ) {
                        case BigintLiteral:
                        case CharSetName:
                        case DecimalLiteral:
                        case Identifier:
                        case KW_ADD:
                        case KW_ADMIN:
                        case KW_AFTER:
                        case KW_ALL:
                        case KW_ALTER:
                        case KW_ANALYZE:
                        case KW_ARCHIVE:
                        case KW_ARRAY:
                        case KW_AS:
                        case KW_ASC:
                        case KW_AUTHORIZATION:
                        case KW_BEFORE:
                        case KW_BETWEEN:
                        case KW_BIGINT:
                        case KW_BINARY:
                        case KW_BOOLEAN:
                        case KW_BOTH:
                        case KW_BUCKET:
                        case KW_BUCKETS:
                        case KW_BY:
                        case KW_CASCADE:
                        case KW_CASE:
                        case KW_CAST:
                        case KW_CHANGE:
                        case KW_CLUSTER:
                        case KW_CLUSTERED:
                        case KW_CLUSTERSTATUS:
                        case KW_COLLECTION:
                        case KW_COLUMNS:
                        case KW_COMMENT:
                        case KW_COMPACT:
                        case KW_COMPACTIONS:
                        case KW_COMPUTE:
                        case KW_CONCATENATE:
                        case KW_CONTINUE:
                        case KW_CREATE:
                        case KW_CUBE:
                        case KW_CURSOR:
                        case KW_DATA:
                        case KW_DATABASES:
                        case KW_DATE:
                        case KW_DATETIME:
                        case KW_DBPROPERTIES:
                        case KW_DECIMAL:
                        case KW_DEFAULT:
                        case KW_DEFERRED:
                        case KW_DEFINED:
                        case KW_DELETE:
                        case KW_DELIMITED:
                        case KW_DEPENDENCY:
                        case KW_DESC:
                        case KW_DESCRIBE:
                        case KW_DIRECTORIES:
                        case KW_DIRECTORY:
                        case KW_DISABLE:
                        case KW_DISTRIBUTE:
                        case KW_DOUBLE:
                        case KW_DROP:
                        case KW_ELEM_TYPE:
                        case KW_ENABLE:
                        case KW_ESCAPED:
                        case KW_EXCLUSIVE:
                        case KW_EXISTS:
                        case KW_EXPLAIN:
                        case KW_EXPORT:
                        case KW_EXTERNAL:
                        case KW_FALSE:
                        case KW_FETCH:
                        case KW_FIELDS:
                        case KW_FILE:
                        case KW_FILEFORMAT:
                        case KW_FIRST:
                        case KW_FLOAT:
                        case KW_FOR:
                        case KW_FORMAT:
                        case KW_FORMATTED:
                        case KW_FULL:
                        case KW_FUNCTIONS:
                        case KW_GRANT:
                        case KW_GROUP:
                        case KW_GROUPING:
                        case KW_HOLD_DDLTIME:
                        case KW_IDXPROPERTIES:
                        case KW_IF:
                        case KW_IGNORE:
                        case KW_IMPORT:
                        case KW_IN:
                        case KW_INDEX:
                        case KW_INDEXES:
                        case KW_INNER:
                        case KW_INPATH:
                        case KW_INPUTDRIVER:
                        case KW_INPUTFORMAT:
                        case KW_INSERT:
                        case KW_INT:
                        case KW_INTERSECT:
                        case KW_INTO:
                        case KW_IS:
                        case KW_ITEMS:
                        case KW_JAR:
                        case KW_KEYS:
                        case KW_KEY_TYPE:
                        case KW_LATERAL:
                        case KW_LEFT:
                        case KW_LIKE:
                        case KW_LIMIT:
                        case KW_LINES:
                        case KW_LOAD:
                        case KW_LOCAL:
                        case KW_LOCATION:
                        case KW_LOCK:
                        case KW_LOCKS:
                        case KW_LOGICAL:
                        case KW_LONG:
                        case KW_MAP:
                        case KW_MAPJOIN:
                        case KW_MATERIALIZED:
                        case KW_MINUS:
                        case KW_MSCK:
                        case KW_NONE:
                        case KW_NOSCAN:
                        case KW_NOT:
                        case KW_NO_DROP:
                        case KW_NULL:
                        case KW_OF:
                        case KW_OFFLINE:
                        case KW_OPTION:
                        case KW_ORDER:
                        case KW_OUT:
                        case KW_OUTER:
                        case KW_OUTPUTDRIVER:
                        case KW_OUTPUTFORMAT:
                        case KW_OVERWRITE:
                        case KW_OWNER:
                        case KW_PARTITION:
                        case KW_PARTITIONED:
                        case KW_PARTITIONS:
                        case KW_PERCENT:
                        case KW_PLUS:
                        case KW_PRETTY:
                        case KW_PRINCIPALS:
                        case KW_PROCEDURE:
                        case KW_PROTECTION:
                        case KW_PURGE:
                        case KW_RANGE:
                        case KW_READ:
                        case KW_READONLY:
                        case KW_READS:
                        case KW_REBUILD:
                        case KW_RECORDREADER:
                        case KW_RECORDWRITER:
                        case KW_REGEXP:
                        case KW_RENAME:
                        case KW_REPAIR:
                        case KW_REPLACE:
                        case KW_RESTRICT:
                        case KW_REVOKE:
                        case KW_REWRITE:
                        case KW_RIGHT:
                        case KW_RLIKE:
                        case KW_ROLE:
                        case KW_ROLES:
                        case KW_ROLLUP:
                        case KW_ROW:
                        case KW_ROWS:
                        case KW_SCHEMA:
                        case KW_SCHEMAS:
                        case KW_SEMI:
                        case KW_SERDE:
                        case KW_SERDEPROPERTIES:
                        case KW_SET:
                        case KW_SETS:
                        case KW_SHARED:
                        case KW_SHOW:
                        case KW_SHOW_DATABASE:
                        case KW_SKEWED:
                        case KW_SMALLINT:
                        case KW_SORT:
                        case KW_SORTED:
                        case KW_SSL:
                        case KW_STATISTICS:
                        case KW_STORED:
                        case KW_STREAMTABLE:
                        case KW_STRING:
                        case KW_STRUCT:
                        case KW_TABLE:
                        case KW_TABLES:
                        case KW_TBLPROPERTIES:
                        case KW_TEMPORARY:
                        case KW_TERMINATED:
                        case KW_TIMESTAMP:
                        case KW_TINYINT:
                        case KW_TO:
                        case KW_TOUCH:
                        case KW_TRANSACTIONS:
                        case KW_TRIGGER:
                        case KW_TRUE:
                        case KW_TRUNCATE:
                        case KW_UNARCHIVE:
                        case KW_UNDO:
                        case KW_UNION:
                        case KW_UNIONTYPE:
                        case KW_UNLOCK:
                        case KW_UNSET:
                        case KW_UNSIGNED:
                        case KW_UPDATE:
                        case KW_USE:
                        case KW_USER:
                        case KW_USING:
                        case KW_UTC:
                        case KW_UTCTIMESTAMP:
                        case KW_VALUES:
                        case KW_VALUE_TYPE:
                        case KW_VIEW:
                        case KW_WHILE:
                        case KW_WITH:
                        case LPAREN:
                        case MINUS:
                        case Number:
                        case PLUS:
                        case STAR:
                        case SmallintLiteral:
                        case StringLiteral:
                        case TILDE:
                        case TinyintLiteral:
                            {
                            alt22=1;
                            }
                            break;
                    }

                    switch (alt22) {
                        case 1 :
                            // IdentifiersParser.g:168:32: selectExpression ( COMMA selectExpression )*
                            {
                            pushFollow(FOLLOW_selectExpression_in_function971);
                            selectExpression81=gHiveParser.selectExpression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_selectExpression.add(selectExpression81.getTree());

                            // IdentifiersParser.g:168:49: ( COMMA selectExpression )*
                            loop21:
                            do {
                                int alt21=2;
                                switch ( input.LA(1) ) {
                                case COMMA:
                                    {
                                    alt21=1;
                                    }
                                    break;

                                }

                                switch (alt21) {
                            	case 1 :
                            	    // IdentifiersParser.g:168:50: COMMA selectExpression
                            	    {
                            	    COMMA82=(Token)match(input,COMMA,FOLLOW_COMMA_in_function974); if (state.failed) return retval; 
                            	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA82);


                            	    pushFollow(FOLLOW_selectExpression_in_function976);
                            	    selectExpression83=gHiveParser.selectExpression();

                            	    state._fsp--;
                            	    if (state.failed) return retval;
                            	    if ( state.backtracking==0 ) stream_selectExpression.add(selectExpression83.getTree());

                            	    }
                            	    break;

                            	default :
                            	    break loop21;
                                }
                            } while (true);


                            }
                            break;

                    }


                    }
                    break;

            }


            RPAREN84=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_function994); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN84);


            // IdentifiersParser.g:170:12: ( KW_OVER ws= window_specification )?
            int alt24=2;
            alt24 = dfa24.predict(input);
            switch (alt24) {
                case 1 :
                    // IdentifiersParser.g:170:13: KW_OVER ws= window_specification
                    {
                    KW_OVER85=(Token)match(input,KW_OVER,FOLLOW_KW_OVER_in_function997); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_KW_OVER.add(KW_OVER85);


                    pushFollow(FOLLOW_window_specification_in_function1001);
                    ws=gHiveParser.window_specification();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_window_specification.add(ws.getTree());

                    }
                    break;

            }


            // AST REWRITE
            // elements: selectExpression, selectExpression, functionName, ws, functionName, functionName, ws
            // token labels: 
            // rule labels: retval, ws
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_ws=new RewriteRuleSubtreeStream(adaptor,"rule ws",ws!=null?ws.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 171:12: -> {$star != null}? ^( TOK_FUNCTIONSTAR functionName ( $ws)? )
            if (star != null) {
                // IdentifiersParser.g:171:32: ^( TOK_FUNCTIONSTAR functionName ( $ws)? )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(TOK_FUNCTIONSTAR, "TOK_FUNCTIONSTAR")
                , root_1);

                adaptor.addChild(root_1, stream_functionName.nextTree());

                // IdentifiersParser.g:171:65: ( $ws)?
                if ( stream_ws.hasNext() ) {
                    adaptor.addChild(root_1, stream_ws.nextTree());

                }
                stream_ws.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            else // 172:12: -> {$dist == null}? ^( TOK_FUNCTION functionName ( ( selectExpression )+ )? ( $ws)? )
            if (dist == null) {
                // IdentifiersParser.g:172:32: ^( TOK_FUNCTION functionName ( ( selectExpression )+ )? ( $ws)? )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(TOK_FUNCTION, "TOK_FUNCTION")
                , root_1);

                adaptor.addChild(root_1, stream_functionName.nextTree());

                // IdentifiersParser.g:172:60: ( ( selectExpression )+ )?
                if ( stream_selectExpression.hasNext() ) {
                    if ( !(stream_selectExpression.hasNext()) ) {
                        throw new RewriteEarlyExitException();
                    }
                    while ( stream_selectExpression.hasNext() ) {
                        adaptor.addChild(root_1, stream_selectExpression.nextTree());

                    }
                    stream_selectExpression.reset();

                }
                stream_selectExpression.reset();

                // IdentifiersParser.g:172:82: ( $ws)?
                if ( stream_ws.hasNext() ) {
                    adaptor.addChild(root_1, stream_ws.nextTree());

                }
                stream_ws.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            else // 173:29: -> ^( TOK_FUNCTIONDI functionName ( ( selectExpression )+ )? )
            {
                // IdentifiersParser.g:173:32: ^( TOK_FUNCTIONDI functionName ( ( selectExpression )+ )? )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(TOK_FUNCTIONDI, "TOK_FUNCTIONDI")
                , root_1);

                adaptor.addChild(root_1, stream_functionName.nextTree());

                // IdentifiersParser.g:173:62: ( ( selectExpression )+ )?
                if ( stream_selectExpression.hasNext() ) {
                    if ( !(stream_selectExpression.hasNext()) ) {
                        throw new RewriteEarlyExitException();
                    }
                    while ( stream_selectExpression.hasNext() ) {
                        adaptor.addChild(root_1, stream_selectExpression.nextTree());

                    }
                    stream_selectExpression.reset();

                }
                stream_selectExpression.reset();

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
            if ( state.backtracking==0 ) { gParent.popMsg(state); }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "function"


    public static class functionName_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "functionName"
    // IdentifiersParser.g:176:1: functionName : ( KW_IF | KW_ARRAY | KW_MAP | KW_STRUCT | KW_UNIONTYPE | functionIdentifier );
    public final HiveParser_IdentifiersParser.functionName_return functionName() throws RecognitionException {
        HiveParser_IdentifiersParser.functionName_return retval = new HiveParser_IdentifiersParser.functionName_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token KW_IF86=null;
        Token KW_ARRAY87=null;
        Token KW_MAP88=null;
        Token KW_STRUCT89=null;
        Token KW_UNIONTYPE90=null;
        HiveParser_IdentifiersParser.functionIdentifier_return functionIdentifier91 =null;


        CommonTree KW_IF86_tree=null;
        CommonTree KW_ARRAY87_tree=null;
        CommonTree KW_MAP88_tree=null;
        CommonTree KW_STRUCT89_tree=null;
        CommonTree KW_UNIONTYPE90_tree=null;

         gParent.pushMsg("function name", state); 
        try {
            // IdentifiersParser.g:179:5: ( KW_IF | KW_ARRAY | KW_MAP | KW_STRUCT | KW_UNIONTYPE | functionIdentifier )
            int alt25=6;
            switch ( input.LA(1) ) {
            case KW_IF:
                {
                alt25=1;
                }
                break;
            case KW_ARRAY:
                {
                alt25=2;
                }
                break;
            case KW_MAP:
                {
                alt25=3;
                }
                break;
            case KW_STRUCT:
                {
                alt25=4;
                }
                break;
            case KW_UNIONTYPE:
                {
                alt25=5;
                }
                break;
            case Identifier:
            case KW_ADD:
            case KW_ADMIN:
            case KW_AFTER:
            case KW_ALL:
            case KW_ALTER:
            case KW_ANALYZE:
            case KW_ARCHIVE:
            case KW_AS:
            case KW_ASC:
            case KW_AUTHORIZATION:
            case KW_BEFORE:
            case KW_BETWEEN:
            case KW_BIGINT:
            case KW_BINARY:
            case KW_BOOLEAN:
            case KW_BOTH:
            case KW_BUCKET:
            case KW_BUCKETS:
            case KW_BY:
            case KW_CASCADE:
            case KW_CHANGE:
            case KW_CLUSTER:
            case KW_CLUSTERED:
            case KW_CLUSTERSTATUS:
            case KW_COLLECTION:
            case KW_COLUMNS:
            case KW_COMMENT:
            case KW_COMPACT:
            case KW_COMPACTIONS:
            case KW_COMPUTE:
            case KW_CONCATENATE:
            case KW_CONTINUE:
            case KW_CREATE:
            case KW_CUBE:
            case KW_CURSOR:
            case KW_DATA:
            case KW_DATABASES:
            case KW_DATE:
            case KW_DATETIME:
            case KW_DBPROPERTIES:
            case KW_DECIMAL:
            case KW_DEFAULT:
            case KW_DEFERRED:
            case KW_DEFINED:
            case KW_DELETE:
            case KW_DELIMITED:
            case KW_DEPENDENCY:
            case KW_DESC:
            case KW_DESCRIBE:
            case KW_DIRECTORIES:
            case KW_DIRECTORY:
            case KW_DISABLE:
            case KW_DISTRIBUTE:
            case KW_DOUBLE:
            case KW_DROP:
            case KW_ELEM_TYPE:
            case KW_ENABLE:
            case KW_ESCAPED:
            case KW_EXCLUSIVE:
            case KW_EXISTS:
            case KW_EXPLAIN:
            case KW_EXPORT:
            case KW_EXTERNAL:
            case KW_FALSE:
            case KW_FETCH:
            case KW_FIELDS:
            case KW_FILE:
            case KW_FILEFORMAT:
            case KW_FIRST:
            case KW_FLOAT:
            case KW_FOR:
            case KW_FORMAT:
            case KW_FORMATTED:
            case KW_FULL:
            case KW_FUNCTIONS:
            case KW_GRANT:
            case KW_GROUP:
            case KW_GROUPING:
            case KW_HOLD_DDLTIME:
            case KW_IDXPROPERTIES:
            case KW_IGNORE:
            case KW_IMPORT:
            case KW_IN:
            case KW_INDEX:
            case KW_INDEXES:
            case KW_INNER:
            case KW_INPATH:
            case KW_INPUTDRIVER:
            case KW_INPUTFORMAT:
            case KW_INSERT:
            case KW_INT:
            case KW_INTERSECT:
            case KW_INTO:
            case KW_IS:
            case KW_ITEMS:
            case KW_JAR:
            case KW_KEYS:
            case KW_KEY_TYPE:
            case KW_LATERAL:
            case KW_LEFT:
            case KW_LIKE:
            case KW_LIMIT:
            case KW_LINES:
            case KW_LOAD:
            case KW_LOCAL:
            case KW_LOCATION:
            case KW_LOCK:
            case KW_LOCKS:
            case KW_LOGICAL:
            case KW_LONG:
            case KW_MAPJOIN:
            case KW_MATERIALIZED:
            case KW_MINUS:
            case KW_MSCK:
            case KW_NONE:
            case KW_NOSCAN:
            case KW_NO_DROP:
            case KW_NULL:
            case KW_OF:
            case KW_OFFLINE:
            case KW_OPTION:
            case KW_ORDER:
            case KW_OUT:
            case KW_OUTER:
            case KW_OUTPUTDRIVER:
            case KW_OUTPUTFORMAT:
            case KW_OVERWRITE:
            case KW_OWNER:
            case KW_PARTITION:
            case KW_PARTITIONED:
            case KW_PARTITIONS:
            case KW_PERCENT:
            case KW_PLUS:
            case KW_PRETTY:
            case KW_PRINCIPALS:
            case KW_PROCEDURE:
            case KW_PROTECTION:
            case KW_PURGE:
            case KW_RANGE:
            case KW_READ:
            case KW_READONLY:
            case KW_READS:
            case KW_REBUILD:
            case KW_RECORDREADER:
            case KW_RECORDWRITER:
            case KW_REGEXP:
            case KW_RENAME:
            case KW_REPAIR:
            case KW_REPLACE:
            case KW_RESTRICT:
            case KW_REVOKE:
            case KW_REWRITE:
            case KW_RIGHT:
            case KW_RLIKE:
            case KW_ROLE:
            case KW_ROLES:
            case KW_ROLLUP:
            case KW_ROW:
            case KW_ROWS:
            case KW_SCHEMA:
            case KW_SCHEMAS:
            case KW_SEMI:
            case KW_SERDE:
            case KW_SERDEPROPERTIES:
            case KW_SET:
            case KW_SETS:
            case KW_SHARED:
            case KW_SHOW:
            case KW_SHOW_DATABASE:
            case KW_SKEWED:
            case KW_SMALLINT:
            case KW_SORT:
            case KW_SORTED:
            case KW_SSL:
            case KW_STATISTICS:
            case KW_STORED:
            case KW_STREAMTABLE:
            case KW_STRING:
            case KW_TABLE:
            case KW_TABLES:
            case KW_TBLPROPERTIES:
            case KW_TEMPORARY:
            case KW_TERMINATED:
            case KW_TIMESTAMP:
            case KW_TINYINT:
            case KW_TO:
            case KW_TOUCH:
            case KW_TRANSACTIONS:
            case KW_TRIGGER:
            case KW_TRUE:
            case KW_TRUNCATE:
            case KW_UNARCHIVE:
            case KW_UNDO:
            case KW_UNION:
            case KW_UNLOCK:
            case KW_UNSET:
            case KW_UNSIGNED:
            case KW_UPDATE:
            case KW_USE:
            case KW_USER:
            case KW_USING:
            case KW_UTC:
            case KW_UTCTIMESTAMP:
            case KW_VALUES:
            case KW_VALUE_TYPE:
            case KW_VIEW:
            case KW_WHILE:
            case KW_WITH:
                {
                alt25=6;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 25, 0, input);

                throw nvae;

            }

            switch (alt25) {
                case 1 :
                    // IdentifiersParser.g:180:5: KW_IF
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    KW_IF86=(Token)match(input,KW_IF,FOLLOW_KW_IF_in_functionName1133); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    KW_IF86_tree = 
                    (CommonTree)adaptor.create(KW_IF86)
                    ;
                    adaptor.addChild(root_0, KW_IF86_tree);
                    }

                    }
                    break;
                case 2 :
                    // IdentifiersParser.g:180:13: KW_ARRAY
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    KW_ARRAY87=(Token)match(input,KW_ARRAY,FOLLOW_KW_ARRAY_in_functionName1137); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    KW_ARRAY87_tree = 
                    (CommonTree)adaptor.create(KW_ARRAY87)
                    ;
                    adaptor.addChild(root_0, KW_ARRAY87_tree);
                    }

                    }
                    break;
                case 3 :
                    // IdentifiersParser.g:180:24: KW_MAP
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    KW_MAP88=(Token)match(input,KW_MAP,FOLLOW_KW_MAP_in_functionName1141); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    KW_MAP88_tree = 
                    (CommonTree)adaptor.create(KW_MAP88)
                    ;
                    adaptor.addChild(root_0, KW_MAP88_tree);
                    }

                    }
                    break;
                case 4 :
                    // IdentifiersParser.g:180:33: KW_STRUCT
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    KW_STRUCT89=(Token)match(input,KW_STRUCT,FOLLOW_KW_STRUCT_in_functionName1145); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    KW_STRUCT89_tree = 
                    (CommonTree)adaptor.create(KW_STRUCT89)
                    ;
                    adaptor.addChild(root_0, KW_STRUCT89_tree);
                    }

                    }
                    break;
                case 5 :
                    // IdentifiersParser.g:180:45: KW_UNIONTYPE
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    KW_UNIONTYPE90=(Token)match(input,KW_UNIONTYPE,FOLLOW_KW_UNIONTYPE_in_functionName1149); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    KW_UNIONTYPE90_tree = 
                    (CommonTree)adaptor.create(KW_UNIONTYPE90)
                    ;
                    adaptor.addChild(root_0, KW_UNIONTYPE90_tree);
                    }

                    }
                    break;
                case 6 :
                    // IdentifiersParser.g:180:60: functionIdentifier
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_functionIdentifier_in_functionName1153);
                    functionIdentifier91=functionIdentifier();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, functionIdentifier91.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
            if ( state.backtracking==0 ) { gParent.popMsg(state); }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "functionName"


    public static class castExpression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "castExpression"
    // IdentifiersParser.g:183:1: castExpression : KW_CAST LPAREN expression KW_AS primitiveType RPAREN -> ^( TOK_FUNCTION primitiveType expression ) ;
    public final HiveParser_IdentifiersParser.castExpression_return castExpression() throws RecognitionException {
        HiveParser_IdentifiersParser.castExpression_return retval = new HiveParser_IdentifiersParser.castExpression_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token KW_CAST92=null;
        Token LPAREN93=null;
        Token KW_AS95=null;
        Token RPAREN97=null;
        HiveParser_IdentifiersParser.expression_return expression94 =null;

        HiveParser.primitiveType_return primitiveType96 =null;


        CommonTree KW_CAST92_tree=null;
        CommonTree LPAREN93_tree=null;
        CommonTree KW_AS95_tree=null;
        CommonTree RPAREN97_tree=null;
        RewriteRuleTokenStream stream_KW_AS=new RewriteRuleTokenStream(adaptor,"token KW_AS");
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_KW_CAST=new RewriteRuleTokenStream(adaptor,"token KW_CAST");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleSubtreeStream stream_expression=new RewriteRuleSubtreeStream(adaptor,"rule expression");
        RewriteRuleSubtreeStream stream_primitiveType=new RewriteRuleSubtreeStream(adaptor,"rule primitiveType");
         gParent.pushMsg("cast expression", state); 
        try {
            // IdentifiersParser.g:186:5: ( KW_CAST LPAREN expression KW_AS primitiveType RPAREN -> ^( TOK_FUNCTION primitiveType expression ) )
            // IdentifiersParser.g:187:5: KW_CAST LPAREN expression KW_AS primitiveType RPAREN
            {
            KW_CAST92=(Token)match(input,KW_CAST,FOLLOW_KW_CAST_in_castExpression1184); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KW_CAST.add(KW_CAST92);


            LPAREN93=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_castExpression1190); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN93);


            pushFollow(FOLLOW_expression_in_castExpression1202);
            expression94=expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_expression.add(expression94.getTree());

            KW_AS95=(Token)match(input,KW_AS,FOLLOW_KW_AS_in_castExpression1214); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KW_AS.add(KW_AS95);


            pushFollow(FOLLOW_primitiveType_in_castExpression1226);
            primitiveType96=gHiveParser.primitiveType();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_primitiveType.add(primitiveType96.getTree());

            RPAREN97=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_castExpression1232); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN97);


            // AST REWRITE
            // elements: expression, primitiveType
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 192:12: -> ^( TOK_FUNCTION primitiveType expression )
            {
                // IdentifiersParser.g:192:15: ^( TOK_FUNCTION primitiveType expression )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(TOK_FUNCTION, "TOK_FUNCTION")
                , root_1);

                adaptor.addChild(root_1, stream_primitiveType.nextTree());

                adaptor.addChild(root_1, stream_expression.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
            if ( state.backtracking==0 ) { gParent.popMsg(state); }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "castExpression"


    public static class caseExpression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "caseExpression"
    // IdentifiersParser.g:195:1: caseExpression : KW_CASE expression ( KW_WHEN expression KW_THEN expression )+ ( KW_ELSE expression )? KW_END -> ^( TOK_FUNCTION KW_CASE ( expression )* ) ;
    public final HiveParser_IdentifiersParser.caseExpression_return caseExpression() throws RecognitionException {
        HiveParser_IdentifiersParser.caseExpression_return retval = new HiveParser_IdentifiersParser.caseExpression_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token KW_CASE98=null;
        Token KW_WHEN100=null;
        Token KW_THEN102=null;
        Token KW_ELSE104=null;
        Token KW_END106=null;
        HiveParser_IdentifiersParser.expression_return expression99 =null;

        HiveParser_IdentifiersParser.expression_return expression101 =null;

        HiveParser_IdentifiersParser.expression_return expression103 =null;

        HiveParser_IdentifiersParser.expression_return expression105 =null;


        CommonTree KW_CASE98_tree=null;
        CommonTree KW_WHEN100_tree=null;
        CommonTree KW_THEN102_tree=null;
        CommonTree KW_ELSE104_tree=null;
        CommonTree KW_END106_tree=null;
        RewriteRuleTokenStream stream_KW_THEN=new RewriteRuleTokenStream(adaptor,"token KW_THEN");
        RewriteRuleTokenStream stream_KW_CASE=new RewriteRuleTokenStream(adaptor,"token KW_CASE");
        RewriteRuleTokenStream stream_KW_WHEN=new RewriteRuleTokenStream(adaptor,"token KW_WHEN");
        RewriteRuleTokenStream stream_KW_END=new RewriteRuleTokenStream(adaptor,"token KW_END");
        RewriteRuleTokenStream stream_KW_ELSE=new RewriteRuleTokenStream(adaptor,"token KW_ELSE");
        RewriteRuleSubtreeStream stream_expression=new RewriteRuleSubtreeStream(adaptor,"rule expression");
         gParent.pushMsg("case expression", state); 
        try {
            // IdentifiersParser.g:198:5: ( KW_CASE expression ( KW_WHEN expression KW_THEN expression )+ ( KW_ELSE expression )? KW_END -> ^( TOK_FUNCTION KW_CASE ( expression )* ) )
            // IdentifiersParser.g:199:5: KW_CASE expression ( KW_WHEN expression KW_THEN expression )+ ( KW_ELSE expression )? KW_END
            {
            KW_CASE98=(Token)match(input,KW_CASE,FOLLOW_KW_CASE_in_caseExpression1273); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KW_CASE.add(KW_CASE98);


            pushFollow(FOLLOW_expression_in_caseExpression1275);
            expression99=expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_expression.add(expression99.getTree());

            // IdentifiersParser.g:200:5: ( KW_WHEN expression KW_THEN expression )+
            int cnt26=0;
            loop26:
            do {
                int alt26=2;
                switch ( input.LA(1) ) {
                case KW_WHEN:
                    {
                    alt26=1;
                    }
                    break;

                }

                switch (alt26) {
            	case 1 :
            	    // IdentifiersParser.g:200:6: KW_WHEN expression KW_THEN expression
            	    {
            	    KW_WHEN100=(Token)match(input,KW_WHEN,FOLLOW_KW_WHEN_in_caseExpression1282); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_KW_WHEN.add(KW_WHEN100);


            	    pushFollow(FOLLOW_expression_in_caseExpression1284);
            	    expression101=expression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_expression.add(expression101.getTree());

            	    KW_THEN102=(Token)match(input,KW_THEN,FOLLOW_KW_THEN_in_caseExpression1286); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_KW_THEN.add(KW_THEN102);


            	    pushFollow(FOLLOW_expression_in_caseExpression1288);
            	    expression103=expression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_expression.add(expression103.getTree());

            	    }
            	    break;

            	default :
            	    if ( cnt26 >= 1 ) break loop26;
            	    if (state.backtracking>0) {state.failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(26, input);
                        throw eee;
                }
                cnt26++;
            } while (true);


            // IdentifiersParser.g:201:5: ( KW_ELSE expression )?
            int alt27=2;
            switch ( input.LA(1) ) {
                case KW_ELSE:
                    {
                    alt27=1;
                    }
                    break;
            }

            switch (alt27) {
                case 1 :
                    // IdentifiersParser.g:201:6: KW_ELSE expression
                    {
                    KW_ELSE104=(Token)match(input,KW_ELSE,FOLLOW_KW_ELSE_in_caseExpression1297); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_KW_ELSE.add(KW_ELSE104);


                    pushFollow(FOLLOW_expression_in_caseExpression1299);
                    expression105=expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_expression.add(expression105.getTree());

                    }
                    break;

            }


            KW_END106=(Token)match(input,KW_END,FOLLOW_KW_END_in_caseExpression1307); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KW_END.add(KW_END106);


            // AST REWRITE
            // elements: expression, KW_CASE
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 202:12: -> ^( TOK_FUNCTION KW_CASE ( expression )* )
            {
                // IdentifiersParser.g:202:15: ^( TOK_FUNCTION KW_CASE ( expression )* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(TOK_FUNCTION, "TOK_FUNCTION")
                , root_1);

                adaptor.addChild(root_1, 
                stream_KW_CASE.nextNode()
                );

                // IdentifiersParser.g:202:38: ( expression )*
                while ( stream_expression.hasNext() ) {
                    adaptor.addChild(root_1, stream_expression.nextTree());

                }
                stream_expression.reset();

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
            if ( state.backtracking==0 ) { gParent.popMsg(state); }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "caseExpression"


    public static class whenExpression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "whenExpression"
    // IdentifiersParser.g:205:1: whenExpression : KW_CASE ( KW_WHEN expression KW_THEN expression )+ ( KW_ELSE expression )? KW_END -> ^( TOK_FUNCTION KW_WHEN ( expression )* ) ;
    public final HiveParser_IdentifiersParser.whenExpression_return whenExpression() throws RecognitionException {
        HiveParser_IdentifiersParser.whenExpression_return retval = new HiveParser_IdentifiersParser.whenExpression_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token KW_CASE107=null;
        Token KW_WHEN108=null;
        Token KW_THEN110=null;
        Token KW_ELSE112=null;
        Token KW_END114=null;
        HiveParser_IdentifiersParser.expression_return expression109 =null;

        HiveParser_IdentifiersParser.expression_return expression111 =null;

        HiveParser_IdentifiersParser.expression_return expression113 =null;


        CommonTree KW_CASE107_tree=null;
        CommonTree KW_WHEN108_tree=null;
        CommonTree KW_THEN110_tree=null;
        CommonTree KW_ELSE112_tree=null;
        CommonTree KW_END114_tree=null;
        RewriteRuleTokenStream stream_KW_THEN=new RewriteRuleTokenStream(adaptor,"token KW_THEN");
        RewriteRuleTokenStream stream_KW_CASE=new RewriteRuleTokenStream(adaptor,"token KW_CASE");
        RewriteRuleTokenStream stream_KW_WHEN=new RewriteRuleTokenStream(adaptor,"token KW_WHEN");
        RewriteRuleTokenStream stream_KW_END=new RewriteRuleTokenStream(adaptor,"token KW_END");
        RewriteRuleTokenStream stream_KW_ELSE=new RewriteRuleTokenStream(adaptor,"token KW_ELSE");
        RewriteRuleSubtreeStream stream_expression=new RewriteRuleSubtreeStream(adaptor,"rule expression");
         gParent.pushMsg("case expression", state); 
        try {
            // IdentifiersParser.g:208:5: ( KW_CASE ( KW_WHEN expression KW_THEN expression )+ ( KW_ELSE expression )? KW_END -> ^( TOK_FUNCTION KW_WHEN ( expression )* ) )
            // IdentifiersParser.g:209:5: KW_CASE ( KW_WHEN expression KW_THEN expression )+ ( KW_ELSE expression )? KW_END
            {
            KW_CASE107=(Token)match(input,KW_CASE,FOLLOW_KW_CASE_in_whenExpression1349); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KW_CASE.add(KW_CASE107);


            // IdentifiersParser.g:210:6: ( KW_WHEN expression KW_THEN expression )+
            int cnt28=0;
            loop28:
            do {
                int alt28=2;
                switch ( input.LA(1) ) {
                case KW_WHEN:
                    {
                    alt28=1;
                    }
                    break;

                }

                switch (alt28) {
            	case 1 :
            	    // IdentifiersParser.g:210:8: KW_WHEN expression KW_THEN expression
            	    {
            	    KW_WHEN108=(Token)match(input,KW_WHEN,FOLLOW_KW_WHEN_in_whenExpression1358); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_KW_WHEN.add(KW_WHEN108);


            	    pushFollow(FOLLOW_expression_in_whenExpression1360);
            	    expression109=expression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_expression.add(expression109.getTree());

            	    KW_THEN110=(Token)match(input,KW_THEN,FOLLOW_KW_THEN_in_whenExpression1362); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_KW_THEN.add(KW_THEN110);


            	    pushFollow(FOLLOW_expression_in_whenExpression1364);
            	    expression111=expression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_expression.add(expression111.getTree());

            	    }
            	    break;

            	default :
            	    if ( cnt28 >= 1 ) break loop28;
            	    if (state.backtracking>0) {state.failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(28, input);
                        throw eee;
                }
                cnt28++;
            } while (true);


            // IdentifiersParser.g:211:5: ( KW_ELSE expression )?
            int alt29=2;
            switch ( input.LA(1) ) {
                case KW_ELSE:
                    {
                    alt29=1;
                    }
                    break;
            }

            switch (alt29) {
                case 1 :
                    // IdentifiersParser.g:211:6: KW_ELSE expression
                    {
                    KW_ELSE112=(Token)match(input,KW_ELSE,FOLLOW_KW_ELSE_in_whenExpression1373); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_KW_ELSE.add(KW_ELSE112);


                    pushFollow(FOLLOW_expression_in_whenExpression1375);
                    expression113=expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_expression.add(expression113.getTree());

                    }
                    break;

            }


            KW_END114=(Token)match(input,KW_END,FOLLOW_KW_END_in_whenExpression1383); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KW_END.add(KW_END114);


            // AST REWRITE
            // elements: expression, KW_WHEN
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 212:12: -> ^( TOK_FUNCTION KW_WHEN ( expression )* )
            {
                // IdentifiersParser.g:212:15: ^( TOK_FUNCTION KW_WHEN ( expression )* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(TOK_FUNCTION, "TOK_FUNCTION")
                , root_1);

                adaptor.addChild(root_1, 
                stream_KW_WHEN.nextNode()
                );

                // IdentifiersParser.g:212:38: ( expression )*
                while ( stream_expression.hasNext() ) {
                    adaptor.addChild(root_1, stream_expression.nextTree());

                }
                stream_expression.reset();

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
            if ( state.backtracking==0 ) { gParent.popMsg(state); }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "whenExpression"


    public static class constant_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "constant"
    // IdentifiersParser.g:215:1: constant : ( Number | dateLiteral | StringLiteral | stringLiteralSequence | BigintLiteral | SmallintLiteral | TinyintLiteral | DecimalLiteral | charSetStringLiteral | booleanValue );
    public final HiveParser_IdentifiersParser.constant_return constant() throws RecognitionException {
        HiveParser_IdentifiersParser.constant_return retval = new HiveParser_IdentifiersParser.constant_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token Number115=null;
        Token StringLiteral117=null;
        Token BigintLiteral119=null;
        Token SmallintLiteral120=null;
        Token TinyintLiteral121=null;
        Token DecimalLiteral122=null;
        HiveParser_IdentifiersParser.dateLiteral_return dateLiteral116 =null;

        HiveParser_IdentifiersParser.stringLiteralSequence_return stringLiteralSequence118 =null;

        HiveParser_IdentifiersParser.charSetStringLiteral_return charSetStringLiteral123 =null;

        HiveParser_IdentifiersParser.booleanValue_return booleanValue124 =null;


        CommonTree Number115_tree=null;
        CommonTree StringLiteral117_tree=null;
        CommonTree BigintLiteral119_tree=null;
        CommonTree SmallintLiteral120_tree=null;
        CommonTree TinyintLiteral121_tree=null;
        CommonTree DecimalLiteral122_tree=null;

         gParent.pushMsg("constant", state); 
        try {
            // IdentifiersParser.g:218:5: ( Number | dateLiteral | StringLiteral | stringLiteralSequence | BigintLiteral | SmallintLiteral | TinyintLiteral | DecimalLiteral | charSetStringLiteral | booleanValue )
            int alt30=10;
            alt30 = dfa30.predict(input);
            switch (alt30) {
                case 1 :
                    // IdentifiersParser.g:219:5: Number
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    Number115=(Token)match(input,Number,FOLLOW_Number_in_constant1425); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    Number115_tree = 
                    (CommonTree)adaptor.create(Number115)
                    ;
                    adaptor.addChild(root_0, Number115_tree);
                    }

                    }
                    break;
                case 2 :
                    // IdentifiersParser.g:220:7: dateLiteral
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_dateLiteral_in_constant1433);
                    dateLiteral116=dateLiteral();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, dateLiteral116.getTree());

                    }
                    break;
                case 3 :
                    // IdentifiersParser.g:221:7: StringLiteral
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    StringLiteral117=(Token)match(input,StringLiteral,FOLLOW_StringLiteral_in_constant1441); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    StringLiteral117_tree = 
                    (CommonTree)adaptor.create(StringLiteral117)
                    ;
                    adaptor.addChild(root_0, StringLiteral117_tree);
                    }

                    }
                    break;
                case 4 :
                    // IdentifiersParser.g:222:7: stringLiteralSequence
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_stringLiteralSequence_in_constant1449);
                    stringLiteralSequence118=stringLiteralSequence();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, stringLiteralSequence118.getTree());

                    }
                    break;
                case 5 :
                    // IdentifiersParser.g:223:7: BigintLiteral
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    BigintLiteral119=(Token)match(input,BigintLiteral,FOLLOW_BigintLiteral_in_constant1457); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    BigintLiteral119_tree = 
                    (CommonTree)adaptor.create(BigintLiteral119)
                    ;
                    adaptor.addChild(root_0, BigintLiteral119_tree);
                    }

                    }
                    break;
                case 6 :
                    // IdentifiersParser.g:224:7: SmallintLiteral
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    SmallintLiteral120=(Token)match(input,SmallintLiteral,FOLLOW_SmallintLiteral_in_constant1465); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    SmallintLiteral120_tree = 
                    (CommonTree)adaptor.create(SmallintLiteral120)
                    ;
                    adaptor.addChild(root_0, SmallintLiteral120_tree);
                    }

                    }
                    break;
                case 7 :
                    // IdentifiersParser.g:225:7: TinyintLiteral
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    TinyintLiteral121=(Token)match(input,TinyintLiteral,FOLLOW_TinyintLiteral_in_constant1473); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    TinyintLiteral121_tree = 
                    (CommonTree)adaptor.create(TinyintLiteral121)
                    ;
                    adaptor.addChild(root_0, TinyintLiteral121_tree);
                    }

                    }
                    break;
                case 8 :
                    // IdentifiersParser.g:226:7: DecimalLiteral
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    DecimalLiteral122=(Token)match(input,DecimalLiteral,FOLLOW_DecimalLiteral_in_constant1481); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    DecimalLiteral122_tree = 
                    (CommonTree)adaptor.create(DecimalLiteral122)
                    ;
                    adaptor.addChild(root_0, DecimalLiteral122_tree);
                    }

                    }
                    break;
                case 9 :
                    // IdentifiersParser.g:227:7: charSetStringLiteral
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_charSetStringLiteral_in_constant1489);
                    charSetStringLiteral123=charSetStringLiteral();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, charSetStringLiteral123.getTree());

                    }
                    break;
                case 10 :
                    // IdentifiersParser.g:228:7: booleanValue
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_booleanValue_in_constant1497);
                    booleanValue124=booleanValue();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, booleanValue124.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
            if ( state.backtracking==0 ) { gParent.popMsg(state); }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "constant"


    public static class stringLiteralSequence_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "stringLiteralSequence"
    // IdentifiersParser.g:231:1: stringLiteralSequence : StringLiteral ( StringLiteral )+ -> ^( TOK_STRINGLITERALSEQUENCE StringLiteral ( StringLiteral )+ ) ;
    public final HiveParser_IdentifiersParser.stringLiteralSequence_return stringLiteralSequence() throws RecognitionException {
        HiveParser_IdentifiersParser.stringLiteralSequence_return retval = new HiveParser_IdentifiersParser.stringLiteralSequence_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token StringLiteral125=null;
        Token StringLiteral126=null;

        CommonTree StringLiteral125_tree=null;
        CommonTree StringLiteral126_tree=null;
        RewriteRuleTokenStream stream_StringLiteral=new RewriteRuleTokenStream(adaptor,"token StringLiteral");

        try {
            // IdentifiersParser.g:232:5: ( StringLiteral ( StringLiteral )+ -> ^( TOK_STRINGLITERALSEQUENCE StringLiteral ( StringLiteral )+ ) )
            // IdentifiersParser.g:233:5: StringLiteral ( StringLiteral )+
            {
            StringLiteral125=(Token)match(input,StringLiteral,FOLLOW_StringLiteral_in_stringLiteralSequence1518); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_StringLiteral.add(StringLiteral125);


            // IdentifiersParser.g:233:19: ( StringLiteral )+
            int cnt31=0;
            loop31:
            do {
                int alt31=2;
                alt31 = dfa31.predict(input);
                switch (alt31) {
            	case 1 :
            	    // IdentifiersParser.g:233:19: StringLiteral
            	    {
            	    StringLiteral126=(Token)match(input,StringLiteral,FOLLOW_StringLiteral_in_stringLiteralSequence1520); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_StringLiteral.add(StringLiteral126);


            	    }
            	    break;

            	default :
            	    if ( cnt31 >= 1 ) break loop31;
            	    if (state.backtracking>0) {state.failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(31, input);
                        throw eee;
                }
                cnt31++;
            } while (true);


            // AST REWRITE
            // elements: StringLiteral, StringLiteral
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 233:34: -> ^( TOK_STRINGLITERALSEQUENCE StringLiteral ( StringLiteral )+ )
            {
                // IdentifiersParser.g:233:37: ^( TOK_STRINGLITERALSEQUENCE StringLiteral ( StringLiteral )+ )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(TOK_STRINGLITERALSEQUENCE, "TOK_STRINGLITERALSEQUENCE")
                , root_1);

                adaptor.addChild(root_1, 
                stream_StringLiteral.nextNode()
                );

                if ( !(stream_StringLiteral.hasNext()) ) {
                    throw new RewriteEarlyExitException();
                }
                while ( stream_StringLiteral.hasNext() ) {
                    adaptor.addChild(root_1, 
                    stream_StringLiteral.nextNode()
                    );

                }
                stream_StringLiteral.reset();

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "stringLiteralSequence"


    public static class charSetStringLiteral_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "charSetStringLiteral"
    // IdentifiersParser.g:236:1: charSetStringLiteral : csName= CharSetName csLiteral= CharSetLiteral -> ^( TOK_CHARSETLITERAL $csName $csLiteral) ;
    public final HiveParser_IdentifiersParser.charSetStringLiteral_return charSetStringLiteral() throws RecognitionException {
        HiveParser_IdentifiersParser.charSetStringLiteral_return retval = new HiveParser_IdentifiersParser.charSetStringLiteral_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token csName=null;
        Token csLiteral=null;

        CommonTree csName_tree=null;
        CommonTree csLiteral_tree=null;
        RewriteRuleTokenStream stream_CharSetLiteral=new RewriteRuleTokenStream(adaptor,"token CharSetLiteral");
        RewriteRuleTokenStream stream_CharSetName=new RewriteRuleTokenStream(adaptor,"token CharSetName");

         gParent.pushMsg("character string literal", state); 
        try {
            // IdentifiersParser.g:239:5: (csName= CharSetName csLiteral= CharSetLiteral -> ^( TOK_CHARSETLITERAL $csName $csLiteral) )
            // IdentifiersParser.g:240:5: csName= CharSetName csLiteral= CharSetLiteral
            {
            csName=(Token)match(input,CharSetName,FOLLOW_CharSetName_in_charSetStringLiteral1565); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_CharSetName.add(csName);


            csLiteral=(Token)match(input,CharSetLiteral,FOLLOW_CharSetLiteral_in_charSetStringLiteral1569); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_CharSetLiteral.add(csLiteral);


            // AST REWRITE
            // elements: csLiteral, csName
            // token labels: csName, csLiteral
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleTokenStream stream_csName=new RewriteRuleTokenStream(adaptor,"token csName",csName);
            RewriteRuleTokenStream stream_csLiteral=new RewriteRuleTokenStream(adaptor,"token csLiteral",csLiteral);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 240:49: -> ^( TOK_CHARSETLITERAL $csName $csLiteral)
            {
                // IdentifiersParser.g:240:52: ^( TOK_CHARSETLITERAL $csName $csLiteral)
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(TOK_CHARSETLITERAL, "TOK_CHARSETLITERAL")
                , root_1);

                adaptor.addChild(root_1, stream_csName.nextNode());

                adaptor.addChild(root_1, stream_csLiteral.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
            if ( state.backtracking==0 ) { gParent.popMsg(state); }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "charSetStringLiteral"


    public static class dateLiteral_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "dateLiteral"
    // IdentifiersParser.g:243:1: dateLiteral : KW_DATE StringLiteral ->;
    public final HiveParser_IdentifiersParser.dateLiteral_return dateLiteral() throws RecognitionException {
        HiveParser_IdentifiersParser.dateLiteral_return retval = new HiveParser_IdentifiersParser.dateLiteral_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token KW_DATE127=null;
        Token StringLiteral128=null;

        CommonTree KW_DATE127_tree=null;
        CommonTree StringLiteral128_tree=null;
        RewriteRuleTokenStream stream_StringLiteral=new RewriteRuleTokenStream(adaptor,"token StringLiteral");
        RewriteRuleTokenStream stream_KW_DATE=new RewriteRuleTokenStream(adaptor,"token KW_DATE");

        try {
            // IdentifiersParser.g:244:5: ( KW_DATE StringLiteral ->)
            // IdentifiersParser.g:245:5: KW_DATE StringLiteral
            {
            KW_DATE127=(Token)match(input,KW_DATE,FOLLOW_KW_DATE_in_dateLiteral1602); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KW_DATE.add(KW_DATE127);


            StringLiteral128=(Token)match(input,StringLiteral,FOLLOW_StringLiteral_in_dateLiteral1604); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_StringLiteral.add(StringLiteral128);


            // AST REWRITE
            // elements: 
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 245:27: ->
            {
                adaptor.addChild(root_0, 
                      // Create DateLiteral token, but with the text of the string value
                      // This makes the dateLiteral more consistent with the other type literals.
                      adaptor.create(TOK_DATELITERAL, (StringLiteral128!=null?StringLiteral128.getText():null))
                    );

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "dateLiteral"


    public static class expression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "expression"
    // IdentifiersParser.g:253:1: expression : precedenceOrExpression ;
    public final HiveParser_IdentifiersParser.expression_return expression() throws RecognitionException {
        HiveParser_IdentifiersParser.expression_return retval = new HiveParser_IdentifiersParser.expression_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        HiveParser_IdentifiersParser.precedenceOrExpression_return precedenceOrExpression129 =null;



         gParent.pushMsg("expression specification", state); 
        try {
            // IdentifiersParser.g:256:5: ( precedenceOrExpression )
            // IdentifiersParser.g:257:5: precedenceOrExpression
            {
            root_0 = (CommonTree)adaptor.nil();


            pushFollow(FOLLOW_precedenceOrExpression_in_expression1643);
            precedenceOrExpression129=precedenceOrExpression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, precedenceOrExpression129.getTree());

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
            if ( state.backtracking==0 ) { gParent.popMsg(state); }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "expression"


    public static class atomExpression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "atomExpression"
    // IdentifiersParser.g:260:1: atomExpression : ( KW_NULL -> TOK_NULL | dateLiteral | constant | castExpression | caseExpression | whenExpression | ( functionName LPAREN )=> function | tableOrColumn | LPAREN ! expression RPAREN !);
    public final HiveParser_IdentifiersParser.atomExpression_return atomExpression() throws RecognitionException {
        HiveParser_IdentifiersParser.atomExpression_return retval = new HiveParser_IdentifiersParser.atomExpression_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token KW_NULL130=null;
        Token LPAREN138=null;
        Token RPAREN140=null;
        HiveParser_IdentifiersParser.dateLiteral_return dateLiteral131 =null;

        HiveParser_IdentifiersParser.constant_return constant132 =null;

        HiveParser_IdentifiersParser.castExpression_return castExpression133 =null;

        HiveParser_IdentifiersParser.caseExpression_return caseExpression134 =null;

        HiveParser_IdentifiersParser.whenExpression_return whenExpression135 =null;

        HiveParser_IdentifiersParser.function_return function136 =null;

        HiveParser_FromClauseParser.tableOrColumn_return tableOrColumn137 =null;

        HiveParser_IdentifiersParser.expression_return expression139 =null;


        CommonTree KW_NULL130_tree=null;
        CommonTree LPAREN138_tree=null;
        CommonTree RPAREN140_tree=null;
        RewriteRuleTokenStream stream_KW_NULL=new RewriteRuleTokenStream(adaptor,"token KW_NULL");

        try {
            // IdentifiersParser.g:261:5: ( KW_NULL -> TOK_NULL | dateLiteral | constant | castExpression | caseExpression | whenExpression | ( functionName LPAREN )=> function | tableOrColumn | LPAREN ! expression RPAREN !)
            int alt32=9;
            alt32 = dfa32.predict(input);
            switch (alt32) {
                case 1 :
                    // IdentifiersParser.g:262:5: KW_NULL
                    {
                    KW_NULL130=(Token)match(input,KW_NULL,FOLLOW_KW_NULL_in_atomExpression1664); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_KW_NULL.add(KW_NULL130);


                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 262:13: -> TOK_NULL
                    {
                        adaptor.addChild(root_0, 
                        (CommonTree)adaptor.create(TOK_NULL, "TOK_NULL")
                        );

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 2 :
                    // IdentifiersParser.g:263:7: dateLiteral
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_dateLiteral_in_atomExpression1676);
                    dateLiteral131=dateLiteral();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, dateLiteral131.getTree());

                    }
                    break;
                case 3 :
                    // IdentifiersParser.g:264:7: constant
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_constant_in_atomExpression1684);
                    constant132=constant();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, constant132.getTree());

                    }
                    break;
                case 4 :
                    // IdentifiersParser.g:265:7: castExpression
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_castExpression_in_atomExpression1692);
                    castExpression133=castExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, castExpression133.getTree());

                    }
                    break;
                case 5 :
                    // IdentifiersParser.g:266:7: caseExpression
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_caseExpression_in_atomExpression1700);
                    caseExpression134=caseExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, caseExpression134.getTree());

                    }
                    break;
                case 6 :
                    // IdentifiersParser.g:267:7: whenExpression
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_whenExpression_in_atomExpression1708);
                    whenExpression135=whenExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, whenExpression135.getTree());

                    }
                    break;
                case 7 :
                    // IdentifiersParser.g:268:7: ( functionName LPAREN )=> function
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_function_in_atomExpression1724);
                    function136=function();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, function136.getTree());

                    }
                    break;
                case 8 :
                    // IdentifiersParser.g:269:7: tableOrColumn
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_tableOrColumn_in_atomExpression1732);
                    tableOrColumn137=gHiveParser.tableOrColumn();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, tableOrColumn137.getTree());

                    }
                    break;
                case 9 :
                    // IdentifiersParser.g:270:7: LPAREN ! expression RPAREN !
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    LPAREN138=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_atomExpression1740); if (state.failed) return retval;

                    pushFollow(FOLLOW_expression_in_atomExpression1743);
                    expression139=expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, expression139.getTree());

                    RPAREN140=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_atomExpression1745); if (state.failed) return retval;

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "atomExpression"


    public static class precedenceFieldExpression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "precedenceFieldExpression"
    // IdentifiersParser.g:274:1: precedenceFieldExpression : atomExpression ( ( LSQUARE ^ expression RSQUARE !) | ( DOT ^ identifier ) )* ;
    public final HiveParser_IdentifiersParser.precedenceFieldExpression_return precedenceFieldExpression() throws RecognitionException {
        HiveParser_IdentifiersParser.precedenceFieldExpression_return retval = new HiveParser_IdentifiersParser.precedenceFieldExpression_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token LSQUARE142=null;
        Token RSQUARE144=null;
        Token DOT145=null;
        HiveParser_IdentifiersParser.atomExpression_return atomExpression141 =null;

        HiveParser_IdentifiersParser.expression_return expression143 =null;

        HiveParser_IdentifiersParser.identifier_return identifier146 =null;


        CommonTree LSQUARE142_tree=null;
        CommonTree RSQUARE144_tree=null;
        CommonTree DOT145_tree=null;

        try {
            // IdentifiersParser.g:275:5: ( atomExpression ( ( LSQUARE ^ expression RSQUARE !) | ( DOT ^ identifier ) )* )
            // IdentifiersParser.g:276:5: atomExpression ( ( LSQUARE ^ expression RSQUARE !) | ( DOT ^ identifier ) )*
            {
            root_0 = (CommonTree)adaptor.nil();


            pushFollow(FOLLOW_atomExpression_in_precedenceFieldExpression1768);
            atomExpression141=atomExpression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, atomExpression141.getTree());

            // IdentifiersParser.g:276:20: ( ( LSQUARE ^ expression RSQUARE !) | ( DOT ^ identifier ) )*
            loop33:
            do {
                int alt33=3;
                alt33 = dfa33.predict(input);
                switch (alt33) {
            	case 1 :
            	    // IdentifiersParser.g:276:21: ( LSQUARE ^ expression RSQUARE !)
            	    {
            	    // IdentifiersParser.g:276:21: ( LSQUARE ^ expression RSQUARE !)
            	    // IdentifiersParser.g:276:22: LSQUARE ^ expression RSQUARE !
            	    {
            	    LSQUARE142=(Token)match(input,LSQUARE,FOLLOW_LSQUARE_in_precedenceFieldExpression1772); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    LSQUARE142_tree = 
            	    (CommonTree)adaptor.create(LSQUARE142)
            	    ;
            	    root_0 = (CommonTree)adaptor.becomeRoot(LSQUARE142_tree, root_0);
            	    }

            	    pushFollow(FOLLOW_expression_in_precedenceFieldExpression1775);
            	    expression143=expression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, expression143.getTree());

            	    RSQUARE144=(Token)match(input,RSQUARE,FOLLOW_RSQUARE_in_precedenceFieldExpression1777); if (state.failed) return retval;

            	    }


            	    }
            	    break;
            	case 2 :
            	    // IdentifiersParser.g:276:54: ( DOT ^ identifier )
            	    {
            	    // IdentifiersParser.g:276:54: ( DOT ^ identifier )
            	    // IdentifiersParser.g:276:55: DOT ^ identifier
            	    {
            	    DOT145=(Token)match(input,DOT,FOLLOW_DOT_in_precedenceFieldExpression1784); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    DOT145_tree = 
            	    (CommonTree)adaptor.create(DOT145)
            	    ;
            	    root_0 = (CommonTree)adaptor.becomeRoot(DOT145_tree, root_0);
            	    }

            	    pushFollow(FOLLOW_identifier_in_precedenceFieldExpression1787);
            	    identifier146=identifier();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, identifier146.getTree());

            	    }


            	    }
            	    break;

            	default :
            	    break loop33;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "precedenceFieldExpression"


    public static class precedenceUnaryOperator_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "precedenceUnaryOperator"
    // IdentifiersParser.g:279:1: precedenceUnaryOperator : ( PLUS | MINUS | TILDE );
    public final HiveParser_IdentifiersParser.precedenceUnaryOperator_return precedenceUnaryOperator() throws RecognitionException {
        HiveParser_IdentifiersParser.precedenceUnaryOperator_return retval = new HiveParser_IdentifiersParser.precedenceUnaryOperator_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token set147=null;

        CommonTree set147_tree=null;

        try {
            // IdentifiersParser.g:280:5: ( PLUS | MINUS | TILDE )
            // IdentifiersParser.g:
            {
            root_0 = (CommonTree)adaptor.nil();


            set147=(Token)input.LT(1);

            if ( input.LA(1)==MINUS||input.LA(1)==PLUS||input.LA(1)==TILDE ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, 
                (CommonTree)adaptor.create(set147)
                );
                state.errorRecovery=false;
                state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "precedenceUnaryOperator"


    public static class nullCondition_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "nullCondition"
    // IdentifiersParser.g:284:1: nullCondition : ( KW_NULL -> ^( TOK_ISNULL ) | KW_NOT KW_NULL -> ^( TOK_ISNOTNULL ) );
    public final HiveParser_IdentifiersParser.nullCondition_return nullCondition() throws RecognitionException {
        HiveParser_IdentifiersParser.nullCondition_return retval = new HiveParser_IdentifiersParser.nullCondition_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token KW_NULL148=null;
        Token KW_NOT149=null;
        Token KW_NULL150=null;

        CommonTree KW_NULL148_tree=null;
        CommonTree KW_NOT149_tree=null;
        CommonTree KW_NULL150_tree=null;
        RewriteRuleTokenStream stream_KW_NULL=new RewriteRuleTokenStream(adaptor,"token KW_NULL");
        RewriteRuleTokenStream stream_KW_NOT=new RewriteRuleTokenStream(adaptor,"token KW_NOT");

        try {
            // IdentifiersParser.g:285:5: ( KW_NULL -> ^( TOK_ISNULL ) | KW_NOT KW_NULL -> ^( TOK_ISNOTNULL ) )
            int alt34=2;
            switch ( input.LA(1) ) {
            case KW_NULL:
                {
                alt34=1;
                }
                break;
            case KW_NOT:
                {
                alt34=2;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 34, 0, input);

                throw nvae;

            }

            switch (alt34) {
                case 1 :
                    // IdentifiersParser.g:286:5: KW_NULL
                    {
                    KW_NULL148=(Token)match(input,KW_NULL,FOLLOW_KW_NULL_in_nullCondition1840); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_KW_NULL.add(KW_NULL148);


                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 286:13: -> ^( TOK_ISNULL )
                    {
                        // IdentifiersParser.g:286:16: ^( TOK_ISNULL )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(TOK_ISNULL, "TOK_ISNULL")
                        , root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 2 :
                    // IdentifiersParser.g:287:7: KW_NOT KW_NULL
                    {
                    KW_NOT149=(Token)match(input,KW_NOT,FOLLOW_KW_NOT_in_nullCondition1854); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_KW_NOT.add(KW_NOT149);


                    KW_NULL150=(Token)match(input,KW_NULL,FOLLOW_KW_NULL_in_nullCondition1856); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_KW_NULL.add(KW_NULL150);


                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 287:22: -> ^( TOK_ISNOTNULL )
                    {
                        // IdentifiersParser.g:287:25: ^( TOK_ISNOTNULL )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(TOK_ISNOTNULL, "TOK_ISNOTNULL")
                        , root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "nullCondition"


    public static class precedenceUnaryPrefixExpression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "precedenceUnaryPrefixExpression"
    // IdentifiersParser.g:290:1: precedenceUnaryPrefixExpression : ( precedenceUnaryOperator ^)* precedenceFieldExpression ;
    public final HiveParser_IdentifiersParser.precedenceUnaryPrefixExpression_return precedenceUnaryPrefixExpression() throws RecognitionException {
        HiveParser_IdentifiersParser.precedenceUnaryPrefixExpression_return retval = new HiveParser_IdentifiersParser.precedenceUnaryPrefixExpression_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        HiveParser_IdentifiersParser.precedenceUnaryOperator_return precedenceUnaryOperator151 =null;

        HiveParser_IdentifiersParser.precedenceFieldExpression_return precedenceFieldExpression152 =null;



        try {
            // IdentifiersParser.g:291:5: ( ( precedenceUnaryOperator ^)* precedenceFieldExpression )
            // IdentifiersParser.g:292:5: ( precedenceUnaryOperator ^)* precedenceFieldExpression
            {
            root_0 = (CommonTree)adaptor.nil();


            // IdentifiersParser.g:292:5: ( precedenceUnaryOperator ^)*
            loop35:
            do {
                int alt35=2;
                switch ( input.LA(1) ) {
                case MINUS:
                case PLUS:
                case TILDE:
                    {
                    alt35=1;
                    }
                    break;

                }

                switch (alt35) {
            	case 1 :
            	    // IdentifiersParser.g:292:6: precedenceUnaryOperator ^
            	    {
            	    pushFollow(FOLLOW_precedenceUnaryOperator_in_precedenceUnaryPrefixExpression1884);
            	    precedenceUnaryOperator151=precedenceUnaryOperator();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) root_0 = (CommonTree)adaptor.becomeRoot(precedenceUnaryOperator151.getTree(), root_0);

            	    }
            	    break;

            	default :
            	    break loop35;
                }
            } while (true);


            pushFollow(FOLLOW_precedenceFieldExpression_in_precedenceUnaryPrefixExpression1889);
            precedenceFieldExpression152=precedenceFieldExpression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, precedenceFieldExpression152.getTree());

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "precedenceUnaryPrefixExpression"


    public static class precedenceUnarySuffixExpression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "precedenceUnarySuffixExpression"
    // IdentifiersParser.g:295:1: precedenceUnarySuffixExpression : precedenceUnaryPrefixExpression (a= KW_IS nullCondition )? -> {$a != null}? ^( TOK_FUNCTION nullCondition precedenceUnaryPrefixExpression ) -> precedenceUnaryPrefixExpression ;
    public final HiveParser_IdentifiersParser.precedenceUnarySuffixExpression_return precedenceUnarySuffixExpression() throws RecognitionException {
        HiveParser_IdentifiersParser.precedenceUnarySuffixExpression_return retval = new HiveParser_IdentifiersParser.precedenceUnarySuffixExpression_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token a=null;
        HiveParser_IdentifiersParser.precedenceUnaryPrefixExpression_return precedenceUnaryPrefixExpression153 =null;

        HiveParser_IdentifiersParser.nullCondition_return nullCondition154 =null;


        CommonTree a_tree=null;
        RewriteRuleTokenStream stream_KW_IS=new RewriteRuleTokenStream(adaptor,"token KW_IS");
        RewriteRuleSubtreeStream stream_precedenceUnaryPrefixExpression=new RewriteRuleSubtreeStream(adaptor,"rule precedenceUnaryPrefixExpression");
        RewriteRuleSubtreeStream stream_nullCondition=new RewriteRuleSubtreeStream(adaptor,"rule nullCondition");
        try {
            // IdentifiersParser.g:296:5: ( precedenceUnaryPrefixExpression (a= KW_IS nullCondition )? -> {$a != null}? ^( TOK_FUNCTION nullCondition precedenceUnaryPrefixExpression ) -> precedenceUnaryPrefixExpression )
            // IdentifiersParser.g:296:7: precedenceUnaryPrefixExpression (a= KW_IS nullCondition )?
            {
            pushFollow(FOLLOW_precedenceUnaryPrefixExpression_in_precedenceUnarySuffixExpression1906);
            precedenceUnaryPrefixExpression153=precedenceUnaryPrefixExpression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_precedenceUnaryPrefixExpression.add(precedenceUnaryPrefixExpression153.getTree());

            // IdentifiersParser.g:296:39: (a= KW_IS nullCondition )?
            int alt36=2;
            alt36 = dfa36.predict(input);
            switch (alt36) {
                case 1 :
                    // IdentifiersParser.g:296:40: a= KW_IS nullCondition
                    {
                    a=(Token)match(input,KW_IS,FOLLOW_KW_IS_in_precedenceUnarySuffixExpression1911); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_KW_IS.add(a);


                    pushFollow(FOLLOW_nullCondition_in_precedenceUnarySuffixExpression1913);
                    nullCondition154=nullCondition();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_nullCondition.add(nullCondition154.getTree());

                    }
                    break;

            }


            // AST REWRITE
            // elements: precedenceUnaryPrefixExpression, nullCondition, precedenceUnaryPrefixExpression
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 297:5: -> {$a != null}? ^( TOK_FUNCTION nullCondition precedenceUnaryPrefixExpression )
            if (a != null) {
                // IdentifiersParser.g:297:22: ^( TOK_FUNCTION nullCondition precedenceUnaryPrefixExpression )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(TOK_FUNCTION, "TOK_FUNCTION")
                , root_1);

                adaptor.addChild(root_1, stream_nullCondition.nextTree());

                adaptor.addChild(root_1, stream_precedenceUnaryPrefixExpression.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            else // 298:5: -> precedenceUnaryPrefixExpression
            {
                adaptor.addChild(root_0, stream_precedenceUnaryPrefixExpression.nextTree());

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "precedenceUnarySuffixExpression"


    public static class precedenceBitwiseXorOperator_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "precedenceBitwiseXorOperator"
    // IdentifiersParser.g:302:1: precedenceBitwiseXorOperator : BITWISEXOR ;
    public final HiveParser_IdentifiersParser.precedenceBitwiseXorOperator_return precedenceBitwiseXorOperator() throws RecognitionException {
        HiveParser_IdentifiersParser.precedenceBitwiseXorOperator_return retval = new HiveParser_IdentifiersParser.precedenceBitwiseXorOperator_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token BITWISEXOR155=null;

        CommonTree BITWISEXOR155_tree=null;

        try {
            // IdentifiersParser.g:303:5: ( BITWISEXOR )
            // IdentifiersParser.g:304:5: BITWISEXOR
            {
            root_0 = (CommonTree)adaptor.nil();


            BITWISEXOR155=(Token)match(input,BITWISEXOR,FOLLOW_BITWISEXOR_in_precedenceBitwiseXorOperator1961); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BITWISEXOR155_tree = 
            (CommonTree)adaptor.create(BITWISEXOR155)
            ;
            adaptor.addChild(root_0, BITWISEXOR155_tree);
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "precedenceBitwiseXorOperator"


    public static class precedenceBitwiseXorExpression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "precedenceBitwiseXorExpression"
    // IdentifiersParser.g:307:1: precedenceBitwiseXorExpression : precedenceUnarySuffixExpression ( precedenceBitwiseXorOperator ^ precedenceUnarySuffixExpression )* ;
    public final HiveParser_IdentifiersParser.precedenceBitwiseXorExpression_return precedenceBitwiseXorExpression() throws RecognitionException {
        HiveParser_IdentifiersParser.precedenceBitwiseXorExpression_return retval = new HiveParser_IdentifiersParser.precedenceBitwiseXorExpression_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        HiveParser_IdentifiersParser.precedenceUnarySuffixExpression_return precedenceUnarySuffixExpression156 =null;

        HiveParser_IdentifiersParser.precedenceBitwiseXorOperator_return precedenceBitwiseXorOperator157 =null;

        HiveParser_IdentifiersParser.precedenceUnarySuffixExpression_return precedenceUnarySuffixExpression158 =null;



        try {
            // IdentifiersParser.g:308:5: ( precedenceUnarySuffixExpression ( precedenceBitwiseXorOperator ^ precedenceUnarySuffixExpression )* )
            // IdentifiersParser.g:309:5: precedenceUnarySuffixExpression ( precedenceBitwiseXorOperator ^ precedenceUnarySuffixExpression )*
            {
            root_0 = (CommonTree)adaptor.nil();


            pushFollow(FOLLOW_precedenceUnarySuffixExpression_in_precedenceBitwiseXorExpression1982);
            precedenceUnarySuffixExpression156=precedenceUnarySuffixExpression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, precedenceUnarySuffixExpression156.getTree());

            // IdentifiersParser.g:309:37: ( precedenceBitwiseXorOperator ^ precedenceUnarySuffixExpression )*
            loop37:
            do {
                int alt37=2;
                alt37 = dfa37.predict(input);
                switch (alt37) {
            	case 1 :
            	    // IdentifiersParser.g:309:38: precedenceBitwiseXorOperator ^ precedenceUnarySuffixExpression
            	    {
            	    pushFollow(FOLLOW_precedenceBitwiseXorOperator_in_precedenceBitwiseXorExpression1985);
            	    precedenceBitwiseXorOperator157=precedenceBitwiseXorOperator();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) root_0 = (CommonTree)adaptor.becomeRoot(precedenceBitwiseXorOperator157.getTree(), root_0);

            	    pushFollow(FOLLOW_precedenceUnarySuffixExpression_in_precedenceBitwiseXorExpression1988);
            	    precedenceUnarySuffixExpression158=precedenceUnarySuffixExpression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, precedenceUnarySuffixExpression158.getTree());

            	    }
            	    break;

            	default :
            	    break loop37;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "precedenceBitwiseXorExpression"


    public static class precedenceStarOperator_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "precedenceStarOperator"
    // IdentifiersParser.g:313:1: precedenceStarOperator : ( STAR | DIVIDE | MOD | DIV );
    public final HiveParser_IdentifiersParser.precedenceStarOperator_return precedenceStarOperator() throws RecognitionException {
        HiveParser_IdentifiersParser.precedenceStarOperator_return retval = new HiveParser_IdentifiersParser.precedenceStarOperator_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token set159=null;

        CommonTree set159_tree=null;

        try {
            // IdentifiersParser.g:314:5: ( STAR | DIVIDE | MOD | DIV )
            // IdentifiersParser.g:
            {
            root_0 = (CommonTree)adaptor.nil();


            set159=(Token)input.LT(1);

            if ( (input.LA(1) >= DIV && input.LA(1) <= DIVIDE)||input.LA(1)==MOD||input.LA(1)==STAR ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, 
                (CommonTree)adaptor.create(set159)
                );
                state.errorRecovery=false;
                state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "precedenceStarOperator"


    public static class precedenceStarExpression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "precedenceStarExpression"
    // IdentifiersParser.g:318:1: precedenceStarExpression : precedenceBitwiseXorExpression ( precedenceStarOperator ^ precedenceBitwiseXorExpression )* ;
    public final HiveParser_IdentifiersParser.precedenceStarExpression_return precedenceStarExpression() throws RecognitionException {
        HiveParser_IdentifiersParser.precedenceStarExpression_return retval = new HiveParser_IdentifiersParser.precedenceStarExpression_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        HiveParser_IdentifiersParser.precedenceBitwiseXorExpression_return precedenceBitwiseXorExpression160 =null;

        HiveParser_IdentifiersParser.precedenceStarOperator_return precedenceStarOperator161 =null;

        HiveParser_IdentifiersParser.precedenceBitwiseXorExpression_return precedenceBitwiseXorExpression162 =null;



        try {
            // IdentifiersParser.g:319:5: ( precedenceBitwiseXorExpression ( precedenceStarOperator ^ precedenceBitwiseXorExpression )* )
            // IdentifiersParser.g:320:5: precedenceBitwiseXorExpression ( precedenceStarOperator ^ precedenceBitwiseXorExpression )*
            {
            root_0 = (CommonTree)adaptor.nil();


            pushFollow(FOLLOW_precedenceBitwiseXorExpression_in_precedenceStarExpression2045);
            precedenceBitwiseXorExpression160=precedenceBitwiseXorExpression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, precedenceBitwiseXorExpression160.getTree());

            // IdentifiersParser.g:320:36: ( precedenceStarOperator ^ precedenceBitwiseXorExpression )*
            loop38:
            do {
                int alt38=2;
                alt38 = dfa38.predict(input);
                switch (alt38) {
            	case 1 :
            	    // IdentifiersParser.g:320:37: precedenceStarOperator ^ precedenceBitwiseXorExpression
            	    {
            	    pushFollow(FOLLOW_precedenceStarOperator_in_precedenceStarExpression2048);
            	    precedenceStarOperator161=precedenceStarOperator();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) root_0 = (CommonTree)adaptor.becomeRoot(precedenceStarOperator161.getTree(), root_0);

            	    pushFollow(FOLLOW_precedenceBitwiseXorExpression_in_precedenceStarExpression2051);
            	    precedenceBitwiseXorExpression162=precedenceBitwiseXorExpression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, precedenceBitwiseXorExpression162.getTree());

            	    }
            	    break;

            	default :
            	    break loop38;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "precedenceStarExpression"


    public static class precedencePlusOperator_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "precedencePlusOperator"
    // IdentifiersParser.g:324:1: precedencePlusOperator : ( PLUS | MINUS );
    public final HiveParser_IdentifiersParser.precedencePlusOperator_return precedencePlusOperator() throws RecognitionException {
        HiveParser_IdentifiersParser.precedencePlusOperator_return retval = new HiveParser_IdentifiersParser.precedencePlusOperator_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token set163=null;

        CommonTree set163_tree=null;

        try {
            // IdentifiersParser.g:325:5: ( PLUS | MINUS )
            // IdentifiersParser.g:
            {
            root_0 = (CommonTree)adaptor.nil();


            set163=(Token)input.LT(1);

            if ( input.LA(1)==MINUS||input.LA(1)==PLUS ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, 
                (CommonTree)adaptor.create(set163)
                );
                state.errorRecovery=false;
                state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "precedencePlusOperator"


    public static class precedencePlusExpression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "precedencePlusExpression"
    // IdentifiersParser.g:329:1: precedencePlusExpression : precedenceStarExpression ( precedencePlusOperator ^ precedenceStarExpression )* ;
    public final HiveParser_IdentifiersParser.precedencePlusExpression_return precedencePlusExpression() throws RecognitionException {
        HiveParser_IdentifiersParser.precedencePlusExpression_return retval = new HiveParser_IdentifiersParser.precedencePlusExpression_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        HiveParser_IdentifiersParser.precedenceStarExpression_return precedenceStarExpression164 =null;

        HiveParser_IdentifiersParser.precedencePlusOperator_return precedencePlusOperator165 =null;

        HiveParser_IdentifiersParser.precedenceStarExpression_return precedenceStarExpression166 =null;



        try {
            // IdentifiersParser.g:330:5: ( precedenceStarExpression ( precedencePlusOperator ^ precedenceStarExpression )* )
            // IdentifiersParser.g:331:5: precedenceStarExpression ( precedencePlusOperator ^ precedenceStarExpression )*
            {
            root_0 = (CommonTree)adaptor.nil();


            pushFollow(FOLLOW_precedenceStarExpression_in_precedencePlusExpression2100);
            precedenceStarExpression164=precedenceStarExpression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, precedenceStarExpression164.getTree());

            // IdentifiersParser.g:331:30: ( precedencePlusOperator ^ precedenceStarExpression )*
            loop39:
            do {
                int alt39=2;
                switch ( input.LA(1) ) {
                case MINUS:
                case PLUS:
                    {
                    alt39=1;
                    }
                    break;

                }

                switch (alt39) {
            	case 1 :
            	    // IdentifiersParser.g:331:31: precedencePlusOperator ^ precedenceStarExpression
            	    {
            	    pushFollow(FOLLOW_precedencePlusOperator_in_precedencePlusExpression2103);
            	    precedencePlusOperator165=precedencePlusOperator();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) root_0 = (CommonTree)adaptor.becomeRoot(precedencePlusOperator165.getTree(), root_0);

            	    pushFollow(FOLLOW_precedenceStarExpression_in_precedencePlusExpression2106);
            	    precedenceStarExpression166=precedenceStarExpression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, precedenceStarExpression166.getTree());

            	    }
            	    break;

            	default :
            	    break loop39;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "precedencePlusExpression"


    public static class precedenceAmpersandOperator_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "precedenceAmpersandOperator"
    // IdentifiersParser.g:335:1: precedenceAmpersandOperator : AMPERSAND ;
    public final HiveParser_IdentifiersParser.precedenceAmpersandOperator_return precedenceAmpersandOperator() throws RecognitionException {
        HiveParser_IdentifiersParser.precedenceAmpersandOperator_return retval = new HiveParser_IdentifiersParser.precedenceAmpersandOperator_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token AMPERSAND167=null;

        CommonTree AMPERSAND167_tree=null;

        try {
            // IdentifiersParser.g:336:5: ( AMPERSAND )
            // IdentifiersParser.g:337:5: AMPERSAND
            {
            root_0 = (CommonTree)adaptor.nil();


            AMPERSAND167=(Token)match(input,AMPERSAND,FOLLOW_AMPERSAND_in_precedenceAmpersandOperator2130); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            AMPERSAND167_tree = 
            (CommonTree)adaptor.create(AMPERSAND167)
            ;
            adaptor.addChild(root_0, AMPERSAND167_tree);
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "precedenceAmpersandOperator"


    public static class precedenceAmpersandExpression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "precedenceAmpersandExpression"
    // IdentifiersParser.g:340:1: precedenceAmpersandExpression : precedencePlusExpression ( precedenceAmpersandOperator ^ precedencePlusExpression )* ;
    public final HiveParser_IdentifiersParser.precedenceAmpersandExpression_return precedenceAmpersandExpression() throws RecognitionException {
        HiveParser_IdentifiersParser.precedenceAmpersandExpression_return retval = new HiveParser_IdentifiersParser.precedenceAmpersandExpression_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        HiveParser_IdentifiersParser.precedencePlusExpression_return precedencePlusExpression168 =null;

        HiveParser_IdentifiersParser.precedenceAmpersandOperator_return precedenceAmpersandOperator169 =null;

        HiveParser_IdentifiersParser.precedencePlusExpression_return precedencePlusExpression170 =null;



        try {
            // IdentifiersParser.g:341:5: ( precedencePlusExpression ( precedenceAmpersandOperator ^ precedencePlusExpression )* )
            // IdentifiersParser.g:342:5: precedencePlusExpression ( precedenceAmpersandOperator ^ precedencePlusExpression )*
            {
            root_0 = (CommonTree)adaptor.nil();


            pushFollow(FOLLOW_precedencePlusExpression_in_precedenceAmpersandExpression2151);
            precedencePlusExpression168=precedencePlusExpression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, precedencePlusExpression168.getTree());

            // IdentifiersParser.g:342:30: ( precedenceAmpersandOperator ^ precedencePlusExpression )*
            loop40:
            do {
                int alt40=2;
                switch ( input.LA(1) ) {
                case AMPERSAND:
                    {
                    alt40=1;
                    }
                    break;

                }

                switch (alt40) {
            	case 1 :
            	    // IdentifiersParser.g:342:31: precedenceAmpersandOperator ^ precedencePlusExpression
            	    {
            	    pushFollow(FOLLOW_precedenceAmpersandOperator_in_precedenceAmpersandExpression2154);
            	    precedenceAmpersandOperator169=precedenceAmpersandOperator();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) root_0 = (CommonTree)adaptor.becomeRoot(precedenceAmpersandOperator169.getTree(), root_0);

            	    pushFollow(FOLLOW_precedencePlusExpression_in_precedenceAmpersandExpression2157);
            	    precedencePlusExpression170=precedencePlusExpression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, precedencePlusExpression170.getTree());

            	    }
            	    break;

            	default :
            	    break loop40;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "precedenceAmpersandExpression"


    public static class precedenceBitwiseOrOperator_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "precedenceBitwiseOrOperator"
    // IdentifiersParser.g:346:1: precedenceBitwiseOrOperator : BITWISEOR ;
    public final HiveParser_IdentifiersParser.precedenceBitwiseOrOperator_return precedenceBitwiseOrOperator() throws RecognitionException {
        HiveParser_IdentifiersParser.precedenceBitwiseOrOperator_return retval = new HiveParser_IdentifiersParser.precedenceBitwiseOrOperator_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token BITWISEOR171=null;

        CommonTree BITWISEOR171_tree=null;

        try {
            // IdentifiersParser.g:347:5: ( BITWISEOR )
            // IdentifiersParser.g:348:5: BITWISEOR
            {
            root_0 = (CommonTree)adaptor.nil();


            BITWISEOR171=(Token)match(input,BITWISEOR,FOLLOW_BITWISEOR_in_precedenceBitwiseOrOperator2181); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BITWISEOR171_tree = 
            (CommonTree)adaptor.create(BITWISEOR171)
            ;
            adaptor.addChild(root_0, BITWISEOR171_tree);
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "precedenceBitwiseOrOperator"


    public static class precedenceBitwiseOrExpression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "precedenceBitwiseOrExpression"
    // IdentifiersParser.g:351:1: precedenceBitwiseOrExpression : precedenceAmpersandExpression ( precedenceBitwiseOrOperator ^ precedenceAmpersandExpression )* ;
    public final HiveParser_IdentifiersParser.precedenceBitwiseOrExpression_return precedenceBitwiseOrExpression() throws RecognitionException {
        HiveParser_IdentifiersParser.precedenceBitwiseOrExpression_return retval = new HiveParser_IdentifiersParser.precedenceBitwiseOrExpression_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        HiveParser_IdentifiersParser.precedenceAmpersandExpression_return precedenceAmpersandExpression172 =null;

        HiveParser_IdentifiersParser.precedenceBitwiseOrOperator_return precedenceBitwiseOrOperator173 =null;

        HiveParser_IdentifiersParser.precedenceAmpersandExpression_return precedenceAmpersandExpression174 =null;



        try {
            // IdentifiersParser.g:352:5: ( precedenceAmpersandExpression ( precedenceBitwiseOrOperator ^ precedenceAmpersandExpression )* )
            // IdentifiersParser.g:353:5: precedenceAmpersandExpression ( precedenceBitwiseOrOperator ^ precedenceAmpersandExpression )*
            {
            root_0 = (CommonTree)adaptor.nil();


            pushFollow(FOLLOW_precedenceAmpersandExpression_in_precedenceBitwiseOrExpression2202);
            precedenceAmpersandExpression172=precedenceAmpersandExpression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, precedenceAmpersandExpression172.getTree());

            // IdentifiersParser.g:353:35: ( precedenceBitwiseOrOperator ^ precedenceAmpersandExpression )*
            loop41:
            do {
                int alt41=2;
                switch ( input.LA(1) ) {
                case BITWISEOR:
                    {
                    alt41=1;
                    }
                    break;

                }

                switch (alt41) {
            	case 1 :
            	    // IdentifiersParser.g:353:36: precedenceBitwiseOrOperator ^ precedenceAmpersandExpression
            	    {
            	    pushFollow(FOLLOW_precedenceBitwiseOrOperator_in_precedenceBitwiseOrExpression2205);
            	    precedenceBitwiseOrOperator173=precedenceBitwiseOrOperator();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) root_0 = (CommonTree)adaptor.becomeRoot(precedenceBitwiseOrOperator173.getTree(), root_0);

            	    pushFollow(FOLLOW_precedenceAmpersandExpression_in_precedenceBitwiseOrExpression2208);
            	    precedenceAmpersandExpression174=precedenceAmpersandExpression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, precedenceAmpersandExpression174.getTree());

            	    }
            	    break;

            	default :
            	    break loop41;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "precedenceBitwiseOrExpression"


    public static class precedenceEqualNegatableOperator_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "precedenceEqualNegatableOperator"
    // IdentifiersParser.g:358:1: precedenceEqualNegatableOperator : ( KW_LIKE | KW_RLIKE | KW_REGEXP );
    public final HiveParser_IdentifiersParser.precedenceEqualNegatableOperator_return precedenceEqualNegatableOperator() throws RecognitionException {
        HiveParser_IdentifiersParser.precedenceEqualNegatableOperator_return retval = new HiveParser_IdentifiersParser.precedenceEqualNegatableOperator_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token set175=null;

        CommonTree set175_tree=null;

        try {
            // IdentifiersParser.g:359:5: ( KW_LIKE | KW_RLIKE | KW_REGEXP )
            // IdentifiersParser.g:
            {
            root_0 = (CommonTree)adaptor.nil();


            set175=(Token)input.LT(1);

            if ( input.LA(1)==KW_LIKE||input.LA(1)==KW_REGEXP||input.LA(1)==KW_RLIKE ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, 
                (CommonTree)adaptor.create(set175)
                );
                state.errorRecovery=false;
                state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "precedenceEqualNegatableOperator"


    public static class precedenceEqualOperator_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "precedenceEqualOperator"
    // IdentifiersParser.g:363:1: precedenceEqualOperator : ( precedenceEqualNegatableOperator | EQUAL | EQUAL_NS | NOTEQUAL | LESSTHANOREQUALTO | LESSTHAN | GREATERTHANOREQUALTO | GREATERTHAN );
    public final HiveParser_IdentifiersParser.precedenceEqualOperator_return precedenceEqualOperator() throws RecognitionException {
        HiveParser_IdentifiersParser.precedenceEqualOperator_return retval = new HiveParser_IdentifiersParser.precedenceEqualOperator_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token EQUAL177=null;
        Token EQUAL_NS178=null;
        Token NOTEQUAL179=null;
        Token LESSTHANOREQUALTO180=null;
        Token LESSTHAN181=null;
        Token GREATERTHANOREQUALTO182=null;
        Token GREATERTHAN183=null;
        HiveParser_IdentifiersParser.precedenceEqualNegatableOperator_return precedenceEqualNegatableOperator176 =null;


        CommonTree EQUAL177_tree=null;
        CommonTree EQUAL_NS178_tree=null;
        CommonTree NOTEQUAL179_tree=null;
        CommonTree LESSTHANOREQUALTO180_tree=null;
        CommonTree LESSTHAN181_tree=null;
        CommonTree GREATERTHANOREQUALTO182_tree=null;
        CommonTree GREATERTHAN183_tree=null;

        try {
            // IdentifiersParser.g:364:5: ( precedenceEqualNegatableOperator | EQUAL | EQUAL_NS | NOTEQUAL | LESSTHANOREQUALTO | LESSTHAN | GREATERTHANOREQUALTO | GREATERTHAN )
            int alt42=8;
            switch ( input.LA(1) ) {
            case KW_LIKE:
            case KW_REGEXP:
            case KW_RLIKE:
                {
                alt42=1;
                }
                break;
            case EQUAL:
                {
                alt42=2;
                }
                break;
            case EQUAL_NS:
                {
                alt42=3;
                }
                break;
            case NOTEQUAL:
                {
                alt42=4;
                }
                break;
            case LESSTHANOREQUALTO:
                {
                alt42=5;
                }
                break;
            case LESSTHAN:
                {
                alt42=6;
                }
                break;
            case GREATERTHANOREQUALTO:
                {
                alt42=7;
                }
                break;
            case GREATERTHAN:
                {
                alt42=8;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 42, 0, input);

                throw nvae;

            }

            switch (alt42) {
                case 1 :
                    // IdentifiersParser.g:365:5: precedenceEqualNegatableOperator
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_precedenceEqualNegatableOperator_in_precedenceEqualOperator2262);
                    precedenceEqualNegatableOperator176=precedenceEqualNegatableOperator();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, precedenceEqualNegatableOperator176.getTree());

                    }
                    break;
                case 2 :
                    // IdentifiersParser.g:365:40: EQUAL
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    EQUAL177=(Token)match(input,EQUAL,FOLLOW_EQUAL_in_precedenceEqualOperator2266); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    EQUAL177_tree = 
                    (CommonTree)adaptor.create(EQUAL177)
                    ;
                    adaptor.addChild(root_0, EQUAL177_tree);
                    }

                    }
                    break;
                case 3 :
                    // IdentifiersParser.g:365:48: EQUAL_NS
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    EQUAL_NS178=(Token)match(input,EQUAL_NS,FOLLOW_EQUAL_NS_in_precedenceEqualOperator2270); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    EQUAL_NS178_tree = 
                    (CommonTree)adaptor.create(EQUAL_NS178)
                    ;
                    adaptor.addChild(root_0, EQUAL_NS178_tree);
                    }

                    }
                    break;
                case 4 :
                    // IdentifiersParser.g:365:59: NOTEQUAL
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    NOTEQUAL179=(Token)match(input,NOTEQUAL,FOLLOW_NOTEQUAL_in_precedenceEqualOperator2274); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NOTEQUAL179_tree = 
                    (CommonTree)adaptor.create(NOTEQUAL179)
                    ;
                    adaptor.addChild(root_0, NOTEQUAL179_tree);
                    }

                    }
                    break;
                case 5 :
                    // IdentifiersParser.g:365:70: LESSTHANOREQUALTO
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    LESSTHANOREQUALTO180=(Token)match(input,LESSTHANOREQUALTO,FOLLOW_LESSTHANOREQUALTO_in_precedenceEqualOperator2278); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    LESSTHANOREQUALTO180_tree = 
                    (CommonTree)adaptor.create(LESSTHANOREQUALTO180)
                    ;
                    adaptor.addChild(root_0, LESSTHANOREQUALTO180_tree);
                    }

                    }
                    break;
                case 6 :
                    // IdentifiersParser.g:365:90: LESSTHAN
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    LESSTHAN181=(Token)match(input,LESSTHAN,FOLLOW_LESSTHAN_in_precedenceEqualOperator2282); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    LESSTHAN181_tree = 
                    (CommonTree)adaptor.create(LESSTHAN181)
                    ;
                    adaptor.addChild(root_0, LESSTHAN181_tree);
                    }

                    }
                    break;
                case 7 :
                    // IdentifiersParser.g:365:101: GREATERTHANOREQUALTO
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    GREATERTHANOREQUALTO182=(Token)match(input,GREATERTHANOREQUALTO,FOLLOW_GREATERTHANOREQUALTO_in_precedenceEqualOperator2286); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    GREATERTHANOREQUALTO182_tree = 
                    (CommonTree)adaptor.create(GREATERTHANOREQUALTO182)
                    ;
                    adaptor.addChild(root_0, GREATERTHANOREQUALTO182_tree);
                    }

                    }
                    break;
                case 8 :
                    // IdentifiersParser.g:365:124: GREATERTHAN
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    GREATERTHAN183=(Token)match(input,GREATERTHAN,FOLLOW_GREATERTHAN_in_precedenceEqualOperator2290); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    GREATERTHAN183_tree = 
                    (CommonTree)adaptor.create(GREATERTHAN183)
                    ;
                    adaptor.addChild(root_0, GREATERTHAN183_tree);
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "precedenceEqualOperator"


    public static class subQueryExpression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "subQueryExpression"
    // IdentifiersParser.g:368:1: subQueryExpression : LPAREN ! selectStatement[true] RPAREN !;
    public final HiveParser_IdentifiersParser.subQueryExpression_return subQueryExpression() throws RecognitionException {
        HiveParser_IdentifiersParser.subQueryExpression_return retval = new HiveParser_IdentifiersParser.subQueryExpression_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token LPAREN184=null;
        Token RPAREN186=null;
        HiveParser.selectStatement_return selectStatement185 =null;


        CommonTree LPAREN184_tree=null;
        CommonTree RPAREN186_tree=null;

        try {
            // IdentifiersParser.g:369:5: ( LPAREN ! selectStatement[true] RPAREN !)
            // IdentifiersParser.g:370:5: LPAREN ! selectStatement[true] RPAREN !
            {
            root_0 = (CommonTree)adaptor.nil();


            LPAREN184=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_subQueryExpression2313); if (state.failed) return retval;

            pushFollow(FOLLOW_selectStatement_in_subQueryExpression2316);
            selectStatement185=gHiveParser.selectStatement(true);

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, selectStatement185.getTree());

            RPAREN186=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_subQueryExpression2319); if (state.failed) return retval;

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "subQueryExpression"


    public static class precedenceEqualExpression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "precedenceEqualExpression"
    // IdentifiersParser.g:373:1: precedenceEqualExpression : ( (left= precedenceBitwiseOrExpression -> $left) ( ( KW_NOT precedenceEqualNegatableOperator notExpr= precedenceBitwiseOrExpression ) -> ^( KW_NOT ^( precedenceEqualNegatableOperator $precedenceEqualExpression $notExpr) ) | ( precedenceEqualOperator equalExpr= precedenceBitwiseOrExpression ) -> ^( precedenceEqualOperator $precedenceEqualExpression $equalExpr) | ( KW_NOT KW_IN LPAREN KW_SELECT )=> ( KW_NOT KW_IN subQueryExpression ) -> ^( KW_NOT ^( TOK_SUBQUERY_EXPR ^( TOK_SUBQUERY_OP KW_IN ) subQueryExpression $precedenceEqualExpression) ) | ( KW_NOT KW_IN expressions ) -> ^( KW_NOT ^( TOK_FUNCTION KW_IN $precedenceEqualExpression expressions ) ) | ( KW_IN LPAREN KW_SELECT )=> ( KW_IN subQueryExpression ) -> ^( TOK_SUBQUERY_EXPR ^( TOK_SUBQUERY_OP KW_IN ) subQueryExpression $precedenceEqualExpression) | ( KW_IN expressions ) -> ^( TOK_FUNCTION KW_IN $precedenceEqualExpression expressions ) | ( KW_NOT KW_BETWEEN (min= precedenceBitwiseOrExpression ) KW_AND (max= precedenceBitwiseOrExpression ) ) -> ^( TOK_FUNCTION Identifier[\"between\"] KW_TRUE $left $min $max) | ( KW_BETWEEN (min= precedenceBitwiseOrExpression ) KW_AND (max= precedenceBitwiseOrExpression ) ) -> ^( TOK_FUNCTION Identifier[\"between\"] KW_FALSE $left $min $max) )* | ( KW_EXISTS LPAREN KW_SELECT )=> ( KW_EXISTS subQueryExpression ) -> ^( TOK_SUBQUERY_EXPR ^( TOK_SUBQUERY_OP KW_EXISTS ) subQueryExpression ) );
    public final HiveParser_IdentifiersParser.precedenceEqualExpression_return precedenceEqualExpression() throws RecognitionException {
        HiveParser_IdentifiersParser.precedenceEqualExpression_return retval = new HiveParser_IdentifiersParser.precedenceEqualExpression_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token KW_NOT187=null;
        Token KW_NOT190=null;
        Token KW_IN191=null;
        Token KW_NOT193=null;
        Token KW_IN194=null;
        Token KW_IN196=null;
        Token KW_IN198=null;
        Token KW_NOT200=null;
        Token KW_BETWEEN201=null;
        Token KW_AND202=null;
        Token KW_BETWEEN203=null;
        Token KW_AND204=null;
        Token KW_EXISTS205=null;
        HiveParser_IdentifiersParser.precedenceBitwiseOrExpression_return left =null;

        HiveParser_IdentifiersParser.precedenceBitwiseOrExpression_return notExpr =null;

        HiveParser_IdentifiersParser.precedenceBitwiseOrExpression_return equalExpr =null;

        HiveParser_IdentifiersParser.precedenceBitwiseOrExpression_return min =null;

        HiveParser_IdentifiersParser.precedenceBitwiseOrExpression_return max =null;

        HiveParser_IdentifiersParser.precedenceEqualNegatableOperator_return precedenceEqualNegatableOperator188 =null;

        HiveParser_IdentifiersParser.precedenceEqualOperator_return precedenceEqualOperator189 =null;

        HiveParser_IdentifiersParser.subQueryExpression_return subQueryExpression192 =null;

        HiveParser_IdentifiersParser.expressions_return expressions195 =null;

        HiveParser_IdentifiersParser.subQueryExpression_return subQueryExpression197 =null;

        HiveParser_IdentifiersParser.expressions_return expressions199 =null;

        HiveParser_IdentifiersParser.subQueryExpression_return subQueryExpression206 =null;


        CommonTree KW_NOT187_tree=null;
        CommonTree KW_NOT190_tree=null;
        CommonTree KW_IN191_tree=null;
        CommonTree KW_NOT193_tree=null;
        CommonTree KW_IN194_tree=null;
        CommonTree KW_IN196_tree=null;
        CommonTree KW_IN198_tree=null;
        CommonTree KW_NOT200_tree=null;
        CommonTree KW_BETWEEN201_tree=null;
        CommonTree KW_AND202_tree=null;
        CommonTree KW_BETWEEN203_tree=null;
        CommonTree KW_AND204_tree=null;
        CommonTree KW_EXISTS205_tree=null;
        RewriteRuleTokenStream stream_KW_IN=new RewriteRuleTokenStream(adaptor,"token KW_IN");
        RewriteRuleTokenStream stream_KW_BETWEEN=new RewriteRuleTokenStream(adaptor,"token KW_BETWEEN");
        RewriteRuleTokenStream stream_KW_AND=new RewriteRuleTokenStream(adaptor,"token KW_AND");
        RewriteRuleTokenStream stream_KW_EXISTS=new RewriteRuleTokenStream(adaptor,"token KW_EXISTS");
        RewriteRuleTokenStream stream_KW_NOT=new RewriteRuleTokenStream(adaptor,"token KW_NOT");
        RewriteRuleSubtreeStream stream_subQueryExpression=new RewriteRuleSubtreeStream(adaptor,"rule subQueryExpression");
        RewriteRuleSubtreeStream stream_precedenceEqualNegatableOperator=new RewriteRuleSubtreeStream(adaptor,"rule precedenceEqualNegatableOperator");
        RewriteRuleSubtreeStream stream_precedenceEqualOperator=new RewriteRuleSubtreeStream(adaptor,"rule precedenceEqualOperator");
        RewriteRuleSubtreeStream stream_precedenceBitwiseOrExpression=new RewriteRuleSubtreeStream(adaptor,"rule precedenceBitwiseOrExpression");
        RewriteRuleSubtreeStream stream_expressions=new RewriteRuleSubtreeStream(adaptor,"rule expressions");
        try {
            // IdentifiersParser.g:374:5: ( (left= precedenceBitwiseOrExpression -> $left) ( ( KW_NOT precedenceEqualNegatableOperator notExpr= precedenceBitwiseOrExpression ) -> ^( KW_NOT ^( precedenceEqualNegatableOperator $precedenceEqualExpression $notExpr) ) | ( precedenceEqualOperator equalExpr= precedenceBitwiseOrExpression ) -> ^( precedenceEqualOperator $precedenceEqualExpression $equalExpr) | ( KW_NOT KW_IN LPAREN KW_SELECT )=> ( KW_NOT KW_IN subQueryExpression ) -> ^( KW_NOT ^( TOK_SUBQUERY_EXPR ^( TOK_SUBQUERY_OP KW_IN ) subQueryExpression $precedenceEqualExpression) ) | ( KW_NOT KW_IN expressions ) -> ^( KW_NOT ^( TOK_FUNCTION KW_IN $precedenceEqualExpression expressions ) ) | ( KW_IN LPAREN KW_SELECT )=> ( KW_IN subQueryExpression ) -> ^( TOK_SUBQUERY_EXPR ^( TOK_SUBQUERY_OP KW_IN ) subQueryExpression $precedenceEqualExpression) | ( KW_IN expressions ) -> ^( TOK_FUNCTION KW_IN $precedenceEqualExpression expressions ) | ( KW_NOT KW_BETWEEN (min= precedenceBitwiseOrExpression ) KW_AND (max= precedenceBitwiseOrExpression ) ) -> ^( TOK_FUNCTION Identifier[\"between\"] KW_TRUE $left $min $max) | ( KW_BETWEEN (min= precedenceBitwiseOrExpression ) KW_AND (max= precedenceBitwiseOrExpression ) ) -> ^( TOK_FUNCTION Identifier[\"between\"] KW_FALSE $left $min $max) )* | ( KW_EXISTS LPAREN KW_SELECT )=> ( KW_EXISTS subQueryExpression ) -> ^( TOK_SUBQUERY_EXPR ^( TOK_SUBQUERY_OP KW_EXISTS ) subQueryExpression ) )
            int alt44=2;
            alt44 = dfa44.predict(input);
            switch (alt44) {
                case 1 :
                    // IdentifiersParser.g:375:5: (left= precedenceBitwiseOrExpression -> $left) ( ( KW_NOT precedenceEqualNegatableOperator notExpr= precedenceBitwiseOrExpression ) -> ^( KW_NOT ^( precedenceEqualNegatableOperator $precedenceEqualExpression $notExpr) ) | ( precedenceEqualOperator equalExpr= precedenceBitwiseOrExpression ) -> ^( precedenceEqualOperator $precedenceEqualExpression $equalExpr) | ( KW_NOT KW_IN LPAREN KW_SELECT )=> ( KW_NOT KW_IN subQueryExpression ) -> ^( KW_NOT ^( TOK_SUBQUERY_EXPR ^( TOK_SUBQUERY_OP KW_IN ) subQueryExpression $precedenceEqualExpression) ) | ( KW_NOT KW_IN expressions ) -> ^( KW_NOT ^( TOK_FUNCTION KW_IN $precedenceEqualExpression expressions ) ) | ( KW_IN LPAREN KW_SELECT )=> ( KW_IN subQueryExpression ) -> ^( TOK_SUBQUERY_EXPR ^( TOK_SUBQUERY_OP KW_IN ) subQueryExpression $precedenceEqualExpression) | ( KW_IN expressions ) -> ^( TOK_FUNCTION KW_IN $precedenceEqualExpression expressions ) | ( KW_NOT KW_BETWEEN (min= precedenceBitwiseOrExpression ) KW_AND (max= precedenceBitwiseOrExpression ) ) -> ^( TOK_FUNCTION Identifier[\"between\"] KW_TRUE $left $min $max) | ( KW_BETWEEN (min= precedenceBitwiseOrExpression ) KW_AND (max= precedenceBitwiseOrExpression ) ) -> ^( TOK_FUNCTION Identifier[\"between\"] KW_FALSE $left $min $max) )*
                    {
                    // IdentifiersParser.g:375:5: (left= precedenceBitwiseOrExpression -> $left)
                    // IdentifiersParser.g:375:6: left= precedenceBitwiseOrExpression
                    {
                    pushFollow(FOLLOW_precedenceBitwiseOrExpression_in_precedenceEqualExpression2347);
                    left=precedenceBitwiseOrExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_precedenceBitwiseOrExpression.add(left.getTree());

                    // AST REWRITE
                    // elements: left
                    // token labels: 
                    // rule labels: retval, left
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_left=new RewriteRuleSubtreeStream(adaptor,"rule left",left!=null?left.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 375:41: -> $left
                    {
                        adaptor.addChild(root_0, stream_left.nextTree());

                    }


                    retval.tree = root_0;
                    }

                    }


                    // IdentifiersParser.g:376:5: ( ( KW_NOT precedenceEqualNegatableOperator notExpr= precedenceBitwiseOrExpression ) -> ^( KW_NOT ^( precedenceEqualNegatableOperator $precedenceEqualExpression $notExpr) ) | ( precedenceEqualOperator equalExpr= precedenceBitwiseOrExpression ) -> ^( precedenceEqualOperator $precedenceEqualExpression $equalExpr) | ( KW_NOT KW_IN LPAREN KW_SELECT )=> ( KW_NOT KW_IN subQueryExpression ) -> ^( KW_NOT ^( TOK_SUBQUERY_EXPR ^( TOK_SUBQUERY_OP KW_IN ) subQueryExpression $precedenceEqualExpression) ) | ( KW_NOT KW_IN expressions ) -> ^( KW_NOT ^( TOK_FUNCTION KW_IN $precedenceEqualExpression expressions ) ) | ( KW_IN LPAREN KW_SELECT )=> ( KW_IN subQueryExpression ) -> ^( TOK_SUBQUERY_EXPR ^( TOK_SUBQUERY_OP KW_IN ) subQueryExpression $precedenceEqualExpression) | ( KW_IN expressions ) -> ^( TOK_FUNCTION KW_IN $precedenceEqualExpression expressions ) | ( KW_NOT KW_BETWEEN (min= precedenceBitwiseOrExpression ) KW_AND (max= precedenceBitwiseOrExpression ) ) -> ^( TOK_FUNCTION Identifier[\"between\"] KW_TRUE $left $min $max) | ( KW_BETWEEN (min= precedenceBitwiseOrExpression ) KW_AND (max= precedenceBitwiseOrExpression ) ) -> ^( TOK_FUNCTION Identifier[\"between\"] KW_FALSE $left $min $max) )*
                    loop43:
                    do {
                        int alt43=9;
                        alt43 = dfa43.predict(input);
                        switch (alt43) {
                    	case 1 :
                    	    // IdentifiersParser.g:377:8: ( KW_NOT precedenceEqualNegatableOperator notExpr= precedenceBitwiseOrExpression )
                    	    {
                    	    // IdentifiersParser.g:377:8: ( KW_NOT precedenceEqualNegatableOperator notExpr= precedenceBitwiseOrExpression )
                    	    // IdentifiersParser.g:377:9: KW_NOT precedenceEqualNegatableOperator notExpr= precedenceBitwiseOrExpression
                    	    {
                    	    KW_NOT187=(Token)match(input,KW_NOT,FOLLOW_KW_NOT_in_precedenceEqualExpression2369); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_KW_NOT.add(KW_NOT187);


                    	    pushFollow(FOLLOW_precedenceEqualNegatableOperator_in_precedenceEqualExpression2371);
                    	    precedenceEqualNegatableOperator188=precedenceEqualNegatableOperator();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_precedenceEqualNegatableOperator.add(precedenceEqualNegatableOperator188.getTree());

                    	    pushFollow(FOLLOW_precedenceBitwiseOrExpression_in_precedenceEqualExpression2375);
                    	    notExpr=precedenceBitwiseOrExpression();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_precedenceBitwiseOrExpression.add(notExpr.getTree());

                    	    }


                    	    // AST REWRITE
                    	    // elements: KW_NOT, notExpr, precedenceEqualNegatableOperator, precedenceEqualExpression
                    	    // token labels: 
                    	    // rule labels: retval, notExpr
                    	    // token list labels: 
                    	    // rule list labels: 
                    	    // wildcard labels: 
                    	    if ( state.backtracking==0 ) {

                    	    retval.tree = root_0;
                    	    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    	    RewriteRuleSubtreeStream stream_notExpr=new RewriteRuleSubtreeStream(adaptor,"rule notExpr",notExpr!=null?notExpr.tree:null);

                    	    root_0 = (CommonTree)adaptor.nil();
                    	    // 378:8: -> ^( KW_NOT ^( precedenceEqualNegatableOperator $precedenceEqualExpression $notExpr) )
                    	    {
                    	        // IdentifiersParser.g:378:11: ^( KW_NOT ^( precedenceEqualNegatableOperator $precedenceEqualExpression $notExpr) )
                    	        {
                    	        CommonTree root_1 = (CommonTree)adaptor.nil();
                    	        root_1 = (CommonTree)adaptor.becomeRoot(
                    	        stream_KW_NOT.nextNode()
                    	        , root_1);

                    	        // IdentifiersParser.g:378:20: ^( precedenceEqualNegatableOperator $precedenceEqualExpression $notExpr)
                    	        {
                    	        CommonTree root_2 = (CommonTree)adaptor.nil();
                    	        root_2 = (CommonTree)adaptor.becomeRoot(stream_precedenceEqualNegatableOperator.nextNode(), root_2);

                    	        adaptor.addChild(root_2, stream_retval.nextTree());

                    	        adaptor.addChild(root_2, stream_notExpr.nextTree());

                    	        adaptor.addChild(root_1, root_2);
                    	        }

                    	        adaptor.addChild(root_0, root_1);
                    	        }

                    	    }


                    	    retval.tree = root_0;
                    	    }

                    	    }
                    	    break;
                    	case 2 :
                    	    // IdentifiersParser.g:379:7: ( precedenceEqualOperator equalExpr= precedenceBitwiseOrExpression )
                    	    {
                    	    // IdentifiersParser.g:379:7: ( precedenceEqualOperator equalExpr= precedenceBitwiseOrExpression )
                    	    // IdentifiersParser.g:379:8: precedenceEqualOperator equalExpr= precedenceBitwiseOrExpression
                    	    {
                    	    pushFollow(FOLLOW_precedenceEqualOperator_in_precedenceEqualExpression2408);
                    	    precedenceEqualOperator189=precedenceEqualOperator();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_precedenceEqualOperator.add(precedenceEqualOperator189.getTree());

                    	    pushFollow(FOLLOW_precedenceBitwiseOrExpression_in_precedenceEqualExpression2412);
                    	    equalExpr=precedenceBitwiseOrExpression();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_precedenceBitwiseOrExpression.add(equalExpr.getTree());

                    	    }


                    	    // AST REWRITE
                    	    // elements: equalExpr, precedenceEqualOperator, precedenceEqualExpression
                    	    // token labels: 
                    	    // rule labels: equalExpr, retval
                    	    // token list labels: 
                    	    // rule list labels: 
                    	    // wildcard labels: 
                    	    if ( state.backtracking==0 ) {

                    	    retval.tree = root_0;
                    	    RewriteRuleSubtreeStream stream_equalExpr=new RewriteRuleSubtreeStream(adaptor,"rule equalExpr",equalExpr!=null?equalExpr.tree:null);
                    	    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    	    root_0 = (CommonTree)adaptor.nil();
                    	    // 380:8: -> ^( precedenceEqualOperator $precedenceEqualExpression $equalExpr)
                    	    {
                    	        // IdentifiersParser.g:380:11: ^( precedenceEqualOperator $precedenceEqualExpression $equalExpr)
                    	        {
                    	        CommonTree root_1 = (CommonTree)adaptor.nil();
                    	        root_1 = (CommonTree)adaptor.becomeRoot(stream_precedenceEqualOperator.nextNode(), root_1);

                    	        adaptor.addChild(root_1, stream_retval.nextTree());

                    	        adaptor.addChild(root_1, stream_equalExpr.nextTree());

                    	        adaptor.addChild(root_0, root_1);
                    	        }

                    	    }


                    	    retval.tree = root_0;
                    	    }

                    	    }
                    	    break;
                    	case 3 :
                    	    // IdentifiersParser.g:381:7: ( KW_NOT KW_IN LPAREN KW_SELECT )=> ( KW_NOT KW_IN subQueryExpression )
                    	    {
                    	    // IdentifiersParser.g:381:42: ( KW_NOT KW_IN subQueryExpression )
                    	    // IdentifiersParser.g:381:43: KW_NOT KW_IN subQueryExpression
                    	    {
                    	    KW_NOT190=(Token)match(input,KW_NOT,FOLLOW_KW_NOT_in_precedenceEqualExpression2453); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_KW_NOT.add(KW_NOT190);


                    	    KW_IN191=(Token)match(input,KW_IN,FOLLOW_KW_IN_in_precedenceEqualExpression2455); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_KW_IN.add(KW_IN191);


                    	    pushFollow(FOLLOW_subQueryExpression_in_precedenceEqualExpression2457);
                    	    subQueryExpression192=subQueryExpression();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_subQueryExpression.add(subQueryExpression192.getTree());

                    	    }


                    	    // AST REWRITE
                    	    // elements: KW_NOT, KW_IN, precedenceEqualExpression, subQueryExpression
                    	    // token labels: 
                    	    // rule labels: retval
                    	    // token list labels: 
                    	    // rule list labels: 
                    	    // wildcard labels: 
                    	    if ( state.backtracking==0 ) {

                    	    retval.tree = root_0;
                    	    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    	    root_0 = (CommonTree)adaptor.nil();
                    	    // 382:8: -> ^( KW_NOT ^( TOK_SUBQUERY_EXPR ^( TOK_SUBQUERY_OP KW_IN ) subQueryExpression $precedenceEqualExpression) )
                    	    {
                    	        // IdentifiersParser.g:382:11: ^( KW_NOT ^( TOK_SUBQUERY_EXPR ^( TOK_SUBQUERY_OP KW_IN ) subQueryExpression $precedenceEqualExpression) )
                    	        {
                    	        CommonTree root_1 = (CommonTree)adaptor.nil();
                    	        root_1 = (CommonTree)adaptor.becomeRoot(
                    	        stream_KW_NOT.nextNode()
                    	        , root_1);

                    	        // IdentifiersParser.g:382:20: ^( TOK_SUBQUERY_EXPR ^( TOK_SUBQUERY_OP KW_IN ) subQueryExpression $precedenceEqualExpression)
                    	        {
                    	        CommonTree root_2 = (CommonTree)adaptor.nil();
                    	        root_2 = (CommonTree)adaptor.becomeRoot(
                    	        (CommonTree)adaptor.create(TOK_SUBQUERY_EXPR, "TOK_SUBQUERY_EXPR")
                    	        , root_2);

                    	        // IdentifiersParser.g:382:40: ^( TOK_SUBQUERY_OP KW_IN )
                    	        {
                    	        CommonTree root_3 = (CommonTree)adaptor.nil();
                    	        root_3 = (CommonTree)adaptor.becomeRoot(
                    	        (CommonTree)adaptor.create(TOK_SUBQUERY_OP, "TOK_SUBQUERY_OP")
                    	        , root_3);

                    	        adaptor.addChild(root_3, 
                    	        stream_KW_IN.nextNode()
                    	        );

                    	        adaptor.addChild(root_2, root_3);
                    	        }

                    	        adaptor.addChild(root_2, stream_subQueryExpression.nextTree());

                    	        adaptor.addChild(root_2, stream_retval.nextTree());

                    	        adaptor.addChild(root_1, root_2);
                    	        }

                    	        adaptor.addChild(root_0, root_1);
                    	        }

                    	    }


                    	    retval.tree = root_0;
                    	    }

                    	    }
                    	    break;
                    	case 4 :
                    	    // IdentifiersParser.g:383:7: ( KW_NOT KW_IN expressions )
                    	    {
                    	    // IdentifiersParser.g:383:7: ( KW_NOT KW_IN expressions )
                    	    // IdentifiersParser.g:383:8: KW_NOT KW_IN expressions
                    	    {
                    	    KW_NOT193=(Token)match(input,KW_NOT,FOLLOW_KW_NOT_in_precedenceEqualExpression2496); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_KW_NOT.add(KW_NOT193);


                    	    KW_IN194=(Token)match(input,KW_IN,FOLLOW_KW_IN_in_precedenceEqualExpression2498); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_KW_IN.add(KW_IN194);


                    	    pushFollow(FOLLOW_expressions_in_precedenceEqualExpression2500);
                    	    expressions195=expressions();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_expressions.add(expressions195.getTree());

                    	    }


                    	    // AST REWRITE
                    	    // elements: KW_IN, expressions, precedenceEqualExpression, KW_NOT
                    	    // token labels: 
                    	    // rule labels: retval
                    	    // token list labels: 
                    	    // rule list labels: 
                    	    // wildcard labels: 
                    	    if ( state.backtracking==0 ) {

                    	    retval.tree = root_0;
                    	    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    	    root_0 = (CommonTree)adaptor.nil();
                    	    // 384:8: -> ^( KW_NOT ^( TOK_FUNCTION KW_IN $precedenceEqualExpression expressions ) )
                    	    {
                    	        // IdentifiersParser.g:384:11: ^( KW_NOT ^( TOK_FUNCTION KW_IN $precedenceEqualExpression expressions ) )
                    	        {
                    	        CommonTree root_1 = (CommonTree)adaptor.nil();
                    	        root_1 = (CommonTree)adaptor.becomeRoot(
                    	        stream_KW_NOT.nextNode()
                    	        , root_1);

                    	        // IdentifiersParser.g:384:20: ^( TOK_FUNCTION KW_IN $precedenceEqualExpression expressions )
                    	        {
                    	        CommonTree root_2 = (CommonTree)adaptor.nil();
                    	        root_2 = (CommonTree)adaptor.becomeRoot(
                    	        (CommonTree)adaptor.create(TOK_FUNCTION, "TOK_FUNCTION")
                    	        , root_2);

                    	        adaptor.addChild(root_2, 
                    	        stream_KW_IN.nextNode()
                    	        );

                    	        adaptor.addChild(root_2, stream_retval.nextTree());

                    	        adaptor.addChild(root_2, stream_expressions.nextTree());

                    	        adaptor.addChild(root_1, root_2);
                    	        }

                    	        adaptor.addChild(root_0, root_1);
                    	        }

                    	    }


                    	    retval.tree = root_0;
                    	    }

                    	    }
                    	    break;
                    	case 5 :
                    	    // IdentifiersParser.g:385:7: ( KW_IN LPAREN KW_SELECT )=> ( KW_IN subQueryExpression )
                    	    {
                    	    // IdentifiersParser.g:385:35: ( KW_IN subQueryExpression )
                    	    // IdentifiersParser.g:385:36: KW_IN subQueryExpression
                    	    {
                    	    KW_IN196=(Token)match(input,KW_IN,FOLLOW_KW_IN_in_precedenceEqualExpression2544); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_KW_IN.add(KW_IN196);


                    	    pushFollow(FOLLOW_subQueryExpression_in_precedenceEqualExpression2546);
                    	    subQueryExpression197=subQueryExpression();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_subQueryExpression.add(subQueryExpression197.getTree());

                    	    }


                    	    // AST REWRITE
                    	    // elements: KW_IN, subQueryExpression, precedenceEqualExpression
                    	    // token labels: 
                    	    // rule labels: retval
                    	    // token list labels: 
                    	    // rule list labels: 
                    	    // wildcard labels: 
                    	    if ( state.backtracking==0 ) {

                    	    retval.tree = root_0;
                    	    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    	    root_0 = (CommonTree)adaptor.nil();
                    	    // 386:8: -> ^( TOK_SUBQUERY_EXPR ^( TOK_SUBQUERY_OP KW_IN ) subQueryExpression $precedenceEqualExpression)
                    	    {
                    	        // IdentifiersParser.g:386:11: ^( TOK_SUBQUERY_EXPR ^( TOK_SUBQUERY_OP KW_IN ) subQueryExpression $precedenceEqualExpression)
                    	        {
                    	        CommonTree root_1 = (CommonTree)adaptor.nil();
                    	        root_1 = (CommonTree)adaptor.becomeRoot(
                    	        (CommonTree)adaptor.create(TOK_SUBQUERY_EXPR, "TOK_SUBQUERY_EXPR")
                    	        , root_1);

                    	        // IdentifiersParser.g:386:31: ^( TOK_SUBQUERY_OP KW_IN )
                    	        {
                    	        CommonTree root_2 = (CommonTree)adaptor.nil();
                    	        root_2 = (CommonTree)adaptor.becomeRoot(
                    	        (CommonTree)adaptor.create(TOK_SUBQUERY_OP, "TOK_SUBQUERY_OP")
                    	        , root_2);

                    	        adaptor.addChild(root_2, 
                    	        stream_KW_IN.nextNode()
                    	        );

                    	        adaptor.addChild(root_1, root_2);
                    	        }

                    	        adaptor.addChild(root_1, stream_subQueryExpression.nextTree());

                    	        adaptor.addChild(root_1, stream_retval.nextTree());

                    	        adaptor.addChild(root_0, root_1);
                    	        }

                    	    }


                    	    retval.tree = root_0;
                    	    }

                    	    }
                    	    break;
                    	case 6 :
                    	    // IdentifiersParser.g:387:7: ( KW_IN expressions )
                    	    {
                    	    // IdentifiersParser.g:387:7: ( KW_IN expressions )
                    	    // IdentifiersParser.g:387:8: KW_IN expressions
                    	    {
                    	    KW_IN198=(Token)match(input,KW_IN,FOLLOW_KW_IN_in_precedenceEqualExpression2581); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_KW_IN.add(KW_IN198);


                    	    pushFollow(FOLLOW_expressions_in_precedenceEqualExpression2583);
                    	    expressions199=expressions();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_expressions.add(expressions199.getTree());

                    	    }


                    	    // AST REWRITE
                    	    // elements: expressions, precedenceEqualExpression, KW_IN
                    	    // token labels: 
                    	    // rule labels: retval
                    	    // token list labels: 
                    	    // rule list labels: 
                    	    // wildcard labels: 
                    	    if ( state.backtracking==0 ) {

                    	    retval.tree = root_0;
                    	    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    	    root_0 = (CommonTree)adaptor.nil();
                    	    // 388:8: -> ^( TOK_FUNCTION KW_IN $precedenceEqualExpression expressions )
                    	    {
                    	        // IdentifiersParser.g:388:11: ^( TOK_FUNCTION KW_IN $precedenceEqualExpression expressions )
                    	        {
                    	        CommonTree root_1 = (CommonTree)adaptor.nil();
                    	        root_1 = (CommonTree)adaptor.becomeRoot(
                    	        (CommonTree)adaptor.create(TOK_FUNCTION, "TOK_FUNCTION")
                    	        , root_1);

                    	        adaptor.addChild(root_1, 
                    	        stream_KW_IN.nextNode()
                    	        );

                    	        adaptor.addChild(root_1, stream_retval.nextTree());

                    	        adaptor.addChild(root_1, stream_expressions.nextTree());

                    	        adaptor.addChild(root_0, root_1);
                    	        }

                    	    }


                    	    retval.tree = root_0;
                    	    }

                    	    }
                    	    break;
                    	case 7 :
                    	    // IdentifiersParser.g:389:7: ( KW_NOT KW_BETWEEN (min= precedenceBitwiseOrExpression ) KW_AND (max= precedenceBitwiseOrExpression ) )
                    	    {
                    	    // IdentifiersParser.g:389:7: ( KW_NOT KW_BETWEEN (min= precedenceBitwiseOrExpression ) KW_AND (max= precedenceBitwiseOrExpression ) )
                    	    // IdentifiersParser.g:389:9: KW_NOT KW_BETWEEN (min= precedenceBitwiseOrExpression ) KW_AND (max= precedenceBitwiseOrExpression )
                    	    {
                    	    KW_NOT200=(Token)match(input,KW_NOT,FOLLOW_KW_NOT_in_precedenceEqualExpression2614); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_KW_NOT.add(KW_NOT200);


                    	    KW_BETWEEN201=(Token)match(input,KW_BETWEEN,FOLLOW_KW_BETWEEN_in_precedenceEqualExpression2616); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_KW_BETWEEN.add(KW_BETWEEN201);


                    	    // IdentifiersParser.g:389:27: (min= precedenceBitwiseOrExpression )
                    	    // IdentifiersParser.g:389:28: min= precedenceBitwiseOrExpression
                    	    {
                    	    pushFollow(FOLLOW_precedenceBitwiseOrExpression_in_precedenceEqualExpression2621);
                    	    min=precedenceBitwiseOrExpression();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_precedenceBitwiseOrExpression.add(min.getTree());

                    	    }


                    	    KW_AND202=(Token)match(input,KW_AND,FOLLOW_KW_AND_in_precedenceEqualExpression2624); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_KW_AND.add(KW_AND202);


                    	    // IdentifiersParser.g:389:70: (max= precedenceBitwiseOrExpression )
                    	    // IdentifiersParser.g:389:71: max= precedenceBitwiseOrExpression
                    	    {
                    	    pushFollow(FOLLOW_precedenceBitwiseOrExpression_in_precedenceEqualExpression2629);
                    	    max=precedenceBitwiseOrExpression();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_precedenceBitwiseOrExpression.add(max.getTree());

                    	    }


                    	    }


                    	    // AST REWRITE
                    	    // elements: max, left, min
                    	    // token labels: 
                    	    // rule labels: min, retval, max, left
                    	    // token list labels: 
                    	    // rule list labels: 
                    	    // wildcard labels: 
                    	    if ( state.backtracking==0 ) {

                    	    retval.tree = root_0;
                    	    RewriteRuleSubtreeStream stream_min=new RewriteRuleSubtreeStream(adaptor,"rule min",min!=null?min.tree:null);
                    	    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    	    RewriteRuleSubtreeStream stream_max=new RewriteRuleSubtreeStream(adaptor,"rule max",max!=null?max.tree:null);
                    	    RewriteRuleSubtreeStream stream_left=new RewriteRuleSubtreeStream(adaptor,"rule left",left!=null?left.tree:null);

                    	    root_0 = (CommonTree)adaptor.nil();
                    	    // 390:8: -> ^( TOK_FUNCTION Identifier[\"between\"] KW_TRUE $left $min $max)
                    	    {
                    	        // IdentifiersParser.g:390:11: ^( TOK_FUNCTION Identifier[\"between\"] KW_TRUE $left $min $max)
                    	        {
                    	        CommonTree root_1 = (CommonTree)adaptor.nil();
                    	        root_1 = (CommonTree)adaptor.becomeRoot(
                    	        (CommonTree)adaptor.create(TOK_FUNCTION, "TOK_FUNCTION")
                    	        , root_1);

                    	        adaptor.addChild(root_1, 
                    	        (CommonTree)adaptor.create(Identifier, "between")
                    	        );

                    	        adaptor.addChild(root_1, 
                    	        (CommonTree)adaptor.create(KW_TRUE, "KW_TRUE")
                    	        );

                    	        adaptor.addChild(root_1, stream_left.nextTree());

                    	        adaptor.addChild(root_1, stream_min.nextTree());

                    	        adaptor.addChild(root_1, stream_max.nextTree());

                    	        adaptor.addChild(root_0, root_1);
                    	        }

                    	    }


                    	    retval.tree = root_0;
                    	    }

                    	    }
                    	    break;
                    	case 8 :
                    	    // IdentifiersParser.g:391:7: ( KW_BETWEEN (min= precedenceBitwiseOrExpression ) KW_AND (max= precedenceBitwiseOrExpression ) )
                    	    {
                    	    // IdentifiersParser.g:391:7: ( KW_BETWEEN (min= precedenceBitwiseOrExpression ) KW_AND (max= precedenceBitwiseOrExpression ) )
                    	    // IdentifiersParser.g:391:9: KW_BETWEEN (min= precedenceBitwiseOrExpression ) KW_AND (max= precedenceBitwiseOrExpression )
                    	    {
                    	    KW_BETWEEN203=(Token)match(input,KW_BETWEEN,FOLLOW_KW_BETWEEN_in_precedenceEqualExpression2669); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_KW_BETWEEN.add(KW_BETWEEN203);


                    	    // IdentifiersParser.g:391:20: (min= precedenceBitwiseOrExpression )
                    	    // IdentifiersParser.g:391:21: min= precedenceBitwiseOrExpression
                    	    {
                    	    pushFollow(FOLLOW_precedenceBitwiseOrExpression_in_precedenceEqualExpression2674);
                    	    min=precedenceBitwiseOrExpression();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_precedenceBitwiseOrExpression.add(min.getTree());

                    	    }


                    	    KW_AND204=(Token)match(input,KW_AND,FOLLOW_KW_AND_in_precedenceEqualExpression2677); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_KW_AND.add(KW_AND204);


                    	    // IdentifiersParser.g:391:63: (max= precedenceBitwiseOrExpression )
                    	    // IdentifiersParser.g:391:64: max= precedenceBitwiseOrExpression
                    	    {
                    	    pushFollow(FOLLOW_precedenceBitwiseOrExpression_in_precedenceEqualExpression2682);
                    	    max=precedenceBitwiseOrExpression();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_precedenceBitwiseOrExpression.add(max.getTree());

                    	    }


                    	    }


                    	    // AST REWRITE
                    	    // elements: max, min, left
                    	    // token labels: 
                    	    // rule labels: min, retval, max, left
                    	    // token list labels: 
                    	    // rule list labels: 
                    	    // wildcard labels: 
                    	    if ( state.backtracking==0 ) {

                    	    retval.tree = root_0;
                    	    RewriteRuleSubtreeStream stream_min=new RewriteRuleSubtreeStream(adaptor,"rule min",min!=null?min.tree:null);
                    	    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    	    RewriteRuleSubtreeStream stream_max=new RewriteRuleSubtreeStream(adaptor,"rule max",max!=null?max.tree:null);
                    	    RewriteRuleSubtreeStream stream_left=new RewriteRuleSubtreeStream(adaptor,"rule left",left!=null?left.tree:null);

                    	    root_0 = (CommonTree)adaptor.nil();
                    	    // 392:8: -> ^( TOK_FUNCTION Identifier[\"between\"] KW_FALSE $left $min $max)
                    	    {
                    	        // IdentifiersParser.g:392:11: ^( TOK_FUNCTION Identifier[\"between\"] KW_FALSE $left $min $max)
                    	        {
                    	        CommonTree root_1 = (CommonTree)adaptor.nil();
                    	        root_1 = (CommonTree)adaptor.becomeRoot(
                    	        (CommonTree)adaptor.create(TOK_FUNCTION, "TOK_FUNCTION")
                    	        , root_1);

                    	        adaptor.addChild(root_1, 
                    	        (CommonTree)adaptor.create(Identifier, "between")
                    	        );

                    	        adaptor.addChild(root_1, 
                    	        (CommonTree)adaptor.create(KW_FALSE, "KW_FALSE")
                    	        );

                    	        adaptor.addChild(root_1, stream_left.nextTree());

                    	        adaptor.addChild(root_1, stream_min.nextTree());

                    	        adaptor.addChild(root_1, stream_max.nextTree());

                    	        adaptor.addChild(root_0, root_1);
                    	        }

                    	    }


                    	    retval.tree = root_0;
                    	    }

                    	    }
                    	    break;

                    	default :
                    	    break loop43;
                        }
                    } while (true);


                    }
                    break;
                case 2 :
                    // IdentifiersParser.g:394:7: ( KW_EXISTS LPAREN KW_SELECT )=> ( KW_EXISTS subQueryExpression )
                    {
                    // IdentifiersParser.g:394:38: ( KW_EXISTS subQueryExpression )
                    // IdentifiersParser.g:394:39: KW_EXISTS subQueryExpression
                    {
                    KW_EXISTS205=(Token)match(input,KW_EXISTS,FOLLOW_KW_EXISTS_in_precedenceEqualExpression2737); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_KW_EXISTS.add(KW_EXISTS205);


                    pushFollow(FOLLOW_subQueryExpression_in_precedenceEqualExpression2739);
                    subQueryExpression206=subQueryExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_subQueryExpression.add(subQueryExpression206.getTree());

                    }


                    // AST REWRITE
                    // elements: KW_EXISTS, subQueryExpression
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 394:69: -> ^( TOK_SUBQUERY_EXPR ^( TOK_SUBQUERY_OP KW_EXISTS ) subQueryExpression )
                    {
                        // IdentifiersParser.g:394:72: ^( TOK_SUBQUERY_EXPR ^( TOK_SUBQUERY_OP KW_EXISTS ) subQueryExpression )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(TOK_SUBQUERY_EXPR, "TOK_SUBQUERY_EXPR")
                        , root_1);

                        // IdentifiersParser.g:394:92: ^( TOK_SUBQUERY_OP KW_EXISTS )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(TOK_SUBQUERY_OP, "TOK_SUBQUERY_OP")
                        , root_2);

                        adaptor.addChild(root_2, 
                        stream_KW_EXISTS.nextNode()
                        );

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_1, stream_subQueryExpression.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "precedenceEqualExpression"


    public static class expressions_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "expressions"
    // IdentifiersParser.g:397:1: expressions : LPAREN expression ( COMMA expression )* RPAREN -> ( expression )* ;
    public final HiveParser_IdentifiersParser.expressions_return expressions() throws RecognitionException {
        HiveParser_IdentifiersParser.expressions_return retval = new HiveParser_IdentifiersParser.expressions_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token LPAREN207=null;
        Token COMMA209=null;
        Token RPAREN211=null;
        HiveParser_IdentifiersParser.expression_return expression208 =null;

        HiveParser_IdentifiersParser.expression_return expression210 =null;


        CommonTree LPAREN207_tree=null;
        CommonTree COMMA209_tree=null;
        CommonTree RPAREN211_tree=null;
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleSubtreeStream stream_expression=new RewriteRuleSubtreeStream(adaptor,"rule expression");
        try {
            // IdentifiersParser.g:398:5: ( LPAREN expression ( COMMA expression )* RPAREN -> ( expression )* )
            // IdentifiersParser.g:399:5: LPAREN expression ( COMMA expression )* RPAREN
            {
            LPAREN207=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_expressions2775); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN207);


            pushFollow(FOLLOW_expression_in_expressions2777);
            expression208=expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_expression.add(expression208.getTree());

            // IdentifiersParser.g:399:23: ( COMMA expression )*
            loop45:
            do {
                int alt45=2;
                switch ( input.LA(1) ) {
                case COMMA:
                    {
                    alt45=1;
                    }
                    break;

                }

                switch (alt45) {
            	case 1 :
            	    // IdentifiersParser.g:399:24: COMMA expression
            	    {
            	    COMMA209=(Token)match(input,COMMA,FOLLOW_COMMA_in_expressions2780); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA209);


            	    pushFollow(FOLLOW_expression_in_expressions2782);
            	    expression210=expression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_expression.add(expression210.getTree());

            	    }
            	    break;

            	default :
            	    break loop45;
                }
            } while (true);


            RPAREN211=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_expressions2786); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN211);


            // AST REWRITE
            // elements: expression
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 399:50: -> ( expression )*
            {
                // IdentifiersParser.g:399:53: ( expression )*
                while ( stream_expression.hasNext() ) {
                    adaptor.addChild(root_0, stream_expression.nextTree());

                }
                stream_expression.reset();

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "expressions"


    public static class precedenceNotOperator_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "precedenceNotOperator"
    // IdentifiersParser.g:402:1: precedenceNotOperator : KW_NOT ;
    public final HiveParser_IdentifiersParser.precedenceNotOperator_return precedenceNotOperator() throws RecognitionException {
        HiveParser_IdentifiersParser.precedenceNotOperator_return retval = new HiveParser_IdentifiersParser.precedenceNotOperator_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token KW_NOT212=null;

        CommonTree KW_NOT212_tree=null;

        try {
            // IdentifiersParser.g:403:5: ( KW_NOT )
            // IdentifiersParser.g:404:5: KW_NOT
            {
            root_0 = (CommonTree)adaptor.nil();


            KW_NOT212=(Token)match(input,KW_NOT,FOLLOW_KW_NOT_in_precedenceNotOperator2812); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            KW_NOT212_tree = 
            (CommonTree)adaptor.create(KW_NOT212)
            ;
            adaptor.addChild(root_0, KW_NOT212_tree);
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "precedenceNotOperator"


    public static class precedenceNotExpression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "precedenceNotExpression"
    // IdentifiersParser.g:407:1: precedenceNotExpression : ( precedenceNotOperator ^)* precedenceEqualExpression ;
    public final HiveParser_IdentifiersParser.precedenceNotExpression_return precedenceNotExpression() throws RecognitionException {
        HiveParser_IdentifiersParser.precedenceNotExpression_return retval = new HiveParser_IdentifiersParser.precedenceNotExpression_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        HiveParser_IdentifiersParser.precedenceNotOperator_return precedenceNotOperator213 =null;

        HiveParser_IdentifiersParser.precedenceEqualExpression_return precedenceEqualExpression214 =null;



        try {
            // IdentifiersParser.g:408:5: ( ( precedenceNotOperator ^)* precedenceEqualExpression )
            // IdentifiersParser.g:409:5: ( precedenceNotOperator ^)* precedenceEqualExpression
            {
            root_0 = (CommonTree)adaptor.nil();


            // IdentifiersParser.g:409:5: ( precedenceNotOperator ^)*
            loop46:
            do {
                int alt46=2;
                switch ( input.LA(1) ) {
                case KW_NOT:
                    {
                    alt46=1;
                    }
                    break;

                }

                switch (alt46) {
            	case 1 :
            	    // IdentifiersParser.g:409:6: precedenceNotOperator ^
            	    {
            	    pushFollow(FOLLOW_precedenceNotOperator_in_precedenceNotExpression2834);
            	    precedenceNotOperator213=precedenceNotOperator();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) root_0 = (CommonTree)adaptor.becomeRoot(precedenceNotOperator213.getTree(), root_0);

            	    }
            	    break;

            	default :
            	    break loop46;
                }
            } while (true);


            pushFollow(FOLLOW_precedenceEqualExpression_in_precedenceNotExpression2839);
            precedenceEqualExpression214=precedenceEqualExpression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, precedenceEqualExpression214.getTree());

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "precedenceNotExpression"


    public static class precedenceAndOperator_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "precedenceAndOperator"
    // IdentifiersParser.g:413:1: precedenceAndOperator : KW_AND ;
    public final HiveParser_IdentifiersParser.precedenceAndOperator_return precedenceAndOperator() throws RecognitionException {
        HiveParser_IdentifiersParser.precedenceAndOperator_return retval = new HiveParser_IdentifiersParser.precedenceAndOperator_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token KW_AND215=null;

        CommonTree KW_AND215_tree=null;

        try {
            // IdentifiersParser.g:414:5: ( KW_AND )
            // IdentifiersParser.g:415:5: KW_AND
            {
            root_0 = (CommonTree)adaptor.nil();


            KW_AND215=(Token)match(input,KW_AND,FOLLOW_KW_AND_in_precedenceAndOperator2861); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            KW_AND215_tree = 
            (CommonTree)adaptor.create(KW_AND215)
            ;
            adaptor.addChild(root_0, KW_AND215_tree);
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "precedenceAndOperator"


    public static class precedenceAndExpression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "precedenceAndExpression"
    // IdentifiersParser.g:418:1: precedenceAndExpression : precedenceNotExpression ( precedenceAndOperator ^ precedenceNotExpression )* ;
    public final HiveParser_IdentifiersParser.precedenceAndExpression_return precedenceAndExpression() throws RecognitionException {
        HiveParser_IdentifiersParser.precedenceAndExpression_return retval = new HiveParser_IdentifiersParser.precedenceAndExpression_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        HiveParser_IdentifiersParser.precedenceNotExpression_return precedenceNotExpression216 =null;

        HiveParser_IdentifiersParser.precedenceAndOperator_return precedenceAndOperator217 =null;

        HiveParser_IdentifiersParser.precedenceNotExpression_return precedenceNotExpression218 =null;



        try {
            // IdentifiersParser.g:419:5: ( precedenceNotExpression ( precedenceAndOperator ^ precedenceNotExpression )* )
            // IdentifiersParser.g:420:5: precedenceNotExpression ( precedenceAndOperator ^ precedenceNotExpression )*
            {
            root_0 = (CommonTree)adaptor.nil();


            pushFollow(FOLLOW_precedenceNotExpression_in_precedenceAndExpression2882);
            precedenceNotExpression216=precedenceNotExpression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, precedenceNotExpression216.getTree());

            // IdentifiersParser.g:420:29: ( precedenceAndOperator ^ precedenceNotExpression )*
            loop47:
            do {
                int alt47=2;
                switch ( input.LA(1) ) {
                case KW_AND:
                    {
                    alt47=1;
                    }
                    break;

                }

                switch (alt47) {
            	case 1 :
            	    // IdentifiersParser.g:420:30: precedenceAndOperator ^ precedenceNotExpression
            	    {
            	    pushFollow(FOLLOW_precedenceAndOperator_in_precedenceAndExpression2885);
            	    precedenceAndOperator217=precedenceAndOperator();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) root_0 = (CommonTree)adaptor.becomeRoot(precedenceAndOperator217.getTree(), root_0);

            	    pushFollow(FOLLOW_precedenceNotExpression_in_precedenceAndExpression2888);
            	    precedenceNotExpression218=precedenceNotExpression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, precedenceNotExpression218.getTree());

            	    }
            	    break;

            	default :
            	    break loop47;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "precedenceAndExpression"


    public static class precedenceOrOperator_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "precedenceOrOperator"
    // IdentifiersParser.g:424:1: precedenceOrOperator : KW_OR ;
    public final HiveParser_IdentifiersParser.precedenceOrOperator_return precedenceOrOperator() throws RecognitionException {
        HiveParser_IdentifiersParser.precedenceOrOperator_return retval = new HiveParser_IdentifiersParser.precedenceOrOperator_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token KW_OR219=null;

        CommonTree KW_OR219_tree=null;

        try {
            // IdentifiersParser.g:425:5: ( KW_OR )
            // IdentifiersParser.g:426:5: KW_OR
            {
            root_0 = (CommonTree)adaptor.nil();


            KW_OR219=(Token)match(input,KW_OR,FOLLOW_KW_OR_in_precedenceOrOperator2912); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            KW_OR219_tree = 
            (CommonTree)adaptor.create(KW_OR219)
            ;
            adaptor.addChild(root_0, KW_OR219_tree);
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "precedenceOrOperator"


    public static class precedenceOrExpression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "precedenceOrExpression"
    // IdentifiersParser.g:429:1: precedenceOrExpression : precedenceAndExpression ( precedenceOrOperator ^ precedenceAndExpression )* ;
    public final HiveParser_IdentifiersParser.precedenceOrExpression_return precedenceOrExpression() throws RecognitionException {
        HiveParser_IdentifiersParser.precedenceOrExpression_return retval = new HiveParser_IdentifiersParser.precedenceOrExpression_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        HiveParser_IdentifiersParser.precedenceAndExpression_return precedenceAndExpression220 =null;

        HiveParser_IdentifiersParser.precedenceOrOperator_return precedenceOrOperator221 =null;

        HiveParser_IdentifiersParser.precedenceAndExpression_return precedenceAndExpression222 =null;



        try {
            // IdentifiersParser.g:430:5: ( precedenceAndExpression ( precedenceOrOperator ^ precedenceAndExpression )* )
            // IdentifiersParser.g:431:5: precedenceAndExpression ( precedenceOrOperator ^ precedenceAndExpression )*
            {
            root_0 = (CommonTree)adaptor.nil();


            pushFollow(FOLLOW_precedenceAndExpression_in_precedenceOrExpression2933);
            precedenceAndExpression220=precedenceAndExpression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, precedenceAndExpression220.getTree());

            // IdentifiersParser.g:431:29: ( precedenceOrOperator ^ precedenceAndExpression )*
            loop48:
            do {
                int alt48=2;
                switch ( input.LA(1) ) {
                case KW_OR:
                    {
                    alt48=1;
                    }
                    break;

                }

                switch (alt48) {
            	case 1 :
            	    // IdentifiersParser.g:431:30: precedenceOrOperator ^ precedenceAndExpression
            	    {
            	    pushFollow(FOLLOW_precedenceOrOperator_in_precedenceOrExpression2936);
            	    precedenceOrOperator221=precedenceOrOperator();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) root_0 = (CommonTree)adaptor.becomeRoot(precedenceOrOperator221.getTree(), root_0);

            	    pushFollow(FOLLOW_precedenceAndExpression_in_precedenceOrExpression2939);
            	    precedenceAndExpression222=precedenceAndExpression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, precedenceAndExpression222.getTree());

            	    }
            	    break;

            	default :
            	    break loop48;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "precedenceOrExpression"


    public static class booleanValue_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "booleanValue"
    // IdentifiersParser.g:435:1: booleanValue : ( KW_TRUE ^| KW_FALSE ^);
    public final HiveParser_IdentifiersParser.booleanValue_return booleanValue() throws RecognitionException {
        HiveParser_IdentifiersParser.booleanValue_return retval = new HiveParser_IdentifiersParser.booleanValue_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token KW_TRUE223=null;
        Token KW_FALSE224=null;

        CommonTree KW_TRUE223_tree=null;
        CommonTree KW_FALSE224_tree=null;

        try {
            // IdentifiersParser.g:436:5: ( KW_TRUE ^| KW_FALSE ^)
            int alt49=2;
            switch ( input.LA(1) ) {
            case KW_TRUE:
                {
                alt49=1;
                }
                break;
            case KW_FALSE:
                {
                alt49=2;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 49, 0, input);

                throw nvae;

            }

            switch (alt49) {
                case 1 :
                    // IdentifiersParser.g:437:5: KW_TRUE ^
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    KW_TRUE223=(Token)match(input,KW_TRUE,FOLLOW_KW_TRUE_in_booleanValue2963); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    KW_TRUE223_tree = 
                    (CommonTree)adaptor.create(KW_TRUE223)
                    ;
                    root_0 = (CommonTree)adaptor.becomeRoot(KW_TRUE223_tree, root_0);
                    }

                    }
                    break;
                case 2 :
                    // IdentifiersParser.g:437:16: KW_FALSE ^
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    KW_FALSE224=(Token)match(input,KW_FALSE,FOLLOW_KW_FALSE_in_booleanValue2968); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    KW_FALSE224_tree = 
                    (CommonTree)adaptor.create(KW_FALSE224)
                    ;
                    root_0 = (CommonTree)adaptor.becomeRoot(KW_FALSE224_tree, root_0);
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "booleanValue"


    public static class tableOrPartition_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "tableOrPartition"
    // IdentifiersParser.g:440:1: tableOrPartition : tableName ( partitionSpec )? -> ^( TOK_TAB tableName ( partitionSpec )? ) ;
    public final HiveParser_IdentifiersParser.tableOrPartition_return tableOrPartition() throws RecognitionException {
        HiveParser_IdentifiersParser.tableOrPartition_return retval = new HiveParser_IdentifiersParser.tableOrPartition_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        HiveParser_FromClauseParser.tableName_return tableName225 =null;

        HiveParser_IdentifiersParser.partitionSpec_return partitionSpec226 =null;


        RewriteRuleSubtreeStream stream_tableName=new RewriteRuleSubtreeStream(adaptor,"rule tableName");
        RewriteRuleSubtreeStream stream_partitionSpec=new RewriteRuleSubtreeStream(adaptor,"rule partitionSpec");
        try {
            // IdentifiersParser.g:441:4: ( tableName ( partitionSpec )? -> ^( TOK_TAB tableName ( partitionSpec )? ) )
            // IdentifiersParser.g:442:4: tableName ( partitionSpec )?
            {
            pushFollow(FOLLOW_tableName_in_tableOrPartition2988);
            tableName225=gHiveParser.tableName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_tableName.add(tableName225.getTree());

            // IdentifiersParser.g:442:14: ( partitionSpec )?
            int alt50=2;
            switch ( input.LA(1) ) {
                case KW_PARTITION:
                    {
                    alt50=1;
                    }
                    break;
            }

            switch (alt50) {
                case 1 :
                    // IdentifiersParser.g:442:14: partitionSpec
                    {
                    pushFollow(FOLLOW_partitionSpec_in_tableOrPartition2990);
                    partitionSpec226=partitionSpec();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_partitionSpec.add(partitionSpec226.getTree());

                    }
                    break;

            }


            // AST REWRITE
            // elements: partitionSpec, tableName
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 442:29: -> ^( TOK_TAB tableName ( partitionSpec )? )
            {
                // IdentifiersParser.g:442:32: ^( TOK_TAB tableName ( partitionSpec )? )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(TOK_TAB, "TOK_TAB")
                , root_1);

                adaptor.addChild(root_1, stream_tableName.nextTree());

                // IdentifiersParser.g:442:52: ( partitionSpec )?
                if ( stream_partitionSpec.hasNext() ) {
                    adaptor.addChild(root_1, stream_partitionSpec.nextTree());

                }
                stream_partitionSpec.reset();

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "tableOrPartition"


    public static class partitionSpec_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "partitionSpec"
    // IdentifiersParser.g:445:1: partitionSpec : KW_PARTITION LPAREN partitionVal ( COMMA partitionVal )* RPAREN -> ^( TOK_PARTSPEC ( partitionVal )+ ) ;
    public final HiveParser_IdentifiersParser.partitionSpec_return partitionSpec() throws RecognitionException {
        HiveParser_IdentifiersParser.partitionSpec_return retval = new HiveParser_IdentifiersParser.partitionSpec_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token KW_PARTITION227=null;
        Token LPAREN228=null;
        Token COMMA230=null;
        Token RPAREN232=null;
        HiveParser_IdentifiersParser.partitionVal_return partitionVal229 =null;

        HiveParser_IdentifiersParser.partitionVal_return partitionVal231 =null;


        CommonTree KW_PARTITION227_tree=null;
        CommonTree LPAREN228_tree=null;
        CommonTree COMMA230_tree=null;
        CommonTree RPAREN232_tree=null;
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_KW_PARTITION=new RewriteRuleTokenStream(adaptor,"token KW_PARTITION");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleSubtreeStream stream_partitionVal=new RewriteRuleSubtreeStream(adaptor,"rule partitionVal");
        try {
            // IdentifiersParser.g:446:5: ( KW_PARTITION LPAREN partitionVal ( COMMA partitionVal )* RPAREN -> ^( TOK_PARTSPEC ( partitionVal )+ ) )
            // IdentifiersParser.g:447:5: KW_PARTITION LPAREN partitionVal ( COMMA partitionVal )* RPAREN
            {
            KW_PARTITION227=(Token)match(input,KW_PARTITION,FOLLOW_KW_PARTITION_in_partitionSpec3022); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KW_PARTITION.add(KW_PARTITION227);


            LPAREN228=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_partitionSpec3029); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN228);


            pushFollow(FOLLOW_partitionVal_in_partitionSpec3031);
            partitionVal229=partitionVal();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_partitionVal.add(partitionVal229.getTree());

            // IdentifiersParser.g:448:26: ( COMMA partitionVal )*
            loop51:
            do {
                int alt51=2;
                switch ( input.LA(1) ) {
                case COMMA:
                    {
                    alt51=1;
                    }
                    break;

                }

                switch (alt51) {
            	case 1 :
            	    // IdentifiersParser.g:448:27: COMMA partitionVal
            	    {
            	    COMMA230=(Token)match(input,COMMA,FOLLOW_COMMA_in_partitionSpec3034); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA230);


            	    pushFollow(FOLLOW_partitionVal_in_partitionSpec3037);
            	    partitionVal231=partitionVal();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_partitionVal.add(partitionVal231.getTree());

            	    }
            	    break;

            	default :
            	    break loop51;
                }
            } while (true);


            RPAREN232=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_partitionSpec3042); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN232);


            // AST REWRITE
            // elements: partitionVal
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 448:57: -> ^( TOK_PARTSPEC ( partitionVal )+ )
            {
                // IdentifiersParser.g:448:60: ^( TOK_PARTSPEC ( partitionVal )+ )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(TOK_PARTSPEC, "TOK_PARTSPEC")
                , root_1);

                if ( !(stream_partitionVal.hasNext()) ) {
                    throw new RewriteEarlyExitException();
                }
                while ( stream_partitionVal.hasNext() ) {
                    adaptor.addChild(root_1, stream_partitionVal.nextTree());

                }
                stream_partitionVal.reset();

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "partitionSpec"


    public static class partitionVal_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "partitionVal"
    // IdentifiersParser.g:451:1: partitionVal : identifier ( EQUAL constant )? -> ^( TOK_PARTVAL identifier ( constant )? ) ;
    public final HiveParser_IdentifiersParser.partitionVal_return partitionVal() throws RecognitionException {
        HiveParser_IdentifiersParser.partitionVal_return retval = new HiveParser_IdentifiersParser.partitionVal_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token EQUAL234=null;
        HiveParser_IdentifiersParser.identifier_return identifier233 =null;

        HiveParser_IdentifiersParser.constant_return constant235 =null;


        CommonTree EQUAL234_tree=null;
        RewriteRuleTokenStream stream_EQUAL=new RewriteRuleTokenStream(adaptor,"token EQUAL");
        RewriteRuleSubtreeStream stream_constant=new RewriteRuleSubtreeStream(adaptor,"rule constant");
        RewriteRuleSubtreeStream stream_identifier=new RewriteRuleSubtreeStream(adaptor,"rule identifier");
        try {
            // IdentifiersParser.g:452:5: ( identifier ( EQUAL constant )? -> ^( TOK_PARTVAL identifier ( constant )? ) )
            // IdentifiersParser.g:453:5: identifier ( EQUAL constant )?
            {
            pushFollow(FOLLOW_identifier_in_partitionVal3073);
            identifier233=identifier();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_identifier.add(identifier233.getTree());

            // IdentifiersParser.g:453:16: ( EQUAL constant )?
            int alt52=2;
            switch ( input.LA(1) ) {
                case EQUAL:
                    {
                    alt52=1;
                    }
                    break;
            }

            switch (alt52) {
                case 1 :
                    // IdentifiersParser.g:453:17: EQUAL constant
                    {
                    EQUAL234=(Token)match(input,EQUAL,FOLLOW_EQUAL_in_partitionVal3076); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_EQUAL.add(EQUAL234);


                    pushFollow(FOLLOW_constant_in_partitionVal3078);
                    constant235=constant();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_constant.add(constant235.getTree());

                    }
                    break;

            }


            // AST REWRITE
            // elements: constant, identifier
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 453:34: -> ^( TOK_PARTVAL identifier ( constant )? )
            {
                // IdentifiersParser.g:453:37: ^( TOK_PARTVAL identifier ( constant )? )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(TOK_PARTVAL, "TOK_PARTVAL")
                , root_1);

                adaptor.addChild(root_1, stream_identifier.nextTree());

                // IdentifiersParser.g:453:62: ( constant )?
                if ( stream_constant.hasNext() ) {
                    adaptor.addChild(root_1, stream_constant.nextTree());

                }
                stream_constant.reset();

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "partitionVal"


    public static class dropPartitionSpec_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "dropPartitionSpec"
    // IdentifiersParser.g:456:1: dropPartitionSpec : KW_PARTITION LPAREN dropPartitionVal ( COMMA dropPartitionVal )* RPAREN -> ^( TOK_PARTSPEC ( dropPartitionVal )+ ) ;
    public final HiveParser_IdentifiersParser.dropPartitionSpec_return dropPartitionSpec() throws RecognitionException {
        HiveParser_IdentifiersParser.dropPartitionSpec_return retval = new HiveParser_IdentifiersParser.dropPartitionSpec_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token KW_PARTITION236=null;
        Token LPAREN237=null;
        Token COMMA239=null;
        Token RPAREN241=null;
        HiveParser_IdentifiersParser.dropPartitionVal_return dropPartitionVal238 =null;

        HiveParser_IdentifiersParser.dropPartitionVal_return dropPartitionVal240 =null;


        CommonTree KW_PARTITION236_tree=null;
        CommonTree LPAREN237_tree=null;
        CommonTree COMMA239_tree=null;
        CommonTree RPAREN241_tree=null;
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_KW_PARTITION=new RewriteRuleTokenStream(adaptor,"token KW_PARTITION");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleSubtreeStream stream_dropPartitionVal=new RewriteRuleSubtreeStream(adaptor,"rule dropPartitionVal");
        try {
            // IdentifiersParser.g:457:5: ( KW_PARTITION LPAREN dropPartitionVal ( COMMA dropPartitionVal )* RPAREN -> ^( TOK_PARTSPEC ( dropPartitionVal )+ ) )
            // IdentifiersParser.g:458:5: KW_PARTITION LPAREN dropPartitionVal ( COMMA dropPartitionVal )* RPAREN
            {
            KW_PARTITION236=(Token)match(input,KW_PARTITION,FOLLOW_KW_PARTITION_in_dropPartitionSpec3112); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KW_PARTITION.add(KW_PARTITION236);


            LPAREN237=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_dropPartitionSpec3119); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN237);


            pushFollow(FOLLOW_dropPartitionVal_in_dropPartitionSpec3121);
            dropPartitionVal238=dropPartitionVal();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_dropPartitionVal.add(dropPartitionVal238.getTree());

            // IdentifiersParser.g:459:30: ( COMMA dropPartitionVal )*
            loop53:
            do {
                int alt53=2;
                switch ( input.LA(1) ) {
                case COMMA:
                    {
                    alt53=1;
                    }
                    break;

                }

                switch (alt53) {
            	case 1 :
            	    // IdentifiersParser.g:459:31: COMMA dropPartitionVal
            	    {
            	    COMMA239=(Token)match(input,COMMA,FOLLOW_COMMA_in_dropPartitionSpec3124); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA239);


            	    pushFollow(FOLLOW_dropPartitionVal_in_dropPartitionSpec3127);
            	    dropPartitionVal240=dropPartitionVal();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_dropPartitionVal.add(dropPartitionVal240.getTree());

            	    }
            	    break;

            	default :
            	    break loop53;
                }
            } while (true);


            RPAREN241=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_dropPartitionSpec3132); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN241);


            // AST REWRITE
            // elements: dropPartitionVal
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 459:65: -> ^( TOK_PARTSPEC ( dropPartitionVal )+ )
            {
                // IdentifiersParser.g:459:68: ^( TOK_PARTSPEC ( dropPartitionVal )+ )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(TOK_PARTSPEC, "TOK_PARTSPEC")
                , root_1);

                if ( !(stream_dropPartitionVal.hasNext()) ) {
                    throw new RewriteEarlyExitException();
                }
                while ( stream_dropPartitionVal.hasNext() ) {
                    adaptor.addChild(root_1, stream_dropPartitionVal.nextTree());

                }
                stream_dropPartitionVal.reset();

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "dropPartitionSpec"


    public static class dropPartitionVal_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "dropPartitionVal"
    // IdentifiersParser.g:462:1: dropPartitionVal : identifier dropPartitionOperator constant -> ^( TOK_PARTVAL identifier dropPartitionOperator constant ) ;
    public final HiveParser_IdentifiersParser.dropPartitionVal_return dropPartitionVal() throws RecognitionException {
        HiveParser_IdentifiersParser.dropPartitionVal_return retval = new HiveParser_IdentifiersParser.dropPartitionVal_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        HiveParser_IdentifiersParser.identifier_return identifier242 =null;

        HiveParser_IdentifiersParser.dropPartitionOperator_return dropPartitionOperator243 =null;

        HiveParser_IdentifiersParser.constant_return constant244 =null;


        RewriteRuleSubtreeStream stream_dropPartitionOperator=new RewriteRuleSubtreeStream(adaptor,"rule dropPartitionOperator");
        RewriteRuleSubtreeStream stream_constant=new RewriteRuleSubtreeStream(adaptor,"rule constant");
        RewriteRuleSubtreeStream stream_identifier=new RewriteRuleSubtreeStream(adaptor,"rule identifier");
        try {
            // IdentifiersParser.g:463:5: ( identifier dropPartitionOperator constant -> ^( TOK_PARTVAL identifier dropPartitionOperator constant ) )
            // IdentifiersParser.g:464:5: identifier dropPartitionOperator constant
            {
            pushFollow(FOLLOW_identifier_in_dropPartitionVal3163);
            identifier242=identifier();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_identifier.add(identifier242.getTree());

            pushFollow(FOLLOW_dropPartitionOperator_in_dropPartitionVal3165);
            dropPartitionOperator243=dropPartitionOperator();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_dropPartitionOperator.add(dropPartitionOperator243.getTree());

            pushFollow(FOLLOW_constant_in_dropPartitionVal3167);
            constant244=constant();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_constant.add(constant244.getTree());

            // AST REWRITE
            // elements: dropPartitionOperator, constant, identifier
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 464:47: -> ^( TOK_PARTVAL identifier dropPartitionOperator constant )
            {
                // IdentifiersParser.g:464:50: ^( TOK_PARTVAL identifier dropPartitionOperator constant )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(TOK_PARTVAL, "TOK_PARTVAL")
                , root_1);

                adaptor.addChild(root_1, stream_identifier.nextTree());

                adaptor.addChild(root_1, stream_dropPartitionOperator.nextTree());

                adaptor.addChild(root_1, stream_constant.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "dropPartitionVal"


    public static class dropPartitionOperator_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "dropPartitionOperator"
    // IdentifiersParser.g:467:1: dropPartitionOperator : ( EQUAL | NOTEQUAL | LESSTHANOREQUALTO | LESSTHAN | GREATERTHANOREQUALTO | GREATERTHAN );
    public final HiveParser_IdentifiersParser.dropPartitionOperator_return dropPartitionOperator() throws RecognitionException {
        HiveParser_IdentifiersParser.dropPartitionOperator_return retval = new HiveParser_IdentifiersParser.dropPartitionOperator_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token set245=null;

        CommonTree set245_tree=null;

        try {
            // IdentifiersParser.g:468:5: ( EQUAL | NOTEQUAL | LESSTHANOREQUALTO | LESSTHAN | GREATERTHANOREQUALTO | GREATERTHAN )
            // IdentifiersParser.g:
            {
            root_0 = (CommonTree)adaptor.nil();


            set245=(Token)input.LT(1);

            if ( input.LA(1)==EQUAL||(input.LA(1) >= GREATERTHAN && input.LA(1) <= GREATERTHANOREQUALTO)||(input.LA(1) >= LESSTHAN && input.LA(1) <= LESSTHANOREQUALTO)||input.LA(1)==NOTEQUAL ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, 
                (CommonTree)adaptor.create(set245)
                );
                state.errorRecovery=false;
                state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "dropPartitionOperator"


    public static class sysFuncNames_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "sysFuncNames"
    // IdentifiersParser.g:472:1: sysFuncNames : ( KW_AND | KW_OR | KW_NOT | KW_LIKE | KW_IF | KW_CASE | KW_WHEN | KW_TINYINT | KW_SMALLINT | KW_INT | KW_BIGINT | KW_FLOAT | KW_DOUBLE | KW_BOOLEAN | KW_STRING | KW_BINARY | KW_ARRAY | KW_MAP | KW_STRUCT | KW_UNIONTYPE | EQUAL | EQUAL_NS | NOTEQUAL | LESSTHANOREQUALTO | LESSTHAN | GREATERTHANOREQUALTO | GREATERTHAN | DIVIDE | PLUS | MINUS | STAR | MOD | DIV | AMPERSAND | TILDE | BITWISEOR | BITWISEXOR | KW_RLIKE | KW_REGEXP | KW_IN | KW_BETWEEN );
    public final HiveParser_IdentifiersParser.sysFuncNames_return sysFuncNames() throws RecognitionException {
        HiveParser_IdentifiersParser.sysFuncNames_return retval = new HiveParser_IdentifiersParser.sysFuncNames_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token set246=null;

        CommonTree set246_tree=null;

        try {
            // IdentifiersParser.g:473:5: ( KW_AND | KW_OR | KW_NOT | KW_LIKE | KW_IF | KW_CASE | KW_WHEN | KW_TINYINT | KW_SMALLINT | KW_INT | KW_BIGINT | KW_FLOAT | KW_DOUBLE | KW_BOOLEAN | KW_STRING | KW_BINARY | KW_ARRAY | KW_MAP | KW_STRUCT | KW_UNIONTYPE | EQUAL | EQUAL_NS | NOTEQUAL | LESSTHANOREQUALTO | LESSTHAN | GREATERTHANOREQUALTO | GREATERTHAN | DIVIDE | PLUS | MINUS | STAR | MOD | DIV | AMPERSAND | TILDE | BITWISEOR | BITWISEXOR | KW_RLIKE | KW_REGEXP | KW_IN | KW_BETWEEN )
            // IdentifiersParser.g:
            {
            root_0 = (CommonTree)adaptor.nil();


            set246=(Token)input.LT(1);

            if ( (input.LA(1) >= AMPERSAND && input.LA(1) <= BITWISEXOR)||(input.LA(1) >= DIV && input.LA(1) <= DIVIDE)||(input.LA(1) >= EQUAL && input.LA(1) <= EQUAL_NS)||(input.LA(1) >= GREATERTHAN && input.LA(1) <= GREATERTHANOREQUALTO)||input.LA(1)==KW_AND||input.LA(1)==KW_ARRAY||(input.LA(1) >= KW_BETWEEN && input.LA(1) <= KW_BOOLEAN)||input.LA(1)==KW_CASE||input.LA(1)==KW_DOUBLE||input.LA(1)==KW_FLOAT||input.LA(1)==KW_IF||input.LA(1)==KW_IN||input.LA(1)==KW_INT||input.LA(1)==KW_LIKE||input.LA(1)==KW_MAP||input.LA(1)==KW_NOT||input.LA(1)==KW_OR||input.LA(1)==KW_REGEXP||input.LA(1)==KW_RLIKE||input.LA(1)==KW_SMALLINT||(input.LA(1) >= KW_STRING && input.LA(1) <= KW_STRUCT)||input.LA(1)==KW_TINYINT||input.LA(1)==KW_UNIONTYPE||input.LA(1)==KW_WHEN||(input.LA(1) >= LESSTHAN && input.LA(1) <= LESSTHANOREQUALTO)||(input.LA(1) >= MINUS && input.LA(1) <= NOTEQUAL)||input.LA(1)==PLUS||input.LA(1)==STAR||input.LA(1)==TILDE ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, 
                (CommonTree)adaptor.create(set246)
                );
                state.errorRecovery=false;
                state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "sysFuncNames"


    public static class descFuncNames_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "descFuncNames"
    // IdentifiersParser.g:517:1: descFuncNames : ( sysFuncNames | StringLiteral | functionIdentifier );
    public final HiveParser_IdentifiersParser.descFuncNames_return descFuncNames() throws RecognitionException {
        HiveParser_IdentifiersParser.descFuncNames_return retval = new HiveParser_IdentifiersParser.descFuncNames_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token StringLiteral248=null;
        HiveParser_IdentifiersParser.sysFuncNames_return sysFuncNames247 =null;

        HiveParser_IdentifiersParser.functionIdentifier_return functionIdentifier249 =null;


        CommonTree StringLiteral248_tree=null;

        try {
            // IdentifiersParser.g:518:5: ( sysFuncNames | StringLiteral | functionIdentifier )
            int alt54=3;
            switch ( input.LA(1) ) {
            case AMPERSAND:
            case BITWISEOR:
            case BITWISEXOR:
            case DIV:
            case DIVIDE:
            case EQUAL:
            case EQUAL_NS:
            case GREATERTHAN:
            case GREATERTHANOREQUALTO:
            case KW_AND:
            case KW_ARRAY:
            case KW_BETWEEN:
            case KW_BIGINT:
            case KW_BINARY:
            case KW_BOOLEAN:
            case KW_CASE:
            case KW_DOUBLE:
            case KW_FLOAT:
            case KW_IF:
            case KW_IN:
            case KW_INT:
            case KW_LIKE:
            case KW_MAP:
            case KW_NOT:
            case KW_OR:
            case KW_REGEXP:
            case KW_RLIKE:
            case KW_SMALLINT:
            case KW_STRING:
            case KW_STRUCT:
            case KW_TINYINT:
            case KW_UNIONTYPE:
            case KW_WHEN:
            case LESSTHAN:
            case LESSTHANOREQUALTO:
            case MINUS:
            case MOD:
            case NOTEQUAL:
            case PLUS:
            case STAR:
            case TILDE:
                {
                alt54=1;
                }
                break;
            case StringLiteral:
                {
                alt54=2;
                }
                break;
            case Identifier:
            case KW_ADD:
            case KW_ADMIN:
            case KW_AFTER:
            case KW_ALL:
            case KW_ALTER:
            case KW_ANALYZE:
            case KW_ARCHIVE:
            case KW_AS:
            case KW_ASC:
            case KW_AUTHORIZATION:
            case KW_BEFORE:
            case KW_BOTH:
            case KW_BUCKET:
            case KW_BUCKETS:
            case KW_BY:
            case KW_CASCADE:
            case KW_CHANGE:
            case KW_CLUSTER:
            case KW_CLUSTERED:
            case KW_CLUSTERSTATUS:
            case KW_COLLECTION:
            case KW_COLUMNS:
            case KW_COMMENT:
            case KW_COMPACT:
            case KW_COMPACTIONS:
            case KW_COMPUTE:
            case KW_CONCATENATE:
            case KW_CONTINUE:
            case KW_CREATE:
            case KW_CUBE:
            case KW_CURSOR:
            case KW_DATA:
            case KW_DATABASES:
            case KW_DATE:
            case KW_DATETIME:
            case KW_DBPROPERTIES:
            case KW_DECIMAL:
            case KW_DEFAULT:
            case KW_DEFERRED:
            case KW_DEFINED:
            case KW_DELETE:
            case KW_DELIMITED:
            case KW_DEPENDENCY:
            case KW_DESC:
            case KW_DESCRIBE:
            case KW_DIRECTORIES:
            case KW_DIRECTORY:
            case KW_DISABLE:
            case KW_DISTRIBUTE:
            case KW_DROP:
            case KW_ELEM_TYPE:
            case KW_ENABLE:
            case KW_ESCAPED:
            case KW_EXCLUSIVE:
            case KW_EXISTS:
            case KW_EXPLAIN:
            case KW_EXPORT:
            case KW_EXTERNAL:
            case KW_FALSE:
            case KW_FETCH:
            case KW_FIELDS:
            case KW_FILE:
            case KW_FILEFORMAT:
            case KW_FIRST:
            case KW_FOR:
            case KW_FORMAT:
            case KW_FORMATTED:
            case KW_FULL:
            case KW_FUNCTIONS:
            case KW_GRANT:
            case KW_GROUP:
            case KW_GROUPING:
            case KW_HOLD_DDLTIME:
            case KW_IDXPROPERTIES:
            case KW_IGNORE:
            case KW_IMPORT:
            case KW_INDEX:
            case KW_INDEXES:
            case KW_INNER:
            case KW_INPATH:
            case KW_INPUTDRIVER:
            case KW_INPUTFORMAT:
            case KW_INSERT:
            case KW_INTERSECT:
            case KW_INTO:
            case KW_IS:
            case KW_ITEMS:
            case KW_JAR:
            case KW_KEYS:
            case KW_KEY_TYPE:
            case KW_LATERAL:
            case KW_LEFT:
            case KW_LIMIT:
            case KW_LINES:
            case KW_LOAD:
            case KW_LOCAL:
            case KW_LOCATION:
            case KW_LOCK:
            case KW_LOCKS:
            case KW_LOGICAL:
            case KW_LONG:
            case KW_MAPJOIN:
            case KW_MATERIALIZED:
            case KW_MINUS:
            case KW_MSCK:
            case KW_NONE:
            case KW_NOSCAN:
            case KW_NO_DROP:
            case KW_NULL:
            case KW_OF:
            case KW_OFFLINE:
            case KW_OPTION:
            case KW_ORDER:
            case KW_OUT:
            case KW_OUTER:
            case KW_OUTPUTDRIVER:
            case KW_OUTPUTFORMAT:
            case KW_OVERWRITE:
            case KW_OWNER:
            case KW_PARTITION:
            case KW_PARTITIONED:
            case KW_PARTITIONS:
            case KW_PERCENT:
            case KW_PLUS:
            case KW_PRETTY:
            case KW_PRINCIPALS:
            case KW_PROCEDURE:
            case KW_PROTECTION:
            case KW_PURGE:
            case KW_RANGE:
            case KW_READ:
            case KW_READONLY:
            case KW_READS:
            case KW_REBUILD:
            case KW_RECORDREADER:
            case KW_RECORDWRITER:
            case KW_RENAME:
            case KW_REPAIR:
            case KW_REPLACE:
            case KW_RESTRICT:
            case KW_REVOKE:
            case KW_REWRITE:
            case KW_RIGHT:
            case KW_ROLE:
            case KW_ROLES:
            case KW_ROLLUP:
            case KW_ROW:
            case KW_ROWS:
            case KW_SCHEMA:
            case KW_SCHEMAS:
            case KW_SEMI:
            case KW_SERDE:
            case KW_SERDEPROPERTIES:
            case KW_SET:
            case KW_SETS:
            case KW_SHARED:
            case KW_SHOW:
            case KW_SHOW_DATABASE:
            case KW_SKEWED:
            case KW_SORT:
            case KW_SORTED:
            case KW_SSL:
            case KW_STATISTICS:
            case KW_STORED:
            case KW_STREAMTABLE:
            case KW_TABLE:
            case KW_TABLES:
            case KW_TBLPROPERTIES:
            case KW_TEMPORARY:
            case KW_TERMINATED:
            case KW_TIMESTAMP:
            case KW_TO:
            case KW_TOUCH:
            case KW_TRANSACTIONS:
            case KW_TRIGGER:
            case KW_TRUE:
            case KW_TRUNCATE:
            case KW_UNARCHIVE:
            case KW_UNDO:
            case KW_UNION:
            case KW_UNLOCK:
            case KW_UNSET:
            case KW_UNSIGNED:
            case KW_UPDATE:
            case KW_USE:
            case KW_USER:
            case KW_USING:
            case KW_UTC:
            case KW_UTCTIMESTAMP:
            case KW_VALUES:
            case KW_VALUE_TYPE:
            case KW_VIEW:
            case KW_WHILE:
            case KW_WITH:
                {
                alt54=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 54, 0, input);

                throw nvae;

            }

            switch (alt54) {
                case 1 :
                    // IdentifiersParser.g:519:7: sysFuncNames
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_sysFuncNames_in_descFuncNames3586);
                    sysFuncNames247=sysFuncNames();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, sysFuncNames247.getTree());

                    }
                    break;
                case 2 :
                    // IdentifiersParser.g:520:7: StringLiteral
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    StringLiteral248=(Token)match(input,StringLiteral,FOLLOW_StringLiteral_in_descFuncNames3594); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    StringLiteral248_tree = 
                    (CommonTree)adaptor.create(StringLiteral248)
                    ;
                    adaptor.addChild(root_0, StringLiteral248_tree);
                    }

                    }
                    break;
                case 3 :
                    // IdentifiersParser.g:521:7: functionIdentifier
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_functionIdentifier_in_descFuncNames3602);
                    functionIdentifier249=functionIdentifier();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, functionIdentifier249.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "descFuncNames"


    public static class identifier_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "identifier"
    // IdentifiersParser.g:524:1: identifier : ( Identifier | nonReserved -> Identifier[$nonReserved.text] );
    public final HiveParser_IdentifiersParser.identifier_return identifier() throws RecognitionException {
        HiveParser_IdentifiersParser.identifier_return retval = new HiveParser_IdentifiersParser.identifier_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token Identifier250=null;
        HiveParser_IdentifiersParser.nonReserved_return nonReserved251 =null;


        CommonTree Identifier250_tree=null;
        RewriteRuleSubtreeStream stream_nonReserved=new RewriteRuleSubtreeStream(adaptor,"rule nonReserved");
        try {
            // IdentifiersParser.g:525:5: ( Identifier | nonReserved -> Identifier[$nonReserved.text] )
            int alt55=2;
            switch ( input.LA(1) ) {
            case Identifier:
                {
                alt55=1;
                }
                break;
            case KW_ADD:
            case KW_ADMIN:
            case KW_AFTER:
            case KW_ALL:
            case KW_ALTER:
            case KW_ANALYZE:
            case KW_ARCHIVE:
            case KW_ARRAY:
            case KW_AS:
            case KW_ASC:
            case KW_AUTHORIZATION:
            case KW_BEFORE:
            case KW_BETWEEN:
            case KW_BIGINT:
            case KW_BINARY:
            case KW_BOOLEAN:
            case KW_BOTH:
            case KW_BUCKET:
            case KW_BUCKETS:
            case KW_BY:
            case KW_CASCADE:
            case KW_CHANGE:
            case KW_CLUSTER:
            case KW_CLUSTERED:
            case KW_CLUSTERSTATUS:
            case KW_COLLECTION:
            case KW_COLUMNS:
            case KW_COMMENT:
            case KW_COMPACT:
            case KW_COMPACTIONS:
            case KW_COMPUTE:
            case KW_CONCATENATE:
            case KW_CONTINUE:
            case KW_CREATE:
            case KW_CUBE:
            case KW_CURSOR:
            case KW_DATA:
            case KW_DATABASES:
            case KW_DATE:
            case KW_DATETIME:
            case KW_DBPROPERTIES:
            case KW_DECIMAL:
            case KW_DEFAULT:
            case KW_DEFERRED:
            case KW_DEFINED:
            case KW_DELETE:
            case KW_DELIMITED:
            case KW_DEPENDENCY:
            case KW_DESC:
            case KW_DESCRIBE:
            case KW_DIRECTORIES:
            case KW_DIRECTORY:
            case KW_DISABLE:
            case KW_DISTRIBUTE:
            case KW_DOUBLE:
            case KW_DROP:
            case KW_ELEM_TYPE:
            case KW_ENABLE:
            case KW_ESCAPED:
            case KW_EXCLUSIVE:
            case KW_EXISTS:
            case KW_EXPLAIN:
            case KW_EXPORT:
            case KW_EXTERNAL:
            case KW_FALSE:
            case KW_FETCH:
            case KW_FIELDS:
            case KW_FILE:
            case KW_FILEFORMAT:
            case KW_FIRST:
            case KW_FLOAT:
            case KW_FOR:
            case KW_FORMAT:
            case KW_FORMATTED:
            case KW_FULL:
            case KW_FUNCTIONS:
            case KW_GRANT:
            case KW_GROUP:
            case KW_GROUPING:
            case KW_HOLD_DDLTIME:
            case KW_IDXPROPERTIES:
            case KW_IGNORE:
            case KW_IMPORT:
            case KW_IN:
            case KW_INDEX:
            case KW_INDEXES:
            case KW_INNER:
            case KW_INPATH:
            case KW_INPUTDRIVER:
            case KW_INPUTFORMAT:
            case KW_INSERT:
            case KW_INT:
            case KW_INTERSECT:
            case KW_INTO:
            case KW_IS:
            case KW_ITEMS:
            case KW_JAR:
            case KW_KEYS:
            case KW_KEY_TYPE:
            case KW_LATERAL:
            case KW_LEFT:
            case KW_LIKE:
            case KW_LIMIT:
            case KW_LINES:
            case KW_LOAD:
            case KW_LOCAL:
            case KW_LOCATION:
            case KW_LOCK:
            case KW_LOCKS:
            case KW_LOGICAL:
            case KW_LONG:
            case KW_MAPJOIN:
            case KW_MATERIALIZED:
            case KW_MINUS:
            case KW_MSCK:
            case KW_NONE:
            case KW_NOSCAN:
            case KW_NO_DROP:
            case KW_NULL:
            case KW_OF:
            case KW_OFFLINE:
            case KW_OPTION:
            case KW_ORDER:
            case KW_OUT:
            case KW_OUTER:
            case KW_OUTPUTDRIVER:
            case KW_OUTPUTFORMAT:
            case KW_OVERWRITE:
            case KW_OWNER:
            case KW_PARTITION:
            case KW_PARTITIONED:
            case KW_PARTITIONS:
            case KW_PERCENT:
            case KW_PLUS:
            case KW_PRETTY:
            case KW_PRINCIPALS:
            case KW_PROCEDURE:
            case KW_PROTECTION:
            case KW_PURGE:
            case KW_RANGE:
            case KW_READ:
            case KW_READONLY:
            case KW_READS:
            case KW_REBUILD:
            case KW_RECORDREADER:
            case KW_RECORDWRITER:
            case KW_REGEXP:
            case KW_RENAME:
            case KW_REPAIR:
            case KW_REPLACE:
            case KW_RESTRICT:
            case KW_REVOKE:
            case KW_REWRITE:
            case KW_RIGHT:
            case KW_RLIKE:
            case KW_ROLE:
            case KW_ROLES:
            case KW_ROLLUP:
            case KW_ROW:
            case KW_ROWS:
            case KW_SCHEMA:
            case KW_SCHEMAS:
            case KW_SEMI:
            case KW_SERDE:
            case KW_SERDEPROPERTIES:
            case KW_SET:
            case KW_SETS:
            case KW_SHARED:
            case KW_SHOW:
            case KW_SHOW_DATABASE:
            case KW_SKEWED:
            case KW_SMALLINT:
            case KW_SORT:
            case KW_SORTED:
            case KW_SSL:
            case KW_STATISTICS:
            case KW_STORED:
            case KW_STREAMTABLE:
            case KW_STRING:
            case KW_STRUCT:
            case KW_TABLE:
            case KW_TABLES:
            case KW_TBLPROPERTIES:
            case KW_TEMPORARY:
            case KW_TERMINATED:
            case KW_TIMESTAMP:
            case KW_TINYINT:
            case KW_TO:
            case KW_TOUCH:
            case KW_TRANSACTIONS:
            case KW_TRIGGER:
            case KW_TRUE:
            case KW_TRUNCATE:
            case KW_UNARCHIVE:
            case KW_UNDO:
            case KW_UNION:
            case KW_UNIONTYPE:
            case KW_UNLOCK:
            case KW_UNSET:
            case KW_UNSIGNED:
            case KW_UPDATE:
            case KW_USE:
            case KW_USER:
            case KW_USING:
            case KW_UTC:
            case KW_UTCTIMESTAMP:
            case KW_VALUES:
            case KW_VALUE_TYPE:
            case KW_VIEW:
            case KW_WHILE:
            case KW_WITH:
                {
                alt55=2;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 55, 0, input);

                throw nvae;

            }

            switch (alt55) {
                case 1 :
                    // IdentifiersParser.g:526:5: Identifier
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    Identifier250=(Token)match(input,Identifier,FOLLOW_Identifier_in_identifier3623); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    Identifier250_tree = 
                    (CommonTree)adaptor.create(Identifier250)
                    ;
                    adaptor.addChild(root_0, Identifier250_tree);
                    }

                    }
                    break;
                case 2 :
                    // IdentifiersParser.g:527:7: nonReserved
                    {
                    pushFollow(FOLLOW_nonReserved_in_identifier3631);
                    nonReserved251=nonReserved();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_nonReserved.add(nonReserved251.getTree());

                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 527:19: -> Identifier[$nonReserved.text]
                    {
                        adaptor.addChild(root_0, 
                        (CommonTree)adaptor.create(Identifier, (nonReserved251!=null?input.toString(nonReserved251.start,nonReserved251.stop):null))
                        );

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "identifier"


    public static class functionIdentifier_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "functionIdentifier"
    // IdentifiersParser.g:530:1: functionIdentifier : (db= identifier DOT fn= identifier -> Identifier[$db.text + \".\" + $fn.text] | identifier );
    public final HiveParser_IdentifiersParser.functionIdentifier_return functionIdentifier() throws RecognitionException {
        HiveParser_IdentifiersParser.functionIdentifier_return retval = new HiveParser_IdentifiersParser.functionIdentifier_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token DOT252=null;
        HiveParser_IdentifiersParser.identifier_return db =null;

        HiveParser_IdentifiersParser.identifier_return fn =null;

        HiveParser_IdentifiersParser.identifier_return identifier253 =null;


        CommonTree DOT252_tree=null;
        RewriteRuleTokenStream stream_DOT=new RewriteRuleTokenStream(adaptor,"token DOT");
        RewriteRuleSubtreeStream stream_identifier=new RewriteRuleSubtreeStream(adaptor,"rule identifier");
         gParent.pushMsg("function identifier", state); 
        try {
            // IdentifiersParser.g:533:5: (db= identifier DOT fn= identifier -> Identifier[$db.text + \".\" + $fn.text] | identifier )
            int alt56=2;
            switch ( input.LA(1) ) {
            case Identifier:
                {
                switch ( input.LA(2) ) {
                case DOT:
                    {
                    alt56=1;
                    }
                    break;
                case EOF:
                case KW_AS:
                case LPAREN:
                    {
                    alt56=2;
                    }
                    break;
                default:
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 56, 1, input);

                    throw nvae;

                }

                }
                break;
            case KW_ADD:
            case KW_ADMIN:
            case KW_AFTER:
            case KW_ALL:
            case KW_ALTER:
            case KW_ANALYZE:
            case KW_ARCHIVE:
            case KW_ARRAY:
            case KW_AS:
            case KW_ASC:
            case KW_AUTHORIZATION:
            case KW_BEFORE:
            case KW_BETWEEN:
            case KW_BIGINT:
            case KW_BINARY:
            case KW_BOOLEAN:
            case KW_BOTH:
            case KW_BUCKET:
            case KW_BUCKETS:
            case KW_BY:
            case KW_CASCADE:
            case KW_CHANGE:
            case KW_CLUSTER:
            case KW_CLUSTERED:
            case KW_CLUSTERSTATUS:
            case KW_COLLECTION:
            case KW_COLUMNS:
            case KW_COMMENT:
            case KW_COMPACT:
            case KW_COMPACTIONS:
            case KW_COMPUTE:
            case KW_CONCATENATE:
            case KW_CONTINUE:
            case KW_CREATE:
            case KW_CUBE:
            case KW_CURSOR:
            case KW_DATA:
            case KW_DATABASES:
            case KW_DATE:
            case KW_DATETIME:
            case KW_DBPROPERTIES:
            case KW_DECIMAL:
            case KW_DEFAULT:
            case KW_DEFERRED:
            case KW_DEFINED:
            case KW_DELETE:
            case KW_DELIMITED:
            case KW_DEPENDENCY:
            case KW_DESC:
            case KW_DESCRIBE:
            case KW_DIRECTORIES:
            case KW_DIRECTORY:
            case KW_DISABLE:
            case KW_DISTRIBUTE:
            case KW_DOUBLE:
            case KW_DROP:
            case KW_ELEM_TYPE:
            case KW_ENABLE:
            case KW_ESCAPED:
            case KW_EXCLUSIVE:
            case KW_EXISTS:
            case KW_EXPLAIN:
            case KW_EXPORT:
            case KW_EXTERNAL:
            case KW_FALSE:
            case KW_FETCH:
            case KW_FIELDS:
            case KW_FILE:
            case KW_FILEFORMAT:
            case KW_FIRST:
            case KW_FLOAT:
            case KW_FOR:
            case KW_FORMAT:
            case KW_FORMATTED:
            case KW_FULL:
            case KW_FUNCTIONS:
            case KW_GRANT:
            case KW_GROUP:
            case KW_GROUPING:
            case KW_HOLD_DDLTIME:
            case KW_IDXPROPERTIES:
            case KW_IGNORE:
            case KW_IMPORT:
            case KW_IN:
            case KW_INDEX:
            case KW_INDEXES:
            case KW_INNER:
            case KW_INPATH:
            case KW_INPUTDRIVER:
            case KW_INPUTFORMAT:
            case KW_INSERT:
            case KW_INT:
            case KW_INTERSECT:
            case KW_INTO:
            case KW_IS:
            case KW_ITEMS:
            case KW_JAR:
            case KW_KEYS:
            case KW_KEY_TYPE:
            case KW_LATERAL:
            case KW_LEFT:
            case KW_LIKE:
            case KW_LIMIT:
            case KW_LINES:
            case KW_LOAD:
            case KW_LOCAL:
            case KW_LOCATION:
            case KW_LOCK:
            case KW_LOCKS:
            case KW_LOGICAL:
            case KW_LONG:
            case KW_MAPJOIN:
            case KW_MATERIALIZED:
            case KW_MINUS:
            case KW_MSCK:
            case KW_NONE:
            case KW_NOSCAN:
            case KW_NO_DROP:
            case KW_NULL:
            case KW_OF:
            case KW_OFFLINE:
            case KW_OPTION:
            case KW_ORDER:
            case KW_OUT:
            case KW_OUTER:
            case KW_OUTPUTDRIVER:
            case KW_OUTPUTFORMAT:
            case KW_OVERWRITE:
            case KW_OWNER:
            case KW_PARTITION:
            case KW_PARTITIONED:
            case KW_PARTITIONS:
            case KW_PERCENT:
            case KW_PLUS:
            case KW_PRETTY:
            case KW_PRINCIPALS:
            case KW_PROCEDURE:
            case KW_PROTECTION:
            case KW_PURGE:
            case KW_RANGE:
            case KW_READ:
            case KW_READONLY:
            case KW_READS:
            case KW_REBUILD:
            case KW_RECORDREADER:
            case KW_RECORDWRITER:
            case KW_REGEXP:
            case KW_RENAME:
            case KW_REPAIR:
            case KW_REPLACE:
            case KW_RESTRICT:
            case KW_REVOKE:
            case KW_REWRITE:
            case KW_RIGHT:
            case KW_RLIKE:
            case KW_ROLE:
            case KW_ROLES:
            case KW_ROLLUP:
            case KW_ROW:
            case KW_ROWS:
            case KW_SCHEMA:
            case KW_SCHEMAS:
            case KW_SEMI:
            case KW_SERDE:
            case KW_SERDEPROPERTIES:
            case KW_SET:
            case KW_SETS:
            case KW_SHARED:
            case KW_SHOW:
            case KW_SHOW_DATABASE:
            case KW_SKEWED:
            case KW_SMALLINT:
            case KW_SORT:
            case KW_SORTED:
            case KW_SSL:
            case KW_STATISTICS:
            case KW_STORED:
            case KW_STREAMTABLE:
            case KW_STRING:
            case KW_STRUCT:
            case KW_TABLE:
            case KW_TABLES:
            case KW_TBLPROPERTIES:
            case KW_TEMPORARY:
            case KW_TERMINATED:
            case KW_TIMESTAMP:
            case KW_TINYINT:
            case KW_TO:
            case KW_TOUCH:
            case KW_TRANSACTIONS:
            case KW_TRIGGER:
            case KW_TRUE:
            case KW_TRUNCATE:
            case KW_UNARCHIVE:
            case KW_UNDO:
            case KW_UNION:
            case KW_UNIONTYPE:
            case KW_UNLOCK:
            case KW_UNSET:
            case KW_UNSIGNED:
            case KW_UPDATE:
            case KW_USE:
            case KW_USER:
            case KW_USING:
            case KW_UTC:
            case KW_UTCTIMESTAMP:
            case KW_VALUES:
            case KW_VALUE_TYPE:
            case KW_VIEW:
            case KW_WHILE:
            case KW_WITH:
                {
                switch ( input.LA(2) ) {
                case DOT:
                    {
                    alt56=1;
                    }
                    break;
                case EOF:
                case KW_AS:
                case LPAREN:
                    {
                    alt56=2;
                    }
                    break;
                default:
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 56, 2, input);

                    throw nvae;

                }

                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 56, 0, input);

                throw nvae;

            }

            switch (alt56) {
                case 1 :
                    // IdentifiersParser.g:533:7: db= identifier DOT fn= identifier
                    {
                    pushFollow(FOLLOW_identifier_in_functionIdentifier3665);
                    db=identifier();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_identifier.add(db.getTree());

                    DOT252=(Token)match(input,DOT,FOLLOW_DOT_in_functionIdentifier3667); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_DOT.add(DOT252);


                    pushFollow(FOLLOW_identifier_in_functionIdentifier3671);
                    fn=identifier();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_identifier.add(fn.getTree());

                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 534:5: -> Identifier[$db.text + \".\" + $fn.text]
                    {
                        adaptor.addChild(root_0, 
                        (CommonTree)adaptor.create(Identifier, (db!=null?input.toString(db.start,db.stop):null) + "." + (fn!=null?input.toString(fn.start,fn.stop):null))
                        );

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 2 :
                    // IdentifiersParser.g:536:5: identifier
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_identifier_in_functionIdentifier3692);
                    identifier253=identifier();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, identifier253.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
            if ( state.backtracking==0 ) { gParent.popMsg(state); }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "functionIdentifier"


    public static class principalIdentifier_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "principalIdentifier"
    // IdentifiersParser.g:539:1: principalIdentifier : ( identifier | QuotedIdentifier );
    public final HiveParser_IdentifiersParser.principalIdentifier_return principalIdentifier() throws RecognitionException {
        HiveParser_IdentifiersParser.principalIdentifier_return retval = new HiveParser_IdentifiersParser.principalIdentifier_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token QuotedIdentifier255=null;
        HiveParser_IdentifiersParser.identifier_return identifier254 =null;


        CommonTree QuotedIdentifier255_tree=null;

         gParent.pushMsg("identifier for principal spec", state); 
        try {
            // IdentifiersParser.g:542:5: ( identifier | QuotedIdentifier )
            int alt57=2;
            switch ( input.LA(1) ) {
            case Identifier:
            case KW_ADD:
            case KW_ADMIN:
            case KW_AFTER:
            case KW_ALL:
            case KW_ALTER:
            case KW_ANALYZE:
            case KW_ARCHIVE:
            case KW_ARRAY:
            case KW_AS:
            case KW_ASC:
            case KW_AUTHORIZATION:
            case KW_BEFORE:
            case KW_BETWEEN:
            case KW_BIGINT:
            case KW_BINARY:
            case KW_BOOLEAN:
            case KW_BOTH:
            case KW_BUCKET:
            case KW_BUCKETS:
            case KW_BY:
            case KW_CASCADE:
            case KW_CHANGE:
            case KW_CLUSTER:
            case KW_CLUSTERED:
            case KW_CLUSTERSTATUS:
            case KW_COLLECTION:
            case KW_COLUMNS:
            case KW_COMMENT:
            case KW_COMPACT:
            case KW_COMPACTIONS:
            case KW_COMPUTE:
            case KW_CONCATENATE:
            case KW_CONTINUE:
            case KW_CREATE:
            case KW_CUBE:
            case KW_CURSOR:
            case KW_DATA:
            case KW_DATABASES:
            case KW_DATE:
            case KW_DATETIME:
            case KW_DBPROPERTIES:
            case KW_DECIMAL:
            case KW_DEFAULT:
            case KW_DEFERRED:
            case KW_DEFINED:
            case KW_DELETE:
            case KW_DELIMITED:
            case KW_DEPENDENCY:
            case KW_DESC:
            case KW_DESCRIBE:
            case KW_DIRECTORIES:
            case KW_DIRECTORY:
            case KW_DISABLE:
            case KW_DISTRIBUTE:
            case KW_DOUBLE:
            case KW_DROP:
            case KW_ELEM_TYPE:
            case KW_ENABLE:
            case KW_ESCAPED:
            case KW_EXCLUSIVE:
            case KW_EXISTS:
            case KW_EXPLAIN:
            case KW_EXPORT:
            case KW_EXTERNAL:
            case KW_FALSE:
            case KW_FETCH:
            case KW_FIELDS:
            case KW_FILE:
            case KW_FILEFORMAT:
            case KW_FIRST:
            case KW_FLOAT:
            case KW_FOR:
            case KW_FORMAT:
            case KW_FORMATTED:
            case KW_FULL:
            case KW_FUNCTIONS:
            case KW_GRANT:
            case KW_GROUP:
            case KW_GROUPING:
            case KW_HOLD_DDLTIME:
            case KW_IDXPROPERTIES:
            case KW_IGNORE:
            case KW_IMPORT:
            case KW_IN:
            case KW_INDEX:
            case KW_INDEXES:
            case KW_INNER:
            case KW_INPATH:
            case KW_INPUTDRIVER:
            case KW_INPUTFORMAT:
            case KW_INSERT:
            case KW_INT:
            case KW_INTERSECT:
            case KW_INTO:
            case KW_IS:
            case KW_ITEMS:
            case KW_JAR:
            case KW_KEYS:
            case KW_KEY_TYPE:
            case KW_LATERAL:
            case KW_LEFT:
            case KW_LIKE:
            case KW_LIMIT:
            case KW_LINES:
            case KW_LOAD:
            case KW_LOCAL:
            case KW_LOCATION:
            case KW_LOCK:
            case KW_LOCKS:
            case KW_LOGICAL:
            case KW_LONG:
            case KW_MAPJOIN:
            case KW_MATERIALIZED:
            case KW_MINUS:
            case KW_MSCK:
            case KW_NONE:
            case KW_NOSCAN:
            case KW_NO_DROP:
            case KW_NULL:
            case KW_OF:
            case KW_OFFLINE:
            case KW_OPTION:
            case KW_ORDER:
            case KW_OUT:
            case KW_OUTER:
            case KW_OUTPUTDRIVER:
            case KW_OUTPUTFORMAT:
            case KW_OVERWRITE:
            case KW_OWNER:
            case KW_PARTITION:
            case KW_PARTITIONED:
            case KW_PARTITIONS:
            case KW_PERCENT:
            case KW_PLUS:
            case KW_PRETTY:
            case KW_PRINCIPALS:
            case KW_PROCEDURE:
            case KW_PROTECTION:
            case KW_PURGE:
            case KW_RANGE:
            case KW_READ:
            case KW_READONLY:
            case KW_READS:
            case KW_REBUILD:
            case KW_RECORDREADER:
            case KW_RECORDWRITER:
            case KW_REGEXP:
            case KW_RENAME:
            case KW_REPAIR:
            case KW_REPLACE:
            case KW_RESTRICT:
            case KW_REVOKE:
            case KW_REWRITE:
            case KW_RIGHT:
            case KW_RLIKE:
            case KW_ROLE:
            case KW_ROLES:
            case KW_ROLLUP:
            case KW_ROW:
            case KW_ROWS:
            case KW_SCHEMA:
            case KW_SCHEMAS:
            case KW_SEMI:
            case KW_SERDE:
            case KW_SERDEPROPERTIES:
            case KW_SET:
            case KW_SETS:
            case KW_SHARED:
            case KW_SHOW:
            case KW_SHOW_DATABASE:
            case KW_SKEWED:
            case KW_SMALLINT:
            case KW_SORT:
            case KW_SORTED:
            case KW_SSL:
            case KW_STATISTICS:
            case KW_STORED:
            case KW_STREAMTABLE:
            case KW_STRING:
            case KW_STRUCT:
            case KW_TABLE:
            case KW_TABLES:
            case KW_TBLPROPERTIES:
            case KW_TEMPORARY:
            case KW_TERMINATED:
            case KW_TIMESTAMP:
            case KW_TINYINT:
            case KW_TO:
            case KW_TOUCH:
            case KW_TRANSACTIONS:
            case KW_TRIGGER:
            case KW_TRUE:
            case KW_TRUNCATE:
            case KW_UNARCHIVE:
            case KW_UNDO:
            case KW_UNION:
            case KW_UNIONTYPE:
            case KW_UNLOCK:
            case KW_UNSET:
            case KW_UNSIGNED:
            case KW_UPDATE:
            case KW_USE:
            case KW_USER:
            case KW_USING:
            case KW_UTC:
            case KW_UTCTIMESTAMP:
            case KW_VALUES:
            case KW_VALUE_TYPE:
            case KW_VIEW:
            case KW_WHILE:
            case KW_WITH:
                {
                alt57=1;
                }
                break;
            case QuotedIdentifier:
                {
                alt57=2;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 57, 0, input);

                throw nvae;

            }

            switch (alt57) {
                case 1 :
                    // IdentifiersParser.g:542:7: identifier
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_identifier_in_principalIdentifier3719);
                    identifier254=identifier();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, identifier254.getTree());

                    }
                    break;
                case 2 :
                    // IdentifiersParser.g:543:7: QuotedIdentifier
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    QuotedIdentifier255=(Token)match(input,QuotedIdentifier,FOLLOW_QuotedIdentifier_in_principalIdentifier3727); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    QuotedIdentifier255_tree = 
                    (CommonTree)adaptor.create(QuotedIdentifier255)
                    ;
                    adaptor.addChild(root_0, QuotedIdentifier255_tree);
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
            if ( state.backtracking==0 ) { gParent.popMsg(state); }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "principalIdentifier"


    public static class nonReserved_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "nonReserved"
    // IdentifiersParser.g:546:1: nonReserved : ( KW_TRUE | KW_FALSE | KW_LIKE | KW_EXISTS | KW_ASC | KW_DESC | KW_ORDER | KW_GROUP | KW_BY | KW_AS | KW_INSERT | KW_OVERWRITE | KW_OUTER | KW_LEFT | KW_RIGHT | KW_FULL | KW_PARTITION | KW_PARTITIONS | KW_TABLE | KW_TABLES | KW_COLUMNS | KW_INDEX | KW_INDEXES | KW_REBUILD | KW_FUNCTIONS | KW_SHOW | KW_MSCK | KW_REPAIR | KW_DIRECTORY | KW_LOCAL | KW_USING | KW_CLUSTER | KW_DISTRIBUTE | KW_SORT | KW_UNION | KW_LOAD | KW_EXPORT | KW_IMPORT | KW_DATA | KW_INPATH | KW_IS | KW_NULL | KW_CREATE | KW_EXTERNAL | KW_ALTER | KW_CHANGE | KW_FIRST | KW_AFTER | KW_DESCRIBE | KW_DROP | KW_RENAME | KW_IGNORE | KW_PROTECTION | KW_TO | KW_COMMENT | KW_BOOLEAN | KW_TINYINT | KW_SMALLINT | KW_INT | KW_BIGINT | KW_FLOAT | KW_DOUBLE | KW_DATE | KW_DATETIME | KW_TIMESTAMP | KW_DECIMAL | KW_STRING | KW_ARRAY | KW_STRUCT | KW_UNIONTYPE | KW_PARTITIONED | KW_CLUSTERED | KW_SORTED | KW_INTO | KW_BUCKETS | KW_ROW | KW_ROWS | KW_FORMAT | KW_DELIMITED | KW_FIELDS | KW_TERMINATED | KW_ESCAPED | KW_COLLECTION | KW_ITEMS | KW_KEYS | KW_KEY_TYPE | KW_LINES | KW_STORED | KW_FILEFORMAT | KW_INPUTFORMAT | KW_OUTPUTFORMAT | KW_INPUTDRIVER | KW_OUTPUTDRIVER | KW_OFFLINE | KW_ENABLE | KW_DISABLE | KW_READONLY | KW_NO_DROP | KW_LOCATION | KW_BUCKET | KW_OUT | KW_OF | KW_PERCENT | KW_ADD | KW_REPLACE | KW_RLIKE | KW_REGEXP | KW_TEMPORARY | KW_EXPLAIN | KW_FORMATTED | KW_PRETTY | KW_DEPENDENCY | KW_LOGICAL | KW_SERDE | KW_WITH | KW_DEFERRED | KW_SERDEPROPERTIES | KW_DBPROPERTIES | KW_LIMIT | KW_SET | KW_UNSET | KW_TBLPROPERTIES | KW_IDXPROPERTIES | KW_VALUE_TYPE | KW_ELEM_TYPE | KW_MAPJOIN | KW_STREAMTABLE | KW_HOLD_DDLTIME | KW_CLUSTERSTATUS | KW_UTC | KW_UTCTIMESTAMP | KW_LONG | KW_DELETE | KW_PLUS | KW_MINUS | KW_FETCH | KW_INTERSECT | KW_VIEW | KW_IN | KW_DATABASES | KW_MATERIALIZED | KW_SCHEMA | KW_SCHEMAS | KW_GRANT | KW_REVOKE | KW_SSL | KW_UNDO | KW_LOCK | KW_LOCKS | KW_UNLOCK | KW_SHARED | KW_EXCLUSIVE | KW_PROCEDURE | KW_UNSIGNED | KW_WHILE | KW_READ | KW_READS | KW_PURGE | KW_RANGE | KW_ANALYZE | KW_BEFORE | KW_BETWEEN | KW_BOTH | KW_BINARY | KW_CONTINUE | KW_CURSOR | KW_TRIGGER | KW_RECORDREADER | KW_RECORDWRITER | KW_SEMI | KW_LATERAL | KW_TOUCH | KW_ARCHIVE | KW_UNARCHIVE | KW_COMPUTE | KW_STATISTICS | KW_USE | KW_OPTION | KW_CONCATENATE | KW_SHOW_DATABASE | KW_UPDATE | KW_RESTRICT | KW_CASCADE | KW_SKEWED | KW_ROLLUP | KW_CUBE | KW_DIRECTORIES | KW_FOR | KW_GROUPING | KW_SETS | KW_TRUNCATE | KW_NOSCAN | KW_USER | KW_ROLE | KW_ROLES | KW_INNER | KW_DEFINED | KW_ADMIN | KW_JAR | KW_FILE | KW_OWNER | KW_PRINCIPALS | KW_ALL | KW_DEFAULT | KW_NONE | KW_COMPACT | KW_COMPACTIONS | KW_TRANSACTIONS | KW_REWRITE | KW_AUTHORIZATION | KW_VALUES );
    public final HiveParser_IdentifiersParser.nonReserved_return nonReserved() throws RecognitionException {
        HiveParser_IdentifiersParser.nonReserved_return retval = new HiveParser_IdentifiersParser.nonReserved_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token set256=null;

        CommonTree set256_tree=null;

        try {
            // IdentifiersParser.g:547:5: ( KW_TRUE | KW_FALSE | KW_LIKE | KW_EXISTS | KW_ASC | KW_DESC | KW_ORDER | KW_GROUP | KW_BY | KW_AS | KW_INSERT | KW_OVERWRITE | KW_OUTER | KW_LEFT | KW_RIGHT | KW_FULL | KW_PARTITION | KW_PARTITIONS | KW_TABLE | KW_TABLES | KW_COLUMNS | KW_INDEX | KW_INDEXES | KW_REBUILD | KW_FUNCTIONS | KW_SHOW | KW_MSCK | KW_REPAIR | KW_DIRECTORY | KW_LOCAL | KW_USING | KW_CLUSTER | KW_DISTRIBUTE | KW_SORT | KW_UNION | KW_LOAD | KW_EXPORT | KW_IMPORT | KW_DATA | KW_INPATH | KW_IS | KW_NULL | KW_CREATE | KW_EXTERNAL | KW_ALTER | KW_CHANGE | KW_FIRST | KW_AFTER | KW_DESCRIBE | KW_DROP | KW_RENAME | KW_IGNORE | KW_PROTECTION | KW_TO | KW_COMMENT | KW_BOOLEAN | KW_TINYINT | KW_SMALLINT | KW_INT | KW_BIGINT | KW_FLOAT | KW_DOUBLE | KW_DATE | KW_DATETIME | KW_TIMESTAMP | KW_DECIMAL | KW_STRING | KW_ARRAY | KW_STRUCT | KW_UNIONTYPE | KW_PARTITIONED | KW_CLUSTERED | KW_SORTED | KW_INTO | KW_BUCKETS | KW_ROW | KW_ROWS | KW_FORMAT | KW_DELIMITED | KW_FIELDS | KW_TERMINATED | KW_ESCAPED | KW_COLLECTION | KW_ITEMS | KW_KEYS | KW_KEY_TYPE | KW_LINES | KW_STORED | KW_FILEFORMAT | KW_INPUTFORMAT | KW_OUTPUTFORMAT | KW_INPUTDRIVER | KW_OUTPUTDRIVER | KW_OFFLINE | KW_ENABLE | KW_DISABLE | KW_READONLY | KW_NO_DROP | KW_LOCATION | KW_BUCKET | KW_OUT | KW_OF | KW_PERCENT | KW_ADD | KW_REPLACE | KW_RLIKE | KW_REGEXP | KW_TEMPORARY | KW_EXPLAIN | KW_FORMATTED | KW_PRETTY | KW_DEPENDENCY | KW_LOGICAL | KW_SERDE | KW_WITH | KW_DEFERRED | KW_SERDEPROPERTIES | KW_DBPROPERTIES | KW_LIMIT | KW_SET | KW_UNSET | KW_TBLPROPERTIES | KW_IDXPROPERTIES | KW_VALUE_TYPE | KW_ELEM_TYPE | KW_MAPJOIN | KW_STREAMTABLE | KW_HOLD_DDLTIME | KW_CLUSTERSTATUS | KW_UTC | KW_UTCTIMESTAMP | KW_LONG | KW_DELETE | KW_PLUS | KW_MINUS | KW_FETCH | KW_INTERSECT | KW_VIEW | KW_IN | KW_DATABASES | KW_MATERIALIZED | KW_SCHEMA | KW_SCHEMAS | KW_GRANT | KW_REVOKE | KW_SSL | KW_UNDO | KW_LOCK | KW_LOCKS | KW_UNLOCK | KW_SHARED | KW_EXCLUSIVE | KW_PROCEDURE | KW_UNSIGNED | KW_WHILE | KW_READ | KW_READS | KW_PURGE | KW_RANGE | KW_ANALYZE | KW_BEFORE | KW_BETWEEN | KW_BOTH | KW_BINARY | KW_CONTINUE | KW_CURSOR | KW_TRIGGER | KW_RECORDREADER | KW_RECORDWRITER | KW_SEMI | KW_LATERAL | KW_TOUCH | KW_ARCHIVE | KW_UNARCHIVE | KW_COMPUTE | KW_STATISTICS | KW_USE | KW_OPTION | KW_CONCATENATE | KW_SHOW_DATABASE | KW_UPDATE | KW_RESTRICT | KW_CASCADE | KW_SKEWED | KW_ROLLUP | KW_CUBE | KW_DIRECTORIES | KW_FOR | KW_GROUPING | KW_SETS | KW_TRUNCATE | KW_NOSCAN | KW_USER | KW_ROLE | KW_ROLES | KW_INNER | KW_DEFINED | KW_ADMIN | KW_JAR | KW_FILE | KW_OWNER | KW_PRINCIPALS | KW_ALL | KW_DEFAULT | KW_NONE | KW_COMPACT | KW_COMPACTIONS | KW_TRANSACTIONS | KW_REWRITE | KW_AUTHORIZATION | KW_VALUES )
            // IdentifiersParser.g:
            {
            root_0 = (CommonTree)adaptor.nil();


            set256=(Token)input.LT(1);

            if ( (input.LA(1) >= KW_ADD && input.LA(1) <= KW_ANALYZE)||(input.LA(1) >= KW_ARCHIVE && input.LA(1) <= KW_CASCADE)||input.LA(1)==KW_CHANGE||(input.LA(1) >= KW_CLUSTER && input.LA(1) <= KW_COLLECTION)||(input.LA(1) >= KW_COLUMNS && input.LA(1) <= KW_CONCATENATE)||(input.LA(1) >= KW_CONTINUE && input.LA(1) <= KW_CREATE)||input.LA(1)==KW_CUBE||(input.LA(1) >= KW_CURSOR && input.LA(1) <= KW_DATA)||(input.LA(1) >= KW_DATABASES && input.LA(1) <= KW_DISABLE)||(input.LA(1) >= KW_DISTRIBUTE && input.LA(1) <= KW_ELEM_TYPE)||input.LA(1)==KW_ENABLE||input.LA(1)==KW_ESCAPED||(input.LA(1) >= KW_EXCLUSIVE && input.LA(1) <= KW_EXPORT)||(input.LA(1) >= KW_EXTERNAL && input.LA(1) <= KW_FLOAT)||(input.LA(1) >= KW_FOR && input.LA(1) <= KW_FORMATTED)||input.LA(1)==KW_FULL||(input.LA(1) >= KW_FUNCTIONS && input.LA(1) <= KW_GROUPING)||(input.LA(1) >= KW_HOLD_DDLTIME && input.LA(1) <= KW_IDXPROPERTIES)||(input.LA(1) >= KW_IGNORE && input.LA(1) <= KW_JAR)||(input.LA(1) >= KW_KEYS && input.LA(1) <= KW_LEFT)||(input.LA(1) >= KW_LIKE && input.LA(1) <= KW_LONG)||(input.LA(1) >= KW_MAPJOIN && input.LA(1) <= KW_MINUS)||(input.LA(1) >= KW_MSCK && input.LA(1) <= KW_NOSCAN)||(input.LA(1) >= KW_NO_DROP && input.LA(1) <= KW_OFFLINE)||input.LA(1)==KW_OPTION||(input.LA(1) >= KW_ORDER && input.LA(1) <= KW_OUTPUTFORMAT)||(input.LA(1) >= KW_OVERWRITE && input.LA(1) <= KW_OWNER)||(input.LA(1) >= KW_PARTITION && input.LA(1) <= KW_PLUS)||(input.LA(1) >= KW_PRETTY && input.LA(1) <= KW_RECORDWRITER)||(input.LA(1) >= KW_REGEXP && input.LA(1) <= KW_SCHEMAS)||(input.LA(1) >= KW_SEMI && input.LA(1) <= KW_TABLES)||(input.LA(1) >= KW_TBLPROPERTIES && input.LA(1) <= KW_TERMINATED)||(input.LA(1) >= KW_TIMESTAMP && input.LA(1) <= KW_TRANSACTIONS)||(input.LA(1) >= KW_TRIGGER && input.LA(1) <= KW_UNARCHIVE)||(input.LA(1) >= KW_UNDO && input.LA(1) <= KW_UNIONTYPE)||(input.LA(1) >= KW_UNLOCK && input.LA(1) <= KW_VALUE_TYPE)||input.LA(1)==KW_VIEW||input.LA(1)==KW_WHILE||input.LA(1)==KW_WITH ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, 
                (CommonTree)adaptor.create(set256)
                );
                state.errorRecovery=false;
                state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "nonReserved"

    // $ANTLR start synpred1_IdentifiersParser
    public final void synpred1_IdentifiersParser_fragment() throws RecognitionException {
        // IdentifiersParser.g:121:7: ( COMMA )
        // IdentifiersParser.g:121:8: COMMA
        {
        match(input,COMMA,FOLLOW_COMMA_in_synpred1_IdentifiersParser563); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred1_IdentifiersParser

    // $ANTLR start synpred2_IdentifiersParser
    public final void synpred2_IdentifiersParser_fragment() throws RecognitionException {
        // IdentifiersParser.g:132:17: ( COMMA )
        // IdentifiersParser.g:132:18: COMMA
        {
        match(input,COMMA,FOLLOW_COMMA_in_synpred2_IdentifiersParser664); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred2_IdentifiersParser

    // $ANTLR start synpred3_IdentifiersParser
    public final void synpred3_IdentifiersParser_fragment() throws RecognitionException {
        // IdentifiersParser.g:143:17: ( COMMA )
        // IdentifiersParser.g:143:18: COMMA
        {
        match(input,COMMA,FOLLOW_COMMA_in_synpred3_IdentifiersParser764); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred3_IdentifiersParser

    // $ANTLR start synpred4_IdentifiersParser
    public final void synpred4_IdentifiersParser_fragment() throws RecognitionException {
        // IdentifiersParser.g:156:7: ( COMMA )
        // IdentifiersParser.g:156:8: COMMA
        {
        match(input,COMMA,FOLLOW_COMMA_in_synpred4_IdentifiersParser874); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred4_IdentifiersParser

    // $ANTLR start synpred5_IdentifiersParser
    public final void synpred5_IdentifiersParser_fragment() throws RecognitionException {
        // IdentifiersParser.g:268:7: ( functionName LPAREN )
        // IdentifiersParser.g:268:8: functionName LPAREN
        {
        pushFollow(FOLLOW_functionName_in_synpred5_IdentifiersParser1717);
        functionName();

        state._fsp--;
        if (state.failed) return ;

        match(input,LPAREN,FOLLOW_LPAREN_in_synpred5_IdentifiersParser1719); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred5_IdentifiersParser

    // $ANTLR start synpred6_IdentifiersParser
    public final void synpred6_IdentifiersParser_fragment() throws RecognitionException {
        // IdentifiersParser.g:381:7: ( KW_NOT KW_IN LPAREN KW_SELECT )
        // IdentifiersParser.g:381:8: KW_NOT KW_IN LPAREN KW_SELECT
        {
        match(input,KW_NOT,FOLLOW_KW_NOT_in_synpred6_IdentifiersParser2441); if (state.failed) return ;

        match(input,KW_IN,FOLLOW_KW_IN_in_synpred6_IdentifiersParser2443); if (state.failed) return ;

        match(input,LPAREN,FOLLOW_LPAREN_in_synpred6_IdentifiersParser2445); if (state.failed) return ;

        match(input,KW_SELECT,FOLLOW_KW_SELECT_in_synpred6_IdentifiersParser2447); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred6_IdentifiersParser

    // $ANTLR start synpred7_IdentifiersParser
    public final void synpred7_IdentifiersParser_fragment() throws RecognitionException {
        // IdentifiersParser.g:385:7: ( KW_IN LPAREN KW_SELECT )
        // IdentifiersParser.g:385:8: KW_IN LPAREN KW_SELECT
        {
        match(input,KW_IN,FOLLOW_KW_IN_in_synpred7_IdentifiersParser2534); if (state.failed) return ;

        match(input,LPAREN,FOLLOW_LPAREN_in_synpred7_IdentifiersParser2536); if (state.failed) return ;

        match(input,KW_SELECT,FOLLOW_KW_SELECT_in_synpred7_IdentifiersParser2538); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred7_IdentifiersParser

    // $ANTLR start synpred8_IdentifiersParser
    public final void synpred8_IdentifiersParser_fragment() throws RecognitionException {
        // IdentifiersParser.g:394:7: ( KW_EXISTS LPAREN KW_SELECT )
        // IdentifiersParser.g:394:8: KW_EXISTS LPAREN KW_SELECT
        {
        match(input,KW_EXISTS,FOLLOW_KW_EXISTS_in_synpred8_IdentifiersParser2728); if (state.failed) return ;

        match(input,LPAREN,FOLLOW_LPAREN_in_synpred8_IdentifiersParser2730); if (state.failed) return ;

        match(input,KW_SELECT,FOLLOW_KW_SELECT_in_synpred8_IdentifiersParser2732); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred8_IdentifiersParser

    // Delegated rules

    public final boolean synpred4_IdentifiersParser() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred4_IdentifiersParser_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred6_IdentifiersParser() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred6_IdentifiersParser_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred7_IdentifiersParser() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred7_IdentifiersParser_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred3_IdentifiersParser() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred3_IdentifiersParser_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred1_IdentifiersParser() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred1_IdentifiersParser_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred2_IdentifiersParser() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred2_IdentifiersParser_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred8_IdentifiersParser() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred8_IdentifiersParser_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred5_IdentifiersParser() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred5_IdentifiersParser_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }


    protected DFA6 dfa6 = new DFA6(this);
    protected DFA24 dfa24 = new DFA24(this);
    protected DFA30 dfa30 = new DFA30(this);
    protected DFA31 dfa31 = new DFA31(this);
    protected DFA32 dfa32 = new DFA32(this);
    protected DFA33 dfa33 = new DFA33(this);
    protected DFA36 dfa36 = new DFA36(this);
    protected DFA37 dfa37 = new DFA37(this);
    protected DFA38 dfa38 = new DFA38(this);
    protected DFA44 dfa44 = new DFA44(this);
    protected DFA43 dfa43 = new DFA43(this);
    static final String DFA6_eotS =
        "\u021b\uffff";
    static final String DFA6_eofS =
        "\u021b\uffff";
    static final String DFA6_minS =
        "\1\7\26\uffff\1\7\2\uffff\2\7\10\4\1\14\2\4\1\u011b\1\7\1\u011b"+
        "\1\4\1\u011b\4\4\1\7\1\4\u01e9\uffff";
    static final String DFA6_maxS =
        "\1\u012e\26\uffff\1\u012e\2\uffff\2\u012e\1\u012a\1\u012c\1\u012a"+
        "\1\u012c\4\u012a\1\14\2\u012a\1\u011b\1\u012e\1\u011b\1\u012a\1"+
        "\u011b\4\u012a\1\u012e\1\u012a\u01e9\uffff";
    static final String DFA6_acceptS =
        "\1\uffff\1\1\27\uffff\1\3\30\uffff\105\1\1\uffff\1\2\3\1\1\uffff"+
        "\25\1\1\uffff\26\1\1\uffff\27\1\1\uffff\26\1\1\uffff\26\1\1\uffff"+
        "\26\1\1\uffff\26\1\1\uffff\30\1\2\uffff\27\1\2\uffff\35\1\1\uffff"+
        "\25\1\1\uffff\3\1\1\uffff\25\1\1\uffff\2\1\1\uffff\25\1\1\uffff"+
        "\2\1\1\uffff\25\1\1\uffff\2\1\1\uffff\25\1\1\uffff\32\1\1\uffff"+
        "\25\1\1\uffff";
    static final String DFA6_specialS =
        "\u021b\uffff}>";
    static final String[] DFA6_transitionS = {
            "\1\1\5\uffff\1\1\4\uffff\1\1\7\uffff\7\1\1\uffff\22\1\1\uffff"+
            "\4\1\1\uffff\6\1\1\uffff\2\1\1\uffff\1\1\1\uffff\2\1\1\uffff"+
            "\20\1\1\uffff\4\1\1\uffff\1\1\1\uffff\1\1\1\uffff\4\1\1\uffff"+
            "\10\1\1\uffff\3\1\1\uffff\1\1\1\uffff\4\1\1\uffff\23\1\1\uffff"+
            "\4\1\1\uffff\12\1\1\uffff\4\1\1\uffff\10\1\1\uffff\1\1\1\uffff"+
            "\5\1\1\uffff\2\1\1\uffff\5\1\2\uffff\14\1\1\uffff\20\1\1\uffff"+
            "\24\1\1\uffff\3\1\1\uffff\5\1\1\uffff\4\1\1\uffff\3\1\1\uffff"+
            "\13\1\1\uffff\1\1\2\uffff\1\1\1\uffff\1\1\3\uffff\1\27\2\uffff"+
            "\1\1\2\uffff\2\1\10\uffff\4\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\40\5\uffff\1\44\4\uffff\1\43\7\uffff\1\56\6\61\1\uffff\1"+
            "\61\1\52\15\61\1\50\1\47\1\61\1\uffff\4\61\1\uffff\6\61\1\uffff"+
            "\2\61\1\uffff\1\61\1\uffff\2\61\1\uffff\1\61\1\35\16\61\1\uffff"+
            "\4\61\1\uffff\1\61\1\uffff\1\61\1\uffff\1\61\1\57\2\61\1\uffff"+
            "\1\61\1\46\6\61\1\uffff\3\61\1\uffff\1\61\1\uffff\4\61\1\uffff"+
            "\2\61\1\51\20\61\1\uffff\4\61\1\uffff\12\61\1\uffff\1\53\3\61"+
            "\1\uffff\3\61\1\32\1\61\1\34\2\61\1\uffff\1\61\1\uffff\5\61"+
            "\1\uffff\2\61\1\uffff\5\61\2\uffff\14\61\1\uffff\20\61\1\uffff"+
            "\21\61\1\54\2\61\1\uffff\3\61\1\uffff\5\61\1\uffff\1\61\1\45"+
            "\2\61\1\uffff\2\61\1\55\1\uffff\13\61\1\uffff\1\61\2\uffff\1"+
            "\61\1\uffff\1\61\3\uffff\1\60\2\uffff\1\33\2\uffff\1\36\1\33"+
            "\3\uffff\1\31\4\uffff\1\41\1\37\1\33\1\42",
            "",
            "",
            "\1\67\5\uffff\1\73\4\uffff\1\72\7\uffff\1\105\6\110\1\uffff"+
            "\1\110\1\101\15\110\1\77\1\76\1\110\1\uffff\4\110\1\uffff\6"+
            "\110\1\uffff\2\110\1\uffff\1\110\1\uffff\2\110\1\uffff\1\110"+
            "\1\64\16\110\1\uffff\4\110\1\uffff\1\110\1\uffff\1\110\1\uffff"+
            "\1\110\1\106\2\110\1\uffff\1\110\1\75\6\110\1\uffff\3\110\1"+
            "\uffff\1\110\1\uffff\4\110\1\uffff\2\110\1\100\20\110\1\uffff"+
            "\4\110\1\uffff\12\110\1\uffff\1\102\3\110\1\uffff\3\110\1\111"+
            "\1\110\1\63\2\110\1\uffff\1\110\1\uffff\5\110\1\uffff\2\110"+
            "\1\uffff\5\110\2\uffff\14\110\1\uffff\20\110\1\uffff\21\110"+
            "\1\103\2\110\1\uffff\3\110\1\uffff\5\110\1\uffff\1\110\1\74"+
            "\2\110\1\uffff\2\110\1\104\1\uffff\13\110\1\uffff\1\110\2\uffff"+
            "\1\110\1\uffff\1\110\3\uffff\1\107\2\uffff\1\62\2\uffff\1\65"+
            "\1\62\10\uffff\1\70\1\66\1\62\1\71",
            "\1\116\5\uffff\1\122\4\uffff\1\121\7\uffff\1\134\6\135\1\uffff"+
            "\1\135\1\130\15\135\1\126\1\125\1\135\1\uffff\4\135\1\uffff"+
            "\6\135\1\uffff\2\135\1\uffff\1\135\1\uffff\2\135\1\uffff\1\135"+
            "\1\113\16\135\1\uffff\4\135\1\uffff\1\135\1\uffff\1\135\1\uffff"+
            "\4\135\1\uffff\1\135\1\124\6\135\1\uffff\3\135\1\uffff\1\135"+
            "\1\uffff\4\135\1\uffff\2\135\1\127\20\135\1\uffff\4\135\1\uffff"+
            "\12\135\1\uffff\1\131\3\135\1\uffff\3\135\1\uffff\1\135\1\112"+
            "\2\135\1\uffff\1\135\1\uffff\5\135\1\uffff\2\135\1\uffff\5\135"+
            "\2\uffff\14\135\1\uffff\20\135\1\uffff\21\135\1\132\2\135\1"+
            "\uffff\3\135\1\uffff\5\135\1\uffff\1\135\1\123\2\135\1\uffff"+
            "\2\135\1\133\1\uffff\13\135\1\uffff\1\135\2\uffff\1\135\1\uffff"+
            "\1\135\3\uffff\1\136\2\uffff\1\137\2\uffff\1\114\1\137\10\uffff"+
            "\1\117\1\115\1\137\1\120",
            "\1\146\1\147\1\143\3\uffff\1\170\3\uffff\2\144\1\uffff\1\141"+
            "\2\uffff\1\152\1\153\1\uffff\1\160\1\157\10\uffff\1\163\6\uffff"+
            "\1\162\130\uffff\1\161\12\uffff\1\142\10\uffff\1\151\22\uffff"+
            "\1\150\6\uffff\1\164\35\uffff\1\151\7\uffff\1\151\103\uffff"+
            "\1\156\1\155\1\166\1\140\1\uffff\1\145\1\144\1\154\1\uffff\1"+
            "\145\3\uffff\1\165\3\uffff\1\144",
            "\1\u0082\1\u0083\1\177\3\uffff\1\170\3\uffff\2\u0080\1\uffff"+
            "\1\172\2\uffff\1\u0086\1\u0087\1\uffff\1\u008c\1\u008b\10\uffff"+
            "\1\u008f\6\uffff\1\u008e\130\uffff\1\u008d\12\uffff\1\176\10"+
            "\uffff\1\u0085\22\uffff\1\u0084\6\uffff\1\u0090\35\uffff\1\u0085"+
            "\7\uffff\1\u0085\103\uffff\1\u008a\1\u0089\1\173\1\175\1\uffff"+
            "\1\u0081\1\u0080\1\u0088\1\uffff\1\u0081\3\uffff\1\u0091\3\uffff"+
            "\1\u0080\1\uffff\1\171",
            "\1\u0099\1\u009a\1\u0096\3\uffff\1\170\3\uffff\2\u0097\1\uffff"+
            "\1\u0094\2\uffff\1\u009d\1\u009e\1\uffff\1\u00a3\1\u00a2\10"+
            "\uffff\1\u00a6\6\uffff\1\u00a5\130\uffff\1\u00a4\12\uffff\1"+
            "\u0095\10\uffff\1\u009c\22\uffff\1\u009b\6\uffff\1\u00a7\35"+
            "\uffff\1\u009c\7\uffff\1\u009c\103\uffff\1\u00a1\1\u00a0\1\uffff"+
            "\1\u0093\1\uffff\1\u0098\1\u0097\1\u009f\1\uffff\1\u0098\3\uffff"+
            "\1\u00a8\3\uffff\1\u0097",
            "\1\u00b0\1\u00b1\1\u00ad\3\uffff\1\170\3\uffff\2\u00ae\1\uffff"+
            "\1\u00ab\2\uffff\1\u00b4\1\u00b5\1\uffff\1\u00ba\1\u00b9\10"+
            "\uffff\1\u00bd\6\uffff\1\u00bc\130\uffff\1\u00bb\12\uffff\1"+
            "\u00ac\10\uffff\1\u00b3\22\uffff\1\u00b2\6\uffff\1\u00be\35"+
            "\uffff\1\u00b3\7\uffff\1\u00b3\103\uffff\1\u00b8\1\u00b7\1\uffff"+
            "\1\u00aa\1\uffff\1\u00af\1\u00ae\1\u00b6\1\uffff\1\u00af\3\uffff"+
            "\1\u00bf\3\uffff\1\u00ae\1\uffff\1\u00c0",
            "\1\u00c8\1\u00c9\1\u00c5\3\uffff\1\170\3\uffff\2\u00c6\1\uffff"+
            "\1\u00c3\2\uffff\1\u00cc\1\u00cd\1\uffff\1\u00d2\1\u00d1\10"+
            "\uffff\1\u00d5\6\uffff\1\u00d4\130\uffff\1\u00d3\12\uffff\1"+
            "\u00c4\10\uffff\1\u00cb\22\uffff\1\u00ca\6\uffff\1\u00d6\35"+
            "\uffff\1\u00cb\7\uffff\1\u00cb\103\uffff\1\u00d0\1\u00cf\1\uffff"+
            "\1\u00c2\1\uffff\1\u00c7\1\u00c6\1\u00ce\1\uffff\1\u00c7\3\uffff"+
            "\1\u00d7\3\uffff\1\u00c6",
            "\1\u00df\1\u00e0\1\u00dc\3\uffff\1\170\3\uffff\2\u00dd\1\uffff"+
            "\1\u00da\2\uffff\1\u00e3\1\u00e4\1\uffff\1\u00e9\1\u00e8\10"+
            "\uffff\1\u00ec\6\uffff\1\u00eb\130\uffff\1\u00ea\12\uffff\1"+
            "\u00db\10\uffff\1\u00e2\22\uffff\1\u00e1\6\uffff\1\u00ed\35"+
            "\uffff\1\u00e2\7\uffff\1\u00e2\103\uffff\1\u00e7\1\u00e6\1\uffff"+
            "\1\u00d9\1\uffff\1\u00de\1\u00dd\1\u00e5\1\uffff\1\u00de\3\uffff"+
            "\1\u00ee\3\uffff\1\u00dd",
            "\1\u00f6\1\u00f7\1\u00f3\3\uffff\1\170\3\uffff\2\u00f4\1\uffff"+
            "\1\u00f1\2\uffff\1\u00fa\1\u00fb\1\uffff\1\u0100\1\u00ff\10"+
            "\uffff\1\u0103\6\uffff\1\u0102\130\uffff\1\u0101\12\uffff\1"+
            "\u00f2\10\uffff\1\u00f9\22\uffff\1\u00f8\6\uffff\1\u0104\35"+
            "\uffff\1\u00f9\7\uffff\1\u00f9\103\uffff\1\u00fe\1\u00fd\1\uffff"+
            "\1\u00f0\1\uffff\1\u00f5\1\u00f4\1\u00fc\1\uffff\1\u00f5\3\uffff"+
            "\1\u0105\3\uffff\1\u00f4",
            "\1\u010d\1\u010e\1\u010a\3\uffff\1\170\3\uffff\2\u010b\1\uffff"+
            "\1\u0108\2\uffff\1\u0111\1\u0112\1\uffff\1\u0117\1\u0116\10"+
            "\uffff\1\u011a\6\uffff\1\u0119\130\uffff\1\u0118\12\uffff\1"+
            "\u0109\10\uffff\1\u0110\22\uffff\1\u010f\6\uffff\1\u011b\35"+
            "\uffff\1\u0110\7\uffff\1\u0110\103\uffff\1\u0115\1\u0114\1\uffff"+
            "\1\u0107\1\uffff\1\u010c\1\u010b\1\u0113\1\uffff\1\u010c\3\uffff"+
            "\1\u011c\3\uffff\1\u010b",
            "\1\u011e",
            "\1\u0125\1\u0126\1\u0122\3\uffff\1\170\3\uffff\2\u0123\1\uffff"+
            "\1\u0120\2\uffff\1\u0129\1\u012a\1\uffff\1\u012f\1\u012e\10"+
            "\uffff\1\u0132\6\uffff\1\u0131\130\uffff\1\u0130\12\uffff\1"+
            "\u0121\10\uffff\1\u0128\22\uffff\1\u0127\6\uffff\1\u0133\35"+
            "\uffff\1\u0128\7\uffff\1\u0128\103\uffff\1\u012d\1\u012c\1\u0135"+
            "\1\u011f\1\uffff\1\u0124\1\u0123\1\u012b\1\uffff\1\u0124\3\uffff"+
            "\1\u0134\3\uffff\1\u0123",
            "\1\u013e\1\u013f\1\u013b\3\uffff\1\170\3\uffff\2\u013c\1\uffff"+
            "\1\u0139\2\uffff\1\u0142\1\u0143\1\uffff\1\u0148\1\u0147\10"+
            "\uffff\1\u014b\6\uffff\1\u014a\130\uffff\1\u0149\12\uffff\1"+
            "\u013a\10\uffff\1\u0141\22\uffff\1\u0140\6\uffff\1\u014c\35"+
            "\uffff\1\u0141\7\uffff\1\u0141\103\uffff\1\u0146\1\u0145\1\u014e"+
            "\1\u0138\1\uffff\1\u013d\1\u013c\1\u0144\1\uffff\1\u013d\3\uffff"+
            "\1\u014d\3\uffff\1\u013c",
            "\1\u0151",
            "\1\u0158\5\uffff\1\u015c\4\uffff\1\u015b\7\uffff\1\u0166\6"+
            "\u0169\1\uffff\1\u0169\1\u0162\15\u0169\1\u0160\1\u015f\1\u0169"+
            "\1\uffff\4\u0169\1\uffff\6\u0169\1\uffff\2\u0169\1\uffff\1\u0169"+
            "\1\uffff\2\u0169\1\uffff\1\u0169\1\u0155\16\u0169\1\uffff\4"+
            "\u0169\1\uffff\1\u0169\1\uffff\1\u0169\1\uffff\1\u0169\1\u0167"+
            "\2\u0169\1\uffff\1\u0169\1\u015e\6\u0169\1\uffff\3\u0169\1\uffff"+
            "\1\u0169\1\uffff\4\u0169\1\uffff\2\u0169\1\u0161\20\u0169\1"+
            "\uffff\4\u0169\1\uffff\12\u0169\1\uffff\1\u0163\3\u0169\1\uffff"+
            "\3\u0169\1\u0152\1\u0169\1\u0154\2\u0169\1\uffff\1\u0169\1\uffff"+
            "\5\u0169\1\uffff\2\u0169\1\uffff\5\u0169\2\uffff\14\u0169\1"+
            "\uffff\20\u0169\1\uffff\21\u0169\1\u0164\2\u0169\1\uffff\3\u0169"+
            "\1\uffff\5\u0169\1\uffff\1\u0169\1\u015d\2\u0169\1\uffff\2\u0169"+
            "\1\u0165\1\uffff\13\u0169\1\uffff\1\u0169\1\u016a\1\uffff\1"+
            "\u0169\1\uffff\1\u0169\3\uffff\1\u0168\2\uffff\1\u0153\2\uffff"+
            "\1\u0156\1\u0153\10\uffff\1\u0159\1\u0157\1\u0153\1\u015a",
            "\1\u016b",
            "\1\u0174\1\u0175\1\u0171\3\uffff\1\170\3\uffff\2\u0172\1\uffff"+
            "\1\u016d\2\uffff\1\u0178\1\u0179\1\uffff\1\u017e\1\u017d\10"+
            "\uffff\1\u0181\6\uffff\1\u0180\130\uffff\1\u017f\12\uffff\1"+
            "\u0170\10\uffff\1\u0177\22\uffff\1\u0176\6\uffff\1\u0182\35"+
            "\uffff\1\u0177\7\uffff\1\u0177\103\uffff\1\u017c\1\u017b\1\u016c"+
            "\1\u016f\1\uffff\1\u0173\1\u0172\1\u017a\1\uffff\1\u0173\3\uffff"+
            "\1\u0183\3\uffff\1\u0172",
            "\1\u0185",
            "\1\u018e\1\u018f\1\u018b\3\uffff\1\170\3\uffff\2\u018c\1\uffff"+
            "\1\u0187\2\uffff\1\u0192\1\u0193\1\uffff\1\u0198\1\u0197\10"+
            "\uffff\1\u019b\6\uffff\1\u019a\130\uffff\1\u0199\12\uffff\1"+
            "\u018a\10\uffff\1\u0191\22\uffff\1\u0190\6\uffff\1\u019c\35"+
            "\uffff\1\u0191\7\uffff\1\u0191\103\uffff\1\u0196\1\u0195\1\u0186"+
            "\1\u0189\1\uffff\1\u018d\1\u018c\1\u0194\1\uffff\1\u018d\3\uffff"+
            "\1\u019d\3\uffff\1\u018c",
            "\1\u01a7\1\u01a8\1\u01a4\3\uffff\1\170\3\uffff\2\u01a5\1\uffff"+
            "\1\u01a0\2\uffff\1\u01ab\1\u01ac\1\uffff\1\u01b1\1\u01b0\10"+
            "\uffff\1\u01b4\6\uffff\1\u01b3\130\uffff\1\u01b2\12\uffff\1"+
            "\u01a3\10\uffff\1\u01aa\22\uffff\1\u01a9\6\uffff\1\u01b5\35"+
            "\uffff\1\u01aa\7\uffff\1\u01aa\103\uffff\1\u01af\1\u01ae\1\u019f"+
            "\1\u01a2\1\uffff\1\u01a6\1\u01a5\1\u01ad\1\uffff\1\u01a6\3\uffff"+
            "\1\u01b6\3\uffff\1\u01a5",
            "\1\u01c0\1\u01c1\1\u01bd\3\uffff\1\170\3\uffff\2\u01be\1\uffff"+
            "\1\u01b8\2\uffff\1\u01c4\1\u01c5\1\uffff\1\u01ca\1\u01c9\10"+
            "\uffff\1\u01cd\6\uffff\1\u01cc\130\uffff\1\u01cb\12\uffff\1"+
            "\u01bc\10\uffff\1\u01c3\22\uffff\1\u01c2\6\uffff\1\u01ce\35"+
            "\uffff\1\u01c3\7\uffff\1\u01c3\103\uffff\1\u01c8\1\u01c7\1\u01b9"+
            "\1\u01bb\1\uffff\1\u01bf\1\u01be\1\u01c6\1\uffff\1\u01bf\3\uffff"+
            "\1\u01cf\3\uffff\1\u01be",
            "\1\u01d9\1\u01da\1\u01d6\3\uffff\1\170\3\uffff\2\u01d7\1\uffff"+
            "\1\u01d1\2\uffff\1\u01dd\1\u01de\1\uffff\1\u01e3\1\u01e2\10"+
            "\uffff\1\u01e6\6\uffff\1\u01e5\130\uffff\1\u01e4\12\uffff\1"+
            "\u01d5\10\uffff\1\u01dc\22\uffff\1\u01db\6\uffff\1\u01e7\35"+
            "\uffff\1\u01dc\7\uffff\1\u01dc\103\uffff\1\u01e1\1\u01e0\1\u01d2"+
            "\1\u01d4\1\uffff\1\u01d8\1\u01d7\1\u01df\1\uffff\1\u01d8\3\uffff"+
            "\1\u01e8\3\uffff\1\u01d7",
            "\1\u01f0\5\uffff\1\u01f4\4\uffff\1\u01f3\7\uffff\1\u01fe\6"+
            "\u0201\1\uffff\1\u0201\1\u01fa\15\u0201\1\u01f8\1\u01f7\1\u0201"+
            "\1\uffff\4\u0201\1\uffff\6\u0201\1\uffff\2\u0201\1\uffff\1\u0201"+
            "\1\uffff\2\u0201\1\uffff\1\u0201\1\u01ed\16\u0201\1\uffff\4"+
            "\u0201\1\uffff\1\u0201\1\uffff\1\u0201\1\uffff\1\u0201\1\u01ff"+
            "\2\u0201\1\uffff\1\u0201\1\u01f6\6\u0201\1\uffff\3\u0201\1\uffff"+
            "\1\u0201\1\uffff\4\u0201\1\uffff\2\u0201\1\u01f9\20\u0201\1"+
            "\uffff\4\u0201\1\uffff\12\u0201\1\uffff\1\u01fb\3\u0201\1\uffff"+
            "\3\u0201\1\u01ea\1\u0201\1\u01ec\2\u0201\1\uffff\1\u0201\1\uffff"+
            "\5\u0201\1\uffff\2\u0201\1\uffff\5\u0201\2\uffff\14\u0201\1"+
            "\uffff\20\u0201\1\uffff\21\u0201\1\u01fc\2\u0201\1\uffff\3\u0201"+
            "\1\uffff\5\u0201\1\uffff\1\u0201\1\u01f5\2\u0201\1\uffff\2\u0201"+
            "\1\u01fd\1\uffff\13\u0201\1\uffff\1\u0201\2\uffff\1\u0201\1"+
            "\uffff\1\u0201\3\uffff\1\u0200\2\uffff\1\u01eb\2\uffff\1\u01ee"+
            "\1\u01eb\10\uffff\1\u01f1\1\u01ef\1\u01eb\1\u01f2",
            "\1\u020a\1\u020b\1\u0207\3\uffff\1\170\3\uffff\2\u0208\1\uffff"+
            "\1\u0202\2\uffff\1\u020e\1\u020f\1\uffff\1\u0214\1\u0213\10"+
            "\uffff\1\u0217\6\uffff\1\u0216\130\uffff\1\u0215\12\uffff\1"+
            "\u0206\10\uffff\1\u020d\22\uffff\1\u020c\6\uffff\1\u0218\35"+
            "\uffff\1\u020d\7\uffff\1\u020d\103\uffff\1\u0212\1\u0211\1\u0203"+
            "\1\u0205\1\uffff\1\u0209\1\u0208\1\u0210\1\uffff\1\u0209\3\uffff"+
            "\1\u0219\3\uffff\1\u0208",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA6_eot = DFA.unpackEncodedString(DFA6_eotS);
    static final short[] DFA6_eof = DFA.unpackEncodedString(DFA6_eofS);
    static final char[] DFA6_min = DFA.unpackEncodedStringToUnsignedChars(DFA6_minS);
    static final char[] DFA6_max = DFA.unpackEncodedStringToUnsignedChars(DFA6_maxS);
    static final short[] DFA6_accept = DFA.unpackEncodedString(DFA6_acceptS);
    static final short[] DFA6_special = DFA.unpackEncodedString(DFA6_specialS);
    static final short[][] DFA6_transition;

    static {
        int numStates = DFA6_transitionS.length;
        DFA6_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA6_transition[i] = DFA.unpackEncodedString(DFA6_transitionS[i]);
        }
    }

    class DFA6 extends DFA {

        public DFA6(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 6;
            this.eot = DFA6_eot;
            this.eof = DFA6_eof;
            this.min = DFA6_min;
            this.max = DFA6_max;
            this.accept = DFA6_accept;
            this.special = DFA6_special;
            this.transition = DFA6_transition;
        }
        public String getDescription() {
            return "65:1: groupingSetExpression : ( groupByExpression -> ^( TOK_GROUPING_SETS_EXPRESSION groupByExpression ) | LPAREN groupByExpression ( COMMA groupByExpression )* RPAREN -> ^( TOK_GROUPING_SETS_EXPRESSION ( groupByExpression )+ ) | LPAREN RPAREN -> ^( TOK_GROUPING_SETS_EXPRESSION ) );";
        }
    }
    static final String DFA24_eotS =
        "\101\uffff";
    static final String DFA24_eofS =
        "\1\2\100\uffff";
    static final String DFA24_minS =
        "\1\4\100\uffff";
    static final String DFA24_maxS =
        "\1\u012a\100\uffff";
    static final String DFA24_acceptS =
        "\1\uffff\1\1\1\2\76\uffff";
    static final String DFA24_specialS =
        "\101\uffff}>";
    static final String[] DFA24_transitionS = {
            "\3\2\3\uffff\1\2\3\uffff\2\2\1\uffff\1\2\2\uffff\2\2\1\uffff"+
            "\2\2\1\uffff\27\2\2\uffff\1\2\1\uffff\4\2\1\uffff\6\2\1\uffff"+
            "\4\2\1\uffff\2\2\1\uffff\20\2\1\uffff\10\2\1\uffff\4\2\1\uffff"+
            "\10\2\1\uffff\5\2\1\uffff\7\2\1\uffff\25\2\1\uffff\12\2\1\uffff"+
            "\4\2\1\uffff\10\2\1\uffff\7\2\1\1\2\2\1\uffff\5\2\2\uffff\62"+
            "\2\1\uffff\11\2\1\uffff\4\2\1\uffff\3\2\1\uffff\13\2\1\uffff"+
            "\6\2\1\uffff\2\2\1\uffff\1\2\1\uffff\3\2\1\uffff\1\2\3\uffff"+
            "\2\2\2\uffff\1\2",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA24_eot = DFA.unpackEncodedString(DFA24_eotS);
    static final short[] DFA24_eof = DFA.unpackEncodedString(DFA24_eofS);
    static final char[] DFA24_min = DFA.unpackEncodedStringToUnsignedChars(DFA24_minS);
    static final char[] DFA24_max = DFA.unpackEncodedStringToUnsignedChars(DFA24_maxS);
    static final short[] DFA24_accept = DFA.unpackEncodedString(DFA24_acceptS);
    static final short[] DFA24_special = DFA.unpackEncodedString(DFA24_specialS);
    static final short[][] DFA24_transition;

    static {
        int numStates = DFA24_transitionS.length;
        DFA24_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA24_transition[i] = DFA.unpackEncodedString(DFA24_transitionS[i]);
        }
    }

    class DFA24 extends DFA {

        public DFA24(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 24;
            this.eot = DFA24_eot;
            this.eof = DFA24_eof;
            this.min = DFA24_min;
            this.max = DFA24_max;
            this.accept = DFA24_accept;
            this.special = DFA24_special;
            this.transition = DFA24_transition;
        }
        public String getDescription() {
            return "170:12: ( KW_OVER ws= window_specification )?";
        }
    }
    static final String DFA30_eotS =
        "\113\uffff";
    static final String DFA30_eofS =
        "\3\uffff\1\13\107\uffff";
    static final String DFA30_minS =
        "\1\7\2\uffff\1\4\107\uffff";
    static final String DFA30_maxS =
        "\1\u012e\2\uffff\1\u012c\107\uffff";
    static final String DFA30_acceptS =
        "\1\uffff\1\1\1\2\1\uffff\1\5\1\6\1\7\1\10\1\11\1\12\1\uffff\1\3"+
        "\76\uffff\1\4";
    static final String DFA30_specialS =
        "\113\uffff}>";
    static final String[] DFA30_transitionS = {
            "\1\4\5\uffff\1\10\4\uffff\1\7\67\uffff\1\2\36\uffff\1\11\u0094"+
            "\uffff\1\11\42\uffff\1\1\11\uffff\1\5\1\3\1\uffff\1\6",
            "",
            "",
            "\3\13\3\uffff\1\13\3\uffff\2\13\1\uffff\1\13\2\uffff\2\13\1"+
            "\uffff\2\13\1\uffff\27\13\2\uffff\1\13\1\uffff\4\13\1\uffff"+
            "\6\13\1\uffff\4\13\1\uffff\2\13\1\uffff\20\13\1\uffff\10\13"+
            "\1\uffff\4\13\1\uffff\10\13\1\uffff\5\13\1\uffff\7\13\1\uffff"+
            "\25\13\1\uffff\12\13\1\uffff\4\13\1\uffff\10\13\1\uffff\7\13"+
            "\1\uffff\2\13\1\uffff\5\13\2\uffff\62\13\1\uffff\11\13\1\uffff"+
            "\4\13\1\uffff\3\13\1\uffff\13\13\1\uffff\6\13\1\uffff\2\13\1"+
            "\uffff\1\13\1\uffff\3\13\1\uffff\1\13\3\uffff\2\13\2\uffff\1"+
            "\13\1\uffff\1\112",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA30_eot = DFA.unpackEncodedString(DFA30_eotS);
    static final short[] DFA30_eof = DFA.unpackEncodedString(DFA30_eofS);
    static final char[] DFA30_min = DFA.unpackEncodedStringToUnsignedChars(DFA30_minS);
    static final char[] DFA30_max = DFA.unpackEncodedStringToUnsignedChars(DFA30_maxS);
    static final short[] DFA30_accept = DFA.unpackEncodedString(DFA30_acceptS);
    static final short[] DFA30_special = DFA.unpackEncodedString(DFA30_specialS);
    static final short[][] DFA30_transition;

    static {
        int numStates = DFA30_transitionS.length;
        DFA30_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA30_transition[i] = DFA.unpackEncodedString(DFA30_transitionS[i]);
        }
    }

    class DFA30 extends DFA {

        public DFA30(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 30;
            this.eot = DFA30_eot;
            this.eof = DFA30_eof;
            this.min = DFA30_min;
            this.max = DFA30_max;
            this.accept = DFA30_accept;
            this.special = DFA30_special;
            this.transition = DFA30_transition;
        }
        public String getDescription() {
            return "215:1: constant : ( Number | dateLiteral | StringLiteral | stringLiteralSequence | BigintLiteral | SmallintLiteral | TinyintLiteral | DecimalLiteral | charSetStringLiteral | booleanValue );";
        }
    }
    static final String DFA31_eotS =
        "\101\uffff";
    static final String DFA31_eofS =
        "\1\1\100\uffff";
    static final String DFA31_minS =
        "\1\4\100\uffff";
    static final String DFA31_maxS =
        "\1\u012c\100\uffff";
    static final String DFA31_acceptS =
        "\1\uffff\1\2\76\uffff\1\1";
    static final String DFA31_specialS =
        "\101\uffff}>";
    static final String[] DFA31_transitionS = {
            "\3\1\3\uffff\1\1\3\uffff\2\1\1\uffff\1\1\2\uffff\2\1\1\uffff"+
            "\2\1\1\uffff\27\1\2\uffff\1\1\1\uffff\4\1\1\uffff\6\1\1\uffff"+
            "\4\1\1\uffff\2\1\1\uffff\20\1\1\uffff\10\1\1\uffff\4\1\1\uffff"+
            "\10\1\1\uffff\5\1\1\uffff\7\1\1\uffff\25\1\1\uffff\12\1\1\uffff"+
            "\4\1\1\uffff\10\1\1\uffff\7\1\1\uffff\2\1\1\uffff\5\1\2\uffff"+
            "\62\1\1\uffff\11\1\1\uffff\4\1\1\uffff\3\1\1\uffff\13\1\1\uffff"+
            "\6\1\1\uffff\2\1\1\uffff\1\1\1\uffff\3\1\1\uffff\1\1\3\uffff"+
            "\2\1\2\uffff\1\1\1\uffff\1\100",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA31_eot = DFA.unpackEncodedString(DFA31_eotS);
    static final short[] DFA31_eof = DFA.unpackEncodedString(DFA31_eofS);
    static final char[] DFA31_min = DFA.unpackEncodedStringToUnsignedChars(DFA31_minS);
    static final char[] DFA31_max = DFA.unpackEncodedStringToUnsignedChars(DFA31_maxS);
    static final short[] DFA31_accept = DFA.unpackEncodedString(DFA31_acceptS);
    static final short[] DFA31_special = DFA.unpackEncodedString(DFA31_specialS);
    static final short[][] DFA31_transition;

    static {
        int numStates = DFA31_transitionS.length;
        DFA31_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA31_transition[i] = DFA.unpackEncodedString(DFA31_transitionS[i]);
        }
    }

    class DFA31 extends DFA {

        public DFA31(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 31;
            this.eot = DFA31_eot;
            this.eof = DFA31_eof;
            this.min = DFA31_min;
            this.max = DFA31_max;
            this.accept = DFA31_accept;
            this.special = DFA31_special;
            this.transition = DFA31_transition;
        }
        public String getDescription() {
            return "()+ loopback of 233:19: ( StringLiteral )+";
        }
    }
    static final String DFA32_eotS =
        "\u0282\uffff";
    static final String DFA32_eofS =
        "\1\uffff\1\26\1\131\7\uffff\2\3\3\uffff\1\131\1\uffff\4\131\u026d"+
        "\uffff";
    static final String DFA32_minS =
        "\1\7\2\4\7\uffff\2\4\1\uffff\1\7\1\uffff\1\4\1\uffff\4\4\2\uffff"+
        "\1\32\77\uffff\1\32\100\uffff\1\32\77\uffff\1\32\130\uffff\1\32"+
        "\77\uffff\1\32\77\uffff\1\32\76\uffff\1\32\77\uffff\1\32\77\uffff"+
        "\22\0";
    static final String DFA32_maxS =
        "\1\u012e\1\u012a\1\u012c\7\uffff\2\u012a\1\uffff\1\u012e\1\uffff"+
        "\1\u012a\1\uffff\4\u012a\2\uffff\1\u0117\77\uffff\1\u0117\100\uffff"+
        "\1\u0117\77\uffff\1\u0117\130\uffff\1\u0117\77\uffff\1\u0117\77"+
        "\uffff\1\u0117\76\uffff\1\u0117\77\uffff\1\u0117\77\uffff\22\0";
    static final String DFA32_acceptS =
        "\3\uffff\1\3\10\uffff\1\4\1\uffff\1\7\1\uffff\1\7\4\uffff\1\11\1"+
        "\1\76\uffff\1\7\1\2\1\uffff\1\7\1\10\174\uffff\1\7\77\uffff\1\7"+
        "\1\5\27\uffff\1\6\1\7\77\uffff\1\7\77\uffff\1\7\100\uffff\1\7\77"+
        "\uffff\1\7\120\uffff";
    static final String DFA32_specialS =
        "\1\0\1\1\1\2\7\uffff\1\3\1\4\3\uffff\1\5\1\uffff\1\6\1\7\1\10\1"+
        "\11\u025b\uffff\1\12\1\13\1\14\1\15\1\16\1\17\1\20\1\21\1\22\1\23"+
        "\1\24\1\25\1\26\1\27\1\30\1\31\1\32\1\33}>";
    static final String[] DFA32_transitionS = {
            "\1\3\5\uffff\1\3\4\uffff\1\3\7\uffff\1\23\6\24\1\uffff\1\24"+
            "\1\17\15\24\1\15\1\14\1\24\1\uffff\4\24\1\uffff\6\24\1\uffff"+
            "\2\24\1\uffff\1\24\1\uffff\2\24\1\uffff\1\24\1\2\16\24\1\uffff"+
            "\4\24\1\uffff\1\24\1\uffff\1\24\1\uffff\4\24\1\uffff\1\24\1"+
            "\13\6\24\1\uffff\3\24\1\uffff\1\24\1\uffff\4\24\1\uffff\2\24"+
            "\1\16\20\24\1\uffff\4\24\1\uffff\12\24\1\uffff\1\20\3\24\1\uffff"+
            "\3\24\1\uffff\1\24\1\1\2\24\1\uffff\1\24\1\uffff\5\24\1\uffff"+
            "\2\24\1\uffff\5\24\2\uffff\14\24\1\uffff\20\24\1\uffff\21\24"+
            "\1\21\2\24\1\uffff\3\24\1\uffff\5\24\1\uffff\1\24\1\12\2\24"+
            "\1\uffff\2\24\1\22\1\uffff\13\24\1\uffff\1\24\2\uffff\1\24\1"+
            "\uffff\1\24\3\uffff\1\25\5\uffff\1\3\11\uffff\2\3\1\uffff\1"+
            "\3",
            "\3\26\3\uffff\1\26\3\uffff\2\26\1\uffff\1\27\2\uffff\2\26\1"+
            "\uffff\2\26\1\uffff\27\26\2\uffff\1\26\1\uffff\4\26\1\uffff"+
            "\6\26\1\uffff\4\26\1\uffff\2\26\1\uffff\20\26\1\uffff\10\26"+
            "\1\uffff\4\26\1\uffff\10\26\1\uffff\5\26\1\uffff\7\26\1\uffff"+
            "\25\26\1\uffff\12\26\1\uffff\4\26\1\uffff\10\26\1\uffff\7\26"+
            "\1\uffff\2\26\1\uffff\5\26\2\uffff\62\26\1\uffff\11\26\1\uffff"+
            "\4\26\1\uffff\3\26\1\uffff\13\26\1\uffff\6\26\1\uffff\2\26\1"+
            "\125\1\26\1\uffff\3\26\1\uffff\1\26\3\uffff\2\26\2\uffff\1\26",
            "\3\131\3\uffff\1\131\3\uffff\2\131\1\uffff\1\127\2\uffff\2"+
            "\131\1\uffff\2\131\1\uffff\27\131\2\uffff\1\131\1\uffff\4\131"+
            "\1\uffff\6\131\1\uffff\4\131\1\uffff\2\131\1\uffff\20\131\1"+
            "\uffff\10\131\1\uffff\4\131\1\uffff\10\131\1\uffff\5\131\1\uffff"+
            "\7\131\1\uffff\25\131\1\uffff\12\131\1\uffff\4\131\1\uffff\10"+
            "\131\1\uffff\7\131\1\uffff\2\131\1\uffff\5\131\2\uffff\62\131"+
            "\1\uffff\11\131\1\uffff\4\131\1\uffff\3\131\1\uffff\13\131\1"+
            "\uffff\6\131\1\uffff\2\131\1\130\1\131\1\uffff\3\131\1\uffff"+
            "\1\131\3\uffff\2\131\2\uffff\1\131\1\uffff\1\126",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\3\3\3\uffff\1\3\3\uffff\2\3\1\uffff\1\u0098\2\uffff\2\3\1"+
            "\uffff\2\3\1\uffff\27\3\2\uffff\1\3\1\uffff\4\3\1\uffff\6\3"+
            "\1\uffff\4\3\1\uffff\2\3\1\uffff\20\3\1\uffff\10\3\1\uffff\4"+
            "\3\1\uffff\10\3\1\uffff\5\3\1\uffff\7\3\1\uffff\25\3\1\uffff"+
            "\12\3\1\uffff\4\3\1\uffff\10\3\1\uffff\7\3\1\uffff\2\3\1\uffff"+
            "\5\3\2\uffff\62\3\1\uffff\11\3\1\uffff\4\3\1\uffff\3\3\1\uffff"+
            "\13\3\1\uffff\6\3\1\uffff\2\3\1\u00d6\1\3\1\uffff\3\3\1\uffff"+
            "\1\3\3\uffff\2\3\2\uffff\1\3",
            "\3\3\3\uffff\1\3\3\uffff\2\3\1\uffff\1\u00d8\2\uffff\2\3\1"+
            "\uffff\2\3\1\uffff\27\3\2\uffff\1\3\1\uffff\4\3\1\uffff\6\3"+
            "\1\uffff\4\3\1\uffff\2\3\1\uffff\20\3\1\uffff\10\3\1\uffff\4"+
            "\3\1\uffff\10\3\1\uffff\5\3\1\uffff\7\3\1\uffff\25\3\1\uffff"+
            "\12\3\1\uffff\4\3\1\uffff\10\3\1\uffff\7\3\1\uffff\2\3\1\uffff"+
            "\5\3\2\uffff\62\3\1\uffff\11\3\1\uffff\4\3\1\uffff\3\3\1\uffff"+
            "\13\3\1\uffff\6\3\1\uffff\2\3\1\u0116\1\3\1\uffff\3\3\1\uffff"+
            "\1\3\3\uffff\2\3\2\uffff\1\3",
            "",
            "\1\u0117\5\uffff\1\u0117\4\uffff\1\u0117\7\uffff\7\u0117\1"+
            "\uffff\22\u0117\1\uffff\4\u0117\1\uffff\6\u0117\1\uffff\2\u0117"+
            "\1\uffff\1\u0117\1\uffff\2\u0117\1\uffff\20\u0117\1\uffff\4"+
            "\u0117\1\uffff\1\u0117\1\uffff\1\u0117\1\uffff\4\u0117\1\uffff"+
            "\10\u0117\1\uffff\3\u0117\1\uffff\1\u0117\1\uffff\4\u0117\1"+
            "\uffff\23\u0117\1\uffff\4\u0117\1\uffff\12\u0117\1\uffff\4\u0117"+
            "\1\uffff\10\u0117\1\uffff\1\u0117\1\uffff\5\u0117\1\uffff\2"+
            "\u0117\1\uffff\5\u0117\2\uffff\14\u0117\1\uffff\20\u0117\1\uffff"+
            "\24\u0117\1\uffff\3\u0117\1\uffff\5\u0117\1\uffff\4\u0117\1"+
            "\uffff\3\u0117\1\uffff\13\u0117\1\uffff\1\u0117\1\u012f\1\uffff"+
            "\1\u0117\1\uffff\1\u0117\3\uffff\1\u0117\2\uffff\1\u0117\2\uffff"+
            "\2\u0117\10\uffff\4\u0117",
            "",
            "\3\131\3\uffff\1\131\3\uffff\2\131\1\uffff\1\u0131\2\uffff"+
            "\2\131\1\uffff\2\131\1\uffff\27\131\2\uffff\1\131\1\uffff\4"+
            "\131\1\uffff\6\131\1\uffff\4\131\1\uffff\2\131\1\uffff\20\131"+
            "\1\uffff\10\131\1\uffff\4\131\1\uffff\10\131\1\uffff\5\131\1"+
            "\uffff\7\131\1\uffff\25\131\1\uffff\12\131\1\uffff\4\131\1\uffff"+
            "\10\131\1\uffff\7\131\1\uffff\2\131\1\uffff\5\131\2\uffff\62"+
            "\131\1\uffff\11\131\1\uffff\4\131\1\uffff\3\131\1\uffff\13\131"+
            "\1\uffff\6\131\1\uffff\2\131\1\u0130\1\131\1\uffff\3\131\1\uffff"+
            "\1\131\3\uffff\2\131\2\uffff\1\131",
            "",
            "\3\131\3\uffff\1\131\3\uffff\2\131\1\uffff\1\u0171\2\uffff"+
            "\2\131\1\uffff\2\131\1\uffff\27\131\2\uffff\1\131\1\uffff\4"+
            "\131\1\uffff\6\131\1\uffff\4\131\1\uffff\2\131\1\uffff\20\131"+
            "\1\uffff\10\131\1\uffff\4\131\1\uffff\10\131\1\uffff\5\131\1"+
            "\uffff\7\131\1\uffff\25\131\1\uffff\12\131\1\uffff\4\131\1\uffff"+
            "\10\131\1\uffff\7\131\1\uffff\2\131\1\uffff\5\131\2\uffff\62"+
            "\131\1\uffff\11\131\1\uffff\4\131\1\uffff\3\131\1\uffff\13\131"+
            "\1\uffff\6\131\1\uffff\2\131\1\u0170\1\131\1\uffff\3\131\1\uffff"+
            "\1\131\3\uffff\2\131\2\uffff\1\131",
            "\3\131\3\uffff\1\131\3\uffff\2\131\1\uffff\1\u01b1\2\uffff"+
            "\2\131\1\uffff\2\131\1\uffff\27\131\2\uffff\1\131\1\uffff\4"+
            "\131\1\uffff\6\131\1\uffff\4\131\1\uffff\2\131\1\uffff\20\131"+
            "\1\uffff\10\131\1\uffff\4\131\1\uffff\10\131\1\uffff\5\131\1"+
            "\uffff\7\131\1\uffff\25\131\1\uffff\12\131\1\uffff\4\131\1\uffff"+
            "\10\131\1\uffff\7\131\1\uffff\2\131\1\uffff\5\131\2\uffff\62"+
            "\131\1\uffff\11\131\1\uffff\4\131\1\uffff\3\131\1\uffff\13\131"+
            "\1\uffff\6\131\1\uffff\2\131\1\u01b0\1\131\1\uffff\3\131\1\uffff"+
            "\1\131\3\uffff\2\131\2\uffff\1\131",
            "\3\131\3\uffff\1\131\3\uffff\2\131\1\uffff\1\u01f0\2\uffff"+
            "\2\131\1\uffff\2\131\1\uffff\27\131\2\uffff\1\131\1\uffff\4"+
            "\131\1\uffff\6\131\1\uffff\4\131\1\uffff\2\131\1\uffff\20\131"+
            "\1\uffff\10\131\1\uffff\4\131\1\uffff\10\131\1\uffff\5\131\1"+
            "\uffff\7\131\1\uffff\25\131\1\uffff\12\131\1\uffff\4\131\1\uffff"+
            "\10\131\1\uffff\7\131\1\uffff\2\131\1\uffff\5\131\2\uffff\62"+
            "\131\1\uffff\11\131\1\uffff\4\131\1\uffff\3\131\1\uffff\13\131"+
            "\1\uffff\6\131\1\uffff\2\131\1\u01f1\1\131\1\uffff\3\131\1\uffff"+
            "\1\131\3\uffff\2\131\2\uffff\1\131",
            "\3\131\3\uffff\1\131\3\uffff\2\131\1\uffff\1\u0230\2\uffff"+
            "\2\131\1\uffff\2\131\1\uffff\27\131\2\uffff\1\131\1\uffff\4"+
            "\131\1\uffff\6\131\1\uffff\4\131\1\uffff\2\131\1\uffff\20\131"+
            "\1\uffff\10\131\1\uffff\4\131\1\uffff\10\131\1\uffff\5\131\1"+
            "\uffff\7\131\1\uffff\25\131\1\uffff\12\131\1\uffff\4\131\1\uffff"+
            "\10\131\1\uffff\7\131\1\uffff\2\131\1\uffff\5\131\2\uffff\62"+
            "\131\1\uffff\11\131\1\uffff\4\131\1\uffff\3\131\1\uffff\13\131"+
            "\1\uffff\6\131\1\uffff\2\131\1\u0231\1\131\1\uffff\3\131\1\uffff"+
            "\1\131\3\uffff\2\131\2\uffff\1\131",
            "",
            "",
            "\1\u0270\6\u0271\1\uffff\17\u0271\2\uffff\1\u0271\1\uffff\4"+
            "\u0271\1\uffff\6\u0271\1\uffff\2\u0271\1\uffff\1\u0271\1\uffff"+
            "\2\u0271\1\uffff\20\u0271\1\uffff\4\u0271\1\uffff\1\u0271\1"+
            "\uffff\1\u0271\1\uffff\4\u0271\1\uffff\10\u0271\1\uffff\3\u0271"+
            "\1\uffff\1\u0271\1\uffff\4\u0271\1\uffff\2\u0271\1\uffff\20"+
            "\u0271\1\uffff\4\u0271\1\uffff\12\u0271\2\uffff\3\u0271\1\uffff"+
            "\3\u0271\1\uffff\4\u0271\1\uffff\1\u0271\1\uffff\5\u0271\1\uffff"+
            "\2\u0271\1\uffff\5\u0271\2\uffff\14\u0271\1\uffff\20\u0271\1"+
            "\uffff\24\u0271\1\uffff\3\u0271\1\uffff\5\u0271\1\uffff\4\u0271"+
            "\1\uffff\3\u0271\1\uffff\13\u0271\1\uffff\1\u0271\2\uffff\1"+
            "\u0271\1\uffff\1\u0271",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\u0272\6\u0273\1\uffff\17\u0273\2\uffff\1\u0273\1\uffff\4"+
            "\u0273\1\uffff\6\u0273\1\uffff\2\u0273\1\uffff\1\u0273\1\uffff"+
            "\2\u0273\1\uffff\20\u0273\1\uffff\4\u0273\1\uffff\1\u0273\1"+
            "\uffff\1\u0273\1\uffff\4\u0273\1\uffff\10\u0273\1\uffff\3\u0273"+
            "\1\uffff\1\u0273\1\uffff\4\u0273\1\uffff\2\u0273\1\uffff\20"+
            "\u0273\1\uffff\4\u0273\1\uffff\12\u0273\2\uffff\3\u0273\1\uffff"+
            "\3\u0273\1\uffff\4\u0273\1\uffff\1\u0273\1\uffff\5\u0273\1\uffff"+
            "\2\u0273\1\uffff\5\u0273\2\uffff\14\u0273\1\uffff\20\u0273\1"+
            "\uffff\24\u0273\1\uffff\3\u0273\1\uffff\5\u0273\1\uffff\4\u0273"+
            "\1\uffff\3\u0273\1\uffff\13\u0273\1\uffff\1\u0273\2\uffff\1"+
            "\u0273\1\uffff\1\u0273",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\u0274\6\u0275\1\uffff\17\u0275\2\uffff\1\u0275\1\uffff\4"+
            "\u0275\1\uffff\6\u0275\1\uffff\2\u0275\1\uffff\1\u0275\1\uffff"+
            "\2\u0275\1\uffff\20\u0275\1\uffff\4\u0275\1\uffff\1\u0275\1"+
            "\uffff\1\u0275\1\uffff\4\u0275\1\uffff\10\u0275\1\uffff\3\u0275"+
            "\1\uffff\1\u0275\1\uffff\4\u0275\1\uffff\2\u0275\1\uffff\20"+
            "\u0275\1\uffff\4\u0275\1\uffff\12\u0275\2\uffff\3\u0275\1\uffff"+
            "\3\u0275\1\uffff\4\u0275\1\uffff\1\u0275\1\uffff\5\u0275\1\uffff"+
            "\2\u0275\1\uffff\5\u0275\2\uffff\14\u0275\1\uffff\20\u0275\1"+
            "\uffff\24\u0275\1\uffff\3\u0275\1\uffff\5\u0275\1\uffff\4\u0275"+
            "\1\uffff\3\u0275\1\uffff\13\u0275\1\uffff\1\u0275\2\uffff\1"+
            "\u0275\1\uffff\1\u0275",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\u0276\6\u0277\1\uffff\17\u0277\2\uffff\1\u0277\1\uffff\4"+
            "\u0277\1\uffff\6\u0277\1\uffff\2\u0277\1\uffff\1\u0277\1\uffff"+
            "\2\u0277\1\uffff\20\u0277\1\uffff\4\u0277\1\uffff\1\u0277\1"+
            "\uffff\1\u0277\1\uffff\4\u0277\1\uffff\10\u0277\1\uffff\3\u0277"+
            "\1\uffff\1\u0277\1\uffff\4\u0277\1\uffff\2\u0277\1\uffff\20"+
            "\u0277\1\uffff\4\u0277\1\uffff\12\u0277\2\uffff\3\u0277\1\uffff"+
            "\3\u0277\1\uffff\4\u0277\1\uffff\1\u0277\1\uffff\5\u0277\1\uffff"+
            "\2\u0277\1\uffff\5\u0277\2\uffff\14\u0277\1\uffff\20\u0277\1"+
            "\uffff\24\u0277\1\uffff\3\u0277\1\uffff\5\u0277\1\uffff\4\u0277"+
            "\1\uffff\3\u0277\1\uffff\13\u0277\1\uffff\1\u0277\2\uffff\1"+
            "\u0277\1\uffff\1\u0277",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\u0278\6\u0279\1\uffff\17\u0279\2\uffff\1\u0279\1\uffff\4"+
            "\u0279\1\uffff\6\u0279\1\uffff\2\u0279\1\uffff\1\u0279\1\uffff"+
            "\2\u0279\1\uffff\20\u0279\1\uffff\4\u0279\1\uffff\1\u0279\1"+
            "\uffff\1\u0279\1\uffff\4\u0279\1\uffff\10\u0279\1\uffff\3\u0279"+
            "\1\uffff\1\u0279\1\uffff\4\u0279\1\uffff\2\u0279\1\uffff\20"+
            "\u0279\1\uffff\4\u0279\1\uffff\12\u0279\2\uffff\3\u0279\1\uffff"+
            "\3\u0279\1\uffff\4\u0279\1\uffff\1\u0279\1\uffff\5\u0279\1\uffff"+
            "\2\u0279\1\uffff\5\u0279\2\uffff\14\u0279\1\uffff\20\u0279\1"+
            "\uffff\24\u0279\1\uffff\3\u0279\1\uffff\5\u0279\1\uffff\4\u0279"+
            "\1\uffff\3\u0279\1\uffff\13\u0279\1\uffff\1\u0279\2\uffff\1"+
            "\u0279\1\uffff\1\u0279",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\u027a\6\u027b\1\uffff\17\u027b\2\uffff\1\u027b\1\uffff\4"+
            "\u027b\1\uffff\6\u027b\1\uffff\2\u027b\1\uffff\1\u027b\1\uffff"+
            "\2\u027b\1\uffff\20\u027b\1\uffff\4\u027b\1\uffff\1\u027b\1"+
            "\uffff\1\u027b\1\uffff\4\u027b\1\uffff\10\u027b\1\uffff\3\u027b"+
            "\1\uffff\1\u027b\1\uffff\4\u027b\1\uffff\2\u027b\1\uffff\20"+
            "\u027b\1\uffff\4\u027b\1\uffff\12\u027b\2\uffff\3\u027b\1\uffff"+
            "\3\u027b\1\uffff\4\u027b\1\uffff\1\u027b\1\uffff\5\u027b\1\uffff"+
            "\2\u027b\1\uffff\5\u027b\2\uffff\14\u027b\1\uffff\20\u027b\1"+
            "\uffff\24\u027b\1\uffff\3\u027b\1\uffff\5\u027b\1\uffff\4\u027b"+
            "\1\uffff\3\u027b\1\uffff\13\u027b\1\uffff\1\u027b\2\uffff\1"+
            "\u027b\1\uffff\1\u027b",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\u027c\6\u027d\1\uffff\17\u027d\2\uffff\1\u027d\1\uffff\4"+
            "\u027d\1\uffff\6\u027d\1\uffff\2\u027d\1\uffff\1\u027d\1\uffff"+
            "\2\u027d\1\uffff\20\u027d\1\uffff\4\u027d\1\uffff\1\u027d\1"+
            "\uffff\1\u027d\1\uffff\4\u027d\1\uffff\10\u027d\1\uffff\3\u027d"+
            "\1\uffff\1\u027d\1\uffff\4\u027d\1\uffff\2\u027d\1\uffff\20"+
            "\u027d\1\uffff\4\u027d\1\uffff\12\u027d\2\uffff\3\u027d\1\uffff"+
            "\3\u027d\1\uffff\4\u027d\1\uffff\1\u027d\1\uffff\5\u027d\1\uffff"+
            "\2\u027d\1\uffff\5\u027d\2\uffff\14\u027d\1\uffff\20\u027d\1"+
            "\uffff\24\u027d\1\uffff\3\u027d\1\uffff\5\u027d\1\uffff\4\u027d"+
            "\1\uffff\3\u027d\1\uffff\13\u027d\1\uffff\1\u027d\2\uffff\1"+
            "\u027d\1\uffff\1\u027d",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\u027e\6\u027f\1\uffff\17\u027f\2\uffff\1\u027f\1\uffff\4"+
            "\u027f\1\uffff\6\u027f\1\uffff\2\u027f\1\uffff\1\u027f\1\uffff"+
            "\2\u027f\1\uffff\20\u027f\1\uffff\4\u027f\1\uffff\1\u027f\1"+
            "\uffff\1\u027f\1\uffff\4\u027f\1\uffff\10\u027f\1\uffff\3\u027f"+
            "\1\uffff\1\u027f\1\uffff\4\u027f\1\uffff\2\u027f\1\uffff\20"+
            "\u027f\1\uffff\4\u027f\1\uffff\12\u027f\2\uffff\3\u027f\1\uffff"+
            "\3\u027f\1\uffff\4\u027f\1\uffff\1\u027f\1\uffff\5\u027f\1\uffff"+
            "\2\u027f\1\uffff\5\u027f\2\uffff\14\u027f\1\uffff\20\u027f\1"+
            "\uffff\24\u027f\1\uffff\3\u027f\1\uffff\5\u027f\1\uffff\4\u027f"+
            "\1\uffff\3\u027f\1\uffff\13\u027f\1\uffff\1\u027f\2\uffff\1"+
            "\u027f\1\uffff\1\u027f",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\u0280\6\u0281\1\uffff\17\u0281\2\uffff\1\u0281\1\uffff\4"+
            "\u0281\1\uffff\6\u0281\1\uffff\2\u0281\1\uffff\1\u0281\1\uffff"+
            "\2\u0281\1\uffff\20\u0281\1\uffff\4\u0281\1\uffff\1\u0281\1"+
            "\uffff\1\u0281\1\uffff\4\u0281\1\uffff\10\u0281\1\uffff\3\u0281"+
            "\1\uffff\1\u0281\1\uffff\4\u0281\1\uffff\2\u0281\1\uffff\20"+
            "\u0281\1\uffff\4\u0281\1\uffff\12\u0281\2\uffff\3\u0281\1\uffff"+
            "\3\u0281\1\uffff\4\u0281\1\uffff\1\u0281\1\uffff\5\u0281\1\uffff"+
            "\2\u0281\1\uffff\5\u0281\2\uffff\14\u0281\1\uffff\20\u0281\1"+
            "\uffff\24\u0281\1\uffff\3\u0281\1\uffff\5\u0281\1\uffff\4\u0281"+
            "\1\uffff\3\u0281\1\uffff\13\u0281\1\uffff\1\u0281\2\uffff\1"+
            "\u0281\1\uffff\1\u0281",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff"
    };

    static final short[] DFA32_eot = DFA.unpackEncodedString(DFA32_eotS);
    static final short[] DFA32_eof = DFA.unpackEncodedString(DFA32_eofS);
    static final char[] DFA32_min = DFA.unpackEncodedStringToUnsignedChars(DFA32_minS);
    static final char[] DFA32_max = DFA.unpackEncodedStringToUnsignedChars(DFA32_maxS);
    static final short[] DFA32_accept = DFA.unpackEncodedString(DFA32_acceptS);
    static final short[] DFA32_special = DFA.unpackEncodedString(DFA32_specialS);
    static final short[][] DFA32_transition;

    static {
        int numStates = DFA32_transitionS.length;
        DFA32_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA32_transition[i] = DFA.unpackEncodedString(DFA32_transitionS[i]);
        }
    }

    class DFA32 extends DFA {

        public DFA32(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 32;
            this.eot = DFA32_eot;
            this.eof = DFA32_eof;
            this.min = DFA32_min;
            this.max = DFA32_max;
            this.accept = DFA32_accept;
            this.special = DFA32_special;
            this.transition = DFA32_transition;
        }
        public String getDescription() {
            return "260:1: atomExpression : ( KW_NULL -> TOK_NULL | dateLiteral | constant | castExpression | caseExpression | whenExpression | ( functionName LPAREN )=> function | tableOrColumn | LPAREN ! expression RPAREN !);";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA32_0 = input.LA(1);

                         
                        int index32_0 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (LA32_0==KW_NULL) ) {s = 1;}

                        else if ( (LA32_0==KW_DATE) ) {s = 2;}

                        else if ( (LA32_0==BigintLiteral||LA32_0==CharSetName||LA32_0==DecimalLiteral||LA32_0==Number||(LA32_0 >= SmallintLiteral && LA32_0 <= StringLiteral)||LA32_0==TinyintLiteral) ) {s = 3;}

                        else if ( (LA32_0==KW_TRUE) ) {s = 10;}

                        else if ( (LA32_0==KW_FALSE) ) {s = 11;}

                        else if ( (LA32_0==KW_CAST) ) {s = 12;}

                        else if ( (LA32_0==KW_CASE) ) {s = 13;}

                        else if ( (LA32_0==KW_IF) && (synpred5_IdentifiersParser())) {s = 14;}

                        else if ( (LA32_0==KW_ARRAY) ) {s = 15;}

                        else if ( (LA32_0==KW_MAP) && (synpred5_IdentifiersParser())) {s = 16;}

                        else if ( (LA32_0==KW_STRUCT) ) {s = 17;}

                        else if ( (LA32_0==KW_UNIONTYPE) ) {s = 18;}

                        else if ( (LA32_0==Identifier) ) {s = 19;}

                        else if ( ((LA32_0 >= KW_ADD && LA32_0 <= KW_ANALYZE)||LA32_0==KW_ARCHIVE||(LA32_0 >= KW_AS && LA32_0 <= KW_CASCADE)||LA32_0==KW_CHANGE||(LA32_0 >= KW_CLUSTER && LA32_0 <= KW_COLLECTION)||(LA32_0 >= KW_COLUMNS && LA32_0 <= KW_CONCATENATE)||(LA32_0 >= KW_CONTINUE && LA32_0 <= KW_CREATE)||LA32_0==KW_CUBE||(LA32_0 >= KW_CURSOR && LA32_0 <= KW_DATA)||LA32_0==KW_DATABASES||(LA32_0 >= KW_DATETIME && LA32_0 <= KW_DISABLE)||(LA32_0 >= KW_DISTRIBUTE && LA32_0 <= KW_ELEM_TYPE)||LA32_0==KW_ENABLE||LA32_0==KW_ESCAPED||(LA32_0 >= KW_EXCLUSIVE && LA32_0 <= KW_EXPORT)||LA32_0==KW_EXTERNAL||(LA32_0 >= KW_FETCH && LA32_0 <= KW_FLOAT)||(LA32_0 >= KW_FOR && LA32_0 <= KW_FORMATTED)||LA32_0==KW_FULL||(LA32_0 >= KW_FUNCTIONS && LA32_0 <= KW_GROUPING)||(LA32_0 >= KW_HOLD_DDLTIME && LA32_0 <= KW_IDXPROPERTIES)||(LA32_0 >= KW_IGNORE && LA32_0 <= KW_JAR)||(LA32_0 >= KW_KEYS && LA32_0 <= KW_LEFT)||(LA32_0 >= KW_LIKE && LA32_0 <= KW_LONG)||(LA32_0 >= KW_MAPJOIN && LA32_0 <= KW_MINUS)||(LA32_0 >= KW_MSCK && LA32_0 <= KW_NOSCAN)||LA32_0==KW_NO_DROP||(LA32_0 >= KW_OF && LA32_0 <= KW_OFFLINE)||LA32_0==KW_OPTION||(LA32_0 >= KW_ORDER && LA32_0 <= KW_OUTPUTFORMAT)||(LA32_0 >= KW_OVERWRITE && LA32_0 <= KW_OWNER)||(LA32_0 >= KW_PARTITION && LA32_0 <= KW_PLUS)||(LA32_0 >= KW_PRETTY && LA32_0 <= KW_RECORDWRITER)||(LA32_0 >= KW_REGEXP && LA32_0 <= KW_SCHEMAS)||(LA32_0 >= KW_SEMI && LA32_0 <= KW_STRING)||(LA32_0 >= KW_TABLE && LA32_0 <= KW_TABLES)||(LA32_0 >= KW_TBLPROPERTIES && LA32_0 <= KW_TERMINATED)||(LA32_0 >= KW_TIMESTAMP && LA32_0 <= KW_TRANSACTIONS)||LA32_0==KW_TRIGGER||(LA32_0 >= KW_TRUNCATE && LA32_0 <= KW_UNARCHIVE)||(LA32_0 >= KW_UNDO && LA32_0 <= KW_UNION)||(LA32_0 >= KW_UNLOCK && LA32_0 <= KW_VALUE_TYPE)||LA32_0==KW_VIEW||LA32_0==KW_WHILE||LA32_0==KW_WITH) ) {s = 20;}

                        else if ( (LA32_0==LPAREN) ) {s = 21;}

                         
                        input.seek(index32_0);

                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA32_1 = input.LA(1);

                         
                        int index32_1 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (LA32_1==EOF||(LA32_1 >= AMPERSAND && LA32_1 <= BITWISEXOR)||LA32_1==COMMA||(LA32_1 >= DIV && LA32_1 <= DIVIDE)||(LA32_1 >= EQUAL && LA32_1 <= EQUAL_NS)||(LA32_1 >= GREATERTHAN && LA32_1 <= GREATERTHANOREQUALTO)||(LA32_1 >= Identifier && LA32_1 <= KW_CASCADE)||LA32_1==KW_CHANGE||(LA32_1 >= KW_CLUSTER && LA32_1 <= KW_COLLECTION)||(LA32_1 >= KW_COLUMNS && LA32_1 <= KW_CONCATENATE)||(LA32_1 >= KW_CONTINUE && LA32_1 <= KW_CUBE)||(LA32_1 >= KW_CURSOR && LA32_1 <= KW_DATA)||(LA32_1 >= KW_DATABASES && LA32_1 <= KW_DISABLE)||(LA32_1 >= KW_DISTRIBUTE && LA32_1 <= KW_ESCAPED)||(LA32_1 >= KW_EXCLUSIVE && LA32_1 <= KW_EXPORT)||(LA32_1 >= KW_EXTERNAL && LA32_1 <= KW_FLOAT)||(LA32_1 >= KW_FOR && LA32_1 <= KW_FULL)||(LA32_1 >= KW_FUNCTIONS && LA32_1 <= KW_IDXPROPERTIES)||(LA32_1 >= KW_IGNORE && LA32_1 <= KW_LEFT)||(LA32_1 >= KW_LIKE && LA32_1 <= KW_LONG)||(LA32_1 >= KW_MAP && LA32_1 <= KW_MINUS)||(LA32_1 >= KW_MSCK && LA32_1 <= KW_OFFLINE)||(LA32_1 >= KW_OPTION && LA32_1 <= KW_OUTPUTFORMAT)||(LA32_1 >= KW_OVERWRITE && LA32_1 <= KW_OWNER)||(LA32_1 >= KW_PARTITION && LA32_1 <= KW_PLUS)||(LA32_1 >= KW_PRETTY && LA32_1 <= KW_TABLES)||(LA32_1 >= KW_TBLPROPERTIES && LA32_1 <= KW_TRANSACTIONS)||(LA32_1 >= KW_TRIGGER && LA32_1 <= KW_UNARCHIVE)||(LA32_1 >= KW_UNDO && LA32_1 <= KW_UNIONTYPE)||(LA32_1 >= KW_UNLOCK && LA32_1 <= KW_VALUE_TYPE)||(LA32_1 >= KW_VIEW && LA32_1 <= KW_WITH)||(LA32_1 >= LESSTHAN && LA32_1 <= LESSTHANOREQUALTO)||LA32_1==LSQUARE||(LA32_1 >= MINUS && LA32_1 <= NOTEQUAL)||LA32_1==PLUS||(LA32_1 >= RPAREN && LA32_1 <= RSQUARE)||LA32_1==STAR) ) {s = 22;}

                        else if ( (LA32_1==DOT) ) {s = 23;}

                        else if ( (LA32_1==LPAREN) && (synpred5_IdentifiersParser())) {s = 85;}

                         
                        input.seek(index32_1);

                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA32_2 = input.LA(1);

                         
                        int index32_2 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (LA32_2==StringLiteral) ) {s = 86;}

                        else if ( (LA32_2==DOT) ) {s = 87;}

                        else if ( (LA32_2==LPAREN) && (synpred5_IdentifiersParser())) {s = 88;}

                        else if ( (LA32_2==EOF||(LA32_2 >= AMPERSAND && LA32_2 <= BITWISEXOR)||LA32_2==COMMA||(LA32_2 >= DIV && LA32_2 <= DIVIDE)||(LA32_2 >= EQUAL && LA32_2 <= EQUAL_NS)||(LA32_2 >= GREATERTHAN && LA32_2 <= GREATERTHANOREQUALTO)||(LA32_2 >= Identifier && LA32_2 <= KW_CASCADE)||LA32_2==KW_CHANGE||(LA32_2 >= KW_CLUSTER && LA32_2 <= KW_COLLECTION)||(LA32_2 >= KW_COLUMNS && LA32_2 <= KW_CONCATENATE)||(LA32_2 >= KW_CONTINUE && LA32_2 <= KW_CUBE)||(LA32_2 >= KW_CURSOR && LA32_2 <= KW_DATA)||(LA32_2 >= KW_DATABASES && LA32_2 <= KW_DISABLE)||(LA32_2 >= KW_DISTRIBUTE && LA32_2 <= KW_ESCAPED)||(LA32_2 >= KW_EXCLUSIVE && LA32_2 <= KW_EXPORT)||(LA32_2 >= KW_EXTERNAL && LA32_2 <= KW_FLOAT)||(LA32_2 >= KW_FOR && LA32_2 <= KW_FULL)||(LA32_2 >= KW_FUNCTIONS && LA32_2 <= KW_IDXPROPERTIES)||(LA32_2 >= KW_IGNORE && LA32_2 <= KW_LEFT)||(LA32_2 >= KW_LIKE && LA32_2 <= KW_LONG)||(LA32_2 >= KW_MAP && LA32_2 <= KW_MINUS)||(LA32_2 >= KW_MSCK && LA32_2 <= KW_OFFLINE)||(LA32_2 >= KW_OPTION && LA32_2 <= KW_OUTPUTFORMAT)||(LA32_2 >= KW_OVERWRITE && LA32_2 <= KW_OWNER)||(LA32_2 >= KW_PARTITION && LA32_2 <= KW_PLUS)||(LA32_2 >= KW_PRETTY && LA32_2 <= KW_TABLES)||(LA32_2 >= KW_TBLPROPERTIES && LA32_2 <= KW_TRANSACTIONS)||(LA32_2 >= KW_TRIGGER && LA32_2 <= KW_UNARCHIVE)||(LA32_2 >= KW_UNDO && LA32_2 <= KW_UNIONTYPE)||(LA32_2 >= KW_UNLOCK && LA32_2 <= KW_VALUE_TYPE)||(LA32_2 >= KW_VIEW && LA32_2 <= KW_WITH)||(LA32_2 >= LESSTHAN && LA32_2 <= LESSTHANOREQUALTO)||LA32_2==LSQUARE||(LA32_2 >= MINUS && LA32_2 <= NOTEQUAL)||LA32_2==PLUS||(LA32_2 >= RPAREN && LA32_2 <= RSQUARE)||LA32_2==STAR) ) {s = 89;}

                         
                        input.seek(index32_2);

                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA32_10 = input.LA(1);

                         
                        int index32_10 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (LA32_10==EOF||(LA32_10 >= AMPERSAND && LA32_10 <= BITWISEXOR)||LA32_10==COMMA||(LA32_10 >= DIV && LA32_10 <= DIVIDE)||(LA32_10 >= EQUAL && LA32_10 <= EQUAL_NS)||(LA32_10 >= GREATERTHAN && LA32_10 <= GREATERTHANOREQUALTO)||(LA32_10 >= Identifier && LA32_10 <= KW_CASCADE)||LA32_10==KW_CHANGE||(LA32_10 >= KW_CLUSTER && LA32_10 <= KW_COLLECTION)||(LA32_10 >= KW_COLUMNS && LA32_10 <= KW_CONCATENATE)||(LA32_10 >= KW_CONTINUE && LA32_10 <= KW_CUBE)||(LA32_10 >= KW_CURSOR && LA32_10 <= KW_DATA)||(LA32_10 >= KW_DATABASES && LA32_10 <= KW_DISABLE)||(LA32_10 >= KW_DISTRIBUTE && LA32_10 <= KW_ESCAPED)||(LA32_10 >= KW_EXCLUSIVE && LA32_10 <= KW_EXPORT)||(LA32_10 >= KW_EXTERNAL && LA32_10 <= KW_FLOAT)||(LA32_10 >= KW_FOR && LA32_10 <= KW_FULL)||(LA32_10 >= KW_FUNCTIONS && LA32_10 <= KW_IDXPROPERTIES)||(LA32_10 >= KW_IGNORE && LA32_10 <= KW_LEFT)||(LA32_10 >= KW_LIKE && LA32_10 <= KW_LONG)||(LA32_10 >= KW_MAP && LA32_10 <= KW_MINUS)||(LA32_10 >= KW_MSCK && LA32_10 <= KW_OFFLINE)||(LA32_10 >= KW_OPTION && LA32_10 <= KW_OUTPUTFORMAT)||(LA32_10 >= KW_OVERWRITE && LA32_10 <= KW_OWNER)||(LA32_10 >= KW_PARTITION && LA32_10 <= KW_PLUS)||(LA32_10 >= KW_PRETTY && LA32_10 <= KW_TABLES)||(LA32_10 >= KW_TBLPROPERTIES && LA32_10 <= KW_TRANSACTIONS)||(LA32_10 >= KW_TRIGGER && LA32_10 <= KW_UNARCHIVE)||(LA32_10 >= KW_UNDO && LA32_10 <= KW_UNIONTYPE)||(LA32_10 >= KW_UNLOCK && LA32_10 <= KW_VALUE_TYPE)||(LA32_10 >= KW_VIEW && LA32_10 <= KW_WITH)||(LA32_10 >= LESSTHAN && LA32_10 <= LESSTHANOREQUALTO)||LA32_10==LSQUARE||(LA32_10 >= MINUS && LA32_10 <= NOTEQUAL)||LA32_10==PLUS||(LA32_10 >= RPAREN && LA32_10 <= RSQUARE)||LA32_10==STAR) ) {s = 3;}

                        else if ( (LA32_10==DOT) ) {s = 152;}

                        else if ( (LA32_10==LPAREN) && (synpred5_IdentifiersParser())) {s = 214;}

                         
                        input.seek(index32_10);

                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA32_11 = input.LA(1);

                         
                        int index32_11 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (LA32_11==EOF||(LA32_11 >= AMPERSAND && LA32_11 <= BITWISEXOR)||LA32_11==COMMA||(LA32_11 >= DIV && LA32_11 <= DIVIDE)||(LA32_11 >= EQUAL && LA32_11 <= EQUAL_NS)||(LA32_11 >= GREATERTHAN && LA32_11 <= GREATERTHANOREQUALTO)||(LA32_11 >= Identifier && LA32_11 <= KW_CASCADE)||LA32_11==KW_CHANGE||(LA32_11 >= KW_CLUSTER && LA32_11 <= KW_COLLECTION)||(LA32_11 >= KW_COLUMNS && LA32_11 <= KW_CONCATENATE)||(LA32_11 >= KW_CONTINUE && LA32_11 <= KW_CUBE)||(LA32_11 >= KW_CURSOR && LA32_11 <= KW_DATA)||(LA32_11 >= KW_DATABASES && LA32_11 <= KW_DISABLE)||(LA32_11 >= KW_DISTRIBUTE && LA32_11 <= KW_ESCAPED)||(LA32_11 >= KW_EXCLUSIVE && LA32_11 <= KW_EXPORT)||(LA32_11 >= KW_EXTERNAL && LA32_11 <= KW_FLOAT)||(LA32_11 >= KW_FOR && LA32_11 <= KW_FULL)||(LA32_11 >= KW_FUNCTIONS && LA32_11 <= KW_IDXPROPERTIES)||(LA32_11 >= KW_IGNORE && LA32_11 <= KW_LEFT)||(LA32_11 >= KW_LIKE && LA32_11 <= KW_LONG)||(LA32_11 >= KW_MAP && LA32_11 <= KW_MINUS)||(LA32_11 >= KW_MSCK && LA32_11 <= KW_OFFLINE)||(LA32_11 >= KW_OPTION && LA32_11 <= KW_OUTPUTFORMAT)||(LA32_11 >= KW_OVERWRITE && LA32_11 <= KW_OWNER)||(LA32_11 >= KW_PARTITION && LA32_11 <= KW_PLUS)||(LA32_11 >= KW_PRETTY && LA32_11 <= KW_TABLES)||(LA32_11 >= KW_TBLPROPERTIES && LA32_11 <= KW_TRANSACTIONS)||(LA32_11 >= KW_TRIGGER && LA32_11 <= KW_UNARCHIVE)||(LA32_11 >= KW_UNDO && LA32_11 <= KW_UNIONTYPE)||(LA32_11 >= KW_UNLOCK && LA32_11 <= KW_VALUE_TYPE)||(LA32_11 >= KW_VIEW && LA32_11 <= KW_WITH)||(LA32_11 >= LESSTHAN && LA32_11 <= LESSTHANOREQUALTO)||LA32_11==LSQUARE||(LA32_11 >= MINUS && LA32_11 <= NOTEQUAL)||LA32_11==PLUS||(LA32_11 >= RPAREN && LA32_11 <= RSQUARE)||LA32_11==STAR) ) {s = 3;}

                        else if ( (LA32_11==DOT) ) {s = 216;}

                        else if ( (LA32_11==LPAREN) && (synpred5_IdentifiersParser())) {s = 278;}

                         
                        input.seek(index32_11);

                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA32_15 = input.LA(1);

                         
                        int index32_15 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (LA32_15==LPAREN) && (synpred5_IdentifiersParser())) {s = 304;}

                        else if ( (LA32_15==DOT) ) {s = 305;}

                        else if ( (LA32_15==EOF||(LA32_15 >= AMPERSAND && LA32_15 <= BITWISEXOR)||LA32_15==COMMA||(LA32_15 >= DIV && LA32_15 <= DIVIDE)||(LA32_15 >= EQUAL && LA32_15 <= EQUAL_NS)||(LA32_15 >= GREATERTHAN && LA32_15 <= GREATERTHANOREQUALTO)||(LA32_15 >= Identifier && LA32_15 <= KW_CASCADE)||LA32_15==KW_CHANGE||(LA32_15 >= KW_CLUSTER && LA32_15 <= KW_COLLECTION)||(LA32_15 >= KW_COLUMNS && LA32_15 <= KW_CONCATENATE)||(LA32_15 >= KW_CONTINUE && LA32_15 <= KW_CUBE)||(LA32_15 >= KW_CURSOR && LA32_15 <= KW_DATA)||(LA32_15 >= KW_DATABASES && LA32_15 <= KW_DISABLE)||(LA32_15 >= KW_DISTRIBUTE && LA32_15 <= KW_ESCAPED)||(LA32_15 >= KW_EXCLUSIVE && LA32_15 <= KW_EXPORT)||(LA32_15 >= KW_EXTERNAL && LA32_15 <= KW_FLOAT)||(LA32_15 >= KW_FOR && LA32_15 <= KW_FULL)||(LA32_15 >= KW_FUNCTIONS && LA32_15 <= KW_IDXPROPERTIES)||(LA32_15 >= KW_IGNORE && LA32_15 <= KW_LEFT)||(LA32_15 >= KW_LIKE && LA32_15 <= KW_LONG)||(LA32_15 >= KW_MAP && LA32_15 <= KW_MINUS)||(LA32_15 >= KW_MSCK && LA32_15 <= KW_OFFLINE)||(LA32_15 >= KW_OPTION && LA32_15 <= KW_OUTPUTFORMAT)||(LA32_15 >= KW_OVERWRITE && LA32_15 <= KW_OWNER)||(LA32_15 >= KW_PARTITION && LA32_15 <= KW_PLUS)||(LA32_15 >= KW_PRETTY && LA32_15 <= KW_TABLES)||(LA32_15 >= KW_TBLPROPERTIES && LA32_15 <= KW_TRANSACTIONS)||(LA32_15 >= KW_TRIGGER && LA32_15 <= KW_UNARCHIVE)||(LA32_15 >= KW_UNDO && LA32_15 <= KW_UNIONTYPE)||(LA32_15 >= KW_UNLOCK && LA32_15 <= KW_VALUE_TYPE)||(LA32_15 >= KW_VIEW && LA32_15 <= KW_WITH)||(LA32_15 >= LESSTHAN && LA32_15 <= LESSTHANOREQUALTO)||LA32_15==LSQUARE||(LA32_15 >= MINUS && LA32_15 <= NOTEQUAL)||LA32_15==PLUS||(LA32_15 >= RPAREN && LA32_15 <= RSQUARE)||LA32_15==STAR) ) {s = 89;}

                         
                        input.seek(index32_15);

                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA32_17 = input.LA(1);

                         
                        int index32_17 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (LA32_17==LPAREN) && (synpred5_IdentifiersParser())) {s = 368;}

                        else if ( (LA32_17==DOT) ) {s = 369;}

                        else if ( (LA32_17==EOF||(LA32_17 >= AMPERSAND && LA32_17 <= BITWISEXOR)||LA32_17==COMMA||(LA32_17 >= DIV && LA32_17 <= DIVIDE)||(LA32_17 >= EQUAL && LA32_17 <= EQUAL_NS)||(LA32_17 >= GREATERTHAN && LA32_17 <= GREATERTHANOREQUALTO)||(LA32_17 >= Identifier && LA32_17 <= KW_CASCADE)||LA32_17==KW_CHANGE||(LA32_17 >= KW_CLUSTER && LA32_17 <= KW_COLLECTION)||(LA32_17 >= KW_COLUMNS && LA32_17 <= KW_CONCATENATE)||(LA32_17 >= KW_CONTINUE && LA32_17 <= KW_CUBE)||(LA32_17 >= KW_CURSOR && LA32_17 <= KW_DATA)||(LA32_17 >= KW_DATABASES && LA32_17 <= KW_DISABLE)||(LA32_17 >= KW_DISTRIBUTE && LA32_17 <= KW_ESCAPED)||(LA32_17 >= KW_EXCLUSIVE && LA32_17 <= KW_EXPORT)||(LA32_17 >= KW_EXTERNAL && LA32_17 <= KW_FLOAT)||(LA32_17 >= KW_FOR && LA32_17 <= KW_FULL)||(LA32_17 >= KW_FUNCTIONS && LA32_17 <= KW_IDXPROPERTIES)||(LA32_17 >= KW_IGNORE && LA32_17 <= KW_LEFT)||(LA32_17 >= KW_LIKE && LA32_17 <= KW_LONG)||(LA32_17 >= KW_MAP && LA32_17 <= KW_MINUS)||(LA32_17 >= KW_MSCK && LA32_17 <= KW_OFFLINE)||(LA32_17 >= KW_OPTION && LA32_17 <= KW_OUTPUTFORMAT)||(LA32_17 >= KW_OVERWRITE && LA32_17 <= KW_OWNER)||(LA32_17 >= KW_PARTITION && LA32_17 <= KW_PLUS)||(LA32_17 >= KW_PRETTY && LA32_17 <= KW_TABLES)||(LA32_17 >= KW_TBLPROPERTIES && LA32_17 <= KW_TRANSACTIONS)||(LA32_17 >= KW_TRIGGER && LA32_17 <= KW_UNARCHIVE)||(LA32_17 >= KW_UNDO && LA32_17 <= KW_UNIONTYPE)||(LA32_17 >= KW_UNLOCK && LA32_17 <= KW_VALUE_TYPE)||(LA32_17 >= KW_VIEW && LA32_17 <= KW_WITH)||(LA32_17 >= LESSTHAN && LA32_17 <= LESSTHANOREQUALTO)||LA32_17==LSQUARE||(LA32_17 >= MINUS && LA32_17 <= NOTEQUAL)||LA32_17==PLUS||(LA32_17 >= RPAREN && LA32_17 <= RSQUARE)||LA32_17==STAR) ) {s = 89;}

                         
                        input.seek(index32_17);

                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA32_18 = input.LA(1);

                         
                        int index32_18 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (LA32_18==LPAREN) && (synpred5_IdentifiersParser())) {s = 432;}

                        else if ( (LA32_18==DOT) ) {s = 433;}

                        else if ( (LA32_18==EOF||(LA32_18 >= AMPERSAND && LA32_18 <= BITWISEXOR)||LA32_18==COMMA||(LA32_18 >= DIV && LA32_18 <= DIVIDE)||(LA32_18 >= EQUAL && LA32_18 <= EQUAL_NS)||(LA32_18 >= GREATERTHAN && LA32_18 <= GREATERTHANOREQUALTO)||(LA32_18 >= Identifier && LA32_18 <= KW_CASCADE)||LA32_18==KW_CHANGE||(LA32_18 >= KW_CLUSTER && LA32_18 <= KW_COLLECTION)||(LA32_18 >= KW_COLUMNS && LA32_18 <= KW_CONCATENATE)||(LA32_18 >= KW_CONTINUE && LA32_18 <= KW_CUBE)||(LA32_18 >= KW_CURSOR && LA32_18 <= KW_DATA)||(LA32_18 >= KW_DATABASES && LA32_18 <= KW_DISABLE)||(LA32_18 >= KW_DISTRIBUTE && LA32_18 <= KW_ESCAPED)||(LA32_18 >= KW_EXCLUSIVE && LA32_18 <= KW_EXPORT)||(LA32_18 >= KW_EXTERNAL && LA32_18 <= KW_FLOAT)||(LA32_18 >= KW_FOR && LA32_18 <= KW_FULL)||(LA32_18 >= KW_FUNCTIONS && LA32_18 <= KW_IDXPROPERTIES)||(LA32_18 >= KW_IGNORE && LA32_18 <= KW_LEFT)||(LA32_18 >= KW_LIKE && LA32_18 <= KW_LONG)||(LA32_18 >= KW_MAP && LA32_18 <= KW_MINUS)||(LA32_18 >= KW_MSCK && LA32_18 <= KW_OFFLINE)||(LA32_18 >= KW_OPTION && LA32_18 <= KW_OUTPUTFORMAT)||(LA32_18 >= KW_OVERWRITE && LA32_18 <= KW_OWNER)||(LA32_18 >= KW_PARTITION && LA32_18 <= KW_PLUS)||(LA32_18 >= KW_PRETTY && LA32_18 <= KW_TABLES)||(LA32_18 >= KW_TBLPROPERTIES && LA32_18 <= KW_TRANSACTIONS)||(LA32_18 >= KW_TRIGGER && LA32_18 <= KW_UNARCHIVE)||(LA32_18 >= KW_UNDO && LA32_18 <= KW_UNIONTYPE)||(LA32_18 >= KW_UNLOCK && LA32_18 <= KW_VALUE_TYPE)||(LA32_18 >= KW_VIEW && LA32_18 <= KW_WITH)||(LA32_18 >= LESSTHAN && LA32_18 <= LESSTHANOREQUALTO)||LA32_18==LSQUARE||(LA32_18 >= MINUS && LA32_18 <= NOTEQUAL)||LA32_18==PLUS||(LA32_18 >= RPAREN && LA32_18 <= RSQUARE)||LA32_18==STAR) ) {s = 89;}

                         
                        input.seek(index32_18);

                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA32_19 = input.LA(1);

                         
                        int index32_19 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (LA32_19==DOT) ) {s = 496;}

                        else if ( (LA32_19==LPAREN) && (synpred5_IdentifiersParser())) {s = 497;}

                        else if ( (LA32_19==EOF||(LA32_19 >= AMPERSAND && LA32_19 <= BITWISEXOR)||LA32_19==COMMA||(LA32_19 >= DIV && LA32_19 <= DIVIDE)||(LA32_19 >= EQUAL && LA32_19 <= EQUAL_NS)||(LA32_19 >= GREATERTHAN && LA32_19 <= GREATERTHANOREQUALTO)||(LA32_19 >= Identifier && LA32_19 <= KW_CASCADE)||LA32_19==KW_CHANGE||(LA32_19 >= KW_CLUSTER && LA32_19 <= KW_COLLECTION)||(LA32_19 >= KW_COLUMNS && LA32_19 <= KW_CONCATENATE)||(LA32_19 >= KW_CONTINUE && LA32_19 <= KW_CUBE)||(LA32_19 >= KW_CURSOR && LA32_19 <= KW_DATA)||(LA32_19 >= KW_DATABASES && LA32_19 <= KW_DISABLE)||(LA32_19 >= KW_DISTRIBUTE && LA32_19 <= KW_ESCAPED)||(LA32_19 >= KW_EXCLUSIVE && LA32_19 <= KW_EXPORT)||(LA32_19 >= KW_EXTERNAL && LA32_19 <= KW_FLOAT)||(LA32_19 >= KW_FOR && LA32_19 <= KW_FULL)||(LA32_19 >= KW_FUNCTIONS && LA32_19 <= KW_IDXPROPERTIES)||(LA32_19 >= KW_IGNORE && LA32_19 <= KW_LEFT)||(LA32_19 >= KW_LIKE && LA32_19 <= KW_LONG)||(LA32_19 >= KW_MAP && LA32_19 <= KW_MINUS)||(LA32_19 >= KW_MSCK && LA32_19 <= KW_OFFLINE)||(LA32_19 >= KW_OPTION && LA32_19 <= KW_OUTPUTFORMAT)||(LA32_19 >= KW_OVERWRITE && LA32_19 <= KW_OWNER)||(LA32_19 >= KW_PARTITION && LA32_19 <= KW_PLUS)||(LA32_19 >= KW_PRETTY && LA32_19 <= KW_TABLES)||(LA32_19 >= KW_TBLPROPERTIES && LA32_19 <= KW_TRANSACTIONS)||(LA32_19 >= KW_TRIGGER && LA32_19 <= KW_UNARCHIVE)||(LA32_19 >= KW_UNDO && LA32_19 <= KW_UNIONTYPE)||(LA32_19 >= KW_UNLOCK && LA32_19 <= KW_VALUE_TYPE)||(LA32_19 >= KW_VIEW && LA32_19 <= KW_WITH)||(LA32_19 >= LESSTHAN && LA32_19 <= LESSTHANOREQUALTO)||LA32_19==LSQUARE||(LA32_19 >= MINUS && LA32_19 <= NOTEQUAL)||LA32_19==PLUS||(LA32_19 >= RPAREN && LA32_19 <= RSQUARE)||LA32_19==STAR) ) {s = 89;}

                         
                        input.seek(index32_19);

                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA32_20 = input.LA(1);

                         
                        int index32_20 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (LA32_20==DOT) ) {s = 560;}

                        else if ( (LA32_20==LPAREN) && (synpred5_IdentifiersParser())) {s = 561;}

                        else if ( (LA32_20==EOF||(LA32_20 >= AMPERSAND && LA32_20 <= BITWISEXOR)||LA32_20==COMMA||(LA32_20 >= DIV && LA32_20 <= DIVIDE)||(LA32_20 >= EQUAL && LA32_20 <= EQUAL_NS)||(LA32_20 >= GREATERTHAN && LA32_20 <= GREATERTHANOREQUALTO)||(LA32_20 >= Identifier && LA32_20 <= KW_CASCADE)||LA32_20==KW_CHANGE||(LA32_20 >= KW_CLUSTER && LA32_20 <= KW_COLLECTION)||(LA32_20 >= KW_COLUMNS && LA32_20 <= KW_CONCATENATE)||(LA32_20 >= KW_CONTINUE && LA32_20 <= KW_CUBE)||(LA32_20 >= KW_CURSOR && LA32_20 <= KW_DATA)||(LA32_20 >= KW_DATABASES && LA32_20 <= KW_DISABLE)||(LA32_20 >= KW_DISTRIBUTE && LA32_20 <= KW_ESCAPED)||(LA32_20 >= KW_EXCLUSIVE && LA32_20 <= KW_EXPORT)||(LA32_20 >= KW_EXTERNAL && LA32_20 <= KW_FLOAT)||(LA32_20 >= KW_FOR && LA32_20 <= KW_FULL)||(LA32_20 >= KW_FUNCTIONS && LA32_20 <= KW_IDXPROPERTIES)||(LA32_20 >= KW_IGNORE && LA32_20 <= KW_LEFT)||(LA32_20 >= KW_LIKE && LA32_20 <= KW_LONG)||(LA32_20 >= KW_MAP && LA32_20 <= KW_MINUS)||(LA32_20 >= KW_MSCK && LA32_20 <= KW_OFFLINE)||(LA32_20 >= KW_OPTION && LA32_20 <= KW_OUTPUTFORMAT)||(LA32_20 >= KW_OVERWRITE && LA32_20 <= KW_OWNER)||(LA32_20 >= KW_PARTITION && LA32_20 <= KW_PLUS)||(LA32_20 >= KW_PRETTY && LA32_20 <= KW_TABLES)||(LA32_20 >= KW_TBLPROPERTIES && LA32_20 <= KW_TRANSACTIONS)||(LA32_20 >= KW_TRIGGER && LA32_20 <= KW_UNARCHIVE)||(LA32_20 >= KW_UNDO && LA32_20 <= KW_UNIONTYPE)||(LA32_20 >= KW_UNLOCK && LA32_20 <= KW_VALUE_TYPE)||(LA32_20 >= KW_VIEW && LA32_20 <= KW_WITH)||(LA32_20 >= LESSTHAN && LA32_20 <= LESSTHANOREQUALTO)||LA32_20==LSQUARE||(LA32_20 >= MINUS && LA32_20 <= NOTEQUAL)||LA32_20==PLUS||(LA32_20 >= RPAREN && LA32_20 <= RSQUARE)||LA32_20==STAR) ) {s = 89;}

                         
                        input.seek(index32_20);

                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA32_624 = input.LA(1);

                         
                        int index32_624 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (true) ) {s = 22;}

                        else if ( (synpred5_IdentifiersParser()) ) {s = 561;}

                         
                        input.seek(index32_624);

                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA32_625 = input.LA(1);

                         
                        int index32_625 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (true) ) {s = 22;}

                        else if ( (synpred5_IdentifiersParser()) ) {s = 561;}

                         
                        input.seek(index32_625);

                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA32_626 = input.LA(1);

                         
                        int index32_626 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred5_IdentifiersParser()) ) {s = 561;}

                        else if ( (true) ) {s = 89;}

                         
                        input.seek(index32_626);

                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA32_627 = input.LA(1);

                         
                        int index32_627 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred5_IdentifiersParser()) ) {s = 561;}

                        else if ( (true) ) {s = 89;}

                         
                        input.seek(index32_627);

                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA32_628 = input.LA(1);

                         
                        int index32_628 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (true) ) {s = 3;}

                        else if ( (synpred5_IdentifiersParser()) ) {s = 561;}

                         
                        input.seek(index32_628);

                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA32_629 = input.LA(1);

                         
                        int index32_629 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (true) ) {s = 3;}

                        else if ( (synpred5_IdentifiersParser()) ) {s = 561;}

                         
                        input.seek(index32_629);

                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA32_630 = input.LA(1);

                         
                        int index32_630 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (true) ) {s = 3;}

                        else if ( (synpred5_IdentifiersParser()) ) {s = 561;}

                         
                        input.seek(index32_630);

                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA32_631 = input.LA(1);

                         
                        int index32_631 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (true) ) {s = 3;}

                        else if ( (synpred5_IdentifiersParser()) ) {s = 561;}

                         
                        input.seek(index32_631);

                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA32_632 = input.LA(1);

                         
                        int index32_632 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred5_IdentifiersParser()) ) {s = 561;}

                        else if ( (true) ) {s = 89;}

                         
                        input.seek(index32_632);

                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA32_633 = input.LA(1);

                         
                        int index32_633 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred5_IdentifiersParser()) ) {s = 561;}

                        else if ( (true) ) {s = 89;}

                         
                        input.seek(index32_633);

                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA32_634 = input.LA(1);

                         
                        int index32_634 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred5_IdentifiersParser()) ) {s = 561;}

                        else if ( (true) ) {s = 89;}

                         
                        input.seek(index32_634);

                        if ( s>=0 ) return s;
                        break;
                    case 21 : 
                        int LA32_635 = input.LA(1);

                         
                        int index32_635 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred5_IdentifiersParser()) ) {s = 561;}

                        else if ( (true) ) {s = 89;}

                         
                        input.seek(index32_635);

                        if ( s>=0 ) return s;
                        break;
                    case 22 : 
                        int LA32_636 = input.LA(1);

                         
                        int index32_636 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred5_IdentifiersParser()) ) {s = 561;}

                        else if ( (true) ) {s = 89;}

                         
                        input.seek(index32_636);

                        if ( s>=0 ) return s;
                        break;
                    case 23 : 
                        int LA32_637 = input.LA(1);

                         
                        int index32_637 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred5_IdentifiersParser()) ) {s = 561;}

                        else if ( (true) ) {s = 89;}

                         
                        input.seek(index32_637);

                        if ( s>=0 ) return s;
                        break;
                    case 24 : 
                        int LA32_638 = input.LA(1);

                         
                        int index32_638 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred5_IdentifiersParser()) ) {s = 561;}

                        else if ( (true) ) {s = 89;}

                         
                        input.seek(index32_638);

                        if ( s>=0 ) return s;
                        break;
                    case 25 : 
                        int LA32_639 = input.LA(1);

                         
                        int index32_639 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred5_IdentifiersParser()) ) {s = 561;}

                        else if ( (true) ) {s = 89;}

                         
                        input.seek(index32_639);

                        if ( s>=0 ) return s;
                        break;
                    case 26 : 
                        int LA32_640 = input.LA(1);

                         
                        int index32_640 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred5_IdentifiersParser()) ) {s = 561;}

                        else if ( (true) ) {s = 89;}

                         
                        input.seek(index32_640);

                        if ( s>=0 ) return s;
                        break;
                    case 27 : 
                        int LA32_641 = input.LA(1);

                         
                        int index32_641 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred5_IdentifiersParser()) ) {s = 561;}

                        else if ( (true) ) {s = 89;}

                         
                        input.seek(index32_641);

                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}

            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 32, _s, input);
            error(nvae);
            throw nvae;
        }

    }
    static final String DFA33_eotS =
        "\100\uffff";
    static final String DFA33_eofS =
        "\1\1\77\uffff";
    static final String DFA33_minS =
        "\1\4\77\uffff";
    static final String DFA33_maxS =
        "\1\u012a\77\uffff";
    static final String DFA33_acceptS =
        "\1\uffff\1\3\74\uffff\1\1\1\2";
    static final String DFA33_specialS =
        "\100\uffff}>";
    static final String[] DFA33_transitionS = {
            "\3\1\3\uffff\1\1\3\uffff\2\1\1\uffff\1\77\2\uffff\2\1\1\uffff"+
            "\2\1\1\uffff\27\1\2\uffff\1\1\1\uffff\4\1\1\uffff\6\1\1\uffff"+
            "\4\1\1\uffff\2\1\1\uffff\20\1\1\uffff\10\1\1\uffff\4\1\1\uffff"+
            "\10\1\1\uffff\5\1\1\uffff\7\1\1\uffff\25\1\1\uffff\12\1\1\uffff"+
            "\4\1\1\uffff\10\1\1\uffff\7\1\1\uffff\2\1\1\uffff\5\1\2\uffff"+
            "\62\1\1\uffff\11\1\1\uffff\4\1\1\uffff\3\1\1\uffff\13\1\1\uffff"+
            "\6\1\1\uffff\2\1\1\uffff\1\76\1\uffff\3\1\1\uffff\1\1\3\uffff"+
            "\2\1\2\uffff\1\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA33_eot = DFA.unpackEncodedString(DFA33_eotS);
    static final short[] DFA33_eof = DFA.unpackEncodedString(DFA33_eofS);
    static final char[] DFA33_min = DFA.unpackEncodedStringToUnsignedChars(DFA33_minS);
    static final char[] DFA33_max = DFA.unpackEncodedStringToUnsignedChars(DFA33_maxS);
    static final short[] DFA33_accept = DFA.unpackEncodedString(DFA33_acceptS);
    static final short[] DFA33_special = DFA.unpackEncodedString(DFA33_specialS);
    static final short[][] DFA33_transition;

    static {
        int numStates = DFA33_transitionS.length;
        DFA33_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA33_transition[i] = DFA.unpackEncodedString(DFA33_transitionS[i]);
        }
    }

    class DFA33 extends DFA {

        public DFA33(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 33;
            this.eot = DFA33_eot;
            this.eof = DFA33_eof;
            this.min = DFA33_min;
            this.max = DFA33_max;
            this.accept = DFA33_accept;
            this.special = DFA33_special;
            this.transition = DFA33_transition;
        }
        public String getDescription() {
            return "()* loopback of 276:20: ( ( LSQUARE ^ expression RSQUARE !) | ( DOT ^ identifier ) )*";
        }
    }
    static final String DFA36_eotS =
        "\123\uffff";
    static final String DFA36_eofS =
        "\2\2\121\uffff";
    static final String DFA36_minS =
        "\1\4\1\12\121\uffff";
    static final String DFA36_maxS =
        "\1\u012a\1\u0126\121\uffff";
    static final String DFA36_acceptS =
        "\2\uffff\1\2\73\uffff\1\1\24\uffff";
    static final String DFA36_specialS =
        "\123\uffff}>";
    static final String[] DFA36_transitionS = {
            "\3\2\3\uffff\1\2\3\uffff\2\2\4\uffff\2\2\1\uffff\2\2\1\uffff"+
            "\27\2\2\uffff\1\2\1\uffff\4\2\1\uffff\6\2\1\uffff\4\2\1\uffff"+
            "\2\2\1\uffff\20\2\1\uffff\10\2\1\uffff\4\2\1\uffff\10\2\1\uffff"+
            "\5\2\1\uffff\7\2\1\uffff\15\2\1\1\7\2\1\uffff\12\2\1\uffff\4"+
            "\2\1\uffff\10\2\1\uffff\7\2\1\uffff\2\2\1\uffff\5\2\2\uffff"+
            "\62\2\1\uffff\11\2\1\uffff\4\2\1\uffff\3\2\1\uffff\13\2\1\uffff"+
            "\6\2\1\uffff\2\2\3\uffff\3\2\1\uffff\1\2\3\uffff\2\2\2\uffff"+
            "\1\2",
            "\1\2\52\uffff\1\2\44\uffff\1\2\31\uffff\1\2\4\uffff\1\2\1\uffff"+
            "\1\2\14\uffff\1\2\11\uffff\1\2\3\uffff\1\2\11\uffff\1\2\7\uffff"+
            "\1\76\1\uffff\1\76\5\uffff\1\2\33\uffff\1\2\20\uffff\1\2\12"+
            "\uffff\1\2\32\uffff\1\2\20\uffff\1\2\1\uffff\1\2\17\uffff\1"+
            "\2",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA36_eot = DFA.unpackEncodedString(DFA36_eotS);
    static final short[] DFA36_eof = DFA.unpackEncodedString(DFA36_eofS);
    static final char[] DFA36_min = DFA.unpackEncodedStringToUnsignedChars(DFA36_minS);
    static final char[] DFA36_max = DFA.unpackEncodedStringToUnsignedChars(DFA36_maxS);
    static final short[] DFA36_accept = DFA.unpackEncodedString(DFA36_acceptS);
    static final short[] DFA36_special = DFA.unpackEncodedString(DFA36_specialS);
    static final short[][] DFA36_transition;

    static {
        int numStates = DFA36_transitionS.length;
        DFA36_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA36_transition[i] = DFA.unpackEncodedString(DFA36_transitionS[i]);
        }
    }

    class DFA36 extends DFA {

        public DFA36(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 36;
            this.eot = DFA36_eot;
            this.eof = DFA36_eof;
            this.min = DFA36_min;
            this.max = DFA36_max;
            this.accept = DFA36_accept;
            this.special = DFA36_special;
            this.transition = DFA36_transition;
        }
        public String getDescription() {
            return "296:39: (a= KW_IS nullCondition )?";
        }
    }
    static final String DFA37_eotS =
        "\75\uffff";
    static final String DFA37_eofS =
        "\1\1\74\uffff";
    static final String DFA37_minS =
        "\1\4\74\uffff";
    static final String DFA37_maxS =
        "\1\u012a\74\uffff";
    static final String DFA37_acceptS =
        "\1\uffff\1\2\72\uffff\1\1";
    static final String DFA37_specialS =
        "\75\uffff}>";
    static final String[] DFA37_transitionS = {
            "\2\1\1\74\3\uffff\1\1\3\uffff\2\1\4\uffff\2\1\1\uffff\2\1\1"+
            "\uffff\27\1\2\uffff\1\1\1\uffff\4\1\1\uffff\6\1\1\uffff\4\1"+
            "\1\uffff\2\1\1\uffff\20\1\1\uffff\10\1\1\uffff\4\1\1\uffff\10"+
            "\1\1\uffff\5\1\1\uffff\7\1\1\uffff\25\1\1\uffff\12\1\1\uffff"+
            "\4\1\1\uffff\10\1\1\uffff\7\1\1\uffff\2\1\1\uffff\5\1\2\uffff"+
            "\62\1\1\uffff\11\1\1\uffff\4\1\1\uffff\3\1\1\uffff\13\1\1\uffff"+
            "\6\1\1\uffff\2\1\3\uffff\3\1\1\uffff\1\1\3\uffff\2\1\2\uffff"+
            "\1\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA37_eot = DFA.unpackEncodedString(DFA37_eotS);
    static final short[] DFA37_eof = DFA.unpackEncodedString(DFA37_eofS);
    static final char[] DFA37_min = DFA.unpackEncodedStringToUnsignedChars(DFA37_minS);
    static final char[] DFA37_max = DFA.unpackEncodedStringToUnsignedChars(DFA37_maxS);
    static final short[] DFA37_accept = DFA.unpackEncodedString(DFA37_acceptS);
    static final short[] DFA37_special = DFA.unpackEncodedString(DFA37_specialS);
    static final short[][] DFA37_transition;

    static {
        int numStates = DFA37_transitionS.length;
        DFA37_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA37_transition[i] = DFA.unpackEncodedString(DFA37_transitionS[i]);
        }
    }

    class DFA37 extends DFA {

        public DFA37(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 37;
            this.eot = DFA37_eot;
            this.eof = DFA37_eof;
            this.min = DFA37_min;
            this.max = DFA37_max;
            this.accept = DFA37_accept;
            this.special = DFA37_special;
            this.transition = DFA37_transition;
        }
        public String getDescription() {
            return "()* loopback of 309:37: ( precedenceBitwiseXorOperator ^ precedenceUnarySuffixExpression )*";
        }
    }
    static final String DFA38_eotS =
        "\74\uffff";
    static final String DFA38_eofS =
        "\1\1\73\uffff";
    static final String DFA38_minS =
        "\1\4\73\uffff";
    static final String DFA38_maxS =
        "\1\u012a\73\uffff";
    static final String DFA38_acceptS =
        "\1\uffff\1\2\71\uffff\1\1";
    static final String DFA38_specialS =
        "\74\uffff}>";
    static final String[] DFA38_transitionS = {
            "\2\1\4\uffff\1\1\3\uffff\2\73\4\uffff\2\1\1\uffff\2\1\1\uffff"+
            "\27\1\2\uffff\1\1\1\uffff\4\1\1\uffff\6\1\1\uffff\4\1\1\uffff"+
            "\2\1\1\uffff\20\1\1\uffff\10\1\1\uffff\4\1\1\uffff\10\1\1\uffff"+
            "\5\1\1\uffff\7\1\1\uffff\25\1\1\uffff\12\1\1\uffff\4\1\1\uffff"+
            "\10\1\1\uffff\7\1\1\uffff\2\1\1\uffff\5\1\2\uffff\62\1\1\uffff"+
            "\11\1\1\uffff\4\1\1\uffff\3\1\1\uffff\13\1\1\uffff\6\1\1\uffff"+
            "\2\1\3\uffff\1\1\1\73\1\1\1\uffff\1\1\3\uffff\2\1\2\uffff\1"+
            "\73",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA38_eot = DFA.unpackEncodedString(DFA38_eotS);
    static final short[] DFA38_eof = DFA.unpackEncodedString(DFA38_eofS);
    static final char[] DFA38_min = DFA.unpackEncodedStringToUnsignedChars(DFA38_minS);
    static final char[] DFA38_max = DFA.unpackEncodedStringToUnsignedChars(DFA38_maxS);
    static final short[] DFA38_accept = DFA.unpackEncodedString(DFA38_acceptS);
    static final short[] DFA38_special = DFA.unpackEncodedString(DFA38_specialS);
    static final short[][] DFA38_transition;

    static {
        int numStates = DFA38_transitionS.length;
        DFA38_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA38_transition[i] = DFA.unpackEncodedString(DFA38_transitionS[i]);
        }
    }

    class DFA38 extends DFA {

        public DFA38(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 38;
            this.eot = DFA38_eot;
            this.eof = DFA38_eof;
            this.min = DFA38_min;
            this.max = DFA38_max;
            this.accept = DFA38_accept;
            this.special = DFA38_special;
            this.transition = DFA38_transition;
        }
        public String getDescription() {
            return "()* loopback of 320:36: ( precedenceStarOperator ^ precedenceBitwiseXorExpression )*";
        }
    }
    static final String DFA44_eotS =
        "\165\uffff";
    static final String DFA44_eofS =
        "\25\uffff\1\1\137\uffff";
    static final String DFA44_minS =
        "\1\7\24\uffff\1\4\3\uffff\1\7\121\uffff\1\0\11\uffff";
    static final String DFA44_maxS =
        "\1\u012e\24\uffff\1\u012a\3\uffff\1\u012e\121\uffff\1\0\11\uffff";
    static final String DFA44_acceptS =
        "\1\uffff\1\1\161\uffff\2\2";
    static final String DFA44_specialS =
        "\31\uffff\1\0\121\uffff\1\1\11\uffff}>";
    static final String[] DFA44_transitionS = {
            "\1\1\5\uffff\1\1\4\uffff\1\1\7\uffff\7\1\1\uffff\22\1\1\uffff"+
            "\4\1\1\uffff\6\1\1\uffff\2\1\1\uffff\1\1\1\uffff\2\1\1\uffff"+
            "\20\1\1\uffff\4\1\1\uffff\1\1\1\uffff\1\1\1\uffff\1\1\1\25\2"+
            "\1\1\uffff\10\1\1\uffff\3\1\1\uffff\1\1\1\uffff\4\1\1\uffff"+
            "\23\1\1\uffff\4\1\1\uffff\12\1\1\uffff\4\1\1\uffff\3\1\1\uffff"+
            "\4\1\1\uffff\1\1\1\uffff\5\1\1\uffff\2\1\1\uffff\5\1\2\uffff"+
            "\14\1\1\uffff\20\1\1\uffff\24\1\1\uffff\3\1\1\uffff\5\1\1\uffff"+
            "\4\1\1\uffff\3\1\1\uffff\13\1\1\uffff\1\1\2\uffff\1\1\1\uffff"+
            "\1\1\3\uffff\1\1\2\uffff\1\1\2\uffff\2\1\10\uffff\4\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\3\1\3\uffff\1\1\3\uffff\2\1\1\uffff\1\1\2\uffff\2\1\1\uffff"+
            "\2\1\1\uffff\27\1\2\uffff\1\1\1\uffff\4\1\1\uffff\6\1\1\uffff"+
            "\4\1\1\uffff\2\1\1\uffff\20\1\1\uffff\10\1\1\uffff\4\1\1\uffff"+
            "\10\1\1\uffff\5\1\1\uffff\7\1\1\uffff\25\1\1\uffff\12\1\1\uffff"+
            "\4\1\1\uffff\10\1\1\uffff\7\1\1\uffff\2\1\1\uffff\5\1\2\uffff"+
            "\62\1\1\uffff\11\1\1\uffff\4\1\1\uffff\3\1\1\uffff\13\1\1\uffff"+
            "\6\1\1\uffff\2\1\1\31\1\1\1\uffff\3\1\1\uffff\1\1\3\uffff\2"+
            "\1\2\uffff\1\1",
            "",
            "",
            "",
            "\1\1\5\uffff\1\1\4\uffff\1\1\7\uffff\7\1\1\uffff\22\1\1\uffff"+
            "\4\1\1\uffff\6\1\1\uffff\2\1\1\uffff\1\1\1\uffff\2\1\1\uffff"+
            "\25\1\1\uffff\1\1\1\uffff\1\1\1\uffff\4\1\1\uffff\10\1\1\uffff"+
            "\3\1\1\uffff\1\1\1\uffff\4\1\1\uffff\23\1\1\uffff\4\1\1\uffff"+
            "\12\1\1\uffff\1\153\3\1\1\uffff\10\1\1\uffff\1\1\1\uffff\5\1"+
            "\1\uffff\2\1\1\uffff\5\1\2\uffff\14\1\1\164\20\1\1\163\24\1"+
            "\1\uffff\3\1\1\uffff\5\1\1\uffff\4\1\1\uffff\3\1\1\uffff\13"+
            "\1\1\uffff\1\1\2\uffff\1\1\1\uffff\1\1\3\uffff\1\1\2\uffff\1"+
            "\1\2\uffff\2\1\3\uffff\1\1\3\uffff\5\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA44_eot = DFA.unpackEncodedString(DFA44_eotS);
    static final short[] DFA44_eof = DFA.unpackEncodedString(DFA44_eofS);
    static final char[] DFA44_min = DFA.unpackEncodedStringToUnsignedChars(DFA44_minS);
    static final char[] DFA44_max = DFA.unpackEncodedStringToUnsignedChars(DFA44_maxS);
    static final short[] DFA44_accept = DFA.unpackEncodedString(DFA44_acceptS);
    static final short[] DFA44_special = DFA.unpackEncodedString(DFA44_specialS);
    static final short[][] DFA44_transition;

    static {
        int numStates = DFA44_transitionS.length;
        DFA44_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA44_transition[i] = DFA.unpackEncodedString(DFA44_transitionS[i]);
        }
    }

    class DFA44 extends DFA {

        public DFA44(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 44;
            this.eot = DFA44_eot;
            this.eof = DFA44_eof;
            this.min = DFA44_min;
            this.max = DFA44_max;
            this.accept = DFA44_accept;
            this.special = DFA44_special;
            this.transition = DFA44_transition;
        }
        public String getDescription() {
            return "373:1: precedenceEqualExpression : ( (left= precedenceBitwiseOrExpression -> $left) ( ( KW_NOT precedenceEqualNegatableOperator notExpr= precedenceBitwiseOrExpression ) -> ^( KW_NOT ^( precedenceEqualNegatableOperator $precedenceEqualExpression $notExpr) ) | ( precedenceEqualOperator equalExpr= precedenceBitwiseOrExpression ) -> ^( precedenceEqualOperator $precedenceEqualExpression $equalExpr) | ( KW_NOT KW_IN LPAREN KW_SELECT )=> ( KW_NOT KW_IN subQueryExpression ) -> ^( KW_NOT ^( TOK_SUBQUERY_EXPR ^( TOK_SUBQUERY_OP KW_IN ) subQueryExpression $precedenceEqualExpression) ) | ( KW_NOT KW_IN expressions ) -> ^( KW_NOT ^( TOK_FUNCTION KW_IN $precedenceEqualExpression expressions ) ) | ( KW_IN LPAREN KW_SELECT )=> ( KW_IN subQueryExpression ) -> ^( TOK_SUBQUERY_EXPR ^( TOK_SUBQUERY_OP KW_IN ) subQueryExpression $precedenceEqualExpression) | ( KW_IN expressions ) -> ^( TOK_FUNCTION KW_IN $precedenceEqualExpression expressions ) | ( KW_NOT KW_BETWEEN (min= precedenceBitwiseOrExpression ) KW_AND (max= precedenceBitwiseOrExpression ) ) -> ^( TOK_FUNCTION Identifier[\"between\"] KW_TRUE $left $min $max) | ( KW_BETWEEN (min= precedenceBitwiseOrExpression ) KW_AND (max= precedenceBitwiseOrExpression ) ) -> ^( TOK_FUNCTION Identifier[\"between\"] KW_FALSE $left $min $max) )* | ( KW_EXISTS LPAREN KW_SELECT )=> ( KW_EXISTS subQueryExpression ) -> ^( TOK_SUBQUERY_EXPR ^( TOK_SUBQUERY_OP KW_EXISTS ) subQueryExpression ) );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA44_25 = input.LA(1);

                         
                        int index44_25 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (LA44_25==BigintLiteral||LA44_25==CharSetName||LA44_25==DecimalLiteral||(LA44_25 >= Identifier && LA44_25 <= KW_ANALYZE)||(LA44_25 >= KW_ARCHIVE && LA44_25 <= KW_CHANGE)||(LA44_25 >= KW_CLUSTER && LA44_25 <= KW_COLLECTION)||(LA44_25 >= KW_COLUMNS && LA44_25 <= KW_CONCATENATE)||(LA44_25 >= KW_CONTINUE && LA44_25 <= KW_CREATE)||LA44_25==KW_CUBE||(LA44_25 >= KW_CURSOR && LA44_25 <= KW_DATA)||(LA44_25 >= KW_DATABASES && LA44_25 <= KW_ELEM_TYPE)||LA44_25==KW_ENABLE||LA44_25==KW_ESCAPED||(LA44_25 >= KW_EXCLUSIVE && LA44_25 <= KW_EXPORT)||(LA44_25 >= KW_EXTERNAL && LA44_25 <= KW_FLOAT)||(LA44_25 >= KW_FOR && LA44_25 <= KW_FORMATTED)||LA44_25==KW_FULL||(LA44_25 >= KW_FUNCTIONS && LA44_25 <= KW_GROUPING)||(LA44_25 >= KW_HOLD_DDLTIME && LA44_25 <= KW_JAR)||(LA44_25 >= KW_KEYS && LA44_25 <= KW_LEFT)||(LA44_25 >= KW_LIKE && LA44_25 <= KW_LONG)||(LA44_25 >= KW_MAPJOIN && LA44_25 <= KW_MINUS)||(LA44_25 >= KW_MSCK && LA44_25 <= KW_OFFLINE)||LA44_25==KW_OPTION||(LA44_25 >= KW_ORDER && LA44_25 <= KW_OUTPUTFORMAT)||(LA44_25 >= KW_OVERWRITE && LA44_25 <= KW_OWNER)||(LA44_25 >= KW_PARTITION && LA44_25 <= KW_PLUS)||(LA44_25 >= KW_PRETTY && LA44_25 <= KW_RECORDWRITER)||(LA44_25 >= KW_REGEXP && LA44_25 <= KW_SCHEMAS)||(LA44_25 >= KW_SEMI && LA44_25 <= KW_TABLES)||(LA44_25 >= KW_TBLPROPERTIES && LA44_25 <= KW_TERMINATED)||(LA44_25 >= KW_TIMESTAMP && LA44_25 <= KW_TRANSACTIONS)||(LA44_25 >= KW_TRIGGER && LA44_25 <= KW_UNARCHIVE)||(LA44_25 >= KW_UNDO && LA44_25 <= KW_UNIONTYPE)||(LA44_25 >= KW_UNLOCK && LA44_25 <= KW_VALUE_TYPE)||LA44_25==KW_VIEW||LA44_25==KW_WHILE||LA44_25==KW_WITH||LA44_25==LPAREN||LA44_25==MINUS||(LA44_25 >= Number && LA44_25 <= PLUS)||LA44_25==RPAREN||(LA44_25 >= STAR && LA44_25 <= TinyintLiteral)) ) {s = 1;}

                        else if ( (LA44_25==KW_MAP) ) {s = 107;}

                        else if ( (LA44_25==KW_SELECT) && (synpred8_IdentifiersParser())) {s = 115;}

                        else if ( (LA44_25==KW_REDUCE) && (synpred8_IdentifiersParser())) {s = 116;}

                         
                        input.seek(index44_25);

                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA44_107 = input.LA(1);

                         
                        int index44_107 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (true) ) {s = 1;}

                        else if ( (synpred8_IdentifiersParser()) ) {s = 116;}

                         
                        input.seek(index44_107);

                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}

            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 44, _s, input);
            error(nvae);
            throw nvae;
        }

    }
    static final String DFA43_eotS =
        "\u03a5\uffff";
    static final String DFA43_eofS =
        "\1\1\53\uffff\1\1\1\uffff\1\1\7\uffff\1\1\5\uffff\1\57\1\uffff\4"+
        "\57\1\uffff\2\57\1\uffff\2\57\u035d\uffff";
    static final String DFA43_minS =
        "\1\12\53\uffff\1\7\1\50\1\12\7\uffff\1\7\5\uffff\1\4\1\uffff\4\4"+
        "\1\uffff\2\4\1\uffff\2\4\1\uffff\1\7\26\uffff\1\u011b\25\uffff\1"+
        "\7\4\uffff\1\4\1\uffff\4\4\1\uffff\2\4\1\uffff\2\4\1\uffff\1\7\u0279"+
        "\uffff\1\0\1\uffff\1\0\u00a0\uffff";
    static final String DFA43_maxS =
        "\1\u0127\53\uffff\1\u012e\1\u00d5\1\u0126\7\uffff\1\u012e\5\uffff"+
        "\1\u012a\1\uffff\4\u012a\1\uffff\2\u012a\1\uffff\2\u012a\1\uffff"+
        "\1\u012e\26\uffff\1\u011b\25\uffff\1\u012e\4\uffff\1\u012a\1\uffff"+
        "\4\u012a\1\uffff\2\u012a\1\uffff\2\u012a\1\uffff\1\u012e\u0279\uffff"+
        "\1\0\1\uffff\1\0\u00a0\uffff";
    static final String DFA43_acceptS =
        "\1\uffff\1\11\55\uffff\1\2\61\uffff\1\7\1\1\47\uffff\1\10\24\uffff"+
        "\1\2\100\uffff\1\2\100\uffff\1\2\100\uffff\1\2\100\uffff\1\2\u0081"+
        "\uffff\1\2\100\uffff\1\2\100\uffff\2\2\126\uffff\1\2\3\uffff\1\5"+
        "\1\uffff\1\5\1\6\u0099\uffff\1\10\2\uffff\1\3\1\4";
    static final String DFA43_specialS =
        "\166\uffff\1\0\u028b\uffff\1\1\1\uffff\1\2\u00a0\uffff}>";
    static final String[] DFA43_transitionS = {
            "\1\1\11\uffff\2\57\1\uffff\2\57\1\uffff\16\1\1\66\10\1\2\uffff"+
            "\1\1\1\uffff\4\1\1\uffff\6\1\1\uffff\4\1\1\uffff\2\1\1\uffff"+
            "\20\1\1\uffff\10\1\1\uffff\4\1\1\uffff\10\1\1\uffff\5\1\1\uffff"+
            "\7\1\1\uffff\2\1\1\56\22\1\1\uffff\1\54\11\1\1\uffff\4\1\1\uffff"+
            "\3\1\1\55\4\1\1\uffff\7\1\1\uffff\2\1\1\uffff\5\1\2\uffff\15"+
            "\1\1\54\7\1\1\54\34\1\1\uffff\11\1\1\uffff\4\1\1\uffff\3\1\1"+
            "\uffff\13\1\1\uffff\6\1\1\uffff\2\57\5\uffff\1\57\5\uffff\2"+
            "\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\57\2\uffff\1\1\2\uffff\1\57\4\uffff\1\57\7\uffff\7\57\1"+
            "\uffff\22\57\1\uffff\1\77\3\57\1\uffff\6\57\1\uffff\2\57\1\uffff"+
            "\1\57\1\uffff\2\57\1\uffff\20\57\1\uffff\1\100\3\57\1\uffff"+
            "\1\57\1\uffff\1\57\1\uffff\4\57\1\uffff\10\57\1\uffff\3\57\1"+
            "\1\1\57\1\uffff\2\57\1\74\1\57\1\1\14\57\1\107\6\57\1\uffff"+
            "\2\57\1\106\1\57\1\uffff\1\57\1\103\10\57\1\uffff\1\111\3\57"+
            "\1\uffff\3\57\1\uffff\4\57\1\uffff\1\57\1\uffff\1\76\4\57\1"+
            "\uffff\2\57\1\uffff\5\57\2\uffff\14\57\1\1\20\57\1\1\12\57\1"+
            "\101\11\57\1\uffff\3\57\1\uffff\5\57\1\uffff\4\57\1\uffff\1"+
            "\57\1\104\1\57\1\uffff\13\57\1\uffff\1\57\1\uffff\1\1\1\57\1"+
            "\1\1\57\3\uffff\1\57\2\uffff\1\57\2\uffff\2\57\3\uffff\1\1\4"+
            "\uffff\4\57",
            "\1\141\130\uffff\1\140\23\uffff\1\142\67\uffff\1\142\7\uffff"+
            "\1\142",
            "\1\1\52\uffff\1\1\44\uffff\1\1\31\uffff\1\1\4\uffff\1\1\1\uffff"+
            "\1\1\14\uffff\1\1\11\uffff\1\1\3\uffff\1\1\11\uffff\1\1\17\uffff"+
            "\1\1\33\uffff\1\1\20\uffff\1\1\12\uffff\1\1\32\uffff\1\1\20"+
            "\uffff\1\1\1\uffff\1\1\4\uffff\1\166\12\uffff\1\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\u008a\2\uffff\1\1\2\uffff\1\u008a\4\uffff\1\u008a\7\uffff"+
            "\7\u008a\1\uffff\22\u008a\1\uffff\1\176\3\u008a\1\uffff\6\u008a"+
            "\1\uffff\2\u008a\1\uffff\1\u008a\1\uffff\2\u008a\1\uffff\20"+
            "\u008a\1\uffff\1\177\3\u008a\1\uffff\1\u008a\1\uffff\1\u008a"+
            "\1\uffff\4\u008a\1\uffff\10\u008a\1\uffff\3\u008a\1\1\1\u008a"+
            "\1\uffff\2\u008a\1\173\1\u008a\1\1\14\u008a\1\u0086\6\u008a"+
            "\1\uffff\2\u008a\1\u0085\1\u008a\1\uffff\1\u008a\1\u0082\10"+
            "\u008a\1\uffff\1\u0088\3\u008a\1\uffff\3\u008a\1\uffff\4\u008a"+
            "\1\uffff\1\u008a\1\uffff\1\175\4\u008a\1\uffff\2\u008a\1\uffff"+
            "\5\u008a\2\uffff\14\u008a\1\1\20\u008a\1\1\12\u008a\1\u0080"+
            "\11\u008a\1\uffff\3\u008a\1\uffff\5\u008a\1\uffff\4\u008a\1"+
            "\uffff\1\u008a\1\u0083\1\u008a\1\uffff\13\u008a\1\uffff\1\u008a"+
            "\1\uffff\1\1\1\u008a\1\1\1\u008a\3\uffff\1\u008a\2\uffff\1\u008a"+
            "\2\uffff\2\u008a\3\uffff\1\1\4\uffff\4\u008a",
            "",
            "",
            "",
            "",
            "",
            "\3\57\3\uffff\1\57\3\uffff\2\57\1\uffff\1\57\2\uffff\2\57\1"+
            "\uffff\2\57\1\uffff\25\57\1\u009f\1\57\2\uffff\1\57\1\uffff"+
            "\4\57\1\uffff\6\57\1\uffff\4\57\1\uffff\2\57\1\uffff\20\57\1"+
            "\uffff\10\57\1\uffff\4\57\1\uffff\10\57\1\uffff\5\57\1\uffff"+
            "\7\57\1\uffff\25\57\1\uffff\12\57\1\uffff\4\57\1\uffff\10\57"+
            "\1\uffff\7\57\1\uffff\2\57\1\uffff\5\57\2\uffff\62\57\1\uffff"+
            "\11\57\1\uffff\4\57\1\uffff\3\57\1\uffff\13\57\1\uffff\6\57"+
            "\1\uffff\4\57\1\uffff\3\57\1\uffff\1\57\3\uffff\2\57\2\uffff"+
            "\1\57",
            "",
            "\3\57\3\uffff\1\57\3\uffff\2\57\1\uffff\1\57\2\uffff\2\57\1"+
            "\uffff\2\57\1\uffff\25\57\1\u00e0\1\57\2\uffff\1\57\1\uffff"+
            "\4\57\1\uffff\6\57\1\uffff\4\57\1\uffff\2\57\1\uffff\20\57\1"+
            "\uffff\10\57\1\uffff\4\57\1\uffff\10\57\1\uffff\5\57\1\uffff"+
            "\7\57\1\uffff\25\57\1\uffff\12\57\1\uffff\4\57\1\uffff\10\57"+
            "\1\uffff\7\57\1\uffff\2\57\1\uffff\5\57\2\uffff\62\57\1\uffff"+
            "\11\57\1\uffff\4\57\1\uffff\3\57\1\uffff\13\57\1\uffff\6\57"+
            "\1\uffff\4\57\1\uffff\3\57\1\uffff\1\57\3\uffff\2\57\2\uffff"+
            "\1\57",
            "\3\57\3\uffff\1\57\3\uffff\2\57\1\uffff\1\57\2\uffff\2\57\1"+
            "\uffff\2\57\1\uffff\25\57\1\u0121\1\57\2\uffff\1\57\1\uffff"+
            "\4\57\1\uffff\6\57\1\uffff\4\57\1\uffff\2\57\1\uffff\20\57\1"+
            "\uffff\10\57\1\uffff\4\57\1\uffff\10\57\1\uffff\5\57\1\uffff"+
            "\7\57\1\uffff\25\57\1\uffff\12\57\1\uffff\4\57\1\uffff\10\57"+
            "\1\uffff\7\57\1\uffff\2\57\1\uffff\5\57\2\uffff\62\57\1\uffff"+
            "\11\57\1\uffff\4\57\1\uffff\3\57\1\uffff\13\57\1\uffff\6\57"+
            "\1\uffff\4\57\1\uffff\3\57\1\uffff\1\57\3\uffff\2\57\2\uffff"+
            "\1\57",
            "\3\57\3\uffff\1\57\3\uffff\2\57\1\uffff\1\57\2\uffff\2\57\1"+
            "\uffff\2\57\1\uffff\25\57\1\u0162\1\57\2\uffff\1\57\1\uffff"+
            "\4\57\1\uffff\6\57\1\uffff\4\57\1\uffff\2\57\1\uffff\20\57\1"+
            "\uffff\10\57\1\uffff\4\57\1\uffff\10\57\1\uffff\5\57\1\uffff"+
            "\7\57\1\uffff\25\57\1\uffff\12\57\1\uffff\4\57\1\uffff\10\57"+
            "\1\uffff\7\57\1\uffff\2\57\1\uffff\5\57\2\uffff\62\57\1\uffff"+
            "\11\57\1\uffff\4\57\1\uffff\3\57\1\uffff\13\57\1\uffff\6\57"+
            "\1\uffff\4\57\1\uffff\3\57\1\uffff\1\57\3\uffff\2\57\2\uffff"+
            "\1\57",
            "\3\57\3\uffff\1\57\3\uffff\2\57\1\uffff\1\57\2\uffff\2\57\1"+
            "\uffff\2\57\1\uffff\25\57\1\u01a3\1\57\2\uffff\1\57\1\uffff"+
            "\4\57\1\uffff\6\57\1\uffff\4\57\1\uffff\2\57\1\uffff\20\57\1"+
            "\uffff\10\57\1\uffff\4\57\1\uffff\10\57\1\uffff\5\57\1\uffff"+
            "\7\57\1\uffff\25\57\1\uffff\12\57\1\uffff\4\57\1\uffff\10\57"+
            "\1\uffff\7\57\1\uffff\2\57\1\uffff\5\57\2\uffff\62\57\1\uffff"+
            "\11\57\1\uffff\4\57\1\uffff\3\57\1\uffff\13\57\1\uffff\6\57"+
            "\1\uffff\4\57\1\uffff\3\57\1\uffff\1\57\3\uffff\2\57\2\uffff"+
            "\1\57",
            "",
            "\3\57\3\uffff\1\57\3\uffff\2\57\1\uffff\1\57\2\uffff\2\57\1"+
            "\uffff\2\57\1\uffff\27\57\2\uffff\1\57\1\uffff\4\57\1\uffff"+
            "\6\57\1\uffff\4\57\1\uffff\2\57\1\uffff\20\57\1\uffff\10\57"+
            "\1\uffff\4\57\1\uffff\10\57\1\uffff\5\57\1\uffff\7\57\1\uffff"+
            "\25\57\1\uffff\12\57\1\uffff\4\57\1\uffff\10\57\1\uffff\7\57"+
            "\1\uffff\2\57\1\uffff\5\57\2\uffff\62\57\1\uffff\11\57\1\uffff"+
            "\4\57\1\uffff\3\57\1\uffff\13\57\1\uffff\6\57\1\uffff\4\57\1"+
            "\uffff\3\57\1\1\1\57\3\uffff\2\57\2\uffff\1\57",
            "\3\57\3\uffff\1\57\3\uffff\2\57\1\uffff\1\57\2\uffff\2\57\1"+
            "\uffff\2\57\1\uffff\4\57\1\u0225\22\57\2\uffff\1\57\1\uffff"+
            "\4\57\1\uffff\6\57\1\uffff\4\57\1\uffff\2\57\1\uffff\20\57\1"+
            "\uffff\10\57\1\uffff\4\57\1\uffff\10\57\1\uffff\5\57\1\uffff"+
            "\7\57\1\uffff\25\57\1\uffff\12\57\1\uffff\4\57\1\uffff\10\57"+
            "\1\uffff\7\57\1\uffff\2\57\1\uffff\5\57\2\uffff\62\57\1\uffff"+
            "\11\57\1\uffff\4\57\1\uffff\3\57\1\uffff\13\57\1\uffff\6\57"+
            "\1\uffff\4\57\1\uffff\3\57\1\uffff\1\57\3\uffff\2\57\2\uffff"+
            "\1\57",
            "",
            "\3\57\3\uffff\1\57\3\uffff\2\57\1\uffff\1\57\2\uffff\2\57\1"+
            "\uffff\2\57\1\uffff\27\57\2\uffff\1\57\1\uffff\4\57\1\uffff"+
            "\6\57\1\uffff\4\57\1\uffff\2\57\1\uffff\20\57\1\uffff\10\57"+
            "\1\uffff\4\57\1\uffff\10\57\1\uffff\5\57\1\uffff\7\57\1\uffff"+
            "\25\57\1\uffff\12\57\1\uffff\4\57\1\uffff\10\57\1\uffff\7\57"+
            "\1\uffff\2\57\1\uffff\5\57\2\uffff\62\57\1\uffff\11\57\1\uffff"+
            "\4\57\1\uffff\3\57\1\uffff\13\57\1\uffff\1\u0266\5\57\1\uffff"+
            "\4\57\1\uffff\3\57\1\uffff\1\57\3\uffff\2\57\2\uffff\1\57",
            "\3\57\3\uffff\1\57\3\uffff\2\57\1\uffff\1\57\2\uffff\2\57\1"+
            "\uffff\2\57\1\uffff\27\57\2\uffff\1\57\1\uffff\4\57\1\uffff"+
            "\6\57\1\uffff\4\57\1\uffff\2\57\1\uffff\20\57\1\uffff\10\57"+
            "\1\uffff\4\57\1\uffff\10\57\1\uffff\5\57\1\uffff\7\57\1\uffff"+
            "\14\57\1\u02a8\10\57\1\uffff\12\57\1\uffff\4\57\1\uffff\10\57"+
            "\1\uffff\7\57\1\uffff\1\u02a7\1\57\1\uffff\5\57\2\uffff\62\57"+
            "\1\uffff\11\57\1\uffff\4\57\1\uffff\3\57\1\uffff\13\57\1\uffff"+
            "\6\57\1\uffff\4\57\1\uffff\3\57\1\uffff\1\57\3\uffff\2\57\2"+
            "\uffff\1\57",
            "",
            "\1\1\5\uffff\1\1\4\uffff\1\1\7\uffff\7\1\1\uffff\22\1\1\uffff"+
            "\4\1\1\uffff\6\1\1\uffff\2\1\1\uffff\1\1\1\uffff\2\1\1\uffff"+
            "\20\1\1\uffff\4\1\1\uffff\1\1\1\uffff\1\1\1\uffff\4\1\1\uffff"+
            "\10\1\1\uffff\3\1\1\uffff\1\1\1\uffff\4\1\1\uffff\23\1\1\uffff"+
            "\4\1\1\uffff\12\1\1\uffff\4\1\1\uffff\10\1\1\uffff\1\1\1\uffff"+
            "\5\1\1\uffff\2\1\1\uffff\5\1\2\uffff\14\1\1\uffff\20\1\1\uffff"+
            "\24\1\1\uffff\3\1\1\uffff\5\1\1\uffff\4\1\1\uffff\3\1\1\uffff"+
            "\13\1\1\uffff\1\1\2\uffff\1\1\1\uffff\1\1\3\uffff\1\u02ff\2"+
            "\uffff\1\1\2\uffff\2\1\7\uffff\5\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\u0302",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\u0306\5\uffff\1\u0306\4\uffff\1\u0306\7\uffff\7\u0306\1"+
            "\uffff\22\u0306\1\uffff\4\u0306\1\uffff\6\u0306\1\uffff\2\u0306"+
            "\1\uffff\1\u0306\1\uffff\2\u0306\1\uffff\20\u0306\1\uffff\4"+
            "\u0306\1\uffff\1\u0306\1\uffff\1\u0306\1\uffff\4\u0306\1\uffff"+
            "\10\u0306\1\uffff\3\u0306\1\uffff\1\u0306\1\uffff\4\u0306\1"+
            "\uffff\23\u0306\1\uffff\4\u0306\1\uffff\12\u0306\1\uffff\1\u0304"+
            "\3\u0306\1\uffff\10\u0306\1\uffff\1\u0306\1\uffff\5\u0306\1"+
            "\uffff\2\u0306\1\uffff\5\u0306\2\uffff\14\u0306\1\u0305\20\u0306"+
            "\1\u0303\24\u0306\1\uffff\3\u0306\1\uffff\5\u0306\1\uffff\4"+
            "\u0306\1\uffff\3\u0306\1\uffff\13\u0306\1\uffff\1\u0306\2\uffff"+
            "\1\u0306\1\uffff\1\u0306\3\uffff\1\u0306\2\uffff\1\u0306\2\uffff"+
            "\2\u0306\10\uffff\4\u0306",
            "",
            "",
            "",
            "",
            "\3\u008a\7\uffff\2\u008a\1\uffff\1\u008a\17\uffff\1\u008a\15"+
            "\uffff\1\1\134\uffff\1\u008a\u008e\uffff\2\u008a\1\uffff\2\u008a"+
            "\2\uffff\1\u008a\7\uffff\1\u008a",
            "",
            "\3\u008a\7\uffff\2\u008a\1\uffff\1\u008a\17\uffff\1\u008a\15"+
            "\uffff\1\1\134\uffff\1\u008a\u008e\uffff\2\u008a\1\uffff\2\u008a"+
            "\2\uffff\1\u008a\7\uffff\1\u008a",
            "\3\u008a\7\uffff\2\u008a\1\uffff\1\u008a\17\uffff\1\u008a\15"+
            "\uffff\1\1\134\uffff\1\u008a\u008e\uffff\2\u008a\1\uffff\2\u008a"+
            "\2\uffff\1\u008a\7\uffff\1\u008a",
            "\3\u008a\7\uffff\2\u008a\1\uffff\1\u008a\17\uffff\1\u008a\15"+
            "\uffff\1\1\134\uffff\1\u008a\u008e\uffff\2\u008a\1\uffff\2\u008a"+
            "\2\uffff\1\u008a\7\uffff\1\u008a",
            "\3\u008a\7\uffff\2\u008a\1\uffff\1\u008a\17\uffff\1\u008a\15"+
            "\uffff\1\1\134\uffff\1\u008a\u008e\uffff\2\u008a\1\uffff\2\u008a"+
            "\2\uffff\1\u008a\7\uffff\1\u008a",
            "",
            "\3\u008a\7\uffff\2\u008a\1\uffff\1\u008a\17\uffff\1\u008a\152"+
            "\uffff\1\u008a\u008e\uffff\2\u008a\1\uffff\2\u008a\1\uffff\1"+
            "\1\1\u008a\7\uffff\1\u008a",
            "\3\u008a\7\uffff\2\u008a\1\uffff\1\u008a\14\uffff\1\1\2\uffff"+
            "\1\u008a\152\uffff\1\u008a\u008e\uffff\2\u008a\1\uffff\2\u008a"+
            "\2\uffff\1\u008a\7\uffff\1\u008a",
            "",
            "\3\u008a\7\uffff\2\u008a\1\uffff\1\u008a\17\uffff\1\u008a\152"+
            "\uffff\1\u008a\u0085\uffff\1\1\10\uffff\2\u008a\1\uffff\2\u008a"+
            "\2\uffff\1\u008a\7\uffff\1\u008a",
            "\3\u008a\7\uffff\2\u008a\1\uffff\1\u008a\17\uffff\1\u008a\151"+
            "\uffff\1\1\1\u008a\51\uffff\1\1\144\uffff\2\u008a\1\uffff\2"+
            "\u008a\2\uffff\1\u008a\7\uffff\1\u008a",
            "",
            "\1\1\5\uffff\1\1\4\uffff\1\1\7\uffff\7\1\1\uffff\22\1\1\uffff"+
            "\4\1\1\uffff\6\1\1\uffff\2\1\1\uffff\1\1\1\uffff\2\1\1\uffff"+
            "\20\1\1\uffff\4\1\1\uffff\1\1\1\uffff\1\1\1\uffff\4\1\1\uffff"+
            "\10\1\1\uffff\3\1\1\uffff\1\1\1\uffff\4\1\1\uffff\23\1\1\uffff"+
            "\4\1\1\uffff\12\1\1\uffff\4\1\1\uffff\10\1\1\uffff\1\1\1\uffff"+
            "\5\1\1\uffff\2\1\1\uffff\5\1\2\uffff\14\1\1\uffff\20\1\1\uffff"+
            "\24\1\1\uffff\3\1\1\uffff\5\1\1\uffff\4\1\1\uffff\3\1\1\uffff"+
            "\13\1\1\uffff\1\1\2\uffff\1\1\1\uffff\1\1\3\uffff\1\u03a0\2"+
            "\uffff\1\1\2\uffff\2\1\7\uffff\5\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA43_eot = DFA.unpackEncodedString(DFA43_eotS);
    static final short[] DFA43_eof = DFA.unpackEncodedString(DFA43_eofS);
    static final char[] DFA43_min = DFA.unpackEncodedStringToUnsignedChars(DFA43_minS);
    static final char[] DFA43_max = DFA.unpackEncodedStringToUnsignedChars(DFA43_maxS);
    static final short[] DFA43_accept = DFA.unpackEncodedString(DFA43_acceptS);
    static final short[] DFA43_special = DFA.unpackEncodedString(DFA43_specialS);
    static final short[][] DFA43_transition;

    static {
        int numStates = DFA43_transitionS.length;
        DFA43_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA43_transition[i] = DFA.unpackEncodedString(DFA43_transitionS[i]);
        }
    }

    class DFA43 extends DFA {

        public DFA43(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 43;
            this.eot = DFA43_eot;
            this.eof = DFA43_eof;
            this.min = DFA43_min;
            this.max = DFA43_max;
            this.accept = DFA43_accept;
            this.special = DFA43_special;
            this.transition = DFA43_transition;
        }
        public String getDescription() {
            return "()* loopback of 376:5: ( ( KW_NOT precedenceEqualNegatableOperator notExpr= precedenceBitwiseOrExpression ) -> ^( KW_NOT ^( precedenceEqualNegatableOperator $precedenceEqualExpression $notExpr) ) | ( precedenceEqualOperator equalExpr= precedenceBitwiseOrExpression ) -> ^( precedenceEqualOperator $precedenceEqualExpression $equalExpr) | ( KW_NOT KW_IN LPAREN KW_SELECT )=> ( KW_NOT KW_IN subQueryExpression ) -> ^( KW_NOT ^( TOK_SUBQUERY_EXPR ^( TOK_SUBQUERY_OP KW_IN ) subQueryExpression $precedenceEqualExpression) ) | ( KW_NOT KW_IN expressions ) -> ^( KW_NOT ^( TOK_FUNCTION KW_IN $precedenceEqualExpression expressions ) ) | ( KW_IN LPAREN KW_SELECT )=> ( KW_IN subQueryExpression ) -> ^( TOK_SUBQUERY_EXPR ^( TOK_SUBQUERY_OP KW_IN ) subQueryExpression $precedenceEqualExpression) | ( KW_IN expressions ) -> ^( TOK_FUNCTION KW_IN $precedenceEqualExpression expressions ) | ( KW_NOT KW_BETWEEN (min= precedenceBitwiseOrExpression ) KW_AND (max= precedenceBitwiseOrExpression ) ) -> ^( TOK_FUNCTION Identifier[\"between\"] KW_TRUE $left $min $max) | ( KW_BETWEEN (min= precedenceBitwiseOrExpression ) KW_AND (max= precedenceBitwiseOrExpression ) ) -> ^( TOK_FUNCTION Identifier[\"between\"] KW_FALSE $left $min $max) )*";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA43_118 = input.LA(1);

                         
                        int index43_118 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (LA43_118==KW_SELECT) && (synpred7_IdentifiersParser())) {s = 771;}

                        else if ( (LA43_118==KW_MAP) ) {s = 772;}

                        else if ( (LA43_118==KW_REDUCE) && (synpred7_IdentifiersParser())) {s = 773;}

                        else if ( (LA43_118==BigintLiteral||LA43_118==CharSetName||LA43_118==DecimalLiteral||(LA43_118 >= Identifier && LA43_118 <= KW_ANALYZE)||(LA43_118 >= KW_ARCHIVE && LA43_118 <= KW_CHANGE)||(LA43_118 >= KW_CLUSTER && LA43_118 <= KW_COLLECTION)||(LA43_118 >= KW_COLUMNS && LA43_118 <= KW_CONCATENATE)||(LA43_118 >= KW_CONTINUE && LA43_118 <= KW_CREATE)||LA43_118==KW_CUBE||(LA43_118 >= KW_CURSOR && LA43_118 <= KW_DATA)||(LA43_118 >= KW_DATABASES && LA43_118 <= KW_DISABLE)||(LA43_118 >= KW_DISTRIBUTE && LA43_118 <= KW_ELEM_TYPE)||LA43_118==KW_ENABLE||LA43_118==KW_ESCAPED||(LA43_118 >= KW_EXCLUSIVE && LA43_118 <= KW_EXPORT)||(LA43_118 >= KW_EXTERNAL && LA43_118 <= KW_FLOAT)||(LA43_118 >= KW_FOR && LA43_118 <= KW_FORMATTED)||LA43_118==KW_FULL||(LA43_118 >= KW_FUNCTIONS && LA43_118 <= KW_GROUPING)||(LA43_118 >= KW_HOLD_DDLTIME && LA43_118 <= KW_JAR)||(LA43_118 >= KW_KEYS && LA43_118 <= KW_LEFT)||(LA43_118 >= KW_LIKE && LA43_118 <= KW_LONG)||(LA43_118 >= KW_MAPJOIN && LA43_118 <= KW_MINUS)||(LA43_118 >= KW_MSCK && LA43_118 <= KW_OFFLINE)||LA43_118==KW_OPTION||(LA43_118 >= KW_ORDER && LA43_118 <= KW_OUTPUTFORMAT)||(LA43_118 >= KW_OVERWRITE && LA43_118 <= KW_OWNER)||(LA43_118 >= KW_PARTITION && LA43_118 <= KW_PLUS)||(LA43_118 >= KW_PRETTY && LA43_118 <= KW_RECORDWRITER)||(LA43_118 >= KW_REGEXP && LA43_118 <= KW_SCHEMAS)||(LA43_118 >= KW_SEMI && LA43_118 <= KW_TABLES)||(LA43_118 >= KW_TBLPROPERTIES && LA43_118 <= KW_TERMINATED)||(LA43_118 >= KW_TIMESTAMP && LA43_118 <= KW_TRANSACTIONS)||(LA43_118 >= KW_TRIGGER && LA43_118 <= KW_UNARCHIVE)||(LA43_118 >= KW_UNDO && LA43_118 <= KW_UNIONTYPE)||(LA43_118 >= KW_UNLOCK && LA43_118 <= KW_VALUE_TYPE)||LA43_118==KW_VIEW||LA43_118==KW_WHILE||LA43_118==KW_WITH||LA43_118==LPAREN||LA43_118==MINUS||(LA43_118 >= Number && LA43_118 <= PLUS)||(LA43_118 >= SmallintLiteral && LA43_118 <= TinyintLiteral)) ) {s = 774;}

                         
                        input.seek(index43_118);

                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA43_770 = input.LA(1);

                         
                        int index43_770 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred6_IdentifiersParser()) ) {s = 931;}

                        else if ( (true) ) {s = 932;}

                         
                        input.seek(index43_770);

                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA43_772 = input.LA(1);

                         
                        int index43_772 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred7_IdentifiersParser()) ) {s = 773;}

                        else if ( (true) ) {s = 774;}

                         
                        input.seek(index43_772);

                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}

            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 43, _s, input);
            error(nvae);
            throw nvae;
        }

    }
 

    public static final BitSet FOLLOW_KW_GROUP_in_groupByClause72 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_KW_BY_in_groupByClause74 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5FEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_groupByExpression_in_groupByClause80 = new BitSet(new long[]{0x0000000000000402L,0x0400000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_COMMA_in_groupByClause88 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5FEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_groupByExpression_in_groupByClause90 = new BitSet(new long[]{0x0000000000000402L,0x0400000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_KW_WITH_in_groupByClause103 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_KW_ROLLUP_in_groupByClause105 = new BitSet(new long[]{0x0000000000000002L,0x0400000000000000L});
    public static final BitSet FOLLOW_KW_WITH_in_groupByClause113 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
    public static final BitSet FOLLOW_KW_CUBE_in_groupByClause115 = new BitSet(new long[]{0x0000000000000002L,0x0400000000000000L});
    public static final BitSet FOLLOW_KW_GROUPING_in_groupByClause128 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000400000000L});
    public static final BitSet FOLLOW_KW_SETS_in_groupByClause130 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_LPAREN_in_groupByClause137 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5FEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_groupingSetExpression_in_groupByClause139 = new BitSet(new long[]{0x0000000000000400L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_COMMA_in_groupByClause143 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5FEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_groupingSetExpression_in_groupByClause145 = new BitSet(new long[]{0x0000000000000400L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_RPAREN_in_groupByClause150 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_groupByExpression_in_groupingSetExpression244 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_groupingSetExpression265 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5FEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_groupByExpression_in_groupingSetExpression271 = new BitSet(new long[]{0x0000000000000400L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_COMMA_in_groupingSetExpression274 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5FEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_groupByExpression_in_groupingSetExpression276 = new BitSet(new long[]{0x0000000000000400L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_RPAREN_in_groupingSetExpression283 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_groupingSetExpression305 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_RPAREN_in_groupingSetExpression310 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_groupByExpression350 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KW_HAVING_in_havingClause381 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5FEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_havingCondition_in_havingClause383 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_havingCondition422 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KW_ORDER_in_orderByClause454 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_KW_BY_in_orderByClause456 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5FEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_columnRefOrder_in_orderByClause458 = new BitSet(new long[]{0x0000000000000402L});
    public static final BitSet FOLLOW_COMMA_in_orderByClause462 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5FEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_columnRefOrder_in_orderByClause464 = new BitSet(new long[]{0x0000000000000402L});
    public static final BitSet FOLLOW_KW_CLUSTER_in_clusterByClause506 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_KW_BY_in_clusterByClause508 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_LPAREN_in_clusterByClause514 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5FEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_expression_in_clusterByClause516 = new BitSet(new long[]{0x0000000000000400L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_COMMA_in_clusterByClause519 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5FEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_expression_in_clusterByClause521 = new BitSet(new long[]{0x0000000000000400L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_RPAREN_in_clusterByClause525 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KW_CLUSTER_in_clusterByClause546 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_KW_BY_in_clusterByClause548 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5FEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_expression_in_clusterByClause554 = new BitSet(new long[]{0x0000000000000402L});
    public static final BitSet FOLLOW_COMMA_in_clusterByClause566 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5FEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_expression_in_clusterByClause568 = new BitSet(new long[]{0x0000000000000402L});
    public static final BitSet FOLLOW_KW_PARTITION_in_partitionByClause612 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_KW_BY_in_partitionByClause614 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_LPAREN_in_partitionByClause620 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5FEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_expression_in_partitionByClause622 = new BitSet(new long[]{0x0000000000000400L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_COMMA_in_partitionByClause625 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5FEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_expression_in_partitionByClause627 = new BitSet(new long[]{0x0000000000000400L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_RPAREN_in_partitionByClause631 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KW_PARTITION_in_partitionByClause652 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_KW_BY_in_partitionByClause654 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5FEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_expression_in_partitionByClause660 = new BitSet(new long[]{0x0000000000000402L});
    public static final BitSet FOLLOW_COMMA_in_partitionByClause668 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5FEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_expression_in_partitionByClause670 = new BitSet(new long[]{0x0000000000000402L});
    public static final BitSet FOLLOW_KW_DISTRIBUTE_in_distributeByClause712 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_KW_BY_in_distributeByClause714 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_LPAREN_in_distributeByClause720 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5FEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_expression_in_distributeByClause722 = new BitSet(new long[]{0x0000000000000400L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_COMMA_in_distributeByClause725 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5FEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_expression_in_distributeByClause727 = new BitSet(new long[]{0x0000000000000400L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_RPAREN_in_distributeByClause731 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KW_DISTRIBUTE_in_distributeByClause752 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_KW_BY_in_distributeByClause754 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5FEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_expression_in_distributeByClause760 = new BitSet(new long[]{0x0000000000000402L});
    public static final BitSet FOLLOW_COMMA_in_distributeByClause768 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5FEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_expression_in_distributeByClause770 = new BitSet(new long[]{0x0000000000000402L});
    public static final BitSet FOLLOW_KW_SORT_in_sortByClause812 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_KW_BY_in_sortByClause814 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_LPAREN_in_sortByClause820 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5FEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_columnRefOrder_in_sortByClause822 = new BitSet(new long[]{0x0000000000000400L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_COMMA_in_sortByClause830 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5FEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_columnRefOrder_in_sortByClause832 = new BitSet(new long[]{0x0000000000000400L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_RPAREN_in_sortByClause836 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KW_SORT_in_sortByClause857 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_KW_BY_in_sortByClause859 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5FEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_columnRefOrder_in_sortByClause865 = new BitSet(new long[]{0x0000000000000402L});
    public static final BitSet FOLLOW_COMMA_in_sortByClause878 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5FEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_columnRefOrder_in_sortByClause880 = new BitSet(new long[]{0x0000000000000402L});
    public static final BitSet FOLLOW_functionName_in_function923 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_LPAREN_in_function929 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABFFFFED6L,0x3EDF5FEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x00007C4648A5FFDDL});
    public static final BitSet FOLLOW_STAR_in_function950 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_KW_DISTINCT_in_function966 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5FEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x00007C4648A5FFDDL});
    public static final BitSet FOLLOW_selectExpression_in_function971 = new BitSet(new long[]{0x0000000000000400L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_COMMA_in_function974 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5FEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x00007C0648A5FFDDL});
    public static final BitSet FOLLOW_selectExpression_in_function976 = new BitSet(new long[]{0x0000000000000400L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_RPAREN_in_function994 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0020000000000000L});
    public static final BitSet FOLLOW_KW_OVER_in_function997 = new BitSet(new long[]{0x0000000004000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_window_specification_in_function1001 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KW_IF_in_functionName1133 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KW_ARRAY_in_functionName1137 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KW_MAP_in_functionName1141 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KW_STRUCT_in_functionName1145 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KW_UNIONTYPE_in_functionName1149 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functionIdentifier_in_functionName1153 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KW_CAST_in_castExpression1184 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_LPAREN_in_castExpression1190 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5FEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_expression_in_castExpression1202 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_KW_AS_in_castExpression1214 = new BitSet(new long[]{0x00100E0000000000L,0x0000800008002C00L,0x0000000000000200L,0x0180408000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_primitiveType_in_castExpression1226 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_RPAREN_in_castExpression1232 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KW_CASE_in_caseExpression1273 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5FEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_expression_in_caseExpression1275 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_KW_WHEN_in_caseExpression1282 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5FEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_expression_in_caseExpression1284 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_KW_THEN_in_caseExpression1286 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5FEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_expression_in_caseExpression1288 = new BitSet(new long[]{0x0000000000000000L,0x0000000140000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_KW_ELSE_in_caseExpression1297 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5FEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_expression_in_caseExpression1299 = new BitSet(new long[]{0x0000000000000000L,0x0000000100000000L});
    public static final BitSet FOLLOW_KW_END_in_caseExpression1307 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KW_CASE_in_whenExpression1349 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_KW_WHEN_in_whenExpression1358 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5FEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_expression_in_whenExpression1360 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_KW_THEN_in_whenExpression1362 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5FEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_expression_in_whenExpression1364 = new BitSet(new long[]{0x0000000000000000L,0x0000000140000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_KW_ELSE_in_whenExpression1373 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5FEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_expression_in_whenExpression1375 = new BitSet(new long[]{0x0000000000000000L,0x0000000100000000L});
    public static final BitSet FOLLOW_KW_END_in_whenExpression1383 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Number_in_constant1425 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_dateLiteral_in_constant1433 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_StringLiteral_in_constant1441 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_stringLiteralSequence_in_constant1449 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BigintLiteral_in_constant1457 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SmallintLiteral_in_constant1465 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TinyintLiteral_in_constant1473 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DecimalLiteral_in_constant1481 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_charSetStringLiteral_in_constant1489 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_booleanValue_in_constant1497 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_StringLiteral_in_stringLiteralSequence1518 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_StringLiteral_in_stringLiteralSequence1520 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_CharSetName_in_charSetStringLiteral1565 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_CharSetLiteral_in_charSetStringLiteral1569 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KW_DATE_in_dateLiteral1602 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_StringLiteral_in_dateLiteral1604 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_precedenceOrExpression_in_expression1643 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KW_NULL_in_atomExpression1664 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_dateLiteral_in_atomExpression1676 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_constant_in_atomExpression1684 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_castExpression_in_atomExpression1692 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_caseExpression_in_atomExpression1700 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_whenExpression_in_atomExpression1708 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_function_in_atomExpression1724 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_tableOrColumn_in_atomExpression1732 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_atomExpression1740 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5FEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_expression_in_atomExpression1743 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_RPAREN_in_atomExpression1745 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_atomExpression_in_precedenceFieldExpression1768 = new BitSet(new long[]{0x0000000000020002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_LSQUARE_in_precedenceFieldExpression1772 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5FEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_expression_in_precedenceFieldExpression1775 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000008000000000L});
    public static final BitSet FOLLOW_RSQUARE_in_precedenceFieldExpression1777 = new BitSet(new long[]{0x0000000000020002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_DOT_in_precedenceFieldExpression1784 = new BitSet(new long[]{0xFDE9FFFDFC000000L,0xB7AEFF7ABDFFFED6L,0x3EDF5EEE7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000000000A5FFDDL});
    public static final BitSet FOLLOW_identifier_in_precedenceFieldExpression1787 = new BitSet(new long[]{0x0000000000020002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_KW_NULL_in_nullCondition1840 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KW_NOT_in_nullCondition1854 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000040000000000L});
    public static final BitSet FOLLOW_KW_NULL_in_nullCondition1856 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_precedenceUnaryOperator_in_precedenceUnaryPrefixExpression1884 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5EEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_precedenceFieldExpression_in_precedenceUnaryPrefixExpression1889 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_precedenceUnaryPrefixExpression_in_precedenceUnarySuffixExpression1906 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_KW_IS_in_precedenceUnarySuffixExpression1911 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000050000000000L});
    public static final BitSet FOLLOW_nullCondition_in_precedenceUnarySuffixExpression1913 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BITWISEXOR_in_precedenceBitwiseXorOperator1961 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_precedenceUnarySuffixExpression_in_precedenceBitwiseXorExpression1982 = new BitSet(new long[]{0x0000000000000042L});
    public static final BitSet FOLLOW_precedenceBitwiseXorOperator_in_precedenceBitwiseXorExpression1985 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5EEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_precedenceUnarySuffixExpression_in_precedenceBitwiseXorExpression1988 = new BitSet(new long[]{0x0000000000000042L});
    public static final BitSet FOLLOW_precedenceBitwiseXorExpression_in_precedenceStarExpression2045 = new BitSet(new long[]{0x000000000000C002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000040080000000L});
    public static final BitSet FOLLOW_precedenceStarOperator_in_precedenceStarExpression2048 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5EEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_precedenceBitwiseXorExpression_in_precedenceStarExpression2051 = new BitSet(new long[]{0x000000000000C002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000040080000000L});
    public static final BitSet FOLLOW_precedenceStarExpression_in_precedencePlusExpression2100 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000440000000L});
    public static final BitSet FOLLOW_precedencePlusOperator_in_precedencePlusExpression2103 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5EEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_precedenceStarExpression_in_precedencePlusExpression2106 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000440000000L});
    public static final BitSet FOLLOW_AMPERSAND_in_precedenceAmpersandOperator2130 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_precedencePlusExpression_in_precedenceAmpersandExpression2151 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_precedenceAmpersandOperator_in_precedenceAmpersandExpression2154 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5EEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_precedencePlusExpression_in_precedenceAmpersandExpression2157 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_BITWISEOR_in_precedenceBitwiseOrOperator2181 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_precedenceAmpersandExpression_in_precedenceBitwiseOrExpression2202 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_precedenceBitwiseOrOperator_in_precedenceBitwiseOrExpression2205 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5EEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_precedenceAmpersandExpression_in_precedenceBitwiseOrExpression2208 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_precedenceEqualNegatableOperator_in_precedenceEqualOperator2262 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EQUAL_in_precedenceEqualOperator2266 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EQUAL_NS_in_precedenceEqualOperator2270 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOTEQUAL_in_precedenceEqualOperator2274 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LESSTHANOREQUALTO_in_precedenceEqualOperator2278 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LESSTHAN_in_precedenceEqualOperator2282 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GREATERTHANOREQUALTO_in_precedenceEqualOperator2286 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GREATERTHAN_in_precedenceEqualOperator2290 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_subQueryExpression2313 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000100000000L,0x0000000020001000L});
    public static final BitSet FOLLOW_selectStatement_in_subQueryExpression2316 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_RPAREN_in_subQueryExpression2319 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_precedenceBitwiseOrExpression_in_precedenceEqualExpression2347 = new BitSet(new long[]{0x0000010001B00002L,0x0000000000000000L,0x0000010000200002L,0x0000000000202000L,0x0000000106000000L});
    public static final BitSet FOLLOW_KW_NOT_in_precedenceEqualExpression2369 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000200000L,0x0000000000202000L});
    public static final BitSet FOLLOW_precedenceEqualNegatableOperator_in_precedenceEqualExpression2371 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5EEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_precedenceBitwiseOrExpression_in_precedenceEqualExpression2375 = new BitSet(new long[]{0x0000010001B00002L,0x0000000000000000L,0x0000010000200002L,0x0000000000202000L,0x0000000106000000L});
    public static final BitSet FOLLOW_precedenceEqualOperator_in_precedenceEqualExpression2408 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5EEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_precedenceBitwiseOrExpression_in_precedenceEqualExpression2412 = new BitSet(new long[]{0x0000010001B00002L,0x0000000000000000L,0x0000010000200002L,0x0000000000202000L,0x0000000106000000L});
    public static final BitSet FOLLOW_KW_NOT_in_precedenceEqualExpression2453 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_KW_IN_in_precedenceEqualExpression2455 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_subQueryExpression_in_precedenceEqualExpression2457 = new BitSet(new long[]{0x0000010001B00002L,0x0000000000000000L,0x0000010000200002L,0x0000000000202000L,0x0000000106000000L});
    public static final BitSet FOLLOW_KW_NOT_in_precedenceEqualExpression2496 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_KW_IN_in_precedenceEqualExpression2498 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_expressions_in_precedenceEqualExpression2500 = new BitSet(new long[]{0x0000010001B00002L,0x0000000000000000L,0x0000010000200002L,0x0000000000202000L,0x0000000106000000L});
    public static final BitSet FOLLOW_KW_IN_in_precedenceEqualExpression2544 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_subQueryExpression_in_precedenceEqualExpression2546 = new BitSet(new long[]{0x0000010001B00002L,0x0000000000000000L,0x0000010000200002L,0x0000000000202000L,0x0000000106000000L});
    public static final BitSet FOLLOW_KW_IN_in_precedenceEqualExpression2581 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_expressions_in_precedenceEqualExpression2583 = new BitSet(new long[]{0x0000010001B00002L,0x0000000000000000L,0x0000010000200002L,0x0000000000202000L,0x0000000106000000L});
    public static final BitSet FOLLOW_KW_NOT_in_precedenceEqualExpression2614 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_KW_BETWEEN_in_precedenceEqualExpression2616 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5EEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_precedenceBitwiseOrExpression_in_precedenceEqualExpression2621 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_KW_AND_in_precedenceEqualExpression2624 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5EEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_precedenceBitwiseOrExpression_in_precedenceEqualExpression2629 = new BitSet(new long[]{0x0000010001B00002L,0x0000000000000000L,0x0000010000200002L,0x0000000000202000L,0x0000000106000000L});
    public static final BitSet FOLLOW_KW_BETWEEN_in_precedenceEqualExpression2669 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5EEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_precedenceBitwiseOrExpression_in_precedenceEqualExpression2674 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_KW_AND_in_precedenceEqualExpression2677 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5EEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_precedenceBitwiseOrExpression_in_precedenceEqualExpression2682 = new BitSet(new long[]{0x0000010001B00002L,0x0000000000000000L,0x0000010000200002L,0x0000000000202000L,0x0000000106000000L});
    public static final BitSet FOLLOW_KW_EXISTS_in_precedenceEqualExpression2737 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_subQueryExpression_in_precedenceEqualExpression2739 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_expressions2775 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5FEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_expression_in_expressions2777 = new BitSet(new long[]{0x0000000000000400L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_COMMA_in_expressions2780 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5FEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_expression_in_expressions2782 = new BitSet(new long[]{0x0000000000000400L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_RPAREN_in_expressions2786 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KW_NOT_in_precedenceNotOperator2812 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_precedenceNotOperator_in_precedenceNotExpression2834 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5FEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_precedenceEqualExpression_in_precedenceNotExpression2839 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KW_AND_in_precedenceAndOperator2861 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_precedenceNotExpression_in_precedenceAndExpression2882 = new BitSet(new long[]{0x0000000200000002L});
    public static final BitSet FOLLOW_precedenceAndOperator_in_precedenceAndExpression2885 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5FEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_precedenceNotExpression_in_precedenceAndExpression2888 = new BitSet(new long[]{0x0000000200000002L});
    public static final BitSet FOLLOW_KW_OR_in_precedenceOrOperator2912 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_precedenceAndExpression_in_precedenceOrExpression2933 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000800000000000L});
    public static final BitSet FOLLOW_precedenceOrOperator_in_precedenceOrExpression2936 = new BitSet(new long[]{0xFDEFFFFDFC042080L,0xF7AEFF7ABDFFFED6L,0x3EDF5FEF7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000780648A5FFDDL});
    public static final BitSet FOLLOW_precedenceAndExpression_in_precedenceOrExpression2939 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000800000000000L});
    public static final BitSet FOLLOW_KW_TRUE_in_booleanValue2963 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KW_FALSE_in_booleanValue2968 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_tableName_in_tableOrPartition2988 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0200000000000000L});
    public static final BitSet FOLLOW_partitionSpec_in_tableOrPartition2990 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KW_PARTITION_in_partitionSpec3022 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_LPAREN_in_partitionSpec3029 = new BitSet(new long[]{0xFDE9FFFDFC000000L,0xB7AEFF7ABDFFFED6L,0x3EDF5EEE7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000000000A5FFDDL});
    public static final BitSet FOLLOW_partitionVal_in_partitionSpec3031 = new BitSet(new long[]{0x0000000000000400L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_COMMA_in_partitionSpec3034 = new BitSet(new long[]{0xFDE9FFFDFC000000L,0xB7AEFF7ABDFFFED6L,0x3EDF5EEE7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000000000A5FFDDL});
    public static final BitSet FOLLOW_partitionVal_in_partitionSpec3037 = new BitSet(new long[]{0x0000000000000400L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_RPAREN_in_partitionSpec3042 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_partitionVal3073 = new BitSet(new long[]{0x0000000000100002L});
    public static final BitSet FOLLOW_EQUAL_in_partitionVal3076 = new BitSet(new long[]{0x0000000000042080L,0x0000020000000400L,0x0000000000000000L,0x4000000000000000L,0x0000580200000000L});
    public static final BitSet FOLLOW_constant_in_partitionVal3078 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KW_PARTITION_in_dropPartitionSpec3112 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_LPAREN_in_dropPartitionSpec3119 = new BitSet(new long[]{0xFDE9FFFDFC000000L,0xB7AEFF7ABDFFFED6L,0x3EDF5EEE7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000000000A5FFDDL});
    public static final BitSet FOLLOW_dropPartitionVal_in_dropPartitionSpec3121 = new BitSet(new long[]{0x0000000000000400L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_COMMA_in_dropPartitionSpec3124 = new BitSet(new long[]{0xFDE9FFFDFC000000L,0xB7AEFF7ABDFFFED6L,0x3EDF5EEE7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000000000A5FFDDL});
    public static final BitSet FOLLOW_dropPartitionVal_in_dropPartitionSpec3127 = new BitSet(new long[]{0x0000000000000400L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_RPAREN_in_dropPartitionSpec3132 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_dropPartitionVal3163 = new BitSet(new long[]{0x0000000001900000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000106000000L});
    public static final BitSet FOLLOW_dropPartitionOperator_in_dropPartitionVal3165 = new BitSet(new long[]{0x0000000000042080L,0x0000020000000400L,0x0000000000000000L,0x4000000000000000L,0x0000580200000000L});
    public static final BitSet FOLLOW_constant_in_dropPartitionVal3167 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sysFuncNames_in_descFuncNames3586 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_StringLiteral_in_descFuncNames3594 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functionIdentifier_in_descFuncNames3602 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_identifier3623 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_nonReserved_in_identifier3631 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_functionIdentifier3665 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_DOT_in_functionIdentifier3667 = new BitSet(new long[]{0xFDE9FFFDFC000000L,0xB7AEFF7ABDFFFED6L,0x3EDF5EEE7FEF7FFFL,0xEFBBFFFFDFFFEFFFL,0x0000000000A5FFDDL});
    public static final BitSet FOLLOW_identifier_in_functionIdentifier3671 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_functionIdentifier3692 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_principalIdentifier3719 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QuotedIdentifier_in_principalIdentifier3727 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COMMA_in_synpred1_IdentifiersParser563 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COMMA_in_synpred2_IdentifiersParser664 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COMMA_in_synpred3_IdentifiersParser764 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COMMA_in_synpred4_IdentifiersParser874 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functionName_in_synpred5_IdentifiersParser1717 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_LPAREN_in_synpred5_IdentifiersParser1719 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KW_NOT_in_synpred6_IdentifiersParser2441 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_KW_IN_in_synpred6_IdentifiersParser2443 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_LPAREN_in_synpred6_IdentifiersParser2445 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000020000000L});
    public static final BitSet FOLLOW_KW_SELECT_in_synpred6_IdentifiersParser2447 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KW_IN_in_synpred7_IdentifiersParser2534 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_LPAREN_in_synpred7_IdentifiersParser2536 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000020000000L});
    public static final BitSet FOLLOW_KW_SELECT_in_synpred7_IdentifiersParser2538 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KW_EXISTS_in_synpred8_IdentifiersParser2728 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_LPAREN_in_synpred8_IdentifiersParser2730 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000020000000L});
    public static final BitSet FOLLOW_KW_SELECT_in_synpred8_IdentifiersParser2732 = new BitSet(new long[]{0x0000000000000002L});

}