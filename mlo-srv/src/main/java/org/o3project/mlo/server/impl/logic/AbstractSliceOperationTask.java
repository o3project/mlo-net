/**
 * AbstractSliceOperationTask.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import java.util.Map;

import org.o3project.mlo.server.dto.RestifRequestDto;
import org.o3project.mlo.server.logic.ConfigConstants;
import org.o3project.mlo.server.logic.EquipmentConfiguratorFactory;
import org.o3project.mlo.server.logic.InternalException;
import org.o3project.mlo.server.logic.MloException;
import org.o3project.mlo.server.logic.SliceOperationTask;
import org.seasar.framework.container.annotation.tiger.Binding;


/**
 * This is the base class of {@link SliceOperationTask}.
 *
 */
public abstract class AbstractSliceOperationTask implements SliceOperationTask, ConfigConstants {
	
	@Binding
	protected EquipmentConfiguratorFactory equipmentConfiguratorFactory;
	
	private RestifRequestDto reqDto = null;
	
	@SuppressWarnings("unused")
	private Map<String, String> paramMap = null;
	
	/**
	 * Checks whether this task is ready or not.
	 * If not, throws an exception.
	 * @throws MloException An exception which means this task is not ready.
	 */
	protected void checkPreparation() throws MloException {
		if (reqDto == null) {
			throw new InternalException("reqDto is null.");
		}
	}

	/**
	 * Obtains the request object.
	 * @return The request DTO.
	 * @throws MloException An exception if this task is not ready.
	 */
	protected final RestifRequestDto getRequestDto() throws MloException {
		checkPreparation();
		return reqDto;
	}

	/*
	 * (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.SliceOperationTask#prepare(org.o3project.mlo.server.dto.RestifRequestDto)
	 */
	@Override
	public void setRequestDto(RestifRequestDto requestDto) {
		this.reqDto = requestDto;
	}
	
	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.SliceOperationTask#setRequestParamMap(java.util.Map)
	 */
	@Override
	public void setRequestParamMap(Map<String, String> reqParamMap) {
		this.paramMap = reqParamMap;
	}
}
