package main.java.measurements;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.java.utils.FileIterator;
import main.java.utils.H2Util;
import main.java.utils.ModelUtil;

public class FileMeasurement {

	/**
	 * List of all measurements performed on the file
	 */
	private List<Measurement<?, ?>> measurements;

	private String dataPath;

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

	//Column names
	private final String START = "START_TIME";
	private final String END = "END_TIME";

	/**
	 * Viewing a file from a database
	 * 
	 * @param fileID   Identifier of the file in the database
	 * @param url location of the file
	 * @param con      Connection to the database
	 * @param log      Logging information
	 */
	public FileMeasurement(Long fileID, String url, Connection con, org.slf4j.Logger log) throws SQLException {
		this.con = con;
		this.fileID = fileID;
		initaliseMeasurements();
		this.log = log;
		this.dataPath = url;
		writeTime(START);
		this.fileIter = ModelUtil.parseURLlineByLine(url, log);
	}
	
	/**
	 * No database context 
	 * 
	 * @param dataPath where to find the file, must be from the form file:///<path>
	 * @param measurements which measurements should be conducted
	 * @param log logging information
	 */
	public FileMeasurement(String dataPath, List<Measurement<?, ?>> measurements, org.slf4j.Logger log) {
		this.dataPath = dataPath;
		this.log = log;
		this.measurements = measurements;
		this.fileIter = ModelUtil.parseURLlineByLine(dataPath, log);
	}
	
	/**
	 * calls {@link ModelUtil#conductMeasurements(Measurements, FileIterator, Logger) 
	 * which conducts all measurements on each statement of the file
	 */
	public void startMeasurement() {
		long start = System.currentTimeMillis();
		log.info("Start measurements on file #" + fileID);
		ModelUtil.conductMeasurements(measurements, fileIter, log);
		long end = System.currentTimeMillis();
		log.info("Finished measurements on file #" + fileID + " after " + (end - start) + "ms");
	}

	/**
	 * Write the results to each database after the measurements are conducted
	 * 
	 * <p>
	 * The total number of lines, if applicable the parsing errors and the results 
	 * of the measurements
	 * </p><p>
	 * Also set the end time to mark the file as processed
	 * </p>
	 * @throws SQLException when an error occurs during writing to the database
	 */
	public void writeToDatabase() throws SQLException {
		writeNumLines();
		writeToErrorDatabase();
		writeToResultDatabase();
		writeTime(END);
	}

	/**
	 * writes the errors that occurred while parsing the file to the database
	 * 
	 * @throws SQLException when an error occurs during writing to the database
	 */
	private void writeToErrorDatabase() throws SQLException {
		log.info("Start writing errors");
		HashMap<Long, String> errors = fileIter.getErrors();
		for (Long line : errors.keySet()) {
			String errorMsg = errors.get(line).replace("'", "''");
			String query = "INSERT into " + H2Util.ERROR_DATABASE + " values (" + fileID + ", " + line + ", '"
					+ errorMsg + "')";
			H2Util.executeAndUpdate(con, query);
		}
		log.info("Finished writing errors");
	}
	
	/**
	 * writes the results of the different measurements to the database 
	 * 
	 * @throws SQLException when an error occurs during writing to the database
	 */
	private void writeToResultDatabase() throws SQLException {
		log.info("Start writing results from file " + fileID);
		for (Measurement<?, ?> measurment : measurements) {
			List<String> queries = measurment.writeToDatabase();
			String queryBeginning = "INSERT into " + H2Util.RESULT_DATABASE + " values (" + fileID + ", ";
			String queryEnd = ")";
			for (String queryValue : queries) {
				String query = queryBeginning + queryValue + queryEnd;
				H2Util.executeAndUpdate(con, query);
			}
		}
		log.info("Finished writing results from file " + fileID);
	}

	/**
	 * writes the current time in the given column 
	 * 
	 * @param column where to write the current time, start or end 
	 * 
	 * @throws SQLException when an error occurs during writing to the database
	 */
	private void writeTime(String column) throws SQLException {
		log.info("Start writing time to " + column + " of file " + fileID);
		String query = "UPDATE " + H2Util.FILE_DATABASE + " SET " + column + " = '" + new Timestamp(System.currentTimeMillis()) + "' WHERE FILE_ID = " + fileID;
		H2Util.executeAndUpdate(con, query);
		log.info("Finished writing time to " + column + " of file " + fileID);
	}
	
	/**
	 * writes the total number of lines in the file organisation database
	 * 
	 * @throws SQLException when an error occurs during writing to the database
	 */
	private void writeNumLines() throws SQLException{
		log.info("Start writing total number of lines of file " + fileID);
		String query = "UPDATE " + H2Util.FILE_DATABASE + " SET TOTAL_NUMBER_OF_LINES = " + fileIter.getNumLines() + " WHERE FILE_ID = " + fileID;
		H2Util.executeAndUpdate(con, query);
		log.info("Finished writing total number of lines of file " + fileID);
	}
	
	/**
	 * Configure, which measurements will be conducted
	 */
	private void initaliseMeasurements() {
		measurements = new ArrayList<Measurement<?, ?>>();
		measurements.add(new ShouldBeFloat());
		measurements.add(new ShouldBeDouble());
		measurements.add(new ShouldBeDateTime());
		measurements.add(new ShouldBeDecimal());
		measurements.add(new ShouldBeDoubleOrFloatNotDecimal());
		measurements.add(new PropertyHasRange());
	}

}
