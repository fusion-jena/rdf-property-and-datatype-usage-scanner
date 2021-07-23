package measurements;

import java.util.HashMap;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.vocabulary.RDFS;

import utils.GlobalNames;
import utils.HashMapInsertUtil;

/**
 * Check, if a statement defines a range for a property
 * 
 * <p>
 * HashMap with the property name as key and an inner HashMap with the range as
 * key and how often the range is defined as value
 * </p>
 * Issue #4
 */
public class PropertyRangeMeasurement extends Measurement<String, HashMap<String, Long>> {

	public PropertyRangeMeasurement() {
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
	public String toString() {
		String s = PropertyRangeMeasurement.class.getName() + ":";
		for (String propertyName : super.occurs.keySet()) {
			HashMap<String, Long> innerHashMap = super.occurs.get(propertyName);
			for (String range : innerHashMap.keySet()) {
				s += "\n\t" + propertyName + "\t" + GlobalNames.RANGE + ":\t" + range + "\t" + innerHashMap.get(range);
			}
		}
		return s;
	}
}