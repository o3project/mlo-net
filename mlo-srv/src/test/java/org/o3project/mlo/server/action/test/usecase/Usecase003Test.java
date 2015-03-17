/**
 * ActionUsecaseTest.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.action.test.usecase;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * ActionUsecaseTest
 *
 */
public class Usecase003Test extends UsecaseTestBase {
	
	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.action.test.usecase.UsecaseTestBase#setUp()
	 */
	@Before
	protected void setUp() throws Exception {
		super.setUp();
		include("usecase.003.app.dicon");
	}
	
	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.action.test.usecase.UsecaseTestBase#tearDown()
	 */
	@After
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Checks that slice space is separated by component name, demoApl and mloClient.
	 *
	 * The same slices can be created (/DEMO/CREATE) if component names are different.
	 * The slice information of anothe component name must not have been read (/DEMO/READ).
	 * @throws Exception
	 */
	@Test
	public void testUsecase01() throws Exception {
		
		// Can create sliceA by demoApl.
		_assertMloAction(
				"CREATE", "usecase003/01.01.demoApl.create.req.xml", 
				"usecase003/01.01.demoApl.create.res.xml");
		
		// Can read sliceA@demoApl by demoApl.
		_assertMloAction(
				"READ", "usecase003/demoApl.read.00000001.req.xml", 
				"usecase003/01.02.demoApl.read.00000001.res.xml");
		
		// Can create sliceA by mloClient.
		_assertMloAction(
				"CREATE", "usecase003/01.03.mloClient.create.req.xml", 
				"usecase003/01.03.mloClient.create.res.xml");
		
		// Can read sliceA@mloClient by mloClient.
		_assertMloAction(
				"READ", "usecase003/mloClient.read.00000002.req.xml", 
				"usecase003/01.04.mloClient.read.00000002.res.xml");
		
		// Must not read sliceA@mloClient by demoApl.
		_assertMloAction(
				"READ", "usecase003/demoApl.read.00000002.req.xml", 
				"usecase003/demoApl.read.NoData.res.xml");
		
		// Must not read sliceA@demoApl by mloClient.
		_assertMloAction(
				"READ", "usecase003/mloClient.read.00000001.req.xml", 
				"usecase003/mloClient.read.NoData.res.xml");
	}
	
	/**
	 * Checks that slice space is separated by component name, demoApl and mloClient.
	 *
	 * The slice information of another component name must not have been deleted (/DEMO/DELETE).
	 * @throws Exception
	 */
	@Test
	public void testUsecase02() throws Exception {
	
		// Setups: Creates sliceA@demoApl
		_assertMloAction(
				"CREATE", "usecase003/01.01.demoApl.create.req.xml", 
				"usecase003/01.01.demoApl.create.res.xml");
		
		// Setups: Creates sliceA@mloClient
		_assertMloAction(
				"CREATE", "usecase003/01.03.mloClient.create.req.xml", 
				"usecase003/01.03.mloClient.create.res.xml");

		
		// Must not delete sliceA@mloClient by demoApl.
		_assertMloAction(
				"DELETE", "usecase003/02.01.demoApl.delete.00000002.req.xml", 
				"usecase003/demoApl.delete.AlreadyDeleted.res.xml");
		
		// Must not delete sliceA@demoApl by mloClient.
		_assertMloAction(
				"DELETE", "usecase003/02.02.mloClient.delete.00000001.req.xml", 
				"usecase003/mloClient.delete.AlreadyDeleted.res.xml");
		
		// Can delete sliceA@demoApl by demoApl.
		_assertMloAction(
				"DELETE", "usecase003/02.03.demoApl.delete.00000001.req.xml", 
				"usecase003/02.03.demoApl.delete.00000001.res.xml");
		
		// Can delete sliceA@mloClient by mloClient.
		_assertMloAction(
				"DELETE", "usecase003/02.04.mloClient.delete.00000002.req.xml", 
				"usecase003/02.04.mloClient.delete.00000002.res.xml");
	}
	
	/**
	 * Checks that slice space is separated by component name, demoApl and mloClient.
	 *
	 * The slice information of another component name must not have been updated (/DEMO/UPDATE).
	 * @throws Exception
	 */
	@Test
	public void testUsecase03() throws Exception {
	
		// Setup : Creates sliceA@demoApl
		_assertMloAction(
				"CREATE", "usecase003/01.01.demoApl.create.req.xml", 
				"usecase003/01.01.demoApl.create.res.xml");
		
		// Setup: Creates sliceA@mloClient
		_assertMloAction(
				"CREATE", "usecase003/01.03.mloClient.create.req.xml", 
				"usecase003/01.03.mloClient.create.res.xml");

		
		// Must not update sliceA@mloClient by demoApl.
		_assertMloAction(
				"UPDATE", "usecase003/03.01.demoApl.update.mod.00000002.req.xml", 
				"usecase003/03.01.demoApl.update.SliceIdDoesNotExist.res.xml");
		
		// Must not update sliceA@demoApl by mloClient.
		_assertMloAction(
				"UPDATE", "usecase003/03.02.mloClient.update.mod.00000001.req.xml", 
				"usecase003/03.02.mloClient.update.SliceIdDoesNotExist.res.xml");
		
		// Can update sliceA@demoApl by demoApl.
		_assertMloAction(
				"UPDATE", "usecase003/03.03.demoApl.update.mod.00000001.req.xml", 
				"usecase003/03.03.demoApl.update.mod.00000001.res.xml");
		
		// Can update  sliceA@mloClient by mloClient.
		_assertMloAction(
				"UPDATE", "usecase003/03.04.mloClient.update.mod.00000002.req.xml", 
				"usecase003/03.04.mloClient.update.mod.00000002.res.xml");
		
		// Can read correct data of sliceA@demoApl by demoApl.
		_assertMloAction(
				"READ", "usecase003/demoApl.read.00000001.req.xml", 
				"usecase003/03.05.demoApl.read.00000001.res.xml");

		// Can read correct data of sliceA@mloClient by mloClient.
		_assertMloAction(
				"READ", "usecase003/mloClient.read.00000002.req.xml", 
				"usecase003/03.06.mloClient.read.00000002.res.xml");
	}

	/**
	 * Checks that slice space is separated by component name, demoApl and mloClient.
	 *
	 * The slice list of another component name must not have been obtained (/DEMO/slices).
	 * @throws Exception
	 */
	@Test
	public void testUsecase04_slicesGet() throws Exception {
	
		// Setup: Creates sliceA, sliceB @demoApl
		_assertMloAction(
				"CREATE", "usecase003/04.01.demoApl.create.req.xml", 
				"usecase003/04.01.demoApl.create.res.xml");
		_assertMloAction(
				"CREATE", "usecase003/04.02.demoApl.create.req.xml", 
				"usecase003/04.02.demoApl.create.res.xml");
		
		// Setup: Creates sliceC, sliceD @mloClient
		_assertMloAction(
				"CREATE", "usecase003/04.03.mloClient.create.req.xml", 
				"usecase003/04.03.mloClient.create.res.xml");
		_assertMloAction(
				"CREATE", "usecase003/04.04.mloClient.create.req.xml", 
				"usecase003/04.04.mloClient.create.res.xml");
		
		// demoApl can retrieve sliceA, sliceB, but not sliceC, sliceD.
		Map<String, String> demoAplParamMap = new HashMap<String, String>();
		demoAplParamMap.put("owner", "demoApl");
		_assertMloSlicesGetAction(demoAplParamMap, 
				"usecase003/04.05.demoApl.get.slices.res.xml");
		demoAplParamMap = null;
		
		// mloClient can retrieve sliceC, sliceD, but not sliceA, sliceB.
		Map<String, String> mloClientParamMap = new HashMap<String, String>();
		mloClientParamMap.put("owner", "mloClient");
		_assertMloSlicesGetAction(mloClientParamMap, 
				"usecase003/04.06.mloClient.get.slices.res.xml");
		mloClientParamMap = null;
	}
}
