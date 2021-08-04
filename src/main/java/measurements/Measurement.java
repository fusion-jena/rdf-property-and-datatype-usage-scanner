package main.java.measurements;

import java.util.HashMap;

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

	public Measurement() {
	}

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

	@Override
	public abstract String toString();
}
