package blind.experiment.rdf_datatype_usage.measure;

public class ValidZeroOrOneNotation extends UsedAsDatatype {

	/**
	 * @return true if the lexicalValue is 0 or 1, otherwise false
	 */
	@Override
	public boolean measure(String lexicalValue) {
		return "0".equals(lexicalValue) || "1".equals(lexicalValue);
	}

}
