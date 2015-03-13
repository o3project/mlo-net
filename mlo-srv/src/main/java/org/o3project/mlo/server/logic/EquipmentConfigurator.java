/**
 * EquipmentConfigurator.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.logic;

import org.o3project.mlo.server.dto.FlowDto;
import org.o3project.mlo.server.dto.RestifRequestDto;

/**
 * This interface designates the configuration actor to node equipments.
 */
public interface EquipmentConfigurator {

	/**
	 * Configures nodes to add flow.
	 * @param newFlowDto the new flow.
	 * @param reqFlowDto the requested flow.
	 * @param restifRequestDto the request DTO.
	 * @throws MloException Error occurs in configuring.
	 */
	void addFlow(FlowDto newFlowDto, FlowDto reqFlowDto, RestifRequestDto restifRequestDto) throws MloException;
	
	/**
	 * Configures nodes to delete flow.
	 * @param deletedFlowDto the deleted flow. 
	 * @param restifRequestDto the request DTO.
	 * @throws MloException Error occurs in configuring.
	 */
	void delFlow(FlowDto deletedFlowDto, RestifRequestDto restifRequestDto) throws MloException;
	
	/**
	 * Configures nodes to modify flow.
	 * @param newFlowDto the new flow.
	 * @param reqFlowDto the requested flow.
	 * @param restifRequestDto the request DTO.
	 * @throws MloException Error occurs in configuring.
	 */
	void modFlow(FlowDto newFlowDto, FlowDto reqFlowDto, RestifRequestDto restifRequestDto) throws MloException;
}
