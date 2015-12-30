package client.gui.dataHelpEntry;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.TreeSet;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import shared.communication.DownloadBatchResult;
import client.controller.BatchListener;
import client.controller.BatchState;


/**
 * TablePanel for Table entry
 */
public class TablePanel extends JPanel implements BatchListener{
	
	private BatchState batchState;
	private JTable recordTable;
	private recordTableModel recordModel;
	
	public TablePanel() {
	    
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
	 * Highlight and load the selected cell with values
	 */	
	@Override
    public void selectedCell(String[][] cellValue) {
		
		int recNo = batchState.getRecordNo();
		int fColNo = batchState.getFieldColNo()+1;
		
		if(recNo!=recordTable.getSelectedRow())
			recordTable.setRowSelectionInterval(recNo, recNo);
		
		if(recordTable.getSelectedColumn()>0)
			if(fColNo!= recordTable.getSelectedColumn())
				recordTable.setColumnSelectionInterval(fColNo, fColNo);	
		
    }

	@Override
    public void cellChanged(String newCellValue) {
	    // TODO Auto-generated method stub
	    
    }

	
	/**
	 * Set up the table structure with right rows and fields
	 */	
	@Override
    public void Download(DownloadBatchResult getBatchRes) {
	   
		recordModel = new recordTableModel(batchState,getBatchRes);
		recordTable = new JTable(recordModel);
		
		recordTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		recordTable.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		recordTable.getTableHeader().setReorderingAllowed(false);
		recordTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		recordTable.setCellSelectionEnabled(true);
		recordTable.setGridColor(Color.BLACK);
		recordTable.setRowHeight(20);
		
		recordTable.setRowSelectionInterval(0,0);
		recordTable.setColumnSelectionInterval(1,1);
		recordTable.addMouseListener(MyMouseListener);

		TableColumnModel columnModel = recordTable.getColumnModel();
		
		for (int i = 0; i < recordModel.getColumnCount(); ++i) 
		{
			TableColumn column = columnModel.getColumn(i);
			column.setPreferredWidth(70);
			
			if(i>0)
				column.setCellRenderer(new ColorCellRenderer());
		}
		
		add(new JScrollPane(recordTable), BorderLayout.CENTER);	 
    }
	

	/**
	 * Action listener for JTable
	 */	
	MouseListener MyMouseListener = new MouseListener() {

		@Override
        public void mouseClicked(MouseEvent e) {
		
			int recordNo = batchState.getRecordNo();
			int fieldColNo = batchState.getFieldColNo();
			int tableRecordNo = recordTable.getSelectedRow();
			int tableFieldNo =0;
			
			if(recordTable.getSelectedColumn() > 0)
				tableFieldNo = recordTable.getSelectedColumn()-1;
			
			if(recordNo != tableRecordNo)
				batchState.rowSelectedChanged(tableRecordNo);
			
			if( fieldColNo != tableFieldNo)
				batchState.colSelectedChanged(tableFieldNo);
			
			// Mouse Actionlistener for check Suggestion 
			if(e.getButton() == MouseEvent.BUTTON3 && recordTable.getSelectedColumn() > 0)
			{			
		 			int recNo = recordTable.getSelectedRow();
		 			int fieldNo = recordTable.getSelectedColumn()-1;
		 			String checkValue = batchState.getCellValues(recNo,fieldNo);
		 			
		 			if(checkValue != null && !batchState.qualityCheck(fieldNo, checkValue))
		 			{	
		 				JPopupMenu popLabel = new JPopupMenu();
						JMenuItem sugg = new JMenuItem("See Suggestion");
		 				popLabel.add(sugg );
		 				popLabel.show(e.getComponent(), e.getX(), e.getY());
		 		
		 				sugg.addActionListener(new ActionListener() {
		 					public void actionPerformed(ActionEvent e) {
		 						CreateSuggDialog sugg = new CreateSuggDialog(batchState,recordTable.getSelectedRow(),recordTable.getSelectedColumn()-1);
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
	        
	};
	
	
	@Override
    public void Zoom(double zoomNo, boolean setZoom) {
	    // TODO Auto-generated method stub
		
    }

	@Override
    public void Zoom(double zoomNo) {
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
	

	/**
	 * TableCellRender for highlighting cell with unknown value
	 */		
	class ColorCellRenderer extends DefaultTableCellRenderer {
	
		public Component getTableCellRendererComponent(JTable table, Object obj, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cell = super.getTableCellRendererComponent(table, obj, isSelected, hasFocus, row, column);
			
            String checkValue = (String) obj;
           
            if (table.isCellSelected(row, column))
                setForeground(Color.black);          
            
           if(checkValue != null && !batchState.qualityCheck(column-1, checkValue))
            	this.setBackground(Color.red);
           
            else
            	this.setBackground(Color.white);
            
            return cell;
	}
		
  }

}//end of TablePanel
