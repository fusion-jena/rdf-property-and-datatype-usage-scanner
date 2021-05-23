package analyse;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class StringDateTimeTest {
	
	@Test
	void validDatesWithoutOffset() {
		List<String> dates = Arrays.asList(
				"2001-10-26T21:32:52", 
				"2001-10-26T21:32:52.123", // Angabe von ms ist optional
				"2103-10-31T12:34:59", // Zukuenftiges Datum
				"-2001-10-30T21:32:52",
				"-2401-11-20T00:00:00",
				"-2401-11-20T24:00:00",
				"2015-12-25T23:49:12"
				);
		
		for (String date : dates) {
			assertTrue(StringUtil.isValidDate(date));
		}
	}

	@Test
	void invalidDatesWithoutOffset() {
		// Ungueltige Eingaben
		List<String> dates = Arrays.asList(
				"2001-10-26", // Uhrzeit fehlt
				"2001-10-2621:32:52", // Trennzeichen fehlt
				"2001-10-26U21:32:52", // Trennzeichen falsch
				"2001-10-26T21:32", // Sekunden fehlen
				"2001-10-26T25:32:52", //Ungueltige Uhrzeit
				"2001-02-29T25:32:52", //2001 kein Schaltjahr
				"2002-37-45T23:00:23" //Monat und Tag falsch
				);
		
		for (String date : dates) {
			assertFalse(StringUtil.isValidDate(date));
		}
	}
	
	@Test
	void validDatesWithOffset() {
		List<String> dates = Arrays.asList(
				"2001-10-26T19:32:52Z", //Z als UTC Zeitzone
				//Zeitzone zwischen -14:00 und +14:00
				"2001-10-26T21:32:52+02:00",
				"2001-10-26T19:32:52+00:00",
				"1999-10-26T19:32:52-00:00",
				"2001-11-26T19:32:52+09:39", 
				"2021-10-26T19:32:52-10:11",
				"2021-10-26T19:32:52-08:39",
				"2021-10-26T19:32:52-14:00",
				"2021-10-26T19:32:52+14:00"
				);
		
		for (String date : dates) {
			assertTrue(StringUtil.isValidDate(date));
		}
	}
	
	@Test
	void invalidDatesWithOffset() {
		List<String> dates = Arrays.asList(
				"2001-10-26T19:32:52E", // falsches Zeichen
				"2021-10-26T19:32:52+14:39", //offset ausserhalb des erlaubten Bereichs
				"2021-10-26T19:32:52-14:39"
				);
		
		for (String date : dates) {
			assertFalse(StringUtil.isValidDate(date));
		}
	}

}
