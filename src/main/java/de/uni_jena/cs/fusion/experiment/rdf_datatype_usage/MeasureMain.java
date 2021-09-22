package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.ModelFactory;
import org.apache.log4j.PropertyConfigurator;

import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measure.CouldBeBoolean;
import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measure.CouldBeDouble;
import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measure.CouldBeDoubleOrFloatNotDecimal;
import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measure.CouldBeFloat;
import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measure.CouldBeInteger;
import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measure.CouldBeTemporal;
import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measure.FileMeasure;
import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measure.Measure;
import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measure.ShouldBeDecimal;
import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measure.UsedAsPropertyRange;
import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils.StringUtil;

public class MeasureMain {

	/**
	 * Contains one class for each measurement that will be conducted
	 */
	private static List<Measure<?, ?>> measures;

	private static String dataPath;

	private static org.slf4j.Logger log;
	
	public MeasureMain(String dataPath) {
		MeasureMain.dataPath = dataPath;
		initalisationProcess();
		startMeasurement();
	}

	/**
	 * Initial setup
	 * 
	 * initalisation of measurements, initial creation of dataset for speed up set
	 * datapath init logger
	 */
	private static void initalisationProcess() {
		ModelFactory.createDefaultModel();
		initaliseMeasureFunctions();
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
	private static void initaliseMeasureFunctions() {
		measures = new ArrayList<Measure<?, ?>>();
		measures.add(new CouldBeFloat());
		measures.add(new CouldBeDouble());
		measures.add(new CouldBeTemporal());
		measures.add(new CouldBeDoubleOrFloatNotDecimal());
		measures.add(new ShouldBeDecimal());
		measures.add(new UsedAsPropertyRange());
		measures.add(new CouldBeBoolean());
		measures.add(new CouldBeInteger());
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
		
		startMeasurement();
		
	}
	
	private static void startMeasurement() {
		FileMeasure fileIterator = new FileMeasure(dataPath, measures, log);
		fileIterator.startMeasurements();
		// get the results
		for (Measure<?, ?> me : measures) {
			log.info(me.toString());
		}
	}

}
