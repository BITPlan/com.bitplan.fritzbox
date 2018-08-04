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
package de.ingo.fritzbox.data;

import java.io.Serializable;


/**
 * This enumeration contains all known call types. Every call type contains the
 * value with which they are identified at the FritzBox.
 * https://raw.githubusercontent.com/ISchwarz23/FritzBox-API/master/src/main/java/de/ingo/fritzbox/data/CallType.java
 *
 * @author Ingo Schwarz
 */
public enum CallType implements Serializable {

  INCOMING_CALL(1), MISSED_CALL(2), UNKNOWN(3), OUTGOING_CALL(4);


  private final int value;

  CallType(final int value) {
    this.value = value;
  }

  /**
   * Returns the value of the CallType the FritzBox is working with.
   *
   * @return The call type value as integer.
   */
  public int getValue() {
    return this.value;
  }

  /**
   * Creates a call type object from the given value.
   *
   * @param x
   *            The value to create a CallType for.
   * @return The created CallType.
   */
  public static CallType fromInteger(final int x) {

    switch (x) {
    case 1:
      return INCOMING_CALL;
    case 2:
      return MISSED_CALL;
    case 4:
      return OUTGOING_CALL;
    default:
      return UNKNOWN;
    }
  }

}

