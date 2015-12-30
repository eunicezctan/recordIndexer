package shared.communication;

/**
 * Communicator class that communicate with the client
 * Set and get username, pwd, batch_id and field value
 *
 */

public class SubmitBatch_Params {

	private String username;
	private String password;
	private int batch_id;
	private String field_value;
	
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
	public int getBatch_id() {
		return batch_id;
	}
	public void setBatch_id(int batch_id) {
		this.batch_id = batch_id;
	}
	public String getField_value() {
		return field_value;
	}
	public void setField_value(String field_value) {
		this.field_value = field_value;
	}
	
	
	
}
