package server.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import shared.model.FieldModel;

/**
 * 
 * Worked with FieldModel class and performed query, insert 
 * and update operation on Field table.
 * 
 */

public class FieldDAO {


	/**
	 * 
	 * Called by ServerFacade to check on valid project_id.
	 * 
	 */
	
	public int getProId(int project_id) throws DatabaseException {
		
		int proj_id=-1;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String query = "select project_id from Field where project_id= ?";
			stmt = Database.getDB().getConnection().prepareStatement(query);
			stmt.setInt(1, project_id);
			
			rs = stmt.executeQuery();
			while (rs.next()) {
				proj_id= rs.getInt(1);
				
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
		
		return proj_id;		
		
	}//end of getProjectField()
	
	
	/**
	  * Return a list of Field objects from Field table for a project_id
	  * @return List of FieldModel objects
	  * @throws DatabaseException
	  */
	
	public List<FieldModel> getField(int project_id) throws DatabaseException {
		
		ArrayList<FieldModel> result = new ArrayList<FieldModel>();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String query = "select * from Field where project_id= ?";
			stmt = Database.getDB().getConnection().prepareStatement(query);
			stmt.setInt(1, project_id);
			
			rs = stmt.executeQuery();
			while (rs.next()) {
				int field_id = rs.getInt(1);
				String title = rs.getString(2);
				int xcoord = rs.getInt(3);
				int width = rs.getInt(4);
				String helpHtml= rs.getString(5);
				int column_no= rs.getInt(6);
				int pro_id= rs.getInt(7);
				String knownData= rs.getString(8);

				result.add(new FieldModel(field_id, title, xcoord, width, helpHtml, column_no ,pro_id, knownData));
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
		
		return result;		
		
	}//end of getProjectField()
	
	
	/**
	  * Return a list of Field objects from Field table
	  * @return List of FieldModel objects
	  * @throws DatabaseException
	  */
	
	public List<FieldModel> getAllField() throws DatabaseException {
		
		ArrayList<FieldModel> result = new ArrayList<FieldModel>();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String query = "select * from Field";
			stmt = Database.getDB().getConnection().prepareStatement(query);

			rs = stmt.executeQuery();
			while (rs.next()) {
				int field_id = rs.getInt(1);
				String title = rs.getString(2);
				int xcoord = rs.getInt(3);
				int width = rs.getInt(4);
				String helpHtml= rs.getString(5);
				int column_no= rs.getInt(6);
				int project_id= rs.getInt(7);
				String knownData= rs.getString(8);

				result.add(new FieldModel(field_id, title, xcoord, width, helpHtml, column_no ,project_id, knownData));
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
		
		return result;		
		
	}//end of getAllField()
	
	
	
	/**
	  * To add new Field data into the Field table
	  * @param FieldModel object
	  * @throws DatabaseException
	  */
	public void addField (FieldModel field) throws DatabaseException{
		
		PreparedStatement stmt = null;
		ResultSet keyRS = null;		
		try {
			String query = "insert into Field (title, xcoord, width, helphtml, columun_no ,project_id, knowndata) "
						 + "values (?, ?, ?, ?, ?, ?, ?)";
			stmt = Database.getDB().getConnection().prepareStatement(query);
			stmt.setString(1, field.getTitle());
			stmt.setInt(2, field.getXcoord());
			stmt.setInt(3, field.getWidth());
			stmt.setString(4, field.getHelpHtml());
			stmt.setInt(5, field.getColumn_no());
			stmt.setInt(6, field.getProject_id());
			stmt.setString(7, field.getKnownData());

			if (stmt.executeUpdate() == 1) {
				Statement keyStmt = Database.getDB().getConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				int field_id = keyRS.getInt(1);
				field.setField_id(field_id);
			}
			else {
				throw new DatabaseException("Could not insert Field");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not insert Field", e);
		}
		finally {
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}
					
	}//end of addField()
	
	
	/**
	  * Used by DataImporter to add data into the Field table.
	  * @throws DatabaseException
	  */

	public void addField(ArrayList<FieldModel> field, int project_id) throws DatabaseException {
		
		int column=1;
		for(FieldModel getField:field )
		{
			PreparedStatement stmt = null;
			ResultSet keyRS = null;		
			try {
				String query = "insert into Field (title, xcoord, width, helphtml, column_no ,project_id, knowndata) "
							 + "values (?, ?, ?, ?, ?, ?, ?)";
				stmt = Database.getDB().getConnection().prepareStatement(query);
				stmt.setString(1, getField.getTitle());
				stmt.setInt(2, getField.getXcoord());
				stmt.setInt(3, getField.getWidth());
				stmt.setString(4, getField.getHelpHtml());
				stmt.setInt(5, column++);
				stmt.setInt(6, project_id);
				stmt.setString(7, getField.getKnownData());

				if (stmt.executeUpdate() == 1) {
					Statement keyStmt = Database.getDB().getConnection().createStatement();
					keyRS = keyStmt.executeQuery("select last_insert_rowid()");
					keyRS.next();
					int field_id = keyRS.getInt(1);
					getField.setField_id(field_id);
				}
				else {
					throw new DatabaseException("Could not insert Field");
				}
			}
			catch (SQLException | DatabaseException e) {
				throw new DatabaseException("Could not insert Field", e);
			}
			finally {
				Database.safeClose(stmt);
				Database.safeClose(keyRS);
			}
		}
	}//end of addField()
	
}//end of Class FieldDAO
