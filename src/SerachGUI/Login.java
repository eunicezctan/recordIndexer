package SerachGUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import shared.communication.ValidateUser_Params;
import client.ClientCommunicator;
import client.ClientException;

public class Login extends JDialog{

	private JLabel lHost;
	private JLabel lPort;
	private JLabel lUser;
	private JLabel lPwd;
	private JTextField host;
	private JTextField port;
	private JTextField user;
	private JPasswordField pwd;
	private JButton submit;
	private SearchGUI search;
	private String getHost;
	private String getPort;
	private String getUsername;
	private String getPwd;
	
	
	public void setPartner(SearchGUI search){
		this.search = search;	
	}//end of setPartner to SearchGUI Class
	
	
	public Login() {
		
		setTitle("Login to Indexer");
		setModal(true);
		setSize(400,300);
		setResizable(false);
		setLocationRelativeTo(null);	
		
		//getHost = "localhost";
		//getPort = "8088";
		//getUsername = "test1";
		//getPwd = "test1";
		
		addWindowListener(new WindowAdapter(){	
			@Override public void windowClosing(WindowEvent e){
				System.exit(0);
			}	
		});
	}// end of constructor
	
	
	
	public void loginWindow(){
		
		JPanel panel= new JPanel(new GridBagLayout());
		add(panel);	
		
		GridBagConstraints grid = new GridBagConstraints();
		
		grid.fill=GridBagConstraints.HORIZONTAL;
		
		lHost = new JLabel("Host: ");
		grid.gridx=0;
		grid.gridy=0;
		grid.gridwidth=1;
		panel.add(lHost,grid);
		
		host  = new JTextField(20);
		grid.gridx=1;
		grid.gridy=0;
		grid.gridwidth=2;
		panel.add(host,grid);
		
		lPort = new JLabel("Port: ");
		grid.gridx=0;
		grid.gridy=1;
		grid.gridwidth=1;
		panel.add(lPort,grid);
		
		port  = new JTextField(20);
		grid.gridx=1;
		grid.gridy=1;
		grid.gridwidth=2;
		panel.add(port,grid);
		
		lUser = new JLabel("User: ");
		grid.gridx=0;
		grid.gridy=2;
		grid.gridwidth=1;
		panel.add(lUser,grid);
		
		user  = new JTextField(20);
		grid.gridx=1;
		grid.gridy=2;
		grid.gridwidth=2;
		panel.add(user,grid);
		
		lPwd = new JLabel("Password: ");
		grid.gridx=0;
		grid.gridy=3;
		grid.gridwidth=1;
		panel.add(lPwd,grid);
		
		pwd = new JPasswordField(20);
		grid.gridx=1;
		grid.gridy=3;
		grid.gridwidth=2;
		panel.add(pwd,grid);
		
		submit = new JButton("Submit");
		grid.gridx=1;
		grid.gridy=4;
		grid.gridwidth=2;
		panel.add(submit,grid);	
		
		submit.addActionListener(new ActionListener() {

			@Override
            public void actionPerformed(ActionEvent e) {
	            
				getHost = host.getText().trim();
				getPort = port.getText().trim();
				getUsername= user.getText().trim();
				getPwd = new String(pwd.getPassword());
				
				ClientCommunicator client = new ClientCommunicator(getHost,getPort );
				ValidateUser_Params getUser = new ValidateUser_Params();
				
				getUser.setUsername(getUsername);
				getUser.setPassword(getPwd);
				
			    try {
	                String userResult = client.ValidateUser(getUser).toString();
	                
	                if (!userResult.equals("FALSE\n"))
	                {
	                	setVisible(false);
	                	search.Project();
	                	search.setVisible(true);
	                }
	                
                }
                catch (ClientException e1) {
	                e1.printStackTrace();
                }      
            }	
		});
	}//end of loginWindow()


	public String getGetHost() {
		return getHost;
	}


	public void setGetHost(String gethost) {
		this.getHost = gethost;
	}


	public String getGetPort() {
		return getPort;
	}


	public void setGetPort(String getPort) {
		this.getPort = getPort;
	}


	public String getGetUsername() {
		return getUsername;
	}


	public void setGetUsername(String getUsername) {
		this.getUsername = getUsername;
	}


	public String getGetPwd() {
		return getPwd;
	}


	public void setGetPwd(String getPwd) {
		this.getPwd = getPwd;
	}

	

}//end of class Login
