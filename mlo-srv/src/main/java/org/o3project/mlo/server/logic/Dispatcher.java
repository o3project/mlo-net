/**
 * Dispatcher.java
 * (C) 2013, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.logic;

import java.util.Map;

import org.o3project.mlo.server.dto.RestifRequestDto;
import org.o3project.mlo.server.dto.RestifResponseDto;


/**
 * This interface designates the dispatcher interface of slice operation task {@link SliceOperationTask}.
 * The implementation class of this interface should control and dispatch slice operation tasks 
 */
public interface Dispatcher {

	/**
	 * Initializes this component.
	 * This method is called only at once after the application is launched.
	 * @throws MloException Failed to initialize.
	 */
	void init() throws MloException;
	
	/**
	 * Disposes this component.
	 * This method is called only at once in stopping the application.
	 * All tasks should be stopped in this method.
	 * @throws MloException Failed to dispose.
	 */
	void dispose() throws MloException;

	/**
	 * Controls and dispatches slice operation tasks.
	 * This method is thread-safe.
	 * The specified request DTO must be validated before this method is called.
	 * @param sliceOpTask the slice operation task.
	 * @param reqDto the request DTO.
	 * @return the response DTO.
	 * @throws MloException Error occurs in dispatching the task.
	 */
	RestifResponseDto dispatch(SliceOperationTask sliceOpTask, RestifRequestDto reqDto) throws MloException;

	/**
	 * Controls and dispatches slice operation tasks.
	 * This method is thread-safe.
	 * The specified request DTO must be validated before this method is called.
	 * @param sliceOpTask the slice operation task.
	 * @param paramMap the request parameter map. 
	 * @param reqDto the request DTO.
	 * @return the response DTO.
	 * @throws MloException Error occurs in dispatching the task.
	 */
	RestifResponseDto dispatch(SliceOperationTask sliceOpTask, Map<String, String> paramMap) throws MloException;
}
