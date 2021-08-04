package main.java.measurements;

import java.util.HashMap;

import org.apache.jena.datatypes.xsd.impl.XSDDouble;
import org.apache.jena.datatypes.xsd.impl.XSDFloat;
import org.apache.jena.rdf.model.Literal;

import main.java.utils.GlobalNames;
import main.java.utils.NumberUtil;
import main.java.utils.HashMapInsertUtil;

/**
 * Using Double or Float but Decimal should be used
 * 
 * Issue #7
 */
public class DoubleFloatDecimalMeasurement extends MeasurementOnObject<String, HashMap<GlobalNames, Long>> {

	public DoubleFloatDecimalMeasurement() {
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
		} else {
			return;
		}

		HashMapInsertUtil.insertElement(propertyName, datatype, super.occurs);
	}

	@Override
	public String toString() {
		String s = DoubleFloatDecimalMeasurement.class.getName() + ":";
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
