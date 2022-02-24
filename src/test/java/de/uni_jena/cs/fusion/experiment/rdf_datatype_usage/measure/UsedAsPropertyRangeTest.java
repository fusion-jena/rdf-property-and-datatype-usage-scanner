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

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.jena.datatypes.xsd.impl.XSDDouble;
import org.apache.jena.datatypes.xsd.impl.XSDFloat;
import org.apache.jena.vocabulary.XSD;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UsedAsPropertyRangeTest {

	private org.slf4j.Logger log;
	private List<Measure> measures;
	private UsedAsPropertyRange m;
	
	@BeforeEach
	void init() {
		log = org.slf4j.LoggerFactory.getLogger("test");
		measures = new ArrayList<Measure>();
		m = new UsedAsPropertyRange();
		measures.add(m);
	}
	
	@Test
	void propertyRangeStatementNQ() {
		List<String> datatypes = Arrays.asList(
				XSDDouble.XSDdouble.getURI(),//"http://www.w3.org/2001/XMLSchema#double",
				XSD.integer.getURI(),//"http://www.w3.org/2001/XMLSchema#int", 
				XSDFloat.XSDfloat.getURI(),//"http://www.w3.org/2001/XMLSchema#float", 
				XSD.dateTime.getURI()//"http://www.w3.org/2001/XMLSchema#dateTime"
				);
		
		for (String type : datatypes) {
			String line = MeasureTestUtil.createPropertyRangeLine(type);
			MeasureTestUtil.conductMeasurement(measures, log, line);
			assertEquals(1, m.getOccurs().get(type).get(MeasureTestUtil.predicateName));
			m.getOccurs().clear();
		}
	}
	
	@Test
	void otherStatementNQ() {
		List<String> objects = Arrays.asList(
				"78345",
				"test test test", 
				"2020-12-19T11:11:11",
				"53.5"
				);
		
		for (String object : objects) {
			String line = MeasureTestUtil.createStringLine(object);
			MeasureTestUtil.conductMeasurement(measures, log, line);
			assertTrue(m.getOccurs().isEmpty());
		}
		
		String line = MeasureTestUtil.createFloatLine("5025e-2");
		MeasureTestUtil.conductMeasurement(measures, log, line);
		assertTrue(m.getOccurs().isEmpty());
		
		line = MeasureTestUtil.createDoubleLine("90");
		MeasureTestUtil.conductMeasurement(measures, log, line);
		assertTrue(m.getOccurs().isEmpty());
	}

}
