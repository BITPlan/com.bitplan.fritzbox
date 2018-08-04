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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="device")
public class Device {
  @XmlAttribute
  public String identifier;
  @XmlAttribute
  public String id;
  @XmlAttribute
  public int functionbitmask;
  @XmlAttribute
  public String fwversion;
  @XmlAttribute
  public String manufacturer;
  @XmlAttribute
  public String productname;
  @XmlElement
  public int present;
  @XmlElement
  public String name;
  @XmlElement(name="switch")
  public SwitchState state;
  @XmlElement
  PowerMeter powermeter;
  @XmlElement
  Temperature temperature;
  
  @XmlTransient
  public String getAin() {
    String ain=identifier.replace(" ", "");
    return ain;
  }
  
  @Override
  public String toString() {
    return "Device [identifier=" + identifier + ", id=" + id
        + ", functionBitmask=" + functionbitmask + ", firmwareVersion="
        + fwversion + ", manufacturer=" + manufacturer + ", productName="
        + productname + ", present=" + present + ", name=" + name
        + ", switchState=" + state + ", powerMeter=" + powermeter
        + ", temperature=" + temperature + "]";
  }

  
}
