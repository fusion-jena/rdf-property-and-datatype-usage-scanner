package analyse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class StringUtil {

		
	/**
	 * Check if parameter is formated like a valid xsd:dateTime object
	 * 
	 * @param s Literal that is checked
	 * @return false - if the parameter can't be stored as a xsd:dateTime, else true
	 */
	public static boolean isValidDate(String s) {
		/*
		 * xsd:dateTime see
		 * https://www.w3.org/TR/2012/REC-xmlschema11-2-20120405/datatypes.html#dateTime
		 * Regular Expression \n mandatory information: yyyy-MM-DD'T'HH:mm:ss where T is
		 * a delimeter between day and hour can be followed by .ms and an optional
		 * timezone offset (z for utc or else [-14:00,+14:00])
		 */

		Pattern pattern = Pattern.compile("^" + // beginning of the string
				"-?([1-9][0-9]{3,}|0[0-9]{3})" + // year
				"-(0[1-9]|1[0-2])" + // month
				"-(0[1-9]|[12][0-9]|3[01])" + // day
				"T(([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9](\\.[0-9]+)?|(24:00:00(\\.0+)?))" +
				// hour:min:sec[.ms]
				"(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00))?" // optional timezone offset
				+ "$" // end of the string
		);

		Matcher matcher = pattern.matcher(s);
		return matcher.find();
	}
}
