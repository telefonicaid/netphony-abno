package io.swagger.model;


import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.JsonProperty;


@ApiModel(description = "")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JaxRSServerCodegen", date = "2015-10-29T10:48:30.233+01:00")
public class Label  {
  
  public enum LabelTypeEnum {
     GMPLS_FIXED,  GMPLS_FLEXI, 
  };
  private LabelTypeEnum labelType = null;
  private Integer labelValue = null;

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("labelType")
  public LabelTypeEnum getLabelType() {
    return labelType;
  }
  public void setLabelType(LabelTypeEnum labelType) {
    this.labelType = labelType;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("labelValue")
  public Integer getLabelValue() {
    return labelValue;
  }
  public void setLabelValue(Integer labelValue) {
    this.labelValue = labelValue;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Label {\n");
    
    sb.append("  labelType: ").append(labelType).append("\n");
    sb.append("  labelValue: ").append(labelValue).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
