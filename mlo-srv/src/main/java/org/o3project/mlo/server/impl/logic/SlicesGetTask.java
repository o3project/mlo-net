/**
 * SlicesGetTask.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.o3project.mlo.server.dto.RestifRequestDto;
import org.o3project.mlo.server.dto.RestifResponseDto;
import org.o3project.mlo.server.logic.SliceOperationTask;
import org.seasar.framework.container.annotation.tiger.Binding;


/**
 * This class is the implementation class to update a slice called in {@link SliceManager}.
 */
public class SlicesGetTask implements SliceOperationTask {
	
	@Binding
	private SliceManager sliceManager;
	
	private Map<String, String> paramMap = null;
	
	/**
	 * Setter method (for DI setter injection).
	 * @param sliceManager The instance. 
	 */
	public void setSliceManager(SliceManager sliceManager) {
		this.sliceManager = sliceManager;
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public RestifResponseDto call() throws Exception {
		Map<String, String> params = paramMap;
		if (params == null) {
			params = new HashMap<String, String>();
		}
		params = Collections.unmodifiableMap(params);
		
		synchronized (sliceManager) {
			return sliceManager.getSlices(params);
		}
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.SliceOperationTask#prepare(org.o3project.mlo.server.dto.RestifRequestDto)
	 */
	@Override
	public void setRequestDto(RestifRequestDto reqDto) {
		// do nothing.
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.SliceOperationTask#setRequestParamMap(java.util.Map)
	 */
	@Override
	public void setRequestParamMap(Map<String, String> reqParamMap) {
		this.paramMap = reqParamMap;
	}
}
