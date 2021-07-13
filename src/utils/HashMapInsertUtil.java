package utils;

import java.util.HashMap;

public abstract class HashMapInsertUtil {

	/**
	 * Increase how often the key has occured by one
	 * 
	 * @param keyName Name of the key
	 * @param hashMap HashMap where the quantity of the key is increased
	 */
	public static <K> void insertElement(K keyName, HashMap<K, Long> hashMap) {
		Long value = hashMap.putIfAbsent(keyName, 1L);
		if (value != null) {
			hashMap.put(keyName, value + 1);
		}
	}

	/**
	 * Increase how often the property and the datatype occur in combination by one
	 * 
	 * First get the inner HashMap of the property
	 * </p>
	 * Then increase how often the data type has occured
	 * 
	 * @param outerKey - Name of the property / key
	 * @param innerKey     - inner key of the inner HashMap
	 * @param hashMap    - HashMap in which the element will be inserted
	 */
	public static <K1, K2>void insertElement(K1 outerKey, K2 innerKey,
			HashMap<K1, HashMap<K2, Long>> hashMap) {
		HashMap<K2, Long> newHashMap = new HashMap<K2, Long>();
		HashMap<K2, Long> datatypeHashMap = hashMap.putIfAbsent(outerKey, newHashMap);
		if (datatypeHashMap == null) {
			newHashMap.put(innerKey, 1L);
		} else {
			insertElement(innerKey, datatypeHashMap);
		}
	}
}
