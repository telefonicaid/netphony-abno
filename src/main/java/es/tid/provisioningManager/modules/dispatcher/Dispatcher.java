package es.tid.provisioningManager.modules.dispatcher;

import java.util.logging.Logger;

public class Dispatcher {
	
	private Logger log = Logger.getLogger("Dispatcher");
	
	public void selectDispatcher(InfoDispatcher infoDispatcher){
		
		log.info("Launching Dispatcher ("+infoDispatcher.getConfigurationMode()+") to "+ infoDispatcher.getControllerIP()+":"+infoDispatcher.getControllerPort()+" ; with Endpoints: " +infoDispatcher.getEndpoints().toString()+" ; and ERO: "+infoDispatcher.getEro().toString());
		
		if (infoDispatcher.getConfigurationMode().equals("COPEmulatedMode")){
			
			log.info("Launching COPEmulatedMode dispathcer...");
			log.info("Sendiing request to: "+ infoDispatcher.getControllerIP()+":"+infoDispatcher.getControllerPort()+" ;  with Endpoints: " +infoDispatcher.getEndpoints().toString()+" ; and ERO: "+infoDispatcher.getEro().toString());
			
		}else if (infoDispatcher.getConfigurationMode().equals("COPMode")){
			
			COPModeDispatcher modedispatcher = new COPModeDispatcher();
			modedispatcher.sendRequest(infoDispatcher);
			
		}else if (infoDispatcher.getConfigurationMode().equals("TransportApiMode")){
			
			TransportApiDispatcher modedispatcher = new TransportApiDispatcher();
			modedispatcher.sendRequest(infoDispatcher);
			
		} else if (infoDispatcher.getConfigurationMode().equals("GMPLS")){
			
			log.info("Launching GMPLS dispathcer...");
			//Sending Initiate to PCE
			log.info("GMPLS dispathcer not implemented");
			
		}else log.info("Dispatcher not implemented");
		
	}



}
