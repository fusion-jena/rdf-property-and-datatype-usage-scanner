package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.ModelFactory;
import org.apache.log4j.PropertyConfigurator;

import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measurements.FileMeasurement;
import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measurements.Measurement;
import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measurements.PropertyHasRange;
import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measurements.ShouldBeDateTime;
import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measurements.CouldBeBoolean;
import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measurements.ShouldBeDecimal;
import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measurements.CouldBeInteger;
import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measurements.ShouldBeDouble;
import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measurements.ShouldBeDoubleOrFloatNotDecimal;
import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measurements.ShouldBeFloat;
import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils.StringUtil;

public class MeasurementMain {

	/**
	 * Contains one class for each measurement that will be conducted
	 */
	private static List<Measurement<?, ?>> measurements;

	private static String dataPath;

	private static org.slf4j.Logger log;
	
	public MeasurementMain(String dataPath) {
		MeasurementMain.dataPath = dataPath;
		initalisationProcess();
		measurement();
	}

	/**
	 * Initial setup
	 * 
	 * initalisation of measurements, initial creation of dataset for speed up set
	 * datapath init logger
	 */
	private static void initalisationProcess() {
		ModelFactory.createDefaultModel();
		initaliseMeasurementFunctions();
		if (dataPath == null) {
			dataPath = examplePaths();
		}
		
		//TODO log austauschen?
		System.setProperty("fName", StringUtil.createStorageFile("log"));
		
		PropertyConfigurator.configure("src/main/resources/log4j.properties");
		log = org.slf4j.LoggerFactory.getLogger(dataPath);
	}
	
	/**
	 * Initialises the list with the classes for each measurement that will be
	 * conducted
	 */
	private static void initaliseMeasurementFunctions() {
		measurements = new ArrayList<Measurement<?, ?>>();
		measurements.add(new ShouldBeFloat());
		measurements.add(new ShouldBeDouble());
		measurements.add(new ShouldBeDateTime());
		measurements.add(new ShouldBeDoubleOrFloatNotDecimal());
		measurements.add(new ShouldBeDecimal());
		measurements.add(new PropertyHasRange());
		measurements.add(new CouldBeBoolean());
		measurements.add(new CouldBeInteger());
	}

	/**
	 * Easy switch of examplefiles TODO entfernen
	 * 
	 * @return String of the filename that will be examined
	 */
	private static String examplePaths() {
		String dataPath = "file:///C:/Users/Merle/Desktop/Uni2/fsu_jena/ss21/hiwi/analyse/src/main/resources/";
//		dataPath += "ccrdf.html-rdfa.sample.nq";
//		dataPath += "dpef.html-rdfa.nq-00000";
//		dataPath += "test_nq.nq";
		dataPath += "customExample.nq";
//		dataPath += "long_nq.nq";
		return dataPath;
	}

	/**
	 * If no argument is passed, one of the example files is used
	 * @param args
	 */
	public static void main(String args[]) {
				
		if(args.length == 1) {
			dataPath = args[0];
		}
			
		initalisationProcess();
		
		measurement();
		
	}
	
	private static void measurement() {
		FileMeasurement fileIterator = new FileMeasurement(dataPath, measurements, log);
		fileIterator.startMeasurement();
		// get the results
		for (Measurement<?, ?> me : measurements) {
			log.info(me.toString());
		}
	}

}
