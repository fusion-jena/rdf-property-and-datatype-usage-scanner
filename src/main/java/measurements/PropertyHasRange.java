package main.java.measurements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.vocabulary.RDFS;

import main.java.utils.GlobalNames;
import main.java.utils.HashMapInsertUtil;

/**
 * Check, if a statement defines a range for a property
 * 
 * <p>
 * HashMap with the property name as key and an inner HashMap with the range as
 * key and how often the range is defined as value
 * </p>
 * Issue #4
 */
public class PropertyHasRange extends Measurement<String, HashMap<String, Long>> {

	public PropertyHasRange() {
		super();
		super.occurs = new HashMap<String, HashMap<String, Long>>();
	}

	@Override
	public void conductMeasurement(RDFNode subject, Property property, RDFNode object) {
		// check if in this statement the property is rdfs:range
		if (!RDFS.range.equals(property)) {
			return;
		}
		HashMapInsertUtil.insertElement(subject.toString(), object.toString(), super.occurs);
	}

	@Override
	public List<String> writeToDatabase() {
		List<String> values = new ArrayList<String>();
		for(String property : super.occurs.keySet()) {
			HashMap<String, Long> innerHashMap = super.occurs.get(property);
			for(String range : innerHashMap.keySet()) {
				values.add("'" +property + "', '" + this.getClass().getSimpleName() + "', '" + range +"', " + innerHashMap.get(range));
			}
		}
		return values;
	}
	
	@Override
	public String toString() {
		String s = "\n" + this.getClass().getSimpleName() + ":";
		for (String propertyName : super.occurs.keySet()) {
			HashMap<String, Long> innerHashMap = super.occurs.get(propertyName);
			for (String range : innerHashMap.keySet()) {
				s += "\n\t" + propertyName + "\t" + GlobalNames.RANGE + ":\t" + range + "\t" + innerHashMap.get(range);
			}
		}
		return s;
	}

}
