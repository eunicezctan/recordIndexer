package shared.communication;

/**
 * Communicator class that communicate with the client
 * Return user parameters from UserModel table
 *
 */

public class ValidateUserResult {
	
	private String firstname;
	private String lastname;
	private int indexedrecords;
	private int setIndex;
	
	
	public ValidateUserResult(String firstname, String lastname, int indexedrecords, int setIndex) {
	    this.firstname = ("FirstName");
	    this.lastname = ("LastName");
	    this.indexedrecords = 0;
	    this.setIndex = 0;
    }

	public ValidateUserResult() {
	    // TODO Auto-generated constructor stub
    }
	
	public int getSetIndex() {
		return setIndex;
	}

	public void setSetIndex(int setIndex) {
		this.setIndex = setIndex;
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
	public int getIndexedrecords() {
		return indexedrecords;
	}
	public void setIndexedrecords(int indexedrecords) {
		this.indexedrecords = indexedrecords;
	}
	
	
	@Override
    public String toString() {
		
		if(setIndex==1)
		{
			return 	"TRUE\n"
					+firstname + "\n"
					+lastname + "\n"
					+indexedrecords + "\n";
		}
		
		else
			return "FALSE\n";
    } 
	
}
