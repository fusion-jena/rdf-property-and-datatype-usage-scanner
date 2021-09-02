package test.java.measurements;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.measurements.Measurement;
import main.java.measurements.ShouldBeDoubleOrFloatNotDecimal;
import main.java.utils.StringUtil;

class ShouldBeDoubleOrFloatNotDecimalTest {
	
	private List<String> invalidFloatNumbers = Arrays.asList("23 9707897", "3.34j23", "9789H23", "dsaf");
	
	private List<String> specialFloatingpointValues = Arrays.asList("Infinity", "-Infinity", "NaN");
	
	private List<String> validFloat = Arrays.asList("+0.0", "-0.0", "123E-12", "-0.625E-1", "100E-20", "98656E25", "3.4E38", "0.33333", "3.14129", "345.2349235");
	
	List<String> validDouble = Arrays.asList("5E-1", "1250E-4", "0.0", "0.52886675", "10E2");
	
	private org.slf4j.Logger log;
	private List<Measurement<?, ?>> measurements;
	private ShouldBeDoubleOrFloatNotDecimal m;


	@BeforeEach
	void init() {
		log = org.slf4j.LoggerFactory.getLogger("test");
		measurements = new ArrayList<Measurement<?, ?>>();
		m = new ShouldBeDoubleOrFloatNotDecimal();
		measurements.add(m);
	}
	
	/*****************************************************************/
	/*******************************Float*****************************/
	/*****************************************************************/
	
	@Test
	void testValidFloatValidDecimalNQ() {
		for(String number : validFloat) {
			String line = MeasurementTestUtil.createStringLine(number);
			MeasurementTestUtil.conductMeasurement(measurements, log, line);
			assertTrue(m.getOccurs().isEmpty());
		}
	}
	
	@Test
	void testValidFloatValidDecimalMethod() {
		List<Float> numbers = Arrays.asList(
				// Integer
				0f, +0f, -0f, -123f, 457000000f,
				// Floatingpoint
				1.5f, -1.5f, -0.000000234f, (float) 1 / 3, Float.MAX_VALUE, Float.MIN_VALUE);
		for (Float number : numbers) {
			assertFalse(StringUtil.isValidDoubleOrFloatAndInvalidDecimal(number + ""));
		}
		for (String number : validFloat) {
			assertFalse(StringUtil.isValidDoubleOrFloatAndInvalidDecimal(number));
		}
	}

	/*******************************************************************/
	/*****************************Double********************************/
	/*******************************************************************/
	
	@Test
	void testValidDoubleValidDecimalNQ() {
		for (String number : validDouble) {
			String line = MeasurementTestUtil.createStringLine(number);
			MeasurementTestUtil.conductMeasurement(measurements, log, line);
			assertTrue(m.getOccurs().isEmpty());
		}
		for (String number : validFloat) {
			String line = MeasurementTestUtil.createStringLine(number);
			MeasurementTestUtil.conductMeasurement(measurements, log, line);
			assertTrue(m.getOccurs().isEmpty());
		}
	}
	
	@Test
	void testValidDoubleValidDecimalMethod() {
		List<Double> numbers = Arrays.asList(0.5, 0.25, -0.125, +0.0, -0.0, 1.0, 10.0, -15.0, 3456.0, 1.5, -3.125, 0.2,
				-0.9, (double) (Math.pow(2, 10) + 7.1), (double) 1 / 3, (double) -9 / 7, 0.6,
				(double) Math.pow(5, -10));
		for (Double number : numbers) {
			assertFalse(StringUtil.isValidDoubleOrFloatAndInvalidDecimal(number + ""));
		}
		
		for (String number : validDouble) {
			assertFalse(StringUtil.isValidDoubleOrFloatAndInvalidDecimal(number + ""));
		}
	}
	
	/*******************************************************************/
	/***************************Float & Double**************************/
	/*******************************************************************/
	
	@Test
	void testValidFloatOrDoubleInvalidDecimalNQ() {
		for (String number: specialFloatingpointValues) {
			String line = MeasurementTestUtil.createStringLine(number);
			MeasurementTestUtil.conductMeasurement(measurements, log, line);
			assertEquals(1, m.getOccurs().get(MeasurementTestUtil.predicateName));
			m.getOccurs().clear();
		}
	}
	
	@Test
	void testValidFloatOrDoubleInvalidDecimalMethod() {
		for (String number : specialFloatingpointValues) {
			assertTrue(StringUtil.isValidDoubleOrFloatAndInvalidDecimal(number));
		}
	}
	
	@Test
	void testInvalidNumberNQ() {
		for (String number : invalidFloatNumbers) {
			String line = MeasurementTestUtil.createStringLine(number);
			MeasurementTestUtil.conductMeasurement(measurements, log, line);
			assertTrue(m.getOccurs().isEmpty());
		}
	}
	
	@Test
	void testInvalidNumberMethod() {
		for (String number : invalidFloatNumbers) {
			assertFalse(StringUtil.isValidDoubleOrFloatAndInvalidDecimal(number));
		}
	}

}
