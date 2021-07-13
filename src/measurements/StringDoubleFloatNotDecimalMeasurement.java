package measurements;

import java.util.HashMap;

import org.apache.jena.datatypes.xsd.impl.RDFLangString;
import org.apache.jena.datatypes.xsd.impl.XSDBaseStringType;
import org.apache.jena.rdf.model.Literal;

import utils.GlobalNames;
import utils.StringUtil;
import utils.HashMapInsertUtil;

/**
 * Literals that have the data type String, but also could be Float or Double
 * but NOT Decimal
 * 
 * Issue #11
 */
public class StringDoubleFloatNotDecimalMeasurement extends MeasurementOnObject<String, Long> {

	public StringDoubleFloatNotDecimalMeasurement() {
		super();
		super.occurs = new HashMap<String, Long>();
	}

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

	@Override
	public String toString() {
		String s = StringDoubleFloatNotDecimalMeasurement.class.getName() + ":";
		for (String propertyName : super.occurs.keySet()) {
			s += "\n\t" + propertyName + "\t" + super.occurs.get(propertyName) + "\t" + GlobalNames.DOUBLE + "/"
					+ GlobalNames.FLOAT;
		}
		return s;
	}

}
