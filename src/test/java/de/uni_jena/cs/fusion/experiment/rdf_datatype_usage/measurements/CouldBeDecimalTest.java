package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measurements;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils.StringUtil;

class CouldBeDecimalTest {

	private List<String> validNumbers = Arrays.asList(
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
	
	private List<String> invalidNumbers = Arrays.asList(
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
	
	private org.slf4j.Logger log;
	private List<Measure<?, ?>> measures;
	private CouldBeDecimal m;
	
	@BeforeEach
	void init() {
		log = org.slf4j.LoggerFactory.getLogger("test");
		measures = new ArrayList<Measure<?, ?>>();
		m = new CouldBeDecimal();
		measures.add(m);
	}
	
	
	
	@Test
	void validStringsNQ() {
		for (String number : validNumbers) {
			String line = MeasureTestUtil.createStringLine(number);
			MeasureTestUtil.conductMeasurement(measures, log, line);
			assertEquals(1, m.getOccurs().get(MeasureTestUtil.predicateName));
			m.getOccurs().clear();
		}
	}
	
	@Test
	void validStringsMethod() {
		for (String number : validNumbers) {
			assertTrue(StringUtil.isValidDecimal(number));
		}
	}

	@Test
	void invalidStringsNQ() {
		for(String number : invalidNumbers) {
			String line = MeasureTestUtil.createStringLine(number);
			MeasureTestUtil.conductMeasurement(measures, log, line);
			assertTrue(m.getOccurs().isEmpty());
		}
	}
	
	@Test
	void invalidStringsMethod() {
		for (String number : invalidNumbers) {
			assertFalse(StringUtil.isValidDecimal(number));
		}
	}

}
