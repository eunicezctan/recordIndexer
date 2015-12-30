package shared.communication;

/**
 * Communicator class that communicate with the client
 * Set and get username and pwd and project id
 *
 */

public class GetSampleImage_Params {

	private String username;
	private String password;
	private int project_id;
	private String hostURL;
	
	
	public String getHostURL() {
		return hostURL;
	}
	public void setHostURL(String hostURL) {
		this.hostURL = hostURL;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getProject_id() {
		return project_id;
	}
	public void setProject_id(int pro_id) {
		this.project_id = pro_id;
	}
	
	
	
	
}
