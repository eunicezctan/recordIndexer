package client.controller;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

import client.qualityChecker.QualityChecker;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import shared.communication.DownloadBatchResult;

/**
 *  Main controller between different panels for Indexer GUI
 */
public class BatchState implements BatchListener{
	
	private transient ClientFacade clientFacade;
	private transient QualityChecker quality;
	private transient ArrayList<BatchListener> listener; 
	private ArrayList <Integer> getXcoord = new ArrayList <Integer>();
	private ArrayList <Integer>  getWidth = new ArrayList <Integer>();
	private DownloadBatchResult getBatchRes;
	private String cellValues [][];
	private boolean setInvert;
	private double zoomNo;
	private double navZoom;
	private boolean toggle;
	private int firstYcoord;
	private int recordHeight; 
	private int recordsPerImage; 
	private int iWidth;
	private int iHeight;
	private int calXcoord; 
	private int nYcoord;
	private int calWidth; 
	private int fieldColNo;
	private int recordNo;
	private int fieldSize;
	
	//User's GUI persistent state
	private String nameUser;
	private int windowXPos;
	private int windowYPos;
	private int windowWidth;
	private int windowHeight;
	
	private int mainSplitWidth;
	private int mainSplitHeight;
	private int mainSplitDiv;
	
	private int subSplitWidth;
	private int subSplitHeight;
	private int subSplitDiv;
	
	private int w_translateX;
	private int w_translateY;

	
	/**
	 * Constructor
	 */
	
	public BatchState() { 
		
		listener = new ArrayList <BatchListener>();	
		quality = new QualityChecker();
		getBatchRes = null;
		setInvert = false;
		toggle = true;
		zoomNo = 1;
		navZoom = 1;
		firstYcoord = 0;
		recordHeight = 0; 
		recordsPerImage =0; 
		iWidth = 0; 
		iHeight = 0; 
		calXcoord = 0; 
		nYcoord = 0;
		calWidth = 0; 
		fieldColNo = 0;
		recordNo = 0;
		fieldSize = 0;
	}
	
	
	/**
	 *  Initial an array list of BatchListener & QualityChecker for existing user
	 */
	public void BatchUserState(){
	
		listener = new ArrayList<BatchListener>();
		quality = new QualityChecker();
	}
	
	
	/**
	 *  Set pointer between BatchState and ClientFacade
	 */
	public void setClient(ClientFacade clientFacade){
		this.clientFacade = clientFacade;
	}
	
	
	/**
	 *  Add listener to array list of BatchListener
	 */
	public void addListener (BatchListener lis) {
		listener.add(lis);
	}
	
	
	/**
	 *  Call by clickCellChanged 
	 *  Update current selected cell for all BatchListener
	 */
	@Override
    public void selectedCell(String[][] cellValue) {
	   
		for (BatchListener l : listener)
			l.selectedCell(cellValues);
    }
	
	
	/**
	 *  Call by Form Panel & Table Panel
	 *  Update row index for all BatchListener
	 */
	
	public void rowSelectedChanged(int listIndex) {
	   
		int bYcoord =  iHeight + firstYcoord;
		nYcoord =bYcoord + (listIndex * recordHeight);
		recordNo=listIndex;
		selectedCell(null);	    
    }
	
	/**
	 *  Call by Form Panel & Table Panel
	 *  Update col index for all BatchListener
	 */
	public void colSelectedChanged(int col) {
		
		if(fieldColNo!=col)
		{
			int bXcoord =  iWidth + getXcoord.get(0)/2;
			calXcoord = iWidth;
		
			for(int i=0; i<=col; i++)
			{
				calXcoord = iWidth + getXcoord.get(i)/2;
				calWidth =  getWidth.get(i)/2;
			}
		
			fieldColNo=col;
			selectedCell(null);	  
		}
    }
	

	
	/**
	 *  Call by Indexer for initial load user state
	 *  Update row & col index for all BatchListener
	 */
	public void colRowSelectedChanged (int listIndex,int col) {
		
		int bYcoord =  iHeight + firstYcoord;
		nYcoord =bYcoord + (listIndex * recordHeight);
		recordNo=listIndex;
		selectedCell(null);	 
		if(fieldColNo!=col)
		{
			int bXcoord =  iWidth + getXcoord.get(0)/2;
			calXcoord = iWidth;
		
			for(int i=0; i<=col; i++)
			{
				calXcoord = iWidth + getXcoord.get(i)/2;
				calWidth =  getWidth.get(i)/2;
			}
		
			fieldColNo=col;
			selectedCell(null);	  
		}
    }
	
	
	/**
	 *  Call by ImagePanel to calculate cell coordinates and pass  
	 *  the result to selectedCell
	 */
	public void imageSelectedChanged(int w_X, int w_Y) {
	   	
		//calculate the X Coordinate
		int bXcoord =  iWidth + getXcoord.get(0)/2;
		int tXcoord = iWidth + (getXcoord.get(getXcoord.size()-1)/2) + (getWidth.get(getWidth.size()-1)/2);
		calXcoord = iWidth;
		calWidth =0;
		fieldColNo = 0;
		
		if(w_X>= bXcoord && w_X < tXcoord)
		{
			for(int i=0; i<getXcoord.size(); i++)
			{
				calXcoord = iWidth + getXcoord.get(i)/2;
				calWidth =  getWidth.get(i)/2;
				
				if(w_X >= calXcoord && w_X < calXcoord+calWidth)
					break;
				
				fieldColNo++;
			}
			
			//calculate the Y Coordinate
			int bYcoord =  iHeight + firstYcoord;
			int tYcoord =bYcoord + (recordsPerImage * recordHeight);
			nYcoord = bYcoord;
			
			recordNo=0;
			while(w_Y> nYcoord)
			{
				nYcoord += recordHeight;
				recordNo++;
			}
			nYcoord = nYcoord -recordHeight;
			recordNo--;
		
			if(nYcoord >= bYcoord && nYcoord <  tYcoord)		
				selectedCell(null);
		}
    }
	
	
	/**
	 *  Call by table and form entry to update value changed
	 */
	@Override
    public void cellChanged(String newCellValue) {
	    
		cellValues [recordNo] [fieldColNo]=newCellValue;		
    }
	

	/**
	 *  Call by table and cellChanged to check for known value
	 */
	public boolean qualityCheck(int index, String checkValue){
		
		return quality.findValue(index ,checkValue);
	}
	
	
	public TreeSet<String> qualitySugg( int index, String inputWord){
	
		return quality.sugSimWord(index,inputWord);
	}

	
	/**
	 *  Call by calZoom to update Zoom In or Zoom out status
	 */
	@Override
    public void Zoom(double zoomNo, boolean setZoom) {
	   
		for (BatchListener l : listener)
			l.Zoom(zoomNo, setZoom);
   
	}
	
	
	/**
	 *  Call by Button & Image panel to calculate zooming value
	 */
	public void calZoom(boolean getZoom) {
		
		if(getZoom)
			zoomNo= zoomNo *0.5;
			
		else
			zoomNo= zoomNo/0.5;
	  
		Zoom(zoomNo, getZoom);
    }
	
	
	/**
	 *  Call by Indexer frame to set zooming value for persistent user state
	 */
	@Override
    public void Zoom(double zoomNo) {
	   
		for (BatchListener l : listener)
			l.Zoom(zoomNo);   
    }
	

	/**
	 *  Called by DownLoadBatch and pass getBatchRes to all BatchListener
	 */
	@Override
    public void Download( DownloadBatchResult getBatchRes) {
		
		this.getBatchRes = getBatchRes;
		
		//give an array list of known URL to Quality Checker
		quality.downloadKnowFile(this.getBatchRes.getGetKnown());
		
		for (BatchListener l : listener)
			l.Download(this.getBatchRes);
    }
	

	/**
	 *  Called by DownLoadBatch to set record structure
	 */
	public void setRecordCell() {
		
		cellValues = new String[getBatchRes.getRecordsPerImage()] [getBatchRes.getFieldSize()]; 	
		fieldSize = getBatchRes.getFieldSize();
		
    }
	
	
	/**
	 *  Call by ImagePanel to set image coord details
	 */
	public void setImageDetail(int iWidth, int iHeight) {
		
		this.iWidth = iWidth;
		this.iHeight = iHeight;
		firstYcoord = getBatchRes.getFirstYcoord()/2;
		recordHeight = getBatchRes.getRecordHeight()/2;
		recordsPerImage = getBatchRes.getRecordsPerImage(); 
		getXcoord = getBatchRes.getGetXcoord();
		getWidth = getBatchRes.getGetWidth();	  
    }
	


	/**
	 *  Call by Button Panel for submitting a batch
	 */
	@Override
    public void Submit() {
	    
		int recordsPerImage = getBatchRes.getRecordsPerImage();
		int fieldSize = getBatchRes.getFieldSize();
		
		StringBuilder sortValue = new StringBuilder();
	
		for(int x=0; x < recordsPerImage; x++)
		{
			for(int y=0; y < fieldSize ; y++)
			{
				sortValue.append(cellValues[x][y]);
				if((fieldSize-y) > 1)
					sortValue.append(",");		
			}
			
			if ((recordsPerImage-x) > 1)
			sortValue.append(";");
		}
			
		clientFacade.SubmitBatch(sortValue.toString());
		
		resetUserData();
		
		for (BatchListener l : listener)
			l.Submit();   
    }
	
	
	/**
	 *  Call by Submit to reset user data
	 */
	public void resetUserData(){
		
		getBatchRes = null;
		setInvert = false;
		toggle = true;
		zoomNo = 1;
		firstYcoord = 0;
		recordHeight = 0; 
		recordsPerImage =0; 
		iWidth = 0; 
		iHeight = 0; 
		calXcoord = 0; 
		nYcoord = 0;
		calWidth = 0; 
		fieldColNo = 0;
		recordNo = 0;
		fieldSize = 0;
	}
	

	/**
	 *  Called by ButtonPanel to set the Toggle status of cell
	 */
	@Override
    public void Toggle() {
	   
		for (BatchListener l : listener)
			l.Toggle();
    }
	
	/**
	 *  Called by ButtonPanel to compute the Toggle status of cell
	 */
	
	public void setToogle(){
	   	
		if(toggle == true)
			toggle=false;
		else if(toggle == false)
			toggle = true;	
	}
	
	

	/**
	 *  Called by Button panel to save user state info
	 */
	@Override
    public void Save() {
		
		for (BatchListener l : listener)
			l.Save();
    }
	
	
	/**
	 *  Called by Button panel to save user state to XML
	 */
    public void SaveToXML() {
	  
		XStream xStream = new XStream(new DomDriver());
		
		OutputStream outFile;
        try {
	        outFile = new BufferedOutputStream(new FileOutputStream( nameUser +".xml"));
	        xStream.toXML(this, outFile);
			outFile.close();	
        }
        catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }    
    }
	

	/**
	 *  Called by ButtonPanel to compute the invert status for the image
	 */
	@Override
    public void Invert() {
	  
		for (BatchListener l : listener)
			l.Invert();
	}
	

	/**
	 *  Called by ButtonPanel to compute the highlight status of cell
	 */
	
	public void setInvert(){
	   	
		if(setInvert == true)
			setInvert=false;
		else if(setInvert == false)
			setInvert = true;	
		
	}
	
	
	/**
	 *  Called by ButtonPanel when logout is clicked
	 */
	@Override
    public void Logout() {
	    
		for (BatchListener l : listener)
			l.Logout();
	    
    }
	
	
	/**
	 *  Called by Form and Table panel to get its values
	 */
	public String getCellValues( int row, int column) {
		return cellValues[row][column];
	}

	
	/**
	 *  Called by Form and Table panel to update its values
	 *  Call selectedCell to update new selected cell
	 */

	public void setCellValues(String changedCell, int row, int column) {
		
		cellValues[row][column] = changedCell;
		selectedCell(cellValues);
	}
	
	
	/**
	 *   Getter and Setter for the class variables
	 */
	public boolean getToggle() {
		return toggle;
	}

	public void setToggle(boolean toggle) {
		this.toggle = toggle;
	}
	
	
	public int getCalXcoord() {
		return calXcoord;
	}

	public void setCalXcoord(int calXcoord) {
		this.calXcoord = calXcoord;
	}

	public int getnYcoord() {
		return nYcoord;
	}

	public void setnYcoord(int nYcoord) {
		this.nYcoord = nYcoord;
	}

	public int getCalWidth() {
		return calWidth;
	}

	public void setCalWidth(int calWidth) {
		this.calWidth = calWidth;
	}

	public int getFieldColNo() {
		return fieldColNo;
	}

	public void setFieldColNo(int fieldColNo) {
		this.fieldColNo = fieldColNo;
	}

	public int getRecordNo() {
		return recordNo;
	}

	public void setRecordNo(int recordNo) {
		this.recordNo = recordNo;
	}
	
	public double getZoomNo() {
		return zoomNo;
	}

	public void setZoomNo(double zoomValue) {
		this.zoomNo = zoomValue;
	}
	
	public int getFieldSize() {
		return fieldSize;
	}


	public void setFieldSize(int fieldSize) {
		this.fieldSize = fieldSize;
	}


	public String getNameUser() {
		return nameUser;
	}


	public void setNameUser(String nameUser) {
		this.nameUser = nameUser;
	}


	public int getWindowXPos() {
		return windowXPos;
	}


	public void setWindowXPos(int windowXPos) {
		this.windowXPos = windowXPos;
	}


	public int getWindowYPos() {
		return windowYPos;
	}


	public void setWindowYPos(int windowYPos) {
		this.windowYPos = windowYPos;
	}


	public int getWindowWidth() {
		return windowWidth;
	}


	public void setWindowWidth(int windowWidth) {
		this.windowWidth = windowWidth;
	}


	public int getWindowHeight() {
		return windowHeight;
	}


	public void setWindowHeight(int windowHeight) {
		this.windowHeight = windowHeight;
	}



	public int getMainSplitWidth() {
		return mainSplitWidth;
	}


	public void setMainSplitWidth(int mainSplitWidth) {
		this.mainSplitWidth = mainSplitWidth;
	}


	public int getMainSplitHeight() {
		return mainSplitHeight;
	}


	public void setMainSplitHeight(int mainSplitHeight) {
		this.mainSplitHeight = mainSplitHeight;
	}



	public int getSubSplitWidth() {
		return subSplitWidth;
	}


	public void setSubSplitWidth(int subSplitWidth) {
		this.subSplitWidth = subSplitWidth;
	}


	public int getSubSplitHeight() {
		return subSplitHeight;
	}


	public void setSubSplitHeight(int subSplitHeight) {
		this.subSplitHeight = subSplitHeight;
	}


	public int getMainSplitDiv() {
		return mainSplitDiv;
	}


	public void setMainSplitDiv(int mainSplitDiv) {
		this.mainSplitDiv = mainSplitDiv;
	}


	public int getSubSplitDiv() {
		return subSplitDiv;
	}


	public void setSubSplitDiv(int subSplitDiv) {
		this.subSplitDiv = subSplitDiv;
	}


	public DownloadBatchResult getGetBatchRes() {
		return getBatchRes;
	}


	public void setGetBatchRes(DownloadBatchResult getBatchRes) {
		this.getBatchRes = getBatchRes;
	}

	public boolean getSetInvert() {
		return setInvert;
	}


	public void setSetInvert(boolean setInvert) {
		this.setInvert = setInvert;
	}


	public double getNavZoom() {
		return navZoom;
	}


	public void setNavZoom(double navZoom) {
		this.navZoom = navZoom;
	}


	public int getW_translateX() {
		return w_translateX;
	}


	public void setW_translateX(int w_translateX) {
		this.w_translateX = w_translateX;
	}


	public int getW_translateY() {
		return w_translateY;
	}


	public void setW_translateY(int w_translateY) {
		this.w_translateY = w_translateY;
	}
	
	
}//end of BatchState

