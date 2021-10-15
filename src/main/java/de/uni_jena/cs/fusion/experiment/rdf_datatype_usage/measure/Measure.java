package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;

/**
 * Abstraction of all measurement classes
 */
public abstract class Measure {

	/**
	 * Map for the results
	 * key: datatype
	 * value : map with 
	 * 	key: property
	 * 	value: how often the combination occured
	 */
	protected Map<String, Map<String, Long>> occurs;

	public Measure() {
		occurs = new HashMap<String, Map<String, Long>>();
	}

	/**
	 * Conduct the measurement on a single statement
	 * 
	 * @param subject  of the statement
	 * @param property of the statement
	 * @param object   of the statement
	 */
	public abstract void measure(RDFNode subject, Property property, RDFNode object);

	/**
	 * Creates a list of Strings that can be used to fill a database
	 * 
	 * <p>
	 * Important: each string must be appended to another string which contains:
	 * </p>
	 * <p>
	 * 'INSERT into <NAME OF DATABASE> values ( <FILE_IDENTIFIER>, ' and ')'
	 * </p>
	 * 
	 * @return a list of String statements
	 */
	public List<Object[]> writeToDatabase(){
		List<Object[]> values = new ArrayList<Object[]>();
		for (Entry<String, Map<String, Long>> datatype : occurs.entrySet()) {
			for (Entry<String, Long> property : datatype.getValue().entrySet()) {
				values.add(new Object[] {property.getKey(), this.getClass().getSimpleName(), datatype.getKey(), property.getValue()});
			}
		}
		return values;
	}

	@Override
	public String toString() {
		String s = "\n" + this.getClass().getSimpleName() + ":";
		for (String datatype : occurs.keySet()) {
			Map<String, Long> propertyDatatypeMap = occurs.get(datatype);
			for (String property : propertyDatatypeMap.keySet()) {
				s += "\n\t" + datatype + "\t" + property + "\t" + propertyDatatypeMap.get(property);
			}
		}
		return s;
	}
	
	public Map<String, Map<String, Long>> getOccurs() {
		return occurs;
	}

}
