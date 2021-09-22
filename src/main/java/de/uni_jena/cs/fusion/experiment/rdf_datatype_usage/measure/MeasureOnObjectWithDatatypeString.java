package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils.GlobalNames;

public abstract class MeasureOnObjectWithDatatypeString extends MeasureOnObject<String, Long> {
	
	public MeasureOnObjectWithDatatypeString() {
		super();
		super.occurs = new HashMap<String, Long>();
	}
	
	public List<String> writeToDatabase(){
		List<String> values = new ArrayList<String>();
		for(String property : super.occurs.keySet()) {
			values.add("'" +property + "', '" + this.getClass().getSimpleName() + "', '" + GlobalNames.STRING + "', " + super.occurs.get(property));
		}
		return values;
	}
		
	@Override
	public String toString() {
		String s = "\n" + this.getClass().getSimpleName()+ ":";
		for (String property : super.occurs.keySet()) {
			s += "\n\t" + GlobalNames.STRING + "\t" + property + "\t" + super.occurs.get(property);
		}
		return s;
	}
	
}
