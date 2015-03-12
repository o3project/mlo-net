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
	 * スライス変更でフロー削除に失敗し、スライス削除でも失敗するケース
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUsecase01() throws Exception {
		
		// sliceA を demoApl で作成できる。
		_assertMloAction(
				"CREATE", "usecaseDeleteSliceFail/01.demoApl.create.req.xml", 
				"usecaseDeleteSliceFail/01.demoApl.create.res.xml");
		
		// demoApl で sliceA@demoApl を読み込める。
		_assertMloAction(
				"READ", "usecaseDeleteSliceFail/demoApl.read.00000001.req.xml", 
				"usecaseDeleteSliceFail/02.demoApl.read.00000001.res.xml");
		
		// demoApl で SliceA@DemoAplの変更(フロー削除)に失敗する
		_assertMloAction(
				"UPDATE", "usecaseDeleteSliceFail/03.demoApl.update.del.req.xml", 
				"usecaseDeleteSliceFail/03.demoApl.update.del.res.error.xml");
		
		// demoApl で sliceA@demoApl を読み込める。
		_assertMloAction(
				"READ", "usecaseDeleteSliceFail/demoApl.read.00000001.req.xml", 
				"usecaseDeleteSliceFail/02.demoApl.read.00000001.res.xml");
				
		// demoApl で スライス削除に失敗する。
		_assertMloAction(
				"DELETE", "usecaseDeleteSliceFail/04.demoApl.delete.00000001.req.xml", 
				"usecaseDeleteSliceFail/04.demoApl.delete.00000001.res.error.xml");
		
		// demoApl で sliceA@demoApl を読み込める。
		_assertMloAction(
				"READ", "usecaseDeleteSliceFail/demoApl.read.00000001.req.xml", 
				"usecaseDeleteSliceFail/02.demoApl.read.00000001.res.xml");
	}
	
}
