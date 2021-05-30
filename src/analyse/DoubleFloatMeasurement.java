package analyse;

import org.apache.jena.datatypes.xsd.impl.RDFLangString;
import org.apache.jena.datatypes.xsd.impl.XSDBaseStringType;
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
		if (!(literal.getDatatype() instanceof RDFLangString)
				&& !(literal.getDatatype() instanceof XSDBaseStringType)) {
			System.err.println("Hier");
		}
		
		String lexicalValue = literal.getLexicalForm();
		
		if(StringUtil.isValidFloat(lexicalValue)) {
			super.insertElement("Float");
			super.insertElement("Double");
		}else if(StringUtil.isValidDouble(lexicalValue)) {
			super.insertElement("Double");
		}
	}
		
	@Override
	public String toString() {
		String s = DoubleFloatMeasurement.class.getName() + ":\n";
		for (String key : super.occurs.keySet()) {
			s += "\t" + key + "\t" + super.occurs.get(key) + "\n";
		}
		return s;
	}

}
