package main.java.measurements;

import org.apache.jena.datatypes.xsd.impl.RDFLangString;
import org.apache.jena.datatypes.xsd.impl.XSDBaseStringType;
import org.apache.jena.rdf.model.Literal;

import main.java.utils.HashMapInsertUtil;
import main.java.utils.StringUtil;

/**
 * Using String but Boolean could be used alternative
 *
 * Issue #16
 */
public class CouldBeBoolean extends MeasurementOnObjectWithDatatypeString {

	@Override
	public void conductMeasurement(String propertyName, Literal literal) {
		// Only Strings are of interest
		if (!(literal.getDatatype() instanceof RDFLangString)
				&& !(literal.getDatatype() instanceof XSDBaseStringType)) {
			return;
		}

		String lexicalValue = literal.getLexicalForm();

		if (StringUtil.isValidBoolean(lexicalValue)) {
			HashMapInsertUtil.insertElement(propertyName, super.occurs);
		}
	}

}
