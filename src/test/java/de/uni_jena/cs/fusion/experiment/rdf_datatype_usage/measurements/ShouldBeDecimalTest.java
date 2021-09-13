package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measurements;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measurements.Measurement;
import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measurements.ShouldBeDecimal;
import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils.GlobalNames;
import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils.NumberUtil;
import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils.StringUtil;

public class ShouldBeDecimalTest {
	
	private org.slf4j.Logger log;
	private List<Measurement<?, ?>> measurements;
	private ShouldBeDecimal m;

	
	@BeforeEach
	void init() {
		log = org.slf4j.LoggerFactory.getLogger("test");
		measurements = new ArrayList<Measurement<?, ?>>();
		m = new ShouldBeDecimal();
		measurements.add(m);
	}
	
	/********************************************************************
	 ***************************** Double *******************************
	 ********************************************************************/
	List<String> specialValues = Arrays.asList(
			"NaN", 
			"INF", 
			"-INF");
	
	List<String> validDoubleValues = Arrays.asList(
			"0.0",
			"-0.0",
			"0.5",
			"3125e-5",
			"10.5",
			"314129",
			"53460.9887E4",
			"314125E-3",
			"1.024E3",
			"-9.765625e-4"
			);
	
	List<String> invalidDoubleValues = Arrays.asList(
			"0.1",
			"0.2",
			"200E-3",
			"-10.71",
			"5876E-2");
	
	@Test
	void replaceDoubleByDecimalNQ() {
		for (String value : invalidDoubleValues) {
			String line = MeasurementTestUtil.createDoubleLine(value);
			MeasurementTestUtil.conductMeasurement(measurements, log, line);
			//only double entries
			assertFalse(m.getOccurs().get(MeasurementTestUtil.predicateName).containsKey(GlobalNames.FLOAT)); 
			assertFalse(m.getOccurs().get(MeasurementTestUtil.predicateName).containsKey(GlobalNames.STRING));
			assertEquals(1, m.getOccurs().get(MeasurementTestUtil.predicateName).get(GlobalNames.DOUBLE));
			m.getOccurs().clear();
		}
	}
	
	@Test
	void notReplaceDoubleByDecimalNQ() {
		for (String value : specialValues) {
			String line = MeasurementTestUtil.createDoubleLine(value);
			MeasurementTestUtil.conductMeasurement(measurements, log, line);
			assertTrue(m.getOccurs().isEmpty());
		}
		for (String value : validDoubleValues) {
			String line = MeasurementTestUtil.createDoubleLine(value);
			MeasurementTestUtil.conductMeasurement(measurements, log, line);
			assertTrue(m.getOccurs().isEmpty());
		}
	}
	
	@Test
	void replaceDoubleByDecimalMethod() {
		//values that should be stored as decimal and not as double
		List<Double> doubleValues = Arrays.asList(
				(double) 1 / 3, 
				(double) -1 / 5, 
				(double) 3.1, 
				(double) -10.7);
		List<String> stringValues = Arrays.asList(
				"0.33333333333333333333333333",
				"-0.2",
				"3.1",
				"-10.7");
		for (int i = 0; i < doubleValues.size(); i++) {
			assertTrue(NumberUtil.shouldBeReplacedByDecimal(doubleValues.get(i), stringValues.get(i)));
		}
	}
	
	@Test
	void notReplaceDoubleByDecimalMethod() {
		//values that can be stored as double
		List<Double> doubleValues = Arrays.asList(
				(double) 1/2,
				(double) -1/Math.pow(2, 10),
				(double) 200.5, 
				(double) +0.0,
				(double) -0.0,
				(double) 314129,
				(double) 534609887);
		List<String> stringValues = Arrays.asList(
				"0.5", 
				"-9.765625e-4",
				"0.2005E3",
				"0",
				"0",
				"314129", 
				"534609887");
		for (int i = 0; i < doubleValues.size(); i++) {
			assertFalse(NumberUtil.shouldBeReplacedByDecimal(doubleValues.get(i), stringValues.get(i)));
		}
		//Values that can't be stored as decimal
		doubleValues = Arrays.asList(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NaN);
		stringValues = Arrays.asList("Infinity", "-Infinity", "NaN");
		for (int i = 0; i < doubleValues.size(); i++) {
			assertFalse(NumberUtil.shouldBeReplacedByDecimal(doubleValues.get(i), stringValues.get(i)));
		}
	}
	
	/********************************************************************
	 ****************************** Float *******************************
	 ********************************************************************/
	
	List<String> validFloatValues = Arrays.asList(
			"0.0",
			"-0.0",
			"-0.5",
			"31.25E-1",
			"534609888",
			"-25E-2",
			"800.0078125",
			"0.125E5"
			);
	
	List<String> invalidFloatValues = Arrays.asList(
			"0.1", 
			"-0.3",
			"3.14129",
			"7893465E-5",
			"0.000000059",
			"0.999"
			);
	
	@Test
	void replaceFloatByDecimalNQ() {
		for (String value : invalidFloatValues) {
			String line = MeasurementTestUtil.createFloatLine(value);
			MeasurementTestUtil.conductMeasurement(measurements, log, line);
			assertFalse(m.getOccurs().get(MeasurementTestUtil.predicateName).containsKey(GlobalNames.DOUBLE));
			assertFalse(m.getOccurs().get(MeasurementTestUtil.predicateName).containsKey(GlobalNames.STRING));
			assertEquals(1, m.getOccurs().get(MeasurementTestUtil.predicateName).get(GlobalNames.FLOAT));
			m.getOccurs().clear();
		}
	}
	
	@Test 
	void notReplaceFloatByDecimalNQ() {
		for (String value : specialValues) {
			String line = MeasurementTestUtil.createFloatLine(value);
			MeasurementTestUtil.conductMeasurement(measurements, log, line);
			assertTrue(m.getOccurs().isEmpty());
		}
		for (String value : validFloatValues) {
			String line = MeasurementTestUtil.createFloatLine(value);
			MeasurementTestUtil.conductMeasurement(measurements, log, line);
			assertTrue(m.getOccurs().isEmpty());
		}
	}
	
	@Test
	void floatReplaceByDecimalMethod() {
		List<Float> floatValues = Arrays.asList(
				(float) 1/3,
				(float) -1/5, 
				(float) 3.14129,
				(float) -758.1024569,
				(float) 534609887);
		//534609887 -> can't be stored as float
		List<String> stringValues = Arrays.asList(
				"0.33333333",
				"-0.2", 
				"3.14129", 
				"-758.1024569",
				"534609887");
		
		for (int i = 0; i < floatValues.size(); i++) {
			assertTrue(NumberUtil.shouldBeReplacedByDecimal(floatValues.get(i), stringValues.get(i)));
		}
	}
		
	@Test
	void notReplaceFloatByDecimalMethod() {
		//values that can be stored as float
		List<Float> floatValues = Arrays.asList(
				(float) 1 / 2, 
				(float) -1 / 4,
				(float) 534609888,
				(float) Math.pow(2, -5));
		List<String> stringValues = Arrays.asList(
				"0.5",
				"-0.25",
				"534609888",
				"3.125E-2");
		for (int i = 0; i < floatValues.size(); i++) {
			assertFalse(NumberUtil.shouldBeReplacedByDecimal(floatValues.get(i), stringValues.get(i)));
		}
		//values that can't be stored as decimal
		floatValues = Arrays.asList(Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NaN);
		stringValues = Arrays.asList("Infinity", "-Infinity", "NaN");
		for (int i = 0; i < floatValues.size(); i++) {
			assertFalse(NumberUtil.shouldBeReplacedByDecimal(floatValues.get(i), stringValues.get(i)));
		}
	}
	
	/********************************************************************
	 ****************************** String ******************************
	 ********************************************************************/
	private List<String> validNumbers = Arrays.asList(
			//positive ganze Zahlen
			"19", 
			"+200000",
			"23553.0",
			"09832569769785347897",
			//negative ganze Zahlen
			"-0987325",
			"-89734",
			//positive Kommazahlen
			"19.14298", 
			"+200000.12449786235",
			"23553.0",
			"098325697.69785347897",
			//negative Kommazahlen
			"-098732.5",
			"-89734.015567",
			"-8.888888888888");
	
	private List<String> invalidNumbers = Arrays.asList(
			//, statt .
			",",
			"89734,015567",
			"-897,34015567",
			//mehr als ein .
			"-89734.015.567",
			//exp Darstellung
			"1234.456E+2",
			"-1234.456E-2",
			//, und .
			"+1,234.456",
			//mehrere Vorzeichen
			"+-1,234.456",
			"+1,234+.456",
			//Leerzeichen und Buchstaben
			" 1 234.456",
			"a345bw54",
			//NaN, inf, -inf,
			"NaN",
			"-Infinity", 
			"Infinity"
			);
	
	@Test
	void validStringsNQ() {
		for (String number : validNumbers) {
			String line = MeasurementTestUtil.createStringLine(number);
			MeasurementTestUtil.conductMeasurement(measurements, log, line);
			assertFalse(m.getOccurs().get(MeasurementTestUtil.predicateName).containsKey(GlobalNames.DOUBLE));
			assertFalse(m.getOccurs().get(MeasurementTestUtil.predicateName).containsKey(GlobalNames.FLOAT));
			assertEquals(1, m.getOccurs().get(MeasurementTestUtil.predicateName).get(GlobalNames.STRING));
			m.getOccurs().clear();
		}
	}
	
	@Test
	void validStringsMethod() {
		for (String number : validNumbers) {
			assertTrue(StringUtil.isValidDecimal(number));
		}
	}

	@Test
	void invalidStringsNQ() {
		for(String number : invalidNumbers) {
			String line = MeasurementTestUtil.createStringLine(number);
			MeasurementTestUtil.conductMeasurement(measurements, log, line);
			assertTrue(m.getOccurs().isEmpty());
		}
	}
	
	@Test
	void invalidStringsMethod() {
		for (String number : invalidNumbers) {
			assertFalse(StringUtil.isValidDecimal(number));
		}
	}
}
