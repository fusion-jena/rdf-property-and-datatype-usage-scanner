package main.java.measurements;

import java.util.HashMap;

import org.apache.jena.datatypes.xsd.impl.RDFLangString;
import org.apache.jena.datatypes.xsd.impl.XSDBaseStringType;
import org.apache.jena.rdf.model.Literal;

import main.java.utils.GlobalNames;
import main.java.utils.StringUtil;
import main.java.utils.HashMapInsertUtil;

/**
 * Statement uses data type String but xsd:dateTime should be used
 * 
 * Issue #6
 */
public class ShouldBeDateTime extends MeasurementOnObject<String, Long> {

	public ShouldBeDateTime() {
		super();
		super.occurs = new HashMap<String, Long>();
	}

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

	@Override
	public String toString() {
		String s = ShouldBeDateTime.class.getName() + ":";
		for (String key : super.occurs.keySet()) {
			s += "\n\t" + key + "\t" + super.occurs.get(key) + "\t" + GlobalNames.XSD_DATETIME;
		}
		return s;
	}

}
