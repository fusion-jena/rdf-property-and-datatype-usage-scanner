package measurements;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import utils.StringUtil;

class StringDoubleFloatNotDecimalMeasurementTest {

	@Test
	void testValidFloatValidDecimal() {
		List<Float> numbers = Arrays.asList(
				// Integer
				0f, +0f, -0f, -123f, 457000000f,
				// Floatingpoint
				1.5f, -1.5f, -0.000000234f, (float) 1 / 3, Float.MAX_VALUE, Float.MIN_VALUE);
		for (Float number : numbers) {
			assertFalse(StringUtil.isValidDoubleOrFloatAndInvalidDecimal(number + ""));
		}
		// Exp-Representation:
		List<String> numbers2 = Arrays.asList("123E-12", "-0.625E-1", "100E-20", "98656E25", "3.4E38");
		for (String number : numbers2) {
			assertFalse(StringUtil.isValidDoubleOrFloatAndInvalidDecimal(number));
		}
	}

	@Test
	void testValidFloatInvalidDecimal() {
		// NaN, + inf, - inf
		List<String> numbers = Arrays.asList(Float.NEGATIVE_INFINITY + "", // -inf
				Float.NaN + "", // NaN
				Float.POSITIVE_INFINITY + "", // +inf
				Double.NaN + "", // NaN Double
				Double.POSITIVE_INFINITY + "", // + inf Double
				Double.NEGATIVE_INFINITY + "" // - inf Double
		);
		for (String number : numbers) {
			assertTrue(StringUtil.isValidDoubleOrFloatAndInvalidDecimal(number));
		}
	}

	@Test
	void testInvalidFloat() {
		// No numbers
		List<String> numbers = Arrays.asList("23 9707897", "3.34j23", "9789H23", "dsaf");
		for (String number : numbers) {
			assertFalse(StringUtil.isValidDoubleOrFloatAndInvalidDecimal(number));
		}
	}

	@Test
	void testValidDoubleValidDecimal() {
		List<Double> numbers = Arrays.asList(0.5, 0.25, -0.125, +0.0, -0.0, 1.0, 10.0, -15.0, 3456.0, 1.5, -3.125, 0.2,
				-0.9, (double) (Math.pow(2, 10) + 7.1), (double) 1 / 3, (double) -9 / 7, 0.6,
				(double) Math.pow(5, -10));
		for (Double number : numbers) {
			assertFalse(StringUtil.isValidDoubleOrFloatAndInvalidDecimal(number + ""));
		}
		List<String> numbers2 = Arrays.asList("5E-1", "1250E-4", "0.0", "0.52886675", "10E2");
		for (String number : numbers2) {
			assertFalse(StringUtil.isValidDoubleOrFloatAndInvalidDecimal(number + ""));
		}
	}

	@Test
	void testValidDoubleInvalidDecimal() {
		// NaN, + inf, - inf (double)
		List<String> numbers = Arrays.asList(Double.NaN + "", Double.POSITIVE_INFINITY + "",
				Double.NEGATIVE_INFINITY + "", Float.NEGATIVE_INFINITY + "", Float.NEGATIVE_INFINITY + "",
				Float.NaN + "");
		for (String number : numbers) {
			assertTrue(StringUtil.isValidDoubleOrFloatAndInvalidDecimal(number));
		}
	}

	@Test
	void testInvalidDouble() {
		// No numbers
		List<String> numbers = Arrays.asList("23 9707897", "3.34j23", "9789H23", "dsaf");
		for (String number : numbers) {
			assertFalse(StringUtil.isValidDoubleOrFloatAndInvalidDecimal(number));
		}
	}

}
