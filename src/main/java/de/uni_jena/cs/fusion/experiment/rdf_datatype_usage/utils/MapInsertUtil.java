package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils;

import java.util.HashMap;
import java.util.Map;

public abstract class MapInsertUtil {

	/**
	 * Increase how often the key has occured by one
	 * 
	 * @param keyName Name of the key
	 * @param map HashMap where the quantity of the key is increased
	 */
	public static <K> void insertElement(K keyName, Map<K, Long> map) {
		Long value = map.putIfAbsent(keyName, 1L);
		if (value != null) {
			map.put(keyName, value + 1);
		}
	}

	/**
	 * Increase how often the property and the datatype occur in combination by one
	 * 
	 * First get the inner HashMap of the outer key
	 * </p>
	 * Then increase how often the inner key has occured
	 * 
	 * @param outerKey - Name of the property / key
	 * @param innerKey     - inner key of the inner map
	 * @param map    - Map in which the element will be inserted
	 */
	public static <K1, K2>void insertElement(K1 outerKey, K2 innerKey,
			Map<K1, Map<K2, Long>> map) {
		Map<K2, Long> newHashMap = new HashMap<K2, Long>();
		Map<K2, Long> innerMap = map.putIfAbsent(outerKey, newHashMap);
		if (innerMap == null) {
			newHashMap.put(innerKey, 1L);
		} else {
			insertElement(innerKey, innerMap);
		}
	}
}
