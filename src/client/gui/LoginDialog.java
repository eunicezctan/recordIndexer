package client.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.*;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import client.controller.BatchState;
import client.controller.ClientFacade;



/**
 *  Login GUI	
 */
public class LoginDialog extends JDialog {

	private ClientFacade clientFacade;
	private BatchState batchState;
	private Indexer window;
	private JLabel lUser;
	private JLabel lPwd;
	private JTextField user;
	private JPasswordField pwd;
	private JButton login;
	private JButton exit;
	private Boolean userState; 
	
		
	/**
	 *  Constructor	
	 */
	public LoginDialog(ClientFacade clientFacade) {
		
		this.clientFacade = clientFacade; 
		
		setTitle("Login to Indexer");
		setModal(true);
		setSize(400,140);
		setResizable(false);
		setLocationRelativeTo(null);	
		setLayout(new BorderLayout());
		userState = false;
			
		addWindowListener(new WindowAdapter(){	
			@Override public void windowClosing(WindowEvent e){
				System.exit(0);
			}	
		});
	}
	
	
	
	/**
	 *  Set pointer to Indexer Class	
	 */
	public void setPartner(Indexer window){
			this.window = window;	
	}
	
	
		
	/**
	 * Main panel for login screen		
	 */
	public void loginWindow(){
			
		JPanel pUser= new JPanel(new GridBagLayout());
		add(pUser,BorderLayout.CENTER);	
			
		GridBagConstraints grid = new GridBagConstraints();
		grid.fill=GridBagConstraints.NONE;
			
		lUser = new JLabel("User: ");
		grid.gridx=0;
		grid.gridy=1;
		grid.gridwidth=1;
		pUser.add(lUser,grid);
			
		user  = new JTextField(20);
		user.setPreferredSize(new Dimension(150, 30));
		grid.gridx=1;
		grid.gridy=1;
		grid.gridwidth=2;
		pUser.add(user,grid);
			
		lPwd = new JLabel("Password: ");
		grid.gridx=0;
		grid.gridy=2;
		grid.gridwidth=1;
		pUser.add(lPwd,grid);
			
		pwd = new JPasswordField(20);
		pwd.setPreferredSize(new Dimension(150, 30));
		grid.gridx=1;
		grid.gridy=2;
		grid.gridwidth=2;
		pUser.add(pwd,grid);
		
		JPanel pLogin= new JPanel(new GridBagLayout());
		add(pLogin,BorderLayout.SOUTH);	
		
		GridBagConstraints gLogin = new GridBagConstraints();
		gLogin.fill=GridBagConstraints.NONE;
			
		login = new JButton("Login");
		login.setPreferredSize(new Dimension(80, 30));
		gLogin.gridx=1;
		gLogin.gridy=0;
		grid.gridwidth=2;
		pLogin.add(login,gLogin);	
		
		exit = new JButton("Exit");
		exit.setPreferredSize(new Dimension(80, 30));
		gLogin.gridx=3;
		gLogin.gridy=0;
		grid.gridwidth=2;
		pLogin.add(exit,gLogin);
		
		//setup indexer window menu
		window.IndexerWindow();
		
		setActionListener();	
	}
	
	
   /**
    * ActionListener for login and exit button
    */
	public void setActionListener(){
		
		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		        
				clientFacade.setGetUsername(user.getText().trim());
				clientFacade.setGetPwd(new String(pwd.getPassword()));
		        String userResult = clientFacade.ValidateUser();
		        
		           if (!userResult.equals("FALSE\n"))
		           {
		        	  String printUser [] = userResult.split("\n");
		        	  String name = user.getText().trim();
		        	  JOptionPane.showMessageDialog(null, "Welcome, "+ name +"\n" + "You have indexed " 
		        			  						+ printUser[3] +" Records","Welocme", JOptionPane.PLAIN_MESSAGE);   
		        	
		        	  user.setText("");
		              pwd.setText("");
		              
		              //call check checkUserState
		        	  checkUserState(name);
		              window.setPointer(batchState);
		              
		             if(userState)
		            	 window.loadExistingUserState ();
		              
		              setVisible(false);
		              window.setVisible(true); 
		           }
		           else
		        	   JOptionPane.showMessageDialog(null, "Invalid Login", "Error", JOptionPane.ERROR_MESSAGE);    
	        }	
		});
		
		exit.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
				System.exit(0);
             }				
		});
		
	}
	

	/**
	 *  Check for user file exist	
	 */
	public void checkUserState(String name){
		
		userState = false;
		File getFile = new File(name+".xml");
    	  
   	  	if(getFile.exists())
   	  		setUserState(name);
   		 
   	  	else
   	  		newUserState(name);
		
	}
	
	
	/**
	 *  Called if user file exist
	 */
	public void setUserState(String name){
		
		XStream xStream = new XStream(new DomDriver());
		InputStream inFile;
        try {
	        inFile = new BufferedInputStream(new FileInputStream(name+".xml"));
	        batchState = (BatchState) xStream.fromXML(inFile);
	        inFile.close();
			
			batchState.BatchUserState();
			batchState.setClient(clientFacade);
			userState = true;
        }
        catch (IOException e) {
	        e.printStackTrace();
        }
	}
	
	

	/**
	 *  Called if user does not exist
	 */
	public void newUserState(String name) {
	 
		batchState = new BatchState();
		
		batchState.setClient(clientFacade);
		batchState.setNameUser(name);
		
	}
	
	
}// end of class InLogin
