package es.tid.abno.modules;

import java.util.HashMap;
import java.util.LinkedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import es.tid.abno.modules.database.OpTable;




public class ABNOController {
	private static Logger log=LoggerFactory.getLogger("ABNO Controller");
	private static ABNOParameters params;
	private static HashMap<Integer,OpTable> OPtable=new HashMap<Integer,OpTable>();
	
	public static void main(String[] args) 
	{
		/*Load parameters*/
		if (args.length >=1 ){
			params=new ABNOParameters(args[0]);
		}else{
			params=new ABNOParameters();
		}
		params.initialize();
				
		/*Listen for json requests*/
		System.out.println("Abnoport: "+params.getAbnoPort());
		Server server = new Server(params.getAbnoPort());
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);	 
		context.setContextPath("/");
		server.setHandler(context);

		/*Adding Servlets*/
		/*Each case is a workflow scenario*/
		LinkedList<Path_Computation> path_Computationlist = new LinkedList<Path_Computation>(); 
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
		
		/*
		 * STARTING SERVLET
		 */
		context.addServlet(new ServletHolder(new AbnoServlet(path_Computationlist,params, OPtable)),"/");

		
		try {
			server.start();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			server.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
