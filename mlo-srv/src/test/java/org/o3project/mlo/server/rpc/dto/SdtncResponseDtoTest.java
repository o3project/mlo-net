/**
 * SdtncResponseDtoTest.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.rpc.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
import org.o3project.mlo.server.rpc.dto.SdtncLoginDto;
import org.o3project.mlo.server.rpc.dto.SdtncLspDto;
import org.o3project.mlo.server.rpc.dto.SdtncNniDto;
import org.o3project.mlo.server.rpc.dto.SdtncPathEndPointDto;
import org.o3project.mlo.server.rpc.dto.SdtncPathInfoDto;
import org.o3project.mlo.server.rpc.dto.SdtncPathRouteDto;
import org.o3project.mlo.server.rpc.dto.SdtncPwDto;
import org.o3project.mlo.server.rpc.dto.SdtncQosDto;
import org.o3project.mlo.server.rpc.dto.SdtncResponseDto;
import org.o3project.mlo.server.rpc.dto.SdtncUniDto;
import org.o3project.mlo.server.rpc.dto.SdtncVlanDto;
import org.o3project.mlo.server.rpc.dto.SdtncVlinkDto;
import org.o3project.mlo.server.rpc.dto.SdtncVpathDto;

/**
 * SdtncResponseDtoTest
 *
 */
public class SdtncResponseDtoTest {
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
    public void testCreateInstance_001()  throws Throwable {

    	
        @SuppressWarnings("unused")
		SdtncResponseDto resDto = SdtncTestUtil.readResFromXml(serdes, DATA_PATH, "sdtnc.login.post.res.xml");

        //		<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
        //		<root>
        //		    <head>
        //		        <sequenceNumber>00000101</sequenceNumber>
        //		        <time>timeStr</time>
        //		        <majorResponseCode>00000423</majorResponseCode>
        //		        <minorResponseCode>00000554</minorResponseCode>
        //		        <errorDetail>errorDetail</errorDetail>
        //		    </head>
        //		    <auth>
        //		        <token>token</token>
        //		    </auth>
        //		    <login loginId="loginId">
        //		        <loginPassword>loginPassword</loginPassword>
        //		        <ipAddress>ipAddress</ipAddress>
        //		        <accountName>accountName</accountName>
        //		        <accountLoginDate>accountLoginDate</accountLoginDate>
        //		        <accountTimeZone>accountTimeZone</accountTimeZone>
        //		    </login>
        //		    <vpath vObjectIndex="vObjectIndex">
        //		        <pathRoute pathProtection="234">
        //		            <link linkId="linkId">
        //		                <passOrder>534</passOrder>
        //		            </link>
        //		        </pathRoute>
        //		        <pathInfo pathId="123.0">
        //		            <pathGrade>534</pathGrade>
        //		        </pathInfo>
        //		        <pathEndPoint startEndPoint="startEndPoint">
        //		            <neId>neId</neId>
        //		            <portId>523</portId>
        //		        </pathEndPoint>
        //		        <lsp>
        //		            <vLspDownLabel>AAAAAAAA</vLspDownLabel>
        //		            <vLspUpLabel>AAAAAAAAAA==</vLspUpLabel>
        //		        </lsp>
        //		        <pw>
        //		            <vPwDownLabel>AAAAAAAAAAA=</vPwDownLabel>
        //		            <vPwUpLabel>AAAAAAAAAAAA</vPwUpLabel>
        //		        </pw>
        //		        <qos>
        //		            <ingressQoSQueueClass>756</ingressQoSQueueClass>
        //		            <slaMode>856</slaMode>
        //		            <ingressQoSCIR>234.0</ingressQoSCIR>
        //		            <ingressQoSPIR>745.0</ingressQoSPIR>
        //		        </qos>
        //		        <vlan>
        //		            <vlanTagStack>534</vlanTagStack>
        //		            <tagOperation>645</tagOperation>
        //		            <uni>
        //		                <tpVlanOutVid>887</tpVlanOutVid>
        //		                <tpVlanInnerVid>888</tpVlanInnerVid>
        //		                <tpVlanReceiveCos>789</tpVlanReceiveCos>
        //		                <tpVlanOutCos>756</tpVlanOutCos>
        //		            </uni>
        //		            <nni tpVlanChangeOutVid="654">
        //		                <tpVlanChangeOutCos>775</tpVlanChangeOutCos>
        //		            </nni>
        //		        </vlan>
        //		    </vpath>
        //		    <vlink>
        //		        <tpVlanChangeOutVid>235</tpVlanChangeOutVid>
        //		        <tpVlanChangeOutCos>646</tpVlanChangeOutCos>
        //		    </vlink>
        //		</root>

        SdtncHeadDto head = new SdtncHeadDto();
        head.sequenceNumber = new Integer(101);
        head.time = "timeStr";
        head.majorResponseCode = new Integer(423);
        head.minorResponseCode = new Integer(554);
        head.errorDetail = "errorDetail";
        SdtncLoginDto logIn = new SdtncLoginDto();
        logIn.loginId = "loginId";
        logIn.loginPassword = "loginPassword";
        logIn.loginPassword = "loginPassword";
        logIn.ipAddress = "ipAddress";
        logIn.accountName = "accountName";
        logIn.accountLoginDate = "accountLoginDate";
        logIn.accountTimeZone = "accountTimeZone";
        SdtncAuthDto auth = new SdtncAuthDto();
        auth.token = "token";
        SdtncVpathDto sdtncVpathDto = new SdtncVpathDto();
        sdtncVpathDto.resourceIndex = "resourceIndex";
        sdtncVpathDto.pathRoute = null;
        SdtncPathRouteDto sdtncPathRouteDto = new SdtncPathRouteDto();
        sdtncPathRouteDto.lineGroupWorkingProtection = 234;
        sdtncPathRouteDto.line = null;
        SdtncLineDto lneDto = new SdtncLineDto();
        lneDto.elementIndex = "elementIndex";
        lneDto.lineSequence = 534;

        ArrayList<SdtncLineDto> listline = new ArrayList<SdtncLineDto>();
        listline.add(lneDto);
        sdtncPathRouteDto.line = listline;

        ArrayList<SdtncPathRouteDto> listPathRoute = new ArrayList<SdtncPathRouteDto>();
        listPathRoute.add(sdtncPathRouteDto);
        sdtncVpathDto.pathRoute = listPathRoute;

        sdtncVpathDto.pathInfo = null;
        SdtncPathInfoDto sdtncPathInfoDto = new SdtncPathInfoDto();
        sdtncPathInfoDto.vGrade = 534;
        sdtncVpathDto.pathInfo = sdtncPathInfoDto;
        sdtncVpathDto.pathEndPoint = null;
        SdtncPathEndPointDto SdtncPathEndPointDto = new SdtncPathEndPointDto();
        SdtncPathEndPointDto.startEndPoint = "startEndPoint";
        SdtncPathEndPointDto.neId = "neId";
        SdtncPathEndPointDto.portId = "523";

        ArrayList<SdtncPathEndPointDto> listPathEndPoint = new ArrayList<SdtncPathEndPointDto>();
        listPathEndPoint.add(SdtncPathEndPointDto);
        sdtncVpathDto.pathEndPoint = listPathEndPoint;

        sdtncVpathDto.lsp = null;
        SdtncLspDto sdtncLspDto = new SdtncLspDto();
        sdtncLspDto.vLspDownLabel = "vLspDownLabel";
        sdtncLspDto.vLspUpLabel = "vLspUpLabel";
        sdtncVpathDto.lsp = sdtncLspDto;
        sdtncVpathDto.pw = null;
        SdtncPwDto sdtncPwDto = new SdtncPwDto();
        sdtncPwDto.vPwUpLabel = "vPwUpLabel";
        sdtncPwDto.vPwDownLabel = "vPwDownLabel";
        sdtncVpathDto.pw = sdtncPwDto;
        sdtncVpathDto.qos = null;
        SdtncQosDto sdtncQosDto = new SdtncQosDto();
        sdtncQosDto.vQoSPriority = 756;
        sdtncQosDto.vQoSSla = 856;
        sdtncQosDto.vQoSPir = 234;
        sdtncQosDto.vQoSCir = 745;
        sdtncVpathDto.qos = sdtncQosDto;
        sdtncVpathDto.vlan = null;
        List<SdtncVpathDto> vpathList = new ArrayList<SdtncVpathDto>();
        SdtncVlanDto sdtncVlanDto = new SdtncVlanDto();
        sdtncVlanDto.vVlanTagNum = 534 ;
        sdtncVlanDto.vVlanTagMode = 645 ;
        sdtncVlanDto.uni = null;
        SdtncUniDto sdtncUniDto = new SdtncUniDto();
        sdtncUniDto.vVlanVLanIdNniClient = 887;
        sdtncUniDto.vVlanInnerVlanIdNniClient = 888;
        sdtncUniDto.vVlanInnerCosNniClient = 789;
        sdtncUniDto.vVlanOutterCosNniClient = 756;
        sdtncVlanDto.uni = sdtncUniDto;
        sdtncVlanDto.nni = null;
        SdtncNniDto sdtncNniDto = new SdtncNniDto();
        sdtncNniDto.vVlanVlanIdNniLine = 654;
        sdtncNniDto.vVlanOutterCosNniLine = 775;
        sdtncVlanDto.nni = sdtncNniDto;
        sdtncVpathDto.vlan = sdtncVlanDto;
        vpathList.add(sdtncVpathDto);

        List<SdtncVlinkDto> vlinkList = new ArrayList<SdtncVlinkDto>();
        SdtncVlinkDto sdtncVlinkDto = new SdtncVlinkDto();
        sdtncVlinkDto.vObjectIndex = "333";
        sdtncVlinkDto.vObjectName = "333";
        sdtncVlinkDto.vObjectStatus = 333;
        sdtncVlinkDto.vObjectDescription = "333";
        sdtncVlinkDto.resourceIndex = "333";
        sdtncVlinkDto.vLineSource = "333";
        sdtncVlinkDto.vLineSink = "333";
        vlinkList.add(sdtncVlinkDto);

        // DTO
        SdtncResponseDto responceDto = new SdtncResponseDto();
        responceDto.head = head;
        responceDto.auth = auth;
        responceDto.login = logIn;
        responceDto.vpath = vpathList;
        responceDto.vlink = vlinkList;

        // DTO => XML
        String resXml = null;
        ByteArrayOutputStream ostream = null;
        ostream = new ByteArrayOutputStream();
        JAXB.marshal(responceDto, ostream);
        try {
            resXml = new String(ostream.toByteArray(), "UTF-8");
            System.out.println(resXml);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // XML => DTO
        // SdtncSerdesImpl　呼び出し。
        ByteArrayInputStream istream = new ByteArrayInputStream(resXml.getBytes());
        SdtncSerdesImpl serdesImple = new SdtncSerdesImpl();
        SdtncResponseDto request_unmarshaled = serdesImple.deserializeFromXml(istream);

        // 確認　（前DTO　= 後DTO）
        assertEquals(request_unmarshaled.head.sequenceNumber, responceDto.head.sequenceNumber);
        assertEquals(request_unmarshaled.head.time, responceDto.head.time);
        assertEquals(request_unmarshaled.head.majorResponseCode, responceDto.head.majorResponseCode);
        assertEquals(request_unmarshaled.head.minorResponseCode , responceDto.head.minorResponseCode);
        assertEquals(request_unmarshaled.head.errorDetail, responceDto.head.errorDetail);
        assertEquals(request_unmarshaled.auth.token, responceDto.auth.token);
        assertEquals(request_unmarshaled.login.loginId, responceDto.login.loginId);
        assertEquals(request_unmarshaled.login.ipAddress, responceDto.login.ipAddress);
        assertEquals(request_unmarshaled.login.accountName, responceDto.login.accountName);
        assertEquals(request_unmarshaled.login.accountLoginDate, responceDto.login.accountLoginDate);
        assertEquals(request_unmarshaled.login.accountTimeZone, responceDto.login.accountTimeZone);
        assertEquals(request_unmarshaled.vpath.get(0).resourceIndex ,responceDto.vpath.get(0).resourceIndex);
        assertEquals(request_unmarshaled.vpath.get(0).pathRoute.get(0).lineGroupWorkingProtection, responceDto.vpath.get(0).pathRoute.get(0).lineGroupWorkingProtection);
        assertEquals(request_unmarshaled.vpath.get(0).pathRoute.get(0).line.get(0).elementIndex, responceDto.vpath.get(0).pathRoute.get(0).line.get(0).elementIndex);
        assertEquals(request_unmarshaled.vpath.get(0).pathRoute.get(0).line.get(0).lineSequence, responceDto.vpath.get(0).pathRoute.get(0).line.get(0).lineSequence);
        assertEquals(request_unmarshaled.vpath.get(0).pathInfo.vGrade ,responceDto.vpath.get(0).pathInfo.vGrade);
        assertEquals(request_unmarshaled.vpath.get(0).pathEndPoint.get(0).startEndPoint, responceDto.vpath.get(0).pathEndPoint.get(0).startEndPoint);
        assertEquals(request_unmarshaled.vpath.get(0).pathEndPoint.get(0).neId, responceDto.vpath.get(0).pathEndPoint.get(0).neId);
        assertEquals(request_unmarshaled.vpath.get(0).pathEndPoint.get(0).portId, responceDto.vpath.get(0).pathEndPoint.get(0).portId);
        assertEquals(request_unmarshaled.vpath.get(0).lsp.vLspDownLabel ,responceDto.vpath.get(0).lsp.vLspDownLabel);
        assertEquals(request_unmarshaled.vpath.get(0).lsp.vLspUpLabel ,responceDto.vpath.get(0).lsp.vLspUpLabel);
        assertEquals(request_unmarshaled.vpath.get(0).pw.vPwUpLabel, responceDto.vpath.get(0).pw.vPwUpLabel);
        assertEquals(request_unmarshaled.vpath.get(0).pw.vPwDownLabel, responceDto.vpath.get(0).pw.vPwDownLabel);
        assertEquals(request_unmarshaled.vpath.get(0).qos.vQoSPriority, responceDto.vpath.get(0).qos.vQoSPriority);
        assertEquals(request_unmarshaled.vpath.get(0).qos.vQoSSla, responceDto.vpath.get(0).qos.vQoSSla);
        assertEquals(request_unmarshaled.vpath.get(0).qos.vQoSPir, responceDto.vpath.get(0).qos.vQoSPir, 0);
        assertEquals(request_unmarshaled.vpath.get(0).qos.vQoSCir, responceDto.vpath.get(0).qos.vQoSCir, 0);
        assertEquals(request_unmarshaled.vpath.get(0).vlan.vVlanTagNum ,responceDto.vpath.get(0).vlan.vVlanTagNum);
        assertEquals(request_unmarshaled.vpath.get(0).vlan.vVlanTagMode ,responceDto.vpath.get(0).vlan.vVlanTagMode);
        assertEquals(request_unmarshaled.vpath.get(0).vlan.uni.vVlanVLanIdNniClient, responceDto.vpath.get(0).vlan.uni.vVlanVLanIdNniClient);
        assertEquals(request_unmarshaled.vpath.get(0).vlan.uni.vVlanInnerVlanIdNniClient, responceDto.vpath.get(0).vlan.uni.vVlanInnerVlanIdNniClient);
        assertEquals(request_unmarshaled.vpath.get(0).vlan.uni.vVlanInnerCosNniClient, responceDto.vpath.get(0).vlan.uni.vVlanInnerCosNniClient);
        assertEquals(request_unmarshaled.vpath.get(0).vlan.uni.vVlanOutterCosNniClient, responceDto.vpath.get(0).vlan.uni.vVlanOutterCosNniClient);
        assertEquals(request_unmarshaled.vpath.get(0).vlan.nni.vVlanVlanIdNniLine ,responceDto.vpath.get(0).vlan.nni.vVlanVlanIdNniLine);
        assertEquals(request_unmarshaled.vpath.get(0).vlan.nni.vVlanOutterCosNniLine ,responceDto.vpath.get(0).vlan.nni.vVlanOutterCosNniLine);
        assertEquals(request_unmarshaled.vlink.get(0).vObjectIndex, responceDto.vlink.get(0).vObjectIndex);
        assertEquals(request_unmarshaled.vlink.get(0).vObjectName, responceDto.vlink.get(0).vObjectName);
        assertEquals(request_unmarshaled.vlink.get(0).vObjectStatus, responceDto.vlink.get(0).vObjectStatus);
        assertEquals(request_unmarshaled.vlink.get(0).vObjectDescription, responceDto.vlink.get(0).vObjectDescription);
        assertEquals(request_unmarshaled.vlink.get(0).resourceIndex, responceDto.vlink.get(0).resourceIndex);
        assertEquals(request_unmarshaled.vlink.get(0).vLineSource, responceDto.vlink.get(0).vLineSource);
        assertEquals(request_unmarshaled.vlink.get(0).vLineSink, responceDto.vlink.get(0).vLineSink);
    }

    /**
     * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncSerdesImpl#deserializeFromXml(java.io.InputStream)}.
     * @throws Throwable
     */
    @Test
    public void testDeserializeFromXml_req_res() throws Throwable {
        SdtncResponseDto resDto = SdtncTestUtil.readResFromXml(serdes, DATA_PATH, "sdtnc.res.xml");
        assertNotNull(resDto.head);
        assertNotNull(resDto.auth);
        assertNotNull(resDto.login);
        assertNotNull(resDto.slice);
        assertNotNull(resDto.vpath);
        assertNotNull(resDto.vlink);
    }
}
