/**
 * SliceManagerOtherImplTest.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Map;

import javax.xml.bind.JAXB;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.WriterAppender;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.dto.RestifRequestDto;
import org.o3project.mlo.server.dto.RestifResponseDto;
import org.o3project.mlo.server.impl.logic.ConfigProviderImpl;
import org.o3project.mlo.server.impl.logic.DebugRestifChecker;
import org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl;
import org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOptDeviceImpl;
import org.o3project.mlo.server.impl.logic.SerdesImpl;
import org.o3project.mlo.server.impl.logic.SliceManager;
import org.o3project.mlo.server.impl.rpc.service.OdenOSAdapterServiceImpl;
import org.o3project.mlo.server.impl.rpc.service.OdenOSConfigImpl;
import org.o3project.mlo.server.impl.rpc.service.OdenosTask;
import org.o3project.mlo.server.impl.rpc.service.OdenosTaskDummy;
import org.o3project.mlo.server.impl.rpc.service.SdtncDeleteMethodImpl;
import org.o3project.mlo.server.impl.rpc.service.SdtncDtoOtherConfigImpl;
import org.o3project.mlo.server.impl.rpc.service.SdtncGetMethodImpl;
import org.o3project.mlo.server.impl.rpc.service.SdtncPostMethodImpl;
import org.o3project.mlo.server.impl.rpc.service.SdtncServiceImpl;
import org.o3project.mlo.server.logic.ApiCallException;
import org.o3project.mlo.server.logic.ConfigProvider;
import org.o3project.mlo.server.logic.DbAccessException;
import org.o3project.mlo.server.logic.InternalException;
import org.o3project.mlo.server.logic.MloException;
import org.o3project.mlo.server.logic.OtherException;
import org.o3project.mlo.server.logic.Serdes;
import org.o3project.mlo.server.logic.TimeOutException;
import org.o3project.mlo.server.logic.TopologyRepository;
import org.o3project.mlo.server.rpc.dto.SdtncRequestDto;
import org.o3project.mlo.server.rpc.dto.SdtncResponseDto;
import org.o3project.mlo.server.rpc.entity.PTFlowEntity;
import org.o3project.mlo.server.rpc.entity.PTLinkEntity;
import org.o3project.mlo.server.rpc.service.OdenOSDriver;
import org.o3project.mlo.server.rpc.service.OdenOSListener;
import org.o3project.mlo.server.rpc.service.SdtncInvoker;
import org.o3project.mlo.server.rpc.service.SdtncMethod;

/**
 * SliceManagerOtherImplTest
 *
 */
public class SliceManagerOtherImplTest {
	
	private static final String DATA_PATH = "src/test/resources/org/o3project/mlo/server/logic/data/other";

	private final Serdes serdes = new SerdesImpl();
	private SliceManager obj;
	private EquipmentConfiguratorOtherImpl equConf;
	private DummyDriver driver;
	private OdenosTaskDummy odenosTask;
	private OdenOSAdapterServiceImpl odenAdp;
	private EquipmentConfiguratorOptDeviceImpl equipmentConfiguratorOptDeviceImpl;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		File propFile = new File("src/test/resources/", "test.mlo-srv.properties");
		ConfigProviderImpl configProvider = ConfigProviderImplTest.createConfigProviderImpl(propFile.getAbsolutePath());
		
		TopologyRepository topologyRepositoryDefaultImpl = TopologyRepositoryDefaultImplTest.createObjLoaded(configProvider);
		
		DebugRestifChecker debugRestifChecker = new DebugRestifChecker();
		debugRestifChecker.setConfigProvider(configProvider);
		
		obj = new SliceManager();
		obj.setTopologyRepository(topologyRepositoryDefaultImpl);
		obj.setDebugRestifChecker(debugRestifChecker);
		equConf = new EquipmentConfiguratorOtherImpl();
		
		ConfigProvider configProvider2 = ConfigProviderImplTest.createConfigProviderImpl(null);
		
		// setup EquipmentConfigurator for opt device.
		driver = new DummyDriver();
		driver.setRequestLinkWait(0);
		odenosTask = new OdenosTaskDummy();
		odenAdp = createDefaultObj(odenosTask, driver);
		driver.setListener(odenAdp);
		odenAdp.setDriver(driver);
		OdenOSConfigImpl odenOSConfig = new OdenOSConfigImpl();
		odenOSConfig.setConfigProvider(configProvider2);
		equipmentConfiguratorOptDeviceImpl = new EquipmentConfiguratorOptDeviceImpl();
		equipmentConfiguratorOptDeviceImpl.setOdenOSAdapterService(odenAdp);
		equipmentConfiguratorOptDeviceImpl.setConfigProvider(configProvider2);
		equConf.setEquipmentConfiguratorOptDeviceImpl(equipmentConfiguratorOptDeviceImpl);
		
		SdtncServiceImpl sdtncSrv = new SdtncServiceImpl();
		DummyInvoker dummyInvoker = new DummyInvoker();
		sdtncSrv.setSdtncInvoker(dummyInvoker);
		SdtncGetMethodImpl sdtncGetMethod = new SdtncGetMethodImpl();
		SdtncDeleteMethodImpl sdtncDeleteMethod = new SdtncDeleteMethodImpl();
		SdtncPostMethodImpl sdtncPostMethod = new SdtncPostMethodImpl();
		sdtncSrv.setSdtncGetMethod(sdtncGetMethod);
		sdtncSrv.setSdtncDeleteMethod(sdtncDeleteMethod);
		sdtncSrv.setSdtncPostMethod(sdtncPostMethod);
		equConf.setSdtncService(sdtncSrv);
		
		SdtncDtoOtherConfigImpl dtoConf = new SdtncDtoOtherConfigImpl();
		dtoConf.setConfigProvider(configProvider2);
		equConf.setSdtncDtoOtherConfig(dtoConf);
		equConf.setTopologyRepository(topologyRepositoryDefaultImpl);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		obj = null;
		equConf = null;
		odenosTask = null;
		driver = null;
		odenAdp = null;
		equipmentConfiguratorOptDeviceImpl = null;
	}
	
	public static OdenOSAdapterServiceImpl createDefaultObj(OdenosTask odenosTask, OdenOSDriver driver) {
		OdenOSAdapterServiceImpl odenOSAdpObj = null;
		odenOSAdpObj = new OdenOSAdapterServiceImpl();
		
		odenOSAdpObj.setDriver(driver);
		
		ConfigProviderImpl configProvider = ConfigProviderImplTest.createConfigProviderImpl(null);
		OdenOSConfigImpl odenOSConfig = new OdenOSConfigImpl();
		odenOSConfig.setConfigProvider(configProvider);
		odenOSAdpObj.setOdenOSConfig(odenOSConfig);
		
		odenOSAdpObj.setOdenosTask(odenosTask);
		return odenOSAdpObj;
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.SliceManager#createSlice(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 */
	@Test
	public void testCreateSlice_n_001() throws Throwable {
		RestifRequestDto reqDto = null;
		RestifResponseDto resDto = null;
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.001.create.req.xml");
		resDto = obj.createSlice(reqDto, equConf);
		assertTrue(DtoTestUtils.isSameResAsXml(serdes, resDto, DATA_PATH, "dummy.n.001.create.res.xml"));
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.SliceManager#createSlice(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 */
	@Test
	public void testCreateSlice_a_already() throws Throwable {
		RestifRequestDto reqDto = null;
		RestifResponseDto resDto = null;
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.001.create.req.xml");
		resDto = obj.createSlice(reqDto, equConf);
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.001.create.req.xml");
		resDto = null;
		try {
			resDto = obj.createSlice(reqDto, equConf);
			fail();
		} catch (ApiCallException e) {
			assertNull(resDto);
			assertEquals("AlreadyCreated", e.getMessage());
			assertEquals("sliceA", e.getSliceName());
			assertEquals("00000001", e.getSliceId());
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.SliceManager#createSlice(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 */
	@Test
	public void testCreateSlice_a_NoFlow() throws Throwable {
		RestifRequestDto reqDto = null;
		RestifResponseDto resDto = null;
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.a.NoFlow.create.req.xml");
		try {
			resDto = obj.createSlice(reqDto, equConf);
			fail();
		} catch (OtherException e) {
			assertNull(resDto);
			assertEquals("NoResource/FlowName=flow2", e.getMessage());
			assertEquals("sliceA", e.getSliceName());
			assertEquals("1", e.getSliceId());
		} catch (ApiCallException e) {
			fail();
			assertNull(resDto);
			assertEquals("BadRequest/FlowNotFound/flow2", e.getMessage());
			assertEquals("sliceA", e.getSliceName());
			assertEquals("", e.getSliceId());
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.SliceManager#updateSlice(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 */
	@Test
	public void testUpdateSlice_add_n_001() throws Throwable {
		try{
		RestifRequestDto reqDto = null;
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.001.create.req.xml");
		obj.createSlice(reqDto, equConf);
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.001.update.add.req.xml");
		obj.updateSlice(reqDto, equConf);
		fail();
		}catch(ApiCallException e){
			assertTrue(e instanceof ApiCallException);
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.SliceManager#updateSlice(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 */
	@Test
	public void testUpdateSlice_mod_n_001() throws Throwable {
		RestifRequestDto reqDto = null;
		RestifResponseDto resDto = null;
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.001.create.req.xml");
		resDto = obj.createSlice(reqDto, equConf);
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.001.update.mod.req.xml");
		resDto = obj.updateSlice(reqDto, equConf);
		assertTrue(DtoTestUtils.isSameResAsXml(serdes, resDto, DATA_PATH, "dummy.n.001.update.mod.res.xml"));
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.SliceManager#updateSlice(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 */
	@Test
	public void testUpdateSlice_del_n_001() throws Throwable {
		RestifRequestDto reqDto = null;
		RestifResponseDto resDto = null;
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.001.create.req.xml");
		resDto = obj.createSlice(reqDto, equConf);
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.001.update.del.req.xml");
		resDto = obj.updateSlice(reqDto, equConf);
		assertTrue(DtoTestUtils.isSameResAsXml(serdes, resDto, DATA_PATH, "dummy.n.001.update.del.res.xml"));
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.SliceManager#deleteSlice(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 */
	@Test
	public void testDeleteSlice_n_001() throws Throwable {
		RestifRequestDto reqDto = null;
		RestifResponseDto resDto = null;
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.001.create.req.xml");
		resDto = obj.createSlice(reqDto, equConf);
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.001.delete.req.xml");
		resDto = obj.deleteSlice(reqDto, equConf);
		assertTrue(DtoTestUtils.isSameResAsXml(serdes, resDto, DATA_PATH, "dummy.n.001.delete.res.xml"));
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.SliceManager#deleteSlice(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 */
	@Test
	public void testDeleteSlice_a_already() throws Throwable {
		RestifRequestDto reqDto = null;
		RestifResponseDto resDto = null;
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.001.delete.req.xml");
		try {
			resDto = obj.deleteSlice(reqDto, equConf);
			fail();
		} catch (ApiCallException e) {
			assertEquals(null, resDto);
			assertEquals("AlreadyDeleted", e.getMessage());
			assertEquals(null, e.getSliceId());
			assertEquals(null, e.getSliceName());
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.SliceManager#readSlice(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 */
	@Test
	public void testReadSlice_n_001() throws Throwable {
		RestifRequestDto reqDto = null;
		RestifResponseDto resDto = null;
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.001.create.req.xml");
		resDto = obj.createSlice(reqDto, equConf);
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.001.read.req.xml");
		resDto = obj.readSlice(reqDto);
		assertTrue(DtoTestUtils.isSameResAsXml(serdes, resDto, DATA_PATH, "dummy.n.001.read.res.xml"));
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.SliceManager#updateSlice(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 */
	@Test
	public void testUpdateSlice_add_n_002() throws Throwable {
		RestifRequestDto reqDto = null;
		RestifResponseDto resDto = null;

		// create
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.002.create.req.xml");
		resDto = obj.createSlice(reqDto, equConf);
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.002.read.req.xml");
		resDto = obj.readSlice(reqDto);
		assertTrue(DtoTestUtils.isSameResAsXml(serdes, resDto, DATA_PATH, "dummy.n.002.create.read.res.xml"));

		// update.add
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.002.update.add.req.xml");
		resDto = obj.updateSlice(reqDto, equConf);
		assertTrue(DtoTestUtils.isSameResAsXml(serdes, resDto, DATA_PATH, "dummy.n.002.update.add.res.xml"));
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.002.read.req.xml");
		resDto = obj.readSlice(reqDto);
		assertTrue(DtoTestUtils.isSameResAsXml(serdes, resDto, DATA_PATH, "dummy.n.002.update.add.read.res.xml"));
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.SliceManager#updateSlice(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 */
	@Test
	public void testUpdateSlice_mod_n_002() throws Throwable {
		RestifRequestDto reqDto = null;
		RestifResponseDto resDto = null;
		
		// create 
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.002.create.req.xml");
		resDto = obj.createSlice(reqDto, equConf);
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.002.read.req.xml");
		resDto = obj.readSlice(reqDto);
		assertTrue(DtoTestUtils.isSameResAsXml(serdes, resDto, DATA_PATH, "dummy.n.002.create.read.res.xml"));

		// update.mod
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.002.update.mod.req.xml");
		resDto = obj.updateSlice(reqDto, equConf);
		assertTrue(DtoTestUtils.isSameResAsXml(serdes, resDto, DATA_PATH, "dummy.n.002.update.mod.res.xml"));
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.002.update.mod.read.req.xml");
		resDto = obj.readSlice(reqDto);
		assertTrue(DtoTestUtils.isSameResAsXml(serdes, resDto, DATA_PATH, "dummy.n.002.update.mod.read.res.xml"));
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.SliceManager#updateSlice(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 */
	@Test
	public void testOperateSlice_mod_n_003() throws Throwable {
		RestifRequestDto reqDto = null;
		RestifResponseDto resDto = null;
		
		// create
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.003.01.create.req.xml");
		resDto = obj.createSlice(reqDto, equConf);
		assertTrue(DtoTestUtils.isSameResAsXml(serdes, resDto, DATA_PATH, "dummy.n.003.01.create.res.xml"));
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.003.read.00000001.req.xml");
		resDto = obj.readSlice(reqDto);
		assertTrue(DtoTestUtils.isSameResAsXml(serdes, resDto, DATA_PATH, "dummy.n.003.02.read.00000001.res.xml"));
		
		// update.add
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.003.03.update.add.req.xml");
		resDto = obj.updateSlice(reqDto, equConf);
		assertTrue(DtoTestUtils.isSameResAsXml(serdes, resDto, DATA_PATH, "dummy.n.003.03.update.add.res.xml"));
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.003.read.00000001.req.xml");
		resDto = obj.readSlice(reqDto);
		assertTrue(DtoTestUtils.isSameResAsXml(serdes, resDto, DATA_PATH, "dummy.n.003.04.read.00000001.res.xml"));
		
		// create
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.003.05.create.req.xml");
		resDto = obj.createSlice(reqDto, equConf);
		assertTrue(DtoTestUtils.isSameResAsXml(serdes, resDto, DATA_PATH, "dummy.n.003.05.create.res.xml"));
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.003.read.00000002.req.xml");
		resDto = obj.readSlice(reqDto);
		assertTrue(DtoTestUtils.isSameResAsXml(serdes, resDto, DATA_PATH, "dummy.n.003.06.read.00000002.res.xml"));
		
		// update.mod
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.003.07.update.mod.req.xml");
		resDto = obj.updateSlice(reqDto, equConf);
		assertTrue(DtoTestUtils.isSameResAsXml(serdes, resDto, DATA_PATH, "dummy.n.003.07.update.mod.res.xml"));
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.003.read.00000001.req.xml");
		resDto = obj.readSlice(reqDto);
		assertTrue(DtoTestUtils.isSameResAsXml(serdes, resDto, DATA_PATH, "dummy.n.003.08.read.00000001.res.xml"));
		
		// update.del
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.003.09.update.del.req.xml");
		resDto = obj.updateSlice(reqDto, equConf);
		assertTrue(DtoTestUtils.isSameResAsXml(serdes, resDto, DATA_PATH, "dummy.n.003.09.update.del.res.xml"));
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.003.read.00000001.req.xml");
		resDto = obj.readSlice(reqDto);
		assertTrue(DtoTestUtils.isSameResAsXml(serdes, resDto, DATA_PATH, "dummy.n.003.10.read.00000001.res.xml"));
		
		// delete
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.003.11.delete.req.xml");
		resDto = obj.deleteSlice(reqDto, equConf);
		assertTrue(DtoTestUtils.isSameResAsXml(serdes, resDto, DATA_PATH, "dummy.n.003.11.delete.res.xml"));
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.003.read.00000001.req.xml");
		try {
			resDto = null;
			resDto = obj.readSlice(reqDto);
		} catch (ApiCallException e) {
			assertEquals(null, resDto);
			assertEquals("NoData", e.getMessage());
		}
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.003.read.00000002.req.xml");
		resDto = obj.readSlice(reqDto);
		assertTrue(DtoTestUtils.isSameResAsXml(serdes, resDto, DATA_PATH, "dummy.n.003.12.read.00000002.res.xml"));
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.SliceManager#createSlice(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 */
	@Test
	public void testCreateSlice_a_CreateAPICallError() throws Throwable {
		RestifRequestDto reqDto = null;
		RestifResponseDto resDto = null;
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.a.CreateAPICallError.create.req.xml");
		try {
			resDto = obj.createSlice(reqDto, equConf);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof ApiCallException);
			assertEquals(null, resDto);
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.SliceManager#deleteSlice(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 */
	@Test
	public void testDeleteSlice_a_DeleteInternalError() throws Throwable {
		RestifRequestDto reqDto = null;
		RestifResponseDto resDto = null;
		
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.a.DeleteInternalError.create.req.xml");
		resDto = obj.createSlice(reqDto, equConf);
		
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.001.delete.req.xml");
		resDto = null;
		try {
			resDto = obj.deleteSlice(reqDto, equConf);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof InternalException);
			assertEquals(null, resDto);
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.SliceManager#createSlice(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 */
	@Test
	public void testCreateSlice_a_DebugOff_CreateAPICallError() throws Throwable {
		RestifRequestDto reqDto = null;
		RestifResponseDto resDto = null;

		setDebugOff();
		
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.a.CreateAPICallError.create.req.xml");
		resDto = obj.createSlice(reqDto, equConf);
		assertNotNull(resDto);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.SliceManager#createSlice(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 */
	@Test
	public void testCreateSlice_a_BeforeCreateToggledTimeOutError() throws Throwable {
		RestifRequestDto reqDto = null;
		RestifResponseDto resDto = null;
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.a.BeforeCreateToggledTimeOutError.create.req.xml");
		try {
			resDto = obj.createSlice(reqDto, equConf);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof TimeOutException);
			assertEquals(null, resDto);
		}
		
		resDto = obj.createSlice(reqDto, equConf);
		assertNotNull(resDto);
		
		resDto = null;
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.001.delete.req.xml");
		resDto = obj.deleteSlice(reqDto, equConf);
		assertNotNull(resDto);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.SliceManager#createSlice(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 */
	@Test
	public void testCreateSlice_a_DebugOff_BeforeCreateToggledTimeOutError() throws Throwable {
		RestifRequestDto reqDto = null;
		RestifResponseDto resDto = null;

		setDebugOff();

		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.a.BeforeCreateToggledTimeOutError.create.req.xml");
		
		resDto = obj.createSlice(reqDto, equConf);
		assertNotNull(resDto);
		
		resDto = null;
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.001.delete.req.xml");
		resDto = obj.deleteSlice(reqDto, equConf);
		assertNotNull(resDto);
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.SliceManager#createSlice(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 */
	@Test
	public void testCreateSlice_a_AfterDeleteToggledDBAccessError() throws Throwable {
		RestifRequestDto reqDto = null;
		RestifResponseDto resDto = null;
		
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.a.AfterDeleteToggledDBAccessError.create.req.xml");
		resDto = obj.createSlice(reqDto, equConf);
		assertNotNull(resDto);

		resDto = null;
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.001.delete.req.xml");
		try {
			resDto = obj.deleteSlice(reqDto, equConf);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof DbAccessException);
			assertEquals(null, resDto);
		}

		resDto = null;
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.001.delete.req.xml");
		try {
			resDto = obj.deleteSlice(reqDto, equConf);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof ApiCallException);
			assertEquals(null, resDto);
			assertEquals("AlreadyDeleted", e.getMessage());
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.SliceManager#createSlice(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 */
	@Test
	public void testCreateSlice_a_DebugOff_AfterDeleteToggledDBAccessError() throws Throwable {
		RestifRequestDto reqDto = null;
		RestifResponseDto resDto = null;

		setDebugOff();
		
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.a.AfterDeleteToggledDBAccessError.create.req.xml");
		resDto = obj.createSlice(reqDto, equConf);
		assertNotNull(resDto);

		resDto = null;
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.001.delete.req.xml");
		resDto = obj.deleteSlice(reqDto, equConf);
		assertNotNull(resDto);

		resDto = null;
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.001.delete.req.xml");
		try {
			resDto = obj.deleteSlice(reqDto, equConf);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof ApiCallException);
			assertEquals(null, resDto);
			assertEquals("AlreadyDeleted", e.getMessage());
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.SliceManager#updateSlice(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 */
	@Test
	public void testUpdateSlice_unknow() throws Throwable {
		
		StringWriter writer = new StringWriter();
		WriterAppender appender = new WriterAppender(new PatternLayout("%p, %m%n"),writer);
		getLogger().addAppender(appender);
		getLogger().setAdditivity(false);
		
		RestifRequestDto reqDto = null;
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.001.create.req.xml");
		obj.createSlice(reqDto, equConf);
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.001.update.unknow.req.xml");
		obj.updateSlice(reqDto, equConf);
		String logString = writer.toString();
		assertTrue(logString.contains("Unexpected reqFlow.type, which is ignored"));
	}

	/**
	 * @return
	 */
	private static Logger getLogger() {
		return LogManager.getLogger(SliceManager.class);
	}
	
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.SliceManager#updateSlice(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 */
	@Test
	public void testUpdateSlice_update_mod_fail() throws Throwable {
		
		RestifRequestDto reqDto = null;
		RestifResponseDto resDto = null;
		try{
			reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.001.create.req.xml");
			resDto = obj.createSlice(reqDto, equConf);
			reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.001.update.mod.fail.req.xml");
			resDto = obj.updateSlice(reqDto, equConf);
		}catch(Exception e){
			assertTrue(e instanceof ApiCallException);
			assertEquals(e.getMessage(),"vlanId is different  [srcCENodeName=tokyo, srcCEPortNo=00000001, dstCENodeName=akashi, dstCEPortNo=00000005]");
		}
		
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.001.read.req.xml");
		resDto = obj.readSlice(reqDto);
		assertTrue(DtoTestUtils.isSameResAsXml(serdes, resDto, DATA_PATH, "dummy.n.001.read.res.xml"));
		
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.001.delete.req.xml");
		resDto = obj.deleteSlice(reqDto, equConf);
		assertTrue(DtoTestUtils.isSameResAsXml(serdes, resDto, DATA_PATH, "dummy.n.001.delete.res.xml"));
		
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.SliceManager#updateSlice(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 */
	@Test
	public void testUpdateSlice_update_add_fail() throws Throwable {
		
		RestifRequestDto reqDto = null;
		RestifResponseDto resDto = null;
		try{
			reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.001.create.req.xml");
			resDto = obj.createSlice(reqDto, equConf);
			reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.001.update.add.fail.req.xml");
			resDto = obj.updateSlice(reqDto, equConf);
		}catch(Exception e){
			assertTrue(e instanceof ApiCallException);
			assertEquals(e.getMessage(),"vlanId is different  [srcCENodeName=tokyo, srcCEPortNo=00000001, dstCENodeName=akashi, dstCEPortNo=00000005]");
		}
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.001.read.req.xml");
		resDto = obj.readSlice(reqDto);
		assertTrue(DtoTestUtils.isSameResAsXml(serdes, resDto, DATA_PATH, "dummy.n.001.read.res.xml"));
		
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.001.delete.req.xml");
		resDto = obj.deleteSlice(reqDto, equConf);
		assertTrue(DtoTestUtils.isSameResAsXml(serdes, resDto, DATA_PATH, "dummy.n.001.delete.res.xml"));
	}
	
	/**
	 * Sets debug.restif.enable to false.
	 */
	private void setDebugOff() {
		// debug.restif.enable = false
		// In this case, dummy exception must not occur.
		ConfigProviderImpl configProvider = ConfigProviderImplTest.createConfigProviderImpl((new File(DATA_PATH, "mlo-srv.debug-off.properties")).getAbsolutePath());
		DebugRestifChecker debugRestifChecker = new DebugRestifChecker();
		debugRestifChecker.setConfigProvider(configProvider);
		obj.setDebugRestifChecker(debugRestifChecker);
	}
	
	// Dummy Invoker for test.
	private class DummyInvoker implements SdtncInvoker {

		private static final String PATH_LOGIN = "login";
		private static final String PATH_LOGOUT = "logout";
		private static final String PATH_LINK = "link";
		private static final String PATH_PATH = "path";
		private static final String TEMP_PATH = "src/test/resources/org/o3project/mlo/server/logic/data/base";
		@Override
		public SdtncResponseDto invoke(SdtncMethod method,
				SdtncRequestDto reqDto, String path, Map<String, String> params)
				throws MloException {
			
			SdtncResponseDto resDto = new SdtncResponseDto();
			if("POST".equals(method.getName())){
				if(PATH_LOGIN.equals(path)){
					File xmlFile = new File(TEMP_PATH, "login.res.xml");
					resDto = createDto(xmlFile);
				}else if(PATH_LOGOUT.equals(path)){
					File xmlFile = new File(TEMP_PATH, "logout.res.xml");
					resDto = createDto(xmlFile);
				}else if(PATH_PATH.equals(path)){
					File xmlFile = new File(TEMP_PATH, "createVpath.res.xml");
					resDto = createDto(xmlFile);
				}
			}else if ("GET".equals(method.getName())){
				if(PATH_LINK.equals(path)){
					File xmlFile = new File(TEMP_PATH, "MultVLink.res.xml");
					resDto = createDto(xmlFile);
				}else if(PATH_PATH.equals(path)){
					File xmlFile = new File(TEMP_PATH, "Vpath.res.xml");
					resDto = createDto(xmlFile);
				}
			}else if ("DELETE".equals(method.getName())){
				if(PATH_PATH.equals(path)){
					File xmlFile = new File(TEMP_PATH, "delVpath.res.xml");
					resDto = createDto(xmlFile);
				}
			}
			
			return resDto;
		}
		
		private SdtncResponseDto createDto(File xmlFile) throws MloException{
			SdtncResponseDto resDto = null;
			InputStream istream = null;
			try {
				istream = new FileInputStream(xmlFile);
				resDto = JAXB.unmarshal(istream, SdtncResponseDto.class);
			} catch (FileNotFoundException e) {
				// Never pass here.
				throw new InternalException("templete file is not found [file = " + xmlFile.getName() +"]");
			} finally {
				if (istream != null) {
					try {
						istream.close();
					} catch (IOException e) {
						resDto = null;
						e.printStackTrace();
					}
				}
			}
			
			return resDto;
		}
	}
	
	
	// Dummy driver for test.
	public class DummyDriver implements OdenOSDriver {

		private PTLinkEntity getLinkResponse = null;
		@SuppressWarnings("unused")
		private int requestLinkWait = 0;
		private int deleteFlowWait = 0;
		private int putFlowWait = 0;
		private OdenOSListener listener = null;
		
		/* (non-Javadoc)
		 * @see org.o3project.mlo.server.rpc.service.IOdenOSDriver#getLink(java.lang.String)
		 */
		@Override
		public PTLinkEntity getLink(String linkId) throws Exception {
			return getLinkResponse;
		}

		/* (non-Javadoc)
		 * @see org.o3project.mlo.server.rpc.service.IOdenOSDriver#requestLink(org.o3project.mlo.server.rpc.entity.PTLinkEntity)
		 */
		@Override
		public void requestLink(PTLinkEntity entity) throws Exception {
			listener.notifyLinkChanged(entity.linkId);
		}

		public void setGetLinkResponse(PTLinkEntity getLinkResponse) {
			this.getLinkResponse = getLinkResponse;
		}

		public void setRequestLinkWait(int requestLinkWait) {
			this.requestLinkWait = requestLinkWait;
		}

		public void setDeleteFlowWait(int deleteFlowWait) {
			this.deleteFlowWait = deleteFlowWait;
		}

		public void setListener(OdenOSListener listener) {
			this.listener = listener;
		}

		@Override
		public void deleteFlow(String odenosFlowId) throws Exception {
			if (deleteFlowWait < 0) {
				throw new InternalException("test");
			}
			listener.notifyFlowDeleted(odenosFlowId);
		}

		/* (non-Javadoc)
		 * @see org.o3project.mlo.server.rpc.service.IOdenOSDriver#putFlow(org.o3project.mlo.server.rpc.entity.PTFlowEntity)
		 */
		@Override
		public void putFlow(PTFlowEntity entity)
				throws Exception {
			if (putFlowWait < 0) {
				throw new InternalException("test");
			}
			listener.notifyFlowChanged(entity.flowId);
		}

	}
}
