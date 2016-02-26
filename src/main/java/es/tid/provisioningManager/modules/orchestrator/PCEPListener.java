package es.tid.provisioningManager.modules.orchestrator;

import java.net.Inet4Address;
import java.util.logging.Logger;

import es.tid.emulator.node.transport.EmulatedPCCPCEPSession;
import es.tid.pce.client.ClientRequestManager;
import es.tid.pce.pcep.constructs.Request;
import es.tid.pce.pcep.messages.PCEPRequest;
import es.tid.pce.pcep.messages.PCEPResponse;
import es.tid.pce.pcep.objects.BandwidthRequested;
import es.tid.pce.pcep.objects.EndPointsIPv4;
import es.tid.pce.pcep.objects.ObjectiveFunction;
import es.tid.pce.pcep.objects.RequestParameters;
import es.tid.pce.pcepsession.PCEPSessionsInformation;

public class PCEPListener {
	private static EmulatedPCCPCEPSession PCEsession;
	private PCEPSessionsInformation pcepSessionManager;
	ClientRequestManager crm;
	private Logger log;
	
	public PCEPListener(){
		pcepSessionManager=new PCEPSessionsInformation();
		PCEsession = new EmulatedPCCPCEPSession("localhost", 4189 ,false,pcepSessionManager);
		PCEsession.start();		
		this.crm=PCEsession.crm;
		log=Logger.getLogger("PCEP listener");
	}
	
	public PCEPResponse recieve(){
		PCEPResponse pcepResponse = testResponse("192.168.8.1","192.168.8.3");
		return pcepResponse;
	}
	public PCEPResponse testResponse(String ipSourceString, String ipDestString) {
		log.info("**  PCE  **");		
		log.info("Calculating cost between " + ipSourceString + " and " + ipDestString);		
		
		try{
			Thread.sleep(1000);
			Inet4Address ipSource = (Inet4Address) Inet4Address.getByName(ipSourceString);
			Inet4Address ipDest = (Inet4Address) Inet4Address.getByName(ipDestString);
			PCEPRequest p_r = new PCEPRequest();
			Request req = new Request();
			p_r.addRequest(req);
			RequestParameters rp= new RequestParameters();
			rp.setPbit(true);
			req.setRequestParameters(rp);
			rp.setRequestID(EmulatedPCCPCEPSession.getNewReqIDCounter());
			EndPointsIPv4 ep=new EndPointsIPv4();				
			req.setEndPoints(ep);
			ep.setSourceIP(ipSource);								
			ep.setDestIP(ipDest);	
			ObjectiveFunction of=new ObjectiveFunction();
			of.setOFcode(1100);
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
			return null;	
		}	
	}	

}
