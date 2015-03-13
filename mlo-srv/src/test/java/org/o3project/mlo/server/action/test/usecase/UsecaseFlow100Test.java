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
public class UsecaseFlow100Test extends UsecaseTestBase {
	
	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.action.test.usecase.UsecaseTestBase#setUp()
	 */
	@Before
	protected void setUp() throws Exception {
		super.setUp();
		include("usecase.flow100.app.dicon");
	}
	
	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.action.test.usecase.UsecaseTestBase#tearDown()
	 */
	@After
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Test
	public void testUsecase_normal_scenario() throws Exception {
		
		// 1スライスに100フロー作成
		_assertMloAction(
				"CREATE", "usecaseFlow100/01.create.req.xml",
				"usecaseFlow100/01.create.res.xml");
		
		_assertMloAction(
				"READ", "usecaseFlow100/read.00000001.req.xml",
				"usecaseFlow100/02.read.00000001.res.xml");
		
		// 100フロー変更
		_assertMloAction(
				"UPDATE", "usecaseFlow100/03.update.mod.req.xml", 
				"usecaseFlow100/03.update.mod.res.xml");
		
		_assertMloAction(
				"READ", "usecaseFlow100/read.00000001.req.xml", 
				"usecaseFlow100/04.read.00000001.res.xml");
		
		// スライス削除
		_assertMloAction(
				"DELETE", "usecaseFlow100/05.delete.req.xml", 
				"usecaseFlow100/05.delete.res.xml");
		
	}
}
