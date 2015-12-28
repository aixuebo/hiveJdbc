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
 * find_in_set('ab','abc,b,ab,c,def') 返回值是3,即ab在第三个位置,下标从1开始计数
 * UDFFindInSet.
 * SELECT _FUNC_('ab','abc,b,ab,c,def') 返回值是3,即ab在第三个位置,下标从1开始计数
 * 
 * Returns null if either argument is null 如果两个参数中有任意一个参数是null,则返回null
 * Returns 0 if the first argument has any commas. 表示第一个参数中如果有任何逗号,则都返回0
 * 
 * _FUNC_(key,'311,128,345,956')=0 表示该函数返回值是0,即第一个参数没有在第二个参数中找到匹配对象,则返回0
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
	  
	//如果两个参数中有任意一个参数是null,则返回null
    if (s == null || txtarray == null) {
      return null;
    }

    byte[] search_bytes = s.getBytes();
    
    //第一个参数中如果有任何逗号,则都返回0
    for (int i = 0; i < s.getLength(); i++) {
      if (search_bytes[i] == ',') {
        result.set(0);
        return result;
      }
    }

    byte[] data = txtarray.getBytes();
    int search_length = s.getLength();

    int cur_pos_in_array = 0;//表示第二个参数已经匹配到第几个元素了,例如第二个参数是abc,ab 如果该参数为2,说明目前要匹配的是ab
    int cur_length = 0;//表示第一个参数的已经匹配到什么位置了
    boolean matching = true;//表示匹配成功

    for (int i = 0; i < txtarray.getLength(); i++) {
      if (data[i] == ',') {//遇到,号,则要重新与第一个参数进行匹配
        cur_pos_in_array++;
        if (matching && cur_length == search_length) {//如果匹配成功,则直接返回该位置即可
          result.set(cur_pos_in_array);
          return result;
        } else {//重新匹配
          matching = true;
          cur_length = 0;
        }
      } else {
        if (cur_length + 1 <= search_length) {//说明第二个参数中某一个元素 还尚未超过第一个参数长度,可以不断循环,例如第一个参数是abc,第二个参数是abd,现在已经循环到b了
        	/**
        	 * 1.matching如果是false,说明已经不匹配了,接下来也没有必要再去匹配,
        	 * 2.search_bytes[cur_length] != data[i],例如第一个参数是abc,第二个参数是abd,当c不匹配d的时候,说明匹配不成功,返回false
        	 */
          if (!matching || search_bytes[cur_length] != data[i]) {
            matching = false;
          }
        } else {//说明第二个参数中某一个元素，已经超过了第一个参数的长度,因此肯定是不匹配了,比如第一个参数是abc,第二个参数是abcd,肯定是不匹配了
          matching = false;
        }
        cur_length++;
      }

    }

    if (matching && cur_length == search_length) {//匹配成功,则返回即可
      cur_pos_in_array++;
      result.set(cur_pos_in_array);
      return result;
    } else {
      result.set(0);
      return result;
    }
  }

}
