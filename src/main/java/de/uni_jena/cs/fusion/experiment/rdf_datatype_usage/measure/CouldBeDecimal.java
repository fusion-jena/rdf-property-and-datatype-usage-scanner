package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measure;

import org.apache.jena.datatypes.xsd.impl.RDFLangString;
import org.apache.jena.datatypes.xsd.impl.XSDBaseStringType;
import org.apache.jena.rdf.model.Literal;

import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils.MapInsertUtil;
import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils.StringUtil;

/**
 * Using String but Decimal could be used
 * 
 * Issue #5
 */
public class CouldBeDecimal extends MeasureOnObject {

	@Override
	public void measure(String propertyName, Literal literal) {
		// Only Strings are of interest
		if (!(literal.getDatatype() instanceof RDFLangString)
				&& !(literal.getDatatype() instanceof XSDBaseStringType)) {
			return;
		}

		String lexicalValue = literal.getLexicalForm();

		if (StringUtil.isValidDecimal(lexicalValue)) {
			MapInsertUtil.insertElement(literal.getDatatypeURI(), propertyName, super.occurs);
		}
	}

}
