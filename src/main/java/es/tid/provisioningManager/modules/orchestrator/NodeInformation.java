package es.tid.provisioningManager.modules.orchestrator;

public class NodeInformation
{
	private String id;
	private String dest_id;
	private String source_port; 
	private String dest_port;
	private String associatedMac;
	private String secondAssociatedMac;
	private Integer vlan;
	private String controller;

	NodeInformation(String id, int source_port, int dest_port, String associatedMac, String secondAssociatedMac,Integer vlan)
	{
		this.id = id;
		this.source_port = ((Integer)source_port).toString();
		this.dest_port = ((Integer)dest_port).toString();
		this.associatedMac = associatedMac;
		this.secondAssociatedMac = secondAssociatedMac;
		this.vlan = vlan;
		this.dest_id = null;
	}
	NodeInformation(String id, String dest_id, int source_port, int dest_port, String associatedMac, String secondAssociatedMac,Integer vlan)
	{
		this.id = id;
		this.source_port = ((Integer)source_port).toString();
		this.dest_port = ((Integer)dest_port).toString();
		this.associatedMac = associatedMac;
		this.secondAssociatedMac = secondAssociatedMac;
		this.vlan = vlan;
		this.dest_id = dest_id;
	}
	public String getId() 
	{
		return id;
	}
	public String getSource_port() 
	{
		return source_port;
	}
	public String getDest_port() 
	{
		return dest_port;
	}
	public String getAssociatedMac() 
	{
		return associatedMac;
	}
	public void setAssociatedMac(String associatedMac) 
	{
		this.associatedMac = associatedMac;
	}
	public Integer getVlan() 
	{
		return vlan;
	}
	public String getSecondAssociatedMac() 
	{
		return secondAssociatedMac;
	}
	public void setSecondAssociatedMac(String secondAssociatedMac)
	{
		this.secondAssociatedMac = secondAssociatedMac;
	}
	public String getDest_id()
	{
		return dest_id;
	}
	public String getController() 
	{
		return controller;
	}
	public void setController(String controller)
	{
		this.controller = controller;
	}
	@Override
	public String toString() 
	{
		return "NodeInformation [id=" + id + ", dest_id=" + dest_id
				+ ", source_port=" + source_port + ", dest_port="
				+ dest_port + ", associatedMac=" + associatedMac
				+ ", secondAssociatedMac=" + secondAssociatedMac
				+ ", vlan=" + vlan + "]";
	}

}
