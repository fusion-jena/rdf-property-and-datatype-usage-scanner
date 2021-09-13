package test.java.measurements;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.measurements.CouldBeBoolean;
import main.java.measurements.Measurement;
import main.java.utils.StringUtil;

public class CouldBeBooleanTest {
	
	private List<String> validBoolean = Arrays.asList("true", "false", "0", "1");
	
	private List<String> invalidBoolean = Arrays.asList("True", "False", "0.0", "1.0", "dieser satz ergibt keinen sinn", "(8)", "231");
	
	private org.slf4j.Logger log;
	private List<Measurement<?, ?>> measurements;
	private CouldBeBoolean m;
	
	@BeforeEach
	void init() {
		log = org.slf4j.LoggerFactory.getLogger("test");
		measurements = new ArrayList<Measurement<?, ?>>();
		m = new CouldBeBoolean();
		measurements.add(m);
	}
	
	/***********************************************************************************/
	/************************************Method****************************************/
	/***********************************************************************************/
	
	@Test
	void validBooleanMethod() {
		for(String validEntry : validBoolean) {
			assertTrue(StringUtil.isValidBoolean(validEntry));
		}
	}
	
	@Test
	void invalidBooleanMethod() {
		for(String invalidEntry : invalidBoolean) {
			assertFalse(StringUtil.isValidBoolean(invalidEntry));
		}
	}
	
	/***********************************************************************************/
	/*****************************************NQ****************************************/
	/***********************************************************************************/
	
	@Test
	void validBooleanNQ() {
		for(String validEntry : validBoolean) {
			String line = MeasurementTestUtil.createStringLine(validEntry);
			MeasurementTestUtil.conductMeasurement(measurements, log, line);
			assertEquals(1, m.getOccurs().get(MeasurementTestUtil.predicateName));
			m.getOccurs().clear();
		}
	}
	
	@Test
	void invalidBooleanNQ() {
		for(String invalidEntry : invalidBoolean) {
			String line = MeasurementTestUtil.createStringLine(invalidEntry);
			MeasurementTestUtil.conductMeasurement(measurements, log, line);
			assertTrue(m.getOccurs().isEmpty());
		}
	}


	
	
}
