package analyse;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.jena.rdf.model.Literal;

/**
 * String statt xsd:dateTime verwendet
 * Issue #6
 */
public class DateTimeMeasurement extends Measurement {

	public DateTimeMeasurement() {
		super(DateTimeMeasurement.class.getName());
	}

	@Override
	public void conductMeasurement(Literal literal) {
		// Nur Strings von Interesse
		if (literal.getDatatype().getJavaClass() != String.class) {
			return;
		}

		String lexicalValue = literal.getLexicalForm();

		try {
			//xsd:datetime Format -> 'T' trennt Datum von Uhrzeit, escapen
			new SimpleDateFormat("yyyy-MM-DD'T'HH:mm:ss").parse(lexicalValue);
			//Wenn parsen erfolgreich war -> es handelt sich um ein Datum  
			super.occurs++;
		} catch (IllegalArgumentException | ParseException e) {
//			logger.error("Cannot parse", e);
//			logger.error("Kein Datum:\t" + lexicalValue);
		} 
	}
	
	@Override
	public String toString() {
		return DateTimeMeasurement.class.getName() + ":\t" + super.occurs;
	}

}
