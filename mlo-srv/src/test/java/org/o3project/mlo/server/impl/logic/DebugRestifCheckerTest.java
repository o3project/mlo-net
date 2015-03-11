/**
 * DebugRestifCheckerTest.java
 * (C) 2013, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import static org.junit.Assert.*;

import java.io.File;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.impl.logic.DebugRestifChecker;
import org.o3project.mlo.server.logic.ApiCallException;
import org.o3project.mlo.server.logic.ConfigProvider;
import org.o3project.mlo.server.logic.DbAccessException;
import org.o3project.mlo.server.logic.InternalException;
import org.o3project.mlo.server.logic.MloException;
import org.o3project.mlo.server.logic.OtherException;
import org.o3project.mlo.server.logic.TimeOutException;

/**
 * DebugRestifCheckerTest
 *
 */
public class DebugRestifCheckerTest {
	private ConfigProvider configProvider;
	private DebugRestifChecker obj;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		File propFile = new File("src/test/resources/", "test.mlo-srv.properties");
		configProvider = ConfigProviderImplTest.createConfigProviderImpl(propFile.getAbsolutePath());
		obj = new DebugRestifChecker();
		obj.setConfigProvider(configProvider);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		configProvider = null;
		obj = null;
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.DebugRestifChecker#checkBefore(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCheckBefore_SimpleAPICallError() {
		try {
			obj.checkBefore("ReadAPICallError", "Read");
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof ApiCallException);
		}
		
		try {
			obj.checkBefore("ReadAPICallError", "Read");
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof ApiCallException);
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.DebugRestifChecker#checkBefore(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCheckBefore_SimpleDBAccessError() {
		try {
			obj.checkBefore("CreateDBAccessError", "Create");
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof DbAccessException);
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.DebugRestifChecker#checkBefore(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCheckBefore_SimpleInternalError() {
		try {
			obj.checkBefore("UpdateInternalError", "Update");
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof InternalException);
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.DebugRestifChecker#checkBefore(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCheckBefore_SimpleTimeOutError() {
		try {
			obj.checkBefore("DeleteTimeOutError", "Delete");
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof TimeOutException);
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.DebugRestifChecker#checkBefore(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCheckBefore_SimpleOtherError() {
		try {
			obj.checkBefore("ReadOtherError", "Read");
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof OtherException);
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.DebugRestifChecker#checkBefore(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCheckBefore_BeforeUpdateToggledAPICallError() {
		String sliceName = "BeforeUpdateToggledAPICallError";
		String ope = "Update";
		
		try {
			obj.checkBefore(sliceName, ope);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof ApiCallException);
		}
		
		try {
			obj.checkBefore(sliceName, ope);
			assertTrue(true);
		} catch (MloException e) {
			fail();
		}
		
		try {
			obj.checkBefore(sliceName, ope);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof ApiCallException);
		}
		
		try {
			obj.checkBefore(sliceName, ope);
			assertTrue(true);
		} catch (MloException e) {
			fail();
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.DebugRestifChecker#checkBefore(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCheckBefore_BeforeCreateToggledAPICallError() {
		String sliceName = "BeforeCreateToggledAPICallError";
		String ope = "Create";
		
		try {
			obj.checkBefore(sliceName, ope);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof ApiCallException);
		}
		
		try {
			obj.checkBefore(sliceName, ope);
			assertTrue(true);
		} catch (MloException e) {
			fail();
		}
		
		try {
			obj.checkBefore(sliceName, ope);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof ApiCallException);
		}
		
		try {
			obj.checkBefore(sliceName, ope);
			assertTrue(true);
		} catch (MloException e) {
			fail();
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.DebugRestifChecker#checkAfter(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCheckAfter_AfterCreateToggledTimeOutError() {
		String sliceName = "AfterCreateToggledTimeOutError";
		String ope = "Create";
		
		try {
			obj.checkAfter(sliceName, ope);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof TimeOutException);
		}
		
		try {
			obj.checkAfter(sliceName, ope);
			assertTrue(true);
		} catch (MloException e) {
			fail();
		}
		
		try {
			obj.checkAfter(sliceName, ope);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof TimeOutException);
		}
		
		try {
			obj.checkAfter(sliceName, ope);
			assertTrue(true);
		} catch (MloException e) {
			fail();
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.DebugRestifChecker#checkAfter(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCheckAfter_AfterDeleteToggledTimeOutError() {
		String sliceName = "AfterDeleteToggledTimeOutError";
		String ope = "Delete";
		
		try {
			obj.checkAfter(sliceName, ope);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof TimeOutException);
		}
		
		try {
			obj.checkAfter(sliceName, ope);
			assertTrue(true);
		} catch (MloException e) {
			fail();
		}
		
		try {
			obj.checkAfter(sliceName, ope);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof TimeOutException);
		}
		
		try {
			obj.checkAfter(sliceName, ope);
			assertTrue(true);
		} catch (MloException e) {
			fail();
		}
	}

}
