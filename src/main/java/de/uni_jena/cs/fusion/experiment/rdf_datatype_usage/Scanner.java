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
package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.concurrent.CountDownLatch;
import java.util.zip.GZIPOutputStream;

import org.apache.jena.query.ARQ;
import org.apache.log4j.PropertyConfigurator;
import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.tools.Csv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

public class Scanner implements Runnable {

	private static Logger log = LoggerFactory.getLogger(Scanner.class);

	private static final String CREATE_CATEGORY_DATABASE_TABLE = ""//
			+ "CREATE TABLE IF NOT EXISTS categories ("//
			+ "  category_id IDENTITY,"//
			+ "  name VARCHAR NOT NULL"//
			+ ")";
	private static final String CREATE_FILE_DATABASE_TABLE = ""//
			+ "CREATE TABLE IF NOT EXISTS files ("//
			+ "  file_id BIGINT PRIMARY KEY AUTO_INCREMENT(1,1) NOT NULL,"//
			+ "  category_id BIGINT REFERENCES categories(category_id),"//
			+ "  url VARCHAR NOT NULL,"//
			+ "  start_time TIMESTAMP,"//
			+ "  end_time TIMESTAMP,"//
			+ "  total_number_of_lines BIGINT"//
			+ ")";
	private static final String CREATE_RESULT_DATABSE_TABLE = ""//
			+ "CREATE TABLE IF NOT EXISTS measurements ("//
			+ "  file_id BIGINT REFERENCES files(file_id),"//
			+ "  property VARCHAR NOT NULL,"//
			+ "  measurement VARCHAR NOT NULL,"//
			+ "  datatype VARCHAR NOT NULL,"//
			+ "  quantity BIGINT NOT NULL"//
			+ ")";
	private static final String CREATE_ERROR_LINE_DATABASE_TABLE = ""//
			+ "CREATE TABLE IF NOT EXISTS errors ("//
			+ "  file_id BIGINT REFERENCES files(file_id),"//
			+ "  line BIGINT NOT NULL,"//
			+ "  error_message TINYTEXT"//
			+ ")";
	private static final String EXPORT_RESULTS_QUERY = ""//
			+ "SELECT"//
			+ "  name AS CATEGORY,"//
			+ "  url AS FILE_URL,"//
			+ "  measurement,"//
			+ "  property,"//
			+ "  datatype,"//
			+ "  quantity "//
			+ "FROM measurements "//
			+ "  NATURAL JOIN files "//
			+ "  NATURAL JOIN categories";
	private static final String EXPORT_ERRORS_QUERY = ""//
			+ "SELECT"//
			+ "  name AS CATEGORY,"//
			+ "  url AS FILE_URL,"//
			+ "  line,"//
			+ "  error_message "//
			+ "FROM errors "//
			+ "  NATURAL JOIN files "//
			+ "  NATURAL JOIN categories";

	public static final String DB_URL_TEMPLATE = "jdbc:h2:file:%s/scan;AUTO_SERVER=TRUE;AUTO_SERVER_PORT=9090;LOB_TIMEOUT=900000";
	public static final String DB_USER = "user_00";
	public static final String DB_PASS = "pwd";

	public static void main(String... args) throws Exception {
		int exitCode = new CommandLine(new Scanner()).execute(args);
		System.exit(exitCode);
	}

	@Option(names = "--category", description = "Category of the files listet in --list to schedule for scanning.")
	String category;

	@Option(names = "--list", description = "URL of the list of files to schedule for scanning.")
	URL list;

	@Option(names = "--scan", description = "If set, start or continue the scan.")
	boolean scan;

	@Option(names = "--threads", description = "Number of parallel threads to use for scanning. Can be used to optimize the throughput. Default: 8")
	int threads = 8;

	@Option(names = "--results", description = "File to export the results as GZ compressed CSV.")
	File resultFile;

	@Option(names = "--errors", description = "File to export the logged errors as GZ compressed CSV.")
	File errorFile;

	@Parameters(index = "0", description = "Folder to store the internal database for scann management and results.")
	File databaseFolder;

	@Override
	public void run() {
		try {
			PropertyConfigurator.configure("src/main/resources/log4j.properties");
			// Initializing database
			JdbcConnectionPool pool = getConnectionPool();
			try (Connection con = pool.getConnection()) {
				createDatabaseTables(con);
			}

			if (category != null && list != null) {
				log.info("Scheduling file URLs for scanning");
				try (Connection con = pool.getConnection()) {
					schedulList(category, list, con);
				}
			}

			if (scan) {
				log.info("Running scan");
				scan(pool);
			}

			if (resultFile != null) {
				log.info("Exporting results to " + resultFile.getAbsolutePath());
				try (Connection con = pool.getConnection()) {
					export(resultFile, EXPORT_RESULTS_QUERY, con);
				}
			}

			if (errorFile != null) {
				log.info("Exporting errors to " + errorFile.getAbsolutePath());
				try (Connection con = pool.getConnection()) {
					export(errorFile, EXPORT_ERRORS_QUERY, con);
				}
			}

			pool.dispose();
		} catch (Throwable t) {
			log.error("Unexpected Error:\n", t);
			System.exit(1);
		}
	}

	private JdbcConnectionPool getConnectionPool() throws SQLException {
		databaseFolder.mkdirs();
		return JdbcConnectionPool.create(String.format(DB_URL_TEMPLATE, databaseFolder.getAbsolutePath()), DB_USER,
				DB_PASS);
	}

	public void export(File file, String query, Connection con)
			throws FileNotFoundException, IOException, SQLException {
		try (Statement stmt = con.createStatement();
				Writer writer = new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(file), true),
						StandardCharsets.UTF_8);) {
			file.getParentFile().mkdirs();
			try (ResultSet result = stmt.executeQuery(query)) {
				new Csv().write(writer, result);
			}
		}
	}

	/**
	 * Creates a table for the organisation of the files and one for the results of
	 * the measurements
	 * 
	 * <p>
	 * Table for file organisation:
	 * </p>
	 * <p>
	 * Primary key as identifier in other databases, URL or path where the file is
	 * stored
	 * </p>
	 * <p>
	 * Table for the results
	 * </p>
	 * <p>
	 * Contains the source file (primary key of the first database), property,
	 * measurement, quantity and the datatype
	 * </p>
	 * <p>
	 * Table for lines with errors:
	 * </p>
	 * <p>
	 * Contains the source file source file (primary key of the first database), the
	 * line number and the error message
	 * </p>
	 * 
	 * @param con Connection to the database
	 * @throws SQLException in case of an invalid sql command
	 */
	private static void createDatabaseTables(Connection con) throws SQLException {
		con.createStatement().execute(CREATE_CATEGORY_DATABASE_TABLE);
		con.createStatement().execute(CREATE_FILE_DATABASE_TABLE);
		con.createStatement().execute(CREATE_RESULT_DATABSE_TABLE);
		con.createStatement().execute(CREATE_ERROR_LINE_DATABASE_TABLE);
		con.commit();
	}

	public static void schedulList(String category, URL list, Connection con) throws SQLException, IOException {
		// add category
		PreparedStatement categoryStmt = con.prepareStatement(
				"INSERT INTO categories (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM categories WHERE NAME = ? )");
		categoryStmt.setString(1, category);
		categoryStmt.setString(2, category);
		categoryStmt.executeUpdate();

		// add file URLs from list
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(list.openStream()))) {
			String line = reader.readLine();
			PreparedStatement fileStmt = con.prepareStatement(
					"INSERT INTO files (category_id, url, start_time, end_time, total_number_of_lines) SELECT category_id, ?, null, null, null FROM categories WHERE NAME = ? ");
			fileStmt.setString(2, category);
			while (line != null) {
				fileStmt.setString(1, line);
				fileStmt.executeUpdate();
				line = reader.readLine();
			}
		}
		con.commit();
	}

	public void scan(JdbcConnectionPool pool) throws InterruptedException {
		ARQ.init();
		CountDownLatch latch = new CountDownLatch(this.threads);
		Object lock = new Object();
		long start = System.currentTimeMillis();

		for (int i = 0; i < this.threads; i++) {
			new ScanThread(pool, latch, lock).start();
		}

		latch.await();
		long end = System.currentTimeMillis();

		log.info(String.format("[%s] Scan with %d threads finished after %d ms.",
				new Timestamp(System.currentTimeMillis()), +this.threads, end - start));
	}
}
