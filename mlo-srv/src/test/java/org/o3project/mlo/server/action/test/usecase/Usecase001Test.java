/**
 * ActionUsecaseTest.java
 * (C) 2013, Hitachi Solutions, Ltd.
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
public class Usecase001Test extends UsecaseTestBase {
	
	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.action.test.usecase.UsecaseTestBase#setUp()
	 */
	@Before
	protected void setUp() throws Exception {
		super.setUp();
		include("usecase.001.app.dicon");
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
		
		_assertMloAction(
				"CREATE", "usecase001/01.create.req.xml", 
				"usecase001/01.create.res.xml");
		
		_assertMloAction(
				"READ", "usecase001/read.00000001.req.xml", 
				"usecase001/02.read.00000001.res.xml");
		
		_assertMloAction(
				"UPDATE", "usecase001/03.update.mod.req.xml", 
				"usecase001/03.update.mod.res.xml");
		
		_assertMloAction(
				"READ", "usecase001/read.00000001.req.xml", 
				"usecase001/04.read.00000001.res.xml");
		
		_assertMloAction(
				"UPDATE", "usecase001/05.update.del.req.xml", 
				"usecase001/05.update.del.res.xml");
		
		_assertMloAction(
				"READ", "usecase001/read.00000001.req.xml", 
				"usecase001/06.read.00000001.res.xml");
		
		_assertMloAction(
				"CREATE", "usecase001/07.create.req.xml", 
				"usecase001/07.create.res.xml");
		
		_assertMloAction(
				"READ", "usecase001/read.00000002.req.xml", 
				"usecase001/08.read.00000002.res.xml");
		
		_assertMloAction(
				"CREATE", "usecase001/09.create.req.xml", 
				"usecase001/09.create.res.xml");
		
		_assertMloAction(
				"READ", "usecase001/read.00000002.req.xml", 
				"usecase001/10.read.00000002.res.xml");
		
		_assertMloAction(
				"DELETE", "usecase001/11.delete.req.xml", 
				"usecase001/11.delete.res.xml");
		
		_assertMloAction(
				"READ", "usecase001/read.00000001.req.xml", 
				"usecase001/12.read.00000001.res.xml");
		
		_assertMloAction(
				"UPDATE", "usecase001/13.update.add.req.xml", 
				"usecase001/13.update.add.res.xml");
		
		_assertMloAction(
				"READ", "usecase001/read.00000002.req.xml", 
				"usecase001/14.read.00000002.res.xml");
	}
	
	@Test
	public void testUsecase_normal_slices() throws Exception {
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("owner", "demoApl");
		
		_assertMloSlicesGetAction(paramMap, "usecase001/51.get.slices.res.xml");
		
		_assertMloAction(
				"CREATE", "usecase001/52.create.req.xml", 
				"usecase001/52.create.res.xml");
		
		_assertMloAction(
				"CREATE", "usecase001/53.create.req.xml", 
				"usecase001/53.create.res.xml");
		
		_assertMloSlicesGetAction(paramMap, "usecase001/54.get.slices.res.xml");
	}
	
	@Test
	public void testUsecase_anomaly_CreateSlice_BadRequest() throws Exception {
		// サポート外のソース CE ノード名
		_assertMloAction(
				"CREATE", "usecase001/61.create.req.xml", 
				"usecase001/61.create.res.xml");
		
		// サポート外のディスティネーション CE ノード名
		_assertMloAction(
				"CREATE", "usecase001/62.create.req.xml", 
				"usecase001/62.create.res.xml");
		
		// 帯域幅が大きすぎる
		_assertMloAction(
				"CREATE", "usecase001/63.create.req.xml", 
				"usecase001/63.create.res.xml");
	}
	
	@Test
	public void testUsecase_anomaly_UpdateSlice_BadRequest() throws Exception {
		
		// セットアップ
		_assertMloAction(
				"CREATE", "usecase001/71.create.req.xml", 
				"usecase001/71.create.res.xml");
		
		// 確認
		_assertMloAction(
				"READ", "usecase001/read.00000001.req.xml", 
				"usecase001/72.read.00000001.res.xml");
		
		// Inadequate band width
		_assertMloAction(
				"UPDATE", "usecase001/73.update.add.req.xml", 
				"usecase001/73.update.add.res.xml");
		
		// フローが増えていないことを確認
		_assertMloAction(
				"READ", "usecase001/read.00000001.req.xml", 
				"usecase001/72.read.00000001.res.xml");
		
		// Inadequate band width
		_assertMloAction(
				"UPDATE", "usecase001/74.update.mod.req.xml", 
				"usecase001/74.update.mod.res.xml");
		
		// フローが増えていないことを確認
		_assertMloAction(
				"READ", "usecase001/read.00000001.req.xml", 
				"usecase001/72.read.00000001.res.xml");
		
		// セットアップ (fa101 削除)
		_assertMloAction(
				"UPDATE", "usecase001/75.update.del.req.xml", 
				"usecase001/75.update.del.res.xml");
		
		// 確認
		_assertMloAction(
				"READ", "usecase001/read.00000001.req.xml", 
				"usecase001/76.read.00000001.res.xml");
		
		// 削除済みフローの変更
		_assertMloAction(
				"UPDATE", "usecase001/77.update.mod.req.xml", 
				"usecase001/77.update.mod.res.xml");
		
		// 削除済みフローの削除
		_assertMloAction(
				"UPDATE", "usecase001/78.update.del.req.xml", 
				"usecase001/78.update.del.res.xml");
		
		// 他のデータが変更されていないことを確認
		_assertMloAction(
				"READ", "usecase001/read.00000001.req.xml", 
				"usecase001/76.read.00000001.res.xml");
	}
}
