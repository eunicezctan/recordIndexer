package client.controller;

import shared.communication.DownloadBatchResult;


/**
 *  BatchListener interface for panels synchronous 
 */
public interface BatchListener {
	
	public void selectedCell (String[][] cellValue);
	public void cellChanged (String newCellValue);
	public void Download(DownloadBatchResult getBatchRes);
	public void Zoom(double zoomNo, boolean setZoom);
	public void Zoom(double zoomNo);
	public void Invert();
	public void Submit();
	public void Toggle();
	public void Save();
	public void Logout();
		
}//end of BatchListener
