package es.tid.COPServiceTopology.swagger.api.impl;

import es.tid.COPServiceTopology.swagger.api.*;
import es.tid.COPServiceTopology.swagger.model.*;
import es.tid.COPServiceTopology.swagger.model.Edge.EdgeTypeEnum;
import es.tid.COPServiceTopology.swagger.model.EdgeEnd.SwitchingCapEnum;
import es.tid.tedb.DomainTEDB;
import es.tid.tedb.InterDomainEdge;
import es.tid.tedb.IntraDomainEdge;
import es.tid.tedb.Node_Info;
import es.tid.tedb.TEDB;
import es.tid.topologyModuleBase.writer.TopologyServerCOP;

import com.sun.jersey.multipart.FormDataParam;

import es.tid.topologyModuleBase.database.SimpleTopology;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import es.tid.COPServiceTopology.swagger.api.NotFoundException;

import java.io.InputStream;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import javax.ws.rs.core.Response;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JaxRSServerCodegen", date = "2016-05-23T12:45:37.903+02:00")
public class ConfigApiServiceImpl extends ConfigApiService {
  
      @Override
      public Response retrieveTopologies()
      throws NotFoundException {
    	  SimpleTopology ted = TopologyServerCOP.getActualTed();
    	  TopologiesSchema tSchema = new TopologiesSchema();
    	  List<Topology> tops = new ArrayList<Topology>();
    	  for(TEDB t : ted.getAllDB()){
    		 if(t != null)tops.add( translateTopology((DomainTEDB) t));
    	  }
    	  tSchema.setTopology(tops);
	      return Response.ok().entity(tSchema).build();
	  }
  
      @Override
      public Response retrieveTopologiesTopologyTopologyById(String topologyId)
      throws NotFoundException {
      
    	  TEDB db = TopologyServerCOP.getActualTed().getDB(topologyId);
    	  if(db == null)
    		  return Response.serverError().build();
    	  else
    		  return Response.ok().entity(translateTopology((DomainTEDB) db)).build();
      }
  
      @Override
      public Response retrieveTopologiesTopologyEdgesEdgesById(String topologyId,String edgeId)
      throws NotFoundException {
    	  SimpleTopology ted = TopologyServerCOP.getActualTed();
    	  TEDB db = ted.getDB(topologyId);
    	  if(db==null){
    		  return Response.noContent().build();
    	  }
    	  Iterator<IntraDomainEdge> it = ((DomainTEDB)db).getIntraDomainLinks().iterator();
    	  for( ; it.hasNext() ; ){
    		   Edge edge = translateEdge((DomainTEDB)db, it.next());
    		   if( edge.getEdgeId().equals(edgeId)){
    			   return Response.ok().entity(edge).build();
    		   }
    		  
    	  }
    	  return Response.noContent().build();
      }
  
      @Override
      public Response retrieveTopologiesTopologyEdgesLocalIfidLocalIfidById(String topologyId,String edgeId)
      throws NotFoundException {
      // do some magic!
      return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
  }
  
      @Override
      public Response retrieveTopologiesTopologyEdgesRemoteIfidRemoteIfidById(String topologyId,String edgeId)
      throws NotFoundException {
      // do some magic!
      return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
  }
  
      @Override
      public Response retrieveTopologiesTopologyEdgesSourceSourceById(String topologyId,String edgeId)
      throws NotFoundException {
    	  SimpleTopology ted = TopologyServerCOP.getActualTed();
    	  TEDB db = ted.getDB(topologyId);
    	  if(db==null){
    		  return Response.noContent().build();
    	  }
    	  Iterator<IntraDomainEdge> it = ((DomainTEDB)db).getIntraDomainLinks().iterator();
    	  for( ; it.hasNext() ; ){
    		   Edge edge = translateEdge((DomainTEDB)db, it.next());
    		   if( edge.getEdgeId().equals(edgeId)){
    			   //TODO donde sacar el node Source de un edge?
  //  			   return Response.ok().entity(translateNode( it.next().getSource() )).build();
    		   }
    		  
    	  }
	      return Response.noContent().build();
	  }
  
      @Override
      public Response retrieveTopologiesTopologyEdgesSourceEdgeEndEdgeEndById(String topologyId,String edgeId,String edgeEndId)
      throws NotFoundException {
      // do some magic!
      return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
  }
  
      @Override
      public Response retrieveTopologiesTopologyEdgesTargetTargetById(String topologyId,String edgeId)
      throws NotFoundException {
    	  SimpleTopology ted = TopologyServerCOP.getActualTed();
    	  TEDB db = ted.getDB(topologyId);
    	  if(db==null){
    		  return Response.noContent().build();
    	  }
    	  Iterator<IntraDomainEdge> it = ((DomainTEDB)db).getIntraDomainLinks().iterator();
    	  for( ; it.hasNext() ; ){
    		   Edge edge = translateEdge((DomainTEDB)db, it.next());
    		   if( edge.getEdgeId().equals(edgeId)){
    			   //TODO donde sacar el node Target de un edge?
 //   			   return Response.ok().entity(translateNode( it.next().getTarget() )).build();
    		   }
    		  
    	  }
	      return Response.noContent().build();
  }
  
      @Override
      public Response retrieveTopologiesTopologyEdgesTargetEdgeEndEdgeEndById(String topologyId,String edgeId,String edgeEndId)
      throws NotFoundException {
      // do some magic!
	      return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	  }
      
      private Node getNodeById(DomainTEDB db, String nodeId){
    	  for (Object n : db.getIntraDomainLinksvertexSet()){
    		  if(n instanceof es.tid.tedb.elements.Node){
    			  Node node= translateNode((es.tid.tedb.elements.Node)n);
    			  if(node.getNodeId().equals(nodeId)){
    				  return node;
    			  }
    		  }
    	  }
    	  return null;
      }
  
      @Override
      public Response retrieveTopologiesTopologyNodesNodesById(String topologyId,String nodeId)
      throws NotFoundException {
    	  SimpleTopology ted = TopologyServerCOP.getActualTed();
    	  TEDB db = ted.getDB(topologyId);
    	  if(db==null){
    		  return Response.noContent().build();
    	  }
    	  Node node = getNodeById((DomainTEDB) db, nodeId);
    	  if(node == null){
    		  return Response.noContent().build();
    	  }else{
    		  return Response.ok().entity(node).build();
    	  }
	  }
  
      @Override
      public Response retrieveTopologiesTopologyNodesEdgeEndEdgeEndById(String topologyId,String nodeId,String edgeEndId)
      throws NotFoundException {
    	  SimpleTopology ted = TopologyServerCOP.getActualTed();
    	  TEDB db = ted.getDB(topologyId);
    	  if(db==null){
    		  return Response.noContent().build();
    	  }
    	  Node node = getNodeById((DomainTEDB) db, nodeId);
    	  if(node == null){
    		  return Response.noContent().build();
    	  }else{
    		  for(EdgeEnd edgeEnd : node.getEdgeEnd()){
    			  if(edgeEnd.getEdgeEndId().equals(edgeEndId)){
    				  return Response.ok().entity(edgeEnd).build();
    			  }
    		  }
    	  }
    	  return Response.noContent().build();
	  }
      
  private Node translateNode(es.tid.tedb.elements.Node n){
	  Node node = new Node();
	  node.setName(n.getNodeID());
	  node.setDomain(n.getDomain()+"");
	  node.setNodetype(n.getLayer());
	  node.setNodeId(n.getNodeID());
	  List<EdgeEnd>intList = new ArrayList<EdgeEnd>();
	  for (es.tid.tedb.elements.Intf i : n.getIntfList()){
		  intList.add(translateEdgeEnd(n, i));
	  }
	  node.setEdgeEnd(intList);
	  
	  
	  
	  //node.setUnderlayAbstractTopology(underlayAbstractTopology);
	  
	  return node;
  }
  
  private EdgeEnd translateEdgeEnd(es.tid.tedb.elements.Node n, es.tid.tedb.elements.Intf i){
	  EdgeEnd edgeEnd = new EdgeEnd();
	  edgeEnd.setEdgeEndId(n.getNodeID()+"-"+i.getName());
	  edgeEnd.setName(i.getName());
	  edgeEnd.setPeerNodeId(i.getAddress().get(0)); //correct map?
	  //edgeEnd.setSwitchingCap(SwitchingCapEnum.lsc);
	  return edgeEnd;
  }
  
  private EdgeEnd translateEdgeEnd(es.tid.tedb.elements.EndPoint e){
	  EdgeEnd edgeEnd = new EdgeEnd();
	  edgeEnd.setEdgeEndId(e.getNode()+"-"+e.getIntf());
	  edgeEnd.setName(e.getNode()+"-"+e.getIntf());
	  edgeEnd.setPeerNodeId(e.getNode()); //correct map?
	  //edgeEnd.setSwitchingCap(SwitchingCapEnum.lsc);
	  return edgeEnd;
  }
  
  
  private EthEdge translateEthEdge(){
	  EthEdge ethEdge = new EthEdge();
	  
	  return ethEdge;
  }
  
  private Bitmap translateBitmap(){
	  Bitmap bitmap = new Bitmap();
	  
	  return bitmap;
  }
  
  private Edge translateEdge(DomainTEDB db,IntraDomainEdge e){
	  
	  Edge edge = new Edge();
	  edge.setName(e.getLinkID());
	  edge.setEdgeId(e.getLinkID());
	  if(e.getType().equals("")){
		  edge.setEdgeType(EdgeTypeEnum.dwdm_edge);
	  }else if(e.getType().equals("")){
		  edge.setEdgeType(EdgeTypeEnum.eth_edge);
	  }
	  edge.setMetric(e.getTemetric()+"");
	  if(e.getBw()!=null){
		  edge.setMaxResvBw(e.getBw().getMaxBandwidth()+"");
		  edge.setUnreservBw(e.getBw().getUnreservedBw()+"");
	  }
	  Object src = e.getSrc_Numif_id();
	  if( src instanceof es.tid.tedb.elements.EndPoint ){
		  Node node = getNodeById( db, ((es.tid.tedb.elements.EndPoint) src).getNode());
		  edge.setSource(node);
		  for(EdgeEnd end : node.getEdgeEnd()){
			  if(end.getName().equals(((es.tid.tedb.elements.EndPoint) src).getIntf()) ){
				  edge.setLocalIfid(end);
			  }
		  }
		  
	  }
	  
	  Object dst = e.getDst_Numif_id();
	  if( dst instanceof es.tid.tedb.elements.EndPoint ){
		  Node node = getNodeById( db, ((es.tid.tedb.elements.EndPoint) dst).getNode());
		  edge.setTarget(node);
		  for(EdgeEnd end : node.getEdgeEnd()){
			  if(end.getName().equals(((es.tid.tedb.elements.EndPoint) dst).getIntf()) ){
				  edge.setRemoteIfid(end);
			  }
		  }
	  }
	 
	  //edge.setLocalIfid( translateEdgeEnd(e.getSrc_if_id())) ); 
	  //edge.setRemoteIfid( translateEdgeEnd(e.getDst_if_id()) );
	  
	  return edge;
  }
  /*private Edge translateEdge(InterDomainEdge e){
	  Edge edge = new Edge();
	  edge.setName(e.getDomain_src_router()+"-"+e.getDomain_dst_router());
	  edge.setEdgeId(e.hashCode()+"");
	  if(e.getType().equals("")){
		  edge.setEdgeType(EdgeTypeEnum.dwdm_edge);
	  }else if(e.getType().equals("")){
		  edge.setEdgeType(EdgeTypeEnum.eth_edge);
	  }
	  edge.setMetric(e.get+"");
	  edge.setMaxResvBw(e.getBw().getMaxBandwidth()+"");
	  edge.setUnreservBw(e.getBw().getUnreservedBw()+"");
	  edge.setSource( translateNode(e.getLocal_Node_Info()) );
	  edge.setTarget( translateNode(e.getRemote_Node_Info()) );
	  //edge.setLocalIfid( translateEdgeEnd(e.getSrc_if_id())) ); 
	  //edge.setRemoteIfid( translateEdgeEnd(e.getDst_if_id()) );
	  
	  return edge;
  }*/
  
  
 

private DwdmChannel translateDwdmChannel(){
	  DwdmChannel dwdmChannel = new DwdmChannel();
	  
	  return dwdmChannel;
  }
  
  private DwdmEdge translateDwdmEdge(){
	  DwdmEdge dwdmEdge = new DwdmEdge();
	  
	  return dwdmEdge;
  }
  
  private Topology translateTopology(DomainTEDB ted){
	  Topology topology = new Topology();
	  /*if(ted==null){
		  topology.setTopologyId("topology null Exception");
		  return topology;
	  }
	  if(ted.getDomainID()==null){
		  topology.setTopologyId("getDomainID null Exception");
		  return topology;
	  }*/
	  //topology.setTopologyId(ted.getDomainID().toString());
	  
	  List<Edge> edges = new ArrayList<Edge>();
	  for(IntraDomainEdge link : ted.getIntraDomainLinks()){
		  edges.add(translateEdge(ted, link));
	  }
	  topology.setEdges(edges);
	  
	  List<Node> nodes = new ArrayList<Node>();
	  for(Object node : ted.getIntraDomainLinksvertexSet()){
		  if(node instanceof es.tid.tedb.elements.Node){
			  nodes.add(translateNode((es.tid.tedb.elements.Node)node));
		  }
	  }
	  topology.setNodes(nodes);
	  
	  //topology.setUnderlayTopology(); //TODO
	  
	  return topology;
  }
  
 
  
  
  
  
  
  
}
