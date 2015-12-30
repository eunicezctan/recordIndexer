package client.gui.topMenu;

import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import shared.communication.DownloadBatchResult;
import client.controller.*;

public class ButtonPanel extends JPanel implements BatchListener {
	
	private JButton zoomIn;
	private JButton zoomOut;
	private JButton invert;
	private JButton toogle;
	private JButton save;
	private JButton submit;
	private BatchState batchState;


	/**
	 * Set pointer
	 */
	public void setPointer(BatchState batchState){
		
		this.batchState = batchState;
		this.batchState.addListener(this);	
	}
	
	/**
	 * Constructor
	 */
	public ButtonPanel() {
	   
      setLayout(new FlowLayout(FlowLayout.LEFT));
      
      zoomIn = new JButton("Zoom In");
      add(zoomIn);
		
      zoomOut = new JButton("Zoom Out");
      add(zoomOut);
		
      invert = new JButton("Invert");
      add(invert);
		
      toogle = new JButton("Toogle");
      add(toogle);
		
      save = new JButton("Save");
      add(save);
		
      submit = new JButton("Submit");
      add(submit);
      
      setActionListener();
    }

	
	/**
	 *  ActionListener for the button panel
	 */
	public void setActionListener(){
		
		zoomIn.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        	batchState.calZoom(false);
	        }
		  });
		
		zoomOut.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        	batchState.calZoom(true);
	        }
		  });
		
		invert.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        	batchState.setInvert();
	        	batchState.Invert();
	        }
		  });
		
		toogle.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        	batchState.setToogle();
	        	batchState.Toggle();
	        }
		  });
		
		submit.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        	batchState.Submit();
	        }
		  });
		
		save.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        	batchState.Save();
	        	batchState.SaveToXML();
	        }
		  });	
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
    public void Zoom(double zoomNo) {
	    // TODO Auto-generated method stub
	    
    }
	
	
	/**
	 *  Disable and enable and buttons state 
	 */
	@Override
    public void Download(DownloadBatchResult getBatchRes) {
		
		if(getBatchRes==null)
		{
			zoomIn.setEnabled(false);	
			zoomOut.setEnabled(false);
			invert.setEnabled(false);
			toogle.setEnabled(false);
			save.setEnabled(false);
			submit.setEnabled(false);
		}
		
		else if(getBatchRes != null)
		{
			zoomIn.setEnabled(true);	
			zoomOut.setEnabled(true);
			invert.setEnabled(true);
			toogle.setEnabled(true);
			save.setEnabled(true);
			submit.setEnabled(true);
		}	  
    }
	
	
	/**
	 *  Disable buttons state upon submit
	 */
	@Override
    public void Submit() {
	   
		zoomIn.setEnabled(false);	
		zoomOut.setEnabled(false);
		invert.setEnabled(false);
		toogle.setEnabled(false);
		save.setEnabled(false);
		submit.setEnabled(false);
	    
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

	
	/**
	 *  Disable buttons state upon logout
	 */
	@Override
    public void Logout() {
	  
		zoomIn.setEnabled(false);	
		zoomOut.setEnabled(false);
		invert.setEnabled(false);
		toogle.setEnabled(false);
		save.setEnabled(false);
		submit.setEnabled(false);
	    
    }

}//end of ButtonPanel
