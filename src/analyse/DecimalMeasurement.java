package analyse;

import org.apache.jena.datatypes.xsd.impl.RDFLangString;
import org.apache.jena.datatypes.xsd.impl.XSDBaseStringType;
import org.apache.jena.rdf.model.Literal;

/**
 * Verwendung von String statt Decimal 
 * 
 * Issue #5
 */
public class DecimalMeasurement extends Measurement {

	public DecimalMeasurement() {
		super(DecimalMeasurement.class.getName());
	}

	@Override
	public void conductMeasurement(Literal literal) {
		// Nur Strings von Interesse
		if (!(literal.getDatatype() instanceof RDFLangString)
				&& !(literal.getDatatype() instanceof XSDBaseStringType)) {
			System.err.println("Hier");
		}

		String lexicalValue = literal.getLexicalForm();

		if (StringUtil.isValidDecimal(lexicalValue)) {
			super.insertElement("xsd:decimal");
		}
	}

	@Override
	public String toString() {
		String s = DecimalMeasurement.class.getName() + ":\n";
		for (String key : super.occurs.keySet()) {
			s += "\t" + key + "\t" + super.occurs.get(key) + "\n";
		}
		return s;
	}

}
