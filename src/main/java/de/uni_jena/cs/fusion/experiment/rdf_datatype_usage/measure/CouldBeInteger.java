package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measure;

import org.apache.jena.datatypes.xsd.impl.RDFLangString;
import org.apache.jena.datatypes.xsd.impl.XSDBaseStringType;
import org.apache.jena.rdf.model.Literal;

import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils.StringUtil;
import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils.MapInsertUtil;

/**
 * Using String but Integer should be used
 *
 * Issue #17
 */
public class CouldBeInteger extends MeasureOnObject {

	@Override
	public void measure(String propertyName, Literal literal) {
		// Only strings are of interest
		if (!(literal.getDatatype() instanceof RDFLangString)
				&& !(literal.getDatatype() instanceof XSDBaseStringType)) {
			return;
		}

		String lexicalValue = literal.getLexicalForm();

		if (StringUtil.isValidInteger(lexicalValue)) {
			MapInsertUtil.insertElement(literal.getDatatypeURI(), propertyName, super.occurs);
		}

	}

}
