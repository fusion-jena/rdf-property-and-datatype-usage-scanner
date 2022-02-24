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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.jena.datatypes.xsd.impl.XSDBaseStringType;
import org.apache.jena.datatypes.xsd.impl.XSDDouble;
import org.apache.jena.datatypes.xsd.impl.XSDFloat;
import org.apache.jena.vocabulary.RDFS;


public final class MeasureTestUtil {

	public static final String predicateName = "test:predicate";

	public static final String doubleIRI = XSDDouble.XSDdouble.getURI();//"http://www.w3.org/2001/XMLSchema#double";
	public static final String floatIRI = XSDFloat.XSDfloat.getURI();//"http://www.w3.org/2001/XMLSchema#float";
	public static final String rangeIRI = RDFS.range.getURI();//"<http://www.w3.org/2000/01/rdf-schema#range>";
	public static final String stringIRI = XSDBaseStringType.XSDstring.getURI();//"http://www.w3.org/2001/XMLSchema#string";
	
	
	
	public static final String createDoubleLine(String object) {
		String line = "<test:subject> <" + predicateName + "> ";
		line += "\"" + object + "\"^^<" + doubleIRI + "> <test:graph>.";
		return line;
	}

	public static final String createFloatLine(String object) {
		String line = "<test:subject> <" + predicateName + "> ";
		line += "\"" + object + "\"^^<" + floatIRI + "> <test:graph>.";
		return line;
	}

	public static final String createStringLine(String object) {
		String line = "<test:subject> <" + MeasureTestUtil.predicateName + "> ";
		line += "\"" + object + "\" <test:graph>.";
		return line;
	}

	public static final String createPropertyRangeLine(String object) {
		String line = "<" + predicateName + "> <" + rangeIRI + "> <" + object + "> <test:graph>.";
		return line;
	}

	public static final void conductMeasurement(List<Measure> measures, org.slf4j.Logger log,
			String line) {
	    try {
	    	File tmpFile = File.createTempFile("data", ".nq");
	    	BufferedWriter writer = new BufferedWriter(new FileWriter(tmpFile.getAbsoluteFile()));
	        writer.write(line);
			writer.close();
			
			
			FileMeasure fileMeasure = new FileMeasure("file:///" + tmpFile.getAbsolutePath(), measures, log);
			fileMeasure.startMeasurements();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}

}
