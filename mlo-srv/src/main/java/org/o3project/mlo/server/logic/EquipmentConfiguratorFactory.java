/**
 * EquipmentConfiguratorFactory.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.logic;

/**
 * This interface designates the factory interface of {@link EquipmentConfigurator} interfaces.
 */
public interface EquipmentConfiguratorFactory {
	
	/**
	 * Obtains the {@link EquipmentConfigurator} instance.
	 * @param srcComponentName Key.
	 * @return the instance.
	 */
	EquipmentConfigurator getConfigurator(String srcComponentName);
}
