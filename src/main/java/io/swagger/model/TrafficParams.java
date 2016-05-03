package io.swagger.model;


import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.JsonProperty;


@ApiModel(description = "")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JaxRSServerCodegen", date = "2015-10-29T10:48:30.233+01:00")
public class TrafficParams  {
  
  private Integer latency = null;
  private Double OSNR = null;
  private Double estimatedPLR = null;
  public enum QosClassEnum {
     gold,  silver, 
  };
  private QosClassEnum qosClass = null;
  private Integer reservedBandwidth = null;

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("latency")
  public Integer getLatency() {
    return latency;
  }
  public void setLatency(Integer latency) {
    this.latency = latency;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("OSNR")
  public Double getOSNR() {
    return OSNR;
  }
  public void setOSNR(Double OSNR) {
    this.OSNR = OSNR;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("estimatedPLR")
  public Double getEstimatedPLR() {
    return estimatedPLR;
  }
  public void setEstimatedPLR(Double estimatedPLR) {
    this.estimatedPLR = estimatedPLR;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("qosClass")
  public QosClassEnum getQosClass() {
    return qosClass;
  }
  public void setQosClass(QosClassEnum qosClass) {
    this.qosClass = qosClass;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("reservedBandwidth")
  public Integer getReservedBandwidth() {
    return reservedBandwidth;
  }
  public void setReservedBandwidth(Integer reservedBandwidth) {
    this.reservedBandwidth = reservedBandwidth;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class TrafficParams {\n");
    
    sb.append("  latency: ").append(latency).append("\n");
    sb.append("  OSNR: ").append(OSNR).append("\n");
    sb.append("  estimatedPLR: ").append(estimatedPLR).append("\n");
    sb.append("  qosClass: ").append(qosClass).append("\n");
    sb.append("  reservedBandwidth: ").append(reservedBandwidth).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
