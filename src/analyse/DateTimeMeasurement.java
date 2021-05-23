package analyse;

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
		//TODO anpassen auf String Zeug von rdf 
		if (literal.getDatatype().getJavaClass() != String.class) {
			return;
		}

		String lexicalValue = literal.getLexicalForm();
		
		if(StringUtil.isValidDate(lexicalValue)) {
			super.occurs++;
		}

	}
	
	@Override
	public String toString() {
		return DateTimeMeasurement.class.getName() + ":\t" + super.occurs;
	}

}
