package server.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import shared.model.ProjectModel;

/**
 * 
 * Worked with ProjectModel class and performed query, insert 
 * and update operation on Project table.
 * 
 */

public class ProjectDAO {

	
	 /**
	  * Return a single Project object from Project table
	  * @return Project object
	  * @throws DatabaseException
	  */
	public ProjectModel getProject(int project_id) throws DatabaseException {
				
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ProjectModel getProRes= new ProjectModel();
		try {
			String query = "select * from Project where p_id= ?";
			stmt = Database.getDB().getConnection().prepareStatement(query);
			stmt.setInt(1, project_id);
			
			rs = stmt.executeQuery();
			while (rs.next()) {
				getProRes.setProject_id(rs.getInt(1)); 
				getProRes.setTitle(rs.getString(2)); 
				getProRes.setRecordsperimage(rs.getInt(3)); 
				getProRes.setFirstycoord(rs.getInt(4)); 
				getProRes.setRecordheight(rs.getInt(5));
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

		return getProRes;
	}
	
	/**
	  * Return a list of Project objects from Project table
	  * @return List of ProjectModel object
	  * @throws DatabaseException
	  */
	public List<ProjectModel> getAllProject() throws DatabaseException {
		
		ArrayList<ProjectModel> result = new ArrayList<ProjectModel>();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String query = "select * from Project";
			stmt = Database.getDB().getConnection().prepareStatement(query);

			rs = stmt.executeQuery();
			while (rs.next()) {
				int project_id = rs.getInt(1);
				String title = rs.getString(2);
				int recordsPerimage = rs.getInt(3);
				int firstYcoord = rs.getInt(4);
				int recordHeight = rs.getInt(5);
				
				result.add(new ProjectModel(project_id, title, recordsPerimage, firstYcoord, recordHeight));
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
	}//end of getAllProject()
	
	/**
	  * To add Projects into the Project table
	  * @param ProjectModel object
	  * @throws DatabaseException
	  */
	
	public void addProject (ProjectModel project) throws DatabaseException{
		
		PreparedStatement stmt = null;
		ResultSet keyRS = null;		
		try {
			String query = "insert into Project (title, recordsperimage, firstycoord, recordheight) "
						 + "values (?, ?, ?, ?)";
			stmt = Database.getDB().getConnection().prepareStatement(query);
			stmt.setString(1, project.getTitle());
			stmt.setInt(2, project.getRecordsperimage());
			stmt.setInt(3, project.getFirstycoord());
			stmt.setInt(4, project.getRecordheight());

			if (stmt.executeUpdate() == 1) {
				Statement keyStmt = Database.getDB().getConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				int project_id = keyRS.getInt(1);
				project.setProject_id(project_id);
			}
			else {
				throw new DatabaseException("Could not insert Project");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not insert Project", e);
		}
		finally {
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}
		
	}//end of addProject()
	

}//end of Class ProjectDAO
