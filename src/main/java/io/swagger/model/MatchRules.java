package io.swagger.model;


import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.JsonProperty;


@ApiModel(description = "")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JaxRSServerCodegen", date = "2015-10-29T10:48:30.233+01:00")
public class MatchRules  {
  
  private Integer mplsLabel = null;
  private Integer ethType = null;
  private Integer ipEcn = null;
  private Integer icmpv4Type = null;
  private String ethDst = null;
  private Integer vlanPcp = null;
  private String ipv4Dst = null;
  private Integer arpTpa = null;
  private Integer arpSha = null;
  private Integer ipv6Exthdr = null;
  private Integer icmpv6Type = null;
  private String ipv6Src = null;
  private Integer mplsTc = null;
  private Integer tunnelId = null;
  private Integer sctpDst = null;
  private String ethSrc = null;
  private Integer ipv6NdTarget = null;
  private Integer tcpSrc = null;
  private String ipv4Src = null;
  private Integer icmpv6Code = null;
  private Integer mplsBos = null;
  private Long experimentalTeid = null;
  private Integer ipv6NdTll = null;
  private Integer sctpSrc = null;
  private Integer udpDst = null;
  private Integer pbbIsid = null;
  private String ipv6Flabel = null;
  private String inPort = null;
  private Integer icmpv4Code = null;
  private Integer ipDscp = null;
  private String inPhyPort = null;
  private Integer ipProto = null;
  private Integer arpTha = null;
  private Integer arpSpa = null;
  private String ipv6Dst = null;
  private Integer udpSrc = null;
  private Integer arpOp = null;
  private Integer ipv6NdSll = null;
  private Integer vlanVid = null;
  private Integer experimentalGmplsWsonLabel = null;
  private String metadata = null;
  private Integer tcpDst = null;

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("mplsLabel")
  public Integer getMplsLabel() {
    return mplsLabel;
  }
  public void setMplsLabel(Integer mplsLabel) {
    this.mplsLabel = mplsLabel;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("ethType")
  public Integer getEthType() {
    return ethType;
  }
  public void setEthType(Integer ethType) {
    this.ethType = ethType;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("ipEcn")
  public Integer getIpEcn() {
    return ipEcn;
  }
  public void setIpEcn(Integer ipEcn) {
    this.ipEcn = ipEcn;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("icmpv4Type")
  public Integer getIcmpv4Type() {
    return icmpv4Type;
  }
  public void setIcmpv4Type(Integer icmpv4Type) {
    this.icmpv4Type = icmpv4Type;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("ethDst")
  public String getEthDst() {
    return ethDst;
  }
  public void setEthDst(String ethDst) {
    this.ethDst = ethDst;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("vlanPcp")
  public Integer getVlanPcp() {
    return vlanPcp;
  }
  public void setVlanPcp(Integer vlanPcp) {
    this.vlanPcp = vlanPcp;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("ipv4Dst")
  public String getIpv4Dst() {
    return ipv4Dst;
  }
  public void setIpv4Dst(String ipv4Dst) {
    this.ipv4Dst = ipv4Dst;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("arpTpa")
  public Integer getArpTpa() {
    return arpTpa;
  }
  public void setArpTpa(Integer arpTpa) {
    this.arpTpa = arpTpa;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("arpSha")
  public Integer getArpSha() {
    return arpSha;
  }
  public void setArpSha(Integer arpSha) {
    this.arpSha = arpSha;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("ipv6Exthdr")
  public Integer getIpv6Exthdr() {
    return ipv6Exthdr;
  }
  public void setIpv6Exthdr(Integer ipv6Exthdr) {
    this.ipv6Exthdr = ipv6Exthdr;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("icmpv6Type")
  public Integer getIcmpv6Type() {
    return icmpv6Type;
  }
  public void setIcmpv6Type(Integer icmpv6Type) {
    this.icmpv6Type = icmpv6Type;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("ipv6Src")
  public String getIpv6Src() {
    return ipv6Src;
  }
  public void setIpv6Src(String ipv6Src) {
    this.ipv6Src = ipv6Src;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("mplsTc")
  public Integer getMplsTc() {
    return mplsTc;
  }
  public void setMplsTc(Integer mplsTc) {
    this.mplsTc = mplsTc;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("tunnelId")
  public Integer getTunnelId() {
    return tunnelId;
  }
  public void setTunnelId(Integer tunnelId) {
    this.tunnelId = tunnelId;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("sctpDst")
  public Integer getSctpDst() {
    return sctpDst;
  }
  public void setSctpDst(Integer sctpDst) {
    this.sctpDst = sctpDst;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("ethSrc")
  public String getEthSrc() {
    return ethSrc;
  }
  public void setEthSrc(String ethSrc) {
    this.ethSrc = ethSrc;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("ipv6NdTarget")
  public Integer getIpv6NdTarget() {
    return ipv6NdTarget;
  }
  public void setIpv6NdTarget(Integer ipv6NdTarget) {
    this.ipv6NdTarget = ipv6NdTarget;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("tcpSrc")
  public Integer getTcpSrc() {
    return tcpSrc;
  }
  public void setTcpSrc(Integer tcpSrc) {
    this.tcpSrc = tcpSrc;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("ipv4Src")
  public String getIpv4Src() {
    return ipv4Src;
  }
  public void setIpv4Src(String ipv4Src) {
    this.ipv4Src = ipv4Src;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("icmpv6Code")
  public Integer getIcmpv6Code() {
    return icmpv6Code;
  }
  public void setIcmpv6Code(Integer icmpv6Code) {
    this.icmpv6Code = icmpv6Code;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("mplsBos")
  public Integer getMplsBos() {
    return mplsBos;
  }
  public void setMplsBos(Integer mplsBos) {
    this.mplsBos = mplsBos;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("experimentalTeid")
  public Long getExperimentalTeid() {
    return experimentalTeid;
  }
  public void setExperimentalTeid(Long experimentalTeid) {
    this.experimentalTeid = experimentalTeid;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("ipv6NdTll")
  public Integer getIpv6NdTll() {
    return ipv6NdTll;
  }
  public void setIpv6NdTll(Integer ipv6NdTll) {
    this.ipv6NdTll = ipv6NdTll;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("sctpSrc")
  public Integer getSctpSrc() {
    return sctpSrc;
  }
  public void setSctpSrc(Integer sctpSrc) {
    this.sctpSrc = sctpSrc;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("udpDst")
  public Integer getUdpDst() {
    return udpDst;
  }
  public void setUdpDst(Integer udpDst) {
    this.udpDst = udpDst;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("pbbIsid")
  public Integer getPbbIsid() {
    return pbbIsid;
  }
  public void setPbbIsid(Integer pbbIsid) {
    this.pbbIsid = pbbIsid;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("ipv6Flabel")
  public String getIpv6Flabel() {
    return ipv6Flabel;
  }
  public void setIpv6Flabel(String ipv6Flabel) {
    this.ipv6Flabel = ipv6Flabel;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("inPort")
  public String getInPort() {
    return inPort;
  }
  public void setInPort(String inPort) {
    this.inPort = inPort;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("icmpv4Code")
  public Integer getIcmpv4Code() {
    return icmpv4Code;
  }
  public void setIcmpv4Code(Integer icmpv4Code) {
    this.icmpv4Code = icmpv4Code;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("ipDscp")
  public Integer getIpDscp() {
    return ipDscp;
  }
  public void setIpDscp(Integer ipDscp) {
    this.ipDscp = ipDscp;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("inPhyPort")
  public String getInPhyPort() {
    return inPhyPort;
  }
  public void setInPhyPort(String inPhyPort) {
    this.inPhyPort = inPhyPort;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("ipProto")
  public Integer getIpProto() {
    return ipProto;
  }
  public void setIpProto(Integer ipProto) {
    this.ipProto = ipProto;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("arpTha")
  public Integer getArpTha() {
    return arpTha;
  }
  public void setArpTha(Integer arpTha) {
    this.arpTha = arpTha;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("arpSpa")
  public Integer getArpSpa() {
    return arpSpa;
  }
  public void setArpSpa(Integer arpSpa) {
    this.arpSpa = arpSpa;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("ipv6Dst")
  public String getIpv6Dst() {
    return ipv6Dst;
  }
  public void setIpv6Dst(String ipv6Dst) {
    this.ipv6Dst = ipv6Dst;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("udpSrc")
  public Integer getUdpSrc() {
    return udpSrc;
  }
  public void setUdpSrc(Integer udpSrc) {
    this.udpSrc = udpSrc;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("arpOp")
  public Integer getArpOp() {
    return arpOp;
  }
  public void setArpOp(Integer arpOp) {
    this.arpOp = arpOp;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("ipv6NdSll")
  public Integer getIpv6NdSll() {
    return ipv6NdSll;
  }
  public void setIpv6NdSll(Integer ipv6NdSll) {
    this.ipv6NdSll = ipv6NdSll;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("vlanVid")
  public Integer getVlanVid() {
    return vlanVid;
  }
  public void setVlanVid(Integer vlanVid) {
    this.vlanVid = vlanVid;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("experimentalGmplsWsonLabel")
  public Integer getExperimentalGmplsWsonLabel() {
    return experimentalGmplsWsonLabel;
  }
  public void setExperimentalGmplsWsonLabel(Integer experimentalGmplsWsonLabel) {
    this.experimentalGmplsWsonLabel = experimentalGmplsWsonLabel;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("metadata")
  public String getMetadata() {
    return metadata;
  }
  public void setMetadata(String metadata) {
    this.metadata = metadata;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("tcpDst")
  public Integer getTcpDst() {
    return tcpDst;
  }
  public void setTcpDst(Integer tcpDst) {
    this.tcpDst = tcpDst;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class MatchRules {\n");
    
    sb.append("  mplsLabel: ").append(mplsLabel).append("\n");
    sb.append("  ethType: ").append(ethType).append("\n");
    sb.append("  ipEcn: ").append(ipEcn).append("\n");
    sb.append("  icmpv4Type: ").append(icmpv4Type).append("\n");
    sb.append("  ethDst: ").append(ethDst).append("\n");
    sb.append("  vlanPcp: ").append(vlanPcp).append("\n");
    sb.append("  ipv4Dst: ").append(ipv4Dst).append("\n");
    sb.append("  arpTpa: ").append(arpTpa).append("\n");
    sb.append("  arpSha: ").append(arpSha).append("\n");
    sb.append("  ipv6Exthdr: ").append(ipv6Exthdr).append("\n");
    sb.append("  icmpv6Type: ").append(icmpv6Type).append("\n");
    sb.append("  ipv6Src: ").append(ipv6Src).append("\n");
    sb.append("  mplsTc: ").append(mplsTc).append("\n");
    sb.append("  tunnelId: ").append(tunnelId).append("\n");
    sb.append("  sctpDst: ").append(sctpDst).append("\n");
    sb.append("  ethSrc: ").append(ethSrc).append("\n");
    sb.append("  ipv6NdTarget: ").append(ipv6NdTarget).append("\n");
    sb.append("  tcpSrc: ").append(tcpSrc).append("\n");
    sb.append("  ipv4Src: ").append(ipv4Src).append("\n");
    sb.append("  icmpv6Code: ").append(icmpv6Code).append("\n");
    sb.append("  mplsBos: ").append(mplsBos).append("\n");
    sb.append("  experimentalTeid: ").append(experimentalTeid).append("\n");
    sb.append("  ipv6NdTll: ").append(ipv6NdTll).append("\n");
    sb.append("  sctpSrc: ").append(sctpSrc).append("\n");
    sb.append("  udpDst: ").append(udpDst).append("\n");
    sb.append("  pbbIsid: ").append(pbbIsid).append("\n");
    sb.append("  ipv6Flabel: ").append(ipv6Flabel).append("\n");
    sb.append("  inPort: ").append(inPort).append("\n");
    sb.append("  icmpv4Code: ").append(icmpv4Code).append("\n");
    sb.append("  ipDscp: ").append(ipDscp).append("\n");
    sb.append("  inPhyPort: ").append(inPhyPort).append("\n");
    sb.append("  ipProto: ").append(ipProto).append("\n");
    sb.append("  arpTha: ").append(arpTha).append("\n");
    sb.append("  arpSpa: ").append(arpSpa).append("\n");
    sb.append("  ipv6Dst: ").append(ipv6Dst).append("\n");
    sb.append("  udpSrc: ").append(udpSrc).append("\n");
    sb.append("  arpOp: ").append(arpOp).append("\n");
    sb.append("  ipv6NdSll: ").append(ipv6NdSll).append("\n");
    sb.append("  vlanVid: ").append(vlanVid).append("\n");
    sb.append("  experimentalGmplsWsonLabel: ").append(experimentalGmplsWsonLabel).append("\n");
    sb.append("  metadata: ").append(metadata).append("\n");
    sb.append("  tcpDst: ").append(tcpDst).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
