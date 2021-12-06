package blind.experiment.rdf_datatype_usage.measure;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import blind.experiment.rdf_datatype_usage.measure.Measure;
import blind.experiment.rdf_datatype_usage.measure.UsedAsDatatype;

class UsedAsDatatypeTest {
	
	private org.slf4j.Logger log;
	private List<Measure> measures;
	private UsedAsDatatype m;
	
	@BeforeEach
	void init() {
		log = org.slf4j.LoggerFactory.getLogger("test");
		measures = new ArrayList<Measure>();
		m = new UsedAsDatatype();
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
		assertTrue(m.getOccurs().containsKey(MeasureTestUtil.doubleIRI));
		assertEquals(1, m.getOccurs().get(MeasureTestUtil.doubleIRI).keySet().size());
		assertTrue(m.getOccurs().get(MeasureTestUtil.doubleIRI).containsKey(MeasureTestUtil.predicateName));
		assertEquals(values.size(), m.getOccurs().get(MeasureTestUtil.doubleIRI).get(MeasureTestUtil.predicateName));
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
		line = MeasureTestUtil.createPropertyRangeLine(MeasureTestUtil.doubleIRI);
		MeasureTestUtil.conductMeasurement(measures, log, line);
		
		assertEquals(3, m.getOccurs().keySet().size());
		
		assertTrue(m.getOccurs().containsKey(MeasureTestUtil.doubleIRI));
		assertEquals(1, m.getOccurs().get(MeasureTestUtil.doubleIRI).keySet().size());
		assertTrue(m.getOccurs().get(MeasureTestUtil.doubleIRI).containsKey(MeasureTestUtil.predicateName));
		assertEquals(1, m.getOccurs().get(MeasureTestUtil.doubleIRI).get(MeasureTestUtil.predicateName));
		
		assertTrue(m.getOccurs().containsKey(MeasureTestUtil.floatIRI));
		assertEquals(1, m.getOccurs().get(MeasureTestUtil.floatIRI).keySet().size());
		assertTrue(m.getOccurs().get(MeasureTestUtil.floatIRI).containsKey(MeasureTestUtil.predicateName));
		assertEquals(1, m.getOccurs().get(MeasureTestUtil.floatIRI).get(MeasureTestUtil.predicateName));
		
		assertTrue(m.getOccurs().containsKey(MeasureTestUtil.stringIRI));
		assertEquals(1, m.getOccurs().get(MeasureTestUtil.stringIRI).keySet().size());
		assertTrue(m.getOccurs().get(MeasureTestUtil.stringIRI).containsKey(MeasureTestUtil.predicateName));
		assertEquals(1, m.getOccurs().get(MeasureTestUtil.stringIRI).get(MeasureTestUtil.predicateName));
		
	}
	
	@Test
	void multiplePredicatsOneDatatype() {
		String line;
		line = "<test:subject> <predicate_1> \"" + 20.5 + "\"^^<" + MeasureTestUtil.floatIRI + "> <test:graph>.";
		MeasureTestUtil.conductMeasurement(measures, log, line);
		line = "<test:subject> <predicate_1> \"" + 788.0767 + "\"^^<" + MeasureTestUtil.floatIRI + "> <test:graph>.";
		MeasureTestUtil.conductMeasurement(measures, log, line);
		line = "<test:subject> <predicate_1> \"" + 666.666 + "\"^^<" + MeasureTestUtil.floatIRI + "> <test:graph>.";
		MeasureTestUtil.conductMeasurement(measures, log, line);
		
		line = "<test:subject> <predicate_2> \"" + 323434 + "\"^^<" + MeasureTestUtil.floatIRI + "> <test:graph>.";
		MeasureTestUtil.conductMeasurement(measures, log, line);
		line = "<test:subject> <predicate_2> \"" + 9999 + "\"^^<" + MeasureTestUtil.floatIRI + "> <test:graph>.";
		MeasureTestUtil.conductMeasurement(measures, log, line);
		
		assertEquals(1, m.getOccurs().keySet().size());
		assertTrue(m.getOccurs().containsKey(MeasureTestUtil.floatIRI));
		assertEquals(2, m.getOccurs().get(MeasureTestUtil.floatIRI).keySet().size());
		assertTrue(m.getOccurs().get(MeasureTestUtil.floatIRI).containsKey("predicate_1"));
		assertEquals(3, m.getOccurs().get(MeasureTestUtil.floatIRI).get("predicate_1"));
		assertTrue(m.getOccurs().get(MeasureTestUtil.floatIRI).containsKey("predicate_2"));
		assertEquals(2, m.getOccurs().get(MeasureTestUtil.floatIRI).get("predicate_2"));
	}
	
	@Test
	void multiplePredicatesMultipleDatatypes() {
		String line;
		line = "<test:subject> <predicate_1> \"" + 20.15 + "\"^^<" + MeasureTestUtil.floatIRI + "> <test:graph>.";
		MeasureTestUtil.conductMeasurement(measures, log, line);
		line = "<test:subject> <predicate_1> \"" + 78.80767 + "\"^^<" + MeasureTestUtil.doubleIRI + "> <test:graph>.";
		MeasureTestUtil.conductMeasurement(measures, log, line);
		line = "<test:subject> <predicate_1> \"" + "fdsowenxkj jdsoixjwer" + "\"" + " <test:graph>.";
		MeasureTestUtil.conductMeasurement(measures, log, line);
		
		line = "<test:subject> <predicate_2> \"" + 3.14159 + "\"^^<" + MeasureTestUtil.floatIRI + "> <test:graph>.";
		MeasureTestUtil.conductMeasurement(measures, log, line);
		line = "<test:subject> <predicate_2> \"" + "dswewr jsdf .,kjwer" + "\"" + " <test:graph>.";
		MeasureTestUtil.conductMeasurement(measures, log, line);
		
		assertEquals(3, m.getOccurs().keySet().size());
		
		assertTrue(m.getOccurs().containsKey(MeasureTestUtil.doubleIRI));
		assertEquals(1 ,m.getOccurs().get(MeasureTestUtil.doubleIRI).keySet().size());
		assertTrue(m.getOccurs().get(MeasureTestUtil.doubleIRI).containsKey("predicate_1"));
		assertEquals(1 ,m.getOccurs().get(MeasureTestUtil.doubleIRI).get("predicate_1"));
		
		assertTrue(m.getOccurs().containsKey(MeasureTestUtil.floatIRI));
		assertEquals(2 ,m.getOccurs().get(MeasureTestUtil.floatIRI).keySet().size());
		assertTrue(m.getOccurs().get(MeasureTestUtil.floatIRI).containsKey("predicate_1"));
		assertEquals(1 ,m.getOccurs().get(MeasureTestUtil.floatIRI).get("predicate_1"));
		assertTrue(m.getOccurs().get(MeasureTestUtil.floatIRI).containsKey("predicate_2"));
		assertEquals(1 ,m.getOccurs().get(MeasureTestUtil.floatIRI).get("predicate_2"));
		
		assertTrue(m.getOccurs().containsKey(MeasureTestUtil.stringIRI));
		assertEquals(2 ,m.getOccurs().get(MeasureTestUtil.stringIRI).keySet().size());
		assertTrue(m.getOccurs().get(MeasureTestUtil.stringIRI).containsKey("predicate_1"));
		assertEquals(1 ,m.getOccurs().get(MeasureTestUtil.floatIRI).get("predicate_1"));
		assertTrue(m.getOccurs().get(MeasureTestUtil.floatIRI).containsKey("predicate_2"));
		assertEquals(1 ,m.getOccurs().get(MeasureTestUtil.floatIRI).get("predicate_2"));
	}


}
