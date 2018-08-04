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

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.bind.JAXB;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

/**
 * a FritzBox! session
 * 
 * @author wf
 *
 */
public class FritzBoxSessionImpl implements FritzBoxSession {

  public static boolean debug = false;
  // prepare a LOGGER
  protected static Logger LOGGER = Logger.getLogger("com.bitplan.fritzbox");

  String LOGIN_URL = "/login_sid.lua";

  private static final String DEFAULT_SESSION_ID = "0000000000000000";
  private Fritzbox fritzbox;
  Charset UTF_16LE = Charset.forName("utf-16le");
  private SessionInfo sessionInfo;
  // set to true to create new mockito statements
  public boolean domockito=false;

  /**
   * disable SSL
   */
  private void disableSslVerification() {
    try {
      // Create a trust manager that does not validate certificate chains
      TrustManager[] trustAllCerts = new TrustManager[] {
          new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
              return null;
            }

            public void checkClientTrusted(X509Certificate[] certs,
                String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs,
                String authType) {
            }
          } };

      // Install the all-trusting trust manager
      SSLContext sc = SSLContext.getInstance("SSL");
      sc.init(null, trustAllCerts, new java.security.SecureRandom());
      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

      // Create all-trusting host name verifier
      HostnameVerifier allHostsValid = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
          return true;
        }
      };

      // Install the all-trusting host verifier
      HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (KeyManagementException e) {
      e.printStackTrace();
    }
  }

  /**
   * create a FritzBox Session
   */
  public FritzBoxSessionImpl(Fritzbox pFritzbox) {
    this.fritzbox = pFritzbox;
    // https://stackoverflow.com/questions/19540289/how-to-fix-the-java-security-cert-certificateexception-no-subject-alternative
    this.disableSslVerification();
  }

  @Override
  public FritzBoxSession login() {
    try {
      sessionInfo = this.getSessionInfo("");
      String challengeResponse = sessionInfo.Challenge + "-"
          + this.getMd5(sessionInfo.Challenge + "-" + fritzbox.getPassword());
      String params = String.format("?username=%s&response=%s",
          fritzbox.getUsername(), challengeResponse);
      sessionInfo = this.getSessionInfo(params);

    } catch (Throwable th) {
      String msg = th.getMessage();
      LOGGER.log(Level.SEVERE, msg, th);
    }
    return this;
  }

  /**
   * get the session info
   * 
   * @param params
   * @return - the SessionInfo
   * @throws Exception
   * @throws IOException
   */
  protected SessionInfo getSessionInfo(String params)
      throws Exception, IOException {
    SessionInfo sessionInfo = this.getTypedResponse(LOGIN_URL, params,
        SessionInfo.class);
    return sessionInfo;
  }

  /**
   * get the XML Result
   * 
   * @param relativeUrl
   * @param params
   * @param clazz
   * @return the extracted JaxB Object
   * @throws Exception
   */
  @SuppressWarnings("rawtypes")
  public <T> T getTypedResponse(String relativeUrl, String params, Class clazz)
      throws Exception {
    String xml = this.getResponse(relativeUrl, params);
    @SuppressWarnings("unchecked")
    T result = (T) JAXB.unmarshal(new StringReader(xml), clazz);
    return result;
  }

  /**
   * get the response for the given relative Url
   * 
   * @param relativeUrl
   * @param params
   * @return the response
   * @throws Exception
   */
  public String getResponse(String relativeUrl, String params)
      throws Exception {
    // call the potentially mocked Response handler
    String response = this.doGetResponse(relativeUrl, params);
    if (domockito) {
      // Mockito helper
      String mockito = String.format(
          "doReturn(\"%s\").when(session).doGetResponse(\"%s\",\"%s\");",
          response.replaceAll("\"", Matcher.quoteReplacement("\\\"")),
          relativeUrl, params
          );
      LOGGER.log(Level.INFO, mockito);
    }
    return response;
  }

  /**
   * response handler (to be mocked in testcases)
   * @param relativeUrl
   * @param params
   * @return the response
   * @throws Exception
   */
  protected String doGetResponse(String relativeUrl, String params)
      throws Exception {
    String response;
    // if logged in we need to add the session id security parameter
    String security = "";
    if (this.sessionInfo != null
        && !DEFAULT_SESSION_ID.equals(this.sessionInfo.SID)) {
      security = "&sid=" + this.sessionInfo.SID;
    }
    response = readUrl(fritzbox.getUrl() + relativeUrl + params + security);
    if (debug) {
      LOGGER.log(Level.INFO, params);
      LOGGER.log(Level.INFO, response);
    }
    return response;
  }

  /**
   * read from the given url
   * 
   * @param url
   * @return the string read
   * @throws MalformedURLException
   * @throws IOException
   */
  public static String readUrl(String url)
      throws MalformedURLException, IOException {
    InputStream urlStream = new URL(url).openStream();
    String response = readResponse(urlStream);
    return response;
  }

  /**
   * read a response string from the given input stream
   * 
   * @param stream
   * @return - the response string
   * @throws IOException
   */
  public static String readResponse(InputStream stream) throws IOException {
    StringWriter responseWriter = new StringWriter();
    IOUtils.copy(stream, responseWriter, "UTF-8");
    String response = responseWriter.toString();
    stream.close();
    if (response != null && response.endsWith("\n"))
      response = response.substring(0, response.length() - 1);
    return response;
  }

  @Override
  public void logout() {
    this.sessionInfo = null;
  }

  @Override
  public String getMd5(String input) {
    String md5 = DigestUtils.md5Hex(input.getBytes(UTF_16LE));
    return md5;
  }

}
