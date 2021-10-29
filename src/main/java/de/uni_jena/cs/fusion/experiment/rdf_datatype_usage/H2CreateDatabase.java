package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;
import org.h2.tools.Server;

import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils.H2Util;

/**
 * Initialisation at the beginning.
 *
 * <p>
 * Creates three databases (general file overview, results, errors)
 * </p>
 * <p>
 * Must be executed before H2DoMeasurements
 * </p>
 */
public class H2CreateDatabase {

	// Queries to generate the database
	// Caution: overwrites existing databases!
	private static final String CREATE_CATEGORY_DATABASE_TABLE = "CREATE TABLE " + H2Util.CATEGORY_DATABASE_TABLE
			+ " (CATEGORY_ID INT PRIMARY KEY AUTO_INCREMENT(1,1) NOT NULL, NAME VARCHAR(25) NOT NULL)";
	private static final String CREATE_FILE_DATABASE_TABLE = "CREATE TABLE " + H2Util.FILE_DATABASE_TABLE
			+ " (FILE_ID BIGINT PRIMARY KEY AUTO_INCREMENT(1,1) NOT NULL, CATEGORY_ID INT REFERENCES "
			+ H2Util.CATEGORY_DATABASE_TABLE
			+ "(CATEGORY_ID), URL VARCHAR(2048) NOT NULL, START_TIME TIMESTAMP, END_TIME TIMESTAMP , TOTAL_NUMBER_OF_LINES BIGINT )";
	private static final String CREATE_RESULT_DATABSE_TABLE = "CREATE TABLE " + H2Util.RESULT_DATABASE_TABLE
			+ " (FILE_ID BIGINT REFERENCES " + H2Util.FILE_DATABASE_TABLE
			+ "(FILE_ID), PROPERTY VARCHAR(2048) NOT NULL, MEASUREMENT VARCHAR(40) NOT NULL, DATATYPE VARCHAR(2048) NOT NULL, QUANTITY BIGINT NOT NULL)";
	private static final String CREATE_ERROR_LINE_DATABASE_TABLE = "CREATE TABLE " + H2Util.ERROR_DATABASE_TABLE
			+ " (FILE_ID BIGINT REFERENCES " + H2Util.FILE_DATABASE_TABLE
			+ "(FILE_ID), LINE BIGINT NOT NULL, ERROR_MESSAGE TINYTEXT)"; // TODO tinytext ausreichend?

	private static org.slf4j.Logger log;

	/**
	 * List of files which store the links to the .nq files
	 */
	private static List<String> listFiles = Arrays.asList(// number of files in the document
			"html-embedded-jsonld", // 5273
			"html-mf-adr", // 19
			"html-mf-geo", // 4
			"html-mf-hcalendar", // 13
			"html-mf-hcard", // 2316
			"html-mf-hlisting", // 4
			"html-mf-hrecipe", // 4
			"html-mf-hresume", // 1
			"html-mf-hreview", // 15
			"html-mf-species", // 1
			"html-mf-xfn", // 46
			"html-microdata", // 8480
			"html-rdfa" // 5167
	);

	public static void main(String[] args) {
	
		PropertyConfigurator.configure("src/main/resources/log4j.properties");
		log = org.slf4j.LoggerFactory.getLogger("H2CreateDatabase");

		try {
			// Register for JDCB driver
			Class.forName(H2Util.JDBC_DRIVER);

			Server server = Server.createTcpServer().start();
			log.info("Starting server");
			server.start();
			// Open connection
			log.info("Connecting to database");
			Connection con = DriverManager.getConnection(H2Util.DB_URL, H2Util.USER, H2Util.PASS);

			createDatabaseTables(con);
			
			fillCategoryDatabaseTable(con);

			fillFileOrganisationDatabaseTable(con);
			
			con.close();
			log.info("Connection closed");
			server.stop();
			log.info("Server stopped");

		} catch (SQLException | ClassNotFoundException ex) {
			log.error(ex.getMessage());
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
		log.info("Create table " + H2Util.CATEGORY_DATABASE_TABLE);
		H2Util.executeQuery(con, CREATE_CATEGORY_DATABASE_TABLE);
		log.info("Table " + H2Util.CATEGORY_DATABASE_TABLE + " created");

		log.info("Create table " + H2Util.FILE_DATABASE_TABLE);
		H2Util.executeQuery(con, CREATE_FILE_DATABASE_TABLE);
		log.info("Table " + H2Util.FILE_DATABASE_TABLE + " created");

		log.info("Create table " + H2Util.RESULT_DATABASE_TABLE);
		H2Util.executeQuery(con, CREATE_RESULT_DATABSE_TABLE);
		log.info("Table " + H2Util.RESULT_DATABASE_TABLE + " created");

		log.info("Create table " + H2Util.ERROR_DATABASE_TABLE);
		H2Util.executeQuery(con, CREATE_ERROR_LINE_DATABASE_TABLE);
		log.info("Table " + H2Util.ERROR_DATABASE_TABLE + " created");
	}

	/**
	 * Each .list file represents a category that is assigned an id
	 * 
	 * @param con connection to the database
	 * @throws SQLException in case of an invalid SQL command
	 */
	private static void fillCategoryDatabaseTable(Connection con) throws SQLException {
		log.info("Fill " + H2Util.CATEGORY_DATABASE_TABLE);
		for (int idx = 0; idx < listFiles.size(); idx++) {
			String query = "INSERT into " + H2Util.CATEGORY_DATABASE_TABLE + " values (" + (idx + 1) + ", '"
					+ listFiles.get(idx) + "')";
			H2Util.executeQuery(con, query);
		}
		log.info(H2Util.CATEGORY_DATABASE_TABLE + " filled");
	}

	/**
	 * Parses the *.list files line by line and inserts the URL into the database
	 * 
	 * <p>
	 * Files from
	 * {@link http://webdatacommons.org/structureddata/2020-12/stats/how_to_get_the_data.html}
	 * </p>
	 * <p>
	 * Takes a file an parses it line by line.
	 * </p>
	 * <p>
	 * Each line is inserted into {@link FILE_DATABASE}
	 * </p>
	 * 
	 * @param con Connection to the database
	 * @throws SQLException in case of an invalid SQL command
	 */
	private static void fillFileOrganisationDatabaseTable(Connection con) throws SQLException {
		String path = "src/main/resources/list_files/";
		String fileExtension = ".list";

		for (int idx = 0; idx < listFiles.size(); idx++) {
			String file = listFiles.get(idx);
			log.info("Start reading lines from file " + path + file + fileExtension);
			BufferedReader reader;
			try {
				reader = new BufferedReader(new FileReader(path + file + fileExtension));
				String line = reader.readLine();
				try(PreparedStatement ps = con.prepareStatement("INSERT into " + H2Util.FILE_DATABASE_TABLE + " values (default, " + (idx + 1)
						+ ", ? , null, null, null)");){
					while (line != null) {
						ps.setString(1, line);
						ps.executeUpdate();
						line = reader.readLine();
					}
				}
			} catch (IOException e) {
				log.error(e.getMessage());
			}
			log.info("Finished reading lines from file " + path + file + fileExtension);
		}

	}

}
