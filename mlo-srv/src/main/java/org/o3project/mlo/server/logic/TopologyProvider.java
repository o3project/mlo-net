/**
 * TopologyProvider.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.logic;

import java.util.Map;

import org.o3project.mlo.server.component.FlowObj;
import org.o3project.mlo.server.component.TopologyObj;

/**
 * This interface designates the provider feature of network topology.
 */
public interface TopologyProvider {

	/**
	 * Loads topology.
	 * @return the result. true if successfully loaded.
	 */
	boolean load();

	/**
	 * Obtains the topology object loaded.
	 * @return the topology object.
	 */
	TopologyObj getTopologyObj();
	
	/**
	 * Obtains the flow object map.
	 * The key of the map is the name of the flow.
	 * @return the flow object map.
	 */
	Map<String, FlowObj> getFlowObjMap();
}
