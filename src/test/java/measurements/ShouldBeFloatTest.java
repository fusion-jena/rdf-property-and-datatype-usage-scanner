package test.java.measurements;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.measurements.Measurement;
import main.java.measurements.ShouldBeFloat;
import main.java.utils.StringUtil;

public class ShouldBeFloatTest {
	
private List<String> specialValues = Arrays.asList("NaN", "Infinity", "-Infinity");
	
	private List<String> invalidNumbers = Arrays.asList("235 34.324", "3A-5", "--34", "12,56");
	
	private List<String> validFloatingpointNumber = Arrays.asList("5E-1", "1250E-4", "0.0", "+0.0", "-0.0", "37.25", "314", "0.1875", "10E2");
	
	private List<String> invalidFloatingpointNumber = Arrays.asList("-0.1", "2e-1", "0.33333333", "-909E-2", "3.14159e-5", "10e-12", "534609887");
	//534609887 is valid double but invalid float
	
	
	private org.slf4j.Logger log;
	private List<Measurement<?, ?>> measurements;
	private ShouldBeFloat m;
	
	@BeforeEach
	void init() {
		log = org.slf4j.LoggerFactory.getLogger("test");
		measurements = new ArrayList<Measurement<?, ?>>();
		m = new ShouldBeFloat();
		measurements.add(m);
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
			String line = MeasurementTestUtil.createStringLine(value);
			MeasurementTestUtil.conductMeasurement(measurements, log, line);
			assertEquals(1, m.getOccurs().get(MeasurementTestUtil.predicateName));
			m.getOccurs().clear();
		}
	}
	
	@Test 
	void validFloatInvalidDecimalNQ() {
		for (String value : specialValues) {
			String line = MeasurementTestUtil.createStringLine(value);
			MeasurementTestUtil.conductMeasurement(measurements, log, line);
			assertEquals(1, m.getOccurs().get(MeasurementTestUtil.predicateName));
			m.getOccurs().clear();
		}
	}
	
	@Test
	void invalidNumbersNQ() {
		for (String value : invalidNumbers) {
			String line = MeasurementTestUtil.createStringLine(value);
			MeasurementTestUtil.conductMeasurement(measurements, log, line);
			assertTrue(m.getOccurs().isEmpty());
		}
	}

	@Test
	void invalidFloatValidDecimalNQ() {
		for (String value : invalidFloatingpointNumber) {
			String line = MeasurementTestUtil.createStringLine(value);
			MeasurementTestUtil.conductMeasurement(measurements, log, line);
			assertTrue(m.getOccurs().isEmpty());
		}
	}

}
