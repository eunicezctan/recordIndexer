package server.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import shared.model.BatchModel;

/**
 * Worked with BatchModel class and performed query, insert 
 * and update operation on Batch table.
 * 
 */

public class BatchDAO {
	
	/**
	 * Used by ServerFacade SubmitBatch function for returning a project id
	 * 
	 */
	
    public int getProjectId(int b_id) throws DatabaseException {
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int project_id=0;
		
		try {
			
			String query = "select project_id from Batch where b_id = ?";
			stmt = Database.getDB().getConnection().prepareStatement(query);
			stmt.setInt(1, b_id);
			
			rs = stmt.executeQuery();
			while (rs.next()) {
				project_id = rs.getInt(1);
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
		
		return project_id;

	}	//end of getImageURL	
	
	 /**
	  * Return a single image URL from Batch table
	  * @return URL in String
	  * @throws DatabaseException
	  */

	public String getImageURL(int project_id) throws DatabaseException {
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String file = null;
		
		try {
			
			String query = "select min(file) from Batch where project_id = ?";
			stmt = Database.getDB().getConnection().prepareStatement(query);
			stmt.setInt(1, project_id);
			
			rs = stmt.executeQuery();
			while (rs.next()) {
				file = rs.getString(1);
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
		
		return file;

	}	//end of getImageURL
	

	 /**
	  * Return a list of Batch objects from Batch table
	  * @return List of BatchModel object
	  * @throws DatabaseException
	  */
		
	public BatchModel getBatch(int project_id) throws DatabaseException {
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		BatchModel getBatchRes= new BatchModel();
		
		try {
			String query = "select b_id, file from Batch where status = 1 and project_id = ? LIMIT 1";
			stmt = Database.getDB().getConnection().prepareStatement(query);
			stmt.setInt(1, project_id);
			
			rs = stmt.executeQuery();
			while (rs.next()) {
				getBatchRes.setBatch_id(rs.getInt(1));
				getBatchRes.setFile(rs.getString(2));
				getBatchRes.setProject_id(project_id);
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
		
		return getBatchRes;		
		
	}//end of getBatch()		
				
			
	/**
	  * To add new Batch data into the Batch table
	  * @param BatchModel object
	  * @throws DatabaseException
	  */
	public void addBatch (BatchModel batch) throws DatabaseException{
		
		PreparedStatement stmt = null;
		ResultSet keyRS = null;		
		try {
			String query = "insert into Batch ( file, project_id,status) values (?, ?, ?)";
			stmt = Database.getDB().getConnection().prepareStatement(query);
			stmt.setString(1, batch.getFile());
			stmt.setInt(2, batch.getProject_id());
			stmt.setInt(3, batch.getStatus());

			if (stmt.executeUpdate() == 1) {
				Statement keyStmt = Database.getDB().getConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				int batch_id = keyRS.getInt(1);
				batch.setBatch_id(batch_id);
			}
			else {
				throw new DatabaseException("Could not insert Batch");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not insert Batch", e);
		}
		finally {
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}
				
	}//end of addBatch()
	
			
	/**
	 * To update a Batch data in the Batch table
	 * @param Batch object
	 * @throws DatabaseException
	 */		
	public void updateBatch (int status, int b_id) throws DatabaseException{
		
		PreparedStatement stmt = null;
		try {
			String query = "update Batch set status = ? where b_id = ? ";
			stmt = Database.getDB().getConnection().prepareStatement(query);
			stmt.setInt(1, status);
			stmt.setInt(2, b_id);

			
			if (stmt.executeUpdate() != 1) {
				throw new DatabaseException("Could not update Batch");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not update Batch", e);
		}
		finally {
			Database.safeClose(stmt);
		}
				
	 }//end of updateBatch()	



	/**
	  * Used by DataImporter to import data into the Batch table.
	  * @param List of RecordModel object
	  * @throws DatabaseException
	  */
	public void addBatch(ArrayList<BatchModel> batch, int project_id) throws DatabaseException {
		
		for(BatchModel getBatch:batch )
		{
			PreparedStatement stmt = null;
			ResultSet keyRS = null;		
			try {
				String query = "insert into Batch ( file, project_id,status) values (?, ?, ?)";
				stmt = Database.getDB().getConnection().prepareStatement(query);
				stmt.setString(1, getBatch.getFile());
				stmt.setInt(2, project_id);
				stmt.setInt(3, getBatch.getStatus() );

				if (stmt.executeUpdate() == 1) {
					Statement keyStmt = Database.getDB().getConnection().createStatement();
					keyRS = keyStmt.executeQuery("select last_insert_rowid()");
					keyRS.next();
					int batch_id = keyRS.getInt(1);
					getBatch.setBatch_id(batch_id);
	
					Database.getDB().getRecordDAO().addRecord(getBatch.getRecord(),batch_id,project_id);
				}
				else {
					throw new DatabaseException("Could not insert Batch");
				}
			}
			catch (SQLException | DatabaseException e) {
				throw new DatabaseException("Could not insert Batch", e);
			}
			finally {
				Database.safeClose(stmt);
				Database.safeClose(keyRS);
			}
		}
    }
		
 }//end of Class BatchDAO
