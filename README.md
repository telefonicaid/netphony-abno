# cne-abno-base
Reference Implementation of ABNO

## Things to consider
- Own ABNOController implementation needs a PCE Session, if there isn't a PCE Session, the implementation will shutdown.
- To start ABNOController is required a .xml file with some ABNO parameters, a example of .xml file you could show in [ABNOConfiguration.md](ABNOConfiguration.md).


## To start ABNOController (base) run next command:
```
java -cp ABNO.jar es.tid.abno.modules.ABNOController ABNOConfiguration.xml -Dname=ABNOController
```

## Create an LSP throw ABNO Controller:
```
curl 'localhost:4445?Operation_Type=L0ProvisioningWF&Source_Node=192.168.1.1&Destination_Node=192.168.1.3&Operation=add&ID_Operation=1234&Bandwidth=30000'
```
