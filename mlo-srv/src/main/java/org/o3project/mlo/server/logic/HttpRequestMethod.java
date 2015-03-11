/**
 * HttpRequestMethod.java
 * (C) 2015, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.logic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * This is an interface which designates a method of HTTP request.
 */
public interface HttpRequestMethod {
	
	/**
	 * Obtains the HTTP method name of this request.
	 * GET, POST, PUT, DELETE and so on.
	 * @return The method name.
	 */
	String getName();
	
	/**
	 * Specifies whether this request writes HTTP request body or not.
	 * For GET or DELETE method, request body will not be needed.
	 * @return If true, the request body is written.
	 */
	boolean isSetDoOutput();
	
	/**
	 * Sets request properties.
	 * @param requestProperties The request properties.
	 */
	void setRequestProperties(Map<String, String> requestProperties);
	
	/**
	 * Writes the request body to output stream in invoking the HTTP request.
	 * @param <REQUEST_DTO> The type of the DTO object written to HTTP request body.
	 * @param reqDto The request DTO.
	 * @param ostream The output stream for the request body.
	 */
	<REQUEST_DTO> void handleReqOutput(REQUEST_DTO reqDto, OutputStream ostream);
	
	/**
	 * Handles the HTTP response as a response DTO from input stream.
	 * @param <RESPONSE_DTO> The type of the DTO object received from HTTP response body.
	 * @param statusCode Status code of HTTP Response
	 * @param istream The input stream for the response body.
	 * @param resClazz The type of the response DTO object.
	 * @return The response DTO.
	 * @throws HttpRequestInvokerException An Exception occurred in handling.
	 * @throws IOException An IO Exception.
	 */
	<RESPONSE_DTO> RESPONSE_DTO handleResInput(int statusCode, InputStream istream, Class<RESPONSE_DTO> resClazz) 
			throws HttpRequestInvokerException, IOException;
	
	/**
	 * Obtains the connection timeout.
	 * The HTTP connection uses the value obtained by this method.
	 * If this method returns zero, no timeout is set.
	 * @return timeout [milliseconds].
	 */
	Integer getConnectionTimeoutMilliSec();

	/**
	 * Obtains the read timeout.
	 * The HTTP connection uses the value obtained by this method.
	 * If this method returns zero, no timeout is set.
	 * @return timeout [milliseconds].
	 */
	Integer getReadTimeoutMilliSec();
}
