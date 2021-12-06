package blind.experiment.rdf_datatype_usage.measure;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidIntegerNotation extends UsedAsDatatype {

	/**
	 * Check if the passed string is a valid integer notation.
	 *
	 * @param lexicalValue Literal that is checked
	 * @return true if the parameter can be stored as integer, else false
	 */
	@Override
	public boolean measure(String lexicalValue) {
		// See
		// https://www.w3.org/TR/xmlschema11-2/#integer-lexical-representation
		// for regular expression of an integer
		Pattern pattern = Pattern.compile("^" // String beginning
				+ "(\\+|-)?" // optional sign
				+ "[0-9]+" // a finite sequence of digits
				+ "$" // End of the string
		);
		Matcher matcher = pattern.matcher(lexicalValue);

		return matcher.find();
	}
}
