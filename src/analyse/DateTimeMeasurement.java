package analyse;

import org.apache.jena.datatypes.xsd.impl.RDFLangString;
import org.apache.jena.datatypes.xsd.impl.XSDBaseStringType;
import org.apache.jena.rdf.model.Literal;

/**
 * String statt xsd:dateTime verwendet
 * 
 * Issue #6
 */
public class DateTimeMeasurement extends Measurement {

	public DateTimeMeasurement() {
		super(DateTimeMeasurement.class.getName());
	}

	@Override
	public void conductMeasurement(Literal literal) {
		// Nur Strings von Interesse
		if (!(literal.getDatatype() instanceof RDFLangString)
				&& !(literal.getDatatype() instanceof XSDBaseStringType)) {
			System.err.println("Hier");
		}

		String lexicalValue = literal.getLexicalForm();

		if (StringUtil.isValidDate(lexicalValue)) {
			super.insertElement("xsd:dateTime");
		}

	}

	@Override
	public String toString() {
		String s = DateTimeMeasurement.class.getName() + ":\n";
		for (String key : super.occurs.keySet()) {
			s += "\t" + key + "\t" + super.occurs.get(key) + "\n";
		}
		return s;
	}

}
