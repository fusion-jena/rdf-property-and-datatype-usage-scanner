package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.rdf.model.Literal;

import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils.HashMapInsertUtil;

/**
 * Count how often a property occurs with a datatype
 * 
 * <p>
 * HashMap with the property name as key, value is another HashMap.
 * </p>
 * <p>
 * Its key is the datatype of the literal, the value is the quantity.
 * </p>
 * Issue #3
 */
public class UsageCount extends MeasureOnObject<String, HashMap<String, Long>> {

	public UsageCount() {
		super();
		super.occurs = new HashMap<String, HashMap<String, Long>>();
	}

	@Override
	public void measure(String propertyName, Literal literal) {
		RDFDatatype datatype = literal.getDatatype();
		HashMapInsertUtil.insertElement(propertyName, datatype.getURI(), super.occurs);
	}

	@Override
	public List<String> writeToDatabase() {
		List<String> values = new ArrayList<String>();
		for (String property : super.occurs.keySet()) {
			HashMap<String, Long> innerHashMap = super.occurs.get(property);
			for (String datatype : innerHashMap.keySet()) {
				values.add("'" + property + "', '" + this.getClass().getSimpleName() + "', '" + datatype + "', "
						+ innerHashMap.get(datatype));
			}
		}
		return values;
	}

	@Override
	public String toString() {
		String s = "\n" + this.getClass().getSimpleName();
		for (String property : super.occurs.keySet()) {
			HashMap<String, Long> innerHashMap = super.occurs.get(property);
			for (String datatype : innerHashMap.keySet()) {
				s += "\n\t" + property + "\tused " + datatype + "\t" + innerHashMap.get(datatype) + " times";
			}
		}
		return s;
	}
}
