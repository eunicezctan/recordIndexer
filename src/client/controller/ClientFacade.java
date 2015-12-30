package client.controller;

import shared.communication.*;
import client.ClientCommunicator;
import client.ClientException;

/**
 *  Client Facade to add as an interface between Client GUI and Client Communicator
 */

public class ClientFacade {
	
	private ClientCommunicator client;
	private DownloadBatchResult getBatchRes;
	private String getUsername;
	private String getPwd;
	private String path;
	
	
	/**
	 * Constructor
	 * @param batchState 
	 */
	public ClientFacade(String getHost, String getPort) {
	    
		path = "http://" + getHost+ ":" + getPort+ "/" ;
		client = new ClientCommunicator(getHost,getPort );
		
		//getUsername = "test1";
		//getPwd = "test1";
    }
		
			
	/**
	 *  Call ValidateUser in client communicator
	 */
	public String ValidateUser (){
		ValidateUser_Params getUser = new ValidateUser_Params();
		getUser.setUsername(getUsername);
		getUser.setPassword(getPwd);
		String userResult = null;
		try {
	        userResult = client.ValidateUser(getUser).toString();
        }
        catch (ClientException e) { e.printStackTrace();}
		return userResult;
	}
	
	/**
	 *  Call GetProject in client communicator
	 */
	public String GetProjects() {
	
		ValidateUser_Params getUser = new ValidateUser_Params();
		getUser.setUsername(getUsername);
		getUser.setPassword(getPwd);
		String getProject = null;
		
		try {
	        getProject = client.GetProjects(getUser).toString();
        }
        catch (ClientException e) {
	        e.printStackTrace();
        }
	
		return getProject;
	}
	
	
	/**
	 *  Call GetSampleImage in client communicator
	 */
	public String GetSampleImage (int project_id){
		
		GetSampleImage_Params getUser = new GetSampleImage_Params(); 
		String getImage = null;
	    
		try {
				getUser.setUsername(getUsername);
				getUser.setPassword(getPwd);
				getUser.setHostURL(path);
				getUser.setProject_id(project_id);
				getImage = client.GetSampleImage(getUser).toString();
		}
		catch (ClientException e1) { e1.printStackTrace();}
		
		return getImage;
    }
		
	
	/**
	 *  Call DownloadBatch in client communicator
	 */
	public DownloadBatchResult Download (int project_id){
		
		GetSampleImage_Params getUser = new GetSampleImage_Params();
		
		try {
			 getUser.setUsername(getUsername);
			 getUser.setPassword(getPwd);
			 getUser.setHostURL(path);
			 getUser.setProject_id(project_id);
			 getBatchRes = client.DownloadBatch(getUser);
			 
			 }
        
        catch (ClientException e) {
	        e.printStackTrace(); }
		
		return getBatchRes;
	}
	
	

	/**
	 *  Call SubmitBatch in client communicator
	 */
	public SubmitBatchResult SubmitBatch (String sortValue){
		
		
		SubmitBatch_Params getUser = new SubmitBatch_Params();		
				
		try {
			
				getUser.setUsername(getUsername);
				getUser.setPassword(getPwd);
				getUser.setBatch_id(getBatchRes.getBatch_id());
				getUser.setField_value(sortValue);
				client.SubmitBatch(getUser).toString();
			
        }
        catch (ClientException e) {
	        e.printStackTrace();
        }	
		
		return null;
		
	}

	
	/**
	 *  Getter & Setter for ClientFacade
	 */
	
	public String getGetUsername() {
		return getUsername;
	}

	public void setGetUsername(String getUsername) {
		this.getUsername = getUsername;
	}

	public String getGetPwd() {
		return getPwd;
	}

	public void setGetPwd(String getPwd) {
		this.getPwd = getPwd;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public DownloadBatchResult getGetBatchRes() {
		return getBatchRes;
	}

	public void setGetBatchRes(DownloadBatchResult getBatchRes) {
		this.getBatchRes = getBatchRes;
	}

}//end of ClientFacade
