package es.tid.abno.modules.workflows;

import java.util.HashMap;
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
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author b.mvas b.jmgj
 * Examples of use:
 * curl 'localhost:4445?Operation_Type=L0ProvisioningWF&Source_Node=00:00:00:00:00:00:FF:01&Destination_Node=00:00:00:00:00:00:03:04&source_interface=3&destination_interface=2&Operation=add&ID_Operation=1234&Bandwidth=30000'
 * curl 'localhost:4445?Operation_Type=L0ProvisioningWF&Source_Node=00:00:00:00:00:00:FF:01&Destination_Node=00:00:00:00:00:00:03:04&source_interface=3&destination_interface=2&Exclude_Node=00:00:00:00:00:00:03:03:01&Operation=add&ID_Operation=1234&Bandwidth=30000'
 * 
 */

public class L0ProvisioningWF extends Workflow
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
	


	public L0ProvisioningWF(HttpServletRequest request, HttpServletResponse response, LinkedList<Path_Computation> path_Computationlist, ABNOParameters params, HashMap<Integer, OpTable> oPtable) 
	{
		super(request, response, path_Computationlist, params, oPtable);
	}

	@Override
	public void handleRequest() 
	{
		boolean delete=false;
		boolean update=false;
		float bandwidth=0;
		long time_start = System.currentTimeMillis();
		System.out.println("<REQ_L0PROV> "+time_start);
		
		String idOperation = request.getParameter("ID_Operation");
		this.operation = request.getParameter("Operation");
		this.excludeNode = request.getParameter("Exclude_Node");
		this.OFCode=request.getParameter("OF");
		
		if (request.getParameter("m")!=null){
			this.m=Integer.parseInt(request.getParameter("m"));
		}else this.m=-1;
		log.info("jm m: "+m);
		
		this.ero = request.getParameter("ERO");
		
		if (this.operation.equals("update")){
			
			//this.source = this.oPtable.get(Integer.parseInt(idOperation)).getSrcNode();
			//this.destination = this.oPtable.get(Integer.parseInt(idOperation)).getDstNode();
			//this.source_interface = this.oPtable.get(Integer.parseInt(idOperation)).getSrcPort();
			//this.destination_interface = this.oPtable.get(Integer.parseInt(idOperation)).getDstPort();
			//Asumimos que en la entrada ya habia puertos, debería comprobarse
			
		} else{//for add and del
			
			this.source = request.getParameter("Source_Node");
			this.destination = request.getParameter("Destination_Node");
			
			
			
			this.OFCode=request.getParameter("OF");



			if (request.getParameter("source_interface")!=null && request.getParameter("destination_interface")!=null){
				source_interface = Integer.parseInt(request.getParameter("source_interface"));
				destination_interface = Integer.parseInt(request.getParameter("destination_interface"));
			}
			
		}
		

	
		if (this.operation.equals("del")){
			delete = true;
		}else if (this.operation.equals("update")){
			update = true;
		}else if (this.operation.equals("add")){
			idOperation=String.valueOf(serviceCounter.incrementAndGet());
		}

		String bw=request.getParameter("Bandwidth");

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
			

		
		log.info(" XXXX Before Delete");
		if (delete){
			
			log.info(" XXXX Delete");
			
			log.info(" XXXX isPCEInstantiation: "+params.getPolicy().get("L0ProvisioningWF").getL0pceCapabilities().getInstantiation());
			if (params.getPolicy().get("L0ProvisioningWF").getL0pceCapabilities().getInstantiation()==false){
			
			long id=this.oPtable.get(Integer.parseInt(idOperation)).getPCCoperationID();
			callProvisioningManager(pcepResponsel0ML, this.source, this.destination,(float) bandwidth, delete, id);
			
			}else{
				
//				log.info(" XXXX Sending PCEP Initiate Delete to PCE");
//				int id=(int)this.oPtable.get(Integer.parseInt(idOperation)).getPCCoperationID();
//				//callProvisioningManager(pcepResponsel0ML, this.source, this.destination,(float) bandwidth, delete, id);
//				log.info(" XXXX id: "+id);
//				callPCE(delete(pcepResponsel0ML,id));
//				log.info("Finish callPCE Delete");
				
				
				
				
//				if (m!=-1){//MediaChannel
//					//L0ProvisioningWF could be obtain throw request.getParameter("Operation_Type");
//					oFcode= params.getPolicy().get("L0ProvisioningWF").getMediaChannel().getOfCode();
//					pcepResponsel0ML = path_Computationlist.getFirst().calculateMediaChannelPath(source, destination, source_interface, destination_interface, m, oFcode);
//					log.info("Finish calculatePath MediaChannel");
//					
//				}else if (bw!=null){
//					log.info("Bandwith is: "+bw.toString());
//					bandwidth=Float.parseFloat(bw);
//					//pcepResponsel0ML = path_Computationlist.getFirst().calculatePath(source,ip_dest, bandwidth);
//					pcepResponsel0ML = path_Computationlist.getFirst().calculatePath(source, destination, source_interface, destination_interface, bandwidth, oFcode,nodeExclude,portExclude);
//					log.info("Finish calculatePath");
//				}else{
//					//Does not have Bandwidth parameter
//					pcepResponsel0ML = path_Computationlist.getFirst().calculatePath(source,destination, 0);
//				}
				
				
				
				//pcepResponsel0ML = path_Computationlist.getFirst().calculateMediaChannelPath(source, destination, source_interface, destination_interface, m, oFcode);
				//pcepResponsel0ML = path_Computationlist.getFirst().calculatePath(source,destination, 0);
				//log.info(" XXXX pcepResponsel0ML: "+pcepResponsel0ML);
				log.info(" XXXX Sending PCEP Initiate Delete to PCE");
				int id=(int)this.oPtable.get(Integer.parseInt(idOperation)).getPCCoperationID();
				
				log.info(" XXXX id: "+id);
				PCEPInitiate del = delete(id);
				
				GeneralizedEndPoints endPointsInitiate = new GeneralizedEndPoints();
				endPointsInitiate = Path_Computation.createGeneralizedEndpoints(source, source_interface, destination, destination_interface);
				del.getPcepIntiatedLSPList().get(0).setEndPoint(endPointsInitiate);
				log.info(" XXXX del: " +del.toString());
				responseToInitiate=callPCE(del); 
				log.info("Finish callPCE Delete");
				
			}
		
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		replyMessage(jsonParams.toString());

		long time_end = System.currentTimeMillis();
		System.out.println("<RESP_L0PROV> "+time_end);
		if (operation.equals("add")){
			int idLSP=-1;
			if (responseToInitiate!=null){
				if (responseToInitiate instanceof PCEPReport){
					idLSP = ((PCEPReport) responseToInitiate).getStateReportList().get(0).getLSP().getLspId();
				}
			}
			this.oPtable.put(Integer.parseInt(idOperation), new OpTable(request.getRemoteAddr(),params.getPMAddress(), String.valueOf(params.getPcepPortPM()), idLSP, "L0ProvisioningWF"));
		}else{
			log.info(" XXXX Else operation.equals('add')");
			this.oPtable.remove(Integer.parseInt(idOperation));
			log.info(" XXXX Finish remove");
		}
		
		//update message
//		if (operation.equals("update")){
//			this.oPtable.put(Integer.parseInt(idOperation), new OpTable(request.getRemoteAddr(),params.getPMAddress(), String.valueOf(params.getPcepPortPM()), 0, "L0ProvisioningWF"));
//		}else{
//			this.oPtable.remove(Integer.parseInt(idOperation));
//		}
		//log.info("jm ver gt pccoperationid"+this.oPtable.get(Integer.parseInt(idOperation)).getPCCoperationID());
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
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			replyMessage(jsonParams.toString());
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
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			replyMessage(jsonParams.toString());
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
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			replyMessage(jsonParams.toString());
			return true;
		}
		return false;

	}

}