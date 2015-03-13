/**
 * HttpRequestInvokerDummyImpl.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import net.arnx.jsonic.JSON;

import org.o3project.mlo.server.logic.HttpRequestInvoker;
import org.o3project.mlo.server.logic.HttpRequestInvokerException;
import org.o3project.mlo.server.logic.HttpRequestMethod;
import org.seasar.framework.util.ResourceUtil;

class HttpRequestInvokerDummyImpl implements HttpRequestInvoker {
	
	static final String DEFAULT_LD_TOPO_URI = "http://127.0.0.1:8080/nbiService/etc/ld/topo";
	static final String DEFAULT_RY_SWITCHES_URI = "http://127.0.0.1:8888/v1.0/topology/switches";
	static final String DEFAULT_RY_LINKS_URI    = "http://127.0.0.1:8888/v1.0/topology/links";

	static final String DATA_DIR_PATH = "org/o3project/mlo/server/rpc/service/data/";
	static final String DATA_LD_TOPO     = "dummy.topo.conf.default.json";
	static final String DATA_RY_SWITCHES = "dummy.switches.default.dat";
	static final String DATA_RY_LINKS    = "dummy.links.default.dat";
	
	IOException ioExceptionThrownInInvoke = null;
	
	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.HttpRequestInvoker#invoke(java.lang.String, org.o3project.mlo.server.logic.HttpRequestMethod, java.lang.Object, java.lang.Class)
	 */
	@Override
	public <REQUEST_DTO, RESPONSE_DTO> RESPONSE_DTO invoke(String uri, HttpRequestMethod method, REQUEST_DTO reqObj, Class<RESPONSE_DTO> resClazz)
			throws HttpRequestInvokerException, IOException {
		if (ioExceptionThrownInInvoke != null) {
			throw ioExceptionThrownInInvoke;
		}
		String path = null;
		switch (uri) {
		case DEFAULT_LD_TOPO_URI:
			path = DATA_DIR_PATH + DATA_LD_TOPO;
			break;
		case DEFAULT_RY_SWITCHES_URI:
			path = DATA_DIR_PATH + DATA_RY_SWITCHES;
			break;
		case DEFAULT_RY_LINKS_URI:
			path = DATA_DIR_PATH + DATA_RY_LINKS;
			break;
		default:
			throw new UnsupportedOperationException("not implemented uri: " + uri);
		}
		RESPONSE_DTO resDto = null;
		if (path != null) {
			URL url = ResourceUtil.getResource(path);
			InputStream istream = null;
			try {
				istream = url.openStream();
				resDto = JSON.decode(istream, resClazz);
			} finally {
				if (istream != null) {
					istream.close();
					istream = null;
				}
			}
		}
		return resDto;
	}
}