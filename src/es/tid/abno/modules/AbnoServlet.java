package es.tid.abno.modules;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.tid.abno.modules.database.OpTable;
import es.tid.abno.modules.workflows.Workflow;
//import tid.abno.modules.workflows.Workflow;
import es.tid.pce.pcep.PCEPProtocolViolationException;
import es.tid.pce.pcep.messages.PCEPMessageTypes;
import es.tid.pce.pcep.messages.PCEPResponse;
import es.tid.pce.pcep.messages.PCEPTELinkSuggestion;
import es.tid.util.UtilsFunctions;

@SuppressWarnings("serial")
public class AbnoServlet extends HttpServlet {
	public static final Logger log =Logger.getLogger("AbnoServlet");
	private LinkedList<Path_Computation> path_Computationlist;	
	private ABNOParameters params;
	private HashMap<Integer, OpTable> opTable;

	/**
	 * Constructor for the ABNO mode
	 * @param path_Computationl3
	 * @param params
	 * @param oPtable 
	 */
	public AbnoServlet(LinkedList<Path_Computation> path_Computationlist, ABNOParameters params, HashMap<Integer, OpTable> oPtable ){
		this.path_Computationlist = path_Computationlist;
		this.params = params;
		this.opTable=oPtable;
	}
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String operation = null;
		log.info("Received Something!!!!");
		
		/*Read JSON*/
		/*
		RequestBufferReader requestBufferReader = new RequestBufferReader(request);	
		JSONObject requestPath = null;
		try {
			requestPath =  new JSONObject(requestBufferReader.getRequestBuffer().toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ip_source = requestPath.getString("source");
			ip_dest = requestPath.getString("dest");
			log.info("Source :"+ip_source+"Dest"+ip_dest);
		}
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		/*Call Path Computation*/
		//ip_source = "10.95.73.72";
		//ip_dest = "10.95.73.74"; 
		
		//By the time being a way to call this is
		//curl 'localhost:4445?source=192.168.1.1&destination=192.168.1.2'
		//curl 'localhost:4445?source=192.168.1.1&destination=192.168.1.2&operation=WLAN_PATH_PROVISIONING'
		//curl 'localhost:4445?source=00:14:2c:59:e5:5e:2b:00&destination=00:14:2c:59:e5:66:ed:00&source_mac=00:1a:70:10:3a:d4&dest_mac=00:1e:c9:bb:7e:54&source_interface=20&destination_interface=20&operation=WLAN_PATH_PROVISIONING'
		//curl 'localhost:4445?source=192.168.1.1&destination=192.168.1.2&operation=IP_PROVISIONING'
		
		/*
		 * operation can be:
		 * WLAN_PATH_PROVISIONING
		 * ...
		 */
		   
		String workflowParam = request.getParameter("Operation_Type");
		log.info("request.getParameter(\"Operation_Type\"):::"+request.getParameter("Operation_Type"));
		
		try 
		{
			Class<?> act = Class.forName("es.tid.abno.modules.workflows."+workflowParam);
			
			Class[] cArg = new Class[5];
			
			cArg[0] = HttpServletRequest.class;
			cArg[1] = HttpServletResponse.class;
			cArg[2] = LinkedList.class;
			cArg[3] = ABNOParameters.class;
			cArg[4] = HashMap.class;
			
			Object[] args = new Object[5];
			args[0] = request;
			args[1] = response;
			args[2] = path_Computationlist;
			args[3] = params;
			args[4] = this.opTable;
			
			Workflow workflow = (Workflow)act.getDeclaredConstructor(cArg).newInstance(args);
			
			workflow.handleRequest();
		}
		catch (Exception e1)
		{
			log.info(UtilsFunctions.exceptionToString(e1));			
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
	}
	/*
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		this.doGet(request, response);
	}
	*/
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
		
		StringBuilder sb = new StringBuilder();
		
		
	    BufferedReader reader = request.getReader();
	    try {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            sb.append(line).append('\n');
	        }
	    } finally {
	        reader.close();
	    }
	    System.out.println(sb.toString());
		
		String workflowParam = request.getParameter("Operation_Type");
		log.info("request.getParameter(\"Operation_Type\"):::"+request.getParameter("Operation_Type"));
		
		try 
		{
			Class<?> act = Class.forName("tid.abno.modules.workflows."+workflowParam);
			
			Class[] cArg = new Class[5];
			
			cArg[0] = HttpServletRequest.class;
			cArg[1] = HttpServletResponse.class;
			cArg[2] = LinkedList.class;
			cArg[3] = ABNOParameters.class;
			cArg[4] = HashMap.class;
			
			Object[] args = new Object[5];
			args[0] = request;
			args[1] = response;
			args[2] = path_Computationlist;
			args[3] = params;
			args[4] = this.opTable;
			
			Workflow workflow = (Workflow)act.getDeclaredConstructor(cArg).newInstance(args);
			
			workflow.handleRequest(sb.toString());
		}
		catch (Exception e1)
		{
			log.info(UtilsFunctions.exceptionToString(e1));			
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
	}
	
	private void callProvisioningManager(PCEPResponse pcepResponse) {
		//log.info("Response " +pcepResponse.getResponseList().get(0).getPath(0).geteRO());
		
		pcepResponse.setMessageType(PCEPMessageTypes.MESSAGE_INITIATE);
		
		log.info("Opening new PCEP Session  on port" + params.getPcepPortTM());
		
		try {
			@SuppressWarnings("resource")
			Socket clientSocket = new Socket("localhost", params.getPcepPortTM());			
			log.info("Socket opened");	
			/*SENDING PCEP MESSAGE*/
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			try {
				pcepResponse.encode();
			} catch (PCEPProtocolViolationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				log.fine("Sending message");
				outToServer.write(pcepResponse.getBytes());
				outToServer.flush();
			} catch (IOException e) {
				log.warning("Error sending msg: " + e.getMessage());
			}
		} catch (IOException e) {
			log.severe("Couldn't get I/O for connection to port" + params.getPcepPortTM());
			//FIXME: Send message to controller		
		} 
	}
	
	

}
