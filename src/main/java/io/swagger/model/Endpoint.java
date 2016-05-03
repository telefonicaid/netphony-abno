package io.swagger.model;


import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.JsonProperty;


@ApiModel(description = "")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JaxRSServerCodegen", date = "2015-10-29T10:48:30.233+01:00")
public class Endpoint  {
  
  private String routerId = null;
  private String interfaceId = null;
  private String endpointId = null;

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("routerId")
  public String getRouterId() {
    return routerId;
  }
  public void setRouterId(String routerId) {
    this.routerId = routerId;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("interfaceId")
  public String getInterfaceId() {
    return interfaceId;
  }
  public void setInterfaceId(String interfaceId) {
    this.interfaceId = interfaceId;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("endpointId")
  public String getEndpointId() {
    return endpointId;
  }
  public void setEndpointId(String endpointId) {
    this.endpointId = endpointId;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Endpoint {\n");
    
    sb.append("  routerId: ").append(routerId).append("\n");
    sb.append("  interfaceId: ").append(interfaceId).append("\n");
    sb.append("  endpointId: ").append(endpointId).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
