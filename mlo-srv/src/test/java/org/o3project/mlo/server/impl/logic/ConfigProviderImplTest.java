/**
 * ConfigProviderImplTest.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.Map;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.WriterAppender;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.impl.logic.ConfigProviderImpl;
import org.o3project.mlo.server.logic.ConfigConstants;

/**
 * ConfigProviderImplTest
 *
 */
public class ConfigProviderImplTest implements ConfigConstants {
	
	private static final String DATA_PATH = "src/test/resources/org/o3project/mlo/server/logic/data";
	@SuppressWarnings("unused")
	private ConfigProviderImpl obj;
	@SuppressWarnings("unused")
	private ConfigProviderImpl obj2;

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
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.ConfigProviderImpl#ConfigProviderImpl(java.lang.String)}.
	 */
	@Test
	public void testConfigProviderImpl() {
		String propertiesFilePath = null;
		ConfigProviderImpl obj = createConfigProviderImpl(propertiesFilePath);
		assertNotNull(obj);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.ConfigProviderImpl#getProperties()}.
	 */
	@Test
	public void testGetProperties_normal_path_null() {
		String propertiesFilePath = null;
		ConfigProviderImpl obj = createConfigProviderImpl(propertiesFilePath);
		Map<String, String> props = obj.getProperties();
		assertNotNull(props);
		assertNotNull(props.get(PROP_KEY_DEBUG_CLIENTS));
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.ConfigProviderImpl#getProperties()}.
	 */
	@Test
	public void testGetProperties_normal() {
		String propertiesFilePath = DATA_PATH + "/mlo-srv.001.properties";
		ConfigProviderImpl obj = createConfigProviderImpl(propertiesFilePath);
		Map<String, String> props = obj.getProperties();
		assertNotNull(props);
		assertEquals("test001", props.get(PROP_KEY_DEBUG_CLIENTS));
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.ConfigProviderImpl#getProperties()}.
	 */
	@Test
	public void testGetProperties_anomaly_no_file() {
		String propertiesFilePath = DATA_PATH + "/no_file";
		ConfigProviderImpl obj = createConfigProviderImpl(propertiesFilePath);
		Map<String, String> props = obj.getProperties();
		assertNotNull(props);
		assertNotNull(props.get(PROP_KEY_DEBUG_CLIENTS));
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.ConfigProviderImpl#getProperties()}.
	 */
	@Test
	public void testGetProperties_anomaly_directory() {
		String propertiesFilePath = DATA_PATH;
		ConfigProviderImpl obj = createConfigProviderImpl(propertiesFilePath);
		Map<String, String> props = obj.getProperties();
		assertNotNull(props);
		assertNotNull(props.get(PROP_KEY_DEBUG_CLIENTS));
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.ConfigProviderImpl#getProperty(java.lang.String)}.
	 */
	@Test
	public void testGetProperty_normal() {
		String propertiesFilePath = DATA_PATH + "/mlo-srv.001.properties";
		ConfigProviderImpl obj = createConfigProviderImpl(propertiesFilePath);
		String prop = obj.getProperty(PROP_KEY_DEBUG_CLIENTS);
		assertEquals("test001", prop);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.ConfigProviderImpl#getCommaSplitedProperty(java.lang.String)}.
	 */
	@Test
	public void testGetCommaSplitedProperty_normal() {
		String propertiesFilePath = DATA_PATH + "/mlo-srv.002.properties";
		ConfigProviderImpl obj = createConfigProviderImpl(propertiesFilePath);
		String[] actuals = obj.getCommaSplitedProperty(PROP_KEY_DEBUG_CLIENTS);
		String[] expecteds = new String[]{"client001", "client002", "client003"};
		assertEquals(expecteds.length, actuals.length);
		assertArrayEquals(expecteds, actuals);
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.ConfigProviderImpl#getCommaSplitedProperty(java.lang.String)}.
	 */
	@Test
	public void testGetCommaSplitedProperty_error() {
		String propertiesFilePath = DATA_PATH + "/mlo-srv.002.properties";
		ConfigProviderImpl obj = createConfigProviderImpl(propertiesFilePath);
		String[] actuals = obj.getCommaSplitedProperty("test");
		assertNull(actuals);
	}
	
	/**
	 * Test method for {@link ConfigProviderImpl#getBooleanProperty(String)}.
	 */
	@Test
	public void testBooleanProperty_normal_true() {
		String propertiesFilePath = DATA_PATH + "/mlo-srv.001.properties";
		String propKey = "mlo.server.config.test.booleanTrueProp";
		ConfigProviderImpl obj = createConfigProviderImpl(propertiesFilePath);
		Boolean prop = obj.getBooleanProperty(propKey);
		assertEquals(Boolean.TRUE, prop);
	}
	
	/**
	 * Test method for {@link ConfigProviderImpl#getBooleanProperty(String)}.
	 */
	@Test
	public void testBooleanProperty_normal_false() {
		String propertiesFilePath = DATA_PATH + "/mlo-srv.001.properties";
		String propKey = "mlo.server.config.test.booleanFalseProp";
		ConfigProviderImpl obj = createConfigProviderImpl(propertiesFilePath);
		Boolean prop = obj.getBooleanProperty(propKey);
		assertEquals(Boolean.FALSE, prop);
	}
	
	/**
	 * Test method for {@link ConfigProviderImpl#getBooleanProperty(String)}.
	 */
	@Test
	public void testBooleanProperty_anomaly_notTrueFalse() {
		String propertiesFilePath = DATA_PATH + "/mlo-srv.001.properties";
		String propKey = "mlo.server.config.test.booleanNotTrueFalseProp";
		ConfigProviderImpl obj = createConfigProviderImpl(propertiesFilePath);
		Boolean prop = obj.getBooleanProperty(propKey);
		assertEquals(Boolean.FALSE, prop);
	}
	
	/**
	 * Test method for {@link ConfigProviderImpl#getIntegerProperty(String)}.
	 */
	@Test
	public void testIntegerProperty_normal_integer100() {
		String propertiesFilePath = DATA_PATH + "/mlo-srv.001.properties";
		String propKey = "mlo.server.config.test.integer100Prop";
		ConfigProviderImpl obj = createConfigProviderImpl(propertiesFilePath);
		Integer prop = obj.getIntegerProperty(propKey);
		assertEquals(Integer.valueOf(100), prop);
	}
	
	/**
	 * Test method for {@link ConfigProviderImpl#getIntegerProperty(String)}.
	 */
	@Test
	public void testIntegerProperty_anomaly_notInteger() {
		String propertiesFilePath = DATA_PATH + "/mlo-srv.001.properties";
		String propKey = "mlo.server.config.test.integerNotIntegerProp";
		ConfigProviderImpl obj = createConfigProviderImpl(propertiesFilePath);
		Integer prop = obj.getIntegerProperty(propKey);
		assertEquals(null, prop);
	}
	
	/**
	 * Test method for {@link ConfigProviderImpl#getSubProperties(String)}.
	 */
	@Test
	public void testGetSubProperties_pattern1() {
		String propertiesFilePath = DATA_PATH + "/mlo-srv.003.properties";
		ConfigProviderImpl obj = createConfigProviderImpl(propertiesFilePath);
		
		String keyPrefix = "mlo.server.config.pattern.1.";
		Map<String, String> propMap = obj.getSubProperties(keyPrefix);
		assertEquals(2, propMap.size());
		assertEquals("pattern1", propMap.get("name"));
		assertEquals("1", propMap.get("id"));
	}
	
	/**
	 * Test method for {@link ConfigProviderImpl#getSubProperties(String)}.
	 */
	@Test
	public void testGetSubProperties_pattern2() {
		String propertiesFilePath = DATA_PATH + "/mlo-srv.003.properties";
		ConfigProviderImpl obj = createConfigProviderImpl(propertiesFilePath);
		
		String keyPrefix = "mlo.server.config.pattern.2.";
		Map<String, String> propMap = obj.getSubProperties(keyPrefix);
		assertEquals(3, propMap.size());
		assertEquals("pattern2", propMap.get("name"));
		assertEquals("2", propMap.get("id"));
		assertEquals("ThisIsPattern2", propMap.get("desc"));
	}
	
	/**
	 * Test method for {@link ConfigProviderImpl#getSubProperties(String)}.
	 */
	@Test
	public void testGetSubProperties_pattern3() {
		String propertiesFilePath = DATA_PATH + "/mlo-srv.003.properties";
		ConfigProviderImpl obj = createConfigProviderImpl(propertiesFilePath);
		
		String keyPrefix = "mlo.server.config.pattern.3.";
		Map<String, String> propMap = obj.getSubProperties(keyPrefix);
		assertEquals(0, propMap.size());
	}

	/**
	 * Test method for {@link ConfigProviderImpl#getIntegerProperty_NumberFormatException(String)}.
	 */
	@Test
	public void testGetIntegerProperty_NumberFormatException() {
		
		StringWriter writer = new StringWriter();
		WriterAppender appender = new WriterAppender(new PatternLayout("%p, %m%n"),writer);
		getLogger().addAppender(appender);
		getLogger().setAdditivity(false);
		try {
			String propertiesFilePath = DATA_PATH + "/mlo-srv.003.properties";
			ConfigProviderImpl obj = createConfigProviderImpl(propertiesFilePath);
			obj.getIntegerProperty("あいうえお");
			String logString = writer.toString();
			assertTrue(logString.contains("あいうえお"));
		} catch (Exception e) {
			fail();
		} finally{
			getLogger().removeAppender(appender);
			getLogger().setAdditivity(true);
		}
	}

	/**
	 * @return
	 */
	private Logger getLogger() {
		return LogManager.getLogger(ConfigProviderImpl.class);
	}

	/**
	 * @param propertiesFilePath
	 * @return
	 */
	public static ConfigProviderImpl createConfigProviderImpl(String propertiesFilePath) {
		ConfigProviderImpl obj = new ConfigProviderImpl("default.mlo-srv.properties", propertiesFilePath);
		return obj;
	}
}
