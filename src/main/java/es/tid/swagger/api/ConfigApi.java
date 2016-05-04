package es.tid.swagger.api;

import io.swagger.annotations.ApiParam;

import com.sun.jersey.multipart.FormDataParam;

import es.tid.swagger.api.ConfigApiService;
import es.tid.swagger.api.NotFoundException;
import es.tid.swagger.api.factories.ConfigApiServiceFactory;
import es.tid.swagger.model.*;

import java.util.*;
import java.util.List;
import java.io.InputStream;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import javax.ws.rs.core.Response;
import javax.ws.rs.*;

@Path("/restconf/config")


@io.swagger.annotations.Api(value = "/config", description = "the config API")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JaxRSServerCodegen", date = "2015-10-29T10:48:30.233+01:00")
public class ConfigApi  {

   private final ConfigApiService delegate = ConfigApiServiceFactory.getConfigApi();

    @GET
    @Path("/calls/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve calls", notes = "Retrieve operation of resource: calls", response = Call.class, responseContainer = "List")
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Call.class, responseContainer = "List"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Call.class, responseContainer = "List"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Calls not found", response = Call.class, responseContainer = "List") })

    public Response retrieveCalls()
    throws NotFoundException {
        return delegate.retrieveCalls();
    }
    @PUT
    @Path("/calls/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Update calls by ID", notes = "Update operation of resource: calls", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Calls not found", response = Void.class) })

    public Response updateCallsById(@ApiParam(value = "ID of calls" ,required=true) List<Call> calls)
    throws NotFoundException {
        return delegate.updateCallsById(calls);
    }
    @POST
    @Path("/calls/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Create calls by ID", notes = "Create operation of resource: calls", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Calls not found", response = Void.class) })

    public Response createCallsById(@ApiParam(value = "ID of calls" ,required=true) List<Call> calls)
    throws NotFoundException {
        return delegate.createCallsById(calls);
    }
    @DELETE
    @Path("/calls/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete calls by ID", notes = "Delete operation of resource: calls", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Calls not found", response = Void.class) })

    public Response deleteCallsById()
    throws NotFoundException {
        return delegate.deleteCallsById();
    }
    @GET
    @Path("/calls/call/{callId}/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve call by ID", notes = "Retrieve operation of resource: call", response = Call.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Call.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Call.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Call not found", response = Call.class) })

    public Response retrieveCallsCallCallById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId)
    throws NotFoundException {
        return delegate.retrieveCallsCallCallById(callId);
    }
    @PUT
    @Path("/calls/call/{callId}/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Update call by ID", notes = "Update operation of resource: call", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Call not found", response = Void.class) })

    public Response updateCallsCallCallById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of call" ,required=true) Call call)
    throws NotFoundException {
        return delegate.updateCallsCallCallById(callId,call);
    }
    @POST
    @Path("/calls/call/{callId}/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Create call by ID", notes = "Create operation of resource: call", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Call not found", response = Void.class) })

    public Response createCallsCallCallById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of call" ,required=true) Call call)
    throws NotFoundException {
        return delegate.createCallsCallCallById(callId,call);
    }
    @DELETE
    @Path("/calls/call/{callId}/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete call by ID", notes = "Delete operation of resource: call", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Call not found", response = Void.class) })

    public Response deleteCallsCallCallById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId)
    throws NotFoundException {
        return delegate.deleteCallsCallCallById(callId);
    }
    @GET
    @Path("/calls/call/{callId}/aEnd/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve aEnd by ID", notes = "Retrieve operation of resource: aEnd", response = Endpoint.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Endpoint.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Endpoint.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Aend not found", response = Endpoint.class) })

    public Response retrieveCallsCallAEndAEndById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId)
    throws NotFoundException {
        return delegate.retrieveCallsCallAEndAEndById(callId);
    }
    @PUT
    @Path("/calls/call/{callId}/aEnd/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Update aEnd by ID", notes = "Update operation of resource: aEnd", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Aend not found", response = Void.class) })

    public Response updateCallsCallAEndAEndById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of aEnd" ,required=true) Endpoint aEnd)
    throws NotFoundException {
        return delegate.updateCallsCallAEndAEndById(callId,aEnd);
    }
    @POST
    @Path("/calls/call/{callId}/aEnd/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Create aEnd by ID", notes = "Create operation of resource: aEnd", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Aend not found", response = Void.class) })

    public Response createCallsCallAEndAEndById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of aEnd" ,required=true) Endpoint aEnd)
    throws NotFoundException {
        return delegate.createCallsCallAEndAEndById(callId,aEnd);
    }
    @DELETE
    @Path("/calls/call/{callId}/aEnd/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete aEnd by ID", notes = "Delete operation of resource: aEnd", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Aend not found", response = Void.class) })

    public Response deleteCallsCallAEndAEndById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId)
    throws NotFoundException {
        return delegate.deleteCallsCallAEndAEndById(callId);
    }
    @GET
    @Path("/calls/call/{callId}/connections/{connectionId}/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve connections by ID", notes = "Retrieve operation of resource: connections", response = Connection.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Connection.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Connection.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Connections not found", response = Connection.class) })

    public Response retrieveCallsCallConnectionsConnectionsById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId)
    throws NotFoundException {
        return delegate.retrieveCallsCallConnectionsConnectionsById(callId,connectionId);
    }
    @GET
    @Path("/calls/call/{callId}/connections/{connectionId}/aEnd/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve aEnd by ID", notes = "Retrieve operation of resource: aEnd", response = Endpoint.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Endpoint.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Endpoint.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Aend not found", response = Endpoint.class) })

    public Response retrieveCallsCallConnectionsAEndAEndById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId)
    throws NotFoundException {
        return delegate.retrieveCallsCallConnectionsAEndAEndById(callId,connectionId);
    }
    @PUT
    @Path("/calls/call/{callId}/connections/{connectionId}/aEnd/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Update aEnd by ID", notes = "Update operation of resource: aEnd", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Aend not found", response = Void.class) })

    public Response updateCallsCallConnectionsAEndAEndById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId,
    @ApiParam(value = "ID of aEnd" ,required=true) Endpoint aEnd)
    throws NotFoundException {
        return delegate.updateCallsCallConnectionsAEndAEndById(callId,connectionId,aEnd);
    }
    @POST
    @Path("/calls/call/{callId}/connections/{connectionId}/aEnd/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Create aEnd by ID", notes = "Create operation of resource: aEnd", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Aend not found", response = Void.class) })

    public Response createCallsCallConnectionsAEndAEndById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId,
    @ApiParam(value = "ID of aEnd" ,required=true) Endpoint aEnd)
    throws NotFoundException {
        return delegate.createCallsCallConnectionsAEndAEndById(callId,connectionId,aEnd);
    }
    @DELETE
    @Path("/calls/call/{callId}/connections/{connectionId}/aEnd/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete aEnd by ID", notes = "Delete operation of resource: aEnd", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Aend not found", response = Void.class) })

    public Response deleteCallsCallConnectionsAEndAEndById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId)
    throws NotFoundException {
        return delegate.deleteCallsCallConnectionsAEndAEndById(callId,connectionId);
    }
    @GET
    @Path("/calls/call/{callId}/connections/{connectionId}/match/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve match by ID", notes = "Retrieve operation of resource: match", response = MatchRules.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = MatchRules.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = MatchRules.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Match not found", response = MatchRules.class) })

    public Response retrieveCallsCallConnectionsMatchMatchById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId)
    throws NotFoundException {
        return delegate.retrieveCallsCallConnectionsMatchMatchById(callId,connectionId);
    }
    @PUT
    @Path("/calls/call/{callId}/connections/{connectionId}/match/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Update match by ID", notes = "Update operation of resource: match", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Match not found", response = Void.class) })

    public Response updateCallsCallConnectionsMatchMatchById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId,
    @ApiParam(value = "ID of match" ,required=true) MatchRules match)
    throws NotFoundException {
        return delegate.updateCallsCallConnectionsMatchMatchById(callId,connectionId,match);
    }
    @POST
    @Path("/calls/call/{callId}/connections/{connectionId}/match/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Create match by ID", notes = "Create operation of resource: match", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Match not found", response = Void.class) })

    public Response createCallsCallConnectionsMatchMatchById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId,
    @ApiParam(value = "ID of match" ,required=true) MatchRules match)
    throws NotFoundException {
        return delegate.createCallsCallConnectionsMatchMatchById(callId,connectionId,match);
    }
    @DELETE
    @Path("/calls/call/{callId}/connections/{connectionId}/match/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete match by ID", notes = "Delete operation of resource: match", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Match not found", response = Void.class) })

    public Response deleteCallsCallConnectionsMatchMatchById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId)
    throws NotFoundException {
        return delegate.deleteCallsCallConnectionsMatchMatchById(callId,connectionId);
    }
    @GET
    @Path("/calls/call/{callId}/connections/{connectionId}/path/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve path by ID", notes = "Retrieve operation of resource: path", response = PathType.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = PathType.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = PathType.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Path not found", response = PathType.class) })

    public Response retrieveCallsCallConnectionsPathPathById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId)
    throws NotFoundException {
        return delegate.retrieveCallsCallConnectionsPathPathById(callId,connectionId);
    }
    @PUT
    @Path("/calls/call/{callId}/connections/{connectionId}/path/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Update path by ID", notes = "Update operation of resource: path", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Path not found", response = Void.class) })

    public Response updateCallsCallConnectionsPathPathById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId,
    @ApiParam(value = "ID of path" ,required=true) PathType path)
    throws NotFoundException {
        return delegate.updateCallsCallConnectionsPathPathById(callId,connectionId,path);
    }
    @POST
    @Path("/calls/call/{callId}/connections/{connectionId}/path/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Create path by ID", notes = "Create operation of resource: path", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Path not found", response = Void.class) })

    public Response createCallsCallConnectionsPathPathById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId,
    @ApiParam(value = "ID of path" ,required=true) PathType path)
    throws NotFoundException {
        return delegate.createCallsCallConnectionsPathPathById(callId,connectionId,path);
    }
    @DELETE
    @Path("/calls/call/{callId}/connections/{connectionId}/path/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete path by ID", notes = "Delete operation of resource: path", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Path not found", response = Void.class) })

    public Response deleteCallsCallConnectionsPathPathById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId)
    throws NotFoundException {
        return delegate.deleteCallsCallConnectionsPathPathById(callId,connectionId);
    }
    @GET
    @Path("/calls/call/{callId}/connections/{connectionId}/path/label/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve label by ID", notes = "Retrieve operation of resource: label", response = Label.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Label.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Label.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Label not found", response = Label.class) })

    public Response retrieveCallsCallConnectionsPathLabelLabelById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId)
    throws NotFoundException {
        return delegate.retrieveCallsCallConnectionsPathLabelLabelById(callId,connectionId);
    }
    @PUT
    @Path("/calls/call/{callId}/connections/{connectionId}/path/label/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Update label by ID", notes = "Update operation of resource: label", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Label not found", response = Void.class) })

    public Response updateCallsCallConnectionsPathLabelLabelById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId,
    @ApiParam(value = "ID of label" ,required=true) Label label)
    throws NotFoundException {
        return delegate.updateCallsCallConnectionsPathLabelLabelById(callId,connectionId,label);
    }
    @POST
    @Path("/calls/call/{callId}/connections/{connectionId}/path/label/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Create label by ID", notes = "Create operation of resource: label", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Label not found", response = Void.class) })

    public Response createCallsCallConnectionsPathLabelLabelById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId,
    @ApiParam(value = "ID of label" ,required=true) Label label)
    throws NotFoundException {
        return delegate.createCallsCallConnectionsPathLabelLabelById(callId,connectionId,label);
    }
    @DELETE
    @Path("/calls/call/{callId}/connections/{connectionId}/path/label/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete label by ID", notes = "Delete operation of resource: label", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Label not found", response = Void.class) })

    public Response deleteCallsCallConnectionsPathLabelLabelById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId)
    throws NotFoundException {
        return delegate.deleteCallsCallConnectionsPathLabelLabelById(callId,connectionId);
    }
    @GET
    @Path("/calls/call/{callId}/connections/{connectionId}/path/topo_components/{endpointId}/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve topo_components by ID", notes = "Retrieve operation of resource: topo_components", response = Endpoint.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Endpoint.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Endpoint.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Topo_components not found", response = Endpoint.class) })

    public Response retrieveCallsCallConnectionsPathTopoComponentsTopoComponentsById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId,
    @ApiParam(value = "ID of endpoint",required=true) @PathParam("endpointId") String endpointId)
    throws NotFoundException {
        return delegate.retrieveCallsCallConnectionsPathTopoComponentsTopoComponentsById(callId,connectionId,endpointId);
    }
    @PUT
    @Path("/calls/call/{callId}/connections/{connectionId}/path/topo_components/{endpointId}/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Update topo_components by ID", notes = "Update operation of resource: topo_components", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Topo_components not found", response = Void.class) })

    public Response updateCallsCallConnectionsPathTopoComponentsTopoComponentsById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId,
    @ApiParam(value = "ID of endpoint",required=true) @PathParam("endpointId") String endpointId,
    @ApiParam(value = "ID of topo_components" ,required=true) Endpoint topoComponents)
    throws NotFoundException {
        return delegate.updateCallsCallConnectionsPathTopoComponentsTopoComponentsById(callId,connectionId,endpointId,topoComponents);
    }
    @POST
    @Path("/calls/call/{callId}/connections/{connectionId}/path/topo_components/{endpointId}/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Create topo_components by ID", notes = "Create operation of resource: topo_components", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Topo_components not found", response = Void.class) })

    public Response createCallsCallConnectionsPathTopoComponentsTopoComponentsById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId,
    @ApiParam(value = "ID of endpoint",required=true) @PathParam("endpointId") String endpointId,
    @ApiParam(value = "ID of topo_components" ,required=true) Endpoint topoComponents)
    throws NotFoundException {
        return delegate.createCallsCallConnectionsPathTopoComponentsTopoComponentsById(callId,connectionId,endpointId,topoComponents);
    }
    @DELETE
    @Path("/calls/call/{callId}/connections/{connectionId}/path/topo_components/{endpointId}/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete topo_components by ID", notes = "Delete operation of resource: topo_components", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Topo_components not found", response = Void.class) })

    public Response deleteCallsCallConnectionsPathTopoComponentsTopoComponentsById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId,
    @ApiParam(value = "ID of endpoint",required=true) @PathParam("endpointId") String endpointId)
    throws NotFoundException {
        return delegate.deleteCallsCallConnectionsPathTopoComponentsTopoComponentsById(callId,connectionId,endpointId);
    }
    @GET
    @Path("/calls/call/{callId}/connections/{connectionId}/traffic_params/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve traffic_params by ID", notes = "Retrieve operation of resource: traffic_params", response = TrafficParams.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = TrafficParams.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = TrafficParams.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Traffic_params not found", response = TrafficParams.class) })

    public Response retrieveCallsCallConnectionsTrafficParamsTrafficParamsById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId)
    throws NotFoundException {
        return delegate.retrieveCallsCallConnectionsTrafficParamsTrafficParamsById(callId,connectionId);
    }
    @PUT
    @Path("/calls/call/{callId}/connections/{connectionId}/traffic_params/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Update traffic_params by ID", notes = "Update operation of resource: traffic_params", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Traffic_params not found", response = Void.class) })

    public Response updateCallsCallConnectionsTrafficParamsTrafficParamsById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId,
    @ApiParam(value = "ID of traffic_params" ,required=true) TrafficParams trafficParams)
    throws NotFoundException {
        return delegate.updateCallsCallConnectionsTrafficParamsTrafficParamsById(callId,connectionId,trafficParams);
    }
    @POST
    @Path("/calls/call/{callId}/connections/{connectionId}/traffic_params/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Create traffic_params by ID", notes = "Create operation of resource: traffic_params", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Traffic_params not found", response = Void.class) })

    public Response createCallsCallConnectionsTrafficParamsTrafficParamsById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId,
    @ApiParam(value = "ID of traffic_params" ,required=true) TrafficParams trafficParams)
    throws NotFoundException {
        return delegate.createCallsCallConnectionsTrafficParamsTrafficParamsById(callId,connectionId,trafficParams);
    }
    @DELETE
    @Path("/calls/call/{callId}/connections/{connectionId}/traffic_params/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete traffic_params by ID", notes = "Delete operation of resource: traffic_params", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Traffic_params not found", response = Void.class) })

    public Response deleteCallsCallConnectionsTrafficParamsTrafficParamsById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId)
    throws NotFoundException {
        return delegate.deleteCallsCallConnectionsTrafficParamsTrafficParamsById(callId,connectionId);
    }
    @GET
    @Path("/calls/call/{callId}/connections/{connectionId}/transport_layer/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve transport_layer by ID", notes = "Retrieve operation of resource: transport_layer", response = TransportLayerType.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = TransportLayerType.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = TransportLayerType.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Transport_layer not found", response = TransportLayerType.class) })

    public Response retrieveCallsCallConnectionsTransportLayerTransportLayerById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId)
    throws NotFoundException {
        return delegate.retrieveCallsCallConnectionsTransportLayerTransportLayerById(callId,connectionId);
    }
    @PUT
    @Path("/calls/call/{callId}/connections/{connectionId}/transport_layer/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Update transport_layer by ID", notes = "Update operation of resource: transport_layer", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Transport_layer not found", response = Void.class) })

    public Response updateCallsCallConnectionsTransportLayerTransportLayerById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId,
    @ApiParam(value = "ID of transport_layer" ,required=true) TransportLayerType transportLayer)
    throws NotFoundException {
        return delegate.updateCallsCallConnectionsTransportLayerTransportLayerById(callId,connectionId,transportLayer);
    }
    @POST
    @Path("/calls/call/{callId}/connections/{connectionId}/transport_layer/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Create transport_layer by ID", notes = "Create operation of resource: transport_layer", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Transport_layer not found", response = Void.class) })

    public Response createCallsCallConnectionsTransportLayerTransportLayerById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId,
    @ApiParam(value = "ID of transport_layer" ,required=true) TransportLayerType transportLayer)
    throws NotFoundException {
        return delegate.createCallsCallConnectionsTransportLayerTransportLayerById(callId,connectionId,transportLayer);
    }
    @DELETE
    @Path("/calls/call/{callId}/connections/{connectionId}/transport_layer/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete transport_layer by ID", notes = "Delete operation of resource: transport_layer", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Transport_layer not found", response = Void.class) })

    public Response deleteCallsCallConnectionsTransportLayerTransportLayerById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId)
    throws NotFoundException {
        return delegate.deleteCallsCallConnectionsTransportLayerTransportLayerById(callId,connectionId);
    }
    @GET
    @Path("/calls/call/{callId}/connections/{connectionId}/zEnd/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve zEnd by ID", notes = "Retrieve operation of resource: zEnd", response = Endpoint.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Endpoint.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Endpoint.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Zend not found", response = Endpoint.class) })

    public Response retrieveCallsCallConnectionsZEndZEndById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId)
    throws NotFoundException {
        return delegate.retrieveCallsCallConnectionsZEndZEndById(callId,connectionId);
    }
    @PUT
    @Path("/calls/call/{callId}/connections/{connectionId}/zEnd/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Update zEnd by ID", notes = "Update operation of resource: zEnd", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Zend not found", response = Void.class) })

    public Response updateCallsCallConnectionsZEndZEndById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId,
    @ApiParam(value = "ID of zEnd" ,required=true) Endpoint zEnd)
    throws NotFoundException {
        return delegate.updateCallsCallConnectionsZEndZEndById(callId,connectionId,zEnd);
    }
    @POST
    @Path("/calls/call/{callId}/connections/{connectionId}/zEnd/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Create zEnd by ID", notes = "Create operation of resource: zEnd", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Zend not found", response = Void.class) })

    public Response createCallsCallConnectionsZEndZEndById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId,
    @ApiParam(value = "ID of zEnd" ,required=true) Endpoint zEnd)
    throws NotFoundException {
        return delegate.createCallsCallConnectionsZEndZEndById(callId,connectionId,zEnd);
    }
    @DELETE
    @Path("/calls/call/{callId}/connections/{connectionId}/zEnd/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete zEnd by ID", notes = "Delete operation of resource: zEnd", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Zend not found", response = Void.class) })

    public Response deleteCallsCallConnectionsZEndZEndById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId)
    throws NotFoundException {
        return delegate.deleteCallsCallConnectionsZEndZEndById(callId,connectionId);
    }
    @GET
    @Path("/calls/call/{callId}/match/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve match by ID", notes = "Retrieve operation of resource: match", response = MatchRules.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = MatchRules.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = MatchRules.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Match not found", response = MatchRules.class) })

    public Response retrieveCallsCallMatchMatchById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId)
    throws NotFoundException {
        return delegate.retrieveCallsCallMatchMatchById(callId);
    }
    @PUT
    @Path("/calls/call/{callId}/match/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Update match by ID", notes = "Update operation of resource: match", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Match not found", response = Void.class) })

    public Response updateCallsCallMatchMatchById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of match" ,required=true) MatchRules match)
    throws NotFoundException {
        return delegate.updateCallsCallMatchMatchById(callId,match);
    }
    @POST
    @Path("/calls/call/{callId}/match/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Create match by ID", notes = "Create operation of resource: match", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Match not found", response = Void.class) })

    public Response createCallsCallMatchMatchById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of match" ,required=true) MatchRules match)
    throws NotFoundException {
        return delegate.createCallsCallMatchMatchById(callId,match);
    }
    @DELETE
    @Path("/calls/call/{callId}/match/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete match by ID", notes = "Delete operation of resource: match", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Match not found", response = Void.class) })

    public Response deleteCallsCallMatchMatchById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId)
    throws NotFoundException {
        return delegate.deleteCallsCallMatchMatchById(callId);
    }
    @GET
    @Path("/calls/call/{callId}/traffic_params/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve traffic_params by ID", notes = "Retrieve operation of resource: traffic_params", response = TrafficParams.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = TrafficParams.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = TrafficParams.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Traffic_params not found", response = TrafficParams.class) })

    public Response retrieveCallsCallTrafficParamsTrafficParamsById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId)
    throws NotFoundException {
        return delegate.retrieveCallsCallTrafficParamsTrafficParamsById(callId);
    }
    @PUT
    @Path("/calls/call/{callId}/traffic_params/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Update traffic_params by ID", notes = "Update operation of resource: traffic_params", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Traffic_params not found", response = Void.class) })

    public Response updateCallsCallTrafficParamsTrafficParamsById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of traffic_params" ,required=true) TrafficParams trafficParams)
    throws NotFoundException {
        return delegate.updateCallsCallTrafficParamsTrafficParamsById(callId,trafficParams);
    }
    @POST
    @Path("/calls/call/{callId}/traffic_params/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Create traffic_params by ID", notes = "Create operation of resource: traffic_params", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Traffic_params not found", response = Void.class) })

    public Response createCallsCallTrafficParamsTrafficParamsById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of traffic_params" ,required=true) TrafficParams trafficParams)
    throws NotFoundException {
        return delegate.createCallsCallTrafficParamsTrafficParamsById(callId,trafficParams);
    }
    @DELETE
    @Path("/calls/call/{callId}/traffic_params/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete traffic_params by ID", notes = "Delete operation of resource: traffic_params", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Traffic_params not found", response = Void.class) })

    public Response deleteCallsCallTrafficParamsTrafficParamsById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId)
    throws NotFoundException {
        return delegate.deleteCallsCallTrafficParamsTrafficParamsById(callId);
    }
    @GET
    @Path("/calls/call/{callId}/transport_layer/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve transport_layer by ID", notes = "Retrieve operation of resource: transport_layer", response = TransportLayerType.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = TransportLayerType.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = TransportLayerType.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Transport_layer not found", response = TransportLayerType.class) })

    public Response retrieveCallsCallTransportLayerTransportLayerById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId)
    throws NotFoundException {
        return delegate.retrieveCallsCallTransportLayerTransportLayerById(callId);
    }
    @PUT
    @Path("/calls/call/{callId}/transport_layer/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Update transport_layer by ID", notes = "Update operation of resource: transport_layer", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Transport_layer not found", response = Void.class) })

    public Response updateCallsCallTransportLayerTransportLayerById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of transport_layer" ,required=true) TransportLayerType transportLayer)
    throws NotFoundException {
        return delegate.updateCallsCallTransportLayerTransportLayerById(callId,transportLayer);
    }
    @POST
    @Path("/calls/call/{callId}/transport_layer/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Create transport_layer by ID", notes = "Create operation of resource: transport_layer", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Transport_layer not found", response = Void.class) })

    public Response createCallsCallTransportLayerTransportLayerById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of transport_layer" ,required=true) TransportLayerType transportLayer)
    throws NotFoundException {
        return delegate.createCallsCallTransportLayerTransportLayerById(callId,transportLayer);
    }
    @DELETE
    @Path("/calls/call/{callId}/transport_layer/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete transport_layer by ID", notes = "Delete operation of resource: transport_layer", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Transport_layer not found", response = Void.class) })

    public Response deleteCallsCallTransportLayerTransportLayerById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId)
    throws NotFoundException {
        return delegate.deleteCallsCallTransportLayerTransportLayerById(callId);
    }
    @GET
    @Path("/calls/call/{callId}/zEnd/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve zEnd by ID", notes = "Retrieve operation of resource: zEnd", response = Endpoint.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Endpoint.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Endpoint.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Zend not found", response = Endpoint.class) })

    public Response retrieveCallsCallZEndZEndById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId)
    throws NotFoundException {
        return delegate.retrieveCallsCallZEndZEndById(callId);
    }
    @PUT
    @Path("/calls/call/{callId}/zEnd/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Update zEnd by ID", notes = "Update operation of resource: zEnd", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Zend not found", response = Void.class) })

    public Response updateCallsCallZEndZEndById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of zEnd" ,required=true) Endpoint zEnd)
    throws NotFoundException {
        return delegate.updateCallsCallZEndZEndById(callId,zEnd);
    }
    @POST
    @Path("/calls/call/{callId}/zEnd/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Create zEnd by ID", notes = "Create operation of resource: zEnd", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Zend not found", response = Void.class) })

    public Response createCallsCallZEndZEndById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId,
    @ApiParam(value = "ID of zEnd" ,required=true) Endpoint zEnd)
    throws NotFoundException {
        return delegate.createCallsCallZEndZEndById(callId,zEnd);
    }
    @DELETE
    @Path("/calls/call/{callId}/zEnd/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete zEnd by ID", notes = "Delete operation of resource: zEnd", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Zend not found", response = Void.class) })

    public Response deleteCallsCallZEndZEndById(@ApiParam(value = "ID of call",required=true) @PathParam("callId") String callId)
    throws NotFoundException {
        return delegate.deleteCallsCallZEndZEndById(callId);
    }
    @GET
    @Path("/connections/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve connections", notes = "Retrieve operation of resource: connections", response = Connection.class, responseContainer = "List")
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Connection.class, responseContainer = "List"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Connection.class, responseContainer = "List"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Connections not found", response = Connection.class, responseContainer = "List") })

    public Response retrieveConnections()
    throws NotFoundException {
        return delegate.retrieveConnections();
    }
    @PUT
    @Path("/connections/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Update connections by ID", notes = "Update operation of resource: connections", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Connections not found", response = Void.class) })

    public Response updateConnectionsById(@ApiParam(value = "ID of connections" ,required=true) List<Connection> connections)
    throws NotFoundException {
        return delegate.updateConnectionsById(connections);
    }
    @POST
    @Path("/connections/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Create connections by ID", notes = "Create operation of resource: connections", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Connections not found", response = Void.class) })

    public Response createConnectionsById(@ApiParam(value = "ID of connections" ,required=true) List<Connection> connections)
    throws NotFoundException {
        return delegate.createConnectionsById(connections);
    }
    @DELETE
    @Path("/connections/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete connections by ID", notes = "Delete operation of resource: connections", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Connections not found", response = Void.class) })

    public Response deleteConnectionsById()
    throws NotFoundException {
        return delegate.deleteConnectionsById();
    }
    @GET
    @Path("/connections/connection/{connectionId}/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve connection by ID", notes = "Retrieve operation of resource: connection", response = Connection.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Connection.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Connection.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Connection not found", response = Connection.class) })

    public Response retrieveConnectionsConnectionConnectionById(@ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId)
    throws NotFoundException {
        return delegate.retrieveConnectionsConnectionConnectionById(connectionId);
    }
    @PUT
    @Path("/connections/connection/{connectionId}/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Update connection by ID", notes = "Update operation of resource: connection", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Connection not found", response = Void.class) })

    public Response updateConnectionsConnectionConnectionById(@ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId,
    @ApiParam(value = "ID of connection" ,required=true) Connection connection)
    throws NotFoundException {
        return delegate.updateConnectionsConnectionConnectionById(connectionId,connection);
    }
    @POST
    @Path("/connections/connection/{connectionId}/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Create connection by ID", notes = "Create operation of resource: connection", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Connection not found", response = Void.class) })

    public Response createConnectionsConnectionConnectionById(@ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId,
    @ApiParam(value = "ID of connection" ,required=true) Connection connection)
    throws NotFoundException {
        return delegate.createConnectionsConnectionConnectionById(connectionId,connection);
    }
    @DELETE
    @Path("/connections/connection/{connectionId}/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete connection by ID", notes = "Delete operation of resource: connection", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Connection not found", response = Void.class) })

    public Response deleteConnectionsConnectionConnectionById(@ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId)
    throws NotFoundException {
        return delegate.deleteConnectionsConnectionConnectionById(connectionId);
    }
    @GET
    @Path("/connections/connection/{connectionId}/aEnd/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve aEnd by ID", notes = "Retrieve operation of resource: aEnd", response = Endpoint.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Endpoint.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Endpoint.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Aend not found", response = Endpoint.class) })

    public Response retrieveConnectionsConnectionAEndAEndById(@ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId)
    throws NotFoundException {
        return delegate.retrieveConnectionsConnectionAEndAEndById(connectionId);
    }
    @PUT
    @Path("/connections/connection/{connectionId}/aEnd/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Update aEnd by ID", notes = "Update operation of resource: aEnd", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Aend not found", response = Void.class) })

    public Response updateConnectionsConnectionAEndAEndById(@ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId,
    @ApiParam(value = "ID of aEnd" ,required=true) Endpoint aEnd)
    throws NotFoundException {
        return delegate.updateConnectionsConnectionAEndAEndById(connectionId,aEnd);
    }
    @POST
    @Path("/connections/connection/{connectionId}/aEnd/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Create aEnd by ID", notes = "Create operation of resource: aEnd", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Aend not found", response = Void.class) })

    public Response createConnectionsConnectionAEndAEndById(@ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId,
    @ApiParam(value = "ID of aEnd" ,required=true) Endpoint aEnd)
    throws NotFoundException {
        return delegate.createConnectionsConnectionAEndAEndById(connectionId,aEnd);
    }
    @DELETE
    @Path("/connections/connection/{connectionId}/aEnd/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete aEnd by ID", notes = "Delete operation of resource: aEnd", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Aend not found", response = Void.class) })

    public Response deleteConnectionsConnectionAEndAEndById(@ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId)
    throws NotFoundException {
        return delegate.deleteConnectionsConnectionAEndAEndById(connectionId);
    }
    @GET
    @Path("/connections/connection/{connectionId}/match/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve match by ID", notes = "Retrieve operation of resource: match", response = MatchRules.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = MatchRules.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = MatchRules.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Match not found", response = MatchRules.class) })

    public Response retrieveConnectionsConnectionMatchMatchById(@ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId)
    throws NotFoundException {
        return delegate.retrieveConnectionsConnectionMatchMatchById(connectionId);
    }
    @PUT
    @Path("/connections/connection/{connectionId}/match/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Update match by ID", notes = "Update operation of resource: match", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Match not found", response = Void.class) })

    public Response updateConnectionsConnectionMatchMatchById(@ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId,
    @ApiParam(value = "ID of match" ,required=true) MatchRules match)
    throws NotFoundException {
        return delegate.updateConnectionsConnectionMatchMatchById(connectionId,match);
    }
    @POST
    @Path("/connections/connection/{connectionId}/match/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Create match by ID", notes = "Create operation of resource: match", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Match not found", response = Void.class) })

    public Response createConnectionsConnectionMatchMatchById(@ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId,
    @ApiParam(value = "ID of match" ,required=true) MatchRules match)
    throws NotFoundException {
        return delegate.createConnectionsConnectionMatchMatchById(connectionId,match);
    }
    @DELETE
    @Path("/connections/connection/{connectionId}/match/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete match by ID", notes = "Delete operation of resource: match", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Match not found", response = Void.class) })

    public Response deleteConnectionsConnectionMatchMatchById(@ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId)
    throws NotFoundException {
        return delegate.deleteConnectionsConnectionMatchMatchById(connectionId);
    }
    @GET
    @Path("/connections/connection/{connectionId}/path/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve path by ID", notes = "Retrieve operation of resource: path", response = PathType.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = PathType.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = PathType.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Path not found", response = PathType.class) })

    public Response retrieveConnectionsConnectionPathPathById(@ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId)
    throws NotFoundException {
        return delegate.retrieveConnectionsConnectionPathPathById(connectionId);
    }
    @PUT
    @Path("/connections/connection/{connectionId}/path/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Update path by ID", notes = "Update operation of resource: path", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Path not found", response = Void.class) })

    public Response updateConnectionsConnectionPathPathById(@ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId,
    @ApiParam(value = "ID of path" ,required=true) PathType path)
    throws NotFoundException {
        return delegate.updateConnectionsConnectionPathPathById(connectionId,path);
    }
    @POST
    @Path("/connections/connection/{connectionId}/path/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Create path by ID", notes = "Create operation of resource: path", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Path not found", response = Void.class) })

    public Response createConnectionsConnectionPathPathById(@ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId,
    @ApiParam(value = "ID of path" ,required=true) PathType path)
    throws NotFoundException {
        return delegate.createConnectionsConnectionPathPathById(connectionId,path);
    }
    @DELETE
    @Path("/connections/connection/{connectionId}/path/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete path by ID", notes = "Delete operation of resource: path", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Path not found", response = Void.class) })

    public Response deleteConnectionsConnectionPathPathById(@ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId)
    throws NotFoundException {
        return delegate.deleteConnectionsConnectionPathPathById(connectionId);
    }
    @GET
    @Path("/connections/connection/{connectionId}/path/label/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve label by ID", notes = "Retrieve operation of resource: label", response = Label.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Label.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Label.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Label not found", response = Label.class) })

    public Response retrieveConnectionsConnectionPathLabelLabelById(@ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId)
    throws NotFoundException {
        return delegate.retrieveConnectionsConnectionPathLabelLabelById(connectionId);
    }
    @PUT
    @Path("/connections/connection/{connectionId}/path/label/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Update label by ID", notes = "Update operation of resource: label", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Label not found", response = Void.class) })

    public Response updateConnectionsConnectionPathLabelLabelById(@ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId,
    @ApiParam(value = "ID of label" ,required=true) Label label)
    throws NotFoundException {
        return delegate.updateConnectionsConnectionPathLabelLabelById(connectionId,label);
    }
    @POST
    @Path("/connections/connection/{connectionId}/path/label/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Create label by ID", notes = "Create operation of resource: label", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Label not found", response = Void.class) })

    public Response createConnectionsConnectionPathLabelLabelById(@ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId,
    @ApiParam(value = "ID of label" ,required=true) Label label)
    throws NotFoundException {
        return delegate.createConnectionsConnectionPathLabelLabelById(connectionId,label);
    }
    @DELETE
    @Path("/connections/connection/{connectionId}/path/label/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete label by ID", notes = "Delete operation of resource: label", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Label not found", response = Void.class) })

    public Response deleteConnectionsConnectionPathLabelLabelById(@ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId)
    throws NotFoundException {
        return delegate.deleteConnectionsConnectionPathLabelLabelById(connectionId);
    }
    @GET
    @Path("/connections/connection/{connectionId}/path/topo_components/{endpointId}/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve topo_components by ID", notes = "Retrieve operation of resource: topo_components", response = Endpoint.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Endpoint.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Endpoint.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Topo_components not found", response = Endpoint.class) })

    public Response retrieveConnectionsConnectionPathTopoComponentsTopoComponentsById(@ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId,
    @ApiParam(value = "ID of endpoint",required=true) @PathParam("endpointId") String endpointId)
    throws NotFoundException {
        return delegate.retrieveConnectionsConnectionPathTopoComponentsTopoComponentsById(connectionId,endpointId);
    }
    @PUT
    @Path("/connections/connection/{connectionId}/path/topo_components/{endpointId}/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Update topo_components by ID", notes = "Update operation of resource: topo_components", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Topo_components not found", response = Void.class) })

    public Response updateConnectionsConnectionPathTopoComponentsTopoComponentsById(@ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId,
    @ApiParam(value = "ID of endpoint",required=true) @PathParam("endpointId") String endpointId,
    @ApiParam(value = "ID of topo_components" ,required=true) Endpoint topoComponents)
    throws NotFoundException {
        return delegate.updateConnectionsConnectionPathTopoComponentsTopoComponentsById(connectionId,endpointId,topoComponents);
    }
    @POST
    @Path("/connections/connection/{connectionId}/path/topo_components/{endpointId}/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Create topo_components by ID", notes = "Create operation of resource: topo_components", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Topo_components not found", response = Void.class) })

    public Response createConnectionsConnectionPathTopoComponentsTopoComponentsById(@ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId,
    @ApiParam(value = "ID of endpoint",required=true) @PathParam("endpointId") String endpointId,
    @ApiParam(value = "ID of topo_components" ,required=true) Endpoint topoComponents)
    throws NotFoundException {
        return delegate.createConnectionsConnectionPathTopoComponentsTopoComponentsById(connectionId,endpointId,topoComponents);
    }
    @DELETE
    @Path("/connections/connection/{connectionId}/path/topo_components/{endpointId}/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete topo_components by ID", notes = "Delete operation of resource: topo_components", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Topo_components not found", response = Void.class) })

    public Response deleteConnectionsConnectionPathTopoComponentsTopoComponentsById(@ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId,
    @ApiParam(value = "ID of endpoint",required=true) @PathParam("endpointId") String endpointId)
    throws NotFoundException {
        return delegate.deleteConnectionsConnectionPathTopoComponentsTopoComponentsById(connectionId,endpointId);
    }
    @GET
    @Path("/connections/connection/{connectionId}/traffic_params/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve traffic_params by ID", notes = "Retrieve operation of resource: traffic_params", response = TrafficParams.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = TrafficParams.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = TrafficParams.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Traffic_params not found", response = TrafficParams.class) })

    public Response retrieveConnectionsConnectionTrafficParamsTrafficParamsById(@ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId)
    throws NotFoundException {
        return delegate.retrieveConnectionsConnectionTrafficParamsTrafficParamsById(connectionId);
    }
    @PUT
    @Path("/connections/connection/{connectionId}/traffic_params/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Update traffic_params by ID", notes = "Update operation of resource: traffic_params", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Traffic_params not found", response = Void.class) })

    public Response updateConnectionsConnectionTrafficParamsTrafficParamsById(@ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId,
    @ApiParam(value = "ID of traffic_params" ,required=true) TrafficParams trafficParams)
    throws NotFoundException {
        return delegate.updateConnectionsConnectionTrafficParamsTrafficParamsById(connectionId,trafficParams);
    }
    @POST
    @Path("/connections/connection/{connectionId}/traffic_params/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Create traffic_params by ID", notes = "Create operation of resource: traffic_params", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Traffic_params not found", response = Void.class) })

    public Response createConnectionsConnectionTrafficParamsTrafficParamsById(@ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId,
    @ApiParam(value = "ID of traffic_params" ,required=true) TrafficParams trafficParams)
    throws NotFoundException {
        return delegate.createConnectionsConnectionTrafficParamsTrafficParamsById(connectionId,trafficParams);
    }
    @DELETE
    @Path("/connections/connection/{connectionId}/traffic_params/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete traffic_params by ID", notes = "Delete operation of resource: traffic_params", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Traffic_params not found", response = Void.class) })

    public Response deleteConnectionsConnectionTrafficParamsTrafficParamsById(@ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId)
    throws NotFoundException {
        return delegate.deleteConnectionsConnectionTrafficParamsTrafficParamsById(connectionId);
    }
    @GET
    @Path("/connections/connection/{connectionId}/transport_layer/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve transport_layer by ID", notes = "Retrieve operation of resource: transport_layer", response = TransportLayerType.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = TransportLayerType.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = TransportLayerType.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Transport_layer not found", response = TransportLayerType.class) })

    public Response retrieveConnectionsConnectionTransportLayerTransportLayerById(@ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId)
    throws NotFoundException {
        return delegate.retrieveConnectionsConnectionTransportLayerTransportLayerById(connectionId);
    }
    @PUT
    @Path("/connections/connection/{connectionId}/transport_layer/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Update transport_layer by ID", notes = "Update operation of resource: transport_layer", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Transport_layer not found", response = Void.class) })

    public Response updateConnectionsConnectionTransportLayerTransportLayerById(@ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId,
    @ApiParam(value = "ID of transport_layer" ,required=true) TransportLayerType transportLayer)
    throws NotFoundException {
        return delegate.updateConnectionsConnectionTransportLayerTransportLayerById(connectionId,transportLayer);
    }
    @POST
    @Path("/connections/connection/{connectionId}/transport_layer/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Create transport_layer by ID", notes = "Create operation of resource: transport_layer", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Transport_layer not found", response = Void.class) })

    public Response createConnectionsConnectionTransportLayerTransportLayerById(@ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId,
    @ApiParam(value = "ID of transport_layer" ,required=true) TransportLayerType transportLayer)
    throws NotFoundException {
        return delegate.createConnectionsConnectionTransportLayerTransportLayerById(connectionId,transportLayer);
    }
    @DELETE
    @Path("/connections/connection/{connectionId}/transport_layer/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete transport_layer by ID", notes = "Delete operation of resource: transport_layer", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Transport_layer not found", response = Void.class) })

    public Response deleteConnectionsConnectionTransportLayerTransportLayerById(@ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId)
    throws NotFoundException {
        return delegate.deleteConnectionsConnectionTransportLayerTransportLayerById(connectionId);
    }
    @GET
    @Path("/connections/connection/{connectionId}/zEnd/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve zEnd by ID", notes = "Retrieve operation of resource: zEnd", response = Endpoint.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Endpoint.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Endpoint.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Zend not found", response = Endpoint.class) })

    public Response retrieveConnectionsConnectionZEndZEndById(@ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId)
    throws NotFoundException {
        return delegate.retrieveConnectionsConnectionZEndZEndById(connectionId);
    }
    @PUT
    @Path("/connections/connection/{connectionId}/zEnd/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Update zEnd by ID", notes = "Update operation of resource: zEnd", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Zend not found", response = Void.class) })

    public Response updateConnectionsConnectionZEndZEndById(@ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId,
    @ApiParam(value = "ID of zEnd" ,required=true) Endpoint zEnd)
    throws NotFoundException {
        return delegate.updateConnectionsConnectionZEndZEndById(connectionId,zEnd);
    }
    @POST
    @Path("/connections/connection/{connectionId}/zEnd/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Create zEnd by ID", notes = "Create operation of resource: zEnd", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Zend not found", response = Void.class) })

    public Response createConnectionsConnectionZEndZEndById(@ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId,
    @ApiParam(value = "ID of zEnd" ,required=true) Endpoint zEnd)
    throws NotFoundException {
        return delegate.createConnectionsConnectionZEndZEndById(connectionId,zEnd);
    }
    @DELETE
    @Path("/connections/connection/{connectionId}/zEnd/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete zEnd by ID", notes = "Delete operation of resource: zEnd", response = Void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Zend not found", response = Void.class) })

    public Response deleteConnectionsConnectionZEndZEndById(@ApiParam(value = "ID of connection",required=true) @PathParam("connectionId") String connectionId)
    throws NotFoundException {
        return delegate.deleteConnectionsConnectionZEndZEndById(connectionId);
    }
}

