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

/**
 * FritzBox session interface
 * 
 * @author wf
 *
 */
public interface FritzBoxSession {
  /**
   * login if needed
   * @return the session
   */
  public FritzBoxSession connect();
  
  /**
   * logout
   */
  public void close();
  
  /**
   * get the md5 for the given input
   * @param input
   * @return the md5 digest
   */
  public String getMd5(String input);
  
  /**
   * get an XML Result for the given params
   * @param relativeUrl
   * @param params
   * @param clazz
   * @return the type
   * @throws Exception 
   */
  @SuppressWarnings("rawtypes")
  public <T> T getTypedResponse(String relativeUrl,String params,Class clazz) throws Exception;

  /**
   * get a String response for the given params
   * @param baseUrl
   * @param params
   * @return the response
   * @throws Exception 
   */
  public String getResponse(String baseUrl, String params) throws Exception;
}
