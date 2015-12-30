package shared.communication;

/**
 * Communicator class that communicate with the client
 * Set and get username, pwd, field_id and search
 *
 */

public class GetSearch_Params {
	
	private String username;
	private String password;
	private String field_id;
	private String serach;
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
	public String getField_id() {
		return field_id;
	}
	public void setField_id(String field_id) {
		this.field_id = field_id;
	}
	public String getSerach() {
		return serach;
	}
	public void setSerach(String serach) {
		this.serach = serach;
	}
	
	
	

}
