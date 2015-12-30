package server.database;

/**
 * A Database exception class that throws exception on any Database error
 * 
 *
 */

@SuppressWarnings("serial")
public class DatabaseException extends Exception{

	public DatabaseException() {
		return;
	}

	public DatabaseException(String message) {
		super(message);
	}

	public DatabaseException(Throwable cause) {
		super(cause);

	}

	public DatabaseException(String message, Throwable cause) {
		super(message, cause);
	}

	
}
