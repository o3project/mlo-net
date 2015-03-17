package org.o3project.mlo.server.impl.logic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.o3project.mlo.server.dto.FlowDto;
import org.o3project.mlo.server.dto.RestifCommonDto;
import org.o3project.mlo.server.dto.RestifComponentDto;
import org.o3project.mlo.server.dto.RestifRequestDto;
import org.o3project.mlo.server.dto.RestifResponseDto;
import org.o3project.mlo.server.dto.SliceDto;
import org.o3project.mlo.server.logic.Serdes;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


public class DtoTestUtils {
	private static final Log LOG = LogFactory.getLog(DtoTestUtils.class);

	/**
	 * Sets normal values to common header.
	 * @param dto
	 */
	public static void setNormalCommonDto(RestifRequestDto dto) {
		dto.common = new RestifCommonDto();
		dto.common.version = 1;
		dto.common.srcComponent = new RestifComponentDto();
		dto.common.srcComponent.name = "demoApl________________________E";
		dto.common.dstComponent = new RestifComponentDto();
		dto.common.dstComponent.name = "mlo____________________________E";
		dto.common.operation = "Request";
	}

	/**
	 * Sets normal values to slice part. The number of flows can be specified.
	 * @param dto
	 * @param count
	 */
	public static void setNormalSliceDto(RestifRequestDto dto, int count) {
		dto.slice = new SliceDto();
		dto.slice.name = "TestSliceName__________________E";
		dto.slice.id = 1;
		dto.slice.flows = new ArrayList<FlowDto>();
		for (int i = 1; i <= count; i++) {
			FlowDto flow = new FlowDto();
			flow.type = "add";
			flow.name = String.format("FlowName%03d____________________E", i);
			flow.id = i;
			flow.srcCENodeName = String.format("SourceCENodeName%03d____________E", i);
			flow.srcCEPortNo = String.format("%08d", i);
			flow.dstCENodeName = String.format("DestCENodeName%03d______________E", i);
			flow.dstCEPortNo = String.format("%08d", i);
			flow.reqBandWidth = i;
			flow.reqDelay = i;
			if ((i % 2) == 0) {
				flow.protectionLevel = "0";
			} else {
				flow.protectionLevel = "1";
			}
			dto.slice.flows.add(flow);
		}
	}
	
	/**
	 * Reads request DTO from XML file.
	 * @param serdes the {@link Serdes} instance.
	 * @param dirName xml file directory. 
	 * @param xmlFileName xml file name.
	 * @return the request DTO.
	 * @throws Throwable Failure in reading.
	 */
	public static RestifRequestDto readReqFromXml(Serdes serdes, String dirName, String xmlFileName) throws Throwable {
		File xmlfile = new File(dirName, xmlFileName);
		InputStream istream = null;
		RestifRequestDto reqDto = null;
		try {
			istream = new FileInputStream(xmlfile);
			reqDto = serdes.unmarshal(istream);
		} finally {
			if (istream != null) {
				istream.close();
			}
		}
		return reqDto;
	}

	/**
	 * Designates whether response DTO has same contents as specified xml file.
	 * @param serdes the {@link Serdes} instance.
	 * @param resDto {@link RestifResponseDto} instance.
	 * @param dirName xml file directory.
	 * @param xmlFileName xml file name.
	 * @return true if those are same.
	 * @throws Throwable Failure in processing.
	 */
	public static boolean isSameResAsXml(Serdes serdes, RestifResponseDto resDto, String dirName, String xmlFileName) throws Throwable {
		String resXml = null;
		ByteArrayOutputStream ostream = null;
		try {
			ostream = new ByteArrayOutputStream();
			serdes.marshal(resDto, ostream);
			resXml = new String(ostream.toByteArray(), "UTF-8");
		} finally {
			if (ostream != null) {
				ostream.close();
			}
		}
		
		String expectedXml = null;
		try {
			ostream = new ByteArrayOutputStream();
			
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(new File(dirName, xmlFileName));
				byte[] buf = new byte[1024];
				int rlen = -1;
				while (!Thread.currentThread().isInterrupted()) {
					rlen = fis.read(buf, 0, buf.length);
					if (rlen < 0) {
						break;
					}
					ostream.write(buf, 0, rlen);
				}
				expectedXml = new String(ostream.toByteArray(), "UTF-8");
			} finally {
				if (fis != null) {
					fis.close();
				}
			}
			
		} finally {
			if (ostream != null) {
				ostream .close();
			}
		}
		
		InputStream istream1 = null;
		InputStream istream2 = null;
	
		boolean isSame = false;
		try {
			istream1 = new ByteArrayInputStream(resXml.getBytes("UTF-8"));
			istream2 = new ByteArrayInputStream(expectedXml.getBytes("UTF-8"));;
			
			isSame = isSameXml(istream1, istream2);
		} finally {
			if (istream1 != null) {
				istream1.close();
			}
			if (istream2 != null) {
				istream2.close();
			}
		}
		
		if (!isSame) {
			LOG.info("---- actual:");
			LOG.info(resXml);
			LOG.info("---- expected:");
			LOG.info(expectedXml);
		}
		
		return isSame;
	}

	/**
	 * Checks whether contents in xml stream is same as xml.
	 * @param istream1 the input stream.
	 * @param istream2 the input stream.
	 * @return true if those are same.
	 * @throws ParserConfigurationException Parser configuration error.
	 * @throws SAXException Failure in parsing.
	 * @throws IOException Failure in reading.
	 */
	public static boolean isSameXml(InputStream istream1, InputStream istream2)
			throws ParserConfigurationException, SAXException, IOException {
		boolean isSame;
		Document doc1 = null;
		Document doc2 = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		dbf.setCoalescing(true);
		dbf.setIgnoringElementContentWhitespace(true);
		dbf.setIgnoringComments(true);
		
		DocumentBuilder db = dbf.newDocumentBuilder();
		
		doc1 = db.parse(istream1);
		doc1.normalizeDocument();

		doc2 = db.parse(istream2);
		doc2.normalizeDocument();
		
		isSame = doc1.isEqualNode(doc2);
		return isSame;
	}
}
