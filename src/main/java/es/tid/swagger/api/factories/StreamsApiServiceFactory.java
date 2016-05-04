package es.tid.swagger.api.factories;

import es.tid.swagger.api.StreamsApiService;
import es.tid.swagger.api.impl.StreamsApiServiceImpl;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JaxRSServerCodegen", date = "2015-10-29T10:48:30.233+01:00")
public class StreamsApiServiceFactory {

   private final static StreamsApiService service = new StreamsApiServiceImpl();

   public static StreamsApiService getStreamsApi()
   {
      return service;
   }
}
