package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measure;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils.FileIterator;
import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils.H2Util;
import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils.ModelUtil;

public class FileMeasure {

	/**
	 * List of all measures performed on the file
	 */
	private List<Measure<?, ?>> measures;

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
		this.dataPath = url;
		this.fileIter = ModelUtil.parseURLlineByLine(url, log);
	}

	/**
	 * No database context
	 * 
	 * @param dataPath     where to find the file, must be from the form
	 *                     file:///<path>
	 * @param measures which measures should be conducted
	 * @param log          logging information
	 */
	public FileMeasure(String dataPath, List<Measure<?, ?>> measures, org.slf4j.Logger log) {
		this.dataPath = dataPath;
		this.log = log;
		this.measures = measures;
		this.fileIter = ModelUtil.parseURLlineByLine(dataPath, log);
	}

	/**
	 * calls
	 * {@link ModelUtil#conductMeasurement(Measures, FileIterator, Logger)
	 * which conducts all measures on each statement of the file
	 */
	public void startMeasurements() {
		long start = System.currentTimeMillis();
		log.info("Start measures on file #" + fileID);
		ModelUtil.conductMeasurements(measures, fileIter, log);
		long end = System.currentTimeMillis();
		log.info("Finished measures on file #" + fileID + " after " + (end - start) + "ms");
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
		H2Util.writeTime(con, H2Util.END, fileID, log);
	}

	/**
	 * writes the errors that occurred while parsing the file to the error table
	 * 
	 * @throws SQLException when an error occurs
	 */
	private void writeToErrorDatabaseTable() throws SQLException {
		log.info("Start writing errors");
		HashMap<Long, String> errors = fileIter.getErrors();
		for (Long line : errors.keySet()) {
			String errorMsg = errors.get(line).replace("'", "''");
			String query = "INSERT into " + H2Util.ERROR_DATABASE_TABLE + " values (" + fileID + ", " + line + ", '"
					+ errorMsg + "')";
			H2Util.executeAndUpdate(con, query);
		}
		log.info("Finished writing errors");
	}

	/**
	 * writes the results of the different measurements to the result table
	 * 
	 * @throws SQLException when an error occurs 
	 */
	private void writeToResultDatabaseTable() throws SQLException {
		log.info("Start writing results from file " + fileID);
		for (Measure<?, ?> measure : measures) {
			List<String> queries = measure.writeToDatabase();
			String queryBeginning = "INSERT into " + H2Util.RESULT_DATABASE_TABLE + " values (" + fileID + ", ";
			String queryEnd = ")";
			for (String queryValue : queries) {
				String query = queryBeginning + queryValue + queryEnd;
				H2Util.executeAndUpdate(con, query);
			}
		}
		log.info("Finished writing results from file " + fileID);
	}

	/**
	 * writes the total number of lines in the file organisation table
	 * 
	 * @throws SQLException when an error occurs 
	 */
	private void writeNumLines() throws SQLException {
		log.info("Start writing total number of lines of file " + fileID);
		String query = "UPDATE " + H2Util.FILE_DATABASE_TABLE + " SET TOTAL_NUMBER_OF_LINES = " + fileIter.getNumLines()
				+ " WHERE FILE_ID = " + fileID;
		H2Util.executeAndUpdate(con, query);
		log.info("Finished writing total number of lines of file " + fileID);
	}

	/**
	 * Configure, which measurements will be conducted
	 */
	private void initaliseMeasures() {
		measures = new ArrayList<Measure<?, ?>>();
		measures.add(new CouldBeFloat());
		measures.add(new CouldBeDouble());
		measures.add(new CouldBeTemporal());
		measures.add(new ShouldBeDecimal());
		measures.add(new CouldBeDoubleOrFloatNotDecimal());
		measures.add(new UsedAsPropertyRange());
		measures.add(new CouldBeBoolean());
		measures.add(new CouldBeInteger());
	}

	public long getFileID() {
		return fileID;
	}

}
