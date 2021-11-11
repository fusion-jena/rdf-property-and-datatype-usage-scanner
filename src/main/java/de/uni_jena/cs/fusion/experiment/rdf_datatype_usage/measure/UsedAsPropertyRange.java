package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.measure;

import java.util.HashMap;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.vocabulary.RDFS;

/**
 * Check, if a statement defines a range for a property
 * 
 * Issue #4
 */
public class UsedAsPropertyRange extends Measure {

	@Override
	public void measure(RDFNode subject, Property property, RDFNode object) {
		// check if in this statement the property is rdfs:range
		if (RDFS.range.equals(property)) {
			// datatype, property
			super.occurs.computeIfAbsent(object.toString(), k -> new HashMap<String, Long>()).merge(subject.toString(),
					1L, Long::sum);
		}
	}

}
