package main;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;

import measurements.StringDateTimeMeasurement;
import measurements.StringDecimalMeasurement;
import measurements.StringDoubleFloatMeasurement;
import measurements.StringDoubleFloatNotDecimalMeasurement;
import measurements.DoubleFloatDecimalMeasurement;
import measurements.Measurement;
import measurements.PropertyRangeMeasurement;
import utils.ModelUtil;

public class Main extends Object {

	/**
	 * Contains one class for each measurement that will be conducted
	 */
	private static List<Measurement<?, ?>> measurements;

	private static String dataPath;

	private static org.slf4j.Logger log;

	/**
	 * Initial setup
	 * 
	 * initalisation of measurements, initial creation of dataset for speed up set
	 * datapath init logger
	 */
	private static void initalisationProcess() {
		ModelFactory.createDefaultModel();
		initaliseMeasurementFunctions();
		dataPath = examplePaths();
		log = org.slf4j.LoggerFactory.getLogger(dataPath);
	}

	/**
	 * Initialises the list with the classes for each measurement that will be
	 * conducted
	 */
	private static void initaliseMeasurementFunctions() {
		measurements = new ArrayList<Measurement<?, ?>>();
		measurements.add(new StringDoubleFloatMeasurement());
		measurements.add(new StringDateTimeMeasurement());
		measurements.add(new StringDecimalMeasurement());
		measurements.add(new StringDoubleFloatNotDecimalMeasurement());
		measurements.add(new DoubleFloatDecimalMeasurement());
		measurements.add(new PropertyRangeMeasurement());
	}

	/**
	 * Easy switch of examplefiles TODO entfernen
	 * 
	 * @return String of the filename that will be examined
	 */
	private static String examplePaths() {
		String dataPath = "src/main/resources/";
//		dataPath += "ccrdf.html-rdfa.sample.nq";
//		dataPath += "dpef.html-rdfa.nq-00000";
//		dataPath += "test_nq.nq";
		dataPath += "customExample.nq";
//		dataPath += "long_nq.nq";
		return dataPath;
	}

	public static void main(String args[]) {
			
		initalisationProcess();

		List<Statement> allStatements = ModelUtil.parseLineByLine(dataPath, log);
		ModelUtil.conductMeasurements(measurements, allStatements, log);
		// get the results
		for (Measurement<?, ?> me : measurements) {
			log.info(me.toString());
		}
	}

}
