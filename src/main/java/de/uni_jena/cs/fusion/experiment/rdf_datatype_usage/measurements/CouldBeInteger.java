package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measurements;

import org.apache.jena.datatypes.xsd.impl.RDFLangString;
import org.apache.jena.datatypes.xsd.impl.XSDBaseStringType;
import org.apache.jena.rdf.model.Literal;

import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils.StringUtil;
import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils.HashMapInsertUtil;

public class CouldBeInteger extends MeasurementOnObjectWithDatatypeString {

	@Override
	public void conductMeasurement(String propertyName, Literal literal) {
		// Only strings are of interest
		if (!(literal.getDatatype() instanceof RDFLangString)
				&& !(literal.getDatatype() instanceof XSDBaseStringType)) {
			return;
		}

		String lexicalValue = literal.getLexicalForm();

		if (StringUtil.isValidInteger(lexicalValue)) {
			HashMapInsertUtil.insertElement(propertyName, super.occurs);
		}

	}

}
