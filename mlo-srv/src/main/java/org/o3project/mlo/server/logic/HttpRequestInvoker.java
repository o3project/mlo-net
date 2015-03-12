/**
 * HttpRequestInvoker.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.logic;

import java.io.IOException;

/**
 * This interface designates the HTTP requester.
 *
 */
public interface HttpRequestInvoker {

	/**
	 * Invokes the HTTP method.
	 * 
	 * @param <REQUEST_DTO> the type of request DTO.
	 * @param <RESPONSE_DTO> the type of response DTO.
	 * @param uri URI.
	 * @param method the method object.
	 * @param reqObj the request instance including request body.
	 * @param resClazz the type of the response DTO.
	 * @return the response instance including response body.
	 * @throws HttpRequestInvokerException Failed to invoke the HTTP API.
	 * @throws IOException the I/O exception. 
	 */
	<REQUEST_DTO, RESPONSE_DTO> 
	RESPONSE_DTO invoke(String uri, HttpRequestMethod method, REQUEST_DTO reqObj, Class<RESPONSE_DTO> resClazz)
		throws HttpRequestInvokerException, IOException;
}
