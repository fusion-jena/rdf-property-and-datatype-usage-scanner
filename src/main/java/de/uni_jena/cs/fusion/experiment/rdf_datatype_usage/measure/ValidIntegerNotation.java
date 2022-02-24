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
