package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measure;

import java.math.BigDecimal;

public class UnpreciseRepresentableInDouble extends UsedAsDatatype {

	/**
	 * Checks if a double should be replaced by decimal
	 * 
	 * Checks whether the parameter can be represented as decimal and if so, whether
	 * using decimal, instead of double, would produce more accurate results
	 * 
	 * @param doubleValue  double variable that will be checked
	 * @param lexicalValue lexical representation of the double value
	 * @return true if the number can be represented as decimal AND using decimal
	 *         would lead to more accurate results, else false
	 */
	@Override
	public boolean measure(String lexicalValue) {
		try {
			Double doubleValue = Double.valueOf(lexicalValue);
			// special values of double, that can't be represented by decimal
			if (doubleValue.isInfinite() || doubleValue.isNaN()) {
				return false;
			}

			// see
			// https://docs.oracle.com/javase/1.5.0/docs/api/java/math/BigDecimal.html#BigDecimal(double)
			// Get the exact representation of the value of the double
			// Exact conversion -> "unpredictable behavior"
			BigDecimal doubleDecimal = new BigDecimal(doubleValue);

			// Convert the lexical value into Big Decimal
			// represents the expected value
			// "Predictable behavior"
			BigDecimal stringDecimal = new BigDecimal(lexicalValue);

			// Compare exact representation with the expected value
			// A decimal should be used, if the values are not the same
			return doubleDecimal.compareTo(stringDecimal) != 0;
		} catch (NumberFormatException e) {
			return false;
		}
	}

}
