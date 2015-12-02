package es.tid.abno.modules.workflows;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.tid.abno.modules.ABNOParameters;
import es.tid.abno.modules.Path_Computation;
import es.tid.abno.modules.database.OpTable;
import es.tid.pce.pcep.constructs.GeneralizedBandwidthSSON;
import es.tid.pce.pcep.constructs.PCEPIntiatedLSP;
import es.tid.pce.pcep.constructs.UpdateRequest;
import es.tid.pce.pcep.messages.PCEPInitiate;
import es.tid.pce.pcep.messages.PCEPMessage;
import es.tid.pce.pcep.messages.PCEPReport;
import es.tid.pce.pcep.messages.PCEPResponse;
import es.tid.pce.pcep.messages.PCEPUpdate;
import es.tid.pce.pcep.objects.Bandwidth;
import es.tid.pce.pcep.objects.BandwidthRequested;
import es.tid.pce.pcep.objects.BandwidthRequestedGeneralizedBandwidth;
import es.tid.pce.pcep.objects.EndPointDataPathID;
import es.tid.pce.pcep.objects.EndPoints;
import es.tid.pce.pcep.objects.EndPointsIPv4;
import es.tid.pce.pcep.objects.ExplicitRouteObject;
import es.tid.pce.pcep.objects.LSP;
import es.tid.pce.pcep.objects.SRP;
import es.tid.pce.pcep.objects.tlvs.SymbolicPathNameTLV;
import es.tid.pce.pcep.objects.tlvs.subtlvs.SymbolicPathNameSubTLV;
import es.tid.pce.pcepsession.GenericPCEPSession;
import es.tid.util.UtilsFunctions;

/**
 * 
 * @author b.jmgj
 *
 */

public abstract class WorkflowCOP 
{
	/**
	 * Logger
	 */
	protected static Logger log=Logger.getLogger("ABNO Controller");

	protected Hashtable<String, String> request;
	protected String response;
	protected LinkedList<Path_Computation> path_Computationlist;
	protected ABNOParameters params;
	protected HashMap<Integer, OpTable> oPtable;
	static protected AtomicInteger serviceCounter=new AtomicInteger(1);

	protected PCEPReport reportResp;

	public WorkflowCOP(Hashtable<String, String> request, String response, LinkedList<Path_Computation> path_Computationlist, ABNOParameters params, HashMap<Integer, OpTable> oPtable)
	{
		this.request = request;
		this.response = response;
		this.path_Computationlist = path_Computationlist;
		this.params = params;
		this.oPtable=oPtable;
	}
	
	public String getResponse(){
		return response;
	}

	/**
	 * Handle the ABNO request
	 */
	public abstract void handleRequest();

	/**
	 * Handle the ABNO request
	 */
	public void handleRequest(String s){}

	/**
	 * The request is OK (200)
	 */
//	
//	protected void replyOkey()
//	{
//		response.setHeader("Content-Type", "application/json");
//		response.setStatus(HttpServletResponse.SC_OK);
//	}
//	
//	protected void replyOK()
//	{
//		try 
//		{
//			response.setStatus(HttpServletResponse.SC_OK);
//			response.getWriter().println("<html><body><p>Everything OK</p></body></html>");
//		} 
//		catch (IOException e) 
//		{
//			log.info(UtilsFunctions.exceptionToString(e));
//		}
//	}
//
//	/**
//	 * This is really replying Internal Server Error (500)
//	 */
//	protected void replyError()
//	{
//		try 
//		{
//			response.setHeader("Content-Type", "application/json");
//			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//		} 
//		catch (IOException e) 
//		{
//			log.info(UtilsFunctions.exceptionToString(e));
//		}
//	}
//
//	/**
//	 * This is really replying Client Server Error (400)
//	 * The request sent by the client was syntactically incorrect.
//	 */
//	protected void replyClientError()
//	{
//		try 
//		{
//			response.setHeader("Content-Type", "application/json");
//			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
//		} 
//		catch (IOException e) 
//		{
//			log.info(UtilsFunctions.exceptionToString(e));
//		}
//	}
//
//	/**
//	 * @param code : code that will be sent
//	 */
//	protected void reply(int code)
//	{
//		try 
//		{
//			response.setStatus(code);
//			response.getWriter().println("<html><body><p>Code :"+code+" </p></body></html>");
//		} 
//		catch (IOException e) 
//		{
//			log.info(UtilsFunctions.exceptionToString(e));
//		}
//	}
//
//	/**
//	 * 
//	 * @param message : message
//	 */
//	protected void replyMessage(String message)
//	{
//		//response.setHeader("Content-Type", "text/plain");
//		response.setHeader("Content-Type", "application/json");
//		try 
//		{    
//			response.getWriter().println(message);
//			//response.getWriter().println("<html><body><p>Code :"+message+" </p></body></html>");
//		} 
//		catch (IOException e) 
//		{
//			log.info(UtilsFunctions.exceptionToString(e));
//		}
//	}


	protected void printOPTable() {

		Iterator<Integer> intit=this.oPtable.keySet().iterator();
		System.out.println("-----------------------------------------------------------------");
		System.out.println("ID\tOwner\t\tOpType\t\tProvIP\t\tProvID");
		System.out.println("-----------------------------------------------------------------");
		while(intit.hasNext()){
			int id=intit.next();
			OpTable opt=this.oPtable.get(id);
			System.out.println(id+"\t"+opt.getAppIP()+"\t"+opt.getWorkflow()+"\t"+opt.getPCCIP()+"\t"+opt.getPCCoperationID());
		}
		System.out.println("-----------------------------------------------------------------");
	}

	protected PCEPInitiate responseTOinitiate(PCEPResponse pcepresponse, int m)
	{
		PCEPInitiate pcepInit = new PCEPInitiate();

		//For the time being, no need to put anything here
		SRP rsp = new SRP();

		//For the time being, no need to put anything here
		LSP lsp = new LSP();
		SymbolicPathNameTLV spn = new SymbolicPathNameTLV();
		spn.setSymbolicPathNameID("IDEALIST".getBytes());
		lsp.setSymbolicPathNameTLV_tlv(spn);	

		ExplicitRouteObject ero;
		ero = (pcepresponse.getResponse(0).getPath(0).geteRO());
		
		PCEPIntiatedLSP pcepIntiatedLSP = new PCEPIntiatedLSP();
		pcepIntiatedLSP.setEro(ero);
		pcepIntiatedLSP.setRsp(rsp);
		pcepIntiatedLSP.setLsp(lsp);
	
		if (m==-1){
			pcepIntiatedLSP.setBandwidth((BandwidthRequested)pcepresponse.getResponse(0).getBandwidth());
		}else {
			BandwidthRequestedGeneralizedBandwidth gb= new BandwidthRequestedGeneralizedBandwidth();
			
			// Adding Bandwidth to the request
			GeneralizedBandwidthSSON bandwidth=new GeneralizedBandwidthSSON();
			bandwidth.setM(m);
			gb.setGeneralizedBandwidth(bandwidth);
			
			pcepIntiatedLSP.setBandwidth(gb);	
			
		}
				
		pcepInit.getPcepIntiatedLSPList().add(pcepIntiatedLSP);

		return pcepInit;
	}
	
	protected PCEPInitiate responseTOinitiate(PCEPResponse pcepresponse)
	{
		PCEPInitiate pcepInit = new PCEPInitiate();

		//For the time being, no need to put anything here
		SRP rsp = new SRP();

		//For the time being, no need to put anything here
		LSP lsp = new LSP();

		ExplicitRouteObject ero;
		ero = (pcepresponse.getResponse(0).getPath(0).geteRO());
		
		PCEPIntiatedLSP pcepIntiatedLSPList = new PCEPIntiatedLSP();
		log.info("jm ero initiate: "+ ero.toString());
		pcepIntiatedLSPList.setEro(ero);
		pcepIntiatedLSPList.setRsp(rsp);
		pcepIntiatedLSPList.setLsp(lsp);
		
		//FIXME
		pcepIntiatedLSPList.setBandwidth((BandwidthRequested)pcepresponse.getResponse(0).getBandwidth()); //Asumo que este bw funciona. Revisar si falla.

		pcepInit.getPcepIntiatedLSPList().add(pcepIntiatedLSPList);

		return pcepInit;
	}
	
	protected PCEPInitiate responseTOupdate(PCEPResponse pcepresponse)
	{
		PCEPInitiate pcepInit = new PCEPInitiate();

		//For the time being, no need to put anything here
		SRP rsp = new SRP();

		//For the time being, no need to put anything here
		LSP lsp = new LSP();

		ExplicitRouteObject ero;
		ero = (pcepresponse.getResponse(0).getPath(0).geteRO());
		
		PCEPIntiatedLSP pcepIntiatedLSPList = new PCEPIntiatedLSP();
		log.info("jm ero initiate: "+ ero.toString());
		pcepIntiatedLSPList.setEro(ero);
		pcepIntiatedLSPList.setRsp(rsp);
		pcepIntiatedLSPList.setLsp(lsp);
		
		//FIXME
		pcepIntiatedLSPList.setBandwidth((BandwidthRequested)pcepresponse.getResponse(0).getBandwidth()); //Asumo que este bw funciona. Revisar si falla.

		pcepInit.getPcepIntiatedLSPList().add(pcepIntiatedLSPList);

		return pcepInit;
	}
	

	
	protected PCEPMessage  callPCE(PCEPInitiate pcepInit) 
	{
		log.info("Call PCE");
		return path_Computationlist.getFirst().getCrm().initiate(pcepInit, 60000);
					
	}

	
	protected void callProvisioningManager(PCEPInitiate pcepInit)
	{
		log.info("Call ProvisioningManager");
		Socket clientSocket = null;
		try 
		{
			clientSocket = new Socket(params.getPMAddress(), params.getPcepPortTM());
			log.info("Socket opened");	
			/*SENDING PCEP MESSAGE*/
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			try 
			{
				//log.info(" Workflow pcepInit.getLength()::"+pcepInit.getBytes());
				pcepInit.encode();
				log.fine("Sending message");
				outToServer.write(pcepInit.getBytes());
				outToServer.flush();
			} 
			catch (Exception e) 
			{
				log.info(UtilsFunctions.exceptionToString(e));
			}
			//readMsg
			/*
			InputStream iosocket = clientSocket.getInputStream();
			while(iosocket.available()<=0){
				try {
					Thread.currentThread().sleep(10);
					System.out.println("Waiting to report...");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}*/
		
		} 
		catch (IOException e) 
		{
			log.severe("Couldn't get I/O for connection to port" + params.getPcepPortTM());
		} 
		
		
		//Wait and Read response (PCEP Report)
		try {

			DataInputStream inFromServer = new DataInputStream (clientSocket.getInputStream());

			log.info("Provisionig Manager: Waiting for Report...");
			byte[] msg=null;
			msg=readMsg(inFromServer);
			System.out.println("PM: DONE!");
			this.reportResp = new PCEPReport(msg);
			//System.out.println(reportResp.toString());
			log.info("Report Receive: "+this.reportResp.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * It should really receive a PCEPInitiate but for commodity of developers it receives PCEPResponse
	 * @param pcepResponse
	 */
	protected void callProvisioningManager(PCEPResponse pcepResponse) 
	{
		//log.info("Response " +pcepResponse.getResponseList().get(0).getPath(0).geteRO());


		log.info("Opening new PCEP Session  on port" + params.getPcepPortTM());

		PCEPInitiate pceInit = new PCEPInitiate();
		//For the time being, no need to put anything here
		SRP rsp = new SRP();

		//For the time being, no need to put anything here
		LSP lsp = new LSP();

		EndPointsIPv4 endP = new EndPointsIPv4();
		//Esto no se lee en ningun en ningun lado
		try 
		{
			Inet4Address source = (Inet4Address) Inet4Address.getByName("192.168.1.1");
			Inet4Address dest = (Inet4Address)Inet4Address.getByName("192.168.1.3");

			endP.setSourceIP(source);
			endP.setDestIP(dest);
		} 
		catch (UnknownHostException e) 
		{
			log.info(UtilsFunctions.exceptionToString(e));
		}


		ExplicitRouteObject ero;
		ero = (pcepResponse.getResponse(0).getPath(0).geteRO());

		PCEPIntiatedLSP pcepIntiatedLSPList = new PCEPIntiatedLSP();
		pcepIntiatedLSPList.setEro(ero);
		pcepIntiatedLSPList.setRsp(rsp);
		pcepIntiatedLSPList.setLsp(lsp);
		pcepIntiatedLSPList.setEndPoint(endP);
		//FIXME
		pcepIntiatedLSPList.setBandwidth((BandwidthRequested)pcepResponse.getResponse(0).getBandwidth()); //Asumo que este bw funciona. Revisar si falla.

		pceInit.getPcepIntiatedLSPList().add(pcepIntiatedLSPList);

		try 
		{
			Socket clientSocket = new Socket(params.getPMAddress(), params.getPcepPortTM());			
			log.info("Socket opened");	
			/*SENDING PCEP MESSAGE*/
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			try 
			{
				pceInit.encode();
				log.fine("Sending message");
				outToServer.write(pceInit.getBytes());
				outToServer.flush();
			} 
			catch (Exception e) 
			{
				log.info(UtilsFunctions.exceptionToString(e));
			}
		} 
		catch (IOException e) 
		{
			log.severe("Couldn't get I/O for connection to port" + params.getPcepPortTM());
		} 
	}
	protected void callProvisioningManager(PCEPResponse pcepResponse, String source, String dest, float bandwidth, boolean delete, long id) 
	{
		//log.info("Response " +pcepResponse.getResponseList().get(0).getPath(0).geteRO());


		log.info("Opening new PCEP Session  on port" + params.getPcepPortTM());

		PCEPInitiate pceInit = new PCEPInitiate();
		//For the time being, no need to put anything here
		SRP rsp = new SRP();
		if (delete){
			rsp.setrFlag(true);
		}

		//For the time being, no need to put anything here
		LSP lsp = new LSP();
		PCEPIntiatedLSP pcepIntiatedLSPList = new PCEPIntiatedLSP();
		if (((source==null)||(dest==null))&&delete){
			log.info("Delete service");
			source="0.0.0.0";
			dest="0.0.0.0";
		}

		if (source.length()<16){
			EndPointsIPv4 endP = new EndPointsIPv4();
			//Esto no se lee en ningun en ningun lado
			try 
			{
				Inet4Address src = (Inet4Address) Inet4Address.getByName(source);
				Inet4Address dst = (Inet4Address)Inet4Address.getByName(dest);

				endP.setSourceIP(src);
				endP.setDestIP(dst);
				pcepIntiatedLSPList.setEndPoint(endP);

			} 
			catch (UnknownHostException e) 
			{
				log.info(UtilsFunctions.exceptionToString(e));
			}
		} else {
			EndPointDataPathID endP=new EndPointDataPathID();
			endP.setDestSwitchID(dest);
			endP.setSourceSwitchID(source);
			pcepIntiatedLSPList.setEndPoint(endP);

		}

		ExplicitRouteObject ero=null;
		if (pcepResponse.getResponse(0)==null)
			ero=new ExplicitRouteObject();

		else if (pcepResponse.getResponse(0).getPathList().size()==0)
			ero=new ExplicitRouteObject();

		else if (pcepResponse.getResponse(0).getPath(0).geteRO()==null)
			ero=new ExplicitRouteObject();
		else if (pcepResponse!=null)
			ero = (pcepResponse.getResponse(0).getPath(0).geteRO());

		pcepIntiatedLSPList.setEro(ero);
		pcepIntiatedLSPList.setRsp(rsp);
		pcepIntiatedLSPList.setLsp(lsp);
		pcepIntiatedLSPList.getLsp().setLspId((int)id);
		//pcepIntiatedLSPList.setEndPoint(endP);
		BandwidthRequested bw= new BandwidthRequested();
		bw.setBw(bandwidth);
		pcepIntiatedLSPList.setBandwidth(bw); //Asumo que este bw funciona. Revisar si falla.


		pceInit.getPcepIntiatedLSPList().add(pcepIntiatedLSPList);

		//Esto era de Jaume
		//Socket clientSocket = callProvisioningManager((PCEPMessage)pceInit);			
		Socket clientSocket = null;			

		try 
		{
			clientSocket = new Socket(params.getPMAddress(), params.getPcepPortTM());			
			log.info("Socket opened");	
			/*SENDING PCEP MESSAGE*/
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			try 
			{
				pceInit.encode();
				log.fine("Sending message");
				outToServer.write(pceInit.getBytes());
				outToServer.flush();
			} 
			catch (Exception e) 
			{
				log.info(UtilsFunctions.exceptionToString(e));
			}
		} 
		catch (IOException e) 
		{
			log.severe("Couldn't get I/O for connection to port" + params.getPcepPortTM());
		} 
		try {

			DataInputStream inFromServer = new DataInputStream (clientSocket.getInputStream());

			System.out.println("VNTMClient: Waiting for Response...");
			int counter=0;
			byte[] msg=null;
			Thread.currentThread().sleep(1000);

			while ((counter<200)&&(msg==null)){		
				try{
					System.out.println("waiting...."+counter);

					msg=readMsg(inFromServer);
					counter++;
				} catch(Exception e){
					counter++;
					e.printStackTrace();
				}
			}
			System.out.println("VNTMClient: DONE!");
			this.reportResp = new PCEPReport(msg);
			//System.out.println(reportResp.toString());
			log.info("Report Receive: "+this.reportResp.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	protected void callProvisioningManagerToUpdate(PCEPResponse pcepResponse, String source, String dest, float bandwidth, boolean delete, long id) 
	{
		//log.info("Response " +pcepResponse.getResponseList().get(0).getPath(0).geteRO());


		log.info("Update PCEP Session  on port" + params.getPcepPortTM());

		PCEPUpdate pceUdp = new PCEPUpdate();
		//For the time being, no need to put anything here
		SRP rsp = new SRP();
		if (delete){
			rsp.setrFlag(true);
		}

		//For the time being, no need to put anything here
		LSP lsp = new LSP();
		UpdateRequest pcepUpdateLSPList = new UpdateRequest();
		if (((source==null)||(dest==null))&&delete){
			log.info("Delete service");
			source="0.0.0.0";
			dest="0.0.0.0";
		}

//		if (source.length()<16){
//			EndPointsIPv4 endP = new EndPointsIPv4();
//			//Esto no se lee en ningun en ningun lado
//			try 
//			{
//				Inet4Address src = (Inet4Address) Inet4Address.getByName(source);
//				Inet4Address dst = (Inet4Address)Inet4Address.getByName(dest);
//
//				endP.setSourceIP(src);
//				endP.setDestIP(dst);
//				pcepUpdateLSPList. setEndPoint(endP);
//
//			} 
//			catch (UnknownHostException e) 
//			{
//				log.info(UtilsFunctions.exceptionToString(e));
//			}
//		} else {
//			EndPointDataPathID endP=new EndPointDataPathID();
//			endP.setDestSwitchID(dest);
//			endP.setSourceSwitchID(source);
//			pcepUpdateLSPList.setEndPoint(endP);
//
//		}

		ExplicitRouteObject ero=null;
		if (pcepResponse.getResponse(0)==null)
			ero=new ExplicitRouteObject();

		else if (pcepResponse.getResponse(0).getPathList().size()==0)
			ero=new ExplicitRouteObject();

		else if (pcepResponse.getResponse(0).getPath(0).geteRO()==null)
			ero=new ExplicitRouteObject();
		else if (pcepResponse!=null)
			ero = (pcepResponse.getResponse(0).getPath(0).geteRO());

//		pcepUpdateLSPList.setEro(ero);
//		pcepUpdateLSPList.setRsp(rsp);
//		pcepUpdateLSPList.setLsp(lsp);
//		pcepUpdateLSPList.getLsp().setLspId((int)id);
//		//pcepIntiatedLSPList.setEndPoint(endP);
//		BandwidthRequested bw= new BandwidthRequested();
//		bw.setBw(bandwidth);
//		pcepUpdateLSPList.setBandwidth(bw); //Asumo que este bw funciona. Revisar si falla.


		pceUdp.getUpdateRequestList().add(pcepUpdateLSPList);

		//Esto era de Jaume
		//Socket clientSocket = callProvisioningManager((PCEPMessage)pceInit);			
		Socket clientSocket = null;			

		try 
		{
			clientSocket = new Socket(params.getPMAddress(), params.getPcepPortTM());			
			log.info("Socket opened");	
			/*SENDING PCEP MESSAGE*/
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			try 
			{
				pceUdp.encode();
				log.fine("Sending message");
				outToServer.write(pceUdp.getBytes());
				outToServer.flush();
			} 
			catch (Exception e) 
			{
				log.info(UtilsFunctions.exceptionToString(e));
			}
		} 
		catch (IOException e) 
		{
			log.severe("Couldn't get I/O for connection to port" + params.getPcepPortTM());
		} 
		try {

			DataInputStream inFromServer = new DataInputStream (clientSocket.getInputStream());

			System.out.println("VNTMClient: Waiting for Response...");
			int counter=0;
			byte[] msg=null;
			Thread.currentThread().sleep(1000);

			while ((counter<200)&&(msg==null)){		
				try{
					System.out.println("waiting...."+counter);

					msg=readMsg(inFromServer);
					counter++;
				} catch(Exception e){
					counter++;
					e.printStackTrace();
				}
			}
			System.out.println("VNTMClient: DONE!");
			this.reportResp = new PCEPReport(msg);
			//System.out.println(reportResp.toString());
			log.info("Report Receive: "+this.reportResp.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	protected byte[] readMsg(DataInputStream in) throws IOException{
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
				log.warning("Error reading data: "+ e.getMessage());
				throw e;
			}catch (Exception e) {
				log.warning("readMsg Oops: " + e.getMessage());
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
				log.warning("End of stream has been reached");
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
