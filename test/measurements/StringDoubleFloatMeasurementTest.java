package measurements;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import utils.StringUtil;

class StringDoubleFloatMeasurementTest {
	
	/**************************************************************/
	/***************************Double*****************************/
	/**************************************************************/
	@Test
	void validDoubleInvalidBigDecimalTest() {
		List<String> numbers = Arrays.asList(Double.NEGATIVE_INFINITY + "", // -inf
				Double.NaN + "", // NaN
				Double.POSITIVE_INFINITY + "" // +inf
		);

		for (String number : numbers) {
			assertTrue(StringUtil.isValidDouble(number));
		}
	}

	@Test
	void validDoubleValidBigDecimalTest() {
		List<Double> numbers = Arrays.asList(0.5, 0.25, -0.125, +0.0, -0.0, 1.0, 10.0, -15.0, 3456.0, 1.5, -3.125);
		for (Double number : numbers) {
			assertTrue(StringUtil.isValidDouble(number + ""));
		}
		List<String> numbers2 = Arrays.asList("5E-1", "1250E-4", "0.0", "0.1875", "10E2");
		for (String number : numbers2) {
			assertTrue(StringUtil.isValidDouble(number + ""));
		}
	}

	@Test
	void invalidDoubleValidBigDecimalTest() {
		// https://www.h-schmidt.net/FloatConverter/IEEE754de.html
		List<Double> numbers = Arrays.asList(0.2, -0.9, (double) (Math.pow(2, 10) + 7.1),
				(double) 1 / 3, (double) -9 / 7, 0.6, (double) Math.pow(5, -10));
		for (Double number : numbers) {
			assertFalse(StringUtil.isValidDouble(number + ""));
		}
	}

	@Test
	void invalidDoubleNumbers() {
		List<String> numbers = Arrays.asList(
				// Zahlen mit ' '
				"235 34.324",
				// Zahlen mit Buchstaben ,
				"3A-5", "--34", "12,56");
		System.out.println();
		for (String number : numbers) {
			assertFalse(StringUtil.isValidFloat(number));
		}
	}
	
	/**************************************************************/
	/****************************Float*****************************/
	/**************************************************************/
	@Test
	void validFloatInvalidBegDecimalTest() {
		List<String> numbers = Arrays.asList(Float.NEGATIVE_INFINITY + "", Float.NaN + "",
				Float.POSITIVE_INFINITY + "");
		for (String number : numbers) {
			assertTrue(StringUtil.isValidFloat(number));
		}
	}

	@Test
	void validFloatValidBigDecimalTest() {
		// Kommadarstellung
		List<Float> numbers = Arrays.asList(
				// Integer
				0.0f, +0.0f, -0.0f, -123f, 457000000f, -8908f, (float) (Math.pow(2, 10)),
				// Kommazahlen
				1.5f, -1.5f, (float) (Math.pow(2, 11)), (float) (-1 / 8));

		for (Float number : numbers) {
			assertTrue(StringUtil.isValidFloat(number + ""));
		}
		// Exp Darstellung
		List<String> numbers2 = Arrays.asList("3.2E1", // 2^5
				"-0.625E-1"// 2^-16
		);
		for (String number : numbers2) {
			assertTrue(StringUtil.isValidFloat(number));
		}
	}

	@Test
	public void invalidFloatValidBigDecimal() {
		// https://www.h-schmidt.net/FloatConverter/IEEE754de.html
		List<Float> numbers = Arrays.asList(0.2f, -0.9f, (float) (Math.pow(2, 10) + 7.1), (float) 1 / 3, (float) -9 / 7,
				(float) 0.6, (float) Math.pow(5, -10));
		for (Float number : numbers) {
			assertFalse(StringUtil.isValidFloat(number + ""));
		}
	}

	@Test
	public void invalidFloatNumbers() {
		List<String> numbers = Arrays.asList(
				// Zahlen mit ' '
				"235 34.324",
				// Zahlen mit Buchstaben ,
				"3A-5", "--34", "12,56");
		System.out.println();
		for (String number : numbers) {
			assertFalse(StringUtil.isValidFloat(number));
		}
	}

}
