/**
 * EquipmentConfiguratorNullImpl.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import org.o3project.mlo.server.dto.FlowDto;
import org.o3project.mlo.server.dto.RestifRequestDto;
import org.o3project.mlo.server.logic.EquipmentConfigurator;
import org.o3project.mlo.server.logic.MloException;

/**
 * This class is the null implementation class of {@link EquipmentConfigurator} interface.
 *
 */
public class EquipmentConfiguratorNullImpl implements EquipmentConfigurator {

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.EquipmentConfigurator#addFlow(org.o3project.mlo.server.dto.FlowDto)
	 */
	@Override
	public void addFlow(FlowDto addedFlowDto, FlowDto reqDto, RestifRequestDto restifRequestDto) throws MloException {
		// do nothing.
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.EquipmentConfigurator#delFlow(org.o3project.mlo.server.dto.FlowDto)
	 */
	@Override
	public void delFlow(FlowDto deletedFlowDto, RestifRequestDto restifRequestDto) throws MloException {
		// do nothing.
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.EquipmentConfigurator#modFlow(org.o3project.mlo.server.dto.FlowDto)
	 */
	@Override
	public void modFlow(FlowDto modifiedFlowDto, FlowDto reqDto, RestifRequestDto restifRequestDto) throws MloException {
		// do nothing.
	}
}
