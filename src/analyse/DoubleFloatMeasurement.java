package analyse;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.jena.rdf.model.Literal;

/**
 * String statt Double / Float verwendet
 * Issue #8
 */
public class DoubleFloatMeasurement extends Measurement {
	
	public DoubleFloatMeasurement() {
		super(DoubleFloatMeasurement.class.getName());
	}
	
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
		//Weitere Spezifikation -> Int -> faellt hier auch rein
		//-> jeder Float ist auch Double
		try {
			//Zahl -> Double?
			doubleValue = NumberUtils.createDouble(lexicalValue);
//			logger.info(doubleValue);
			//TODO Abfangen von +- inf als Werte die nicht gezaehlt werden sollten?
			if(doubleValue != Double.POSITIVE_INFINITY && doubleValue != Double.NEGATIVE_INFINITY) {				
				super.occurs++;
			}
		}catch(NumberFormatException e) {
//			logger.error("Cannot parse", e);
//			logger.warn("Kein Double: " + literal);
		}
	}
	
	@Override
	public String toString() {
		return DoubleFloatMeasurement.class.getName() + ":\t" + super.occurs;
	}

}
