/**
 * EquipmentConfigurationFactoryImpl.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.action.test.usecase;

import org.o3project.mlo.server.impl.logic.EquipmentConfiguratorNullImpl;
import org.o3project.mlo.server.logic.EquipmentConfigurator;
import org.o3project.mlo.server.logic.EquipmentConfiguratorFactory;
import org.seasar.framework.container.annotation.tiger.Binding;


/**
 * EquipmentConfigurationFactoryImpl
 *
 */
public class EquipmentConfiguratorFactoryImpl implements
		EquipmentConfiguratorFactory {
	
	@Binding
	private EquipmentConfiguratorNullImpl equipmentConfiguratorNullImpl;

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.EquipmentConfiguratorFactory#getConfigurator(java.lang.String)
	 */
	@Override
	public EquipmentConfigurator getConfigurator(String srcComponentName) {
		return equipmentConfiguratorNullImpl;
	}

}
