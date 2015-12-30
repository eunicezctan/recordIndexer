package client.gui.dataHelpEntry;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.html.HTMLEditorKit;

import shared.communication.DownloadBatchResult;
import client.controller.BatchListener;
import client.controller.BatchState;


/**
 * GUI Layout for File menu	
 */
public class FieldPanel extends JPanel implements BatchListener {
	
	private BatchState batchState;
	private ArrayList<String> getChelp;
	private JEditorPane cPane;
	
	

	/**
	 * Constructor
	 */
	public FieldPanel() {
				
		setBackground(Color.white);
		setLayout(new BorderLayout());
    }

	
	/**
	 * Set pointer
	 */
	public void setPointer(BatchState batchState){
		
		this.batchState = batchState;
		this.batchState.addListener(this);	
	}
	

	/**
	 *  BatchState Listener methods
	 */
	

	/**
	 * Load the selected help field 
	 */	
	@Override
    public void selectedCell(String[][] cellValue) {
		
		try {
	        cPane.setPage(new URL(getChelp.get(batchState.getFieldColNo())));
        }
        catch (IOException e) {
	        e.printStackTrace();
        }   
    }

	
	@Override
    public void cellChanged(String newCellValue) {
		 // TODO Auto-generated method stub
		
    }

	
	/**
	 * Set up field help panel structure and load the first help field 
	 */	
	@Override
    public void Download(DownloadBatchResult getBatchRes) {
		
		getChelp = getBatchRes.getGetChelp();
		
		cPane = new JEditorPane();
		cPane.setEditable(false);
		
		HTMLEditorKit pHtml = new HTMLEditorKit();
		cPane.setEditorKit(pHtml);
		
		try {
	        cPane.setPage(new URL(getChelp.get(0)));
        }
        catch (IOException e) {
	        e.printStackTrace();
        }
		JScrollPane cScrollPane = new JScrollPane(cPane);	
		add(cScrollPane,BorderLayout.CENTER);
		    
    }

	@Override
    public void Zoom(double zoomNo, boolean setZoom) {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    public void Invert() {
	    // TODO Auto-generated method stub
	    
    }

	
	/**
	 * Remove all components when batch is submitted
	 */	
	@Override
    public void Submit() {
	 removeAll();
	 repaint();
	    
    }

	@Override
    public void Toggle() {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    public void Save() {
	    // TODO Auto-generated method stub
	    
    }

	/**
	 * Remove all components when user logout
	 */	
	@Override
    public void Logout() {
		removeAll();
		repaint();
    }

	@Override
    public void Zoom(double zoomNo) {
	    // TODO Auto-generated method stub
	    
    }
	
}//end of FieldPanel
