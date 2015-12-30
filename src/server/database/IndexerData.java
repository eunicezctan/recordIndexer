package server.database;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.w3c.dom.Element;

import shared.model.ProjectModel;
import shared.model.UserModel;

public class IndexerData {

	
	private ArrayList<UserModel> user = new ArrayList<UserModel>();
	private ArrayList<ProjectModel> project = new ArrayList<ProjectModel>();

	public IndexerData(Element root) {
		
		ArrayList<Element> rootElements = DataImporter.getChildElements(root);
		 
		ArrayList<Element> userElements = DataImporter.getChildElements(rootElements.get(0));
		for(Element userElement : userElements)
			user.add(new UserModel(userElement));

		ArrayList<Element> projectElements = DataImporter.getChildElements(rootElements.get(1));
		for(Element projectElement : projectElements)
			project.add(new ProjectModel(projectElement));
	}//end of constructor
	
	
	public IndexerData() {
	    // TODO Auto-generated constructor stub
    }


	public void createTable() {
	
		Statement stmt= null;
		
		try {
				String query = 
						"DROP TABLE IF EXISTS Field;"
						+ "CREATE TABLE Field ("
						+ "f_id INTEGER PRIMARY KEY AUTOINCREMENT  NOT NULL UNIQUE,"
						+ "title VARCHAR NOT NULL ,"
						+ "xcoord INTEGER NOT NULL ,"
						+ "width INTEGER NOT NULL ,"
						+ "helphtml VARCHAR NOT NULL ,"
						+ "column_no INTEGER NOT NULL ,"
						+ "project_id INTEGER NOT NULL ,"
						+ "knowndata VARCHAR);"
						+""
						+ "DROP TABLE IF EXISTS Batch;"
						+ "CREATE TABLE Batch ("
						+ "b_id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ,"
						+ "file VARCHAR NOT NULL , "
						+ "project_id INTEGER NOT NULL , "
						+ "status INTEGER NOT NULL );" 
						+""
						+ "DROP TABLE IF EXISTS Project;"
						+ "CREATE TABLE Project ("
						+ "p_id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , "
						+ "title VARCHAR NOT NULL , "
						+ "recordsperimage INTEGER NOT NULL , "
						+ "firstycoord INTEGER NOT NULL , "
						+ "recordheight INTEGER NOT NULL );"
						+ ""
						+ "DROP TABLE IF EXISTS Record;"
						+ "CREATE TABLE Record ("
						+ "r_id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , "
						+ "value VARCHAR, "
						+ "record_no INTEGER NOT NULL , "
						+ "field_id INTEGER NOT NULL , "
						+ "batch_id INTEGER NOT NULL );"
						+ ""
						+ "DROP TABLE IF EXISTS User;"
						+ "CREATE TABLE User ("
						+ "u_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE ,"
						+ "username VARCHAR NOT NULL ,"
						+ "password VARCHAR NOT NULL ,"
						+ "firstname VARCHAR NOT NULL ,"
						+ "lastname VARCHAR NOT NULL ,"
						+ "email VARCHAR NOT NULL ,"
						+ "indexedrecords INTEGER, "
						+ "currentbatch INTEGER);";
	    					
				stmt = Database.getDB().getConnection().createStatement();
				stmt.executeUpdate(query);  			
		}
        catch (SQLException  e) {
	        e.printStackTrace();
        }
		finally {
			Database.safeClose(stmt);
		}
		
	}//end of createTable() 
	
	
	public void insertData() throws DatabaseException{
		
		for(UserModel getUser:user)
		{
			Database.getDB().getUserDAO().addUser(getUser);
		}
		
		for(ProjectModel getProject:project)
		{
			Database.getDB().getProjectDAO().addProject(getProject);
			Database.getDB().getFieldDAO().addField(getProject.getField(),getProject.getProject_id());
			Database.getDB().getBatchDAO().addBatch(getProject.getBatch(),getProject.getProject_id());
			
		}
		
	}//end of insertData()
	

}//end of Class IndexerData
