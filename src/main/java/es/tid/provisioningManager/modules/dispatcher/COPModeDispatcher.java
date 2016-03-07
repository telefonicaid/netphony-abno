package es.tid.provisioningManager.modules.dispatcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.logging.Logger;

import es.tid.rsvp.objects.subobjects.EROSubobject;

public class COPModeDispatcher {
	
	private Logger log = Logger.getLogger("Dispatcher");
	
	public void sendRequest(InfoDispatcher infoDispatcher){
		
		log.info("#### Launching COPMode dispathcer...");
		
		String callId = "1";
		String aEnd = null;
		String aEnd_routerId = infoDispatcher.getSourceNode();
		String aEnd_interfaceId = ""+infoDispatcher.getSourceIntf();
		if (aEnd_interfaceId.equals("-1")){
			String aEnd_endpointId = infoDispatcher.getHashEroNode().get(infoDispatcher.getSourceNode()).getNodeID();
			aEnd = "\"aEnd\":{\"routerId\":\""+aEnd_routerId+"\",\"endpointId\":\""+aEnd_endpointId+"\"}";
			
		}else{
			String aEnd_endpointId = infoDispatcher.getHashEroNode().get(infoDispatcher.getSourceNode()).getNodeID()+"_"+infoDispatcher.getSourceIntf();
			aEnd = "\"aEnd\":{\"routerId\":\""+aEnd_routerId+"\",\"interfaceId\":\""+aEnd_interfaceId+"\",\"endpointId\":\""+aEnd_endpointId+"\"}";
			
		}
		
		String zEnd = null;
		String zEnd_routerId = infoDispatcher.getDestNode();
		String zEnd_interfaceId = ""+infoDispatcher.getDestIntf();
		if (zEnd_interfaceId.equals("-1")){
			String zEnd_endpointId = infoDispatcher.getHashEroNode().get(infoDispatcher.getDestNode()).getNodeID();
			zEnd = "\"zEnd\":{\"routerId\":\""+zEnd_routerId+"\",\"endpointId\":\""+zEnd_endpointId+"\"}";
			
		}else{
			String zEnd_endpointId = infoDispatcher.getHashEroNode().get(infoDispatcher.getDestNode()).getNodeID()+"_"+infoDispatcher.getDestIntf();
			zEnd = "\"zEnd\":{\"routerId\":\""+zEnd_routerId+"\",\"interfaceId\":\""+zEnd_interfaceId+"\",\"endpointId\":\""+zEnd_endpointId+"\"}";
			
		}
		
		
		//Include path in request
		//IF ERO size are less than 2, only include endpoints and we are not send path object
		String path = null;
		if (infoDispatcher.getEro().getEROSubobjectList().size() > 2){
			String topo_components = "";
			Iterator<EROSubobject> iterEROSubobjectList = infoDispatcher.getEro().getEROSubobjectList().iterator();
			int indice = 0;
			while(iterEROSubobjectList.hasNext()){
				indice++;
				EROSubobject eroSubobject = iterEROSubobjectList.next();
				
				String endpoint = null;
				String endpoint_routerId = eroSubobject.getstringNodeEROSubobject(eroSubobject);
				String endpoint_interfaceId = ""+eroSubobject.getIntfEROSubobject(eroSubobject);
				
				
				if (endpoint_interfaceId.equals("-1")){
					String endpoint_endpointId = infoDispatcher.getHashEroNode().get(endpoint_routerId).getNodeID();
					endpoint = "\"end"+indice+"\":{\"routerId\":\""+endpoint_routerId+"\",\"endpointId\":\""+endpoint_endpointId+"\"}";
					
				}else{
					String endpoint_endpointId = infoDispatcher.getHashEroNode().get(endpoint_routerId).getNodeID()+"_"+endpoint_interfaceId;
					endpoint = "\"end"+indice+"\":{\"routerId\":\""+endpoint_routerId+"\",\"interfaceId\":\""+endpoint_interfaceId+"\",\"endpointId\":\""+endpoint_endpointId+"\"}";
					
				}
								
				topo_components = topo_components + endpoint;
				if (iterEROSubobjectList.hasNext()){
					topo_components = topo_components + ",";
				}
				
				
			}
			path = "\"path\":{"+topo_components+"}";
		}
				
		
		String tparams = "\"trafficParams\":{\"latency\":100,\"reservedBandwidth\":100000000}";
		
		String tlayer = "\"transportLayer\":{\"layer\":\"ethernet\", \"direction\":\"bidir\"}";
		
		
		String jsonCOP = "{\"callId\":\""+callId+"\","+aEnd+","+zEnd+",";
		if (path!=null) jsonCOP = jsonCOP + path + ",";
		jsonCOP = jsonCOP + tparams+","+tlayer+"}";
		
		String curl_string = "time curl -X POST -H \"Content-type:application/json\" -u admin:pswd1 http://"+ infoDispatcher.getControllerIP() +":"+ infoDispatcher.getControllerPort() +"/restconf/config/calls/call/"+callId+"/ -d '"+jsonCOP+"'";
		//String curl_string = "date";
		//log.info("Sending curl: "+ curl_string);
			
		String[] curl_array = new String[] {
		"bash",
		"-c",
		curl_string
		};
		
				
		try {
			log.info("Executing command...");
			Process proc_curl = Runtime.getRuntime().exec(curl_array);
			proc_curl.waitFor();
            InputStream is = proc_curl.getInputStream();
            BufferedReader br = new BufferedReader (new InputStreamReader (is));
            String aux = br.readLine();
            String output_curl="";
            while (aux!=null)
            {
            	output_curl += aux+"\n";
                aux = br.readLine();
            }
            log.info("-----------------------Command to execute-----------------------\n"+curl_string+"\n"+
            		"-----------------------Output of command execute-----------------------\n"+output_curl+
            		"--------------------------------------------------------------------------");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.info("ERROR to execute command (curl)");
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
}
