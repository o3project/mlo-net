/**
 * CreateSliceTask.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import org.o3project.mlo.server.dto.RestifRequestDto;
import org.o3project.mlo.server.dto.RestifResponseDto;
import org.o3project.mlo.server.logic.EquipmentConfigurator;

/**
 * This is the implementation class to create slice in {@link SliceManager} class.
 */
public class CreateSliceTask extends SliceOperationBaseTask {
	
	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public RestifResponseDto call() throws Exception {
		RestifRequestDto reqDto = getRequestDto(); 
		EquipmentConfigurator equConf = 
				equipmentConfiguratorFactory.getConfigurator(
						reqDto.common.srcComponent.name);
		synchronized (sliceManager) {
			return sliceManager.createSlice(reqDto, equConf);
		}
	}
}
