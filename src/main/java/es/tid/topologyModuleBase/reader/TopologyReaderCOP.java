package es.tid.topologyModuleBase.reader;


import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.locks.Lock;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import es.tid.tedb.IntraDomainEdge;
import es.tid.tedb.SimpleTEDB;

import es.tid.topologyModuleBase.TopologyModuleParams;
import es.tid.topologyModuleBase.COPServiceTopology.client.ApiClient;
import es.tid.topologyModuleBase.COPServiceTopology.client.ApiException;
import es.tid.topologyModuleBase.COPServiceTopology.client.api.DefaultApi;
import es.tid.topologyModuleBase.COPServiceTopology.model.EdgeEnd;
import es.tid.topologyModuleBase.COPServiceTopology.model.TopologiesSchema;
import es.tid.topologyModuleBase.COPServiceTopology.model.*;
import es.tid.topologyModuleBase.database.SimpleTopology;
import es.tid.topologyModuleBase.reader.TopologyReader;

public class TopologyReaderCOP extends TopologyReader
{

	public TopologyReaderCOP(SimpleTopology ted,TopologyModuleParams params, Lock lock)
	{
		super(ted,params,lock);
	}

	@Override
	public void readTopology() 
	{
		lock.lock();
		//Initialize Traffic Engineering Database
		ApiClient apiClient = new ApiClient();
		apiClient.setBasePath("http://"+params.getRemoteCOPhost()+":"+params.getRemoteCOPPort()+"/restconf");
		readNetwork(new DefaultApi(apiClient));
		lock.unlock();

	}

	public void readNetwork(DefaultApi api) {
		try {
			TopologiesSchema retrieveTopologies = api.retrieveTopologies();
			//EdgeEnd retrieveLocalIf = api.retrieveTopologiesTopologyEdgesLocalIfidLocalIfidById("1", "ADVA_2_CTTC_2");
			log.info(retrieveTopologies.toString());
			for(Topology top : retrieveTopologies.getTopology()){
				for(Node n : top.getNodes()){
					es.tid.tedb.elements.Node node = TranslateModel.translate2Node(n);
					((SimpleTEDB)ted.getDB()).getNetworkGraph().addVertex(node);
				}
				for(Edge e: top.getEdges()){
					es.tid.tedb.elements.Link link = TranslateModel.translate2Link(e);
					fromLinkToIntradomainlink(link);
				}
			}
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
		

	}

	private void fromLinkToIntradomainlink(es.tid.tedb.elements.Link link){
		boolean finished=false;
		//System.out.println(link.toString());
		Iterator<Object> vertices=((SimpleTEDB)this.ted.getDB()).getNetworkGraph().vertexSet().iterator();
		es.tid.tedb.elements.Node src=null; es.tid.tedb.elements.Node dst=null;
		while (vertices.hasNext() && !finished){
			es.tid.tedb.elements.Node node=(es.tid.tedb.elements.Node) vertices.next();
			if (link.getDest().getNode().equals(node.getNodeID()))
				dst=node;
			else if (link.getSource().getNode().equals(node.getNodeID()))
				src=node;

		}
		IntraDomainEdge edge= new IntraDomainEdge();
		edge.setBw(link.getBandwidth());
		edge.setDirectional(link.isDirectional());
		edge.setLinkID(link.getLinkID());
		edge.setType(link.getType());
		edge.setDst_Numif_id(link.getDest());
		edge.setSrc_Numif_id(link.getSource());
		if (src==null){
			log.info("SRC NULL");
		}
		if (dst==null){
			log.info("DST NULL");
		}

		((SimpleTEDB)this.ted.getDB()).getNetworkGraph().addEdge(src, dst, edge);
		((SimpleTEDB)this.ted.getDB()).getIntraDomainEdges().add(edge);

	}

	public static String getCharacterDataFromElement(Element e) {
		org.w3c.dom.Node child = e.getFirstChild();
		if (child instanceof CharacterData) {
			CharacterData cd = (CharacterData) child;
			return cd.getData();
		} else {
			return "?";
		}
	}
}
