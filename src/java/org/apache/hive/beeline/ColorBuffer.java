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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * A buffer that can output segments using ANSI color.
 *
 */
final class ColorBuffer implements Comparable<Object> {
  private static final ColorBuffer.ColorAttr BOLD = new ColorAttr("\033[1m");//粗体
  private static final ColorBuffer.ColorAttr NORMAL = new ColorAttr("\033[m");//正常颜色
  private static final ColorBuffer.ColorAttr REVERS = new ColorAttr("\033[7m");
  private static final ColorBuffer.ColorAttr LINED = new ColorAttr("\033[4m");//添加下划线
  private static final ColorBuffer.ColorAttr GREY = new ColorAttr("\033[1;30m");
  private static final ColorBuffer.ColorAttr RED = new ColorAttr("\033[1;31m");
  private static final ColorBuffer.ColorAttr GREEN = new ColorAttr("\033[1;32m");
  private static final ColorBuffer.ColorAttr BLUE = new ColorAttr("\033[1;34m");
  private static final ColorBuffer.ColorAttr CYAN = new ColorAttr("\033[1;36m");
  private static final ColorBuffer.ColorAttr YELLOW = new ColorAttr("\033[1;33m");
  private static final ColorBuffer.ColorAttr MAGENTA = new ColorAttr("\033[1;35m");
  private static final ColorBuffer.ColorAttr INVISIBLE = new ColorAttr("\033[8m");

  private final List<Object> parts = new LinkedList<Object>();//存储的字符串内容集合 以及 颜色信息的集合
  private int visibleLength = 0;//有效的总长度---颜色是不算有效长度的,即该长度表示的是字符串的长度

  private final boolean useColor;//true表示使用颜色


  public ColorBuffer(boolean useColor) {
    this.useColor = useColor;
    append("");
  }

  public ColorBuffer(String str, boolean useColor) {
    this.useColor = useColor;
    append(str);
  }

  /**
   * Pad the specified String with spaces to the indicated length
   *
   * @param str
   *          the String to pad
   * @param len
   *          the length we want the return String to be
   * @return the passed in String with spaces appended until the
   *         length matches the specified length.
   */
  ColorBuffer pad(ColorBuffer str, int len) {
    while (str.getVisibleLength() < len) {
      str.append(" ");
    }
    return append(str);
  }

  ColorBuffer center(String str, int len) {
    StringBuilder buf = new StringBuilder(str);
    while (buf.length() < len) {
      buf.append(" ");
      if (buf.length() < len) {
        buf.insert(0, " ");
      }
    }
    return append(buf.toString());
  }

  ColorBuffer pad(String str, int len) {
    if (str == null) {
      str = "";
    }
    return pad(new ColorBuffer(str, false), len);
  }

  public String getColor() {
    return getBuffer(useColor);
  }

  public String getMono() {
    return getBuffer(false);
  }

  String getBuffer(boolean color) {
    StringBuilder buf = new StringBuilder();
    for (Object part : parts) {
      if (!color && part instanceof ColorBuffer.ColorAttr) {
        continue;
      }
      buf.append(part.toString());
    }
    return buf.toString();
  }


  /**
   * Truncate the ColorBuffer to the specified length and return
   * the new ColorBuffer. Any open color tags will be closed.
   * Do nothing if the specified length is <= 0.
   * 一个个迭代,截取到len个长度为止,颜色是不算有效长度的
   */
  public ColorBuffer truncate(int len) {
    if (len <= 0) {
      return this;
    }
    ColorBuffer cbuff = new ColorBuffer(useColor);
    ColorBuffer.ColorAttr lastAttr = null;
    for (Iterator<Object> i = parts.iterator(); cbuff.getVisibleLength() < len && i.hasNext();) {
      Object next = i.next();
      if (next instanceof ColorBuffer.ColorAttr) {
        lastAttr = (ColorBuffer.ColorAttr) next;
        cbuff.append((ColorBuffer.ColorAttr) next);
        continue;
      }
      String val = next.toString();
      if (cbuff.getVisibleLength() + val.length() > len) {//只是截取一部分文字,因为全部文字会超出范围
        int partLen = len - cbuff.getVisibleLength();
        val = val.substring(0, partLen);
      }
      cbuff.append(val);
    }

    // close off the buffer with a normal tag
    if (lastAttr != null && lastAttr != NORMAL) {
      cbuff.append(NORMAL);
    }

    return cbuff;
  }


  @Override
  public String toString() {
    return getColor();
  }

  public ColorBuffer append(String str) {
    parts.add(str);
    visibleLength += str.length();
    return this;
  }

  public ColorBuffer append(ColorBuffer buf) {
    parts.addAll(buf.parts);
    visibleLength += buf.getVisibleLength();
    return this;
  }

  //添加一个属性信息--即颜色信息
  private ColorBuffer append(ColorBuffer.ColorAttr attr) {
    parts.add(attr);//颜色是不算有效长度的
    return this;
  }

  public int getVisibleLength() {
    return visibleLength;
  }

  //为给定的val 附加一定的颜色
  private ColorBuffer append(ColorBuffer.ColorAttr attr, String val) {
    parts.add(attr);
    parts.add(val);
    parts.add(NORMAL);//将添加后的结果重新恢复为正常颜色
    visibleLength += val.length();
    return this;
  }

  //为给定字符串添加各种颜色
  public ColorBuffer bold(String str) {
    return append(BOLD, str);
  }

  public ColorBuffer lined(String str) {
    return append(LINED, str);
  }

  public ColorBuffer grey(String str) {
    return append(GREY, str);
  }

  public ColorBuffer red(String str) {
    return append(RED, str);
  }

  public ColorBuffer blue(String str) {
    return append(BLUE, str);
  }

  public ColorBuffer green(String str) {
    return append(GREEN, str);
  }

  public ColorBuffer cyan(String str) {
    return append(CYAN, str);
  }

  public ColorBuffer yellow(String str) {
    return append(YELLOW, str);
  }

  public ColorBuffer magenta(String str) {
    return append(MAGENTA, str);
  }

  private static class ColorAttr {
    private final String attr;

    public ColorAttr(String attr) {
      this.attr = attr;
    }

    @Override
    public String toString() {
      return attr;
    }
  }

  public int compareTo(Object other) {
    return getMono().compareTo(((ColorBuffer) other).getMono());
  }
}