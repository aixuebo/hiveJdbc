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

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import jline.console.completer.ArgumentCompleter;
import jline.console.completer.Completer;

//表示一个数据库的连接---采用JDBC的方式连接数据库,因此利用的也是hive的jdbc方式,因此beeline就是hive的jdbc的一个客户端
class DatabaseConnection {
  private static final String HIVE_AUTH_USER = "user";
  private static final String HIVE_AUTH_PASSWD = "password";
  private static final String HIVE_VAR_PREFIX = "hivevar:";
  private static final String HIVE_CONF_PREFIX = "hiveconf:";

  private final BeeLine beeLine;

  //以下是建立的连接以及数据库元数据对象信息
  private Connection connection;
  private DatabaseMetaData meta;

  //以下是请求信息
  private final String driver;
  private final String url;
  private final Properties info;//保存user和密码的信息


  private Schema schema = null;
  private Completer sqlCompleter = null;

  public boolean isClosed() {
    return (null == connection);
  }

  public DatabaseConnection(BeeLine beeLine, String driver, String url,
       Properties info) throws SQLException {
    this.beeLine = beeLine;
    this.driver = driver;
    this.url = url;
    this.info = info;
  }

  @Override
  public String toString() {
    return getUrl() + "";
  }

  //设置自动补全
  void setCompletions(boolean skipmeta) throws SQLException, IOException {
    final String extraNameCharacters =
        getDatabaseMetaData() == null || getDatabaseMetaData().getExtraNameCharacters() == null ? ""
            : getDatabaseMetaData().getExtraNameCharacters();

    // setup the completer for the database
    //设置sql提供的补全集合
    sqlCompleter = new ArgumentCompleter(
        new ArgumentCompleter.AbstractArgumentDelimiter() {
          // delimiters for SQL statements are any
          // non-letter-or-number characters, except
          // underscore and characters that are specified
          // by the database to be valid name identifiers.
          @Override
          public boolean isDelimiterChar(CharSequence buffer, int pos) {
            char c = buffer.charAt(pos);
            if (Character.isWhitespace(c)) {//空格是可以拆分的字符
              return true;
            }
              //不是数字、正常字母、_、不再extraNameCharacters里面的字符都认为是拆分符号
            return !(Character.isLetterOrDigit(c))
                && c != '_'
                && extraNameCharacters.indexOf(c) == -1;
          }
        },
        new SQLCompleter(SQLCompleter.getSQLCompleters(beeLine, skipmeta)));
    // not all argument elements need to hold true
    ((ArgumentCompleter) sqlCompleter).setStrict(false);
  }


  /**
   * Connection to the specified data source.
   * 真的创建连接
   */
  boolean connect() throws SQLException {
    try {
      if (driver != null && driver.length() != 0) {
        Class.forName(driver);
      }
    } catch (ClassNotFoundException cnfe) {
      return beeLine.error(cnfe);
    }

    boolean isDriverRegistered = false;
    try {
      isDriverRegistered = DriverManager.getDriver(getUrl()) != null;
    } catch (Exception e) {
    }

    try {
      close();
    } catch (Exception e) {
      return beeLine.error(e);
    }

    //设置上下文环境信息
    Map<String, String> hiveVars = beeLine.getOpts().getHiveVariables();
    for (Map.Entry<String, String> var : hiveVars.entrySet()) {
      info.put(HIVE_VAR_PREFIX + var.getKey(), var.getValue());
    }

    Map<String, String> hiveConfVars = beeLine.getOpts().getHiveConfVariables();
    for (Map.Entry<String, String> var : hiveConfVars.entrySet()) {
      info.put(HIVE_CONF_PREFIX + var.getKey(), var.getValue());
    }

    if (isDriverRegistered) {
      // if the driver registered in the driver manager, get the connection via the driver manager
      setConnection(DriverManager.getConnection(getUrl(), info));
    } else {
      beeLine.debug("Use the driver from local added jar file.");
      setConnection(getConnectionFromLocalDriver(getUrl(), info));
    }
    setDatabaseMetaData(getConnection().getMetaData());

    try {
      beeLine.info(beeLine.loc("connected", new Object[] {
          getDatabaseMetaData().getDatabaseProductName(),
          getDatabaseMetaData().getDatabaseProductVersion()}));
    } catch (Exception e) {
      beeLine.handleException(e);
    }

    try {
      beeLine.info(beeLine.loc("driver", new Object[] {
          getDatabaseMetaData().getDriverName(),
          getDatabaseMetaData().getDriverVersion()}));
    } catch (Exception e) {
      beeLine.handleException(e);
    }

    //设置是否自动提交
    try {
      getConnection().setAutoCommit(beeLine.getOpts().getAutoCommit());
      // TODO: Setting autocommit should not generate an exception as long as it is set to false
      // beeLine.autocommitStatus(getConnection());
    } catch (Exception e) {
      beeLine.handleException(e);
    }

    //设置隔离级别
    try {
      beeLine.getCommands().isolation("isolation: " + beeLine.getOpts().getIsolation());
    } catch (Exception e) {
      beeLine.handleException(e);
    }

    return true;
  }

  public Connection getConnectionFromLocalDriver(String url, Properties properties) {
    Collection<Driver> drivers = beeLine.getDrivers();
    for (Driver d : drivers) {
      try {
        if (d.acceptsURL(url) && beeLine.isSupportedLocalDriver(d)) {
          String clazzName = d.getClass().getName();
          beeLine.debug("Driver name is " + clazzName);
          Driver driver =
            (Driver) Class.forName(clazzName, true, Thread.currentThread().getContextClassLoader())
              .newInstance();
          return driver.connect(url, properties);
        }
      } catch (Exception e) {
        beeLine.error("Fail to connect with a local driver due to the exception:" + e);
        beeLine.error(e);
      }
    }
    return null;
  }


  public Connection getConnection() throws SQLException {
    if (connection != null) {
      return connection;
    }
    connect();
    return connection;
  }


  public void reconnect() throws Exception {
    close();
    getConnection();
  }


  public void close() {
    try {
      try {
        if (connection != null && !connection.isClosed()) {
          beeLine.output(beeLine.loc("closing", connection));//向客户端输出closing对应的信息
          connection.close();
        }
      } catch (Exception e) {
        beeLine.handleException(e);
      }
    } finally {
      setConnection(null);
      setDatabaseMetaData(null);
    }
  }

  //返回所有数据库表集合
  public String[] getTableNames(boolean force) {
    Schema.Table[] t = getSchema().getTables();
    Set<String> names = new TreeSet<String>();
    for (int i = 0; t != null && i < t.length; i++) {
      names.add(t[i].getName());
    }
    return names.toArray(new String[names.size()]);
  }

  Schema getSchema() {
    if (schema == null) {
      schema = new Schema();
    }
    return schema;
  }

  void setConnection(Connection connection) {
    this.connection = connection;
  }

  DatabaseMetaData getDatabaseMetaData() {
    return meta;
  }

  void setDatabaseMetaData(DatabaseMetaData meta) {
    this.meta = meta;
  }

  String getUrl() {
    return url;
  }

  Completer getSQLCompleter() {
    return sqlCompleter;
  }

  class Schema {//表示一个数据库
    private Table[] tables = null;//该数据库下所有的表---相当于使用缓存层

    Table[] getTables() {
      if (tables != null) {
        return tables;
      }

      List<Table> tnames = new LinkedList<Table>();

      try {
        ResultSet rs = getDatabaseMetaData().getTables(getConnection().getCatalog(),
            null, "%", new String[] {"TABLE"});//通过元数据读取信息
        try {
          while (rs.next()) {
            tnames.add(new Table(rs.getString("TABLE_NAME")));
          }
        } finally {
          try {
            rs.close();
          } catch (Exception e) {
          }
        }
      } catch (Throwable t) {
      }
      return tables = tnames.toArray(new Table[0]);
    }

    Table getTable(String name) {
      Table[] t = getTables();
      for (int i = 0; t != null && i < t.length; i++) {
        if (name.equalsIgnoreCase(t[i].getName())) {
          return t[i];
        }
      }
      return null;
    }

    class Table {//表示一个表
      final String name;
      Column[] columns;

      public Table(String name) {
        this.name = name;
      }


      public String getName() {
        return name;
      }

      class Column {
        final String name;
        boolean isPrimaryKey;//是否是主键

        public Column(String name) {
          this.name = name;
        }
      }
    }
  }
}
