package client.gui.dataHelpEntry;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import client.controller.BatchState;
import shared.communication.DownloadBatchResult;


/**
 * TableModel for TablePanel
 */
public class recordTableModel extends AbstractTableModel{

	private ArrayList<String> recordName;
	private BatchState batchState;
	private int getCcount;
	private int getRcount;
	private String cName [];
	
	
	/**
	 * Constructor
	 */
	public recordTableModel(BatchState batchState, DownloadBatchResult getBatchRes) {
		
		this.batchState = batchState;
		
		getCcount = getBatchRes.getFieldSize()+1; 
		getRcount = getBatchRes.getRecordsPerImage(); 
		recordName = getBatchRes.getGetCname();  
		recordName.add(0, "Record No");		   
		cName = recordName.toArray(new String[recordName.size()]);
		recordName.remove(0);	 
    }

	

	/**
	 * Print out column name in the table
	 */
	@Override
    public String getColumnName(int column) {
	   
	    return cName[column];
    }
	

	/**
	 * Return column count in the table
	 */
	@Override
    public int getColumnCount() {
	
	    return  getCcount;
    }


	/**
	 * Return row count in the table
	 */
	@Override
    public int getRowCount() {

	    return getRcount;
    }

	
	/**
	 * Set column 0 to be not editable
	 */
	@Override
    public boolean isCellEditable(int row, int column) {
	   
		if (column ==0)
			return false;
		else
			return true;

	}
	
	
	/**
	 * Print values to table entry
	 */
	@Override
    public void setValueAt(Object aValue, int row, int column) {
		String changedCell = aValue.toString().trim();
		
		if(changedCell.length() > 0)
			batchState.setCellValues(changedCell,row,(column-1));
		
		//fireTableCellUpdated(row, column);
    }


	/**
	 * Set values to Batch State
	 */
	@Override
    public Object getValueAt(int row, int column) {
		Object result = null;

		if(column ==0)
			result = row+1;
		else
			result = batchState.getCellValues(row,column-1);
			
		return result;
	   
    }
}//end of recordTableModel
