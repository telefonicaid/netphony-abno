package es.tid.abno.modules;

import java.io.File;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.jetty.util.log.Log;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.tid.abno.modules.policy.L0PCECapabilities;
import es.tid.abno.modules.policy.MediaChannel;
import es.tid.abno.modules.policy.Policy;




public class ABNOParameters
{
	public static final int ABNOOpticalLayer = 4;
  
  private int abnoPort;
  private int pcepPortPM;
  private PCEParameters pceOpticalLayer;
  private String PMAddress;
  private int abnoMode;
  private String confFile;
  private String ipRedis;
  private Integer portRedis;
  private String Demo=null;
  
  private Hashtable<String, Policy> policy  = new Hashtable<String, Policy>();

  public ABNOParameters()
  {
    setConfFile("ABNOConfiguration.xml");
  }

  public ABNOParameters(String confFile)
  {
    if (confFile != null)
      setConfFile(confFile);
    else
      confFile = "ABNOConfiguration.xml";
  }

  public void initialize()
  {
    try
    {
      DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      File confFile = new File(this.confFile);
      Document doc = builder.parse(confFile);

      NodeList nodes_ABNOPort = doc.getElementsByTagName("ABNOPort");
      Element abnoPort = (Element)nodes_ABNOPort.item(0);
      this.abnoPort = Integer.parseInt(getCharacterDataFromElement(abnoPort));

      NodeList nodes_PCEPPort = doc.getElementsByTagName("PCEPPortPM");
      Element pcepPortPM = (Element)nodes_PCEPPort.item(0);
      this.pcepPortPM = Integer.parseInt(getCharacterDataFromElement(pcepPortPM));

      NodeList nodes_ABNOMode = doc.getElementsByTagName("ABNOMode");
      Element abnoMode = (Element)nodes_ABNOMode.item(0);
      this.abnoMode = Integer.parseInt(getCharacterDataFromElement(abnoMode));
      
      NodeList nodes_PMAddress = doc.getElementsByTagName("PMAddress");
      Element PMAddressEl = (Element)nodes_PMAddress.item(0);
      this.PMAddress = getCharacterDataFromElement(PMAddressEl);
          
      NodeList nodes_PCEPParameters = doc.getElementsByTagName("PCEParameters");
      for (int i = 0; i < nodes_PCEPParameters.getLength(); i++) {
        Element pcepParameters = (Element)nodes_PCEPParameters.item(i);

        NodeList node_PCEPort = pcepParameters.getElementsByTagName("PCEPort");
        Element pcePortElement = (Element)node_PCEPort.item(0);
        int pcePort = Integer.parseInt(getCharacterDataFromElement(pcePortElement));

        NodeList node_PCEAddress = pcepParameters.getElementsByTagName("PCEAddress");
        Element pceAddressElement = (Element)node_PCEAddress.item(0);
        String pceAddress = getCharacterDataFromElement(pceAddressElement);
        
        //Policy
        if (pcepParameters.getElementsByTagName("Policy")!=null){
        	NodeList nodes_Policy = pcepParameters.getElementsByTagName("Policy");
            for (int iPolicy = 0; iPolicy < nodes_Policy.getLength(); iPolicy++) {
            	Element parametersPolicy = (Element)nodes_Policy.item(i);
            	Policy policyread = new Policy();
            	
            	//WFName
            	//If there is Policy, WFName is  required 
            	NodeList nodes_WFName = parametersPolicy.getElementsByTagName("WFName");
                Element wfNameElement = (Element)nodes_WFName.item(0);
                policyread.setWfName(getCharacterDataFromElement(wfNameElement));
                
                //MediaChannel
                if (parametersPolicy.getElementsByTagName("MediaChannel").getLength()!=0){
                	NodeList nodes_MediaChannel = parametersPolicy.getElementsByTagName("MediaChannel");
                    Element mediaChannelParameters = (Element)nodes_MediaChannel.item(0);
                    MediaChannel mediaChannelread = new MediaChannel();
                    
                    if (mediaChannelParameters.getElementsByTagName("OFCode").item(0)!=null){                    	
                    	Element ofCodeElement = (Element) mediaChannelParameters.getElementsByTagName("OFCode").item(0);                       
                        mediaChannelread.setOfCode(Integer.parseInt(getCharacterDataFromElement(ofCodeElement)));                    	
                    }
                    
                    policyread.setMediaChannel(mediaChannelread);
                }
             
                
                //L0PCECapabilities
                if (parametersPolicy.getElementsByTagName("L0PCECapabilities")!=null){
                	NodeList nodes_L0PCECapabilities = parametersPolicy.getElementsByTagName("L0PCECapabilities");
                    Element l0PCECapabilitiesParameters = (Element)nodes_L0PCECapabilities.item(0);
                    L0PCECapabilities l0pceCapabilitiesread = new L0PCECapabilities();
                    if (l0PCECapabilitiesParameters.getElementsByTagName("Instantiation")!=null){
                    	NodeList node_Instantiation = l0PCECapabilitiesParameters.getElementsByTagName("Instantiation");
                        Element instantiationElement = (Element)node_Instantiation.item(0);
                        l0pceCapabilitiesread.setInstantiation(Boolean.parseBoolean(getCharacterDataFromElement(instantiationElement)));
                    }
                    policyread.setL0pceCapabilities(l0pceCapabilitiesread);            	
                }       
                this.policy.put(getCharacterDataFromElement(wfNameElement), policyread);
            }
        }        
        //End policy parameters

        NodeList node_PCEMode = pcepParameters.getElementsByTagName("PCEMode");
        Element pceModeElement = (Element)node_PCEMode.item(0);
        int pceMode = Integer.parseInt(getCharacterDataFromElement(pceModeElement));
        switch (pceMode) {
        case 2:
          this.pceOpticalLayer = new PCEParameters(pceAddress, pcePort);
        }

      }

    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public int getPcepPortPM() { return this.pcepPortPM; }

  public void setPcepPortPM(int pcepPortPM)
  {
    this.pcepPortPM = pcepPortPM;
  }

  private String getCharacterDataFromElement(Element e) {
    Node child = e.getFirstChild();
    if ((child instanceof CharacterData)) {
      CharacterData cd = (CharacterData)child;
      return cd.getData();
    }
    return "?";
  }

  public String getConfFile() {
    return this.confFile;
  }

  public void setConfFile(String confFile) {
    this.confFile = confFile;
  }

  public int getPcepPortTM()
  {
    return this.pcepPortPM;
  }

  public void setPcepPortTM(int pcepPortTM) {
    this.pcepPortPM = pcepPortTM;
  }

  public int getAbnoPort() {
    return this.abnoPort;
  }

  public void setAbnoPort(int abnoPort) {
    this.abnoPort = abnoPort;
  }

  public int getAbnoMode() {
    return this.abnoMode;
  }

  public void setAbnoMode(int abnoMode) {
    this.abnoMode = abnoMode;
  }

  public PCEParameters getPceOpticalLayer() {
    return this.pceOpticalLayer;
  }

  public void setPceOpticalLayer(PCEParameters pceOpticalLayer) {
    this.pceOpticalLayer = pceOpticalLayer;
  }


  public String getPMAddress() {
    return this.PMAddress;
  }

  public void setPMAddress(String pMAddress) {
    this.PMAddress = pMAddress;
  }

  public String getIpRedis() {
    return this.ipRedis;
  }

  public void setIpRedis(String ipRedis) {
    this.ipRedis = ipRedis;
  }

  public Integer getPortRedis() {
    return this.portRedis;
  }

  public void setPortRedis(Integer portRedis) {
    this.portRedis = portRedis;
  }
  
  public String getDemo() {
	return Demo;
  }

  public void setDemo(String demo) {
	Demo = demo;
  }


	public Hashtable<String, Policy> getPolicy() {
		return policy;
	}
	
	public void setPolicy(Hashtable<String, Policy> policy) {
		this.policy = policy;
	}

}