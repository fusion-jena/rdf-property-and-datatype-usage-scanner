package main.java.measurements;

import org.apache.jena.datatypes.xsd.impl.RDFLangString;
import org.apache.jena.datatypes.xsd.impl.XSDBaseStringType;
import org.apache.jena.rdf.model.Literal;

import main.java.utils.StringUtil;
import main.java.utils.HashMapInsertUtil;

/**
 * Literals that have the data type String, but also could be Float or Double
 * but NOT Decimal
 * 
 * Issue #11
 */
public class ShouldBeDoubleOrFloatNotDecimal extends MeasurementOnObjectWithDatatypeString {

	@Override
	public void conductMeasurement(String propertyName, Literal literal) {
		// Only Strings of interest
		if (!(literal.getDatatype() instanceof RDFLangString)
				&& !(literal.getDatatype() instanceof XSDBaseStringType)) {
			return;
		}

		String lexicalValue = literal.getLexicalForm();

		// NaN, + inf, - inf are equal for Float and Double
		if (StringUtil.isValidDoubleOrFloatAndInvalidDecimal(lexicalValue)) {
			HashMapInsertUtil.insertElement(propertyName, super.occurs);
		}
	}

}
