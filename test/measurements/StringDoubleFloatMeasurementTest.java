package measurements;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import utils.GlobalNames;
import utils.StringUtil;

class StringDoubleFloatMeasurementTest {
	
	private List<String> specialValues = Arrays.asList("NaN", "Infinity", "-Infinity");
	
	private List<String> invalidNumbers = Arrays.asList("235 34.324", "3A-5", "--34", "12,56");
	
	private List<String> validFloatingpointNumber = Arrays.asList("5E-1", "1250E-4", "0.0", "+0.0", "-0.0", "37.25", "314", "0.1875", "10E2");
	
	private List<String> invalidFloatingpointNumber = Arrays.asList("-0.1", "2e-1", "0.33333333", "-909E-2", "3.14159e-5", "10e-12");
	
	private org.slf4j.Logger log;
	private List<Measurement<?, ?>> measurements;
	private StringDoubleFloatMeasurement m;


	@BeforeEach
	void init() {
		log = org.slf4j.LoggerFactory.getLogger("test");
		measurements = new ArrayList<Measurement<?, ?>>();
		m = new StringDoubleFloatMeasurement();
		measurements.add(m);
	}
		
	/**************************************************************/
	/***************************Double*****************************/
	/**************************************************************/
		
	@Test
	void validDoubleInvalidDecimalMethod() {
		for (String number : specialValues) {
			assertTrue(StringUtil.isValidDouble(number));
		}
	}
	
	@Test
	void validDoubleValidDecimalMethod() {
		List<Double> numbers = Arrays.asList(0.5, 0.25, -0.125, +0.0, -0.0, 1.0, 10.0, -15.0, 3456.0, 1.5, -3.125);
		for (Double number : numbers) {
			assertTrue(StringUtil.isValidDouble(number + ""));
		}
		for (String number : validFloatingpointNumber) {
			assertTrue(StringUtil.isValidDouble(number));
		}
	}
	
	void validDoubleValidDecimalNQ() {
		String line = MeasurementTestUtil.createStringLine("534609887");
		MeasurementTestUtil.conductMeasurement(measurements, log, line);
		assertFalse(m.getOccurs().get(MeasurementTestUtil.predicateName).containsKey(GlobalNames.FLOAT));
		assertEquals(1, m.getOccurs().get(MeasurementTestUtil.predicateName).get(GlobalNames.DOUBLE));
		m.getOccurs().clear();
	}
	
	@Test
	void invalidDoubleValidDecimalMethod() {
		// https://www.h-schmidt.net/FloatConverter/IEEE754de.html
		List<Double> numbers = Arrays.asList(0.2, -0.9, (double) (Math.pow(2, 10) + 7.1),
				(double) 1 / 3, (double) -9 / 7, 0.6, (double) Math.pow(5, -10));
		for (Double number : numbers) {
			assertFalse(StringUtil.isValidDouble(number + ""));
		}
	}

	@Test
	void invalidDoubleMethod() {
		for (String number : invalidNumbers) {
			assertFalse(StringUtil.isValidFloat(number));
		}
	}
	
	/**************************************************************/
	/****************************Float*****************************/
	/**************************************************************/
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
	
	/**************************************************************/
	/************************Float & Double************************/
	/**************************************************************/

	@Test
	void validFloatValidDoubleValidDecimalNQ() {
		for (String value : validFloatingpointNumber) {
			String line = MeasurementTestUtil.createStringLine(value);
			MeasurementTestUtil.conductMeasurement(measurements, log, line);
			assertEquals(2, m.getOccurs().get(MeasurementTestUtil.predicateName).size());
			assertEquals(1, m.getOccurs().get(MeasurementTestUtil.predicateName).get(GlobalNames.FLOAT));
			assertEquals(1, m.getOccurs().get(MeasurementTestUtil.predicateName).get(GlobalNames.DOUBLE));
			m.getOccurs().clear();
		}
	}
	
	@Test 
	void validFloatValidDoubleInvalidDecimalNQ() {
		for (String value : specialValues) {
			String line = MeasurementTestUtil.createStringLine(value);
			MeasurementTestUtil.conductMeasurement(measurements, log, line);
			assertEquals(2, m.getOccurs().get(MeasurementTestUtil.predicateName).keySet().size());
			assertEquals(1, m.getOccurs().get(MeasurementTestUtil.predicateName).get(GlobalNames.DOUBLE));
			assertEquals(1, m.getOccurs().get(MeasurementTestUtil.predicateName).get(GlobalNames.FLOAT));
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
	void invalidFloatInvalidDoubleInvalidDecimalNQ() {
		for (String value : invalidFloatingpointNumber) {
			String line = MeasurementTestUtil.createStringLine(value);
			MeasurementTestUtil.conductMeasurement(measurements, log, line);
			assertTrue(m.getOccurs().isEmpty());
		}
	}
}
