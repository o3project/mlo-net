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
public class Usecase004Test extends UsecaseTestBase {
	
	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.action.test.usecase.UsecaseTestBase#setUp()
	 */
	@Before
	protected void setUp() throws Exception {
		super.setUp();
		include("usecase.004.app.dicon");
	}
	
	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.action.test.usecase.UsecaseTestBase#tearDown()
	 */
	@After
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * In the case that first flow is successfully modified but second flow fails to be modified in updating a slice.
	 * @throws Exception
	 */
	@Test
	public void testUsecase01() throws Exception {
		
		// Can create sliceA by demoApl.
		_assertMloAction(
				"CREATE", "usecase004/01.01.demoApl.create.req.xml", 
				"usecase004/01.01.demoApl.create.res.xml");
		
		// Can read sliceA@demoApl by demoApl.
		_assertMloAction(
				"READ", "usecase004/demoApl.read.00000001.req.xml", 
				"usecase004/01.02.demoApl.read.00000001.res.xml");
		
		// Cannot modify SliceA@DemoApl by demoApl.
		_assertMloAction(
				"UPDATE", "usecase004/01.03.demoApl.update.mod.req.xml", 
				"usecase004/01.03.demoApl.update.error.res.xml");
		
		// Can read sliceA@demoApl by demoApl (and can confirm that only first flow is updated).
		_assertMloAction(
				"READ", "usecase004/demoApl.read.00000001.req.xml", 
				"usecase004/01.04.demoApl.read.00000001.res.xml");
	}
	
	
	/**
	 * In the case that first flow is successfully added but second flow fails to be modified in updating a slice.
	 * @throws Exception
	 */
	@Test
	public void testUsecase02() throws Exception {
		
		// Can create demoApl by sliceA.
		_assertMloAction(
				"CREATE", "usecase004/02.01.demoApl.create.req.xml", 
				"usecase004/02.01.demoApl.create.res.xml");
		
		// Can read sliceA@demoApl by demoApl.
		_assertMloAction(
				"READ", "usecase004/demoApl.read.00000001.req.xml", 
				"usecase004/02.02.demoApl.read.00000001.res.xml");
		
		// Cannot modify SliceA@DemoApl by demoApl.
		_assertMloAction(
				"UPDATE", "usecase004/02.03.demoApl.update.mod.req.xml", 
				"usecase004/02.03.demoApl.update.error.res.xml");
		
		// can read sliceA@demoApl by demoApl (a flow has been added, but existing flow is not modified.)
		_assertMloAction(
				"READ", "usecase004/demoApl.read.00000001.req.xml", 
				"usecase004/02.04.demoApl.read.00000001.res.xml");
	}
	
	/**
	 * In the case that first flow is successfully deleted but second flow fails to be modified in updating a slice.
	 * @throws Exception
	 */
	@Test
	public void testUsecase03() throws Exception {
		
		// Can create sliceA by demoApl.
		_assertMloAction(
				"CREATE", "usecase004/03.01.demoApl.create.req.xml", 
				"usecase004/03.01.demoApl.create.res.xml");
		
		// Can read sliceA@demoApl by demoApl.
		_assertMloAction(
				"READ", "usecase004/demoApl.read.00000001.req.xml", 
				"usecase004/03.02.demoApl.read.00000001.res.xml");
		
		// Cannot modify SliceA@DemoApl by demoApl.
		_assertMloAction(
				"UPDATE", "usecase004/03.03.demoApl.update.mod.req.xml", 
				"usecase004/03.03.demoApl.update.error.res.xml");
		
		// Can read sliceA@demoApl by demoApl (first flow has been deleted. There exists no flow in this slice).
		_assertMloAction(
				"READ", "usecase004/demoApl.read.00000001.req.xml", 
				"usecase004/03.04.demoApl.read.00000001.res.xml");
	}
}
