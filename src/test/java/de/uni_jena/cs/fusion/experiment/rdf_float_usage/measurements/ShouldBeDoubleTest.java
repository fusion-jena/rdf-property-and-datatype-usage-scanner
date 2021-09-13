package de.uni_jena.cs.fusion.experiment.rdf_float_usage.measurements;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.uni_jena.cs.fusion.experiment.rdf_float_usage.utils.StringUtil;
import de.uni_jena.cs.fusion.experiment.rdf_float_usage.measurements.ShouldBeDouble;
import de.uni_jena.cs.fusion.experiment.rdf_float_usage.measurements.Measurement;

public class ShouldBeDoubleTest {

	private List<String> specialValues = Arrays.asList("NaN", "Infinity", "-Infinity");
	
	private List<String> invalidNumbers = Arrays.asList("235 34.324", "3A-5", "--34", "12,56");
	
	private List<String> validFloatingpointNumber = Arrays.asList("5E-1", "1250E-4", "0.0", "+0.0", "-0.0", "37.25", "314", "0.1875", "10E2", "534609887");
	// 534609887 is valid double but invalid float
	
	private List<String> invalidFloatingpointNumber = Arrays.asList("-0.1", "2e-1", "0.33333333", "-909E-2", "3.14159e-5", "10e-12");
	
	private org.slf4j.Logger log;
	private List<Measurement<?, ?>> measurements;
	private ShouldBeDouble m;
	
	@BeforeEach
	void init() {
		log = org.slf4j.LoggerFactory.getLogger("test");
		measurements = new ArrayList<Measurement<?, ?>>();
		m = new ShouldBeDouble();
		measurements.add(m);
	}
	
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
	
	@Test
	void validDoubleValidDecimalNQ() {
		for (String value : validFloatingpointNumber) {
			String line = MeasurementTestUtil.createStringLine(value);
			MeasurementTestUtil.conductMeasurement(measurements, log, line);
			assertEquals(1, m.getOccurs().get(MeasurementTestUtil.predicateName));
			m.getOccurs().clear();
		}
	}
	
	@Test 
	void validDoubleInvalidDecimalNQ() {
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
	void invalidDoubleValidDecimalNQ() {
		for (String value : invalidFloatingpointNumber) {
			String line = MeasurementTestUtil.createStringLine(value);
			MeasurementTestUtil.conductMeasurement(measurements, log, line);
			assertTrue(m.getOccurs().isEmpty());
		}
	}
}
