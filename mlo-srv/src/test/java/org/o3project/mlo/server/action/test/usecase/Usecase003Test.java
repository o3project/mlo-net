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
	 * コンポーネント名 demoApl と mloClient の分割確認。
	 * 
	 * コンポーネント名が違う場合、同じスライス名のスライスが作成 (/DEMO/CREATE) できる。
	 * コンポーネント名が違うスライス情報を読み込めない (/DEMO/READ)。
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUsecase01() throws Exception {
		
		// sliceA を demoApl で作成できる。
		_assertMloAction(
				"CREATE", "usecase003/01.01.demoApl.create.req.xml", 
				"usecase003/01.01.demoApl.create.res.xml");
		
		// demoApl で sliceA@demoApl を読み込める。
		_assertMloAction(
				"READ", "usecase003/demoApl.read.00000001.req.xml", 
				"usecase003/01.02.demoApl.read.00000001.res.xml");
		
		// sliceA を mloClient で作成できる。
		_assertMloAction(
				"CREATE", "usecase003/01.03.mloClient.create.req.xml", 
				"usecase003/01.03.mloClient.create.res.xml");
		
		// mloClient で sliceA@mloClient を読み込める。
		_assertMloAction(
				"READ", "usecase003/mloClient.read.00000002.req.xml", 
				"usecase003/01.04.mloClient.read.00000002.res.xml");
		
		// demoApl で sliceA@mloClient を読み込むことができない。
		_assertMloAction(
				"READ", "usecase003/demoApl.read.00000002.req.xml", 
				"usecase003/demoApl.read.NoData.res.xml");
		
		// mloClient で sliceA@demoApl を読み込むことができない。
		_assertMloAction(
				"READ", "usecase003/mloClient.read.00000001.req.xml", 
				"usecase003/mloClient.read.NoData.res.xml");
	}
	
	/**
	 * コンポーネント名 demoApl と mloClient の分割確認。
	 * 
	 * コンポーネント名が違う場合、スライスを削除 (/DEMO/DELETE) できない。
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUsecase02() throws Exception {
	
		// 準備: sliceA@demoApl を作成する。
		_assertMloAction(
				"CREATE", "usecase003/01.01.demoApl.create.req.xml", 
				"usecase003/01.01.demoApl.create.res.xml");
		
		// 準備: sliceA@mloClient を作成する。
		_assertMloAction(
				"CREATE", "usecase003/01.03.mloClient.create.req.xml", 
				"usecase003/01.03.mloClient.create.res.xml");

		
		// demoApl から sliceA@mloClient を削除することができない。
		_assertMloAction(
				"DELETE", "usecase003/02.01.demoApl.delete.00000002.req.xml", 
				"usecase003/demoApl.delete.AlreadyDeleted.res.xml");
		
		// mloClient から sliceA@demoApl を削除することができない。
		_assertMloAction(
				"DELETE", "usecase003/02.02.mloClient.delete.00000001.req.xml", 
				"usecase003/mloClient.delete.AlreadyDeleted.res.xml");
		
		// demoApl から sliceA@demoApl を削除することができる。
		_assertMloAction(
				"DELETE", "usecase003/02.03.demoApl.delete.00000001.req.xml", 
				"usecase003/02.03.demoApl.delete.00000001.res.xml");
		
		// mloClient から sliceA@mloClient を削除することができる。
		_assertMloAction(
				"DELETE", "usecase003/02.04.mloClient.delete.00000002.req.xml", 
				"usecase003/02.04.mloClient.delete.00000002.res.xml");
	}
	
	/**
	 * コンポーネント名 demoApl と mloClient の分割確認。
	 * 
	 * コンポーネント名が違う場合、スライスを変更 (/DEMO/UPDATE) できない。
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUsecase03() throws Exception {
	
		// 準備: sliceA@demoApl を作成する。
		_assertMloAction(
				"CREATE", "usecase003/01.01.demoApl.create.req.xml", 
				"usecase003/01.01.demoApl.create.res.xml");
		
		// 準備: sliceA@mloClient を作成する。
		_assertMloAction(
				"CREATE", "usecase003/01.03.mloClient.create.req.xml", 
				"usecase003/01.03.mloClient.create.res.xml");

		
		// demoApl から sliceA@mloClient を更新することができない。
		_assertMloAction(
				"UPDATE", "usecase003/03.01.demoApl.update.mod.00000002.req.xml", 
				"usecase003/03.01.demoApl.update.SliceIdDoesNotExist.res.xml");
		
		// mloClient から sliceA@demoApl を更新することができない。
		_assertMloAction(
				"UPDATE", "usecase003/03.02.mloClient.update.mod.00000001.req.xml", 
				"usecase003/03.02.mloClient.update.SliceIdDoesNotExist.res.xml");
		
		// demoApl から sliceA@demoApl を更新することができる。
		_assertMloAction(
				"UPDATE", "usecase003/03.03.demoApl.update.mod.00000001.req.xml", 
				"usecase003/03.03.demoApl.update.mod.00000001.res.xml");
		
		// mloClient から sliceA@mloClient を更新することができる。
		_assertMloAction(
				"UPDATE", "usecase003/03.04.mloClient.update.mod.00000002.req.xml", 
				"usecase003/03.04.mloClient.update.mod.00000002.res.xml");
		
		// demoApl で sliceA@demoApl を読み込んだ結果が正しい。
		_assertMloAction(
				"READ", "usecase003/demoApl.read.00000001.req.xml", 
				"usecase003/03.05.demoApl.read.00000001.res.xml");

		// mloClient で sliceA@mloClient を読み込んだ結果が正しい。
		_assertMloAction(
				"READ", "usecase003/mloClient.read.00000002.req.xml", 
				"usecase003/03.06.mloClient.read.00000002.res.xml");
	}

	/**
	 * コンポーネント名 demoApl と mloClient の分割確認。
	 * 
	 * コンポーネント名が違う場合、スライス一覧を取得できない。
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUsecase04_slicesGet() throws Exception {
	
		// 準備: sliceA, sliceB @demoApl を作成する。
		_assertMloAction(
				"CREATE", "usecase003/04.01.demoApl.create.req.xml", 
				"usecase003/04.01.demoApl.create.res.xml");
		_assertMloAction(
				"CREATE", "usecase003/04.02.demoApl.create.req.xml", 
				"usecase003/04.02.demoApl.create.res.xml");
		
		// 準備: sliceC, sliceD @mloClient を作成する。
		_assertMloAction(
				"CREATE", "usecase003/04.03.mloClient.create.req.xml", 
				"usecase003/04.03.mloClient.create.res.xml");
		_assertMloAction(
				"CREATE", "usecase003/04.04.mloClient.create.req.xml", 
				"usecase003/04.04.mloClient.create.res.xml");
		
		// demoApl は sliceA, sliceB は取れるが sliceC, sliceD は取れない。
		Map<String, String> demoAplParamMap = new HashMap<String, String>();
		demoAplParamMap.put("owner", "demoApl");
		_assertMloSlicesGetAction(demoAplParamMap, 
				"usecase003/04.05.demoApl.get.slices.res.xml");
		demoAplParamMap = null;
		
		// mloClient は sliceC, sliceD は取れるが sliceA, sliceB は取れない。
		Map<String, String> mloClientParamMap = new HashMap<String, String>();
		mloClientParamMap.put("owner", "mloClient");
		_assertMloSlicesGetAction(mloClientParamMap, 
				"usecase003/04.06.mloClient.get.slices.res.xml");
		mloClientParamMap = null;
	}
}
