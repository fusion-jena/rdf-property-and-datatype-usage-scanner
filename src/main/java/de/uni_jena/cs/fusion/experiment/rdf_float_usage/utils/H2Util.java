package de.uni_jena.cs.fusion.experiment.rdf_float_usage.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class H2Util {
	
	public static final String JDBC_DRIVER = "org.h2.Driver";

	//	public static final String DB_URL = "jdbc:h2:mem:"; //in memory 
	public static final String DB_URL = "jdbc:h2:file:C:/Users/Merle/Desktop/Uni2/fsu_jena/ss21/hiwi/analyse/h2/rdf-analyse";
	public static final String USER = "user_00";
	public static final String PASS = "pwd";

	// Name of database
	public static final String FILE_DATABASE = "FILE_ORGANISATION";
	public static final String RESULT_DATABASE = "MEASUREMENT_RESULT";
	public static final String ERROR_DATABASE = "ERROR_LINES";
	
	/**
	 * Executes a single SQL Query and updates the database
	 * 
	 * @param con   Connection to the the database
	 * @param query is executed
	 * @throws SQLException in case of an invalid sql command
	 * 
	 */
	public static void executeAndUpdate(Connection con, String query) throws SQLException {
		Statement stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

}
