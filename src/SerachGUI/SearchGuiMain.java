package SerachGUI;

import client.ClientException;

public class SearchGuiMain {

	public static void main(String[] args) throws ClientException {
		
		SearchGUI search = new SearchGUI();
		Login login = new Login();
		
		//set pointer between Login and SearchGUI
		login.setPartner(search);
		search.setPartner(login);
		
		login.loginWindow();
		login.setVisible(true);
		//search.Project();
		//search.setVisible(true);		
	}

}
