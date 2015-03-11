/**
 * UpdateSliceTask.java
 * (C) 2013, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import org.o3project.mlo.server.dto.RestifRequestDto;
import org.o3project.mlo.server.dto.RestifResponseDto;
import org.o3project.mlo.server.logic.EquipmentConfigurator;

/**
 * {@link SliceManager} クラスを利用した、スライス変更操作タスククラスです
 *
 */
public class UpdateSliceTask extends SliceOperationBaseTask {
	
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
			return sliceManager.updateSlice(reqDto, equConf);
		}
	}
}
