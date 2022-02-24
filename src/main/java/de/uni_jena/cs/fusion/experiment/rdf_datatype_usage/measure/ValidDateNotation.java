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

public class ValidDateNotation extends UsedAsDatatype {

	/**
	 * Check if parameter is formated like a valid xsd:date notation
	 * 
	 * @param s literal that is checked
	 * @return true if the parameter can be stored as xsd:date, else false
	 */
	@Override
	public boolean measure(String lexicalValue) {
		/*
		 * Regular expression for xsd:date see
		 * https://www.w3.org/TR/2012/REC-xmlschema11-2-20120405/datatypes.html#date
		 * 3.3.9.2 Lexical Mapping
		 */
		Pattern pattern = Pattern.compile("^" // beginning of the string
				+ "-?" // optional leading -
				+ "([1-9][0-9]{3,}|0[0-9]{3})-" // year
				+ "(0[1-9]|1[0-2])-" // month
				+ "(0[1-9]|[12][0-9]|3[01])" // day
				+ "(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00))?" // optional timezone offset
				+ "$" // end of the string
		);
		Matcher matcher = pattern.matcher(lexicalValue);
		return matcher.find();
	}

}
