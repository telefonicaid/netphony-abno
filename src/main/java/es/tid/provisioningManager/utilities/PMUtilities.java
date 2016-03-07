package es.tid.provisioningManager.utilities;

import java.net.Inet4Address;
import java.util.Iterator;
import java.util.LinkedList;

import es.tid.of.DataPathID;
import es.tid.pce.pcep.messages.PCEPInitiate;
import es.tid.pce.pcep.objects.EndPointsIPv4;
import es.tid.provisioningManager.objects.RouterInfoPM;
import es.tid.provisioningManager.objects.Topology;
import es.tid.rsvp.objects.subobjects.EROSubobject;
import es.tid.rsvp.objects.subobjects.IPv4prefixEROSubobject;
import es.tid.rsvp.objects.subobjects.UnnumberIfIDEROSubobject;
import es.tid.tedb.elements.Intf;
import es.tid.tedb.elements.Link;
import es.tid.tedb.elements.Node;

public class PMUtilities {

	public static void divideByLayers(LinkedList<Inet4Address> pathIPList,
			LinkedList<RouterInfoPM> routerInfoList, Topology mytopology2,
			LinkedList<LinkedList<Inet4Address>> pathIPListLayers,
			LinkedList<LinkedList<RouterInfoPM>> routerInfoListLayers) {
		Iterator<Inet4Address> inetiter= pathIPList.iterator();
		Iterator<RouterInfoPM> infoiter=routerInfoList.iterator();

		boolean nogetnext=false;
		Inet4Address auxIP2=null;
		RouterInfoPM auxinfo2=null;
		Inet4Address auxIP=null;
		RouterInfoPM auxinfo=null;

		while (inetiter.hasNext()){
			if(nogetnext) {
				auxIP=auxIP2;
				auxinfo=auxinfo2;
			}else{
				auxIP=inetiter.next();
				auxinfo=infoiter.next();
			}
			nogetnext=false;
			String configMode=auxinfo.getConfigurationMode();
			if (configMode==null)
				System.out.println("Error a mirar en el nodo: "+auxinfo.getRouterID());
			if(configMode.equals("PCEP")||configMode.equals("OF")|| configMode.equals("RestAPI")){
				auxIP2=inetiter.next();
				auxinfo2=infoiter.next();
				LinkedList<Inet4Address> auxIPList=new LinkedList<Inet4Address>();
				LinkedList<RouterInfoPM> auxInfoList=new LinkedList<RouterInfoPM>();
				auxIPList.add(auxIP);
				auxInfoList.add(auxinfo);
				boolean finish=false;
				while ((auxinfo2.getLayer().equals(auxinfo.getLayer()))&&(auxinfo2.getConfigurationMode().equals(configMode))&&(finish==false)){
					//Tenemos que contemplar el caso de PCEP de capa tres con IPLink entre un router y otro
					//¿Usar un Objeto Exclude con los ya configurados? Ver con OJO...
					auxIPList.add(auxIP2);
					auxInfoList.add(auxinfo2);
					if (infoiter.hasNext()){
						auxIP2=inetiter.next();
						auxinfo2=infoiter.next();
					} else {
						finish=true;
					}
				}
				if (finish==false){
					nogetnext=true;
				}
				pathIPListLayers.add(auxIPList);
				routerInfoListLayers.add(auxInfoList);
			} else {
				LinkedList<Inet4Address> auxIPList=new LinkedList<Inet4Address>();
				LinkedList<RouterInfoPM> auxInfoList=new LinkedList<RouterInfoPM>();
				auxIPList.add(auxIP);
				auxInfoList.add(auxinfo);
				pathIPListLayers.add(auxIPList);
				routerInfoListLayers.add(auxInfoList);
			}

		} if (nogetnext) {
			LinkedList<Inet4Address> auxIPList=new LinkedList<Inet4Address>();
			LinkedList<RouterInfoPM> auxInfoList=new LinkedList<RouterInfoPM>();
			auxIPList.add(auxIP2);
			auxInfoList.add(auxinfo2);
			pathIPListLayers.add(auxIPList);
			routerInfoListLayers.add(auxInfoList);

		}

	}


	public static Intf getIntfFromLabel(int n, String routerID, Topology mytopology2) {
		Node node=getNodeByName(routerID, mytopology2);
		Iterator intfList=node.getIntfList().iterator();
		while (intfList.hasNext()) {
			Intf intf=(Intf) intfList.next();
			System.out.println("Comparamos label de interfaz: "+intf.getLabel()+" y del path: "+n);
			if (intf.getLabel()==n){
				System.out.println("Lo hemos encontrado!!");
				return intf;
			}
		}
		return null;
	}



	public boolean isThereAnOpticalRouterHere(LinkedList<Inet4Address> pathIPList, String route, Topology mytopology){
		Iterator<Inet4Address> pathiter=pathIPList.iterator();
		while(pathiter.hasNext()){
			Inet4Address ip=pathiter.next();
			Node node= getNodeByAddress(ip, route, mytopology);
			if (node.getLayer().equals("transport"))
				return true;
		}
		return false;
	}


	public static Link getLink(String src, String dst, Topology mytopology){
		Iterator<Link> linkiter=mytopology.getLinkList().iterator();
		while (linkiter.hasNext()){
			Link link=linkiter.next();
			System.out.println("Comparando: "+src+" "+dst+" Con: "+link.getSource().getNode()+" "+link.getDest().getNode());
			if (link.getSource().getNode().equals(src)&&link.getDest().getNode().equals(dst)){
				return link;
			}else if (link.getSource().getNode().equals(dst)&&link.getDest().getNode().equals(src)){
				return link;
			}
		}
		return null;
	}


	public static Intf getIntfByName(Node node, String intf) {
		Iterator intfiter = node.getIntfList().iterator();
		while (intfiter.hasNext()) {
			Intf intface=(Intf) intfiter.next();
			if (intface.getName().equals(intf))
				return intface;
		}
		System.out.println("Error not interface found");
		return null;
	}


	public static Node getNodeByName(String routerID, Topology mytopology) {
		Iterator nodeiter = mytopology.getNodeList().iterator();
		while (nodeiter.hasNext()){
			Node node=(Node) nodeiter.next();
			if (node.getNodeID().equals(routerID))
				return node;
		}
		return null;
	}

//	public static void initializeLabelFire(Topology grafo){
//		Iterator nodes=grafo.getNodeList().iterator();
//		while (nodes.hasNext()){
//			Node node=(Node)nodes.next();
//			if (node.getLayer().equals("IP")){
//				Iterator intf=node.getIntfList().iterator();
//				while (intf.hasNext()){
//					Intf intface=(Intf)intf.next();
//					setInterfaceLabel(intface, node);
//				}
//			}
//		}
//	}
//
//	public static void setInterfaceLabel(Intf intf, Node node){
//		if (intf.getName().equals("MX240-3_ge-2/1/8")){
//			intf.setLabel(6);
//		}
//		else if (intf.getName().equals("MX240-3_ge-2/1/9")){
//			intf.setLabel(4);
//		}
//		else if (intf.getName().equals("MX240-2_ge-2/1/8")){
//			intf.setLabel(7);
//		}
//		else if (intf.getName().equals("MX240-2_ge-2/1/9")){
//			intf.setLabel(6);
//		}
//		else if (intf.getName().equals("MX240-1_ge-2/1/8")){
//			intf.setLabel(4);
//		}
//		else if (intf.getName().equals("MX240-1_ge-2/1/9")){
//			intf.setLabel(7);
//		}		
//		else if (intf.getName().equals("MX240-1_xe-2/2/0")){
//			intf.setLabel(4);
//		}		
//		else if (intf.getName().equals("MX240-1_xe-2/3/0")){
//			intf.setLabel(7);
//		}		
//		else if (intf.getName().equals("MX240-2_xe-2/2/0")){
//			intf.setLabel(7);
//		}		
//		else if (intf.getName().equals("MX240-2_xe-2/3/0")){
//			intf.setLabel(6);
//		}		
//		else if (intf.getName().equals("MX240-3_xe-2/2/0")){
//			intf.setLabel(6);
//		}		
//		else if (intf.getName().equals("MX240-3_xe-2/3/0")){
//			intf.setLabel(4);
//		}
//		else
//			intf.setLabel(9);
//	}
	
	public static long getBandwidth(String opticalMode) {
		if (opticalMode.equals("Adva")){
			return 1000;
		} else if (opticalMode.equals("Infinera")) {
			return 10000;
		}		
		return 0;
	}


	public static void printIPsquare(
			LinkedList<LinkedList<Inet4Address>> pathIPListLayers) {
		for(int i=0; i<pathIPListLayers.size(); i++){
			for (int j=0; j<pathIPListLayers.get(i).size(); j++){
				System.out.println("Layer: "+i+" IP: "+pathIPListLayers.get(i).get(j));
			}
		}
	}

	
	public static String getIntfNameFromLabel(Inet4Address otherip, Inet4Address targetip, Topology mytopology, int n) {
		Iterator<Link> linkiter=mytopology.getLinkList().iterator();
		Node othernode = getNodeByAddress(otherip, null, mytopology);
		String othername=othernode.getNodeID();
		System.out.println("************** getIntfNameFromLabel ******************");
		System.out.println("targetip: "+targetip+" otherip: "+otherip+" n: "+n);
		String targetname=getNodeByAddress(targetip, null, mytopology).getNodeID();
		while (linkiter.hasNext()){
			Link link=linkiter.next();
			//System.out.println("*************************************************************");
			//System.out.println("Checking: \t"+link.getDest().getNode()+"-"+link.getSource().getNode());
			//System.out.println("And: \t"+othername+"-"+targetname);
			if ((othername.equals(link.getSource().getNode()))&&(targetname.equals(link.getDest().getNode()))){
				Intf intf=getIntfByName(othernode, link.getSource().getIntf());
				//System.out.println("LABEL: "+n+" "+intf.getLabel());
				if (n==intf.getLabel()){
					System.out.println("Chosen Interface: "+link.getDest().getIntf());
					return link.getDest().getIntf();
				}
			}else if ((targetname.equals(link.getSource().getNode()))&&(othername.equals(link.getDest().getNode()))){
				Intf intf=getIntfByName(othernode, link.getDest().getIntf());
				//System.out.println("LABEL: "+n+" "+intf.getLabel());
				if (n==intf.getLabel()){
					System.out.println("Chosen Interface: "+link.getSource().getIntf());
					return link.getSource().getIntf();
				}
			}
		}
		
		System.out.println("******** Error no interface found **********");
		return null;
	}

	public static String getIntfNameFromLabelDataPathID(Inet4Address otherip, Inet4Address targetip, Topology mytopology, int n) {
		Iterator<Link> linkiter=mytopology.getLinkList().iterator();
		Node othernode = getNodeByAddress(otherip, null, mytopology);
		System.out.println("othernode: "+othernode);
		String othername=othernode.getNodeID();
		System.out.println("************** getIntfNameFromLabel DataPatID ******************");
		System.out.println("targetip: "+targetip+" otherip: "+otherip+" n: "+n);
		String targetname=getNodeByAddress(targetip, null, mytopology).getNodeID();
		while (linkiter.hasNext()){
			Link link=linkiter.next();
			//System.out.println("*************************************************************");
			//System.out.println("Checking: \t"+link.getDest().getNode()+"-"+link.getSource().getNode());
			//System.out.println("And: \t"+othername+"-"+targetname);
			if ((othername.equals(link.getSource().getNode()))&&(targetname.equals(link.getDest().getNode()))){
				Intf intf=getIntfByName(othernode, link.getSource().getIntf());
				//System.out.println("LABEL: "+n+" "+intf.getLabel());
				if (n==intf.getLabel()){
					System.out.println("Chosen Interface: "+link.getDest().getIntf());
					return link.getDest().getIntf();
				}
			}else if ((targetname.equals(link.getSource().getNode()))&&(othername.equals(link.getDest().getNode()))){
				Intf intf=getIntfByName(othernode, link.getDest().getIntf());
				//System.out.println("LABEL: "+n+" "+intf.getLabel());
				if (n==intf.getLabel()){
					System.out.println("Chosen Interface: "+link.getSource().getIntf());
					return link.getSource().getIntf();
				}
			}
		}
		
		System.out.println("******** Error no interface found **********");
		return null;
	}
	
	public static int getIntfFromLabelInfinera(int n, Inet4Address ip) {
		if ((n==5)&&(ip.getHostAddress().equals("172.16.0.1"))){
			return 20;
		}
		else if ((n==8)&&(ip.getHostAddress().equals("172.16.0.1"))){
			return 10;
		}
		else if ((n==8)&&(ip.getHostAddress().equals("172.16.0.2"))){
			return 10;
		}
		else if ((n==7)&&(ip.getHostAddress().equals("172.16.0.2"))){
			return 20;
		}
		else if ((n==7)&&(ip.getHostAddress().equals("172.16.0.3"))){
			return 10;
		}
		else if ((n==5)&&(ip.getHostAddress().equals("172.16.0.3"))){
			return 20;
		}
		return 0;
	}

	public static int getIntfFromLabelAdva(int n, Inet4Address ip) {
		if ((n==5)&&(ip.getHostAddress().equals("172.16.1.40"))){
			return 27;
		}
		else if ((n==8)&&(ip.getHostAddress().equals("172.16.1.40"))){
			return 25;
		}
		else if ((n==8)&&(ip.getHostAddress().equals("172.16.1.38"))){
			return 21;
		}
		else if ((n==7)&&(ip.getHostAddress().equals("172.16.1.38"))){
			return 17;
		}
		else if ((n==7)&&(ip.getHostAddress().equals("172.16.1.34"))){
			return 3;
		}
		else if ((n==5)&&(ip.getHostAddress().equals("172.16.1.34"))){
			return 1;
		}
		return 0;
	}


	public static int numberOfNodes(LinkedList<LinkedList<RouterInfoPM>> routerInfoListLayers) {
		int counter=0;
		for (int i=0; i<routerInfoListLayers.size(); i++){
			counter+=routerInfoListLayers.get(i).size();
		}		
		return counter;
	}

	public static int numberOfNetworkElementsInLayer(LinkedList<LinkedList<RouterInfoPM>> routerInfoListLayers, String layerInfo) {
		int counter=0;
		for (int i=0; i<routerInfoListLayers.size(); i++){
			if (routerInfoListLayers.get(i).get(0).getLayer().equals(layerInfo)){
				counter+=routerInfoListLayers.get(i).size();
			}
		}		
		return counter;
	}
	

	public static Intf getOppositeInterfaceByUnnumber(Inet4Address ip, PCEPInitiate pcepInitiate) {
		// TODO Auto-generated method stub
		Iterator<EROSubobject> iterSO=pcepInitiate.getPcepIntiatedLSPList().get(0).getEro().getEROSubobjectList().iterator();
		while (iterSO.hasNext()){
			EROSubobject eso=iterSO.next();
			if (eso instanceof UnnumberIfIDEROSubobject){
				if (((UnnumberIfIDEROSubobject)eso).getRouterID().equals(ip)){
					//Tenemos el SubobjectoEro, buscamos la interfaz
					//Intf auxintf=getIntfByUnnumber(((UnnumberIfIDEROSubobject)eso).get); //nueva funcion que busca interfaz de un unnumber
					//
					//return getOppositeInterface(auxintf); //Nueva funcion que busca interfaz opuesta
				}
			}
		}
		return null;
	}


	public static void removeFromEro(PCEPInitiate pcepInit, Inet4Address src, Inet4Address dest) {
		boolean delete=true;
		for (int i =0; i<pcepInit.getPcepIntiatedLSPList().get(0).getEro().getEROSubobjectList().size(); i++){
			Inet4Address ip = getIPfromEroSubObj(pcepInit.getPcepIntiatedLSPList().get(0).getEro().getEROSubobjectList().get(i));
			if ((ip==null)&&(delete==true)) {
				pcepInit.getPcepIntiatedLSPList().get(0).getEro().getEROSubobjectList().remove(i);
				i--;
			}else if ((ip==null)&&(delete==false)){
				//do nothing
			}
			else if ((ip.equals(src))&&(delete=true)){
				delete=false;
			} else if ((ip.equals(dest))&&(delete==false)) {
				delete=true;
			} else if (delete==true){
				pcepInit.getPcepIntiatedLSPList().get(0).getEro().getEROSubobjectList().remove(i);
				i--;
			}		
		}
		((EndPointsIPv4)pcepInit.getPcepIntiatedLSPList().get(0).getEndPoint()).setDestIP(getIPfromEroSubObj(pcepInit.getPcepIntiatedLSPList().get(0).getEro().getEROSubobjectList().getLast()));
		((EndPointsIPv4)pcepInit.getPcepIntiatedLSPList().get(0).getEndPoint()).setSourceIP(getIPfromEroSubObj(pcepInit.getPcepIntiatedLSPList().get(0).getEro().getEROSubobjectList().getFirst()));
	}
	
	public static Inet4Address getIPfromEroSubObj(EROSubobject eroSubobject) {
		if (eroSubobject instanceof IPv4prefixEROSubobject){
			return ((IPv4prefixEROSubobject)eroSubobject).getIpv4address();
		} else if (eroSubobject instanceof UnnumberIfIDEROSubobject){
			return ((UnnumberIfIDEROSubobject)eroSubobject).getRouterID();
		}
		return null;
	}
	
	
	public static Intf getIntfByAddress(Inet4Address interfaceIP, String route, Topology mytopology) {
		//FIXME: Warning!!!! this.tmParams.getRoute() 
		
		Intf intfResponse = getIntfByAddress(route,interfaceIP.getHostAddress(), mytopology);
		//System.out.println("intfResponse " + intfResponse);
		//System.out.println("Intf " + intf.getName());
		return intfResponse;

	}

	public static Intf getIntfByAddress(String route, String hostAddress, Topology mytopology) {
		Iterator nodeiter = mytopology.getNodeList().iterator();
		while (nodeiter.hasNext()){
			Node node=(Node) nodeiter.next();
			Iterator intfiter = node.getIntfList().iterator();
			while (intfiter.hasNext()) {
				Intf intf=(Intf) intfiter.next();
				System.out.println("Comparando: "+intf.getAddress().get(0).toString()+" y "+hostAddress);
				if (intf.getAddress().get(0).equals(hostAddress))
					return intf;
			}

		}
		return null;
	}

	public static Node getNodeByAddress(Inet4Address managementAddress, String route, Topology mytopology) {
		//FIXME: Warning!!! this.tmParams.getRoute()
		Node nodeResponse = getNodeByAddress(route,managementAddress.getHostAddress(), mytopology);
		System.out.println("Node " + nodeResponse);
		//System.out.println("Node " + node.getNodeID());
		return nodeResponse;
	}
	
	public static Node getNodeByAddressDataPathID(Inet4Address managementAddress, String route, Topology mytopology) {
		//FIXME: Warning!!! this.tmParams.getRoute()
		Node nodeResponse = getNodeByAddress(route,managementAddress.getHostAddress(), mytopology);
		System.out.println("Node " + nodeResponse);
		//System.out.println("Node " + node.getNodeID());
		return nodeResponse;
	}

	public static Node getNodeByAddress(String route, String hostAddress, Topology mytopology) {
		Iterator nodeiter = mytopology.getNodeList().iterator();
		while (nodeiter.hasNext()){
			Node node=(Node) nodeiter.next();
			for(int i=0; i<node.getAddress().size();i++){
				if (node.getAddress().get(i).equals(hostAddress))
					return node;
			}
		}
		return null;
	}
	
	public static Node getNodeByIntfAddress(Inet4Address interfaceIP, String route, Topology mytopology) {
		//We should call getNodeByName (TM de Carlos) and modify this function to support this kind of Request
		//FIXME: Warning!! this.tmParams.getRoute()
		Node nodeResponse = getNodeByIntfAddress(route,interfaceIP.getHostAddress(), mytopology);
		return nodeResponse;

	}

	public static Node getNodeByIntfAddress(String route, String hostAddress, Topology mytopology) {
		Iterator nodeiter = mytopology.getNodeList().iterator();
		while (nodeiter.hasNext()){
			Node node=(Node) nodeiter.next();
			Iterator intfiter = node.getIntfList().iterator();
			System.out.println("Search in the node : "+node.getNodeID());
			while (intfiter.hasNext()) {
				Intf intf=(Intf) intfiter.next();
				for (int i=0; i<intf.getAddress().size(); i++){
					if (intf.getAddress().get(i).equals(hostAddress))
						return node;
				}
			}
			//si hay problemas con la comparación, miramos las ips de los nodos...
			for (int j=0; j<node.getAddress().size(); j++)
				if(node.getAddress().get(j).equals(hostAddress))
					return node;

		}

		return null;
	}


	public static Node getNodeByIntfAddress(DataPathID dataPathID, String route, Topology mytopology) {
		//We should call getNodeByName (TM de Carlos) and modify this function to support this kind of Request
		System.out.println("Entro en getNodeByIntfAddress DataPathID");
		Node nodeResponse = getNodeByIntfAddress(route, dataPathID.getDataPathID(), mytopology);
		return nodeResponse;
	}
	
}
