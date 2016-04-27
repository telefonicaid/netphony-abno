package tid.provisioningManager.channels;

import java.util.LinkedList;

public class DispatcherChannel {

	
	private String dispatcherName;
	private LinkedList<Object> dispatcherSend;
	private LinkedList<Object> dispactcherReceive;
	
	public DispatcherChannel(String dispatcherName){
		
		this.dispatcherSend = new LinkedList<Object>();
		this.dispactcherReceive = new LinkedList<Object>();
		this.dispatcherName = dispatcherName;
	}
	
	public void dispatcherSend(Object o){
		
		dispatcherSend.add(o);
		
	}
	
	public void coreSend(Object o){
		
		dispactcherReceive.add(o);
		
	}
	
	public Object dispactcherReceive(){
		
		if(!dispactcherReceive.isEmpty()){
			return dispactcherReceive.remove();
		}else{
			return null;
		}
	
	}
	
	public Object coreReceive(){
		
		if(!dispatcherSend.isEmpty()){
			return dispatcherSend.remove();
		}else{
			return null;
		}
	
	}

	public String getDispatcherName() {
		return dispatcherName;
	}

	public void setDispatcherName(String dispatcherName) {
		this.dispatcherName = dispatcherName;
	}
	
	public void showStatus(){
		
		System.out.println("Estado colas dispatcher "+this.getDispatcherName());
		System.out.println("Envios del core: "+dispactcherReceive.size());
		System.out.println("Envios del dispatcher: "+dispatcherSend.size());
		
	}
	
	
}
