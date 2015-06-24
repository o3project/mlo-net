/**
 * EtcAction.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.action;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.arnx.jsonic.JSON;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.o3project.mlo.server.component.NodeObj;
import org.o3project.mlo.server.dto.LdNodeDto;
import org.o3project.mlo.server.dto.LdTopoDto;
import org.o3project.mlo.server.dto.RyLinkDto;
import org.o3project.mlo.server.dto.RySwitchDto;
import org.o3project.mlo.server.impl.logic.TopologyRepositoryDefaultImpl;
import org.o3project.mlo.server.logic.LdTopologyRepository;
import org.o3project.mlo.server.logic.MloException;
import org.o3project.mlo.server.logic.TopologyRepository;
import org.seasar.struts.annotation.Execute;

/**
 * This is the action class to provide network topology.
 *
 */
public class EtcAction {
	
	private static final Log LOG = LogFactory.getLog(EtcAction.class);
	
	@Resource 
	private HttpServletRequest request;
	
	@Resource 
	private HttpServletResponse response;
	
	private TopologyRepository topologyRepository;
	private LdTopologyRepository ldTopologyRepository;
	
	/**
	 * @param topologyRepository the topologyRepository to set
	 */
	public void setTopologyRepository(TopologyRepositoryDefaultImpl topologyRepository) {
		this.topologyRepository = topologyRepository;
		this.ldTopologyRepository = topologyRepository;
	}

	/**
	 * Provides LD topology.
	 * @return LD topology.
	 * @throws MloException An exception thrown when MLO fails.
	 * @throws IOException An exception thrown when MLO failed to read/write to servlet stream.
	 */
	@Execute(validator = false, urlPattern = "ld/topo")
	public String ldTopo() throws MloException, IOException {
		logAccess(request);
		if (isGetMethod(request.getMethod())) {
			response.setContentType("application/json; charset=utf-8");
			LdTopoDto ldTopoDto = ldTopologyRepository.getLdTopo();
			LdTopoDto obj = createClonedObject(ldTopoDto);
			if (obj.switches != null) {
				for (LdNodeDto node : obj.switches) {
					String state = topologyRepository.getComponentState(NodeObj.class, node.name);
					if (state == null) {
						state = "ok";
					}
					node.state = state;
				}
			}
			if (obj.hosts != null) {
				for (LdNodeDto node : obj.hosts) {
					String state = topologyRepository.getComponentState(NodeObj.class, node.name);
					if (state == null) {
						state = "ok";
					}
					node.state = state;
				}
			}
			writeJsonTo(obj, response.getOutputStream());
		} else {
			throw new UnsupportedOperationException("Unsupported HTTP method.");
		}
		return null;
	}
	
	/**
	 * Provides Ryu switch topology.
	 * @return Ryu switch topology.
	 * @throws MloException An exception thrown when MLO fails.
	 * @throws IOException An exception thrown when MLO failed to read/write to servlet stream.
	 */
	@Execute(validator = false, urlPattern = "ryu/topology/switches")
	public String rySwitches() throws MloException, IOException {
		logAccess(request);
		if (isGetMethod(request.getMethod())) {
			response.setContentType("application/json; charset=utf-8");
			List<RySwitchDto> obj = ldTopologyRepository.getRySwitches();
			writeJsonTo(obj, response.getOutputStream());
		} else {
			throw new UnsupportedOperationException("Unsupported HTTP method.");
		}
		return null;
	}
	
	/**
	 * Provides Ryu link topology.
	 * @return Ryu link topology.
	 * @throws MloException An exception thrown when MLO fails.
	 * @throws IOException An exception thrown when MLO failed to read/write to servlet stream.
	 */
	@Execute(validator = false, urlPattern = "ryu/topology/links")
	public String ryLinks() throws MloException, IOException {
		logAccess(request);
		if (isGetMethod(request.getMethod())) {
			response.setContentType("application/json; charset=utf-8");
			List<RyLinkDto> obj = ldTopologyRepository.getRyLinks();
			writeJsonTo(obj, response.getOutputStream());
		} else {
			throw new UnsupportedOperationException("Unsupported HTTP method.");
		}
		return null;
	}

	/**
	 * Writes object to stream with json encoding.
	 * @param obj An object.
	 * @param ostream An stream written to
	 * @throws IOException An exception in writing to stream.
	 */
	void writeJsonTo(Object obj, OutputStream ostream) throws IOException {
		String body = JSON.encode(obj, true);
		ostream.write(body.getBytes("UTF-8"));
		if (LOG.isDebugEnabled()) {
			LOG.debug("The following body written.");
			LOG.debug(body);
		}
	}
	
	<TYPE> TYPE createClonedObject(TYPE obj) throws IOException {
		String body = JSON.encode(obj, true);
		@SuppressWarnings("unchecked")
		TYPE cloned = (TYPE) JSON.decode(body, obj.getClass());
		return cloned;
	}

	private boolean isGetMethod(String methodName) {
		return ActionUtil.isSupportingHttpMethod(methodName, "GET");
	}
    
    /**
     * Logs HTTP access.
     * @param req HTTP request.
     */
    static void logAccess(HttpServletRequest req) {
    	if (LOG.isInfoEnabled()) {
    		LOG.info(String.format("%s %s %s", 
    				req.getMethod(), req.getRequestURI(), req.getQueryString()));
    	}
    }
}
