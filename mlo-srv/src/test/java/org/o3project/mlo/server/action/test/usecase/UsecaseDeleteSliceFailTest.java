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
public class UsecaseDeleteSliceFailTest extends UsecaseTestBase {
	
	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.action.test.usecase.UsecaseTestBase#setUp()
	 */
	@Before
	protected void setUp() throws Exception {
		super.setUp();
		include("usecase.DeleteSliceFail.app.dicon");
	}
	
	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.action.test.usecase.UsecaseTestBase#tearDown()
	 */
	@After
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * In the case that flow cannot be deleted and the slice cannot be deleted.
	 * @throws Exception
	 */
	@Test
	public void testUsecase01() throws Exception {
		
		// Can create sliceA by demoApl.
		_assertMloAction(
				"CREATE", "usecaseDeleteSliceFail/01.demoApl.create.req.xml", 
				"usecaseDeleteSliceFail/01.demoApl.create.res.xml");
		
		// Can read sliceA@demoApl by demoApl.
		_assertMloAction(
				"READ", "usecaseDeleteSliceFail/demoApl.read.00000001.req.xml", 
				"usecaseDeleteSliceFail/02.demoApl.read.00000001.res.xml");
		
		// Cannot delete flow in SliceA@DemoApl by demoApl.
		_assertMloAction(
				"UPDATE", "usecaseDeleteSliceFail/03.demoApl.update.del.req.xml", 
				"usecaseDeleteSliceFail/03.demoApl.update.del.res.error.xml");
		
		// Can read sliceA@demoApl by demoApl.
		_assertMloAction(
				"READ", "usecaseDeleteSliceFail/demoApl.read.00000001.req.xml", 
				"usecaseDeleteSliceFail/02.demoApl.read.00000001.res.xml");
				
		// Cannot delete the slice by demoApl.
		_assertMloAction(
				"DELETE", "usecaseDeleteSliceFail/04.demoApl.delete.00000001.req.xml", 
				"usecaseDeleteSliceFail/04.demoApl.delete.00000001.res.error.xml");
		
		// Can read sliceA@demoApl in demoApl.
		_assertMloAction(
				"READ", "usecaseDeleteSliceFail/demoApl.read.00000001.req.xml", 
				"usecaseDeleteSliceFail/02.demoApl.read.00000001.res.xml");
	}
	
}
