package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public abstract class H2Util {

	public static final String JDBC_DRIVER = "org.h2.Driver";

	// public static final String DB_URL = "jdbc:h2:mem:"; //in memory
	public static final String DB_URL = "jdbc:h2:file:" + System.getProperty("user.dir")
			+ "/h2/rdf-analyse;AUTO_SERVER=TRUE;AUTO_SERVER_PORT=9090";
	public static final String USER = "user_00";
	public static final String PASS = "pwd";

	// Name of database
	public static final String FILE_DATABASE_TABLE = "FILE_ORGANISATION";
	public static final String RESULT_DATABASE_TABLE = "MEASUREMENT_RESULT";
	public static final String ERROR_DATABASE_TABLE = "ERROR_LINES";
	public static final String CATEGORY_DATABASE_TABLE = "CATEGORIES";

	// Column names
	public static final String START = "START_TIME";
	public static final String END = "END_TIME";

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

	/**
	 * writes the current time in the given column
	 * 
	 * @param column where to write the current time, start or end
	 * 
	 * @throws SQLException when an error occurs during writing to the database
	 */
	public static void writeTime(Connection con, String column, long fileID, org.slf4j.Logger log) throws SQLException {
		log.info("Start writing time to " + column + " of file " + fileID);
		String query = "UPDATE " + H2Util.FILE_DATABASE_TABLE + " SET " + column + " = '"
				+ new Timestamp(System.currentTimeMillis()) + "' WHERE FILE_ID = " + fileID;
		H2Util.executeAndUpdate(con, query);
		log.info("Finished writing time to " + column + " of file " + fileID);
	}

}
