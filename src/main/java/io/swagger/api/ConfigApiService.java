package io.swagger.api;

import io.swagger.api.*;
import io.swagger.model.*;

import com.sun.jersey.multipart.FormDataParam;

import io.swagger.model.Call;
import java.util.*;
import io.swagger.model.Endpoint;
import io.swagger.model.Connection;
import io.swagger.model.MatchRules;
import io.swagger.model.PathType;
import io.swagger.model.Label;
import io.swagger.model.TrafficParams;
import io.swagger.model.TransportLayerType;

import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import javax.ws.rs.core.Response;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JaxRSServerCodegen", date = "2015-10-29T10:48:30.233+01:00")
public abstract class ConfigApiService {
  
      public abstract Response retrieveCalls()
      throws NotFoundException;
  
      public abstract Response updateCallsById(List<Call> calls)
      throws NotFoundException;
  
      public abstract Response createCallsById(List<Call> calls)
      throws NotFoundException;
  
      public abstract Response deleteCallsById()
      throws NotFoundException;
  
      public abstract Response retrieveCallsCallCallById(String callId)
      throws NotFoundException;
  
      public abstract Response updateCallsCallCallById(String callId,Call call)
      throws NotFoundException;
  
      public abstract Response createCallsCallCallById(String callId,Call call)
      throws NotFoundException;
  
      public abstract Response deleteCallsCallCallById(String callId)
      throws NotFoundException;
  
      public abstract Response retrieveCallsCallAEndAEndById(String callId)
      throws NotFoundException;
  
      public abstract Response updateCallsCallAEndAEndById(String callId,Endpoint aEnd)
      throws NotFoundException;
  
      public abstract Response createCallsCallAEndAEndById(String callId,Endpoint aEnd)
      throws NotFoundException;
  
      public abstract Response deleteCallsCallAEndAEndById(String callId)
      throws NotFoundException;
  
      public abstract Response retrieveCallsCallConnectionsConnectionsById(String callId,String connectionId)
      throws NotFoundException;
  
      public abstract Response retrieveCallsCallConnectionsAEndAEndById(String callId,String connectionId)
      throws NotFoundException;
  
      public abstract Response updateCallsCallConnectionsAEndAEndById(String callId,String connectionId,Endpoint aEnd)
      throws NotFoundException;
  
      public abstract Response createCallsCallConnectionsAEndAEndById(String callId,String connectionId,Endpoint aEnd)
      throws NotFoundException;
  
      public abstract Response deleteCallsCallConnectionsAEndAEndById(String callId,String connectionId)
      throws NotFoundException;
  
      public abstract Response retrieveCallsCallConnectionsMatchMatchById(String callId,String connectionId)
      throws NotFoundException;
  
      public abstract Response updateCallsCallConnectionsMatchMatchById(String callId,String connectionId,MatchRules match)
      throws NotFoundException;
  
      public abstract Response createCallsCallConnectionsMatchMatchById(String callId,String connectionId,MatchRules match)
      throws NotFoundException;
  
      public abstract Response deleteCallsCallConnectionsMatchMatchById(String callId,String connectionId)
      throws NotFoundException;
  
      public abstract Response retrieveCallsCallConnectionsPathPathById(String callId,String connectionId)
      throws NotFoundException;
  
      public abstract Response updateCallsCallConnectionsPathPathById(String callId,String connectionId,PathType path)
      throws NotFoundException;
  
      public abstract Response createCallsCallConnectionsPathPathById(String callId,String connectionId,PathType path)
      throws NotFoundException;
  
      public abstract Response deleteCallsCallConnectionsPathPathById(String callId,String connectionId)
      throws NotFoundException;
  
      public abstract Response retrieveCallsCallConnectionsPathLabelLabelById(String callId,String connectionId)
      throws NotFoundException;
  
      public abstract Response updateCallsCallConnectionsPathLabelLabelById(String callId,String connectionId,Label label)
      throws NotFoundException;
  
      public abstract Response createCallsCallConnectionsPathLabelLabelById(String callId,String connectionId,Label label)
      throws NotFoundException;
  
      public abstract Response deleteCallsCallConnectionsPathLabelLabelById(String callId,String connectionId)
      throws NotFoundException;
  
      public abstract Response retrieveCallsCallConnectionsPathTopoComponentsTopoComponentsById(String callId,String connectionId,String endpointId)
      throws NotFoundException;
  
      public abstract Response updateCallsCallConnectionsPathTopoComponentsTopoComponentsById(String callId,String connectionId,String endpointId,Endpoint topoComponents)
      throws NotFoundException;
  
      public abstract Response createCallsCallConnectionsPathTopoComponentsTopoComponentsById(String callId,String connectionId,String endpointId,Endpoint topoComponents)
      throws NotFoundException;
  
      public abstract Response deleteCallsCallConnectionsPathTopoComponentsTopoComponentsById(String callId,String connectionId,String endpointId)
      throws NotFoundException;
  
      public abstract Response retrieveCallsCallConnectionsTrafficParamsTrafficParamsById(String callId,String connectionId)
      throws NotFoundException;
  
      public abstract Response updateCallsCallConnectionsTrafficParamsTrafficParamsById(String callId,String connectionId,TrafficParams trafficParams)
      throws NotFoundException;
  
      public abstract Response createCallsCallConnectionsTrafficParamsTrafficParamsById(String callId,String connectionId,TrafficParams trafficParams)
      throws NotFoundException;
  
      public abstract Response deleteCallsCallConnectionsTrafficParamsTrafficParamsById(String callId,String connectionId)
      throws NotFoundException;
  
      public abstract Response retrieveCallsCallConnectionsTransportLayerTransportLayerById(String callId,String connectionId)
      throws NotFoundException;
  
      public abstract Response updateCallsCallConnectionsTransportLayerTransportLayerById(String callId,String connectionId,TransportLayerType transportLayer)
      throws NotFoundException;
  
      public abstract Response createCallsCallConnectionsTransportLayerTransportLayerById(String callId,String connectionId,TransportLayerType transportLayer)
      throws NotFoundException;
  
      public abstract Response deleteCallsCallConnectionsTransportLayerTransportLayerById(String callId,String connectionId)
      throws NotFoundException;
  
      public abstract Response retrieveCallsCallConnectionsZEndZEndById(String callId,String connectionId)
      throws NotFoundException;
  
      public abstract Response updateCallsCallConnectionsZEndZEndById(String callId,String connectionId,Endpoint zEnd)
      throws NotFoundException;
  
      public abstract Response createCallsCallConnectionsZEndZEndById(String callId,String connectionId,Endpoint zEnd)
      throws NotFoundException;
  
      public abstract Response deleteCallsCallConnectionsZEndZEndById(String callId,String connectionId)
      throws NotFoundException;
  
      public abstract Response retrieveCallsCallMatchMatchById(String callId)
      throws NotFoundException;
  
      public abstract Response updateCallsCallMatchMatchById(String callId,MatchRules match)
      throws NotFoundException;
  
      public abstract Response createCallsCallMatchMatchById(String callId,MatchRules match)
      throws NotFoundException;
  
      public abstract Response deleteCallsCallMatchMatchById(String callId)
      throws NotFoundException;
  
      public abstract Response retrieveCallsCallTrafficParamsTrafficParamsById(String callId)
      throws NotFoundException;
  
      public abstract Response updateCallsCallTrafficParamsTrafficParamsById(String callId,TrafficParams trafficParams)
      throws NotFoundException;
  
      public abstract Response createCallsCallTrafficParamsTrafficParamsById(String callId,TrafficParams trafficParams)
      throws NotFoundException;
  
      public abstract Response deleteCallsCallTrafficParamsTrafficParamsById(String callId)
      throws NotFoundException;
  
      public abstract Response retrieveCallsCallTransportLayerTransportLayerById(String callId)
      throws NotFoundException;
  
      public abstract Response updateCallsCallTransportLayerTransportLayerById(String callId,TransportLayerType transportLayer)
      throws NotFoundException;
  
      public abstract Response createCallsCallTransportLayerTransportLayerById(String callId,TransportLayerType transportLayer)
      throws NotFoundException;
  
      public abstract Response deleteCallsCallTransportLayerTransportLayerById(String callId)
      throws NotFoundException;
  
      public abstract Response retrieveCallsCallZEndZEndById(String callId)
      throws NotFoundException;
  
      public abstract Response updateCallsCallZEndZEndById(String callId,Endpoint zEnd)
      throws NotFoundException;
  
      public abstract Response createCallsCallZEndZEndById(String callId,Endpoint zEnd)
      throws NotFoundException;
  
      public abstract Response deleteCallsCallZEndZEndById(String callId)
      throws NotFoundException;
  
      public abstract Response retrieveConnections()
      throws NotFoundException;
  
      public abstract Response updateConnectionsById(List<Connection> connections)
      throws NotFoundException;
  
      public abstract Response createConnectionsById(List<Connection> connections)
      throws NotFoundException;
  
      public abstract Response deleteConnectionsById()
      throws NotFoundException;
  
      public abstract Response retrieveConnectionsConnectionConnectionById(String connectionId)
      throws NotFoundException;
  
      public abstract Response updateConnectionsConnectionConnectionById(String connectionId,Connection connection)
      throws NotFoundException;
  
      public abstract Response createConnectionsConnectionConnectionById(String connectionId,Connection connection)
      throws NotFoundException;
  
      public abstract Response deleteConnectionsConnectionConnectionById(String connectionId)
      throws NotFoundException;
  
      public abstract Response retrieveConnectionsConnectionAEndAEndById(String connectionId)
      throws NotFoundException;
  
      public abstract Response updateConnectionsConnectionAEndAEndById(String connectionId,Endpoint aEnd)
      throws NotFoundException;
  
      public abstract Response createConnectionsConnectionAEndAEndById(String connectionId,Endpoint aEnd)
      throws NotFoundException;
  
      public abstract Response deleteConnectionsConnectionAEndAEndById(String connectionId)
      throws NotFoundException;
  
      public abstract Response retrieveConnectionsConnectionMatchMatchById(String connectionId)
      throws NotFoundException;
  
      public abstract Response updateConnectionsConnectionMatchMatchById(String connectionId,MatchRules match)
      throws NotFoundException;
  
      public abstract Response createConnectionsConnectionMatchMatchById(String connectionId,MatchRules match)
      throws NotFoundException;
  
      public abstract Response deleteConnectionsConnectionMatchMatchById(String connectionId)
      throws NotFoundException;
  
      public abstract Response retrieveConnectionsConnectionPathPathById(String connectionId)
      throws NotFoundException;
  
      public abstract Response updateConnectionsConnectionPathPathById(String connectionId,PathType path)
      throws NotFoundException;
  
      public abstract Response createConnectionsConnectionPathPathById(String connectionId,PathType path)
      throws NotFoundException;
  
      public abstract Response deleteConnectionsConnectionPathPathById(String connectionId)
      throws NotFoundException;
  
      public abstract Response retrieveConnectionsConnectionPathLabelLabelById(String connectionId)
      throws NotFoundException;
  
      public abstract Response updateConnectionsConnectionPathLabelLabelById(String connectionId,Label label)
      throws NotFoundException;
  
      public abstract Response createConnectionsConnectionPathLabelLabelById(String connectionId,Label label)
      throws NotFoundException;
  
      public abstract Response deleteConnectionsConnectionPathLabelLabelById(String connectionId)
      throws NotFoundException;
  
      public abstract Response retrieveConnectionsConnectionPathTopoComponentsTopoComponentsById(String connectionId,String endpointId)
      throws NotFoundException;
  
      public abstract Response updateConnectionsConnectionPathTopoComponentsTopoComponentsById(String connectionId,String endpointId,Endpoint topoComponents)
      throws NotFoundException;
  
      public abstract Response createConnectionsConnectionPathTopoComponentsTopoComponentsById(String connectionId,String endpointId,Endpoint topoComponents)
      throws NotFoundException;
  
      public abstract Response deleteConnectionsConnectionPathTopoComponentsTopoComponentsById(String connectionId,String endpointId)
      throws NotFoundException;
  
      public abstract Response retrieveConnectionsConnectionTrafficParamsTrafficParamsById(String connectionId)
      throws NotFoundException;
  
      public abstract Response updateConnectionsConnectionTrafficParamsTrafficParamsById(String connectionId,TrafficParams trafficParams)
      throws NotFoundException;
  
      public abstract Response createConnectionsConnectionTrafficParamsTrafficParamsById(String connectionId,TrafficParams trafficParams)
      throws NotFoundException;
  
      public abstract Response deleteConnectionsConnectionTrafficParamsTrafficParamsById(String connectionId)
      throws NotFoundException;
  
      public abstract Response retrieveConnectionsConnectionTransportLayerTransportLayerById(String connectionId)
      throws NotFoundException;
  
      public abstract Response updateConnectionsConnectionTransportLayerTransportLayerById(String connectionId,TransportLayerType transportLayer)
      throws NotFoundException;
  
      public abstract Response createConnectionsConnectionTransportLayerTransportLayerById(String connectionId,TransportLayerType transportLayer)
      throws NotFoundException;
  
      public abstract Response deleteConnectionsConnectionTransportLayerTransportLayerById(String connectionId)
      throws NotFoundException;
  
      public abstract Response retrieveConnectionsConnectionZEndZEndById(String connectionId)
      throws NotFoundException;
  
      public abstract Response updateConnectionsConnectionZEndZEndById(String connectionId,Endpoint zEnd)
      throws NotFoundException;
  
      public abstract Response createConnectionsConnectionZEndZEndById(String connectionId,Endpoint zEnd)
      throws NotFoundException;
  
      public abstract Response deleteConnectionsConnectionZEndZEndById(String connectionId)
      throws NotFoundException;
  
}
