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

/**
 * Represents the result of a measure
 * 
 * <p>
 * Used for inserting the results into the database.
 * </p>
 * <p>
 * Represents a single entry
 * </p>
 */
public class MeasureResult {

	/**
	 * Name of the measure
	 */
	private String measure;
	private String property;
	private String datatype;
	private Long quantity;

	public MeasureResult(String measure, String property, String datatype, Long quantity) {
		this.measure = measure;
		this.property = property;
		this.datatype = datatype;
		this.quantity = quantity;
	}

	public String getMeasure() {
		return measure;
	}

	public String getProperty() {
		return property;
	}

	public String getDatatype() {
		return datatype;
	}

	public Long getQuantity() {
		return quantity;
	}

}
