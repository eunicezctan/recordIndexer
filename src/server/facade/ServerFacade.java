package server.facade;

import java.util.List;
import server.ServerException;
import server.database.Database;
import server.database.DatabaseException;
import shared.communication.*;
import shared.model.*;

public class ServerFacade {

	/**
	 * Initialize the Database
	 *
	 */	
	
	public static void initialize() throws ServerException {		
		try {
			Database.initialize();		
		}
		catch (DatabaseException e) {
			throw new ServerException(e.getMessage(), e);
		}		
	}
	
	/**
	 * Called by ValidateUserHandler to validate User in the User database
	 *
	 */	
	
	public static ValidateUserResult ValidateUser (ValidateUser_Params params) throws ServerException {	
		
		try {
			
			Database.getDB().startTransaction();
			ValidateUserResult retUser = new ValidateUserResult();
			
			retUser = Database.getDB().getUserDAO().ValidateUser(params);
			Database.getDB().endTransaction(true);
			return retUser;
		}
		catch (DatabaseException e) {
			
			Database.getDB().endTransaction(false);
			throw new ServerException(e.getMessage(), e);
		}
	}//end of getUser()
	
	
	/**
	 * Called by GetProjectHandler to get all projects in the project database
	 *
	 */	
	
	public static GetProjectsResult getProjects(ValidateUser_Params params) throws ServerException {	
		
		try {
			
			Database.getDB().startTransaction();
			ValidateUserResult retUser = new ValidateUserResult();
			
			GetProjectsResult getProject = new GetProjectsResult();
			List<ProjectModel> retProject = null;
			
			 retUser = Database.getDB().getUserDAO().ValidateUser(params);
			 
			if (retUser.getSetIndex()==0)
			{
				Database.getDB().endTransaction(false);
				return getProject;
			}
			
			else
			{
				retProject = Database.getDB().getProjectDAO().getAllProject();
				getProject.setProject(retProject);
				getProject.setSetIndex(1);
				Database.getDB().endTransaction(true);
				return getProject;			
			}			
		}
		catch (DatabaseException e) {
			Database.getDB().endTransaction(false);
			throw new ServerException(e.getMessage(), e);
		}	
		
	}//end getProjects()
	


	/**
	 * Called by GetSampleImageHandler to get a sample image in the Bath database
	 *
	 */	
	
	public static GetSampleImageResult getImageURL (GetSampleImage_Params params) throws ServerException {	
		
		try {
			
			Database.getDB().startTransaction();
			
			ValidateUser_Params userParams = new ValidateUser_Params (); 
			
			ValidateUserResult retUser = new ValidateUserResult();
			GetSampleImageResult getImage = new GetSampleImageResult();
			
			userParams.setUsername(params.getUsername());
			userParams.setPassword(params.getPassword());
			
			retUser = Database.getDB().getUserDAO().ValidateUser(userParams);
			 
			if (retUser.getSetIndex()==0)
			{
				Database.getDB().endTransaction(false);
				return getImage;
			}
			
			else
			{
				String img= Database.getDB().getBatchDAO().getImageURL(params.getProject_id());
			
				if(img != null)
				{
					getImage.setImageURL(params.getHostURL()+img);
					getImage.setSetIndex(1);
				}
				
				Database.getDB().endTransaction(true);
				return getImage;			
			}		
		}
		catch (DatabaseException e) {
			Database.getDB().endTransaction(false);
			throw new ServerException(e.getMessage(), e);
		}	
		
	}//end getImageURL()
	


	/**
	 * Called by DownloadBatchHandler to download batch that have been assign to a user
	 *
	 */	
	
   public static DownloadBatchResult DownloadBatch(GetSampleImage_Params params) throws ServerException {
		
	try {
			
			Database.getDB().startTransaction();
			
			ValidateUser_Params userParams = new ValidateUser_Params (); 
			
			DownloadBatchResult getResult = new DownloadBatchResult();
			ProjectModel retProject = null;
			List<FieldModel>  retField = null;
			BatchModel retBatch = null;
	
			userParams.setUsername(params.getUsername());
			userParams.setPassword(params.getPassword());
			
			int retUser;
			retUser = Database.getDB().getUserDAO().ValidateUserBatch(userParams);
			
			// if no such user or user has assign batch
			if (retUser==-1 || retUser !=0)
			{
				Database.getDB().endTransaction(false);
				return getResult;
			}
			
			else
			{
				int project_id = params.getProject_id();
				
				retProject = Database.getDB().getProjectDAO().getProject(project_id);
				
				// if invalid id
				if(retProject.getTitle() == null)
				{
					Database.getDB().endTransaction(false);
					return getResult;
				}
				
				retBatch = Database.getDB().getBatchDAO().getBatch(project_id);
		
				// if no available batch
				if(retBatch.getFile() == null)
				{
					Database.getDB().endTransaction(false);
					return getResult;
				}
				
				else
				{
					retField = Database.getDB().getFieldDAO().getField(project_id);
				
					//Update User and Batch Table
					Database.getDB().getBatchDAO().updateBatch(-1,retBatch.getBatch_id());
					Database.getDB().getUserDAO().updateUserBatch(retBatch.getBatch_id(),params.getUsername(),params.getPassword() );
				
					//add All result into DownloadBatchResult
					getResult.setProject(retProject);
					getResult.setField(retField);
					getResult.setBatch(retBatch);
					getResult.setHostURL(params.getHostURL());
					getResult.setParam();
					getResult.setSetIndex(1);
				
					Database.getDB().endTransaction(true);
					return getResult;	
				}
			}	
		}
		catch (DatabaseException e) {
			Database.getDB().endTransaction(false);
			throw new ServerException(e.getMessage(), e);
		}
	
	}//end of DownloadBatch()

   
	/**
	 * Called by SubmitBatchHandler to submit a batch that have been indexed by a user
	 *
	 */		
	public static SubmitBatchResult SubmitBatch(SubmitBatch_Params params) throws ServerException
	{
		try
		{
			Database.getDB().startTransaction();
			
			ValidateUser_Params userParams = new ValidateUser_Params(); 
			SubmitBatchResult getSubmit = new SubmitBatchResult();
			
			userParams.setUsername(params.getUsername());
			userParams.setPassword(params.getPassword());
			
			int retBatch;
			retBatch = Database.getDB().getUserDAO().ValidateUserBatch(userParams);
			
			// if no such user or user has assign batch
			if (retBatch==-1 || retBatch !=params.getBatch_id())
			{
				Database.getDB().endTransaction(false);
				return getSubmit;
			}
						
			else
			{
				int project_id= Database.getDB().getBatchDAO().getProjectId(retBatch);
				getSubmit=Database.getDB().getRecordDAO().submitBatch(params.getField_value(), retBatch, project_id);
				
				//Successful add records
				if(getSubmit.getSetIndex()==1)
				{
					Database.getDB().getUserDAO().updateUserIndex(getSubmit.getTotalIndex(),0,params.getUsername(),params.getPassword());
					Database.getDB().endTransaction(true);
					return  getSubmit;	
				}
				
				Database.getDB().endTransaction(false);
				return  getSubmit;	
			}	
		}
		catch (DatabaseException e) {
			Database.getDB().endTransaction(false);
			throw new ServerException(e.getMessage(), e);
		}	
		
	}//end of SubmitBatch()
	
	
	/**
	 * Called by GetFieldsHandler to get the fields either a project or the entire list 
	 *
	 */	
	
	public static GetFieldsResult getField(GetSampleImage_Params params) throws ServerException {	
		
		try {
			
			Database.getDB().startTransaction();
			
			ValidateUser_Params userParams = new ValidateUser_Params (); 
			ValidateUserResult retUser = new ValidateUserResult();
			
			GetFieldsResult getField = new GetFieldsResult ();
			List<FieldModel> retField = null;
			
			userParams.setUsername(params.getUsername());
			userParams.setPassword(params.getPassword());
			
			retUser = Database.getDB().getUserDAO().ValidateUser(userParams);
			
			if (retUser.getSetIndex()==0)
			{
				Database.getDB().endTransaction(false);
				return getField ;
			}
			
			else
			{
				int project_id = params.getProject_id();
				
				if(project_id != 0)
				{
					int retPro=Database.getDB().getFieldDAO().getProId(project_id);
					if(retPro != -1)
					{
						retField = Database.getDB().getFieldDAO().getField(project_id);
						getField.setField(retField);
						getField.setSetIndex(1);
					}
				}
				
				else
				{
					retField = Database.getDB().getFieldDAO().getAllField();
					getField.setField(retField);
					getField.setSetIndex(1);
				}
				
				Database.getDB().endTransaction(true);
				return getField ;			
			}	
		}
		catch (DatabaseException e) {
			Database.getDB().endTransaction(false);
			throw new ServerException(e.getMessage(), e);
		}	
		
	}//end getField()
	

	/**
	 * Called by SearchHandler to get the search for values in the Record table 
	 *
	 */	
	public static GetSearchResult GetSearch(GetSearch_Params params) throws ServerException 
	{
		try {
			
			Database.getDB().startTransaction();
			
			ValidateUser_Params userParams = new ValidateUser_Params (); 
			ValidateUserResult retUser = new ValidateUserResult();
			
			GetSearchResult getSearch = new GetSearchResult();
			
			userParams.setUsername(params.getUsername());
			userParams.setPassword(params.getPassword());
	
			retUser = Database.getDB().getUserDAO().ValidateUser(userParams);
			
			
			if (retUser.getSetIndex()==0)
			{
				Database.getDB().endTransaction(false);
				return getSearch ;
			}
			
			else
			{	
				getSearch=Database.getDB().getRecordDAO().getSearch(params.getField_id(), params.getSerach(),params.getHostURL());
				
				if(getSearch.getSetIndex()==0)
				{
					Database.getDB().endTransaction(false);
					return getSearch ;	
				}
				
				Database.getDB().endTransaction(true);
				return getSearch ;			
			}	
		}
		catch (DatabaseException e) {
			Database.getDB().endTransaction(false);
			throw new ServerException(e.getMessage(), e);
		}	
	}//end GetSearch()
	
	
}//end of Class ServerFacade

