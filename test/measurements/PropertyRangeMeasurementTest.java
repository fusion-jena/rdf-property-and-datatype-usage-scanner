package measurements;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PropertyRangeMeasurementTest {

	private org.slf4j.Logger log;
	private List<Measurement<?, ?>> measurements;
	private PropertyRangeMeasurement m;
	
	@BeforeEach
	void init() {
		log = org.slf4j.LoggerFactory.getLogger("test");
		measurements = new ArrayList<Measurement<?,?>>();
		m = new PropertyRangeMeasurement();
		measurements.add(m);
	}
	
	@Test
	void propertyRangeStatementNQ() {
		List<String> datatypes = Arrays.asList(
				"http://www.w3.org/2001/XMLSchema#double",
				"http://www.w3.org/2001/XMLSchema#int", 
				"http://www.w3.org/2001/XMLSchema#float", 
				"http://www.w3.org/2001/XMLSchema#dateTime"
				);
		
		for (String type : datatypes) {
			String line = MeasurementTestUtil.createPropertyRangeLine(type);
			MeasurementTestUtil.conductMeasurement(measurements, log, line);
			assertTrue(m.getOccurs().containsKey(MeasurementTestUtil.predicateName));
			assertEquals(1, m.getOccurs().get(MeasurementTestUtil.predicateName).get(type));
			m.getOccurs().clear();
		}
	}
	
	@Test
	void otherStatementNQ() {
		List<String> objects = Arrays.asList(
				"78345",
				"test test test", 
				"2020-12-19T11:11:11",
				"53.5"
				);
		
		for (String object : objects) {
			String line = MeasurementTestUtil.createStringLine(object);
			MeasurementTestUtil.conductMeasurement(measurements, log, line);
			assertTrue(m.getOccurs().isEmpty());
		}
		
		String line = MeasurementTestUtil.createFloatLine("5025e-2");
		MeasurementTestUtil.conductMeasurement(measurements, log, line);
		assertTrue(m.getOccurs().isEmpty());
		
		line = MeasurementTestUtil.createDoubleLine("90");
		MeasurementTestUtil.conductMeasurement(measurements, log, line);
		assertTrue(m.getOccurs().isEmpty());
	}

}
