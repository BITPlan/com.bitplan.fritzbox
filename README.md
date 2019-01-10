### com.bitplan.fritzbox
[Java API for AVM FritzBox Homeautomation](http://wiki.bitplan.com/index.php/Fritzbox-java-api)

[![Travis (.org)](https://img.shields.io/travis/BITPlan/com.bitplan.fritzbox.svg)](https://travis-ci.org/BITPlan/com.bitplan.fritzbox)
[![Maven Central](https://img.shields.io/maven-central/v/com.bitplan/com.bitplan.fritzbox.svg)](https://search.maven.org/artifact/com.bitplan/com.bitplan.fritzbox/0.0.5/jar)
[![GitHub issues](https://img.shields.io/github/issues/BITPlan/com.bitplan.fritzbox.svg)](https://github.com/BITPlan/com.bitplan.fritzbox/issues)
[![GitHub issues](https://img.shields.io/github/issues-closed/BITPlan/com.bitplan.fritzbox.svg)](https://github.com/BITPlan/com.bitplan.fritzbox/issues/?q=is%3Aissue+is%3Aclosed)
[![GitHub](https://img.shields.io/github/license/BITPlan/com.bitplan.fritzbox.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![BITPlan](http://wiki.bitplan.com/images/wiki/thumb/3/38/BITPlanLogoFontLessTransparent.png/198px-BITPlanLogoFontLessTransparent.png)](http://www.bitplan.com)

### Documentation
* [Wiki](http://wiki.bitplan.com/index.php/Fritzbox-java-api)
* [com.bitplan.fritzbox Project pages](https://BITPlan.github.io/com.bitplan.fritzbox)
* [Javadoc](https://BITPlan.github.io/com.bitplan.fritzbox/apidocs/index.html)
* [Test-Report](https://BITPlan.github.io/com.bitplan.fritzbox/surefire-report.html)
### Maven dependency

Maven dependency
```xml
<!-- Java API for AVM FritzBox Homeautomation http://wiki.bitplan.com/index.php/Fritzbox-java-api -->
<dependency>
  <groupId>com.bitplan</groupId>
  <artifactId>com.bitplan.fritzbox</artifactId>
  <version>0.0.5</version>
</dependency>
```

[Current release at repo1.maven.org](http://repo1.maven.org/maven2/com/bitplan/com.bitplan.fritzbox/0.0.5/)

### How to build
```
git clone https://github.com/BITPlan/com.bitplan.fritzbox
cd com.bitplan.fritzbox
mvn install
```
## Version history
* 0.0.1: 2018-08-04 First release via GitHub / Maven central
* 0.0.2: 2018-08-07 adds call list option
* 0.0.3: 2018-08-07 adds FritzBoxSessionBuilder and FritzBoxImpl.getInstance()
* 0.0.4: 2018-08-10 adds ainForName lookup
* 0.0.5: 2018-08-24 connect/close for session handling to avoid timeouts - close session for every command
