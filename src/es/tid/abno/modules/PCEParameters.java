package es.tid.abno.modules;

public class PCEParameters {
	
	public static final int PCEIPlayer = 1;
	public static final int PCEOpticalLayer = 2;
	public static final int PCEmultilayer = 3;
	
	/**
	 * Address of the PCE Server
	 */
	private String pCEAddress;
	/**
	 * Port of the PCE Server
	 */
	private int pCEPort;
	/**
	 * OF CODE of the PCE
	 */
	private int ofCode;
	
	/**
	 * Public constructor
	 */
	public PCEParameters(String pCEAddress, int pCEPort, int ofCode){
		this.pCEAddress = pCEAddress;
		this.pCEPort = pCEPort;
		this.ofCode = ofCode;		
	}
	
	public PCEParameters(String pCEAddress, int pCEPort){
		this.pCEAddress = pCEAddress;
		this.pCEPort = pCEPort;
		this.ofCode = -1;		
	}
	
	public String getpCEAddress() {
		return pCEAddress;
	}
	public void setpCEAddress(String pCEAddress) {
		this.pCEAddress = pCEAddress;
	}
	public int getpCEPort() {
		return pCEPort;
	}
	public void setpCEPort(int pCEPort) {
		this.pCEPort = pCEPort;
	}
	public int getOfCode() {
		return ofCode;
	}
	public void setOfCode(int ofCode) {
		this.ofCode = ofCode;
	}
}
