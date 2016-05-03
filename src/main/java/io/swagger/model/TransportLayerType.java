package io.swagger.model;


import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.JsonProperty;


@ApiModel(description = "")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JaxRSServerCodegen", date = "2015-10-29T10:48:30.233+01:00")
public class TransportLayerType  {
  
  public enum LayerEnum {
     dwdm_link,  ethernet,  ethernet_broadcast,  mpls, 
  };
  private LayerEnum layer = null;
  private String layerId = null;

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("layer")
  public LayerEnum getLayer() {
    return layer;
  }
  public void setLayer(LayerEnum layer) {
    this.layer = layer;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("layerId")
  public String getLayerId() {
    return layerId;
  }
  public void setLayerId(String layerId) {
    this.layerId = layerId;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class TransportLayerType {\n");
    
    sb.append("  layer: ").append(layer).append("\n");
    sb.append("  layerId: ").append(layerId).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
