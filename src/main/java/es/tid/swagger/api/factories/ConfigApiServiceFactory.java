package es.tid.swagger.api.factories;

import es.tid.swagger.api.ConfigApiService;
import es.tid.swagger.api.impl.ConfigApiServiceImpl;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JaxRSServerCodegen", date = "2015-10-29T10:48:30.233+01:00")
public class ConfigApiServiceFactory {

   private final static ConfigApiService service = new ConfigApiServiceImpl();

   public static ConfigApiService getConfigApi()
   {
      return service;
   }
}
