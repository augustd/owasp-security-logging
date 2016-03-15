[![Build Status](https://travis-ci.org/javabeanz/owasp-security-logging.svg?branch=master)](https://travis-ci.org/javabeanz/owasp-security-logging)

[![Coverity Scan Status](https://scan.coverity.com/projects/3657/badge.svg)](https://scan.coverity.com/projects/3657)
[![codecov.io](https://codecov.io/github/javabeanz/owasp-security-logging/coverage.svg?branch=master)](https://codecov.io/github/javabeanz/owasp-security-logging?branch=master)
[![Dependency Status](https://www.versioneye.com/user/projects/56b7177df6e506003159ab58/badge.svg?style=flat)](https://www.versioneye.com/user/projects/56b7177df6e506003159ab58)
[![Codacy Badge](https://api.codacy.com/project/badge/grade/4a192a53a1ed4dbd9ec6f029cba60af1)](https://www.codacy.com/app/java-development/owasp-security-logging)

[![License][license-image]][license-url]
[![Version][maven-version]][maven-url]

owasp-security-logging
======================

[OWASP Security Logging Project](https://www.owasp.org/index.php/OWASP_Security_Logging_Project) - Standard Java API to log security related events.  [Documentation](https://github.com/javabeanz/owasp-security-logging/wiki)

As of version 1.1.0 Logback and Log4j support are in separate projects. To use OWASP Security Logging with Logback, add the following Maven dependency to pom.xml:

```xml
<dependency>
  <groupId>org.owasp</groupId>
  <artifactId>security-logging-logback</artifactId>
  <version>1.1.0</version>
</dependency>
```

To use it with Log4j, add: 

```xml
<dependency>
  <groupId>org.owasp</groupId>
  <artifactId>security-logging-log4j</artifactId>
  <version>1.1.0</version>
</dependency>
```
(Log4j users please see [Usage with Log4j](https://github.com/javabeanz/owasp-security-logging/wiki/Usage-with-Log4j))

Maven imports will automatically include the common classes. If using OWASP Security Logging on your classpath, you need to include security-logging-common-VERSION.jar in addition to the correct jar for either Logback or Log4j. 

----

[license-url]: https://github.com/javabeanz/owasp-security-logging/blob/master/LICENSE
[license-image]: https://img.shields.io/badge/license-apache%20v2-brightgreen.svg

[maven-url]: http://search.maven.org/#browse|-1016717255
[maven-version]: https://img.shields.io/maven-central/v/org.owasp/security-logging.svg?style=flat

