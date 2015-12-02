package es.tid.provisioningManager.modules.orchestrator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import org.apache.commons.beanutils.BeanUtils;

import redis.clients.jedis.Jedis;
import es.tid.provisioningManager.modules.ProvisioningManagerParams;
import es.tid.provisioningManager.modules.dispatcher.COPModeDispatcher;
import es.tid.provisioningManager.modules.dispatcher.Dispatcher;
import es.tid.provisioningManager.modules.dispatcher.InfoDispatcher;
import es.tid.provisioningManager.modules.topologyModule.TopologyModuleParams;
import tid.provisioningManager.modules.comms.CommsToPCE;
import tid.provisioningManager.modules.comms.CommsToTM;
//import tid.provisioningManager.modules.dispatcher.Dispatcher;
import tid.provisioningManager.modules.dispatcher.DispatcherQueue;
import tid.provisioningManager.modules.dispatcher.LSPWriter;
import tid.provisioningManager.objects.ChangeRoute;
import tid.provisioningManager.objects.ConfigureLSPpcep;
import tid.provisioningManager.objects.CreateIPLink;
import tid.provisioningManager.objects.CreateLSP;
import tid.provisioningManager.objects.CreateLightPath;
import tid.provisioningManager.objects.CreateMulticast;
import tid.provisioningManager.objects.OFObject;
import tid.provisioningManager.objects.UpdateIpLink;
import tid.provisioningManager.objects.UpdateL0Link;
import es.tid.provisioningManager.objects.RouterInfoPM;
import es.tid.provisioningManager.objects.Topology;
import es.tid.provisioningManager.objects.lsps.LSP;
import es.tid.provisioningManager.utilities.PMUtilities;
import tid.provisioningManager.objects.openflow.PushFlowController;
import tid.provisioningManager.objects.openflow.StaticFlow;
import tid.topologyModule.writer.gson.GsonClient;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import es.tid.of.DataPathID;
import es.tid.pce.computingEngine.algorithms.vlan.VLAN_Multicast_algorithm;
import es.tid.pce.pcep.PCEPProtocolViolationException;
import es.tid.pce.pcep.constructs.Path;
import es.tid.pce.pcep.constructs.StateReport;
import es.tid.pce.pcep.messages.PCEPInitiate;
import es.tid.pce.pcep.messages.PCEPReport;
import es.tid.pce.pcep.objects.*;
import es.tid.protocol.commons.ByteHandler;
import es.tid.rsvp.objects.subobjects.DataPathIDEROSubobject;
import es.tid.rsvp.objects.subobjects.EROSubobject;
import es.tid.rsvp.objects.subobjects.GeneralizedLabelEROSubobject;
import es.tid.rsvp.objects.subobjects.IPv4prefixEROSubobject;
import es.tid.rsvp.objects.subobjects.OpenFlowIDEROSubobject;
import es.tid.rsvp.objects.subobjects.OpenFlowUnnumberIfIDEROSubobject;
import es.tid.rsvp.objects.subobjects.SubObjectValues;
import es.tid.rsvp.objects.subobjects.SwitchIDEROSubobject;
import es.tid.rsvp.objects.subobjects.SwitchIDEROSubobjectEdge;
import es.tid.rsvp.objects.subobjects.UnnumberIfIDEROSubobject;
import es.tid.rsvp.objects.subobjects.UnnumberedDataPathIDEROSubobject;
import es.tid.tedb.elements.Intf;
import es.tid.tedb.elements.Link;
import es.tid.tedb.elements.Node;
import es.tid.util.UtilsFunctions;

public class Orchestrator  extends Thread{

	private Logger log=Logger.getLogger("Orchestrator");
	private ProvisioningManagerParams params;
	private TopologyModuleParams tmParams;	
	private PCEPInitiate pcepInitiate;
	private Topology mytopology;
	private DataOutputStream out;
	private LinkedList<LSP> lsps;
	private HashMap<Integer, ExplicitRouteObject> eroIdMap;
	static  AtomicInteger oPcounter = new AtomicInteger(1);


	//FIXME : We should ask TM for Topology. We have duplicated information

	public Orchestrator(ProvisioningManagerParams params, PCEPInitiate pcepInitiate, DataOutputStream out, LinkedList<LSP> lsps, HashMap<Integer, ExplicitRouteObject> eroIdMap){	
		this.out = out;
		this.params = params;
		this.tmParams = new TopologyModuleParams(params.getTopologyModuleAddress(),params.getTopologyModulePort());
		this.pcepInitiate = pcepInitiate;
		this.lsps=lsps;
		this.eroIdMap=eroIdMap;
	}

	public void run(){
		log.info("---- Starting ORCHESTRATOR ------");

		if (pcepInitiate!= null){
			/*Process the PCEP message. Generate a LinkedList with the ips of the nodes*/
			log.info("PCEPInitiate arrived!!::"+pcepInitiate.getPcepIntiatedLSPList().get(0).getEro());
			if (pcepInitiate.getPcepIntiatedLSPList().get(0).getEro() == null)
			{
				log.warning("Something wrong with received packet - ERO null");			
			}
			else
			{
				LinkedList<EROSubobject> erolist = new LinkedList<EROSubobject>();
				erolist = pcepInitiate.getPcepIntiatedLSPList().get(0).getEro().EROSubobjectList;
				log.info("Number of EROSubobjects in PCEPInitiate: " + erolist.size());
				
				this.mytopology= initializFromTM();
				
				
				Hashtable<String, Node> hashEroNode = new Hashtable<String, Node>();
				
				LinkedList<InfoDispatcher> infodispatcherlist = new LinkedList<InfoDispatcher>();
				 
				Iterator<EROSubobject> iterErolist = erolist.iterator();
				while(iterErolist.hasNext()){
					EROSubobject erosubobject = (EROSubobject) iterErolist.next();
					//log.info("Cheking EROSubobject --> " + erosubobject.getClass().getName());
					

					Iterator<Node> iterNodelist = this.mytopology.getNodeList().iterator();
					while(iterNodelist.hasNext()){
						Node node = iterNodelist.next();
						if (IdEROSubobject(erosubobject).equals(IdNode(node, erosubobject))){
						
							hashEroNode.put(IdEROSubobject(erosubobject),node);
							
						}	
					}
					
					if (infodispatcherlist.size()>0 && (infodispatcherlist.getLast().getRouterType().equals(hashEroNode.get(IdEROSubobject(erosubobject)).getRouterType())) && (infodispatcherlist.getLast().getConfigurationMode().equals(hashEroNode.get(IdEROSubobject(erosubobject)).getConfigurationMode()))){
													
						infodispatcherlist.getLast().getEro().getEROSubobjectList().add(erosubobject);
						
					}else {
						
						InfoDispatcher infodispatcher = new InfoDispatcher();
						//Add Controller IP to InfoDispatcher
						infodispatcher.setControllerIP(hashEroNode.get(IdEROSubobject(erosubobject)).getControllerIP().toString());
						//Add Controller Port to InfoDispatcher
						infodispatcher.setControllerPort(hashEroNode.get(IdEROSubobject(erosubobject)).getControllerPort().toString());
						//Add configurationMode to InfoDispatcher
						infodispatcher.setConfigurationMode(hashEroNode.get(IdEROSubobject(erosubobject)).getConfigurationMode());
						//Add routerType to InfoDispatcher
						infodispatcher.setRouterType(hashEroNode.get(IdEROSubobject(erosubobject)).getRouterType());
						//Add ERO to InfoDispatcher
						infodispatcher.getEro().getEROSubobjectList().add(erosubobject);

						//Insert infodispatcher in new infodispatcherlist
						infodispatcherlist.add(infodispatcher);
						
						//Adding EROSubobject with Source Interface.
						//Calculating Source interface
						
						//Only ADD EROSubjecto from Second infodispatcherlist element
						if (infodispatcherlist.size()>1){
							
							EROSubobject previouserosubobject = infodispatcherlist.get(infodispatcherlist.size() - 2).getEro().getEROSubobjectList().getLast();
							EROSubobject erofirstsubobject = createfirstEroSubobject(erosubobject, previouserosubobject, hashEroNode, this.mytopology);
							infodispatcherlist.getLast().getEro().getEROSubobjectList().addFirst(erofirstsubobject);
							
						}
						
					}
					
					
													
				}
				
				Iterator<InfoDispatcher> iterInfodispatcherlist = infodispatcherlist.iterator();
				while(iterInfodispatcherlist.hasNext()){
					InfoDispatcher infoDispatcher = iterInfodispatcherlist.next();
					infoDispatcher.setHashEroNode(hashEroNode);
					infoDispatcher.createEndPoints();
					if (!(infoDispatcher.getEndpoints().getP2PEndpoints().getSourceEndPoint().toString().equals(infoDispatcher.getEndpoints().getP2PEndpoints().getDestinationEndPoint().toString()))){
						Dispatcher dispatcher = new Dispatcher();
						dispatcher.selectDispatcher(infoDispatcher);
					}else log.info("EndPoints are the same (Node + IF). Not Launch Dispatcher");
					
				}
			}
		}else log.info("pcepInitiate null");
		
		sendReport();
		log.info("Orchestrator ENDs.");
	}
	
	private EROSubobject createfirstEroSubobject(EROSubobject erosubobject, EROSubobject previouserosubobject, Hashtable<String, Node>  hashEroNode, Topology topology){
		EROSubobject erofirstsubobject_return = null;
		String targetIP = null;
		String otherIP = null;
		
		if (erosubobject instanceof IPv4prefixEROSubobject){
			
		}else if (erosubobject instanceof UnnumberIfIDEROSubobject){
			UnnumberIfIDEROSubobject erofirstsubobject = new UnnumberIfIDEROSubobject();
			((UnnumberIfIDEROSubobject) erofirstsubobject).setRouterID(((UnnumberIfIDEROSubobject) erosubobject).getRouterID());
			targetIP = hashEroNode.get(((UnnumberIfIDEROSubobject) erofirstsubobject).getRouterID().getHostAddress()).getAddress().get(0);
			otherIP = hashEroNode.get(((UnnumberIfIDEROSubobject) previouserosubobject).getRouterID().getHostAddress()).getAddress().get(0);
			erofirstsubobject_return = erofirstsubobject;
		}else if (erosubobject instanceof DataPathIDEROSubobject){
			
		}else if (erosubobject instanceof UnnumberedDataPathIDEROSubobject){
			UnnumberedDataPathIDEROSubobject erofirstsuboject = new UnnumberedDataPathIDEROSubobject();
			((UnnumberedDataPathIDEROSubobject)erofirstsuboject).setDataPath(((UnnumberedDataPathIDEROSubobject) erosubobject).getDataPath());
			targetIP = hashEroNode.get(((UnnumberedDataPathIDEROSubobject) erofirstsuboject).getDataPath().getDataPathID()).getAddress().get(0);
			otherIP = hashEroNode.get(((UnnumberedDataPathIDEROSubobject) previouserosubobject).getDataPath().getDataPathID()).getAddress().get(0);
			erofirstsubobject_return = erofirstsuboject;
		
		} else log.info("erofirstsuboject not implemented in createfirstEroSubobject()");
		
		String srcIntf = null;
		try {
			srcIntf = PMUtilities.getIntfNameFromLabel((Inet4Address) Inet4Address.getByName(otherIP) ,(Inet4Address) Inet4Address.getByName(targetIP), topology,7);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (erosubobject instanceof UnnumberIfIDEROSubobject){
			((UnnumberIfIDEROSubobject)erofirstsubobject_return).setInterfaceID(Long.valueOf(srcIntf));	
		}else if (erosubobject instanceof UnnumberedDataPathIDEROSubobject){
			((UnnumberedDataPathIDEROSubobject)erofirstsubobject_return).setInterfaceID(Long.valueOf(srcIntf));
		}
			
		return erofirstsubobject_return;

	}
	
	
	private String IdEROSubobject(EROSubobject erosubobject){
		//log.info("Cheking EROSubobject --> " + erosubobject.getClass().getName());
		String idEROSubobject = null;
		
		if (erosubobject instanceof IPv4prefixEROSubobject){
			//log.info("erosubobject is type IPv4prefixEROSubobject");
			idEROSubobject = ((IPv4prefixEROSubobject) erosubobject).getIpv4address().getHostAddress();
			
		}else if (erosubobject instanceof UnnumberIfIDEROSubobject){
			//log.info("erosubobject is type UnnumberIfIDEROSubobject");
			idEROSubobject = ((UnnumberIfIDEROSubobject) erosubobject).getRouterID().getHostAddress();
			
		}else if (erosubobject instanceof DataPathIDEROSubobject){
			
		}else if (erosubobject instanceof UnnumberedDataPathIDEROSubobject){
			//log.info("erosubobject is type UnnumberedDataPathIDEROSubobject");
			idEROSubobject = ((UnnumberedDataPathIDEROSubobject) erosubobject).getDataPath().getDataPathID();
			
		} else log.info("idEROSubobject not implemented in IdEROSubobject()");
		
		return idEROSubobject;
		
	}
	
	private String IdNode(Node node, EROSubobject erosubobject){
		
		String idNode = null;
		
		if (erosubobject instanceof IPv4prefixEROSubobject){
			idNode = node.getAddress().get(0);
		}else if (erosubobject instanceof UnnumberIfIDEROSubobject){
			idNode = node.getAddress().get(0);
		}else if (erosubobject instanceof DataPathIDEROSubobject){
			idNode = node.getDataPathID();
		}else if (erosubobject instanceof UnnumberedDataPathIDEROSubobject){
			idNode = node.getDataPathID();
		} else log.info("idNode not implemented in IdNode()");
		
		
		
		return idNode;
		
	}
	
	
	
	
//
//	private GeneralizedLabelEROSubobject getGeneralizedLabelEROSubobject(
//			ExplicitRouteObject geteRO) {
//		Iterator<?> eroiter= geteRO.getEROSubobjectList().iterator();
//		while (eroiter.hasNext()){
//			Object soero = eroiter.next();
//			if (soero instanceof GeneralizedLabelEROSubobject)
//				return (GeneralizedLabelEROSubobject)soero;
//		}
//		return null;
//	}
//
//	/*
//	 * 		throwDispatchersStraussVersion has been created for "First international SDN-based Network Orchestration of 
//	 *		Variable-capacity OPS over Programmable Flexi-grid EON" (Strauss project)
//	 *
//	 * 
//	 */
//	private void throwDispatchersStraussVersion(){
//		DispatcherQueue dispatcherQueue = new DispatcherQueue(5);
//		ArrayList<String> switchIDs=new ArrayList<String>();
//		ArrayList<String> inIDs=new ArrayList<String>();
//		ArrayList<String> outIDs=new ArrayList<String>();
//
//		//Taking all switchIDs, and in/out interfaces to call dispatchers (configure)
//		switchIDs.add(((OpenFlowUnnumberIfIDEROSubobject)this.pcepInitiate.getPcepIntiatedLSPList().getFirst().getEro().getEROSubobjectList().get(0)).getSwitchID());
//		inIDs.add(String.valueOf(((OpenFlowUnnumberIfIDEROSubobject)this.pcepInitiate.getPcepIntiatedLSPList().getFirst().getEro().getEROSubobjectList().get(0)).getInterfaceID()));
//		outIDs.add(String.valueOf(((OpenFlowUnnumberIfIDEROSubobject)this.pcepInitiate.getPcepIntiatedLSPList().getFirst().getEro().getEROSubobjectList().get(1)).getInterfaceID()));
//		for (int i=2; i<this.pcepInitiate.getPcepIntiatedLSPList().getFirst().getEro().getEROSubobjectList().size()-1;i++){
//			if (this.pcepInitiate.getPcepIntiatedLSPList().getFirst().getEro().getEROSubobjectList().get(i).getType()==SubObjectValues.ERO_SUBOBJECT_UNNUMBERED_IF_ID_OPEN_FLOW){
//				switchIDs.add(((OpenFlowUnnumberIfIDEROSubobject)this.pcepInitiate.getPcepIntiatedLSPList().getFirst().getEro().getEROSubobjectList().get(i)).getSwitchID());
//				outIDs.add(String.valueOf(((OpenFlowUnnumberIfIDEROSubobject)this.pcepInitiate.getPcepIntiatedLSPList().getFirst().getEro().getEROSubobjectList().get(i)).getInterfaceID()));
//				inIDs.add(getUnnumberedIntfFromSwitchIDs(switchIDs.get(i-2),switchIDs.get(i-1), outIDs.get(i-2)));
//			} else if (this.pcepInitiate.getPcepIntiatedLSPList().getFirst().getEro().getEROSubobjectList().get(i).getType()==SubObjectValues.ERO_SUBOBJECT_ID_OPEN_FLOW){
//				switchIDs.add(((OpenFlowIDEROSubobject)this.pcepInitiate.getPcepIntiatedLSPList().getFirst().getEro().getEROSubobjectList().get(i)).getSwitchID());
//				outIDs.add(String.valueOf(((OpenFlowUnnumberIfIDEROSubobject)this.pcepInitiate.getPcepIntiatedLSPList().getFirst().getEro().getEROSubobjectList().get(i+1)).getInterfaceID()));
//				inIDs.add(getUnnumberedIntfFromSwitchIDs(switchIDs.get(i-2),switchIDs.get(i-1), outIDs.get(i-2)));
//			}
//		}
//
//		//Log: Path
//		String salida="";
//		for (int j=0; j<switchIDs.size(); j++) {
//			salida=salida+inIDs.get(j)+"/"+switchIDs.get(j)+"/"+outIDs.get(j)+"; ";
//		}
//		System.out.println("Before configuring, PATH=<"+salida+">");
//
//
//		for(int j=0; j<switchIDs.size(); j++) {
//			if (inIDs.get(j).equals("?") ||outIDs.get(j).equals("?")){
//				System.out.println("Ignoring configuration in Node: "+switchIDs.get(j)+". Interfafe not found");
//			} else {
//				OFObject ofo=new OFObject(Integer.parseInt(inIDs.get(j)),Integer.parseInt(outIDs.get(j)));
//				ofo.setSwitchID(switchIDs.get(j).replace(":", "-"));
//				ofo.setRouterType(getRouterType(switchIDs.get(j).replace(":", "-")));
//				ofo.setBandwidth(((BandwidthRequested)this.pcepInitiate.getPcepIntiatedLSPList().getFirst().getBandwidth()).getBw());
//				if (ofo.getDstport()==1001)
//					ofo.setDstport(1);
//				if (ofo.getSrcport()==1001)
//					ofo.setSrcport(1);
//
//				Dispatcher dispatcher = new Dispatcher(ofo);
//				dispatcherQueue.execute(dispatcher);
//			}
//		}
//	}
//
//	private String getRouterType(String nodeid) {
//		Iterator<Node> iternodes=this.mytopology.getNodeList().iterator();
//		while(iternodes.hasNext()){
//			Node node =iternodes.next();
//			if (node.getNodeID().equals(nodeid))
//				return node.getRouterType();
//		}
//		return null;
//	}
//
//
//	private String getUnnumberedIntfFromSwitchIDs(String srce, String dst, String sourceIntrf){
//
//		String source=srce.replace(":","-");
//		String dest=dst.replace(":","-");
//		System.out.println("Finding Link: "+source+"/"+dest);
//
//		Iterator<Link> linkiter=this.mytopology.getLinkList().iterator();
//		while(linkiter.hasNext()){
//			Link link=linkiter.next();
//			//System.out.println("Comparando:"+source+"/"+link.getSourceId()+"  &&  "+dest+"/"+link.getDestID()+"  &&  "+sourceIntrf+"/"+link.getSourceIntf());
//			//System.out.println("Y Comparando:"+dest+"/"+link.getSourceId()+"  &&  "+source+"/"+link.getDestID()+"  &&  "+sourceIntrf+"/"+link.getDestIntf());
//			if (link.getSourceId().equals(source) && link.getDestID().equals(dest) && link.getSourceIntf().equals(sourceIntrf)){
//				return link.getDestIntf();
//			} else if ( link.getSourceId().equals(dest) && link.getDestID().equals(source) && link.getDestIntf().equals(sourceIntrf)){
//				return link.getSourceIntf();
//			}
//		}
//		return "?";
//	}
//
//
//	/**
//	 * Throw all the dispatcher, both for the forward and backward route
//	 * @param routerInfoList
//	 * @param pathIPList
//	 * @param mytopology
//	 */
//	private void throwDispatchers_edges(Hashtable<String,RouterInfoPM> routerInfoList, LinkedList<Object> pathList) 
//	{
//		/*Start Dispatcher Pool*/
//		DispatcherQueue dispatcherQueue = new DispatcherQueue(1);
//
//
//		Dispatcher dispatcher = new Dispatcher(new CreateMulticast(routerInfoList, pathList));
//		dispatcherQueue.execute(dispatcher);
//
//		//LSPWriter lspWriter = new LSPWriter(params);
//		//lspWriter.write(routerInfoList,pathList);
//	}
//
//
//	/**
//	 * Throw all the dispatcher, both for the forward and backward route
//	 * @param routerInfoList
//	 * @param pathIPList
//	 * @param mytopology
//	 */
////	private void throwDispatchers(Hashtable<String,RouterInfoPM> routerInfoList, LinkedList<Object> pathList) 
////	{
////		/*Start Dispatcher Pool*/
////		DispatcherQueue dispatcherQueue = new DispatcherQueue(5);
////		try{
////			/*Get interface IP source*/
////			String source = ((NodeInformation)pathList.get(0)).getId();
////			log.info("Source interface IP " + source);	
////			/*Get interface IP dest*/
////			int size = pathList.size();
////			for (int i=0; i< size; i++)
////			{					
////				/*Configure forward route*/
////				/*No  need for the last hop*/
////				log.info("----------Configuring forward route "+ i+ "----------" );					
////				/*Dispatcher*/
////				log.info("pathList.get(i):"+pathList.get(i));
////				log.info("((NodeInformation)pathList.get(i)).getId():"+((NodeInformation)pathList.get(i)).getId());
////				if (routerInfoList.get(((NodeInformation)pathList.get(i)).getId()) == null)
////				{
////					log.severe("One of the switches is not in the topology");
////					throw new Exception();
////				}
////				NodeInformation nodeInf = (NodeInformation)pathList.get(i);
////				Dispatcher dispatcher = new Dispatcher(new ChangeRoute(routerInfoList.get(nodeInf.getId()), ((NodeInformation)pathList.get(0)).getAssociatedMac(), ((NodeInformation)pathList.get(size-1)).getAssociatedMac(), nodeInf,params));
////				dispatcherQueue.execute(dispatcher);												
////			}
////		}
////		catch(Exception e)
////		{
////			log.info(UtilsFunctions.exceptionToString(e));
////		}
////
////		LSPWriter lspWriter = new LSPWriter(params);
////		lspWriter.write(routerInfoList,pathList);
////	}
//
//	/**
//	 * 
//	 */

	//Este código genera una petición 'getFullTopology' a TM.jar, recibe la respuesta y la convierte de JSON a la clase Topology
	private Topology initializFromTM() {
		String requestToDo = null;
		Socket socket = null;
		DataOutputStream out = null;
		DataInputStream in = null;
		JsonObject json= new JsonObject();
		json.addProperty("domainID", "1");

		requestToDo= json.toString();
		try {
			socket= new Socket("localhost", 9876);    

			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Mando la informacion
		int length = requestToDo.length();
		byte[] bytesToSend = new byte[(length+4)];
		bytesToSend[0]= (byte) (1 << 4  );
		bytesToSend[0]=(byte) (bytesToSend[0] | 0);
		bytesToSend[1] = 0x00;
		bytesToSend[2]=(byte)(length >>> 8 & 0xff);
		bytesToSend[3]=(byte)(length & 0xff);
		System.arraycopy(requestToDo.getBytes(), 0, bytesToSend, 4, length);

		try {
			out.write(bytesToSend);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String response = null;	
		boolean readMessage=true;
		while (readMessage) {
			try {
				response = readMsg(in);
				//System.out.println(response);
				if (response != null){
					readMessage=false;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Gson gson = new Gson();
		System.out.println("---- Answer from the Topology Module ------");
		System.out.println(response);

		//		Old code. Translating malformed JSON
		//		int indexNode= response.indexOf("nodeList");
		//		int indexLink= response.indexOf("linkList");
		//
		//		
		//		String responseaux=response.substring(indexNode-2, indexLink-4);
		//		responseaux=responseaux+","+response.substring(indexLink-1, response.length()-3)+"}";
		//		System.out.println(responseaux);

		Topology grafo = new Topology();
		try{
			grafo = gson.fromJson(response, es.tid.provisioningManager.objects.Topology.class);
		}catch (Exception e)
		{
			e.printStackTrace();
		}

		return grafo;
	}

//
//	/*
//	 * 		getTopologyFromFloodLight and initializFromTM are equivalent.
//	 * 		The first one has been used by b.jmb and the second one by b.aam
//	 */
//	private Hashtable<String,RouterInfoPM> getTopologyNodes()
//	{
//		Hashtable<String,RouterInfoPM> routerInfoList = new Hashtable<String,RouterInfoPM>();
//		GsonClient gsonClient = new GsonClient(params.getTopologyModuleAddress(),params.getTopologyModulePort());
//
//		Collection<RouterInfoPM> colRouter = gsonClient.getTopologyNodes();
//
//		for (RouterInfoPM router : colRouter) 
//		{
//			routerInfoList.put(router.getRouterID(),router);
//		}
//		return routerInfoList;
//	}
//
//
//	/*
//	private Hashtable<String,RouterInfoPM> getTopologyFromFloodLight()
//	{
//		Hashtable<String,RouterInfoPM> routerInfoList = new Hashtable<String,RouterInfoPM>();
//
//		try
//		{
//			log.info("params.getControllerIP():"+params.getControllerIP());
//			log.info("params.getControllerPort():"+params.getControllerPort());
//			log.info("params.getControllerTopologyQuery():"+params.getControllerTopologyQuery());
//
//			URL topoplogyURL = new URL("http://"+params.getControllerIP()+":"+params.getControllerPort()+
//					params.getControllerTopologyQuery());
//
//
//	        URLConnection yc = topoplogyURL.openConnection();
//	        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
//
//	        String inputLine;
//	        String response = "";
//
//	        while ((inputLine = in.readLine()) != null) 
//	        {
//	        	response = response + inputLine;
//	        }
//	        in.close();
//
//	        JSONParser parser = new JSONParser();
//			Object obj = parser.parse(response);
//
//			JSONArray msg = (JSONArray) obj;
//			Iterator<JSONObject> iterator = msg.iterator();
//			while (iterator.hasNext()) 
//			{
//				JSONObject jsonObject = (JSONObject) iterator.next();
//
//				RouterInfoPM rInfo = new RouterInfoPM();
//				rInfo.setMacAddress((String)jsonObject.get("mac"));
//
//				rInfo.setRouterID((String)jsonObject.get("dpid"));
//
//
//				JSONArray ports = (JSONArray) jsonObject.get("ports");
//				Iterator<JSONObject> portIterator = ports.iterator();
//				while (portIterator.hasNext()) 
//				{
//					JSONObject jsonPortObject = (JSONObject) portIterator.next();
//					rInfo.setMacAddress((String)jsonPortObject.get("hardwareAddress"));
//					log.info("hardwareAddress:"+rInfo.getMacAddress());
//				}
//
//
//				rInfo.setRouterType("HP");
//				rInfo.setConfigurationMode("Openflow");
//				routerInfoList.put(rInfo.getRouterID(),rInfo);
//				log.info("rInfo.getRouterID():"+rInfo.getRouterID());
//			}
//		}
//		catch (Exception e)
//		{
//			log.info(FuncionesUtiles.exceptionToString(e));
//		}
//
//		return routerInfoList;
//	}
//	 */
//
//	/**
//	 * Decode ERO and call Topology Module to get the info of each node
//	 * @param eroList
//	 * @return
//	 */
//	private LinkedList<RouterInfoPM> getRouterInfoFromERO(ExplicitRouteObject eroList){
//		Integer eroListSize = eroList.getEROSubobjectList().size();
//		log.info("Ero list Size: " + eroListSize);
//		LinkedList<RouterInfoPM> routerInfoList = new LinkedList<RouterInfoPM>();
//		for(int i = 0; i < eroListSize; i++){
//			EROSubobject eroSubobject = eroList.getEROSubobjectList().get(i);
//			Node auxNode = new Node();			
//			switch(eroSubobject.getType()){			
//			case SubObjectValues.ERO_SUBOBJECT_IPV4PREFIX:								
//				IPv4prefixEROSubobject eroSubobjectIPv4 = (IPv4prefixEROSubobject)eroSubobject;
//				if(eroSubobjectIPv4.getPrefix()!=32){
//					/*Interface ID*/
//					auxNode = PMUtilities.getNodeByIntfAddress(eroSubobjectIPv4.getIpv4address(),this.tmParams.getRoute(),this.mytopology);
//				}
//				else{
//					/*Router ID*/
//					auxNode = PMUtilities.getNodeByAddress(eroSubobjectIPv4.getIpv4address(),this.tmParams.getRoute(),this.mytopology);
//				}
//				break;
//			case SubObjectValues.ERO_SUBOBJECT_UNNUMBERED_IF_ID:
//				/*Router ID*/
//				UnnumberIfIDEROSubobject eroSubobjectUI = (UnnumberIfIDEROSubobject)eroSubobject;
//				auxNode = PMUtilities.getNodeByAddress(eroSubobjectUI.getRouterID(),this.tmParams.getRoute(),this.mytopology);
//				break;
//			}
//			/*Create the RouterInfoPM*/
//			RouterInfoPM auxRouterInfoPM = new RouterInfoPM();
//			auxRouterInfoPM.fromNode(auxNode);
//			/*Add it to the list RouterInfoPM*/
//			routerInfoList.add(auxRouterInfoPM);			
//		}		
//		return routerInfoList;		
//	}
//
//
//	/**
//	 * OLD
//	 * Decode ERO, only accept ERO_SUBOBJECT_IPV4PREFIX
//	 * Prefix 30 for the first n-1 hops, prefix 32 for the last hop
//	 * @param ero
//	 * @return
//	 */
//	private LinkedList<Object> decodeERO(ExplicitRouteObject eroList){
//		LinkedList<Object> pathList = new LinkedList<Object>();
//		log.info(eroList.getEROSubobjectList().toString());
//		Integer eroListSize = eroList.getEROSubobjectList().size();
//		log.info("Ero list Size: " + eroListSize);
//		for(int i = 0; i < eroListSize; i++){
//			EROSubobject eroSubobject = eroList.getEROSubobjectList().get(i);
//			if (eroSubobject.getType()==SubObjectValues.ERO_SUBOBJECT_LABEL){
//
//				if ((eroSubobject instanceof GeneralizedLabelEROSubobject) && (Arrays.equals(((GeneralizedLabelEROSubobject)eroSubobject).getLabel(), new BigInteger(VLAN_Multicast_algorithm.BYTE_TAG, 2).toByteArray())))
//				{
//					log.info("Adding label: " + eroSubobject);
//					pathList.add(eroSubobject);
//				}
//
//				log.info("Element NOT added::"+eroSubobject.getClass().getName());
//
//			}
//			if (eroSubobject.getType()==SubObjectValues.ERO_SUBOBJECT_IPV4PREFIX){
//				IPv4prefixEROSubobject eroSubobjectIPv4 = (IPv4prefixEROSubobject)eroSubobject;
//				if((eroSubobjectIPv4.getPrefix() == 30) && (eroListSize !=eroListSize-1)){
//					Inet4Address nodeAddress = eroSubobjectIPv4.getIpv4address();
//					pathList.add(nodeAddress);						
//				}
//				else if ((eroSubobjectIPv4.getPrefix() == 32) && (i == eroListSize-1)){
//					Inet4Address nodeAddress = eroSubobjectIPv4.getIpv4address();
//					pathList.add(nodeAddress);					
//				}
//				else{
//					Inet4Address nodeAddress = eroSubobjectIPv4.getIpv4address();
//					pathList.add(nodeAddress);
//				}
//				log.info("ERO_SUBOBJECT_IPV4PREFIX::"+SubObjectValues.ERO_SUBOBJECT_IPV4PREFIX);
//			}else if (eroSubobject.getType()==SubObjectValues.ERO_SUBOBJECT_UNNUMBERED_IF_ID){				
//				UnnumberIfIDEROSubobject eroSubobjectUI = (UnnumberIfIDEROSubobject)eroSubobject;
//				Inet4Address nodeAddress = eroSubobjectUI.getRouterID();
//				pathList.add(nodeAddress);
//				/*long interfaceID=eroSubobjectUI.getInterfaceID();
//				pathList.add(nodeAddress);*/
//				log.info("ERO_SUBOBJECT_UNNUMBERED_IF_ID::"+SubObjectValues.ERO_SUBOBJECT_UNNUMBERED_IF_ID);
//			}
//
//			else if (eroSubobject.getType()==SubObjectValues.ERO_SUBOBJECT_SWITCH_ID){				
//				SwitchIDEROSubobject eroSubobjectUI = (SwitchIDEROSubobject)eroSubobject;
//				byte[] nodeAddress = eroSubobjectUI.getSwitchID();
//				StringBuilder sb = new StringBuilder(18);
//				log.info("nodeAddress::"+nodeAddress);
//				for (byte b : nodeAddress) {
//					if (sb.length() > 0)
//						sb.append(':');
//					sb.append(String.format("%02x", b));
//				}
//				String associatedMac = eroSubobjectUI.getAssociated_mac() == null ? null : ByteHandler.ByteMACToString(eroSubobjectUI.getAssociated_mac());
//				String secondAssociatedMac = eroSubobjectUI.getSecond_associated_mac() == null ? null : ByteHandler.ByteMACToString(eroSubobjectUI.getSecond_associated_mac());
//				pathList.add(new NodeInformation(sb.toString(), eroSubobjectUI.getSource_int(), eroSubobjectUI.getDest_int(), associatedMac, secondAssociatedMac,eroSubobjectUI.getVlan()));
//				log.info("eroSubobjectUI.getVlan()::"+eroSubobjectUI.getVlan()+",sb.toString()::"+sb.toString());
//			}
//			else if (eroSubobject.getType()==SubObjectValues.ERO_SUBOBJECT_SWITCH_ID_EDGE)
//			{
//				log.info("SubObjectValues.ERO_SUBOBJECT_SWITCH_ID_EDGE");
//				SwitchIDEROSubobjectEdge eroSubobjectUI = (SwitchIDEROSubobjectEdge)eroSubobject;
//				byte[] nodeAddress = eroSubobjectUI.getSource_SwitchID();
//				StringBuilder sb_source = new StringBuilder(18);
//
//				for (byte b : nodeAddress) {
//					if (sb_source.length() > 0)
//						sb_source.append(':');
//					sb_source.append(String.format("%02x", b));
//				}
//				log.info("sb_source::"+sb_source);
//
//				StringBuilder sb_dest = new StringBuilder(18);
//
//				nodeAddress = eroSubobjectUI.getDest_SwitchID();
//				for (byte b : nodeAddress) {
//					if (sb_dest.length() > 0)
//						sb_dest.append(':');
//					sb_dest.append(String.format("%02x", b));
//				}
//
//				log.info("sb_dest::"+sb_dest);
//
//				String associatedMac = eroSubobjectUI.getAssociated_mac() == null ? null : ByteHandler.ByteMACToString(eroSubobjectUI.getAssociated_mac());
//				String secondAssociatedMac = eroSubobjectUI.getSecond_associated_mac() == null ? null : ByteHandler.ByteMACToString(eroSubobjectUI.getSecond_associated_mac());
//				pathList.add(new NodeInformation(sb_source.toString(), sb_dest.toString(), eroSubobjectUI.getSource_int(), eroSubobjectUI.getDest_int(), associatedMac, secondAssociatedMac,eroSubobjectUI.getVlan()));
//				log.info("eroSubobjectUI.getVlan()::"+eroSubobjectUI.getVlan()+",sb.toString()::"+sb_source.toString());
//			}
//			else if(eroSubobject.getType()==SubObjectValues.ERO_SUBOBJECT_DATAPATH_ID){				
//				DataPathIDEROSubobject eroSubobjectDpid = (DataPathIDEROSubobject)eroSubobject;
//				//DataPathID dataPathID = eroSubobjectDpid.getDataPath();
//				pathList.add(eroSubobjectDpid);
//				/*long interfaceID=eroSubobjectUI.getInterfaceID();
//				pathList.add(nodeAddress);*/
//				log.info("ERO_SUBOBJECT_DATAPATH_ID::"+SubObjectValues.ERO_SUBOBJECT_DATAPATH_ID);
//			}
//			else if(eroSubobject.getType()==SubObjectValues.ERO_SUBOBJECT_UNNUMBERED_DATAPATH_ID){				
//				UnnumberedDataPathIDEROSubobject eroSubobjectDpid = (UnnumberedDataPathIDEROSubobject)eroSubobject;
//				pathList.add(eroSubobjectDpid);
//				/*long interfaceID=eroSubobjectUI.getInterfaceID();
//				pathList.add(nodeAddress);*/
//				log.info("ERO_SUBOBJECT_UNNUMBERED_DATAPATH_ID::"+SubObjectValues.ERO_SUBOBJECT_UNNUMBERED_DATAPATH_ID);
//			}
//			else
//			{
//				log.info("Unknown ERO subobject found!!::eroSubobject.getType():"+eroSubobject.getType());
//			}
//		}	
//		return pathList;
//	}
//
//	public class NodeInformation
//	{
//		private String id;
//		private String dest_id;
//		private String source_port; 
//		private String dest_port;
//		private String associatedMac;
//		private String secondAssociatedMac;
//		private Integer vlan;
//		private String controller;
//
//		NodeInformation(String id, int source_port, int dest_port, String associatedMac, String secondAssociatedMac,Integer vlan)
//		{
//			this.id = id;
//			this.source_port = ((Integer)source_port).toString();
//			this.dest_port = ((Integer)dest_port).toString();
//			this.associatedMac = associatedMac;
//			this.secondAssociatedMac = secondAssociatedMac;
//			this.vlan = vlan;
//			this.dest_id = null;
//		}
//		NodeInformation(String id, String dest_id, int source_port, int dest_port, String associatedMac, String secondAssociatedMac,Integer vlan)
//		{
//			this.id = id;
//			this.source_port = ((Integer)source_port).toString();
//			this.dest_port = ((Integer)dest_port).toString();
//			this.associatedMac = associatedMac;
//			this.secondAssociatedMac = secondAssociatedMac;
//			this.vlan = vlan;
//			this.dest_id = dest_id;
//		}
//		public String getId() 
//		{
//			return id;
//		}
//		public String getSource_port() 
//		{
//			return source_port;
//		}
//		public String getDest_port() 
//		{
//			return dest_port;
//		}
//		public String getAssociatedMac() 
//		{
//			return associatedMac;
//		}
//		public void setAssociatedMac(String associatedMac) 
//		{
//			this.associatedMac = associatedMac;
//		}
//		public Integer getVlan() 
//		{
//			return vlan;
//		}
//		public String getSecondAssociatedMac() 
//		{
//			return secondAssociatedMac;
//		}
//		public void setSecondAssociatedMac(String secondAssociatedMac)
//		{
//			this.secondAssociatedMac = secondAssociatedMac;
//		}
//		public String getDest_id()
//		{
//			return dest_id;
//		}
//		public String getController() 
//		{
//			return controller;
//		}
//		public void setController(String controller)
//		{
//			this.controller = controller;
//		}
//		@Override
//		public String toString() 
//		{
//			return "NodeInformation [id=" + id + ", dest_id=" + dest_id
//					+ ", source_port=" + source_port + ", dest_port="
//					+ dest_port + ", associatedMac=" + associatedMac
//					+ ", secondAssociatedMac=" + secondAssociatedMac
//					+ ", vlan=" + vlan + "]";
//		}
//
//	}
//
//	/**
//	 * OLD
//	 *Get the info of each router from their IP_interface
//	 * 
//	 * @param pathIPList
//	 * @param routerInfo
//	 * @param mytopology
//	 */	
//	private void queryTopologyModule(LinkedList<Inet4Address> pathIPList,LinkedList<RouterInfoPM> routerInfo){
//		/*For each Interface Address in the path */
//		for(int i = 0; i < pathIPList.size() - 1 ; i++){			
//			/*Get the Node info */
//			Node auxNode = PMUtilities.getNodeByIntfAddress(pathIPList.get(i),this.tmParams.getRoute(),this.mytopology);
//		
//			/*FIXME*/	
//			//Check if not null
//			log.info("Node " + auxNode.toString());
//			/*Create the RouterInfoPM*/
//			RouterInfoPM auxRouterInfoPM = new RouterInfoPM();
//			auxRouterInfoPM.fromNode(auxNode);
//
//			/*Add it to the list RouterInfoPM*/
//			routerInfo.add(auxRouterInfoPM);
//		}
//
//		/*The last EROSubobject is the managementAddress of the node*/
//		Node auxNode = PMUtilities.getNodeByAddress(pathIPList.getLast(),this.tmParams.getRoute(),this.mytopology);
//		//log.info("Node " + auxNode.toString());
//
//		/*Create the RouterInfoPM*/
//		RouterInfoPM auxRouterInfoPM = new RouterInfoPM();
//		auxRouterInfoPM.fromNode(auxNode);
//
//		/*Add it to the list RouterInfoPM*/	
//		routerInfo.add(auxRouterInfoPM);
//
//		System.out.println("------ Show Router info ------");
//		for (int i=0; i<routerInfo.size(); i++){
//			System.out.println(routerInfo.get(i).toString());
//			routerInfo.get(i).logAllInfo();
//		}
//	}
//
//	private void generateBidirectionalPath(LinkedList<LinkedList<RouterInfoPM>> routerInfoListLayers, LinkedList<LinkedList<Inet4Address>> pathIPListLayers, LinkedList<String> path, LinkedList<String> path2,	String lastIF, String firstIF){
//		if (1==pathIPListLayers.getFirst().size()){
//			Link link=PMUtilities.getLink(routerInfoListLayers.getFirst().getFirst().getRouterID(), routerInfoListLayers.get(1).getFirst().getRouterID(), this.mytopology);
//			String intf=null;
//			if (routerInfoListLayers.getFirst().getFirst().getRouterID().equals(link.getDest().getNode()))
//				intf=link.getDest().getIntf();
//			else
//				intf=link.getSource().getIntf();
//			log.info("Ticket: 1 Interface: "+intf);
//			String intfIP=PMUtilities.getIntfByName(PMUtilities.getNodeByName(routerInfoListLayers.getFirst().getFirst().getRouterID(), this.mytopology), intf).getAddress().get(1);
//			firstIF=intfIP;
//
//		}else{
//			Link link=PMUtilities.getLink(routerInfoListLayers.getFirst().getFirst().getRouterID(), routerInfoListLayers.getFirst().get(1).getRouterID(), this.mytopology);
//			String intf=null;
//			if (routerInfoListLayers.getFirst().getFirst().getRouterID().equals(link.getDest().getNode()))
//				intf=link.getDest().getIntf();
//			else
//				intf=link.getSource().getIntf();
//			log.info("Ticket: 1 Interface: "+intf);
//			String intfIP=PMUtilities.getIntfByName(PMUtilities.getNodeByName(routerInfoListLayers.getFirst().getFirst().getRouterID(), this.mytopology), intf).getAddress().get(1);
//			firstIF=intfIP;
//		}
//		for(int j=0; j<pathIPListLayers.size();j++){
//			for (int i=0; i<pathIPListLayers.get(j).size(); i++){
//				log.info("Entramos la "+j+"'esima vez en el elemento: "+i);
//				if (((j==pathIPListLayers.size()-1)&&(i+1==pathIPListLayers.get(j).size()-1))||((i==pathIPListLayers.get(j).size()-1)&&(j+1==pathIPListLayers.size()-1)&&(1==pathIPListLayers.get(j+1).size()))){
//					//Last hop
//					if (i==pathIPListLayers.get(j).size()-1){
//						Link link=PMUtilities.getLink(routerInfoListLayers.get(j).get(i).getRouterID(), routerInfoListLayers.get(j+1).getFirst().getRouterID(), this.mytopology);
//						String intf=null;
//						String intf2=null;
//						if (routerInfoListLayers.get(j+1).getFirst().getRouterID().equals(link.getDest().getNode())){
//							intf=link.getDest().getIntf();
//							intf2=link.getSource().getIntf();
//						}
//						else{
//							intf=link.getSource().getIntf();
//							intf2=link.getDest().getIntf();
//						}
//						log.info("Ticket: 1 Interface: "+intf);
//						String intfIP=PMUtilities.getIntfByName(PMUtilities.getNodeByName(routerInfoListLayers.get(j+1).getFirst().getRouterID(), this.mytopology), intf).getAddress().get(1);
//						String intfIP2=PMUtilities.getIntfByName(PMUtilities.getNodeByName(routerInfoListLayers.get(j).getLast().getRouterID(), this.mytopology), intf2).getAddress().get(1);
//
//						lastIF=intfIP;
//						//path.add(intfIP);
//						path2.addFirst(intfIP2);
//						path.add(intfIP);
//
//					} else {
//						Link link=PMUtilities.getLink(routerInfoListLayers.get(j).get(i).getRouterID(), routerInfoListLayers.get(j).get(i+1).getRouterID(), this.mytopology);
//						String intf=null;
//						String intf2=null;
//
//						if (routerInfoListLayers.get(j).get(i+1).getRouterID().equals(link.getDest().getNode())) {
//							intf=link.getDest().getIntf();
//							intf2=link.getSource().getIntf();
//						}
//						else {
//							intf=link.getSource().getIntf();
//							intf2=link.getDest().getIntf();
//						}
//						log.info("Ticket: 2 Interface: "+intf);				
//						String intfIP=PMUtilities.getIntfByName(PMUtilities.getNodeByName(routerInfoListLayers.get(j).get(i+1).getRouterID(), this.mytopology), intf).getAddress().get(1);
//						String intfIP2=PMUtilities.getIntfByName(PMUtilities.getNodeByName(routerInfoListLayers.get(j).get(i).getRouterID(), this.mytopology), intf2).getAddress().get(1);
//
//						lastIF=intfIP;
//						//path.add(intfIP);
//						path2.addFirst(intfIP2);
//						path.add(intfIP);
//
//
//					}	
//				}else if ((j==pathIPListLayers.size()-1)&&(i==pathIPListLayers.get(j).size()-1)){
//					log.info("Excluding the last round");
//				}
//				else {
//					//Other hops
//
//					if (i==pathIPListLayers.get(j).size()-1){
//						Link link=PMUtilities.getLink(routerInfoListLayers.get(j).get(i).getRouterID(), routerInfoListLayers.get(j+1).getFirst().getRouterID(), this.mytopology);
//						String intf=null;
//						String intf2=null;
//
//						if (routerInfoListLayers.get(j+1).getFirst().getRouterID().equals(link.getDest().getNode())){
//							intf=link.getDest().getIntf();
//							intf2=link.getSource().getIntf();
//
//						}
//						else{
//							intf=link.getSource().getIntf();
//							intf2=link.getDest().getIntf();
//
//						}
//						log.info("Ticket: 3 Interface: "+intf);
//						String intfIP=PMUtilities.getIntfByName(PMUtilities.getNodeByName(routerInfoListLayers.get(j+1).getFirst().getRouterID(), this.mytopology), intf).getAddress().get(1);
//						String intfIP2=PMUtilities.getIntfByName(PMUtilities.getNodeByName(routerInfoListLayers.get(j).getLast().getRouterID(), this.mytopology), intf2).getAddress().get(1);
//
//						path.add(intfIP);
//						path2.addFirst(intfIP2);
//
//					} else {
//						Link link=PMUtilities.getLink(routerInfoListLayers.get(j).get(i).getRouterID(), routerInfoListLayers.get(j).get(i+1).getRouterID(), this.mytopology);
//						String intf=null;
//						String intf2=null;
//
//						if (routerInfoListLayers.get(j).get(i+1).getRouterID().equals(link.getDest().getNode())){
//							intf=link.getDest().getIntf();
//							intf2=link.getSource().getIntf();
//
//						}
//						else{
//							intf=link.getSource().getIntf();
//							intf2=link.getDest().getIntf();
//
//						}
//						log.info("Ticket: 4 Interface: "+intf);
//						String intfIP=PMUtilities.getIntfByName(PMUtilities.getNodeByName(routerInfoListLayers.get(j).get(i+1).getRouterID(), this.mytopology), intf).getAddress().get(1);
//						String intfIP2=PMUtilities.getIntfByName(PMUtilities.getNodeByName(routerInfoListLayers.get(j).get(i).getRouterID(), this.mytopology), intf2).getAddress().get(1);
//
//						path.add(intfIP);
//						path2.addFirst(intfIP2);
//
//					}
//				}
//			}
//		}
//	}
//
//	private Dispatcher getDispacther4TransportLayer( LinkedList<RouterInfoPM> routerInfo, LinkedList<Inet4Address> pathIP, Inet4Address lastIP, Inet4Address beforeIP, int n, boolean delete) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException{
//		Dispatcher dispatcher=null;
//		if (routerInfo.get(0).getConfigurationMode().equals("PCEP")){
//			PCEPInitiate pcepInit;
//			pcepInit = (PCEPInitiate) BeanUtils.cloneBean(this.pcepInitiate);
//			PMUtilities.removeFromEro(pcepInit, pathIP.getFirst(), pathIP.getLast());
//			log.info("ERO modificado: "+pcepInit.getPcepIntiatedLSPList().get(0).getEro().toString()); 
//			log.info("EndPoints: "+((EndPointsIPv4)pcepInit.getPcepIntiatedLSPList().get(0).getEndPoint()).getDestIP()+"  "+((EndPointsIPv4)pcepInit.getPcepIntiatedLSPList().get(0).getEndPoint()).getSourceIP());
//			dispatcher = new Dispatcher(new ConfigureLSPpcep(pcepInit));
//			//FIXME: How to delete in pcep case??
//		} else if ((routerInfo.get(0).getConfigurationMode().equals("OF"))&&(routerInfo.get(0).getRouterType().equals("Adva"))){
//			int sourceIntf=PMUtilities.getIntfFromLabelAdva(n,pathIP.getFirst());
//			int destIntf=PMUtilities.getIntfFromLabelAdva(n,pathIP.getLast());
//			dispatcher =new Dispatcher(new CreateLightPath(sourceIntf,destIntf, delete));
//		} else if ((routerInfo.get(0).getConfigurationMode().equals("OF"))&&(routerInfo.get(0).getRouterType().equals("Infinera"))){
//			//Infinera's mode
//			int sourceIntf=PMUtilities.getIntfFromLabelInfinera(n,pathIP.getFirst());
//			int destIntf=PMUtilities.getIntfFromLabelInfinera(n,pathIP.getLast());
//			String sourceIP=pathIP.getFirst().getHostAddress();
//			String destIP=pathIP.getLast().getHostAddress();
//			CreateLightPath clp=new CreateLightPath(sourceIntf,destIntf, delete); clp.setSrcip(sourceIP); clp.setDstip(destIP);
//			dispatcher =new Dispatcher(clp);
//		}else if ((routerInfo.get(0).getConfigurationMode().equals("RestAPI"))&&(routerInfo.get(0).getRouterType().equals("Infinera"))){
//			log.info("Entering config Infinera...");
//			String srcIntf=PMUtilities.getIntfNameFromLabel(lastIP,pathIP.getFirst(), this.mytopology,n);
//			String dstIntf=PMUtilities.getIntfNameFromLabel(beforeIP,pathIP.getLast(), this.mytopology,n);		
//			String sourceIP=PMUtilities.getNodeByAddress(pathIP.getFirst(),this.tmParams.getRoute(),this.mytopology).getAddress().get(1);
//			String destIP=PMUtilities.getNodeByAddress(pathIP.getLast(),this.tmParams.getRoute(),this.mytopology).getAddress().get(1);
//			String managIP=pathIP.getFirst().getHostAddress();
//			//FIXME: Coger y añadir los nombres de las interfaces para el 
//			CreateLightPath clp=new CreateLightPath(0,0, delete); 
//			log.info("Params "+ sourceIP+ " "+destIP+" "+managIP+" "+srcIntf+" "+dstIntf);
//			clp.setSrcip(sourceIP); 
//			clp.setDstip(destIP); 
//			clp.setManagementip(managIP);
//			clp.setSrcportname(srcIntf);	
//			clp.setDstportname(dstIntf);
//			dispatcher =new Dispatcher(clp);
//		}else if (routerInfo.get(0).getConfigurationMode().equals("EmulatedMode")){
//			log.info("EmulatedMode DataPathID Configuration");
//			String srcIntf=PMUtilities.getIntfNameFromLabel(lastIP,beforeIP, this.mytopology,n);
//			String dstIntf=PMUtilities.getIntfNameFromLabel(beforeIP,lastIP, this.mytopology,n);
//			String sourceIP=beforeIP.toString();
//			String destIP=lastIP.toString();
//			String managIP=pathIP.getFirst().getHostAddress();
//			String srcDpid = null;
//			String dstDpid = null;
//			for(int i=0; i< mytopology.getNodeList().size(); i++){
//				if (mytopology.getNodeList().get(i).getAddress().get(0).toString().equals(sourceIP.substring(1, sourceIP.length()))){
//					srcDpid=mytopology.getNodeList().get(i).getDataPathID();
//
//				}else if (mytopology.getNodeList().get(i).getAddress().get(0).toString().equals(destIP.substring(1, destIP.length()))){
//					dstDpid=mytopology.getNodeList().get(i).getDataPathID();
//				}
//			}
//
//			CreateLightPath clp=new CreateLightPath(0,0, delete); 
//			clp.setSrcip(sourceIP); 
//			clp.setDstip(destIP); 
//			clp.setSrcDpid(srcDpid);
//			clp.setDstDpid(dstDpid);
//			clp.setManagementip(managIP);
//			clp.setSrcportname(srcIntf);	
//			clp.setDstportname(dstIntf);
//			clp.setConfigurationmode(routerInfo.get(0).getConfigurationMode());
//			clp.setRouterType(routerInfo.get(0).getRouterType());
//			clp.setControllerIP(routerInfo.get(0).getControllerIP());
//			clp.setControllerPort(routerInfo.get(0).getControllerPort());
//			log.info("Params "+ clp.getManagementip() +" "+  clp.getSrcip() +" "+  clp.getSrcDpid() +" "+ clp.getDstip() +" "+ clp.getDstDpid() +" "+ clp.getSrcportname() +" "+ clp.getDstportname() +" "+ clp.getConfigurationmode() +" "+ clp.getRouterType()+" "+ clp.getControllerIP()+" "+ clp.getControllerPort());
//			dispatcher =new Dispatcher(clp);
//
//			//Dispatcher to Update the topology in Topology Module [via JSON] & PCE [via OSPF] manually
//			/*if (((PMUtilities.getLink(sourceIP, destIP, this.mytopology)==null)&&!delete) || ((PMUtilities.getLink(sourceIP, destIP, this.mytopology)!=null)&&delete)) {
//				log.info("Updating L0 Link ");
//				String srcIPname=PMUtilities.getNodeByAddress(beforeIP, null, this.mytopology).getNodeID();
//				String dstIPname=PMUtilities.getNodeByAddress( lastIP, null, this.mytopology).getNodeID();
//				UpdateL0Link uil= new UpdateL0Link(srcIntf, srcIPname, dstIntf, dstIPname, this.mytopology, delete);
//				Dispatcher tmdispatcher = new Dispatcher(uil);
//				tmdispatcher.start();
//			} */
//
//			/*if (routerInfo.get(0).getRouterType().equals("Infinera")){
//				log.info("Infinera");
//			}
//			else if (routerInfo.get(0).getRouterType().equals("Ciena")){
//				log.info("Ciena");
//			}*/
//
//			/*try 
//			{
//				// Dispatcher is dynamically obtain 
//				Class<?> act = Class.forName("tid.provisioningManager.modules.dispatcher." + routerInfo.get(0).getRouterType()+routerInfo.get(0).getConfigurationMode()+"Dispatcher");
//
//				Class[] cArg = new Class[5];
//
//				cArg[0] = StaticFlow.class;
//				cArg[1] = String.class;
//				cArg[2] = String.class;
//
//				srcport
//				dstport
//				srcip
//				dstip
//				managementip
//
//				Object[] args = new Object[5];
//				args[0] = staticFlow;
//				args[1] = rout.getControllerIP();
//				args[2] = rout.getControllerPort();
//
//
//				PushFlowController pushFlow = (PushFlowController)act.getDeclaredConstructor(cArg).newInstance(args);
//				pushFlow.sendRequest();
//			}
//			catch (Exception e1)
//			{
//				log.info("Critical - No Dispatcher found");
//				return -1;
//			}*/
//
//			//FIXME: Actualizar topologia CAPA 0
//		}else if (routerInfo.get(0).getConfigurationMode().equals("COPEmulatedMode")){
//	
//			log.info("CopEmulatedMode DataPathID Configuration");	
//			//
//			//
//			Inet4Address nodeIP = pathIP.getFirst();
//			Inet4Address previousIP = beforeIP;
//			Inet4Address nextIP = lastIP;
//			
//			//pathip -> ip del nodo; lastip -> ip del nodo; beforeip -> ip del nodo siguiente; //en el �ltimo nodo last y before cambian
//			//Hacemos 3 casos, para el primer nodo, para los intermedios y para el �ltimo
//			//log.info("jm recibiendo ip " + nodeIP +" - "+ previousIP +" - "+ nextIP);
//			String srcIntf = null;
//			String dstIntf = null;
//			
////			if (nodeIP == previousIP){
////				//Primer caso -> primer nodo
////				log.info("jm  viendo ip " + previousIP + " - " +nodeIP+ " - " + nextIP);
////				srcIntf="66";
////				dstIntf=PMUtilities.getIntfNameFromLabel(nextIP,nodeIP, this.mytopology,n);
////				log.info("jm Primer nodo -> puerto origen: " +srcIntf+" puerto destino: "+dstIntf);
////				
////			}  else if (nodeIP == nextIP){
////				//segundo caso -> �ltimo nodo
////				log.info("jm  viendo ip " + previousIP + " - " +nodeIP+ " - " + nextIP);
////				srcIntf=PMUtilities.getIntfNameFromLabel(previousIP,nodeIP, this.mytopology,n);
////				dstIntf="67";
////				log.info("jm Primer nodo -> puerto origen: " +srcIntf+" puerto destino: "+dstIntf);
////				
////			} else {
////				//Tercer caso -> nodos intermedios
////				log.info("jm  viendo ip " + previousIP + " - " +nodeIP+ " - " + nextIP);
////				srcIntf=PMUtilities.getIntfNameFromLabel(previousIP,nodeIP, this.mytopology,n);
////				dstIntf=PMUtilities.getIntfNameFromLabel(nextIP,nodeIP, this.mytopology,n);
////				log.info("jm Primer nodo -> puerto origen: " +srcIntf+" puerto destino: "+dstIntf);
////			
////			}
//			
//			
//			
//			String srcIP=nodeIP.toString();
//			String destIP=nodeIP.toString();
//			
//			String managIP=pathIP.getFirst().getHostAddress();
//			String srcDpid = null;
//			String dstDpid = null;
//			String controllerIP = routerInfo.get(0).getControllerIP();
//			String controllerPort = routerInfo.get(0).getControllerPort();
//			
//			//Calculamos los Dpid
//			for(int i=0; i< mytopology.getNodeList().size(); i++){
//				if (mytopology.getNodeList().get(i).getAddress().get(0).toString().equals(srcIP.substring(1, srcIP.length()))){
//					srcDpid=mytopology.getNodeList().get(i).getDataPathID();
//					dstDpid=srcDpid;
//				}
//			}
//			
//			
//			CreateLightPath clp=new CreateLightPath(0,0, delete); 
//			clp.setSrcip(destIP); 
//			clp.setDstip(destIP);
//			clp.setSrcDpid(dstDpid);	//Only need dstDpid to send to controllers
//			clp.setDstDpid(dstDpid);
//			clp.setManagementip(managIP);
//			clp.setSrcportname(srcIntf);	
//			clp.setDstportname(dstIntf);
//			clp.setConfigurationmode(routerInfo.get(0).getConfigurationMode());
//			clp.setRouterType(routerInfo.get(0).getRouterType());
//			clp.setControllerIP(controllerIP);
//			clp.setControllerPort(controllerPort);
//			//log.info("Params "+ clp.getManagementip() +" "+  clp.getSrcip() +" "+  clp.getSrcDpid() +" "+ clp.getDstip() +" "+ clp.getDstDpid() +" "+ clp.getSrcportname() +" "+ clp.getDstportname() +" "+ clp.getConfigurationmode() +" "+ clp.getRouterType()+" "+ clp.getControllerIP()+" "+ clp.getControllerPort());
//			dispatcher =new Dispatcher(clp);
//		}else if (routerInfo.get(0).getConfigurationMode().equals("COPDiscusMode")){
//	
//			log.info("COPDiscusMode Configuration");
//			Inet4Address nodeIP = pathIP.getFirst();
//			Inet4Address previousIP = beforeIP;
//			Inet4Address nextIP = lastIP;
//			
//			//pathip -> ip del nodo; lastip -> ip del nodo; beforeip -> ip del nodo siguiente; //en el �ltimo nodo last y before cambian
//			//Hacemos 3 casos, para el primer nodo, para los intermedios y para el �ltimo
//			//log.info("jm recibiendo ip " + nodeIP +" - "+ previousIP +" - "+ nextIP);
//			String srcIntf = null;
//			String dstIntf = null;
//			
////			if (nodeIP == previousIP){
////				//Primer caso -> primer nodo
////				log.info("jm  viendo ip " + previousIP + " - " +nodeIP+ " - " + nextIP);
////				srcIntf="66";
////				dstIntf=PMUtilities.getIntfNameFromLabel(nextIP,nodeIP, this.mytopology,n);
////				log.info("jm Primer nodo -> puerto origen: " +srcIntf+" puerto destino: "+dstIntf);
////				
////			}  else if (nodeIP == nextIP){
////				//segundo caso -> �ltimo nodo
////				log.info("jm  viendo ip " + previousIP + " - " +nodeIP+ " - " + nextIP);
////				srcIntf=PMUtilities.getIntfNameFromLabel(previousIP,nodeIP, this.mytopology,n);
////				dstIntf="67";
////				log.info("jm Primer nodo -> puerto origen: " +srcIntf+" puerto destino: "+dstIntf);
////				
////			} else {
////				//Tercer caso -> nodos intermedios
////				log.info("jm  viendo ip " + previousIP + " - " +nodeIP+ " - " + nextIP);
////				srcIntf=PMUtilities.getIntfNameFromLabel(previousIP,nodeIP, this.mytopology,n);
////				dstIntf=PMUtilities.getIntfNameFromLabel(nextIP,nodeIP, this.mytopology,n);
////				log.info("jm Primer nodo -> puerto origen: " +srcIntf+" puerto destino: "+dstIntf);
////			
////			}
//			
//			
//			
//			String srcIP=nodeIP.toString();
//			String destIP=nodeIP.toString();
//			
//			String managIP=pathIP.getFirst().getHostAddress();
//			String srcDpid = null;
//			String dstDpid = null;
//			String controllerIP = routerInfo.get(0).getControllerIP();
//			String controllerPort = routerInfo.get(0).getControllerPort();
//			
//			//Calculamos los Dpid
//			for(int i=0; i< mytopology.getNodeList().size(); i++){
//				if (mytopology.getNodeList().get(i).getAddress().get(0).toString().equals(srcIP.substring(1, srcIP.length()))){
//					srcDpid=mytopology.getNodeList().get(i).getDataPathID();
//					dstDpid=srcDpid;
//				}
//			}
//			
//			
//			CreateLightPath clp=new CreateLightPath(0,0, delete); 
//			clp.setSrcip(destIP); 
//			clp.setDstip(destIP);
//			clp.setSrcDpid(dstDpid);	//Only need dstDpid to send to controllers
//			clp.setDstDpid(dstDpid);
//			clp.setManagementip(managIP);
//			clp.setSrcportname(srcIntf);	
//			clp.setDstportname(dstIntf);
//			clp.setConfigurationmode(routerInfo.get(0).getConfigurationMode());
//			clp.setRouterType(routerInfo.get(0).getRouterType());
//			clp.setControllerIP(controllerIP);
//			clp.setControllerPort(controllerPort);
//			//log.info("Params "+ clp.getManagementip() +" "+  clp.getSrcip() +" "+  clp.getSrcDpid() +" "+ clp.getDstip() +" "+ clp.getDstDpid() +" "+ clp.getSrcportname() +" "+ clp.getDstportname() +" "+ clp.getConfigurationmode() +" "+ clp.getRouterType()+" "+ clp.getControllerIP()+" "+ clp.getControllerPort());
//			dispatcher =new Dispatcher(clp);
//		} else {
//			//Another configuration mode
//			log.info("Error No dispatcher launch");
//		}
//		return dispatcher;
//	}
//	
//	//with path (generic objetct)
//	private Dispatcher getDispacther4TransportLayerCOP( LinkedList<RouterInfoPM> routerInfo, Object thisHop, Object previousHop, Object nextHop, int n, boolean delete) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException{
//		Dispatcher dispatcher=null;
//		if (routerInfo.get(0).getConfigurationMode().equals("COPEmulatedMode")){
//	
//			log.info("CopEmulatedMode DataPathID Configuration");	
//			//
//			//
////			Inet4Address nodeIP = thisHop.getFirst();
////			Inet4Address previousIP = beforeIP;
////			Inet4Address nextIP = nextHop;
//			
//			//pathip -> ip del nodo; lastip -> ip del nodo; beforeip -> ip del nodo siguiente; //en el �ltimo nodo last y before cambian
//			//Hacemos 3 casos, para el primer nodo, para los intermedios y para el �ltimo
//			//log.info("jm recibiendo ip " + nodeIP +" - "+ previousIP +" - "+ nextIP);
//			
////			log.info("jm ver info thishop: "+thisHop.toString());
////			log.info("jm ver info phop: "+previousHop.toString());
////			log.info("jm ver info nhop: "+nextHop.toString());
//			String srcIntf = null;
//			String dstIntf = null;
//			String srcDpid = null;
//			String dstDpid = null;
//			
//			srcDpid = ((UnnumberedDataPathIDEROSubobject)thisHop).getDataPath().getDataPathID();
//			String dpidPreviousHop = ((UnnumberedDataPathIDEROSubobject)previousHop).getDataPath().getDataPathID();
//			String dpidNextHop = ((UnnumberedDataPathIDEROSubobject)nextHop).getDataPath().getDataPathID();
//			
//			if ((srcDpid.equalsIgnoreCase(dpidPreviousHop)) && (srcDpid.equalsIgnoreCase(dpidNextHop))){
//				//First hop in ERO -> know srcIntf and dstIntf
//				log.info("jm First hop");
//				srcIntf = Long.toString(((UnnumberedDataPathIDEROSubobject)thisHop).getInterfaceID());
//				dstIntf = Long.toString(((UnnumberedDataPathIDEROSubobject)nextHop).getInterfaceID());
//			}else{
//				log.info("jm intermediate or last hop");
//				
//				
//				
//				String nameEndPointSrc="";
//				String nameEndPointDst="";
//				//Search name of node with specific DPID
//				for(int i=0; i< mytopology.getNodeList().size(); i++){
//
//					if (mytopology.getNodeList().get(i).getDataPathID().equals(srcDpid)){
//						nameEndPointSrc = mytopology.getNodeList().get(i).getNodeID();
//					}else if (mytopology.getNodeList().get(i).getDataPathID().equals(dpidPreviousHop)){
//						nameEndPointDst = mytopology.getNodeList().get(i).getNodeID();
//					}else{
//						log.info("No interface found");
//					}
//						
//				}
//				
//				log.info("jm ver nombre origen y destino: "+nameEndPointSrc+" "+nameEndPointDst);
//				//search source interface interface
//				for(int i=0; i< mytopology.getLinkList().size(); i++){
//
//					if (mytopology.getLinkList().get(i).getSource().getNode().equals(nameEndPointSrc) && mytopology.getLinkList().get(i).getDest().getNode().equals(nameEndPointDst)){
//						
//						//srcIntf = mytopology.getLinkList().get(i).getSourceIntf();
//						//log.info("jm ver get interface: "+ mytopology.getLinkList().get(i).getSource().toString());
//						log.info("jm ver get interface: "+ mytopology.getLinkList().get(i).getSource().getIntf());
//						srcIntf = mytopology.getLinkList().get(i).getSource().getIntf();
//						
//					}else{
//						log.info("No interface found");
//					}
//						
//				}
//				
//				dstIntf = Long.toString(((UnnumberedDataPathIDEROSubobject)thisHop).getInterfaceID());
//
//				
//			}		
//			
//			dstDpid = srcDpid;
//			
//			
//			
//			//String managIP=thisHop.getFirst().getHostAddress();
//			String controllerIP = routerInfo.get(0).getControllerIP();
//			String controllerPort = routerInfo.get(0).getControllerPort();
//			
//			
//			
//			CreateLightPath clp=new CreateLightPath(0,0, delete);
//			clp.setSrcDpid(dstDpid);	//Only need dstDpid to send to controllers
//			clp.setDstDpid(dstDpid);
//			//clp.setManagementip(managIP);
//			clp.setSrcportname(srcIntf);	
//			clp.setDstportname(dstIntf);
//			clp.setConfigurationmode(routerInfo.get(0).getConfigurationMode());
//			clp.setRouterType(routerInfo.get(0).getRouterType());
//			clp.setControllerIP(controllerIP);
//			clp.setControllerPort(controllerPort);
//			//log.info("Params "+ clp.getManagementip() +" "+  clp.getSrcip() +" "+  clp.getSrcDpid() +" "+ clp.getDstip() +" "+ clp.getDstDpid() +" "+ clp.getSrcportname() +" "+ clp.getDstportname() +" "+ clp.getConfigurationmode() +" "+ clp.getRouterType()+" "+ clp.getControllerIP()+" "+ clp.getControllerPort());
//			dispatcher =new Dispatcher(clp);
//		} else {
//			//Another configuration mode
//			log.info("Error No dispatcher launch");
//		}
//		return dispatcher;
//	}
//
//	private void throwDispatchersV2(LinkedList<RouterInfoPM> routerInfoList, LinkedList<Inet4Address> pathIPList, Topology mytopology, int n, boolean delete) {		
//		DispatcherQueue dispatcherQueue = new DispatcherQueue(5);
//		boolean layerzero=false;
//		LinkedList<LinkedList<RouterInfoPM>> routerInfoListLayers=new LinkedList<LinkedList<RouterInfoPM>>();
//		LinkedList<LinkedList<Inet4Address>> pathIPListLayers=new LinkedList<LinkedList<Inet4Address>>();
//		PMUtilities.divideByLayers(pathIPList, routerInfoList, mytopology, pathIPListLayers, routerInfoListLayers);
//		PMUtilities.printIPsquare(pathIPListLayers);
//
//		try {
//			for (int i=0; i<pathIPListLayers.size();i++){
//				Dispatcher dispatcher=null;
//				String opticalMode=null;
//				if (routerInfoListLayers.get(i).get(0).getLayer().equals("transport")){
//					log.info("layerzero equals true");
//					layerzero=true;
//					while (routerInfoListLayers.get(i).get(0).getLayer().equals("transport")){
//						opticalMode=routerInfoListLayers.get(i).get(0).getRouterType();
//
//						//generating Dispatcher for Optical Nodes. It works for ADVA Rest API, Infinera REST API, PCEP (tnX nodes) and Emulated Mode
//						dispatcher=getDispacther4TransportLayer(routerInfoListLayers.get(i),  pathIPListLayers.get(i), pathIPListLayers.get(i-1).getLast(), pathIPListLayers.get(i+1).getFirst(),  n,  delete);
//
//						dispatcherQueue.execute(dispatcher);//Executing Dispatcher
//						routerInfoListLayers.remove(i);//Deleting Optical nodes configured
//						pathIPListLayers.remove(i);
//						log.info("Entramos aqu�");
//					}
//					long bw=PMUtilities.getBandwidth(opticalMode);
//					Intf srcIntf = PMUtilities.getIntfFromLabel(n,routerInfoList.getFirst().getRouterID(), mytopology);//Fake
//					Intf destIntf = PMUtilities.getIntfFromLabel(n,routerInfoList.getLast().getRouterID(), mytopology);//Fake
//					Node srcNode = PMUtilities.getNodeByName(routerInfoListLayers.get(i-1).getLast().getRouterID(), this.mytopology);
//					Node destNode = PMUtilities.getNodeByName(routerInfoListLayers.get(i).getFirst().getRouterID(), this.mytopology);
//					log.info("SRC: NODE "+routerInfoListLayers.get(i-1).getLast().getRouterID());
//					log.info("DST: NODE "+routerInfoListLayers.get(i).getFirst().getRouterID());
//					if (srcNode==null || destNode==null){
//						log.info("Error. Nodes are not in Topology Module.");
//					}
//
//					//Configuring IPLink in L3 Nodes (IP interfaces)
//					Dispatcher dispatcherNodeSrc = new Dispatcher(new CreateIPLink((Inet4Address)Inet4Address.getByName(destNode.getAddress().get(0)), (Inet4Address)Inet4Address.getByName(destIntf.getAddress().get(1)), 
//							(Inet4Address)Inet4Address.getByName(srcNode.getAddress().get(0)), (Inet4Address)Inet4Address.getByName(srcIntf.getAddress().get(1)), 
//							srcNode.getConfigurationMode(),srcIntf.getName(), delete, bw));
//					dispatcherQueue.execute(dispatcherNodeSrc);
//
//					Dispatcher dispatcherNodeDst = new Dispatcher(new CreateIPLink((Inet4Address)Inet4Address.getByName(srcNode.getAddress().get(0)), (Inet4Address)Inet4Address.getByName(srcIntf.getAddress().get(1)), 
//							(Inet4Address)Inet4Address.getByName(destNode.getAddress().get(0)), (Inet4Address)Inet4Address.getByName(destIntf.getAddress().get(1)), 
//							destNode.getConfigurationMode(), destIntf.getName(), delete, bw));
//					dispatcherQueue.execute(dispatcherNodeDst);
//
//
//					//Dispatcher to Update the topology in Topology Module [via JSON] & PCE [via OSPF] manually
//					if ((PMUtilities.getLink(routerInfoList.get(0).getRouterID(), routerInfoList.get(routerInfoList.size()-1).getRouterID(), this.mytopology)==null)&&!delete){
//						UpdateIpLink uil= new UpdateIpLink(srcIntf.getName(), routerInfoList.get(0).getRouterID(), destIntf.getName(), routerInfoList.get(routerInfoList.size()-1).getRouterID(), mytopology, delete, bw);
//						Dispatcher tmdispatcher = new Dispatcher(uil);
//						tmdispatcher.start();
//					} else if ((PMUtilities.getLink(routerInfoList.get(0).getRouterID(), routerInfoList.get(routerInfoList.size()-1).getRouterID(),this.mytopology)!=null)&&delete){
//						UpdateIpLink uil= new UpdateIpLink(srcIntf.getName(), routerInfoList.get(0).getRouterID(), destIntf.getName(), routerInfoList.get(routerInfoList.size()-1).getRouterID(), mytopology, delete, bw);
//						Dispatcher tmdispatcher = new Dispatcher(uil);
//						tmdispatcher.start();
//					}
//
//				}
//			}} catch (Exception e) {
//				e.printStackTrace();
//			}
//		if ((PMUtilities.numberOfNodes(routerInfoListLayers)>2)||!layerzero) {
//			//We only take care of Juniper routers...
//
//			//this.mytopology= initializFromTM();
//			log.info("-----MPLS_Link Creation Mode-----");
//			log.info("Number of l3 routers: "+PMUtilities.numberOfNodes(routerInfoListLayers));
//
//			try{
//				LinkedList<String> path=new LinkedList<String>();
//				LinkedList<String> path2=new LinkedList<String>();
//				String nodetobeconfigured=pathIPListLayers.getFirst().getFirst().getHostAddress();
//				String nodetobeconfigured2=pathIPListLayers.getLast().getLast().getHostAddress();
//				String lastIF=null;
//				String firstIF=null;
//
//				float bw=((BandwidthRequested)this.pcepInitiate.getPcepIntiatedLSPList().getFirst().getBandwidth()).getBw();
//
//				String lspName=routerInfoList.getFirst().getRouterID()+"_"+routerInfoList.getLast().getRouterID();
//				int id=search4LspId(lspName);
//				lspName=lspName+":"+id;
//
//				//printinfobeforeconfigure(nodetobeconfigured, nodetobeconfigured2, lspName, path, path2);
//
//				Dispatcher dispatcher = new Dispatcher(new CreateLSP(nodetobeconfigured,lastIF,lspName,path, bw));
//				dispatcher.start();
//				Dispatcher dispatcher2 = new Dispatcher(new CreateLSP(nodetobeconfigured2,firstIF,lspName,path2, bw));
//				dispatcher2.start();
//
//				//Saving the lsp 
//				if (!delete){
//					LSP lsp=new LSP(lspName, oPcounter.get()+1,routerInfoList.getFirst().getManagementAddress(), routerInfoList.getLast().getManagementAddress(), path, bw);
//					lsps.add(lsp);
//					addInfoRedis(lspName,routerInfoList.getFirst().getManagementAddress(), routerInfoList.getLast().getManagementAddress(), path, bw);
//				}
//				else {
//					LSP lspaux=getLspFromID((int)pcepInitiate.getPcepIntiatedLSPList().get(0).getLsp().getLspId());
//					pcepInitiate.getPcepIntiatedLSPList().get(0).setBandwidth(new BandwidthRequested());
//					((BandwidthRequested)pcepInitiate.getPcepIntiatedLSPList().get(0).getBandwidth()).setBw(lspaux.getBw());
//					if (lspaux==null)
//						log.info("LSP NOT FOUND");
//					else
//						delInfoRedis(lspaux.getId());
//				}
//				path.addFirst(routerInfoList.getFirst().getManagementAddress().getHostAddress());
//
//				informPCEandTM(path, ((BandwidthRequested)pcepInitiate.getPcepIntiatedLSPList().get(0).getBandwidth()), delete);
//
//
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//		}
//		if (pcepInitiate.getPcepIntiatedLSPList().get(0).getRsp().isrFlag())
//			eroIdMap.remove((int)pcepInitiate.getPcepIntiatedLSPList().get(0).getLsp().getLspId());
//		else
//			eroIdMap.put(oPcounter.incrementAndGet(), pcepInitiate.getPcepIntiatedLSPList().getFirst().getEro());
//	}
//	
//	private void throwDispatchersV3(LinkedList<RouterInfoPM> routerInfoList, LinkedList<Inet4Address> pathIPList, Topology mytopology, int n, boolean delete) {		
//		DispatcherQueue dispatcherQueue = new DispatcherQueue(5);
//		boolean layerzero=false;
//		LinkedList<LinkedList<RouterInfoPM>> routerInfoListLayers=new LinkedList<LinkedList<RouterInfoPM>>();
//		LinkedList<LinkedList<Inet4Address>> pathIPListLayers=new LinkedList<LinkedList<Inet4Address>>();
//		PMUtilities.divideByLayers(pathIPList, routerInfoList, mytopology, pathIPListLayers, routerInfoListLayers);
//		PMUtilities.printIPsquare(pathIPListLayers);
//
//		log.info("Number nodes: " + PMUtilities.numberOfNodes(routerInfoListLayers) + " Path size: "+ pathIPListLayers.size() + " numberOfL3PacketRouters: " + PMUtilities.numberOfNetworkElementsInLayer(routerInfoListLayers, "IP") + " numberOfOpticalNodes: " + PMUtilities.numberOfNetworkElementsInLayer(routerInfoListLayers, "transport"));
//
//		// Save LSP Redis DB
//		if (PMUtilities.numberOfNetworkElementsInLayer(routerInfoListLayers, "IP")==0){
//			if (!delete){
//				log.info("Adding Path info in Redis");
//				LinkedList<String> path=new LinkedList<String>();
//
//				// Path search 
//				for (int j=0; j<routerInfoListLayers.size(); j++){
//					String infIP=routerInfoListLayers.get(j).get(0).getRouterID();
//					path.add(infIP);
//				}
//
//				float bw=((BandwidthRequested)this.pcepInitiate.getPcepIntiatedLSPList().getFirst().getBandwidth()).getBw();
//				String lspName=routerInfoList.getFirst().getRouterID()+"_"+routerInfoList.getLast().getRouterID();
//				int id=search4LspId(lspName);
//				lspName=lspName+":"+id;
//				LSP lsp=new LSP(lspName, oPcounter.get()+1,routerInfoList.getFirst().getManagementAddress(), routerInfoList.getLast().getManagementAddress(), path, bw);
//				lsps.add(lsp);
//				addInfoRedis_optics(lspName,routerInfoList.getFirst().getManagementAddress(), routerInfoList.getLast().getManagementAddress(), path, bw);
//			}
//		}
//
//		String configurationmode=null;
//		String nodetype=null;
//		String configurationmode_next=null;
//		String nodetype_next=null;
//		try {
//			for (int i=0; i<pathIPListLayers.size();i++){
//				Dispatcher dispatcher=null;
//				configurationmode=routerInfoListLayers.get(i).get(0).getConfigurationMode();
//				nodetype=routerInfoListLayers.get(i).get(0).getRouterType();
//				configurationmode_next=routerInfoListLayers.get(i+1).get(0).getConfigurationMode();
//				nodetype_next=routerInfoListLayers.get(i+1).get(0).getRouterType();
//
//				//routerInfoList.get(i).logAllInfo();
//
//				if (routerInfoListLayers.get(i).get(0).getLayer().equals("transport")){
//					log.info("Transport node " + configurationmode + " " + nodetype + " Next info: "+ configurationmode_next + " " + nodetype_next);
//					layerzero=true;
//
//					// The Dispatchers are launch in the initial hop and every time there is a technology change
//					if ((configurationmode_next.equals(configurationmode) && nodetype_next.equals(nodetype))) {
//
//						//generating Dispatcher for Optical Nodes. It works for ADVA Rest API, Infinera REST API, PCEP (tnX nodes) and Emulated Mode
//						//dispatcher=getDispacther4TransportLayer(routerInfoListLayers.get(i),  pathIPListLayers.get(i), pathIPListLayers.get(i-1).getLast(), pathIPListLayers.get(i+1).getFirst(),  n,  delete);
//						if (PMUtilities.numberOfNetworkElementsInLayer(routerInfoListLayers, "IP")==0){
//							log.info("All params "+pathIPListLayers.get(i).getFirst()+ " "+ pathIPListLayers.get(i).getLast()+" -- "+pathIPListLayers.get(i+1).getFirst()+" -- "+pathIPListLayers.get(i+1).getLast());
//							log.info("Transport node " + routerInfoListLayers.get(i).get(0).getConfigurationMode() + " " + routerInfoListLayers.get(i).get(0).getRouterType() + " Next info: "+ routerInfoListLayers.get(i+1).get(0).getConfigurationMode() + " " + routerInfoListLayers.get(i+1).get(0).getRouterType());				
//							dispatcher=getDispacther4TransportLayer(routerInfoListLayers.get(i),  pathIPListLayers.get(i), pathIPListLayers.get(i).getLast(), pathIPListLayers.get(i+1).getFirst(),  n,  delete);
//						} else {
//							dispatcher=getDispacther4TransportLayer(routerInfoListLayers.get(i),  pathIPListLayers.get(i), pathIPListLayers.get(i-1).getLast(), pathIPListLayers.get(i+1).getFirst(),  n,  delete);
//						}
//						dispatcherQueue.execute(dispatcher);//Executing Dispatcher
//						routerInfoListLayers.remove(i);//Deleting Optical nodes configured
//						pathIPListLayers.remove(i);
//
//						//Configuring IPLink in L3 Nodes (IP interfaces)
//						if(PMUtilities.numberOfNetworkElementsInLayer(routerInfoListLayers, "IP")==2){
//							long bw=PMUtilities.getBandwidth(nodetype);
//							Intf srcIntf = PMUtilities.getIntfFromLabel(n,routerInfoList.getFirst().getRouterID(), mytopology);//Fake
//							Intf destIntf = PMUtilities.getIntfFromLabel(n,routerInfoList.getLast().getRouterID(), mytopology);//Fake
//							Node srcNode = PMUtilities.getNodeByName(routerInfoListLayers.get(i-1).getLast().getRouterID(), this.mytopology);
//							Node destNode = PMUtilities.getNodeByName(routerInfoListLayers.get(i).getFirst().getRouterID(), this.mytopology);
//							log.info("SRC: NODE "+routerInfoListLayers.get(i-1).getLast().getRouterID());
//							log.info("DST: NODE "+routerInfoListLayers.get(i).getFirst().getRouterID());
//							if (srcNode==null || destNode==null){
//								log.info("Error. Nodes are not in Topology Module.");
//							}
//
//							Dispatcher dispatcherNodeSrc = new Dispatcher(new CreateIPLink((Inet4Address)Inet4Address.getByName(destNode.getAddress().get(0)), (Inet4Address)Inet4Address.getByName(destIntf.getAddress().get(1)), 
//									(Inet4Address)Inet4Address.getByName(srcNode.getAddress().get(0)), (Inet4Address)Inet4Address.getByName(srcIntf.getAddress().get(1)), 
//									srcNode.getConfigurationMode(),srcIntf.getName(), delete, bw));
//							dispatcherQueue.execute(dispatcherNodeSrc);
//
//							Dispatcher dispatcherNodeDst = new Dispatcher(new CreateIPLink((Inet4Address)Inet4Address.getByName(srcNode.getAddress().get(0)), (Inet4Address)Inet4Address.getByName(srcIntf.getAddress().get(1)), 
//									(Inet4Address)Inet4Address.getByName(destNode.getAddress().get(0)), (Inet4Address)Inet4Address.getByName(destIntf.getAddress().get(1)), 
//									destNode.getConfigurationMode(), destIntf.getName(), delete, bw));
//							dispatcherQueue.execute(dispatcherNodeDst);
//
//							//Dispatcher to Update the topology in Topology Module [via JSON] & PCE [via OSPF] manually
//							if ((PMUtilities.getLink(routerInfoList.get(0).getRouterID(), routerInfoList.get(routerInfoList.size()-1).getRouterID(), this.mytopology)==null)&&!delete){
//								UpdateIpLink uil= new UpdateIpLink(srcIntf.getName(), routerInfoList.get(0).getRouterID(), destIntf.getName(), routerInfoList.get(routerInfoList.size()-1).getRouterID(), mytopology, delete, bw);
//								Dispatcher tmdispatcher = new Dispatcher(uil);
//								tmdispatcher.start();
//							} else if ((PMUtilities.getLink(routerInfoList.get(0).getRouterID(), routerInfoList.get(routerInfoList.size()-1).getRouterID(),this.mytopology)!=null)&&delete){
//								UpdateIpLink uil= new UpdateIpLink(srcIntf.getName(), routerInfoList.get(0).getRouterID(), destIntf.getName(), routerInfoList.get(routerInfoList.size()-1).getRouterID(), mytopology, delete, bw);
//								Dispatcher tmdispatcher = new Dispatcher(uil);
//								tmdispatcher.start();
//							}
//						}
//					}
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		if ((PMUtilities.numberOfNetworkElementsInLayer(routerInfoListLayers, "IP")>2)||!layerzero) {
//			//We only take care of Juniper routers...
//
//			//this.mytopology= initializFromTM();
//			log.info("-----MPLS_Link Creation Mode-----");
//			log.info("Number of l3 routers: "+PMUtilities.numberOfNetworkElementsInLayer(routerInfoListLayers, "IP"));			
//			try{
//				LinkedList<String> path=new LinkedList<String>();
//				LinkedList<String> path2=new LinkedList<String>();
//				String nodetobeconfigured=pathIPListLayers.getFirst().getFirst().getHostAddress();
//				String nodetobeconfigured2=pathIPListLayers.getLast().getLast().getHostAddress();
//				String lastIF=null;
//				String firstIF=null;
//
//				generateBidirectionalPath(routerInfoListLayers, pathIPListLayers, path, path2,	lastIF, firstIF);
//
//				float bw=((BandwidthRequested)this.pcepInitiate.getPcepIntiatedLSPList().getFirst().getBandwidth()).getBw();
//
//				String lspName=routerInfoList.getFirst().getRouterID()+"_"+routerInfoList.getLast().getRouterID();
//				int id=search4LspId(lspName);
//				lspName=lspName+":"+id;
//
//				//printinfobeforeconfigure(nodetobeconfigured, nodetobeconfigured2, lspName, path, path2);
//
//				Dispatcher dispatcher = new Dispatcher(new CreateLSP(nodetobeconfigured,lastIF,lspName,path, bw));
//				dispatcher.start();
//				Dispatcher dispatcher2 = new Dispatcher(new CreateLSP(nodetobeconfigured2,firstIF,lspName,path2, bw));
//				dispatcher2.start();
//
//				//Saving the lsp 
//				if (!delete){
//					LSP lsp=new LSP(lspName, oPcounter.get()+1,routerInfoList.getFirst().getManagementAddress(), routerInfoList.getLast().getManagementAddress(), path, bw);
//					lsps.add(lsp);
//					addInfoRedis(lspName,routerInfoList.getFirst().getManagementAddress(), routerInfoList.getLast().getManagementAddress(), path, bw);
//				}
//				else {
//					LSP lspaux=getLspFromID((int)pcepInitiate.getPcepIntiatedLSPList().get(0).getLsp().getLspId());
//					pcepInitiate.getPcepIntiatedLSPList().get(0).setBandwidth(new BandwidthRequested());
//					((BandwidthRequested)pcepInitiate.getPcepIntiatedLSPList().get(0).getBandwidth()).setBw(lspaux.getBw());
//					if (lspaux==null)
//						log.info("LSP NOT FOUND");
//					else
//						delInfoRedis(lspaux.getId());
//				}
//				path.addFirst(routerInfoList.getFirst().getManagementAddress().getHostAddress());
//
//				informPCEandTM(path, ((BandwidthRequested)pcepInitiate.getPcepIntiatedLSPList().get(0).getBandwidth()), delete);
//
//
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//		}
//		if (pcepInitiate.getPcepIntiatedLSPList().get(0).getRsp().isrFlag())
//			eroIdMap.remove((int)pcepInitiate.getPcepIntiatedLSPList().get(0).getLsp().getLspId());
//		else
//			eroIdMap.put(oPcounter.incrementAndGet(), pcepInitiate.getPcepIntiatedLSPList().getFirst().getEro());
//	}
//
//	private void throwDispatchersV4(LinkedList<RouterInfoPM> routerInfoList, LinkedList<Inet4Address> pathIPList, Topology mytopology, int n, boolean delete) {		
//
//		DispatcherQueue dispatcherQueue = new DispatcherQueue(5);
//		boolean layerzero=false;
//		LinkedList<LinkedList<RouterInfoPM>> routerInfoListLayers=new LinkedList<LinkedList<RouterInfoPM>>();
//		LinkedList<LinkedList<Inet4Address>> pathIPListLayers=new LinkedList<LinkedList<Inet4Address>>();
//		PMUtilities.divideByLayers(pathIPList, routerInfoList, mytopology, pathIPListLayers, routerInfoListLayers);
//		PMUtilities.printIPsquare(pathIPListLayers);
//
//		log.info("Number nodes: " + PMUtilities.numberOfNodes(routerInfoListLayers) + " Path size: "+ pathIPListLayers.size() + " numberOfL3PacketRouters: " + PMUtilities.numberOfNetworkElementsInLayer(routerInfoListLayers, "IP") + " numberOfOpticalNodes: " + PMUtilities.numberOfNetworkElementsInLayer(routerInfoListLayers, "transport"));
//
//		// Save LSP Redis DB
//		if (PMUtilities.numberOfNetworkElementsInLayer(routerInfoListLayers, "IP")==0){
//			if (!delete){
//				log.info("Adding Path info in Redis");
//				LinkedList<String> path=new LinkedList<String>();
//
//				// Path search 
//				for (int j=0; j<routerInfoListLayers.size(); j++){
//					String infIP=routerInfoListLayers.get(j).get(0).getRouterID();
//					path.add(infIP);
//				}
//
//				//float bw=this.pcepInitiate.getPcepIntiatedLSPList().getFirst().getBandwidth().getBw();
//				float bw = 100;
//				String lspName=routerInfoList.getFirst().getRouterID()+"_"+routerInfoList.getLast().getRouterID();
//				int id=search4LspId(lspName);
//				lspName=lspName+":"+id;
//				LSP lsp=new LSP(lspName, oPcounter.get()+1,routerInfoList.getFirst().getManagementAddress(), routerInfoList.getLast().getManagementAddress(), path, bw);
//				lsps.add(lsp);
//				//addInfoRedis_optics(lspName,routerInfoList.getFirst().getManagementAddress(), routerInfoList.getLast().getManagementAddress(), path, bw);
//			}
//		}
//
//		String configurationmode=null;
//		String nodetype=null;
//		String configurationmode_next=null;
//		String nodetype_next=null;
//		String configurationmode_prev=null;
//		String nodetype_prev=null;
//
//		try {			
//			for (int i=0; i<pathIPListLayers.size();i++){
//				Dispatcher dispatcher=null;
//				boolean call_dispatcher=false;
//				configurationmode=routerInfoListLayers.get(i).get(0).getConfigurationMode();
//				nodetype=routerInfoListLayers.get(i).get(0).getRouterType();
//				//routerInfoList.get(i).logAllInfo();
//
//				if (routerInfoListLayers.get(i).get(0).getLayer().equals("transport")){
//					layerzero=true;
//
//					if (i==0){
//						configurationmode_next=routerInfoListLayers.get(i+1).get(0).getConfigurationMode();
//						nodetype_next=routerInfoListLayers.get(i+1).get(0).getRouterType();
//						call_dispatcher=true;
//
//					}else if (i<pathIPListLayers.size()-1 && i!=0){
//						configurationmode_next=routerInfoListLayers.get(i+1).get(0).getConfigurationMode();
//						nodetype_next=routerInfoListLayers.get(i+1).get(0).getRouterType();
//						configurationmode_prev=routerInfoListLayers.get(i-1).get(0).getConfigurationMode();
//						nodetype_prev=routerInfoListLayers.get(i-1).get(0).getRouterType();		
//						if ((configurationmode_next.equals(configurationmode) && nodetype_next.equals(nodetype))) {
//							call_dispatcher=true;
//
//						} else if ((!configurationmode_prev.equals(configurationmode) || !nodetype_prev.equals(nodetype))) {
//							call_dispatcher=true;							
//						}
//					} else if (i==pathIPListLayers.size()-1){
//						configurationmode_prev=routerInfoListLayers.get(i-1).get(0).getConfigurationMode();
//						nodetype_prev=routerInfoListLayers.get(i-1).get(0).getRouterType();
//						if ((!configurationmode_prev.equals(configurationmode) || !nodetype_prev.equals(nodetype))) {
//							call_dispatcher=true;
//						}						
//					}
//
//					log.info("Init Transport node: " + configurationmode + " " + nodetype + "\n      Next Transport node: "+ configurationmode_next + " " + nodetype_next);
//
//					// The Dispatchers are launched in single hop domains and everytime there is not a technology change 
//					if (call_dispatcher) {
//
//						//generating Dispatcher for Optical Nodes. It works for ADVA Rest API, Infinera REST API, PCEP (tnX nodes) and Emulated Mode
//						//dispatcher=getDispacther4TransportLayer(routerInfoListLayers.get(i),  pathIPListLayers.get(i), pathIPListLayers.get(i-1).getLast(), pathIPListLayers.get(i+1).getFirst(),  n,  delete);
//						if (PMUtilities.numberOfNetworkElementsInLayer(routerInfoListLayers, "IP")==0){
//							if (i<pathIPListLayers.size()-1){
//								if (i==0){//Para el primer nodo
//									dispatcher=getDispacther4TransportLayer(routerInfoListLayers.get(i),  pathIPListLayers.get(i), pathIPListLayers.get(i+1).getLast(), pathIPListLayers.get(i).getFirst(),  n,  delete);
//								} else {//Para los nodos intermedios
//									dispatcher=getDispacther4TransportLayer(routerInfoListLayers.get(i),  pathIPListLayers.get(i), pathIPListLayers.get(i+1).getLast(), pathIPListLayers.get(i-1).getFirst(),  n,  delete);
//								}
//							} else {//para el �ltimo nodo
//								dispatcher=getDispacther4TransportLayer(routerInfoListLayers.get(i),  pathIPListLayers.get(i), pathIPListLayers.get(i).getLast(), pathIPListLayers.get(i-1).getFirst(),  n,  delete);
//							}
//						} else {
//							dispatcher=getDispacther4TransportLayer(routerInfoListLayers.get(i),  pathIPListLayers.get(i), pathIPListLayers.get(i-1).getLast(), pathIPListLayers.get(i+1).getFirst(),  n,  delete);
//						}
//						dispatcherQueue.execute(dispatcher);//Executing Dispatcher
//						//routerInfoListLayers.remove(i);//Deleting Optical nodes configured
//						//pathIPListLayers.remove(i);
//
//						//Configuring IPLink in L3 Nodes (IP interfaces)
//						if(PMUtilities.numberOfNetworkElementsInLayer(routerInfoListLayers, "IP")==2){
//							long bw=PMUtilities.getBandwidth(nodetype);
//							Intf srcIntf = PMUtilities.getIntfFromLabel(n,routerInfoList.getFirst().getRouterID(), mytopology);//Fake
//							Intf destIntf = PMUtilities.getIntfFromLabel(n,routerInfoList.getLast().getRouterID(), mytopology);//Fake
//							Node srcNode = PMUtilities.getNodeByName(routerInfoListLayers.get(i-1).getLast().getRouterID(), this.mytopology);
//							Node destNode = PMUtilities.getNodeByName(routerInfoListLayers.get(i).getFirst().getRouterID(), this.mytopology);
//							log.info("SRC: NODE "+routerInfoListLayers.get(i-1).getLast().getRouterID());
//							log.info("DST: NODE "+routerInfoListLayers.get(i).getFirst().getRouterID());
//							if (srcNode==null || destNode==null){
//								log.info("Error. Nodes are not in Topology Module.");
//							}
//
//							Dispatcher dispatcherNodeSrc = new Dispatcher(new CreateIPLink((Inet4Address)Inet4Address.getByName(destNode.getAddress().get(0)), (Inet4Address)Inet4Address.getByName(destIntf.getAddress().get(1)), 
//									(Inet4Address)Inet4Address.getByName(srcNode.getAddress().get(0)), (Inet4Address)Inet4Address.getByName(srcIntf.getAddress().get(1)), 
//									srcNode.getConfigurationMode(),srcIntf.getName(), delete, bw));
//							dispatcherQueue.execute(dispatcherNodeSrc);
//
//							Dispatcher dispatcherNodeDst = new Dispatcher(new CreateIPLink((Inet4Address)Inet4Address.getByName(srcNode.getAddress().get(0)), (Inet4Address)Inet4Address.getByName(srcIntf.getAddress().get(1)), 
//									(Inet4Address)Inet4Address.getByName(destNode.getAddress().get(0)), (Inet4Address)Inet4Address.getByName(destIntf.getAddress().get(1)), 
//									destNode.getConfigurationMode(), destIntf.getName(), delete, bw));
//							dispatcherQueue.execute(dispatcherNodeDst);
//
//							//Dispatcher to Update the topology in Topology Module [via JSON] & PCE [via OSPF] manually
//							if ((PMUtilities.getLink(routerInfoList.get(0).getRouterID(), routerInfoList.get(routerInfoList.size()-1).getRouterID(), this.mytopology)==null)&&!delete){
//								UpdateIpLink uil= new UpdateIpLink(srcIntf.getName(), routerInfoList.get(0).getRouterID(), destIntf.getName(), routerInfoList.get(routerInfoList.size()-1).getRouterID(), mytopology, delete, bw);
//								Dispatcher tmdispatcher = new Dispatcher(uil);
//								tmdispatcher.start();
//							} else if ((PMUtilities.getLink(routerInfoList.get(0).getRouterID(), routerInfoList.get(routerInfoList.size()-1).getRouterID(),this.mytopology)!=null)&&delete){
//								UpdateIpLink uil= new UpdateIpLink(srcIntf.getName(), routerInfoList.get(0).getRouterID(), destIntf.getName(), routerInfoList.get(routerInfoList.size()-1).getRouterID(), mytopology, delete, bw);
//								Dispatcher tmdispatcher = new Dispatcher(uil);
//								tmdispatcher.start();
//							}
//						}
//					}
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		if ((PMUtilities.numberOfNetworkElementsInLayer(routerInfoListLayers, "IP")>2)||!layerzero) {
//			//We only take care of Juniper routers...
//
//			//this.mytopology= initializFromTM();
//			log.info("-----MPLS_Link Creation Mode-----");
//			log.info("Number of l3 routers: "+PMUtilities.numberOfNetworkElementsInLayer(routerInfoListLayers, "IP"));			
//			try{
//				LinkedList<String> path=new LinkedList<String>();
//				LinkedList<String> path2=new LinkedList<String>();
//				String nodetobeconfigured=pathIPListLayers.getFirst().getFirst().getHostAddress();
//				String nodetobeconfigured2=pathIPListLayers.getLast().getLast().getHostAddress();
//				String lastIF=null;
//				String firstIF=null;
//
//				generateBidirectionalPath(routerInfoListLayers, pathIPListLayers, path, path2,	lastIF, firstIF);
//
//				float bw=((BandwidthRequested) this.pcepInitiate.getPcepIntiatedLSPList().getFirst().getBandwidth()).getBw();
//
//				String lspName=routerInfoList.getFirst().getRouterID()+"_"+routerInfoList.getLast().getRouterID();
//				int id=search4LspId(lspName);
//				lspName=lspName+":"+id;
//
//				//printinfobeforeconfigure(nodetobeconfigured, nodetobeconfigured2, lspName, path, path2);
//
//				Dispatcher dispatcher = new Dispatcher(new CreateLSP(nodetobeconfigured,lastIF,lspName,path, bw));
//				dispatcher.start();
//				Dispatcher dispatcher2 = new Dispatcher(new CreateLSP(nodetobeconfigured2,firstIF,lspName,path2, bw));
//				dispatcher2.start();
//
//				//Saving the lsp 
//				if (!delete){
//					LSP lsp=new LSP(lspName, oPcounter.get()+1,routerInfoList.getFirst().getManagementAddress(), routerInfoList.getLast().getManagementAddress(), path, bw);
//					lsps.add(lsp);
//					addInfoRedis(lspName,routerInfoList.getFirst().getManagementAddress(), routerInfoList.getLast().getManagementAddress(), path, bw);
//				}
//				else {
//					LSP lspaux=getLspFromID((int)pcepInitiate.getPcepIntiatedLSPList().get(0).getLsp().getLspId());
//					pcepInitiate.getPcepIntiatedLSPList().get(0).setBandwidth(new BandwidthRequested());
//					((BandwidthRequested)pcepInitiate.getPcepIntiatedLSPList().get(0).getBandwidth()).setBw(lspaux.getBw());
//					if (lspaux==null)
//						log.info("LSP NOT FOUND");
//					else
//						delInfoRedis(lspaux.getId());
//				}
//				path.addFirst(routerInfoList.getFirst().getManagementAddress().getHostAddress());
//
//				informPCEandTM(path, ((BandwidthRequested)pcepInitiate.getPcepIntiatedLSPList().get(0).getBandwidth()), delete);
//
//
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//		}
//		if (pcepInitiate.getPcepIntiatedLSPList().get(0).getRsp().isrFlag())
//			eroIdMap.remove((int)pcepInitiate.getPcepIntiatedLSPList().get(0).getLsp().getLspId());
//		else
//			eroIdMap.put(oPcounter.incrementAndGet(), pcepInitiate.getPcepIntiatedLSPList().getFirst().getEro());
//	}
//	
//	
//	//V5 to Unnumbered DPID ERO Subobject
//	private void throwDispatchersV5(LinkedList<RouterInfoPM> routerInfoList, LinkedList<Inet4Address> pathIPList, LinkedList<Object> pathList, Topology mytopology, int n, boolean delete) {		
//
//		DispatcherQueue dispatcherQueue = new DispatcherQueue(5);
//		boolean layerzero=false;
//		LinkedList<LinkedList<RouterInfoPM>> routerInfoListLayers=new LinkedList<LinkedList<RouterInfoPM>>();
//		LinkedList<LinkedList<Inet4Address>> pathIPListLayers=new LinkedList<LinkedList<Inet4Address>>();
//		PMUtilities.divideByLayers(pathIPList, routerInfoList, mytopology, pathIPListLayers, routerInfoListLayers);
//		PMUtilities.printIPsquare(pathIPListLayers);
//
//		log.info("Number nodes: " + PMUtilities.numberOfNodes(routerInfoListLayers) + " Path size: "+ pathIPListLayers.size() + " numberOfL3PacketRouters: " + PMUtilities.numberOfNetworkElementsInLayer(routerInfoListLayers, "IP") + " numberOfOpticalNodes: " + PMUtilities.numberOfNetworkElementsInLayer(routerInfoListLayers, "transport"));
//
//		// Save LSP Redis DB
//		if (PMUtilities.numberOfNetworkElementsInLayer(routerInfoListLayers, "IP")==0){
//			if (!delete){
//				log.info("Adding Path info in Redis");
//				LinkedList<String> path=new LinkedList<String>();
//
//				// Path search 
//				for (int j=0; j<routerInfoListLayers.size(); j++){
//					String infIP=routerInfoListLayers.get(j).get(0).getRouterID();
//					path.add(infIP);
//				}
//
//				//float bw=this.pcepInitiate.getPcepIntiatedLSPList().getFirst().getBandwidth().getBw();
//				float bw = 100;
//				String lspName=routerInfoList.getFirst().getRouterID()+"_"+routerInfoList.getLast().getRouterID();
//				int id=search4LspId(lspName);
//				lspName=lspName+":"+id;
//				LSP lsp=new LSP(lspName, oPcounter.get()+1,routerInfoList.getFirst().getManagementAddress(), routerInfoList.getLast().getManagementAddress(), path, bw);
//				lsps.add(lsp);
//				//addInfoRedis_optics(lspName,routerInfoList.getFirst().getManagementAddress(), routerInfoList.getLast().getManagementAddress(), path, bw);
//			}
//		}
//
//		String configurationmode=null;
//		String nodetype=null;
//		String configurationmode_next=null;
//		String nodetype_next=null;
//		String configurationmode_prev=null;
//		String nodetype_prev=null;
//
//		try {			
//			for (int i=0; i<pathIPListLayers.size();i++){
//				Dispatcher dispatcher=null;
//				boolean call_dispatcher=false;
//				configurationmode=routerInfoListLayers.get(i).get(0).getConfigurationMode();
//				nodetype=routerInfoListLayers.get(i).get(0).getRouterType();
//				//routerInfoList.get(i).logAllInfo();
//
//				if (routerInfoListLayers.get(i).get(0).getLayer().equals("transport")){
//					layerzero=true;
//
//					if (i==0){
//						configurationmode_next=routerInfoListLayers.get(i+1).get(0).getConfigurationMode();
//						nodetype_next=routerInfoListLayers.get(i+1).get(0).getRouterType();
//						call_dispatcher=true;
//
//					}else if (i<pathIPListLayers.size()-1 && i!=0){
//						configurationmode_next=routerInfoListLayers.get(i+1).get(0).getConfigurationMode();
//						nodetype_next=routerInfoListLayers.get(i+1).get(0).getRouterType();
//						configurationmode_prev=routerInfoListLayers.get(i-1).get(0).getConfigurationMode();
//						nodetype_prev=routerInfoListLayers.get(i-1).get(0).getRouterType();		
//						if ((configurationmode_next.equals(configurationmode) && nodetype_next.equals(nodetype))) {
//							call_dispatcher=true;
//
//						} else if ((!configurationmode_prev.equals(configurationmode) || !nodetype_prev.equals(nodetype))) {
//							call_dispatcher=true;							
//						}
//					} else if (i==pathIPListLayers.size()-1){
//						configurationmode_prev=routerInfoListLayers.get(i-1).get(0).getConfigurationMode();
//						nodetype_prev=routerInfoListLayers.get(i-1).get(0).getRouterType();
//						if ((!configurationmode_prev.equals(configurationmode) || !nodetype_prev.equals(nodetype))) {
//							call_dispatcher=true;
//						}						
//					}
//
//					log.info("Init Transport node: " + configurationmode + " " + nodetype + "\n      Next Transport node: "+ configurationmode_next + " " + nodetype_next);
//					log.info("jm call_dispatcher: "+call_dispatcher);
//					// The Dispatchers are launched in single hop domains and everytime there is not a technology change 
//					if (call_dispatcher) {
//						
//						//dispatcher=getDispacther4TransportLayerCOP(routerInfoListLayers.get(i),  pathIPListLayers.get(i), pathIPListLayers.get(i+1).getLast(), pathIPListLayers.get(i).getFirst(),  n,  delete);
//
//						//generating Dispatcher for Optical Nodes. It works for ADVA Rest API, Infinera REST API, PCEP (tnX nodes) and Emulated Mode
//						//dispatcher=getDispacther4TransportLayer(routerInfoListLayers.get(i),  pathIPListLayers.get(i), pathIPListLayers.get(i-1).getLast(), pathIPListLayers.get(i+1).getFirst(),  n,  delete);
//						if (PMUtilities.numberOfNetworkElementsInLayer(routerInfoListLayers, "IP")==0){
//							if (i<pathIPListLayers.size()-1){
//								if (i==0){//Para el primer nodo
//									dispatcher=getDispacther4TransportLayerCOP(routerInfoListLayers.get(i),  pathList.get(i), pathList.get(i), pathList.get(i+1),  n,  delete);
//								} else {//Para los nodos intermedios
//									dispatcher=getDispacther4TransportLayerCOP(routerInfoListLayers.get(i),  pathList.get(i), pathList.get(i-1), pathList.get(i+1),  n,  delete);
//
//								}
//							} else {
//								//ultimo nodo
//								dispatcher=getDispacther4TransportLayerCOP(routerInfoListLayers.get(i),  pathList.get(i), pathList.get(i-1), pathList.get(i),  n,  delete);
//							}
//						} else {
//							log.info("The Node doesnt have IP layer");
//						}
//						dispatcherQueue.execute(dispatcher);//Executing Dispatcher
//						//routerInfoListLayers.remove(i);//Deleting Optical nodes configured
//						//pathIPListLayers.remove(i);
//
//						//Configuring IPLink in L3 Nodes (IP interfaces)
//						if(PMUtilities.numberOfNetworkElementsInLayer(routerInfoListLayers, "IP")==2){
//							long bw=PMUtilities.getBandwidth(nodetype);
//							Intf srcIntf = PMUtilities.getIntfFromLabel(n,routerInfoList.getFirst().getRouterID(), mytopology);//Fake
//							Intf destIntf = PMUtilities.getIntfFromLabel(n,routerInfoList.getLast().getRouterID(), mytopology);//Fake
//							Node srcNode = PMUtilities.getNodeByName(routerInfoListLayers.get(i-1).getLast().getRouterID(), this.mytopology);
//							Node destNode = PMUtilities.getNodeByName(routerInfoListLayers.get(i).getFirst().getRouterID(), this.mytopology);
//							log.info("SRC: NODE "+routerInfoListLayers.get(i-1).getLast().getRouterID());
//							log.info("DST: NODE "+routerInfoListLayers.get(i).getFirst().getRouterID());
//							if (srcNode==null || destNode==null){
//								log.info("Error. Nodes are not in Topology Module.");
//							}
//
//							Dispatcher dispatcherNodeSrc = new Dispatcher(new CreateIPLink((Inet4Address)Inet4Address.getByName(destNode.getAddress().get(0)), (Inet4Address)Inet4Address.getByName(destIntf.getAddress().get(1)), 
//									(Inet4Address)Inet4Address.getByName(srcNode.getAddress().get(0)), (Inet4Address)Inet4Address.getByName(srcIntf.getAddress().get(1)), 
//									srcNode.getConfigurationMode(),srcIntf.getName(), delete, bw));
//							dispatcherQueue.execute(dispatcherNodeSrc);
//
//							Dispatcher dispatcherNodeDst = new Dispatcher(new CreateIPLink((Inet4Address)Inet4Address.getByName(srcNode.getAddress().get(0)), (Inet4Address)Inet4Address.getByName(srcIntf.getAddress().get(1)), 
//									(Inet4Address)Inet4Address.getByName(destNode.getAddress().get(0)), (Inet4Address)Inet4Address.getByName(destIntf.getAddress().get(1)), 
//									destNode.getConfigurationMode(), destIntf.getName(), delete, bw));
//							dispatcherQueue.execute(dispatcherNodeDst);
//
//							//Dispatcher to Update the topology in Topology Module [via JSON] & PCE [via OSPF] manually
//							if ((PMUtilities.getLink(routerInfoList.get(0).getRouterID(), routerInfoList.get(routerInfoList.size()-1).getRouterID(), this.mytopology)==null)&&!delete){
//								UpdateIpLink uil= new UpdateIpLink(srcIntf.getName(), routerInfoList.get(0).getRouterID(), destIntf.getName(), routerInfoList.get(routerInfoList.size()-1).getRouterID(), mytopology, delete, bw);
//								Dispatcher tmdispatcher = new Dispatcher(uil);
//								tmdispatcher.start();
//							} else if ((PMUtilities.getLink(routerInfoList.get(0).getRouterID(), routerInfoList.get(routerInfoList.size()-1).getRouterID(),this.mytopology)!=null)&&delete){
//								UpdateIpLink uil= new UpdateIpLink(srcIntf.getName(), routerInfoList.get(0).getRouterID(), destIntf.getName(), routerInfoList.get(routerInfoList.size()-1).getRouterID(), mytopology, delete, bw);
//								Dispatcher tmdispatcher = new Dispatcher(uil);
//								tmdispatcher.start();
//							}
//						}
//					}
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		if ((PMUtilities.numberOfNetworkElementsInLayer(routerInfoListLayers, "IP")>2)||!layerzero) {
//			//We only take care of Juniper routers...
//
//			//this.mytopology= initializFromTM();
//			log.info("-----MPLS_Link Creation Mode-----");
//			log.info("Number of l3 routers: "+PMUtilities.numberOfNetworkElementsInLayer(routerInfoListLayers, "IP"));			
//			try{
//				LinkedList<String> path=new LinkedList<String>();
//				LinkedList<String> path2=new LinkedList<String>();
//				String nodetobeconfigured=pathIPListLayers.getFirst().getFirst().getHostAddress();
//				String nodetobeconfigured2=pathIPListLayers.getLast().getLast().getHostAddress();
//				String lastIF=null;
//				String firstIF=null;
//
//				generateBidirectionalPath(routerInfoListLayers, pathIPListLayers, path, path2,	lastIF, firstIF);
//
//				float bw=((BandwidthRequested) this.pcepInitiate.getPcepIntiatedLSPList().getFirst().getBandwidth()).getBw();
//
//				String lspName=routerInfoList.getFirst().getRouterID()+"_"+routerInfoList.getLast().getRouterID();
//				int id=search4LspId(lspName);
//				lspName=lspName+":"+id;
//
//				//printinfobeforeconfigure(nodetobeconfigured, nodetobeconfigured2, lspName, path, path2);
//
//				Dispatcher dispatcher = new Dispatcher(new CreateLSP(nodetobeconfigured,lastIF,lspName,path, bw));
//				dispatcher.start();
//				Dispatcher dispatcher2 = new Dispatcher(new CreateLSP(nodetobeconfigured2,firstIF,lspName,path2, bw));
//				dispatcher2.start();
//
//				//Saving the lsp 
//				if (!delete){
//					LSP lsp=new LSP(lspName, oPcounter.get()+1,routerInfoList.getFirst().getManagementAddress(), routerInfoList.getLast().getManagementAddress(), path, bw);
//					lsps.add(lsp);
//					addInfoRedis(lspName,routerInfoList.getFirst().getManagementAddress(), routerInfoList.getLast().getManagementAddress(), path, bw);
//				}
//				else {
//					LSP lspaux=getLspFromID((int)pcepInitiate.getPcepIntiatedLSPList().get(0).getLsp().getLspId());
//					pcepInitiate.getPcepIntiatedLSPList().get(0).setBandwidth(new BandwidthRequested());
//					((BandwidthRequested)pcepInitiate.getPcepIntiatedLSPList().get(0).getBandwidth()).setBw(lspaux.getBw());
//					if (lspaux==null)
//						log.info("LSP NOT FOUND");
//					else
//						delInfoRedis(lspaux.getId());
//				}
//				path.addFirst(routerInfoList.getFirst().getManagementAddress().getHostAddress());
//
//				informPCEandTM(path, ((BandwidthRequested)pcepInitiate.getPcepIntiatedLSPList().get(0).getBandwidth()), delete);
//
//
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//		}
//		if (pcepInitiate.getPcepIntiatedLSPList().get(0).getRsp().isrFlag())
//			eroIdMap.remove((int)pcepInitiate.getPcepIntiatedLSPList().get(0).getLsp().getLspId());
//		else
//			eroIdMap.put(oPcounter.incrementAndGet(), pcepInitiate.getPcepIntiatedLSPList().getFirst().getEro());
//	}
//
//	private void delInfoRedis(String id) {
//
//		log.info("Deleting LSP "+id+" in Redis DB");
//		Jedis jedis = new Jedis("photonics", 6379);
//		jedis.connect();
//
//		log.info("Deleting LSP:"+id+" with ID: "+(int)pcepInitiate.getPcepIntiatedLSPList().get(0).getLsp().getLspId());
//
//		jedis.del("LSP_"+id);
//		jedis.lrem("lsps", 0,"LSP_"+id);
//
//		jedis.disconnect();
//	}
//
//
//
//
//	private LSP getLspFromID(int lspId) {
//
//		Iterator<LSP> lspiter=lsps.iterator();
//		while (lspiter.hasNext()){
//			LSP next=lspiter.next();
//			if (next.getNumber()==lspId)
//				return next;
//		}	
//		return null;
//	}
//
//
//
//	/*
//	 * This function prints an lsp
//	 * It can be used in throwDispatcherV2, in LSP config part.
//	 */
//	private void printinfobeforeconfigure(String nodetobeconfigured,
//			String nodetobeconfigured2, String lspName,
//			LinkedList<String> path, LinkedList<String> path2) {
//
//		System.out.println("----------------------------");
//		System.out.println("Config Info: ");
//		System.out.println("-LSP: "+lspName);
//		System.out.println("-Source: "+nodetobeconfigured);
//		System.out.println("-Destination: "+nodetobeconfigured2);
//		System.out.println("-Path: ");
//		for (int i=0; i<path.size(); i++)
//			System.out.println("\t"+path.get(i));
//		System.out.println("-Inverse Path: ");
//		for (int i=0; i<path2.size(); i++)
//			System.out.println("\t"+path2.get(i));
//	}
//
//	private void addInfoRedis_optics(String lspName, Inet4Address managementAddress,
//			Inet4Address managementAddress2, LinkedList<String> path, float bw) {
//		// TODO Auto-generated method stub
//		String infoToSend="{\"log\":\"service Setup\",\"LSP_Id\":\"";
//		infoToSend=infoToSend+lspName+"\",\"data\":[";
//		Node node1=PMUtilities.getNodeByAddress(managementAddress,this.tmParams.getRoute(),this.mytopology);
//		infoToSend=infoToSend+"{\"routerID\":\""+node1.getNodeID()+"\"}";
//		for (int i=1; i<path.size(); i++){
//			infoToSend=infoToSend+",{\"routerID\":\""+path.get(i)+"\"}";
//		}
//		//Node node2=getNodeByAddress(managementAddress2);
//		//infoToSend=infoToSend+",{\"routerID\":\""+node2.getNodeID()+"\"}";
//		infoToSend=infoToSend+"],\"Bandwidth\":"+bw+"}";
//
//		Jedis jedis = new Jedis(params.getRedisIP(), 6379);
//		//Jedis jedis = new Jedis("photonics", 6379);
//		jedis.connect();
//
//		log.info("Adding LSP:"+lspName+" with ID: "+oPcounter.get()+1);
//
//		jedis.set("LSP_"+lspName, infoToSend);
//		jedis.rpush("lsps", "LSP_"+lspName);
//
//		jedis.disconnect();
//	}
//
//	private void addInfoRedis(String lspName, Inet4Address managementAddress,
//			Inet4Address managementAddress2, LinkedList<String> path, float bw) {
//		// TODO Auto-generated method stub
//		String infoToSend="{\"log\":\"service Setup\",\"LSP_Id\":\"";
//		infoToSend=infoToSend+lspName+"\",\"data\":[";
//		Node node1=PMUtilities.getNodeByAddress(managementAddress,this.tmParams.getRoute(),this.mytopology);
//		infoToSend=infoToSend+"{\"routerID\":\""+node1.getNodeID()+"\"}";
//		for (int i=0; i<path.size(); i++){
//			try {
//				Node nodeaux=PMUtilities.getNodeByIntfAddress((Inet4Address)Inet4Address.getByName(path.get(i)),this.tmParams.getRoute(),this.mytopology);
//				infoToSend=infoToSend+",{\"routerID\":\""+nodeaux.getNodeID()+"\"}";
//			} catch (UnknownHostException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		//Node node2=getNodeByAddress(managementAddress2);
//		//infoToSend=infoToSend+",{\"routerID\":\""+node2.getNodeID()+"\"}";
//		infoToSend=infoToSend+"],\"Bandwidth\":"+bw+"}";
//
//		Jedis jedis = new Jedis(params.getRedisIP(), 6379);
//		//Jedis jedis = new Jedis("photonics", 6379);
//		jedis.connect();
//
//		log.info("Adding LSP:"+lspName+" with ID: "+oPcounter.get()+1);
//
//		jedis.set("LSP_"+lspName, infoToSend);
//		jedis.rpush("lsps", "LSP_"+lspName);
//
//		jedis.disconnect();
//	}
//
//	private int search4LspId(String lspName) {
//		Iterator<LSP> lspiter=lsps.iterator();
//		int id=0;
//		while (lspiter.hasNext()){
//			LSP lsp=lspiter.next();
//			//log.info("Comparando: "+lsp.getId()+" y "+lspName);
//			if(lsp.getId().contains(lspName))
//				id++;
//		}
//		return id;
//	}
//
//	private void informPCEandTM(LinkedList<String> path, BandwidthRequested bandwidth, boolean delete) {
//		try{
//			for (int i=0; i+1<path.size(); i++){
//				//First of all, we send a OSPF message to PCE
//				Node srcNode=PMUtilities.getNodeByAddress((Inet4Address)Inet4Address.getByName(path.get(i)),this.tmParams.getRoute(),this.mytopology);
//				if (srcNode==null)
//					srcNode=PMUtilities.getNodeByIntfAddress((Inet4Address)Inet4Address.getByName(path.get(i)),this.tmParams.getRoute(),this.mytopology);
//				if (srcNode==null)
//					log.info("ERROR, NO ENCUENTRA EL NODO: "+path.get(i));
//				Node dstNode=PMUtilities.getNodeByAddress((Inet4Address)Inet4Address.getByName(path.get(i+1)),this.tmParams.getRoute(),this.mytopology);
//				if (dstNode==null)
//					dstNode=PMUtilities.getNodeByIntfAddress((Inet4Address)Inet4Address.getByName(path.get(i+1)),this.tmParams.getRoute(),this.mytopology);
//				if (dstNode==null)
//					log.info("ERROR, NO ENCUENTRA EL NODO: "+path.get(i+1));
//				Link link=PMUtilities.getLink(srcNode.getNodeID(), dstNode.getNodeID(), this.mytopology);
//
//				if (link==null){
//					log.info("link a null");
//				}
//				if (link.getBandwidth()==null){
//					log.info("Bandwidth del link a null");
//				}
//				if (bandwidth==null){
//					log.info("Bandwidth de la peticion a null");  
//				}
//
//				log.info("Informamos al PCE"+(float)link.getBandwidth().getMaxBandwidth()+" "+(long)link.getBandwidth().getUnreservedBw()+bandwidth.getBw());
//				//Finally, we update topology information to TM
//				CommsToPCE sendMsgPCE=new CommsToPCE(4189, "localhost"); //FIXME: Pasar por xml y guardar en params...
//				if (delete){
//					sendMsgPCE.updateLink((Inet4Address)Inet4Address.getByName(srcNode.getAddress().get(0)), (Inet4Address)Inet4Address.getByName(dstNode.getAddress().get(0)), (float)link.getBandwidth().getMaxBandwidth(),(long)link.getBandwidth().getUnreservedBw()+bandwidth.getBw());
//					try{
//						Thread.currentThread().sleep(1000);
//					}catch (Exception e){
//						e.printStackTrace();
//					}
//					sendMsgPCE.updateLink((Inet4Address)Inet4Address.getByName(dstNode.getAddress().get(0)), (Inet4Address)Inet4Address.getByName(srcNode.getAddress().get(0)), (float)link.getBandwidth().getMaxBandwidth(),(long)link.getBandwidth().getUnreservedBw()+bandwidth.getBw());
//				}
//				else{
//					sendMsgPCE.updateLink((Inet4Address)Inet4Address.getByName(srcNode.getAddress().get(0)), (Inet4Address)Inet4Address.getByName(dstNode.getAddress().get(0)), (float)link.getBandwidth().getMaxBandwidth(),(long)link.getBandwidth().getUnreservedBw()-bandwidth.getBw());
//					try{
//						Thread.currentThread().sleep(1000);
//					}catch (Exception e){
//						e.printStackTrace();
//					}
//					sendMsgPCE.updateLink((Inet4Address)Inet4Address.getByName(dstNode.getAddress().get(0)), (Inet4Address)Inet4Address.getByName(srcNode.getAddress().get(0)), (float)link.getBandwidth().getMaxBandwidth(),(long)link.getBandwidth().getUnreservedBw()-bandwidth.getBw());
//				}
//				log.info("Informamos al TM");
//				CommsToTM sendMsgTM=new CommsToTM(params.getTopologyModulePort(), params.getTopologyModuleAddress());
//				if (delete)
//					sendMsgTM.updateLink(link, (long)link.getBandwidth().getMaxBandwidth(), (long)link.getBandwidth().getUnreservedBw()+(long)bandwidth.getBw(), false);
//				else 
//					sendMsgTM.updateLink(link, (long)link.getBandwidth().getMaxBandwidth(), (long)link.getBandwidth().getUnreservedBw()-(long)bandwidth.getBw(), false);
//			}
//		} catch(Exception e){
//			e.printStackTrace();
//		}
//	}

	private static String readMsg(DataInputStream in) throws IOException{
		byte[] hdr = new byte[4];
		byte[] temp = null;
		boolean endHdr = false;
		int r = 0;
		int length = 0;
		boolean endMsg = false;
		int offsetHdr = 0;            	
		int offset = -1;
		while (!endMsg) {
			try {
				if (endHdr) { 
					r = in.read(temp, offset, 1);
					if (r == -1){
						return null;
					}
				}
				else {		
					if (hdr != null){
						r = in.read(hdr, offsetHdr, 1);
						if (r == -1){
							return null;
						}
					}
				}
			}catch (IOException e){
				//System.out.println("Salgo por excepcion");
				throw e;
			}catch (Exception e) {		
				throw new IOException();
			}
			if (r > 0) {
				if (offsetHdr == 2) {
					length = ((int)hdr[offsetHdr]&0xFF) << 8;
				}
				if (offsetHdr == 3) {
					length = length | (((int)hdr[offsetHdr]&0xFF));                					
					temp = new byte[length];
					endHdr = true;
					offsetHdr++;
				}
				if ((length > 0) && (offset == length - 1)) {
					endMsg = true;
				}
				if (endHdr){                				
					offset++;
				}
				else {
					offsetHdr++;
				}
			}
			else if (r==-1){
				//log.warning("End of stream has been reached");
				throw new IOException();
			}
		}
		if (length > 0) {                			
			String response = new String(temp);
			return response;	 //Respuesta sin la cabecera               			
		}	
		else return null;

	}

	private void sendReport(){		
		log.info("Sending Report to ABNO or VNTM");
//		try{
//			Thread.currentThread().sleep(6000);
//		}catch (Exception e){
//			e.printStackTrace();
//		}
		PCEPReport rpt= new PCEPReport();
		rpt.setStateReportList(new LinkedList<StateReport>());
		rpt.getStateReportList().add(new StateReport());
		rpt.getStateReportList().get(0).setLSP(pcepInitiate.getPcepIntiatedLSPList().get(0).getLsp());
		rpt.getStateReportList().get(0).setSRP(pcepInitiate.getPcepIntiatedLSPList().get(0).getRsp());
		rpt.getStateReportList().get(0).getLSP().setLspId(oPcounter.get());
		rpt.getStateReportList().get(0).setPath(new Path());
		rpt.getStateReportList().get(0).getPath().seteRO(pcepInitiate.getPcepIntiatedLSPList().get(0).getEro());

		try {
			rpt.encode();
		} catch (PCEPProtocolViolationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (out== null)
			System.out.println("No se crea bien el out");
		else{
			try {
				this.out.write(rpt.getBytes());
				this.out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

//	private void sendReportNoInfo()
//	{
//		PCEPReport rpt= new PCEPReport();
//
//		try 
//		{
//			rpt.encode();
//		} catch (PCEPProtocolViolationException e) 
//		{
//			log.info(UtilsFunctions.exceptionToString(e));
//		}
//		if (out== null)
//		{
//			log.info("No se crea bien el out");
//		}
//		else
//		{
//			try 
//			{
//				log.info("Enviamos Report hacia atras :"+rpt.getBytes());
//				this.out.write(rpt.getBytes());
//				this.out.flush();
//			} 
//			catch (IOException e) 
//			{
//				log.info(UtilsFunctions.exceptionToString(e));
//			}
//		}
//	}

}
