package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.jena.rdf.model.Statement;
import org.apache.jena.riot.RiotException;

/***
 * Makes the statement of a file iterable 
 * 
 * <p>
 * Reads a file line by line.
 * </p><p>
 * {@link StatementIterator} provides the next statement from that file
 * </p>
 */
public class FileIterator implements Iterable<Statement> {

	/**
	 * Reading the file line by line
	 */
	private BufferedReader reader;

	/**
	 * total number of lines in the file
	 */
	private Long numLines;

	/**
	 * key: line the error occurred; value: error message
	 */
	private HashMap<Long, String> errors;

	private org.slf4j.Logger log;

	/**
	 * Initialisation of the iterator for the file
	 * 
	 * @param url where to find the file
	 * @param log logging information
	 * @throws IOException when an error occurred while the file was read
	 */
	public FileIterator(String url, org.slf4j.Logger log) throws IOException {
		this.log = log;
		reader = createBufferedReader(url);
		numLines = 0L;
		errors = new HashMap<Long, String>();
	}

	@Override
	public Iterator<Statement> iterator() {
		try {
			return new StatementIterator();
		} catch (IOException e) {
			log.error(e.getMessage());
			System.exit(1);
		}
		return null;
	}

	/**
	 * Iterator over the statements of a file
	 */
	private class StatementIterator implements Iterator<Statement> {
		/**
		 * statements from the current line
		 */
		List<Statement> statements;
		/**
		 * statements from the next line
		 */
		List<Statement> statementsFromNextLine;

		String nextLine;

		/**
		 * Initalisation
		 * 
		 * @throws IOException
		 */
		public StatementIterator() throws IOException {
			// read the first line
			nextLine = reader.readLine();
			if (nextLine != null) {
				//sets nextStatement to the content of the first valid, not empty line
				parseNextLine();
				//this is switched to the current line and nextStatement is updated
				switchToNextLine();
			} else {
				// when the document is empty, the statements are empty too
				statements = new ArrayList<Statement>();
				statementsFromNextLine = new ArrayList<Statement>();
			}
		}

		/**
		 * Update the current line
		 * 
		 * <p>
		 * The current line gets the statements from the next line
		 * </p><p>
		 * In {@link #parseNextLine} {@link statementFromNextLine} is updated 
		 * to the next line
		 * </p>
		 */
		private void switchToNextLine() throws IOException {
			statements = statementsFromNextLine;
			parseNextLine();
		}

		/**
		 * Parses the next statement from the file
		 * 
		 * <p>
		 * Reads in new lines until a new valid statement has been read or until no more
		 * lines exist in the document
		 * </p>
		 * <p>
		 * In case of an error during parsing, this error is added to the error hash
		 * map.
		 * </p>
		 */
		private void parseNextLine() throws IOException {
			// if a new line with a valid list of statements was found
			boolean gotNextLine = false;
			// repeat until a valid, not empty line was found or there is no further line in
			// the document
			while (!gotNextLine && nextLine != null) {
				// increment the total number of lines
				numLines++;
				try {
					// try to parse the statements from the next line
					statementsFromNextLine = ModelUtil.createStatementsFromLine(nextLine);
					// check if the line contains statements
					// if the line contains a statement -> while loop breaks off
					gotNextLine = !statementsFromNextLine.isEmpty();
				} catch (RiotException e) {
					// In case of an error while parsing the line, log an error instead of adding
					// the tuple to the model
					// the error message must be updated, the line number is always 1 because the
					// parser get's only one line to parse
					log.warn(e.getMessage().replace("line: 1", "line: " + numLines));
					// add the current line to the error hash map
					// remove the position information from the error message
					errors.put(numLines, e.getMessage().replaceFirst("\\[line: 1, col: \\d*\\s*\\]\\s*", ""));
					// TODO soll die "Spalte" mit ausgegeben werden?
				}
				// update the line that will be parsed next
				nextLine = reader.readLine();
			}
		}

		@Override
		public boolean hasNext() {
			// a next list of statement exists
			return !statements.isEmpty() // if the current list of statements contains a statement
					|| !statementsFromNextLine.isEmpty(); // or if the next line contains a statement
		}

		@Override
		public Statement next() {
			//when the current statement list is empty, 
			//update the content of current and next statemen
			while (statements.isEmpty()) {
				try {
					switchToNextLine();
				} catch (IOException e) {
					log.error(e.getMessage());
					System.exit(1);
				}
			}
			//else return the first statement from the current statement list
			return statements.remove(0);
		}

	}

	/**
	 * Initalisation of {@link FileIterator.reader}
	 * 
	 * @param urlString link to the file
	 * @return a new buffered reader on the inputstream of the file
	 */
	private BufferedReader createBufferedReader(String urlString) throws IOException {
		URL url = new URL(urlString);
		if (urlString.endsWith(".gz")) {
			// Unzip the file
			GZIPInputStream gzipStream = new GZIPInputStream(url.openStream());
			return new BufferedReader(new InputStreamReader(gzipStream));
		} else {
			return new BufferedReader(new InputStreamReader(url.openStream()));
		}
	}

	public Long getNumLines() {
		return numLines;
	}

	public HashMap<Long, String> getErrors() {
		return errors;
	}
}
