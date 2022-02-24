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

import java.util.HashMap;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;

/**
 * Counts occurrences of an IRI as datatype of a literal
 * 
 */
public class UsedAsDatatype extends Measure {

	public boolean measure(String lexicalValue) {
		return true;
	}

	/**
	 * Conduct the measurement on the literal of the statement
	 * 
	 * @param propertyName - property of the statement, key for the HashMap
	 * @param literal      - object of the statement, is examined
	 */
	public void measure(String propertyName, Literal literal) {
		if (this.measure(literal.getLexicalForm())) {
			super.occurs.computeIfAbsent(literal.getDatatypeURI(), k -> new HashMap<String, Long>()).merge(propertyName,
					1L, Long::sum);
		}
	}

	@Override
	public final void measure(RDFNode subject, Property property, RDFNode object) {
		// Only statements with literals are of interest
		if (object.isLiteral()) {
			measure(property.getURI(), object.asLiteral());
		}
	}

}
