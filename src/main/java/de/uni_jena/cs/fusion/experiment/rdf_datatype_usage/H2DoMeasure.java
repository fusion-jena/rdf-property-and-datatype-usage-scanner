package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.jena.query.ARQ;
import org.apache.log4j.PropertyConfigurator;
import org.h2.tools.Server;

import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measure.FileMeasure;
import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils.H2Util;

public class H2DoMeasure extends Thread {

	private static org.slf4j.Logger log;

	private static final Object lock = new Object();

	private static CountDownLatch latch;
	private static int numThreads;

	public static void main(String[] args) {
		numThreads = 8;
		ARQ.init();
		latch = new CountDownLatch(numThreads);

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
			log.info(new Timestamp(System.currentTimeMillis()) + " - Starting server");
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
		} catch (Throwable t) {
			log.error("Unexpected Error occured:");
			log.error(t.getMessage());
			System.exit(1);
		}
		server.stop();
		log.info(new Timestamp(System.currentTimeMillis()) + " - Server stopped");
		duration = end - start;
		log.warn(new Timestamp(System.currentTimeMillis()) + " used " + numThreads + " threads, took " + duration
				+ "ms to finnish");
		System.exit(0);
	}

	private FileMeasure getNextFileMeasure(Connection con) throws SQLException, NoSuchElementException {
		// if no unfinished file exist, return null, otherwise try to get one
		String leftFilesCountQuery = ""//
				+ "SELECT COUNT(*) AS UNFINISHED_COUNT "//
				+ "FROM " + H2Util.FILE_DATABASE_TABLE + " "//
				+ "WHERE END_TIME IS NULL";
		ResultSet leftFilesCountResult = con.createStatement().executeQuery(leftFilesCountQuery);
		leftFilesCountResult.next();
		if (leftFilesCountResult.getLong("UNFINISHED_COUNT") == 0) {
			return null;
		} else {
			synchronized (lock) {
				Statement stmt = con.createStatement();
				String queryFilesToWorkOn = ""//
						+ "SELECT FILE_ID, URL "//
						+ "FROM " + H2Util.FILE_DATABASE_TABLE + " "//
						+ "WHERE START_TIME IS NULL "//
						// restart after a day
						+ "OR (START_TIME < '" + new Timestamp(System.currentTimeMillis() - 24 * 60 * 60 * 1000)
						+ "' AND END_TIME IS NULL) "//
						+ "LIMIT 1";
				try (ResultSet result = stmt.executeQuery(queryFilesToWorkOn)) {
					if (result.next()) {
						FileMeasure fileMeasure = new FileMeasure(result.getLong("FILE_ID"), result.getString("URL"),
								con, log, this);
						H2Util.writeTime(con, H2Util.START, fileMeasure.getFileID(), log);
						return fileMeasure;
					} else {
						throw new NoSuchElementException();
					}
				}
			}
		}
	}

	@Override
	public void run() {
		try {
			while (true) {
				Long identifier = null;
				// Open connection
				try (Connection con = DriverManager.getConnection(H2Util.DB_URL, H2Util.USER, H2Util.PASS)) {

					FileMeasure fileMeasure = getNextFileMeasure(con);
					if (fileMeasure != null) {
						identifier = fileMeasure.getFileID();

						// changes will be committed to the database explicitly
						con.setAutoCommit(false);

						log.info("{} - Thread {} - Start processing file: {}",
								new Timestamp(System.currentTimeMillis()), this.getId(), identifier);
						fileMeasure.startMeasurements();
						fileMeasure.writeToDatabase();
						log.info("{} - Thread {} - Finished processing file: {}",
								new Timestamp(System.currentTimeMillis()), this.getId(), identifier);
					} else {
						break;
					}
				} catch (NoSuchElementException e) {
					TimeUnit.HOURS.sleep(1);
				} catch (Throwable t) {
					if (identifier != null) {
						log.info(new Timestamp(System.currentTimeMillis()) + " - Error during work on file "
								+ identifier);
					}
					log.error(t.getMessage());

					TimeUnit.MINUTES.sleep(1); // wait one minutes to not flood the log
				}
			}
		} catch (InterruptedException ie) {
			log.error(ie.getMessage());
		}
		latch.countDown();
	}
}
