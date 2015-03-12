/**
 * ConfigProvider.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.logic;

import java.util.Map;

/**
 * This is the interface which provides the configuration of MLO.
 */
public interface ConfigProvider {

	/**
	 * Obtains the property map, which is loaded from property files.
	 * @return the property map.
	 */
	Map<String, String> getProperties();
	
	/**
	 * Obtains the property value, which is loaded from property files.
	 * @param key the propery key. 
	 * @return the propery value.
	 */
	String getProperty(String key);
	
	/**
	 * Obtains the property value as boolean type.
	 * If "true" is set in property files, this returns true.
	 * Otherwise, returns false.
	 * @param key the property key.
	 * @return the property value.
	 */
	Boolean getBooleanProperty(String key);
	
	/**
	 * Obtains the property value as integer type.
	 * If the property does not exist or the property value cannot be paused as integer,
	 * this returns null.
	 * @param key the property key.
	 * @return the property value.
	 */
	Integer getIntegerProperty(String key);

	/**
	 * Obtains the comma-separated property value as string array.
	 * @param key the property key.
	 * @return the property value.
	 */
	String[] getCommaSplitedProperty(String key);
	
	/**
	 * Obtains the property values as property map with specified key prefix string.
	 * The keys of the property map are strings which are rest parts of the specified key prefix.
	 * The property value with perfect-matching key to the key prefix is not included in the propery map.
	 * @param keyPrefix the key prefix. 
	 * @return the propery map.
	 */
	Map<String, String> getSubProperties(String keyPrefix);
}
