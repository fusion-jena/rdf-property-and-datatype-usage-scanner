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
package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measure;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.ScanThread;
import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils.FileIterator;
import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils.ModelUtil;

public class FileMeasure {

	/**
	 * List of all measures performed on the file
	 */
	private List<Measure> measures;

	private org.slf4j.Logger log;

	/**
	 * Iterator over the statements of the file
	 */
	private FileIterator fileIter;

	/**
	 * Connection to the database
	 */
	private Connection con;

	/**
	 * ID of the file in the file organisation database
	 */
	private Long fileID;

	private String url;

	/**
	 * Viewing a file from a database
	 * 
	 * @param fileID Identifier of the file in the database
	 * @param url    location of the file
	 * @param con    Connection to the database
	 * @param log    Logging information
	 */
	public FileMeasure(Long fileID, String url, Connection con, org.slf4j.Logger log) throws SQLException {
		this.con = con;
		this.fileID = fileID;
		initaliseMeasures();
		this.log = log;
		this.url = url;
		this.fileIter = ModelUtil.parseURLlineByLine(url, log);
	}

	/**
	 * Viewing a file from a database - multithreaded context
	 * 
	 * @param fileID Identifier of the file in the database
	 * @param url    location of the file
	 * @param con    Connection to the database
	 * @param log    Logging information
	 * @param thread Thread which is working on the file
	 */
	public FileMeasure(Long fileID, String url, Connection con, org.slf4j.Logger log, ScanThread thread)
			throws SQLException {
		this.con = con;
		this.fileID = fileID;
		initaliseMeasures();
		this.log = log;
		this.url = url;
		this.fileIter = ModelUtil.parseURLlineByLine(url, log, thread);
	}

	/**
	 * No database context
	 * 
	 * @param dataPath where to find the file, must be from the form file:///<path>
	 * @param measures which measures should be conducted
	 * @param log      logging information
	 */
	public FileMeasure(String dataPath, List<Measure> measures, org.slf4j.Logger log) {
		this.log = log;
		this.measures = measures;
		this.fileIter = ModelUtil.parseURLlineByLine(dataPath, log);
	}

	/**
	 * calls {@link ModelUtil#conductMeasurement(Measures, FileIterator, Logger)
	 * which conducts all measures on each statement of the file
	 */
	public void startMeasurements() {
		ModelUtil.conductMeasurements(measures, fileIter, log);
	}

	/**
	 * Writes the results into the respective database table after the measures have
	 * been carried out.
	 * 
	 * <p>
	 * The total number of lines, if applicable the parsing errors and the results
	 * of the measures
	 * </p>
	 * <p>
	 * Also set the end time to mark the file as processed
	 * </p>
	 * 
	 * @throws SQLException when an error occurs during writing to the database
	 */
	public void writeToDatabase() throws SQLException {
		writeNumLines();
		writeToErrorDatabaseTable();
		writeToResultDatabaseTable();
		con.createStatement().execute("UPDATE files SET end_time = '"
				+ new Timestamp(System.currentTimeMillis()) + "' WHERE file_id = " + fileID);
		// commit all the changes and new lines to the database
		con.commit();
	}

	/**
	 * writes the errors that occurred while parsing the file to the error table
	 * 
	 * @throws SQLException when an error occurs
	 */
	private void writeToErrorDatabaseTable() throws SQLException {
		Map<Long, List<String>> errors = fileIter.getErrors();
		try (PreparedStatement ps = con.prepareStatement("INSERT INTO errors VALUES (?,?,?)");) {
			ps.setLong(1, fileID);
			for (Long line : errors.keySet()) {
				ps.setLong(2, line);
				for (String errorMsg : errors.get(line)) {
					ps.setString(3, errorMsg);
					ps.execute();
				}
			}
		}
	}

	/**
	 * writes the results of the different measurements to the result table
	 * 
	 * @throws SQLException when an error occurs
	 */
	private void writeToResultDatabaseTable() throws SQLException {
		try (PreparedStatement ps = con.prepareStatement("INSERT INTO measurements VALUES (?,?,?,?,?)");) {
			ps.setLong(1, fileID);
			for (Measure measure : measures) {
				for (MeasureResult result : measure.writeToDatabase()) {
					ps.setString(2, result.getProperty());
					ps.setString(3, result.getMeasure());
					ps.setString(4, result.getDatatype());
					ps.setLong(5, result.getQuantity());
					ps.execute();
				}
			}
		}
	}

	/**
	 * writes the total number of lines in the file organization table
	 * 
	 * @throws SQLException when an error occurs
	 */
	private void writeNumLines() throws SQLException {
		con.createStatement().execute(
				"UPDATE files SET total_number_of_lines = " + fileIter.getNumLines() + " WHERE file_id = " + fileID);
	}

	/**
	 * Configure, which measurements will be conducted
	 */
	private void initaliseMeasures() {
		measures = new ArrayList<Measure>();
		measures.add(new UnpreciseRepresentableInDouble());
		measures.add(new UnpreciseRepresentableInFloat());
		measures.add(new UsedAsDatatype());
		measures.add(new UsedAsPropertyRange());
		measures.add(new ValidDateNotation());
		measures.add(new ValidDateTimeNotation());
		measures.add(new ValidDecimalNotation());
		measures.add(new ValidExponentialNotation());
		measures.add(new ValidInfOrNaNNotation());
		measures.add(new ValidIntegerNotation());
		measures.add(new ValidTimeNotation());
		measures.add(new ValidTrueOrFalseNotation());
		measures.add(new ValidZeroOrOneNotation());
	}

	public long getFileID() {
		return fileID;
	}

	public String getUrl() {
		return url;
	}

}
