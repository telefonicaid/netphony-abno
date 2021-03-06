package es.tid.abno.modules;

import java.util.HashMap;
import java.util.LinkedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import es.tid.abno.modules.database.OpTable;


/**
 * @author b.jmgj
 */

public class ABNOCOPController {
	private static Logger log;
	private static ABNOParameters params;
	private static HashMap<Integer,OpTable> OPtable=new HashMap<Integer,OpTable>();
	private static LinkedList<Path_Computation> path_Computationlist;
	 
	public static void main(String[] args) throws Exception {

		log=LoggerFactory.getLogger("COP Controller");
		//Parametros PCE
		/*Load parameters*/
		if (args.length >=1 ){
			params=new ABNOParameters(args[0]);
		}else{
			params=new ABNOParameters();
		}
		params.initialize();
	

		/*Adding Servlets*/
		path_Computationlist = new LinkedList<Path_Computation>(); 
		switch(params.getAbnoMode()){
			case(ABNOParameters.ABNOOpticalLayer):
			{
				log.info("ABNO started with an L0-PCE");
				Path_Computation path_ComputationOL = new Path_Computation(params.getPceOpticalLayer());
				path_Computationlist.add(path_ComputationOL);
				break;
			}
			default: 
			{
				log.info("Error in PCE Parameters");
			}
		}

		
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        log.info("Abnoport: "+params.getAbnoPort());
        Server jettyServer = new Server(params.getAbnoPort());
        jettyServer.setHandler(context);
        ServletHolder jerseyServlet =  
        		context.addServlet( com.sun.jersey.spi.container.servlet.ServletContainer.class, "/*");


        jerseyServlet.setInitParameter(
                "com.sun.jersey.config.property.packages",
                "io.swagger.jaxrs.json;io.swagger.jaxrs.listing;es.tid.swagger.api");
        
        jerseyServlet.setInitParameter(
                "com.sun.jersey.spi.container.ContainerRequestFilters",
                "com.sun.jersey.api.container.filter.PostReplaceFilter");
        
        jerseyServlet.setInitParameter(
                "com.sun.jersey.api.json.POJOMappingFeatures",
                "true");
        
        jerseyServlet.setInitOrder(1);
		
        
        try {
            jettyServer.start();
            //jettyServer.dumpStdErr();
            jettyServer.join();
        } catch(Exception e){
        	log.error(e.getStackTrace().toString());
        }finally {     
            jettyServer.destroy();
        }
		
		
        

        
    }
 
	// Get methods
	//ABNOParameters params
	public static ABNOParameters getParams() {
		return params;
	}
	//HashMap<Integer,OpTable> OPtable
	public static HashMap<Integer,OpTable> getOPtable() {
		return OPtable;
	}
	//ABNOParameters params
	public static LinkedList<Path_Computation> getPath_Computationlist() {
		return path_Computationlist;
	}
		

}