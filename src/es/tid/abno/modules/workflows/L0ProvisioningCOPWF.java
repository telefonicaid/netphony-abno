package es.tid.abno.modules.workflows;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.tid.abno.modules.ABNOParameters;
import es.tid.abno.modules.Path_Computation;
import es.tid.abno.modules.database.OpTable;
import es.tid.pce.pcep.messages.PCEPInitiate;
import es.tid.pce.pcep.messages.PCEPMessage;
import es.tid.pce.pcep.messages.PCEPReport;
import es.tid.pce.pcep.messages.PCEPResponse;
import es.tid.pce.pcep.objects.GeneralizedEndPoints;
import tid.ipnms.json.JSONException;
import tid.ipnms.json.JSONObject;

/**
 * 
 * @author b.mvas b.jmgj
 * Examples of use:
 * curl 'localhost:4445?Operation_Type=L0ProvisioningWF&Source_Node=00:00:00:00:00:00:FF:01&Destination_Node=00:00:00:00:00:00:03:04&source_interface=3&destination_interface=2&Operation=add&ID_Operation=1234&Bandwidth=30000'
 * curl 'localhost:4445?Operation_Type=L0ProvisioningWF&Source_Node=00:00:00:00:00:00:FF:01&Destination_Node=00:00:00:00:00:00:03:04&source_interface=3&destination_interface=2&Exclude_Node=00:00:00:00:00:00:03:03:01&Operation=add&ID_Operation=1234&Bandwidth=30000'
 * 
 */

public class L0ProvisioningCOPWF extends WorkflowCOP
{
	public static String add_mult_vlan = "add_mult_vlan";
	private String operation;
	private String source;
	private String destination;
	private Integer source_interface=-1;
	private Integer destination_interface=-1;
	private String excludeNode;
	private String nodeExclude;	// Substring from excludeNode for dpid
	private long portExclude;
	private String OFCode;
	private int m=0;
	private String ero;
	private PCEPMessage responseToInitiate; //In the better case could be a report message
	


	public L0ProvisioningCOPWF(Hashtable<String, String> request, String response, LinkedList<Path_Computation> path_Computationlist, ABNOParameters params, HashMap<Integer, OpTable> oPtable) 
	{
		super(request, response, path_Computationlist, params, oPtable);
	}

	@Override
	public void handleRequest() 
	{
		
		//Get parameters from request
		String idOperation = request.get("ID_Operation");
		this.operation = request.get("Operation");
		this.excludeNode = request.get("Exclude_Node");
		this.OFCode=request.get("OF");
		if (request.get("m")!=null){
			this.m=Integer.parseInt(request.get("m"));
		}else this.m=-1;
		log.info("jm m: "+m);
		this.ero = request.get("ERO");
		
		String remoteAddr = request.get("remoteAddr");
		String bw=request.get("Bandwidth");
		
		
		boolean delete=false;
		boolean update=false;
		float bandwidth=0;
		long time_start = System.currentTimeMillis();
		System.out.println("<REQ_L0PROV> "+time_start);
		
		response = "probando response denetro del workwlow";
		
		
		
		if (this.operation.equals("update")){
			
			//this.source = this.oPtable.get(Integer.parseInt(idOperation)).getSrcNode();
			//this.destination = this.oPtable.get(Integer.parseInt(idOperation)).getDstNode();
			//this.source_interface = this.oPtable.get(Integer.parseInt(idOperation)).getSrcPort();
			//this.destination_interface = this.oPtable.get(Integer.parseInt(idOperation)).getDstPort();
			//Asumimos que en la entrada ya habia puertos, debería comprobarse
			
		} else{//for add and del
			
			this.source = request.get("Source_Node");
			this.destination = request.get("Destination_Node");
			
			
			
			this.OFCode=request.get("OF");



			if (request.get("source_interface")!=null && request.get("destination_interface")!=null){
				source_interface = Integer.parseInt(request.get("source_interface"));
				destination_interface = Integer.parseInt(request.get("destination_interface"));
			}
			
		}
		

	
		if (this.operation.equals("del")){
			delete = true;
		}else if (this.operation.equals("update")){
			update = true;
		}else if (this.operation.equals("add")){
			idOperation=String.valueOf(serviceCounter.incrementAndGet());
		}

		

		if (idOperation!=null)
			if (check4Services(idOperation))
				return;		

		int oFcode=1;
		if (OFCode!=null)
			oFcode=Integer.parseInt(OFCode);
		
		System.out.println("Sending Request to PCE-L0.");
		PCEPResponse pcepResponsel0ML=null;
		
		/*
		 * XRO 
		 */
		if (this.excludeNode!=null){

			nodeExclude=excludeNode.substring(0, 23);
			portExclude=conversionXROport(excludeNode);	
			log.info("Exclude_Node is:: " + nodeExclude + ":" + portExclude);
		
		}
		
		//Obtain ERO (throw curl or request to pce)

		if (this.ero != null){
			log.info("jm curl with ERO not supported yet");
		}
		else if (this.operation.equals("add")){
			if (m!=-1){//MediaChannel
				//L0ProvisioningWF could be obtain throw request.getParameter("Operation_Type");
				oFcode= params.getPolicy().get("L0ProvisioningWF").getMediaChannel().getOfCode();
				pcepResponsel0ML = path_Computationlist.getFirst().calculateMediaChannelPath(source, destination, source_interface, destination_interface, m, oFcode);
				log.info("Finish calculatePath MediaChannel");
				
			}else if (bw!=null){
				log.info("Bandwith is: "+bw.toString());
				bandwidth=Float.parseFloat(bw);
				//pcepResponsel0ML = path_Computationlist.getFirst().calculatePath(source,ip_dest, bandwidth);
				pcepResponsel0ML = path_Computationlist.getFirst().calculatePath(source, destination, source_interface, destination_interface, bandwidth, oFcode,nodeExclude,portExclude);
				log.info("Finish calculatePath");
			}else{
				//Does not have Bandwidth parameter
				pcepResponsel0ML = path_Computationlist.getFirst().calculatePath(source,destination, 0);
			}
		}
			

		
		
		if (delete){
			long id=this.oPtable.get(Integer.parseInt(idOperation)).getPCCoperationID();
			callProvisioningManager(pcepResponsel0ML, this.source, this.destination,(float) bandwidth, delete, id);
		}else if (this.operation.equals("update")){
			
			
			if (pcepResponsel0ML.getResponseList().get(0).getNoPath()!=null){
				System.out.println("PCE-l0 returns NO PATH. Error.");
			}else{
				log.info("Sending PCEP update to Provisioning Manager");
				callProvisioningManager(responseTOupdate(pcepResponsel0ML));
				log.info("Finish callProvisioningManagerToUpdate");
			}
			
		}else{
			if (pcepResponsel0ML.getResponseList().get(0).getNoPath()!=null){
				System.out.println("ERROR. PCE-l0 returns NO PATH.");
			}else{
				log.info("jm isPCEInstantiation: "+params.getPolicy().get("L0ProvisioningWF").getL0pceCapabilities().getInstantiation());
				if (params.getPolicy().get("L0ProvisioningWF").getL0pceCapabilities().getInstantiation()){ // PCE Statefull with instantiation capability
					
					log.info("Sending PCEP Initiate to PCE");
					
		
					//respIni= callPCE(responseTOinitiate(pcepResponsel0ML,m));
					PCEPInitiate pcepInit = responseTOinitiate(pcepResponsel0ML,m);
					GeneralizedEndPoints endPointsInitiate = new GeneralizedEndPoints();
					endPointsInitiate = Path_Computation.createGeneralizedEndpoints(source, source_interface, destination, destination_interface);
					pcepInit.getPcepIntiatedLSPList().get(0).setEndPoint(endPointsInitiate);
					responseToInitiate=callPCE(pcepInit); //m could value -1 if MediaChannel case
					log.info("Finish callPCE");
					
									
				}else {
					log.info("Sending PCEP Initiate to Provisioning Manager");
					callProvisioningManager(responseTOinitiate(pcepResponsel0ML));
					log.info("Finish callProvisioningManager");
				}
				
			}	
		}
		
		//this.oPtable.containsKey(Integer.parseInt(idOperation));
		log.info("jm ver tabla para idoperacion: "+this.oPtable.get(Integer.parseInt(idOperation)));
		
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("ID_Operation", idOperation);
			jsonParams.put("Operation_Type","L0_PROVISIONING");
			jsonParams.put("Source_Node",source);
			jsonParams.put("Destination_Node",destination);
			jsonParams.put("Source_interface", source_interface);
			jsonParams.put("Destination_interface", destination_interface);
			jsonParams.put("Result","L0_PATH_CONFIGURED");
			jsonParams.put("Error_Code","NO_ERROR");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		response = jsonParams.toString();

		long time_end = System.currentTimeMillis();
		System.out.println("<RESP_L0PROV> "+time_end);
		if (operation.equals("add")){
			int idLSP=-1;
			if (responseToInitiate!=null){
				if (responseToInitiate instanceof PCEPReport){
					idLSP = ((PCEPReport) responseToInitiate).getStateReportList().get(0).getLSP().getLspId();
				}
			}
			this.oPtable.put(Integer.parseInt(idOperation), new OpTable(remoteAddr,params.getPMAddress(), String.valueOf(params.getPcepPortPM()), idLSP, "L0ProvisioningWF"));
		}else{
			this.oPtable.remove(Integer.parseInt(idOperation));
		}
		
		//update message
//		if (operation.equals("update")){
//			this.oPtable.put(Integer.parseInt(idOperation), new OpTable(request.getRemoteAddr(),params.getPMAddress(), String.valueOf(params.getPcepPortPM()), 0, "L0ProvisioningWF"));
//		}else{
//			this.oPtable.remove(Integer.parseInt(idOperation));
//		}
		log.info("jm ver gt pccoperationid"+this.oPtable.get(Integer.parseInt(idOperation)).getPCCoperationID());
		this.printOPTable();
	}

	private long conversionXROport (String node){
		long portExclude = 0;

/*		if (node.length()==26) // Port with zero
			portExclude = Long.parseLong(node.substring(25, 26));

		if (node.length()==25) // Port without zero*/
		
		portExclude = Long.parseLong(node.substring(node.length()-1, node.length()));

		return portExclude;

	}

	private boolean check4Services(String idOperation){
		if (this.oPtable.containsKey(Integer.parseInt(idOperation))&& this.operation.equals("add")){
			JSONObject jsonParams = new JSONObject();
			try {
				jsonParams.put("ID_Operation", idOperation);
				jsonParams.put("Operation_Type","L0_PROVISIONING");
				jsonParams.put("Source_Node",source);
				jsonParams.put("Destination_Node",destination);
				jsonParams.put("Result","ID_ALREADY_EXISTS");
				jsonParams.put("Error_Code","ID_ERROR");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			response = jsonParams.toString();
			return true;
		}

		try{
			//Thread.currentThread().sleep(6000);
		}catch (Exception e){
			e.printStackTrace();
		}
		
		if (!this.oPtable.containsKey(Integer.parseInt(idOperation))&& this.operation.equals("update")){
			JSONObject jsonParams = new JSONObject();
			try {
				jsonParams.put("ID_Operation", idOperation);
				jsonParams.put("Operation_Type","L0_PROVISIONING");
				jsonParams.put("Source_Node",source);
				jsonParams.put("Destination_Node",destination);
				jsonParams.put("Result","Update:ID_doesnt_Exists");
				jsonParams.put("Error_Code","ID_ERROR");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			response = jsonParams.toString();
			return true;
		}

		try{
			//Thread.currentThread().sleep(6000);
		}catch (Exception e){
			e.printStackTrace();
		}
		

		if (!this.oPtable.containsKey(Integer.parseInt(idOperation)) && this.operation.equals("del")){
			JSONObject jsonParams = new JSONObject();
			try {
				jsonParams.put("ID_Operation", idOperation);
				jsonParams.put("Operation_Type","L0_PROVISIONING");
				jsonParams.put("Source_Node",source);
				jsonParams.put("Destination_Node",destination);
				jsonParams.put("Result","Delete:ID_doesnt_Exists");
				jsonParams.put("Error_Code","ID_ERROR");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			response = jsonParams.toString();
			return true;
		}
		return false;

	}

}