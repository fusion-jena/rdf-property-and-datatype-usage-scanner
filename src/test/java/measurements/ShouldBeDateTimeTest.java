package test.java.measurements;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.measurements.Measurement;
import main.java.measurements.ShouldBeDateTime;
import main.java.utils.StringUtil;

class ShouldBeDateTimeTest {

	private List<String> validDatesWithoutOffset = Arrays.asList("2001-10-26T21:32:52", "2001-10-26T21:32:52.123", // ms
																													// is
																													// optional
			"2103-10-31T12:34:59", // Future date
			"-2001-10-30T21:32:52", "-2401-11-20T00:00:00", "-2401-11-20T24:00:00", "2015-12-25T23:49:12");

	List<String> invalidDatesWithoutOffset = Arrays.asList("2001-10-26", // missing time
			"2001-10-2621:32:52", // missing delimeter
			"2001-10-26U21:32:52", // wrong delimeter
			"2001-10-26T21:32", // missing seconds
			"2001-10-26T25:32:52", // invalid time
			"2001-02-29T25:32:52", // 2001 -> no leap year
			"2002-37-45T23:00:23" // wrong month and day
	);

	List<String> validDatesWithOffset = Arrays.asList("2001-10-26T19:32:52Z", // Z as UTC Timezone
			// Timezone between -14:00 and +14:00
			"2001-10-26T21:32:52+02:00", "2001-10-26T19:32:52+00:00", "1999-10-26T19:32:52-00:00",
			"2001-11-26T19:32:52+09:39", "2021-10-26T19:32:52-10:11", "2021-10-26T19:32:52-08:39",
			"2021-10-26T19:32:52-14:00", "2021-10-26T19:32:52+14:00");

	List<String> invalidDatesWithOffset = Arrays.asList("2001-10-26T19:32:52E", // wrong charakter
			"2021-10-26T19:32:52+14:39", // offset out of the valid range
			"2021-10-26T19:32:52-14:39");

	private org.slf4j.Logger log;
	private List<Measurement<?, ?>> measurements;
	private ShouldBeDateTime m;


	@BeforeEach
	void init() {
		log = org.slf4j.LoggerFactory.getLogger("test");
		measurements = new ArrayList<Measurement<?, ?>>();
		m = new ShouldBeDateTime();
		measurements.add(m);
	}

	/**
	 * Test of the method {@link StringUtil#isValidDate()}
	 */

	@Test
	void validDatesWithoutOffsetMethod() {

		for (String date : validDatesWithoutOffset) {
			assertTrue(StringUtil.isValidDate(date));
		}
	}

	@Test
	void invalidDatesWithoutOffsetMethod() {
		for (String date : invalidDatesWithoutOffset) {
			assertFalse(StringUtil.isValidDate(date));
		}
	}

	@Test
	void validDatesWithOffsetMethod() {
		for (String date : validDatesWithOffset) {
			assertTrue(StringUtil.isValidDate(date));
		}
	}
	
	@Test
	void invalidDatesWithOffsetMethod() {
		for (String date : invalidDatesWithOffset) {
			assertFalse(StringUtil.isValidDate(date));
		}
	}

	/**
	 * NQ
	 */

	@Test
	void validDatesWithoutOffsetNQ() {
		for (String date : validDatesWithoutOffset) {
			String line = MeasurementTestUtil.createStringLine(date);
			MeasurementTestUtil.conductMeasurement(measurements, log, line);
			assertEquals(m.getOccurs().get(MeasurementTestUtil.predicateName), 1);
			m.getOccurs().clear();
		}
	}

	@Test
	void invalidDatesWithoutOffsetNQ() {
		for (String date : invalidDatesWithoutOffset) {
			String line = MeasurementTestUtil.createStringLine(date);
			MeasurementTestUtil.conductMeasurement(measurements, log, line);
			assertTrue(m.getOccurs().keySet().isEmpty());
			m.getOccurs().clear();
		}
	}

	@Test
	void validDatesWithOffsetNQ() {
		for (String date : validDatesWithOffset) {
			String line = MeasurementTestUtil.createStringLine(date);
			MeasurementTestUtil.conductMeasurement(measurements, log, line);
			assertEquals(m.getOccurs().get(MeasurementTestUtil.predicateName), 1);
			m.getOccurs().clear();
		}
	}

	@Test
	void invalidDatesWithOffsetNQ() {
		for (String date : invalidDatesWithOffset) {
			String line = MeasurementTestUtil.createStringLine(date);
			MeasurementTestUtil.conductMeasurement(measurements, log, line);
			assertTrue(m.getOccurs().keySet().isEmpty());
			m.getOccurs().clear();
		}
	}

}
