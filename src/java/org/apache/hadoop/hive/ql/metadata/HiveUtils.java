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

package org.apache.hadoop.hive.ql.metadata;

import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.common.JavaUtils;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.metastore.api.FieldSchema;
import org.apache.hadoop.hive.ql.index.HiveIndexHandler;
import org.apache.hadoop.hive.ql.security.HadoopDefaultAuthenticator;
import org.apache.hadoop.hive.ql.security.HiveAuthenticationProvider;
import org.apache.hadoop.hive.ql.security.authorization.DefaultHiveAuthorizationProvider;
import org.apache.hadoop.hive.ql.security.authorization.HiveAuthorizationProvider;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.util.ReflectionUtils;

/**
 * General collection of helper functions.
 *
 */
public final class HiveUtils {

  public static final char QUOTE = '"';
  public static final char COLON = ':';
  public static final String LBRACKET = "[";
  public static final String RBRACKET = "]";
  public static final String LBRACE = "{";
  public static final String RBRACE = "}";
  public static final String LINE_SEP = System.getProperty("line.separator");//回车 换行 字节数组是13和10

  /**
  	定义字符串String str = "ss\\s\n\rs";
  1.打印该字符串System.out.println(str);
  输出:
	ss\s

	s
   总结:可以看出来该字符串自动被转换了
   2.原样打印该字符串 System.out.println(escapeString(str));
     输出:依然是:ss\\s\n\rs
     
     该方法是原样输出字符串内容,防止转义等情况发生
   */
  public static String escapeString(String str) {
    int length = str.length();
    StringBuilder escape = new StringBuilder(length + 16);

    for (int i = 0; i < length; ++i) {
      char c = str.charAt(i);
      switch (c) {
      case '"':
      case '\\':
        escape.append('\\');
        escape.append(c);
        break;
      case '\b':
        escape.append('\\');
        escape.append('b');
        break;
      case '\f':
        escape.append('\\');
        escape.append('f');
        break;
      case '\n':
        escape.append('\\');
        escape.append('n');
        break;
      case '\r':
        escape.append('\\');
        escape.append('r');
        break;
      case '\t':
        escape.append('\\');
        escape.append('t');
        break;
      default:
        // Control characeters! According to JSON RFC u0020
        if (c < ' ') {//asscii比32还小的话,使用16进制
          String hex = Integer.toHexString(c);
          escape.append('\\');
          escape.append('u');
          for (int j = 4; j > hex.length(); --j) {
            escape.append('0');
          }
          escape.append(hex);
        } else {
          escape.append(c);
        }
        break;
      }
    }
    return (escape.toString());
  }

  static final byte[] escapeEscapeBytes = "\\\\".getBytes();//92 92 即\ \ 
  static final byte[] escapeUnescapeBytes = "\\".getBytes();//92 即\
  static final byte[] newLineEscapeBytes = "\\n".getBytes();;//92 110,即\和n
  static final byte[] newLineUnescapeBytes = "\n".getBytes();//10
  static final byte[] carriageReturnEscapeBytes = "\\r".getBytes();//92 114,即\和r
  static final byte[] carriageReturnUnescapeBytes = "\r".getBytes();//13
  static final byte[] tabEscapeBytes = "\\t".getBytes();//92 116,即\和t
  static final byte[] tabUnescapeBytes = "\t".getBytes();//9
  static final byte[] ctrlABytes = "\u0001".getBytes();//1

  /**
   * escapeText(new Text("ss\\s\n\rs")),返回值还是原样的new Text("ss\\s\n\rs")
   */
  public static Text escapeText(Text text) {
    int length = text.getLength();
    byte[] textBytes = text.getBytes();

    Text escape = new Text(text);
    escape.clear();

    for (int i = 0; i < length; ++i) {
      int c = text.charAt(i);
      byte[] escaped;//转义字节数组
      int start;//从转义字节数组的第几个位置开始截取
      int len;//截取转义字节数组的长度

      switch (c) {

      case '\\':
        escaped = escapeEscapeBytes;
        start = 0;
        len = escaped.length;
        break;

      case '\n':
        escaped = newLineEscapeBytes;
        start = 0;
        len = escaped.length;
        break;

      case '\r':
        escaped = carriageReturnEscapeBytes;
        start = 0;
        len = escaped.length;
        break;

      case '\t':
        escaped = tabEscapeBytes;
        start = 0;
        len = escaped.length;
        break;

      case '\u0001':
        escaped = tabUnescapeBytes;
        start = 0;
        len = escaped.length;
        break;

      default:
        escaped = textBytes;
        start = i;
        len = 1;
        break;
      }

      escape.append(escaped, start, len);

    }
    return escape;
  }

  /**
   * 将text中的内容进行解析
   * 例如:unescapeText(new Text("ss\\s\n\rs"))
   * 输出的text为:
   * ss\s

	s
	返回值是7.即输出的长度
   */
  public static int unescapeText(Text text) {
    Text escape = new Text(text);
    text.clear();

    int length = escape.getLength();
    byte[] textBytes = escape.getBytes();

    boolean hadSlash = false;
    for (int i = 0; i < length; ++i) {
      int c = escape.charAt(i);
      switch (c) {
      case '\\':
        if (hadSlash) {
          text.append(textBytes, i, 1);
          hadSlash = false;
        }
        else {
          hadSlash = true;
        }
        break;
      case 'n':
        if (hadSlash) {
          byte[] newLine = newLineUnescapeBytes;
          text.append(newLine, 0, newLine.length);
        }
        else {
          text.append(textBytes, i, 1);
        }
        hadSlash = false;
        break;
      case 'r':
        if (hadSlash) {
          byte[] carriageReturn = carriageReturnUnescapeBytes;
          text.append(carriageReturn, 0, carriageReturn.length);
        }
        else {
          text.append(textBytes, i, 1);
        }
        hadSlash = false;
        break;

      case 't':
        if (hadSlash) {
          byte[] tab = tabUnescapeBytes;
          text.append(tab, 0, tab.length);
        }
        else {
          text.append(textBytes, i, 1);
        }
        hadSlash = false;
        break;

      case '\t':
        if (hadSlash) {
          text.append(textBytes, i-1, 1);
          hadSlash = false;
        }

        byte[] ctrlA = ctrlABytes;
        text.append(ctrlA, 0, ctrlA.length);
        break;

      default:
        if (hadSlash) {
          text.append(textBytes, i-1, 1);
          hadSlash = false;
        }

        text.append(textBytes, i, 1);
        break;
      }
    }
    return text.getLength();
  }

  /**
   * 轻量级别的保留字符串内容,仅仅保留\n \r \t三种转义
   * 例如lightEscapeString("ss\\s\n\rs")
   * 返回值ss\s\n\rs
   * 可见,\\被转义成\了,但是\r和\n没有被转义
   */
  public static String lightEscapeString(String str) {
    int length = str.length();
    StringBuilder escape = new StringBuilder(length + 16);

    for (int i = 0; i < length; ++i) {
      char c = str.charAt(i);
      switch (c) {
      case '\n':
        escape.append('\\');
        escape.append('n');
        break;
      case '\r':
        escape.append('\\');
        escape.append('r');
        break;
      case '\t':
        escape.append('\\');
        escape.append('t');
        break;
      default:
        escape.append(c);
        break;
      }
    }
    return (escape.toString());
  }

  /**
   * Regenerate an identifier as part of unparsing it back to SQL text.
   * 对sql的关键字而言,添加``字符
   */
  public static String unparseIdentifier(String identifier) {
    // In the future, if we support arbitrary characters in
    // identifiers, then we'll need to escape any backticks
    // in identifier by doubling them up.
    return "`" + identifier + "`";
  }

  public static HiveStorageHandler getStorageHandler(
    Configuration conf, String className) throws HiveException {

    if (className == null) {
      return null;
    }
    try {
      Class<? extends HiveStorageHandler> handlerClass =
        (Class<? extends HiveStorageHandler>)
        Class.forName(className, true, JavaUtils.getClassLoader());
      HiveStorageHandler storageHandler = (HiveStorageHandler)
        ReflectionUtils.newInstance(handlerClass, conf);
      return storageHandler;
    } catch (ClassNotFoundException e) {
      throw new HiveException("Error in loading storage handler."
          + e.getMessage(), e);
    }
  }

  private HiveUtils() {
    // prevent instantiation
  }

  public static HiveIndexHandler getIndexHandler(HiveConf conf,
      String indexHandlerClass) throws HiveException {

    if (indexHandlerClass == null) {
      return null;
    }
    try {
      Class<? extends HiveIndexHandler> handlerClass =
        (Class<? extends HiveIndexHandler>)
        Class.forName(indexHandlerClass, true, JavaUtils.getClassLoader());
      HiveIndexHandler indexHandler = (HiveIndexHandler)
        ReflectionUtils.newInstance(handlerClass, conf);
      return indexHandler;
    } catch (ClassNotFoundException e) {
      throw new HiveException("Error in loading index handler."
          + e.getMessage(), e);
    }
  }

  @SuppressWarnings("unchecked")
  public static HiveAuthorizationProvider getAuthorizeProviderManager(
      Configuration conf, HiveConf.ConfVars authorizationProviderConfKey,
      HiveAuthenticationProvider authenticator) throws HiveException {

    String clsStr = HiveConf.getVar(conf, authorizationProviderConfKey);

    HiveAuthorizationProvider ret = null;
    try {
      Class<? extends HiveAuthorizationProvider> cls = null;
      if (clsStr == null || clsStr.trim().equals("")) {
        cls = DefaultHiveAuthorizationProvider.class;
      } else {
        cls = (Class<? extends HiveAuthorizationProvider>) Class.forName(
            clsStr, true, JavaUtils.getClassLoader());
      }
      if (cls != null) {
        ret = ReflectionUtils.newInstance(cls, conf);
      }
    } catch (Exception e) {
      throw new HiveException(e);
    }
    ret.setAuthenticator(authenticator);
    return ret;
  }

  /**
   * 返回hadoop的访问权限
   * @param conf
   * @param authenticatorConfKey
   * @return
   * @throws HiveException
   */
  @SuppressWarnings("unchecked")
  public static HiveAuthenticationProvider getAuthenticator(
      Configuration conf, HiveConf.ConfVars authenticatorConfKey
      ) throws HiveException {

    String clsStr = HiveConf.getVar(conf, authenticatorConfKey);

    HiveAuthenticationProvider ret = null;
    try {
      Class<? extends HiveAuthenticationProvider> cls = null;
      if (clsStr == null || clsStr.trim().equals("")) {
        cls = HadoopDefaultAuthenticator.class;
      } else {
        cls = (Class<? extends HiveAuthenticationProvider>) Class.forName(
            clsStr, true, JavaUtils.getClassLoader());
      }
      if (cls != null) {
        ret = ReflectionUtils.newInstance(cls, conf);
      }
    } catch (Exception e) {
      throw new HiveException(e);
    }
    return ret;
  }


  /**
   * Convert FieldSchemas to columnNames with backticks around them.
   * 将所有的属性,转换成`column1`,`column2`,`column3` 这样的字符串返回
   * */
  public static String getUnparsedColumnNamesFromFieldSchema(
      List<FieldSchema> fieldSchemas) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < fieldSchemas.size(); i++) {
      if (i > 0) {
        sb.append(",");
      }
      sb.append(HiveUtils.unparseIdentifier(fieldSchemas.get(i).getName()));
    }
    return sb.toString();
  }
}
