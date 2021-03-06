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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.text.ChoiceFormat;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import jline.console.completer.Completer;
import jline.console.completer.StringsCompleter;
import jline.console.completer.FileNameCompleter;
import jline.console.ConsoleReader;
import jline.console.history.History;
import jline.console.history.FileHistory;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.hadoop.io.IOUtils;

/**
 * A console SQL shell with command completion.
 * <p>
 * TODO:
 * <ul>
 * <li>User-friendly connection prompts</li>
 * <li>Page results</li>
 * <li>Handle binary data (blob fields)</li>
 * <li>Implement command aliases</li>
 * <li>Stored procedure execution</li>
 * <li>Binding parameters to prepared statements</li>
 * <li>Scripting language</li>
 * <li>XA transactions</li>
 * </ul>
 *
 */
public class BeeLine implements Closeable {
  private static final ResourceBundle resourceBundle =
      ResourceBundle.getBundle(BeeLine.class.getSimpleName());
  private final BeeLineSignalHandler signalHandler = null;
  private static final String separator = System.getProperty("line.separator");//回车换行
  private boolean exit = false;//true表示退出
  private final DatabaseConnections connections = new DatabaseConnections();
  public static final String COMMAND_PREFIX = "!";//命令前缀
  private final Completer beeLineCommandCompleter;//持有所有支持的命令集合
  private Collection<Driver> drivers = null;
  private final BeeLineOpts opts = new BeeLineOpts(this, System.getProperties());
  private String lastProgress = null;//上一次的进度内容
  private final Map<SQLWarning, Date> seenWarnings = new HashMap<SQLWarning, Date>();
  private final Commands commands = new Commands(this);
  private OutputFile scriptOutputFile = null;//脚本文件
  private OutputFile recordOutputFile = null;//记录内容的文件
  private PrintStream outputStream = new PrintStream(System.out, true);
  private PrintStream errorStream = new PrintStream(System.err, true);
  private ConsoleReader consoleReader;
  private List<String> batch = null;//批处理执行的命令集合
  private final Reflector reflector;

  private History history;

  private static final Options options = new Options();

  public static final String BEELINE_DEFAULT_JDBC_DRIVER = "org.apache.hive.jdbc.HiveDriver";//默认hive的jdbc驱动
  public static final String BEELINE_DEFAULT_JDBC_URL = "jdbc:hive2://";//默认读取hive的url,采用嵌入式的方式读取hive的数据

  private static final String SCRIPT_OUTPUT_PREFIX = ">>>";
  private static final int SCRIPT_OUTPUT_PAD_SIZE = 5;

  //beeline的程序的最终状态码
  private static final int ERRNO_OK = 0;//成功执行
  private static final int ERRNO_ARGS = 1;//执行过程中遇到exception异常了
  private static final int ERRNO_OTHER = 2;//说明遇到其他的异常,导致退出程序

  private static final String HIVE_VAR_PREFIX = "--hivevar";
  private static final String HIVE_CONF_PREFIX = "--hiveconf";

  //输出格式 vertical/table/csv2/tsv2/dsv/csv/tsv/xmlattr/xmlelements
  private final Map<Object, Object> formats = map(new Object[] {
      "vertical", new VerticalOutputFormat(this),
      "table", new TableOutputFormat(this),
      "csv2", new SeparatedValuesOutputFormat(this, ','),
      "tsv2", new SeparatedValuesOutputFormat(this, '\t'),
      "dsv", new SeparatedValuesOutputFormat(this, BeeLineOpts.DEFAULT_DELIMITER_FOR_DSV),
      "csv", new DeprecatedSeparatedValuesOutputFormat(this, ','),
      "tsv", new DeprecatedSeparatedValuesOutputFormat(this, '\t'),
      "xmlattr", new XMLAttributeOutputFormat(this),
      "xmlelements", new XMLElementOutputFormat(this),
  });

  private List<String> supportedLocalDriver =
    new ArrayList<String>(Arrays.asList("com.mysql.jdbc.Driver", "org.postgresql.Driver"));

  //如何处理各种命令
  final CommandHandler[] commandHandlers = new CommandHandler[] {
      new ReflectiveCommandHandler(this, new String[] {"quit", "done", "exit"},//三个表示执行的都是同一个命令,即quit命令,关闭当前数据库,并且设置退出beeline客户端---!quit
          null),
      new ReflectiveCommandHandler(this, new String[] {"connect", "open"},//2个表示执行的都是同一个命令,即connect命令,连接一个数据库  ----!connect <url> <username> <password> [driver]
          new Completer[] {new StringsCompleter(getConnectionURLExamples())}),
      new ReflectiveCommandHandler(this, new String[] {"describe"},
          new Completer[] {new TableNameCompletor(this)}),//获取一个表的详细描述内容 或者所有表的内容 ---!describe tables  或者!describe tableName(具体表名)
      new ReflectiveCommandHandler(this, new String[] {"indexes"},//获取索引信息----!indexes table
          new Completer[] {new TableNameCompletor(this)}),
      new ReflectiveCommandHandler(this, new String[] {"primarykeys"},//执行DatabaseMetaData的getPrimaryKeys方法,打印一个表的主键列信息----!primarykeys tableName
          new Completer[] {new TableNameCompletor(this)}),
      new ReflectiveCommandHandler(this, new String[] {"exportedkeys"},//执行DatabaseMetaData的getExportedKeys方法,打印一个表的外键属性信息----!exportedkeys tableName
          new Completer[] {new TableNameCompletor(this)}),
      new ReflectiveCommandHandler(this, new String[] {"manual"},//manual命令读取manual.txt说明文件---!manual
          null),
      new ReflectiveCommandHandler(this, new String[] {"importedkeys"},//执行DatabaseMetaData的getImportedKeys方法,打印一个表的检索表中外键列引用的主键列信息----!importedkeys tableName
          new Completer[] {new TableNameCompletor(this)}),
      new ReflectiveCommandHandler(this, new String[] {"procedures"},//获取存储过程的描述---!procedures procedureName(存储过程的name)----调用DatabaseMetaData对象的getProcedures方法获取结果
          null),
      new ReflectiveCommandHandler(this, new String[] {"tables"},//执行DatabaseMetaData的getTables方法,打印一个表的属性信息----!tables tableName
          null),
      new ReflectiveCommandHandler(this, new String[] {"typeinfo"},//执行DatabaseMetaData的getTypeInfo方法,打印数据库提供的字段类型信息----!typeinfo
          null),
      new ReflectiveCommandHandler(this, new String[] {"columns"},//执行DatabaseMetaData的getColumns方法,打印一个表的所有列属性信息----!columns tableName
          new Completer[] {new TableNameCompletor(this)}),
      new ReflectiveCommandHandler(this, new String[] {"reconnect"},//重新连接当前数据库----!reconnect
          null),
      new ReflectiveCommandHandler(this, new String[] {"dropall"},//drop table 所有的表----!dropall
          new Completer[] {new TableNameCompletor(this)}),
      new ReflectiveCommandHandler(this, new String[] {"history"},//获取历史命令,基于jline工具包记录的---格式!history
          null),
      new ReflectiveCommandHandler(this, new String[] {"metadata"},//执行DatabaseMetaData对象的方法,即获取数据库的元数据信息---!metadata "" 数据库Name tableName,注意 第一个""参数表示catalog
          new Completer[] {
              new StringsCompleter(getMetadataMethodNames())}),
      new ReflectiveCommandHandler(this, new String[] {"nativesql"},//执行Connection的nativeSQL方法-----!nativesql sql
          null),
      new ReflectiveCommandHandler(this, new String[] {"dbinfo"},//执行当前数据库的DatabaseMetaData的若干个方法,并且打印信息,相当于当前数据库的一些描述信息----!dbinfo
          null),
      new ReflectiveCommandHandler(this, new String[] {"rehash"},//重新刷新自动补全信息---!rehash
          null),
      new ReflectiveCommandHandler(this, new String[] {"verbose"},//设置用debug模式输出,即set verbose true----!verbose
          null),
      new ReflectiveCommandHandler(this, new String[] {"run"},//执行一个脚本文件---脚本中存放了所有的命令集合,用回车换行表示一个单独的命令---!run filePath
          new Completer[] {new FileNameCompleter()}),
      new ReflectiveCommandHandler(this, new String[] {"batch"},//开启和执行一个批处理命令集合----!batch
          null),
      new ReflectiveCommandHandler(this, new String[] {"list"},//打印目前所有配置的数据库连接信息----!list   该命令配合go命令使用
          null),
      new ReflectiveCommandHandler(this, new String[] {"all"},//对所有数据库都执行all 后面的内容  -----!all sql
          null),
      new ReflectiveCommandHandler(this, new String[] {"go", "#"},//切换第几个数据库,跟list命令一起使用 ----!go Integer
          null),
      new ReflectiveCommandHandler(this, new String[] {"script"},//开启和关闭一个脚本文件,在开启期间可以添加脚本内容----开启!script filePath,关闭!script
          new Completer[] {new FileNameCompleter()}),
      new ReflectiveCommandHandler(this, new String[] {"record"},//开启和关闭一个记录输出内容的文件---开启 !record filePath 关闭 !record
          new Completer[] {new FileNameCompleter()}),
      new ReflectiveCommandHandler(this, new String[] {"brief"},//设置不用debug模式输出,即set verbose false----!brief
          null),
      new ReflectiveCommandHandler(this, new String[] {"close"},//关闭当前数据库连接   !close
          null),
      new ReflectiveCommandHandler(this, new String[] {"closeall"},//关闭所有的数据库 ----!closeall
          null),
      new ReflectiveCommandHandler(this, new String[] {"isolation"},//设置隔离级别----!isolation TRANSACTION_NONE/TRANSACTION_READ_COMMITTED/TRANSACTION_READ_UNCOMMITTED/TRANSACTION_REPEATABLE_READ/TRANSACTION_SERIALIZABLE
          new Completer[] {new StringsCompleter(getIsolationLevels())}),
      new ReflectiveCommandHandler(this, new String[] {"outputformat"},//设置输出的打印形式----!outputformat vertical/table/csv2/tsv2/dsv/csv/tsv/xmlattr/xmlelements
          new Completer[] {new StringsCompleter(
              formats.keySet().toArray(new String[0]))}),
      new ReflectiveCommandHandler(this, new String[] {"autocommit"},//设置当前数据库是否是自动提交----!autocommit on/off
          null),
      new ReflectiveCommandHandler(this, new String[] {"commit"},//设置提交当前事务----!commit
          null),
      new ReflectiveCommandHandler(this, new String[] {"properties"},//连接所有的数据库,每一个数据库对应一个配置文件,多个数据源使用空格,分割多个配置文件---!properties filePath1 filePath2
          new Completer[] {new FileNameCompleter()}),
      new ReflectiveCommandHandler(this, new String[] {"rollback"},//设置回滚当前事务----!rollback
          null),
      new ReflectiveCommandHandler(this, new String[] {"help", "?"},//帮助命令 !help
          null),
      new ReflectiveCommandHandler(this, new String[] {"set"},//!set key value 修改配置文件
          getOpts().optionCompleters()),
      new ReflectiveCommandHandler(this, new String[] {"save"},//将当前配置信息重新写入到beeline的配置文件中----!save
          null),
      new ReflectiveCommandHandler(this, new String[] {"scan"},//打印所有的driver信息----!scan
          null),
      new ReflectiveCommandHandler(this, new String[] {"sql"},//执行一个sql----!sql sql
          null),
      new ReflectiveCommandHandler(this, new String[] {"sh"},//执行一个shell命令----!sh shell,其中shell可以用空格拆分成执行数组参数等信息
          null),
      new ReflectiveCommandHandler(this, new String[] {"call"},//执行一个系统调用----!call sql
          null),
      new ReflectiveCommandHandler(this, new String[] {"nullemptystring"},
          new Completer[] {new BooleanCompleter()}),
      new ReflectiveCommandHandler(this, new String[]{"addlocaldriverjar"},//添加一个driver的jar----!addlocaldriverjar driverJarPath
          null),
      new ReflectiveCommandHandler(this, new String[]{"addlocaldrivername"},//添加一个driver的class----!addlocaldrivername driverClassName
          null)
  };

  //默认已知的dirver都是hive的driver
  static final SortedSet<String> KNOWN_DRIVERS = new TreeSet<String>(Arrays.asList(
      new String[] {
          "org.apache.hive.jdbc.HiveDriver",//server2
          "org.apache.hadoop.hive.jdbc.HiveDriver",//server1
      }));


  static {
    try {
      Class.forName("jline.console.ConsoleReader");//从输入流中读取数据内容
    } catch (Throwable t) {
      throw new ExceptionInInitializerError("jline-missing");
    }
  }

    /**
     -d <driver class>  -u <database url>  -n <username>  -p <password>
     -w (or) --password-file <file>
     -a <authType>
     -i <init file>
     -e <query sql>
     -f <script file>
     -help
     --hivevar key value
     --hiveconf key value
     */
  static {
    // -d <driver class>
    options.addOption(OptionBuilder
        .hasArg()
        .withArgName("driver class")
        .withDescription("the driver class to use")
        .create('d'));

    // -u <database url>
    options.addOption(OptionBuilder
        .hasArg()
        .withArgName("database url")
        .withDescription("the JDBC URL to connect to")
        .create('u'));

    // -n <username>
    options.addOption(OptionBuilder
        .hasArg()
        .withArgName("username")
        .withDescription("the username to connect as")
        .create('n'));

    // -p <password>
    options.addOption(OptionBuilder
        .hasArg()
        .withArgName("password")
        .withDescription("the password to connect as")
        .create('p'));

    // -w (or) --password-file <file>
    options.addOption(OptionBuilder
        .hasArg()
        .withArgName("password-file")
        .withDescription("the password file to read password from")
        .withLongOpt("password-file")
        .create('w'));

    // -a <authType>
    options.addOption(OptionBuilder
        .hasArg()
        .withArgName("authType")
        .withDescription("the authentication type")
        .create('a'));

    // -i <init file>
    options.addOption(OptionBuilder
        .hasArg()
        .withArgName("init")
        .withDescription("script file for initialization")
        .create('i'));

    // -e <query>
    options.addOption(OptionBuilder
        .hasArgs()
        .withArgName("query")
        .withDescription("query that should be executed")
        .create('e'));

    // -f <script file>
    options.addOption(OptionBuilder
        .hasArg()
        .withArgName("file")
        .withDescription("script file that should be executed")
        .create('f'));

    // -help
    options.addOption(OptionBuilder
        .withLongOpt("help")
        .withDescription("display this message")
        .create('h'));

    // Substitution option --hivevar 设置hive的sql中可以使用的变量
    options.addOption(OptionBuilder
        .withValueSeparator()
        .hasArgs(2)
        .withArgName("key=value")
        .withLongOpt("hivevar")
        .withDescription("hive variable name and value")
        .create());

    //hive conf option --hiveconf
    options.addOption(OptionBuilder
        .withValueSeparator()
        .hasArgs(2)
        .withArgName("property=value")
        .withLongOpt("hiveconf")
        .withDescription("Use value for given property")
        .create());
  }

  //返回jar对应的Manifest文件对象
  static Manifest getManifest() throws IOException {
    URL base = BeeLine.class.getResource("/META-INF/MANIFEST.MF");
    URLConnection c = base.openConnection();
    if (c instanceof JarURLConnection) {
      return ((JarURLConnection) c).getManifest();
    }
    return null;
  }

  //读取jar包内/META-INF/MANIFEST.MF的name对应的值,如果该值不存在,则使用?形式表示
  String getManifestAttribute(String name) {
    try {
      Manifest m = getManifest();
      if (m == null) {
        return "??";
      }

      Attributes attrs = m.getAttributes("beeline");
      if (attrs == null) {
        return "???";
      }

      String val = attrs.getValue(name);
      if (val == null || "".equals(val)) {
        return "????";
      }

      return val;
    } catch (Exception e) {
      e.printStackTrace(errorStream);
      return "?????";
    }
  }


  String getApplicationTitle() {
    Package pack = BeeLine.class.getPackage();

    return loc("app-introduction", new Object[] {
        "Beeline",
        pack.getImplementationVersion() == null ? "???"
            : pack.getImplementationVersion(),
        "Apache Hive",
        // getManifestAttribute ("Specification-Title"),
        // getManifestAttribute ("Implementation-Version"),
        // getManifestAttribute ("Implementation-ReleaseDate"),
        // getManifestAttribute ("Implementation-Vendor"),
        // getManifestAttribute ("Implementation-License"),
    });
  }

  String getApplicationContactInformation() {
    return getManifestAttribute("Implementation-Vendor");
  }

  String loc(String res) {
    return loc(res, new Object[0]);
  }

  String loc(String res, int param) {
    try {
      return MessageFormat.format(
          new ChoiceFormat(resourceBundle.getString(res)).format(param),
          new Object[] {new Integer(param)});
    } catch (Exception e) {
      return res + ": " + param;
    }
  }

  String loc(String res, Object param1) {
    return loc(res, new Object[] {param1});
  }

  String loc(String res, Object param1, Object param2) {
    return loc(res, new Object[] {param1, param2});
  }

  //通过res这个key,找到资源文件中引用的value,该value需要动态参数,因为动态参数就是第二个参数数组
  String loc(String res, Object[] params) {
    try {
      return MessageFormat.format(resourceBundle.getString(res), params);
    } catch (Exception e) {
      e.printStackTrace(getErrorStream());
      try {
        return res + ": " + Arrays.asList(params);
      } catch (Exception e2) {
        return res;
      }
    }
  }

  protected String locElapsedTime(long milliseconds) {
    if (getOpts().getShowElapsedTime()) {
      return loc("time-ms", new Object[] {new Double(milliseconds / 1000d)});
    }
    return "";
  }


  /**
   * Starts the program.
   * 程序入口
   */
  public static void main(String[] args) throws IOException {
    mainWithInputRedirection(args, null);
  }

  /**
   * Starts the program with redirected input. For redirected output,
   * setOutputStream() and setErrorStream can be used.
   * Exits with 0 on success, 1 on invalid arguments, and 2 on any other error
   *
   * @param args
   *          same as main()
   *
   * @param inputStream
   *          redirected input, or null to use standard input
   * 程序的入口
   */
  public static void mainWithInputRedirection(String[] args, InputStream inputStream)
      throws IOException {
    BeeLine beeLine = new BeeLine();
    int status = beeLine.begin(args, inputStream);

    if (!Boolean.getBoolean(BeeLineOpts.PROPERTY_NAME_EXIT)) {//System.getProperty(name)的结果是否是false
        System.exit(status);
    }
  }


  public BeeLine() {
    beeLineCommandCompleter = new BeeLineCommandCompleter(BeeLineCommandCompleter.getCompleters
        (this));
    reflector = new Reflector(this);

    // attempt to dynamically load signal handler
    /* TODO disable signal handler
    try {
      Class<?> handlerClass =
          Class.forName("org.apache.hive.beeline.SunSignalHandler");
      signalHandler = (BeeLineSignalHandler)
          handlerClass.newInstance();
    } catch (Throwable t) {
      // ignore and leave cancel functionality disabled
    }
    */
  }

  //获取当前数据库对象
  DatabaseConnection getDatabaseConnection() {
    return getDatabaseConnections().current();
  }

  //获取当前数据库的连接
  Connection getConnection() throws SQLException {
    if (getDatabaseConnections().current() == null) {
      throw new IllegalArgumentException(loc("no-current-connection"));
    }
    if (getDatabaseConnections().current().getConnection() == null) {
          throw new IllegalArgumentException(loc("no-current-connection"));
      }
    return getDatabaseConnections().current().getConnection();
  }

  //获取当前数据库的元数据信息
  DatabaseMetaData getDatabaseMetaData() {
    if (getDatabaseConnections().current() == null) {
      throw new IllegalArgumentException(loc("no-current-connection"));
    }
    if (getDatabaseConnections().current().getDatabaseMetaData() == null) {
      throw new IllegalArgumentException(loc("no-current-connection"));
    }
    return getDatabaseConnections().current().getDatabaseMetaData();
  }


  //隔离级别
  public String[] getIsolationLevels() {
    return new String[] {
        "TRANSACTION_NONE",
        "TRANSACTION_READ_COMMITTED",
        "TRANSACTION_READ_UNCOMMITTED",
        "TRANSACTION_REPEATABLE_READ",
        "TRANSACTION_SERIALIZABLE",
    };
  }

  //获取当前元数据的方法集合
  public String[] getMetadataMethodNames() {
    try {
      TreeSet<String> mnames = new TreeSet<String>();
      Method[] m = DatabaseMetaData.class.getDeclaredMethods();
      for (int i = 0; m != null && i < m.length; i++) {
        mnames.add(m[i].getName());
      }
      return mnames.toArray(new String[0]);
    } catch (Throwable t) {
      return new String[0];
    }
  }

  //url连接的demo集合
  public String[] getConnectionURLExamples() {
    return new String[] {
        "jdbc:JSQLConnect://<hostname>/database=<database>",
        "jdbc:cloudscape:<database>;create=true",
        "jdbc:twtds:sqlserver://<hostname>/<database>",
        "jdbc:daffodilDB_embedded:<database>;create=true",
        "jdbc:datadirect:db2://<hostname>:50000;databaseName=<database>",
        "jdbc:inetdae:<hostname>:1433",
        "jdbc:datadirect:oracle://<hostname>:1521;SID=<database>;MaxPooledStatements=0",
        "jdbc:datadirect:sqlserver://<hostname>:1433;SelectMethod=cursor;DatabaseName=<database>",
        "jdbc:datadirect:sybase://<hostname>:5000",
        "jdbc:db2://<hostname>/<database>",
        "jdbc:hive2://<hostname>",
        "jdbc:hsqldb:<database>",
        "jdbc:idb:<database>.properties",
        "jdbc:informix-sqli://<hostname>:1526/<database>:INFORMIXSERVER=<database>",
        "jdbc:interbase://<hostname>//<database>.gdb",
        "jdbc:microsoft:sqlserver://<hostname>:1433;DatabaseName=<database>;SelectMethod=cursor",
        "jdbc:mysql://<hostname>/<database>?autoReconnect=true",
        "jdbc:oracle:thin:@<hostname>:1521:<database>",
        "jdbc:pointbase:<database>,database.home=<database>,create=true",
        "jdbc:postgresql://<hostname>:5432/<database>",
        "jdbc:postgresql:net//<hostname>/<database>",
        "jdbc:sybase:Tds:<hostname>:4100/<database>?ServiceName=<database>",
        "jdbc:weblogic:mssqlserver4:<database>@<hostname>:1433",
        "jdbc:odbc:<database>",
        "jdbc:sequelink://<hostname>:4003/[Oracle]",
        "jdbc:sequelink://<hostname>:4004/[Informix];Database=<database>",
        "jdbc:sequelink://<hostname>:4005/[Sybase];Database=<database>",
        "jdbc:sequelink://<hostname>:4006/[SQLServer];Database=<database>",
        "jdbc:sequelink://<hostname>:4011/[ODBC MS Access];Database=<database>",
        "jdbc:openlink://<hostname>/DSN=SQLServerDB/UID=sa/PWD=",
        "jdbc:solid://<hostname>:<port>/<UID>/<PWD>",
        "jdbc:dbaw://<hostname>:8889/<database>",
    };
  }

  /**
   * Entry point to creating a {@link ColorBuffer} with color
   * enabled or disabled depending on the value of {@link BeeLineOpts#getColor}.
   */
  ColorBuffer getColorBuffer() {
    return new ColorBuffer(getOpts().getColor());
  }


  /**
   * Entry point to creating a {@link ColorBuffer} with color
   * enabled or disabled depending on the value of {@link BeeLineOpts#getColor}.
   */
  ColorBuffer getColorBuffer(String msg) {
    return new ColorBuffer(msg, getOpts().getColor());
  }


  public class BeelineParser extends GnuParser {

    //可以设置key=value形式的数据参数,比如--key=value,如果设置boolean类型为true则可以简单的写错--key即可
    @Override
    protected void processOption(final String arg, final ListIterator iter) throws  ParseException {
      if ((arg.startsWith("--")) && !(arg.equals(HIVE_VAR_PREFIX) || (arg.equals(HIVE_CONF_PREFIX)) || (arg.equals("--help")))) {
        String stripped = arg.substring(2, arg.length());
        String[] parts = split(stripped, "=");
        debug(loc("setting-prop", Arrays.asList(parts)));
        if (parts.length >= 2) {
          getOpts().set(parts[0], parts[1], true);
        } else {
          getOpts().set(parts[0], "true", true);
        }
      } else {
        super.processOption(arg, iter);
      }
    }

  }

  //解析命令行的参数信息
  int initArgs(String[] args) {
    List<String> commands = Collections.emptyList();//要执行初始化的命令
    List<String> files = Collections.emptyList();//数据库连接对象文件

    CommandLine cl;
    BeelineParser beelineParser;

    try {
      beelineParser = new BeelineParser();//解命令行的配置参数
      cl = beelineParser.parse(options, args);
    } catch (ParseException e1) {
      output(e1.getMessage());
      usage();
      return -1;
    }

    String driver = null, user = null, pass = null, url = null;
    String auth = null;


    if (cl.hasOption("help")) {
      usage();
      return 0;
    }

    Properties hiveVars = cl.getOptionProperties("hivevar");//说明设置的是hive在sql中的变量
    for (String key : hiveVars.stringPropertyNames()) {
      getOpts().getHiveVariables().put(key, hiveVars.getProperty(key));
    }

    Properties hiveConfs = cl.getOptionProperties("hiveconf");//设置
    for (String key : hiveConfs.stringPropertyNames()) {
      getOpts().getHiveConfVariables().put(key, hiveConfs.getProperty(key));
    }

    driver = cl.getOptionValue("d");
    auth = cl.getOptionValue("a");
    user = cl.getOptionValue("n");
    getOpts().setAuthType(auth);
    if (cl.hasOption("w")) {
      pass = obtainPasswordFromFile(cl.getOptionValue("w"));//解析密码文件
    } else {
      pass = cl.getOptionValue("p");
    }
    url = cl.getOptionValue("u");
    getOpts().setInitFile(cl.getOptionValue("i"));
    getOpts().setScriptFile(cl.getOptionValue("f"));
    if (cl.getOptionValues('e') != null) {
      commands = Arrays.asList(cl.getOptionValues('e'));
    }


    // TODO: temporary disable this for easier debugging
    /*
    if (url == null) {
      url = BEELINE_DEFAULT_JDBC_URL;
    }
    if (driver == null) {
      driver = BEELINE_DEFAULT_JDBC_DRIVER;
    }
    */

    //执行connect命令,需要的参数依次是url 用户名 密码 driver,期间用空格分割
    if (url != null) {
      String com = "!connect " //进行connect命令,创建一个数据库的连接
          + url + " "
          + (user == null || user.length() == 0 ? "''" : user) + " "
          + (pass == null || pass.length() == 0 ? "''" : pass) + " "
          + (driver == null ? "" : driver);
      debug("issuing: " + com);
      dispatch(com);
    }

    // now load properties files
    for (Iterator<String> i = files.iterator(); i.hasNext();) {//加载配置文件,让其创建更多的数据库连接
      dispatch("!properties " + i.next());
    }

    int code = 0;
    if (!commands.isEmpty()) {
      for (Iterator<String> i = commands.iterator(); i.hasNext();) {
        String command = i.next().toString();
        debug(loc("executing-command", command));
        if (!dispatch(command)) {
          code++;
        }
      }
      exit = true; // execute and exit
    }
    return code;
  }

  /**
   * Obtains a password from the passed file path.
   * 解析密码文件
   */
  private String obtainPasswordFromFile(String passwordFilePath) {
    try {
      Path path = Paths.get(passwordFilePath);
      byte[] passwordFileContents = Files.readAllBytes(path);
      return new String(passwordFileContents, "UTF-8").trim();
    } catch (Exception e) {
      throw new RuntimeException("Unable to read user password from the password file: "
          + passwordFilePath, e);
    }
  }

  /**
   * Start accepting input from stdin, and dispatch it
   * to the appropriate {@link CommandHandler} until the
   * global variable <code>exit</code> is true.
   */
  public int begin(String[] args, InputStream inputStream) throws IOException {
    try {
      // load the options first, so we can override on the command line
      getOpts().load();
    } catch (Exception e) {
      // nothing
    }

    try {
      int code = initArgs(args);
      if (code != 0) {
        return code;
      }

      if (getOpts().getScriptFile() != null) {
        return executeFile(getOpts().getScriptFile());
      }
      try {
        info(getApplicationTitle());
      } catch (Exception e) {
        // ignore
      }
      ConsoleReader reader = getConsoleReader(inputStream);
      return execute(reader, false);
    } finally {
        close();
    }
  }

  //每次切换一个数据库连接的时候,都进行初始化该文件的命令
  int runInit() {
    String initFile = getOpts().getInitFile();
    if (initFile != null) {
      info("Running init script " + initFile);
      try {
        return executeFile(initFile);
      } finally {
        exit = false;
      }
    }
    return ERRNO_OK;
  }

  private int executeFile(String fileName) {
    FileInputStream initStream = null;
    try {
      initStream = new FileInputStream(fileName);
      return execute(getConsoleReader(initStream), true);
    } catch (Throwable t) {
      handleException(t);
      return ERRNO_OTHER;
    } finally {
      IOUtils.closeStream(initStream);
      consoleReader = null;
      output("");   // dummy new line
    }
  }

    /**
     * 不断的执行命令
     * @param reader 表示输入源窗口
     * @param exitOnError true表示遇见异常则退出beeline
     */
  private int execute(ConsoleReader reader, boolean exitOnError) {
    String line;
    while (!exit) {//没有退出,则一直循环
      try {
        // Execute one instruction; terminate on executing a script if there is an error
        // in silent mode, prevent the query and prompt being echoed back to terminal
        line = getOpts().isSilent() ? reader.readLine(null, ConsoleReader.NULL_MASK) : reader.readLine(getPrompt());

        if (!dispatch(line) && exitOnError) {
          return ERRNO_OTHER;
        }
      } catch (Throwable t) {
        handleException(t);
        return ERRNO_OTHER;
      }
    }
    return ERRNO_OK;
  }

  @Override
  public void close() {
    commands.closeall(null);
  }

  public ConsoleReader getConsoleReader(InputStream inputStream) throws IOException {
    if (inputStream != null) {//开启一个输入流,用JLine管理
      // ### NOTE: fix for sf.net bug 879425.
      consoleReader = new ConsoleReader(inputStream, getOutputStream());
    } else {
      consoleReader = new ConsoleReader();
    }

    //disable the expandEvents for the purpose of backward compatibility
    consoleReader.setExpandEvents(false);

    // setup history 初始化历史命令文件
    ByteArrayOutputStream hist = null;
    if (new File(getOpts().getHistoryFile()).isFile()) {//说明历史文件存在
      try {
        // save the current contents of the history buffer. This gets
        // around a bug in JLine where setting the output before the
        // input will clobber the history input, but setting the
        // input before the output will cause the previous commands
        // to not be saved to the buffer.
        FileInputStream historyIn = new FileInputStream(getOpts().getHistoryFile());//读取历史文件
        hist = new ByteArrayOutputStream();//临时存储历史的命令内容
        int n;
        while ((n = historyIn.read()) != -1) {//读取每一行的历史文件
          hist.write(n);//将历史文件写入到输出流中
        }
        historyIn.close();
      } catch (Exception e) {
        handleException(e);
      }
    }

    try {
      // now set the output for the history
      consoleReader.setHistory(new FileHistory(new File(getOpts().getHistoryFile())));//设置历史命令,即命令都会进入历史命令文件中
    } catch (Exception e) {
      handleException(e);
    }

    if (inputStream instanceof FileInputStream) {
      // from script.. no need to load history and no need of completer, either
      return consoleReader;
    }
    try {
      // now load in the previous history
      if (hist != null) {
        History h = consoleReader.getHistory();
        if (h instanceof FileHistory) {
          ((FileHistory) consoleReader.getHistory()).load(new ByteArrayInputStream(hist
              .toByteArray()));
        } else {
          consoleReader.getHistory().add(hist.toString());
        }
      }
    } catch (Exception e) {
        handleException(e);
    }

    // add shutdown hook to flush the history to history file
    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
        @Override
        public void run() {
            History h = consoleReader.getHistory();
            if (h instanceof FileHistory) {
                try {
                    ((FileHistory) h).flush();
                } catch (IOException e) {
                    error(e);
                }
            }
        }
    }));

    consoleReader.addCompleter(new BeeLineCompleter(this));//添加自动补全功能
    return consoleReader;
  }


  void usage() {
    output(loc("cmd-usage"));
  }


  /**
   * Dispatch the specified line to the appropriate {@link CommandHandler}.
   * 执行一个命令
   * @param line
   *          the command-line to dispatch
   * @return true if the command was "successful"
   * 例如
   * !metadata xxxx 表示执行commands类的metadata方法,后面xxx表示需要的参数信息,集合用空格进行拆分
   */
  boolean dispatch(String line) {
    if (line == null) {
      // exit
      exit = true;
      return true;
    }

    if (line.trim().length() == 0) {
      return true;
    }

    if (isComment(line)) {
      return true;
    }

    line = line.trim();

    // save it to the current script, if any 保存到当前脚本内
    if (scriptOutputFile != null) {
      scriptOutputFile.addLine(line);
    }

    if (isHelpRequest(line)) {
      line = "!help";
    }

    if (line.startsWith(COMMAND_PREFIX)) {//以!开头,说明是任务命令
      Map<String, CommandHandler> cmdMap = new TreeMap<String, CommandHandler>();//key是执行CommandHandler命令的时候匹配的是哪个命令key,value是具体的命令处理器
      line = line.substring(1);//取消!
      for (int i = 0; i < commandHandlers.length; i++) {
        String match = commandHandlers[i].matches(line);//说明该命令是否匹配该行数据
        if (match != null) {
          CommandHandler prev = cmdMap.put(match, commandHandlers[i]);
          if (prev != null) {
            return error(loc("multiple-matches",
                Arrays.asList(prev.getName(), commandHandlers[i].getName())));//说明命令匹配多个hander,因此存在异常
          }
        }
      }

      if (cmdMap.size() == 0) {
        return error(loc("unknown-command", line));
      }
      if (cmdMap.size() > 1) {
        // any exact match?
        CommandHandler handler = cmdMap.get(line);
        if (handler == null) {
          return error(loc("multiple-matches", cmdMap.keySet().toString()));
        }
        return handler.execute(line);
      }
      return cmdMap.values().iterator().next()
          .execute(line);//执行一个命令
    } else {
      return commands.sql(line, getOpts().getEntireLineAsCommand());//说明此时是sql,而不是任务
    }
  }

  /**
   * Test whether a line requires a continuation.
   *
   * @param line
   *          the line to be tested
   *
   * @return true if continuation required
   * true表示需要继续解析命令
   */
  boolean needsContinuation(String line) {
    if (isHelpRequest(line)) {//说明是help命令,则不需要继续解析了
      return false;
    }

    if (line.startsWith(COMMAND_PREFIX)) {//说明是!开头的命令,也不需要解析了
      return false;
    }

    if (isComment(line)) {//说明是备注,则不需要继续解析了
      return false;
    }

    String trimmed = line.trim();

    if (trimmed.length() == 0) {
      return false;
    }

    if (!getOpts().isAllowMultiLineCommand()) {//说明不允许多行显示一个sql,因此也不需要继续解析了
      return false;
    }

    return !trimmed.endsWith(";");//如果不是以;结尾的,说明还要继续解析
  }

  /**
   * Test whether a line is a help request other than !help.
   *
   * @param line
   *          the line to be tested
   *
   * @return true if a help request
   * true表示该命令是help命令
   */
  boolean isHelpRequest(String line) {
    return line.equals("?") || line.equalsIgnoreCase("help");
  }

  /**
   * Test whether a line is a comment.
   *
   * @param line
   *          the line to be tested
   *
   * @return true if a comment
   * true表示该行数据是备注
   */
  boolean isComment(String line) {
    // SQL92 comment prefix is "--"
    // beeline also supports shell-style "#" prefix
    String lineTrimmed = line.trim();
    return lineTrimmed.startsWith("#") || lineTrimmed.startsWith("--");
  }

  /**
   * Print the specified message to the console
   *
   * @param msg
   *          the message to print
   */
  void output(String msg) {
    output(msg, true);
  }


  void info(String msg) {
    if (!(getOpts().isSilent())) {
      output(msg, true, getErrorStream());
    }
  }


  void info(ColorBuffer msg) {
    if (!(getOpts().isSilent())) {
      output(msg, true, getErrorStream());
    }
  }


  /**
   * Issue the specified error message
   *
   * @param msg
   *          the message to issue
   * @return false always
   */
  boolean error(String msg) {
    output(getColorBuffer().red(msg), true, getErrorStream());
    return false;
  }


  boolean error(Throwable t) {
    handleException(t);
    return false;
  }


  void debug(String msg) {
    if (getOpts().getVerbose()) {
      output(getColorBuffer().blue(msg), true, getErrorStream());//蓝色字打印
    }
  }


  void output(ColorBuffer msg) {
    output(msg, true);
  }


  void output(String msg, boolean newline, PrintStream out) {
    output(getColorBuffer(msg), newline, out);
  }


  void output(ColorBuffer msg, boolean newline) {
    output(msg, newline, getOutputStream());
  }

  //打印输出内容,并且可能的话,还要讲结果计入到recordOutputFile文件中
  void output(ColorBuffer msg, boolean newline, PrintStream out) {
    if (newline) {
      out.println(msg.getColor());
    } else {
      out.print(msg.getColor());
    }

    if (recordOutputFile == null) {
      return;
    }

    // only write to the record file if we are writing a line ...
    // otherwise we might get garbage from backspaces and such.
    if (newline) {
      recordOutputFile.addLine(msg.getMono()); // always just write mono
    } else {
      recordOutputFile.print(msg.getMono());
    }
  }


  /**
   * Print the specified message to the console
   *
   * @param msg
   *          the message to print
   * @param newline
   *          if false, do not append a newline
   */
  void output(String msg, boolean newline) {
    output(getColorBuffer(msg), newline);
  }


  void autocommitStatus(Connection c) throws SQLException {
    info(loc("autocommit-status", c.getAutoCommit() + ""));
  }


  /**
   * Ensure that autocommit is on for the current connection
   *
   * @return true if autocommit is set
   */
  boolean assertAutoCommit() {
    if (!(assertConnection())) {
      return false;
    }
    try {
      if (getDatabaseConnection().getConnection().getAutoCommit()) {
        return error(loc("autocommit-needs-off"));
      }
    } catch (Exception e) {
      return error(e);
    }
    return true;
  }


  /**
   * Assert that we have an active, living connection. Print
   * an error message if we do not.
   *
   * @return true if there is a current, active connection
   * 确定连接一定存在
   */
  boolean assertConnection() {
    try {
      if (getDatabaseConnection() == null || getDatabaseConnection().getConnection() == null) {
        return error(loc("no-current-connection"));
      }
      if (getDatabaseConnection().getConnection().isClosed()) {
        return error(loc("connection-is-closed"));
      }
    } catch (SQLException sqle) {
      return error(loc("no-current-connection"));
    }
    return true;
  }


  /**
   * Print out any warnings that exist for the current connection.
   */
  void showWarnings() {
    try {
      if (getDatabaseConnection().getConnection() == null
          || !getOpts().getVerbose()) {
        return;
      }
      showWarnings(getDatabaseConnection().getConnection().getWarnings());
    } catch (Exception e) {
      handleException(e);
    }
  }


  /**
   * Print the specified warning on the console, as well as
   * any warnings that are returned from {@link SQLWarning#getNextWarning}.
   *
   * @param warn
   *          the {@link SQLWarning} to print
   */
  void showWarnings(SQLWarning warn) {
    if (warn == null) {
      return;
    }

    if (seenWarnings.get(warn) == null) {
      // don't re-display warnings we have already seen
      seenWarnings.put(warn, new java.util.Date());
      handleSQLException(warn);
    }

    SQLWarning next = warn.getNextWarning();
    if (next != warn) {
      showWarnings(next);
    }
  }

  //打印连接的提示信息
  String getPrompt() {
    if (getDatabaseConnection() == null || getDatabaseConnection().getUrl() == null) {
      return "beeline> ";
    } else {
      String printClosed = getDatabaseConnection().isClosed() ? " (closed)" : "";
      return getPrompt(getDatabaseConnections().getIndex()
          + ": " + getDatabaseConnection().getUrl()) + printClosed + "> ";
    }
  }

  //打印连接的url
  static String getPrompt(String url) {
    if (url == null || url.length() == 0) {
      url = "beeline";
    }
    if (url.indexOf(";") > -1) {
      url = url.substring(0, url.indexOf(";"));
    }
    if (url.indexOf("?") > -1) {
      url = url.substring(0, url.indexOf("?"));//将?参数信息取消掉
    }
    if (url.length() > 45) {//最多打印前45个字节内容
      url = url.substring(0, 45);
    }
    return url;
  }


  /**
   * Try to obtain the current size of the specified {@link ResultSet} by jumping to the last row
   * and getting the row number.
   *
   * @param rs
   *          the {@link ResultSet} to get the size for
   * @return the size, or -1 if it could not be obtained
   * 尝试获取当前结果集的数量
   */
  int getSize(ResultSet rs) {
    try {
      if (rs.getType() == rs.TYPE_FORWARD_ONLY) {
        return -1;
      }
      rs.last();//移动到最后一个数据
      int total = rs.getRow();//获取最后一行数据的行号
      rs.beforeFirst();//恢复到以前的游标位置
      return total;
    } catch (SQLException sqle) {
      return -1;
    }
    // JDBC 1 driver error
    catch (AbstractMethodError ame) {
      return -1;
    }
  }

  //JDBC连接数据库的一个表,获取表的列的结果集
  ResultSet getColumns(String table) throws SQLException {
    if (!(assertConnection())) {
      return null;
    }
    return getDatabaseConnection().getDatabaseMetaData().getColumns(
        getDatabaseConnection().getDatabaseMetaData().getConnection().getCatalog(), null, table, "%");
  }

    //JDBC连接数据库的一个表,获取表的结果集
  ResultSet getTables() throws SQLException {
    if (!(assertConnection())) {
      return null;
    }
    return getDatabaseConnection().getDatabaseMetaData().getTables(
        getDatabaseConnection().getDatabaseMetaData().getConnection().getCatalog(), null, "%",
        new String[] {"TABLE"});
  }

  //获取所有列的信息内容---用于自动补全功能的实现
  String[] getColumnNames(DatabaseMetaData meta) throws SQLException {
    Set<String> names = new HashSet<String>();
    info(loc("building-tables"));
    try {
      ResultSet columns = getColumns("%");//获取每一个列的信息集合
      try {
        int total = getSize(columns);//获取的总记录数---用于打印执行进度
        int index = 0;//已经解析了多少条数据了

        while (columns.next()) {//表示每一个列
          // add the following strings:
          // 1. column name
          // 2. table name
          // 3. tablename.columnname

          progress(index++, total);//用于记录执行进度
          String name = columns.getString("TABLE_NAME");
          names.add(name);
          names.add(columns.getString("COLUMN_NAME"));
          names.add(columns.getString("TABLE_NAME") + "."
              + columns.getString("COLUMN_NAME"));
        }
        progress(index, index);
      } finally {
        columns.close();
      }
      info(loc("done"));
      return names.toArray(new String[0]);
    } catch (Throwable t) {
      handleException(t);
      return new String[0];
    }
  }


  // //////////////////
  // String utilities
  // //////////////////


  /**
   * Split the line into an array by tokenizing on space characters
   *
   * @param line
   *          the line to break up
   * @return an array of individual words
   * 按照空格拆分字符串
   */
  String[] split(String line) {
    return split(line, " ");
  }

  //取消引号
  String dequote(String str) {
    if (str == null) {
      return null;
    }
    while ((str.startsWith("'") && str.endsWith("'"))
        || (str.startsWith("\"") && str.endsWith("\""))) {
      str = str.substring(1, str.length() - 1);
    }
    return str;
  }

  //拆分字符串---拆分成数组
  String[] split(String line, String delim) {
    StringTokenizer tok = new StringTokenizer(line, delim);
    String[] ret = new String[tok.countTokens()];
    int index = 0;
    while (tok.hasMoreTokens()) {
      String t = tok.nextToken();
      t = dequote(t);
      ret[index++] = t;
    }
    return ret;
  }

  //将数组中每两个元素,组成一个map的key和value
  static Map<Object, Object> map(Object[] obs) {
    Map<Object, Object> m = new HashMap<Object, Object>();
    for (int i = 0; i < obs.length - 1; i += 2) {
      m.put(obs[i], obs[i + 1]);
    }
    return Collections.unmodifiableMap(m);
  }


  static boolean getMoreResults(Statement stmnt) {
    try {
      return stmnt.getMoreResults();
    } catch (Throwable t) {
      return false;
    }
  }

  //对value的值进行xml的编码解析
  static String xmlattrencode(String str) {
    str = replace(str, "\"", "&quot;");
    str = replace(str, "<", "&lt;");
    return str;
  }

  //将source中from的内容,替换成to的内容
  static String replace(String source, String from, String to) {
    if (source == null) {
      return null;
    }

    if (from.equals(to)) {//说明不用更改,即更改前后数据一致
      return source;
    }

    StringBuilder replaced = new StringBuilder();

    int index = -1;
    while ((index = source.indexOf(from)) != -1) {
      replaced.append(source.substring(0, index));
      replaced.append(to);
      source = source.substring(index + from.length());//source截断,因为已经一部分正常了
    }
    replaced.append(source);

    return replaced.toString();
  }


  /**
   * Split the line based on spaces, asserting that the
   * number of words is correct.
   *
   * @param line
   *          the line to split 使用空格拆分字符串
   * @param assertLen
   *          the number of words to assure 断言一定是这个长度的数组
   * @param usage
   *          the message to output if there are an incorrect
   *          number of words. 打印的错误信息
   * @return the split lines, or null if the assertion failed.
   * 拆分字符串,并且断言一定拆分后数组length满足条件,否则打印错误信息
   */
  String[] split(String line, int assertLen, String usage) {
    String[] ret = split(line);//按照空格拆分字符串

    if (ret.length != assertLen) {
      error(usage);
      return null;
    }

    return ret;
  }


  /**
   * Wrap the specified string by breaking on space characters.
   *
   * @param toWrap
   *          the string to wrap
   * @param len
   *          the maximum length of any line 一行最大允许多少长度
   * @param start
   *          the number of spaces to pad at the
   *          beginning of a line
   * @return the wrapped string
   * 将toWrap按照空格拆分,然后存储成多行数据,每一行最多有len个长度
   */
  String wrap(String toWrap, int len, int start) {
    StringBuilder buff = new StringBuilder();//存储所有数据
    StringBuilder line = new StringBuilder();//存储一行的数据

    char[] head = new char[start];
    Arrays.fill(head, ' ');//头文件的长度

    for (StringTokenizer tok = new StringTokenizer(toWrap, " "); tok.hasMoreTokens();) {
      String next = tok.nextToken();
      if (line.length() + next.length() > len) {//说明该行现有的数据长度+此时的长度 已经大于最大值了,因此要换行处理
        buff.append(line).append(separator).append(head);//将line的内容写入到buff中,然后回车换行,然后加入头文件
        line.setLength(0);//重置lent的位置
      }

      line.append(line.length() == 0 ? "" : " ").append(next);//每一个字符串中间使用空格进行拆分
    }

    buff.append(line);
    return buff.toString();
  }


  /**
   * Output a progress indicator to the console.
   *
   * @param cur
   *          the current progress
   * @param max
   *          the maximum progress, or -1 if unknown
   * 向输出流中打印进度内容
   */
  void progress(int cur, int max) {
    StringBuilder out = new StringBuilder();

    if (lastProgress != null) {
      char[] back = new char[lastProgress.length()];
      Arrays.fill(back, '\b');//\b是acsii的8,即回退键,因此相当于在流中输入上一次内容的回退键内容
      out.append(back);
    }

      //   cur/max (crl*100/max)% 即打印此时位置和总位置,以及此时位置占用的百分比值
    String progress = cur + "/"
    + (max == -1 ? "?" : "" + max) + " "
    + (max == -1 ? "(??%)"
        : ("(" + (cur * 100 / (max == 0 ? 1 : max)) + "%)"));

    if (cur >= max && max != -1) {
      progress += " " + loc("done") + separator;//separator表示回车换行,表示已经完成进度
      lastProgress = null;
    } else {
      lastProgress = progress;
    }

    out.append(progress);//输出进度内容

    outputStream.print(out.toString());
    outputStream.flush();
  }

  // /////////////////////////////
  // Exception handling routines
  // /////////////////////////////

  void handleException(Throwable e) {
    while (e instanceof InvocationTargetException) {
      e = ((InvocationTargetException) e).getTargetException();
    }

    if (e instanceof SQLException) {
      handleSQLException((SQLException) e);
    } else if (e instanceof EOFException) {
      setExit(true);  // CTRL-D
    } else if (!(getOpts().getVerbose())) {
      if (e.getMessage() == null) {
        error(e.getClass().getName());
      } else {
        error(e.getMessage());
      }
    } else {
      e.printStackTrace(getErrorStream());
    }
  }


  void handleSQLException(SQLException e) {
    if (e instanceof SQLWarning && !(getOpts().getShowWarnings())) {//是否打印警告sql的异常
      return;
    }

    error(loc(e instanceof SQLWarning ? "Warning" : "Error",
        new Object[] {
            e.getMessage() == null ? "" : e.getMessage().trim(),
            e.getSQLState() == null ? "" : e.getSQLState().trim(),
            new Integer(e.getErrorCode())}));

    if (getOpts().getVerbose()) {
      e.printStackTrace(getErrorStream());
    }

    if (!getOpts().getShowNestedErrs()) {
      return;
    }

    for (SQLException nested = e.getNextException(); nested != null && nested != e; nested = nested
        .getNextException()) {
      handleSQLException(nested);
    }
  }


  boolean scanForDriver(String url) {
    try {
      // already registered
      if (findRegisteredDriver(url) != null) {
        return true;
      }

      // first try known drivers...
      scanDrivers(true);

      if (findRegisteredDriver(url) != null) {
        return true;
      }

      // now really scan...
      scanDrivers(false);

      if (findRegisteredDriver(url) != null) {
        return true;
      }

      // find whether exists a local driver to accept the url
      if (findLocalDriver(url) != null) {
        return true;
      }

      return false;
    } catch (Exception e) {
      debug(e.toString());
      return false;
    }
  }


  private Driver findRegisteredDriver(String url) {
    for (Enumeration drivers = DriverManager.getDrivers(); drivers != null
        && drivers.hasMoreElements();) {
      Driver driver = (Driver) drivers.nextElement();
      try {
        if (driver.acceptsURL(url)) {
          return driver;
        }
      } catch (Exception e) {
      }
    }
    return null;
  }

  public Driver findLocalDriver(String url) throws Exception {
    if(drivers == null){
      return null;
    }

    for (Driver d : drivers) {
      try {
        String clazzName = d.getClass().getName();
        Driver driver = (Driver) Class.forName(clazzName, true,
          Thread.currentThread().getContextClassLoader()).newInstance();
        if (driver.acceptsURL(url) && isSupportedLocalDriver(driver)) {
          return driver;
        }
      } catch (SQLException e) {
        error(e);
        throw new Exception(e);
      }
    }
    return null;
  }

  public boolean isSupportedLocalDriver(Driver driver) {
    String driverName = driver.getClass().getName();
    for (String name : supportedLocalDriver) {
      if (name.equals(driverName)) {
        return true;
      }
    }
    return false;
  }

  public void addLocalDriverClazz(String driverClazz) {
    supportedLocalDriver.add(driverClazz);
  }

  Driver[] scanDrivers(String line) throws IOException {
    return scanDrivers(false);
  }

  //扫描本地所有的driver实例对象
    //参数true表示仅仅扫描已经知道的,false表示扫描所有jar包
  Driver[] scanDrivers(boolean knownOnly) throws IOException {
    long start = System.currentTimeMillis();

    Set<String> classNames = new HashSet<String>();

    if (!knownOnly) {
      classNames.addAll(Arrays.asList(
          ClassNameCompleter.getClassNames()));
    }

    classNames.addAll(KNOWN_DRIVERS);

    Set driverClasses = new HashSet();

    for (Iterator<String> i = classNames.iterator(); i.hasNext();) {//循环所有的class类
      String className = i.next().toString();

      if (className.toLowerCase().indexOf("driver") == -1) {//说明该类肯定不是driver
        continue;
      }

      try {
        Class c = Class.forName(className, false,
            Thread.currentThread().getContextClassLoader());
        if (!Driver.class.isAssignableFrom(c)) {//说明该类不是driver
          continue;
        }

        if (Modifier.isAbstract(c.getModifiers())) {//说明是抽象类,也不可以
          continue;
        }

        // now instantiate and initialize it
        driverClasses.add(c.newInstance());
      } catch (Throwable t) {
      }
    }
    info("scan complete in "
        + (System.currentTimeMillis() - start) + "ms");
    return (Driver[]) driverClasses.toArray(new Driver[0]);
  }

  //老的方式扫描所有的driver实例
  private Driver[] scanDriversOLD(String line) {
    long start = System.currentTimeMillis();

    Set<String> paths = new HashSet<String>();
    Set driverClasses = new HashSet();

    for (StringTokenizer tok = new StringTokenizer(
        System.getProperty("java.ext.dirs"),
        System.getProperty("path.separator")); tok.hasMoreTokens();) {
      File[] files = new File(tok.nextToken()).listFiles();
      for (int i = 0; files != null && i < files.length; i++) {
        paths.add(files[i].getAbsolutePath());
      }
    }

    for (StringTokenizer tok = new StringTokenizer(
        System.getProperty("java.class.path"),
        System.getProperty("path.separator")); tok.hasMoreTokens();) {
      paths.add(new File(tok.nextToken()).getAbsolutePath());
    }

    for (Iterator<String> i = paths.iterator(); i.hasNext();) {
      File f = new File(i.next());
      output(getColorBuffer().pad(loc("scanning", f.getAbsolutePath()), 60),
          false);

      try {
        ZipFile zf = new ZipFile(f);
        int total = zf.size();
        int index = 0;

        for (Enumeration zfEnum = zf.entries(); zfEnum.hasMoreElements();) {
          ZipEntry entry = (ZipEntry) zfEnum.nextElement();
          String name = entry.getName();
          progress(index++, total);

          if (name.endsWith(".class")) {
            name = name.replace('/', '.');
            name = name.substring(0, name.length() - 6);

            try {
              // check for the string "driver" in the class
              // to see if we should load it. Not perfect, but
              // it is far too slow otherwise.
              if (name.toLowerCase().indexOf("driver") != -1) {
                Class c = Class.forName(name, false,
                    getClass().getClassLoader());
                if (Driver.class.isAssignableFrom(c)
                    && !(Modifier.isAbstract(
                        c.getModifiers()))) {
                  try {
                    // load and initialize
                    Class.forName(name);
                  } catch (Exception e) {
                  }
                  driverClasses.add(c.newInstance());
                }
              }
            } catch (Throwable t) {
            }
          }
        }
        progress(total, total);
      } catch (Exception e) {
      }
    }

    info("scan complete in "
        + (System.currentTimeMillis() - start) + "ms");
    return (Driver[]) driverClasses.toArray(new Driver[0]);
  }


  // /////////////////////////////////////
  // ResultSet output formatting classes
  // /////////////////////////////////////



  int print(ResultSet rs) throws SQLException {
    String format = getOpts().getOutputFormat();//使用什么形式输出查询的结果集
    OutputFormat f = (OutputFormat) formats.get(format);//找到对应的输出类型的实例

    if (f == null) {//默认使用table方式
      error(loc("unknown-format", new Object[] {
          format, formats.keySet()}));
      f = new TableOutputFormat(this);
    }

    Rows rows;
    //如何读取数据集内容
    if (getOpts().getIncremental()) {
      rows = new IncrementalRows(this, rs);
    } else {
      rows = new BufferedRows(this, rs);
    }
    return f.print(rows);//进行打印数据
  }


  Statement createStatement() throws SQLException {
    Statement stmnt = getDatabaseConnection().getConnection().createStatement();
    if (getOpts().timeout > -1) {
      stmnt.setQueryTimeout(getOpts().timeout);
    }
    if (signalHandler != null) {
      signalHandler.setStatement(stmnt);
    }
    return stmnt;
  }

  //运行一个批处理sql,参数是sql集合
  void runBatch(List<String> statements) {
    try {
      Statement stmnt = createStatement();
      try {
        for (Iterator<String> i = statements.iterator(); i.hasNext();) {
          stmnt.addBatch(i.next().toString());//在Statement中添加一个批处理,将sql都添加到批处理中
        }
        int[] counts = stmnt.executeBatch();//执行批处理

        output(getColorBuffer().pad(getColorBuffer().bold("COUNT"), 8)
            .append(getColorBuffer().bold("STATEMENT")));

        for (int i = 0; counts != null && i < counts.length; i++) {
          output(getColorBuffer().pad(counts[i] + "", 8)
              .append(statements.get(i).toString()));//打印每一个sql执行的结果影响的行数
        }
      } finally {
        try {
          stmnt.close();
        } catch (Exception e) {
        }
      }
    } catch (Exception e) {
      handleException(e);
    }
  }

  public int runCommands(String[] cmds) {
    return runCommands(Arrays.asList(cmds));
  }

  //执行一组命令--返回成功执行的命令次数
  public int runCommands(List<String> cmds) {
    int successCount = 0;//执行成功的命令集合
    try {
      // TODO: Make script output prefixing configurable. Had to disable this since
      // it results in lots of test diffs.
      for (String cmd : cmds) {
        info(getColorBuffer().pad(SCRIPT_OUTPUT_PREFIX, SCRIPT_OUTPUT_PAD_SIZE).append(cmd));//打印命令 >>>cmd
        // if we do not force script execution, abort
        // when a failure occurs.
        if (dispatch(cmd) || getOpts().getForce()) {//执行每一个命令
          ++successCount;
        } else {
          error(loc("abort-on-error", cmd));
          return successCount;
        }
      }
    } catch (Exception e) {
      handleException(e);
    }
    return successCount;
  }

  // ////////////////////////
  // Command methods follow
  // ////////////////////////
  //设置自动补全
  void setCompletions() throws SQLException, IOException {
    if (getDatabaseConnection() != null) {
      getDatabaseConnection().setCompletions(getOpts().getFastConnect());
    }
  }

  public BeeLineOpts getOpts() {
    return opts;
  }

  DatabaseConnections getDatabaseConnections() {
    return connections;
  }

  Completer getCommandCompletor() {
    return beeLineCommandCompleter;
  }

  public boolean isExit() {
    return exit;
  }

  public void setExit(boolean exit) {
    this.exit = exit;
  }

  Collection<Driver> getDrivers() {
    return drivers;
  }

  void setDrivers(Collection<Driver> drivers) {
    this.drivers = drivers;
  }

  public static String getSeparator() {
    return separator;
  }

  Commands getCommands() {
    return commands;
  }

  OutputFile getScriptOutputFile() {
    return scriptOutputFile;
  }

  void setScriptOutputFile(OutputFile script) {
    this.scriptOutputFile = script;
  }

  OutputFile getRecordOutputFile() {
    return recordOutputFile;
  }

  void setRecordOutputFile(OutputFile record) {
    this.recordOutputFile = record;
  }

  public void setOutputStream(PrintStream outputStream) {
    this.outputStream = new PrintStream(outputStream, true);
  }

  PrintStream getOutputStream() {
    return outputStream;
  }

  public void setErrorStream(PrintStream errorStream) {
    this.errorStream = new PrintStream(errorStream, true);
  }

  PrintStream getErrorStream() {
    return errorStream;
  }

  ConsoleReader getConsoleReader() {
    return consoleReader;
  }

  void setConsoleReader(ConsoleReader reader) {
    this.consoleReader = reader;
  }

  List<String> getBatch() {
    return batch;
  }

  void setBatch(List<String> batch) {
    this.batch = batch;
  }

  protected Reflector getReflector() {
    return reflector;
  }
}
