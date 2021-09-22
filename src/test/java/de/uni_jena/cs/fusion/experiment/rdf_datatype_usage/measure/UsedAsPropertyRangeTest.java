package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measure;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UsedAsPropertyRangeTest {

	private org.slf4j.Logger log;
	private List<Measure<?, ?>> measures;
	private UsedAsPropertyRange m;
	
	@BeforeEach
	void init() {
		log = org.slf4j.LoggerFactory.getLogger("test");
		measures = new ArrayList<Measure<?,?>>();
		m = new UsedAsPropertyRange();
		measures.add(m);
	}
	
	@Test
	void propertyRangeStatementNQ() {
		List<String> datatypes = Arrays.asList(
				"http://www.w3.org/2001/XMLSchema#double",
				"http://www.w3.org/2001/XMLSchema#int", 
				"http://www.w3.org/2001/XMLSchema#float", 
				"http://www.w3.org/2001/XMLSchema#dateTime"
				);
		
		for (String type : datatypes) {
			String line = MeasureTestUtil.createPropertyRangeLine(type);
			MeasureTestUtil.conductMeasurement(measures, log, line);
			assertTrue(m.getOccurs().containsKey(MeasureTestUtil.predicateName));
			assertEquals(1, m.getOccurs().get(MeasureTestUtil.predicateName).get(type));
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
