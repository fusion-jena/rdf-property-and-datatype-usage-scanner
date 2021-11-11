package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measure;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidDateTimeNotation extends UsedAsDatatype {

	/**
	 * Check if parameter is formated like a valid xsd:dateTime or xsd:dateTimeStamp
	 * notation
	 * 
	 * <p>
	 * the two types are checked together because we're only interested in the
	 * result, IF the parameter can be interpreted as xsd:dateTime or
	 * xsd:dateTimeStamp, but not which one
	 * </p>
	 * 
	 * @param lexicalValue literal that is checked
	 * @return true if the parameter can be stored as xsd:dateTime or as
	 *         xsd:dateTimeStamp, else false
	 */
	@Override
	public boolean measure(String lexicalValue) {
		/*
		 * Regular expression for xsd:dateTime see
		 * https://www.w3.org/TR/2012/REC-xmlschema11-2-20120405/datatypes.html#dateTime
		 * 3.3.7.2 Lexical Mapping mandatory information: yyyy-MM-DD'T'HH:mm:ss where T
		 * is a delimeter between day and hour can be followed by .ms and an optional
		 * timezone offset (z for utc or else [-14:00,+14:00])
		 * 
		 * this regular expression is also used for xsd:dateTimeStamp, as in
		 * https://www.w3.org/TR/2012/REC-xmlschema11-2-20120405/datatypes.html# stated,
		 * in dateTimeStamp the timezone offset is obligatory
		 */

		Pattern pattern = Pattern.compile("^" + // beginning of the string
				"-?([1-9][0-9]{3,}|0[0-9]{3})" + // year
				"-(0[1-9]|1[0-2])" + // month
				"-(0[1-9]|[12][0-9]|3[01])" + // day
				"T(([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9](\\.[0-9]+)?|(24:00:00(\\.0+)?))" +
				// hour:min:sec[.ms] or midnight
				"(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00))?" // optional timezone offset
				+ "$" // end of the string
		);

		Matcher matcher = pattern.matcher(lexicalValue);
		return matcher.find();
	}

}
