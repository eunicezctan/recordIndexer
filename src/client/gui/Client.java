package client.gui;

import client.controller.*;

/**
 *  Main Client login interface	
 */

public class Client {

	public static void main(String[] args) {
		
		String host= args[0];
		String port = args[1];
		
		ClientFacade clientFacade = new ClientFacade(host, port);
				
		Indexer window = new Indexer(clientFacade);
		LoginDialog login = new LoginDialog(clientFacade);
		
		//set pointer between Login and SearchGUI
		login.setPartner(window);
		window.setPartner(login);
		
		login.loginWindow();
		login.setVisible(true);
		
	}

}//end of Client
