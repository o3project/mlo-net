/**
 * HttpRequestInvokerImpl.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.o3project.mlo.server.logic.HttpRequestInvoker;
import org.o3project.mlo.server.logic.HttpRequestInvokerException;
import org.o3project.mlo.server.logic.HttpRequestMethod;

/**
 * An implementation class of {@link HttpRequestInvoker} interface.
 */
public class HttpRequestInvokerImpl implements HttpRequestInvoker {
	/*
	 * (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.HttpRequestInvoker#invoke(java.lang.String, org.o3project.mlo.server.logic.HttpRequestMethod, java.lang.Object, java.lang.Class)
	 */
	@Override
	public <REQUEST_DTO, RESPONSE_DTO> 
	RESPONSE_DTO invoke(String uri, HttpRequestMethod method, REQUEST_DTO reqObj, Class<RESPONSE_DTO> resClazz) 
			throws HttpRequestInvokerException, IOException {

		RESPONSE_DTO resDto;
		URL url = null;
		HttpURLConnection conn = null;

		try {
			// Connects to uri.
			url = new URL(uri);
			conn = (HttpURLConnection) url.openConnection();
			if (method.getConnectionTimeoutMilliSec() != null) {
				conn.setConnectTimeout(method.getConnectionTimeoutMilliSec());
			}
			if (method.getReadTimeoutMilliSec() != null) {
				conn.setReadTimeout(method.getReadTimeoutMilliSec());
			}
			// Sets the method name.
			conn.setRequestMethod(method.getName());

			// Sets whether the request has the content body or not.
			boolean isHandleOut = method.isSetDoOutput();
			conn.setDoOutput(isHandleOut);

			// Invoke to send request.
			if (isHandleOut) {
				Map<String, String> reqProps = new HashMap<String, String>();
				method.setRequestProperties(reqProps);
				for (Map.Entry<String, String> entry : reqProps.entrySet()) {
					conn.setRequestProperty(entry.getKey(), entry.getValue());
				}

				OutputStream ostream = null;
				try {
					ostream = conn.getOutputStream();
					method.handleReqOutput(reqObj, ostream);
				} finally {
					if (ostream != null) {
						ostream.close();
						ostream = null;
					}
				}
			}

			// Obtains the status code.
			int statusCode = conn.getResponseCode();

			// Receives the response body.
			InputStream istream = null;
			try {
				istream = conn.getInputStream();
				resDto = method.handleResInput(statusCode, istream, resClazz);
			} finally {
				if (istream != null) {
					istream.close();
					istream = null;
				}
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
