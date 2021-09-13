package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measurements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.jena.datatypes.xsd.impl.RDFLangString;
import org.apache.jena.datatypes.xsd.impl.XSDBaseStringType;
import org.apache.jena.datatypes.xsd.impl.XSDDouble;
import org.apache.jena.datatypes.xsd.impl.XSDFloat;
import org.apache.jena.rdf.model.Literal;

import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils.GlobalNames;
import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils.HashMapInsertUtil;
import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils.NumberUtil;
import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils.StringUtil;

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
	public List<String> writeToDatabase() {
		List<String> values = new ArrayList<String>();
		for(String property : super.occurs.keySet()) {
			HashMap<GlobalNames, Long> innerHashMap = super.occurs.get(property);
			for(GlobalNames datatype: innerHashMap.keySet()) {
				values.add("'" + property + "', '" + this.getClass().getSimpleName() + "', '" + datatype + "', " + innerHashMap.get(datatype));
			}
		}
		return values;
	}
	
	@Override
	public String toString() {
		String s = "\n" + this.getClass().getSimpleName() + ":";
		for (String key : super.occurs.keySet()) {
			HashMap<GlobalNames, Long> hashMap = super.occurs.get(key);
			for (GlobalNames datatype : hashMap.keySet()) {
				long value = hashMap.get(datatype);
				s += "\n\t" + datatype + "\t" + key + "\t" + value;
			}
		}
		return s;
	}

}
