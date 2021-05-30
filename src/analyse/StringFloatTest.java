package analyse;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class StringFloatTest {

	@Test
	void validFloatInvalidBegDecimalTest() {
		List<String> numbers = Arrays.asList(Float.intBitsToFloat(0xff800000) + "", // -inf
				Float.intBitsToFloat(0x7fc00000) + "", // NaN
				Float.intBitsToFloat(0x7f800000) + "" // +inf
				);

		for (String number : numbers) {
			assertTrue(StringUtil.isValidFloat(number));
		}
	}

	@Test
	void validFloatValidBigDecimalTest() {
		//Kommadarstellung
		List<Float> numbers = Arrays.asList(
				// Integer
				0.0f, +0.0f, -0.0f, -123f, 457000000f, -8908f, (float) (Math.pow(2, 10)),
				// Kommazahlen
				1.5f, -1.5f, (float) (Math.pow(2, 11)), (float) (-1 / 8));

		for (Float number : numbers) {
			assertTrue(StringUtil.isValidFloat(number + ""));
		}
		//Exp Darstellung
		List<String> numbers2 = Arrays.asList("3.2E1",//2^5
				"-0.625E-1"//2^-16
				);
		for (String number : numbers2) {
			if(!StringUtil.isValidFloat(number)) {
				System.out.println(number);
			}
			assertTrue(StringUtil.isValidFloat(number));
		}
	}

	@Test
	public void invalidFloatValidBigDecimal() {
		// TODO Pruefen
		//https://www.h-schmidt.net/FloatConverter/IEEE754de.html
		List<Float> numbers = Arrays.asList(0.2f, -0.9f, (float) (Math.pow(2, 10) + 7.1),
				(float) 1 / 3, (float) -9 / 7, (float) 0.6, (float) Math.pow(5, -10));
		for (Float number : numbers) {
			assertFalse(StringUtil.isValidFloat(number + ""));
		}
	}

	@Test
	public void invalidNumbers() {
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
