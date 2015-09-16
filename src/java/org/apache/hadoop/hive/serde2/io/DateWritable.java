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
package org.apache.hadoop.hive.serde2.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hive.serde2.ByteStream.Output;
import org.apache.hadoop.hive.serde2.lazybinary.LazyBinaryUtils;
import org.apache.hadoop.hive.serde2.lazybinary.LazyBinaryUtils.VInt;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableUtils;


/**
 * DateWritable
 * Writable equivalent of java.sql.Date.
 *
 * Dates are of the format
 *    YYYY-MM-DD
 * 时间序列化,仅仅需要存储"距离1970-01-01多少天"即可表示任何时间格式
 */
public class DateWritable implements WritableComparable<DateWritable> {
  private static final Log LOG = LogFactory.getLog(DateWritable.class);

  private static final long MILLIS_PER_DAY = TimeUnit.DAYS.toMillis(1);//24小时对应的时间long毫秒值,即24*60*60*1000

  // Local time zone.
  // Java TimeZone has no mention of thread safety. Use thread local instance to be safe.
  private static final ThreadLocal<TimeZone> LOCAL_TIMEZONE = new ThreadLocal<TimeZone>() {
    @Override
    protected TimeZone initialValue() {
      return Calendar.getInstance().getTimeZone();
    }
  };

  // Internal representation is an integer representing day offset from our epoch value 1970-01-01
  //表示距离1970-01-01多少天,通过该值可以获取各种时间格式
  private int daysSinceEpoch = 0;

  /* Constructors */
  public DateWritable() {
  }

  public DateWritable(DateWritable d) {
    set(d);
  }

  public DateWritable(Date d) {
    set(d);
  }

  public DateWritable(int d) {
    set(d);
  }

  /**
   * Set the DateWritable based on the days since epoch date.
   * @param d integer value representing days since epoch date
   */
  public void set(int d) {
    daysSinceEpoch = d;
  }

  /**
   * Set the DateWritable based on the year/month/day of the date in the local timezone.
   * @param d Date value
   */
  public void set(Date d) {
    if (d == null) {
      daysSinceEpoch = 0;
      return;
    }

    //将d转化成距离1970-01-01多少天
    set(dateToDays(d));
  }

  public void set(DateWritable d) {
    set(d.daysSinceEpoch);
  }

  /**
   *
   * @return Date value corresponding to the date in the local time zone
   * 将距离1970-01-01多少天,转化成应该是什么时间戳
   */
  public Date get() {
    return new Date(daysToMillis(daysSinceEpoch));
  }

  //表示距离1970-01-01多少天,通过该值可以获取各种时间格式
  public int getDays() {
    return daysSinceEpoch;
  }

  /**
   *
   * @return time in seconds corresponding to this DateWritable
   * 将距离1970-01-01多少天,转化成应该是什么时间戳,时间戳单位是秒
   */
  public long getTimeInSeconds() {
    return get().getTime() / 1000;
  }

  //对应于函数getTimeInSeconds,参数l的单位是秒,因此要*1000表示时间戳
  public static Date timeToDate(long l) {
    return new Date(l * 1000);
  }

  //将距离1970-01-01多少天,转化成应该是什么时间戳
  public static long daysToMillis(int d) {
    // Convert from day offset to ms in UTC, then apply local timezone offset.
    long millisUtc = d * MILLIS_PER_DAY;
    return millisUtc - LOCAL_TIMEZONE.get().getOffset(millisUtc);
  }

  //将d转化成距离1970-01-01多少天
  public static int dateToDays(Date d) {
    // convert to equivalent time in UTC, then get day offset
    long millisLocal = d.getTime();
    long millisUtc = millisLocal + LOCAL_TIMEZONE.get().getOffset(millisLocal);
    return (int)(millisUtc / MILLIS_PER_DAY);
  }

  public void setFromBytes(byte[] bytes, int offset, int length, VInt vInt) {
    LazyBinaryUtils.readVInt(bytes, offset, vInt);
    assert (length == vInt.length);
    set(vInt.value);
  }

  public void writeToByteStream(Output byteStream) {
    LazyBinaryUtils.writeVInt(byteStream, getDays());
  }


  @Override
  public void readFields(DataInput in) throws IOException {
    daysSinceEpoch = WritableUtils.readVInt(in);
  }

  @Override
  public void write(DataOutput out) throws IOException {
    WritableUtils.writeVInt(out, daysSinceEpoch);
  }

  @Override
  public int compareTo(DateWritable d) {
    return daysSinceEpoch - d.daysSinceEpoch;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof DateWritable)) {
      return false;
    }
    return compareTo((DateWritable) o) == 0;
  }

  @Override
  public String toString() {
    return get().toString();
  }

  @Override
  public int hashCode() {
    return daysSinceEpoch;
  }
}
