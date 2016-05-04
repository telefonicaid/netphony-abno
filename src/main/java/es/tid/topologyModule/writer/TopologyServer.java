package es.tid.topologyModule.writer;

import java.util.concurrent.locks.Lock;
import java.util.logging.Logger;

import es.tid.topologyModule.TopologyModuleParams;
import es.tid.topologyModule.database.SimpleTopology;
/**
 * 
 * @author jaume
 *
 */
public abstract class TopologyServer extends Thread
{
	/**
	 * Logger
	 */
	protected static Logger log=Logger.getLogger("TMController");
	
	protected SimpleTopology ted;
	protected TopologyModuleParams params;
	protected Lock lock;
	protected InformationRetriever infRetriever;
	
	
	public TopologyServer(SimpleTopology ted, TopologyModuleParams params, Lock lock)
	{
		this.ted = ted;
		this.params = params;
		this.lock = lock;
		infRetriever = new InformationRetriever(ted, params, lock);
	}
	
	abstract void serveTopology();
}
