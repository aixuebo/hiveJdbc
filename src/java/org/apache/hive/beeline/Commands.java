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

/*
 * This source file is based on code taken from SQLLine 1.0.2
 * See SQLLine notice in LICENSE
 */
package org.apache.hive.beeline;

import org.apache.hadoop.io.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.SQLWarning;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.apache.hadoop.hive.common.cli.ShellCmdExecutor;
import org.apache.hive.jdbc.HiveStatement;

//所有的命令,都使用空格进行拆分,第一个位置设置的是要执行命令的方法名字,比如metadata,后面空格位置跟着的是需要的额参数集合
public class Commands {
  private final BeeLine beeLine;
  private static final int DEFAULT_QUERY_PROGRESS_INTERVAL = 1000;//默认查询进度的时间周期间隔
  private static final int DEFAULT_QUERY_PROGRESS_THREAD_TIMEOUT = 10 * 1000;//默认查询进度的时间超时时间

  /**
   * @param beeLine
   */
  Commands(BeeLine beeLine) {
    this.beeLine = beeLine;
  }

    /**
     比如 ResultSet getTables()
     * @param line 内容是!metadata  catalog database table types[]
     * @return
     */
  public boolean metadata(String line) {
    beeLine.debug(line);

    String[] parts = beeLine.split(line);//使用空格拆分命令
    List<String> params = new LinkedList<String>(Arrays.asList(parts));
    if (parts == null || parts.length == 0) {
      return dbinfo("");
    }
    //移除metadata 和 catalog
    params.remove(0);
    params.remove(0);
    beeLine.debug(params.toString());
    return metadata(parts[1],
        params.toArray(new String[0]));//查询数据库下所有的表的元数据
  }

  //获取元数据信息

    /**
     * 执行DatabaseMetaData对象的方法
     * @param cmd  DatabaseMetaData对象的某一个具体的方法
     * @param args 该方法需要的参数集合
     * @return
     */
  public boolean metadata(String cmd, String[] args) {
    if (!(beeLine.assertConnection())) {
      return false;
    }

    try {
      Method[] m = beeLine.getDatabaseMetaData().getClass().getMethods();
      Set<String> methodNames = new TreeSet<String>();
      Set<String> methodNamesUpper = new TreeSet<String>();
      for (int i = 0; i < m.length; i++) {
        methodNames.add(m[i].getName());
        methodNamesUpper.add(m[i].getName().toUpperCase());
      }

      if (!methodNamesUpper.contains(cmd.toUpperCase())) {//说明没有要调用的方法
        beeLine.error(beeLine.loc("no-such-method", cmd));
        beeLine.error(beeLine.loc("possible-methods"));
        for (Iterator<String> i = methodNames.iterator(); i.hasNext();) {
          beeLine.error("   " + i.next());
        }
        return false;
      }

      Object res = beeLine.getReflector().invoke(beeLine.getDatabaseMetaData(),
          DatabaseMetaData.class, cmd, Arrays.asList(args));

      if (res instanceof ResultSet) {
        ResultSet rs = (ResultSet) res;
        if (rs != null) {
          try {
            beeLine.print(rs);//打印结果集
          } finally {
            rs.close();
          }
        }
      } else if (res != null) {
        beeLine.output(res.toString());
      }
    } catch (Exception e) {
      return beeLine.error(e);
    }

    return true;
  }

  //添加一个driver的class----!addlocaldrivername driverClassName
  public boolean addlocaldrivername(String line) {
    String driverName = arg1(line, "driver class name");
    try {
      beeLine.setDrivers(Arrays.asList(beeLine.scanDrivers(false)));
    } catch (IOException e) {
      beeLine.error("Fail to scan drivers due to the exception:" + e);
      beeLine.error(e);
    }
    for (Driver d : beeLine.getDrivers()) {
      if (driverName.equals(d.getClass().getName())) {
        beeLine.addLocalDriverClazz(driverName);
        return true;
      }
    }
    beeLine.error("Fail to find a driver which contains the driver class");
    return false;
  }

  //添加一个driver的jar----!addlocaldriverjar driverJarPath
  public boolean addlocaldriverjar(String line) {
    // If jar file is in the hdfs, it should be downloaded first.
    String jarPath = arg1(line, "jar path");
    File p = new File(jarPath);
    if (!p.exists()) {
      beeLine.error("The jar file in the path " + jarPath + " can't be found!");
      return false;
    }

    URLClassLoader classLoader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
    try {
      beeLine.debug(jarPath + " is added to the local beeline.");
      URLClassLoader newClassLoader = new URLClassLoader(new URL[]{p.toURL()}, classLoader);

      Thread.currentThread().setContextClassLoader(newClassLoader);
      beeLine.setDrivers(Arrays.asList(beeLine.scanDrivers(false)));
    } catch (Exception e) {
      beeLine.error("Fail to add local jar due to the exception:" + e);
      beeLine.error(e);
    }
    return true;
  }

  //获取历史命令,基于jline工具包记录的
  public boolean history(String line) {
    Iterator hist = beeLine.getConsoleReader().getHistory().entries();
    int index = 1;//历史命令下标计数器
    while(hist.hasNext()){
      beeLine.output(beeLine.getColorBuffer().pad(index + ".", 6)
          .append(hist.next().toString()));
    }
    return true;
  }

  //获取第一个参数的值
  String arg1(String line, String paramname) {
    return arg1(line, paramname, null);
  }

    /**
     * line必须能解析成2个元素的数组,即命令+一个参数
     * @param line
     * @param paramname
     * @param def 默认值--出异常的时候使用
     * @return 获取第一个参数的值
     */
  String arg1(String line, String paramname, String def) {
    String[] ret = beeLine.split(line);//按照空格拆分

    if (ret == null || ret.length != 2) {//出异常,因此使用默认值
      if (def != null) {
        return def;
      }
      throw new IllegalArgumentException(beeLine.loc("arg-usage",
          new Object[] {ret.length == 0 ? "" : ret[0],
              paramname}));
    }
    return ret[1];
  }

  //获取索引信息----indexes table
  public boolean indexes(String line) throws Exception {
    return metadata("getIndexInfo", new String[] {
        beeLine.getConnection().getCatalog(), null,//参数catalog和数据库
        arg1(line, "table name"),//参数表名字,即获取line的第一个参数
        false + "",//不是唯一索引
        true + ""});
  }

  //执行DatabaseMetaData的getPrimaryKeys方法,打印一个表的主键列信息----!primarykeys tableName
  public boolean primarykeys(String line) throws Exception {
    return metadata("getPrimaryKeys", new String[] {
        beeLine.getConnection().getCatalog(), null,
        arg1(line, "table name"),});
  }

  //执行DatabaseMetaData的getExportedKeys方法,打印一个表的外键属性信息----!exportedkeys tableName
  public boolean exportedkeys(String line) throws Exception {
    return metadata("getExportedKeys", new String[] {
        beeLine.getConnection().getCatalog(), null,
        arg1(line, "table name"),});
  }

  //执行DatabaseMetaData的getImportedKeys方法,打印一个表的检索表中外键列引用的主键列信息----!importedkeys tableName
  public boolean importedkeys(String line) throws Exception {
    return metadata("getImportedKeys", new String[] {
        beeLine.getConnection().getCatalog(), null,
        arg1(line, "table name"),});
  }

  //获取存储过程的描述---!procedures procedureName(存储过程的name)
  public boolean procedures(String line) throws Exception {
    //调用DatabaseMetaData对象的getProcedures方法获取结果
    return metadata("getProcedures", new String[] {
        beeLine.getConnection().getCatalog(), null,
        arg1(line, "procedure name pattern", "%"),});
  }

  //执行DatabaseMetaData的getTables方法,打印一个表的属性信息----!tables tableName
  public boolean tables(String line) throws Exception {
    return metadata("getTables", new String[] {
        beeLine.getConnection().getCatalog(), null,
        arg1(line, "table name", "%"), null});
  }

  //执行DatabaseMetaData的getTypeInfo方法,打印数据库提供的字段类型信息----!typeinfo
  public boolean typeinfo(String line) throws Exception {
    return metadata("getTypeInfo", new String[0]);
  }

 //执行Connection的nativeSQL方法-----!nativesql sql
  public boolean nativesql(String sql) throws Exception {
    if (sql.startsWith(BeeLine.COMMAND_PREFIX)) {
      sql = sql.substring(1);
    }
    if (sql.startsWith("native")) {
      sql = sql.substring("native".length() + 1);
    }
    String nat = beeLine.getConnection().nativeSQL(sql);
    beeLine.output(nat);
    return true;
  }

  //执行DatabaseMetaData的getColumns方法,打印一个表的所有列属性信息----!columns tableName
  public boolean columns(String line) throws Exception {
    return metadata("getColumns", new String[] {
        beeLine.getConnection().getCatalog(), null,
        arg1(line, "table name"), "%"});
  }

  //drop table 所有的表----!dropall
  public boolean dropall(String line) {
    if (beeLine.getDatabaseConnection() == null || beeLine.getDatabaseConnection().getUrl() == null) {
      return beeLine.error(beeLine.loc("no-current-connection"));
    }
    try {
      if (!(beeLine.getConsoleReader().readLine(beeLine.loc("really-drop-all")).equals("y"))) {
        return beeLine.error("abort-drop-all");
      }

      List<String> cmds = new LinkedList<String>();//加入批处理集合中
      ResultSet rs = beeLine.getTables();
      try {
        while (rs.next()) {
          cmds.add("DROP TABLE "
              + rs.getString("TABLE_NAME") + ";");
        }
      } finally {
        try {
          rs.close();
        } catch (Exception e) {
        }
      }
      // run as a batch
      return beeLine.runCommands(cmds) == cmds.size();
    } catch (Exception e) {
      return beeLine.error(e);
    }
  }

  //重新连接当前数据库----!reconnect
  public boolean reconnect(String line) {
    if (beeLine.getDatabaseConnection() == null || beeLine.getDatabaseConnection().getUrl() == null) {
      return beeLine.error(beeLine.loc("no-current-connection"));
    }
    beeLine.info(beeLine.loc("reconnecting", beeLine.getDatabaseConnection().getUrl()));
    try {
      beeLine.getDatabaseConnection().reconnect();
    } catch (Exception e) {
      return beeLine.error(e);
    }
    return true;
  }

  //打印所有的driver信息----!scan
  public boolean scan(String line) throws IOException {
    TreeSet<String> names = new TreeSet<String>();

    if (beeLine.getDrivers() == null) {
      beeLine.setDrivers(Arrays.asList(beeLine.scanDrivers(line)));
    }

    beeLine.info(beeLine.loc("drivers-found-count", beeLine.getDrivers().size()));

    // unique the list
    for (Iterator<Driver> i = beeLine.getDrivers().iterator(); i.hasNext();) {
      names.add(i.next().getClass().getName());
    }

    //打印信息
    beeLine.output(beeLine.getColorBuffer()
        .bold(beeLine.getColorBuffer().pad(beeLine.loc("compliant"), 10).getMono())
        .bold(beeLine.getColorBuffer().pad(beeLine.loc("jdbc-version"), 8).getMono())
        .bold(beeLine.getColorBuffer(beeLine.loc("driver-class")).getMono()));

    for (Iterator<String> i = names.iterator(); i.hasNext();) {
      String name = i.next().toString();
      try {
        Driver driver = (Driver) Class.forName(name).newInstance();
        ColorBuffer msg = beeLine.getColorBuffer()
            .pad(driver.jdbcCompliant() ? "yes" : "no", 10)
            .pad(driver.getMajorVersion() + "."
                + driver.getMinorVersion(), 8)
            .append(name);
        if (driver.jdbcCompliant()) {
          beeLine.output(msg);
        } else {
          beeLine.output(beeLine.getColorBuffer().red(msg.getMono()));
        }
      } catch (Throwable t) {
        beeLine.output(beeLine.getColorBuffer().red(name)); // error with driver
      }
    }
    return true;
  }

  //将当前配置信息重新写入到beeline的配置文件中----!save
  public boolean save(String line) throws IOException {
    beeLine.info(beeLine.loc("saving-options", beeLine.getOpts().getPropertiesFile()));
    beeLine.getOpts().save();
    return true;
  }


  public boolean load(String line) throws IOException {
    beeLine.getOpts().load();
    beeLine.info(beeLine.loc("loaded-options", beeLine.getOpts().getPropertiesFile()));
    return true;
  }


  public boolean config(String line) {
    try {
      Properties props = beeLine.getOpts().toProperties();
      Set keys = new TreeSet(props.keySet());
      for (Iterator i = keys.iterator(); i.hasNext();) {
        String key = (String) i.next();
        beeLine.output(beeLine.getColorBuffer()
            .green(beeLine.getColorBuffer().pad(key.substring(
                beeLine.getOpts().PROPERTY_PREFIX.length()), 20)
                .getMono())
            .append(props.getProperty(key)));
      }
    } catch (Exception e) {
      return beeLine.error(e);
    }
    return true;
  }

  //!set key value 修改配置文件
  public boolean set(String line) {
    if (line == null || line.trim().equals("set")
        || line.length() == 0) {
      return config(null);
    }

    String[] parts = beeLine.split(line, 3, "Usage: set <key> <value>");
    if (parts == null) {
      return false;
    }

    String key = parts[1];
    String value = parts[2];
    boolean success = beeLine.getOpts().set(key, value, false);//第三个参数true表示安静的修改,false表示要打印日志
    // if we autosave, then save
    if (success && beeLine.getOpts().getAutosave()) {//说明我们要更改配置文件
      try {
        beeLine.getOpts().save();
      } catch (Exception saveException) {
      }
    }
    return success;
  }

  //设置提交当前事务----!commit
  public boolean commit(String line) throws SQLException {
    if (!(beeLine.assertConnection())) {
      return false;
    }
    if (!(beeLine.assertAutoCommit())) {
      return false;
    }
    try {
      long start = System.currentTimeMillis();
      beeLine.getDatabaseConnection().getConnection().commit();
      long end = System.currentTimeMillis();
      beeLine.showWarnings();
      beeLine.info(beeLine.loc("commit-complete")
          + " " + beeLine.locElapsedTime(end - start));
      return true;
    } catch (Exception e) {
      return beeLine.error(e);
    }
  }

  //设置回滚当前事务----!rollback
  public boolean rollback(String line) throws SQLException {
    if (!(beeLine.assertConnection())) {
      return false;
    }
    if (!(beeLine.assertAutoCommit())) {
      return false;
    }
    try {
      long start = System.currentTimeMillis();
      beeLine.getDatabaseConnection().getConnection().rollback();
      long end = System.currentTimeMillis();
      beeLine.showWarnings();
      beeLine.info(beeLine.loc("rollback-complete")
          + " " + beeLine.locElapsedTime(end - start));
      return true;
    } catch (Exception e) {
      return beeLine.error(e);
    }
  }

  //设置当前数据库是否是自动提交----!autocommit on/off
  public boolean autocommit(String line) throws SQLException {
    if (!(beeLine.assertConnection())) {
      return false;
    }
    if (line.endsWith("on")) {
      beeLine.getDatabaseConnection().getConnection().setAutoCommit(true);
    } else if (line.endsWith("off")) {
      beeLine.getDatabaseConnection().getConnection().setAutoCommit(false);
    }
    beeLine.showWarnings();
    beeLine.autocommitStatus(beeLine.getDatabaseConnection().getConnection());//打印修改后的日志信息
    return true;
  }

  //执行当前数据库的DatabaseMetaData的若干个方法,并且打印信息,相当于当前数据库的一些描述信息----!dbinfo
  public boolean dbinfo(String line) {
    if (!(beeLine.assertConnection())) {
      return false;
    }

    beeLine.showWarnings();
    int padlen = 50;

    String[] m = new String[] {
        "allProceduresAreCallable",
        "allTablesAreSelectable",
        "dataDefinitionCausesTransactionCommit",
        "dataDefinitionIgnoredInTransactions",
        "doesMaxRowSizeIncludeBlobs",
        "getCatalogSeparator",
        "getCatalogTerm",
        "getDatabaseProductName",
        "getDatabaseProductVersion",
        "getDefaultTransactionIsolation",
        "getDriverMajorVersion",
        "getDriverMinorVersion",
        "getDriverName",
        "getDriverVersion",
        "getExtraNameCharacters",
        "getIdentifierQuoteString",
        "getMaxBinaryLiteralLength",
        "getMaxCatalogNameLength",
        "getMaxCharLiteralLength",
        "getMaxColumnNameLength",
        "getMaxColumnsInGroupBy",
        "getMaxColumnsInIndex",
        "getMaxColumnsInOrderBy",
        "getMaxColumnsInSelect",
        "getMaxColumnsInTable",
        "getMaxConnections",
        "getMaxCursorNameLength",
        "getMaxIndexLength",
        "getMaxProcedureNameLength",
        "getMaxRowSize",
        "getMaxSchemaNameLength",
        "getMaxStatementLength",
        "getMaxStatements",
        "getMaxTableNameLength",
        "getMaxTablesInSelect",
        "getMaxUserNameLength",
        "getNumericFunctions",
        "getProcedureTerm",
        "getSchemaTerm",
        "getSearchStringEscape",
        "getSQLKeywords",
        "getStringFunctions",
        "getSystemFunctions",
        "getTimeDateFunctions",
        "getURL",
        "getUserName",
        "isCatalogAtStart",
        "isReadOnly",
        "nullPlusNonNullIsNull",
        "nullsAreSortedAtEnd",
        "nullsAreSortedAtStart",
        "nullsAreSortedHigh",
        "nullsAreSortedLow",
        "storesLowerCaseIdentifiers",
        "storesLowerCaseQuotedIdentifiers",
        "storesMixedCaseIdentifiers",
        "storesMixedCaseQuotedIdentifiers",
        "storesUpperCaseIdentifiers",
        "storesUpperCaseQuotedIdentifiers",
        "supportsAlterTableWithAddColumn",
        "supportsAlterTableWithDropColumn",
        "supportsANSI92EntryLevelSQL",
        "supportsANSI92FullSQL",
        "supportsANSI92IntermediateSQL",
        "supportsBatchUpdates",
        "supportsCatalogsInDataManipulation",
        "supportsCatalogsInIndexDefinitions",
        "supportsCatalogsInPrivilegeDefinitions",
        "supportsCatalogsInProcedureCalls",
        "supportsCatalogsInTableDefinitions",
        "supportsColumnAliasing",
        "supportsConvert",
        "supportsCoreSQLGrammar",
        "supportsCorrelatedSubqueries",
        "supportsDataDefinitionAndDataManipulationTransactions",
        "supportsDataManipulationTransactionsOnly",
        "supportsDifferentTableCorrelationNames",
        "supportsExpressionsInOrderBy",
        "supportsExtendedSQLGrammar",
        "supportsFullOuterJoins",
        "supportsGroupBy",
        "supportsGroupByBeyondSelect",
        "supportsGroupByUnrelated",
        "supportsIntegrityEnhancementFacility",
        "supportsLikeEscapeClause",
        "supportsLimitedOuterJoins",
        "supportsMinimumSQLGrammar",
        "supportsMixedCaseIdentifiers",
        "supportsMixedCaseQuotedIdentifiers",
        "supportsMultipleResultSets",
        "supportsMultipleTransactions",
        "supportsNonNullableColumns",
        "supportsOpenCursorsAcrossCommit",
        "supportsOpenCursorsAcrossRollback",
        "supportsOpenStatementsAcrossCommit",
        "supportsOpenStatementsAcrossRollback",
        "supportsOrderByUnrelated",
        "supportsOuterJoins",
        "supportsPositionedDelete",
        "supportsPositionedUpdate",
        "supportsSchemasInDataManipulation",
        "supportsSchemasInIndexDefinitions",
        "supportsSchemasInPrivilegeDefinitions",
        "supportsSchemasInProcedureCalls",
        "supportsSchemasInTableDefinitions",
        "supportsSelectForUpdate",
        "supportsStoredProcedures",
        "supportsSubqueriesInComparisons",
        "supportsSubqueriesInExists",
        "supportsSubqueriesInIns",
        "supportsSubqueriesInQuantifieds",
        "supportsTableCorrelationNames",
        "supportsTransactions",
        "supportsUnion",
        "supportsUnionAll",
        "usesLocalFilePerTable",
        "usesLocalFiles",
    };

    //执行上面的所有方法,并且打印
    for (int i = 0; i < m.length; i++) {
      try {
        beeLine.output(beeLine.getColorBuffer().pad(m[i], padlen).append(
            "" + beeLine.getReflector().invoke(beeLine.getDatabaseMetaData(),
                m[i], new Object[0])));
      } catch (Exception e) {
        beeLine.handleException(e);
      }
    }
    return true;
  }


  public boolean verbose(String line) {
    beeLine.info("verbose: on");
    return set("set verbose true");
  }

  //设置输出的打印形式----!outputformat vertical/table/csv2/tsv2/dsv/csv/tsv/xmlattr/xmlelements
  public boolean outputformat(String line) {
    return set("set " + line);
  }


  public boolean brief(String line) {
    beeLine.info("verbose: off");
    return set("set verbose false");
  }

  //设置隔离级别----!isolation TRANSACTION_NONE/TRANSACTION_READ_COMMITTED/TRANSACTION_READ_UNCOMMITTED/TRANSACTION_REPEATABLE_READ/TRANSACTION_SERIALIZABLE
  public boolean isolation(String line) throws SQLException {
    if (!(beeLine.assertConnection())) {
      return false;
    }

    int i;

    if (line.endsWith("TRANSACTION_NONE")) {
      i = Connection.TRANSACTION_NONE;
    } else if (line.endsWith("TRANSACTION_READ_COMMITTED")) {
      i = Connection.TRANSACTION_READ_COMMITTED;
    } else if (line.endsWith("TRANSACTION_READ_UNCOMMITTED")) {
      i = Connection.TRANSACTION_READ_UNCOMMITTED;
    } else if (line.endsWith("TRANSACTION_REPEATABLE_READ")) {
      i = Connection.TRANSACTION_REPEATABLE_READ;
    } else if (line.endsWith("TRANSACTION_SERIALIZABLE")) {
      i = Connection.TRANSACTION_SERIALIZABLE;
    } else {
      return beeLine.error("Usage: isolation <TRANSACTION_NONE "
          + "| TRANSACTION_READ_COMMITTED "
          + "| TRANSACTION_READ_UNCOMMITTED "
          + "| TRANSACTION_REPEATABLE_READ "
          + "| TRANSACTION_SERIALIZABLE>");
    }

    beeLine.getDatabaseConnection().getConnection().setTransactionIsolation(i);

    int isol = beeLine.getDatabaseConnection().getConnection().getTransactionIsolation();
    final String isoldesc;
    switch (i)
    {
    case Connection.TRANSACTION_NONE:
      isoldesc = "TRANSACTION_NONE";
      break;
    case Connection.TRANSACTION_READ_COMMITTED:
      isoldesc = "TRANSACTION_READ_COMMITTED";
      break;
    case Connection.TRANSACTION_READ_UNCOMMITTED:
      isoldesc = "TRANSACTION_READ_UNCOMMITTED";
      break;
    case Connection.TRANSACTION_REPEATABLE_READ:
      isoldesc = "TRANSACTION_REPEATABLE_READ";
      break;
    case Connection.TRANSACTION_SERIALIZABLE:
      isoldesc = "TRANSACTION_SERIALIZABLE";
      break;
    default:
      isoldesc = "UNKNOWN";
    }

    beeLine.info(beeLine.loc("isolation-status", isoldesc));
    return true;
  }

  //开启和执行一个批处理命令集合----!batch
  public boolean batch(String line) {
    if (!(beeLine.assertConnection())) {
      return false;
    }
    if (beeLine.getBatch() == null) {//说明要开启一个批处理
      beeLine.setBatch(new LinkedList<String>());
      beeLine.info(beeLine.loc("batch-start"));
      return true;
    } else {//去运行一个批处理集合
      beeLine.info(beeLine.loc("running-batch"));
      try {
        beeLine.runBatch(beeLine.getBatch());
        return true;
      } catch (Exception e) {
        return beeLine.error(e);
      } finally {
        beeLine.setBatch(null);
      }
    }
  }
  //执行一个sql
  public boolean sql(String line) {
    return execute(line, false, false);
  }

  public boolean sql(String line, boolean entireLineAsCommand) {
    return execute(line, false, entireLineAsCommand);
  }

  //执行一个shell命令----!sh shell
  public boolean sh(String line) {
    if (line == null || line.length() == 0) {
      return false;
    }

    if (!line.startsWith("sh")) {
      return false;
    }

    line = line.substring("sh".length()).trim();

    // Support variable substitution. HIVE-6791.
    // line = new VariableSubstitution().substitute(new HiveConf(BeeLine.class), line.trim());

    try {
      ShellCmdExecutor executor = new ShellCmdExecutor(line, beeLine.getOutputStream(),
          beeLine.getErrorStream());
      int ret = executor.execute();
      if (ret != 0) {
        beeLine.output("Command failed with exit code = " + ret);
        return false;
      }
      return true;
    } catch (Exception e) {
      beeLine.error("Exception raised from Shell command " + e);
      beeLine.error(e);
      return false;
    }
  }

  //执行一个存储过程
  public boolean call(String line) {
    return execute(line, true, false);
  }

    /**
     * 执行一个sql 或者 存储过程
     * @param line
     * @param call true表示执行的是存储过程,false表示执行的是一个sql
     * @param entireLineAsCommand
     * @return
     */
  private boolean execute(String line, boolean call, boolean entireLineAsCommand) {
    if (line == null || line.length() == 0) {
      return false; // ???
    }

    // ### FIXME: doing the multi-line handling down here means
    // higher-level logic never sees the extra lines. So,
    // for example, if a script is being saved, it won't include
    // the continuation lines! This is logged as sf.net
    // bug 879518.

    // use multiple lines for statements not terminated by ";"
    try {
      //When using -e, console reader is not initialized and command is a single line
      while (beeLine.getConsoleReader() != null && !(line.trim().endsWith(";"))
        && beeLine.getOpts().isAllowMultiLineCommand()) {

        if (!beeLine.getOpts().isSilent()) {
          StringBuilder prompt = new StringBuilder(beeLine.getPrompt());
          for (int i = 0; i < prompt.length() - 1; i++) {
            if (prompt.charAt(i) != '>') {
              prompt.setCharAt(i, i % 2 == 0 ? '.' : ' ');
            }
          }
        }

        String extra = null;
        if (beeLine.getOpts().isSilent() && beeLine.getOpts().getScriptFile() != null) {//读取脚本文件
          extra = beeLine.getConsoleReader().readLine(null, jline.console.ConsoleReader.NULL_MASK);
        } else {//读取提示文件
          extra = beeLine.getConsoleReader().readLine(beeLine.getPrompt());
        }

        if (extra == null) { //it happens when using -f and the line of cmds does not end with ;
          break;
        }
        if (!beeLine.isComment(extra)) {
          line += "\n" + extra;
        }
      }
    } catch (Exception e) {
      beeLine.handleException(e);
    }

    if (!(beeLine.assertConnection())) {
      return false;
    }

    line = line.trim();
    String[] cmds;
    if (entireLineAsCommand) {
      cmds = new String[1];
      cmds[0] = line;
    } else {
      cmds = line.split(";");
    }
    for (int i = 0; i < cmds.length; i++) {
      String sql = cmds[i].trim();
      if (sql.length() != 0) {
        if (beeLine.isComment(sql)) {
          //skip this and rest cmds in the line
          break;
        }
        if (sql.startsWith(BeeLine.COMMAND_PREFIX)) {
          sql = sql.substring(1);
        }

        String prefix = call ? "call" : "sql";

        if (sql.startsWith(prefix)) {
          sql = sql.substring(prefix.length());
        }

        // batch statements?
        if (beeLine.getBatch() != null) {
          beeLine.getBatch().add(sql);
          continue;
        }

        try {
          Statement stmnt = null;
          boolean hasResults;
          Thread logThread = null;

          try {
            long start = System.currentTimeMillis();

            if (call) {
              stmnt = beeLine.getDatabaseConnection().getConnection().prepareCall(sql);
              hasResults = ((CallableStatement) stmnt).execute();
            } else {
              stmnt = beeLine.createStatement();
              if (beeLine.getOpts().isSilent()) {
                hasResults = stmnt.execute(sql);
              } else {
                logThread = new Thread(createLogRunnable(stmnt));
                logThread.setDaemon(true);
                logThread.start();
                hasResults = stmnt.execute(sql);
                logThread.interrupt();
                logThread.join(DEFAULT_QUERY_PROGRESS_THREAD_TIMEOUT);
              }
            }

            beeLine.showWarnings();

            if (hasResults) {
              do {
                ResultSet rs = stmnt.getResultSet();
                try {
                  int count = beeLine.print(rs);//打印结果集
                  long end = System.currentTimeMillis();

                  beeLine.info(beeLine.loc("rows-selected", count) + " "
                      + beeLine.locElapsedTime(end - start));
                } finally {
                  if (logThread != null) {
                    logThread.join(DEFAULT_QUERY_PROGRESS_THREAD_TIMEOUT);
                    showRemainingLogsIfAny(stmnt);
                    logThread = null;
                  }
                  rs.close();
                }
              } while (BeeLine.getMoreResults(stmnt));
            } else {
              int count = stmnt.getUpdateCount();
              long end = System.currentTimeMillis();
              beeLine.info(beeLine.loc("rows-affected", count)
                  + " " + beeLine.locElapsedTime(end - start));
            }
          } finally {
            if (logThread != null) {
              if (!logThread.isInterrupted()) {
                logThread.interrupt();
              }
              logThread.join(DEFAULT_QUERY_PROGRESS_THREAD_TIMEOUT);
              showRemainingLogsIfAny(stmnt);
            }
            if (stmnt != null) {
              stmnt.close();
            }
          }
        } catch (Exception e) {
          return beeLine.error(e);
        }
        beeLine.showWarnings();
      }
    }
    return true;
  }

  private Runnable createLogRunnable(Statement statement) {
    if (statement instanceof HiveStatement) {
      final HiveStatement hiveStatement = (HiveStatement) statement;

      Runnable runnable = new Runnable() {
        @Override
        public void run() {
          while (hiveStatement.hasMoreLogs()) {
            try {
              // fetch the log periodically and output to beeline console
              for (String log : hiveStatement.getQueryLog()) {
                beeLine.info(log);
              }
              Thread.sleep(DEFAULT_QUERY_PROGRESS_INTERVAL);
            } catch (SQLException e) {
              beeLine.error(new SQLWarning(e));
              return;
            } catch (InterruptedException e) {
              beeLine.debug("Getting log thread is interrupted, since query is done!");
              showRemainingLogsIfAny(hiveStatement);
              return;
            }
          }
        }
      };
      return runnable;
    } else {
      beeLine.debug("The statement instance is not HiveStatement type: " + statement.getClass());
      return new Runnable() {
        @Override
        public void run() {
          // do nothing.
        }
      };
    }
  }

  private void showRemainingLogsIfAny(Statement statement) {
    if (statement instanceof HiveStatement) {
      HiveStatement hiveStatement = (HiveStatement) statement;
      List<String> logs;
      do {
        try {
          logs = hiveStatement.getQueryLog();
        } catch (SQLException e) {
          beeLine.error(new SQLWarning(e));
          return;
        }
        for (String log : logs) {
          beeLine.info(log);
        }
      } while (logs.size() > 0);
    } else {
      beeLine.debug("The statement instance is not HiveStatement type: " + statement.getClass());
    }
  }

  //关闭当前数据库,并且设置退出beeline客户端---!quit
  public boolean quit(String line) {
    beeLine.setExit(true);
    close(null);
    return true;
  }


  /**
   * Close all connections.
   * 关闭所有的数据库 ----!closeall
   */
  public boolean closeall(String line) {
    if (close(null)) {
      while (close(null)) {
        ;
      }
      return true;
    }
    return false;
  }


  /**
   * Close the current connection.
   * 关闭当前数据库连接   !close
   */
  public boolean close(String line) {
    if (beeLine.getDatabaseConnection() == null) {
      return false;
    }
    try {
      if (beeLine.getDatabaseConnection().getConnection() != null
          && !(beeLine.getDatabaseConnection().getConnection().isClosed())) {
        int index = beeLine.getDatabaseConnections().getIndex();
        beeLine.info(beeLine.loc("closing", index, beeLine.getDatabaseConnection()));//打印说正在关闭哪个序号的连接
        beeLine.getDatabaseConnection().getConnection().close();
      } else {
        beeLine.info(beeLine.loc("already-closed"));
      }
    } catch (Exception e) {
      return beeLine.error(e);
    }
    beeLine.getDatabaseConnections().remove();//切换数据库
    return true;
  }


  /**
   * Connect to the database defined in the specified properties file.
   * 连接所有的数据库,每一个数据库对应一个配置文件,多个数据源使用空格,分割多个配置文件---!properties filePath1 filePath2
   */
  public boolean properties(String line) throws Exception {
    String example = "";
    example += "Usage: properties <properties file>" + BeeLine.getSeparator();

    String[] parts = beeLine.split(line);
    if (parts.length < 2) {
      return beeLine.error(example);//打印错误信息提示
    }

    int successes = 0;

    for (int i = 1; i < parts.length; i++) {//循环所有的配置文件
      Properties props = new Properties();
      InputStream stream = new FileInputStream(parts[i]);
      try {
        props.load(stream);
      } finally {
        IOUtils.closeStream(stream);
      }
      if (connect(props)) {//添加设置一个连接
        successes++;
      }
    }

    if (successes != (parts.length - 1)) {//确保所有的配置文件连接都成功
      return false;
    } else {
      return true;
    }
  }

  //连接一个数据库  ----!connect <url> <username> <password> [driver]
  public boolean connect(String line) throws Exception {
    String example = "Usage: connect <url> <username> <password> [driver]"
        + BeeLine.getSeparator();

    String[] parts = beeLine.split(line);
    if (parts == null) {
      return false;
    }

    if (parts.length < 2) {
      return beeLine.error(example);
    }

    String url = parts.length < 2 ? null : parts[1];
    String user = parts.length < 3 ? null : parts[2];
    String pass = parts.length < 4 ? null : parts[3];
    String driver = parts.length < 5 ? null : parts[4];

    Properties props = new Properties();
    if (url != null) {
      props.setProperty("url", url);
    }
    if (driver != null) {
      props.setProperty("driver", driver);
    }
    if (user != null) {
      props.setProperty("user", user);
    }
    if (pass != null) {
      props.setProperty("password", pass);
    }

    return connect(props);
  }

  //从props中读取key集合的信息,读取到第一个有存在value的则截至,返回该value
  private String getProperty(Properties props, String[] keys) {
    for (int i = 0; i < keys.length; i++) {
      String val = props.getProperty(keys[i]);
      if (val != null) {
        return val;
      }
    }

    for (Iterator i = props.keySet().iterator(); i.hasNext();) {//说明没有找到该key
      String key = (String) i.next();
      for (int j = 0; j < keys.length; j++) {
        if (key.endsWith(keys[j])) {//获取以key结尾的信息,此处用处应该不大,因为容易出现问题
          return props.getProperty(key);
        }
      }
    }

    return null;
  }


  public boolean connect(Properties props) throws IOException {
    String url = getProperty(props, new String[] {
        "url",
        "javax.jdo.option.ConnectionURL",
        "ConnectionURL",
    });
    String driver = getProperty(props, new String[] {
        "driver",
        "javax.jdo.option.ConnectionDriverName",
        "ConnectionDriverName",
    });
    String username = getProperty(props, new String[] {
        "user",
        "javax.jdo.option.ConnectionUserName",
        "ConnectionUserName",
    });
    String password = getProperty(props, new String[] {
        "password",
        "javax.jdo.option.ConnectionPassword",
        "ConnectionPassword",
    });
    String auth = getProperty(props, new String[] {"auth"});

    if (url == null || url.length() == 0) {
      return beeLine.error("Property \"url\" is required");
    }
    if (driver == null || driver.length() == 0) {
      if (!beeLine.scanForDriver(url)) {
        return beeLine.error(beeLine.loc("no-driver", url));
      }
    }

    beeLine.info("Connecting to " + url);

    if (username == null) {
      username = beeLine.getConsoleReader().readLine("Enter username for " + url + ": ");
    }
    props.setProperty("user", username);
    if (password == null) {
      password = beeLine.getConsoleReader().readLine("Enter password for " + url + ": ",
          new Character('*'));
    }
    props.setProperty("password", password);

    if (auth == null) {
      auth = beeLine.getOpts().getAuthType();
    }
    if (auth != null) {
      props.setProperty("auth", auth);
    }

    try {
      beeLine.getDatabaseConnections().setConnection(
          new DatabaseConnection(beeLine, driver, url, props));//添加一个连接
      beeLine.getDatabaseConnection().getConnection();
      beeLine.runInit();//每次切换一个数据库连接的时候,都进行初始化该文件的命令

      beeLine.setCompletions();//设置自动补全功能
      return true;
    } catch (SQLException sqle) {
      return beeLine.error(sqle);
    } catch (IOException ioe) {
      return beeLine.error(ioe);
    }
  }

  //重新刷新自动补全信息---!rehash
  public boolean rehash(String line) {
    try {
      if (!(beeLine.assertConnection())) {
        return false;
      }
      if (beeLine.getDatabaseConnection() != null) {
        beeLine.getDatabaseConnection().setCompletions(false);
      }
      return true;
    } catch (Exception e) {
      return beeLine.error(e);
    }
  }


  /**
   * List the current connections
   * 打印目前所有配置的数据库连接信息----!list
   * 该命令配合go命令使用
   */
  public boolean list(String line) {
    int index = 0;//配合go语法使用
    beeLine.info(beeLine.loc("active-connections", beeLine.getDatabaseConnections().size()));

    for (Iterator<DatabaseConnection> i = beeLine.getDatabaseConnections().iterator(); i.hasNext(); index++) {//循环所有的数据库连接
      DatabaseConnection c = i.next();
      boolean closed = false;
      try {
        closed = c.getConnection().isClosed();//判断该连接是否断开
      } catch (Exception e) {
        closed = true;
      }

      beeLine.output(beeLine.getColorBuffer().pad(" #" + index + "", 5)
          .pad(closed ? beeLine.loc("closed") : beeLine.loc("open"), 9)
          .append(c.getUrl()));//根据是否断开,打印所有连接信息
    }

    return true;
  }

  //true表示都执行成功,一旦一个失败都是返回false
  //对所有数据库都执行all 后面的内容
  //!all sql
  public boolean all(String line) {
    int index = beeLine.getDatabaseConnections().getIndex();//先获取当前数据库
    boolean success = true;

    for (int i = 0; i < beeLine.getDatabaseConnections().size(); i++) {//循环每一个数据库
      beeLine.getDatabaseConnections().setIndex(i);//不断切换数据库
      beeLine.output(beeLine.loc("executing-con", beeLine.getDatabaseConnection()));
      // ### FIXME: this is broken for multi-line SQL
      success = sql(line.substring("all ".length())) && success;//执行一个sql
    }

    // restore index
    beeLine.getDatabaseConnections().setIndex(index);//恢复当前数据库
    return success;
  }

  //切换第几个数据库,跟list命令一起使用 ----!go Integer
  public boolean go(String line) {
    String[] parts = beeLine.split(line, 2, "Usage: go <connection index>");
    if (parts == null) {
      return false;
    }
    int index = Integer.parseInt(parts[1]);
    if (!(beeLine.getDatabaseConnections().setIndex(index))) {
      beeLine.error(beeLine.loc("invalid-connection", "" + index));
      list(""); // list the current connections
      return false;
    }
    return true;
  }


  /**
   * Save or stop saving a script to a file
   * 开启和关闭一个脚本文件,在开启期间可以添加脚本内容----开启!script filePath,关闭!script
   */
  public boolean script(String line) {
    if (beeLine.getScriptOutputFile() == null) {
      return startScript(line);
    } else {
      return stopScript(line);
    }
  }


  /**
   * Stop writing to the script file and close the script.
   */
  private boolean stopScript(String line) {
    try {
      beeLine.getScriptOutputFile().close();//关闭脚本文件
    } catch (Exception e)
    {
      beeLine.handleException(e);
    }

    beeLine.output(beeLine.loc("script-closed", beeLine.getScriptOutputFile()));
    beeLine.setScriptOutputFile(null);
    return true;
  }


  /**
   * Start writing to the specified script file.
   */
  private boolean startScript(String line) {
    if (beeLine.getScriptOutputFile() != null) {
      return beeLine.error(beeLine.loc("script-already-running", beeLine.getScriptOutputFile()));
    }

    String[] parts = beeLine.split(line, 2, "Usage: script <filename>");
    if (parts == null) {
      return false;
    }

    try {
      beeLine.setScriptOutputFile(new OutputFile(parts[1]));//打开一个脚本文件
      beeLine.output(beeLine.loc("script-started", beeLine.getScriptOutputFile()));
      return true;
    } catch (Exception e) {
      return beeLine.error(e);
    }
  }


  /**
   * Run a script from the specified file.
   * 执行一个脚本文件---脚本中存放了所有的命令集合,用回车换行表示一个单独的命令
   * 第二个位置是文件路径
   * !run filePath
   */
  public boolean run(String line) {
    String[] parts = beeLine.split(line, 2, "Usage: run <scriptfile>");//必须一个命令+一个参数
    if (parts == null) {
      return false;
    }

    List<String> cmds = new LinkedList<String>();//收集的总命令集合

    try {
      BufferedReader reader = new BufferedReader(new FileReader(
          parts[1]));//读取文件
      try {
        // ### NOTE: fix for sf.net bug 879427
        StringBuilder cmd = null;//一个单独的命令
        for (;;) {
          String scriptLine = reader.readLine();

          if (scriptLine == null) {
            break;
          }

          String trimmedLine = scriptLine.trim();
          if (beeLine.getOpts().getTrimScripts()) {//命令要进行trim处理
            scriptLine = trimmedLine;
          }

          if (cmd != null) {//说明是第二行的命令
            // we're continuing an existing command
            cmd.append(" \n");//加入一个回车
            cmd.append(scriptLine);
            if (trimmedLine.endsWith(";")) {//如果命令是以;结尾,说明多行命令结束
              // this command has terminated
              cmds.add(cmd.toString());//添加到命令集合中
              cmd = null;
            }
          } else {
            // we're starting a new command
            if (beeLine.needsContinuation(scriptLine)) {//是否需要继续,即多行命令
              // multi-line
              cmd = new StringBuilder(scriptLine);
            } else {//说明是单行命令,加入到命令集合中
              // single-line
              cmds.add(scriptLine);
            }
          }
        }

        if (cmd != null) {//处理最后一个命令
          // ### REVIEW: oops, somebody left the last command
          // unterminated; should we fix it for them or complain?
          // For now be nice and fix it.
          cmd.append(";");//将命令自动加上;,因为我们正常执行最后一个命令的时候都不加入;
          cmds.add(cmd.toString());//加入到命令集合
        }
      } finally {
        reader.close();
      }

      // success only if all the commands were successful
      return beeLine.runCommands(cmds) == cmds.size();//true表示全部命令都执行成功
    } catch (Exception e) {
      return beeLine.error(e);
    }
  }


  /**
   * Save or stop saving all output to a file.
   * 开启和关闭一个记录输出内容的文件---开启 !record filePath 关闭 !record
   */
  public boolean record(String line) {
    if (beeLine.getRecordOutputFile() == null) {
      return startRecording(line);
    } else {
      return stopRecording(line);
    }
  }


  /**
   * Stop writing output to the record file.
   */
  private boolean stopRecording(String line) {
    try {
      beeLine.getRecordOutputFile().close();
    } catch (Exception e) {
      beeLine.handleException(e);
    }
    beeLine.setRecordOutputFile(null);
    beeLine.output(beeLine.loc("record-closed", beeLine.getRecordOutputFile()));
    return true;
  }


  /**
   * Start writing to the specified record file.
   */
  private boolean startRecording(String line) {
    if (beeLine.getRecordOutputFile() != null) {
      return beeLine.error(beeLine.loc("record-already-running", beeLine.getRecordOutputFile()));
    }

    String[] parts = beeLine.split(line, 2, "Usage: record <filename>");
    if (parts == null) {
      return false;
    }

    try {
      OutputFile recordOutput = new OutputFile(parts[1]);
      beeLine.output(beeLine.loc("record-started", recordOutput));
      beeLine.setRecordOutputFile(recordOutput);
      return true;
    } catch (Exception e) {
      return beeLine.error(e);
    }
  }


    /**
     * !describe tables  或者!describe tableName(具体表名)
     * 获取一个表的详细描述内容 或者所有表的内容
     */
  public boolean describe(String line) throws SQLException {
    String[] table = beeLine.split(line, 2, "Usage: describe <table name>");//按照空格可以拆分成两部分
    if (table == null) {
      return false;
    }

    ResultSet rs;

    if (table[1].equals("tables")) {
      rs = beeLine.getTables();
    } else {
      rs = beeLine.getColumns(table[1]);
    }

    if (rs == null) {
      return false;
    }

    beeLine.print(rs);
    rs.close();
    return true;
  }

  //打印配置的国际化信息
  public boolean help(String line) {
    String[] parts = beeLine.split(line);
    String cmd = parts.length > 1 ? parts[1] : "";
    int count = 0;
    TreeSet<ColorBuffer> clist = new TreeSet<ColorBuffer>();

    for (int i = 0; i < beeLine.commandHandlers.length; i++) {
      if (cmd.length() == 0 ||
          Arrays.asList(beeLine.commandHandlers[i].getNames()).contains(cmd)) {
        clist.add(beeLine.getColorBuffer().pad("!" + beeLine.commandHandlers[i].getName(), 20)
            .append(beeLine.wrap(beeLine.commandHandlers[i].getHelpText(), 60, 20)));
      }
    }

    for (Iterator<ColorBuffer> i = clist.iterator(); i.hasNext();) {
      beeLine.output(i.next());
    }

    if (cmd.length() == 0) {
      beeLine.output("");
      beeLine.output(beeLine.loc("comments", beeLine.getApplicationContactInformation()));
    }

    return true;
  }

  //manual命令读取manual.txt说明文件
  public boolean manual(String line) throws IOException {
    InputStream in = BeeLine.class.getResourceAsStream("manual.txt");
    if (in == null) {
      return beeLine.error(beeLine.loc("no-manual"));
    }

    BufferedReader breader = new BufferedReader(
        new InputStreamReader(in));
    String man;
    int index = 0;
    while ((man = breader.readLine()) != null) {
      index++;
      beeLine.output(man);

      // silly little pager
      if (index % (beeLine.getOpts().getMaxHeight() - 1) == 0) {
        String ret = beeLine.getConsoleReader().readLine(beeLine.loc("enter-for-more"));
        if (ret != null && ret.startsWith("q")) {
          break;
        }
      }
    }
    breader.close();
    return true;
  }
}
