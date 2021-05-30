package analyse;

import java.util.HashMap;

import org.apache.jena.rdf.model.Literal;
import org.apache.log4j.Logger;

/**
 * Untersuchung eine Literals auf ein Merkmal
 */
public abstract class Measurement {
	
	protected Logger logger;
	
	protected HashMap<String, Integer> occurs;
	
	public Measurement(String loggername) {
		logger = Logger.getLogger(loggername);
		occurs = new HashMap<>();
	}
	
	/**
	 * Untersuchung des Literals auf ein Merkmal 
	 * @param l - zu untersuchendes Literal
	 */
	public abstract void conductMeasurement(Literal l);
	
	/**
	 * Erhoeht die Vorkommen des Schluessels um 1, sollte der Schluessel noch 
	 * nicht gesetzt sein, wird die Anzahl der Vorkommen auf 1 gesetzt
	 * 
	 * @param key - Schluessel dessen vorkommen erhoeht werden soll
	 */
	protected void insertElement(String key) {
		Integer value = occurs.putIfAbsent(key, 1);
		if(value != null) {
			occurs.put(key, value + 1);
		}
	}
	
	public HashMap<String, Integer> getOccurs() {
		return occurs;
	}
	
	@Override
	public abstract String toString();
}
