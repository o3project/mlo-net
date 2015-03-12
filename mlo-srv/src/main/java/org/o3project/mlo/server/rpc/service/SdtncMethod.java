/**
 * SdtncMethod.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.rpc.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.o3project.mlo.server.rpc.dto.SdtncRequestDto;
import org.o3project.mlo.server.rpc.dto.SdtncResponseDto;


/**
 * This interface designates HTTP method to access to SDTNC VRM NBI.
 */
public interface SdtncMethod {
	
	/**
	 * Obtains HTTP method name.
	 * GET or POST or PUT or DELETE must be returned.
	 * @return the method name. 
	 */
	String getName();
	
	/**
	 * Designates whether data are written to request body or not.
	 * @return If true, data are written to request body.
	 */
	boolean isSetDoOutput();
	
	/**
	 * Writes request to output stream of request body.
	 * @param reqDto Request DTO.
	 * @param ostream Output stream of request body.
	 */
	void handleReqOutput(SdtncRequestDto reqDto, OutputStream ostream);
	
	/**
	 * Reads HTTP response from input stream.
	 * @param istream the input stream of response body.
	 * @return the response DTO.
	 */
	SdtncResponseDto handleResInput(InputStream istream);
	
	/**
	 * Obtains the connection timeout.
	 * This value is set in connecting to SDTNC.
	 * If this value is zero, no timeout is set.
	 * The unit is seconds.
	 * @return Timeout.
	 */
	Integer getConnectionTimeoutSec();

	/**
	 * Obtains the read timeout.
	 * This value is set in connecting to SDTNC.
	 * If this value is zero, no timeout is set.
	 * The unit is seconds.
	 * @return Timeout.
	 */
	Integer getReadTimeoutSec();
	
	/**
	 * Creates URL using path and query parameters.
	 * Base URL is retrieved from configuration.
	 * If base URL is http://myHost/myBasePath,
	 * path is myPath, query parameters are {"param1":"value1", "param2":"value2"},
	 * the following value will be returned.
	 * http://myHost/myBasePath/myPath?param1=value1&amp;param2=value2
	 * @param path the path.
	 * @param params the query parameters.
	 * @return URL.
	 */
	String constructUrl(String path, Map<String, String> params);
}
