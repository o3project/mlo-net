/**
 * OdenOSAdapterServiceImplTest.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.rpc.service;

import static org.junit.Assert.*;

import java.io.File;
import java.io.StringWriter;
import java.util.concurrent.CountDownLatch;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.WriterAppender;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.impl.logic.ConfigProviderImpl;
import org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl;
import org.o3project.mlo.server.impl.rpc.service.OdenOSConfigImpl;
import org.o3project.mlo.server.logic.InternalException;
import org.o3project.mlo.server.logic.MloException;
import org.o3project.mlo.server.logic.TimeOutException;
import org.o3project.mlo.server.logic.TopologyConfigConstants;
import org.o3project.mlo.server.rpc.entity.PTFlowEntity;
import org.o3project.mlo.server.rpc.entity.PTLinkEntity;
import org.o3project.mlo.server.rpc.service.OdenOSConstants;
import org.o3project.mlo.server.rpc.service.OdenOSDriver;
import org.o3project.mlo.server.rpc.service.OdenOSConfig;

/**
 * OdenOSAdapterServiceImplTest
 *
 */
public class OdenOSAdapterServiceImplTest implements TopologyConfigConstants {
	
	private static final String DATA_PATH = "src/test/resources/org/o3project/mlo/server/rpc/service/data";
	
	private OdenOSAdapterServiceImpl obj;
	
	private OdenOSDriverStub driver;
	private OdenosTaskDummy odenosTask;
	private ConfigProviderImpl configProvider;
	private OdenOSConfigImpl odenOSConfig;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		driver = new OdenOSDriverStub();
		odenosTask = new OdenosTaskDummy();
		File propFile = new File(DATA_PATH, "odenos.config.003.properties");
		configProvider = new ConfigProviderImpl("default.mlo-srv.properties", propFile.getAbsolutePath());
		odenOSConfig = new OdenOSConfigImpl();
		odenOSConfig.setConfigProvider(configProvider);
		obj = createDefaultObj(odenosTask, driver, odenOSConfig);
		obj.setOdenosTask(odenosTask);
		driver.setListener(obj);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		obj = null;
		driver = null;
	}
	
	public static OdenOSAdapterServiceImpl createDefaultObj(OdenosTask odenosTask, OdenOSDriver driver, OdenOSConfig odenOSConfig) {
		OdenOSAdapterServiceImpl obj = null;
		obj = new OdenOSAdapterServiceImpl();
		
		obj.setDriver(driver);
		
		obj.setOdenOSConfig(odenOSConfig);
		
		obj.setOdenosTask(odenosTask);
		return obj;
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#init()}.
	 * @throws InterruptedException 
	 */
	@Test
	public void testInitAndDispose() throws InterruptedException {
		Throwable th = null;
		
		final CountDownLatch latch = new CountDownLatch(1);
		OdenosTaskDummy task = new OdenosTaskDummy(latch);
		
		obj.setOdenosTask(task);
		
		assertNull(task.getRunningThread());
		assertEquals(false, task.isStarted);
		assertEquals(false, task.isFinished);
		
		try {
			obj.init();
		} catch (Throwable t) {
			th = t;
		}
		assertNull(th);
		
		latch.await();
		
		assertNotNull(task.getRunningThread());
		assertFalse(task.getRunningThread().getName().equals(Thread.currentThread().getName()));
		assertEquals(true, task.isStarted);
		assertEquals(false, task.isFinished);

		try {
			obj.dispose();
		} catch (Throwable t) {
			th = t;
		}
		assertNull(th);
		
		assertNull(task.getRunningThread());
		assertEquals(true, task.isStarted);
		assertEquals(true, task.isFinished);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#getLink(java.lang.String)}.
	 */
	@Test
	public void testGetLink_Normal() {
		try {
			String linkName = String.format("link-%s-%s_%s", AMN6400_1_2_LINK_ID, AMN64001_NAME, AMN64002_NAME); // "link-" + AMN6400_1_2_LINK_ID;
			PTLinkEntity linkEntity = createPTLinkEntity(linkName, 10000, 9999);
			driver.setGetLinkResponse(linkEntity);
			PTLinkEntity res = obj.getLink(OdenOSConstants.LINK_TL_1);
			
			assertEquals(res.linkId, linkEntity.linkId);
			assertEquals(res.srcNode, linkEntity.srcNode);
			assertEquals(res.srcPort, linkEntity.srcPort);
			assertEquals(res.dstNode, linkEntity.dstNode);
			assertEquals(res.dstPort, linkEntity.dstPort);
			assertEquals(res.operStatus, linkEntity.operStatus);
			assertEquals(res.reqLatency, linkEntity.reqLatency);
			assertEquals(res.reqBandwidth, linkEntity.reqBandwidth);
			assertEquals(res.establishmentStatus, linkEntity.establishmentStatus);
		} catch (MloException e) {
			fail();
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#getLink(java.lang.String)}.
	 */
	@Test
	public void testGetLink_Invalid_Parameter() {
		try {
			obj.getLink(null);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof InternalException);
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#getLink(java.lang.String)}.
	 */
	@Test
	public void testGetLink_not_found() {
		try {
			String linkId = OdenOSConstants.LINK_TL_1;
			obj.getLink(linkId);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof InternalException);
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#getLink(java.lang.String)}.
	 */
	@Test
	public void testGetLink_odenos_error() {
		try {
			obj.getLink("test");
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof InternalException);
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#getLink(java.lang.String)}.
	 */
	@Test
	public void testGetLink_Driver_Nothing() {
		try {
			String linkId = OdenOSConstants.LINK_TL_1;
			obj.setDriver(null);
			obj.getLink(linkId);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof InternalException);
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#requestLink(org.o3project.mlo.server.rpc.entity.PTLinkEntity)}.
	 */
	@Test
	public void testRequestLink_normal_TL1() {
		try {
			String linkName = String.format("link-%s-%s_%s", AMN6400_1_2_LINK_ID, AMN64001_NAME, AMN64002_NAME); // "link-" + AMN6400_1_2_LINK_ID;
			PTLinkEntity req = createPTLinkEntity(linkName, 10000, 9999);
			driver.setRequestLinkWait(1);
			obj.requestLink(req);
		} catch (MloException e) {
			fail();
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#requestLink(org.o3project.mlo.server.rpc.entity.PTLinkEntity)}.
	 */
	@Test
	public void testRequestLink_normal_TL2() {
		try {
			String linkName = String.format("link-%s-%s_%s", AMN6400_2_3_LINK_ID, AMN64002_NAME, AMN64003_NAME); // "link-" + AMN6400_2_3_LINK_ID;
			PTLinkEntity req = createPTLinkEntity(linkName, 10000, 9999);
			driver.setRequestLinkWait(1);
			obj.requestLink(req);
		} catch (MloException e) {
			fail();
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#requestLink(org.o3project.mlo.server.rpc.entity.PTLinkEntity)}.
	 */
	@Test
	public void testRequestLink_normal_TL3() {
		try {
			String linkName = String.format("link-%s-%s_%s", AMN6400_1_3_LINK_ID, AMN64001_NAME, AMN64003_NAME);
			PTLinkEntity req = createPTLinkEntity(linkName, 10000, 8);
			driver.setRequestLinkWait(1);
			obj.requestLink(req);
		} catch (MloException e) {
			fail();
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#requestLink(org.o3project.mlo.server.rpc.entity.PTLinkEntity)}.
	 */
	@Test
	public void testRequestLink_normal_TL4() {
		try {
			String linkName = String.format("link-%s-%s_%s", AMN6400_2_1_LINK_ID, AMN64002_NAME, AMN64001_NAME); // "link-" + AMN6400_2_1_LINK_ID;
			PTLinkEntity req = createPTLinkEntity(linkName, 10000, 9999);
			driver.setRequestLinkWait(1);
			obj.requestLink(req);
		} catch (MloException e) {
			fail();
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#requestLink(org.o3project.mlo.server.rpc.entity.PTLinkEntity)}.
	 */
	@Test
	public void testRequestLink_normal_TL5() {
		try {
			String linkName = String.format("link-%s-%s_%s", AMN6400_3_2_LINK_ID, AMN64003_NAME, AMN64002_NAME); // "link-" + AMN6400_3_2_LINK_ID;
			PTLinkEntity req = createPTLinkEntity(linkName, 10000, 9999);
			driver.setRequestLinkWait(1);
			obj.requestLink(req);
		} catch (MloException e) {
			fail();
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#requestLink(org.o3project.mlo.server.rpc.entity.PTLinkEntity)}.
	 */
	@Test
	public void testRequestLink_normal_TL6() {
		try {
			String linkName = String.format("link-%s-%s_%s", AMN6400_3_1_LINK_ID, AMN64003_NAME, AMN64001_NAME); // "link-" + AMN6400_3_1_LINK_ID;
			PTLinkEntity req = createPTLinkEntity(linkName, 10000, 8);
			driver.setRequestLinkWait(1);
			obj.requestLink(req);
		} catch (MloException e) {
			fail();
		}
	}


//	/**
//	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#requestLink(org.o3project.mlo.server.rpc.entity.PTLinkEntity)}.
//	 */
//	@Test
//	public void testRequestLink_Exception() {
//		try {
//			PTLinkEntity req = new PTLinkEntity(OdenOSConstants.LinkInfo.TL1);
//			driver.setRequestLinkWait(-1);
//			obj.requestLink(req);
//			fail();
//		} catch (MloException e) {
//			assertTrue(e instanceof InternalException);
//		}
//	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#requestLink(org.o3project.mlo.server.rpc.entity.PTLinkEntity)}.
	 */
	@Test
	public void testRequestLink_Timeout() {
		try {
			String linkName = String.format("link-%s-%s_%s", AMN6400_1_2_LINK_ID, AMN64001_NAME, AMN64002_NAME); // "link-" + AMN6400_1_2_LINK_ID;
			PTLinkEntity req = createPTLinkEntity(linkName, 10000, 9999);
			driver.setRequestLinkWait(odenOSConfig.getResponseTimeout() + 1);
			obj.requestLink(req);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof TimeOutException);
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#requestLink(org.o3project.mlo.server.rpc.entity.PTLinkEntity)}.
	 */
	@Test
	public void testRequestLink_Driver_Nothing() {
		try {
			String linkName = "link-" + AMN6400_1_2_LINK_ID;
			PTLinkEntity req = createPTLinkEntity(linkName, 10000, 9999);
			obj.setDriver(null);
			obj.requestLink(req);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof InternalException);
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#notifyLinkChanged(java.lang.String)}.
	 */
	@Test
	public void testNotifyLinkChanged_normal_TL1() {
		try {
			String linkId = OdenOSConstants.LINK_TL_1;
			obj.setLinkQueue(linkId);
			obj.notifyLinkChanged(linkId);
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#notifyLinkChanged(java.lang.String)}.
	 */
	@Test
	public void testNotifyLinkChanged_normal_TL2() {
		try {
			String linkId = OdenOSConstants.LINK_TL_2;
			obj.setLinkQueue(linkId);
			obj.notifyLinkChanged(linkId);
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#notifyLinkChanged(java.lang.String)}.
	 */
	@Test
	public void testNotifyLinkChanged_normal_TL3() {
		try {
			String linkId = OdenOSConstants.LINK_TL_3;
			obj.setLinkQueue(linkId);
			obj.notifyLinkChanged(linkId);
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#notifyLinkChanged(java.lang.String)}.
	 */
	@Test
	public void testNotifyLinkChanged_normal_TL4() {
		try {
			String linkId = OdenOSConstants.LINK_TL_4;
			obj.setLinkQueue(linkId);
			obj.notifyLinkChanged(linkId);
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#notifyLinkChanged(java.lang.String)}.
	 */
	@Test
	public void testNotifyLinkChanged_normal_TL5() {
		try {
			String linkId = OdenOSConstants.LINK_TL_5;
			obj.setLinkQueue(linkId);
			obj.notifyLinkChanged(linkId);
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#notifyLinkChanged(java.lang.String)}.
	 */
	@Test
	public void testNotifyLinkChanged_normal_TL6() {
		try {
			String linkId = OdenOSConstants.LINK_TL_6;
			obj.setLinkQueue(linkId);
			obj.notifyLinkChanged(linkId);
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#notifyLinkChanged(java.lang.String)}.
	 */
	@Test
	public void testNotifyLinkChanged_unmatch() {
	    StringWriter writer = new StringWriter();
        WriterAppender appender = new WriterAppender(new PatternLayout("%p, %m%n"),writer);
        getLogger().addAppender(appender);
        getLogger().setAdditivity(false);
        try{
            // 何かしらのログを出力する処理
   			String linkId1 = OdenOSConstants.LINK_TL_1;
    		String linkId2 = OdenOSConstants.LINK_TL_2;
			try {
				obj.setLinkQueue(linkId1);
    			obj.notifyLinkChanged(linkId2);
    		} catch (Exception e) {
    			fail();
    		}
            // エラーログのテスト
            String logString = writer.toString();
            assertTrue(logString.contains("notifyLinkChanged Incorrect response : " + linkId2));

        }finally{
            // ログアペンダを消す
            getLogger().removeAppender(appender);
            getLogger().setAdditivity(true);
        }
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#notifyLinkChanged(java.lang.String)}.
	 */
	@Test
	public void testNotifyLinkChanged_null() {
	    StringWriter writer = new StringWriter();
        WriterAppender appender = new WriterAppender(new PatternLayout("%p, %m%n"),writer);
        getLogger().addAppender(appender);
        getLogger().setAdditivity(false);
        try{
            // 何かしらのログを出力する処理
    		try {
    			String linkId = OdenOSConstants.LINK_TL_1;
				obj.setLinkQueue(linkId);
    			obj.notifyLinkChanged(null);
    		} catch (Exception e) {
    			fail();
    		}
            // エラーログのテスト
            String logString = writer.toString();
            assertTrue(logString.contains("notifyLinkChanged Incorrect response : "));

        }finally{
            // ログアペンダを消す
            getLogger().removeAppender(appender);
            getLogger().setAdditivity(true);
        }
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#deleteFlow(java.lang.String)}.
	 */
	@Test
	public void testDeleteFlow_normal() {
		try {
			driver.setDeleteFlowWait(1);
			obj.deleteFlow("test");
		} catch (MloException e) {
			fail();
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#deleteFlow(java.lang.String)}.
	 */
	@Test
	public void testDeleteFlow_Invalid_Parameter() {
		try {
			driver.setDeleteFlowWait(1);
			obj.deleteFlow(null);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof InternalException);
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#deleteFlow(java.lang.String)}.
	 */
	@Test
	public void testDeleteFlow_Exception() {
		try {
			driver.setDeleteFlowWait(-1);
			obj.deleteFlow("test");
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof InternalException);
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#deleteFlow(java.lang.String)}.
	 */
	@Test
	public void testDeleteFlow_Timeout() {
		try {
			driver.setDeleteFlowWait(odenOSConfig.getResponseTimeout() + 1);
			obj.deleteFlow("test");
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof TimeOutException);
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#deleteFlow(java.lang.String)}.
	 */
	@Test
	public void testDeleteFlow_Driver_Nothing() {
		try {
			obj.setDriver(null);
			obj.deleteFlow("test");
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof InternalException);
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#notifyFlowDeleted(java.lang.String)}.
	 */
	@Test
	public void testNotifyFlowDeleted_normal() {
		try {
			obj.setDeleteFlowQueue("test");
			obj.notifyFlowDeleted("test");
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#notifyLinkDeleted(java.lang.String)}.
	 */
	@Test
	public void testNotifyFlowDeleted_unmatch() {
	    StringWriter writer = new StringWriter();
        WriterAppender appender = new WriterAppender(new PatternLayout("%p, %m%n"),writer);
        getLogger().addAppender(appender);
        getLogger().setAdditivity(false);
        try{
            // 何かしらのログを出力する処理
    		try {
    			obj.setDeleteFlowQueue("test");
    			obj.notifyFlowDeleted("test2");
    		} catch (Exception e) {
    			fail();
    		}
            // エラーログのテスト
            String logString = writer.toString();
            assertTrue(logString.contains("notifyFlowDeleted Incorrect response : " + "test2"));

        }finally{
            // ログアペンダを消す
            getLogger().removeAppender(appender);
            getLogger().setAdditivity(true);
        }
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#notifyLinkDeleted(java.lang.String)}.
	 */
	@Test
	public void testNotifyFlowDeleted_null() {
	    StringWriter writer = new StringWriter();
        WriterAppender appender = new WriterAppender(new PatternLayout("%p, %m%n"),writer);
        getLogger().addAppender(appender);
        getLogger().setAdditivity(false);
        try{
            // 何かしらのログを出力する処理
    		try {
    			obj.setDeleteFlowQueue("test");
    			obj.notifyFlowDeleted(null);
    		} catch (Exception e) {
    			fail();
    		}
            // エラーログのテスト
            String logString = writer.toString();
            assertTrue(logString.contains("notifyFlowDeleted Incorrect response : "));

        }finally{
            // ログアペンダを消す
            getLogger().removeAppender(appender);
            getLogger().setAdditivity(true);
        }
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#putFlow(org.o3project.mlo.server.rpc.entity.PTFlowEntity)}.
	 */
	@Test
	public void testPutFlow_normal_FLOW1() {
		try {
			String flowId = "flow1";
			PTFlowEntity req = createPTFlowEntity(flowId, 10000, 9999);
			driver.setPutFlowWait(1);
			String ret = obj.putFlow(req);
			assertEquals(ret, flowId);
		} catch (MloException e) {
			fail();
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#putFlow(org.o3project.mlo.server.rpc.entity.PTFlowEntity)}.
	 */
	@Test
	public void testPutFlow_normal_FLOW2() {
		try {
			String flowId = "flow2";
			PTFlowEntity req = createPTFlowEntity(flowId, 10000, 9999);
			driver.setPutFlowWait(1);
			String ret = obj.putFlow(req);
			assertEquals(ret, flowId);
		} catch (MloException e) {
			fail();
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#putFlow(org.o3project.mlo.server.rpc.entity.PTFlowEntity)}.
	 */
	@Test
	public void testPutFlow_normal_FLOW3() {
		try {
			String flowId = "flow3";
			PTFlowEntity req = createPTFlowEntity(flowId, 10000, 9999);
			driver.setPutFlowWait(1);
			String ret = obj.putFlow(req);
			assertEquals(ret, flowId);
		} catch (MloException e) {
			fail();
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#putFlow(org.o3project.mlo.server.rpc.entity.PTFlowEntity)}.
	 */
	@Test
	public void testPutFlow_normal_FLOW4() {
		try {
			String flowId = "flow4";
			PTFlowEntity req = createPTFlowEntity(flowId, 10000, 9999);
			driver.setPutFlowWait(1);
			String ret = obj.putFlow(req);
			assertEquals(ret, flowId);
		} catch (MloException e) {
			fail();
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#putFlow(org.o3project.mlo.server.rpc.entity.PTFlowEntity)}.
	 */
	@Test
	public void testPutFlow_Invalid_parameter_entity() {
		try {
			driver.setPutFlowWait(1);
			obj.putFlow(null);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof InternalException);
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#putFlow(org.o3project.mlo.server.rpc.entity.PTFlowEntity)}.
	 */
	@Test
	public void testPutFlow_Exception() {
		try {
			PTFlowEntity req = createPTFlowEntity("flow1", 10000, 9999);
			driver.setPutFlowWait(-1);
			obj.putFlow(req);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof InternalException);
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#putFlow(org.o3project.mlo.server.rpc.entity.PTFlowEntity)}.
	 */
	@Test
	public void testPutFlow_Timeout() {
		try {
			PTFlowEntity req = createPTFlowEntity("flow1", 10000, 9999);
			driver.setPutFlowWait(odenOSConfig.getResponseTimeout() + 1);
			obj.putFlow(req);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof TimeOutException);
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#putFlow(org.o3project.mlo.server.rpc.entity.PTFlowEntity)}.
	 */
	@Test
	public void testPutFlow_Driver_Nothing() {
		try {
			PTFlowEntity req = createPTFlowEntity("flow1", 10000, 9999);
			obj.setDriver(null);
			obj.putFlow(req);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof InternalException);
		}
	}
		
	/* Test method for
	 * {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#requestLink(org.o3project.mlo.server.rpc.entity.PTLinkEntity)}
	 * .
	 */
	@Test
	public void testRequestLink_link_null() {
		try {

			driver.setRequestLinkWait(1);
			obj.requestLink(null);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof InternalException);
		}
	}

	/**
	 * Test method for
	 * {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#requestLink(org.o3project.mlo.server.rpc.entity.PTLinkEntity)}
	 * .
	 */
	@Test
	public void testRequestLink_poll_InterruptedException() {
		try {
			String linkName = "link-" + AMN6400_1_2_LINK_ID;
			PTLinkEntity req = createPTLinkEntity(linkName, 10000, 9999);
			driver.setRequestLinkWait(1);
			Thread.currentThread().interrupt();
			obj.requestLink(req);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof InternalException);
		}
	}

	/**
	 */
	PTLinkEntity createPTLinkEntity(Object linkName, Object bandWidth, Object delay) {
		OdenOSTopology odenOSTopology = new OdenOSTopology();
		return odenOSTopology.createPTLinkEntity(linkName, bandWidth, delay, configProvider);
	}

	/**
	 * Test method for
	 * {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#requestLink(org.o3project.mlo.server.rpc.entity.PTLinkEntity)}
	 * .
	 */
	@Test
	public void testRequestLink_poll_Exception() {
		try {
			String linkName = "link-" + AMN6400_1_2_LINK_ID;
			PTLinkEntity req = createPTLinkEntity(linkName, 10000, 9999);

			driver.setRequestLinkWait(-1);
			obj.requestLink(req);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof InternalException);
		}
	}

	/**
	 * Test method for
	 * {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#requestLink(org.o3project.mlo.server.rpc.entity.PTLinkEntity)}
	 * .
	 */
	// @Test
	// public void testRequestLink_response_vacant() {
	// テスト不可能
	// }
	
	/**
	 * Test method for
	 * {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#notifyLinkChanged(java.lang.String)}
	 * .
	 */
	@Test
	public void testNotifyLinkChanged_InterruptedException() {
		
		StringWriter writer = new StringWriter();
		WriterAppender appender = new WriterAppender(new PatternLayout("%p, %m%n"),writer);
		getLogger().addAppender(appender);
		getLogger().setAdditivity(false);
		
		try {
			Thread.currentThread().interrupt();

			String linkId = OdenOSConstants.LINK_TL_1;
			obj.setLinkQueue(linkId);
			obj.notifyLinkChanged(linkId);
			
			String logString = writer.toString();
			assertTrue(logString.contains("Interrupted in the notifyLinkChanged"));

		} catch (Exception e) {
			fail();
		} finally{
			getLogger().removeAppender(appender);
			getLogger().setAdditivity(true);
		}
	}
	
	/**
	 * Test method for
	 * {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#deleteFlow(java.lang.String)}
	 * .
	 */
	@Test
	public void testDeleteFlow_InterruptedException() {
		try {
			Thread.currentThread().interrupt();
			driver.setDeleteFlowWait(1);
			obj.deleteFlow("test");
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof InternalException);
		}
	}
	
	/**
	 * Test method for
	 * {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#notifyFlowDeleted(java.lang.String)}
	 * .
	 */
	@Test
	public void testNotifyFlowDeleted_InterruptedException() {
		
		StringWriter writer = new StringWriter();
		WriterAppender appender = new WriterAppender(new PatternLayout("%p, %m%n"),writer);
		getLogger().addAppender(appender);
		getLogger().setAdditivity(false);
		
		try {
			Thread.currentThread().interrupt();
			obj.setDeleteFlowQueue("test");
			obj.notifyFlowDeleted("test");
			
			String logString = writer.toString();
			assertTrue(logString.contains("Interrupted in the notifyFlowDeleted"));
			
		} catch (Exception e) {
			fail();
		} finally{
			getLogger().removeAppender(appender);
			getLogger().setAdditivity(true);
		}
	}
	
	/**
	 * Test method for
	 * {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#putFlow(org.o3project.mlo.server.rpc.entity.PTFlowEntity)}
	 * .
	 */
	@Test
	public void testPutFlow_InterruptedException() {
		try {
			Thread.currentThread().interrupt();
			PTFlowEntity req = createPTFlowEntity("flow1", 10000, 9999);
			driver.setPutFlowWait(1);
			obj.putFlow(req);
			fail();
		} catch (MloException e) {
			e.printStackTrace();
			assertTrue(e instanceof InternalException);
		}
	}

	/**
	 * Test method for
	 * {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#putFlow(org.o3project.mlo.server.rpc.entity.PTFlowEntity)}
	 * .
	 */
	// MloExceptionにCatchされてExceptionまで届かないためテスト不可能
	// @Test
	// public void testPutFlow_Exception2() {
	// try {
	// PTFlowEntity req = new PTFlowEntity(OdenOSConstants.FlowInfo.FLOW1,
	// 10000, 9999);
	// driver.setPutFlowWait(-1);
	// String ret = obj.putFlow(req);
	// fail();
	// } catch (MloException e) {
	// assertTrue(e instanceof InternalException);
	// }
	// }

	/**
	 * Test method for
	 * {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#notifyFlowChanged(java.lang.String)}
	 * .
	 */
	@Test
	public void testnotifyFlowChanged_null_element() {
		
		StringWriter writer = new StringWriter();
		WriterAppender appender = new WriterAppender(new PatternLayout("%p, %m%n"),writer);
		getLogger().addAppender(appender);
		getLogger().setAdditivity(false);
		
		try {
			obj.notifyFlowChanged(null);
			assertTrue(true);
			
			String logString = writer.toString();
			assertTrue(logString.contains("notifyFlowChanged Incorrect response"));

		} catch (Exception e) {
			fail();
		} finally{
			getLogger().removeAppender(appender);
			getLogger().setAdditivity(true);
		}
	}

	/**
	 * Test method for
	 * {@link org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl#notifyFlowChanged(java.lang.String)}
	 * .
	 */
	// @Test
	// public void testnotifyFlowChanged_InterruptedException() {
	//
	// putFlow()にて
	// flowChangedQueueMap.putを行うので
	// notifyFlowChanged()にてinterruptを起こす前に
	// putFlow()無いで例外が起こり目的の箇所に到達することができない。
	//
	// }

	/**
	 * @return
	 */
	PTFlowEntity createPTFlowEntity(String flowId, Integer bandWidth, Integer delay) {
		OdenOSTopology odenOSTopology = new OdenOSTopology();
		return odenOSTopology.createPTFlowEntity(flowId, bandWidth, delay, configProvider);
	}

	/**
	 * @return
	 */
	private static Logger getLogger() {
		return LogManager.getLogger(OdenOSAdapterServiceImpl.class);
	}
}
