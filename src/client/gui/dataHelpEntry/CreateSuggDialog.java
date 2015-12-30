package client.gui.dataHelpEntry;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import client.controller.BatchState;


/**
 * Use suggestion dialog for Table and Form Entry
 */
public class CreateSuggDialog extends JDialog{
	
	private BatchState batchState;
	private int row;
	private int col;
	private JPanel rowPanel;
	private JPanel buttonPanel;
	private JButton use;
	private JList listSugg;
	
	
	/**
	 * Constructor
	 */
	public CreateSuggDialog(BatchState batchState, int row, int col) {
   
		this.batchState = batchState;
		this.row = row;
		this.col = col;
		
		setModal(true);
		setSize(350,250);
		setResizable(false);
		setLocationRelativeTo(null);	
		setLayout(new BorderLayout());
		
		rowPanel = new JPanel();
		rowPanel.setLayout(new BorderLayout());
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		
		add(rowPanel,BorderLayout.CENTER);
		add(buttonPanel,BorderLayout.SOUTH);
		
		use = new JButton("Use Suggesstion");
		buttonPanel.add(use);
		
		listSuggCreate();
	}
	
	
	/**
	 * Create Jlist to display a list of suggestion
	 */
	public void listSuggCreate()
	{	
		//Creating Jlist
		listSugg = new JList();
		listSugg.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		DefaultListModel addField = new DefaultListModel();
		rowPanel.add(new JScrollPane(listSugg), BorderLayout.CENTER);
			
		//Check for suggestion value
		String checkValue = batchState.getCellValues(row, col);
		TreeSet<String> getNewSugg = batchState.qualitySugg(col, checkValue);
		
		if(getNewSugg.size()>0)
		{		
			for(String str: getNewSugg)
				addField.addElement(str);
		
			listSugg.setModel(addField);
			listSugg.setSelectedIndex(0);
			listSugg.setBorder(BorderFactory.createEtchedBorder());
			use.setEnabled(true);
		}
	
		else
			use.setEnabled(false);
	
		setActionListener();
	}
	
	/**
	 * ActionListener for JList
	 */
	public void setActionListener(){
		use.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		
				String getSel = (String) listSugg.getSelectedValue();
				batchState.cellChanged(getSel);
				batchState.selectedCell(null);
				dispose();
			}	
		});
	}

	
}
