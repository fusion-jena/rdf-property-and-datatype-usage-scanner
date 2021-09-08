package main.java.measurements;

import java.util.HashMap;
import java.util.List;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;

/**
 * Abstraction of all measurement classes
 * 
 * @param K - property name
 * @param V - can be a HashMap for data type and value or a single value
 */
public abstract class Measurement<K, V> {

	protected HashMap<K, V> occurs;

	/**
	 * Conduct the measorement on a single statement
	 * 
	 * @param subject  of the statement
	 * @param property of the statement
	 * @param object   of the statement
	 */
	public abstract void conductMeasurement(RDFNode subject, Property property, RDFNode object);

	public HashMap<K, V> getOccurs() {
		return occurs;
	}
	
	/**
	 * Creates a list of Strings that can be used to fill a database
	 * 
	 * <p>
	 * Important: each string must be appended to another string which contains:
	 * </p>
	 * <p>
	 * 'INSERT into <NAME OF DATABASE> values ( <FILE_IDENTIFIER>, ' and ')'
	 * </p>
	 * @return a list of String statements 
	 */
	public abstract List<String> writeToDatabase();

	@Override
	public abstract String toString();
}
