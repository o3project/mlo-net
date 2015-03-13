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
public class Usecase002Test extends UsecaseTestBase {
	
	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.action.test.usecase.UsecaseTestBase#setUp()
	 */
	@Before
	protected void setUp() throws Exception {
		super.setUp();
		include("usecase.002.app.dicon");
	}
	
	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.action.test.usecase.UsecaseTestBase#tearDown()
	 */
	@After
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Test
	public void testUsecase() throws Exception {
		
		_assertMloAction(
				"CREATE", "usecase002/01.create.req.xml", 
				"usecase002/01.create.res.xml");
		
		_assertMloAction(
				"READ", "usecase002/read.00000001.req.xml", 
				"usecase002/02.read.00000001.res.xml");
		
		_assertMloAction(
				"UPDATE", "usecase002/03.update.mod.req.xml", 
				"usecase002/03.update.mod.res.xml");
		
		_assertMloAction(
				"READ", "usecase002/read.00000001.req.xml", 
				"usecase002/04.read.00000001.res.xml");
		
		_assertMloAction(
				"UPDATE", "usecase002/05.update.del.req.xml", 
				"usecase002/05.update.del.res.xml");
		
		_assertMloAction(
				"READ", "usecase002/read.00000001.req.xml", 
				"usecase002/06.read.00000001.res.xml");
		
		_assertMloAction(
				"CREATE", "usecase002/07.create.req.xml", 
				"usecase002/07.create.res.xml");
		
		_assertMloAction(
				"READ", "usecase002/read.00000002.req.xml", 
				"usecase002/08.read.00000002.res.xml");
		
		_assertMloAction(
				"CREATE", "usecase002/09.create.req.xml", 
				"usecase002/09.create.res.xml");
		
		_assertMloAction(
				"READ", "usecase002/read.00000002.req.xml", 
				"usecase002/10.read.00000002.res.xml");
		
		_assertMloAction(
				"DELETE", "usecase002/11.delete.req.xml", 
				"usecase002/11.delete.res.xml");
		
		_assertMloAction(
				"READ", "usecase002/read.00000001.req.xml", 
				"usecase002/12.read.00000001.res.xml");
		
		_assertMloAction(
				"UPDATE", "usecase002/13.update.add.req.xml", 
				"usecase002/13.update.add.res.xml");
		
		_assertMloAction(
				"READ", "usecase002/read.00000002.req.xml", 
				"usecase002/14.read.00000002.res.xml");
	}
}
