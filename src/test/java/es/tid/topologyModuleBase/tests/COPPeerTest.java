package es.tid.topologyModuleBase.tests;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import es.tid.tedb.SimpleTEDB;
import es.tid.topologyModuleBase.TMModuleInitiater;
import es.tid.topologyModuleBase.TopologyModuleParamsArray;
import es.tid.topologyModuleBase.database.SimpleTopology;
import es.tid.topologyModuleBase.management.TMManagementServer;

public class COPPeerTest {
    public COPPeerTest(){
		
	}
	
	/**
	 * This tests starts a BGL-LS Speaker, reads the topology from a File and Sends it.
	 * A second speaker is started and reads the topology.
	 * The first speaker is configured to read only the multidomain topology and send the 
	 * multidomain topology
	 * The second speaker is configured as Consumer. 
	 * The speakers are launched in separated non-standard ports for testing purposes.
	 * Both speakers talk and the topology is sent from BGP-Speaker 1 to BGP-Speaker 2
	 * It checks after 10 seconds if the topology of BGP-Speaker 2 is the same as BGP
	 * Speaker 1. 
	 */
	@org.junit.Test
	public void testPeer(){
		//run COP topology-server, topology readed from file
		/*TopologyModuleParamsArray params;
		params=new TopologyModuleParamsArray("src/test/resources/TMConfiguration.xml");
		params.initialize();	
		
		
		SimpleTopology sTop = new SimpleTopology(new SimpleTEDB());
		((SimpleTEDB)sTop.getDB()).createGraph();
		Lock lock = new ReentrantLock();
		
		TMManagementServer TMms=new TMManagementServer(sTop,params);
		TMms.start();
		
		(new TMModuleInitiater(sTop, params, lock)).intiate();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		*/
		//run COP topology-client, topology readed from COP-API
		/*		TopologyModuleParamsArray params2;
				params2=new TopologyModuleParamsArray("src/test/resources/TMConfigurationCOP.xml");
				params2.initialize();	
				
				
				SimpleTopology sTop2 = new SimpleTopology(new SimpleTEDB());
				((SimpleTEDB)sTop2.getDB()).createGraph();
				Lock lock2 = new ReentrantLock();
				
				TMManagementServer TMms2=new TMManagementServer(sTop2,params2);
				TMms2.start();
				
				(new TMModuleInitiater(sTop2, params2, lock2)).intiate();
				
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		*/
		
		//TODO: Check topologies if equals
		
	}
}
