package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measure;

public class ValidTrueOrFalseNotation extends UsedAsDatatype {

	/**
	 * Check if the parameter is true or false
	 * 
	 * @param lexicalValue literal that is checked
	 * @return true if the parameter is one of true, otherwise false
	 */
	@Override
	public boolean measure(String lexicalValue) {
		return "true".equals(lexicalValue) || "false".equals(lexicalValue);
	}

}
