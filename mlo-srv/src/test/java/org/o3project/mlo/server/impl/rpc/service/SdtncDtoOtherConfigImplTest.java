/**
 * SdtncDtoOtherConfigImplTest.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.rpc.service;

import static org.junit.Assert.*;

import java.io.File;
import java.io.StringWriter;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.WriterAppender;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.impl.logic.ConfigProviderImpl;
import org.o3project.mlo.server.impl.rpc.service.SdtncDtoOtherConfigImpl;
import org.o3project.mlo.server.logic.ApiCallException;
import org.o3project.mlo.server.logic.ConfigConstants;
import org.o3project.mlo.server.logic.ConfigProvider;
import org.o3project.mlo.server.logic.MloException;

/**
 * SdtncDtoOtherConfigImplTest
 *
 */
public class SdtncDtoOtherConfigImplTest {

	private static final String DATA_PATH = "src/test/resources/org/o3project/mlo/server/rpc/service/data";
	private static final String DATA_FILE_001 = "sdtnc.dto.config.001.properties";
	private static final String DATA_FILE_003 = "sdtnc.dto.config.003.properties";
	private static final String LOGIN_PASS = "Admin";
	private static final String SLICE_ID = "4";
	private static final String LOGIN_IP_ADDRESS = "172.21.104.15";
	private static final String HOP_LINK_ID = "521";
	private static final String CUT_LINK_ID= "221";
	private static final String NE_ID_A = "1.1.1.0.3.0.1";
	private static final String NE_ID_Z = "1.1.2.0.3.0.1";
	private static final String HOP_PORT_ID_A = "1.1.1.3.5.1.3.1.0.1.1.2.0.3.0.2.1";
	private static final String HOP_PORT_ID_Z = "1.1.2.3.5.1.3.1.0.1.1.2.0.3.0.2.1";
	private static final String CUT_PORT_ID_A = "1.1.1.3.5.1.3.1.0.1.1.2.0.3.0.2.2";
	private static final String CUT_PORT_ID_Z = "1.1.2.3.5.1.3.1.0.1.1.2.0.3.0.2.2";
	private static final String PIR_RATE = "1.2";
	private static final String SLA_MODE = "1";
	
	private ConfigProvider configProvider;
	private SdtncDtoOtherConfigImpl obj;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		setUpObj(DATA_PATH, DATA_FILE_001);
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
	 * オブジェクトをセットアップする。
	 * @param dataDir データディレクトリ
	 * @param propFileName プロパティファイル名
	 */
	private void setUpObj(String dataDir, String propFileName) {
		File propFile = new File(dataDir, propFileName);
		configProvider = new ConfigProviderImpl("default.mlo-srv.properties", propFile.getAbsolutePath());
		obj = new SdtncDtoOtherConfigImpl();
		obj.setConfigProvider(configProvider);
		obj.init();
	}

	/**
	 * Test method for {@link SdtncDtoOtherConfigImpl#setConfigProvider(ConfigProvider)}.
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
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncDtoOtherConfigImpl#getLoginPassword()}.
	 */
	@Test
	public void testGetLoginPassword() {
		String actual = obj.getLoginPassword();
		String expected = LOGIN_PASS;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncDtoOtherConfigImpl#getSliceId()}.
	 */
	@Test
	public void testGetSliceId() {
		String actual = obj.getSliceId();
		String expected = SLICE_ID;
		assertEquals(expected, actual);
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncDtoOtherConfigImpl#getLoginIpAddress()}.
	 */
	@Test
	public void testGetLoginIpAddress() {
		String actual = obj.getLoginIpAddress();
		String expected = LOGIN_IP_ADDRESS;
		assertEquals(expected, actual);
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncDtoOtherConfigImpl#getHopPathId()}.
	 */
	@Test
	public void testGetHopPathId() {
		try{
			obj.getHopPathId();
			fail();
		}catch(Exception e){
			assertTrue(e instanceof UnsupportedOperationException);
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncDtoOtherConfigImpl#getCutPathId()}.
	 */
	@Test
	public void testGetCutPathId() {
		try{
			obj.getCutPathId();
			fail();
		}catch(Exception e){
			assertTrue(e instanceof UnsupportedOperationException);
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncDtoOtherConfigImpl#getHopLinkId()}.
	 */
	@Test
	public void testGetHopLinkId() {
		String actual = obj.getHopLinkId();
		String expected = HOP_LINK_ID;
		assertEquals(expected, actual);
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncDtoOtherConfigImpl#getCutLinkId()}.
	 */
	@Test
	public void testGetCutLinkId() {
		String actual = obj.getCutLinkId();
		String expected = CUT_LINK_ID;
		assertEquals(expected, actual);
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncDtoOtherConfigImpl#getNeIdA()}.
	 */
	@Test
	public void testGetNeIdA() {
		String actual = obj.getNeIdA();
		String expected = NE_ID_A;
		assertEquals(expected, actual);
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncDtoOtherConfigImpl#getNeIdZ()}.
	 */
	@Test
	public void testGetNeIdZ() {
		String actual = obj.getNeIdZ();
		String expected = NE_ID_Z;
		assertEquals(expected, actual);
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncDtoOtherConfigImpl#getHopPortIdA()}.
	 */
	@Test
	public void testGetHopPortIdA() {
		String actual = obj.getHopPortIdA();
		String expected = HOP_PORT_ID_A;
		assertEquals(expected, actual);
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncDtoOtherConfigImpl#getHopPortIdZ()}.
	 */
	@Test
	public void testGetHopPortIdZ() {
		String actual = obj.getHopPortIdZ();
		String expected = HOP_PORT_ID_Z;
		assertEquals(expected, actual);
	}
	
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncDtoOtherConfigImpl#getCutPortIdA()}.
	 */
	@Test
	public void testGetCutPortIdA() {
		String actual = obj.getCutPortIdA();
		String expected = CUT_PORT_ID_A;
		assertEquals(expected, actual);
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncDtoOtherConfigImpl#getCutPortIdZ()}.
	 */
	@Test
	public void testGetCutPortIdZ() {
		String actual = obj.getCutPortIdZ();
		String expected = CUT_PORT_ID_Z;
		assertEquals(expected, actual);
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncDtoOtherConfigImpl#getPirRate()}.
	 */
	@Test
	public void testGetPirRate() {
		String actual = obj.getPirRate();
		String expected = PIR_RATE;
		assertEquals(expected, actual);
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncDtoOtherConfigImpl#getSlaMode()}.
	 */
	@Test
	public void testGetSlaMode() {
		String actual = obj.getSlaMode();
		String expected = SLA_MODE;
		assertEquals(expected, actual);
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncDtoOtherConfigImpl#isPutVPathWorkaroundMode()}.
	 */
	@Test
	public void testIsPutVPathWorkaroundMode() {
		setUpObj(DATA_PATH, DATA_FILE_003);
		Boolean actual = obj.isPutVPathWorkaroundMode();
		Boolean expected = true;
		assertEquals(expected, actual);
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncDtoOtherConfigImpl#getVlanProperty(String, String)}.
	 */
	@Test
	public void testGetVlanProperty_421A() {
		try {
			setUpObj(DATA_PATH, DATA_FILE_003);
			String vlanId = "421";
			String subKey = ConfigConstants.PROP_SUBKEY_PATH_END_POINT_A_PORT_ID;
			String actual;
			actual = obj.getVlanProperty(vlanId, subKey);
			String expected = "1.1.1.3.5.1.3.1.0.1.1.1.0.3.0.2.5";
			assertEquals(expected, actual);
		} catch (MloException e) {
			fail();
		}
		
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncDtoOtherConfigImpl#getVlanProperty(String, String)}.
	 */
	@Test
	public void testGetVlanProperty_421Z() {
		try{
			setUpObj(DATA_PATH, DATA_FILE_003);
			String vlanId = "421";
			String subKey = ConfigConstants.PROP_SUBKEY_PATH_END_POINT_Z_PORT_ID;
			String actual;
			actual = obj.getVlanProperty(vlanId, subKey);
			String expected = "1.1.3.3.5.1.3.1.0.1.1.1.0.3.0.2.5";
			assertEquals(expected, actual);
		} catch (MloException e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncDtoOtherConfigImpl#getVlanProperty(String, String)}.
	 */
	@Test
	public void testGetVlanProperty_422A() {
		try{
			setUpObj(DATA_PATH, DATA_FILE_003);
			String vlanId = "422";
			String subKey = ConfigConstants.PROP_SUBKEY_PATH_END_POINT_A_PORT_ID;
			String actual = obj.getVlanProperty(vlanId, subKey);
			String expected = "1.1.1.3.5.1.3.1.0.1.1.1.0.3.0.2.6";
		assertEquals(expected, actual);
		} catch (MloException e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncDtoOtherConfigImpl#getVlanProperty(String, String)}.
	 */
	@Test
	public void testGetVlanProperty_422Z() {
		try{
			setUpObj(DATA_PATH, DATA_FILE_003);
			String vlanId = "422";
			String subKey = ConfigConstants.PROP_SUBKEY_PATH_END_POINT_Z_PORT_ID;
			String actual = obj.getVlanProperty(vlanId, subKey);
			String expected = "1.1.3.3.5.1.3.1.0.1.1.1.0.3.0.2.6";
			assertEquals(expected, actual);
		} catch (MloException e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncDtoOtherConfigImpl#getExtraFilePath(String, String)}.
	 */
	@Test
	public void testGetExtraFilePath() {
		setUpObj(DATA_PATH, DATA_FILE_001);
		String actual = obj.getVlanExtraMappingFilePath();
		String expected = "src/test/resources/org/o3project/mlo/server/rpc/service/data/test.stdnc.extra.data.csv";
		assertEquals(expected, actual);
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncDtoOtherConfigImpl#getVlanProperty(String, String)}.
	 */
	@Test
	public void testGetVlanProperty_500A() {
		StringWriter writer = new StringWriter();
		WriterAppender appender = new WriterAppender(new PatternLayout("%p, %m%n"),writer);
		getLogger().addAppender(appender);
		getLogger().setAdditivity(false);
		setUpObj(DATA_PATH, DATA_FILE_003);
		String vlanId = "500";
		String subKey = ConfigConstants.PROP_SUBKEY_PATH_END_POINT_A_PORT_ID;
		String actual = null;
		try {
			actual = obj.getVlanProperty(vlanId, subKey);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof ApiCallException);
			
		}
		String logString = writer.toString();
		assertTrue(logString.contains("Extra portId file read failed [file=src/test/resources/org/o3project/mlo/server/rpc/service/data/test.csv]"));
		assertEquals(null, actual);
		getLogger().removeAppender(appender);
		getLogger().setAdditivity(true);
	}

	/**
	 * @return
	 */
	private static Logger getLogger() {
		return LogManager.getLogger(SdtncDtoOtherConfigImpl.class);
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncDtoOtherConfigImpl#getVlanProperty(String, String)}.
	 */
	@Test
	public void testGetVlanProperty_430A() {
		try{
			setUpObj(DATA_PATH, DATA_FILE_001);
			String vlanId = "430";
			String subKey = ConfigConstants.PROP_SUBKEY_PATH_END_POINT_A_PORT_ID;
			String actual = obj.getVlanProperty(vlanId, subKey);
			String expected = "1.1.1.3.5.1.3.1.0.1.1.1.0.3.0.2.30";
			assertEquals(expected, actual);
		} catch (MloException e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncDtoOtherConfigImpl#getVlanProperty(String, String)}.
	 */
	@Test
	public void testGetVlanProperty_430Z() {
		try{
			setUpObj(DATA_PATH, DATA_FILE_001);
			String vlanId = "430";
			String subKey = ConfigConstants.PROP_SUBKEY_PATH_END_POINT_Z_PORT_ID;
			String actual = obj.getVlanProperty(vlanId, subKey);
			String expected = "1.1.3.3.5.1.3.1.0.1.1.1.0.3.0.2.30";
			assertEquals(expected, actual);
		} catch (MloException e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncDtoOtherConfigImpl#getVlanProperty(String, String)}.
	 */
	@Test
	public void testGetVlanProperty_529A() {
		try{
			setUpObj(DATA_PATH, DATA_FILE_001);
			String vlanId = "529";
			String subKey = ConfigConstants.PROP_SUBKEY_PATH_END_POINT_A_PORT_ID;
			String actual = obj.getVlanProperty(vlanId, subKey);
			String expected = "1.1.1.3.5.1.3.1.0.1.1.1.0.3.0.2.129";
			assertEquals(expected, actual);
		} catch (MloException e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncDtoOtherConfigImpl#getVlanProperty(String, String)}.
	 */
	@Test
	public void testGetVlanProperty_529Z() {
		try{
			setUpObj(DATA_PATH, DATA_FILE_001);
			String vlanId = "529";
			String subKey = ConfigConstants.PROP_SUBKEY_PATH_END_POINT_Z_PORT_ID;
			String actual = obj.getVlanProperty(vlanId, subKey);
			String expected = "1.1.3.3.5.1.3.1.0.1.1.1.0.3.0.2.129";
			assertEquals(expected, actual);
		} catch (MloException e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncDtoOtherConfigImpl#getVlanProperty(String, String)}.
	 */
	@Test
	public void testGetVlanProperty_530A() {
		setUpObj(DATA_PATH, DATA_FILE_001);
		String vlanId = "530";
		String subKey = ConfigConstants.PROP_SUBKEY_PATH_END_POINT_A_PORT_ID;
		String actual = null;
		try {
			actual = obj.getVlanProperty(vlanId, subKey);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof ApiCallException);
		}
		assertEquals(null, actual);
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncDtoOtherConfigImpl#getVlanProperty(String, String)}.
	 */
	@Test
	public void testGetVlanProperty_530Z() {
		setUpObj(DATA_PATH, DATA_FILE_001);
		String vlanId = "530";
		String subKey = ConfigConstants.PROP_SUBKEY_PATH_END_POINT_Z_PORT_ID;
		String actual = null;
		try {
			actual = obj.getVlanProperty(vlanId, subKey);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof ApiCallException);
		}
		assertEquals(null, actual);
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncDtoOtherConfigImpl#canCreateExtraFlows()}.
	 */
	@Test
	public void testCanCreateExtraFlows() {
		setUpObj(DATA_PATH, DATA_FILE_001);
		try {
			obj.canCreateExtraFlows();
			fail();
		} catch (Exception e){
			assertTrue(e instanceof UnsupportedOperationException);
		}
	}
}
