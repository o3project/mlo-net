/**
 * TopologyMeta.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.component;

import java.util.Map;

/**
 * This class is the meta information object of components.
 */
public class MetaObj {
	
	/**
	 * The component ID.
	 */
	public String id;
	
	/**
	 * Attributes of the component.
	 */
	public Map<String, Object> attributes;
}
