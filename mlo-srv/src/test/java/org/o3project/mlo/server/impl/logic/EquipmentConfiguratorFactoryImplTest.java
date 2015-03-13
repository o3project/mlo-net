/**
 * EquipmentConfiguratorFactoryImplTest.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import static org.junit.Assert.*;

import java.io.File;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.impl.logic.ConfigProviderImpl;
import org.o3project.mlo.server.impl.logic.EquipmentConfiguratorFactoryImpl;
import org.o3project.mlo.server.impl.logic.EquipmentConfiguratorNullImpl;
import org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl;
import org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOptDeviceImpl;
import org.o3project.mlo.server.logic.EquipmentConfigurator;

/**
 * EquipmentConfiguratorFactoryImplTest
 *
 */
public class EquipmentConfiguratorFactoryImplTest {
	
	private static final String DATA_PATH = "src/test/resources/org/o3project/mlo/server/logic/data";
	
	private EquipmentConfiguratorNullImpl eqConfNull;
	
	private EquipmentConfiguratorOptDeviceImpl fwConf;
	
	private EquipmentConfiguratorFactoryImpl obj;
	
	private EquipmentConfiguratorOtherImpl eqConfOther;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		obj = null;
		eqConfNull = null;
		eqConfOther = null;
		fwConf = null;
	}
	
	private EquipmentConfiguratorFactoryImpl createObj(String propFileName) {
		ConfigProviderImpl configProvider = null;
		if (propFileName != null) {
			File propFile = new File(DATA_PATH, propFileName);
			configProvider = ConfigProviderImplTest.createConfigProviderImpl(propFile.getAbsolutePath());
		} else {
			configProvider = ConfigProviderImplTest.createConfigProviderImpl(null);
		}
		
		obj = new EquipmentConfiguratorFactoryImpl();
		obj.setConfigProvider(configProvider);
		
		eqConfNull = new EquipmentConfiguratorNullImpl();
		obj.setEquipmentConfiguratorNullImpl(eqConfNull);
		
		fwConf = new EquipmentConfiguratorOptDeviceImpl();
		obj.setEquipmentConfiguratorOptDeviceImpl(fwConf);
		
		eqConfOther = new EquipmentConfiguratorOtherImpl();
		obj.setEquipmentConfiguratorOtherImpl(eqConfOther);
		
		return obj;
	}

	/**
	 * Test method for {@link EquipmentConfiguratorFactoryImpl#getConfigurator(String)}.
	 */
	@Test
	public void testGetConfigurator_default() {
		EquipmentConfigurator eqConf = null;
		
		obj = createObj(null);
		
		eqConf = obj.getConfigurator("debugClient");
		assertEquals(eqConfNull, eqConf);
		
		eqConf = obj.getConfigurator("demoApl");
		assertEquals(eqConfOther, eqConf);
	}

	/**
	 * Test method for {@link EquipmentConfiguratorFactoryImpl#getConfigurator(String)}.
	 */
	@Test
	public void testGetConfigurator_00_01() {
		EquipmentConfigurator eqConf = null;
		
		obj = createObj("mlo-srv.featureVer.00-01.properties");
		
		eqConf = obj.getConfigurator("debugClient");
		assertEquals(eqConfNull, eqConf);
		
		eqConf = obj.getConfigurator("demoApl");
		assertEquals(eqConfNull, eqConf);
		
		eqConf = obj.getConfigurator("hitachiApl");
		assertEquals(eqConfNull, eqConf);
	}

	/**
	 * Test method for {@link EquipmentConfiguratorFactoryImpl#getConfigurator(String)}.
	 */
	@Test
	public void testGetConfigurator_00_02() {
		EquipmentConfigurator eqConf = null;
		
		obj = createObj("mlo-srv.featureVer.00-02.properties");
		
		eqConf = obj.getConfigurator("debugClient");
		assertEquals(eqConfNull, eqConf);
		
		eqConf = obj.getConfigurator("demoApl");
		assertEquals(fwConf, eqConf);
	}

	/**
	 * Test method for {@link EquipmentConfiguratorFactoryImpl#getConfigurator(String)}.
	 */
	@Test
	public void testGetConfigurator_00_03() {
		EquipmentConfigurator eqConf = null;
		
		obj = createObj("mlo-srv.featureVer.00-03.properties");
		
		eqConf = obj.getConfigurator("debugClient");
		assertEquals(eqConfNull, eqConf);
		
		eqConf = obj.getConfigurator("demoApl");
		assertEquals(eqConfOther, eqConf);
	}

	/**
	 * Test method for {@link EquipmentConfiguratorFactoryImpl#getConfigurator(String)}.
	 */
	@Test
	public void testGetConfigurator_unknown() {
		EquipmentConfigurator eqConf = null;
		
		obj = createObj("mlo-srv.featureVer.unknown.properties");
		
		eqConf = obj.getConfigurator("debugClient");
		assertEquals(eqConfNull, eqConf);
		
		eqConf = obj.getConfigurator("demoApl");
		assertEquals(eqConfNull, eqConf);
	}
}
