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

package org.apache.hive.service.cli;

import java.util.List;

import org.apache.hadoop.hive.metastore.api.FieldSchema;
import org.apache.hadoop.hive.serde2.typeinfo.BaseTypeParams;
import org.apache.hadoop.hive.serde2.typeinfo.PrimitiveTypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfoFactory;
import org.apache.hive.service.cli.thrift.TPrimitiveTypeEntry;
import org.apache.hive.service.cli.thrift.TTypeDesc;
import org.apache.hive.service.cli.thrift.TTypeEntry;

/**
 * TypeDescriptor.
 * 代表一个列的类型对象
 */
public class TypeDescriptor {

  private final Type type;//类型对象
  private String typeName = null;//类型名字
  private TypeQualifiers typeQualifiers = null;//该类型的补充信息

  public TypeDescriptor(Type type) {
    this.type = type;
  }

  public TypeDescriptor(TTypeDesc tTypeDesc) {
    List<TTypeEntry> tTypeEntries = tTypeDesc.getTypes();
    TPrimitiveTypeEntry top = tTypeEntries.get(0).getPrimitiveEntry();
    this.type = Type.getType(top.getType());
    if (top.isSetTypeQualifiers()) {
      setTypeQualifiers(TypeQualifiers.fromTTypeQualifiers(top.getTypeQualifiers()));
    }
  }

  public TypeDescriptor(String typeName) {
    this.type = Type.getType(typeName);
    if (this.type.isComplexType()) {
      this.typeName = typeName;
    } else if (this.type.isQualifiedType()) {
      TypeInfo pti = TypeInfoFactory.getPrimitiveTypeInfo(typeName);
      BaseTypeParams typeParams = ((PrimitiveTypeInfo) pti).getTypeParams();
      if (typeParams != null) {
        setTypeQualifiers(TypeQualifiers.fromBaseTypeParams(typeParams));
      }
    }
  }

  public Type getType() {
    return type;
  }

  public TTypeDesc toTTypeDesc() {
    TPrimitiveTypeEntry primitiveEntry = new TPrimitiveTypeEntry(type.toTType());
    if (getTypeQualifiers() != null) {
      primitiveEntry.setTypeQualifiers(getTypeQualifiers().toTTypeQualifiers());
    }
    TTypeEntry entry = TTypeEntry.primitiveEntry(primitiveEntry);

    TTypeDesc desc = new TTypeDesc();
    desc.addToTypes(entry);
    return desc;
  }

  public String getTypeName() {
    if (typeName != null) {
      return typeName;
    } else {
      return type.getName();
    }
  }

  public TypeQualifiers getTypeQualifiers() {
    return typeQualifiers;
  }

  public void setTypeQualifiers(TypeQualifiers typeQualifiers) {
    this.typeQualifiers = typeQualifiers;
  }
}
