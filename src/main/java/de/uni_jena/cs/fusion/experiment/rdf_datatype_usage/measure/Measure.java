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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;

/**
 * Abstraction of all measurement classes
 */
public abstract class Measure {

	/**
	 * Map for the results 
	 * key: datatype 
	 * value : map with 
	 * 	key: property 
	 * 	value: how often the combination occured
	 */
	protected Map<String, Map<String, Long>> occurs;

	public Measure() {
		occurs = new HashMap<String, Map<String, Long>>();
	}

	/**
	 * Conduct the measurement on a single statement
	 * 
	 * @param subject  of the statement
	 * @param property of the statement
	 * @param object   of the statement
	 */
	public abstract void measure(RDFNode subject, Property property, RDFNode object);

	/**
	 * Creates a list of {@link MeasureResult} that can be used to fill a database
	 * 
	 * @return a list of {@link MeasureResults}
	 */
	public List<MeasureResult> writeToDatabase() {
		List<MeasureResult> results = new ArrayList<MeasureResult>();
		for (String datatype : occurs.keySet()) {
			Map<String, Long> propertyQuantityMap = occurs.get(datatype);
			for (String property : propertyQuantityMap.keySet()) {
				results.add(new MeasureResult(this.getClass().getSimpleName(), property, datatype,
						propertyQuantityMap.get(property)));
			}
		}
		return results;
	}

	@Override
	public String toString() {
		String s = "\n" + this.getClass().getSimpleName() + ":";
		for (String datatype : occurs.keySet()) {
			Map<String, Long> propertyDatatypeMap = occurs.get(datatype);
			for (String property : propertyDatatypeMap.keySet()) {
				s += "\n\t" + datatype + "\t" + property + "\t" + propertyDatatypeMap.get(property);
			}
		}
		return s;
	}

	public Map<String, Map<String, Long>> getOccurs() {
		return occurs;
	}

}
