package server;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import server.facade.ServerFacade;
import shared.communication.GetFieldsResult;
import shared.communication.GetSampleImage_Params;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;


/**
 * 
 * GetFieldsHandler to act as an interface between Server and Client Communicator
 * 
 */
public class GetFieldsHandler implements HttpHandler{
	

	private Logger logger = Logger.getLogger("record-indexer"); 
	
	private XStream xmlStream = new XStream(new DomDriver());	

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		
		GetFieldsResult result = new GetFieldsResult();
		GetSampleImage_Params params = (GetSampleImage_Params)xmlStream.fromXML(exchange.getRequestBody());
		
		try {
			 result=ServerFacade.getField(params);
		
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

}
