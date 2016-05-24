package es.tid.COPServiceTopology.swagger.api.impl;

import es.tid.COPServiceTopology.swagger.api.*;
import es.tid.COPServiceTopology.swagger.model.*;
import es.tid.COPServiceTopology.swagger.model.Edge.EdgeTypeEnum;
import es.tid.tedb.DomainTEDB;
import es.tid.tedb.IntraDomainEdge;
import es.tid.tedb.Node_Info;
import es.tid.tedb.TEDB;
import es.tid.topologyModuleBase.writer.TopologyServerCOP;

import com.sun.jersey.multipart.FormDataParam;

import es.tid.topologyModuleBase.database.SimpleTopology;

import java.util.Iterator;
import java.util.List;
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

	      return Response.ok().entity(translateTopologiesSchema(ted)).build();
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
    		   Edge edge = translateEdge(it.next());
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
    		   Edge edge = translateEdge(it.next());
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
    		   Edge edge = translateEdge(it.next());
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
  
      @Override
      public Response retrieveTopologiesTopologyNodesNodesById(String topologyId,String nodeId)
      throws NotFoundException {
    	  SimpleTopology ted = TopologyServerCOP.getActualTed();
    	  TEDB db = ted.getDB(topologyId);
    	  if(db==null){
    		  return Response.noContent().build();
    	  }
    	  Node_Info node = ((DomainTEDB)db).getNodeTable().get(nodeId);
    	  if(node==null){
    		  return Response.noContent().build();
    	  }
	      return Response.ok().entity(translateNode(node)).build();
	  }
  
      @Override
      public Response retrieveTopologiesTopologyNodesEdgeEndEdgeEndById(String topologyId,String nodeId,String edgeEndId)
      throws NotFoundException {
      // do some magic!
      return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
  }
      
  private Node translateNode(Node_Info n){
	  Node node = new Node();
	  node.setName(n.getName().toString());
	  node.setDomain(n.getArea_id().toString());
	  node.setNodetype();
	  return node;
  }
  
  private EdgeEnd translateEdgeEnd(){
	  EdgeEnd edgeEnd = new EdgeEnd();
	  
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
  
  private Edge translateEdge(IntraDomainEdge e){
	  Edge edge = new Edge();
	  edge.setName(e.getLinkID());
	  edge.setEdgeId(e.getLinkID());
	  if(e.getType().equals("")){
		  edge.setEdgeType(EdgeTypeEnum.dwdm_edge);
	  }else if(e.getType().equals("")){
		  edge.setEdgeType(EdgeTypeEnum.eth_edge);
	  }
	  edge.setMetric(e.getTemetric()+"");
	  edge.setMaxResvBw(e.getBw().getMaxBandwidth()+"");
	  edge.setUnreservBw(e.getBw().getUnreservedBw()+"");
	  edge.setSource( translateNode(e.getLocal_Node_Info()) );
	  edge.setTarget( translateNode(e.getRemote_Node_Info()) );
	  //edge.setLocalIfid( translateEdgeEnd(e.getSrc_if_id())) ); 
	  //edge.setRemoteIfid( translateEdgeEnd(e.getDst_if_id()) );
	  
	  return edge;
  }
  
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
	  
	  return topology;
  }
  
  private TopologiesSchema translateTopologiesSchema(SimpleTopology ted){
	  TopologiesSchema tSchema = new TopologiesSchema();
	  
	  return tSchema;
  }
  
  
  
  
  
  
}
