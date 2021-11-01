package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import org.apache.log4j.PropertyConfigurator;
import org.h2.tools.Csv;
import org.h2.tools.Server;

import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils.H2Util;

/**
 * After the measure to export the database as csv files
 */
public class H2CreateCSVFiles {

	private static org.slf4j.Logger log;

	private static List<String> queries = Arrays.asList(//
			// category
			"SELECT * FROM " + H2Util.CATEGORY_DATABASE_TABLE,
			// file organisation
			"SELECT * FROM " + H2Util.FILE_DATABASE_TABLE,
			// result
			"SELECT * FROM " + H2Util.RESULT_DATABASE_TABLE,
			// error
			"SELECT * FROM " + H2Util.ERROR_DATABASE_TABLE);

	private static List<String> fileNames = Arrays.asList(//
			"categories", "fileOrganisation", "result", "error");

	private static String path = "csv_results/";
	private static String fileExtension = ".csv";

	public static void main(String[] args) {
		PropertyConfigurator.configure("src/main/resources/log4j.properties");
		log = org.slf4j.LoggerFactory.getLogger("H2CreateCSVFiles");

		Server server = null;

		// Register for JDCB driver
		try {
			Class.forName(H2Util.JDBC_DRIVER);
			server = Server.createTcpServer().start();
			log.info("Starting server");
			server.start();
		} catch (ClassNotFoundException | SQLException e) {
			log.error(e.getMessage());
			System.exit(1);
		}

		Connection con = null;
		log.info("Start connection to the database");
		try {
			con = DriverManager.getConnection(H2Util.DB_URL, H2Util.USER, H2Util.PASS);
		} catch (SQLException e) {
			log.error(e.getMessage());
			System.exit(1);
		}

		try {
			Statement stmt = con.createStatement();
			for (int i = 0; i < queries.size(); i++) {
				log.info("Exporting " + fileNames.get(i) + " as csv file");
				ResultSet result = stmt.executeQuery(queries.get(i));
				try (Writer writer = new OutputStreamWriter(
						new GZIPOutputStream(new FileOutputStream(path + fileNames.get(i) + fileExtension + ".gz")),
						StandardCharsets.UTF_8)) {
					new Csv().write(writer, result);
				} catch (IOException e) {
					log.error(e.getMessage());
					System.exit(1);
				}
			}
		} catch (SQLException e) {
			log.error(e.getMessage());
			System.exit(1);
		}

		log.info("Close connection");
		try {
			con.close();
		} catch (SQLException e) {
			log.error(e.getMessage());
			System.exit(1);
		}

		server.stop();
		log.info("Server stopped");
	}

}
