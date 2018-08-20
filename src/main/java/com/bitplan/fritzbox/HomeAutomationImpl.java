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

import java.util.Arrays;
import java.util.List;

/**
 * Home Automation access
 * 
 * @author wf
 *
 */
public class HomeAutomationImpl implements HomeAutomation {
  public static final String BASE_URL = "/webservices/homeautoswitch.lua";
  private FritzBoxSession session;

  /**
   * construct me from a session
   * 
   * @param session
   */
  public HomeAutomationImpl(FritzBoxSession session) {
    this.session = session;
  }

  @Override
  public List<String> getSwitchList() throws Exception {
    String params = String.format("?switchcmd=getswitchlist");
    String switches = session.getResponse(BASE_URL, params);
    String[] switchList = switches.split(",");
    return Arrays.asList(switchList);
  }

  /**
   * get the Device List
   * 
   * @return the list of devicesls
   * 
   * @throws Exception
   */
  public DeviceList getDeviceListInfos() throws Exception {
    String params = String.format("?switchcmd=getdevicelistinfos");
    DeviceList deviceList = session.getTypedResponse(BASE_URL, params,
        DeviceList.class);
    return deviceList;
  }

  /**
   * switch the power state of the given device
   * 
   * @param ain
   * @param newState
   * @throws Exception
   * @return true if successful
   */
  public String setSwitchOnOff(String ain, boolean newState) throws Exception {
    final String command = newState ? "setswitchon" : "setswitchoff";
    String result = getResponse(ain, command);
    return result;
  }

  /**
   * get the name of the given switch
   * 
   * @param ain
   * @return - the name
   * @throws Exception
   */
  public String getSwitchName(String ain) throws Exception {
    String name = getResponse(ain, "getswitchname");
    return name;
  }

  @Override
  public boolean getSwitchState(String ain) throws Exception {
    String state = getResponse(ain, "getswitchstate");
    return "1".equals(state);
  }

  @Override
  public String getSwitchPresent(String ain) throws Exception {
    String state = getResponse(ain, "getswitchpresent");
    return state;
  }

  @Override
  public Double getSwitchPowerWatt(String ain) throws Exception {
    String powerStr = getResponse(ain, "getswitchpower");
    double power = Double.parseDouble(powerStr);
    return power / 1000.0;
  }

  @Override
  public Double getSwitchEnergy(String ain) throws Exception {
    String energy = getResponse(ain, "getswitchenergy");
    return Double.parseDouble(energy);
  }

  @Override
  public Double getTemperature(String ain) throws Exception {
    String temperatureStr = getResponse(ain, "gettemperature");
    double temperature = Double.parseDouble(temperatureStr);
    return temperature / 10;
  }

  /**
   * get the result for the given ain and command
   * 
   * @param ain
   * @param command
   * @return the response
   * @throws Exception
   */
  public String getResponse(String ain, String command) throws Exception {
    String params = String.format("?switchcmd=%s&ain=%s", command, ain);
    String result = session.getResponse(BASE_URL, params);
    return result;
  }

  /**
   * get the ain for the given devicename
   * 
   * @param deviceName
   * @return the ain or null if not found
   * @throws Exception
   */
  public String getAinForName(String deviceName) throws Exception {
    String ain = null;
    if (deviceName != null) {
      DeviceList deviceList = getDeviceListInfos();
      for (Device device : deviceList.devices) {
        if (deviceName.equals(device.name)) {
          ain = device.getAin();
        }
      }
    }
    return ain;
  }

}
