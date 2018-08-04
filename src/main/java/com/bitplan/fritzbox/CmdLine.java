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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.StringArrayOptionHandler;

import de.ingo.fritzbox.data.Call;

/**
 * command line interface for home automation
 * 
 * @author wf
 *
 */
public class CmdLine {
  // prepare a LOGGER
  protected static Logger LOGGER = Logger.getLogger("com.bitplan.fritzbox");

  public static final String VERSION = "0.0.1";

  @Option(name = "-d", aliases = {
      "--debug" }, usage = "debug\ncreate additional debug output if this switch is used")
  boolean debug = false;

  @Option(name = "-h", aliases = { "--help" }, usage = "help\nshow this usage")
  boolean showHelp = false;

  @Option(name = "-c", aliases = {
      "--calllist" }, usage = "call list\nget the call list")
  boolean doGetCallList = false;

  @Option(name = "-l", aliases = { "--list" }, usage = "list\nlist devices")
  boolean listDevices = false;

  @Option(name = "-r", handler = StringArrayOptionHandler.class, aliases = {
      "--read" }, usage = "read\nread the given devices")
  List<String> readDevices = new ArrayList<>();

  @Option(name = "-s", handler = StringArrayOptionHandler.class, aliases = {
      "--set" }, usage = "set\nset the given devices to the givnen states e.g. main off")
  List<String> setDevices = new ArrayList<>();

  @Option(name = "-v", aliases = {
      "--version" }, usage = "showVersion\nshow current version if this switch is used")
  boolean showVersion = false;

  private CmdLineParser parser;

  Fritzbox fritzbox;
  static int exitCode;
  public static boolean testMode = false;

  /**
   * handle the given Throwable
   * 
   * @param t
   *          the Throwable to handle
   */
  public void handle(Throwable t) {
    System.out.flush();
    t.printStackTrace();
    usage(t.getMessage());
  }

  /**
   * show the Version
   */
  public void showVersion() {
    System.err.println("FritzBox Java API Command Line Version: " + VERSION);
    System.err.println();
    System.err
        .println("github: https://github.com/BITPlan/com.bitplan.fritzbox");
    System.err.println("");
  }

  /**
   * display usage
   * 
   * @param msg
   *          - a message to be displayed (if any)
   */
  public void usage(String msg) {
    System.err.println(msg);
    showVersion();
    System.err.println("  usage: java -jar fritzbox.jar");
    parser.printUsage(System.err);
    exitCode = 1;
  }

  /**
   * print the given error and set the exit Code
   * 
   * @param msg
   */
  public void error(String msg) {
    System.err.println(msg);
    exitCode = 1;
  }

  /**
   * show Help
   */
  public void showHelp() {
    usage("Help");
  }

  /**
   * handle the command line command
   * @throws Throwable 
   */
  public void doCommand() throws Throwable {
    if (fritzbox == null)
      fritzbox = FritzboxImpl.readFromProperties();

    if (fritzbox == null) {
      String msg = String.format(
          "no %s found\nYou might want to create one see http://wiki.bitplan.com/index.php/Fritzbox-java-api#Configuration_File",
          FritzboxImpl.getPropertyFile().getPath());
      error(msg);
      return;
    }
    if (debug) {
      LOGGER.log(Level.INFO, String.format("Logging in to %s with username %s",
          fritzbox.getUrl(), fritzbox.getUsername()));
    }
    FritzBoxSession session = fritzbox.login();
    if (this.doGetCallList) {
      final CallList callList = new CallListImpl(session);
      List<Call> calls = callList.getCallList();
      for (Call call:calls) {
        System.out.println(call);
      }
    } else {
      final HomeAutomation homeAutomation = new HomeAutomationImpl(session);

      final DeviceList deviceList = homeAutomation.getDeviceListInfos();
      if (debug) {
        LOGGER.log(Level.INFO,
            String.format("Found %s devices", deviceList.devices.size()));
        for (final Device device : deviceList.devices) {
          LOGGER.log(Level.INFO, String.format("\t%s", device));
        }
      }

      final List<String> ids = homeAutomation.getSwitchList();
      if (debug) {
        LOGGER.log(Level.INFO,
            String.format("Found %3d device ids: %s", ids.size(), ids));
      }

      if (deviceList.devices.isEmpty()) {
        session.logout();
        return;
      }

      if (listDevices) {
        show("%20s | %10s | %25s | %s", "Name", "By", "Product", "Identifier");
        show("%21s+%12s+%27s+%s", dash(21), dash(12), dash(27), dash(25));
        for (final Device device : deviceList.devices) {
          show("%20s | %10s | %25s | %s", device.name, device.manufacturer,
              device.productname, device.identifier);
        }
      }
      final Map<String, String> ainByName = new HashMap<>();
      for (final Device device : deviceList.devices) {
        ainByName.put(device.name, device.getAin());
      }
      /**
       * loop over all devices to be read
       */
      for (final String readDevice : readDevices) {
        // devices can be specified by name or id
        String ain = readDevice;
        // check if a name was given
        if (ainByName.containsKey(ain)) {
          // get the ain for the name
          ain = ainByName.get(ain);
        }
        readSwitch(homeAutomation, ain);
      }

      /**
       * loop over all devices to be set
       */
      if (setDevices.size() % 2 != 0) {
        usage("set needs pairs of name=on/off");
      }
      for (int i = 0; i < setDevices.size(); i += 2) {
        final String name = setDevices.get(i);
        final String powerState = setDevices.get(i + 1);
        // check if a name was given
        String ain = name;
        if (ainByName.containsKey(ain)) {
          // get the ain for the name
          ain = ainByName.get(ain);
        }
        boolean newState = false;
        switch (powerState) {
        case "on":
          newState = true;
          break;
        case "off":
          newState = false;
          break;
        default:
          usage(String.format(
              "%s is not a valid powerState it needs to be on or off",
              powerState));
        }
        show("switching %s %s", name, powerState);
        homeAutomation.setSwitchOnOff(ain, newState);
      }
    }
  }

  /**
   * read the given switch
   * 
   * @param homeAutomation
   * @param ain
   *          - the identification of the switch
   * @throws Exception
   */
  private void readSwitch(HomeAutomation homeAutomation, String ain)
      throws Exception {
    show(" name: %s", homeAutomation.getSwitchName(ain));
    show("   id: %s ", ain);
    show("alive: %s", homeAutomation.getSwitchPresent(ain));
    show("   on: %s", homeAutomation.getSwitchState(ain));
    show(" uses: %5.0f     W", homeAutomation.getSwitchPowerWatt(ain));
    show(" used: %9.3f kWh", homeAutomation.getSwitchEnergy(ain) / 1000.0);
    show(" temp: %7.1f   °C", homeAutomation.getTemperature(ain));
    show("");
  }

  /**
   * show the given values in the given format
   * 
   * @param format
   * @param values
   */
  public void show(String format, Object... values) {
    System.out.println(String.format(format, values));
  }

  /**
   * get the given number of dashes
   * 
   * @param number
   * @return the number of dashes
   */
  public String dash(int number) {
    return s('-', number);
  }

  /**
   * get a string with number repetitions of the given char
   * 
   * @param c
   * @param number
   * @return - the repeated char
   */
  public String s(char c, int number) {
    final StringBuffer sb = new StringBuffer();
    for (int i = 0; i < number; i++) {
      sb.append(c);
    }
    return sb.toString();
  }

  /**
   * work on the given commands
   * 
   * @throws Exception
   *           if a problem occurs
   */
  protected void work() throws Throwable {
    if (this.showVersion || this.debug) {
      showVersion();
    }
    if (this.showHelp) {
      showHelp();
    } else {
      doCommand();
    }
  }

  /**
   * main routine
   * 
   * @param args
   *          - the command line arguments
   * @return - the exit statusCode
   * 
   */
  public int maininstance(String[] args) {
    parser = new CmdLineParser(this);
    try {
      parser.parseArgument(args);
      work();
      exitCode = 0;
    } catch (final CmdLineException e) {
      // handling of wrong arguments
      usage(e.getMessage());
    } catch (final Throwable th) {
      handle(th);
      exitCode = 1;
    }
    return exitCode;
  }

  /**
   * main routine
   * 
   * @param args
   *          - command line arguments
   */
  public static void main(String[] args) {
    final CmdLine cmdLine = new CmdLine();
    final int result = cmdLine.maininstance(args);
    if (!testMode) {
      System.exit(result);
    }
  }
}
