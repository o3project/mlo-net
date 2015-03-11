/**
 * DeleteSliceTask.java
 * (C) 2013, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import org.o3project.mlo.server.dto.RestifRequestDto;
import org.o3project.mlo.server.dto.RestifResponseDto;
import org.o3project.mlo.server.logic.EquipmentConfigurator;

/**
 * This is the implementation class to delete slices in {@link SliceManager} class.
 */
public class DeleteSliceTask extends SliceOperationBaseTask {
	
	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.impl.logic.SliceOperationBaseTask#call()
	 */
	@Override
	public RestifResponseDto call() throws Exception {
		RestifRequestDto reqDto = getRequestDto(); 
		EquipmentConfigurator equConf = 
				equipmentConfiguratorFactory.getConfigurator(
						reqDto.common.srcComponent.name);
		synchronized (sliceManager) {
			return sliceManager.deleteSlice(reqDto, equConf);
		}
	}
}
