package test.java.measurements;

import java.util.List;

import org.apache.jena.rdf.model.Statement;

import main.java.measurements.Measurement;
import main.java.utils.ModelUtil;

public final class MeasurementTestUtil {

	public static final String predicateName = "test:predicate";

	private static String doubleIRI = "<http://www.w3.org/2001/XMLSchema#double>";
	private static String floatIRI = "<http://www.w3.org/2001/XMLSchema#float>";
	private static String rangeIRI = "<http://www.w3.org/2000/01/rdf-schema#range>";

	public static final String createDoubleLine(String object) {
		String line = "<test:subject> <" + predicateName + "> ";
		line += "\"" + object + "\"^^" + doubleIRI + " <test:graph>.";
		return line;
	}

	public static final String createFloatLine(String object) {
		String line = "<test:subject> <" + predicateName + "> ";
		line += "\"" + object + "\"^^" + floatIRI + " <test:graph>.";
		return line;
	}

	public static final String createStringLine(String object) {
		String line = "<test:subject> <" + MeasurementTestUtil.predicateName + "> ";
		line += "\"" + object + "\" <test:graph>.";
		return line;
	}

	public static final String createPropertyRangeLine(String object) {
		String line = "<" + predicateName + "> " + rangeIRI + " <" + object + "> <test:graph>.";
		return line;
	}

	public static final void conductMeasurement(List<Measurement<?, ?>> measurements, org.slf4j.Logger log,
			String line) {
		List<Statement> allStatements = ModelUtil.createStatementsFromLine(line);
		ModelUtil.conductMeasurements(measurements, allStatements, log);
	}

}