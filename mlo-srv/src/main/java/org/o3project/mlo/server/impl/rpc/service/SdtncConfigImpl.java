/**
 * SdtncConfigImpl.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.rpc.service;

import org.o3project.mlo.server.logic.ConfigConstants;
import org.o3project.mlo.server.logic.ConfigProvider;
import org.o3project.mlo.server.rpc.service.SdtncConfig;
import org.seasar.framework.container.annotation.tiger.Binding;


/**
 * This class is the implementation class of {@link SdtncConfig} interface.
 */
public class SdtncConfigImpl implements SdtncConfig, ConfigConstants {
	
	@Binding
	private ConfigProvider configProvider;
	
	/**
	 * Setter method (for DI setter injection).
	 * @param configProvider The instance. 
	 */
	public void setConfigProvider(ConfigProvider configProvider) {
		this.configProvider = configProvider;
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.SdtncConfig#getNbiBaseUri()
	 */
	@Override
	public String getNbiBaseUri() {
		return configProvider.getProperty(PROP_KEY_SDTNC_NBI_BASE_URI);
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.SdtncConfig#getConnectionTimeoutSec()
	 */
	@Override
	public Integer getConnectionTimeoutSec() {
		return configProvider.getIntegerProperty(PROP_KEY_SDTNC_CONNECTION_TIMEOUT_SEC);
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.SdtncConfig#getReadTimeoutSec()
	 */
	@Override
	public Integer getReadTimeoutSec() {
		return configProvider.getIntegerProperty(PROP_KEY_SDTNC_READ_TIMEOUT_SEC);
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.SdtncConfig#getDummyInvokerSetFlag()
	 */
	@Override
	public boolean getDummyInvokerSetFlag() {
		return configProvider.getBooleanProperty(PROP_KEY_SDTNC_DUMMY_INVOKER_SET_FLAG);
	}
}
