/**
 * CreateSliceValidatorTest.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import static org.junit.Assert.*;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.dto.RestifRequestDto;
import org.o3project.mlo.server.impl.logic.UpdateSliceValidator;
import org.o3project.mlo.server.logic.ApiCallException;

/**
 * XmlIdAdapterTest
 *
 */
public class UpdateSliceValidatorTest {
	
	private UpdateSliceValidator obj = new UpdateSliceValidator();

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Slice_Null() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/SliceIsUndefined");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Common_Null() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/CommonIsUndefined");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_正常_SliceId_99999999() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.id = 99999999;
		
		try {
			obj.validate(dto);
		} catch (ApiCallException e) {
			e.printStackTrace();
			fail("異常終了");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_正常_SliceId_0() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.id = 0;
		
		try {
			obj.validate(dto);
		} catch (ApiCallException e) {
			e.printStackTrace();
			fail("異常終了");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_SliceId_100000000() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.id = 100000000;
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/SliceId");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_SliceId_マイナス1() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.id = -1;
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/SliceId");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_SliceId_null() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.id = null;
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/SliceId");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_正常_Flows_1件() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		
		try {
			obj.validate(dto);
			System.out.println("【正常終了】" + Thread.currentThread().getStackTrace()[1].getMethodName());
		} catch (ApiCallException e) {
			e.printStackTrace();
			fail("異常終了");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_正常_Flows_200件() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		
		try {
			obj.validate(dto);
			System.out.println("【正常終了】" + Thread.currentThread().getStackTrace()[1].getMethodName());
		} catch (ApiCallException e) {
			e.printStackTrace();
			fail("異常終了");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Flows_0件() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 0);
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/FlowsSizeOver");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Flows_201件() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 201);
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/FlowsSizeOver");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Flows_Null() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows = null;
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/FlowsIsUndefined");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_正常_Flow_Add() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).id = null;
		
		try {
			obj.validate(dto);
		} catch (ApiCallException e) {
			e.printStackTrace();
			fail("異常終了");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_正常_Flow_Mod() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).type = "mod";
		
		try {
			obj.validate(dto);
		} catch (ApiCallException e) {
			e.printStackTrace();
			fail("異常終了");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_正常_Flow_Del() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).type = "del";
		dto.slice.flows.get(0).name = null;
		dto.slice.flows.get(0).srcCENodeName = null;
		dto.slice.flows.get(0).srcCEPortNo = null;
		dto.slice.flows.get(0).dstCENodeName = null;
		dto.slice.flows.get(0).dstCEPortNo = null;
		dto.slice.flows.get(0).reqBandWidth = null;
		dto.slice.flows.get(0).reqDelay = null;
		dto.slice.flows.get(0).protectionLevel = null;
		
		try {
			obj.validate(dto);
		} catch (ApiCallException e) {
			e.printStackTrace();
			fail("異常終了");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Add_FlowName_33桁() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).name = "TestFlowName___________________EE";
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/FlowName");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Add_FlowName_空データ() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).name = "";
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/FlowName");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Add_FlowName_null() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).name = null;
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/FlowName");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Add_SourceCENodeName_33桁() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).srcCENodeName = "TestSourceCENodeName___________EE";
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/SourceCENodeName");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Add_SourceCENodeName_空データ() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).srcCENodeName = "";
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/SourceCENodeName");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Add_SourceCENodeName_null() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).srcCENodeName = null;
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/SourceCENodeName");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Add_SourceCEPortNo_9桁() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).srcCEPortNo = "123456789";
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/SourceCEPortNo");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	//@Test
	public void testValivate_異常_Add_SourceCEPortNo_1桁() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).srcCEPortNo = "1";
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/SourceCEPortNo");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Add_SourceCEPortNo_空データ() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).srcCEPortNo = "";
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/SourceCEPortNo");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Add_SourceCEPortNo_null() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).srcCEPortNo = "";
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/SourceCEPortNo");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Add_DestCENodeName_33桁() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).dstCENodeName = "TestDestCENodeName_____________EE";
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/DestCENodeName");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Add_DestCENodeName_空データ() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).dstCENodeName = "";
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/DestCENodeName");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Add_DestCENodeName_null() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).dstCENodeName = null;
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/DestCENodeName");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Add_DestCEPortNo_9桁() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).dstCEPortNo = "123456789";
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/DestCEPortNo");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	//@Test
	public void testValivate_異常_Add_DestCEPortNo_1桁() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).dstCEPortNo = "1";
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/DestCEPortNo");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Add_DestCEPortNo_空データ() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).dstCEPortNo = "";
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/DestCEPortNo");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Add_DestCEPortNo_null() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).dstCEPortNo = "";
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/DestCEPortNo");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_正常_Add_RequestBandWidth_99999999() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).reqBandWidth = 99999999;
		
		try {
			obj.validate(dto);
		} catch (ApiCallException e) {
			e.printStackTrace();
			fail("異常終了");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_正常_Add_RequestBandWidth_0() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).reqBandWidth = 0;
		
		try {
			obj.validate(dto);
		} catch (ApiCallException e) {
			e.printStackTrace();
			fail("異常終了");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Add_RequestBandWidth_100000000() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).reqBandWidth = 100000000;
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/RequestBandWidth");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Add_RequestBandWidth_マイナス1() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).reqBandWidth = -1;
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/RequestBandWidth");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Add_RequestBandWidth_null() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).reqBandWidth = null;
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/RequestBandWidth");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_正常_Add_RequestDelay_9999() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).reqDelay = 9999;
		
		try {
			obj.validate(dto);
		} catch (ApiCallException e) {
			e.printStackTrace();
			fail("異常終了");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_正常_Add_RequestDelay_0() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).reqDelay = 0;
		
		try {
			obj.validate(dto);
		} catch (ApiCallException e) {
			e.printStackTrace();
			fail("異常終了");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Add_RequestDelay_100000000() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).reqDelay = 10000;
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/RequestDelay");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Add_RequestDelay_マイナス1() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).reqDelay = -1;
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/RequestDelay");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Add_RequestDelay_null() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).reqDelay = null;
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/RequestDelay");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Add_ProtectionLevel_2() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).protectionLevel = "2";
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/ProtectionLevel");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_正常_Add_ProtectionLevel_1() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).protectionLevel = "1";
		
		try {
			obj.validate(dto);
		} catch (ApiCallException e) {
			e.printStackTrace();
			fail("異常終了");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_正常_Add_ProtectionLevel_0() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).protectionLevel = "0";
		
		try {
			obj.validate(dto);
		} catch (ApiCallException e) {
			e.printStackTrace();
			fail("異常終了");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Add_ProtectionLevel_null() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).protectionLevel = null;
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/ProtectionLevel");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_正常_Mod_FlowId_99999999() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).type = "mod";
		dto.slice.flows.get(0).id = 99999999;
		
		try {
			obj.validate(dto);
		} catch (ApiCallException e) {
			e.printStackTrace();
			fail("異常終了");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_正常_Mod_FlowId_0() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).type = "mod";
		dto.slice.flows.get(0).id = 0;
		
		try {
			obj.validate(dto);
		} catch (ApiCallException e) {
			e.printStackTrace();
			fail("異常終了");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Mod_FlowId_100000000() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).type = "mod";
		dto.slice.flows.get(0).id = 100000000;
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/FlowId");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Mod_FlowId_マイナス1() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).type = "mod";
		dto.slice.flows.get(0).id = -1;
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/FlowId");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Mod_FlowId_null() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).type = "mod";
		dto.slice.flows.get(0).id = null;
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/FlowId");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Mod_FlowName_33桁() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).type = "mod";
		dto.slice.flows.get(0).name = "TestFlowName___________________EE";
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/FlowName");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Mod_FlowName_空データ() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).type = "mod";
		dto.slice.flows.get(0).name = "";
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/FlowName");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Mod_FlowName_null() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).type = "mod";
		dto.slice.flows.get(0).name = null;
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/FlowName");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Mod_SourceCENodeName_33桁() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).type = "mod";
		dto.slice.flows.get(0).srcCENodeName = "TestSourceCENodeName___________EE";
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/SourceCENodeName");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Mod_SourceCENodeName_空データ() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).type = "mod";
		dto.slice.flows.get(0).srcCENodeName = "";
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/SourceCENodeName");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Mod_SourceCENodeName_null() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).type = "mod";
		dto.slice.flows.get(0).srcCENodeName = null;
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/SourceCENodeName");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Mod_SourceCEPortNo_9桁() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).type = "mod";
		dto.slice.flows.get(0).srcCEPortNo = "123456789";
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/SourceCEPortNo");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	//@Test
	public void testValivate_異常_Mod_SourceCEPortNo_1桁() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).type = "mod";
		dto.slice.flows.get(0).srcCEPortNo = "1";
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/SourceCEPortNo");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Mod_SourceCEPortNo_空データ() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).type = "mod";
		dto.slice.flows.get(0).srcCEPortNo = "";
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/SourceCEPortNo");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Mod_SourceCEPortNo_null() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).type = "mod";
		dto.slice.flows.get(0).srcCEPortNo = "";
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/SourceCEPortNo");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Mod_DestCENodeName_33桁() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).type = "mod";
		dto.slice.flows.get(0).dstCENodeName = "TestDestCENodeName_____________EE";
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/DestCENodeName");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Mod_DestCENodeName_空データ() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).type = "mod";
		dto.slice.flows.get(0).dstCENodeName = "";
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/DestCENodeName");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Mod_DestCENodeName_null() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).type = "mod";
		dto.slice.flows.get(0).dstCENodeName = null;
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/DestCENodeName");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Mod_DestCEPortNo_9桁() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).type = "mod";
		dto.slice.flows.get(0).dstCEPortNo = "123456789";
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/DestCEPortNo");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	//@Test
	public void testValivate_異常_Mod_DestCEPortNo_1桁() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).type = "mod";
		dto.slice.flows.get(0).dstCEPortNo = "1";
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/DestCEPortNo");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Mod_DestCEPortNo_空データ() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).type = "mod";
		dto.slice.flows.get(0).dstCEPortNo = "";
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/DestCEPortNo");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Mod_DestCEPortNo_null() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).type = "mod";
		dto.slice.flows.get(0).dstCEPortNo = "";
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/DestCEPortNo");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_正常_Mod_RequestBandWidth_99999999() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).type = "mod";
		dto.slice.flows.get(0).reqBandWidth = 99999999;
		
		try {
			obj.validate(dto);
		} catch (ApiCallException e) {
			e.printStackTrace();
			fail("異常終了");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_正常_Mod_RequestBandWidth_0() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).type = "mod";
		dto.slice.flows.get(0).reqBandWidth = 0;
		
		try {
			obj.validate(dto);
		} catch (ApiCallException e) {
			e.printStackTrace();
			fail("異常終了");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Mod_RequestBandWidth_100000000() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).type = "mod";
		dto.slice.flows.get(0).reqBandWidth = 100000000;
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/RequestBandWidth");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Mod_RequestBandWidth_マイナス1() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).type = "mod";
		dto.slice.flows.get(0).reqBandWidth = -1;
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/RequestBandWidth");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Mod_RequestBandWidth_null() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).type = "mod";
		dto.slice.flows.get(0).reqBandWidth = null;
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/RequestBandWidth");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_正常_Mod_RequestDelay_9999() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).type = "mod";
		dto.slice.flows.get(0).reqDelay = 9999;
		
		try {
			obj.validate(dto);
		} catch (ApiCallException e) {
			e.printStackTrace();
			fail("異常終了");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_正常_Mod_RequestDelay_0() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).type = "mod";
		dto.slice.flows.get(0).reqDelay = 0;
		
		try {
			obj.validate(dto);
		} catch (ApiCallException e) {
			e.printStackTrace();
			fail("異常終了");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Mod_RequestDelay_100000000() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).type = "mod";
		dto.slice.flows.get(0).reqDelay = 10000;
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/RequestDelay");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Mod_RequestDelay_マイナス1() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).type = "mod";
		dto.slice.flows.get(0).reqDelay = -1;
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/RequestDelay");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Mod_RequestDelay_null() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).type = "mod";
		dto.slice.flows.get(0).reqDelay = null;
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/RequestDelay");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Mod_ProtectionLevel_2() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).type = "mod";
		dto.slice.flows.get(0).protectionLevel = "2";
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/ProtectionLevel");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Mod_ProtectionLevel_null() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).type = "mod";
		dto.slice.flows.get(0).protectionLevel = null;
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/ProtectionLevel");
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_正常_Del_FlowId_99999999() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).type = "del";
		dto.slice.flows.get(0).id = 99999999;
		
		try {
			obj.validate(dto);
		} catch (ApiCallException e) {
			e.printStackTrace();
			fail("異常終了");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_正常_Del_FlowId_0() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).type = "del";
		dto.slice.flows.get(0).id = 0;
		
		try {
			obj.validate(dto);
		} catch (ApiCallException e) {
			e.printStackTrace();
			fail("異常終了");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Del_FlowId_100000000() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).type = "del";
		dto.slice.flows.get(0).id = 100000000;
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/FlowId");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Del_FlowId_マイナス1() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).type = "del";
		dto.slice.flows.get(0).id = -1;
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/FlowId");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_Del_FlowId_null() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).type = "del";
		dto.slice.flows.get(0).id = null;
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/FlowId");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_RequestType() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		DtoTestUtils.setNormalSliceDto(dto, 1);
		dto.slice.flows.get(0).type = "";
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/RequestType");
		}
	}

}
