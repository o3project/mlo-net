/**
 * SdtncInvokerImpl.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.rpc.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.o3project.mlo.server.logic.InternalException;
import org.o3project.mlo.server.logic.MloException;
import org.o3project.mlo.server.logic.OtherException;
import org.o3project.mlo.server.rpc.dto.SdtncRequestDto;
import org.o3project.mlo.server.rpc.dto.SdtncResponseDto;
import org.o3project.mlo.server.rpc.service.SdtncInvoker;
import org.o3project.mlo.server.rpc.service.SdtncMethod;


/**
 * This class is the implementation class of {@link SdtncInvoker} interface.
 *
 */
public class SdtncInvokerImpl implements SdtncInvoker {
	
	private static final int ONE_SECOND = 1000;
	
	private static final int SC_400_BAD_REQUEST = 400;

	/*
	 * (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.SdtncInvoker#invoke(org.o3project.mlo.server.rpc.service.SdtncMethod, org.o3project.mlo.server.rpc.dto.SdtncRequestDto, java.lang.String, java.util.Map)
	 */
	@Override
	public SdtncResponseDto invoke(SdtncMethod method, 
			SdtncRequestDto reqDto, String path, Map<String, String> params) 
					throws MloException {
		SdtncResponseDto resDto = null;
		try {
			resDto = invokeInternal(method, reqDto, path, params);
		} catch (IOException e) {
			throw new InternalException("SDTNC cannot be connected:" + e.getMessage(), e);
		}
		return resDto;
	}
	
	/**
	 * Invokes the process.
	 * @param method Method instance. 
	 * @param reqDto Request DTO.
	 * @param path URL path.
	 * @param paramMap Query parameter map.
	 * @return Response DTO.
	 * @throws IOException IO exception.
	 * @throws MloException API exception.
	 */
	private SdtncResponseDto invokeInternal(SdtncMethod method, 
			SdtncRequestDto reqDto, String path, Map<String, String> paramMap) 
					throws IOException, MloException {
		SdtncResponseDto resDto = null;
		
		URL url = null;
		HttpURLConnection conn = null;
		try {
			url = new URL(method.constructUrl(path, paramMap));
			conn = (HttpURLConnection) url.openConnection();
			if (method.getConnectionTimeoutSec() != null) {
				conn.setConnectTimeout(method.getConnectionTimeoutSec() * ONE_SECOND);				
			}
			if (method.getReadTimeoutSec() != null) {
				conn.setReadTimeout(method.getReadTimeoutSec() * ONE_SECOND);				
			}
			conn.setRequestMethod(method.getName());
			
			boolean isHandleOut = method.isSetDoOutput();
			conn.setDoOutput(isHandleOut);
			if (isHandleOut) {
				conn.setRequestProperty("Content-Type", "application/xml; charset=UTF-8");
				try(OutputStream ostream = conn.getOutputStream()) {
					method.handleReqOutput(reqDto, ostream);
				}
			}
			
			int statusCode = conn.getResponseCode();
			
			try (InputStream istream = conn.getInputStream()) {
				resDto = method.handleResInput(istream);
			}
			
			if (statusCode >= SC_400_BAD_REQUEST) {
				throw new OtherException("SDTNCAccessError/" + statusCode);
			}
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		
		return resDto;
	}
}
