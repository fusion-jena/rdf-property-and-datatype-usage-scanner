package de.uni_jena.cs.fusion.experiment.rdf_float_usage.measurements;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;

/**
 * Abstraction of all measurement classes who examine the object of a statement
 *
 * @param <K> - property name
 * @param <V> - can be a HashMap for data type and value or a single value
 */

public abstract class MeasurementOnObject<K, V> extends Measurement<K, V> {

	/**
	 * Conduct the measurment on the literal of the statement 
	 * 
	 * @param propertyName - property of the statement, key for the HashMap
	 * @param literal - object of the statement, is examined 
	 */
	public abstract void conductMeasurement(String propertyName, Literal literal);

	@Override
	public final void conductMeasurement(RDFNode subject, Property property, RDFNode object) {
		// Only statements with literals are of interest
		if(object.isLiteral()) {
			conductMeasurement(property.getURI(), object.asLiteral());		
		}
	}
	
	@Override
	public abstract String toString();


}
