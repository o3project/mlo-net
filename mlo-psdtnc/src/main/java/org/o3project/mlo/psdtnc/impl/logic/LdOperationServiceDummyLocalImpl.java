/**
 * LdOperationServiceDummyImpl.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.psdtnc.impl.logic;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.o3project.mlo.psdtnc.logic.LdConfJsonConverter;
import org.o3project.mlo.psdtnc.logic.LdOperationException;
import org.o3project.mlo.psdtnc.logic.LdOperationService;
import org.o3project.mlo.server.dto.LdTopoDto;
import org.seasar.framework.util.ResourceUtil;

/**
 * LdOperationServiceDummyImpl
 *
 */
public class LdOperationServiceDummyLocalImpl implements LdOperationService {
	private static final Log LOG = LogFactory.getLog(LdOperationServiceDummyLocalImpl.class);

	private static final String TOPO_CONF_PATH = "org/o3project/mlo/psdtnc/impl/logic/dummy.topo.conf.default.dat";

	private final Object oMutex = new Object();
	
	private Integer status = 0;
	
	private LdConfJsonConverter ldConfJsonConverter;
	
	/**
	 * @param ldConfJsonConverter the ldConfJsonConverter to set
	 */
	public void setLdConfJsonConverter(LdConfJsonConverter ldConfJsonConverter) {
		this.ldConfJsonConverter = ldConfJsonConverter;
	}
	
	/* (non-Javadoc)
	 * @see org.o3project.mlo.psdtnc.logic.LdOperationService#doStart()
	 */
	@Override
	public void doStart() throws LdOperationException {
		LOG.info("doStart called.");
		synchronized (oMutex) {
			status = 1;
		}
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.psdtnc.logic.LdOperationService#doStop()
	 */
	@Override
	public void doStop() throws LdOperationException {
		LOG.info("doStop called.");
		synchronized (oMutex) {
			status = 0;
		}
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.psdtnc.logic.LdOperationService#doStatus()
	 */
	@Override
	public Integer doStatus() throws LdOperationException {
		LOG.info("doStatus called.");
		synchronized (oMutex) {
			return status;
		}
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.psdtnc.logic.LdOperationService#doExecuteSingleCommand(java.lang.String)
	 */
	@Override
	public Integer doExecuteSingleCommand(String shellCommand) throws LdOperationException {
		LOG.info("doStatus called. : " + shellCommand);
		synchronized (oMutex) {
			if (status == 1) {
				return 1;
			} else {
				throw new LdOperationException("Invalid status. : " + status);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.psdtnc.logic.LdOperationService#doLoadTopoConf()
	 */
	@Override
	public LdTopoDto doLoadTopoConf() throws LdOperationException {
		LdTopoDto dto = null;
		try {
			dto = doLoadTopoConfDummyLocal();
		} catch (IOException e) {
			String message = "Failed to load dummy local topo.conf file.";
			LOG.error(message, e);
			throw new LdOperationException(message, e);
		}
		return dto;
	}
	
	private LdTopoDto doLoadTopoConfDummyLocal() throws IOException {
		LdTopoDto dto = null;
		URL topoConfUrl = ResourceUtil.getResource(TOPO_CONF_PATH);
		InputStream stream = null;
		try {
			stream = topoConfUrl.openStream();
			dto = ldConfJsonConverter.convertFromConf(stream);
		} finally {
			if (stream != null) {
				stream.close();
				stream = null;
			}
		}
		return dto;
	}

}
