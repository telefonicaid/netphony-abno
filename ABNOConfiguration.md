# ABNOConfiguration.md

## Example of ABNOConfiguration.xml code
```xml
<?xml version="1.0" encoding="UTF-8"?>
<config>
	<ABNOPort>4445</ABNOPort>
	<PCEPPortPM>4444</PCEPPortPM>
	<PMAddress>localhost</PMAddress>
	<ABNOMode>4</ABNOMode>
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

## Parameters
- <PCEParameters>: Some PCE parameters
- <PCEMode>:
- <Policy>: Specify some parameters in a workflow
- <WFName>: Workflow name
- <PCEAddress>: The PCE IP Address
- <MediaChannel>
- <OFCode>
- <L0PCECapabilities>
- <Instantiation>: true (Send Initiate to PCE), false (Send Initiate to Provisioning Manager).
