package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measure;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class MeasureTest {

	List<String> invalidNumberNotation = Arrays.asList(
			// , statt .
			",", "89734,015567", "-897,34015567",
			// mehr als ein .
			"-89734.015.567",
			// , und .
			"+1,234.456",
			// mehrere Vorzeichen
			"+-1,234.456", "+1,234+.456",
			// Leerzeichen und Buchstaben
			" 1 234.456", "a345bw54");

	List<String> integerNotation = Arrays.asList(
			// positive
			"19", "+200000", "23553", "09832569769785347897",
			// negative
			"-0987325", "-89734");

	List<String> decimalNotation = Arrays.asList(
			// positive
			"19.14298", "+200000.12449786235", "23553.0", "098325697.69785347897",
			// negative
			"-098732.5", "-89734.015567", "-8.888888888888");

	List<String> exponentialNotation = Arrays.asList("1234.456E+2", "-1234.456E-2", "1234.456e+2", "-1234.456e-2");

	List<String> infOrNaNNotation = Arrays.asList("NaN", "INF", "+INF", "-INF");

	List<String> unpreciseInFloatAndDouble = Arrays.asList("0.1", "0.2", "200E-3", "-10.71", "5876E-2", "0.1", "-0.3",
			"0.00000000002");

	List<String> trueOrFalse = Arrays.asList("true", "false");

	List<String> zeroOrOne = Arrays.asList("0", "1");

	List<String> validDateTimesWithoutOffset = Arrays.asList("2001-10-26T21:32:52", "2001-10-26T21:32:52.123", // ms ->
																												// optional
			"2103-10-31T12:34:59", // Future date
			"-2001-10-30T21:32:52", "-2401-11-20T00:00:00", "-2401-11-20T24:00:00", "2015-12-25T23:49:12");

	List<String> invalidDateTimesWithoutOffset = Arrays.asList("2001-10-26T", // missing time
			"2001-10-2621:32:52", // missing delimeter
			"2001-10-26U21:32:52", // wrong delimeter
			"2001-10-26T21:32", // missing seconds
			"2001-10-26T25:32:52", // invalid time
			"2001-02-29T25:32:52", // 2001 -> no leap year
			"2002-37-45T23:00:23" // wrong month and day
	);

	List<String> validDateTimeStampsWithOffset = Arrays.asList("2001-10-26T19:32:52Z", // Z as UTC Timezone
			// Timezone between -14:00 and +14:00
			"2001-10-26T21:32:52+02:00", "2001-10-26T19:32:52+00:00", "1999-10-26T19:32:52-00:00",
			"2001-11-26T19:32:52+09:39", "2021-10-26T19:32:52-10:11", "2021-10-26T19:32:52-08:39",
			"2021-10-26T19:32:52-14:00", "2021-10-26T19:32:52+14:00");

	List<String> invalidDateTimeStampsWithOffset = Arrays.asList("2001-10-26T19:32:52E", // wrong charakter
			"2021-10-26T19:32:52+14:39", // offset out of the valid range
			"2021-10-26T19:32:52-14:39");
	List<String> validDatesWithoutOffset = Arrays.asList(
			// special case: leap year
			"2020-02-29", "2000-02-29",
			// other
			"2017-05-31", "-1995-09-21", "1961-03-16", "1960-04-10", "2014-06-27", "2400-05-27");

	List<String> validDatesWithOffset = Arrays.asList("-2020-02-29Z", "2000-02-29+14:00", "2017-05-31-13:30",
			"1995-09-21+01:30", "1961-03-16+00:00", "1960-04-10+05:45", "2014-06-27-04:30");

	List<String> invalidDatesWithoutOffset = Arrays.asList(
			// invalid month
			"2024-13-10", "2015-00-05", "1994-65-01",
			// invalid day
			"2010-10-00", "2013-07-658",
			// invalid year
			"234-12-12", "00-12-31",
			// swap of month, day, year
			"1234-31-05", "06-12-2018");

	List<String> invalidDatesWithOffset = Arrays.asList(
			// other letter than z
			"-2020-02-29U", "1961-03-16T",
			// invalid date & timezone
			"06-12-2018-15:34", "2024-13-10+12:98",
			// invalid timezone
			"2014-06-27-14:30", "1995-09-21+100:30");

	List<String> validTimesWithoutOffset = Arrays.asList(
			// special case: midnight
			"24:00:00.0000", "24:00:00",
			// other
			"23:01:12.67", "12:34:10.0", "07:56:59");
	List<String> validTimesWithOffset = Arrays.asList(
			// special case: midnight
			"24:00:00.0000Z", "24:00:00+10:05",
			// other
			"23:01:12.67-05:21", "12:34:10.0-14:00", "07:56:59+13:59");

	List<String> invalidTimesWithoutOffset = Arrays.asList(
			// hour
			"25:23:32", "30:23:31",
			// minute
			"00:65:12.099",
			// second
			"10:01:95", "13:24:400",
			// other
			"24:00:01");
	List<String> invalidTimesWithOffset = Arrays.asList("25:23:32Z", "30:23:31-10", "00:65:12.099+34:08",
			"10:01:95-99:99", "13:24:400Z", "24:00:01U", "23:34:10.00T10:00", "10:03:20+14:10", "24:00:00-23:98");

	@Test
	public void unpreciseDoubleRepresentable() {
		for (String lexivalValue : unpreciseInFloatAndDouble) {
			assertTrue(new UnpreciseRepresentableInDouble().measure(lexivalValue), lexivalValue);
		}

	}

	@Test
	public void unpreciseFloatRepresentable() {
		for (String lexivalValue : unpreciseInFloatAndDouble) {
			assertTrue(new UnpreciseRepresentableInFloat().measure(lexivalValue), lexivalValue);
		}
	}

	@Test
	public void usedAsDatatype() {
		for (String lexivalValue : invalidNumberNotation) {
			assertTrue(new UsedAsDatatype().measure(lexivalValue), lexivalValue);
		}
	}

	@Test
	public void validDateNotation() {
		for (String lexivalValue : validDateTimesWithoutOffset) {
			assertFalse(new ValidDateNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : invalidDateTimesWithoutOffset) {
			assertFalse(new ValidDateNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : validDateTimeStampsWithOffset) {
			assertFalse(new ValidDateNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : invalidDateTimeStampsWithOffset) {
			assertFalse(new ValidDateNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : validDatesWithoutOffset) {
			assertTrue(new ValidDateNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : validDatesWithOffset) {
			assertTrue(new ValidDateNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : invalidDatesWithoutOffset) {
			assertFalse(new ValidDateNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : invalidDatesWithOffset) {
			assertFalse(new ValidDateNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : validTimesWithoutOffset) {
			assertFalse(new ValidDateNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : invalidTimesWithoutOffset) {
			assertFalse(new ValidDateNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : invalidTimesWithOffset) {
			assertFalse(new ValidDateNotation().measure(lexivalValue), lexivalValue);
		}
	}

	@Test
	public void validDateTimeNotation() {
		for (String lexivalValue : validDateTimesWithoutOffset) {
			assertTrue(new ValidDateTimeNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : invalidDateTimesWithoutOffset) {
			assertFalse(new ValidDateTimeNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : validDateTimeStampsWithOffset) {
			assertTrue(new ValidDateTimeNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : invalidDateTimeStampsWithOffset) {
			assertFalse(new ValidDateTimeNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : validDatesWithoutOffset) {
			assertFalse(new ValidDateTimeNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : validDatesWithOffset) {
			assertFalse(new ValidDateTimeNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : invalidDatesWithoutOffset) {
			assertFalse(new ValidDateTimeNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : invalidDatesWithOffset) {
			assertFalse(new ValidDateTimeNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : validTimesWithoutOffset) {
			assertFalse(new ValidDateTimeNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : invalidTimesWithoutOffset) {
			assertFalse(new ValidDateTimeNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : invalidTimesWithOffset) {
			assertFalse(new ValidDateTimeNotation().measure(lexivalValue), lexivalValue);
		}
	}

	@Test
	public void validDecimalNotation() {
		for (String lexivalValue : invalidNumberNotation) {
			assertFalse(new ValidDecimalNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : integerNotation) {
			assertFalse(new ValidDecimalNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : decimalNotation) {
			assertTrue(new ValidDecimalNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : exponentialNotation) {
			assertFalse(new ValidDecimalNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : infOrNaNNotation) {
			assertFalse(new ValidDecimalNotation().measure(lexivalValue), lexivalValue);
		}
	}

	@Test
	public void validExponentialNotation() {
		for (String lexivalValue : invalidNumberNotation) {
			assertFalse(new ValidExponentialNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : integerNotation) {
			assertFalse(new ValidExponentialNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : decimalNotation) {
			assertFalse(new ValidExponentialNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : exponentialNotation) {
			assertTrue(new ValidExponentialNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : infOrNaNNotation) {
			assertFalse(new ValidExponentialNotation().measure(lexivalValue), lexivalValue);
		}
	}

	@Test
	public void validFloatSpecialValueNotation() {
		for (String lexivalValue : invalidNumberNotation) {
			assertFalse(new ValidInfOrNaNNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : integerNotation) {
			assertFalse(new ValidInfOrNaNNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : decimalNotation) {
			assertFalse(new ValidInfOrNaNNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : exponentialNotation) {
			assertFalse(new ValidInfOrNaNNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : infOrNaNNotation) {
			assertTrue(new ValidInfOrNaNNotation().measure(lexivalValue), lexivalValue);
		}
	}

	@Test
	public void validIntegerNotation() {
		for (String lexivalValue : invalidNumberNotation) {
			assertFalse(new ValidIntegerNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : integerNotation) {
			assertTrue(new ValidIntegerNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : decimalNotation) {
			assertFalse(new ValidIntegerNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : exponentialNotation) {
			assertFalse(new ValidIntegerNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : infOrNaNNotation) {
			assertFalse(new ValidIntegerNotation().measure(lexivalValue), lexivalValue);
		}
	}

	@Test
	public void validTimeNotation() {
		for (String lexivalValue : validDateTimesWithoutOffset) {
			assertFalse(new ValidTimeNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : invalidDateTimesWithoutOffset) {
			assertFalse(new ValidTimeNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : validDateTimeStampsWithOffset) {
			assertFalse(new ValidTimeNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : invalidDateTimeStampsWithOffset) {
			assertFalse(new ValidTimeNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : validDatesWithoutOffset) {
			assertFalse(new ValidTimeNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : validDatesWithOffset) {
			assertFalse(new ValidTimeNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : invalidDatesWithoutOffset) {
			assertFalse(new ValidTimeNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : invalidDatesWithOffset) {
			assertFalse(new ValidTimeNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : validTimesWithoutOffset) {
			assertTrue(new ValidTimeNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : invalidTimesWithoutOffset) {
			assertFalse(new ValidTimeNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : invalidTimesWithOffset) {
			assertFalse(new ValidTimeNotation().measure(lexivalValue), lexivalValue);
		}
	}

	@Test
	public void validTrueOrFalseNotation() {
		for (String lexivalValue : trueOrFalse) {
			assertTrue(new ValidTrueOrFalseNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : zeroOrOne) {
			assertFalse(new ValidTrueOrFalseNotation().measure(lexivalValue), lexivalValue);
		}
	}

	@Test
	public void validZeroOrOneNotation() {
		for (String lexivalValue : trueOrFalse) {
			assertFalse(new ValidZeroOrOneNotation().measure(lexivalValue), lexivalValue);
		}
		for (String lexivalValue : zeroOrOne) {
			assertTrue(new ValidZeroOrOneNotation().measure(lexivalValue), lexivalValue);
		}
	}
}
