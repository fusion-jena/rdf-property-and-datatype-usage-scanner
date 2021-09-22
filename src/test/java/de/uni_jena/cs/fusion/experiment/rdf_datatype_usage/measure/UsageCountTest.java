package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measure;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UsageCountTest {
	
	private org.slf4j.Logger log;
	private List<Measure<?, ?>> measures;
	private UsageCount m;
	
	private String doubleLink = "http://www.w3.org/2001/XMLSchema#double";
	private String floatLink = "http://www.w3.org/2001/XMLSchema#float";
	
	@BeforeEach
	void init() {
		log = org.slf4j.LoggerFactory.getLogger("test");
		measures = new ArrayList<Measure<?,?>>();
		m = new UsageCount();
		measures.add(m);
	}
	
	@Test
	void onePredicateOneDatatype() {
		String line;
		List<String> values = Arrays.asList("0.122346", "0.456856", "46365", "658453");
		for(String value : values) {
			line = MeasureTestUtil.createDoubleLine(value);
			MeasureTestUtil.conductMeasurement(measures, log, line);
		}
		assertEquals(1, m.getOccurs().keySet().size());
		assertEquals(values.size(), m.getOccurs().get(MeasureTestUtil.predicateName).get(doubleLink));
	}
	
	@Test
	void onePredicateMultipleDatatypes() {
		String line;
		line = MeasureTestUtil.createDoubleLine("0.122346");
		MeasureTestUtil.conductMeasurement(measures, log, line);
		line = MeasureTestUtil.createFloatLine("0.5");
		MeasureTestUtil.conductMeasurement(measures, log, line);
		line = MeasureTestUtil.createStringLine("bbbb");
		MeasureTestUtil.conductMeasurement(measures, log, line);
		line = MeasureTestUtil.createPropertyRangeLine(doubleLink);
		MeasureTestUtil.conductMeasurement(measures, log, line);
		assertEquals(1, m.getOccurs().keySet().size());
		assertEquals(3, m.getOccurs().get(MeasureTestUtil.predicateName).keySet().size());
	}
	
	@Test
	void multiplePredicatsOneDatatype() {
		String line;
		line = "<test:subject> <predicate_1> \"" + 20.5 + "\"^^" + MeasureTestUtil.floatIRI + " <test:graph>.";
		MeasureTestUtil.conductMeasurement(measures, log, line);
		line = "<test:subject> <predicate_1> \"" + 788.0767 + "\"^^" + MeasureTestUtil.floatIRI + " <test:graph>.";
		MeasureTestUtil.conductMeasurement(measures, log, line);
		line = "<test:subject> <predicate_1> \"" + 666.666 + "\"^^" + MeasureTestUtil.floatIRI + " <test:graph>.";
		MeasureTestUtil.conductMeasurement(measures, log, line);
		
		line = "<test:subject> <predicate_2> \"" + 323434 + "\"^^" + MeasureTestUtil.floatIRI + " <test:graph>.";
		MeasureTestUtil.conductMeasurement(measures, log, line);
		line = "<test:subject> <predicate_2> \"" + 9999 + "\"^^" + MeasureTestUtil.floatIRI + " <test:graph>.";
		MeasureTestUtil.conductMeasurement(measures, log, line);
		
		assertEquals(2, m.getOccurs().keySet().size());
		assertEquals(1, m.getOccurs().get("predicate_1").keySet().size());
		assertEquals(3, m.getOccurs().get("predicate_1").get(floatLink));
		assertEquals(1, m.getOccurs().get("predicate_2").keySet().size());
		assertEquals(2, m.getOccurs().get("predicate_2").get(floatLink));
	}
	
	@Test
	void multiplePredicatesMultipleDatatypes() {
		String line;
		line = "<test:subject> <predicate_1> \"" + 20.15 + "\"^^" + MeasureTestUtil.floatIRI + " <test:graph>.";
		MeasureTestUtil.conductMeasurement(measures, log, line);
		line = "<test:subject> <predicate_1> \"" + 78.80767 + "\"^^" + MeasureTestUtil.doubleIRI + " <test:graph>.";
		MeasureTestUtil.conductMeasurement(measures, log, line);
		line = "<test:subject> <predicate_1> \"" + "fdsowenxkj jdsoixjwer" + "\"" + " <test:graph>.";
		MeasureTestUtil.conductMeasurement(measures, log, line);
		
		line = "<test:subject> <predicate_2> \"" + 3.14159 + "\"^^" + MeasureTestUtil.floatIRI + " <test:graph>.";
		MeasureTestUtil.conductMeasurement(measures, log, line);
		line = "<test:subject> <predicate_2> \"" + "dswewr jsdf .,kjwer" + "\"" + " <test:graph>.";
		MeasureTestUtil.conductMeasurement(measures, log, line);
		
		assertEquals(2, m.getOccurs().keySet().size());
		assertEquals(3, m.getOccurs().get("predicate_1").keySet().size());
		assertEquals(2, m.getOccurs().get("predicate_2").keySet().size());
	}


}
