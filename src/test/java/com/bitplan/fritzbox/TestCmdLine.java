/**
 * Copyright (c) 2018 BITPlan GmbH
 *
 * http://www.bitplan.com
 *
 * This file is part of the Opensource project at:
 * https://github.com/BITPlan/com.bitplan.fritzbox
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bitplan.fritzbox;

import org.junit.Test;

/**
 * test the command line interface
 * @author wf
 *
 */
public class TestCmdLine {
  // adapt to your Fritz Box environment
  public static final String DEVICE_TO_READ="Media";
  
  @Test
  public void testCommandLineList() {
    String args[]= {"-l"};
    CmdLine cmdLine=new CmdLine();
    cmdLine.maininstance(args);
  }
  
  @Test
  public void testCommandLineRead() {
    String args[]= {"-r",DEVICE_TO_READ};
    CmdLine cmdLine=new CmdLine();
    cmdLine.maininstance(args);
  }

}
