package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measurements;

import org.apache.jena.datatypes.xsd.impl.RDFLangString;
import org.apache.jena.datatypes.xsd.impl.XSDBaseStringType;
import org.apache.jena.rdf.model.Literal;

import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils.HashMapInsertUtil;
import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils.StringUtil;

/**
 * Using String but Double should be used
 * 
 * Issue #8
 *
 */
public class CouldBeDouble extends MeasureOnObjectWithDatatypeString {

	@Override
	public void measure(String propertyName, Literal literal) {
		// Only Strings are of interest
		if (!(literal.getDatatype() instanceof RDFLangString)
				&& !(literal.getDatatype() instanceof XSDBaseStringType)) {
			return;
		}

		String lexicalValue = literal.getLexicalForm();

		if (StringUtil.isValidDouble(lexicalValue)) {
			HashMapInsertUtil.insertElement(propertyName, super.occurs);
		}
	}

}
