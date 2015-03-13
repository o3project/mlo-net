/**
 * SdtncConfigImplTest.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.rpc.service;

import static org.junit.Assert.*;

import java.io.File;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.impl.logic.ConfigProviderImpl;
import org.o3project.mlo.server.impl.rpc.service.SdtncConfigImpl;
import org.o3project.mlo.server.logic.ConfigProvider;

/**
 * SdtncConfigImplTest
 *
 */
public class SdtncConfigImplTest {

	private static final String DATA_PATH = "src/test/resources/org/o3project/mlo/server/rpc/service/data";
	private static final String DATA_FILE_001 = "sdtnc.config.001.properties";
	
	private ConfigProvider configProvider;
	private SdtncConfigImpl obj;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		File propFile = new File(DATA_PATH, DATA_FILE_001);
		configProvider = new ConfigProviderImpl("default.mlo-srv.properties", propFile.getAbsolutePath());
		obj = new SdtncConfigImpl();
		obj.setConfigProvider(configProvider);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		obj = null;
		configProvider = null;
	}

	/**
	 * Test method for {@link SdtncConfigImpl#setConfigProvider(ConfigProvider)}.
	 */
	@Test
	public void testSetConfigProvider() {
		try {
			obj.setConfigProvider(configProvider);
			assertTrue(true);
		} catch (Throwable th) {
			fail();
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncConfigImpl#getNbiBaseUri()}.
	 */
	@Test
	public void testGetNbiBaseUri() {
		String actual = obj.getNbiBaseUri();
		String expected = "http://myHost/myPath";
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncConfigImpl#getConnectionTimeoutSec()}.
	 */
	@Test
	public void testGetConnectionTimeoutSec() {
		Integer actual = obj.getConnectionTimeoutSec();
		Integer expected = Integer.valueOf(123);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncConfigImpl#getReadTimeoutSec()}.
	 */
	@Test
	public void testGetReadTimeoutSec() {
		Integer actual = obj.getReadTimeoutSec();
		Integer expected = Integer.valueOf(456);
		assertEquals(expected, actual);
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncConfigImpl#getDummyInvokerSetFlag()}.
	 */
	@Test
	public void testGetDummyInvokerSetFlag() {
		boolean actual = obj.getDummyInvokerSetFlag();
		boolean expected = false;
		assertEquals(expected, actual);
	}

}
