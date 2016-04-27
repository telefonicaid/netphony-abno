package io.swagger.model;

import io.swagger.model.Endpoint;
import io.swagger.model.Label;
import java.util.*;

import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.JsonProperty;


@ApiModel(description = "")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JaxRSServerCodegen", date = "2015-10-29T10:48:30.233+01:00")
public class PathType  {
  
  private Boolean multiLayer = null;
  private Boolean noPath = null;
  private String id = null;
  private List<Endpoint> topoComponents = new ArrayList<Endpoint>();
  private Label label = null;

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("multiLayer")
  public Boolean getMultiLayer() {
    return multiLayer;
  }
  public void setMultiLayer(Boolean multiLayer) {
    this.multiLayer = multiLayer;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("noPath")
  public Boolean getNoPath() {
    return noPath;
  }
  public void setNoPath(Boolean noPath) {
    this.noPath = noPath;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("id")
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("topoComponents")
  public List<Endpoint> getTopoComponents() {
    return topoComponents;
  }
  public void setTopoComponents(List<Endpoint> topoComponents) {
    this.topoComponents = topoComponents;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("label")
  public Label getLabel() {
    return label;
  }
  public void setLabel(Label label) {
    this.label = label;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class PathType {\n");
    
    sb.append("  multiLayer: ").append(multiLayer).append("\n");
    sb.append("  noPath: ").append(noPath).append("\n");
    sb.append("  id: ").append(id).append("\n");
    sb.append("  topoComponents: ").append(topoComponents).append("\n");
    sb.append("  label: ").append(label).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
