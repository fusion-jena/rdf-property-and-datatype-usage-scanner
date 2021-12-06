package blind.experiment.rdf_datatype_usage.measure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	 * Creates a list of {@link MeasureResult} that can be used to fill a database
	 * 
	 * @return a list of {@link MeasureResults}
	 */
	public List<MeasureResult> writeToDatabase() {
		List<MeasureResult> results = new ArrayList<MeasureResult>();
		for (String datatype : occurs.keySet()) {
			Map<String, Long> propertyQuantityMap = occurs.get(datatype);
			for (String property : propertyQuantityMap.keySet()) {
				results.add(new MeasureResult(this.getClass().getSimpleName(), property, datatype,
						propertyQuantityMap.get(property)));
			}
		}
		return results;
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
