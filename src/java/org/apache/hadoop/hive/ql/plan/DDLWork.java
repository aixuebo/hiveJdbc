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
package org.apache.hadoop.hive.ql.plan;

import java.io.Serializable;
import java.util.HashSet;

import org.apache.hadoop.hive.ql.hooks.ReadEntity;
import org.apache.hadoop.hive.ql.hooks.WriteEntity;
import org.apache.hadoop.hive.ql.parse.AlterTablePartMergeFilesDesc;

/**
 * DDLWork.
 * 记录所有的可以使用的操作
 */
public class DDLWork implements Serializable {
  private static final long serialVersionUID = 1L;
  private CreateIndexDesc createIndexDesc;
  /**
   *修改index的属性信息 "indexName" ON "tableName" [PARTITION (name=value,name=value,name)] SET IDXPROPERTIES (key=value,key=value),该属于ADDPROPS
   *  "indexName" ON "tableName" [PARTITION (name=value,name=value,name)] REBUILD
   */
  private AlterIndexDesc alterIndexDesc;
  private DropIndexDesc dropIdxDesc;//删除一个索引
  private CreateDatabaseDesc createDatabaseDesc;//创建一个数据库操作
  private SwitchDatabaseDesc switchDatabaseDesc;//切换数据库操作
  private DropDatabaseDesc dropDatabaseDesc;//删除一个数据库操作
  private CreateTableDesc createTblDesc;//创建一个表
  private CreateTableLikeDesc createTblLikeDesc;//创建通过create table like方式创建表
  private CreateViewDesc createVwDesc;//创建一个视图
  private DropTableDesc dropTblDesc;//删除一个数据表操作,也可能仅仅删除该表的某一些分区
  private AlterTableDesc alterTblDesc;//做一个alter更改表数据的操作
  private AlterIndexDesc alterIdxDesc;
  private ShowDatabasesDesc showDatabasesDesc;//SHOW DATABASES|SCHEMAS LIKE "xxx" 模糊查询,一定要带引号
  private ShowTablesDesc showTblsDesc;//SHOW TABLES [(FROM | IN) tableName ] like "xxx"
  private ShowColumnsDesc showColumnsDesc;//SHOW COLUMNS (FROM | IN) tableName [(FROM | IN) db_name ]
  private ShowTblPropertiesDesc showTblPropertiesDesc;//SHOW TBLPROPERTIES tblName [(columnName)] 获取该表的某一个自定义属性内容
  private LockTableDesc lockTblDesc;//lockStatement
  private UnlockTableDesc unlockTblDesc;//unlockStatement
  private ShowFunctionsDesc showFuncsDesc;//SHOW FUNCTIONS [xxx]
  private ShowLocksDesc showLocksDesc;//SHOW LOCKS xxx .($ELEM$ | $KEY$ | $VALUE$ | xxx ) .($ELEM$ | $KEY$ | $VALUE$ | xxx )
  private DescFunctionDesc descFunctionDesc;//DESCRIBE | DESC FUNCTION [EXTENDED] descFuncNames
  private ShowPartitionsDesc showPartsDesc;//展示某个表的某个partition信息
  private ShowCreateTableDesc showCreateTblDesc;//SHOW CREATE TABLE tableName 
  private DescTableDesc descTblDesc;//desc table 描述表
  private AddPartitionDesc addPartitionDesc;//为视图和table添加partition分区
  private RenamePartitionDesc renamePartitionDesc;//alterStatementSuffixRenamePart 为table修改新的partition属性
  private AlterTableSimpleDesc alterTblSimpleDesc;//针对table和partition的一些操作
  private MsckDesc msckDesc;//MSCK [REPAIR] [TABLE tableName PARTITION (name=value,name=value,name),PARTITION (name=value,name=value,name)...]
  private ShowTableStatusDesc showTblStatusDesc;//SHOW TABLE EXTENDED [(FROM | IN) db_name ] like tableName [PARTITION (name=value,name=value,name)]
  private ShowIndexesDesc showIndexesDesc;//SHOW [FORMATTED](INDEX|INDEXES) ON tableName (FROM | IN) db_name ]
  private DescDatabaseDesc descDbDesc;//DESCRIBE | DESC DATABASE [EXTENDED] "dbName"
  private AlterDatabaseDesc alterDbDesc;//更改数据库的信息
  private AlterTableAlterPartDesc alterTableAlterPartDesc;
  private TruncateTableDesc truncateTblDesc;//创建截断表内数据,从表或者表分区删除所有行，不指定分区，将截断表中的所有分区，也可以一次指定多个分区，截断多个分区。
  private AlterTableExchangePartition alterTableExchangePartition;//将tableName1的某一个partition的数据交换到另外一个tableName2中

  private RoleDDLDesc roleDDLDesc;//角色相关
  private GrantDesc grantDesc;
  private ShowGrantDesc showGrantDesc;
  private RevokeDesc revokeDesc;
  private GrantRevokeRoleDDL grantRevokeRoleDDL;

  boolean needLock = false;

  /**
   * ReadEntitites that are passed to the hooks.
   */
  protected HashSet<ReadEntity> inputs;
  /**
   * List of WriteEntities that are passed to the hooks.
   */
  protected HashSet<WriteEntity> outputs;
  private AlterTablePartMergeFilesDesc mergeFilesDesc;

  public DDLWork() {
  }

  public DDLWork(HashSet<ReadEntity> inputs, HashSet<WriteEntity> outputs) {
    this.inputs = inputs;
    this.outputs = outputs;
  }

  public DDLWork(HashSet<ReadEntity> inputs, HashSet<WriteEntity> outputs,
      CreateIndexDesc createIndex) {
    this(inputs, outputs);
    this.createIndexDesc = createIndex;
  }

  public DDLWork(AlterIndexDesc alterIndex) {
    this.alterIndexDesc = alterIndex;
  }

  /**
   * @param createDatabaseDesc
   *          Create Database descriptor
   * 创建一个数据库操作         
   */
  public DDLWork(HashSet<ReadEntity> inputs, HashSet<WriteEntity> outputs,
      CreateDatabaseDesc createDatabaseDesc) {
    this(inputs, outputs);
    this.createDatabaseDesc = createDatabaseDesc;
  }

  /**
   * @param inputs
   * @param outputs
   * @param descDatabaseDesc Database descriptor
   */
  public DDLWork(HashSet<ReadEntity> inputs, HashSet<WriteEntity> outputs,
      DescDatabaseDesc descDatabaseDesc) {
    this(inputs, outputs);
    this.descDbDesc = descDatabaseDesc;
  }

  /**
   * 更改数据库的信息
   */
  public DDLWork(HashSet<ReadEntity> inputs, HashSet<WriteEntity> outputs,
      AlterDatabaseDesc alterDbDesc) {
    this(inputs, outputs);
    this.alterDbDesc = alterDbDesc;
  }

  /**
   * 创建截断表内数据,从表或者表分区删除所有行，不指定分区，将截断表中的所有分区，也可以一次指定多个分区，截断多个分区。
   * @param inputs
   * @param outputs
   * @param truncateTblDesc
   */
  public DDLWork(HashSet<ReadEntity> inputs, HashSet<WriteEntity> outputs,
      TruncateTableDesc truncateTblDesc) {
    this(inputs, outputs);
    this.truncateTblDesc = truncateTblDesc;
  }

  public DescDatabaseDesc getDescDatabaseDesc() {
    return descDbDesc;
  }

  /**
   * @param dropDatabaseDesc
   *          Drop Database descriptor
   * 删除一个数据库操作         
   */
  public DDLWork(HashSet<ReadEntity> inputs, HashSet<WriteEntity> outputs,
      DropDatabaseDesc dropDatabaseDesc) {
    this(inputs, outputs);
    this.dropDatabaseDesc = dropDatabaseDesc;
  }

  /**
   * @param switchDatabaseDesc
   *          Switch Database descriptor
   * 切换数据库操作
   */
  public DDLWork(HashSet<ReadEntity> inputs, HashSet<WriteEntity> outputs,
      SwitchDatabaseDesc switchDatabaseDesc) {
    this(inputs, outputs);
    this.switchDatabaseDesc = switchDatabaseDesc;
  }

  /**
   * @param alterTblDesc
   *          alter table descriptor
   * 做一个alter更改表数据的操作        
   */
  public DDLWork(HashSet<ReadEntity> inputs, HashSet<WriteEntity> outputs,
      AlterTableDesc alterTblDesc) {
    this(inputs, outputs);
    this.alterTblDesc = alterTblDesc;
  }

  /**
   * @param alterIdxDesc
   *          alter index descriptor
   */
  public DDLWork(HashSet<ReadEntity> inputs, HashSet<WriteEntity> outputs,
      AlterIndexDesc alterIdxDesc) {
    this(inputs, outputs);
    this.alterIdxDesc = alterIdxDesc;
  }

  /**
   * @param createTblDesc
   *          create table descriptor
   */
  public DDLWork(HashSet<ReadEntity> inputs, HashSet<WriteEntity> outputs,
      CreateTableDesc createTblDesc) {
    this(inputs, outputs);

    this.createTblDesc = createTblDesc;
  }

  /**
   * @param createTblLikeDesc
   *          create table like descriptor
   */
  public DDLWork(HashSet<ReadEntity> inputs, HashSet<WriteEntity> outputs,
      CreateTableLikeDesc createTblLikeDesc) {
    this(inputs, outputs);

    this.createTblLikeDesc = createTblLikeDesc;
  }

  /**
   * @param createVwDesc
   *          create view descriptor
   */
  public DDLWork(HashSet<ReadEntity> inputs, HashSet<WriteEntity> outputs,
      CreateViewDesc createVwDesc) {
    this(inputs, outputs);

    this.createVwDesc = createVwDesc;
  }

  /**
   * @param dropTblDesc
   *          drop table descriptor
   * 删除一个数据表操作,也可能仅仅删除该表的某一些分区
   */
  public DDLWork(HashSet<ReadEntity> inputs, HashSet<WriteEntity> outputs,
      DropTableDesc dropTblDesc) {
    this(inputs, outputs);

    this.dropTblDesc = dropTblDesc;
  }

  /**
   * @param descTblDesc
   * 描述表
   */
  public DDLWork(HashSet<ReadEntity> inputs, HashSet<WriteEntity> outputs,
      DescTableDesc descTblDesc) {
    this(inputs, outputs);

    this.descTblDesc = descTblDesc;
  }

  /**
   * @param showDatabasesDesc
   */
  public DDLWork(HashSet<ReadEntity> inputs, HashSet<WriteEntity> outputs,
      ShowDatabasesDesc showDatabasesDesc) {
    this(inputs, outputs);

    this.showDatabasesDesc = showDatabasesDesc;
  }

  /**
   * @param showTblsDesc
   */
  public DDLWork(HashSet<ReadEntity> inputs, HashSet<WriteEntity> outputs,
      ShowTablesDesc showTblsDesc) {
    this(inputs, outputs);

    this.showTblsDesc = showTblsDesc;
  }

  /**
   * @param showColumnsDesc
   * SHOW COLUMNS (FROM | IN) tableName [(FROM | IN) db_name ]
   */
  public DDLWork(HashSet<ReadEntity> inputs, HashSet<WriteEntity> outputs,
      ShowColumnsDesc showColumnsDesc) {
    this(inputs, outputs);

    this.showColumnsDesc = showColumnsDesc;
  }

  /**
   * @param lockTblDesc
   */
  public DDLWork(HashSet<ReadEntity> inputs, HashSet<WriteEntity> outputs,
      LockTableDesc lockTblDesc) {
    this(inputs, outputs);

    this.lockTblDesc = lockTblDesc;
  }

  /**
   * @param unlockTblDesc
   */
  public DDLWork(HashSet<ReadEntity> inputs, HashSet<WriteEntity> outputs,
      UnlockTableDesc unlockTblDesc) {
    this(inputs, outputs);

    this.unlockTblDesc = unlockTblDesc;
  }

  /**
   * @param showFuncsDesc
   * SHOW FUNCTIONS [xxx]
   */
  public DDLWork(HashSet<ReadEntity> inputs, HashSet<WriteEntity> outputs,
      ShowFunctionsDesc showFuncsDesc) {
    this(inputs, outputs);

    this.showFuncsDesc = showFuncsDesc;
  }

  /**
   * @param showLocksDesc
   */
  public DDLWork(HashSet<ReadEntity> inputs, HashSet<WriteEntity> outputs,
      ShowLocksDesc showLocksDesc) {
    this(inputs, outputs);

    this.showLocksDesc = showLocksDesc;
  }

  /**
   * @param descFuncDesc
   * DESCRIBE | DESC FUNCTION [EXTENDED] descFuncNames
   */
  public DDLWork(HashSet<ReadEntity> inputs, HashSet<WriteEntity> outputs,
      DescFunctionDesc descFuncDesc) {
    this(inputs, outputs);

    descFunctionDesc = descFuncDesc;
  }

  /**
   * @param showPartsDesc
   * 展示某个表的某个partition信息
   */
  public DDLWork(HashSet<ReadEntity> inputs, HashSet<WriteEntity> outputs,
      ShowPartitionsDesc showPartsDesc) {
    this(inputs, outputs);

    this.showPartsDesc = showPartsDesc;
  }

  /**
   * @param showCreateTblDesc
   */
  public DDLWork(HashSet<ReadEntity> inputs, HashSet<WriteEntity> outputs,
      ShowCreateTableDesc showCreateTblDesc) {
    this(inputs, outputs);

    this.showCreateTblDesc = showCreateTblDesc;
  }

  /**
   * @param addPartitionDesc
   *          information about the partitions we want to add.
   * 为视图和table添加partition分区
   */
  public DDLWork(HashSet<ReadEntity> inputs, HashSet<WriteEntity> outputs,
      AddPartitionDesc addPartitionDesc) {
    this(inputs, outputs);

    this.addPartitionDesc = addPartitionDesc;
  }

  /**
   * @param renamePartitionDesc
   *          information about the partitions we want to add.
   * alterStatementSuffixRenamePart 为table修改新的partition属性
   */
  public DDLWork(HashSet<ReadEntity> inputs, HashSet<WriteEntity> outputs,
      RenamePartitionDesc renamePartitionDesc) {
    this(inputs, outputs);

    this.renamePartitionDesc = renamePartitionDesc;
  }

  /**
   * @param inputs
   * @param outputs
   * @param simpleDesc
   */
  public DDLWork(HashSet<ReadEntity> inputs, HashSet<WriteEntity> outputs,
      AlterTableSimpleDesc simpleDesc) {
    this(inputs, outputs);

    this.alterTblSimpleDesc = simpleDesc;
  }

  /**
   * MSCK [REPAIR] [TABLE tableName PARTITION (name=value,name=value,name),PARTITION (name=value,name=value,name)...]
   */
  public DDLWork(HashSet<ReadEntity> inputs, HashSet<WriteEntity> outputs,
      MsckDesc checkDesc) {
    this(inputs, outputs);

    msckDesc = checkDesc;
  }

  /**
   * @param showTblStatusDesc
   *          show table status descriptor
   * SHOW TABLE EXTENDED [(FROM | IN) db_name ] like tableName [PARTITION (name=value,name=value,name)]
   */
  public DDLWork(HashSet<ReadEntity> inputs, HashSet<WriteEntity> outputs,
      ShowTableStatusDesc showTblStatusDesc) {
    this(inputs, outputs);

    this.showTblStatusDesc = showTblStatusDesc;
  }

  /**
   * @param showTblPropertiesDesc
   *          show table properties descriptor
   * SHOW TBLPROPERTIES tblName [(columnName)] 获取该表的某一个自定义属性内容
   */
  public DDLWork(HashSet<ReadEntity> inputs, HashSet<WriteEntity> outputs,
      ShowTblPropertiesDesc showTblPropertiesDesc) {
    this(inputs, outputs);

    this.showTblPropertiesDesc = showTblPropertiesDesc;
  }

  public DDLWork(HashSet<ReadEntity> inputs, HashSet<WriteEntity> outputs,
      DropIndexDesc dropIndexDesc) {
    this(inputs, outputs);
    this.dropIdxDesc = dropIndexDesc;
  }

  public DDLWork(HashSet<ReadEntity> inputs, HashSet<WriteEntity> outputs,
      RoleDDLDesc roleDDLDesc) {
    this(inputs, outputs);
    this.roleDDLDesc = roleDDLDesc;
  }

  public DDLWork(HashSet<ReadEntity> inputs, HashSet<WriteEntity> outputs,
      GrantDesc grantDesc) {
    this(inputs, outputs);
    this.grantDesc = grantDesc;
  }

  public DDLWork(HashSet<ReadEntity> inputs, HashSet<WriteEntity> outputs,
      ShowGrantDesc showGrant) {
    this(inputs, outputs);
    this.showGrantDesc = showGrant;
  }

  public DDLWork(HashSet<ReadEntity> inputs, HashSet<WriteEntity> outputs,
      RevokeDesc revokeDesc) {
    this(inputs, outputs);
    this.revokeDesc = revokeDesc;
  }

  public DDLWork(HashSet<ReadEntity> inputs, HashSet<WriteEntity> outputs,
      GrantRevokeRoleDDL grantRevokeRoleDDL) {
    this(inputs, outputs);
    this.grantRevokeRoleDDL = grantRevokeRoleDDL;
  }

  //SHOW [FORMATTED](INDEX|INDEXES) ON tableName (FROM | IN) db_name ]
  public DDLWork(HashSet<ReadEntity> inputs, HashSet<WriteEntity> outputs,
      ShowIndexesDesc showIndexesDesc) {
    this(inputs, outputs);
    this.showIndexesDesc = showIndexesDesc;
  }

  public DDLWork(HashSet<ReadEntity> inputs, HashSet<WriteEntity> outputs,
      AlterTablePartMergeFilesDesc mergeDesc) {
    this(inputs, outputs);
    this.mergeFilesDesc = mergeDesc;
  }

  public DDLWork(HashSet<ReadEntity> inputs, HashSet<WriteEntity> outputs,
      AlterTableAlterPartDesc alterPartDesc) {
    this(inputs, outputs);
    this.alterTableAlterPartDesc = alterPartDesc;
  }

  /**
   * 将tableName1的某一个partition的数据交换到另外一个tableName2中
   */
  public DDLWork(HashSet<ReadEntity> inputs, HashSet<WriteEntity> outputs,
      AlterTableExchangePartition alterTableExchangePartition) {
    this(inputs, outputs);
    this.alterTableExchangePartition = alterTableExchangePartition;
  }

    /**
   * @return Create Database descriptor
   */
  public CreateDatabaseDesc getCreateDatabaseDesc() {
    return createDatabaseDesc;
  }

  /**
   * Set Create Database descriptor
   * @param createDatabaseDesc
   */
  public void setCreateDatabaseDesc(CreateDatabaseDesc createDatabaseDesc) {
    this.createDatabaseDesc = createDatabaseDesc;
  }

  /**
   * @return Drop Database descriptor
   */
  public DropDatabaseDesc getDropDatabaseDesc() {
    return dropDatabaseDesc;
  }

  /**
   * Set Drop Database descriptor
   * @param dropDatabaseDesc
   */
  public void setDropDatabaseDesc(DropDatabaseDesc dropDatabaseDesc) {
    this.dropDatabaseDesc = dropDatabaseDesc;
  }

  /**
   * @return Switch Database descriptor
   */
  public SwitchDatabaseDesc getSwitchDatabaseDesc() {
    return switchDatabaseDesc;
  }

  /**
   * Set Switch Database descriptor
   * @param switchDatabaseDesc
   */
  public void setSwitchDatabaseDesc(SwitchDatabaseDesc switchDatabaseDesc) {
    this.switchDatabaseDesc = switchDatabaseDesc;
  }

  /**
   * @return the createTblDesc
   */
  @Explain(displayName = "Create Table Operator")
  public CreateTableDesc getCreateTblDesc() {
    return createTblDesc;
  }

  /**
   * @param createTblDesc
   *          the createTblDesc to set
   */
  public void setCreateTblDesc(CreateTableDesc createTblDesc) {
    this.createTblDesc = createTblDesc;
  }

  /**
   * @return the createIndexDesc
   */
  public CreateIndexDesc getCreateIndexDesc() {
    return createIndexDesc;
  }

  /**
   * @param createIndexDesc
   *          the createIndexDesc to set
   */
  public void setCreateIndexDesc(CreateIndexDesc createIndexDesc) {
    this.createIndexDesc = createIndexDesc;
  }

  /**
   * @return the alterIndexDesc
   */
  public AlterIndexDesc getAlterIndexDesc() {
    return alterIndexDesc;
  }

  /**
   * @param alterIndexDesc
   *          the alterIndexDesc to set
   */
  public void setAlterIndexDesc(AlterIndexDesc alterIndexDesc) {
    this.alterIndexDesc = alterIndexDesc;
  }

  /**
   * @return the createTblDesc
   */
  @Explain(displayName = "Create Table Operator")
  public CreateTableLikeDesc getCreateTblLikeDesc() {
    return createTblLikeDesc;
  }

  /**
   * @param createTblLikeDesc
   *          the createTblDesc to set
   */
  public void setCreateTblLikeDesc(CreateTableLikeDesc createTblLikeDesc) {
    this.createTblLikeDesc = createTblLikeDesc;
  }

  /**
   * @return the createTblDesc
   */
  @Explain(displayName = "Create View Operator")
  public CreateViewDesc getCreateViewDesc() {
    return createVwDesc;
  }

  /**
   * @param createVwDesc
   *          the createViewDesc to set
   */
  public void setCreateViewDesc(CreateViewDesc createVwDesc) {
    this.createVwDesc = createVwDesc;
  }

  /**
   * @return the dropTblDesc
   */
  @Explain(displayName = "Drop Table Operator")
  public DropTableDesc getDropTblDesc() {
    return dropTblDesc;
  }

  /**
   * @param dropTblDesc
   *          the dropTblDesc to set
   */
  public void setDropTblDesc(DropTableDesc dropTblDesc) {
    this.dropTblDesc = dropTblDesc;
  }

  /**
   * @return the alterTblDesc
   */
  @Explain(displayName = "Alter Table Operator")
  public AlterTableDesc getAlterTblDesc() {
    return alterTblDesc;
  }

  /**
   * @param alterTblDesc
   *          the alterTblDesc to set
   */
  public void setAlterTblDesc(AlterTableDesc alterTblDesc) {
    this.alterTblDesc = alterTblDesc;
  }

  /**
   * @return the showDatabasesDesc
   */
  @Explain(displayName = "Show Databases Operator")
  public ShowDatabasesDesc getShowDatabasesDesc() {
    return showDatabasesDesc;
  }

  /**
   * @param showDatabasesDesc
   *          the showDatabasesDesc to set
   */
  public void setShowDatabasesDesc(ShowDatabasesDesc showDatabasesDesc) {
    this.showDatabasesDesc = showDatabasesDesc;
  }

  /**
   * @return the showTblsDesc
   */
  @Explain(displayName = "Show Table Operator")
  public ShowTablesDesc getShowTblsDesc() {
    return showTblsDesc;
  }

  /**
   * @param showTblsDesc
   *          the showTblsDesc to set
   */
  public void setShowTblsDesc(ShowTablesDesc showTblsDesc) {
    this.showTblsDesc = showTblsDesc;
  }

  /**
   * @return the showColumnsDesc
   */
  @Explain(displayName = "Show Columns Operator")
  public ShowColumnsDesc getShowColumnsDesc() {
    return showColumnsDesc;
  }

  /**
   * @param showColumnsDesc
   *          the showColumnsDesc to set
   */
  public void setShowColumnsDesc(ShowColumnsDesc showColumnsDesc) {
    this.showColumnsDesc = showColumnsDesc;
  }

  /**
   * @return the showFuncsDesc
   */
  @Explain(displayName = "Show Function Operator")
  public ShowFunctionsDesc getShowFuncsDesc() {
    return showFuncsDesc;
  }

  /**
   * @return the showLocksDesc
   */
  @Explain(displayName = "Show Lock Operator")
  public ShowLocksDesc getShowLocksDesc() {
    return showLocksDesc;
  }

  /**
   * @return the lockTblDesc
   */
  @Explain(displayName = "Lock Table Operator")
  public LockTableDesc getLockTblDesc() {
    return lockTblDesc;
  }

  /**
   * @return the unlockTblDesc
   */
  @Explain(displayName = "Unlock Table Operator")
  public UnlockTableDesc getUnlockTblDesc() {
    return unlockTblDesc;
  }

  /**
   * @return the descFuncDesc
   */
  @Explain(displayName = "Show Function Operator")
  public DescFunctionDesc getDescFunctionDesc() {
    return descFunctionDesc;
  }

  /**
   * @param showFuncsDesc
   *          the showFuncsDesc to set
   */
  public void setShowFuncsDesc(ShowFunctionsDesc showFuncsDesc) {
    this.showFuncsDesc = showFuncsDesc;
  }

  /**
   * @param showLocksDesc
   *          the showLocksDesc to set
   */
  public void setShowLocksDesc(ShowLocksDesc showLocksDesc) {
    this.showLocksDesc = showLocksDesc;
  }

  /**
   * @param lockTblDesc
   *          the lockTblDesc to set
   */
  public void setLockTblDesc(LockTableDesc lockTblDesc) {
    this.lockTblDesc = lockTblDesc;
  }

  /**
   * @param unlockTblDesc
   *          the unlockTblDesc to set
   */
  public void setUnlockTblDesc(UnlockTableDesc unlockTblDesc) {
    this.unlockTblDesc = unlockTblDesc;
  }

  /**
   * @param descFuncDesc
   *          the showFuncsDesc to set
   */
  public void setDescFuncDesc(DescFunctionDesc descFuncDesc) {
    descFunctionDesc = descFuncDesc;
  }

  /**
   * @return the showPartsDesc
   */
  @Explain(displayName = "Show Partitions Operator")
  public ShowPartitionsDesc getShowPartsDesc() {
    return showPartsDesc;
  }

  /**
   * @param showPartsDesc
   *          the showPartsDesc to set
   */
  public void setShowPartsDesc(ShowPartitionsDesc showPartsDesc) {
    this.showPartsDesc = showPartsDesc;
  }

  /**
   * @return the showCreateTblDesc
   */
  @Explain(displayName = "Show Create Table Operator")
  public ShowCreateTableDesc getShowCreateTblDesc() {
    return showCreateTblDesc;
  }

  /**
   * @param showCreateTblDesc
   *          the showCreateTblDesc to set
   */
  public void setShowCreateTblDesc(ShowCreateTableDesc showCreateTblDesc) {
    this.showCreateTblDesc = showCreateTblDesc;
  }

  /**
   * @return the showIndexesDesc
   */
  @Explain(displayName = "Show Index Operator")
  public ShowIndexesDesc getShowIndexesDesc() {
    return showIndexesDesc;
  }

  public void setShowIndexesDesc(ShowIndexesDesc showIndexesDesc) {
    this.showIndexesDesc = showIndexesDesc;
  }

  /**
   * @return the descTblDesc
   */
  @Explain(displayName = "Describe Table Operator")
  public DescTableDesc getDescTblDesc() {
    return descTblDesc;
  }

  /**
   * @param descTblDesc
   *          the descTblDesc to set
   */
  public void setDescTblDesc(DescTableDesc descTblDesc) {
    this.descTblDesc = descTblDesc;
  }

  /**
   * @return information about the partitions we want to add.
   */
  @Explain(displayName = "Add Partition Operator")
  public AddPartitionDesc getAddPartitionDesc() {
    return addPartitionDesc;
  }

  /**
   * @param addPartitionDesc
   *          information about the partitions we want to add.
   */
  public void setAddPartitionDesc(AddPartitionDesc addPartitionDesc) {
    this.addPartitionDesc = addPartitionDesc;
  }

  /**
   * @return information about the partitions we want to rename.
   */
  public RenamePartitionDesc getRenamePartitionDesc() {
    return renamePartitionDesc;
  }

  /**
   * @param renamePartitionDesc
   *          information about the partitions we want to rename.
   */
  public void setRenamePartitionDesc(RenamePartitionDesc renamePartitionDesc) {
    this.renamePartitionDesc = renamePartitionDesc;
  }

  /**
   * @return information about the table/partitions we want to alter.
   */
  public AlterTableSimpleDesc getAlterTblSimpleDesc() {
    return alterTblSimpleDesc;
  }

  /**
   * @param desc
   *          information about the table/partitions we want to alter.
   */
  public void setAlterTblSimpleDesc(AlterTableSimpleDesc desc) {
    this.alterTblSimpleDesc = desc;
  }

  /**
   * @return Metastore check description
   */
  public MsckDesc getMsckDesc() {
    return msckDesc;
  }

  /**
   * @param msckDesc
   *          metastore check description
   */
  public void setMsckDesc(MsckDesc msckDesc) {
    this.msckDesc = msckDesc;
  }

  /**
   * @return show table descriptor
   */
  public ShowTableStatusDesc getShowTblStatusDesc() {
    return showTblStatusDesc;
  }

  /**
   * @param showTblStatusDesc
   *          show table descriptor
   */
  public void setShowTblStatusDesc(ShowTableStatusDesc showTblStatusDesc) {
    this.showTblStatusDesc = showTblStatusDesc;
  }

  public ShowTblPropertiesDesc getShowTblPropertiesDesc() {
    return showTblPropertiesDesc;
  }

  public void setShowTblPropertiesDesc(ShowTblPropertiesDesc showTblPropertiesDesc) {
    this.showTblPropertiesDesc = showTblPropertiesDesc;
  }

  public CreateViewDesc getCreateVwDesc() {
    return createVwDesc;
  }

  public void setCreateVwDesc(CreateViewDesc createVwDesc) {
    this.createVwDesc = createVwDesc;
  }

  public void setDescFunctionDesc(DescFunctionDesc descFunctionDesc) {
    this.descFunctionDesc = descFunctionDesc;
  }

  public HashSet<ReadEntity> getInputs() {
    return inputs;
  }

  public HashSet<WriteEntity> getOutputs() {
    return outputs;
  }

  public void setInputs(HashSet<ReadEntity> inputs) {
    this.inputs = inputs;
  }

  public void setOutputs(HashSet<WriteEntity> outputs) {
    this.outputs = outputs;
  }

  public DropIndexDesc getDropIdxDesc() {
    return dropIdxDesc;
  }

  public void setDropIdxDesc(DropIndexDesc dropIdxDesc) {
    this.dropIdxDesc = dropIdxDesc;
  }

  /**
   * @return role ddl desc
   */
  public RoleDDLDesc getRoleDDLDesc() {
    return roleDDLDesc;
  }

  /**
   * @param roleDDLDesc role ddl desc
   */
  public void setRoleDDLDesc(RoleDDLDesc roleDDLDesc) {
    this.roleDDLDesc = roleDDLDesc;
  }

  /**
   * @return grant desc
   */
  public GrantDesc getGrantDesc() {
    return grantDesc;
  }

  /**
   * @param grantDesc grant desc
   */
  public void setGrantDesc(GrantDesc grantDesc) {
    this.grantDesc = grantDesc;
  }

  /**
   * @return show grant desc
   */
  public ShowGrantDesc getShowGrantDesc() {
    return showGrantDesc;
  }

  /**
   * @param showGrantDesc
   */
  public void setShowGrantDesc(ShowGrantDesc showGrantDesc) {
    this.showGrantDesc = showGrantDesc;
  }

  public RevokeDesc getRevokeDesc() {
    return revokeDesc;
  }

  public void setRevokeDesc(RevokeDesc revokeDesc) {
    this.revokeDesc = revokeDesc;
  }

  public GrantRevokeRoleDDL getGrantRevokeRoleDDL() {
    return grantRevokeRoleDDL;
  }

  /**
   * @param grantRevokeRoleDDL
   */
  public void setGrantRevokeRoleDDL(GrantRevokeRoleDDL grantRevokeRoleDDL) {
    this.grantRevokeRoleDDL = grantRevokeRoleDDL;
  }

  public void setAlterDatabaseDesc(AlterDatabaseDesc alterDbDesc) {
    this.alterDbDesc = alterDbDesc;
  }

  public AlterDatabaseDesc getAlterDatabaseDesc() {
    return this.alterDbDesc;
  }

  /**
   * @return descriptor for merging files
   */
  public AlterTablePartMergeFilesDesc getMergeFilesDesc() {
    return mergeFilesDesc;
  }

  /**
   * @param mergeDesc descriptor of merging files
   */
  public void setMergeFilesDesc(AlterTablePartMergeFilesDesc mergeDesc) {
    this.mergeFilesDesc = mergeDesc;
  }

  public boolean getNeedLock() {
    return needLock;
  }

  public void setNeedLock(boolean needLock) {
    this.needLock = needLock;
  }

  /**
   * @return information about the partitions we want to change.
   */
  public AlterTableAlterPartDesc getAlterTableAlterPartDesc() {
    return alterTableAlterPartDesc;
  }

  /**
   * @param alterPartitionDesc
   *          information about the partitions we want to change.
   */
  public void setAlterTableAlterPartDesc(AlterTableAlterPartDesc alterPartitionDesc) {
    this.alterTableAlterPartDesc = alterPartitionDesc;
  }

  @Explain(displayName = "Truncate Table Operator")
  public TruncateTableDesc getTruncateTblDesc() {
    return truncateTblDesc;
  }

  public void setTruncateTblDesc(TruncateTableDesc truncateTblDesc) {
    this.truncateTblDesc = truncateTblDesc;
  }

  /**
   * @return information about the table partition to be exchanged
   */
  public AlterTableExchangePartition getAlterTableExchangePartition() {
    return this.alterTableExchangePartition;
  }

  /**
   * @param alterTableExchangePartition
   *          set the value of the table partition to be exchanged
   */
  public void setAlterTableExchangePartition(
      AlterTableExchangePartition alterTableExchangePartition) {
    this.alterTableExchangePartition = alterTableExchangePartition;
  }
}
