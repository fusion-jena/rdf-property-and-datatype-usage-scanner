package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.PropertyConfigurator;
import org.h2.tools.Server;

import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.exceptions.NoItemException;
import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measure.FileMeasure;
import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils.H2Util;
import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils.StringUtil;

public class H2DoMeasure extends Thread {

	private static org.slf4j.Logger log;
	
	private static final Object lock = new Object();
	
	private static CountDownLatch latch;
	private static int numThreads;

	public static void main(String[] args) {
		numThreads = 8;
		latch = new CountDownLatch(numThreads);
		System.setProperty("fName", StringUtil.createStorageFile("log"));

		PropertyConfigurator.configure("src/main/resources/log4j.properties");
		log = org.slf4j.LoggerFactory.getLogger("H2DoMeasurements");
		Server server = null;
		long start = 0L;
		long end = 0L;
		long duration = 0L;
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
		start = System.currentTimeMillis();
		List<H2DoMeasure> threads = new ArrayList<H2DoMeasure>();
		for (int i = 0; i < numThreads; i++) {
			threads.add(new H2DoMeasure());
			threads.get(i).start();
		}
		
		try {
			latch.await();
			end = System.currentTimeMillis();
		} catch (InterruptedException e) {
			log.error(e.getMessage());
		}
		server.stop();
		log.info("Server stopped");
		duration = end - start;
		log.warn(new Timestamp(System.currentTimeMillis()) + " used " + numThreads + " threads, took " + duration +"ms to finnish");
	}
	
	private FileMeasure getNextFileMeasure (Connection con) throws NoItemException {
		synchronized (lock) {
			FileMeasure fileMeasure = null;
			try {
				Statement stmt = con.createStatement();
				String queryFilesToWorkOn = "SELECT FILE_ID, URL FROM " + H2Util.FILE_DATABASE_TABLE + " WHERE (START_TIME < '"
						+ new Timestamp(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000) // restart after a week
						+ "' AND END_TIME IS NULL) OR START_TIME IS NULL LIMIT 1";
				ResultSet result = stmt.executeQuery(queryFilesToWorkOn);
				if(!result.next()) {
					log.info("No data available");
					return null;
				}
				
				fileMeasure = new FileMeasure(result.getLong("FILE_ID"), result.getString("URL"), con, log);
				result.close();

				H2Util.writeTime(con, H2Util.START, fileMeasure.getFileID(), log);
			} catch (SQLException e) {
				throw new NoItemException(e.getMessage());
			}

			return fileMeasure;
		}
	}

	@Override
	public void run() {
		// Open connection
		log.info("Connecting Thread " + this.getId() + " to database");
		Connection con = null;
		try {
			con = DriverManager.getConnection(H2Util.DB_URL, H2Util.USER, H2Util.PASS);
		} catch (SQLException e) {
			log.error(e.getMessage());
			System.exit(1);
		}

		long identifier = -1;
		try {
			FileMeasure fileMeasure = getNextFileMeasure(con);
			while (fileMeasure != null) {
				identifier = fileMeasure.getFileID();

				log.info(new Timestamp(System.currentTimeMillis()) + " - Thread " + this.getId()
						+ " - Start processing file: " + identifier);
				fileMeasure.startMeasurements();
				fileMeasure.writeToDatabase();
				log.info(new Timestamp(System.currentTimeMillis()) + " - Thread " + this.getId()
						+ " - Finished processing file: " + identifier);
				// Get the next file
				fileMeasure = getNextFileMeasure(con);
			}
		} catch (SQLException e) {
			log.info("Error when working on file " + identifier);
			// restart thread
			this.run();
		} catch (NoItemException e) {
			log.error("Cannot get next file:" + e.getMessage());			
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
		latch.countDown();
	}

}
