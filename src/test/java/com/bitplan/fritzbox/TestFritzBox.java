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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import de.ingo.fritzbox.data.Call;

/**
 * test the fritz box access
 */
public class TestFritzBox extends Basetest {
  
  public static final int EXPECTED_DEVICES = 11; // adapt to your fritz box
                                                 // setup
  public static final int EXPECTED_SWITCHES = 10;
  private FritzBoxSession session = null;

  /**
   * get a logged in FritzBox session
   * 
   * @return the session
   */
  public FritzBoxSession getFritzBox() throws Exception {
    FritzBoxSessionImpl.debug = TestSuite.debug;
    if (session == null) {
      Fritzbox fritzbox = FritzboxImpl.readFromProperties();
      if (fritzbox == null) {
        fritzbox=FritzboxMock.getFritzbox();
      }
      assertNotNull("There should be a url in the fritzbox configuration",
          fritzbox.getUrl());
      assertNotNull("There should be a password in the fritzbox configuration",
          fritzbox.getPassword());
      assertNotNull("There should be a username in the fritzbox configuration",
          fritzbox.getUsername());
      if (session == null) {
        session=fritzbox.login();
      }
    }
    return session;
  }

  @Test
  public void testLogin() throws Exception {
    FritzBoxSession lsession = getFritzBox();
    assertNotNull(lsession);
    if (lsession != null) {
      lsession.logout();
      session = null;
    }
  }

  @Test
  public void testHomeAutomation() throws Exception {
    FritzBoxSession lsession = getFritzBox();
    if (lsession != null) {
      HomeAutomationImpl homeAutomation = new HomeAutomationImpl(session);
      List<String> switches = homeAutomation.getSwitchList();
      assertNotNull(switches);
      assertEquals(EXPECTED_SWITCHES, switches.size());
      // FritzBoxSessionImpl.debug=true;
      DeviceList deviceList = homeAutomation.getDeviceListInfos();
      assertEquals(1, deviceList.version);
      assertNotNull(deviceList.devices);
      assertEquals(EXPECTED_DEVICES, deviceList.devices.size());
      for (Device device : deviceList.devices) {
        if (TestSuite.debug) {
          System.out.println(device);
        }
        assertNotNull(device.id);
        assertNotNull(homeAutomation.getSwitchName(device.getAin()));
      }
    }
  }

  @Test
  public void testMd5() {
    FritzBoxSession session = new FritzBoxSessionImpl(new FritzboxImpl());

    String inputs[] = { "secret1", "", "test1", "12345678z-äbc",
        "!\"§$%&/()=?ßüäöÜÄÖé-.,;:_`´+*#'<>≤|" };
    String expected[] = { "210df5bd3d63f46e3a0b68e967eae6d8",
        "d41d8cd98f00b204e9800998ecf8427e", "16c47151c18ac087cd12b3a70746c790",
        "a59dffd36899371d6e8ba951c1eb1171",
        "cb3bab6405639bf466eac17e43534a7d" };
    for (int i = 0; i < inputs.length; i++) {
      String md5 = session.getMd5(inputs[i]);
      // System.out.println(inputs[i]+"="+md5);
      assertEquals(expected[i], md5);
    }
  }
  
  @Test
  public void testCallList() throws Throwable {
    FritzBoxSession lsession = getFritzBox();
    if (lsession != null) {
      // FritzBoxSessionImpl.debug=true;
      // FritzBoxSessionImpl.domockito=true;
      CallAPI callList=new CallAPIImpl(session);
      List<Call> calls = callList.getCallList();
      assertNotNull(calls);
      assertTrue(calls.size()>=2);
    }
  }

}
