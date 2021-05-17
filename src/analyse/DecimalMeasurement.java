package analyse;

import org.apache.jena.rdf.model.Literal;

import java.util.regex.*;

/**
 * Verwendung von String statt Decimal 
 * Issue #5
 */
public class DecimalMeasurement extends Measurement {

	public DecimalMeasurement() {
		super(DecimalMeasurement.class.getName());
	}

	@Override
	public void conductMeasurement(Literal literal) {
		// Nur Strings von Interesse
		if (literal.getDatatype().getJavaClass() != String.class) {
			return;
		}
		
		//RegEx fuer xsd:decimal
		//optionales Vorzeichen +/-
		//beliebig viele Ziffern vor dem . <- Trennzeichen
		//wenn Kommazahl dann . 
		//Nachkommastellen oder Vorkommastellen mit mind. 1 Ziffer
		//Zwischen $ und ^ damit dies der einzige Inhalt des Strings ist
		Pattern pattern = Pattern.compile("^(\\+|-)?[0-9]*\\.?[0-9]+$");
		
		String lexicalValue = literal.getLexicalForm();		
		Matcher matcher = pattern.matcher(lexicalValue);
		
		if(matcher.find()) {
			super.occurs++;
//			logger.info("Decimal:\t" + lexicalValue);
		}
//		else {
//			logger.error("Kein Decimal:\t" + lexicalValue);
//		}
	}

	@Override
	public String toString() {
		return DecimalMeasurement.class.getName() + ":\t" + super.occurs;
	}

}
