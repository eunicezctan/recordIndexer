package server;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.file.Files;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * 
 * DownLoadFileHandler to act as an interface between Server and Client Communicator
 * 
 */
public class DownloadFileHandler implements HttpHandler  {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		
		File file = new File("."+"/CurrentRecord"+ exchange.getRequestURI().getPath());	
		exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
		Files.copy(file.toPath(), exchange.getResponseBody());
		exchange.getResponseBody().close();
	    
    }

}
