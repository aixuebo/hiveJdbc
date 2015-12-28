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

package org.apache.hadoop.hive.ql.udf;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

/**
 * find_in_set('ab','abc,b,ab,c,def') ����ֵ��3,��ab�ڵ�����λ��,�±��1��ʼ����
 * UDFFindInSet.
 * SELECT _FUNC_('ab','abc,b,ab,c,def') ����ֵ��3,��ab�ڵ�����λ��,�±��1��ʼ����
 * 
 * Returns null if either argument is null �������������������һ��������null,�򷵻�null
 * Returns 0 if the first argument has any commas. ��ʾ��һ��������������κζ���,�򶼷���0
 * 
 * _FUNC_(key,'311,128,345,956')=0 ��ʾ�ú�������ֵ��0,����һ������û���ڵڶ����������ҵ�ƥ�����,�򷵻�0
 */
@Description(name = "find_in_set", value = "_FUNC_(str,str_array) - Returns the first occurrence "
    + " of str in str_array where str_array is a comma-delimited string."
    + " Returns null if either argument is null."
    + " Returns 0 if the first argument has any commas.", extended = "Example:\n"
    + "  > SELECT _FUNC_('ab','abc,b,ab,c,def') FROM src LIMIT 1;\n"
    + "  3\n"
    + "  > SELECT * FROM src1 WHERE NOT _FUNC_(key,'311,128,345,956')=0;\n"
    + "  311  val_311\n" + "  128"

)
public class UDFFindInSet extends UDF {
  private final IntWritable result = new IntWritable();

  public IntWritable evaluate(Text s, Text txtarray) {
	  
	//�������������������һ��������null,�򷵻�null
    if (s == null || txtarray == null) {
      return null;
    }

    byte[] search_bytes = s.getBytes();
    
    //��һ��������������κζ���,�򶼷���0
    for (int i = 0; i < s.getLength(); i++) {
      if (search_bytes[i] == ',') {
        result.set(0);
        return result;
      }
    }

    byte[] data = txtarray.getBytes();
    int search_length = s.getLength();

    int cur_pos_in_array = 0;//��ʾ�ڶ��������Ѿ�ƥ�䵽�ڼ���Ԫ����,����ڶ���������abc,ab ����ò���Ϊ2,˵��ĿǰҪƥ�����ab
    int cur_length = 0;//��ʾ��һ���������Ѿ�ƥ�䵽ʲôλ����
    boolean matching = true;//��ʾƥ��ɹ�

    for (int i = 0; i < txtarray.getLength(); i++) {
      if (data[i] == ',') {//����,��,��Ҫ�������һ����������ƥ��
        cur_pos_in_array++;
        if (matching && cur_length == search_length) {//���ƥ��ɹ�,��ֱ�ӷ��ظ�λ�ü���
          result.set(cur_pos_in_array);
          return result;
        } else {//����ƥ��
          matching = true;
          cur_length = 0;
        }
      } else {
        if (cur_length + 1 <= search_length) {//˵���ڶ���������ĳһ��Ԫ�� ����δ������һ����������,���Բ���ѭ��,�����һ��������abc,�ڶ���������abd,�����Ѿ�ѭ����b��
        	/**
        	 * 1.matching�����false,˵���Ѿ���ƥ����,������Ҳû�б�Ҫ��ȥƥ��,
        	 * 2.search_bytes[cur_length] != data[i],�����һ��������abc,�ڶ���������abd,��c��ƥ��d��ʱ��,˵��ƥ�䲻�ɹ�,����false
        	 */
          if (!matching || search_bytes[cur_length] != data[i]) {
            matching = false;
          }
        } else {//˵���ڶ���������ĳһ��Ԫ�أ��Ѿ������˵�һ�������ĳ���,��˿϶��ǲ�ƥ����,�����һ��������abc,�ڶ���������abcd,�϶��ǲ�ƥ����
          matching = false;
        }
        cur_length++;
      }

    }

    if (matching && cur_length == search_length) {//ƥ��ɹ�,�򷵻ؼ���
      cur_pos_in_array++;
      result.set(cur_pos_in_array);
      return result;
    } else {
      result.set(0);
      return result;
    }
  }

}
