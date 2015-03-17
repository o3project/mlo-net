/**
 * ActionUsecaseTest.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.action.test.usecase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * ActionUsecaseTest
 *
 */
public class UsecaseCreateSliceFailTest extends UsecaseTestBase {
	
	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.action.test.usecase.UsecaseTestBase#setUp()
	 */
	@Before
	protected void setUp() throws Exception {
		super.setUp();
		include("usecase.CreateSliceFail.app.dicon");
	}
	
	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.action.test.usecase.UsecaseTestBase#tearDown()
	 */
	@After
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Successfully deletes PW in modifying slice, failed to create PW, and then failed to create PW in adding flow.
	 * @throws Exception
	 */
	@Test
	public void testUsecase01() throws Exception {
		
		// Can create sliceA by demoApl.
		_assertMloAction(
				"CREATE", "usecaseCreateSliceFail/01.demoApl.create.req.xml", 
				"usecaseCreateSliceFail/01.demoApl.create.res.xml");
		
		// Can read sliceA@demoApl by demoApl.
		_assertMloAction(
				"READ", "usecaseCreateSliceFail/demoApl.read.00000001.req.xml", 
				"usecaseCreateSliceFail/02.demoApl.read.00000001.res.xml");
		
		// Cannot modify flow in SliceA@DemoApl by demoApl.
		_assertMloAction(
				"UPDATE", "usecaseCreateSliceFail/03.demoApl.update.mod.req.xml", 
				"usecaseCreateSliceFail/03.demoApl.update.mod.res.error.xml");
		
		// Can read sliceA@demoApl by demoApl.
		_assertMloAction(
				"READ", "usecaseCreateSliceFail/demoApl.read.00000001.req.xml", 
				"usecaseCreateSliceFail/02.demoApl.read.00000001.res.xml");
		
		// Cannot add flow in SliceA@DemoApl by demoApl.
		_assertMloAction(
				"UPDATE", "usecaseCreateSliceFail/04.demoApl.update.add.req.xml", 
				"usecaseCreateSliceFail/04.demoApl.update.add.res.error.xml");
		
		// Can read sliceA@demoApl by demoApl.
		_assertMloAction(
				"READ", "usecaseCreateSliceFail/demoApl.read.00000001.req.xml", 
				"usecaseCreateSliceFail/02.demoApl.read.00000001.res.xml");
		
		// Can delete SliceA@DemoApl by demoApl.
		_assertMloAction(
				"DELETE", "usecaseCreateSliceFail/05.demoApl.delete.00000001.req.xml", 
				"usecaseCreateSliceFail/05.demoApl.delete.res.error.xml");
				
	}
	
}
