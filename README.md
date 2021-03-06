Netphony ABNO v1.3.3
===================

Repository branch build status:

| **Master**  | **Develop**   |
|:---:|:---:|
| [![Build Status](https://travis-ci.org/telefonicaid/netphony-abno.svg?branch=master)](https://travis-ci.org/telefonicaid/netphony-abno) | [![Build Status](https://travis-ci.org/telefonicaid/netphony-abno.svg?branch=develop)](https://travis-ci.org/telefonicaid/netphony-abno) |

Latest Maven Central Release: 

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/es.tid.netphony/abno/badge.svg?style=flat-square)](https://maven-badges.herokuapp.com/maven-central/es.tid.netphony/netphony-abno)

Reference Implementation of ABNO
# Introduction
Application-Based Network Operations (ABNO).
ABNO architecture is proposed in IETF as a framework which enables network
automation and programmability thanks to the utilization of standard protocols and components.
[RFC-7491](https://tools.ietf.org/html/rfc7491)

More documentation in the [ABNO Wiki](https://github.com/telefonicaid/netphony-abno/wiki). 

# **Latest news**
- License is now Apache 2.0 
- Version available in maven central
- Tested with Netphony Transport Node Emulator and PCE v1.3.3 
- COP API available

# Work in progress
- TAPI 1.0 API in progress

# Known issues
- Own ABNOController implementation needs a PCE Session, if there isn't a PCE Session, the implementation will shutdown.
- To start ABNOController is required a .xml file with some ABNO parameters, a example of .xml file you could show in [ABNOConfiguration.md](ABNOConfiguration.md).
- ABNO include COP services with a server autogenerated, the path of the server could be modified in the file [ConfigApi.java](https://pdihub.hi.inet/cne/cne-abno-base/blob/feature/cleanTM/src/main/java/es/tid/swagger/api/ConfigApi.java) line 22.

# Compilation and use

The library can be built using the maven tool. 
To build the .jar file and run the tests, clone the repository, go to the main directory and run:
 ```bash
    cd netphony-abno
    mvn install
 ```
 
 To use the library in your application, add the dependency in your pom.xml file:
  ```xml
    <dependency>
      <groupId>es.tid.netphony</groupId>
      <artifactId>abno</artifactId>
      <version>1.3.3</version>
    </dependency>
 ```
 Authors keep also a copy of the artifact in maven central to facilitate the deployment. (*) In process

## Generation of ABNO Controller auto-executuable jar with COP as RESTCONF API
 ```bash
    cd netphony-abno
    mvn package -P generate-full-jar-ABNO-COP
 ```
## To start ABNOController (base) run next command:
We have to keep in mind that PCE Session it's required.
[PCE github](urlpcegithub).
```
java -Dlog4j.configurationFile=path_tp_log4j2.xml -Dname=ABNOController -jar target/abno-1.3.3-shaded.jar ABNOConfiguration.xml 
```

## ABNOConfiguration.xml
In the same path as a ABNO.jar, created in the previous point, we need this xml configuration file.
You can consult about this configuration file at this link: [ABNOConfiguration.md](ABNOConfiguration.md).




