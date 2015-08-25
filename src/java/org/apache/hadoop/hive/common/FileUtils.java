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

package org.apache.hadoop.hive.common;

import java.io.IOException;
import java.net.URI;
import java.util.BitSet;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.Shell;


/**
 * Collection of file manipulation utilities common across Hive.
 */
public final class FileUtils {

  /**
   * Variant of Path.makeQualified that qualifies the input path against the default file system
   * indicated by the configuration
   *
   * This does not require a FileSystem handle in most cases - only requires the Filesystem URI.
   * This saves the cost of opening the Filesystem - which can involve RPCs - as well as cause
   * errors
   *
   * @param path
   *          path to be fully qualified
   * @param conf
   *          Configuration file
   * @return path qualified relative to default file system
   */
  public static Path makeQualified(Path path, Configuration conf) throws IOException {

    if (!path.isAbsolute()) {
      // in this case we need to get the working directory
      // and this requires a FileSystem handle. So revert to
      // original method.
      return path.makeQualified(FileSystem.get(conf));
    }

    URI fsUri = FileSystem.getDefaultUri(conf);
    URI pathUri = path.toUri();

    String scheme = pathUri.getScheme();
    String authority = pathUri.getAuthority();

    // validate/fill-in scheme and authority. this follows logic
    // identical to FileSystem.get(URI, conf) - but doesn't actually
    // obtain a file system handle

    if (scheme == null) {
      // no scheme - use default file system uri
      scheme = fsUri.getScheme();
      authority = fsUri.getAuthority();
      if (authority == null) {
        authority = "";
      }
    } else {
      if (authority == null) {
        // no authority - use default one if it applies
        if (scheme.equals(fsUri.getScheme()) && fsUri.getAuthority() != null) {
          authority = fsUri.getAuthority();
        } else {
          authority = "";
        }
      }
    }

    return new Path(scheme, authority, pathUri.getPath());
  }

  private FileUtils() {
    // prevent instantiation
  }

/**
 *    * partion的组装
   * partionKey=partionValue/partionKey=partionValue
 * @param partCols
 * @param vals
 * @return
 */
  public static String makePartName(List<String> partCols, List<String> vals) {
    return makePartName(partCols, vals, null);
  }

  /**
   * Makes a valid partition name.
   * @param partCols The partition keys' names
   * @param vals The partition values
   * @param defaultStr
   *         The default name given to a partition value if the respective value is empty or null.
   * @return An escaped, valid partition name.
   * partion的组装
   * partionKey=partionValue/partionKey=partionValue
   */
  public static String makePartName(List<String> partCols, List<String> vals,
      String defaultStr) {
    StringBuilder name = new StringBuilder();
    for (int i = 0; i < partCols.size(); i++) {
      if (i > 0) {
        name.append(Path.SEPARATOR);
      }
      name.append(escapePathName((partCols.get(i)).toLowerCase(), defaultStr));
      name.append('=');
      name.append(escapePathName(vals.get(i), defaultStr));
    }
    return name.toString();
  }

  /**
   * default directory will have the same depth as number of skewed columns
   * this will make future operation easy like DML merge, concatenate merge
   * @param skewedCols
   * @param name
   * @return
   */
  public static String makeDefaultListBucketingDirName(List<String> skewedCols,
      String name) {
    String lbDirName;
    String defaultDir = FileUtils.escapePathName(name);
    StringBuilder defaultDirPath = new StringBuilder();
    for (int i = 0; i < skewedCols.size(); i++) {
      if (i > 0) {
        defaultDirPath.append(Path.SEPARATOR);
      }
      defaultDirPath.append(defaultDir);
    }
    lbDirName = defaultDirPath.toString();
    return lbDirName;
  }

  /**
   * Makes a valid list bucketing directory name.
   * @param lbCols The skewed keys' names
   * @param vals The skewed values
   * @return An escaped, valid list bucketing directory name.
   * 返回编码后的lbCols=vals/lbCols=vals
   */
  public static String makeListBucketingDirName(List<String> lbCols, List<String> vals) {
    StringBuilder name = new StringBuilder();
    for (int i = 0; i < lbCols.size(); i++) {
      if (i > 0) {
        name.append(Path.SEPARATOR);
      }
      name.append(escapePathName((lbCols.get(i)).toLowerCase()));
      name.append('=');
      name.append(escapePathName(vals.get(i)));
    }
    return name.toString();
  }

  // NOTE: This is for generating the internal path name for partitions. Users
  // should always use the MetaStore API to get the path name for a partition.
  // Users should not directly take partition values and turn it into a path
  // name by themselves, because the logic below may change in the future.
  //
  // In the future, it's OK to add new chars to the escape list, and old data
  // won't be corrupt, because the full path name in metastore is stored.
  // In that case, Hive will continue to read the old data, but when it creates
  // new partitions, it will use new names.
  // edit : There are some use cases for which adding new chars does not seem
  // to be backward compatible - Eg. if partition was created with name having
  // a special char that you want to start escaping, and then you try dropping
  // the partition with a hive version that now escapes the special char using
  // the list below, then the drop partition fails to work.
  //需要转义的字符,需要转义的字符的acsii一定是小于128的
  static BitSet charToEscape = new BitSet(128);
  static {
    for (char c = 0; c < ' '; c++) {
      charToEscape.set(c);
    }

    /**
     * ASCII 01-1F are HTTP control characters that need to be escaped.
     * \u000A and \u000D are \n and \r, respectively.
     */
    char[] clist = new char[] {'\u0001', '\u0002', '\u0003', '\u0004',
        '\u0005', '\u0006', '\u0007', '\u0008', '\u0009', '\n', '\u000B',
        '\u000C', '\r', '\u000E', '\u000F', '\u0010', '\u0011', '\u0012',
        '\u0013', '\u0014', '\u0015', '\u0016', '\u0017', '\u0018', '\u0019',
        '\u001A', '\u001B', '\u001C', '\u001D', '\u001E', '\u001F',
        '"', '#', '%', '\'', '*', '/', ':', '=', '?', '\\', '\u007F', '{',
        '[', ']', '^'};

    for (char c : clist) {
      charToEscape.set(c);
    }

    if(Shell.WINDOWS){
      //On windows, following chars need to be escaped as well
      char [] winClist = {' ', '<','>','|'};
      for (char c : winClist) {
        charToEscape.set(c);
      }
    }

  }

  //判断该c字符是否需要转义
  static boolean needsEscaping(char c) {
    return c >= 0 && c < charToEscape.size() && charToEscape.get(c);
  }

  public static String escapePathName(String path) {
    return escapePathName(path, null);
  }

  /**
   * Escapes a path name.
   * @param path The path to escape.
   * @param defaultPath
   *          The default name for the path, if the given path is empty or null.
   * @return An escaped path name.
   * 对字符串进行编码,遇到一些可疑的字符,将其转换为16进制
   */
  public static String escapePathName(String path, String defaultPath) {

    // __HIVE_DEFAULT_NULL__ is the system default value for null and empty string.
    // TODO: we should allow user to specify default partition or HDFS file location.
    if (path == null || path.length() == 0) {
      if (defaultPath == null) {
        //previously, when path is empty or null and no default path is specified,
        // __HIVE_DEFAULT_PARTITION__ was the return value for escapePathName
        return "__HIVE_DEFAULT_PARTITION__";
      } else {
        return defaultPath;
      }
    }

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < path.length(); i++) {
      char c = path.charAt(i);
      if (needsEscaping(c)) {//需要转义
    	//%+c进行16进制转换,最后是2位返回
        sb.append('%');
        sb.append(String.format("%1$02X", (int) c));//%1$02X含义:%1$ 表示获取第一个参数,02表示第一个参数转换后仅仅获取2位数,x表示用16进制表示第一个参数
      } else {
        sb.append(c);
      }
    }
    return sb.toString();
  }

  //将字符串进行反解析,与escapePathName相反,对16进制的字符转换成字符串
  public static String unescapePathName(String path) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < path.length(); i++) {
      char c = path.charAt(i);
      if (c == '%' && i + 2 < path.length()) {
        int code = -1;
        try {
        	//将%后面的两位获取出来,按照16进制转换成10进制数
          code = Integer.valueOf(path.substring(i + 1, i + 3), 16);
        } catch (Exception e) {
          code = -1;
        }
        if (code >= 0) {//将转换的10进制数转换成char字符,添加到字符串中
          sb.append((char) code);
          i += 2;
          continue;
        }
      }
      sb.append(c);
    }
    return sb.toString();
  }

  /**
   * Recursively lists status for all files starting from a particular directory (or individual file
   * as base case).
   *
   * @param fs
   *          file system
   *
   * @param fileStatus
   *          starting point in file system
   *
   * @param results
   *          receives enumeration of all files found
   * 递归fileStatus,查找所有的文件,都添加到results集合中       
   */
  public static void listStatusRecursively(FileSystem fs, FileStatus fileStatus,
      List<FileStatus> results) throws IOException {

    if (fileStatus.isDir()) {
      for (FileStatus stat : fs.listStatus(fileStatus.getPath())) {
        listStatusRecursively(fs, stat, results);
      }
    } else {
      results.add(fileStatus);
    }
  }

}
