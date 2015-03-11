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
	 * 共通ヘッダの正常値設定
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
	 * スライス部の正常値設定。フローの件数指定可能。
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
	 * XML ファイルからリクエスト DTO を読み込む。
	 * @param serdes {@link Serdes} インスタンス
	 * @param dirName xml ファイルのディレクトリ
	 * @param xmlFileName xml ファイルのファイル名
	 * @return リクエスト DTO
	 * @throws Throwable 読み込み中の異常例外
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
	 * レスポンス DTO の内容が XML ファイルと同じかどうか。
	 * @param serdes {@link Serdes} インスタンス
	 * @param resDto {@link RestifResponseDto} インスタンス
	 * @param dirName xml ファイルのディレクトリ
	 * @param xmlFileName xml ファイルのファイル名
	 * @return 同じか否か。同じならば true を返す。
	 * @throws Throwable 処理中の異常例外
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
	 * 入力ストリームから得られる XML が同じものかどうか判断する。
	 * @param istream1 比較対象 xml 1
	 * @param istream2 比較対象 xml 2
	 * @return true の場合、同じ XML。false の場合、異なる XML
	 * @throws ParserConfigurationException パーサコンフィギュレーション異常
	 * @throws SAXException パース中の異常
	 * @throws IOException 読み込み時異常
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
