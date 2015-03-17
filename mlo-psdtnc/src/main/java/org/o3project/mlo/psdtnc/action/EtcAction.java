/**
 * EtcAction.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.psdtnc.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.arnx.jsonic.JSON;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.o3project.mlo.server.dto.LdTopoDto;
import org.o3project.mlo.server.logic.LdTopologyRepository;
import org.o3project.mlo.server.logic.MloException;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.struts.annotation.Execute;

/**
 * EtcAction
 *
 */
public class EtcAction {
	
	private static final Log LOG = LogFactory.getLog(EtcAction.class);
	
	@Resource 
	private HttpServletRequest request;
	
	@Resource 
	private HttpServletResponse response;
	
	private LdTopologyRepository topologyRepository;
	
	/**
	 * @param topologyRepository the topologyRepository to set
	 */
	public void setTopologyRepository(LdTopologyRepository topologyRepository) {
		this.topologyRepository = topologyRepository;
	}

	@Execute(validator = false, urlPattern = "ld/topo")
	public String ldTopo() throws MloException, IOException {
		logAccess(request);
		if (isGetMethod(request.getMethod())) {
			response.setContentType("application/json; charset=utf-8");
			LdTopoDto obj = topologyRepository.getLdTopo();
			writeJsonTo(obj, response.getOutputStream());
		} else {
			throw new UnsupportedOperationException("Unsupported HTTP method.");
		}
		return null;
	}
	
	@Execute(validator = false, urlPattern = "dummy/ryu/switches")
	public String dummyRyuSwitches() throws IOException {
		logAccess(request);
		String path = "org/o3project/mlo/psdtnc/impl/logic/dummy.switches.default.dat";
		HttpServletResponse res = response;
		return dummyRyu(path, res);
	}
	
	@Execute(validator = false, urlPattern = "dummy/ryu/links")
	public String dummyRyuLinks() throws IOException {
		logAccess(request);
		String path = "org/o3project/mlo/psdtnc/impl/logic/dummy.links.default.dat";
		HttpServletResponse res = response;
		return dummyRyu(path, res);
	}

	/**
	 * @param path
	 * @param res
	 * @return
	 * @throws IOException
	 */
	String dummyRyu(String path, HttpServletResponse res) throws IOException {
		if (isGetMethod(request.getMethod())) {
			response.setContentType("application/json; charset=utf-8");
			URL url = ResourceUtil.getResource(path);
			InputStream inputStream = null;
			OutputStream outputStream = null;
			byte[] buf = new byte[1024];
			int rlen = -1;
			outputStream = res.getOutputStream();
			try {
				inputStream = url.openStream();
				while (!Thread.currentThread().isInterrupted()) {
					rlen = inputStream.read(buf, 0, buf.length);
					if (rlen < 0) {
						break;
					}
					outputStream.write(buf, 0, rlen);
				}
			} finally {
				if (inputStream != null) {
					inputStream.close();
					inputStream = null;
				}
			}
		} else {
			throw new UnsupportedOperationException("Unsupported HTTP method.");
		}
		return null;
	}
	
	void writeJsonTo(Object obj, OutputStream ostream) throws IOException {
		String body = JSON.encode(obj, true);
		ostream.write(body.getBytes("UTF-8"));
		if (LOG.isDebugEnabled()) {
			LOG.debug("The following body written.");
			LOG.debug(body);
		}
	}
	
	private boolean isGetMethod(String methodName) {
		return ActionUtil.isSupportingHttpMethod(methodName, "GET");
	}
    
    /**
     * Logs access log.
     * @param req HTTP request.
     */
    static void logAccess(HttpServletRequest req) {
    	if (LOG.isInfoEnabled()) {
    		LOG.info(String.format("%s %s %s", 
    				req.getMethod(), req.getRequestURI(), req.getQueryString()));
    	}
    }
}
