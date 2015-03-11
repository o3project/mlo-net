/**
 * Orchestrator.java
 * (C) 2013, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.logic;

import java.util.Map;

import org.o3project.mlo.server.dto.RestifRequestDto;
import org.o3project.mlo.server.dto.RestifResponseDto;


/**
 * This interface designates orchestration logic in MLO.
 */
public interface Orchestrator {
	
	/**
	 * Initializes this components.
	 * This method is called when the application is launched.
	 */
	void init();
	
	/**
	 * Disposes this components.
	 * This method is called when the application is stopped.
	 */
	void dispose();
	
	/**
	 * Handles the task requested from MLO client to MLO web API.
	 * This method is the facade of the core feature of MLO.
	 * This method must not throw any exception.
	 * The error occurred in processing this method must be returned as response DTO. 
	 * @param sliceOpTask the slice operation task.
	 * @param reqDto the request DTO.
	 * @return the response DTO.
	 */
	RestifResponseDto handle(SliceOperationTask sliceOpTask, RestifRequestDto reqDto);

	/**
	 * Handles the task requested from MLO client to MLO web API.
	 * This method is the facade of the core feature of MLO.
	 * This method is for requests those do not have request body, e.g. GET method.
	 * This method must not throw any exception.
	 * The error occurred in processing this method must be returned as response DTO. 
	 * @param sliceOpTask the slice operation task.
	 * @param paramMap Query parameter map.
	 * @return the response DTO.
	 */
	RestifResponseDto handle(SliceOperationTask sliceOpTask, Map<String, String> paramMap);
}
