package client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import shared.communication.DownloadBatchResult;
import shared.communication.GetFieldsResult;
import shared.communication.GetProjectsResult;
import shared.communication.GetSampleImageResult;
import shared.communication.GetSampleImage_Params;
import shared.communication.GetSearchResult;
import shared.communication.GetSearch_Params;
import shared.communication.SubmitBatchResult;
import shared.communication.SubmitBatch_Params;
import shared.communication.ValidateUserResult;
import shared.communication.ValidateUser_Params;

/**
 * ClientCommunicator class that will communicate with the server
 *
 */

public class ClientCommunicator {


	private String SERVER_HOST;
	private String SERVER_PORT;
	private String URL_PREFIX;
	private String HTTP_POST = "POST";
	private XStream xmlStream;

	public ClientCommunicator() {
		
	}
	
	public ClientCommunicator(String host, String port) {
		xmlStream = new XStream(new DomDriver());
		SERVER_HOST=host;
		SERVER_PORT=port;
		URL_PREFIX= "http://" + SERVER_HOST + ":" + SERVER_PORT;
    }

	public String getSERVER_HOST() {
		return SERVER_HOST;
	}

	public void setSERVER_HOST(String sERVER_HOST) {
		SERVER_HOST = sERVER_HOST;
	}

	public String getSERVER_PORT() {
		return SERVER_PORT;
	}

	public void setSERVER_PORT(String sERVER_PORT) {
		SERVER_PORT = sERVER_PORT;
	}

	

	/**
	 * Client side for validating user credentials and return client credential information with number of indexed recorded
	 * @param ValidateUser_params acces the server class for validating the user
	 * @return ValidateUserResult return the result from the server to the client
	 */
	
	public ValidateUserResult ValidateUser (ValidateUser_Params params) throws ClientException
	{
		return (ValidateUserResult) post ("/ValidateUser", params);
		
	}
	

	/**
	 * Client side for returning information for all available projects
	 * @param ValidateUser_params acces the server class for validating the user
	 * @return GetProjectsResult return all available projects information from the server to the client
	 * @throws ClientException 
	 */

	public GetProjectsResult GetProjects (ValidateUser_Params params) throws ClientException
	{
		return (GetProjectsResult) post ("/GetProjects", params);
	}
	
	
	/**
	 * Client side for returning a sample image for the specific project
	 * @param GetSampleImage_Params pass user id, pwd and project id to the server
	 * @return GetSampleImageResult return a sample image for the specific project from the server to the client
	 * @throws ClientException 
	 */
	
	public GetSampleImageResult GetSampleImage (GetSampleImage_Params params) throws ClientException
	{
		return (GetSampleImageResult) post ("/GetSampleImageResult", params);
	}
	
	/**
	 * Client side for downloading an available batch to a user
	 * @param  GetSampleImage_Params pass user id, pwd and project id to the server
	 * @return DownloadBatchResult return an available batch to a user from the server
	 * @throws ClientException 
	 */
	
	public DownloadBatchResult DownloadBatch (GetSampleImage_Params params) throws ClientException
	{
		return (DownloadBatchResult) post ("/DownloadBatchResult", params);
	}
	

	/**
	 * Client side to handle user batch submission
	 * @param SubmitBatch_Params pass  pass user id, pwd and batch id and field and record values to the server
	 * @return SubmitBatchResult return true if operation is successful
	 * @throws ClientException 
	 */
	
	public SubmitBatchResult SubmitBatch (SubmitBatch_Params params) throws ClientException
	{
		return (SubmitBatchResult) post ("/SubmitBatch", params);
	}
	
	
	/**
	 * Client side for returning the search string of the indexed records
	 * @param Getfields_Params pass  pass user id, pwd id / empty string to the server
	 * @return GetFieldsResult return fields
	 * @throws ClientException 
	 */
	
	public GetFieldsResult GetFields (GetSampleImage_Params params) throws ClientException
	{
		return (GetFieldsResult) post ("/GetFields", params);
	}
	
	
	/**
	 * Client side for returning either all fields of all projects or specific fields of a project
	 * @param Getfields_Params pass  pass user id, field id and search value to the server
	 * @return GetFieldsResult return the search result
	 * @throws ClientException 
	 */
	
	
	public GetSearchResult GetSearch (GetSearch_Params params) throws ClientException
	{
		return (GetSearchResult) post ("/GetSearch", params);
	}
	
	
	
	private Object post (String urlPath, Object postData) throws ClientException {
		Object result=null;
		
		try {
			
			URL url = new URL(URL_PREFIX + urlPath);
			
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod(HTTP_POST);
			connection.setDoOutput(true);
			connection.setDoInput(true);
		
			connection.connect();
			
			xmlStream.toXML(postData, connection.getOutputStream()); // send data across to server
			connection.getOutputStream().close();
			
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new ClientException(String.format("Post failed: %s (http code %d)",
						urlPath, connection.getResponseCode()));
			}
			else
				 result = xmlStream.fromXML(connection.getInputStream()); //get data from the server
		}
		catch (IOException e) {
			throw new ClientException(String.format("Post failed: %s", e.getMessage()), e);
		}
		
		return result;
		
	}

	
}// end of ClientCommunicator
