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

						log.info("Start processing file: {} ({})", url, identifier);
						fileMeasure.startMeasurements();
						fileMeasure.writeToDatabase();
						long end = System.currentTimeMillis();
						log.info("Finished processing after {} ms file: {} ({})", end - start, url, identifier);
					} else {
						break;
					}
				} catch (NoSuchElementException e) {
					TimeUnit.HOURS.sleep(1);
				} catch (Throwable t) {
					if (identifier != null) {
						log.info("Error during work on file " + identifier);
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
