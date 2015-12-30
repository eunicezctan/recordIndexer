package shared.communication;

/**
 * Communicator class that communicate with the client
 * Set and get username and pwd
 *
 */
public class ValidateUser_Params {

	private String username;
	private String password;
	
	public ValidateUser_Params() {

		username=null;
		password=null;
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

	
}
