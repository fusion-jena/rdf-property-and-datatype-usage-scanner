package blind.experiment.rdf_datatype_usage.measure;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.jena.datatypes.xsd.impl.XSDDouble;
import org.apache.jena.datatypes.xsd.impl.XSDFloat;
import org.apache.jena.vocabulary.XSD;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import blind.experiment.rdf_datatype_usage.measure.Measure;
import blind.experiment.rdf_datatype_usage.measure.UsedAsPropertyRange;

class UsedAsPropertyRangeTest {

	private org.slf4j.Logger log;
	private List<Measure> measures;
	private UsedAsPropertyRange m;
	
	@BeforeEach
	void init() {
		log = org.slf4j.LoggerFactory.getLogger("test");
		measures = new ArrayList<Measure>();
		m = new UsedAsPropertyRange();
		measures.add(m);
	}
	
	@Test
	void propertyRangeStatementNQ() {
		List<String> datatypes = Arrays.asList(
				XSDDouble.XSDdouble.getURI(),//"http://www.w3.org/2001/XMLSchema#double",
				XSD.integer.getURI(),//"http://www.w3.org/2001/XMLSchema#int", 
				XSDFloat.XSDfloat.getURI(),//"http://www.w3.org/2001/XMLSchema#float", 
				XSD.dateTime.getURI()//"http://www.w3.org/2001/XMLSchema#dateTime"
				);
		
		for (String type : datatypes) {
			String line = MeasureTestUtil.createPropertyRangeLine(type);
			MeasureTestUtil.conductMeasurement(measures, log, line);
			assertEquals(1, m.getOccurs().get(type).get(MeasureTestUtil.predicateName));
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
			String line = MeasureTestUtil.createStringLine(object);
			MeasureTestUtil.conductMeasurement(measures, log, line);
			assertTrue(m.getOccurs().isEmpty());
		}
		
		String line = MeasureTestUtil.createFloatLine("5025e-2");
		MeasureTestUtil.conductMeasurement(measures, log, line);
		assertTrue(m.getOccurs().isEmpty());
		
		line = MeasureTestUtil.createDoubleLine("90");
		MeasureTestUtil.conductMeasurement(measures, log, line);
		assertTrue(m.getOccurs().isEmpty());
	}

}
