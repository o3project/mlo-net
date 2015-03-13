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
import org.o3project.mlo.server.dto.SliceDto;
import org.o3project.mlo.server.impl.logic.CommonUseSliceValidator;
import org.o3project.mlo.server.logic.ApiCallException;

/**
 * XmlIdAdapterTest
 *
 */
public class CommonUseSliceValidatorTest {
	
	private CommonUseSliceValidator obj = new CommonUseSliceValidator();

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
	public void testValivate_正常_common_Version_255() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		dto.common.version = 255;
		dto.slice = new SliceDto();
		dto.slice.id = 1;
		
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
	public void testValivate_異常_common_Version_0() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		dto.common.version = 0;
		dto.slice = new SliceDto();
		dto.slice.id = 1;
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/Version");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_common_Version_null() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		dto.common.version = null;
		dto.slice = new SliceDto();
		dto.slice.id = 1;
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/Version");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_common_SourceComponent_null() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		dto.common.srcComponent = null;
		dto.slice = new SliceDto();
		dto.slice.id = 1;
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/SourceComponent");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_common_SourceComponent_ComponentName_33桁() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		dto.common.srcComponent.name = "ComponentName__________________EE";
		dto.slice = new SliceDto();
		dto.slice.id = 1;
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/SourceComponent_ComponentName");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_common_SourceComponent_ComponentName_空データ() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		dto.common.srcComponent.name = "";
		dto.slice = new SliceDto();
		dto.slice.id = 1;
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/SourceComponent_ComponentName");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_common_SourceComponent_ComponentName_null() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		dto.common.srcComponent.name = null;
		dto.slice = new SliceDto();
		dto.slice.id = 1;
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/SourceComponent_ComponentName");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_common_DestComponent_null() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		dto.common.dstComponent = null;
		dto.slice = new SliceDto();
		dto.slice.id = 1;
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/DestComponent");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_common_DestComponent_ComponentName_33桁() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		dto.common.dstComponent.name = "ComponentName__________________EE";
		dto.slice = new SliceDto();
		dto.slice.id = 1;
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/DestComponent_ComponentName");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_common_DestComponent_ComponentName_空データ() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		dto.common.dstComponent.name = "";
		dto.slice = new SliceDto();
		dto.slice.id = 1;
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/DestComponent_ComponentName");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_正常_common_Operation_Responce() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		dto.common.operation = "Responce";
		dto.slice = new SliceDto();
		dto.slice.id = 1;
		
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
	public void testValivate_異常_common_Operation_不正値() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		dto.common.operation = "request";
		dto.slice = new SliceDto();
		dto.slice.id = 1;
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/Operation");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_common_Operation_空データ() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		dto.common.operation = "";
		dto.slice = new SliceDto();
		dto.slice.id = 1;
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/Operation");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_common_Operation_null() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		dto.common.operation = null;
		dto.slice = new SliceDto();
		dto.slice.id = 1;
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/Operation");
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.Impl.logic.CreateSliceValidator#validate(org.o3project.mlo.server.dto.RestifRequestDto)}.
	 * @throws Exception 
	 */
	@Test
	public void testValivate_異常_common_DestComponent_ComponentName_null() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		dto.common.dstComponent.name = null;
		dto.slice = new SliceDto();
		dto.slice.id = 1;
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/DestComponent_ComponentName");
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
		dto.slice = new SliceDto();
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
		dto.slice = new SliceDto();
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
		dto.slice = new SliceDto();
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
		dto.slice = new SliceDto();
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
		dto.slice = new SliceDto();
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
	public void testValivate_異常_Slice_null() throws Exception {
		
		RestifRequestDto dto = new RestifRequestDto();
		
		DtoTestUtils.setNormalCommonDto(dto);
		dto.slice = null;
		
		try {
			obj.validate(dto);
			fail("異常終了");
		} catch (ApiCallException e) {
			e.printStackTrace();
			assertEquals(e.getMessage(), "BadRequest/InvalidData/SliceIsUndefined");
		}
	}
}
