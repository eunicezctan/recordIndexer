package client.gui.topMenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import client.controller.*;
import client.gui.Indexer;
import client.gui.LoginDialog;


/**
 * GUI Layout for File menu	
 */
public class MenuPanel extends JPanel {

	private ClientFacade clientFacade;
	private DownLoadBatchDialog downloadBatch;
	private LoginDialog login;
	private Indexer indexer;
	private BatchState batchState;
	
	private JMenuBar mFile;
	private JMenu file;
	private JMenuItem download;
	private JMenuItem logout;
	private JMenuItem exit;
	

	/**
	 * Constructor
	 */
	public MenuPanel() {
		
    }
	
	
	/**
	 * Set pointer
	 */
	public void setPointer(ClientFacade clientFacade, LoginDialog login, BatchState batchState, Indexer indexer){
		
		this.clientFacade = clientFacade;
		this.batchState = batchState;
        this.login = login;
        this.indexer = indexer;
	}
	

	/**
	 * Main GUI Layout for File menu	
	 */
	public void setMenu() {

		mFile = new JMenuBar();
	    file = new JMenu("File");
	    
	    download = new JMenuItem("Download Batch");
	    logout = new JMenuItem("Logout");
	    exit = new JMenuItem("Exit");
	    
	    file.add(download);
	    file.add(logout);
	    file.add(exit);
	    
	    mFile.add(file);
	    add(mFile);	
	    
	    setActionListener();
	}	
	
	public void setActionListener(){
    // ActionListener for menu items
		download.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
        	
				downloadBatch = new DownLoadBatchDialog(clientFacade, batchState); 
				downloadBatch.setMenu();
				downloadBatch.setVisible(true);
			} 	
		});

		logout.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	
        		batchState.Save();
        		batchState.SaveToXML();
        		batchState.Logout();
        		indexer.setVisible(false);
        		login.setVisible(true);    
        	}	    	
    	});

    	exit.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	
        		batchState.Save();
        		batchState.SaveToXML();
        		System.exit(0);         
        	}   	
    	});
	}

}// end of MenuPanel
