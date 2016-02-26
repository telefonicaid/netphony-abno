package es.tid.abno.modules.policy;


/**
 *  
 * @author b.jmgj
 * use in tid.abno.modules.ABNOParameters.java
 * 
**/


public class Policy {

	private String wfName;
	private MediaChannel mediaChannel;
	private L0PCECapabilities l0pceCapabilities;
	
	
	
	
	public MediaChannel getMediaChannel() {
		return mediaChannel;
	}
	public void setMediaChannel(MediaChannel mediaChannel) {
		this.mediaChannel = mediaChannel;
	}
	public L0PCECapabilities getL0pceCapabilities() {
		return l0pceCapabilities;
	}
	public void setL0pceCapabilities(L0PCECapabilities l0pceCapabilities) {
		this.l0pceCapabilities = l0pceCapabilities;
	}
	public String getWfName() {
		return wfName;
	}
	public void setWfName(String wfName) {
		this.wfName = wfName;
	}
}



