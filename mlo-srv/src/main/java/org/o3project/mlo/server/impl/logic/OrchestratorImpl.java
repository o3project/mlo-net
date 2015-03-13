/**
 * OrchestratorImpl.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import java.util.Map;

import org.o3project.mlo.server.dto.RestifCommonDto;
import org.o3project.mlo.server.dto.RestifErrorDto;
import org.o3project.mlo.server.dto.RestifRequestDto;
import org.o3project.mlo.server.dto.RestifResponseDto;
import org.o3project.mlo.server.logic.Dispatcher;
import org.o3project.mlo.server.logic.MloException;
import org.o3project.mlo.server.logic.NbiConstants;
import org.o3project.mlo.server.logic.Orchestrator;
import org.o3project.mlo.server.logic.SliceOperationTask;
import org.seasar.framework.container.annotation.tiger.Aspect;
import org.seasar.framework.container.annotation.tiger.DestroyMethod;
import org.seasar.framework.container.annotation.tiger.InitMethod;


/**
 * This class is the implementation class of {@link Orchestrator} interface.
 */
@Aspect("traceInterceptor")
public class OrchestratorImpl implements Orchestrator, NbiConstants {
	
	private Dispatcher dispatcher;

	/**
	 * Setter method (for DI setter injection).
	 * @param dispatcher The instance. 
	 */
	public void setDispatcher(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.Orchestrator#init()
	 */
	@Override
	@InitMethod
	public void init() {
		// nothing to do.
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.Orchestrator#dispose()
	 */
	@Override
	@DestroyMethod
	public void dispose() {
		// nothing to do.
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.Orchestrator#dispatch(org.o3project.mlo.server.logic.Method, org.o3project.mlo.server.dto.RestifRequestDto)
	 */
	@Override
	public RestifResponseDto handle(SliceOperationTask sliceOpTask, RestifRequestDto reqDto) {
		RestifResponseDto resDto = null;
		try {
			resDto = dispatcher.dispatch(sliceOpTask, reqDto);
		} catch (MloException e) {
			// Not logged here, but logged in dispatcher.
			resDto = createErrorResponse(reqDto, e);
		}
		return resDto;
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.Orchestrator#handle(org.o3project.mlo.server.logic.SliceOperationTask, java.util.Map)
	 */
	@Override
	public RestifResponseDto handle(SliceOperationTask sliceOpTask, Map<String, String> paramMap) {
		RestifResponseDto resDto = null;
		try {
			resDto = dispatcher.dispatch(sliceOpTask, paramMap);
		} catch (MloException e) {
			// Not logged here, but logged in dispatcher.
			String owner = null;
			if (paramMap != null) {
				owner = paramMap.get(REQPARAM_KEY_OWNER);
			}
			resDto = createErrorResponse(
					owner, DEFAULT_COMPONENT_NAME, e);
		}
		return resDto;
	}

	/**
	 * Creates an error response DTO.
	 * @param reqDto Request DTO.
	 * @param e Failure in processing.
	 * @return Response DTO.
	 */
	private RestifResponseDto createErrorResponse(RestifRequestDto reqDto, MloException e) {
		return createErrorResponse(
				reqDto.common.srcComponent.name, 
				reqDto.common.dstComponent.name, 
				e);
	}

	/**
	 * Creates an error response DTO.
	 * @param srcComponentName　Request source component name.
	 * @param dstComponentName Request destination component name.
	 * @param e Failure in processing.
	 * @return Response DTO.
	 */
	private RestifResponseDto createErrorResponse(
			String srcComponentName, String dstComponentName, MloException e) {
		RestifResponseDto resDto = new RestifResponseDto();
		resDto.common = RestifCommonDto.createInstance(1, 
				dstComponentName, 
				srcComponentName, 
				"Response");
		resDto.error = new RestifErrorDto();
		resDto.error.cause = e.getErrorName();
		resDto.error.detail = e.getMessage();
		resDto.error.sliceName = e.getSliceName();
		resDto.error.sliceId = e.getSliceId();
		return resDto;
	}
}
