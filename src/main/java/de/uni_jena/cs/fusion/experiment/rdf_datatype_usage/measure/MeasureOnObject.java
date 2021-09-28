package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measure;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;

/**
 * Abstraction of all measurement classes who examine the object of a statement
 */

public abstract class MeasureOnObject extends Measure {

	/**
	 * Conduct the measurement on the literal of the statement 
	 * 
	 * @param propertyName - property of the statement, key for the HashMap
	 * @param literal - object of the statement, is examined 
	 */
	public abstract void measure(String propertyName, Literal literal);

	@Override
	public final void measure(RDFNode subject, Property property, RDFNode object) {
		// Only statements with literals are of interest
		if(object.isLiteral()) {
			measure(property.getURI(), object.asLiteral());		
		}
	}

}
