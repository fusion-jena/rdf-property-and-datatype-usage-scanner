package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import org.apache.log4j.PropertyConfigurator;
import org.h2.tools.Server;

import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measurements.FileMeasurement;
import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils.H2Util;
import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils.StringUtil;

public class H2DoMeasurements {

	private static org.slf4j.Logger log;

	public static void main(String[] args) {

		System.setProperty("fName", StringUtil.createStorageFile("log"));

		PropertyConfigurator.configure("src/main/resources/log4j.properties");
		log = org.slf4j.LoggerFactory.getLogger("H2DoMeasurements");

		try {
			// Register for JDCB driver
			Class.forName(H2Util.JDBC_DRIVER);

			Server server = Server.createTcpServer().start();
			log.info("Starting server");
			server.start();
			// Open connection
			log.info("Connecting to database");
			Connection con = DriverManager.getConnection(H2Util.DB_URL, H2Util.USER, H2Util.PASS);

			Statement stmt = con.createStatement();
			//TODO parallelisierung
			// TODO Anpassen um ein oder START TIME vor mehr als 7 Tagen und Endzeit ist
			// Null
			String queryAbzuarbeitendeDateien = "SELECT FILE_ID, URL from " + H2Util.FILE_DATABASE
					+ " WHERE START_TIME is NULL LIMIT 20";

			ResultSet result = stmt.executeQuery(queryAbzuarbeitendeDateien);
			Long startTime = System.currentTimeMillis();
			log.info("Starting Time: " + new Timestamp(startTime));
			while (result.next()) { // Holen der Spalteneintraege der jeweiligen Zeile
				long identifier = result.getLong("FILE_ID");
				String url = result.getString("URL");
				log.info(new Timestamp(System.currentTimeMillis()) + " Start processing file: " + identifier);
				FileMeasurement f = new FileMeasurement(identifier, url, con, log);
				f.startMeasurement();
				f.writeToDatabase();
				log.info(new Timestamp(System.currentTimeMillis()) + " Finished processing file: " + identifier);
			}
			Long endTime = System.currentTimeMillis();
			log.info("Ending Time: " + new Timestamp(endTime));
			log.info("Total time: " + ((endTime - startTime) / 1000.0 / 60) + " min");
			con.close();
			log.info("Connection closed");
			server.stop();
			log.info("Server stopped");

		} catch (SQLException | ClassNotFoundException e) {
			log.error(e.getMessage());
			System.exit(1);
		}
	}

}
