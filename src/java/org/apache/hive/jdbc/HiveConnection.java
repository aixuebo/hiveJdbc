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

package org.apache.hive.jdbc;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.Executor;

import javax.security.sasl.Sasl;
import javax.security.sasl.SaslException;

import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.ql.session.SessionState;
import org.apache.hive.service.auth.KerberosSaslHelper;
import org.apache.hive.service.auth.PlainSaslHelper;
import org.apache.hive.service.auth.SaslQOP;
import org.apache.hive.service.cli.thrift.EmbeddedThriftBinaryCLIService;
import org.apache.hive.service.cli.thrift.TCLIService;
import org.apache.hive.service.cli.thrift.TCloseSessionReq;
import org.apache.hive.service.cli.thrift.TOpenSessionReq;
import org.apache.hive.service.cli.thrift.TOpenSessionResp;
import org.apache.hive.service.cli.thrift.TProtocolVersion;
import org.apache.hive.service.cli.thrift.TSessionHandle;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

/**
 * HiveConnection.
 * hive的连接原理,产生一个thrift客户端,通过客户端请求远程的server服务
 *
 * 关于JDBC的支持
 * 回滚等操作hive是不支持的
 * 预编译sql支持,产生预编译class参与执行
 *
 * 一个该对象表示一个连接,持有一个客户端对象和一个session对象保持该连接的上下文信息
 */
public class HiveConnection implements java.sql.Connection {
  private static final String HIVE_AUTH_TYPE= "auth";//制定这个参数,说明二进制socket传输的时候需要用户名和密码
  private static final String HIVE_AUTH_QOP = "sasl.qop";
  private static final String HIVE_AUTH_SIMPLE = "noSasl";

  private static final String HIVE_AUTH_USER = "user";//数据库连接用户名的key
  private static final String HIVE_AUTH_PRINCIPAL = "principal";
  private static final String HIVE_AUTH_PASSWD = "password";//数据库连接密码的key

  //匿名用户的用户名和密码
  private static final String HIVE_ANONYMOUS_USER = "anonymous";
  private static final String HIVE_ANONYMOUS_PASSWD = "anonymous";

  private final String jdbcURI;//配置的jdbc的url
  private final String host;//数据库连接host
  private final int port;//数据库连接端口
  private final Map<String, String> sessConfMap;//数据库?号钱参数
  private final Map<String, String> hiveConfMap;//数据库?号后参数
  private final Map<String, String> hiveVarMap;//数据库#号后参数
  private final boolean isEmbeddedMode;//是否使用内置的数据库

  private TTransport transport;//thrift栓出的模式是http传输还是序列化后的二进制传输----http传输时候使用httpclient进行发送数据
  private TCLIService.Iface client;//thrift客户端
  private boolean isClosed = true;//连接是否关闭
  private SQLWarning warningChain = null;
  private TSessionHandle sessHandle = null;//获取一个请求的session,因为每一个请求都有上下文,因此就是session
  private final List<TProtocolVersion> supportedProtocols = new LinkedList<TProtocolVersion>();//支持的协议版本

  //创建和解析数据库连接串
  public HiveConnection(String uri, Properties info) throws SQLException {
    jdbcURI = uri;
    // parse the connection uri 解析url配置参数
    Utils.JdbcConnectionParams connParams = Utils.parseURL(jdbcURI);
    // extract parsed connection parameters:
    // JDBC URL: jdbc:hive2://<host>:<port>/dbName;sess_var_list?hive_conf_list#hive_var_list
    // each list: <key1>=<val1>;<key2>=<val2> and so on
    // sess_var_list -> sessConfMap
    // hive_conf_list -> hiveConfMap
    // hive_var_list -> hiveVarMap
    host = connParams.getHost();
    port = connParams.getPort();
    sessConfMap = connParams.getSessionVars();
    hiveConfMap = connParams.getHiveConfs();
    hiveVarMap = connParams.getHiveVars();
    isEmbeddedMode = connParams.isEmbeddedMode();

    if (isEmbeddedMode) {
      client = new EmbeddedThriftBinaryCLIService();
    } else {
      // extract user/password from JDBC connection properties if its not supplied in the connection URL
        //设置用户名和密码.info中的信息优先级比url中的优先级大
      if (info.containsKey(HIVE_AUTH_USER)) {
        sessConfMap.put(HIVE_AUTH_USER, info.getProperty(HIVE_AUTH_USER));
        if (info.containsKey(HIVE_AUTH_PASSWD)) {
          sessConfMap.put(HIVE_AUTH_PASSWD, info.getProperty(HIVE_AUTH_PASSWD));
        }
      }
      // open the client transport
      //创建客户端以及与服务端的协议是http协议还是二进制的流协议
      openTransport();
    }

    // add supported protocols
    supportedProtocols.add(TProtocolVersion.HIVE_CLI_SERVICE_PROTOCOL_V1);
    supportedProtocols.add(TProtocolVersion.HIVE_CLI_SERVICE_PROTOCOL_V2);
    supportedProtocols.add(TProtocolVersion.HIVE_CLI_SERVICE_PROTOCOL_V3);

    // open client session
    //获取一个请求的session,因为每一个请求都有上下文,因此就是session
    openSession();

    //配置hive执行前的配置信息
    configureConnection();
  }

  private void openTransport() throws SQLException {
    //获取传输模式对象
    transport = isHttpTransportMode() ? //是否支持http或者https传输模式
        createHttpTransport() :
          createBinaryTransport();
    //根据传输协议创建客户端
    TProtocol protocol = new TBinaryProtocol(transport);
    client = new TCLIService.Client(protocol);
    try {
      transport.open();//打开传输协议
    } catch (TTransportException e) {
      throw new SQLException("Could not open connection to "
          + jdbcURI + ": " + e.getMessage(), " 08S01", e);
    }
  }

  //创建HTTP传输方式
  private TTransport createHttpTransport() throws SQLException {
    // http path should begin with "/"
    String httpPath;
    httpPath = hiveConfMap.get(HiveConf.ConfVars.HIVE_SERVER2_THRIFT_HTTP_PATH.varname);//http传输中的路径,比如http://host:port/index.html,该配置内容是/index.htm  ,因为必须以为/开头
    if(httpPath == null) {
      httpPath = "/";
    }
    if(!httpPath.startsWith("/")) {
      httpPath = "/" + httpPath;
    }

    DefaultHttpClient httpClient = new DefaultHttpClient();//httpclent对象
    String httpUrl = hiveConfMap.get(HiveConf.ConfVars.HIVE_SERVER2_TRANSPORT_MODE.varname) + //获取传输协议是http还是https
        "://" + host + ":" + port + httpPath;//获取最终交流的url
    httpClient.addRequestInterceptor(
        new HttpBasicAuthInterceptor(getUserName(), getPasswd())//设置用户名和密码
        );
    try {
      transport = new THttpClient(httpUrl, httpClient);
    }
    catch (TTransportException e) {
      String msg =  "Could not create http connection to " +
          jdbcURI + ". " + e.getMessage();
      throw new SQLException(msg, " 08S01", e);
    }
    return transport;
  }

  //创建序列化后的二进制传输方式---采用socket传输
  private TTransport createBinaryTransport() throws SQLException {
    transport = new TSocket(host, port);
    // handle secure connection if specified
    if (!sessConfMap.containsKey(HIVE_AUTH_TYPE)
        || !sessConfMap.get(HIVE_AUTH_TYPE).equals(HIVE_AUTH_SIMPLE)) {
      try {
        // If Kerberos
        if (sessConfMap.containsKey(HIVE_AUTH_PRINCIPAL)) {
          Map<String, String> saslProps = new HashMap<String, String>();
          SaslQOP saslQOP = SaslQOP.AUTH;
          if(sessConfMap.containsKey(HIVE_AUTH_QOP)) {
            try {
              saslQOP = SaslQOP.fromString(sessConfMap.get(HIVE_AUTH_QOP));
            } catch (IllegalArgumentException e) {
              throw new SQLException("Invalid " + HIVE_AUTH_QOP + " parameter. " + e.getMessage(), "42000", e);
            }
          }
          saslProps.put(Sasl.QOP, saslQOP.toString());
          saslProps.put(Sasl.SERVER_AUTH, "true");
          transport = KerberosSaslHelper.getKerberosTransport(
              sessConfMap.get(HIVE_AUTH_PRINCIPAL), host, transport, saslProps);
        } else {
          String userName = sessConfMap.get(HIVE_AUTH_USER);
          if ((userName == null) || userName.isEmpty()) {
            userName = HIVE_ANONYMOUS_USER;
          }
          String passwd = sessConfMap.get(HIVE_AUTH_PASSWD);
          if ((passwd == null) || passwd.isEmpty()) {
            passwd = HIVE_ANONYMOUS_PASSWD;
          }
          transport = PlainSaslHelper.getPlainTransport(userName, passwd, transport);
        }
      } catch (SaslException e) {
        throw new SQLException("Could not create secure connection to "
            + jdbcURI + ": " + e.getMessage(), " 08S01", e);
      }
    }
    return transport;
  }

  //获取传输模式---是否支持http或者https传输模式
  private boolean isHttpTransportMode() {
    String transportMode =
        hiveConfMap.get(HiveConf.ConfVars.HIVE_SERVER2_TRANSPORT_MODE.varname);//thrift传输是http传输还是二进制传输   hive.server2.transport.mode
    if(transportMode != null && (transportMode.equalsIgnoreCase("http") ||
        transportMode.equalsIgnoreCase("https"))) {
      return true;
    }
    return false;
  }

    //获取一个请求的session,因为每一个请求都有上下文,因此就是session
  private void openSession() throws SQLException {
    TOpenSessionReq openReq = new TOpenSessionReq();

    // set the session configuration
    // openReq.setConfiguration(null);

    try {
      TOpenSessionResp openResp = client.OpenSession(openReq);//请求session,获取返回值

      // validate connection
      Utils.verifySuccess(openResp.getStatus());//客户端要去校验服务端返回的response的状态码是否合法
      if (!supportedProtocols.contains(openResp.getServerProtocolVersion())) {
        throw new TException("Unsupported Hive2 protocol");
      }
      sessHandle = openResp.getSessionHandle();//返回值中获取session对象,该session对象是服务器返回的
    } catch (TException e) {
      throw new SQLException("Could not establish connection to "
          + jdbcURI + ": " + e.getMessage(), " 08S01", e);
    }
    isClosed = false;
  }

  //配置hive执行前的配置信息
  private void configureConnection() throws SQLException {
    // set the hive variable in session state for local mode
    if (isEmbeddedMode) {
      if (!hiveVarMap.isEmpty()) {
        SessionState.get().setHiveVariables(hiveVarMap);
      }
    } else {
      // for remote JDBC client, try to set the conf var using 'set foo=bar'
      Statement stmt = createStatement();
      for (Entry<String, String> hiveConf : hiveConfMap.entrySet()) {//设置hive的环境信息,比如set hive.auto.convert.join=false;
        stmt.execute("set " + hiveConf.getKey() + "=" + hiveConf.getValue());
      }

      // For remote JDBC client, try to set the hive var using 'set hivevar:key=value'
      for (Entry<String, String> hiveVar : hiveVarMap.entrySet()) {//设置hive的sql中需要的变量,比如-hivevar today=2017-06-06
        stmt.execute("set hivevar:" + hiveVar.getKey() + "=" + hiveVar.getValue());
      }
      stmt.close();
    }
  }

  /**
   * @return username from sessConfMap
   */
  private String getUserName() {
    return getSessionValue(HIVE_AUTH_USER, HIVE_ANONYMOUS_USER);
  }

  /**
   * @return password from sessConfMap
   */
  private String getPasswd() {
    return getSessionValue(HIVE_AUTH_PASSWD, HIVE_ANONYMOUS_PASSWD);
  }

  /**
   * Lookup varName in sessConfMap, if its null or empty return the default
   * value varDefault
   * @param varName
   * @param varDefault
   * @return
   */
  private String getSessionValue(String varName, String varDefault) {
    String varValue = sessConfMap.get(varName);
    if ((varValue == null) || varValue.isEmpty()) {
      varValue = varDefault;
    }
    return varValue;
  }

  public void abort(Executor executor) throws SQLException {
    // JDK 1.7
    throw new SQLException("Method not supported");
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#clearWarnings()
   */

  public void clearWarnings() throws SQLException {
    warningChain = null;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#close()
   */

  public void close() throws SQLException {
    if (!isClosed) {
      TCloseSessionReq closeReq = new TCloseSessionReq(sessHandle);
      try {
        client.CloseSession(closeReq);
      } catch (TException e) {
        throw new SQLException("Error while cleaning up the server resources", e);
      } finally {
        isClosed = true;
        if (transport != null) {
          transport.close();
        }
      }
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#commit()
   */

  public void commit() throws SQLException {
    // TODO Auto-generated method stub
    throw new SQLException("Method not supported");
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#createArrayOf(java.lang.String,
   * java.lang.Object[])
   */

  public Array createArrayOf(String arg0, Object[] arg1) throws SQLException {
    // TODO Auto-generated method stub
    throw new SQLException("Method not supported");
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#createBlob()
   */

  public Blob createBlob() throws SQLException {
    // TODO Auto-generated method stub
    throw new SQLException("Method not supported");
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#createClob()
   */

  public Clob createClob() throws SQLException {
    // TODO Auto-generated method stub
    throw new SQLException("Method not supported");
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#createNClob()
   */

  public NClob createNClob() throws SQLException {
    // TODO Auto-generated method stub
    throw new SQLException("Method not supported");
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#createSQLXML()
   */

  public SQLXML createSQLXML() throws SQLException {
    // TODO Auto-generated method stub
    throw new SQLException("Method not supported");
  }

  /**
   * Creates a Statement object for sending SQL statements to the database.
   *
   * @throws SQLException
   *           if a database access error occurs.
   * @see java.sql.Connection#createStatement()
   */

  public Statement createStatement() throws SQLException {
    if (isClosed) {
      throw new SQLException("Can't create Statement, connection is closed");
    }
    return new HiveStatement(client, sessHandle);
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#createStatement(int, int)
   */

  public Statement createStatement(int resultSetType, int resultSetConcurrency)
      throws SQLException {
    // TODO Auto-generated method stub
    throw new SQLException("Method not supported");
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#createStatement(int, int, int)
   */

  public Statement createStatement(int resultSetType, int resultSetConcurrency,
      int resultSetHoldability) throws SQLException {
    // TODO Auto-generated method stub
    throw new SQLException("Method not supported");
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#createStruct(java.lang.String, java.lang.Object[])
   */

  public Struct createStruct(String typeName, Object[] attributes)
      throws SQLException {
    // TODO Auto-generated method stub
    throw new SQLException("Method not supported");
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#getAutoCommit()
   */

  public boolean getAutoCommit() throws SQLException {
    return true;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#getCatalog()
   */

  public String getCatalog() throws SQLException {
    return "";
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#getClientInfo()
   */

  public Properties getClientInfo() throws SQLException {
    // TODO Auto-generated method stub
    throw new SQLException("Method not supported");
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#getClientInfo(java.lang.String)
   */

  public String getClientInfo(String name) throws SQLException {
    // TODO Auto-generated method stub
    throw new SQLException("Method not supported");
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#getHoldability()
   */

  public int getHoldability() throws SQLException {
    // TODO Auto-generated method stub
    throw new SQLException("Method not supported");
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#getMetaData()
   */

  public DatabaseMetaData getMetaData() throws SQLException {
    return new HiveDatabaseMetaData(client, sessHandle);
  }

  public int getNetworkTimeout() throws SQLException {
    // JDK 1.7
    throw new SQLException("Method not supported");
  }


  public String getSchema() throws SQLException {
    // JDK 1.7
    throw new SQLException("Method not supported");
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#getTransactionIsolation()
   */

  public int getTransactionIsolation() throws SQLException {
    return Connection.TRANSACTION_NONE;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#getTypeMap()
   */

  public Map<String, Class<?>> getTypeMap() throws SQLException {
    // TODO Auto-generated method stub
    throw new SQLException("Method not supported");
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#getWarnings()
   */

  public SQLWarning getWarnings() throws SQLException {
    return warningChain;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#isClosed()
   */

  public boolean isClosed() throws SQLException {
    return isClosed;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#isReadOnly()
   */

  public boolean isReadOnly() throws SQLException {
    return false;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#isValid(int)
   */

  public boolean isValid(int timeout) throws SQLException {
    // TODO Auto-generated method stub
    throw new SQLException("Method not supported");
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#nativeSQL(java.lang.String)
   */

  public String nativeSQL(String sql) throws SQLException {
    // TODO Auto-generated method stub
    throw new SQLException("Method not supported");
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#prepareCall(java.lang.String)
   */

  public CallableStatement prepareCall(String sql) throws SQLException {
    // TODO Auto-generated method stub
    throw new SQLException("Method not supported");
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#prepareCall(java.lang.String, int, int)
   */

  public CallableStatement prepareCall(String sql, int resultSetType,
      int resultSetConcurrency) throws SQLException {
    // TODO Auto-generated method stub
    throw new SQLException("Method not supported");
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#prepareCall(java.lang.String, int, int, int)
   */

  public CallableStatement prepareCall(String sql, int resultSetType,
      int resultSetConcurrency, int resultSetHoldability) throws SQLException {
    // TODO Auto-generated method stub
    throw new SQLException("Method not supported");
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#prepareStatement(java.lang.String)
   * 执行预编译sql
   */
  public PreparedStatement prepareStatement(String sql) throws SQLException {
    return new HivePreparedStatement(client, sessHandle, sql);
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#prepareStatement(java.lang.String, int)
   * 执行预编译sql
   */
  public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
      throws SQLException {
    return new HivePreparedStatement(client, sessHandle, sql);
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#prepareStatement(java.lang.String, int[])
   */
  public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
      throws SQLException {
    // TODO Auto-generated method stub
    throw new SQLException("Method not supported");
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#prepareStatement(java.lang.String,
   * java.lang.String[])
   */

  public PreparedStatement prepareStatement(String sql, String[] columnNames)
      throws SQLException {
    // TODO Auto-generated method stub
    throw new SQLException("Method not supported");
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#prepareStatement(java.lang.String, int, int)
   */

  public PreparedStatement prepareStatement(String sql, int resultSetType,
      int resultSetConcurrency) throws SQLException {
    return new HivePreparedStatement(client, sessHandle, sql);
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#prepareStatement(java.lang.String, int, int, int)
   */

  public PreparedStatement prepareStatement(String sql, int resultSetType,
      int resultSetConcurrency, int resultSetHoldability) throws SQLException {
    // TODO Auto-generated method stub
    throw new SQLException("Method not supported");
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#releaseSavepoint(java.sql.Savepoint)
   */

  public void releaseSavepoint(Savepoint savepoint) throws SQLException {
    // TODO Auto-generated method stub
    throw new SQLException("Method not supported");
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#rollback()
   */

  public void rollback() throws SQLException {
    // TODO Auto-generated method stub
    throw new SQLException("Method not supported");
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#rollback(java.sql.Savepoint)
   */

  public void rollback(Savepoint savepoint) throws SQLException {
    // TODO Auto-generated method stub
    throw new SQLException("Method not supported");
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#setAutoCommit(boolean)
   */

  public void setAutoCommit(boolean autoCommit) throws SQLException {
    if (autoCommit) {
      throw new SQLException("enabling autocommit is not supported");
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#setCatalog(java.lang.String)
   */

  public void setCatalog(String catalog) throws SQLException {
    // TODO Auto-generated method stub
    throw new SQLException("Method not supported");
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#setClientInfo(java.util.Properties)
   */

  public void setClientInfo(Properties properties)
      throws SQLClientInfoException {
    // TODO Auto-generated method stub
    throw new SQLClientInfoException("Method not supported", null);
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#setClientInfo(java.lang.String, java.lang.String)
   */

  public void setClientInfo(String name, String value)
      throws SQLClientInfoException {
    // TODO Auto-generated method stub
    throw new SQLClientInfoException("Method not supported", null);
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#setHoldability(int)
   */

  public void setHoldability(int holdability) throws SQLException {
    // TODO Auto-generated method stub
    throw new SQLException("Method not supported");
  }

  public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
    // JDK 1.7
    throw new SQLException("Method not supported");
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#setReadOnly(boolean)
   */

  public void setReadOnly(boolean readOnly) throws SQLException {
    // TODO Auto-generated method stub
    throw new SQLException("Method not supported");
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#setSavepoint()
   */

  public Savepoint setSavepoint() throws SQLException {
    // TODO Auto-generated method stub
    throw new SQLException("Method not supported");
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#setSavepoint(java.lang.String)
   */

  public Savepoint setSavepoint(String name) throws SQLException {
    // TODO Auto-generated method stub
    throw new SQLException("Method not supported");
  }

  public void setSchema(String schema) throws SQLException {
    // JDK 1.7
    throw new SQLException("Method not supported");
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#setTransactionIsolation(int)
   */

  public void setTransactionIsolation(int level) throws SQLException {
    // TODO: throw an exception?
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Connection#setTypeMap(java.util.Map)
   */

  public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
    // TODO Auto-generated method stub
    throw new SQLException("Method not supported");
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
   */

  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    // TODO Auto-generated method stub
    throw new SQLException("Method not supported");
  }

  /*
   * (non-Javadoc)
   *
   * @see java.sql.Wrapper#unwrap(java.lang.Class)
   */

  public <T> T unwrap(Class<T> iface) throws SQLException {
    // TODO Auto-generated method stub
    throw new SQLException("Method not supported");
  }

}
