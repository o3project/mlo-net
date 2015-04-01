/**
 * ActionUtil.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.action;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.o3project.mlo.server.dto.RestifCommonDto;
import org.o3project.mlo.server.dto.RestifErrorDto;
import org.o3project.mlo.server.dto.RestifRequestDto;
import org.o3project.mlo.server.dto.RestifResponseDto;
import org.o3project.mlo.server.logic.ApiCallException;
import org.o3project.mlo.server.logic.Orchestrator;
import org.o3project.mlo.server.logic.Serdes;
import org.o3project.mlo.server.logic.SliceOperationTask;
import org.o3project.mlo.server.logic.Validator;


/**
 * This is an utility class used in processing for application boundary layer.
 */
public class ActionUtil {
	
	private static final Log LOG = LogFactory.getLog(ActionUtil.class);
	
	private ActionUtil() {
		
	}

    /**
     * Checks whether a specified HTTP method is supported or not. 
     * @param httpMethod Requested HTTP method.
     * @param supportingMethod Supported HTTP method.
     * @return true if supported, otherwise false. 
     */
    static boolean isSupportingHttpMethod(String httpMethod, String supportingMethod) {
    	return isSupportingHttpMethod(httpMethod, new String[]{supportingMethod});
    }
    
    /**
     * Checks whether specified HTTP method is supported or not. 
     * @param httpMethod Requested HTTP method.
     * @param supportingMethod Supported HTTP method.
     * @return true if supported, otherwise false. 
     */
    static boolean isSupportingHttpMethod(String httpMethod, String[] supportingMethods) {
    	boolean bIsSupporting = false;
    	if (httpMethod != null) {
    		for (String supportingMethod : supportingMethods) {
    			if (httpMethod.equalsIgnoreCase(supportingMethod)) {
    				bIsSupporting = true;
    				break;
    			}
    		}
    	}
    	return bIsSupporting;
    }
    
    /**
     * Executes requests.
     * @param orchestrator Orchestrator instance.O
     * @param serdes Serializer-deserializer instance. 
     * @param validator Validator instance for MLO REST I/F.
     * @param sliceOpTask Slice operation task.
     * @param istream Input stream instance including requested MLO API.
     * @param ostream Output stream instance which is MLO API response.
     * @return Always returns null.l
     */
	public static String doAction(Orchestrator orchestrator, Serdes serdes, Validator validator, 
			SliceOperationTask sliceOpTask, 
			InputStream istream, OutputStream ostream) {
		return doAction(orchestrator, serdes, validator, 
				sliceOpTask, new LinkedHashMap<String, String>(), istream, ostream);
    }
    
    /**
     * Executes requests.
     * @param orchestrator Orchestrator instance.O
     * @param serdes Serializer-deserializer instance. 
     * @param validator Validator instance for MLO REST I/F.
     * @param sliceOpTask Slice operation task.
     * @param reqHeaderMap Request header map.
     * @param istream Input stream instance including requested MLO API.
     * @param ostream Output stream instance which is MLO API response.
     * @return Always returns null.l
     */
	public static String doAction(Orchestrator orchestrator, Serdes serdes, Validator validator, 
			SliceOperationTask sliceOpTask, Map<String, String> reqHeaderMap, 
			InputStream istream, OutputStream ostream) {
    	RestifRequestDto reqDto = null;
    	RestifResponseDto resDto = null;
    	try {
    		// Deserialize XML-body request.
    		reqDto = serdes.deserialize(istream, reqHeaderMap.get("content-type"));
    		
    		if (LOG.isDebugEnabled()) {
    			if (null == reqDto) {
            		LOG.debug("########## POST ACTION START (request is null)");
    			} else {
            		LOG.debug("########## POST ACTION START (SliceName : " 
            				+ (reqDto.slice == null ? "null" : reqDto.slice.name) + ", SliceId : " 
            						+ (reqDto.slice == null ? "null" : reqDto.slice.id) + ")");
    			}
    		}
    		
    		// Validates deserialized object.
    		validator.validate(reqDto);
    		
    		// Processes the request.
    		resDto = orchestrator.handle(sliceOpTask, reqDto);
    	} catch (ApiCallException e) {
    		// Creates an error response if an error occurs.
    		resDto = new RestifResponseDto();
    		resDto.common = new RestifCommonDto();
    		resDto.error = new RestifErrorDto();
    		resDto.error.cause = e.getErrorName();
    		resDto.error.detail = e.getMessage();
    	} finally {
    		if (resDto != null) {
    			serdes.serialize(resDto, ostream, reqHeaderMap.get("accept"));
    		}
    		if (LOG.isDebugEnabled()) {
    			if (null == reqDto) {
            		LOG.debug("########## POST ACTION END (request is null)");
    			} else {
            		LOG.debug("########## POST ACTION END (SliceName : " 
            				+ (reqDto.slice == null ? "null" : reqDto.slice.name) + ", SliceId : " 
            						+ (reqDto.slice == null ? "null" : reqDto.slice.id) + ")");
    			}
    		}
    	}
    	return null;
    }
}
