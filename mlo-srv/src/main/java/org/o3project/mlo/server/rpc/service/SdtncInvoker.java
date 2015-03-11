/**
 * SdtncInvoker.java
 * (C) 2013, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.rpc.service;

import java.util.Map;

import org.o3project.mlo.server.logic.MloException;
import org.o3project.mlo.server.rpc.dto.SdtncRequestDto;
import org.o3project.mlo.server.rpc.dto.SdtncResponseDto;


/**
 * This interface designates feature to access to SDTNC VRM NBI.
 */
public interface SdtncInvoker {

	/**
	 * Accesses to SDTNC VRM NBI, and then returns the response.
	 * @param method HTTP methods. 
	 * @param reqDto Request DTO to SDTNC VRM NBI.
	 * @param path URL path.
	 * @param params the query parameter.
	 * @return Response DTO from SDTNC VRM NBI.
	 * @throws MloException Failed to access to SDTNC>
	 */
	SdtncResponseDto invoke(SdtncMethod method, 
			SdtncRequestDto reqDto, String path, Map<String, String> params) 
					throws MloException;

}
