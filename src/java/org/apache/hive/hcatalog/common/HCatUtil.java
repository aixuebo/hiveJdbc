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
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.hive.hcatalog.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.permission.FsAction;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.apache.hadoop.hive.metastore.IMetaStoreClient;
import org.apache.hadoop.hive.metastore.MetaStoreUtils;
import org.apache.hadoop.hive.metastore.api.FieldSchema;
import org.apache.hadoop.hive.metastore.api.MetaException;
import org.apache.hadoop.hive.metastore.api.NoSuchObjectException;
import org.apache.hadoop.hive.ql.exec.Utilities;
import org.apache.hadoop.hive.ql.io.IgnoreKeyTextOutputFormat;
import org.apache.hadoop.hive.ql.metadata.HiveStorageHandler;
import org.apache.hadoop.hive.ql.metadata.Partition;
import org.apache.hadoop.hive.ql.metadata.Table;
import org.apache.hadoop.hive.ql.plan.TableDesc;
import org.apache.hadoop.hive.serde.serdeConstants;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfoUtils;
import org.apache.hadoop.hive.thrift.DelegationTokenIdentifier;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.security.token.Token;
import org.apache.hadoop.security.token.delegation.AbstractDelegationTokenIdentifier;
import org.apache.hadoop.util.ReflectionUtils;
import org.apache.hive.hcatalog.data.Pair;
import org.apache.hive.hcatalog.data.schema.HCatFieldSchema;
import org.apache.hive.hcatalog.data.schema.HCatSchema;
import org.apache.hive.hcatalog.data.schema.HCatSchemaUtils;
import org.apache.hive.hcatalog.mapreduce.FosterStorageHandler;
import org.apache.hive.hcatalog.mapreduce.HCatOutputFormat;
import org.apache.hive.hcatalog.mapreduce.InputJobInfo;
import org.apache.hive.hcatalog.mapreduce.OutputJobInfo;
import org.apache.hive.hcatalog.mapreduce.PartInfo;
import org.apache.hive.hcatalog.mapreduce.StorerInfo;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

public class HCatUtil {

  private static final Logger LOG = LoggerFactory.getLogger(HCatUtil.class);
  private static volatile HiveClientCache hiveClientCache;

  //true表示该job以前运行过
  public static boolean checkJobContextIfRunningFromBackend(JobContext j) {
    if (j.getConfiguration().get("pig.job.converted.fetch", "").equals("") &&
          j.getConfiguration().get("mapred.task.id", "").equals("") &&
          !("true".equals(j.getConfiguration().get("pig.illustrating")))) {
      return false;
    }
    return true;
  }

    //序列化,对java对象进行序列化,然后将结果进行base64编码
  public static String serialize(Serializable obj) throws IOException {
    if (obj == null) {
      return "";
    }
    try {
      ByteArrayOutputStream serialObj = new ByteArrayOutputStream();//字节输出流
      ObjectOutputStream objStream = new ObjectOutputStream(serialObj);//对象输出流,底层还是接的字节输出流
      objStream.writeObject(obj);//将对象写出到输出流中
      objStream.close();
      return encodeBytes(serialObj.toByteArray());
    } catch (Exception e) {
      throw new IOException("Serialization error: " + e.getMessage(), e);
    }
  }

    //反序列化---对str进行反base64编码,然后就是java的一个序列化
  public static Object deserialize(String str) throws IOException {
    if (str == null || str.length() == 0) {
      return null;
    }
    try {
      ByteArrayInputStream serialObj = new ByteArrayInputStream(
        decodeBytes(str));
      ObjectInputStream objStream = new ObjectInputStream(serialObj);
      return objStream.readObject();
    } catch (Exception e) {
      throw new IOException("Deserialization error: " + e.getMessage(), e);
    }
  }

  public static String encodeBytes(byte[] bytes) {
    return new String(Base64.encodeBase64(bytes, false, false));
  }

  public static byte[] decodeBytes(String str) {
    return Base64.decodeBase64(str.getBytes());
  }

  public static List<HCatFieldSchema> getHCatFieldSchemaList(
    FieldSchema... fields) throws HCatException {
    List<HCatFieldSchema> result = new ArrayList<HCatFieldSchema>(
      fields.length);

    for (FieldSchema f : fields) {
      result.add(HCatSchemaUtils.getHCatFieldSchema(f));
    }

    return result;
  }

  //参数是hive的一个表的列集合,转换成hcatalog对应的列对象集合
  public static List<HCatFieldSchema> getHCatFieldSchemaList(
    List<FieldSchema> fields) throws HCatException {
    if (fields == null) {
      return null;
    } else {
      List<HCatFieldSchema> result = new ArrayList<HCatFieldSchema>();
      for (FieldSchema f : fields) {
        result.add(HCatSchemaUtils.getHCatFieldSchema(f));
      }
      return result;
    }
  }

 //hive表中列集合转换成hcatalog的列集合组成的对象
  public static HCatSchema extractSchema(Table table) throws HCatException {
    return new HCatSchema(HCatUtil.getHCatFieldSchemaList(table.getCols()));
  }

  //hive分区列集合组成的对象集合
  public static HCatSchema extractSchema(Partition partition) throws HCatException {
    return new HCatSchema(HCatUtil.getHCatFieldSchemaList(partition.getCols()));
  }

  public static List<FieldSchema> getFieldSchemaList(
    List<HCatFieldSchema> hcatFields) {
    if (hcatFields == null) {
      return null;
    } else {
      List<FieldSchema> result = new ArrayList<FieldSchema>();
      for (HCatFieldSchema f : hcatFields) {
        result.add(HCatSchemaUtils.getFieldSchema(f));
      }
      return result;
    }
  }

  //获取一个table的元数据
  public static Table getTable(IMetaStoreClient client, String dbName, String tableName)
    throws NoSuchObjectException, TException, MetaException {
    return new Table(client.getTable(dbName, tableName));
  }

  public static HCatSchema getTableSchemaWithPtnCols(Table table) throws IOException {
    HCatSchema tableSchema = new HCatSchema(HCatUtil.getHCatFieldSchemaList(table.getCols()));

    if (table.getPartitionKeys().size() != 0) {

      // add partition keys to table schema
      // NOTE : this assumes that we do not ever have ptn keys as columns
      // inside the table schema as well!
      for (FieldSchema fs : table.getPartitionKeys()) {//分区列
        tableSchema.append(HCatSchemaUtils.getHCatFieldSchema(fs));
      }
    }
    return tableSchema;
  }

  /**
   * return the partition columns from a table instance
   *
   * @param table the instance to extract partition columns from
   * @return HCatSchema instance which contains the partition columns
   * @throws IOException
   * 获取hive表的分区列
   */
  public static HCatSchema getPartitionColumns(Table table) throws IOException {
    HCatSchema cols = new HCatSchema(new LinkedList<HCatFieldSchema>());
    if (table.getPartitionKeys().size() != 0) {
      for (FieldSchema fs : table.getPartitionKeys()) {
        cols.append(HCatSchemaUtils.getHCatFieldSchema(fs));
      }
    }
    return cols;
  }

  /**
   * Validate partition schema, checks if the column types match between the
   * partition and the existing table schema. Returns the list of columns
   * present in the partition but not in the table.
   *
   * @param table the table hive的一个表
   * @param partitionSchema the partition schema 分区字段集合
   * @return the list of newly added fields
   * @throws IOException Signals that an I/O exception has occurred.
   * 校验partition的schema
   * partition中列的顺序必须是table中列的顺序,但是partition中列可以不再table中,因此返回不再table中的列集合
   */
  public static List<FieldSchema> validatePartitionSchema(Table table,
                              HCatSchema partitionSchema) throws IOException {
    Map<String, FieldSchema> partitionKeyMap = new HashMap<String, FieldSchema>();//获取hive表分区列的列name和列对象类型

    for (FieldSchema field : table.getPartitionKeys()) {//循环数据库表的分区列字段
      partitionKeyMap.put(field.getName().toLowerCase(), field);
    }

    List<FieldSchema> tableCols = table.getCols();//table表的列集合
    List<FieldSchema> newFields = new ArrayList<FieldSchema>();//新的列集合

    for (int i = 0; i < partitionSchema.getFields().size(); i++) {//循环catalog定义的分区列

      FieldSchema field = HCatSchemaUtils.getFieldSchema(partitionSchema
        .getFields().get(i));//转换成hive的列对象

      FieldSchema tableField;
      if (i < tableCols.size()) {
        tableField = tableCols.get(i);

        if (!tableField.getName().equalsIgnoreCase(field.getName())) {//发现定义的列顺序不是table列的顺序
          throw new HCatException(
            ErrorType.ERROR_SCHEMA_COLUMN_MISMATCH,
            "Expected column <" + tableField.getName()
              + "> at position " + (i + 1)
              + ", found column <" + field.getName()
              + ">");
        }
      } else {
        tableField = partitionKeyMap.get(field.getName().toLowerCase());//说明该列在分区列中

        if (tableField != null) {
          throw new HCatException(
            ErrorType.ERROR_SCHEMA_PARTITION_KEY, "Key <"
            + field.getName() + ">");
        }
      }

      if (tableField == null) {
        // field present in partition but not in table 说明该列在partition中,但是不在table中
        newFields.add(field);
      } else {//说明列是可以找到的
        // field present in both. validate type has not changed
        TypeInfo partitionType = TypeInfoUtils
          .getTypeInfoFromTypeString(field.getType());//在partition中该列类型
        TypeInfo tableType = TypeInfoUtils
          .getTypeInfoFromTypeString(tableField.getType());//在hive表中该列类型

        if (!partitionType.equals(tableType)) {//说明类型不相同
          String msg =
            "Column <"
            + field.getName() + ">, expected <"
            + tableType.getTypeName() + ">, got <"
            + partitionType.getTypeName() + ">";
          LOG.warn(msg);
          throw new HCatException(ErrorType.ERROR_SCHEMA_TYPE_MISMATCH, msg);
        }
      }
    }

    return newFields;
  }

  /**
   * Test if the first FsAction is more permissive than the second. This is
   * useful in cases where we want to ensure that a file owner has more
   * permissions than the group they belong to, for eg. More completely(but
   * potentially more cryptically) owner-r >= group-r >= world-r : bitwise
   * and-masked with 0444 => 444 >= 440 >= 400 >= 000 owner-w >= group-w >=
   * world-w : bitwise and-masked with &0222 => 222 >= 220 >= 200 >= 000
   * owner-x >= group-x >= world-x : bitwise and-masked with &0111 => 111 >=
   * 110 >= 100 >= 000
   *
   * @return true if first FsAction is more permissive than the second, false
   *         if not.
   */
  public static boolean validateMorePermissive(FsAction first, FsAction second) {
    if ((first == FsAction.ALL) || (second == FsAction.NONE)
      || (first == second)) {
      return true;
    }
    switch (first) {
    case READ_EXECUTE:
      return ((second == FsAction.READ) || (second == FsAction.EXECUTE));
    case READ_WRITE:
      return ((second == FsAction.READ) || (second == FsAction.WRITE));
    case WRITE_EXECUTE:
      return ((second == FsAction.WRITE) || (second == FsAction.EXECUTE));
    }
    return false;
  }

  /**
   * Ensure that read or write permissions are not granted without also
   * granting execute permissions. Essentially, r-- , rw- and -w- are invalid,
   * r-x, -wx, rwx, ---, --x are valid
   *
   * @param perms The FsAction to verify
   * @return true if the presence of read or write permission is accompanied
   *         by execute permissions
   */
  public static boolean validateExecuteBitPresentIfReadOrWrite(FsAction perms) {
    if ((perms == FsAction.READ) || (perms == FsAction.WRITE)
      || (perms == FsAction.READ_WRITE)) {
      return false;
    }
    return true;
  }

  public static Token<org.apache.hadoop.mapreduce.security.token.delegation.DelegationTokenIdentifier> getJobTrackerDelegationToken(
    Configuration conf, String userName) throws Exception {
    // LOG.info("getJobTrackerDelegationToken("+conf+","+userName+")");
    JobClient jcl = new JobClient(new JobConf(conf, HCatOutputFormat.class));
    Token<org.apache.hadoop.mapreduce.security.token.delegation.DelegationTokenIdentifier> t = jcl
      .getDelegationToken(new Text(userName));
    // LOG.info("got "+t);
    return t;

    // return null;
  }

  public static Token<? extends AbstractDelegationTokenIdentifier> extractThriftToken(
    String tokenStrForm, String tokenSignature) throws MetaException,
    TException, IOException {
    // LOG.info("extractThriftToken("+tokenStrForm+","+tokenSignature+")");
    Token<? extends AbstractDelegationTokenIdentifier> t = new Token<DelegationTokenIdentifier>();
    t.decodeFromUrlString(tokenStrForm);
    t.setService(new Text(tokenSignature));
    // LOG.info("returning "+t);
    return t;
  }

  /**
   * Create an instance of a storage handler defined in storerInfo. If one cannot be found
   * then FosterStorageHandler is used to encapsulate the InputFormat, OutputFormat and SerDe.
   * This StorageHandler assumes the other supplied storage artifacts are for a file-based storage system.
   * @param conf job's configuration will be used to configure the Configurable StorageHandler
   * @param storerInfo StorerInfo to definining the StorageHandler and InputFormat, OutputFormat and SerDe
   * @return storageHandler instance
   * @throws IOException
   */
  public static HiveStorageHandler getStorageHandler(Configuration conf, StorerInfo storerInfo) throws IOException {
    return getStorageHandler(conf,
      storerInfo.getStorageHandlerClass(),
      storerInfo.getSerdeClass(),
      storerInfo.getIfClass(),
      storerInfo.getOfClass());
  }

  public static HiveStorageHandler getStorageHandler(Configuration conf, PartInfo partitionInfo) throws IOException {
    return HCatUtil.getStorageHandler(
      conf,
      partitionInfo.getStorageHandlerClassName(),
      partitionInfo.getSerdeClassName(),
      partitionInfo.getInputFormatClassName(),
      partitionInfo.getOutputFormatClassName());
  }

  /**
   * Create an instance of a storage handler. If storageHandler == null,
   * then surrrogate StorageHandler is used to encapsulate the InputFormat, OutputFormat and SerDe.
   * This StorageHandler assumes the other supplied storage artifacts are for a file-based storage system.
   * @param conf job's configuration will be used to configure the Configurable StorageHandler
   * @param storageHandler fully qualified class name of the desired StorageHandle instance
   * @param serDe fully qualified class name of the desired SerDe instance
   * @param inputFormat fully qualified class name of the desired InputFormat instance
   * @param outputFormat fully qualified class name of the desired outputFormat instance
   * @return storageHandler instance
   * @throws IOException
   * 获取定义的hive插件
   */
  public static HiveStorageHandler getStorageHandler(Configuration conf,
                             String storageHandler,
                             String serDe,
                             String inputFormat,
                             String outputFormat)
    throws IOException {

    if ((storageHandler == null) || (storageHandler.equals(FosterStorageHandler.class.getName()))) {
      try {
        FosterStorageHandler fosterStorageHandler =
          new FosterStorageHandler(inputFormat, outputFormat, serDe);
        fosterStorageHandler.setConf(conf);
        return fosterStorageHandler;
      } catch (ClassNotFoundException e) {
        throw new IOException("Failed to load "
          + "foster storage handler", e);
      }
    }

    try {
      Class<? extends HiveStorageHandler> handlerClass =
        (Class<? extends HiveStorageHandler>) Class
          .forName(storageHandler, true, Utilities.getSessionSpecifiedClassLoader());
      return (HiveStorageHandler) ReflectionUtils.newInstance(
        handlerClass, conf);
    } catch (ClassNotFoundException e) {
      throw new IOException("Error in loading storage handler."
        + e.getMessage(), e);
    }
  }

  public static Pair<String, String> getDbAndTableName(String tableName) throws IOException {
    String[] dbTableNametokens = tableName.split("\\.");
    if (dbTableNametokens.length == 1) {
      return new Pair<String, String>(MetaStoreUtils.DEFAULT_DATABASE_NAME, tableName);
    } else if (dbTableNametokens.length == 2) {
      return new Pair<String, String>(dbTableNametokens[0], dbTableNametokens[1]);
    } else {
      throw new IOException("tableName expected in the form "
        + "<databasename>.<table name> or <table name>. Got " + tableName);
    }
  }

  public static Map<String, String>
  getInputJobProperties(HiveStorageHandler storageHandler,
      InputJobInfo inputJobInfo) {
    Properties props = inputJobInfo.getTableInfo().getStorerInfo().getProperties();
    props.put(serdeConstants.SERIALIZATION_LIB,storageHandler.getSerDeClass().getName());
    TableDesc tableDesc = new TableDesc(storageHandler.getInputFormatClass(),
      storageHandler.getOutputFormatClass(),props);
    if (tableDesc.getJobProperties() == null) {
      tableDesc.setJobProperties(new HashMap<String, String>());
    }

    Properties mytableProperties = tableDesc.getProperties();
    mytableProperties.setProperty(org.apache.hadoop.hive.metastore.api.hive_metastoreConstants.META_TABLE_NAME,inputJobInfo.getDatabaseName()+ "." + inputJobInfo.getTableName());

    Map<String, String> jobProperties = new HashMap<String, String>();
    try {
      tableDesc.getJobProperties().put(
        HCatConstants.HCAT_KEY_JOB_INFO,
        HCatUtil.serialize(inputJobInfo));

      storageHandler.configureInputJobProperties(tableDesc,
        jobProperties);

    } catch (IOException e) {
      throw new IllegalStateException(
        "Failed to configure StorageHandler", e);
    }

    return jobProperties;
  }

  @InterfaceAudience.Private
  @InterfaceStability.Evolving
  public static void
  configureOutputStorageHandler(HiveStorageHandler storageHandler,
                  Configuration conf,
                  OutputJobInfo outputJobInfo) {
    //TODO replace IgnoreKeyTextOutputFormat with a
    //HiveOutputFormatWrapper in StorageHandler
    Properties props = outputJobInfo.getTableInfo().getStorerInfo().getProperties();
    props.put(serdeConstants.SERIALIZATION_LIB,storageHandler.getSerDeClass().getName());
    TableDesc tableDesc = new TableDesc(storageHandler.getInputFormatClass(),
      IgnoreKeyTextOutputFormat.class,props);
    if (tableDesc.getJobProperties() == null)
      tableDesc.setJobProperties(new HashMap<String, String>());
    for (Map.Entry<String, String> el : conf) {
      tableDesc.getJobProperties().put(el.getKey(), el.getValue());
    }

    Properties mytableProperties = tableDesc.getProperties();
    mytableProperties.setProperty(
        org.apache.hadoop.hive.metastore.api.hive_metastoreConstants.META_TABLE_NAME,
        outputJobInfo.getDatabaseName()+ "." + outputJobInfo.getTableName());

    Map<String, String> jobProperties = new HashMap<String, String>();
    try {
      tableDesc.getJobProperties().put(
        HCatConstants.HCAT_KEY_OUTPUT_INFO,
        HCatUtil.serialize(outputJobInfo));

      storageHandler.configureOutputJobProperties(tableDesc,
        jobProperties);

      Map<String, String> tableJobProperties = tableDesc.getJobProperties();
      if (tableJobProperties != null) {
        if (tableJobProperties.containsKey(HCatConstants.HCAT_KEY_OUTPUT_INFO)) {
          String jobString = tableJobProperties.get(HCatConstants.HCAT_KEY_OUTPUT_INFO);
          if (jobString != null) {
            if  (!jobProperties.containsKey(HCatConstants.HCAT_KEY_OUTPUT_INFO)) {
              jobProperties.put(HCatConstants.HCAT_KEY_OUTPUT_INFO,
                  tableJobProperties.get(HCatConstants.HCAT_KEY_OUTPUT_INFO));
            }
          }
        }
      }
      for (Map.Entry<String, String> el : jobProperties.entrySet()) {
        conf.set(el.getKey(), el.getValue());
      }
    } catch (IOException e) {
      throw new IllegalStateException(
        "Failed to configure StorageHandler", e);
    }
  }

  /**
   * Replace the contents of dest with the contents of src
   * @param src
   * @param dest
   */
  public static void copyConf(Configuration src, Configuration dest) {
    dest.clear();
    for (Map.Entry<String, String> el : src) {
      dest.set(el.getKey(), el.getValue());
    }
  }

  /**
   * Get or create a hive client depending on whether it exits in cache or not
   * @param hiveConf The hive configuration
   * @return the client
   * @throws MetaException When HiveMetaStoreClient couldn't be created
   * @throws IOException
   */
  public static IMetaStoreClient getHiveMetastoreClient(HiveConf hiveConf)
      throws MetaException, IOException {

    if (hiveConf.getBoolean(HCatConstants.HCAT_HIVE_CLIENT_DISABLE_CACHE, false)){
      // If cache is disabled, don't use it.
      return HiveClientCache.getNonCachedHiveMetastoreClient(hiveConf);
    }

    // Singleton behaviour: create the cache instance if required.
    if (hiveClientCache == null) {
      synchronized (IMetaStoreClient.class) {
        if (hiveClientCache == null) {
          hiveClientCache = new HiveClientCache(hiveConf);
        }
      }
    }
    try {
      return hiveClientCache.get(hiveConf);
    } catch (LoginException e) {
      throw new IOException("Couldn't create hiveMetaStoreClient, Error getting UGI for user", e);
    }
  }

  /**
   * Get or create a hive client depending on whether it exits in cache or not.
   * @Deprecated : use {@link #getHiveMetastoreClient(HiveConf)} instead.
   * This was deprecated in Hive 1.2, slated for removal in two versions
   * (i.e. 1.2 & 1.3(projected) will have it, but it will be removed after that)
   * @param hiveConf The hive configuration
   * @return the client
   * @throws MetaException When HiveMetaStoreClient couldn't be created
   * @throws IOException
   */
  @Deprecated
  public static HiveMetaStoreClient getHiveClient(HiveConf hiveConf) throws MetaException, IOException {
    IMetaStoreClient imsc = getHiveMetastoreClient(hiveConf);
    // Try piggybacking on the function that returns IMSC. Current implementation of the IMSC cache
    // has CacheableMetaStoreClients, which are HMSC, so we can return them as-is. If not, it's okay
    // for us to ignore the caching aspect and return a vanilla HMSC.
    if (imsc instanceof HiveMetaStoreClient){
      return (HiveMetaStoreClient)imsc;
    } else {
      return new HiveMetaStoreClient(hiveConf);
    }
  }

  public static void closeHiveClientQuietly(IMetaStoreClient client) {
    try {
      if (client != null)
        client.close();
    } catch (Exception e) {
      LOG.debug("Error closing metastore client. Ignored the error.", e);
    }
  }

  public static HiveConf getHiveConf(Configuration conf)
    throws IOException {

    HiveConf hiveConf = new HiveConf(conf, HCatUtil.class);

    //copy the hive conf into the job conf and restore it
    //in the backend context
    if (conf.get(HCatConstants.HCAT_KEY_HIVE_CONF) == null) {
      conf.set(HCatConstants.HCAT_KEY_HIVE_CONF,
        HCatUtil.serialize(hiveConf.getAllProperties()));
    } else {
      //Copy configuration properties into the hive conf
      Properties properties = (Properties) HCatUtil.deserialize(
        conf.get(HCatConstants.HCAT_KEY_HIVE_CONF));

      for (Map.Entry<Object, Object> prop : properties.entrySet()) {
        if (prop.getValue() instanceof String) {
          hiveConf.set((String) prop.getKey(), (String) prop.getValue());
        } else if (prop.getValue() instanceof Integer) {
          hiveConf.setInt((String) prop.getKey(),
            (Integer) prop.getValue());
        } else if (prop.getValue() instanceof Boolean) {
          hiveConf.setBoolean((String) prop.getKey(),
            (Boolean) prop.getValue());
        } else if (prop.getValue() instanceof Long) {
          hiveConf.setLong((String) prop.getKey(), (Long) prop.getValue());
        } else if (prop.getValue() instanceof Float) {
          hiveConf.setFloat((String) prop.getKey(),
            (Float) prop.getValue());
        }
      }
    }

    if (conf.get(HCatConstants.HCAT_KEY_TOKEN_SIGNATURE) != null) {
      hiveConf.set("hive.metastore.token.signature",
        conf.get(HCatConstants.HCAT_KEY_TOKEN_SIGNATURE));
    }

    return hiveConf;
  }


  public static JobConf getJobConfFromContext(JobContext jobContext) {
    JobConf jobConf;
    // we need to convert the jobContext into a jobConf
    // 0.18 jobConf (Hive) vs 0.20+ jobContext (HCat)
    // begin conversion..
    jobConf = new JobConf(jobContext.getConfiguration());
    // ..end of conversion


    return jobConf;
  }

  public static void copyJobPropertiesToJobConf(
    Map<String, String> jobProperties, JobConf jobConf) {
    for (Map.Entry<String, String> entry : jobProperties.entrySet()) {
      jobConf.set(entry.getKey(), entry.getValue());
    }
  }


  public static boolean isHadoop23() {
    String version = org.apache.hadoop.util.VersionInfo.getVersion();
    if (version.matches("\\b0\\.23\\..+\\b")||version.matches("\\b2\\..*"))
      return true;
    return false;
  }
  /**
   * Used by various tests to make sure the path is safe for Windows
   */
  public static String makePathASafeFileName(String filePath) {
    return new File(filePath).getPath().replaceAll("\\\\", "/");
  }
  public static void assertNotNull(Object t, String msg, Logger logger) {
    if(t == null) {
      if(logger != null) {
        logger.warn(msg);
      }
      throw new IllegalArgumentException(msg);
    }
  }
}
