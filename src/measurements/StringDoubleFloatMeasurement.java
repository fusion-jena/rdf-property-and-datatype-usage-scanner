package measurements;

import java.util.HashMap;

import org.apache.jena.datatypes.xsd.impl.RDFLangString;
import org.apache.jena.datatypes.xsd.impl.XSDBaseStringType;
import org.apache.jena.rdf.model.Literal;

import utils.GlobalNames;
import utils.StringUtil;
import utils.HashMapInsertUtil;

/**
 * Use of String when Float or Double should be used
 * 
 * Issue #8
 */
public class StringDoubleFloatMeasurement extends MeasurementOnObject<String, HashMap<GlobalNames, Long>> {
	
	public StringDoubleFloatMeasurement() {
		super();
		super.occurs= new HashMap<String, HashMap<GlobalNames, Long>>(); 
	}
	
	@Override
	public void conductMeasurement(String propertyName, Literal literal) {
		//Only Strings are of interest
		if (!(literal.getDatatype() instanceof RDFLangString)
				&& !(literal.getDatatype() instanceof XSDBaseStringType)) {
			return;
		}
		
		String lexicalValue = literal.getLexicalForm();
		
		if(StringUtil.isValidFloat(lexicalValue)) {
			HashMapInsertUtil.insertElement(propertyName, GlobalNames.FLOAT, super.occurs);
			HashMapInsertUtil.insertElement(propertyName, GlobalNames.DOUBLE, super.occurs);
		}else if(StringUtil.isValidDouble(lexicalValue)) {
			HashMapInsertUtil.insertElement(propertyName, GlobalNames.DOUBLE, super.occurs);
		}
	}
		
	@Override
	public String toString() {
		String s = StringDoubleFloatMeasurement.class.getName() + ":";
		for (String propertyName : super.occurs.keySet()) {
			HashMap<GlobalNames, Long> hashMap = super.occurs.get(propertyName);
			for (GlobalNames datatype : hashMap.keySet()) {
				s += "\n\t" + propertyName + "\t" + hashMap.get(datatype) + "\t" + datatype;
			}
		}
		return s;
	}

}
