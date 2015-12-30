package server;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import server.facade.ServerFacade;
import shared.communication.DownloadBatchResult;
import shared.communication.GetSampleImage_Params;

import com.sun.net.httpserver.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;



/**
 * 
 * DownloadBatchHandler to act as an interface between Server and Client Communicator
 * 
 */

public class DownloadBatchHandler implements HttpHandler  {
	
	private Logger logger = Logger.getLogger("DownloadBatchHandler"); 
	
	private XStream xmlStream = new XStream(new DomDriver());	

	@Override
	public void handle(HttpExchange exchange) throws IOException {
				
		DownloadBatchResult result = new DownloadBatchResult ();
		
		GetSampleImage_Params params = (GetSampleImage_Params)xmlStream.fromXML(exchange.getRequestBody());
		
		try {
			result=ServerFacade.DownloadBatch(params);
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
