package analyse;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.log4j.Logger;
import org.apache.jena.rdf.model.Statement;

public class Main extends Object {
	
	private static List<Measurement> measurements;
	
	private static void initaliseMeasurementFunctions() {
		measurements = new ArrayList<Measurement>();
		measurements.add(new DoubleFloatMeasurement());	
		measurements.add(new DateTimeMeasurement());
		measurements.add(new DecimalMeasurement());
	}
	
//	static {
//		  BasicConfigurator.configure();
//		}
	
	
	public static void main(String args[]) {		

		initaliseMeasurementFunctions();
			
		String dataPath = "zusammengefuegte_ontologie.ttl";
		dataPath = "test.rdf";
		Model model = RDFDataMgr.loadModel(dataPath);

		StmtIterator iter = model.listStatements();
		
		while (iter.hasNext()) {
			Statement stmt = iter.nextStatement();
			RDFNode object = stmt.getObject();
			
			if(object.isLiteral()) {
				Literal literal = object.asLiteral();
				//Durchfuehrung aller Messungen auf den Literalen				
				for (Measurement m : measurements) {
					m.conductMeasurement(literal);
				}
				
			}
			
		}
		
		Logger logger = Logger.getLogger("analyse.Main");
		
		for(Measurement m : measurements){
			logger.info(m.toString());
		}
	}

}
