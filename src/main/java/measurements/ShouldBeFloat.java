package main.java.measurements;

import java.util.HashMap;

import org.apache.jena.datatypes.xsd.impl.RDFLangString;
import org.apache.jena.datatypes.xsd.impl.XSDBaseStringType;
import org.apache.jena.rdf.model.Literal;

import main.java.utils.GlobalNames;
import main.java.utils.HashMapInsertUtil;
import main.java.utils.StringUtil;

/**
 * Using String but Float should be used
 * 
 * Issue #8
 *
 */
public class ShouldBeFloat extends MeasurementOnObject<String, Long> {

	public ShouldBeFloat() {
		super();
		super.occurs = new HashMap<String, Long>();
	}

	@Override
	public void conductMeasurement(String propertyName, Literal literal) {
		// Only Strings are of interest
		if (!(literal.getDatatype() instanceof RDFLangString)
				&& !(literal.getDatatype() instanceof XSDBaseStringType)) {
			return;
		}

		String lexicalValue = literal.getLexicalForm();

		if (StringUtil.isValidFloat(lexicalValue)) {
			HashMapInsertUtil.insertElement(propertyName, super.occurs);
		}
	}

	@Override
	public String toString() {
		String s = ShouldBeFloat.class.getName() + ":";
		for (String key : super.occurs.keySet()) {
			s += "\n\t" + key + "\t" + super.occurs.get(key) + "\t" + GlobalNames.FLOAT;
		}
		return s;
	}

}
