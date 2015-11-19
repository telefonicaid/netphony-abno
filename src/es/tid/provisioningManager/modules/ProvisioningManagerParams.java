package es.tid.provisioningManager.modules;

import java.io.File;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.tid.util.UtilsFunctions;

public class ProvisioningManagerParams {
	/**
	 * PCEP Port
	 */
	private int pcepPort;
	/**
	 * Topology Module Port
	 */
	private int topologyModulePort;
	/**
	 * Topology Module Address
	 */
	private String topologyModuleAddress;	
	/**
	 * PCE Port
	 */
	private int pcePort;
	/**
	 * Name of the configuration file
	 */
	private String confFile;
	/**
	 * Topology
	 */
	private String topology;
	/**
	 * Default Constructor. The configuration file is PMConfiguration.xml.
	 */
	
	private String RedisIP = "photonics";
	
	private String controllerIP = "";
	
	private int controllerPort = 8080;
	
	/*Floodlight*/
	//private String controllerTopologyQuery = "/wm/core/controller/switches/json";
	
	/*Ryu*/
	private String controllerTopologyQuery = "/v1.0/topology/switches";
	
	private static Logger log=Logger.getLogger("PM Controller");

	private String Demo=null;

	/**
	 * Constructor with the name of the configuration file.
	 * @param confFile Name of the configuration file.
	 */
	public ProvisioningManagerParams(String confFile){
		if (confFile!=null){
			this.setConfFile(confFile);
		}else {
			confFile="PMConfiguration.xml";
		}
	}
	public ProvisioningManagerParams(){
		setConfFile("PMConfiguration.xml");
	}
	public int getPcePort() {
		return pcePort;
	}
	public void setPcePort(int pcePort) {
		this.pcePort = pcePort;
	}
	public void initialize()
	{
		
		DocumentBuilder builder;
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		
			File confFile = new File(this.confFile);
			Document doc;
			
			doc = builder.parse(confFile);
			
			NodeList nodes = doc.getElementsByTagName("TMAddress");
			this.topologyModuleAddress = getCharacterDataFromElement((Element) nodes.item(0));
			
			nodes = doc.getElementsByTagName("TMPort");
			this.topologyModulePort = Integer.parseInt(getCharacterDataFromElement((Element) nodes.item(0)));
			
			nodes = doc.getElementsByTagName("PCEPPort");
			this.pcepPort = Integer.parseInt(getCharacterDataFromElement((Element) nodes.item(0)));
			
			nodes = doc.getElementsByTagName("ControllerIP");
			this.controllerIP = getCharacterDataFromElement((Element)nodes.item(0));
			
			nodes = doc.getElementsByTagName("ControllerPort");
			this.controllerPort = Integer.parseInt(getCharacterDataFromElement((Element)nodes.item(0)));
			
			nodes = doc.getElementsByTagName("RedisIP");
			if (nodes!=null) this.RedisIP = getCharacterDataFromElement((Element)nodes.item(0));
						
			nodes = doc.getElementsByTagName("ControllerTopoQuery");
			this.controllerTopologyQuery = getCharacterDataFromElement((Element)nodes.item(0));
			
		    NodeList node_Demo = doc.getElementsByTagName("Demo");
		    if (node_Demo!= null ) {
			    Element DemoElement = (Element)node_Demo.item(0);
			    if (DemoElement!=null)
			    	Demo = getCharacterDataFromElement(DemoElement);
			}
		
		} 
		catch (Exception e) 
		{
			log.info(UtilsFunctions.exceptionToString(e));
		}
	}

	public String getTopology() {
		return topology;
	}

	public void setTopology(String topology) {
		this.topology = topology;
	}

	public String getTopologyModuleAddress() {
		return topologyModuleAddress;
	}

	public void setTopologyModuleAddress(String topologyModuleAddress) {
		this.topologyModuleAddress = topologyModuleAddress;
	}

	public int getTopologyModulePort() {
		return topologyModulePort;
	}

	public void setTopologyModulePort(int topologyModulePort) {
		this.topologyModulePort = topologyModulePort;
	}

	public String getConfFile() {
		return confFile;
	}

	public void setConfFile(String confFile) {
		this.confFile = confFile;
	}

	public int getPcepPort() {
		return pcepPort;
	}

	public void setPcepPort(int pcepPort) {
		this.pcepPort = pcepPort;
	}
	public String getControllerIP() {
		return controllerIP;
	}
	public void setControllerIP(String controllerIP) {
		this.controllerIP = controllerIP;
	}
	public int getControllerPort() {
		return controllerPort;
	}
	public void setControllerPort(int controllerPort) {
		this.controllerPort = controllerPort;
	}
	public String getControllerTopologyQuery() {
		return controllerTopologyQuery;
	}
	public void setControllerTopologyQuery(String controllerTopologyQuery) {
		this.controllerTopologyQuery = controllerTopologyQuery;
	}
	public String getDemo() {
		return Demo;
	}

	public void setDemo(String demo) {
		Demo = demo;
	}
	
	private String getCharacterDataFromElement(Element e) {
		Node child = e.getFirstChild();
		if (child instanceof CharacterData) {
			CharacterData cd = (CharacterData) child;
			return cd.getData();
		} else {
			return "?";
		}
	}
	public String getRedisIP() {
		return RedisIP;
	}
	public void setRedisIP(String redisIP) {
		RedisIP = redisIP;
	}
	
}

