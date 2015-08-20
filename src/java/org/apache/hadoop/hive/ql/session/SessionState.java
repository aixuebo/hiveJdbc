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

package org.apache.hadoop.hive.ql.session;
import static org.apache.hadoop.hive.metastore.MetaStoreUtils.DEFAULT_DATABASE_NAME;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.conf.HiveConf.ConfVars;
import org.apache.hadoop.hive.ql.MapRedStats;
import org.apache.hadoop.hive.ql.exec.Utilities;
import org.apache.hadoop.hive.ql.history.HiveHistory;
import org.apache.hadoop.hive.ql.history.HiveHistoryImpl;
import org.apache.hadoop.hive.ql.history.HiveHistoryProxyHandler;
import org.apache.hadoop.hive.ql.log.PerfLogger;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.metadata.HiveUtils;
import org.apache.hadoop.hive.ql.plan.HiveOperation;
import org.apache.hadoop.hive.ql.security.HiveAuthenticationProvider;
import org.apache.hadoop.hive.ql.security.authorization.HiveAuthorizationProvider;
import org.apache.hadoop.hive.ql.util.DosToUnix;
import org.apache.hadoop.util.ReflectionUtils;

/**
 * SessionState encapsulates common data associated with a session.
 *
 * Also provides support for a thread static session object that can be accessed
 * from any point in the code to interact with the user and to retrieve
 * configuration information
 */
public class SessionState {
  private static final Log LOG = LogFactory.getLog(SessionState.class);

  /**
   * current configuration.
   */
  protected HiveConf conf;

  /**
   * silent mode.�Ƿ��Ǿ�Ĭģʽ,��Ĭ�ϲ����ӡok,������ʱ�����Ϣ
   */
  protected boolean isSilent;

  /**
   * verbose mode
   */
  protected boolean isVerbose;

  /*
   * HiveHistory Object HiveHistoryImplʵ����
   */
  protected HiveHistory hiveHist;

  /**
   * Streams to read/write from.
   */
  public InputStream in;
  public PrintStream out;
  public PrintStream info;
  public PrintStream err;
  /**
   * Standard output from any child process(es).
   */
  public PrintStream childOut;
  /**
   * Error output from any child process(es).
   */
  public PrintStream childErr;

  /**
   * Temporary file name used to store results of non-Hive commands (e.g., set, dfs)
   * and HiveServer.fetch*() function will read results from this file
   * Ĭ��/tmp/user/sessionIdxxxxx.pipeoutΪ��ʱ�ļ�
   */
  protected File tmpOutputFile;

  /**
   * type of the command.
   */
  private HiveOperation commandType;

  private HiveAuthorizationProvider authorizer;

  private HiveAuthenticationProvider authenticator;

  private CreateTableAutomaticGrant createTableGrants;

  private List<MapRedStats> lastMapRedStatsList;

  private Map<String, String> hiveVariables;

  // A mapping from a hadoop job ID to the stack traces collected from the map reduce task logs
  private Map<String, List<List<String>>> stackTraces;

  // This mapping collects all the configuration variables which have been set by the user
  // explicitely, either via SET in the CLI, the hiveconf option, or a System property.
  // It is a mapping from the variable name to its value.  Note that if a user repeatedly
  // changes the value of a variable, the corresponding change will be made in this mapping.
  private Map<String, String> overriddenConfigurations;

  private Map<String, List<String>> localMapRedErrors;

  private String currentDatabase;

  /**
   * Lineage state.
   */
  LineageState ls;

  private PerfLogger perfLogger;
  /**
   * Get the lineage state stored in this session.
   *
   * @return LineageState
   */
  public LineageState getLineageState() {
    return ls;
  }

  public HiveConf getConf() {
    return conf;
  }

  public void setConf(HiveConf conf) {
    this.conf = conf;
  }

  public File getTmpOutputFile() {
    return tmpOutputFile;
  }

  public void setTmpOutputFile(File f) {
    tmpOutputFile = f;
  }

  public boolean getIsSilent() {
    if(conf != null) {
      return conf.getBoolVar(HiveConf.ConfVars.HIVESESSIONSILENT);
    } else {
      return isSilent;
    }
  }

  public void setIsSilent(boolean isSilent) {
    if(conf != null) {
      conf.setBoolVar(HiveConf.ConfVars.HIVESESSIONSILENT, isSilent);
    }
    this.isSilent = isSilent;
  }

  public boolean getIsVerbose() {
    return isVerbose;
  }

  public void setIsVerbose(boolean isVerbose) {
    this.isVerbose = isVerbose;
  }

  public SessionState(HiveConf conf) {
    this.conf = conf;
    isSilent = conf.getBoolVar(HiveConf.ConfVars.HIVESESSIONSILENT);
    ls = new LineageState();
    overriddenConfigurations = new HashMap<String, String>();
    //����,���һ�ȡsystemϵͳ���õ�����name-value����
    overriddenConfigurations.putAll(HiveConf.getConfSystemProperties());
    // if there isn't already a session name, go ahead and create it. ÿһ��session��ʾ,û���򴴽�һ��
    if (StringUtils.isEmpty(conf.getVar(HiveConf.ConfVars.HIVESESSIONID))) {
      conf.setVar(HiveConf.ConfVars.HIVESESSIONID, makeSessionId());
    }
  }

  private static final SimpleDateFormat DATE_FORMAT =
      new SimpleDateFormat("yyyyMMddHHmm");

  public void setCmd(String cmdString) {
    conf.setVar(HiveConf.ConfVars.HIVEQUERYSTRING, cmdString);
  }

  public String getCmd() {
    return (conf.getVar(HiveConf.ConfVars.HIVEQUERYSTRING));
  }

  public String getQueryId() {
    return (conf.getVar(HiveConf.ConfVars.HIVEQUERYID));
  }

  public Map<String, String> getHiveVariables() {
    if (hiveVariables == null) {
      hiveVariables = new HashMap<String, String>();
    }
    return hiveVariables;
  }

  public void setHiveVariables(Map<String, String> hiveVariables) {
    this.hiveVariables = hiveVariables;
  }

  public String getSessionId() {
    return (conf.getVar(HiveConf.ConfVars.HIVESESSIONID));
  }

  /**
   * Singleton Session object per thread.
   * ÿ���̰߳�һ��SessionState����
   **/
  private static ThreadLocal<SessionState> tss = new ThreadLocal<SessionState>();

  /**
   * start a new session and set it to current session.
   */
  public static SessionState start(HiveConf conf) {
    SessionState ss = new SessionState(conf);
    return start(ss);
  }

  /**
   * set current session to existing session object if a thread is running
   * multiple sessions - it must call this method with the new session object
   * when switching from one session to another.
   * @throws HiveException
   */
  public static SessionState start(SessionState startSs) {

	  //Ϊ���̰߳󶨸�SessionState����
    tss.set(startSs);

    if(startSs.hiveHist == null){
      if (startSs.getConf().getBoolVar(HiveConf.ConfVars.HIVE_SESSION_HISTORY_ENABLED)) {//�Ƿ��¼��session��Ϣ,��HiveHistoryImpl��־�Ƿ���
        startSs.hiveHist = new HiveHistoryImpl(startSs);
      }else {
        //Hive history is disabled, create a no-op proxy
        startSs.hiveHist = HiveHistoryProxyHandler.getNoOpHiveHistoryProxy();
      }
    }

    //������ʱ�ļ�
    if (startSs.getTmpOutputFile() == null) {
      // set temp file containing results to be sent to HiveClient
      try {
        startSs.setTmpOutputFile(createTempFile(startSs.getConf()));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    try {
      startSs.authenticator = HiveUtils.getAuthenticator(
          startSs.getConf(),HiveConf.ConfVars.HIVE_AUTHENTICATOR_MANAGER);
      startSs.authorizer = HiveUtils.getAuthorizeProviderManager(
          startSs.getConf(), HiveConf.ConfVars.HIVE_AUTHORIZATION_MANAGER,
          startSs.authenticator);
      startSs.createTableGrants = CreateTableAutomaticGrant.create(startSs
          .getConf());
    } catch (HiveException e) {
      throw new RuntimeException(e);
    }

    return startSs;
  }

  /**
   * @param conf
   * @return per-session temp file
   * @throws IOException
   * ������ʱ�ļ� /tmp/user/sessionIdxxxxx.pipeout
   */
  private static File createTempFile(HiveConf conf) throws IOException {
	///tmp/userĬ��hive�ı���Ŀ¼ 
    String lScratchDir =
        HiveConf.getVar(conf, HiveConf.ConfVars.LOCALSCRATCHDIR);

    File tmpDir = new File(lScratchDir);
    String sessionID = conf.getVar(HiveConf.ConfVars.HIVESESSIONID);
    if (!tmpDir.exists()) {
      if (!tmpDir.mkdirs()) {
        //Do another exists to check to handle possible race condition
        // Another thread might have created the dir, if that is why
        // mkdirs returned false, that is fine
        if(!tmpDir.exists()){
          throw new RuntimeException("Unable to create log directory "
              + lScratchDir);
        }
      }
    }
    ///tmp/user/sessionIdxxxxx.pipeout,createTempFile�������Զ�����һ���ļ���
    File tmpFile = File.createTempFile(sessionID, ".pipeout", tmpDir);
    tmpFile.deleteOnExit();//�ļ��˳���ʱ��ɾ����
    return tmpFile;
  }

  /**
   * get the current session.
   * ��ȡ���̶߳�Ӧ��SessionState����
   */
  public static SessionState get() {
    return tss.get();
  }

  /**
   * get hiveHitsory object which does structured logging.
   *
   * @return The hive history object
   */
  public HiveHistory getHiveHistory() {
    return hiveHist;
  }

  /**
   * Create a session ID. Looks like:
   *   $user_$pid@$host_$date
   * @return the unique string
   *  ÿһ��session��ʾ
   */
  private static String makeSessionId() {
    return UUID.randomUUID().toString();
  }

  /**
   * This class provides helper routines to emit informational and error
   * messages to the user and log4j files while obeying the current session's
   * verbosity levels.
   *
   * NEVER write directly to the SessionStates standard output other than to
   * emit result data DO use printInfo and printError provided by LogHelper to
   * emit non result data strings.
   *
   * It is perfectly acceptable to have global static LogHelper objects (for
   * example - once per module) LogHelper always emits info/error to current
   * session as required.
   */
  public static class LogHelper {

    protected Log LOG;//log����
    protected boolean isSilent;//�Ƿ�̬ģʽ,����ok������ʱ����Ϣ

    public LogHelper(Log LOG) {
      this(LOG, false);
    }

    public LogHelper(Log LOG, boolean isSilent) {
      this.LOG = LOG;
      this.isSilent = isSilent;
    }

    public PrintStream getOutStream() {
      SessionState ss = SessionState.get();
      return ((ss != null) && (ss.out != null)) ? ss.out : System.out;
    }

    public PrintStream getInfoStream() {
      SessionState ss = SessionState.get();
      return ((ss != null) && (ss.info != null)) ? ss.info : getErrStream();
    }

    public PrintStream getErrStream() {
      SessionState ss = SessionState.get();
      return ((ss != null) && (ss.err != null)) ? ss.err : System.err;
    }

    public PrintStream getChildOutStream() {
      SessionState ss = SessionState.get();
      return ((ss != null) && (ss.childOut != null)) ? ss.childOut : System.out;
    }

    public PrintStream getChildErrStream() {
      SessionState ss = SessionState.get();
      return ((ss != null) && (ss.childErr != null)) ? ss.childErr : System.err;
    }

    public boolean getIsSilent() {
      SessionState ss = SessionState.get();
      // use the session or the one supplied in constructor
      return (ss != null) ? ss.getIsSilent() : isSilent;
    }

    public void printInfo(String info) {
      printInfo(info, null);
    }

    public void printInfo(String info, String detail) {
      if (!getIsSilent()) {
        getInfoStream().println(info);
      }
      LOG.info(info + StringUtils.defaultString(detail));
    }

    public void printError(String error) {
      printError(error, null);
    }

    public void printError(String error, String detail) {
      getErrStream().println(error);
      LOG.error(error + StringUtils.defaultString(detail));
    }
  }

  private static LogHelper _console;

  /**
   * initialize or retrieve console object for SessionState.
   */
  public static LogHelper getConsole() {
    if (_console == null) {
      Log LOG = LogFactory.getLog("SessionState");
      _console = new LogHelper(LOG);
    }
    return _console;
  }

  // ��ʽ��newFile�ļ�·��,��������ڸ�·��,�򷵻�null
  public static String validateFile(Set<String> curFiles, String newFile) {
    SessionState ss = SessionState.get();
    LogHelper console = getConsole();//��ȡLOG��־�ļ�
    Configuration conf = (ss == null) ? new Configuration() : ss.getConf();

    try {
    	//��ʽ��newFile�ļ�·��,��������ڸ�·��,�򷵻�null
      if (Utilities.realFile(newFile, conf) != null) {
        return newFile;
      } else {
        console.printError(newFile + " does not exist");
        return null;
      }
    } catch (IOException e) {
      console.printError("Unable to validate " + newFile + "\nException: "
          + e.getMessage(), "\n"
              + org.apache.hadoop.util.StringUtils.stringifyException(e));
      return null;
    }
  }

  //��newPaths�ļ����ϼ��ص�cloader��,newJar�����ö��Ų��
  public static boolean registerJar(String newJar) {
    LogHelper console = getConsole();
    try {
      ClassLoader loader = Thread.currentThread().getContextClassLoader();
      //��newPaths�ļ����ϼ��ص�cloader��
      ClassLoader newLoader = Utilities.addToClassPath(loader, StringUtils.split(newJar, ","));
      Thread.currentThread().setContextClassLoader(newLoader);
      SessionState.get().getConf().setClassLoader(newLoader);
      console.printInfo("Added " + newJar + " to class path");
      return true;
    } catch (Exception e) {
      console.printError("Unable to register " + newJar + "\nException: "
          + e.getMessage(), "\n"
              + org.apache.hadoop.util.StringUtils.stringifyException(e));
      return false;
    }
  }

  //��pathsToRemove�ļ����ϴ�cloader���Ƴ�    ,jarsToUnregister�����ö��Ų��
  public static boolean unregisterJar(String jarsToUnregister) {
    LogHelper console = getConsole();
    try {
      Utilities.removeFromClassPath(StringUtils.split(jarsToUnregister, ","));
      console.printInfo("Deleted " + jarsToUnregister + " from class path");
      return true;
    } catch (Exception e) {
      console.printError("Unable to unregister " + jarsToUnregister
          + "\nException: " + e.getMessage(), "\n"
              + org.apache.hadoop.util.StringUtils.stringifyException(e));
      return false;
    }
  }

  /**
   * ResourceHook.
   *
   */
  public static interface ResourceHook {
    String preHook(Set<String> cur, String s);

    boolean postHook(Set<String> cur, String s);
  }

  /**
   * ResourceType.
   *
   */
  public static enum ResourceType {
    FILE(new ResourceHook() {
      public String preHook(Set<String> cur, String s) {
        return validateFile(cur, s);
      }

      public boolean postHook(Set<String> cur, String s) {
        return true;
      }
    }),

    JAR(new ResourceHook() {
      public String preHook(Set<String> cur, String s) {
        String newJar = validateFile(cur, s);
        if (newJar != null) {
          return (registerJar(newJar) ? newJar : null);
        } else {
          return null;
        }
      }

      public boolean postHook(Set<String> cur, String s) {
        return unregisterJar(s);
      }
    }),

    ARCHIVE(new ResourceHook() {
      public String preHook(Set<String> cur, String s) {
        return validateFile(cur, s);
      }

      public boolean postHook(Set<String> cur, String s) {
        return true;
      }
    });

    public ResourceHook hook;

    ResourceType(ResourceHook hook) {
      this.hook = hook;
    }
  };

  public static ResourceType find_resource_type(String s) {

    s = s.trim().toUpperCase();

    try {
      return ResourceType.valueOf(s);
    } catch (IllegalArgumentException e) {
    }

    // try singular ���˵��ʵĸ���
    if (s.endsWith("S")) {
      s = s.substring(0, s.length() - 1);
    } else {
      return null;
    }

    try {
      return ResourceType.valueOf(s);
    } catch (IllegalArgumentException e) {
    }
    return null;
  }

  //ÿһ�����͵��ļ����϶�Ӧ��ϵ
  private final HashMap<ResourceType, Set<String>> resource_map = new HashMap<ResourceType, Set<String>>();

  public String add_resource(ResourceType t, String value) {
    // By default don't convert to unix
    return add_resource(t, value, false);
  }

  /**
   * ����һ���Ǳ�����Դvalue,��ResourceType���Ͷ�Ӧ���ļ�������
   * ���ر������غ���ļ�·��
   */
  public String add_resource(ResourceType t, String value, boolean convertToUnix) {
    try {
    	//���طǱ�����Դ������,value����ֵ�Ǳ�����Դ���غ��·��
      value = downloadResource(value, convertToUnix);
    } catch (Exception e) {
      getConsole().printError(e.getMessage());
      return null;
    }

    //��ȡ�����͵��ļ�����
    Set<String> resourceMap = getResourceMap(t);

    String fnlVal = value;
    if (t.hook != null) {
    	//У��value�ļ��Ƿ����
      fnlVal = t.hook.preHook(resourceMap, value);
      if (fnlVal == null) {
        return fnlVal;
      }
    }
    getConsole().printInfo("Added resource: " + fnlVal);
    //����value�ļ���ӵ������Ͷ�Ӧ���ļ�������
    resourceMap.add(fnlVal);

    return fnlVal;
  }

  //��value��ӵ�ResourceType��Ӧ���ļ�������
  public void add_builtin_resource(ResourceType t, String value) {
    getResourceMap(t).add(value);
  }

  //���ظ����͵���Դ�ļ�����
  private Set<String> getResourceMap(ResourceType t) {
    Set<String> result = resource_map.get(t);
    if (result == null) {
      result = new HashSet<String>();
      resource_map.put(t, result);
    }
    return result;
  }

  /**
   * Returns  true if it is from any external File Systems except local
   * true ��ʾ��value��Ӧ���ļ����Ǳ����ļ�ϵͳ,�������ⲿ�ļ�ϵͳ,����HDFS
   */
  public static boolean canDownloadResource(String value) {
    // Allow to download resources from any external FileSystem.
    // And no need to download if it already exists on local file system.
    String scheme = new Path(value).toUri().getScheme();
    return (scheme != null) && !scheme.equalsIgnoreCase("file");
  }

  /**
   * ����value��Ӧ���ļ���Դ
   * @param value
   * @param convertToUnix true��ʾwindosĿ¼ת����unixĿ¼��Ҫת��һ��
   */
  private String downloadResource(String value, boolean convertToUnix) {
    if (canDownloadResource(value)) {//�ж��Ƿ���Ҫ����value�ļ���Դ
      getConsole().printInfo("converting to local " + value);
      ////��Դ���ش洢��Ŀ¼ /tmp/sessionId_resources/
      File resourceDir = new File(getConf().getVar(HiveConf.ConfVars.DOWNLOADED_RESOURCES_DIR));
      String destinationName = new Path(value).getName();
      File destinationFile = new File(resourceDir, destinationName);//��Դ���غ�洢��Ŀ¼+����
      if (resourceDir.exists() && ! resourceDir.isDirectory()) {//�жϸô洢������Դ��Ŀ¼�������,������Ŀ¼
        throw new RuntimeException("The resource directory is not a directory, resourceDir is set to" + resourceDir);
      }
      if (!resourceDir.exists() && !resourceDir.mkdirs()) {//���Ŀ¼������,�򴴽�
        throw new RuntimeException("Couldn't create directory " + resourceDir);
      }
      try {
        FileSystem fs = FileSystem.get(new URI(value), conf);
        //��value�������������������ػ���
        fs.copyToLocalFile(new Path(value), new Path(destinationFile.getCanonicalPath()));
        value = destinationFile.getCanonicalPath();
        if (convertToUnix && DosToUnix.isWindowsScript(destinationFile)) {
          try {
            DosToUnix.convertWindowsScriptToUnix(destinationFile);
          } catch (Exception e) {
            throw new RuntimeException("Caught exception while converting file " +
                destinationFile + " to unix line endings", e);
          }
        }
      } catch (Exception e) {
        throw new RuntimeException("Failed to read external resource " + value, e);
      }
    }
    return value;
  }

  //����Դ����ɾ����value��Դ
  public boolean delete_resource(ResourceType t, String value) {
    if (resource_map.get(t) == null) {
      return false;
    }
    if (t.hook != null) {
      if (!t.hook.postHook(resource_map.get(t), value)) {
        return false;
      }
    }
    return (resource_map.get(t).remove(value));
  }

  /**
   * ��t��Դ���л�ȡ��Դ,����Դһ��Ҫ��filter��Դ������
   * ���filter����null��˵��������,������t����Դ�����ؼ���
   */
  public Set<String> list_resource(ResourceType t, List<String> filter) {
    if (resource_map.get(t) == null) {
      return null;
    }
    Set<String> orig = resource_map.get(t);
    if (filter == null) {
      return orig;
    } else {
      Set<String> fnl = new HashSet<String>();
      for (String one : orig) {
        if (filter.contains(one)) {
          fnl.add(one);
        }
      }
      return fnl;
    }
  }

  //ɾ��ResourceType�����ж�Ӧ����Դ�ļ�
  public void delete_resource(ResourceType t) {
    if (resource_map.get(t) != null) {
      for (String value : resource_map.get(t)) {
        delete_resource(t, value);
      }
      resource_map.remove(t);
    }
  }

  public String getCommandType() {
    if (commandType == null) {
      return null;
    }
    return commandType.getOperationName();
  }

  public HiveOperation getHiveOperation() {
    return commandType;
  }

  public void setCommandType(HiveOperation commandType) {
    this.commandType = commandType;
  }

  public HiveAuthorizationProvider getAuthorizer() {
    return authorizer;
  }

  public void setAuthorizer(HiveAuthorizationProvider authorizer) {
    this.authorizer = authorizer;
  }

  public HiveAuthenticationProvider getAuthenticator() {
    return authenticator;
  }

  public void setAuthenticator(HiveAuthenticationProvider authenticator) {
    this.authenticator = authenticator;
  }

  public CreateTableAutomaticGrant getCreateTableGrants() {
    return createTableGrants;
  }

  public void setCreateTableGrants(CreateTableAutomaticGrant createTableGrants) {
    this.createTableGrants = createTableGrants;
  }

  public List<MapRedStats> getLastMapRedStatsList() {
    return lastMapRedStatsList;
  }

  public void setLastMapRedStatsList(List<MapRedStats> lastMapRedStatsList) {
    this.lastMapRedStatsList = lastMapRedStatsList;
  }

  public void setStackTraces(Map<String, List<List<String>>> stackTraces) {
    this.stackTraces = stackTraces;
  }

  public Map<String, List<List<String>>> getStackTraces() {
    return stackTraces;
  }

  public Map<String, String> getOverriddenConfigurations() {
    if (overriddenConfigurations == null) {
      overriddenConfigurations = new HashMap<String, String>();
    }
    return overriddenConfigurations;
  }

  public void setOverriddenConfigurations(Map<String, String> overriddenConfigurations) {
    this.overriddenConfigurations = overriddenConfigurations;
  }

  public Map<String, List<String>> getLocalMapRedErrors() {
    return localMapRedErrors;
  }

  public void addLocalMapRedErrors(String id, List<String> localMapRedErrors) {
    if (!this.localMapRedErrors.containsKey(id)) {
      this.localMapRedErrors.put(id, new ArrayList<String>());
    }

    this.localMapRedErrors.get(id).addAll(localMapRedErrors);
  }

  public void setLocalMapRedErrors(Map<String, List<String>> localMapRedErrors) {
    this.localMapRedErrors = localMapRedErrors;
  }

  public String getCurrentDatabase() {
    if (currentDatabase == null) {
      currentDatabase = DEFAULT_DATABASE_NAME;
    }
    return currentDatabase;
  }

  public void setCurrentDatabase(String currentDatabase) {
    this.currentDatabase = currentDatabase;
  }

  //
  public void close() throws IOException {
    File resourceDir =
        new File(getConf().getVar(HiveConf.ConfVars.DOWNLOADED_RESOURCES_DIR));////��Դ���ش洢��Ŀ¼ /tmp/sessionId_resources/
    LOG.debug("Removing resource dir " + resourceDir);
    try {
    	//������Դ��Ϣ��ɾ����
      if (resourceDir.exists()) {
        FileUtils.deleteDirectory(resourceDir);
      }
    } catch (IOException e) {
      LOG.info("Error removing session resource dir " + resourceDir, e);
    }
  }

  /**
   * @param resetPerfLogger
   * @return  Tries to return an instance of the class whose name is configured in
   *          hive.exec.perf.logger, but if it can't it just returns an instance of
   *          the base PerfLogger class

   */
  public PerfLogger getPerfLogger(boolean resetPerfLogger) {
    if ((perfLogger == null) || resetPerfLogger) {
      try {
        perfLogger = (PerfLogger) ReflectionUtils.newInstance(conf.getClassByName(
            conf.getVar(ConfVars.HIVE_PERF_LOGGER)), conf);
      } catch (ClassNotFoundException e) {
        LOG.error("Performance Logger Class not found:" + e.getMessage());
        perfLogger = new PerfLogger();
      }
    }
    return perfLogger;
  }

}
