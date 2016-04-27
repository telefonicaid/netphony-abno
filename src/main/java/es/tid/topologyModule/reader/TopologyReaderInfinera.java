package es.tid.topologyModule.reader;

import java.util.concurrent.locks.Lock;

import es.tid.topologyModule.TopologyModuleParams;
import es.tid.topologyModule.database.SimpleTopology;
/**
 * 
 * @author ackes
 *
 */

public class TopologyReaderInfinera extends TopologyReader
{	
	public TopologyReaderInfinera(SimpleTopology ted, TopologyModuleParams params, Lock lock)
	{
		super(ted,params,lock);
	}
	
	@Override
	public void readTopology() 
	{
		System.out.println("Entrando a leer la Topologia");
	}
	
}
