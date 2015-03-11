/**
 * SdtncReqPostCreatePwDtoTest.java
 * (C) 2013, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.rpc.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXB;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.impl.rpc.service.SdtncSerdesImpl;
import org.o3project.mlo.server.impl.rpc.service.SdtncTestUtil;
import org.o3project.mlo.server.rpc.dto.SdtncAuthDto;
import org.o3project.mlo.server.rpc.dto.SdtncHeadDto;
import org.o3project.mlo.server.rpc.dto.SdtncLineDto;
import org.o3project.mlo.server.rpc.dto.SdtncLspDto;
import org.o3project.mlo.server.rpc.dto.SdtncNniDto;
import org.o3project.mlo.server.rpc.dto.SdtncPathEndPointDto;
import org.o3project.mlo.server.rpc.dto.SdtncPathInfoDto;
import org.o3project.mlo.server.rpc.dto.SdtncPathRouteDto;
import org.o3project.mlo.server.rpc.dto.SdtncPwDto;
import org.o3project.mlo.server.rpc.dto.SdtncQosDto;
import org.o3project.mlo.server.rpc.dto.SdtncReqPostCreatePwDto;
import org.o3project.mlo.server.rpc.dto.SdtncRequestDto;
import org.o3project.mlo.server.rpc.dto.SdtncSliceDto;
import org.o3project.mlo.server.rpc.dto.SdtncUniDto;
import org.o3project.mlo.server.rpc.dto.SdtncVlanDto;
import org.o3project.mlo.server.rpc.dto.SdtncVpathDto;

/**
 * SdtncReqPostCreatePwDtoTest
 *
 */
public class SdtncReqPostCreatePwDtoTest {
    private static final String DATA_PATH = "src/test/resources/org/o3project/mlo/server/rpc/service/data";

    private SdtncSerdesImpl serdes;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        serdes = new SdtncSerdesImpl();
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        serdes = null;
    }

    @Test
    public void testCreateInstance_001() {

        //		<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		//    	<CpfIfParamVpath>
		//    	  <slice groupIndex="groupIndex" />
		//    	  <vpath resourceIndex ="resourceIndex">
		//    	    <vObjectName>vObjectName</vObjectName>
		//    	    <vObjectDescription>vObjectDescription</vObjectDescription>
		//    	    <pathInfo>
		//    	      <vGrade>1</vGrade>
		//    	    </pathInfo>
		//    	    <pathEndPoint startEndPoint="startEndPoint">
		//    	      <neId>neId</neId>
		//    	      <portId>portId</portId>
		//    	    </pathEndPoint>
    	//			<pathRoute>
    	//				<lineGroupWorkingProtection>1</lineGroupWorkingProtection>
    	//				<line elementIndex = "elementIndex">
    	//					<lineSequence>1</lineSequence>
    	//				</line>
    	//			</pathRoute>
		//    	    <pw>
		//    	      <vPwDownLabel>vPwDownLabel</vPwDownLabel>
		//    	      <vPwUpLabel>vPwUpLabel</vPwUpLabel>
		//    	    </pw>
		//    	    <qos>
		//    	      <vQoSPriority>1</vQoSPriority>
		//    	      <vQoSSla>1</vQoSSla>
		//    	      <vQoSPir>1</vQoSPir>
		//    	      <vQoSCir>1</vQoSCir>
		//    	    </qos>
		//    	    <vlan>
		//    	      <vVlanTagNum>vVlanTagNum</vVlanTagNum>
		//    	      <vVlanTagMode>vVlanTagMode</vVlanTagMode>
		//    	      <uni>
		//    	        <vVlanVLanIdNniClient>vVlanVLanIdNniClient</vVlanVLanIdNniClient>
		//    	        <vVlanInnerVlanIdNniClient>vVlanInnerVlanIdNniClient</vVlanInnerVlanIdNniClient>
		//    	      </uni>
		//    	    </vlan>
		//    	  </vpath>
		//    	</CpfIfParamVpath>

        // DTO
        List<SdtncVpathDto> vpathList = new ArrayList<SdtncVpathDto>();
        SdtncVpathDto vPath = new SdtncVpathDto();
        SdtncLspDto lsp = new SdtncLspDto();
        lsp.vLspDownLabel = "vLspDownLabel";
        lsp.vLspUpLabel = "vLspUpLabel";
        vPath.lsp = lsp;
        List<SdtncPathEndPointDto> pathEndPointList = new ArrayList<SdtncPathEndPointDto>();
        SdtncPathEndPointDto pathEndPoint = new SdtncPathEndPointDto();
        pathEndPoint.neId = "neId";
        pathEndPoint.portId = "portId";
        pathEndPoint.startEndPoint = "startEndPoint";
        pathEndPointList.add(pathEndPoint);
        vPath.pathEndPoint = pathEndPointList;
        
        SdtncPathInfoDto pathInfo = new SdtncPathInfoDto();
        pathInfo.vGrade = 1;
        vPath.pathInfo = pathInfo;
        
        List<SdtncPathRouteDto> pathRouteList = new ArrayList<SdtncPathRouteDto>();
        SdtncPathRouteDto pathRoute = new SdtncPathRouteDto();
        
        List<SdtncLineDto>lineList = new ArrayList<SdtncLineDto>();
        SdtncLineDto line = new SdtncLineDto();
        line.elementIndex = "elementIndex";
        line.lineSequence = 1;
        lineList.add(line);
        pathRoute.line = lineList;
        pathRoute.lineGroupWorkingProtection = 1;
        pathRouteList.add(pathRoute);
        vPath.pathRoute = pathRouteList;
        
        SdtncPwDto pw = new SdtncPwDto();
        pw.vPwDownLabel = "vPwDownLabel";
        pw.vPwUpLabel = "vPwUpLabel";
        vPath.pw = pw;
        
        SdtncQosDto qos = new SdtncQosDto();
        qos.vQoSPriority = 1;
        qos.vQoSSla = 1;
        qos.vQoSPir = 1;
        qos.vQoSCir = 1;
        vPath.qos = qos;
        
        vPath.resourceIndex = "resourceIndex";
        
        vPath.vObjectDescription = "vObjectDescription";
        vPath.vObjectIndex = "vObjectIndex";
        vPath.vObjectName = "vObjectName";
        vPath.vObjectStatus = 1;
        
        SdtncVlanDto vlan = new SdtncVlanDto();
        vlan.vVlanTagMode = 1;
        vlan.vVlanTagNum = 1;

        SdtncNniDto nni = new SdtncNniDto();
        nni.vVlanOutterCosNniLine = 1;
        nni.vVlanVlanIdNniLine = 1;
        vlan.nni = nni;
        
        SdtncUniDto uni = new SdtncUniDto();
        uni.vVlanInnerCosNniClient = 1;
        uni.vVlanInnerVlanIdNniClient = 1;
        uni.vVlanOutterCosNniClient = 1;
        uni.vVlanVLanIdNniClient = 1;
        vlan.uni = uni;
        vPath.vlan = vlan;
        
        vpathList.add(vPath);
        
        SdtncSliceDto slice = new SdtncSliceDto();
        slice.groupIndex = "sliceId";

        SdtncReqPostCreatePwDto obj = new SdtncReqPostCreatePwDto();
        obj.vpath = vpathList;
        obj.slice = slice;

        // DTO => XML
        String reqXml = null;
        ByteArrayOutputStream ostream = null;
        ostream = new ByteArrayOutputStream();

        // SdtncSerdesImpl　呼び出し。
        SdtncSerdesImpl serdesImple = new SdtncSerdesImpl();
        serdesImple.serializeToXml(obj, ostream);

        try {
            reqXml = new String(ostream.toByteArray(), "UTF-8");
            System.out.println(reqXml);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // XML => DTO
        ByteArrayInputStream istream = new ByteArrayInputStream(reqXml.getBytes());
        SdtncReqPostCreatePwDto request_unmarshaled = JAXB.unmarshal(istream, SdtncReqPostCreatePwDto.class);

        // 確認　（前DTO　= 後DTO）
        assertEquals(request_unmarshaled.slice.groupIndex, obj.slice.groupIndex);
        assertEquals(request_unmarshaled.vpath.get(0).lsp.vLspDownLabel, obj.vpath.get(0).lsp.vLspDownLabel);
        assertEquals(request_unmarshaled.vpath.get(0).lsp.vLspUpLabel, obj.vpath.get(0).lsp.vLspUpLabel);
        assertEquals(request_unmarshaled.vpath.get(0).pathEndPoint.get(0).neId, obj.vpath.get(0).pathEndPoint.get(0).neId);
        assertEquals(request_unmarshaled.vpath.get(0).pathEndPoint.get(0).portId, obj.vpath.get(0).pathEndPoint.get(0).portId);
        assertEquals(request_unmarshaled.vpath.get(0).pathEndPoint.get(0).startEndPoint, obj.vpath.get(0).pathEndPoint.get(0).startEndPoint);
        assertEquals(request_unmarshaled.vpath.get(0).pathInfo.vGrade, obj.vpath.get(0).pathInfo.vGrade);
        assertEquals(request_unmarshaled.vpath.get(0).pathRoute.get(0).line.get(0).elementIndex, obj.vpath.get(0).pathRoute.get(0).line.get(0).elementIndex);
        assertEquals(request_unmarshaled.vpath.get(0).pathRoute.get(0).line.get(0).lineSequence, obj.vpath.get(0).pathRoute.get(0).line.get(0).lineSequence);
        assertEquals(request_unmarshaled.vpath.get(0).pathRoute.get(0).lineGroupWorkingProtection, obj.vpath.get(0).pathRoute.get(0).lineGroupWorkingProtection);
        assertEquals(request_unmarshaled.vpath.get(0).pw.vPwDownLabel, obj.vpath.get(0).pw.vPwDownLabel);
        assertEquals(request_unmarshaled.vpath.get(0).pw.vPwUpLabel, obj.vpath.get(0).pw.vPwUpLabel);
        assertEquals(request_unmarshaled.vpath.get(0).qos.vQoSCir, obj.vpath.get(0).qos.vQoSCir);
        assertEquals(request_unmarshaled.vpath.get(0).qos.vQoSPir, obj.vpath.get(0).qos.vQoSPir);
        assertEquals(request_unmarshaled.vpath.get(0).qos.vQoSPriority, obj.vpath.get(0).qos.vQoSPriority);
        assertEquals(request_unmarshaled.vpath.get(0).qos.vQoSSla, obj.vpath.get(0).qos.vQoSSla);
        assertEquals(request_unmarshaled.vpath.get(0).resourceIndex, obj.vpath.get(0).resourceIndex);
        assertEquals(request_unmarshaled.vpath.get(0).vlan.nni.vVlanOutterCosNniLine, obj.vpath.get(0).vlan.nni.vVlanOutterCosNniLine);
        assertEquals(request_unmarshaled.vpath.get(0).vlan.nni.vVlanVlanIdNniLine, obj.vpath.get(0).vlan.nni.vVlanVlanIdNniLine);
        assertEquals(request_unmarshaled.vpath.get(0).vlan.uni.vVlanInnerCosNniClient, obj.vpath.get(0).vlan.uni.vVlanInnerCosNniClient);
        assertEquals(request_unmarshaled.vpath.get(0).vlan.uni.vVlanInnerVlanIdNniClient, obj.vpath.get(0).vlan.uni.vVlanInnerVlanIdNniClient);
        assertEquals(request_unmarshaled.vpath.get(0).vlan.uni.vVlanOutterCosNniClient, obj.vpath.get(0).vlan.uni.vVlanOutterCosNniClient);
        assertEquals(request_unmarshaled.vpath.get(0).vlan.uni.vVlanVLanIdNniClient, obj.vpath.get(0).vlan.uni.vVlanVLanIdNniClient);
        assertEquals(request_unmarshaled.vpath.get(0).vlan.vVlanTagMode, obj.vpath.get(0).vlan.vVlanTagMode);
        assertEquals(request_unmarshaled.vpath.get(0).vlan.vVlanTagNum, obj.vpath.get(0).vlan.vVlanTagNum);
        assertEquals(request_unmarshaled.vpath.get(0).vObjectDescription, obj.vpath.get(0).vObjectDescription);
        assertEquals(request_unmarshaled.vpath.get(0).vObjectIndex, obj.vpath.get(0).vObjectIndex);
        assertEquals(request_unmarshaled.vpath.get(0).vObjectName, obj.vpath.get(0).vObjectName);
        assertEquals(request_unmarshaled.vpath.get(0).vObjectStatus, obj.vpath.get(0).vObjectStatus);

    }

    /**
     * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncSerdesImpl#serializeToXml(SdtncRequestDto, java.io.OutputStream)}.
     * @throws Throwable
     */
    @Test
    public void testSerializeToXml_req_req() throws Throwable {
    	SdtncReqPostCreatePwDto reqDto = new SdtncReqPostCreatePwDto();
        reqDto.head = new SdtncHeadDto();
        reqDto.auth = new SdtncAuthDto();
        reqDto.slice = new SdtncSliceDto();
        List<SdtncVpathDto> vPathList = new ArrayList<SdtncVpathDto>();
        SdtncVpathDto vPathDto= new SdtncVpathDto();
        vPathList.add(vPathDto);
        reqDto.vpath = vPathList;
        assertTrue(SdtncTestUtil.isSameXmlAs(serdes, reqDto, DATA_PATH, "sdtnc.post.createpw.req.xml"));
    }

}
