package es.tid.topologyModule;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.logging.Logger;

import es.tid.topologyModule.database.SimpleTopology;
import es.tid.topologyModule.reader.TopologyReaderBGPLS;
import es.tid.topologyModule.reader.TopologyReaderController;
import es.tid.topologyModule.reader.TopologyReaderInfinera;
import es.tid.topologyModule.reader.TopologyReaderOSPF;
import es.tid.topologyModule.reader.TopologyReaderXML;
import es.tid.topologyModule.session.ws.WSOldSession;
import es.tid.topologyModule.writer.TopologyServerGson;
import es.tid.topologyModule.writer.TopologyWriterBGPLS;

public class TMModuleInitiater
{
	Logger log=Logger.getLogger("TMController");
			
	SimpleTopology ted;
	TopologyModuleParamsArray params;
	Lock lock;
	
	public TMModuleInitiater(SimpleTopology ted, TopologyModuleParamsArray params, Lock lock)
	{
		this.ted = ted;
		this.params = params;
		this.lock = lock;
	}
	public void intiate()
	{
		ArrayList<TopologyModuleParams> paramList = params.getParamList();
		for (int i = 0; i < paramList.size(); i++)
		{
			TopologyModuleParams actualLittleParams = paramList.get(i);
			//IMPORTERS
			if (actualLittleParams.isXML())
			{
				(new TopologyReaderXML(ted, actualLittleParams,lock)).readTopology();
			}
			
			if (actualLittleParams.isOSPF())
			{
				(new TopologyReaderOSPF(ted, actualLittleParams, lock)).readTopology();
			}
			
			if (actualLittleParams.isFloodLight())
			{
				(new TopologyReaderController(ted, actualLittleParams, lock)).readTopology();
			}
			
			if (actualLittleParams.isRestInfinera())
			{
				(new TopologyReaderInfinera(ted, actualLittleParams,lock)).readTopology();
			}
			
			if (actualLittleParams.isBGPLSReading())
			{
				(new TopologyReaderBGPLS(ted, actualLittleParams,lock)).readTopology();
			}
			
			
			//EXPORTERS
			if (actualLittleParams.isBGPLSWriting())
			{
				(new TopologyWriterBGPLS(ted, actualLittleParams,lock)).serveTopology();
			}
			
			if (actualLittleParams.isGSON())
			{
				(new TopologyServerGson(ted, actualLittleParams,lock)).serveTopology();
			}
			
			
			//BOTH
			if (actualLittleParams.isWSOld()){
				try {
					log.info("WSOld.  ParamsSize: "+paramList.size()+" Time: "+i+" Info: "+actualLittleParams.getIpWSOld()+":"+actualLittleParams.getPortWSOld());
					ServerSocket s= new ServerSocket(actualLittleParams.getPortWSOld());
					while (true){
						WSOldSession wssession= new WSOldSession(s.accept()	,ted);
						wssession.start();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}

}
