package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measurements;

import org.apache.jena.datatypes.xsd.impl.RDFLangString;
import org.apache.jena.datatypes.xsd.impl.XSDBaseStringType;
import org.apache.jena.rdf.model.Literal;

import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils.HashMapInsertUtil;
import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils.StringUtil;

/**
 * Statement uses data type String but xsd:dateTime should be used
 * 
 * Issue #6
 */
public class ShouldBeDateTime extends MeasurementOnObjectWithDatatypeString {

	@Override
	public void conductMeasurement(String propertyName, Literal literal) {
		// Only strings are of interest
		if (!(literal.getDatatype() instanceof RDFLangString)
				&& !(literal.getDatatype() instanceof XSDBaseStringType)) {
			return;
		}

		String lexicalValue = literal.getLexicalForm();

		if (StringUtil.isValidDate(lexicalValue)) {
			HashMapInsertUtil.insertElement(propertyName, super.occurs);
		}

	}

}
