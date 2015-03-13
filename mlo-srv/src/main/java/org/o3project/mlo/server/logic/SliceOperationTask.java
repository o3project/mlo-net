/**
 * SliceOperationTask.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.logic;

import java.util.Map;
import java.util.concurrent.Callable;

import org.o3project.mlo.server.dto.RestifRequestDto;
import org.o3project.mlo.server.dto.RestifResponseDto;


/**
 * This interface designates the slice operation task.
 * The concrete tasks of creating, updating, deleting and getting a slice should implement this interface.
 *
 */
public interface SliceOperationTask extends Callable<RestifResponseDto> {
	
	/**
	 * Sets the request DTO, and then prepares to execute the task.
	 * @param reqDto the requested DTO to be executed.
	 */
	void setRequestDto(RestifRequestDto reqDto);
	
	/**
	 * Sets the request parameter maps.
	 * @param reqParamMap the request parameter map.
	 */
	void setRequestParamMap(Map<String, String> reqParamMap);
}
