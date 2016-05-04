package es.tid.swagger.api;

import io.swagger.annotations.ApiParam;

import com.sun.jersey.multipart.FormDataParam;

import es.tid.swagger.api.NotFoundException;
import es.tid.swagger.api.StreamsApiService;
import es.tid.swagger.api.factories.StreamsApiServiceFactory;
import es.tid.swagger.model.*;

import java.util.List;
import java.io.InputStream;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import javax.ws.rs.core.Response;
import javax.ws.rs.*;

@Path("/restconf/streams")


@io.swagger.annotations.Api(value = "/streams", description = "the streams API")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JaxRSServerCodegen", date = "2015-10-29T10:48:30.233+01:00")
public class StreamsApi  {

   private final StreamsApiService delegate = StreamsApiServiceFactory.getStreamsApi();

    @GET
    @Path("/remove_call/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve remove_call by ID", notes = "Retrieve operation of resource: remove_call", response = Call.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Call.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Call.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Remove_call not found", response = Call.class) })

    public Response retrieveRemoveCallById()
    throws NotFoundException {
        return delegate.retrieveRemoveCallById();
    }
    @GET
    @Path("/update_call/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve update_call by ID", notes = "Retrieve operation of resource: update_call", response = Call.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = Call.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID parameter", response = Call.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Update_call not found", response = Call.class) })

    public Response retrieveUpdateCallById()
    throws NotFoundException {
        return delegate.retrieveUpdateCallById();
    }
}

