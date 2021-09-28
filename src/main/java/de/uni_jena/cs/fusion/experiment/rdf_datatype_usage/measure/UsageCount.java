package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measure;

import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.rdf.model.Literal;

import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils.MapInsertUtil;

/**
 * Count how often a property occurs with a datatype
 * 
 * Issue #3
 */
public class UsageCount extends MeasureOnObject {

	public UsageCount() {
		super();
	}

	@Override
	public void measure(String propertyName, Literal literal) {
		RDFDatatype datatype = literal.getDatatype();
		MapInsertUtil.insertElement(datatype.getURI(), propertyName, super.occurs);
	}

}
