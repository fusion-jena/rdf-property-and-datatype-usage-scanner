package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measure;

import org.apache.jena.datatypes.xsd.impl.XSDDouble;
import org.apache.jena.datatypes.xsd.impl.XSDFloat;
import org.apache.jena.rdf.model.Literal;

import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils.MapInsertUtil;
import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils.NumberUtil;

/**
 * Using Double or Float but Decimal should be used
 * 
 * Issue #7
 */
public class ShouldBeDecimal extends MeasureOnObject {

	@Override
	public void measure(String propertyName, Literal literal) {
		String datatype;
		String lexicalForm = literal.getLexicalForm();
		if (literal.getDatatype() instanceof XSDFloat) {
			datatype = XSDFloat.XSDfloat.getURI();
			float floatValue = literal.getFloat();
			if (!NumberUtil.shouldBeReplacedByDecimal(floatValue, lexicalForm)) {
				return;
			}
		} else if (literal.getDatatype() instanceof XSDDouble) {
			datatype = XSDDouble.XSDdouble.getURI();
			double doubleValue = literal.getDouble();
			if (!NumberUtil.shouldBeReplacedByDecimal(doubleValue, lexicalForm)) {
				return;
			}
		} else {
			return;
		}

		MapInsertUtil.insertElement(datatype, propertyName, super.occurs);
	}

}
