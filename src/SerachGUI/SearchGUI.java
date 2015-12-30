package SerachGUI;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.*;

import shared.communication.GetSampleImage_Params;
import shared.communication.GetSearch_Params;
import shared.communication.ValidateUser_Params;
import client.ClientCommunicator;
import client.ClientException;

public class SearchGUI extends JFrame{

	ClientCommunicator client;
	private Login login;
	
	private JPanel panelPro;
	GridBagConstraints grid;
	
	private JPanel panelBatch;
	GridBagConstraints gridBatch;
	
	private JLabel lPro;
	private JComboBox cPro;
	private JButton submitPro;
	
	private JLabel lField;
	private JList listField;
	private JLabel lSearch;
	private JTextField tSearch;
	private JButton submitSearch;
	
	private JLabel lBatch;
	private JComboBox cBatch;
	private JButton submitBatch;
	
	
	
	
	public void setPartner(Login login){
		this.login=login;	
	}//end of setPartner to Login Class
	
	public SearchGUI(){
		
		super("Search GUI");
		setSize(600,500);
		setResizable(false);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		panelPro = new JPanel(new GridBagLayout());
		add(panelPro, BorderLayout.NORTH);	
		grid = new GridBagConstraints();
		grid.fill=GridBagConstraints.HORIZONTAL;
		grid.insets = new Insets(2,2,2,2);
		
	}// end of constructor
	
	
	public void Project() throws ClientException {
		
		lPro = new JLabel("Project: ");
		grid.gridx=0;
		grid.gridy=0;
		grid.gridwidth=1;
		panelPro.add(lPro,grid);
		
		cPro  = new JComboBox();
		cPro.setPreferredSize(new Dimension(400, 50));
		grid.gridx=1;
		grid.gridy=0;
		grid.gridwidth=2;
		grid.ipady=2;
		panelPro.add(cPro,grid);
		
		//get list of project
		client = new ClientCommunicator(login.getGetHost(), login.getGetPort());
		ValidateUser_Params getUser = new ValidateUser_Params();
		getUser.setUsername(login.getGetUsername());
		getUser.setPassword(login.getGetPwd());
	
	    String getList[] = client.GetProjects(getUser).toString().split("\n");
	   
	    DefaultComboBoxModel addPro = new  DefaultComboBoxModel();
	    for(int i=1; i<getList.length; i+=2) 
		   addPro.addElement(getList[i]);
		   
	    cPro.setModel(addPro);
	    
	    submitPro = new JButton("Submit");
		grid.gridx=1;
		grid.gridy=1;
		grid.gridwidth=2;
		grid.ipady=5;
		panelPro.add(submitPro,grid);	
		
		//Field GUI		
		lField = new JLabel("Field: ");
		grid.gridx=0;
		grid.gridy=2;
		grid.gridwidth=2;
		panelPro.add(lField,grid);
		
		listField = new JList();
		grid.gridx=1;
		grid.gridy=2;
		grid.gridwidth=2;
		grid.ipady=5;
		panelPro.add(listField,grid);
		
		lSearch = new JLabel ("Search: ");
		grid.gridx=0;
		grid.gridy=3;
		grid.gridwidth=2;
		grid.ipady=5;
		panelPro.add(lSearch,grid);

		tSearch = new JTextField();
		grid.gridx=1;
		grid.gridy=3;
		grid.gridwidth=2;
		grid.ipady=5;
		panelPro.add(tSearch,grid);
		
		//Batch GUI
		lBatch = new JLabel("Image: ");
		grid.gridx=0;
		grid.gridy=5;
		grid.gridwidth=1;
		grid.ipady=2;
		panelPro.add(lBatch,grid);
				
		cBatch = new JComboBox();
		cBatch.setPreferredSize(new Dimension(400, 50));
		grid.gridx=1;
		grid.gridy=5;
		grid.gridwidth=1;
		grid.ipady=2;
		panelPro.add(cBatch,grid);
				
		submitPro.addActionListener(new ActionListener() {

			@Override
            public void actionPerformed(ActionEvent e) {
				int getSel = cPro.getSelectedIndex();
				Field(getSel+1);
			}
		});
	
	}//end of Project();
	
	public void Field(int getSel) {
		
		Map<String, String> storeField = new TreeMap<String, String>(); 
		String getField[];

		//get list of project field;
		GetSampleImage_Params getUser = new GetSampleImage_Params();
		getUser.setUsername(login.getGetUsername());
		getUser.setPassword(login.getGetPwd());
		getUser.setProject_id(getSel);
		
		try {
	        getField=client.GetFields(getUser).toString().split("\n");
	        
	        for(int i=1; i<getField.length-1;i+=3)
	        	storeField.put(getField[i], (getField[i+1]));
	       // System.out.println(storeField.entrySet());
	       
        }
        catch (ClientException e) {
	        e.printStackTrace();
        }
		
			
		DefaultListModel addField = new DefaultListModel();
		for(Map.Entry<String,String> entry : storeField.entrySet())
			addField.addElement(new catField(entry.getKey(),entry.getValue()));
		
		listField.setModel(addField);
		listField.setSelectedIndex(0);
		listField.setBorder(BorderFactory.createEtchedBorder());
		
		submitSearch = new JButton ("Search");
		grid.gridx=1;
		grid.gridy=4;
		grid.gridwidth=2;
		grid.ipady=5;
		panelPro.add(submitSearch,grid);
		
		submitSearch.addActionListener(new ActionListener() {

			@Override
            public void actionPerformed(ActionEvent e) {
				
				String getSearch = tSearch.getText().trim();
				List<catField> getSelect = listField.getSelectedValuesList();
				
				StringBuilder getFieldSearch = new StringBuilder();
				for(int i=0; i<getSelect.size();i++)
				{
					getFieldSearch.append(getSelect.get(i).getID());
					if(i < getSelect.size()-1)
						getFieldSearch.append(",");
				}
				//System.out.println(getFieldSearch.toString());
				Batch(getFieldSearch.toString(),getSearch);
			}
		});
		
	}//end of Field()
	
	public void Batch(String getFieldSearch, String getSearch ) {
		
		Map<String, String> storeBatch = new TreeMap<String, String>(); 
		String getBatch[];
		
		GetSearch_Params getUser = new GetSearch_Params();
		String path = "http://" + login.getGetHost()+ ":" + login.getGetPort()+ "/" ; 			
		
		getUser.setUsername(login.getGetUsername());
		getUser.setPassword(login.getGetPwd());
		getUser.setField_id(getFieldSearch);
		getUser.setSerach(getSearch);	
		getUser.setHostURL(path);
		
		try {
			getBatch = client.GetSearch(getUser).toString().split("\n");
			
			if(getBatch.length>1)
			  for(int i=0; i<getBatch.length;i+=4)
		          storeBatch.put(getBatch[i], (getBatch[i+1]));
			
			//System.out.println(storeBatch.entrySet());
        }
        catch (ClientException e) {
	        e.printStackTrace();
        }	

		DefaultComboBoxModel addBatch = new  DefaultComboBoxModel();
		for(Map.Entry<String,String> entry : storeBatch.entrySet())
			addBatch.addElement(new catField(entry.getKey(),entry.getValue()));
		
		cBatch.setModel(addBatch);
		  
		submitBatch = new JButton ("Display");
		grid.gridx=1;
		grid.gridy=6;
		grid.gridwidth=1;
		grid.ipady=2;
		panelPro.add(submitBatch,grid);
		
		submitBatch.addActionListener(new ActionListener() {

			@Override
            public void actionPerformed(ActionEvent e) {
				
				Image image=null;
				try {
					 if(cBatch.getItemCount()>0)	
					 {
						 //Desktop.getDesktop().browse(new URL(cBatch.getSelectedItem().toString()).toURI());
						URL url = new URL (cBatch.getSelectedItem().toString());
						image = ImageIO.read(url);
						Image sImage = image.getScaledInstance(1000, 800, Image.SCALE_DEFAULT);
						
						JFrame frame = new JFrame();
						frame.setSize(1000,800);
						JLabel label =  new JLabel(new ImageIcon(sImage));
						frame.add(label);
						frame.setVisible(true);
                     }
					}
                    catch (IOException e1) {
	                    // TODO Auto-generated catch block
	                    e1.printStackTrace();
                    }
				}
		});
		
	}//end of Batch();
	
	
	class catField {
		private String fId;
		private String fName;
		
		public catField (String fId, String fName){
			this.fId = fId;
			this.fName = fName;
		}
		
		public String toString() {
			return fName;
		}
		
		public String getID() {
			return fId;
		}
	}//end of class catField
	
	class catBatch {
		private String bId;
		private String url;
		
		public catBatch (String bId, String url){
			this.bId = bId;
			this.url = url;
		}
		
		public String toString() {
			return url;
		}
		
		public String getBID() {
			return bId;
		}
	}//end of class catBatch
	
}//end of Class SearchGUI



