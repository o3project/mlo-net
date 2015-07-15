/**
 * TopologyRepository.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.logic;

import org.o3project.mlo.server.dto.FlowDto;

/**
 * This interface designates the repository of network topology.
 */
public interface TopologyRepository {
	/**
	 * Searches a template of flow DTO optimum to specified flow DTO.
	 * Returns a template which has adequate CE nodes, band width and delay in request.
	 * If optimum template is not found, exception occurs.
	 * @param requestFlowDto the request flow DTO.
	 * @return the optimum template of flow DTO.
	 * @throws MloException Failed to search.
	 */
	FlowDto queryFlowTemplate(FlowDto requestFlowDto) throws MloException;
	
	/**
	 * Obtains VLAN ID for specified CE node name and CE port.
	 * If not found, returns null.
	 * @param ceNodeName CE node name.
	 * @param ceNodePortNo CE port.
	 * @return VLAN ID.
	 */
	String queryVlanId(String ceNodeName, String ceNodePortNo);
	
	/**
	 * Searches VLAN ID by flow DTO.
	 * @param flowDto the flow DTO.
	 * @return VLAN ID.
	 */
	String queryVlanId(FlowDto flowDto);
	
	/**
	 * Obtains the offset value of extra VLAN ID.
	 * @return the offset value of VLAN ID.
	 */
	Integer getExtraVlanIdOffset();
	
	String getComponentState(Class<?> componentObjClass, String name);
}
