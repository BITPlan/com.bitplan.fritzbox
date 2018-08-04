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

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.ingo.fritzbox.data.Call;
import de.ingo.fritzbox.utils.CsvParser;

/**
 * access to call list
 * @author wf
 *
 */
public class CallListImpl implements CallList {
  // prepare a LOGGER
  protected static Logger LOGGER = Logger.getLogger("com.bitplan.fritzbox");
  
  private FritzBoxSession session;

  public CallListImpl(FritzBoxSession session) {
    this.session = session;
  }

  /**
   * Requests the call list from the FritzBox and returns it.
   *
   * @return A list of call objects.
   * @throws Throwable
   *           when an exception during communication occurred.
   */
  public List<Call> getCallList() throws Throwable {
    try {
      
      final String baseUrl = "/fon_num/foncalls_list.lua";
      final String params= "?csv=";
      final String response = session.getResponse(baseUrl, params);
      return CsvParser.parseCallList(response);
    } catch (Throwable th) {
      LOGGER.log(Level.SEVERE, th.getMessage(), th);
      throw th;
    }
  }
}
