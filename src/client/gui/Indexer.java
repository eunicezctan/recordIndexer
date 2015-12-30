package client.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import shared.communication.DownloadBatchResult;
import client.controller.BatchListener;
import client.controller.BatchState;
import client.controller.ClientFacade;
import client.gui.dataHelpEntry.FieldPanel;
import client.gui.dataHelpEntry.FormPanel;
import client.gui.dataHelpEntry.TablePanel;
import client.gui.image.DrawingListener;
import client.gui.image.ImageNavPanel;
import client.gui.image.ImagePanel;
import client.gui.topMenu.ButtonPanel;
import client.gui.topMenu.MenuPanel;
import client.qualityChecker.QualityChecker;


/**
 *  Main Indexer window setup	
 */
public class Indexer extends JFrame implements BatchListener{
	
	private ClientFacade clientFacade;
	private BatchState batchState;
	private LoginDialog login;
	
	private JPanel menu;
	private MenuPanel menuBar;
	private ButtonPanel buttonBar;
	
	private JPanel main;
	private JPanel subMain;
	private JSplitPane splitMain;
	private JSplitPane splitSub;
	private ImagePanel image;
	
	//private DataPanel data;
	private JPanel dataMain;
	private JTabbedPane data;
	private TablePanel tablePanel;
	private FormPanel formPanel;
	
	private JPanel otherMain;
	private JTabbedPane other;
	private FieldPanel fieldPanel;
	private ImageNavPanel imageNavPanel;
	

	
	/**
	 *  Constructor	
	 */
	public Indexer(ClientFacade clientFacade) {
		
		super("Indexer");
		this.clientFacade = clientFacade;
		
		setSize(1300,800);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout()); 
    }
	
	/**
	 *  Set pointer to LoginDialog Class	
	 */
	public void setPartner(LoginDialog login){
		this.login = login;	
	}
	
	
	/**
	 *  Set pointer to itself & child components
	 */
	public void setPointer(BatchState batchState){
		
		this.batchState = batchState;
		this.batchState.addListener(this);	
		
		menuBar.setPointer(clientFacade, login, batchState, this);
		buttonBar.setPointer(batchState);
		image.setPointer(batchState);
		tablePanel.setPointer(batchState);
		formPanel.setPointer(batchState);
		fieldPanel.setPointer(batchState); 
		imageNavPanel.setPointer(batchState);	
	}
	

	/**
	 *  Set up the main layout architecture for Indexer
	 */
	public void IndexerWindow() {	
		
		setFileButton();
		setImageData();
		setOtherData();	
	}
	
	
	/**
	 *  Set up the File and Button menu
	 */
	public void setFileButton() {
		
		menu = new JPanel(new GridBagLayout());
		menu.setBorder(new LineBorder(Color.BLACK,1));
		add(menu,BorderLayout.NORTH);	
				
		GridBagConstraints grid = new GridBagConstraints();
		grid.fill=GridBagConstraints.NONE;
		grid.anchor= GridBagConstraints.WEST;
		grid.insets = new Insets(1,1,1,1);
				
		menuBar = new MenuPanel();
		menuBar.setMenu();
		grid.gridx=0;
		grid.gridy=0;
		grid.weightx=10;
		menu.add(menuBar,grid);
				
		buttonBar = new ButtonPanel();
		buttonBar.Download(null);
		grid.gridx=0;
		grid.gridy=1;
		menu.add(buttonBar,grid);
				
	}
	
	
	/**
	 *  Set up the Image, Table & Form 
	 */
	public void setImageData() {
				
		//Image and Data entry main panel 
		main = new JPanel(new BorderLayout());
		add(main,BorderLayout.CENTER);
				
		//Image panel
		image = new ImagePanel();
		image.Download(null);
				
		subMain = new JPanel(new BorderLayout());
		subMain.setBorder(new LineBorder(Color.GRAY,1));
				
		//Add Image and splitSub panel into the main split pane
		splitMain = new JSplitPane(JSplitPane.VERTICAL_SPLIT, image, subMain);
		splitMain.setResizeWeight(0.7);
		main.add(splitMain);
				
		//Data entry panel
		dataMain = new JPanel();
		dataMain.setLayout(new BorderLayout());
		data = new JTabbedPane();
		tablePanel = new TablePanel();
		formPanel = new FormPanel();	
		data.addTab("Table Entry", tablePanel);
		data.addTab("Form Entry", formPanel);	
		dataMain.add(data,BorderLayout.CENTER);
		
		
		//Set textfield focus
		ChangeListener changeListener = new ChangeListener() {
		      public void stateChanged(ChangeEvent e) {
		        JTabbedPane dataTabbedPane = (JTabbedPane) e.getSource();
		        int index = dataTabbedPane.getSelectedIndex();
		        if(index==1)
		        	batchState.selectedCell(null);
		      }
		    };
		     
	    data.addChangeListener(changeListener);
	}
	
	
	/**
	 *  Set up the Field Help and ,Image Nav 
	 */
	public void setOtherData() {
	
		//Other(Help & Image Nav) panel
		otherMain = new JPanel();	
		otherMain.setLayout(new BorderLayout());
		other = new JTabbedPane();
		fieldPanel = new FieldPanel();
		imageNavPanel = new ImageNavPanel();
				
		other.addTab("Field Help", fieldPanel);
		other.addTab("Image Navigation", imageNavPanel);
		otherMain.add(other,BorderLayout.CENTER);
				
		//Add Data entry and Other panel into the Split Pane
		splitSub = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, dataMain, otherMain);
		splitSub.setResizeWeight(0.5);
		subMain.add(splitSub,BorderLayout.CENTER);
				
		//Add drawing listener to ImagePanel and ImageNavPanel			
		image.addDrawingListener(drawingListener1);
		imageNavPanel.addDrawingListener(drawingListener2);	
	}
	

	/**
	 *  Drawing listener for ImagePanel and ImageNavPanel
	 */
	private  DrawingListener drawingListener1 = new DrawingListener() {

		@Override
		public void translationChanged(int w_newOriginX, int w_newOriginY) {
			imageNavPanel.setTranslation(w_newOriginX, w_newOriginY);	
		}			
	};
	
	private  DrawingListener drawingListener2 = new DrawingListener() {

		@Override
		public void translationChanged(int w_newOriginX, int w_newOriginY) {
			image.setTranslation(w_newOriginX, w_newOriginY);	
		}			
	};
	

	/**
	 *  Restored user state 
	 */
	public void loadExistingUserState () {
		
		//restored the GUI position
		setSize(batchState.getWindowWidth(),batchState.getWindowHeight());
		setLocation(batchState.getWindowXPos(), batchState.getWindowYPos());
		
		splitMain.setSize(batchState.getMainSplitWidth(),  batchState.getMainSplitHeight());
		splitMain.setDividerLocation(batchState.getMainSplitDiv());
		
		splitSub.setSize(batchState.getSubSplitWidth(), batchState.getSubSplitHeight());
		splitSub.setDividerLocation(batchState.getSubSplitDiv());
		
		//restore data
		if(batchState.getGetBatchRes()!=null)
		{
			batchState.Download(batchState.getGetBatchRes());
			clientFacade.setGetBatchRes(batchState.getGetBatchRes());
			
			batchState.Toggle();
			batchState.Zoom(batchState.getZoomNo());
		
			if(batchState.getSetInvert())
				batchState.Invert();
			
			image.setTranslation(batchState.getW_translateX(), batchState.getW_translateY());	
			image.notifyTranslationChanged(-1*batchState.getW_translateX(), -1*batchState.getW_translateY());	
		}
	
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
    public void Download(DownloadBatchResult getBatchRes) {
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
	
	@Override
    public void Invert() {
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


	/**
	 *  Save the GUI state of the user
	 */
	@Override
    public void Save() {
	    
	    batchState.setWindowWidth(this.getWidth());
	    batchState.setWindowHeight(this.getHeight());
	    batchState.setWindowXPos(this.getX());
	    batchState.setWindowYPos(this.getY());
	    
	    batchState.setMainSplitWidth(splitMain.getWidth());
	    batchState.setMainSplitHeight(splitMain.getHeight());
	    batchState.setMainSplitDiv(splitMain.getDividerLocation());
	    
	    batchState.setSubSplitWidth(splitSub.getWidth());
	    batchState.setSubSplitHeight(splitSub.getHeight());
	    batchState.setSubSplitDiv(splitSub.getDividerLocation());
	 
    }
	
	
	/**
	 *  Reset the GUI to default when user logout
	 */
	@Override
    public void Logout() {
	    
		setSize(1280,775);
		setLocation(0, 0);
		
		splitMain.setSize(1280, 675);
		splitMain.setDividerLocation(415);
		
		splitSub.setSize(1280, 250);
		splitSub.setDividerLocation(615);
		
		repaint();
	    
    }
		
	
}//end of class Indexer
