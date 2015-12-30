package client.gui.topMenu;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import shared.communication.DownloadBatchResult;
import client.controller.*;


/**
 *  Download Batch GUI for File Menu
 */
public class DownLoadBatchDialog extends JDialog implements BatchListener {
	
	private ClientFacade clientFacade;
	private BatchState batchState;
	
	private JPanel top;
	private JLabel lPro;
	private JComboBox cPro;
	private JButton view;
	
	private JPanel bottom;
	private JButton cancel;
	private JButton download;
	
	
	/**
	 * Constructor
	 */
	public DownLoadBatchDialog(ClientFacade clientFacade, BatchState batchState) {
		
		this.clientFacade = clientFacade;
        this.batchState = batchState;
        
		setTitle("Download");
		setModal(true);
		setSize(500,100);
		setResizable(false);
		setLayout(new BorderLayout());
		setLocationRelativeTo(null);		
		batchState.addListener(this);
	}

	
	/**
	 * GUI Layout for Download Batch	
	 */
	public void setMenu() {
	   
		top = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    add(top, BorderLayout.CENTER);
	    
	    lPro = new JLabel("Projects: ");
	    top.add(lPro);
	    
	    cPro  = new JComboBox();
	    getProject();
		top.add(cPro);
		
		view = new JButton("View Sample");
		top.add(view);
		
		bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
		add(bottom, BorderLayout.SOUTH);
		
		cancel = new JButton("Cancel");
		bottom.add(cancel);
		
		download = new JButton("Download");
		bottom.add(download);
		
		setActionListener();	
	}
	
	
	public void setActionListener() {
		
		//ActionListener for Buttons
		view.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			            	
		Image image=null;
		int project_id = cPro.getSelectedIndex();
		URL url;
			
		try {
			 	url = new URL (clientFacade.GetSampleImage(project_id+1));
			 	image = ImageIO.read(url);
			 	Image sImage = image.getScaledInstance(600, 400, Image.SCALE_DEFAULT);
										
			 	JDialog frame = new JDialog();
			 	frame.setSize(600,400);
			 	frame.setModal(true);
				frame.setResizable(false);
				frame.setAlwaysOnTop(true);
				frame.setLocationRelativeTo(null);
							
				JLabel label =  new JLabel(new ImageIcon(sImage));
				frame.add(label);
				frame.setVisible(true);
		     }
		                        
		  catch (IOException e1) {
			     e1.printStackTrace();}}		
			});	
				
				cancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
			    dispose();
			        	
				}
			});
				
				 download.addActionListener(new ActionListener() {
			     public void actionPerformed(ActionEvent e) {
			        	
			     DownloadBatchResult getBatchRes;
			     int project_id = cPro.getSelectedIndex();
			     getBatchRes = clientFacade.Download(project_id+1);
			        	
			     if(getBatchRes.getSetIndex()==1)
			     {
			        batchState.Download(getBatchRes);
			        batchState.setRecordCell();
			     }
			        		
			     else
			        JOptionPane.showMessageDialog(null, "Batch Already Assign", "Error", JOptionPane.ERROR_MESSAGE); 	
			        dispose();
			     	}
				 });	
		}
	
	/**
	 *  Get list of project
	 */	
	public void getProject(){
	
	    String getList[] = clientFacade.GetProjects().split("\n");
	    DefaultComboBoxModel addPro = new  DefaultComboBoxModel();
		for(int i=1; i<getList.length; i+=2) 
			 addPro.addElement(getList[i]);
			   
		cPro.setModel(addPro);	
	}

	
	/**
	 *  BatchState Listener methods
	 */
	@Override
    public void selectedCell(String[][] cellValue) {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    public void cellChanged(String newCellValue) {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    public void Zoom(double zoomNo, boolean setZoom) {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    public void Download(DownloadBatchResult getBatchRes) {	
		 // TODO Auto-generated method stub
    }

	@Override
    public void Submit() {
		 // TODO Auto-generated method stub
    }

	@Override
    public void Toggle() {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    public void Save() {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    public void Invert() {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    public void Logout() {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    public void Zoom(double zoomNo) {
	    // TODO Auto-generated method stub
	    
    }
	
}//end of DownLoadBatchDialog
