/**
 * SlicesAction.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.action;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.o3project.mlo.server.dto.RestifCommonDto;
import org.o3project.mlo.server.dto.RestifErrorDto;
import org.o3project.mlo.server.dto.RestifResponseDto;
import org.o3project.mlo.server.form.SlicesForm;
import org.o3project.mlo.server.logic.ApiCallException;
import org.o3project.mlo.server.logic.NbiConstants;
import org.o3project.mlo.server.logic.Orchestrator;
import org.o3project.mlo.server.logic.Serdes;
import org.o3project.mlo.server.logic.SliceOperationTask;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

/**
 * This is an extended MLO API action class.
 * In this package, actions for "/DEMO/slices/" path are provided.
 */
public class SlicesAction implements NbiConstants {
	
	private static final Log LOG = LogFactory.getLog(SlicesAction.class);
	
	@Resource 
	private HttpServletRequest request;
	
	@Resource 
	private HttpServletResponse response;
	
	@Resource
	@ActionForm
	private SlicesForm slicesForm;

	@Binding
	private Serdes serdes;
	
	@Binding
	private Orchestrator orchestrator;
	
	@Binding(value = "slicesGetTask")
	private SliceOperationTask sliceOpTask;

	/**
	 * Handles requests to "slices/" path.
	 * Query parameter "owner" is required.
     * @return Always returns null.
     * @throws IOException An exception. 
	 */
	@Execute(validator = false)
	public String index() throws IOException {
    	logAccess(request);
    	String jsp = null;
    	if (ActionUtil.isSupportingHttpMethod(request.getMethod(), "GET")) {
    		Map<String, String> paramMap = createParamMap(slicesForm);
    		jsp = doGetAction(orchestrator, serdes, sliceOpTask, 
    				paramMap, response.getOutputStream());
    	} else {
    		throw new RuntimeException("Unsupported HTTP method.");
    	}
    	return jsp;
	}
	
	/**
	 * Converts query parameter form instance to parameter map.
	 * @param form Form instance 
	 * @return Parameter map.
	 */
	private Map<String, String> createParamMap(SlicesForm form) {
		Map<String, String> paramMap = new HashMap<String, String>();
		if (form.owner != null) {
			paramMap.put(REQPARAM_KEY_OWNER, form.owner);
		}
		return paramMap;
	}
	
	
	/**
     * Processes requests.
     * @param orchestrator Orchestrator instance. 
     * @param serdes Serializer-deserializer instance.
     * @param sliceOpTask Slice operation task.
	 * @param paramMap requested parameter map.
     * @param ostream Output stream.
     * @return Always returns null.
	 */
	private static String doGetAction(Orchestrator orchestrator,
			Serdes serdes,
			SliceOperationTask sliceOpTask, 
			Map<String, String> paramMap, OutputStream ostream) {
    	RestifResponseDto resDto = null;
    	try {
    		if (LOG.isDebugEnabled()) {
    			LOG.debug("########## GET ACTION START : owner=" + paramMap.get("owner"));
    		}
    		// Validates query params.
    		validateParamMap(paramMap);
    		
    		// Handles requests.
    		resDto = orchestrator.handle(sliceOpTask, paramMap);
    	} catch (ApiCallException e) {
    		// Creates error response if failed.
    		resDto = new RestifResponseDto();
    		resDto.common = RestifCommonDto.createInstance(1, 
    				DEFAULT_COMPONENT_NAME, null, "Response");
    		resDto.error = new RestifErrorDto();
    		resDto.error.cause = e.getErrorName();
    		resDto.error.detail = e.getMessage();
    	} finally {
    		if (resDto != null) {
    			serdes.marshal(resDto, ostream);
    		}
    		if (LOG.isDebugEnabled()) {
    			LOG.debug("########## GET ACTION END  : owner=" + paramMap.get("owner"));
    		}
    	}
    	return null;
    }
	
	/**
	 * Validates requested parameters.
	 * If invalid, throws exception.
	 * @param paramMap Requested parameter map. 
	 * @throws ApiCallException Thrown when validation fails.
	 */
	private static void validateParamMap(Map<String, String> paramMap) 
			throws ApiCallException {
		if (paramMap.get(REQPARAM_KEY_OWNER) == null) {
			throw new ApiCallException("Parameter (owner) is not specified.");
		}
	}
    
    /**
     * Logs http access.
     * @param req HTTP requests.P
     */
    static void logAccess(HttpServletRequest req) {
    	if (LOG.isInfoEnabled()) {
    		LOG.info(String.format("%s %s %s", 
    				req.getMethod(), req.getRequestURI(), req.getQueryString()));
    	}
    }
	
}
