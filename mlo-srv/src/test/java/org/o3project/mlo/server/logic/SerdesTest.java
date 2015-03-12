/**
 * RestifTest.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.logic;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.dto.FlowDto;
import org.o3project.mlo.server.dto.LinkInfoDto;
import org.o3project.mlo.server.dto.RestifCommonDto;
import org.o3project.mlo.server.dto.RestifComponentDto;
import org.o3project.mlo.server.dto.RestifErrorDto;
import org.o3project.mlo.server.dto.RestifRequestDto;
import org.o3project.mlo.server.dto.RestifResponseDto;
import org.o3project.mlo.server.dto.SliceDto;
import org.o3project.mlo.server.impl.logic.SerdesImpl;
import org.o3project.mlo.server.logic.Serdes;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * RestifTest
 *
 */
public class SerdesTest {
	
	private static final String DATA_PATH = "src/test/resources/org/o3project/mlo/server/logic/data";

	private Serdes serdes = new SerdesImpl();
	
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
	 * Test method for {@link org.o3project.mlo.server.impl.logic.Serdes#unmarshal(java.io.Reader)}.
	 * @throws Throwable 
	 */
	@Test
	public void testUnmarshal_create_001() throws Throwable {
		File xmlfile = new File(DATA_PATH, "serdes.unmarshal.create.001.xml");
		
		InputStream istream = null;
		RestifRequestDto reqDto = null;
		
		try {
			istream = new FileInputStream(xmlfile);
			
			reqDto = serdes.unmarshal(istream);
			
			assertNotNull(reqDto);
			assertNotNull(reqDto.common);
			assertEquals(Integer.valueOf(1), reqDto.common.version);
			assertEquals("Request", reqDto.common.operation);
			assertEquals("demoApl", reqDto.common.srcComponent.name);
			assertEquals("mlo", reqDto.common.dstComponent.name);
			assertEquals("sliceA", reqDto.slice.name);
			assertEquals(2, reqDto.slice.flows.size());
			assertEquals("flow1", reqDto.slice.flows.get(0).name);
			assertEquals("tokyo123", reqDto.slice.flows.get(0).srcCENodeName);
			assertEquals("00000001", reqDto.slice.flows.get(0).srcCEPortNo);
			assertEquals("osaka123", reqDto.slice.flows.get(0).dstCENodeName);
			assertEquals("00000002", reqDto.slice.flows.get(0).dstCEPortNo);
			assertEquals(Integer.valueOf(5000), reqDto.slice.flows.get(0).reqBandWidth);
			assertEquals(Integer.valueOf(5), reqDto.slice.flows.get(0).reqDelay);
			assertEquals("0", reqDto.slice.flows.get(0).protectionLevel);
			assertEquals("flow2", reqDto.slice.flows.get(1).name);
			assertEquals("nagoya123", reqDto.slice.flows.get(1).srcCENodeName);
			assertEquals("00000003", reqDto.slice.flows.get(1).srcCEPortNo);
			assertEquals("osaka555", reqDto.slice.flows.get(1).dstCENodeName);
			assertEquals("00000004", reqDto.slice.flows.get(1).dstCEPortNo);
			assertEquals(Integer.valueOf(1000), reqDto.slice.flows.get(1).reqBandWidth);
			assertEquals(Integer.valueOf(2), reqDto.slice.flows.get(1).reqDelay);
			assertEquals("0", reqDto.slice.flows.get(1).protectionLevel);
		} finally {
			if (istream != null) {
				istream.close();
			}
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.Serdes#unmarshal(java.io.Reader)}.
	 * @throws Throwable 
	 */
	@Test
	public void testUnmarshal_delete_001() throws Throwable {
		File xmlfile = new File(DATA_PATH, "serdes.unmarshal.delete.001.xml");
		
		InputStream istream = null;
		RestifRequestDto reqDto = null;
		
		try {
			istream = new FileInputStream(xmlfile);
			
			reqDto = serdes.unmarshal(istream);
			
			assertNotNull(reqDto);
			assertNotNull(reqDto.common);
			assertEquals(Integer.valueOf(1), reqDto.common.version);
			assertEquals("Request", reqDto.common.operation);
			assertEquals("demoApl", reqDto.common.srcComponent.name);
			assertEquals("mlo", reqDto.common.dstComponent.name);
			assertNotNull(reqDto.slice);
			assertNull(reqDto.slice.name);
			assertEquals(Integer.valueOf(1), reqDto.slice.id);
			assertNull(reqDto.slice.flows);
		} finally {
			if (istream != null) {
				istream.close();
			}
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.Serdes#unmarshal(java.io.Reader)}.
	 * @throws Throwable 
	 */
	@Test
	public void testUnmarshal_update_001() throws Throwable {
		File xmlfile = new File(DATA_PATH, "serdes.unmarshal.update.001.xml");
		
		InputStream istream = null;
		RestifRequestDto reqDto = null;
		
		try {
			istream = new FileInputStream(xmlfile);
			
			reqDto = serdes.unmarshal(istream);
			
			assertNotNull(reqDto);
			assertNotNull(reqDto.common);
			assertEquals(Integer.valueOf(1), reqDto.common.version);
			assertEquals("Request", reqDto.common.operation);
			assertEquals("demoApl", reqDto.common.srcComponent.name);
			assertEquals("mlo", reqDto.common.dstComponent.name);

			assertNotNull(reqDto.slice);
			assertNull(reqDto.slice.name);
			assertEquals(Integer.valueOf(1), reqDto.slice.id);
			assertEquals(1, reqDto.slice.flows.size());
			assertEquals("add", reqDto.slice.flows.get(0).type);
			assertEquals("flow100", reqDto.slice.flows.get(0).name);
			assertEquals("tokyo123", reqDto.slice.flows.get(0).srcCENodeName);
			assertEquals("00000001", reqDto.slice.flows.get(0).srcCEPortNo);
			assertEquals("osaka999", reqDto.slice.flows.get(0).dstCENodeName);
			assertEquals("00000002", reqDto.slice.flows.get(0).dstCEPortNo);
			assertEquals(Integer.valueOf(1000), reqDto.slice.flows.get(0).reqBandWidth);
			assertEquals(Integer.valueOf(1), reqDto.slice.flows.get(0).reqDelay);
			assertEquals("0", reqDto.slice.flows.get(0).protectionLevel);
		} finally {
			if (istream != null) {
				istream.close();
			}
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.Serdes#unmarshal(java.io.Reader)}.
	 * @throws Throwable 
	 */
	@Test
	public void testUnmarshal_update_002() throws Throwable {
		File xmlfile = new File(DATA_PATH, "serdes.unmarshal.update.002.xml");
		
		InputStream istream = null;
		RestifRequestDto reqDto = null;
		
		try {
			istream = new FileInputStream(xmlfile);
			
			reqDto = serdes.unmarshal(istream);
			
			assertNotNull(reqDto);
			assertNotNull(reqDto.common);
			assertEquals(Integer.valueOf(1), reqDto.common.version);
			assertEquals("Request", reqDto.common.operation);
			assertEquals("demoApl", reqDto.common.srcComponent.name);
			assertEquals("mlo", reqDto.common.dstComponent.name);

			assertNotNull(reqDto.slice);
			assertNull(reqDto.slice.name);
			assertEquals(Integer.valueOf(1), reqDto.slice.id);
			assertEquals(1, reqDto.slice.flows.size());
			assertEquals("mod", reqDto.slice.flows.get(0).type);
			assertEquals("flow200", reqDto.slice.flows.get(0).name);
			assertEquals(Integer.valueOf(2), reqDto.slice.flows.get(0).id);
			assertEquals("nagoya123", reqDto.slice.flows.get(0).srcCENodeName);
			assertEquals("00000003", reqDto.slice.flows.get(0).srcCEPortNo);
			assertEquals("osaka555", reqDto.slice.flows.get(0).dstCENodeName);
			assertEquals("00000004", reqDto.slice.flows.get(0).dstCEPortNo);
			assertEquals(Integer.valueOf(5000), reqDto.slice.flows.get(0).reqBandWidth);
			assertEquals(Integer.valueOf(1), reqDto.slice.flows.get(0).reqDelay);
			assertEquals("0", reqDto.slice.flows.get(0).protectionLevel);
		} finally {
			if (istream != null) {
				istream.close();
			}
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.Serdes#unmarshal(java.io.Reader)}.
	 * @throws Throwable 
	 */
	@Test
	public void testUnmarshal_update_003() throws Throwable {
		File xmlfile = new File(DATA_PATH, "serdes.unmarshal.update.003.xml");
		
		InputStream istream = null;
		RestifRequestDto reqDto = null;
		
		try {
			istream = new FileInputStream(xmlfile);
			
			reqDto = serdes.unmarshal(istream);
			
			assertNotNull(reqDto);
			assertNotNull(reqDto.common);
			assertEquals(Integer.valueOf(1), reqDto.common.version);
			assertEquals("Request", reqDto.common.operation);
			assertEquals("demoApl", reqDto.common.srcComponent.name);
			assertEquals("mlo", reqDto.common.dstComponent.name);

			assertNotNull(reqDto.slice);
			assertNull(reqDto.slice.name);
			assertEquals(Integer.valueOf(1), reqDto.slice.id);
			assertEquals(1, reqDto.slice.flows.size());
			assertEquals("del", reqDto.slice.flows.get(0).type);
			assertNull(reqDto.slice.flows.get(0).name);
			assertEquals(Integer.valueOf(2), reqDto.slice.flows.get(0).id);
			assertNull(reqDto.slice.flows.get(0).srcCENodeName);
			assertNull(reqDto.slice.flows.get(0).srcCEPortNo);
			assertNull(reqDto.slice.flows.get(0).dstCENodeName);
			assertNull(reqDto.slice.flows.get(0).dstCEPortNo);
			assertNull(reqDto.slice.flows.get(0).reqBandWidth);
			assertNull(reqDto.slice.flows.get(0).reqDelay);
			assertNull(reqDto.slice.flows.get(0).protectionLevel);
		} finally {
			if (istream != null) {
				istream.close();
			}
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.Serdes#unmarshal(java.io.Reader)}.
	 * @throws Throwable 
	 */
	@Test
	public void testUnmarshal_read_001() throws Throwable {
		File xmlfile = new File(DATA_PATH, "serdes.unmarshal.read.001.xml");
		
		InputStream istream = null;
		RestifRequestDto reqDto = null;
		
		try {
			istream = new FileInputStream(xmlfile);
			
			reqDto = serdes.unmarshal(istream);
			
			assertNotNull(reqDto);
			assertNotNull(reqDto.common);
			assertEquals(Integer.valueOf(1), reqDto.common.version);
			assertEquals("Request", reqDto.common.operation);
			assertEquals("demoApl", reqDto.common.srcComponent.name);
			assertEquals("mlo", reqDto.common.dstComponent.name);
			assertNotNull(reqDto.slice);
			assertNull(reqDto.slice.name);
			assertEquals(Integer.valueOf(1), reqDto.slice.id);
			assertNull(reqDto.slice.flows);
		} finally {
			if (istream != null) {
				istream.close();
			}
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.Serdes#marshal(org.o3project.mlo.server.dto.RestifResponseDto, java.io.Writer)}.
	 * @throws Throwable 
	 */
	@Test
	public void testMarshal_create_001() throws Throwable {
		RestifResponseDto resDto = new RestifResponseDto();
		resDto.common = createResponseCommonDto();
		resDto.error = null;
		resDto.slices = new ArrayList<SliceDto>();
		
		SliceDto slice = createSliceDto("sliceA", 00000001);
		slice.flows.add(createFlowDto("flow1", 00000001));
		slice.flows.add(createFlowDto("flow2", 00000002));
		resDto.slices.add(slice);
		
		ByteArrayOutputStream ostream = null;
		try {
			ostream = new ByteArrayOutputStream();
			serdes.marshal(resDto, ostream);
			String xml = new String(ostream.toByteArray(), "UTF-8");
		
			assertTrue(isEqualXml(xml, new File(DATA_PATH, "serdes.marshal.create.001.xml")));
		} finally {
			if (ostream != null) {
				ostream.close();
			}
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.Serdes#marshal(org.o3project.mlo.server.dto.RestifResponseDto, java.io.Writer)}.
	 * @throws Throwable 
	 */
	@Test
	public void testMarshal_create_002() throws Throwable {
		RestifResponseDto resDto = new RestifResponseDto();
		resDto.common = createResponseCommonDto();
		resDto.error = createRestifErrorDto("APICallError", "BadRequest/InvalidData/SliceName", "sliceA", "");
		
		ByteArrayOutputStream ostream = null;
		try {
			ostream = new ByteArrayOutputStream();
			serdes.marshal(resDto, ostream);
			String xml = new String(ostream.toByteArray(), "UTF-8");
		
			assertTrue(isEqualXml(xml, new File(DATA_PATH, "serdes.marshal.create.002.xml")));
		} finally {
			if (ostream != null) {
				ostream.close();
			}
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.Serdes#marshal(org.o3project.mlo.server.dto.RestifResponseDto, java.io.Writer)}.
	 * @throws Throwable 
	 */
	@Test
	public void testMarshal_create_003() throws Throwable {
		RestifResponseDto resDto = new RestifResponseDto();
		resDto.common = createResponseCommonDto();
		resDto.error = createRestifErrorDto("OtherError", "NoResource/FlowName=flow2", "sliceA", "00000001");
		
		ByteArrayOutputStream ostream = null;
		try {
			ostream = new ByteArrayOutputStream();
			serdes.marshal(resDto, ostream);
			String xml = new String(ostream.toByteArray(), "UTF-8");
		
			assertTrue(isEqualXml(xml, new File(DATA_PATH, "serdes.marshal.create.003.xml")));
		} finally {
			if (ostream != null) {
				ostream.close();
			}
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.Serdes#marshal(org.o3project.mlo.server.dto.RestifResponseDto, java.io.Writer)}.
	 * @throws Throwable 
	 */
	@Test
	public void testMarshal_create_004() throws Throwable {
		RestifResponseDto resDto = new RestifResponseDto();
		resDto.common = createResponseCommonDto();
		resDto.error = createRestifErrorDto("APICallError", "AlreadyCreated", "sliceA", "00000001");
		
		ByteArrayOutputStream ostream = null;
		try {
			ostream = new ByteArrayOutputStream();
			serdes.marshal(resDto, ostream);
			String xml = new String(ostream.toByteArray(), "UTF-8");
		
			assertTrue(isEqualXml(xml, new File(DATA_PATH, "serdes.marshal.create.004.xml")));
		} finally {
			if (ostream != null) {
				ostream.close();
			}
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.Serdes#marshal(org.o3project.mlo.server.dto.RestifResponseDto, java.io.Writer)}.
	 * @throws Throwable 
	 */
	@Test
	public void testMarshal_delete_001() throws Throwable {
		RestifResponseDto resDto = new RestifResponseDto();
		resDto.common = createResponseCommonDto();
		resDto.error = null;
		resDto.slices = new ArrayList<SliceDto>();
		
		SliceDto slice = createSliceDto(null, 00000001);
		slice.flows = null;
		resDto.slices.add(slice);
		
		ByteArrayOutputStream ostream = null;
		try {
			ostream = new ByteArrayOutputStream();
			serdes.marshal(resDto, ostream);
			String xml = new String(ostream.toByteArray(), "UTF-8");
		
			assertTrue(isEqualXml(xml, new File(DATA_PATH, "serdes.marshal.delete.001.xml")));
		} finally {
			if (ostream != null) {
				ostream.close();
			}
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.Serdes#marshal(org.o3project.mlo.server.dto.RestifResponseDto, java.io.Writer)}.
	 * @throws Throwable 
	 */
	@Test
	public void testMarshal_delete_002() throws Throwable {
		RestifResponseDto resDto = new RestifResponseDto();
		resDto.common = createResponseCommonDto();
		resDto.error = createRestifErrorDto("APICallError", "BadRequest/InvalidData/SliceId", null, null);
		
		ByteArrayOutputStream ostream = null;
		try {
			ostream = new ByteArrayOutputStream();
			serdes.marshal(resDto, ostream);
			String xml = new String(ostream.toByteArray(), "UTF-8");
		
			assertTrue(isEqualXml(xml, new File(DATA_PATH, "serdes.marshal.delete.002.xml")));
		} finally {
			if (ostream != null) {
				ostream.close();
			}
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.Serdes#marshal(org.o3project.mlo.server.dto.RestifResponseDto, java.io.Writer)}.
	 * @throws Throwable 
	 */
	@Test
	public void testMarshal_delete_003() throws Throwable {
		RestifResponseDto resDto = new RestifResponseDto();
		resDto.common = createResponseCommonDto();
		resDto.error = createRestifErrorDto("APICallError", "AlreadyDeleted", null, null);
		
		ByteArrayOutputStream ostream = null;
		try {
			ostream = new ByteArrayOutputStream();
			serdes.marshal(resDto, ostream);
			String xml = new String(ostream.toByteArray(), "UTF-8");
		
			assertTrue(isEqualXml(xml, new File(DATA_PATH, "serdes.marshal.delete.003.xml")));
		} finally {
			if (ostream != null) {
				ostream.close();
			}
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.Serdes#marshal(org.o3project.mlo.server.dto.RestifResponseDto, java.io.Writer)}.
	 * @throws Throwable 
	 */
	@Test
	public void testMarshal_update_001() throws Throwable {
		RestifResponseDto resDto = new RestifResponseDto();
		resDto.common = createResponseCommonDto();
		resDto.error = null;
		resDto.slices = new ArrayList<SliceDto>();
		
		SliceDto slice = createSliceDto(null, 00000001);
		slice.flows.add(createFlowDto("flow200", 00000002));
		slice.flows.add(createFlowDto("flow400", 00000004));
		resDto.slices.add(slice);
		
		ByteArrayOutputStream ostream = null;
		try {
			ostream = new ByteArrayOutputStream();
			serdes.marshal(resDto, ostream);
			String xml = new String(ostream.toByteArray(), "UTF-8");
		
			assertTrue(isEqualXml(xml, new File(DATA_PATH, "serdes.marshal.update.001.xml")));
		} finally {
			if (ostream != null) {
				ostream.close();
			}
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.Serdes#marshal(org.o3project.mlo.server.dto.RestifResponseDto, java.io.Writer)}.
	 * @throws Throwable 
	 */
	@Test
	public void testMarshal_update_002() throws Throwable {
		RestifResponseDto resDto = new RestifResponseDto();
		resDto.common = createResponseCommonDto();
		resDto.error = createRestifErrorDto("APICallError", "BadRequest/InvalidData/SliceId", null, "00000001");
		
		ByteArrayOutputStream ostream = null;
		try {
			ostream = new ByteArrayOutputStream();
			serdes.marshal(resDto, ostream);
			String xml = new String(ostream.toByteArray(), "UTF-8");
		
			assertTrue(isEqualXml(xml, new File(DATA_PATH, "serdes.marshal.update.002.xml")));
		} finally {
			if (ostream != null) {
				ostream.close();
			}
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.Serdes#marshal(org.o3project.mlo.server.dto.RestifResponseDto, java.io.Writer)}.
	 * @throws Throwable 
	 */
	@Test
	public void testMarshal_update_003() throws Throwable {
		RestifResponseDto resDto = new RestifResponseDto();
		resDto.common = createResponseCommonDto();
		resDto.error = createRestifErrorDto("APICallError", "AlreadyModified", null, "00000001");
		
		ByteArrayOutputStream ostream = null;
		try {
			ostream = new ByteArrayOutputStream();
			serdes.marshal(resDto, ostream);
			String xml = new String(ostream.toByteArray(), "UTF-8");
		
			assertTrue(isEqualXml(xml, new File(DATA_PATH, "serdes.marshal.update.003.xml")));
		} finally {
			if (ostream != null) {
				ostream.close();
			}
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.Serdes#marshal(org.o3project.mlo.server.dto.RestifResponseDto, java.io.Writer)}.
	 * @throws Throwable 
	 */
	@Test
	public void testMarshal_read_001() throws Throwable {
		RestifResponseDto resDto = new RestifResponseDto();
		resDto.common = createResponseCommonDto();
		resDto.error = null;
		resDto.slices = new ArrayList<SliceDto>();
		
		FlowDto flow1 = createFlowDto(null, 00000001);
        flow1.linkInfoList = new ArrayList<LinkInfoDto>();
		flow1.srcPTNodeName = "nodeA";
		flow1.srcPTNodeId = 1;
        flow1.dstPTNodeName = "nodeB";
        flow1.dstPTNodeId = 10000001;
        flow1.usedBandWidth = 4000;
        flow1.delayTime = 3;
        flow1.underlayLogicalList = "10000000_10000001";
        flow1.overlayLogicalList = "10000000_10000001";
        LinkInfoDto link1_1 = createLinkInfoDto(00000001);
        link1_1.srcPTNodeName = "nodeA";
        link1_1.srcPTNodeId = 1;
        link1_1.dstPTNodeName = "nodeB";
        link1_1.dstPTNodeId = 2;
        flow1.linkInfoList.add(link1_1);
        
		FlowDto flow2 = createFlowDto(null, 00000002);
        flow2.linkInfoList = new ArrayList<LinkInfoDto>();
		flow2.srcPTNodeName = "nodeA";
		flow2.srcPTNodeId = 10000000;
        flow2.dstPTNodeName = "nodeC";
        flow2.dstPTNodeId = 2;
        flow2.usedBandWidth = 5000;
        flow2.delayTime = 5;
        flow2.underlayLogicalList = "10000000_10000003_10000002";
        flow2.overlayLogicalList = "10000000_10000002";
        LinkInfoDto link2_1 = createLinkInfoDto(20000000);
        link2_1.srcPTNodeName = "nodeA";
        link2_1.srcPTNodeId = 10000000;
        link2_1.dstPTNodeName = "nodeD";
        link2_1.dstPTNodeId = 10000003;
        flow2.linkInfoList.add(link2_1);
        LinkInfoDto link2_2 = createLinkInfoDto(20000001);
        link2_2.srcPTNodeName = "nodeD";
        link2_2.srcPTNodeId = 10000003;
        link2_2.dstPTNodeName = "nodeC";
        link2_2.dstPTNodeId = 10000002;
        flow2.linkInfoList.add(link2_2);

		SliceDto slice = createSliceDto(null, 00000001);
		slice.flows.add(flow1);
		slice.flows.add(flow2);
		resDto.slices.add(slice);
		
		ByteArrayOutputStream ostream = null;
		try {
			ostream = new ByteArrayOutputStream();
			serdes.marshal(resDto, ostream);
			String xml = new String(ostream.toByteArray(), "UTF-8");
		
			assertTrue(isEqualXml(xml, new File(DATA_PATH, "serdes.marshal.read.001.xml")));
		} finally {
			if (ostream != null) {
				ostream.close();
			}
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.Serdes#marshal(org.o3project.mlo.server.dto.RestifResponseDto, java.io.Writer)}.
	 * @throws Throwable 
	 */
	@Test
	public void testMarshal_read_002() throws Throwable {
		RestifResponseDto resDto = new RestifResponseDto();
		resDto.common = createResponseCommonDto();
		resDto.error = createRestifErrorDto("APICallError", "BadRequest/InvalidData/SliceId", null, null);
		
		ByteArrayOutputStream ostream = null;
		try {
			ostream = new ByteArrayOutputStream();
			serdes.marshal(resDto, ostream);
			String xml = new String(ostream.toByteArray(), "UTF-8");
		
			assertTrue(isEqualXml(xml, new File(DATA_PATH, "serdes.marshal.read.002.xml")));
		} finally {
			if (ostream != null) {
				ostream.close();
			}
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.Serdes#marshal(org.o3project.mlo.server.dto.RestifResponseDto, java.io.Writer)}.
	 * @throws Throwable 
	 */
	@Test
	public void testMarshal_read_003() throws Throwable {
		RestifResponseDto resDto = new RestifResponseDto();
		resDto.common = createResponseCommonDto();
		resDto.error = createRestifErrorDto("APICallError", "NoData", null, null);
		
		ByteArrayOutputStream ostream = null;
		try {
			ostream = new ByteArrayOutputStream();
			serdes.marshal(resDto, ostream);
			String xml = new String(ostream.toByteArray(), "UTF-8");
		
			assertTrue(isEqualXml(xml, new File(DATA_PATH, "serdes.marshal.read.003.xml")));
		} finally {
			if (ostream != null) {
				ostream.close();
			}
		}
	}

	private RestifCommonDto createResponseCommonDto() {
		return createResponseCommonDto("demoApl");
	}
	
	private RestifCommonDto createResponseCommonDto(String dstName) {
		RestifCommonDto common = null;
		
		common = new RestifCommonDto();
		common.version = 1;
		common.srcComponent = new RestifComponentDto();
		common.srcComponent.name = "mlo";
		common.dstComponent = new RestifComponentDto();
		common.dstComponent.name = dstName;
		common.operation = "Response";
		
		return common;
	}
	
	private SliceDto createSliceDto(String name, int id) {
		SliceDto slice = null;
		slice = new SliceDto();
		slice.name = name;
		slice.id = id;
		slice.flows = new ArrayList<FlowDto>();
		return slice;
	}
	
	private FlowDto createFlowDto(String name, int id) { 
		FlowDto flow = null;
		flow = new FlowDto();
		flow.name = name;
		flow.id = id;
		return flow;
	}
	
	private LinkInfoDto createLinkInfoDto(int id) { 
		LinkInfoDto linkInfo = null;
		linkInfo = new LinkInfoDto();
		linkInfo.id = id;
		return linkInfo;
	}
	
	private RestifErrorDto createRestifErrorDto(String cause, String detail, String name, String id) { 
		RestifErrorDto restifErrorDto = null;
		restifErrorDto = new RestifErrorDto();
		restifErrorDto.cause = cause;
		restifErrorDto.detail = detail;
		restifErrorDto.sliceName = name;
		restifErrorDto.sliceId = id;
		return restifErrorDto;
	}
	
	private boolean isEqualXml(String xml1, File xmlfile) throws Throwable {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		dbf.setCoalescing(true);
		dbf.setIgnoringElementContentWhitespace(true);
		dbf.setIgnoringComments(true);
		
		DocumentBuilder db = dbf.newDocumentBuilder();
		InputStream istream1 = null;
		InputStream istream2 = null;
		Document doc1 = null;
		Document doc2 = null;
	
		try {
			istream1 = new ByteArrayInputStream(xml1.getBytes("UTF-8"));
			doc1 = db.parse(istream1);
			doc1.normalizeDocument();
		
			istream2 = new FileInputStream(xmlfile);
			doc2 = db.parse(istream2);
			doc2.normalizeDocument();
		} finally {
			if (istream1 != null) {
				istream1.close();
			}
			if (istream2 != null) {
				istream2.close();
			}
		}
		
		return doc1.isEqualNode(doc2);
	}
	
	@SuppressWarnings("unused")
	private String toString(Node node) throws Throwable {
		StringWriter sw = new StringWriter();
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer t = tf.newTransformer();
		t.transform(new DOMSource(node), new StreamResult(sw));
		return sw.toString();
	}
}
