/**
 * SliceManagerImplTest.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.dto.RestifRequestDto;
import org.o3project.mlo.server.dto.RestifResponseDto;
import org.o3project.mlo.server.impl.logic.ConfigProviderImpl;
import org.o3project.mlo.server.impl.logic.DebugRestifChecker;
import org.o3project.mlo.server.impl.logic.EquipmentConfiguratorNullImpl;
import org.o3project.mlo.server.impl.logic.SerdesImpl;
import org.o3project.mlo.server.impl.logic.SliceManager;
import org.o3project.mlo.server.logic.ApiCallException;
import org.o3project.mlo.server.logic.DbAccessException;
import org.o3project.mlo.server.logic.InternalException;
import org.o3project.mlo.server.logic.MloException;
import org.o3project.mlo.server.logic.NbiConstants;
import org.o3project.mlo.server.logic.OtherException;
import org.o3project.mlo.server.logic.Serdes;
import org.o3project.mlo.server.logic.TimeOutException;
import org.o3project.mlo.server.logic.TopologyRepository;

/**
 * SliceManagerImplTest
 *
 */
public class SliceManagerImplTest implements NbiConstants {
	
	private static final String DATA_PATH = "src/test/resources/org/o3project/mlo/server/logic/data/hitachi";

	private final Serdes serdes = new SerdesImpl();
	private SliceManager obj;
	private EquipmentConfiguratorNullImpl equConf;
	
	
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
		equConf = new EquipmentConfiguratorNullImpl();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		obj = null;
		equConf = null;
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
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.SliceManager#updateSlice(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 */
	@Test
	public void testUpdateSlice_add_n_001() throws Throwable {
		RestifRequestDto reqDto = null;
		RestifResponseDto resDto = null;
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.001.create.req.xml");
		resDto = obj.createSlice(reqDto, equConf);
		reqDto = DtoTestUtils.readReqFromXml(serdes, DATA_PATH, "dummy.n.001.update.add.req.xml");
		resDto = obj.updateSlice(reqDto, equConf);
		assertTrue(DtoTestUtils.isSameResAsXml(serdes, resDto, DATA_PATH, "dummy.n.001.update.add.res.xml"));
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
	 * Test method for {@link org.o3project.mlo.server.impl.logic.SliceManager#updateSlice(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 */
	@Test
	public void testGetSlices_n_003() throws Throwable {
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

		// getSlices
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put(REQPARAM_KEY_OWNER, "demoApl");
		resDto = obj.getSlices(paramMap);
		assertTrue(DtoTestUtils.isSameResAsXml(serdes, resDto, DATA_PATH, "dummy.n.003.getSlices.res.xml"));
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
	 * debug.restif.enable を false に設定する。
	 */
	private void setDebugOff() {
		// debug.restif.enable = false
		// この場合はダミー例外が発生してはならない。
		ConfigProviderImpl configProvider = ConfigProviderImplTest.createConfigProviderImpl((new File(DATA_PATH, "mlo-srv.debug-off.properties")).getAbsolutePath());
		DebugRestifChecker debugRestifChecker = new DebugRestifChecker();
		debugRestifChecker.setConfigProvider(configProvider);
		obj.setDebugRestifChecker(debugRestifChecker);
	}
}
