package es.tid.provisioningManager.modules.dispatcher;


import java.util.Hashtable;
import java.util.logging.Logger;

import es.tid.pce.pcep.objects.ExplicitRouteObject;

//import org.eclipse.jetty.util.log.Log;

import es.tid.pce.pcep.objects.GeneralizedEndPoints;
import es.tid.rsvp.objects.subobjects.UnnumberedDataPathIDEROSubobject;
import es.tid.tedb.elements.Node;
import es.tid.rsvp.objects.subobjects.UnnumberIfIDEROSubobject;
import es.tid.rsvp.objects.subobjects.IPv4prefixEROSubobject;
import es.tid.rsvp.objects.subobjects.DataPathIDEROSubobject;

import es.tid.abno.modules.Path_Computation;

public class InfoDispatcher {
	
	private Logger log=Logger.getLogger("infoDispatcher");
	private String controllerIP = null;
	private String controllerPort = null;
	private String configurationMode = null;
	private String routerType = null;
	
	private ExplicitRouteObject ero = new ExplicitRouteObject();
	private GeneralizedEndPoints endpoints = new GeneralizedEndPoints();
	
	private String sourceNode = null;
	private int sourceIntf = -1;
	private String destNode = null;
	private int destIntf = -1;
	
	private Hashtable<String, Node> hashEroNode = new Hashtable<String, Node>();
	
	
	
	
	public String getControllerIP() {
		return controllerIP;
	}
	public void setControllerIP(String controllerIP) {
		this.controllerIP = controllerIP;
	}
	public String getControllerPort() {
		return controllerPort;
	}
	public void setControllerPort(String controllerPort) {
		this.controllerPort = controllerPort;
	}
	public ExplicitRouteObject getEro() {
		return ero;
	}
	public void setEro(ExplicitRouteObject ero) {
		this.ero = ero;
	}
	public String getConfigurationMode() {
		return configurationMode;
	}
	public void setConfigurationMode(String configurationMode) {
		this.configurationMode = configurationMode;
	}
	public String getRouterType() {
		return routerType;
	}
	public void setRouterType(String routerType) {
		this.routerType = routerType;
	}
	
	public GeneralizedEndPoints getEndpoints() {
		return endpoints;
	}
	public void setEndpoints(GeneralizedEndPoints endpoints) {
		this.endpoints = endpoints;
	}
	
	public void createEndPoints(){
		
		
		//Source Endpoints
		if (this.getEro().getEROSubobjectList().getFirst() instanceof IPv4prefixEROSubobject){
			
			this.sourceNode = ((IPv4prefixEROSubobject) this.getEro().getEROSubobjectList().getFirst()).getIpv4address().getHostAddress();
			//this.sourceIntf = ; IPv4prefixEROSubobject doesn't have Interface ID
			
		}else if((this.getEro().getEROSubobjectList().getFirst() instanceof UnnumberIfIDEROSubobject)){
			
			this.sourceNode = ((UnnumberIfIDEROSubobject)this.getEro().getEROSubobjectList().getFirst()).getRouterID().getHostAddress();
			this.sourceIntf = (int) ((UnnumberIfIDEROSubobject) this.getEro().getEROSubobjectList().getFirst()).getInterfaceID();
		
		}else if((this.getEro().getEROSubobjectList().getFirst() instanceof DataPathIDEROSubobject)){
			
			this.sourceNode = ((DataPathIDEROSubobject)this.getEro().getEROSubobjectList().getFirst()).getDataPath().getDataPathID();
			//this.sourceIntf = ; DataPathIDEROSubobject doesn't have Interface ID
			
		}else if((this.getEro().getEROSubobjectList().getFirst() instanceof UnnumberedDataPathIDEROSubobject)){
			
			this.sourceNode = ((UnnumberedDataPathIDEROSubobject)this.getEro().getEROSubobjectList().getFirst()).getDataPath().getDataPathID();
			this.sourceIntf = (int)((UnnumberedDataPathIDEROSubobject)this.getEro().getEROSubobjectList().getFirst()).getInterfaceID();
			
		}else log.info("Source EndPoint not implemented in infoDispatcher");
		
		
		
		
		//Destination Endpoints
		if (this.getEro().getEROSubobjectList().getLast() instanceof IPv4prefixEROSubobject){
			
			this.destNode = ((IPv4prefixEROSubobject)this.getEro().getEROSubobjectList().getLast()).getIpv4address().getHostAddress();
			//this.destIntf = ; IPv4prefixEROSubobject doesn't have Interface ID			 
			
		}else if((this.getEro().getEROSubobjectList().getFirst() instanceof UnnumberIfIDEROSubobject)){
			
			this.destNode = ((UnnumberIfIDEROSubobject)this.getEro().getEROSubobjectList().getLast()).getRouterID().getHostAddress();
			this.destIntf = (int) ((UnnumberIfIDEROSubobject) this.getEro().getEROSubobjectList().getLast()).getInterfaceID();
		
		}else if((this.getEro().getEROSubobjectList().getFirst() instanceof DataPathIDEROSubobject)){
			
			this.destNode = ((DataPathIDEROSubobject)this.getEro().getEROSubobjectList().getLast()).getDataPath().getDataPathID();
			//this.destIntf = ; DataPathIDEROSubobject doesn't have Interface ID		
		}else if((this.getEro().getEROSubobjectList().getFirst() instanceof UnnumberedDataPathIDEROSubobject)){
			
			this.destNode = ((UnnumberedDataPathIDEROSubobject)this.getEro().getEROSubobjectList().getLast()).getDataPath().getDataPathID();
			this.destIntf = (int)((UnnumberedDataPathIDEROSubobject)this.getEro().getEROSubobjectList().getLast()).getInterfaceID();
			
		}else log.info("Destination EndPoint not implemented in infoDispatcher");
		
		
		//Set endpoints
		this.setEndpoints(Path_Computation.createGeneralizedEndpoints(this.sourceNode, this.sourceIntf, this.destNode, this.destIntf ));
		//log.info("EndPoints saved in infoDispatcher: "+this.getEndpoints().toString());
	}
	public String getSourceNode() {
		return sourceNode;
	}
	public void setSourceNode(String sourceNode) {
		this.sourceNode = sourceNode;
	}
	public int getSourceIntf() {
		return sourceIntf;
	}
	public void setSourceIntf(int sourceIntf) {
		this.sourceIntf = sourceIntf;
	}
	public String getDestNode() {
		return destNode;
	}
	public void setDestNode(String destNode) {
		this.destNode = destNode;
	}
	public int getDestIntf() {
		return destIntf;
	}
	public void setDestIntf(int destIntf) {
		this.destIntf = destIntf;
	}
	public Hashtable<String, Node> getHashEroNode() {
		return hashEroNode;
	}
	public void setHashEroNode(Hashtable<String, Node> hashEroNode) {
		this.hashEroNode = hashEroNode;
	}

}




