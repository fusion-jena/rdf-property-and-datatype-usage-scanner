/**
 * The MIT License
 * Copyright © 2021 Heinz Nixdorf Chair for Distributed Information Systems, Friedrich Schiller University Jena
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measure;

import java.math.BigDecimal;

public class UnpreciseRepresentableInFloat extends UsedAsDatatype {

	/**
	 * Checks if a float should be replaced by decimal
	 * 
	 * Checks whether the parameter can be represented as decimal and if so, whether
	 * using decimal, instead of float, would produce more accurate results
	 * 
	 * @param floatValue   float value that will be checked
	 * @param lexicalValue lexical representation of the float value
	 * @return true if the number can be represented as decimal AND using decimal
	 *         would lead to more accurate results, else false
	 */
	@Override
	public boolean measure(String lexicalValue) {
		try {
			Float floatValue = Float.valueOf(lexicalValue);

			// special values of float, that can't be represented by decimal
			if (floatValue.isInfinite() || floatValue.isNaN()) {
				return false;
			}

			// see
			// https://docs.oracle.com/javase/1.5.0/docs/api/java/math/BigDecimal.html#BigDecimal(double)
			// Get the exact representation of the value of the double
			// Exact conversion -> "unpredictable behavior"
			BigDecimal floatDecimal = new BigDecimal(floatValue);

			// Convert the lexical value of the float variable into Big Integer
			// represents the expected value
			// "Predictable behavior"
			BigDecimal stringDecimal = new BigDecimal(lexicalValue);

			// Compare exact representation with the expected value
			// A decimal should be used, if the values are not the same
			return floatDecimal.compareTo(stringDecimal) != 0;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
