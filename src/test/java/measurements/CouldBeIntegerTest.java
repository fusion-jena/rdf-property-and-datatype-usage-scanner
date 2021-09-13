package test.java.measurements;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.measurements.CouldBeInteger;
import main.java.measurements.Measurement;
import main.java.utils.StringUtil;

class CouldBeIntegerTest {

	private List<String> validPositiveIntegers = Arrays.asList("+1", "1", "9876543456123456789012345678900987654321",
			"+623487923948906507098459746974397863497", "500", "2342345", "+67834123");

	private List<String> validNegativeIntegers = Arrays.asList("-34665434534",
			"-9876543456123456789012345678900987654321", "-435953");

	private List<String> floatingPointNumbers = Arrays.asList("0.98056490", "-9.857623", "+100.3452306", "10e4",
			"-10e-3");

	private List<String> invalidEntries = Arrays.asList("as235", "9/345,23", "kjldsfjk", "NaN", "+INF", "-INF");

	private org.slf4j.Logger log;
	private List<Measurement<?, ?>> measurements;
	private CouldBeInteger m;

	@BeforeEach
	void init() {
		log = org.slf4j.LoggerFactory.getLogger("test");
		measurements = new ArrayList<Measurement<?, ?>>();
		m = new CouldBeInteger();
		measurements.add(m);
	}

	/***********************************************************************************/
	/************************************* Method **************************************/
	/***********************************************************************************/

	@Test
	void invalidEntriesMethod() {
		for (String entry : invalidEntries) {
			assertFalse(StringUtil.isValidInteger(entry));
		}
	}

	@Test
	void floatingPointNumbersMethod() {
		for (String entry : floatingPointNumbers) {
			assertFalse(StringUtil.isValidInteger(entry));
		}
	}

	@Test
	void validNegativeIntegersMethod() {
		for (String entry : validNegativeIntegers) {
			assertTrue(StringUtil.isValidInteger(entry));
		}
	}

	@Test
	void validPositiveIntegersMethod() {
		for (String entry : validPositiveIntegers) {
			assertTrue(StringUtil.isValidInteger(entry));
		}
	}

	/***********************************************************************************/
	/****************************************** NQ ****************************************/
	/***********************************************************************************/

	@Test
	void validPositiveIntegersNQ() {
		for (String entry : validPositiveIntegers) {
			String line = MeasurementTestUtil.createStringLine(entry);
			MeasurementTestUtil.conductMeasurement(measurements, log, line);
			assertEquals(1, m.getOccurs().get(MeasurementTestUtil.predicateName));
			m.getOccurs().clear();
		}
	}

	@Test
	void validNegativeIntegersNQ() {
		for (String entry : validNegativeIntegers) {
			String line = MeasurementTestUtil.createStringLine(entry);
			MeasurementTestUtil.conductMeasurement(measurements, log, line);
			assertEquals(1, m.getOccurs().get(MeasurementTestUtil.predicateName));
			m.getOccurs().clear();
		}
	}

	@Test
	void invalidFloatingPointNumbersNQ() {
		for (String entry : floatingPointNumbers) {
			String line = MeasurementTestUtil.createStringLine(entry);
			MeasurementTestUtil.conductMeasurement(measurements, log, line);
			assertTrue(m.getOccurs().isEmpty());
		}
	}

	@Test
	void invalidEntriesNQ() {
		for (String entry : invalidEntries) {
			String line = MeasurementTestUtil.createStringLine(entry);
			MeasurementTestUtil.conductMeasurement(measurements, log, line);
			assertTrue(m.getOccurs().isEmpty());
		}
	}

}
