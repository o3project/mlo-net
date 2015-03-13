/**
 * EquipmentConfiguratorFactoryImpl.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.o3project.mlo.server.logic.ConfigConstants;
import org.o3project.mlo.server.logic.ConfigProvider;
import org.o3project.mlo.server.logic.EquipmentConfigurator;
import org.o3project.mlo.server.logic.EquipmentConfiguratorFactory;
import org.seasar.framework.container.annotation.tiger.Binding;


/**
 * This class is the implementation class of {@link EquipmentConfiguratorFactory} interface.
 */
public class EquipmentConfiguratorFactoryImpl implements
		EquipmentConfiguratorFactory, ConfigConstants {
	
	private static final Log LOG = LogFactory.getLog(EquipmentConfiguratorFactoryImpl.class);
	
	private static final String FEATURE_VERSION_00_01 = "00-01";
	
	private static final String FEATURE_VERSION_00_02 = "00-02";
	
	private static final String FEATURE_VERSION_00_03 = "00-03";
	
	private static final String FEATURE_VERSION_LATEST = FEATURE_VERSION_00_03;
	
	@Binding
	private EquipmentConfigurator equipmentConfiguratorNullImpl;
	
	@Binding
	private EquipmentConfigurator equipmentConfiguratorDefaultImpl;
	
	@Binding
	private EquipmentConfigurator equipmentConfiguratorOtherImpl;
	
	@Binding
	private EquipmentConfigurator equipmentConfiguratorOptDeviceImpl;
	
	@Binding
	private ConfigProvider configProvider;
	
	/**
	 * Setter method (for DI setter injection).
	 * @param configProvider the configProvider to set
	 */
	public void setConfigProvider(ConfigProvider configProvider) {
		this.configProvider = configProvider;
	}
	
	/**
	 * Setter method (for DI setter injection).
	 * @param equipmentConfiguratorNullImpl the equipmentConfiguratorNullImpl to set
	 */
	public void setEquipmentConfiguratorNullImpl(EquipmentConfigurator equipmentConfiguratorNullImpl) {
		this.equipmentConfiguratorNullImpl = equipmentConfiguratorNullImpl;
	}
	
	/**
	 * Setter method (for DI setter injection).
	 * @param equipmentConfiguratorDefaultImpl the equipmentConfiguratorDefaultImpl to set
	 */
	public void setEquipmentConfiguratorDefaultImpl(EquipmentConfigurator equipmentConfiguratorDefaultImpl) {
		this.equipmentConfiguratorDefaultImpl = equipmentConfiguratorDefaultImpl;
	}
	
	/**
	 * Setter method (for DI setter injection).
	 * @param equipmentConfiguratorOtherImpl the equipmentConfiguratorOtherImpl to set
	 */
	public void setEquipmentConfiguratorOtherImpl(EquipmentConfigurator equipmentConfiguratorOtherImpl) {
		this.equipmentConfiguratorOtherImpl = equipmentConfiguratorOtherImpl;
	}
	
	/**
	 * Setter method (for DI setter injection).
	 * @param equipmentConfiguratorOptDeviceImpl The instance. 
	 */
	public void setEquipmentConfiguratorOptDeviceImpl(EquipmentConfigurator equipmentConfiguratorOptDeviceImpl) {
		this.equipmentConfiguratorOptDeviceImpl = equipmentConfiguratorOptDeviceImpl;
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.EquipmentConfiguratorFactory#getConfigurator(java.lang.String)
	 */
	@Override
	public EquipmentConfigurator getConfigurator(String srcComponentName) {
		EquipmentConfigurator obj = equipmentConfiguratorNullImpl;
		String featureVer = getFeatureVersion();
		if ("debugClient".equals(srcComponentName)) {
			// Debug operation
			obj = equipmentConfiguratorNullImpl;
		} else if ("developer".equals(srcComponentName)) {
			// Default
			if (featureVer == null) {
				// default
				obj = equipmentConfiguratorDefaultImpl;
			} else if (FEATURE_VERSION_00_01.equals(featureVer)) {
				obj = equipmentConfiguratorNullImpl;
			} else if (FEATURE_VERSION_00_02.equals(featureVer)) {
				obj = equipmentConfiguratorOptDeviceImpl;
			} else {
				LOG.warn("Unknown feature version is specified. : " + featureVer);
				obj = equipmentConfiguratorNullImpl;
			}
		} else {
			// For other.
			if (featureVer == null) {
				// default
				obj = equipmentConfiguratorOtherImpl;
			} else if (FEATURE_VERSION_00_01.equals(featureVer)) {
				obj = equipmentConfiguratorNullImpl;
			} else if (FEATURE_VERSION_00_02.equals(featureVer)) {
				obj = equipmentConfiguratorOptDeviceImpl;
			} else {
				LOG.warn("Unknown feature version is specified. : " + featureVer);
				obj = equipmentConfiguratorNullImpl;
			}
		}
		return obj;
	}
	
	/**
	 */
	private String getFeatureVersion() {
		String featureVer = configProvider.getProperty(PROP_KEY_DEBUG_FEATURE_VERSION);
		return (FEATURE_VERSION_LATEST.equals(featureVer) ? null : featureVer);
	}
}
