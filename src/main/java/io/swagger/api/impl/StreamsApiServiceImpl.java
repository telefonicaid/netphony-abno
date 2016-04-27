package io.swagger.api.impl;

import io.swagger.api.*;
import io.swagger.model.*;

import com.sun.jersey.multipart.FormDataParam;

import io.swagger.model.Call;

import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import javax.ws.rs.core.Response;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JaxRSServerCodegen", date = "2015-10-29T10:48:30.233+01:00")
public class StreamsApiServiceImpl extends StreamsApiService {
  
      @Override
      public Response retrieveRemoveCallById()
      throws NotFoundException {
      // do some magicoo2!
      return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magicoo2!")).build();
  }
  
      @Override
      public Response retrieveUpdateCallById()
      throws NotFoundException {
      // do some magicoo2!
      return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magicoo2!")).build();
  }
  
}
