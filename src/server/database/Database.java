package server.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 * A class that manage the connection with the Database.
 * This class is to be share among the various DAO classes
 *
 */

public class Database {
	
	private static final String DATABASE_DIRECTORY = "database";
	private static final String DATABASE_FILE = "record.sqlite";
	private static final String DATABASE_URL = "jdbc:sqlite:" + DATABASE_DIRECTORY +
												File.separator + DATABASE_FILE;

	private static Database instance=null;
	private static Logger logger;
	
	static {
		logger = Logger.getLogger("record");
	}

	public static void initialize() throws DatabaseException {
		try {
			
			final String driver = "org.sqlite.JDBC";
			Class.forName(driver);
		}
		catch(ClassNotFoundException e) {
			
			DatabaseException serverEx = new DatabaseException("Could not load database driver", e);
			
			logger.throwing("server.database.Database", "initialize", serverEx);

			throw serverEx; 
		}
	}
		
	private BatchDAO BatchDAO;
	private FieldDAO FieldDAO;
	private ProjectDAO ProjectDAO;
	private RecordDAO RecordDAO;
	private UserDAO UserDAO;
	private IndexerData Index;
	private Connection connection;
	
	public static Database getDB(){
		if(instance== null){
		instance= new Database();
		}
		return instance;
	}
	
	public Database() {
		BatchDAO = new BatchDAO();
		FieldDAO = new FieldDAO();
		ProjectDAO = new ProjectDAO();
		RecordDAO = new RecordDAO();
		UserDAO = new UserDAO();
		Index = new  IndexerData();
		connection = null;
	}
	
	public IndexerData getIndex(){
		return Index;
	}
	
	public void setIndex(IndexerData index1){
		this.Index=index1;
	}
	
	public BatchDAO getBatchDAO() {
		return BatchDAO;
	}
	
	public FieldDAO getFieldDAO() {
		return FieldDAO;
	}
	
	public ProjectDAO getProjectDAO() {
		return ProjectDAO;
	}
	
	public RecordDAO getRecordDAO() {
		return RecordDAO;
	}
	
	public UserDAO getUserDAO() {
		return UserDAO;
	}
	
	public Connection getConnection() {
		return connection;
	}

	public void startTransaction() throws DatabaseException {
		try {
			assert (connection == null);			
			connection = DriverManager.getConnection(DATABASE_URL);
			connection.setAutoCommit(false);
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not connect to database. Make sure " + 
				DATABASE_FILE + " is available in ./" + DATABASE_DIRECTORY, e);
		}
	}
	
	public void endTransaction(boolean commit) {
		if (connection != null) {		
			try {
				if (commit) {
					connection.commit();
				}
				else {
					connection.rollback();
				}
			}
			catch (SQLException e) {
				System.out.println("Could not end transaction");
				e.printStackTrace();
			}
			finally {
				safeClose(connection);
				connection = null;
			}
		}
	}
	
	public static void safeClose(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			}
			catch (SQLException e) {
				// ...
			}
		}
	}
	
	public static void safeClose(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			}
			catch (SQLException e) {
				// ...
			}
		}
	}
	
	public static void safeClose(PreparedStatement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			}
			catch (SQLException e) {
				// ...
			}
		}
	}
	
	public static void safeClose(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			}
			catch (SQLException e) {
				// ...
			}
		}
	}
	
}

