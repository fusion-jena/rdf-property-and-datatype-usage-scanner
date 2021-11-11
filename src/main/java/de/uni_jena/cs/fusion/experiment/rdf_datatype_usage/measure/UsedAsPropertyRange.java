package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measure;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.vocabulary.RDFS;

import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils.MapInsertUtil;

/**
 * Check, if a statement defines a range for a property
 * 
 * Issue #4
 */
public class UsedAsPropertyRange extends Measure {

	@Override
	public void measure(RDFNode subject, Property property, RDFNode object) {
		// check if in this statement the property is rdfs:range
		if (!RDFS.range.equals(property)) {
			return;
		}
		// datatype, property
		MapInsertUtil.insertElement(object.toString(), subject.toString(), super.occurs);
	}

}
