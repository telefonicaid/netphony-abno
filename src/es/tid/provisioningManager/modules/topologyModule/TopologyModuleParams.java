package es.tid.provisioningManager.modules.topologyModule;

public class TopologyModuleParams {
	/**
	 * Route to call the topology Module server
	 */
	private String route;
	
		
	
	public TopologyModuleParams(String address, Integer port){
		this.setRoute("http://" + address + ":" + port+"/axis2/services/TopologyModule");		
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}
	
	
}
