/**
 * OdenOSConfigImplTest.java
 * (C) 2013, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.impl.rpc.service;

import static org.junit.Assert.*;

import java.io.File;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.impl.logic.ConfigProviderImpl;
import org.o3project.mlo.server.impl.rpc.service.OdenOSConfigImpl;
import org.o3project.mlo.server.logic.ConfigProvider;

/**
 * OdenOSConfigImplTest
 *
 */
public class OdenOSConfigImplTest {

	private static final String DATA_PATH = "src/test/resources/org/o3project/mlo/server/rpc/service/data";
	private static final String DATA_FILE_001 = "odenos.config.001.properties";
	private static final String DATA_FILE_002 = "odenos.config.002.properties";
	
	private ConfigProvider configProvider;
	private OdenOSConfigImpl obj;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		setupDefaultObj(DATA_FILE_001);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		configProvider = null;
		obj = null;
	}
	
	private void setupDefaultObj(String fileName) {
		File propFile = new File(DATA_PATH, fileName);
		configProvider = new ConfigProviderImpl("default.mlo-srv.properties", propFile.getAbsolutePath());
		obj = new OdenOSConfigImpl();
		obj.setConfigProvider(configProvider);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSConfigImpl#setConfigProvider(org.o3project.mlo.server.logic.ConfigProvider)}.
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
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSConfigImpl#getRemoteSystemManagerId()}.
	 */
	@Test
	public void testGetRemoteSystemManagerId() {
		String actual = obj.getRemoteSystemManagerId();
		String expected = "myRemoteSystemManager";
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSConfigImpl#getRemoteSystemManagerHost()}.
	 */
	@Test
	public void testGetRemoteSystemManagerHost() {
		String actual = obj.getRemoteSystemManagerHost();
		String expected = "123.456.789.1";
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSConfigImpl#getRemoteSystemManagerPort()}.
	 */
	@Test
	public void testGetRemoteSystemManagerPort() {
		Integer actual = obj.getRemoteSystemManagerPort();
		Integer expected = 9991;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSConfigImpl#getComponentManagerId()}.
	 */
	@Test
	public void testGetComponentManagerId() {
		String actual = obj.getComponentManagerId();
		String expected = "myComponentManager";
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSConfigImpl#getComponentManagerHost()}.
	 */
	@Test
	public void testGetComponentManagerHost() {
		String actual = obj.getComponentManagerHost();
		String expected = "123.456.789.2";
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSConfigImpl#getComponentManagerPort()}.
	 */
	@Test
	public void testGetComponentManagerPort() {
		Integer actual = obj.getComponentManagerPort();
		Integer expected = 9992;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSConfigImpl#getComponentManagerBaseUri()}.
	 */
	@Test
	public void testGetComponentManagerBaseUri_notSpecified() {
		String actual = obj.getComponentManagerBaseUri();
		String expected = "odenos://123.456.789.2:9992/myComponentManager";
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSConfigImpl#getComponentManagerBaseUri()}.
	 */
	@Test
	public void testGetComponentManagerBaseUri_specified() {
		setupDefaultObj(DATA_FILE_002);
		String actual = obj.getComponentManagerBaseUri();
		String expected = "odenos:/myHost:9994/thatComponentManager";
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSConfigImpl#getLauncherHost()}.
	 */
	@Test
	public void testGetLauncherHost() {
		String actual = obj.getLauncherHost();
		String expected = "123.456.789.3";
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSConfigImpl#getLauncherPort()}.
	 */
	@Test
	public void testGetLauncherPort() {
		Integer actual = obj.getLauncherPort();
		Integer expected = 9993;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSConfigImpl#getNetworkComponentIdL2()}.
	 */
	@Test
	public void testGetNetworkComponentIdL2() {
		String actual = obj.getNetworkComponentIdL2();
		String expected = "myNetworkComponentL2";
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSConfigImpl#getNetworkComponentIdL012()}.
	 */
	@Test
	public void testGetNetworkComponentIdL012() {
		String actual = obj.getNetworkComponentIdL012();
		String expected = "myNetworkComponentL012";
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSConfigImpl#getNetworkComponentPath()}.
	 */
	@Test
	public void testGetNetworkComponentPath() {
		String actual = obj.getNetworkComponentPath();
		String expected = "components/myNetworkComponentL2";
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSConfigImpl#getPTDriverId()}.
	 */
	@Test
	public void testGetPTDriverId() {
		String actual = obj.getPTDriverId();
		String expected = "myPacketTransportDriver";
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSConfigImpl#getPTDriverPath()}.
	 */
	@Test
	public void testGetPTDriverPath() {
		String actual = obj.getPTDriverPath();
		String expected = "components/myPacketTransportDriver";
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSConfigImpl#getConnectionId()}.
	 */
	@Test
	public void testGetConnectionId() {
		String actual = obj.getConnectionId();
		String expected = "myConnection";
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSConfigImpl#getConnectionType()}.
	 */
	@Test
	public void testGetConnectionType() {
		String actual = obj.getConnectionType();
		String expected = "myType";
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSConfigImpl#getConnectionState()}.
	 */
	@Test
	public void testGetConnectionState() {
		String actual = obj.getConnectionState();
		String expected = "myState";
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSConfigImpl#getConnectionPath()}.
	 */
	@Test
	public void testGetConnectionPath() {
		String actual = obj.getConnectionPath();
		String expected = "connections/myConnection";
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link OdenOSConfigImpl#isAvailableCreateL2()}.
	 */
	@Test
	public void testIsAvailableCreateL2() {
		Boolean actual = obj.isAvailableCreateL2();
		Boolean expected = Boolean.FALSE;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link OdenOSConfigImpl#isAvailableReqLinkEstablishedCompletion()}.
	 */
	@Test
	public void testIsAvailableReqLinkEstablishedCompletion() {
		Boolean actual = obj.isAvailableReqLinkEstablishedCompletion();
		Boolean expected = Boolean.TRUE;
		assertEquals(expected, actual);
	}

}
