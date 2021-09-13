package main.java.measurements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.java.utils.GlobalNames;

public abstract class MeasurementOnObjectWithDatatypeString extends MeasurementOnObject<String, Long> {
	
	public MeasurementOnObjectWithDatatypeString() {
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