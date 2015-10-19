# cne-abno-base
Reference Implementation of ABNO

## Things to consider
- Own ABNOController implementation needs a PCE Session, if there isn't an PCE Session, the implementation will shutdown.
- To start ABNOController is required a .xml file with some ABNO parameters, a example of .xml file you could show in the end of this readme.


## To start ABNOController (base) run next command:
```
java -cp ABNO.jar es.tid.abno.modules.ABNOController ABNOConfiguration.xml -Dname=ABNOController
```

## Create an LSP throw ABNO Controller:
```
curl 'localhost:4445?Operation_Type=L0ProvisioningWF&Source_Node=192.168.1.1&Destination_Node=192.168.1.3&Operation=add&ID_Operation=1234&Bandwidth=30000'
```

## ABNOConfiguration.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<config>
	<TMAddress>localhost</TMAddress>
	<TMPort>1239</TMPort>
	<ABNOPort>4445</ABNOPort>
	<PCEPPortPM>4444</PCEPPortPM>
	<PMAddress>localhost</PMAddress>
	<ABNOMode>4</ABNOMode>
	<VNTMPort>4190</VNTMPort>
	<VNTMParameters>localhost</VNTMParameters>
	<VNTMAddress>localhost</VNTMAddress>
	<PCEParameterslist>
		<PCEParameters>
			<PCEMode>2</PCEMode>
			<PCEPort>4189</PCEPort>
			<PCEAddress>192.168.1.200</PCEAddress>
			<Policy>
				<WFName>L0ProvisioningWF</WFName>
				<MediaChannel>	
					<OFCode>60000</OFCode>
				</MediaChannel>
				<L0PCECapabilities>
					<Instantiation>true</Instantiation>
				</L0PCECapabilities>
			</Policy>
		</PCEParameters>
	</PCEParameterslist>
</config>
```
