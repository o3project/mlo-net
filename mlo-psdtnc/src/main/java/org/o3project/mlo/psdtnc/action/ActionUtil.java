/**
 * ActionUtil.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.psdtnc.action;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

/**
 * This is an utility class used in processing for application boundary layer.
 */
public final class ActionUtil {
	
	private ActionUtil() {
		super();
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
	static void handleNotImplementedMethod(HttpServletResponse response) throws IOException {
		response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED, "Not implemented.");
	}
}
