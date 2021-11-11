package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measure;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;

import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils.MapInsertUtil;

/**
 * Counts occurrences of an IRI as datatype of a literal
 * 
 */
public class UsedAsDatatype extends Measure {

	public boolean measure(String lexicalValue) {
		return true;
	}

	/**
	 * Conduct the measurement on the literal of the statement
	 * 
	 * @param propertyName - property of the statement, key for the HashMap
	 * @param literal      - object of the statement, is examined
	 */
	public void measure(String propertyName, Literal literal) {
		if (this.measure(literal.getLexicalForm())) {
			MapInsertUtil.insertElement(literal.getDatatypeURI(), propertyName, super.occurs);
		}
	}

	@Override
	public final void measure(RDFNode subject, Property property, RDFNode object) {
		// Only statements with literals are of interest
		if (object.isLiteral()) {
			measure(property.getURI(), object.asLiteral());
		}
	}

}
