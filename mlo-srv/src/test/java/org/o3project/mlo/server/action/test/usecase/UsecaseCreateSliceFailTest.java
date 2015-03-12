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
	 * スライス変更(mod)でPW削除に成功した後、PW作成に失敗、さらにスライス変更(add)でPW作成に失敗するケース
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUsecase01() throws Exception {
		
		// sliceA を demoApl で作成できる。
		_assertMloAction(
				"CREATE", "usecaseCreateSliceFail/01.demoApl.create.req.xml", 
				"usecaseCreateSliceFail/01.demoApl.create.res.xml");
		
		// demoApl で sliceA@demoApl を読み込める。
		_assertMloAction(
				"READ", "usecaseCreateSliceFail/demoApl.read.00000001.req.xml", 
				"usecaseCreateSliceFail/02.demoApl.read.00000001.res.xml");
		
		// demoApl で SliceA@DemoAplの変更(フロー変更)に失敗する
		_assertMloAction(
				"UPDATE", "usecaseCreateSliceFail/03.demoApl.update.mod.req.xml", 
				"usecaseCreateSliceFail/03.demoApl.update.mod.res.error.xml");
		
		// demoApl で sliceA@demoApl を読み込める。
		_assertMloAction(
				"READ", "usecaseCreateSliceFail/demoApl.read.00000001.req.xml", 
				"usecaseCreateSliceFail/02.demoApl.read.00000001.res.xml");
		
		// demoApl で SliceA@DemoAplの変更(フロー追加)に失敗する
		_assertMloAction(
				"UPDATE", "usecaseCreateSliceFail/04.demoApl.update.add.req.xml", 
				"usecaseCreateSliceFail/04.demoApl.update.add.res.error.xml");
		
		// demoApl で sliceA@demoApl を読み込める。
		_assertMloAction(
				"READ", "usecaseCreateSliceFail/demoApl.read.00000001.req.xml", 
				"usecaseCreateSliceFail/02.demoApl.read.00000001.res.xml");
		
		// demoApl で SliceA@DemoAplを削除する
		_assertMloAction(
				"DELETE", "usecaseCreateSliceFail/05.demoApl.delete.00000001.req.xml", 
				"usecaseCreateSliceFail/05.demoApl.delete.res.error.xml");
				
	}
	
}
