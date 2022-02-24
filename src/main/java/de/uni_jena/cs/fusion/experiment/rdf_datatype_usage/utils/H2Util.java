/**
 * The MIT License
 * Copyright © 2021 Heinz Nixdorf Chair for Distributed Information Systems, Friedrich Schiller University Jena
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public abstract class H2Util {

	public static final String JDBC_DRIVER = "org.h2.Driver";

	// public static final String DB_URL = "jdbc:h2:mem:"; //in memory
	public static final String DB_URL = "jdbc:h2:file:" + System.getProperty("user.dir")
			+ "/h2/rdf-analyse;AUTO_SERVER=TRUE;AUTO_SERVER_PORT=9090;LOB_TIMEOUT=900000";
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
	 * Executes a single SQL Query
	 * 
	 * @param con   Connection to the the database
	 * @param query is executed
	 * @throws SQLException in case of an invalid sql command
	 * 
	 */
	public static void executeQuery(Connection con, String query) throws SQLException {
		Statement stmt = con.createStatement();
		stmt.execute(query);
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
		H2Util.executeQuery(con, query);
		log.info("Finished writing time to " + column + " of file " + fileID);
	}

}
