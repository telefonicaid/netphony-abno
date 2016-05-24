package es.tid.COPServiceTopology.swagger.model;

import es.tid.COPServiceTopology.swagger.model.Node;
import es.tid.COPServiceTopology.swagger.model.Edge;
import java.util.*;

import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.JsonProperty;


@ApiModel(description = "")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JaxRSServerCodegen", date = "2016-05-23T12:45:37.903+02:00")
public class Topology  {
  
  private String topologyId = null;
  private List<String> underlayTopology = new ArrayList<String>();
  private List<Node> nodes = new ArrayList<Node>();
  private List<Edge> edges = new ArrayList<Edge>();

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("topologyId")
  public String getTopologyId() {
    return topologyId;
  }
  public void setTopologyId(String topologyId) {
    this.topologyId = topologyId;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("underlayTopology")
  public List<String> getUnderlayTopology() {
    return underlayTopology;
  }
  public void setUnderlayTopology(List<String> underlayTopology) {
    this.underlayTopology = underlayTopology;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("nodes")
  public List<Node> getNodes() {
    return nodes;
  }
  public void setNodes(List<Node> nodes) {
    this.nodes = nodes;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("edges")
  public List<Edge> getEdges() {
    return edges;
  }
  public void setEdges(List<Edge> edges) {
    this.edges = edges;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Topology {\n");
    
    sb.append("  topologyId: ").append(topologyId).append("\n");
    sb.append("  underlayTopology: ").append(underlayTopology).append("\n");
    sb.append("  nodes: ").append(nodes).append("\n");
    sb.append("  edges: ").append(edges).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
