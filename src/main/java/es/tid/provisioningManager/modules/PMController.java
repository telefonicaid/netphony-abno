package es.tid.provisioningManager.modules;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.tid.emulator.node.transport.EmulatedPCCPCEPSession;
import es.tid.pce.client.ClientRequestManager;
import es.tid.pce.pcep.PCEPProtocolViolationException;
import es.tid.pce.pcep.constructs.Request;
import es.tid.pce.pcep.messages.PCEPInitiate;
import es.tid.pce.pcep.messages.PCEPMessage;
import es.tid.pce.pcep.messages.PCEPMessageTypes;
import es.tid.pce.pcep.messages.PCEPRequest;
import es.tid.pce.pcep.messages.PCEPResponse;
import es.tid.pce.pcep.objects.BandwidthRequested;
import es.tid.pce.pcep.objects.EndPointsUnnumberedIntf;
import es.tid.pce.pcep.objects.ExplicitRouteObject;
import es.tid.pce.pcep.objects.ObjectiveFunction;
import es.tid.pce.pcep.objects.RequestParameters;
import es.tid.pce.pcepsession.PCEPSessionsInformation;
import es.tid.util.UtilsFunctions;
import es.tid.provisioningManager.modules.orchestrator.Orchestrator;
import es.tid.provisioningManager.modules.orchestrator.OrchestratorQueue;
import es.tid.provisioningManager.objects.lsps.LSP;

public class PMController {
	private static Logger log=LoggerFactory.getLogger("PM Controller");

	private static HashMap<Integer, ExplicitRouteObject> eroIdMap=new HashMap<Integer, ExplicitRouteObject>();
	private static ProvisioningManagerParams params;
	private static LinkedList<LSP> lsps=new LinkedList<LSP>();


	public static void main(String []args){
		
		try {
		    Thread.sleep(4000);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		    // handle the exception...        
		    // For example consider calling Thread.currentThread().interrupt(); here.
		}

		//shitFunction();
		
		
		//log.info("Shit function!!!!!!!!!!!!!!!!!!!!");
		
		/*Load parameters*/
		if (args.length >=1 ){
			params=new ProvisioningManagerParams(args[0]);
		}else{
			params=new ProvisioningManagerParams();
		}
		params.initialize();
		//log.info("param"+params.getPcepPort());
		/*Read topology from xml and send to the Topology module*/
		readTopology(params.getTopology());

		/*Start Orchestrator Pool*/
		OrchestratorQueue orchestratorQueue = new OrchestratorQueue(5);

		ServerSocket serverSocket = null;
		boolean listening = true;
		try {
			log.info("Listening on port: " + params.getPcepPort());			

			log.info("Listening on address: localhost");
			serverSocket = new ServerSocket(params.getPcepPort());

		} catch (IOException e) {
			System.err.println("Could not listen on port:"+ params.getPcepPort());
			System.exit(-1);
		}

		/*LISTEN FOR PCEP MESSAGES*/
		while (listening) {
			try{
				Socket connectionSocket = serverSocket.accept();			
				DataInputStream in = new DataInputStream(connectionSocket.getInputStream());
				byte[] msg = readMsg(in);//Read a new message
				if (msg != null) {						
					switch(PCEPMessage.getMessageType(msg)){
						case PCEPMessageTypes.MESSAGE_INITIATE:
						{
							log.info("Received PCEP Intiate message");
							/*Start Orchestrator*/
							PCEPInitiate pcepInitiate=new PCEPInitiate();
							try {
								pcepInitiate = new PCEPInitiate(msg);
							} catch (PCEPProtocolViolationException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							Orchestrator orchestrator = new Orchestrator(params,pcepInitiate, new DataOutputStream(connectionSocket.getOutputStream()), lsps, eroIdMap);
							/*Start Orchestrator*/
							orchestratorQueue.execute(orchestrator);
							break;
						}
						case PCEPMessageTypes.MESSAGE_PCREP:
						{
							log.warn("This provisioning manager was upgraded and doesn't expect PCEP Response messages");
							break;
						}
						default:
						{
							log.warn("Received unsupported PCEP message::"+PCEPMessage.getMessageType(msg));
							break;
						}
					}
	
				}
			}
			catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		}

		try {
			serverSocket.close();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

	}
	
	private static PCEPResponse queryPCE(String sourceS, String destS)
	{
		Inet4Address ipOpcticSource = null;
		Inet4Address ipOpticDest = null;
		try{
			ipOpcticSource= (Inet4Address)Inet4Address.getByName(sourceS); //String con la direccion ip de salida
			
			ipOpticDest =(Inet4Address)Inet4Address.getByName(destS);
		}
		catch (Exception e)
		{
			log.info(UtilsFunctions.exceptionToString(e));
		}
		//Enviamos con código de charly
		
		
		PCEPSessionsInformation pcepSessionManagerPCE=new PCEPSessionsInformation();
		EmulatedPCCPCEPSession PCEsession = new EmulatedPCCPCEPSession("localhost", 4189 ,false,pcepSessionManagerPCE);
		PCEsession.start();	
		try {
			Thread.currentThread().sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			log.info(UtilsFunctions.exceptionToString(e));
		}
		ClientRequestManager crm = PCEsession.crm;
		crm.setDataOutputStream(PCEsession.getOut());
		if (PCEsession.getOut()==null)
			System.out.println("La salida esta a null, algo raro pasa...");
		crm.setDataOutputStream(PCEsession.getOut());
		
		System.out.println("Enviamos de: "+ipOpcticSource+" a "+ipOpticDest);
		
		PCEPRequest p_r = new PCEPRequest();
		Request req = new Request();
		p_r.addRequest(req);
		RequestParameters rp= new RequestParameters();
		rp.setPbit(true);
		req.setRequestParameters(rp);
		rp.setRequestID(EmulatedPCCPCEPSession.getNewReqIDCounter());
		//EndPointsIPv4 ep=new EndPointsIPv4();
		EndPointsUnnumberedIntf ep= new EndPointsUnnumberedIntf();
		req.setEndPoints(ep);
		ep.setSourceIP(ipOpcticSource);	
		ep.setDestIP(ipOpticDest);
		ep.setDestIF(3);
		ep.setSourceIF(4);
		ObjectiveFunction of=new ObjectiveFunction();
		of.setOFcode(1200);
		req.setObjectiveFunction(of);

		
		float bw = 100;
		BandwidthRequested bandwidth=new BandwidthRequested();
		bandwidth.setBw(bw);
		req.setBandwidth(bandwidth);					
		
		
		return crm.newRequest(p_r);
	}
	
	/*private static void shitFunction()
	{
		PCEPInitiate pceInit = new PCEPInitiate();
		//For the time being, no need to put anything here
		SRP rsp = new SRP();
		
		//For the time being, no need to put anything here
		LSP lsp = new LSP();
		
		EndPointsIPv4 endP = new EndPointsIPv4();
		//Esto no se lee en ningun en ningun lado
		try 
		{
			Inet4Address source = (Inet4Address) Inet4Address.getByName("192.168.1.9");
			Inet4Address dest = (Inet4Address)Inet4Address.getByName("192.168.1.7");
			
			endP.setSourceIP(source);
			endP.setDestIP(dest);
			
			/*
			PCEParameters pceParams = new PCEParameters("192.168.1.200", 4189, 1001);
			Path_Computation path_Computation = new Path_Computation(pceParams);
			
			PCEPResponse pcepResponse = path_Computation.calculatePath("192.168.1.1","192.168.1.3");
			
			PCEPResponse pcepResponse = queryPCE("192.168.1.9","192.168.1.7");
			
			ExplicitRouteObject ero;
			ero = (pcepResponse.getResponse(0).getPath(0).geteRO());
			
			PCEPIntiatedLSP pcepIntiatedLSPList = new PCEPIntiatedLSP();
			pcepIntiatedLSPList.setEro(ero);
			pcepIntiatedLSPList.setRsp(rsp);
			pcepIntiatedLSPList.setLsp(lsp);
			pcepIntiatedLSPList.setEndPoint(endP);
			
			pceInit.getPcepIntiatedLSPList().add(pcepIntiatedLSPList);
		} 
		catch (UnknownHostException e) 
		{
			log.info(UtilsFunctions.exceptionToString(e));
		}
		
		boolean work = false;
		
		while (!work)
		{
		
			try 
			{
				try {
					log.info("Waiting two seconds!");
					Thread.currentThread().sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					log.info(UtilsFunctions.exceptionToString(e));
				}
				
				Socket clientSocket = new Socket("192.168.1.9", 2222);			
				log.info("Socket opened");	
				/*SENDING PCEP MESSAGE
				DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
				try 
				{
					pceInit.encode();
					log.info("Sending message At LAST!");
					outToServer.write(pceInit.getBytes());
					outToServer.flush();
					work = true;
				} 
				catch (Exception e) 
				{
					log.info(UtilsFunctions.exceptionToString(e));
				}
			} 
			catch (IOException e) 
			{
				work = false;
				log.info(UtilsFunctions.exceptionToString(e));
				log.error("Couldn't get I/O for connection to port" + 2222);
			} 
		}
	}
*/
	private static void readTopology(String topology) {


	}
	/**
	 * Read PCE message from TCP stream
	 * @param in InputStream
	 */
	private static byte[] readMsg(DataInputStream in) throws IOException{
		byte[] ret = null;

		byte[] hdr = new byte[4];
		byte[] temp = null;
		boolean endHdr = false;
		int r = 0;
		int length = 0;
		boolean endMsg = false;
		int offset = 0;

		while (!endMsg) {
			try {
				if (endHdr) {
					r = in.read(temp, offset, 1);
				}
				else {
					r = in.read(hdr, offset, 1);
				}
			} catch (IOException e){
				log.warn("Error reading data: "+ e.getMessage());
				throw e;
			}catch (Exception e) {
				log.warn("readMsg Oops: " + e.getMessage());
				throw new IOException();
			}

			if (r > 0) {
				if (offset == 2) {
					length = ((int)hdr[offset]&0xFF) << 8;
				}
				if (offset == 3) {
					length = length | (((int)hdr[offset]&0xFF));
					temp = new byte[length];
					endHdr = true;
					System.arraycopy(hdr, 0, temp, 0, 4);
				}
				if ((length > 0) && (offset == length - 1)) {
					endMsg = true;
				}
				offset++;
			}
			else if (r==-1){
				log.warn("End of stream has been reached");
				throw new IOException();
			}
		}
		if (length > 0) {
			ret = new byte[length];
			System.arraycopy(temp, 0, ret, 0, length);
		}		
		return ret;
	}
}
