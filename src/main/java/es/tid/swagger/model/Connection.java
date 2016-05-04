package es.tid.swagger.model;

import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.JsonProperty;

import es.tid.swagger.model.Endpoint;
import es.tid.swagger.model.MatchRules;
import es.tid.swagger.model.PathType;
import es.tid.swagger.model.TrafficParams;
import es.tid.swagger.model.TransportLayerType;


@ApiModel(description = "")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JaxRSServerCodegen", date = "2015-10-29T10:48:30.233+01:00")
public class Connection  {
  
  private String controllerDomainId = null;
  private TrafficParams trafficParams = null;
  private String connectionId = null;
  private Endpoint zEnd = null;
  public enum OperStatusEnum {
     down,  up, 
  };
  private OperStatusEnum operStatus = null;
  private Endpoint aEnd = null;
  private PathType path = null;
  private TransportLayerType transportLayer = null;
  private MatchRules match = null;

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("controllerDomainId")
  public String getControllerDomainId() {
    return controllerDomainId;
  }
  public void setControllerDomainId(String controllerDomainId) {
    this.controllerDomainId = controllerDomainId;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("trafficParams")
  public TrafficParams getTrafficParams() {
    return trafficParams;
  }
  public void setTrafficParams(TrafficParams trafficParams) {
    this.trafficParams = trafficParams;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("connectionId")
  public String getConnectionId() {
    return connectionId;
  }
  public void setConnectionId(String connectionId) {
    this.connectionId = connectionId;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("zEnd")
  public Endpoint getZEnd() {
    return zEnd;
  }
  public void setZEnd(Endpoint zEnd) {
    this.zEnd = zEnd;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("operStatus")
  public OperStatusEnum getOperStatus() {
    return operStatus;
  }
  public void setOperStatus(OperStatusEnum operStatus) {
    this.operStatus = operStatus;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("aEnd")
  public Endpoint getAEnd() {
    return aEnd;
  }
  public void setAEnd(Endpoint aEnd) {
    this.aEnd = aEnd;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("path")
  public PathType getPath() {
    return path;
  }
  public void setPath(PathType path) {
    this.path = path;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("transportLayer")
  public TransportLayerType getTransportLayer() {
    return transportLayer;
  }
  public void setTransportLayer(TransportLayerType transportLayer) {
    this.transportLayer = transportLayer;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("match")
  public MatchRules getMatch() {
    return match;
  }
  public void setMatch(MatchRules match) {
    this.match = match;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Connection {\n");
    
    sb.append("  controllerDomainId: ").append(controllerDomainId).append("\n");
    sb.append("  trafficParams: ").append(trafficParams).append("\n");
    sb.append("  connectionId: ").append(connectionId).append("\n");
    sb.append("  zEnd: ").append(zEnd).append("\n");
    sb.append("  operStatus: ").append(operStatus).append("\n");
    sb.append("  aEnd: ").append(aEnd).append("\n");
    sb.append("  path: ").append(path).append("\n");
    sb.append("  transportLayer: ").append(transportLayer).append("\n");
    sb.append("  match: ").append(match).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
