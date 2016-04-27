package io.swagger.api;

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
public abstract class StreamsApiService {
  
      public abstract Response retrieveRemoveCallById()
      throws NotFoundException;
  
      public abstract Response retrieveUpdateCallById()
      throws NotFoundException;
  
}
