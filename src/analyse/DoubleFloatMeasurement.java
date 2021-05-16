package analyse;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.jena.rdf.model.Literal;
import org.apache.log4j.Logger;

/**
 * Wie hauefig String statt Double / Float verwendet wurde 
 * Issue #8
 */
public class DoubleFloatMeasurement extends Measurement {
	
	private static final Logger LOGGER = Logger.getLogger("DoubleFloatMeasurement");

	@Override
	public void conductMeasurement(Literal literal) {
		//Nur Strings von Interesse
		if(literal.getDatatype().getJavaClass() != String.class){
			return;
		}
		
		String lexicalValue = literal.getLexicalForm();
		//Keine Zahl
		if(! NumberUtils.isCreatable(lexicalValue)) {
			return;
		}
		
		double doubleValue = 0.0; //TODO
		//TODO von Interesse ob Float oder Double oder nur allgemein?
		//-> jeder Float ist auch Double
		try {
			//Zahl -> Double?
			doubleValue = NumberUtils.createDouble(lexicalValue);
			LOGGER.info(doubleValue);
			super.occurs++;
		}catch(NumberFormatException e) {
			LOGGER.error("Cannot parse", e);
			LOGGER.warn("Kein Double: " + literal);
		}
	}

}
