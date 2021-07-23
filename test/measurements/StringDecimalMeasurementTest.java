package measurements;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import utils.StringUtil;

class StringDecimalMeasurementTest {

	@Test
	void validStrings() {
		List<String> numbers = Arrays.asList(
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

		for (String number : numbers) {
			assertTrue(StringUtil.isValidDecimal(number));
		}
	}

	@Test
	void invalidStrings() {
		List<String> numbers = Arrays.asList(
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
	
		for (String number : numbers) {
			if(StringUtil.isValidDecimal(number)) {
				System.out.println(number);
			}
			assertFalse(StringUtil.isValidDecimal(number));
		}
	}

}
