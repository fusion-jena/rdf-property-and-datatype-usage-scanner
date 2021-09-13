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

public class H2DoMeasurements extends Thread {

	private static org.slf4j.Logger log;

	public static void main(String[] args) {

		System.setProperty("fName", StringUtil.createStorageFile("log"));

		PropertyConfigurator.configure("src/main/resources/log4j.properties");
		log = org.slf4j.LoggerFactory.getLogger("H2DoMeasurements");
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

		H2DoMeasurements thread1 = new H2DoMeasurements();
		thread1.start();
		try {
			sleep(50);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
		H2DoMeasurements thread2 = new H2DoMeasurements();
		thread2.start();
		try {
			sleep(50);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
		H2DoMeasurements thread3 = new H2DoMeasurements();
		thread3.start();
		try {
			sleep(50);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
		H2DoMeasurements thread4 = new H2DoMeasurements();
		thread4.start();

		server.stop();
		log.info("Server stopped");
	}

	@Override
	public void run() {
		// Open connection
		log.info("Connecting Thread " + this.getId() + " to database");
		Connection con = null;
		try {
			con = DriverManager.getConnection(H2Util.DB_URL, H2Util.USER, H2Util.PASS);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
			System.exit(1);
		}

		Statement stmt = null;
		long identifier = -1;
		try {
			stmt = con.createStatement();
			String queryFilesToWorkOn = "SELECT FILE_ID, URL FROM " + H2Util.FILE_DATABASE + " WHERE (START_TIME < '"
					+ new Timestamp(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000) // restart after a week
					+ "' AND END_TIME IS NULL) OR START_TIME IS NULL LIMIT 1";
			ResultSet result = stmt.executeQuery(queryFilesToWorkOn);

			while (result.next()) {
				identifier = result.getLong("FILE_ID");
				String url = result.getString("URL");
				//Ensure, to work on each file only once
				String checkTime = "SELECT " + H2Util.START + " FROM " + H2Util.FILE_DATABASE + " WHERE FILE_ID = " + identifier; 
				ResultSet resultTime = stmt.executeQuery(checkTime);
				resultTime.next();
				Timestamp time = resultTime.getTimestamp(H2Util.START);
				if(time == null || time.before(new Timestamp(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000))) {
					H2Util.writeTime(con, H2Util.START, identifier, log);
					log.info(new Timestamp(System.currentTimeMillis()) + " - Thread " + this.getId()
					+ " - Start processing file: " + identifier);
					FileMeasurement fileMeasurement = new FileMeasurement(identifier, url, con, log);
					fileMeasurement.startMeasurement();
					fileMeasurement.writeToDatabase();
					log.info(new Timestamp(System.currentTimeMillis()) + " - Thread " + this.getId()
					+ " - Finished processing file: " + identifier);
				}
				// Get the next file
				result.close();
				result = stmt.executeQuery(queryFilesToWorkOn);
			}
		} catch (SQLException e) {
			log.info("Error when working on file " + identifier);
			// restart thread
			this.run();
		}
		try {
			con.close();
		} catch (SQLException e) {
			log.error(e.getMessage());
			System.exit(1);
		}
		log.info("Connection from thread " + this.getId() + " to database closed");
	}

}
