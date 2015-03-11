/**
 * LdServiceImpl.java
 * (C) 2015, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.impl.rpc.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.arnx.jsonic.JSON;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.o3project.mlo.server.dto.LdTopoDto;
import org.o3project.mlo.server.dto.RyLinkDto;
import org.o3project.mlo.server.dto.RySwitchDto;
import org.o3project.mlo.server.logic.ConfigConstants;
import org.o3project.mlo.server.logic.ConfigProvider;
import org.o3project.mlo.server.logic.HttpRequestInvoker;
import org.o3project.mlo.server.logic.HttpRequestInvokerException;
import org.o3project.mlo.server.logic.HttpRequestMethod;
import org.o3project.mlo.server.logic.InternalException;
import org.o3project.mlo.server.logic.MloException;
import org.o3project.mlo.server.logic.OtherException;
import org.o3project.mlo.server.rpc.service.LdService;
import org.seasar.framework.util.ResourceUtil;

/**
 * This class is the implementation class of {@link LdService} interface.
 */
public class LdServiceImpl implements LdService, ConfigConstants {
	private static final Log LOG = LogFactory.getLog(LdServiceImpl.class);
	
	ConfigProvider configProvider;
	
	HttpRequestInvoker httpRequestInvoker;
	
	final HttpRequestMethod httpRequestMethod = new LdGetMethodImpl();
	
	/**
	 * Setter method (for DI setter injection).
	 * @param configProvider the configProvider to set
	 */
	public void setConfigProvider(ConfigProvider configProvider) {
		this.configProvider = configProvider;
	}
	
	/**
	 * Setter method (for DI setter injection).
	 * @param httpRequestInvoker the httpRequestInvoker to set
	 */
	public void setHttpRequestInvoker(HttpRequestInvoker httpRequestInvoker) {
		this.httpRequestInvoker = httpRequestInvoker;
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.LdService#getLdTopo()
	 */
	@Override
	public LdTopoDto getLdTopo() throws MloException {
		LdTopoDto dto = null;
		if (!isRemoteDummyEnable()) {
			dto = getRemoteLdTopo();
		} else {
			dto = loadDtoFromResource("org/o3project/mlo/server/rpc/service/data/dummy.topo.conf.default.dat", LdTopoDto.class);
		}
		return dto;
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.LdService#getRySwitches()
	 */
	@Override
	public List<RySwitchDto> getRySwitches() throws MloException {
		List<RySwitchDto> dto = null;
		if (!isRemoteDummyEnable()) {
			dto = getRemoteRySwitches();
		} else {
			dto = Arrays.asList(loadDtoFromResource("org/o3project/mlo/server/rpc/service/data/dummy.switches.default.dat", RySwitchDto[].class));
		}
		return dto;
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.LdService#getRyLinks()
	 */
	@Override
	public List<RyLinkDto> getRyLinks() throws MloException {
		List<RyLinkDto> dto = null;
		if (!isRemoteDummyEnable()) {
			dto = getRemoteRyLinks();
		} else {
			dto = Arrays.asList(loadDtoFromResource("org/o3project/mlo/server/rpc/service/data/dummy.links.default.dat", RyLinkDto[].class));
		}
		return dto;
	}

	private LdTopoDto getRemoteLdTopo() throws MloException {
		LdTopoDto dto = null;
		try {
			dto = httpRequestInvoker.invoke(getLdTopoUri(), httpRequestMethod, null, LdTopoDto.class);
		} catch (HttpRequestInvokerException e) {
			throw new OtherException("Failed to get LdTopoDto.", e);
		} catch (IOException e) {
			throw new OtherException("Failed to get LdTopoDto.", e);
		}
		return dto;
	}

	private List<RySwitchDto> getRemoteRySwitches() throws MloException {
		List<RySwitchDto> dto = null;
		try {
			RySwitchDto[] dtoArray = httpRequestInvoker.invoke(getRySwitchesUri(), httpRequestMethod, null, RySwitchDto[].class);
			dto = Arrays.asList(dtoArray);
		} catch (HttpRequestInvokerException e) {
			throw new OtherException("Failed to get RySwitchDto.", e);
		} catch (IOException e) {
			throw new OtherException("Failed to get RySwitchDto.", e);
		}
		return dto;
	}

	private List<RyLinkDto> getRemoteRyLinks() throws MloException {
		List<RyLinkDto> dto = null;
		try {
			RyLinkDto[] dtoArray = httpRequestInvoker.invoke(getRyLinksUri(), httpRequestMethod, null, RyLinkDto[].class);
			dto = Arrays.asList(dtoArray);
		} catch (HttpRequestInvokerException e) {
			throw new OtherException("Failed to get RyLinkDto.", e);
		} catch (IOException e) {
			throw new OtherException("Failed to get RyLinkDto.", e);
		}
		return dto;
	}

	private String getLdTopoUri() {
		return configProvider.getProperty(ConfigConstants.PROP_KEY_LD_LD_TOPO_URI);
	}
	
	private String getRySwitchesUri() {
		return configProvider.getProperty(ConfigConstants.PROP_KEY_LD_RY_SWITCHES_URI);
	}
	
	private String getRyLinksUri() {
		return configProvider.getProperty(ConfigConstants.PROP_KEY_LD_RY_LINKS_URI);
	}
	
	private boolean isRemoteDummyEnable() {
		return configProvider.getBooleanProperty(PROP_KEY_LD_DEBUG_REMOTE_DUMMY_ENABLE);
	}
	
	private <DTO> DTO loadDtoFromResource(String path, Class<DTO> dtoClass) throws MloException {
		DTO dto = null;
		InputStream inputStream = null;
		URL url = ResourceUtil.getResource(path);
		try {
			try {
				inputStream = url.openStream();
				dto = JSON.decode(inputStream, dtoClass);
			} finally {
				if (inputStream != null) {
					inputStream.close();
					inputStream = null;
				}
			}
		} catch (IOException e) {
			LOG.error("Unexpected situation.", e);
			throw new InternalException("Unexpected situation.", e);
		}
		return dto;
	}

	/**
	 * This class is implementation class of {@link HttpRequestMethod} interface for LD access.
	 */
	class LdGetMethodImpl implements HttpRequestMethod {
	
		/* (non-Javadoc)
		 * @see org.o3project.mlo.server.logic.HttpRequestMethod#getName()
		 */
		@Override
		public String getName() {
			return "GET";
		}
	
		/* (non-Javadoc)
		 * @see org.o3project.mlo.server.logic.HttpRequestMethod#isSetDoOutput()
		 */
		@Override
		public boolean isSetDoOutput() {
			return false;
		}
	
		/* (non-Javadoc)
		 * @see org.o3project.mlo.server.logic.HttpRequestMethod#setRequestProperties(java.util.Map)
		 */
		@Override
		public void setRequestProperties(Map<String, String> requestProperties) {
			// do nothing.
		}
	
		/* (non-Javadoc)
		 * @see org.o3project.mlo.server.logic.HttpRequestMethod#handleReqOutput(java.lang.Object, java.io.OutputStream)
		 */
		@Override
		public <REQUEST_DTO> void handleReqOutput(REQUEST_DTO reqDto, OutputStream ostream) {
			// do nothing.
		}
	
		/* (non-Javadoc)
		 * @see org.o3project.mlo.server.logic.HttpRequestMethod#handleResInput(int, java.io.InputStream, java.lang.Class)
		 */
		@Override
		public <RESPONSE_DTO> RESPONSE_DTO handleResInput(int statusCode, InputStream istream, Class<RESPONSE_DTO> resClazz)
				throws HttpRequestInvokerException, IOException {
			return JSON.decode(istream, resClazz);
		}
	
		/* (non-Javadoc)
		 * @see org.o3project.mlo.server.logic.HttpRequestMethod#getConnectionTimeoutMilliSec()
		 */
		@Override
		public Integer getConnectionTimeoutMilliSec() {
			return configProvider.getIntegerProperty(PROP_KEY_LD_CONNECTION_TIMEOUT_MSEC);
		}
	
		/* (non-Javadoc)
		 * @see org.o3project.mlo.server.logic.HttpRequestMethod#getReadTimeoutMilliSec()
		 */
		@Override
		public Integer getReadTimeoutMilliSec() {
			return configProvider.getIntegerProperty(PROP_KEY_LD_READ_TIMEOUT_MSEC);
		}
	}
}
