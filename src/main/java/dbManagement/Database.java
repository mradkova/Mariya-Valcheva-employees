package dbManagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Database {

	final String JDBC_DRIVER = "org.h2.Driver";
	final String DB_URL = "jdbc:h2:~/test";

	final String USER = "sa";
	final String PASS = "sa";
	
	Connection conn = null;

	public void createConnection() throws Exception {
		try {
			Class.forName(JDBC_DRIVER);			
			//conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test", USER, PASS);
			conn = DriverManager.getConnection("jdbc:h2:~/test", USER, PASS);
			
			System.out.println("Connected!");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void createTable() throws Exception {
		try {
			PreparedStatement drop = conn.prepareStatement(
					"DROP TABLE IF EXISTS Projects ");
					
			PreparedStatement create = conn.prepareStatement(
					"CREATE TABLE Projects(EmpID int Not Null, ProjectID int Not Null, DateFrom Date, DateTo Date Default NULL)");
			
			drop.executeUpdate();
			create.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("Table Projects created.");
		}
	}
	
	public Connection getConnection() {
		return this.conn;
	}

}
