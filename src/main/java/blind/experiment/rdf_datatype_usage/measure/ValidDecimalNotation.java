package blind.experiment.rdf_datatype_usage.measure;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidDecimalNotation extends UsedAsDatatype {

	/**
	 * Check if the passed string is a valid decimal nut not integer notation.
	 * 
	 * @param s String to examine
	 * @return true if the string can be interpreted as a decimal number else false
	 */
	@Override
	public boolean measure(String lexicalValue) {
		// RegEx fuer xsd:decimal
		// see
		// https://www.w3.org/TR/2012/REC-xmlschema11-2-20120405/datatypes.html#decimal
		// 3.3.1 Lexical Mapping
		Pattern pattern = Pattern.compile("^" // String beginning
				+ "(\\+|-)?" // optional sign
				+ "[0-9]*(\\.[0-9]+)" // number with digits after and optionally before '.'
				+ "$" // End of the string
		);
		Matcher matcher = pattern.matcher(lexicalValue);
		return matcher.find();
	}

}
