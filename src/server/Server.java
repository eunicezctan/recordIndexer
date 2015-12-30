package server;

import java.io.*;
import java.net.*;
import java.util.logging.*;

import com.sun.net.httpserver.*;

import server.facade.*;


/**
 * 
 * Server that run the Recored Indexer
 * 
 */

public class Server {

	private static final int MAX_WAITING_CONNECTIONS = 10;
	
	private static Logger logger;
	
	static {
		try {
			initLog();
		}
		catch (IOException e) {
			System.out.println("Could not initialize log: " + e.getMessage());
		}
	}
	
	private static void initLog() throws IOException {
		
		Level logLevel = Level.FINE;
		
		logger = Logger.getLogger("record-indexer"); 
		logger.setLevel(logLevel);
		logger.setUseParentHandlers(false);
		
		Handler consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(logLevel);
		consoleHandler.setFormatter(new SimpleFormatter());
		logger.addHandler(consoleHandler);

		FileHandler fileHandler = new FileHandler("log.txt", false);
		fileHandler.setLevel(logLevel);
		fileHandler.setFormatter(new SimpleFormatter());
		logger.addHandler(fileHandler);
	}

	
	private HttpServer server;
	
	private Server() {
		return;
	}
	
	private void run(int SERVER_PORT_NUMBER) {
		
		logger.info("Initializing Model");
		
		try {
			ServerFacade.initialize();		
		}
		catch (ServerException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			return;
		}
		
		logger.info("Initializing HTTP Server");
		
		try {
			server = HttpServer.create(new InetSocketAddress(SERVER_PORT_NUMBER),
											MAX_WAITING_CONNECTIONS);
		} 
		catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);			
			return;
		}

		server.setExecutor(null); // use the default executor
		
		server.createContext("/ValidateUser", ValidateUserHandler);
		server.createContext("/GetProjects", GetProjectsHandler);
		server.createContext("/GetSampleImage", GetSampleImageHandler);
		server.createContext("/DownloadBatch", DownloadBatchHandler);
		server.createContext("/SubmitBatch", SubmitBatchHandler);
		server.createContext("/GetFields", GetFieldsHandler);
		server.createContext("/GetSearch", SearchHandler);
		server.createContext("/", DownloadFileHandler );
		
		logger.info("Starting HTTP Server");

		server.start();
	}

	private HttpHandler ValidateUserHandler = new ValidateUserHandler();
	private HttpHandler GetProjectsHandler = new GetProjectsHandler();
	private HttpHandler GetSampleImageHandler = new GetSampleImageHandler();
	private HttpHandler DownloadBatchHandler = new DownloadBatchHandler();
	private HttpHandler SubmitBatchHandler = new SubmitBatchHandler();
	private HttpHandler GetFieldsHandler = new GetFieldsHandler();
	private HttpHandler SearchHandler = new SearchHandler();
	private HttpHandler DownloadFileHandler = new DownloadFileHandler();
	
	public static void main(String[] args) {
		
		int SERVER_PORT_NUMBER;
		
		if(args.length==0)
			SERVER_PORT_NUMBER = 8088;
		else
			SERVER_PORT_NUMBER=Integer.parseInt(args[0]);
		
		new Server().run(SERVER_PORT_NUMBER);
	}

}
