package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measure;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidExponentialNotation extends UsedAsDatatype {

	/**
	 * Check if the passed string is a valid integer notation.
	 *
	 * @param lexicalValue Literal that is checked
	 * @return true if the parameter can be stored as integer, else false
	 */
	@Override
	public boolean measure(String lexicalValue) {
		// see https://www.w3.org/TR/xmlschema11-2/#nt-floatRep
		Pattern pattern = Pattern.compile("^" // String beginning
				+ "(\\+|-)?" // optional sign
				+ "([0-9]+(\\.[0-9]*)?|\\.[0-9]+)" // decimal base
				+ "[Ee](\\+|-)?[0-9]+" // exponent
				+ "$" // End of the string
		);
		Matcher matcher = pattern.matcher(lexicalValue);

		return matcher.find();
	}
}
