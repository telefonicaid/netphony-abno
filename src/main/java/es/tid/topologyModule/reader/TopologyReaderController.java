package es.tid.topologyModule.reader;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;

import es.tid.pce.server.TopologyManager;
import es.tid.tedb.DomainTEDB;
import es.tid.tedb.controllers.TEDUpdaterFloodlight;
import es.tid.tedb.controllers.TEDUpdaterRYU;
import es.tid.util.UtilsFunctions;
import es.tid.topologyModule.TopologyModuleParams;
import es.tid.topologyModule.database.SimpleTopology;
/**
 * 
 * @author jaume
 *
 */

public class TopologyReaderController extends TopologyReader
{	
	public TopologyReaderController(SimpleTopology ted, TopologyModuleParams params, Lock lock)
	{
		super(ted,params,lock);
	}
	
	@Override
	public void readTopology() 
	{
		start();
	}
	
	@Override
	public void run()
	{
		log.info("Initializing TED from WAN.");
		
		ArrayList<String> ips = new ArrayList<String>();
		ArrayList<String> ports = new ArrayList<String>();
		ArrayList<String> types = new ArrayList<String>();
		
		TopologyManager.parseControllerFile(params.getControllerListFile(), ips, ports, types);
		
		while(true) 
		{
			
			TopologyManager.updateTopology(ips, ports, types,  (DomainTEDB)(ted.getDB()), params.getInterDomainFile(), log);
			
			//Floodlight
			//TEDUpdaterFloodlight thread = new TEDUpdaterFloodlight(ips, ports, params.getTopologyPath(),params.getTopologyNodesPath(), (DomainTEDB)(ted.getDB()), log);
			
			//Ryu
			TEDUpdaterRYU thread = new TEDUpdaterRYU(ips, ports, params.getTopologyPath(), params.getTopologyNodesPath(), (DomainTEDB)(ted.getDB()), log);

			thread.setInterDomainFile(params.getInterDomainFile());
			thread.start();
			
			try 
			{
				Thread.sleep(50000);
			} 
			catch (InterruptedException e) 
			{
				log.info(UtilsFunctions.exceptionToString(e));
			}
        }
	}
}
