/**
 * OdenOSConfigImpl.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.rpc.service;

import org.o3project.mlo.server.logic.ConfigConstants;
import org.o3project.mlo.server.logic.ConfigProvider;
import org.o3project.mlo.server.rpc.service.OdenOSConfig;

/**
 * OdenOSConfigImpl
 *
 */
public class OdenOSConfigImpl implements OdenOSConfig, ConfigConstants {
	
	private ConfigProvider configProvider;
	
	/**
	 * Setter method (for DI setter injection).
	 * @param configProvider The instance.
	 */
	public void setConfigProvider(ConfigProvider configProvider) {
		this.configProvider = configProvider;
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.OdenOSConfig#getRemoteSystemManagerId()
	 */
	@Override
	public String getRemoteSystemManagerId() {
		return configProvider.getProperty(PROP_KEY_ODENOS_REMOTE_SYSTEM_MANAGER_ID);
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.OdenOSConfig#getRemoteSystemManagerHost()
	 */
	@Override
	public String getRemoteSystemManagerHost() {
		return configProvider.getProperty(PROP_KEY_ODENOS_REMOTE_SYSTEM_MANAGER_HOST);
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.OdenOSConfig#getRemoteSystemManagerPort()
	 */
	@Override
	public Integer getRemoteSystemManagerPort() {
		String sPort = configProvider.getProperty(PROP_KEY_ODENOS_REMOTE_SYSTEM_MANAGER_PORT);
		return Integer.parseInt(sPort);
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.OdenOSConfig#getComponentManagerId()
	 */
	@Override
	public String getComponentManagerId() {
		return configProvider.getProperty(PROP_KEY_ODENOS_COMPONENT_MANAGER_ID);
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.OdenOSConfig#getComponentManagerHost()
	 */
	@Override
	public String getComponentManagerHost() {
		return configProvider.getProperty(PROP_KEY_ODENOS_COMPONENT_MANAGER_HOST);
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.OdenOSConfig#getComponentManagerPort()
	 */
	@Override
	public Integer getComponentManagerPort() {
		String sPort = configProvider.getProperty(PROP_KEY_ODENOS_COMPONENT_MANAGER_PORT);
		return Integer.parseInt(sPort);
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.OdenOSConfig#getComponentManagerBaseUri()
	 */
	@Override
	public String getComponentManagerBaseUri() {
		String prop = configProvider.getProperty(PROP_KEY_ODENOS_COMPONENT_MANAGER_BASE_URL);
		if (prop == null) {
			String host = getComponentManagerHost();
			Integer port = getComponentManagerPort();
			String objectId = getComponentManagerId();
			prop = getBaseUri(host, port, objectId);
		}
		return prop;
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.OdenOSConfig#getLauncherHost()
	 */
	@Override
	public String getLauncherHost() {
		return configProvider.getProperty(PROP_KEY_ODENOS_LAUNCHER_HOST);
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.OdenOSConfig#getLauncherPort()
	 */
	@Override
	public Integer getLauncherPort() {
		String sPort = configProvider.getProperty(PROP_KEY_ODENOS_LAUNCHER_PORT);
		return Integer.parseInt(sPort);
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.OdenOSConfig#getNetworkComponentIdL2()
	 */
	@Override
	public String getNetworkComponentIdL2() {
		return configProvider.getProperty(PROP_KEY_ODENOS_NETWORK_COMPONENT_ID_L2);
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.OdenOSConfig#getNetworkComponentIdL012()
	 */
	@Override
	public String getNetworkComponentIdL012() {
		return configProvider.getProperty(PROP_KEY_ODENOS_NETWORK_COMPONENT_ID_L012);
	}
	
	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.OdenOSConfig#isL2Included()
	 */
	@Override
	public Boolean isAvailableCreateL2() {
		return configProvider.getBooleanProperty(PROP_KEY_ODENOS_IS_AVAILABLE_CREATE_L2);
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.OdenOSConfig#getNetworkComponentPath()
	 */
	@Override
	public String getNetworkComponentPath() {
		return String.format("components/%s", getNetworkComponentIdL2());
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.OdenOSConfig#getPTDriverId()
	 */
	@Override
	public String getPTDriverId() {
		return configProvider.getProperty(PROP_KEY_ODENOS_PT_DRIVER_ID);
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.OdenOSConfig#getPTDriverPath()
	 */
	@Override
	public String getPTDriverPath() {
		return String.format("components/%s", getPTDriverId());
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.OdenOSConfig#getConnectionComponentId()
	 */
	@Override
	public String getConnectionId() {
		return configProvider.getProperty(PROP_KEY_ODENOS_CONNECTION_ID);
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.OdenOSConfig#getConnectionType()
	 */
	@Override
	public String getConnectionType() {
		return configProvider.getProperty(PROP_KEY_ODENOS_CONNECTION_TYPE);
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.OdenOSConfig#getConnectionState()
	 */
	@Override
	public String getConnectionState() {
		return configProvider.getProperty(PROP_KEY_ODENOS_CONNECTION_STATE);
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.OdenOSConfig#getConnectionPath()
	 */
	@Override
	public String getConnectionPath() {
		return String.format("connections/%s", getConnectionId());
	}
	
	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.OdenOSConfig#getResponseTimeout()
	 */
	@Override
	public Integer getResponseTimeout() {
		String timeout = configProvider.getProperty(PROP_KEY_ODENOS_RESPONSE_TIMEOUTSEC);
		return Integer.parseInt(timeout);
	}
	
	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.OdenOSConfig#isAvailableReqLinkEstablishedCompletion()
	 */
	@Override
	public Boolean isAvailableReqLinkEstablishedCompletion() {
		return configProvider.getBooleanProperty(PROP_KEY_ODENOS_IS_AVAILABLE_REQ_LINK_ESTABLISHED_COMPLETION);
	}
	
	/**
	 */
	private String getBaseUri(String host, Integer port, String objectId) {
		return String.format("odenos://%s:%d/%s", host, port, objectId);
	}
}
