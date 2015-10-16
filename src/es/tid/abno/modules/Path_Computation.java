package es.tid.abno.modules;

import java.net.Inet4Address;
import java.util.LinkedList;
import java.util.logging.Logger;
import es.tid.of.DataPathID;
import es.tid.pce.client.ClientRequestManager;
import es.tid.pce.client.PCCPCEPSession;
import es.tid.pce.pcep.constructs.EndPoint;
import es.tid.pce.pcep.constructs.GeneralizedBandwidthSSON;
import es.tid.pce.pcep.constructs.P2PEndpoints;
import es.tid.pce.pcep.constructs.Request;
import es.tid.pce.pcep.constructs.SVECConstruct;
import es.tid.pce.pcep.messages.PCEPRequest;
import es.tid.pce.pcep.messages.PCEPResponse;
import es.tid.pce.pcep.objects.BandwidthRequested;
import es.tid.pce.pcep.objects.BandwidthRequestedGeneralizedBandwidth;
import es.tid.pce.pcep.objects.EndPointsIPv4;
import es.tid.pce.pcep.objects.EndPointsUnnumberedIntf;
import es.tid.pce.pcep.objects.ExcludeRouteObject;
import es.tid.pce.pcep.objects.GeneralizedEndPoints;
import es.tid.pce.pcep.objects.InterLayer;
import es.tid.pce.pcep.objects.ObjectiveFunction;
import es.tid.pce.pcep.objects.RequestParameters;
import es.tid.pce.pcep.objects.subobjects.DataPathIDXROSubobject;
import es.tid.pce.pcep.objects.subobjects.UnnumberIfIDXROSubobject;
import es.tid.pce.pcep.objects.subobjects.XROSubobject;
import es.tid.pce.pcep.objects.tlvs.EndPointDataPathTLV;
import es.tid.pce.pcep.objects.tlvs.EndPointIPv4TLV;
import es.tid.pce.pcep.objects.tlvs.EndPointUnnumberedDataPathTLV;
import es.tid.pce.pcep.objects.tlvs.UnnumberedEndpointTLV;
import es.tid.pce.pcepsession.PCEPSessionsInformation;
import es.tid.util.UtilsFunctions;



public class Path_Computation extends Thread
{
	private static String DEFAULT_MAC = "00:00:00:00:00:00";
	private static PCCPCEPSession PCEsession;
	private PCEPSessionsInformation pcepSessionManager;
	ClientRequestManager crm;
	private int ofCode;
	private Logger log;

	public Path_Computation(PCEParameters pceParams)
	{
		this.pcepSessionManager = new PCEPSessionsInformation();
		PCEsession = new PCCPCEPSession(pceParams.getpCEAddress(), pceParams.getpCEPort(), false, this.pcepSessionManager);
		PCEsession.start();
		this.crm = PCEsession.crm;
		this.ofCode = pceParams.getOfCode();
		this.log = Logger.getLogger("PCEP listener");
	}
	
	/**
	* @author b.jmgj
	*/
	//Generic PCEPResponse calculatePath (checked to DataPathID)

	public PCEPResponse calculateMediaChannelPath(String SourceString, String DestString, int srcIntf, int dstIntf, int m, int OFCode)
	{
		this.log.info("**  PCE  **");
		this.log.info("Calculating cost between " + SourceString + " and " + DestString + " src port "+srcIntf+ " dst port "+dstIntf+ " m "+m+ " OFCode "+OFCode);
		try {
			// Creating PCEP Request
			PCEPRequest pReq = new PCEPRequest();
			/*LinkedList svecList = new LinkedList();
			pReq.setSvecList(svecList);*/

			Request req = new Request();
			// Creating Request Parameters
			RequestParameters reqParams = new RequestParameters();
			reqParams.setBidirect(true);
			reqParams.setPrio(1);
			//reqParams.setRequestID(1L);
			reqParams.setRequestID(PCCPCEPSession.getNewReqIDCounter());

			req.setRequestParameters(reqParams);
			
			BandwidthRequestedGeneralizedBandwidth gb= new BandwidthRequestedGeneralizedBandwidth();
			
			// Adding Bandwidth to the request
			GeneralizedBandwidthSSON bandwidth=new GeneralizedBandwidthSSON();
			bandwidth.setM(m);
			gb.setGeneralizedBandwidth(bandwidth);
			
			req.setBandwidth(gb);	

			// Adding GeneralizedEndPoints
			GeneralizedEndPoints endP = new GeneralizedEndPoints();
			
			EndPoint sourceEP=new EndPoint();
			EndPoint destEP=new EndPoint();

			//Check if Nodes ID are IP or DataPathId
			
			//String example1 = "192.168.20.1";
			//String example2 = "00:00:00:05:00:00:00:08";
			String pattern_datapathid = "([0-9]{2}:){7}([0-9]{2})";
			String pattern_IPv4 = "([0-9]{1,3}\\.){3}([0-9]{1,3})";
		    
			if (SourceString.matches(pattern_IPv4) && DestString.matches(pattern_IPv4)){
		    	System.out.println("jm Es IP match");

				Inet4Address ipSource = (Inet4Address) Inet4Address.getByName(SourceString);
				Inet4Address ipDest = (Inet4Address) Inet4Address.getByName(DestString);
				
				// Check if there are Interfaces or not
				if (srcIntf>0 && dstIntf>0 ){
					// There are Interfaces in the Request
					log.info("THERE ARE INTERFACES IN THE REQUEST");
					UnnumberedEndpointTLV sourceUnnumberedEndpointTLV = new UnnumberedEndpointTLV();
					UnnumberedEndpointTLV destUnnumberedEndpointTLV = new UnnumberedEndpointTLV();
					
					sourceUnnumberedEndpointTLV.setIPv4address(ipSource);
					sourceUnnumberedEndpointTLV.setIfID(srcIntf);
					
					destUnnumberedEndpointTLV.setIPv4address(ipDest);
					destUnnumberedEndpointTLV.setIfID(dstIntf);
					
					sourceEP.setUnnumberedEndpoint(sourceUnnumberedEndpointTLV);
					destEP.setUnnumberedEndpoint(destUnnumberedEndpointTLV);	
					
				} else {
					// There are no Interfaces in the Request
					log.info("THERE ARE NO INTERFACES IN THE REQUEST");
					EndPointIPv4TLV sourceEndPointIPv4TLV = new EndPointIPv4TLV();
					EndPointIPv4TLV destEndPointIPv4TLV = new EndPointIPv4TLV();
					
					sourceEndPointIPv4TLV.setIPv4address(ipSource);
					destEndPointIPv4TLV.setIPv4address(ipDest);
					
					sourceEP.setEndPointIPv4TLV(sourceEndPointIPv4TLV);
					destEP.setEndPointIPv4TLV(destEndPointIPv4TLV);	
					
				}		
				
			}
		    else if (SourceString.matches(pattern_datapathid) && DestString.matches(pattern_datapathid)){
		    	System.out.println("jm Es una DPID Match");
		    	
		    	// Check if there are Interfaces or not
				if (srcIntf>0 && dstIntf>0 ){
					// There are Interfaces in the Request
					EndPointUnnumberedDataPathTLV sourceUnnumberedDataPathTLV = new EndPointUnnumberedDataPathTLV();
					EndPointUnnumberedDataPathTLV destUnnumberedDataPathTLV = new EndPointUnnumberedDataPathTLV();
					
					sourceUnnumberedDataPathTLV.setSwitchID(SourceString);
					sourceUnnumberedDataPathTLV.setIfID(srcIntf);
					destUnnumberedDataPathTLV.setSwitchID(DestString);
					destUnnumberedDataPathTLV.setIfID(dstIntf);
		
					sourceEP.setEndPointUnnumberedDataPathTLV(sourceUnnumberedDataPathTLV);
					destEP.setEndPointUnnumberedDataPathTLV(destUnnumberedDataPathTLV);
					
					
				} else {
					// There are no Interfaces in the Request
					EndPointDataPathTLV sourceDataPathTLV = new EndPointDataPathTLV();
					EndPointDataPathTLV destDataPathTLV = new EndPointDataPathTLV();
					
					sourceDataPathTLV.setSwitchID(SourceString);
					destDataPathTLV.setSwitchID(DestString);
					
					sourceEP.setEndPointDataPathTLV(sourceDataPathTLV);
					destEP.setEndPointDataPathTLV(destDataPathTLV);	
				}
		    	
		    }
		    else{
		    	log.info("The type of Source and Destination EndPoint is not defined");
		    }
			
			P2PEndpoints p2pep=new P2PEndpoints();
			p2pep.setSourceEndPoints(sourceEP);
			p2pep.setDestinationEndPoints(destEP);			

			endP.setP2PEndpoints(p2pep);
			req.setEndPoints(endP);
			log.info("req:: "+req.getEndPoints().toString());

			if (OFCode!=-1){
				ObjectiveFunction obFunc = new ObjectiveFunction();
				obFunc.setOFcode(OFCode);
		
				req.setObjectiveFunction(obFunc);
			}

			InterLayer il= new InterLayer();
			il.setIbit(true);
			il.setIFlag(true);
			il.setMFlag(true);
			req.setInterLayer(il);

			LinkedList<Request> reqList = new LinkedList<Request>();
			reqList.add(req);

			pReq.setRequestList(reqList);
			
			PCEPResponse pr = this.crm.newRequest(pReq);

			this.log.info("Response from PCE " + pr.toString());
			return pr;
		}
		catch (Exception e) {
			this.log.info(UtilsFunctions.exceptionToString(e));
			return null;
		}
	}

	
	

	public PCEPResponse calculatePath(String SourceString, String DestString, int srcIntf, int dstIntf, float bndwdth, int OFCode, String ExcludeString, long excludePort)
	{
		this.log.info("**  PCE  **");
		this.log.info("Calculating cost between " + SourceString + " and " + DestString + " src port "+srcIntf+ " dst port "+dstIntf+ " bandwidth "+bndwdth + " OFCode "+OFCode);
		try {
			// Creating PCEP Request
			PCEPRequest pReq = new PCEPRequest();
			/*LinkedList svecList = new LinkedList();
			pReq.setSvecList(svecList);*/

			Request req = new Request();
			// Creating Request Parameters
			RequestParameters reqParams = new RequestParameters();
			reqParams.setBidirect(true);
			reqParams.setPrio(1);
			//reqParams.setRequestID(1L);
			reqParams.setRequestID(PCCPCEPSession.getNewReqIDCounter());

			req.setRequestParameters(reqParams);
			
			
			
			// Adding Bandwidth to the request
			BandwidthRequested bandwidth = new BandwidthRequested();
			bandwidth.setBw(bndwdth);

			req.setBandwidth(bandwidth);     
			


			
			//Check if Nodes ID are IP or DataPathId
			
			//String example1 = "192.168.20.1";
			//String example2 = "00:00:00:05:00:00:00:08";
			String pattern_datapathid = "([0-9]{2}:){7}([0-9]{2})";
			String pattern_IPv4 = "([0-9]{1,3}\\.){3}([0-9]{1,3})";

			
			
			// Adding GeneralizedEndPoints
			GeneralizedEndPoints endP = new GeneralizedEndPoints();
			
			EndPoint sourceEP=new EndPoint();
			EndPoint destEP=new EndPoint();
		    
			if (SourceString.matches(pattern_IPv4) && DestString.matches(pattern_IPv4)){
		    	System.out.println("jm Es IP match");

				Inet4Address ipSource = (Inet4Address) Inet4Address.getByName(SourceString);
				Inet4Address ipDest = (Inet4Address) Inet4Address.getByName(DestString);
				
				// Check if there are Interfaces or not
				if (srcIntf>0 && dstIntf>0 ){
					// There are Interfaces in the Request
					log.info("THERE ARE INTERFACES IN THE REQUEST");
					UnnumberedEndpointTLV sourceUnnumberedEndpointTLV = new UnnumberedEndpointTLV();
					UnnumberedEndpointTLV destUnnumberedEndpointTLV = new UnnumberedEndpointTLV();
					
					sourceUnnumberedEndpointTLV.setIPv4address(ipSource);
					sourceUnnumberedEndpointTLV.setIfID(srcIntf);
					
					destUnnumberedEndpointTLV.setIPv4address(ipDest);
					destUnnumberedEndpointTLV.setIfID(dstIntf);
					
					sourceEP.setUnnumberedEndpoint(sourceUnnumberedEndpointTLV);
					destEP.setUnnumberedEndpoint(destUnnumberedEndpointTLV);	
					
				} else {
					// There are no Interfaces in the Request
					log.info("THERE ARE NO INTERFACES IN THE REQUEST");
					EndPointIPv4TLV sourceEndPointIPv4TLV = new EndPointIPv4TLV();
					EndPointIPv4TLV destEndPointIPv4TLV = new EndPointIPv4TLV();
					
					sourceEndPointIPv4TLV.setIPv4address(ipSource);
					destEndPointIPv4TLV.setIPv4address(ipDest);
					
					sourceEP.setEndPointIPv4TLV(sourceEndPointIPv4TLV);
					destEP.setEndPointIPv4TLV(destEndPointIPv4TLV);	
					
				}		
				
			}
		    else if (SourceString.matches(pattern_datapathid) && DestString.matches(pattern_datapathid)){
		    	System.out.println("jm Es una DPID Match");
		    	
		    	// Check if there are Interfaces or not
				if (srcIntf>0 && dstIntf>0 ){
					// There are Interfaces in the Request
					EndPointUnnumberedDataPathTLV sourceUnnumberedDataPathTLV = new EndPointUnnumberedDataPathTLV();
					EndPointUnnumberedDataPathTLV destUnnumberedDataPathTLV = new EndPointUnnumberedDataPathTLV();
					
					sourceUnnumberedDataPathTLV.setSwitchID(SourceString);
					sourceUnnumberedDataPathTLV.setIfID(srcIntf);
					destUnnumberedDataPathTLV.setSwitchID(DestString);
					destUnnumberedDataPathTLV.setIfID(dstIntf);
		
					sourceEP.setEndPointUnnumberedDataPathTLV(sourceUnnumberedDataPathTLV);
					destEP.setEndPointUnnumberedDataPathTLV(destUnnumberedDataPathTLV);
					
					
				} else {
					// There are no Interfaces in the Request
					EndPointDataPathTLV sourceDataPathTLV = new EndPointDataPathTLV();
					EndPointDataPathTLV destDataPathTLV = new EndPointDataPathTLV();
					
					sourceDataPathTLV.setSwitchID(SourceString);
					destDataPathTLV.setSwitchID(DestString);
					
					sourceEP.setEndPointDataPathTLV(sourceDataPathTLV);
					destEP.setEndPointDataPathTLV(destDataPathTLV);	
				}
		    	
		    }
		    else{
		    	log.info("The type of Source and Destination EndPoint is not defined");
		    }
			
			P2PEndpoints p2pep=new P2PEndpoints();
			p2pep.setSourceEndPoints(sourceEP);
			p2pep.setDestinationEndPoints(destEP);			

			endP.setP2PEndpoints(p2pep);
			req.setEndPoints(endP);
			log.info("req:: "+req.getEndPoints().toString());
			
			ExcludeRouteObject XRO= new ExcludeRouteObject();		
			LinkedList<XROSubobject> eROSubobjectList=new LinkedList<XROSubobject>();
			
			if (ExcludeString!=null){
				if (ExcludeString.matches(pattern_IPv4) ){
					UnnumberIfIDXROSubobject xRONode = new UnnumberIfIDXROSubobject();
					Inet4Address ipXRO = (Inet4Address) Inet4Address.getByName(ExcludeString);
					xRONode.setRouterID(ipXRO);
					xRONode.setInterfaceID(excludePort);
				} else if (ExcludeString.matches(pattern_datapathid)){
					//UnnumberedDataPathIDXROSubobject xRONode = new DataPathIDXROSubobject();
					DataPathIDXROSubobject xRONode = new DataPathIDXROSubobject();
					DataPathID dpidxro = new DataPathID();
					dpidxro.setDataPathID(ExcludeString);
					xRONode.setDataPath(dpidxro);
					eROSubobjectList.add(xRONode); 
					XRO.setEROSubobjectList(eROSubobjectList);
				}
				req.setXro(XRO);
				log.info("Path_Computation XRO:: "+req.getXro());
				log.info("req: "+req.toString());
			}
			
			ObjectiveFunction obFunc = new ObjectiveFunction();
			obFunc.setOFcode(OFCode);

			req.setObjectiveFunction(obFunc);

			InterLayer il= new InterLayer();
			il.setIbit(true);
			il.setIFlag(true);
			il.setMFlag(true);
			req.setInterLayer(il);

			LinkedList<Request> reqList = new LinkedList<Request>();
			reqList.add(req);

			pReq.setRequestList(reqList);
			this.log.info("Request from PCE " + pReq.toString());
			
			PCEPResponse pr = this.crm.newRequest(pReq);

			this.log.info("Response from PCE " + pr.toString());
			return pr;
		}
		catch (Exception e) {
			this.log.info(UtilsFunctions.exceptionToString(e));
			return null;
		}
	}
		
	public PCEPResponse calculatePath(String ipSourceString, String ipDestString) {
		this.log.info("**  PCE  **");
		this.log.info("Calculating cost between " + ipSourceString + " and " + ipDestString);
		try
		{
			Inet4Address ipSource = (Inet4Address)Inet4Address.getByName(ipSourceString);
			Inet4Address ipDest = (Inet4Address)Inet4Address.getByName(ipDestString);
			PCEPRequest p_r = new PCEPRequest();
			Request req = new Request();
			p_r.addRequest(req);
			RequestParameters rp = new RequestParameters();
			rp.setPbit(true);
			req.setRequestParameters(rp);
			rp.setRequestID(PCCPCEPSession.getNewReqIDCounter());
			EndPointsIPv4 ep = new EndPointsIPv4();
			req.setEndPoints(ep);
			ep.setSourceIP(ipSource);
			ep.setDestIP(ipDest);
			ObjectiveFunction of = new ObjectiveFunction();
			of.setOFcode(this.ofCode);
			req.setObjectiveFunction(of);

			/*float bw = 100.0F;
      		Bandwidth bandwidth = new Bandwidth();
      		bandwidth.setBw(bw);
      		req.setBandwidth(bandwidth);
			 */
			PCEPResponse pr = this.crm.newRequest(p_r);

			this.log.info("Response from PCE " + pr.toString());
			return pr;
		}
		catch (Exception e) {
			this.log.info("Exception");
			this.log.info(UtilsFunctions.exceptionToString(e));
		}return null;
	}

	
	
	
	
	public PCEPResponse calculatePath(String ipSourceString, String ipDestString, long srcIntf, long dstIntf) {
		log.info("**  PCE  **");		
		log.info("Calculating cost between " + ipSourceString + " and " + ipDestString);		

		try{			
			Inet4Address ipSource = (Inet4Address) Inet4Address.getByName(ipSourceString);
			Inet4Address ipDest = (Inet4Address) Inet4Address.getByName(ipDestString);
			PCEPRequest p_r = new PCEPRequest();
			Request req = new Request();
			p_r.addRequest(req);
			RequestParameters rp= new RequestParameters();
			rp.setPbit(true);
			req.setRequestParameters(rp);
			rp.setRequestID(PCCPCEPSession.getNewReqIDCounter());
			EndPointsUnnumberedIntf ep=new EndPointsUnnumberedIntf();				
			req.setEndPoints(ep);
			ep.setSourceIP(ipSource);
			ep.setSourceIF(srcIntf);
			ep.setDestIP(ipDest);
			ep.setDestIF(dstIntf);	
			ObjectiveFunction of=new ObjectiveFunction();
			of.setOFcode(this.ofCode);
			req.setObjectiveFunction(of);

			/*Metric metric = new Metric();
			if (mymetricType == "bandwidth")
				metric.setMetricType(ObjectParameters.PCEP_METRIC_TYPE_BW);
			if (mymetricType == "te")
				metric.setMetricType(ObjectParameters.PCEP_METRIC_TYPE_TE_METRIC);
			metric.setComputedMetricBit(true);
			req.getMetricList().add(metric);*/

			float bw = 100;
			BandwidthRequested bandwidth=new BandwidthRequested();
			bandwidth.setBw(bw);
			req.setBandwidth(bandwidth);					

			PCEPResponse pr=crm.newRequest(p_r);

			log.info("Response from PCE "+pr.toString());
			return pr;
		}
		catch(Exception e){
			log.info("Exception");
			log.info(UtilsFunctions.exceptionToString(e));
			return null;
		}	
	}

	public PCEPResponse request4GCO(String ipSourceString, String ipDestString, int OFCode) {
		log.info("**  PCE  **");		
		log.info("Calculating cost between " + ipSourceString + " and " + ipDestString);		

		try{			
			Inet4Address ipSource = (Inet4Address) Inet4Address.getByName(ipSourceString);
			Inet4Address ipDest = (Inet4Address) Inet4Address.getByName(ipDestString);
			PCEPRequest p_r = new PCEPRequest();
			Request req = new Request();
			p_r.addRequest(req);
			RequestParameters rp= new RequestParameters();
			rp.setPbit(true);
			req.setRequestParameters(rp);
			rp.setRequestID(PCCPCEPSession.getNewReqIDCounter());
			EndPointsIPv4 ep=new EndPointsIPv4();				
			req.setEndPoints(ep);
			ep.setSourceIP(ipSource);
			ep.setDestIP(ipDest);
			ObjectiveFunction of=new ObjectiveFunction();
			of.setOFcode(OFCode);
			req.setObjectiveFunction(of);

			/*Metric metric = new Metric();
			if (mymetricType == "bandwidth")
				metric.setMetricType(ObjectParameters.PCEP_METRIC_TYPE_BW);
			if (mymetricType == "te")
				metric.setMetricType(ObjectParameters.PCEP_METRIC_TYPE_TE_METRIC);
			metric.setComputedMetricBit(true);
			req.getMetricList().add(metric);*/

			float bw = 100;
			BandwidthRequested bandwidth=new BandwidthRequested();
			bandwidth.setBw(bw);
			req.setBandwidth(bandwidth);					

			PCEPResponse pr=crm.newRequest(p_r);

			log.info("Response from PCE "+pr.toString());
			return pr;
		}
		catch(Exception e){
			log.info("Exception");
			log.info(UtilsFunctions.exceptionToString(e));
			return null;
		}	
	}

	public PCEPResponse calculatePath(String ipSourceString, String ipDestString, float bndwdth) {
		log.info("**  PCE  **");		
		log.info("Calculating cost between " + ipSourceString + " and " + ipDestString);		

		try{			
			Inet4Address ipSource = (Inet4Address) Inet4Address.getByName(ipSourceString);
			Inet4Address ipDest = (Inet4Address) Inet4Address.getByName(ipDestString);
			PCEPRequest p_r = new PCEPRequest();
			Request req = new Request();
			p_r.addRequest(req);
			RequestParameters rp= new RequestParameters();
			rp.setPbit(true);
			req.setRequestParameters(rp);
			rp.setRequestID(PCCPCEPSession.getNewReqIDCounter());
			EndPointsIPv4 ep=new EndPointsIPv4();				
			req.setEndPoints(ep);
			ep.setSourceIP(ipSource);								
			ep.setDestIP(ipDest);	
			ObjectiveFunction of=new ObjectiveFunction();
			of.setOFcode(this.ofCode);
			req.setObjectiveFunction(of);

			/*Metric metric = new Metric();
			if (mymetricType == "bandwidth")
				metric.setMetricType(ObjectParameters.PCEP_METRIC_TYPE_BW);
			if (mymetricType == "te")
				metric.setMetricType(ObjectParameters.PCEP_METRIC_TYPE_TE_METRIC);
			metric.setComputedMetricBit(true);
			req.getMetricList().add(metric);*/


			BandwidthRequested bandwidth=new BandwidthRequested();
			bandwidth.setBw(bndwdth);
			req.setBandwidth(bandwidth);					

			PCEPResponse pr=crm.newRequest(p_r);

			log.info("Response from PCE "+pr.toString());
			return pr;
		}
		catch(Exception e){
			log.info("Exception");
			log.info(UtilsFunctions.exceptionToString(e));
			return null;
		}	
	}

	public PCEPResponse calculatePath(String SourceString, String DestString, float bndwdth, int OFCode)
	{
		this.log.info("**  PCE  **");
		
		try {
			PCEPRequest pReq = new PCEPRequest();
			LinkedList<SVECConstruct> svecList = new LinkedList<SVECConstruct>();
			pReq.setSvecList(svecList);

			Request req = new Request();

			RequestParameters reqParams = new RequestParameters();
			reqParams.setBidirect(false);
			reqParams.setPrio(1);
			reqParams.setRequestID(1L);

			req.setRequestParameters(reqParams);

			BandwidthRequested bandwidth=new BandwidthRequested();
			bandwidth.setBw(bndwdth);
			req.setBandwidth(bandwidth);	


			//***NEW
			GeneralizedEndPoints endP = new GeneralizedEndPoints();
			
			EndPointDataPathTLV sourceDataPathTLV = new EndPointDataPathTLV();
			EndPointDataPathTLV destDataPathTLV = new EndPointDataPathTLV();
			sourceDataPathTLV.setSwitchID(SourceString);
			destDataPathTLV.setSwitchID(DestString);

			EndPoint sourceEP=new EndPoint();
			EndPoint destEP=new EndPoint();
			sourceEP.setEndPointDataPathTLV(sourceDataPathTLV);
			destEP.setEndPointDataPathTLV(destDataPathTLV);

			P2PEndpoints p2pep=new P2PEndpoints();
			p2pep.setSourceEndPoints(sourceEP);
			p2pep.setDestinationEndPoints(destEP);			

			endP.setP2PEndpoints(p2pep);
			req.setEndPoints(endP);
			log.info("req:: "+req.getEndPoints().toString());

			ObjectiveFunction obFunc = new ObjectiveFunction();
			obFunc.setOFcode(OFCode);

			req.setObjectiveFunction(obFunc);

			InterLayer il= new InterLayer();
			il.setIbit(true);
			il.setIFlag(true);
			il.setMFlag(true);
			req.setInterLayer(il);

			LinkedList<Request> reqList = new LinkedList<Request>();
			reqList.add(req);

			pReq.setRequestList(reqList);

			PCEPResponse pr = this.crm.newRequest(pReq);

			this.log.info("Response from PCE " + pr.toString());
			return pr;
		}
		catch (Exception e) {
			this.log.info(UtilsFunctions.exceptionToString(e));
			return null;
		}
	}
	
	

	public PCEPResponse calculatePathXRO(String ipSourceString, String ipDestString, float bndwdth, Inet4Address ipXRO, long portXRO) {
		log.info("**  PCE  **");		
		log.info("Calculating cost between " + ipSourceString + " and " + ipDestString + " whitout " + ipXRO + ":" + portXRO );		

		try{			
			Inet4Address ipSource = (Inet4Address) Inet4Address.getByName(ipSourceString);
			Inet4Address ipDest = (Inet4Address) Inet4Address.getByName(ipDestString);
			PCEPRequest p_r = new PCEPRequest();
			Request req = new Request();
			p_r.addRequest(req);
			RequestParameters rp= new RequestParameters();
			rp.setPbit(true);
			req.setRequestParameters(rp);
			rp.setRequestID(PCCPCEPSession.getNewReqIDCounter());
			EndPointsIPv4 ep=new EndPointsIPv4();				
			req.setEndPoints(ep);
			ep.setSourceIP(ipSource);								
			ep.setDestIP(ipDest);	
			ObjectiveFunction of=new ObjectiveFunction();
			of.setOFcode(this.ofCode);
			req.setObjectiveFunction(of);

			/*Metric metric = new Metric();
			if (mymetricType == "bandwidth")
				metric.setMetricType(ObjectParameters.PCEP_METRIC_TYPE_BW);
			if (mymetricType == "te")
				metric.setMetricType(ObjectParameters.PCEP_METRIC_TYPE_TE_METRIC);
			metric.setComputedMetricBit(true);
			req.getMetricList().add(metric);*/

			BandwidthRequested bandwidth = new BandwidthRequested();
			bandwidth.setBw(bndwdth);
			req.setBandwidth(bandwidth);					

			ExcludeRouteObject XRO= new ExcludeRouteObject();		
			LinkedList<XROSubobject> eROSubobjectList=new LinkedList<XROSubobject>();

			UnnumberIfIDXROSubobject xRONode = new UnnumberIfIDXROSubobject();
			xRONode.setRouterID(ipXRO);
			xRONode.setInterfaceID(portXRO);

			eROSubobjectList.add(xRONode); 
			XRO.setEROSubobjectList(eROSubobjectList);
			req.setXro(XRO);
			log.info("Path_Computation XRO:: "+req.getXro());

			PCEPResponse pr=crm.newRequest(p_r);

			log.info("Response from PCE "+pr.toString());
			return pr;
		}
		catch(Exception e){
			log.info("Exception");
			log.info(UtilsFunctions.exceptionToString(e));
			return null;
		}	
	}

	public PCEPResponse calculateDataPathXRO(String SourceString, String DestString, String dpidXRO, long portXRO, float bndwdth ) {
		log.info("**  PCE  **");		
		log.info("Calculating cost between " + SourceString + " and " + DestString + " whitout " + dpidXRO + ":" + portXRO );		

		try{			
			PCEPRequest p_r = new PCEPRequest();
			Request req = new Request();
			p_r.addRequest(req);
			RequestParameters rp= new RequestParameters();
			rp.setPbit(true);
			req.setRequestParameters(rp);
			rp.setRequestID(PCCPCEPSession.getNewReqIDCounter());
			
			GeneralizedEndPoints endP = new GeneralizedEndPoints();
			
			EndPointDataPathTLV sourceDataPathTLV = new EndPointDataPathTLV();
			EndPointDataPathTLV destDataPathTLV = new EndPointDataPathTLV();
			sourceDataPathTLV.setSwitchID(SourceString);
			destDataPathTLV.setSwitchID(DestString);
				
			EndPoint sourceEP=new EndPoint();
			EndPoint destEP=new EndPoint();
			sourceEP.setEndPointDataPathTLV(sourceDataPathTLV);
			destEP.setEndPointDataPathTLV(destDataPathTLV);
			
			P2PEndpoints p2pep=new P2PEndpoints();
			p2pep.setSourceEndPoints(sourceEP);
			p2pep.setDestinationEndPoints(destEP);	

			endP.setP2PEndpoints(p2pep);
			req.setEndPoints(endP);
			log.info("req:: "+req.getEndPoints().toString());
			
			ObjectiveFunction of=new ObjectiveFunction();
			of.setOFcode(this.ofCode);
			req.setObjectiveFunction(of);
		
			BandwidthRequested bandwidth = new BandwidthRequested();
			bandwidth.setBw(bndwdth);
			req.setBandwidth(bandwidth);					

			ExcludeRouteObject XRO= new ExcludeRouteObject();		
			LinkedList<XROSubobject> eROSubobjectList=new LinkedList<XROSubobject>();

			DataPathIDXROSubobject xRONode = new DataPathIDXROSubobject();
			xRONode.dataPath.setDataPathID(dpidXRO);
									
			eROSubobjectList.add(xRONode); 
			XRO.setEROSubobjectList(eROSubobjectList);
			req.setXro(XRO);
			log.info("Path_Computation XRO:: "+req.getXro());
			log.info("req: "+req.toString());

			PCEPResponse pr=crm.newRequest(p_r);

			log.info("Response from PCE "+pr.toString());
			return pr;
		}
		catch(Exception e){
			log.info("Exception");
			log.info(UtilsFunctions.exceptionToString(e));
			return null;
		}	
	}

	public ClientRequestManager getCrm() {
		return crm;
	}

	public static String getDEFAULT_MAC() {
		return DEFAULT_MAC;
	}

	public static void setDEFAULT_MAC(String dEFAULT_MAC) {
		DEFAULT_MAC = dEFAULT_MAC;
	}
	
	

}