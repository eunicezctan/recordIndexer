package shared.model;

import org.w3c.dom.Element;

import server.database.DataImporter;

/**
 * UserModel class that set and store the data from the User table in the Database
 * using the getter and setter. It parameters are model after the fields in the User table.
 *
 */
public class UserModel {
	private int user_id;
	private String username;
	private String password;
	private String firstname;
	private String lastname;
	private String email;
	private int indexedrecords; 
	private int currentbatch;


	public UserModel() {
		setUser_id(-1);
		setUsername("New Name");
		setPassword("New Pwd");
		setFirstname("New Firstname");
		setLastname("New Lastname");
		setEmail("New Email");
		setIndexedrecords(-1);
		setCurrentbatch(-1);
    }
	
	//for jUint test
	public UserModel(int id, String user, String pwd, String first, String last,
            String email,int index, int current) {
		
		setUser_id(id);
		setUsername(user);
		setPassword(pwd);
		setFirstname(first);
		setLastname(last);
		setEmail(email);
		setIndexedrecords(index);
		setCurrentbatch(current);
    }
	
	
	public UserModel(Element userElement) {
		
		username = DataImporter.getValue((Element)userElement.getElementsByTagName("username").item(0));
		password = DataImporter.getValue((Element)userElement.getElementsByTagName("password").item(0));
		firstname = DataImporter.getValue((Element)userElement.getElementsByTagName("firstname").item(0));
		lastname = DataImporter.getValue((Element)userElement.getElementsByTagName("lastname").item(0));
		email = DataImporter.getValue((Element)userElement.getElementsByTagName("email").item(0));
		indexedrecords = Integer.parseInt(DataImporter.getValue((Element) userElement.getElementsByTagName("indexedrecords").item(0)));
		currentbatch=0;
	}

	

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
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
	
	public String getFirstname() {
		return firstname;
	}
	
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	public String getLastname() {
		return lastname;
	}
	
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public int getIndexedrecords() {
		return indexedrecords;
	}
	
	public void setIndexedrecords(int indexedrecords) {
		this.indexedrecords = indexedrecords;
	}
	
	public int getCurrentbatch() {
		return currentbatch;
	}
	
	public void setCurrentbatch(int currentbatch) {
		this.currentbatch = currentbatch;
	}
	
}//end of Class UserModel
