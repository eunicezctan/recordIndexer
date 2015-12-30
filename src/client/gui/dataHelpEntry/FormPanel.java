package client.gui.dataHelpEntry;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import shared.communication.DownloadBatchResult;
import client.controller.BatchListener;
import client.controller.BatchState;


/**
 * FormPanel for form entry
 */
public class FormPanel extends JPanel implements BatchListener {
	
	private BatchState batchState;
	private JList listField;
	private JPanel rowPanel;
	
	private JPanel fieldPanel;
	private JPanel fieldSubPanel;
	private JLabel fieldLable;
	private JTextField fieldText[];
	
	
	/**
	 * Constructor
	 */
	public FormPanel() {
		
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
	 * Highlight cell with unknown value and load the selected cell with values
	 */	
	@Override
    public void selectedCell(String[][] cellValue) {
		
		int recNo = batchState.getRecordNo();
		int fColNo = batchState.getFieldColNo();
			
		listField.setSelectedIndex(recNo);
		fieldText[fColNo].requestFocusInWindow();
		
		for(int i=0; i< batchState.getFieldSize(); i++)
		{
			String getFieldValue = cellValue[recNo][i];
			fieldText[i].setText(getFieldValue);
			
			if(getFieldValue != null && !batchState.qualityCheck(i, getFieldValue))
				fieldText[i].setBackground(Color.red);
			else
				fieldText[i].setBackground(Color.white);
		}
    }
	

	@Override
    public void cellChanged(String newCellValue) {
	    // TODO Auto-generated method stub
	    
    }


	/**
	 * Set up the form panel structure with right rows and fields
	 */	
	@Override
    public void Download(DownloadBatchResult getBatchRes) {
		
		//Add Row and Field panel layout
		rowPanel = new JPanel();
		rowPanel.setLayout(new BorderLayout());
		rowPanel.setPreferredSize(new Dimension(100, 50));
		rowPanel.setBackground(Color.white);
		
		fieldPanel = new JPanel();
		fieldSubPanel = new JPanel();
		fieldPanel.setLayout(new BorderLayout());
		fieldPanel.add(new JScrollPane(fieldSubPanel), BorderLayout.CENTER);
		
		add(rowPanel, BorderLayout.WEST);
		add(fieldPanel, BorderLayout.CENTER);
		
		listTextCreate(getBatchRes);
	}
	
	
	/**
	 * Create JList and JTextField for field values
	 */	
	public void listTextCreate(DownloadBatchResult getBatchRes)
	{
		//Creating Jlist
		listField = new JList();
		
		listField.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		DefaultListModel addField = new DefaultListModel();
		for(int i=0; i<getBatchRes.getRecordsPerImage();i++)
			addField.addElement(i+1);
		
		listField.setModel(addField);
		listField.setSelectedIndex(0);
		listField.setBorder(BorderFactory.createEtchedBorder());
		rowPanel.add(new JScrollPane(listField), BorderLayout.CENTER);
		
		listField.addListSelectionListener(listListener);	
		
		//Creating JText and JLabel	
		ArrayList<String> fieldName = getBatchRes.getGetCname();
		int fieldSize = fieldName.size();
		fieldSubPanel.setLayout(new GridLayout(fieldSize,2));	
		fieldText = new JTextField[fieldSize] ;
		
		for(int i=0; i < fieldSize ; i++)
		{
			fieldLable = new JLabel(fieldName.get(i));
			fieldText[i]= new JTextField(20);
			fieldText[i].addFocusListener(new MyFocusListener(i,fieldText[i]));
			fieldText[i].addMouseListener(new MyMouseListener(i,fieldText[i]));
			fieldSubPanel.add(fieldLable);
			fieldSubPanel.add(fieldText[i]);
		}	
    }
	

	/**
	 * Action listener for JList 
	 */	
	 ListSelectionListener listListener = new ListSelectionListener() {
	      public void valueChanged(ListSelectionEvent event) {
	    
	    		 if(listField.getSelectedIndex() != batchState.getRecordNo()) // check if change come from List 
	    		 {
	    			batchState.rowSelectedChanged(listField.getSelectedIndex()); 	
	    			
	    	  	 }		   
	      }
	  };
	  
	  
	  /**
		* Action listener for JTextFields
		*/
	  class MyFocusListener implements FocusListener{
		  
		  private int col;
		  JTextField fieldText;

		public MyFocusListener(int col, JTextField fieldText) {
	       
			this.col = col;
	        this.fieldText = fieldText;
			
        }

		@Override
        public void focusGained(FocusEvent e) {
	
			batchState.colSelectedChanged(col); 	       
        }

		@Override
        public void focusLost(FocusEvent e) {
	       
			String getValue = fieldText.getText().trim();
			
			if(getValue.length() > 0)
				batchState.cellChanged(getValue);
        }
		  
	  }
	  
	  /**
		* Mouse Actionlistener for check Suggestion 
		*/	
	  
	  class MyMouseListener implements MouseListener{
		  
		  private int col;
		  JTextField fieldText;

		public MyMouseListener(int col, JTextField fieldText) {
	       
			this.col = col;
	        this.fieldText = fieldText;
			
        }

		@Override
        public void mouseClicked(MouseEvent e) {
			
			if(e.getButton() == MouseEvent.BUTTON3)
			{
				int recNo = batchState.getRecordNo();
				int fieldNo = batchState.getFieldColNo();
				
				String checkValue = fieldText.getText();
				
				if(checkValue != null && !batchState.qualityCheck(fieldNo, checkValue))
	 			{	
					JPopupMenu popLabel = new JPopupMenu();
					JMenuItem sugg = new JMenuItem("See Suggestion");
					popLabel.add(sugg );
					popLabel.show(this.fieldText, e.getX(), e.getY());
					
					sugg.addActionListener(new ActionListener() {
		             public void actionPerformed(ActionEvent e) {
		            	 CreateSuggDialog sugg = new CreateSuggDialog(batchState,batchState.getRecordNo(),batchState.getFieldColNo());
	 						sugg.setVisible(true);
		              }				
					});
	 			}
			
			}
	        
        }
		

		@Override
        public void mousePressed(MouseEvent e) {
	        // TODO Auto-generated method stub
	        
        }

		@Override
        public void mouseReleased(MouseEvent e) {
	        // TODO Auto-generated method stub
	        
        }

		@Override
        public void mouseEntered(MouseEvent e) {
	        // TODO Auto-generated method stub
	        
        }

		@Override
        public void mouseExited(MouseEvent e) {
	        // TODO Auto-generated method stub
	        
        }
		  
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
	
}//end of FormPanel