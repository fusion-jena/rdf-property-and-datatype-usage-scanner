package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measure;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils.StringUtil;

class CouldBeBooleanTest {

	private List<String> validBoolean = Arrays.asList("true", "false", "0", "1");

	private List<String> invalidBoolean = Arrays.asList("True", "False", "0.0", "1.0", "dieser satz ergibt keinen sinn",
			"(8)", "231");

	private org.slf4j.Logger log;
	private List<Measure> measures;
	private CouldBeBoolean m;

	@BeforeEach
	void init() {
		log = org.slf4j.LoggerFactory.getLogger("test");
		measures = new ArrayList<Measure>();
		m = new CouldBeBoolean();
		measures.add(m);
	}

	/***********************************************************************************/
	/************************************
	 * Method
	 *****************************************/
	/***********************************************************************************/

	@Test
	void validBooleanMethod() {
		for (String validEntry : validBoolean) {
			assertTrue(StringUtil.isValidBoolean(validEntry));
		}
	}

	@Test
	void invalidBooleanMethod() {
		for (String invalidEntry : invalidBoolean) {
			assertFalse(StringUtil.isValidBoolean(invalidEntry));
		}
	}

	/***********************************************************************************/
	/*****************************************
	 * NQ
	 ****************************************/
	/***********************************************************************************/

	@Test
	void validBooleanNQ() {
		for (String validEntry : validBoolean) {
			String line = MeasureTestUtil.createStringLine(validEntry);
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
	void invalidBooleanNQ() {
		for (String invalidEntry : invalidBoolean) {
			String line = MeasureTestUtil.createStringLine(invalidEntry);
			MeasureTestUtil.conductMeasurement(measures, log, line);
			assertTrue(m.getOccurs().isEmpty());
		}
	}

}
