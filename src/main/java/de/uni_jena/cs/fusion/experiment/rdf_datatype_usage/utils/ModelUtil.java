package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.riot.RiotException;
import org.apache.jena.riot.system.ErrorHandlerFactory;

import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measure.Measure;

/**
 * Functions for different operations (parsing, analysing) a model
 */
public abstract class ModelUtil {

	/**
	 * Creates an iterator over the statements of the individual lines of the
	 * document
	 * 
	 * @param urlString path to the document
	 * @param log       for logging
	 * 
	 * @return an iterator over the statements of the file
	 */
	public static FileIterator parseURLlineByLine(String urlString, org.slf4j.Logger log) {
		FileIterator fileIter = null;

		try {
			fileIter = new FileIterator(urlString, log);
		} catch (IOException e) {
			log.error(e.getMessage());
			System.exit(1);
		}

		return fileIter;

	}

	/**
	 * Returns the statement of a single line
	 * 
	 * Parse a String, create a model of the created data set and return the
	 * contained statement
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
	public static void conductMeasurements(List<Measure> measurements, FileIterator allStatements,
			org.slf4j.Logger log) {
		// Iterate over all statements of the file
		Iterator<Statement> iter = allStatements.iterator();
		while (iter.hasNext()) {
			Statement stmt = iter.next();

			RDFNode subject = stmt.getSubject();
			Property property = stmt.getPredicate();
			RDFNode object = stmt.getObject();

			// Conduct all measurements on the current statement
			for (Measure measurement : measurements) {
				try {
					measurement.measure(subject, property, object);
				} catch (Throwable t) {
					allStatements.addError(t.getMessage());
				}
			}
		}

	}

}