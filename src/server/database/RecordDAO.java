package server.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import shared.communication.GetSearchResult;
import shared.communication.SubmitBatchResult;
import shared.model.RecordModel;
import shared.model.SearchModel;

/**
 * 
 * Worked with RecordModel class and performed query, insert 
 * and update operation on Record table.
 * 
 */


public class RecordDAO {
	
	//For search function
	private ArrayList<SearchModel> getSearchList = new ArrayList<SearchModel>();
	
	
	/**
	 * Called by ServerFacade for Search function. Take in search parameters and search for valid field by 
	 * calling checkField() and search() to get search result .
	 *
	 */
	public GetSearchResult getSearch (String field, String value, String URL) throws NumberFormatException, DatabaseException{
		
		GetSearchResult getSearch = new GetSearchResult();
		getSearchList.clear();
	
		String getField [] =field.trim().split(",");
		String getValue [] = value.trim().split(","); 
		
		for (int i=0; i<getField.length; i++)
		{
			int checkField = checkField(Integer.parseInt(getField[i].trim()));
			
			if(checkField==-1)
			{
				return getSearch;
			}
		}
		
		for (int i=0; i<getField.length; i++)
		{
			for(int x=0; x<getValue.length; x++)
			{
				Search(getField[i].trim(), getValue[x].trim());	
			}
		}
		
		if(getSearchList.isEmpty())
			return getSearch;
		
		else
		{
			getSearch.setSearch(getSearchList);
			getSearch.setHostURL(URL);
			getSearch.setSetIndex(1);
			return getSearch;
		}
		
	
	}//end of getSearch()
	
	
	/**
	 * Called by getSearch() to validate field.
	 *
	 */
	public int checkField (int field_id) throws DatabaseException {
		
		int getField =-1;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String query = "select f_id from Field where f_id= ?";
			stmt = Database.getDB().getConnection().prepareStatement(query);
			stmt.setInt(1, field_id);
			
			rs = stmt.executeQuery();
			while (rs.next()) {
				getField = rs.getInt(1);				
			}
		}
		catch (SQLException e) {
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			
			throw serverEx;
		}		
		finally {
			Database.safeClose(stmt);
			Database.safeClose(rs);
		}
		
		return getField;
		
	}//end of checkField()
	
	
	/**
	 * Called by Search() to get search result for field_no and value.
	 *
	 */
	public void Search (String field_id, String value) throws DatabaseException {
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String query = "select batch_id, file, record_no, field_id from record join batch on batch.b_id=record.batch_id "
							+ "where field_id= ? and upper(value) = upper(?)";
			
			stmt = Database.getDB().getConnection().prepareStatement(query);
			stmt.setInt(1,Integer.parseInt(field_id));
			stmt.setString(2, value);
			
			rs = stmt.executeQuery();
			while (rs.next()) {
				int batch_id = rs.getInt(1);
				String file = rs.getString(2);
				int record_no = rs.getInt(3);
				int field = rs.getInt(4);
				
				getSearchList.add(new SearchModel(batch_id, file, record_no, field));
			}
		}
		catch (SQLException e) {
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			
			throw serverEx;
		}		
		finally {
			Database.safeClose(stmt);
			Database.safeClose(rs);
		}		
		
	}//end of getSearch()
	
	
	/**
	 * Called by ServerFacade for SubmitBatch function. Take in the the string value and parse it. Get field id 
	 * and all addRecord() to add value.
	 *
	 */
	public SubmitBatchResult submitBatch (String listValue, int batch_id, int project_id) throws DatabaseException{
		
		SubmitBatchResult submit = new SubmitBatchResult();
		TreeSet<Integer> getField=getFieldID(project_id);
		
		int fileSize= getField.size();
		
		//Split value into row of records
		String list [] = listValue.split(";",-1);
		for (int i=0; i<list.length; i++)
		{
			String value [] = list[i].split(",");
			
			//insert empty string in the Record table
			if(value.length==0)
			{				
				for(int y=0; y < fileSize; y++)
				{
					addRecord (new RecordModel("",i+1,(getField.first()+y),batch_id));
				}
			}
			
			for(int x=0; x<value.length; x++)
			{
				 if(value.length==fileSize)
					 addRecord (new RecordModel(value[x],i+1,(getField.first()+x),batch_id));
				 else
					 return submit;
			}	
		}
		
		submit.setTotalIndex(list.length);
		submit.setSetIndex(1);
		return submit;	
		
	}//end of submitBatch() 
	
	
	/**
	  * To add new Record data into the Record table
	  * @param RecordModel object
	  * @throws DatabaseException
	  */	
			
	public void addRecord (RecordModel record) throws DatabaseException{
		
		PreparedStatement stmt = null;
		ResultSet keyRS = null;		
		try {
			String query = "insert into Record (value, record_no, field_id, batch_id) "
						 + "values (?, ?, ?, ?)";
			stmt = Database.getDB().getConnection().prepareStatement(query);
			stmt.setString(1, record.getValue());
			stmt.setInt(2, record.getRecord_no());
			stmt.setInt(3, record.getField_id());
			stmt.setInt(4, record.getBatch_id());

			if (stmt.executeUpdate() == 1) {
				Statement keyStmt = Database.getDB().getConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				int record_id = keyRS.getInt(1);
				record.setRecord_id(record_id);
			}
			else {
				throw new DatabaseException("Could not insert Record");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not insert Record", e);
		}
		finally {
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}	
		
	}//end of addRecord()
	
	/**
	  * Return a list of Record objects from Record table
	  * @return List of RecordModel objects
	  * @throws DatabaseException
	  */
	public List<RecordModel> getAllRecord() throws DatabaseException {
		
		ArrayList<RecordModel> result = new ArrayList<RecordModel>();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String query = "select * from Record";
			stmt = Database.getDB().getConnection().prepareStatement(query);

			rs = stmt.executeQuery();
			while (rs.next()) {
				int record_id = rs.getInt(1);
				String value = rs.getString(2);
				int record_no = rs.getInt(3);
				int field_id = rs.getInt(4);
				int batch_id = rs.getInt(5);

				result.add(new RecordModel(record_id, value, record_no, field_id, batch_id));
			}
		}
		catch (SQLException e) {
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			
			throw serverEx;
		}		
		finally {
			Database.safeClose(stmt);
			Database.safeClose(rs);
		}

		return result;		
		
	}// end of getAllRecord()	
	
		
	/**
	  * To update a Record data in the Record table
	  * @param RecordModel object
	  * @throws DatabaseException
	  */
	public void updateRecord (RecordModel record) throws DatabaseException{
		
		PreparedStatement stmt = null;
		try {
			String query = "update Record set value = ?, record_no = ?, field_no = ?, batch_id = ? where r_id = ?";
			stmt = Database.getDB().getConnection().prepareStatement(query);
			stmt.setString(1, record.getValue());
			stmt.setInt(2, record.getRecord_no());
			stmt.setInt(3, record.getField_id());
			stmt.setInt(4, record.getBatch_id());
			stmt.setInt(5, record.getRecord_id());
			
			if (stmt.executeUpdate() != 1) {
				throw new DatabaseException("Could not update Record");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not update Record", e);
		}
		finally {
			Database.safeClose(stmt);
		}			
				
	}//end of updateRecord()

	/**
	  * Used by Batch to add a Record data in the Record table for DataImporter.
	  * @throws DatabaseException
	  */

	public void addRecord(ArrayList<RecordModel> record, int batch_id, int project_id) throws DatabaseException {
	  
	  int rowCount=1;
	  if(!record.isEmpty())
	  {
		 //Get Field ID
		 TreeSet<Integer> getID=getFieldID(project_id);
			
		 int fieldMax=getID.first()+getID.size();
		 int field_id=getID.first();
		 int inField=field_id;
	
		 for(RecordModel getRecord:record )
		 {	
			if(inField==fieldMax)
			{
				inField=field_id;
				rowCount++;
			}
		
			PreparedStatement stmt = null;	
			try {
				String query = "insert into Record (value, record_no, field_id, batch_id) "
								+ "values (?, ?, ?, ?)";
				stmt = Database.getDB().getConnection().prepareStatement(query);
				stmt.setString(1, getRecord.getValue());
				stmt.setInt(2, rowCount);
				stmt.setInt(3, inField++);
				stmt.setInt(4, batch_id);

				if (stmt.executeUpdate() != 1) {
					throw new DatabaseException("Could not insert Record");
				}
			
			}
			catch (SQLException | DatabaseException e) {
				throw new DatabaseException("Could not insert Record", e);
			}
			finally {
				Database.safeClose(stmt);
			}	
		  }	
		}
	}//end of addRecord()
	
	
	/**
	 * Used by various addRecord() to get the field id from the Field table
	 *
	 */
	
	public TreeSet<Integer> getFieldID(int project_id) throws DatabaseException {
		
		TreeSet<Integer> getField=new TreeSet <Integer>();
		PreparedStatement stmtField = null;
		ResultSet rs = null;	
		
		try {
			String queryField = "select f_id from Field where project_id = ?";
			stmtField = Database.getDB().getConnection().prepareStatement(queryField);
			stmtField.setInt(1, project_id);
			
			rs = stmtField.executeQuery();
			while (rs.next()) {
				int field_id = rs.getInt(1);
				getField.add(field_id);
			}
		}
		catch (SQLException e1) {
			throw new DatabaseException("Could not get Field ID", e1);
		}
		finally {
			Database.safeClose(stmtField);
			Database.safeClose(rs);	
		}
	
	  return getField;	
		
	}//end of getFieldID()
	
}//end of Class RecordDAO
