/**
 * NbiConstants.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.logic;

/**
 * This interface defines constants related to MLO web API (NBI).
 */
public interface NbiConstants {

	/**
	 * The default component name.
	 */
	String DEFAULT_COMPONENT_NAME = "mlo";
	
	/**
	 * Key of query parameter which specifies the owner of the slice.
	 */
	String REQPARAM_KEY_OWNER = "owner";
	
	/**
	 * Key of query parameter which designates whether slice includes flow list or not.
	 */
	String REQPARAM_WITH_FLOW_LIST = "withFlowList";
}
