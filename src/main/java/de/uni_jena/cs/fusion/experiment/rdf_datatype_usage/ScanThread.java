package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.NoSuchElementException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.h2.jdbcx.JdbcConnectionPool;
import org.slf4j.LoggerFactory;

import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measure.FileMeasure;

public class ScanThread extends Thread {

	private static org.slf4j.Logger log = LoggerFactory.getLogger(ScanThread.class);

	private final Object lock;

	private final CountDownLatch latch;

	private final JdbcConnectionPool pool;

	ScanThread(JdbcConnectionPool pool, CountDownLatch latch, Object lock) {
		this.pool = pool;
		this.latch = latch;
		this.lock = lock;
	}

	private FileMeasure getNextFileMeasure(Connection con) throws SQLException, NoSuchElementException {
		// if no unfinished file exist, return null, otherwise try to get one
		String leftFilesCountQuery = "SELECT COUNT(*) AS unfinished_count FROM files WHERE end_time IS NULL";
		ResultSet leftFilesCountResult = con.createStatement().executeQuery(leftFilesCountQuery);
		leftFilesCountResult.next();
		if (leftFilesCountResult.getLong("unfinished_count") == 0) {
			return null;
		} else {
			synchronized (lock) {
				Statement stmt = con.createStatement();
				String queryFilesToWorkOn = ""//
						+ "SELECT file_id, url "//
						+ "FROM files "//
						+ "WHERE start_time IS NULL "//
						// restart after a day
						+ "OR (start_time < '" + new Timestamp(System.currentTimeMillis() - 24 * 60 * 60 * 1000)
						+ "' AND end_time IS NULL) "//
						+ "ORDER BY url "//
						+ "LIMIT 1";
				try (ResultSet result = stmt.executeQuery(queryFilesToWorkOn)) {
					if (result.next()) {
						String url = result.getString("url");
						long fileId = result.getLong("file_id");
						FileMeasure fileMeasure = new FileMeasure(fileId, url, con, log, this);
						con.createStatement().execute("UPDATE files SET start_time = '"
								+ new Timestamp(System.currentTimeMillis()) + "' WHERE file_id = " + fileId);
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
				String url = null;
				// Open connection
				try (Connection con = this.pool.getConnection()) {

					FileMeasure fileMeasure = getNextFileMeasure(con);
					if (fileMeasure != null) {
						identifier = fileMeasure.getFileID();
						url = fileMeasure.getUrl();

						// changes will be committed to the database explicitly
						con.setAutoCommit(false);

						long start = System.currentTimeMillis();

						log.info("{} - Start processing file: {} ({})", new Timestamp(start), url, identifier);
						fileMeasure.startMeasurements();
						fileMeasure.writeToDatabase();
						long end = System.currentTimeMillis();
						log.info("{} - Finished processing after {} ms file: {} ({})", new Timestamp(end), end - start,
								url, identifier);
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
