package analyse;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import utils.NumberUtil;

class DoubleFloatDecimalMeasurementTest {

	@Test
	void replaceDoubleByDecimal() {
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
	void notReplaceDoubleByDecimal() {
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
		//values that can't be stored as decimal
		doubleValues = Arrays.asList(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NaN);
		stringValues = Arrays.asList("Infinity", "-Infinity", "NaN");
		for (int i = 0; i < doubleValues.size(); i++) {
			assertFalse(NumberUtil.shouldBeReplacedByDecimal(doubleValues.get(i), stringValues.get(i)));
		}
	}
	
	@Test
	void floatReplaceByDecimal() {
		List<Float> floatValues = Arrays.asList(
				(float) 1/3,
				(float) -1/5, 
				(float) 3.14129,
				(float) -758.1024569,
				(float) 534609887);
		//534609887 -> kann nicht als float dargestellt werden
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
	void notReplaceFloatByDecimal() {
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

}
