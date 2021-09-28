package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measure;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils.StringUtil;

class CouldBeTemporalTest {

	private org.slf4j.Logger log;
	private List<Measure> measures;
	private CouldBeTemporal m;


	@BeforeEach
	void init() {
		log = org.slf4j.LoggerFactory.getLogger("test");
		measures = new ArrayList<Measure>();
		m = new CouldBeTemporal();
		measures.add(m);
	}
	
	/*************************************************************************/
	/****************************** DateTime *********************************/
	/*************************************************************************/
	private List<String> validDateTimesWithoutOffset = Arrays.asList("2001-10-26T21:32:52", "2001-10-26T21:32:52.123", // ms -> optional
			"2103-10-31T12:34:59", // Future date
			"-2001-10-30T21:32:52", "-2401-11-20T00:00:00", "-2401-11-20T24:00:00", "2015-12-25T23:49:12");

	List<String> invalidDateTimesWithoutOffset = Arrays.asList("2001-10-26T", //missing time
			"2001-10-2621:32:52", // missing delimeter
			"2001-10-26U21:32:52", // wrong delimeter
			"2001-10-26T21:32", // missing seconds
			"2001-10-26T25:32:52", // invalid time
			"2001-02-29T25:32:52", // 2001 -> no leap year
			"2002-37-45T23:00:23" // wrong month and day
	);
	
	/*********************************** Method ******************************/

	@Test
	void validDateTimesWithoutOffsetMethod() {

		for (String date : validDateTimesWithoutOffset) {
			assertTrue(StringUtil.isValidDateTime(date));
		}
	}

	@Test
	void invalidDateTimesWithoutOffsetMethod() {
		for (String date : invalidDateTimesWithoutOffset) {
			assertFalse(StringUtil.isValidDateTime(date));
		}
	}


	/************************************* NQ ********************************/
	@Test
	void validDateTimesWithoutOffsetNQ() {
		for (String date : validDateTimesWithoutOffset) {
			String line = MeasureTestUtil.createStringLine(date);
			MeasureTestUtil.conductMeasurement(measures, log, line);
			assertEquals(1, m.getOccurs().keySet().size());
			assertTrue(m.getOccurs().containsKey(MeasureTestUtil.stringIRI));
			assertEquals(1, m.getOccurs().get(MeasureTestUtil.stringIRI).keySet().size());
			assertTrue(m.getOccurs().get(MeasureTestUtil.stringIRI).containsKey(MeasureTestUtil.predicateName));
			assertEquals(1, m.getOccurs().get(MeasureTestUtil.stringIRI).get(MeasureTestUtil.predicateName));
			m.getOccurs().clear();
		}
	}

	@Test
	void invalidDateTimesWithoutOffsetNQ() {
		for (String date : invalidDateTimesWithoutOffset) {
			String line = MeasureTestUtil.createStringLine(date);
			MeasureTestUtil.conductMeasurement(measures, log, line);
			assertTrue(m.getOccurs().keySet().isEmpty());
		}
	}

	
	/*************************************************************************/
	/**************************** DateTimeStamp ******************************/
	/*************************************************************************/
	List<String> validDateTimeStampsWithOffset = Arrays.asList("2001-10-26T19:32:52Z", // Z as UTC Timezone
			// Timezone between -14:00 and +14:00
			"2001-10-26T21:32:52+02:00", "2001-10-26T19:32:52+00:00", "1999-10-26T19:32:52-00:00",
			"2001-11-26T19:32:52+09:39", "2021-10-26T19:32:52-10:11", "2021-10-26T19:32:52-08:39",
			"2021-10-26T19:32:52-14:00", "2021-10-26T19:32:52+14:00");

	List<String> invalidDateTimeStampsWithOffset = Arrays.asList("2001-10-26T19:32:52E", // wrong charakter
			"2021-10-26T19:32:52+14:39", // offset out of the valid range
			"2021-10-26T19:32:52-14:39");
	
	/*********************************** Method ******************************/
	@Test
	void  validDateTimeStampsWithOffsetMethod() {
		for (String date : validDateTimeStampsWithOffset) {
			assertTrue(StringUtil.isValidDateTime(date));
		}
	}
	
	@Test
	void invalidDateTimeStampsWithOffsetMethod() {
		for (String date : invalidDateTimeStampsWithOffset) {
			assertFalse(StringUtil.isValidDateTime(date));
		}
	}
	/************************************* NQ ********************************/
	@Test
	void validDateTimeStampsWithOffsetNQ() {
		for (String date : validDateTimeStampsWithOffset) {
			String line = MeasureTestUtil.createStringLine(date);
			MeasureTestUtil.conductMeasurement(measures, log, line);
			assertEquals(1, m.getOccurs().keySet().size());
			assertTrue(m.getOccurs().containsKey(MeasureTestUtil.stringIRI));
			assertEquals(1, m.getOccurs().get(MeasureTestUtil.stringIRI).keySet().size());
			assertTrue(m.getOccurs().get(MeasureTestUtil.stringIRI).containsKey(MeasureTestUtil.predicateName));
			assertEquals(1, m.getOccurs().get(MeasureTestUtil.stringIRI).get(MeasureTestUtil.predicateName));
			m.getOccurs().clear();
		}
	}

	@Test
	void invalidDateTimeStampsWithOffsetNQ() {
		for (String date : invalidDateTimeStampsWithOffset) {
			String line = MeasureTestUtil.createStringLine(date);
			MeasureTestUtil.conductMeasurement(measures, log, line);
			assertTrue(m.getOccurs().keySet().isEmpty());
		}
	}
	
	/*************************************************************************/
	/*********************************** Date ********************************/
	/*************************************************************************/
	List<String> validDatesWithoutOffset = Arrays.asList(
			//special case: leap year
			"2020-02-29", "2000-02-29",
			//other
			"2017-05-31", "-1995-09-21", "1961-03-16", "1960-04-10", "2014-06-27", "2400-05-27"
			);
	
	List<String> validDatesWithOffset = Arrays.asList(
			"-2020-02-29Z", "2000-02-29+14:00",
			"2017-05-31-13:30", "1995-09-21+01:30", 
			"1961-03-16+00:00", "1960-04-10+05:45", 
			"2014-06-27-04:30"
			);
	
	List<String> invalidDatesWithoutOffset = Arrays.asList(
			//invalid month
			"2024-13-10", "2015-00-05", "1994-65-01",
			//invalid day
			"2010-10-00", "2013-07-658",
			//invalid year
			"234-12-12", "00-12-31",
			//swap of month, day, year
			"1234-31-05", "06-12-2018" );
	
	List<String> invalidDatesWithOffset = Arrays.asList(
			//other letter than z
			"-2020-02-29U", "1961-03-16T",
			//invalid date & timezone
			"06-12-2018-15:34", "2024-13-10+12:98",
			//invalid timezone
			"2014-06-27-14:30", "1995-09-21+100:30"
			);
	/*********************************** Method ******************************/
	@Test
	void validDatesWithoutOffsetMethod() {
		for(String date : validDatesWithoutOffset) {
			assertTrue(StringUtil.isValidDate(date));
		}
	}
	
	@Test
	void validDatesWithOffsetMethod() {
		for(String date : validDatesWithOffset) {
			assertTrue(StringUtil.isValidDate(date));
		}
	}
	
	@Test
	void invalidDatesWithoutOffsetMethod() {
		for(String date : invalidDatesWithoutOffset) {
			assertFalse(StringUtil.isValidDate(date));
		}
	}
	
	@Test
	void invalidDatesWithOffsetMethod() {
		for(String date : invalidDatesWithOffset) {
			assertFalse(StringUtil.isValidDate(date));
		}
	}
	
	/************************************* NQ ********************************/
	
	@Test
	void validDatesWithoutOffsetNQ() {
		for (String date : validDatesWithoutOffset) {
			String line = MeasureTestUtil.createStringLine(date);
			MeasureTestUtil.conductMeasurement(measures, log, line);
			assertEquals(1, m.getOccurs().keySet().size());
			assertTrue(m.getOccurs().containsKey(MeasureTestUtil.stringIRI));
			assertEquals(1, m.getOccurs().get(MeasureTestUtil.stringIRI).keySet().size());
			assertTrue(m.getOccurs().get(MeasureTestUtil.stringIRI).containsKey(MeasureTestUtil.predicateName));
			assertEquals(1, m.getOccurs().get(MeasureTestUtil.stringIRI).get(MeasureTestUtil.predicateName));
			m.getOccurs().clear();
		}
	}
	
	@Test
	void validDatesWithOffsetNQ() {
		for (String date : validDatesWithOffset) {
			String line = MeasureTestUtil.createStringLine(date);
			MeasureTestUtil.conductMeasurement(measures, log, line);
			assertEquals(1, m.getOccurs().keySet().size());
			assertTrue(m.getOccurs().containsKey(MeasureTestUtil.stringIRI));
			assertEquals(1, m.getOccurs().get(MeasureTestUtil.stringIRI).keySet().size());
			assertTrue(m.getOccurs().get(MeasureTestUtil.stringIRI).containsKey(MeasureTestUtil.predicateName));
			assertEquals(1, m.getOccurs().get(MeasureTestUtil.stringIRI).get(MeasureTestUtil.predicateName));
			m.getOccurs().clear();
		}
	}
	
	@Test
	void invalidDatesWithoutOffsetNQ() {
		for (String date : invalidDatesWithoutOffset) {
			String line = MeasureTestUtil.createStringLine(date);
			MeasureTestUtil.conductMeasurement(measures, log, line);
			assertTrue(m.getOccurs().isEmpty());
		}
	}
	
	@Test
	void invalidDatesWithOffsetNQ() {
		for (String date : invalidDatesWithOffset) {
			String line = MeasureTestUtil.createStringLine(date);
			MeasureTestUtil.conductMeasurement(measures, log, line);
			assertTrue(m.getOccurs().isEmpty());
		}
	}
	
	
	
	/*************************************************************************/
	/*********************************** Time ********************************/
	/*************************************************************************/
	List<String> validTimesWithoutOffset = Arrays.asList(
			//special case: midnight
			"24:00:00.0000", "24:00:00",
			//other 
			"23:01:12.67", "12:34:10.0", "07:56:59"
			);
	List<String> validTimesWithOffset = Arrays.asList(
			//special case: midnight
			"24:00:00.0000Z", "24:00:00+10:05",
			//other
			"23:01:12.67-05:21", "12:34:10.0-14:00", "07:56:59+13:59"
			);
	
	List<String> invalidTimesWithoutOffset = Arrays.asList(
			//hour 
			"25:23:32", "30:23:31",
			//minute
			"00:65:12.099",
			//second
			"10:01:95", "13:24:400",
			//other
			"24:00:01");
	List<String> invalidTimesWithOffset = Arrays.asList(
			"25:23:32Z", "30:23:31-10",
			"00:65:12.099+34:08",
			"10:01:95-99:99", "13:24:400Z",
			"24:00:01U",
			"23:34:10.00T10:00", "10:03:20+14:10", "24:00:00-23:98"
			);
	
	
	/*********************************** Method ******************************/
	@Test
	void validTimesWithoutOffsetMethod() {
		for(String time : validTimesWithoutOffset) {
			assertTrue(StringUtil.isValidTime(time));
		}
	}
	@Test
	void validTimesWithOffsetMethod() {
		for(String time : validTimesWithOffset) {
			assertTrue(StringUtil.isValidTime(time));
		}
	}
	@Test
	void invalidTimesWithoutOffsetMethod() {
		for(String time : invalidTimesWithoutOffset) {
			assertFalse(StringUtil.isValidTime(time));
		}
	}
	@Test
	void invalidTimesWithOffsetMethod() {
		for(String time : invalidTimesWithOffset) {
			assertFalse(StringUtil.isValidTime(time));
		}
	}

	/************************************* NQ ********************************/
	@Test
	void validTimesWithoutOffsetNQ() {
		for(String time : validTimesWithoutOffset) {
			String line = MeasureTestUtil.createStringLine(time);
			MeasureTestUtil.conductMeasurement(measures, log, line);
			assertEquals(1, m.getOccurs().keySet().size());
			assertTrue(m.getOccurs().containsKey(MeasureTestUtil.stringIRI));
			assertEquals(1, m.getOccurs().get(MeasureTestUtil.stringIRI).keySet().size());
			assertTrue(m.getOccurs().get(MeasureTestUtil.stringIRI).containsKey(MeasureTestUtil.predicateName));
			assertEquals(1, m.getOccurs().get(MeasureTestUtil.stringIRI).get(MeasureTestUtil.predicateName));
			m.getOccurs().clear();
		}
	}
	@Test
	void validTimesWithOffsetNQ() {
		for(String time : validTimesWithOffset) {
			String line = MeasureTestUtil.createStringLine(time);
			MeasureTestUtil.conductMeasurement(measures, log, line);
			assertEquals(1, m.getOccurs().keySet().size());
			assertTrue(m.getOccurs().containsKey(MeasureTestUtil.stringIRI));
			assertEquals(1, m.getOccurs().get(MeasureTestUtil.stringIRI).keySet().size());
			assertTrue(m.getOccurs().get(MeasureTestUtil.stringIRI).containsKey(MeasureTestUtil.predicateName));
			assertEquals(1, m.getOccurs().get(MeasureTestUtil.stringIRI).get(MeasureTestUtil.predicateName));
			m.getOccurs().clear();
		}
	}
	@Test
	void invalidTimesWithoutOffsetNQ() {
		for (String time : invalidTimesWithoutOffset) {
			String line = MeasureTestUtil.createStringLine(time);
			MeasureTestUtil.conductMeasurement(measures, log, line);
			assertTrue(m.getOccurs().isEmpty());
		}
	}
	@Test
	void invalidTimesWithOffsetNQ() {
		for (String time : invalidTimesWithOffset) {
			String line = MeasureTestUtil.createStringLine(time);
			MeasureTestUtil.conductMeasurement(measures, log, line);
			assertTrue(m.getOccurs().isEmpty());
		}
	}
}
