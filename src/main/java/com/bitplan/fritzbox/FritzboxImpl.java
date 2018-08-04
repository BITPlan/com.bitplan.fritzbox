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

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * Fritzbox information
 * @author wf
 *
 */
public class FritzboxImpl implements Fritzbox {
  private String url;
  private String username;
  private String password;
  
  public String getUrl() {
    return url;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  /**
   * get the property file
   * @return the property file
   */
  public static File getPropertyFile() {
    final String home = System.getProperty("user.home");
    final File propFile = new File(home + "/.fritzbox/application.properties");
    return propFile;
  }
  
  /**
   * read me from application.properties file
   * @return the fritzbox configuration
   * @throws Exception 
   */
  public static FritzboxImpl readFromProperties() throws Exception {
    FritzboxImpl fritzbox=null;
    File propFile=getPropertyFile();
    if (propFile.exists()) {
      final Properties props= new Properties();
      props.load(new FileInputStream(propFile));
      fritzbox=new FritzboxImpl();
      fritzbox.url=props.getProperty("fritzbox.url");
      fritzbox.username=props.getProperty("fritzbox.username");
      fritzbox.password=props.getProperty("fritzbox.password");
    }
    return fritzbox;
  }

  @Override
  public FritzBoxSession login() {
    FritzBoxSessionImpl session = new FritzBoxSessionImpl(this);
    session.login();
    return session;
  }
}
