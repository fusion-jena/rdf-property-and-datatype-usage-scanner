package main.java.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFLanguages;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.riot.RDFParserBuilder;
import org.apache.jena.riot.RiotException;
import org.apache.jena.riot.system.ErrorHandlerFactory;

import main.java.measurements.Measurement;

/**
 * Functions for different operations (parsing, analysing) a model
 */
public abstract class ModelUtil {

	/**
	 * Generates a model by parsing the document line by line.
	 * 
	 * Every line of the document is parsed individually.
	 * </p>
	 * In case of an error while parsing a line, this line will be skipped. The
	 * subsequent lines are still considered without any effect.
	 * 
	 * @param dataPath document that will be parsed
	 * @param log      for logging errors that are detected while parsing the
	 *                 document
	 * @return List which contains all the statements of the document
	 */
	public static List<Statement> parseLineByLine(String dataPath, org.slf4j.Logger log) {
		// will contain the tuple in the end
		List<Statement> allStatements = new ArrayList<Statement>();
		long lineNumber = 0;
		long numError = 0;
		long startParse = System.currentTimeMillis();
		try {// to read the file
			InputStream stream = new FileInputStream(dataPath);
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			String line = reader.readLine();// get first line

			log.debug("Starting to parse the file line by line");

			while (line != null) {// iterate over the lines
				lineNumber++;
				try {
					// Add all the statements from the current line
					allStatements.addAll(createStatementsFromLine(line));
				} catch (RiotException e) {
					// In case of an error while parsing the line, log an error instead of adding
					// the tuple to the model
					// the error message must be updated, the line number is always 1 because the
					// parser get's only one line to parse
					log.warn(e.getMessage().replace("line: 1", "line: " + lineNumber));
					numError++;
				}
				line = reader.readLine();// get next line
			}
			log.debug("File parsed");

			reader.close();
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
			System.exit(1);
		} catch (IOException e) {
			log.error(e.getMessage());
			System.exit(1);
		}
		long endParse = System.currentTimeMillis();
		log.info("Parsing line by line: " + (endParse - startParse) + " ms");
		log.info("Number of lines in the document: " + lineNumber);
		log.info("Number of lines with an error: " + numError);
		return allStatements;
	}

	/**
	 * Returns the statement of a single line
	 * 
	 * Parse a String, create a model of the created data set and return the contained statement  
	 * <p>
	 * 
	 * @param line String to parse
	 * @return the statement of the parsed line as a list (listStatements of model)
	 * @throws RiotException exception while parsing the line
	 */
	public static List<Statement> createStatementsFromLine(String line) throws RiotException {
		// contains all the statements of the line
		List<Statement> allStatements = new ArrayList<Statement>();

		Dataset dataSet = DatasetFactory.create();
		// Try to parse the current line into the dataset
		RDFParser.fromString(line).lang(Lang.NQUADS).errorHandler(ErrorHandlerFactory.errorHandlerStrictNoLogging)
				.parse(dataSet);
		// If the parsing was successful, add the corresponding tuple to the list
		StmtIterator stmtIt = dataSet.getUnionModel().listStatements();
		while (stmtIt.hasNext()) {
			allStatements.add(stmtIt.next());
		}

		return allStatements;
	}

	/**
	 * Conducts the measurements that are contained in the list for each statement
	 * that is contained in the model
	 * 
	 * @param measurements  List of classes for each measurement that will be
	 *                      conducted
	 * @param allStatements Model whose statements will be examined
	 * @param log           Logging of information
	 */
	public static void conductMeasurements(List<Measurement<?, ?>> measurements, List<Statement> allStatements,
			org.slf4j.Logger log) {
		Iterator<Statement> iter = allStatements.iterator();
		long startMeasurement = System.currentTimeMillis();
		log.debug("Starting the measurements");

		while (iter.hasNext()) {
			Statement stmt = iter.next();

			RDFNode subject = stmt.getSubject();
			Property property = stmt.getPredicate();
			RDFNode object = stmt.getObject();

			// Conduct all measurements on the current statement
			for (Measurement<?, ?> measurement : measurements) {
				measurement.conductMeasurement(subject, property, object);
			}

		}

		long endMeasurement = System.currentTimeMillis();
		log.info("Conducting all measurements on all statements: " + (endMeasurement - startMeasurement) + " ms");
	}

	/**
	 * Tries to parse the whole file with apacheJena, if an error occurs, switches
	 * to parsing the file line by line
	 * 
	 * Shouldn't be used - Takes longer than parsing the file line by line
	 * 
	 * @param dataPath File to parse
	 * @param log      Logging Errors
	 * @return List which contains all the statements of the document
	 */
	@Deprecated
	private static List<Statement> parseWholeFile(String dataPath, org.slf4j.Logger log) {
		List<Statement> allStatements = new ArrayList<Statement>();
		try {
			log.debug("Start parsing file with apache jena");
			Dataset dataset = DatasetFactory.create();
			long startParseOld = System.currentTimeMillis();
			RDFParserBuilder builder = RDFParserBuilder.create().source(dataPath).lang(RDFLanguages.NQ)
					.errorHandler(ErrorHandlerFactory.errorHandlerStrict(log));
			builder.parse(dataset);
			Iterator<String> modelNamesIt = dataset.listNames();
			while (modelNamesIt.hasNext()) {
				Model model = dataset.getNamedModel(modelNamesIt.next());
				StmtIterator stmtIt = model.listStatements();
				while (stmtIt.hasNext()) {
					allStatements.add(stmtIt.next());
				}
			}
			long endParseOld = System.currentTimeMillis();
			log.info("Parsing with apache Jena: " + (endParseOld - startParseOld) + " ms");
		} catch (RiotException e) {
			log.info("Error while parsing the file - Parsing the file line by line instead ");
			allStatements = parseLineByLine(dataPath, log);
		}
		return allStatements;
	}

}