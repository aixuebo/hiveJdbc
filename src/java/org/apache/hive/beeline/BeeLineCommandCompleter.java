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
package org.apache.hive.beeline;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import jline.console.completer.AggregateCompleter;
import jline.console.completer.Completer;
import jline.console.completer.NullCompleter;
import jline.console.completer.StringsCompleter;

//持有所有支持的命令集合---自动补全功能
class BeeLineCommandCompleter extends AggregateCompleter {

  public BeeLineCommandCompleter(List<Completer> completers) {
    super(completers);
  }

  public static List<Completer> getCompleters(BeeLine beeLine){
    List<Completer> completers = new LinkedList<Completer>();

    for (int i = 0; i < beeLine.commandHandlers.length; i++) {//循环配置的所有命令
      String[] cmds = beeLine.commandHandlers[i].getNames();//获取一个命令的所有name别名集合
      for (int j = 0; cmds != null && j < cmds.length; j++) {
        List<Completer> compl = new LinkedList<Completer>();//该命令需要的参数集合
        compl.add(new StringsCompleter(BeeLine.COMMAND_PREFIX + cmds[j]));//!+命令
        compl.addAll(Arrays.asList(beeLine.commandHandlers[i].getParameterCompleters()));//参数集合
        compl.add(new NullCompleter()); // last param no complete 说明最后一个参数不需要编译
        completers.add(new AggregateCompleter(compl.toArray(new Completer[0])));
      }
    }
    return completers;
  }
}