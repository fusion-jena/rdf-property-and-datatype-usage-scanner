package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils.StringUtil;

public class CouldBeFloatTest {
	
private List<String> specialValues = Arrays.asList("NaN", "Infinity", "-Infinity");
	
	private List<String> invalidNumbers = Arrays.asList("235 34.324", "3A-5", "--34", "12,56");
	
	private List<String> validFloatingpointNumber = Arrays.asList("5E-1", "1250E-4", "0.0", "+0.0", "-0.0", "37.25", "314", "0.1875", "10E2");
	
	private List<String> invalidFloatingpointNumber = Arrays.asList("-0.1", "2e-1", "0.33333333", "-909E-2", "3.14159e-5", "10e-12", "534609887");
	//534609887 is valid double but invalid float
	
	
	private org.slf4j.Logger log;
	private List<Measure> measures;
	private CouldBeFloat m;
	
	@BeforeEach
	void init() {
		log = org.slf4j.LoggerFactory.getLogger("test");
		measures = new ArrayList<Measure>();
		m = new CouldBeFloat();
		measures.add(m);
	}
	
	@Test
	void validFloatInvalidDecimalMethod() {
		for (String number : specialValues) {
			assertTrue(StringUtil.isValidFloat(number));
		}
	}

	@Test
	void validFloatValidDecimalMethod() {
		// Kommadarstellung
		List<Float> numbers = Arrays.asList(
				// Integer
				0.0f, +0.0f, -0.0f, -123f, 457000000f, -8908f, (float) (Math.pow(2, 10)),
				// Kommazahlen
				1.5f, -1.5f, (float) (Math.pow(2, 11)), (float) (-1 / 8));

		for (Float number : numbers) {
			assertTrue(StringUtil.isValidFloat(number + ""));
		}

		for (String number : validFloatingpointNumber) {
			assertTrue(StringUtil.isValidFloat(number));
		}
	}

	@Test
	public void invalidFloatValidDecimalMethod() {
		// https://www.h-schmidt.net/FloatConverter/IEEE754de.html
		List<Float> numbers = Arrays.asList(0.2f, -0.9f, (float) (Math.pow(2, 10) + 7.1), (float) 1 / 3, (float) -9 / 7,
				(float) 0.6, (float) Math.pow(5, -10));
		for (Float number : numbers) {
			assertFalse(StringUtil.isValidFloat(number + ""));
		}
	}

	@Test
	public void invalidFloatMethod() {
		for (String number : invalidNumbers) {
			assertFalse(StringUtil.isValidFloat(number));
		}
	}
	
	@Test
	void validFloatValidDecimalNQ() {
		for (String value : validFloatingpointNumber) {
			String line = MeasureTestUtil.createStringLine(value);
			MeasureTestUtil.conductMeasurement(measures, log, line);
			assertEquals(1, m.getOccurs().keySet().size());
			assertTrue(m.getOccurs().containsKey(MeasureTestUtil.stringIRI));
			assertEquals(1, m.getOccurs().get(MeasureTestUtil.stringIRI).keySet().size());
			assertTrue(m.getOccurs().get(MeasureTestUtil.stringIRI).containsKey(MeasureTestUtil.predicateName));
			assertEquals(1, m.getOccurs().get(MeasureTestUtil.stringIRI).get(MeasureTestUtil.predicateName));
			m.getOccurs().clear();
		}
	}
	
	@Test 
	void validFloatInvalidDecimalNQ() {
		for (String value : specialValues) {
			String line = MeasureTestUtil.createStringLine(value);
			MeasureTestUtil.conductMeasurement(measures, log, line);
			assertEquals(1, m.getOccurs().keySet().size());
			assertTrue(m.getOccurs().containsKey(MeasureTestUtil.stringIRI));
			assertEquals(1, m.getOccurs().get(MeasureTestUtil.stringIRI).keySet().size());
			assertTrue(m.getOccurs().get(MeasureTestUtil.stringIRI).containsKey(MeasureTestUtil.predicateName));
			assertEquals(1, m.getOccurs().get(MeasureTestUtil.stringIRI).get(MeasureTestUtil.predicateName));
			m.getOccurs().clear();
		}
	}
	
	@Test
	void invalidNumbersNQ() {
		for (String value : invalidNumbers) {
			String line = MeasureTestUtil.createStringLine(value);
			MeasureTestUtil.conductMeasurement(measures, log, line);
			assertTrue(m.getOccurs().isEmpty());
		}
	}

	@Test
	void invalidFloatValidDecimalNQ() {
		for (String value : invalidFloatingpointNumber) {
			String line = MeasureTestUtil.createStringLine(value);
			MeasureTestUtil.conductMeasurement(measures, log, line);
			assertTrue(m.getOccurs().isEmpty());
		}
	}

}
