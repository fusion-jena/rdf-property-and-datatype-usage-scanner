package main.java.measurements;

import java.util.HashMap;

import org.apache.jena.datatypes.xsd.impl.RDFLangString;
import org.apache.jena.datatypes.xsd.impl.XSDBaseStringType;
import org.apache.jena.datatypes.xsd.impl.XSDDouble;
import org.apache.jena.datatypes.xsd.impl.XSDFloat;
import org.apache.jena.rdf.model.Literal;

import main.java.utils.GlobalNames;
import main.java.utils.HashMapInsertUtil;
import main.java.utils.NumberUtil;
import main.java.utils.StringUtil;

/**
 * Using String, Double or Float but Decimal should be used
 * 
 * Issue #7
 */
public class ShouldBeDecimal extends MeasurementOnObject<String, HashMap<GlobalNames, Long>> {

	public ShouldBeDecimal() {
		super();
		super.occurs = new HashMap<String, HashMap<GlobalNames, Long>>();
	}

	@Override
	public void conductMeasurement(String propertyName, Literal literal) {
		GlobalNames datatype;
		String lexicalForm = literal.getLexicalForm();
		if (literal.getDatatype() instanceof XSDFloat) {
			datatype = GlobalNames.FLOAT;
			float floatValue = literal.getFloat();
			if (!NumberUtil.shouldBeReplacedByDecimal(floatValue, lexicalForm)) {
				return;
			}
		} else if (literal.getDatatype() instanceof XSDDouble) {
			datatype = GlobalNames.DOUBLE;
			double doubleValue = literal.getDouble();
			if (!NumberUtil.shouldBeReplacedByDecimal(doubleValue, lexicalForm)) {
				return;
			}
		} else if (literal.getDatatype() instanceof RDFLangString
				|| literal.getDatatype() instanceof XSDBaseStringType) {
			datatype = GlobalNames.STRING;
			if(! StringUtil.isValidDecimal(lexicalForm)) {
				return;
			}
		} else {
			return;
		}

		HashMapInsertUtil.insertElement(propertyName, datatype, super.occurs);
	}
	
	@Override
	public String toString() {
		String s = ShouldBeDecimal.class.getName() + ":";
		for (String key : super.occurs.keySet()) {
			HashMap<GlobalNames, Long> hashMap = super.occurs.get(key);
			for (GlobalNames datatype : hashMap.keySet()) {
				long value = hashMap.get(datatype);
				s += "\n\t" + key + "\t" + value + "\t" + datatype;
			}
		}
		return s;
	}
}
