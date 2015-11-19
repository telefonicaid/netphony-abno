# cne-abno-base
Reference Implementation of ABNO
## Introduction
Application-Based Network Operations (ABNO).
ABNO architecture is proposed in IETF as a framework which enables network
automation and programmability thanks to the utilization of standard protocols and components.
[RFC-7491](https://tools.ietf.org/html/rfc7491)
## Things to consider
- Own ABNOController implementation needs a PCE Session, if there isn't a PCE Session, the implementation will shutdown.
- To start ABNOController is required a .xml file with some ABNO parameters, a example of .xml file you could show in [ABNOConfiguration.md](ABNOConfiguration.md).

## Dependences
- [PCE]()
- [bgp4peer]()
- [network-protocols]()
- Some Libraries

## Create ABNO project
In this point we explain to create a JAVA project in eclipse IDE.

### Create a New JAVA Project
File -> New -> Java Project.
It is required select in "Select an execution enviroment JRE" "JavaSE-1.6" apart from specify Project name.
![captura1](https://octodcom/images/yaktocat.png)
We continue to import the sources of ABNO and its dependences.
Inside project folder we are going to download the resources.
```
cd workspace/AbnoProject
git clone abno
git clone pce
git clone bgp4peer
git clone network-protocols
```
And import the resources such
File -> Properties -> Java Build Path -> Source -> Add Folder...
abno/src
pce/src
bgp4peer/src
network-protocols/src
![capturax]

We need import some libraries 
File -> Properties -> Java Build Path -> Libraries -> Add External JARs...

![capturax]

Right now we can verify that we have not errors in project.

### Create an external JAR file
In export project we select an export type destination, in this case we select Runnable JAR file.
![capturaJAR1]()

The second step is to define the main launch configuration, and library handling.
At the bottom of next picture we can select if we want save this configurations to create a runnable jar such as ANT script.
![capturaJAR2]()

For the following times we can run the ANT script instead of the previous steps to generate de JAR file.
![capturaJAR2]()

If we want change any configuration to generate de JAR, we need modify the ANT script or repeat the previous steps.


## ABNOConfiguration.xml
In the same path as a ABNO.jar, created in the previous point, we need this xml configuration file.
You can consult about this configuration file at this link: [ABNOConfiguration.md](ABNOConfiguration.md).

## To start ABNOController (base) run next command:
We have to keep in mind that PCE Session it's required.
[PCE github](urlpcegithub).
```
java -cp ABNO.jar es.tid.abno.modules.ABNOController ABNOConfiguration.xml -Dname=ABNOController
```

## Create an LSP throw ABNO Controller:
```
curl 'localhost:4445?Operation_Type=L0ProvisioningWF&Source_Node=192.168.1.1&Destination_Node=192.168.1.3&Operation=add&ID_Operation=1234&Bandwidth=30000'
```
