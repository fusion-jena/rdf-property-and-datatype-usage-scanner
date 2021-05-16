package analyse;

import org.apache.jena.rdf.model.Literal;

public abstract class Measurement {
	/**
	 * TODO ersetzen / weg -> je nach Untersuchung Liste an Pairs besser?
	 * => in der jeweiligen Klasse realisiert
	 */
	protected long occurs = 0;
	
	/**
	 * 
	 * @param l - zu untersuchendes Literal
	 */
	public abstract void conductMeasurement(Literal l);
	
	public long getOccurs() {
		return occurs;
	}
}
