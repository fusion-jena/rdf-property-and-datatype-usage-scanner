package main.java.measurements;

import java.util.HashMap;

import org.apache.jena.datatypes.xsd.impl.RDFLangString;
import org.apache.jena.datatypes.xsd.impl.XSDBaseStringType;
import org.apache.jena.rdf.model.Literal;

import main.java.utils.GlobalNames;
import main.java.utils.StringUtil;
import main.java.utils.HashMapInsertUtil;

/**
 * Instead of String the data type decimal should be used
 * 
 * Issue #5
 */
public class StringDecimalMeasurement extends MeasurementOnObject<String, Long> {

	public StringDecimalMeasurement() {
		super();
		super.occurs = new HashMap<String, Long>();
	}

	@Override
	public void conductMeasurement(String propertyName, Literal literal) {
		//Only String Literals are of interest
		if (!(literal.getDatatype() instanceof RDFLangString)
				&& !(literal.getDatatype() instanceof XSDBaseStringType)) {
			return;
		}

		String lexicalValue = literal.getLexicalForm();

		if (StringUtil.isValidDecimal(lexicalValue)) {
			HashMapInsertUtil.insertElement(propertyName, super.occurs);
		}
	}
	
	@Override
	public String toString() {
		String s = StringDecimalMeasurement.class.getName() + ":";
		for (String key : super.occurs.keySet()) {
			s += "\n\t" + key + "\t" + super.occurs.get(key) + "\t" + GlobalNames.XSD_DECIMAL;
		}
		return s;
	}

}
