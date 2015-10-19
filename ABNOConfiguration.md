# ABNOConfiguration.xml

## Example of .xml code
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
