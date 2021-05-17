package analyse;

import org.apache.jena.rdf.model.Literal;
import org.apache.log4j.Logger;

/**
 * Untersuchung eine Literals auf ein Merkmal
 */
public abstract class Measurement {
	
	protected Logger logger;
	
	/**
	 * TODO ersetzen / weg -> je nach Untersuchung Liste an Pairs besser?
	 * => in der jeweiligen Klasse realisiert
	 */
	protected long occurs = 0;
	
	public Measurement(String loggername) {
		logger = Logger.getLogger(loggername);
	}
	
	/**
	 * Untersuchung des Literals auf ein Merkmal 
	 * @param l - zu untersuchendes Literal
	 */
	public abstract void conductMeasurement(Literal l);
	
	public long getOccurs() {
		return occurs;
	}
	
	@Override
	public abstract String toString();
}
