package tajo.tajo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Oozie & Tajo Example
 *
 */
public class TajoMain 
{
	  public static void main(String[] args) throws Exception {

		    try {
		      Class.forName("org.apache.tajo.jdbc.TajoDriver");
		    } catch (ClassNotFoundException e) {
		      // fill your handling code
		    }

		    Connection dbConnection = DriverManager.getConnection("jdbc:tajo://192.168.0.246:26002/default");

		    Statement stmt = null;
		    ResultSet rs = null;
		    try {
		    	
//		    	id	INT4
//		    	name	TEXT
//		    	score	FLOAT4
//		    	type	TEXT
		    	
		    	PreparedStatement preparedStatement = null;
		    	
		    	String insertTableSQL = "INSERT overwrite INTO TABLE4 select id, name, score, type from TABLE5";
		    	
		      stmt = dbConnection.createStatement();
		      
				preparedStatement = dbConnection.prepareStatement(insertTableSQL);
				 
				preparedStatement.setInt(1, 6);
				preparedStatement.setString(2, "kuntae");
				preparedStatement.setFloat(3, 100);
				preparedStatement.setString(4, "test data");

				preparedStatement.executeUpdate();
				
//		      rs = stmt.executeQuery("select * from table1");
//		      while (rs.next()) {
//		        System.out.println(rs.getString(1) + "," + rs.getString(3));
//		      }
				
		    } finally {
		      if (rs != null) rs.close();
		      if (stmt != null) stmt.close();
		      if (dbConnection != null) dbConnection.close();
		    }
		  }
}
