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

/**
 * Home Automation functions
 * see
 * https://avm.de/fileadmin/user_upload/Global/Service/Schnittstellen/AHA-HTTP-Interface.pdf
 * 
 * @author wf
 *
 */
public interface HomeAutomation {
  /**
   * getswitchlist
   * 
   * @return list of all known switches
   * @throws Exception
   */
  public List<String> getSwitchList() throws Exception;

  /**
   * setswitchon / setswitchoff
   * 
   * @param ain
   * @param newState
   * @return true if successful
   * @throws Exception
   */
  public String setSwitchOnOff(String ain, boolean newState) throws Exception;

  /**
   * getswitchstate
   * 
   * @param ain
   * @return the switch state
   * @throws Exception
   */
  public boolean getSwitchState(String ain) throws Exception;

  /**
   * getswitchpresent
   * 
   * @param ain
   * @return whether the switch is present
   * @throws Exception
   */
  public String getSwitchPresent(String ain) throws Exception;

  /**
   * getswitchpower
   * 
   * @param ain
   * @return the switch power
   * @throws Exception
   */
  public Double getSwitchPowerWatt(String ain) throws Exception;

  /**
   * getswitchenergy
   * 
   * @param ain
   * @return the switch energy
   * @throws Exception
   */
  public Double getSwitchEnergy(String ain) throws Exception;

  /**
   * getswitchname
   * 
   * @param ain
   * @return the switch name
   * @throws Exception
   */
  public String getSwitchName(String ain) throws Exception;

  /**
   * getdevicelistinfos
   * 
   * @return the list of devices
   * @throws Exception
   */
  public DeviceList getDeviceListInfos() throws Exception;

  /**
   * gettemperature
   * 
   * @param ain
   * @return the temperature
   * @throws Exception
   */
  public Double getTemperature(String ain) throws Exception;

  /**
   * get the ain for the given devicename
   * 
   * @param deviceName
   * @return the ain or null if not found
   * @throws Exception
   */
  public String getAinForName(String deviceName) throws Exception;
}
