package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Functions for examining Strings
 */
public abstract class StringUtil {

	/**
	 * Check if the passed String can be stored as a double
	 * 
	 * Also considers the accuracy of double
	 * 
	 * @param s String to examine
	 * @return true if s can be stored as a double, else double
	 */
	public static boolean isValidDouble(String s) {
		Double doubleValue;
		BigDecimal decimalValue;

		try {
			// interpret the String as double value
			doubleValue = Double.parseDouble(s);
		} catch (NumberFormatException e) {
			return false;
		}
		try {
			// interpret the string as a decimal
			decimalValue = new BigDecimal(s);
		} catch (NumberFormatException e) {
			// special valid cases from double that aren't valid as BigDecimal
			return doubleValue.isNaN() | doubleValue.isInfinite();
		}
		try {
			// convert the double to decimal
			BigDecimal doubleToDecimal = new BigDecimal(doubleValue);
			// check if the values are identical
			return decimalValue.compareTo(doubleToDecimal) == 0;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * Check if the passed String can be stored as a float
	 * 
	 * Also considers the accuracy of float
	 * 
	 * @param s String to examine
	 * @return true if s can be stored as a float, else false
	 */
	public static boolean isValidFloat(String s) {
		Float floatValue;
		BigDecimal decimalValue;
		try {
			// interpret the string as a float
			floatValue = Float.parseFloat(s);
		} catch (NumberFormatException e) {
			return false;
		}
		try {
			// interpret the string as a decimal
			decimalValue = new BigDecimal(s);
		} catch (NumberFormatException e) {
			// special cases from Float that can't be stored as BigDecimal but are valid
			// float values
			return floatValue.isNaN() | floatValue.isInfinite();
		}
		try {
			// convert the float to decimal
			BigDecimal floatToDecimal = new BigDecimal(floatValue);
			// check if the values are identical, if not the precision from float is to low
			return decimalValue.compareTo(floatToDecimal) == 0;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * Check if the passed string can be interpreted as xsd:decimal
	 * 
	 * @param s String to examine
	 * @return true if the string can be interpreted as a decimal number else false
	 */
	public static boolean isValidDecimal(String s) {
		// RegEx fuer xsd:decimal
		// see
		// https://www.w3.org/TR/2012/REC-xmlschema11-2-20120405/datatypes.html#decimal
		// 3.3.1 Lexical Mapping
		Pattern pattern = Pattern.compile("^" // String beginning
				+ "(\\+|-)?(" // optional sign
				+ "[0-9]+(\\.[0-9]*)?|" // integer, or number with digits before '.', and decimal places
				+ "\\.?[0-9]+)" // only decimal numbers or a integer
				+ "$" // End of the string
		);
		Matcher matcher = pattern.matcher(s);
		return matcher.find();
	}

	/**
	 * Check if parameter is formated like a valid xsd:dateTime object
	 * 
	 * @param s Literal that is checked
	 * @return false - if the parameter can't be stored as a xsd:dateTime, else true
	 */
	public static boolean isValidDate(String s) {
		/*
		 * Regular expression for xsd:dateTime see
		 * https://www.w3.org/TR/2012/REC-xmlschema11-2-20120405/datatypes.html#dateTime
		 * 3.3.7.2 Lexical Mapping mandatory information: yyyy-MM-DD'T'HH:mm:ss where T
		 * is a delimeter between day and hour can be followed by .ms and an optional
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

	/**
	 * Check if the parameter can be stored as float, but not as decimal
	 * 
	 * Check if the parameter can be interpreted as float
	 * </p>
	 * NaN, + and - infinity can't be represented by decimal
	 * </p>
	 * -> test if the parameter is one of them
	 * 
	 * 
	 * @param s Literal that is checked
	 * @return true if the parameter is Nan, + infinity, - infinity and can be
	 *         stored as a float, else false
	 */
	/*
	 * TODO weg? -> double Methode ausreichend, was bei Float NaN, +/- inf ist, ist
	 * auch bei double einer dieser Werte
	 */
	/*
	 * public static boolean isValidFloatAndInvalidDecimal(String s) { /*Float
	 * floatValue; try { // interpret String as Float floatValue =
	 * Float.parseFloat(s); } catch (NumberFormatException e) { return false; } //
	 * the accuracy of float is not of interest at this point // NaN and +/-
	 * infinity can't be represented by decimal return floatValue.isInfinite() ||
	 * floatValue.isNaN(); return isValidDoubleOrFloatAndInvalidDecimal(s); }
	 */

	/**
	 * Check if the parameter can be stored as double or float, but not as decimal
	 * 
	 * </p>
	 * NaN, + and - infinity can't be represented by decimal
	 * </p>
	 * -> test if the parameter is one of them
	 * 
	 * @param s literal that is checked
	 * @return true if the parameter is Nan, + infinity, - infinity, else false
	 */
	public static boolean isValidDoubleOrFloatAndInvalidDecimal(String s) {
		return s.equals("NaN") || s.equals("INF") || s.equals("+INF") || s.equals("-INF");
	}

	/**
	 * Check if the parameter is a valid boolean
	 * 
	 * <p>
	 * Lexical values that are boolean true, false and 0 or 1
	 * </p>
	 * <p>
	 * {@link https://www.w3.org/TR/xmlschema11-2/#boolean}
	 * </p>
	 * @param s literal that is checked
	 * @return true if the parameter is one of true, false, 0 and 1, else false
	 */
	public static boolean isValidBoolean(String s) {
		return s.equals("true") || s.equals("false") || s.equals("1") || s.equals("0");
	}
	
	/**
	 * Check if a string can be interpreted as an integer
	 *
	 * @param s Literal that is checked
	 * @return true if the parameter can be stored as integer, else false
	 */
	public static boolean isValidInteger(String s) {
		//See
		//https://www.w3.org/TR/xmlschema11-2/#integer-lexical-representation
		//for regular expression of an integer
		Pattern pattern = Pattern.compile("^" // String beginning
				+ "(\\+|-)?" // optional sign
				+ "[0-9]+" //a finite sequence of digits
				+ "$" // End of the string
				);
		Matcher matcher = pattern.matcher(s);
		
		return matcher.find();
	}

	/**
	 * Creates a string containing the current date and time combined with the file
	 * name
	 * 
	 * @param fileName last substring of the generated file name
	 * @return string combined of current date time and the file name
	 */
	public static String createStorageFile(String fileName) {
		Date date = new Date();
		SimpleDateFormat dayFormatter = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat timeFormatter = new SimpleDateFormat("HH-mm-ss");
//		return dayFormatter.format(date) + "T" + timeFormatter.format(date) + "_" + fileName;
		return "_" + fileName;
	}

}
