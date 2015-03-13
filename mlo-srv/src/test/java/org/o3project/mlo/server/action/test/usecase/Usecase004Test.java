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
	 * スライス変更時、1個目のフローの変更は成功し、2個目のフロー変更で失敗するケース
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUsecase01() throws Exception {
		
		// sliceA を demoApl で作成できる。
		_assertMloAction(
				"CREATE", "usecase004/01.01.demoApl.create.req.xml", 
				"usecase004/01.01.demoApl.create.res.xml");
		
		// demoApl で sliceA@demoApl を読み込める。
		_assertMloAction(
				"READ", "usecase004/demoApl.read.00000001.req.xml", 
				"usecase004/01.02.demoApl.read.00000001.res.xml");
		
		// demoApl で SliceA@DemoAplの変更に失敗する
		_assertMloAction(
				"UPDATE", "usecase004/01.03.demoApl.update.mod.req.xml", 
				"usecase004/01.03.demoApl.update.error.res.xml");
		
		// demoApl で sliceA@demoApl を読み込める。(1フロー目だけ更新されている)
		_assertMloAction(
				"READ", "usecase004/demoApl.read.00000001.req.xml", 
				"usecase004/01.04.demoApl.read.00000001.res.xml");
	}
	
	
	/**
	 * スライス変更時、1個目のフローの追加は成功し、2個目のフロー変更で失敗するケース
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUsecase02() throws Exception {
		
		// sliceA を demoApl で作成できる。
		_assertMloAction(
				"CREATE", "usecase004/02.01.demoApl.create.req.xml", 
				"usecase004/02.01.demoApl.create.res.xml");
		
		// demoApl で sliceA@demoApl を読み込める。
		_assertMloAction(
				"READ", "usecase004/demoApl.read.00000001.req.xml", 
				"usecase004/02.02.demoApl.read.00000001.res.xml");
		
		// demoApl で SliceA@DemoAplの変更に失敗する
		_assertMloAction(
				"UPDATE", "usecase004/02.03.demoApl.update.mod.req.xml", 
				"usecase004/02.03.demoApl.update.error.res.xml");
		
		// demoApl で sliceA@demoApl を読み込める。(フローが追加され、既存フローは変更されていない)
		_assertMloAction(
				"READ", "usecase004/demoApl.read.00000001.req.xml", 
				"usecase004/02.04.demoApl.read.00000001.res.xml");
	}
	
	/**
	 * スライス変更時、1個目のフローの削除は成功し、2個目のフロー追加で失敗するケース
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUsecase03() throws Exception {
		
		// sliceA を demoApl で作成できる。
		_assertMloAction(
				"CREATE", "usecase004/03.01.demoApl.create.req.xml", 
				"usecase004/03.01.demoApl.create.res.xml");
		
		// demoApl で sliceA@demoApl を読み込める。
		_assertMloAction(
				"READ", "usecase004/demoApl.read.00000001.req.xml", 
				"usecase004/03.02.demoApl.read.00000001.res.xml");
		
		// demoApl で SliceA@DemoAplの変更に失敗する
		_assertMloAction(
				"UPDATE", "usecase004/03.03.demoApl.update.mod.req.xml", 
				"usecase004/03.03.demoApl.update.error.res.xml");
		
		// demoApl で sliceA@demoApl を読み込める。(フローが削除され、１つもフローが存在しなくなる)
		_assertMloAction(
				"READ", "usecase004/demoApl.read.00000001.req.xml", 
				"usecase004/03.04.demoApl.read.00000001.res.xml");
	}
}
