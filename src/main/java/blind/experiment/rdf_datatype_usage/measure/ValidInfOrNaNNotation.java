package blind.experiment.rdf_datatype_usage.measure;

public class ValidInfOrNaNNotation extends UsedAsDatatype {

	/**
	 * Check if the parameter can be stored as double or float, but not as decimal
	 * 
	 * </p>
	 * NaN, + and - infinity can't be represented by decimal
	 * </p>
	 * -> test if the parameter is one of them
	 * 
	 * @param lexicalValue literal that is checked
	 * @return true if the parameter is Nan, + infinity, - infinity, else false
	 */
	@Override
	public boolean measure(String lexicalValue) {
		return "NaN".equals(lexicalValue) || "INF".equals(lexicalValue) || "+INF".equals(lexicalValue)
				|| "-INF".equals(lexicalValue);
	}

}
