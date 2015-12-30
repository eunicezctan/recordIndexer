package server.database;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import shared.communication.ValidateUserResult;
import shared.communication.ValidateUser_Params;
import shared.model.UserModel;

/**
 * 
 * Worked with UserModel class and performed query, insert 
 * and update operation on User table.
 * 
 */

 public class UserDAO {
	 
	 public UserDAO() {
	        // TODO Auto-generated constructor stub
        }


 /**
  * Called by ServerFacade to validate user current batch
  *
  */
	 
 public int ValidateUserBatch (ValidateUser_Params user) throws DatabaseException {
			
	PreparedStatement stmt = null;
	ResultSet rs = null;
	int getCurBatch=-1;
	
	try {
			String query = "select currentbatch from User where username = ? and password = ?";
			stmt = Database.getDB().getConnection().prepareStatement(query);
			stmt.setString(1, user.getUsername());
			stmt.setString(2, user.getPassword());
				
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				getCurBatch = rs.getInt(1); 
			}
		}
		catch (SQLException e) {
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);		
			throw serverEx;
		}		
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		
		return getCurBatch;
			
	}//end of ValidateUser()	 
	 
	 
	 
	/**
	  * For a single user validation
	  * @return A single UserModel
	  * @throws DatabaseException
	  */
	public ValidateUserResult ValidateUser (ValidateUser_Params user) throws DatabaseException {
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ValidateUserResult getUserRes= new ValidateUserResult();
		
		try {
			String query = "select firstname, lastname,indexedrecords from User where username = ? and password = ?";
			stmt = Database.getDB().getConnection().prepareStatement(query);
			stmt.setString(1, user.getUsername());
			stmt.setString(2, user.getPassword());
			
			rs = stmt.executeQuery();
		
			while (rs.next()) {
				getUserRes.setFirstname(rs.getString(1)); 
				getUserRes.setLastname(rs.getString(2)); 
				getUserRes.setIndexedrecords(rs.getInt(3)); 
				getUserRes.setSetIndex(1);
			}
		}
		catch (SQLException e) {
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);		
			throw serverEx;
		}		
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}

		return getUserRes;
		
	}//end of ValidateUser()
	
	 /**
	  * To add users into the User table
	  * @param UserModel object
	  * @throws DatabaseException
	  */
	
	public void addUser (UserModel user) throws DatabaseException{
		
		PreparedStatement stmt = null;
		ResultSet keyRS = null;		
		try {
			String query = "insert into User (username, password, firstname, lastname, email, indexedrecords, currentbatch) "
						 + "values (?, ?, ?, ?, ?, ?, ?)";
			stmt = Database.getDB().getConnection().prepareStatement(query);
			stmt.setString(1, user.getUsername());
			stmt.setString(2, user.getPassword());
			stmt.setString(3, user.getFirstname());
			stmt.setString(4, user.getLastname());
			stmt.setString(5, user.getEmail() );
			stmt.setInt(6, user.getIndexedrecords());
			stmt.setInt(7, user.getCurrentbatch());

			if (stmt.executeUpdate() == 1) {
				Statement keyStmt =  Database.getDB().getConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				int user_id = keyRS.getInt(1);
				user.setUser_id(user_id);
			}
			else {
				throw new DatabaseException("Could not insert User");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not insert User", e);
		}
		finally {
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}
				
	}//end of addUser()
	
	/**
	  * To update a user at the User table
	  * @param UserModel object to be updated
	  * @throws DatabaseException
	  */
	
	public void updateUserBatch (int curBatch, String user, String password) throws DatabaseException{
		
		PreparedStatement stmt = null;
		try {
			String query = "update User set currentbatch =  ? where username = ? and password = ?";
			stmt = Database.getDB().getConnection().prepareStatement(query);
			stmt.setInt(1, curBatch);
			stmt.setString(2, user);
			stmt.setString(3, password);
			
			if (stmt.executeUpdate() != 1) {
				throw new DatabaseException("Could not update User");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not update User", e);
		}
		finally {
			Database.safeClose(stmt);
		}			
		
	}//end of updateUserBatch()
	
	/**
	 * Called by ServerFacade to update indexed records and current batch when it is done.
	 *
	 */
	
	public void updateUserIndex(int indexed, int curBatch, String user, String password) throws DatabaseException{
		
		PreparedStatement stmt = null;
		try {
			String query = "update User set indexedrecords = indexedrecords+  ?, currentbatch = ? where username = ? and password = ?";
			stmt = Database.getDB().getConnection().prepareStatement(query);
			stmt.setInt(1, indexed);
			stmt.setInt(2, curBatch);
			stmt.setString(3, user);
			stmt.setString(4, password);
			
			if (stmt.executeUpdate() != 1) {
				throw new DatabaseException("Could not update User");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not update User", e);
		}
		finally {
			Database.safeClose(stmt);
		}
	}//end of updateUserIndex()
	
}//end of Class UserDAO
