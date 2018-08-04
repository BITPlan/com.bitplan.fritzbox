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
package de.ingo.fritzbox.utils;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import de.ingo.fritzbox.data.Call;


/**
 * A CsvParser to parse csv files provided by the FritzBox.<br>
 * Currently supported csv files: call list
 *
 * @author Ingo Schwarz
 */
public class CsvParser {

  /**
   * Parses a call list CSV file.
   *
   * @param csvString
   *            The CSV file as string.
   * @return The parsed calls as list of Call objects.
   */
  public static List<Call> parseCallList(final String csvString) {
    final List<Call> callerList = new ArrayList<>();
    String csv = csvString.substring(csvString.indexOf("\n") + 1);
    csv = csv.substring(csv.indexOf("\n") + 1);
    csv = new String(csv.getBytes(), Charset.forName("UTF-8"));

    String line;
    while (csv.indexOf("\n") > 0 && (line = csv.substring(0, csv.indexOf("\n"))).length() > 6) {
      csv = csv.substring(csv.indexOf("\n") + 1);
      callerList.add(parseCallListEntry(line));
    }

    return callerList;
  }

  /**
   * Parses a certain line of the call list CSV file.
   *
   * @param csvLine
   *            The line of the CSV call list file.
   * @return The parsed call.
   */
  private static Call parseCallListEntry(final String csvLine) {

    final String[] lineData = csvLine.split(";");
    final Call.Builder builder = new Call.Builder().type(lineData[0]).callerName(lineData[2])
        .callerNumber(lineData[3]).substationName(lineData[4]).substationNumber(lineData[5]);

    try {
      builder.date(lineData[1]);
    } catch (final ParseException e) {
      // TODO log exception
    }
    try {
      builder.duration(lineData[6]);
    } catch (final ParseException e) {
      // TODO log exception
      e.printStackTrace();
    }

    return builder.build();
  }

}
