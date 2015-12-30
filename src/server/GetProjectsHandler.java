package server;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.*;

import com.sun.net.httpserver.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import shared.communication.*;
import server.facade.*;

/**
 * 
 * GetProjectsHandler to act as an interface between Server and Client Communicator
 * 
 */
public class GetProjectsHandler implements HttpHandler {
	
	private Logger logger = Logger.getLogger("record-indexer"); 
	
	private XStream xmlStream = new XStream(new DomDriver());	

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		
		GetProjectsResult result = new GetProjectsResult();
		ValidateUser_Params params = (ValidateUser_Params)xmlStream.fromXML(exchange.getRequestBody());
		
		try {
			result = ServerFacade.getProjects(params);
		}
		catch (ServerException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			return;				
		}
			
		
		exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
		xmlStream.toXML(result, exchange.getResponseBody());
		exchange.getResponseBody().close();
	}
	
}// end of GetProjectsHandler()
