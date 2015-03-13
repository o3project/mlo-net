/**
 * SdtncVpathDtoTest.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.rpc.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.impl.rpc.service.SdtncSerdesImpl;
import org.o3project.mlo.server.impl.rpc.service.SdtncTestUtil;
import org.o3project.mlo.server.rpc.dto.SdtncLspDto;
import org.o3project.mlo.server.rpc.dto.SdtncPathEndPointDto;
import org.o3project.mlo.server.rpc.dto.SdtncPathInfoDto;
import org.o3project.mlo.server.rpc.dto.SdtncPathRouteDto;
import org.o3project.mlo.server.rpc.dto.SdtncPwDto;
import org.o3project.mlo.server.rpc.dto.SdtncQosDto;
import org.o3project.mlo.server.rpc.dto.SdtncReqPostCreatePwDto;
import org.o3project.mlo.server.rpc.dto.SdtncRequestDto;
import org.o3project.mlo.server.rpc.dto.SdtncResponseDto;
import org.o3project.mlo.server.rpc.dto.SdtncVlanDto;
import org.o3project.mlo.server.rpc.dto.SdtncVpathDto;

/**
 * SdtncVpathDtoTest
 *
 */
public class SdtncVpathDtoTest {
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

    /**
     * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncSerdesImpl#serializeToXml(SdtncRequestDto, java.io.OutputStream)}.
     * @throws Throwable
     */
    @Test
    public void testSerializeToXml_vpath_req() throws Throwable {
    	SdtncReqPostCreatePwDto reqDto = new SdtncReqPostCreatePwDto();
        SdtncVpathDto vpathDto = new SdtncVpathDto();
        vpathDto.vObjectIndex = "vObjectIndex";
        vpathDto.vObjectName = "vObjectName";
        vpathDto.vObjectStatus = new Integer(126);
        vpathDto.vObjectDescription = "vObjectDescription";
        vpathDto.resourceIndex = "resourceIndex";

        SdtncPathRouteDto pathRouteDto = new SdtncPathRouteDto();
        ArrayList<SdtncPathRouteDto> listpathRoute = new ArrayList<>();
        listpathRoute.add(pathRouteDto);

        SdtncPathEndPointDto pathEndPointDto = new SdtncPathEndPointDto();
        ArrayList<SdtncPathEndPointDto> listpathEndPoint = new ArrayList<>();
        listpathEndPoint.add(pathEndPointDto);

        vpathDto.pathRoute = listpathRoute;
        vpathDto.pathInfo = new SdtncPathInfoDto();
        vpathDto.pathEndPoint = listpathEndPoint;
        vpathDto.lsp = new SdtncLspDto();
        vpathDto.pw = new SdtncPwDto();
        vpathDto.qos = new SdtncQosDto();
        vpathDto.vlan = new SdtncVlanDto();

        ArrayList<SdtncVpathDto> vpathArray = new ArrayList<>();
        vpathArray.add(vpathDto);

        reqDto.vpath = vpathArray;
        assertTrue(SdtncTestUtil.isSameXmlAs(serdes, reqDto, DATA_PATH, "sdtnc.vpath.req.xml"));
    }

    /**
     * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncSerdesImpl#deserializeFromXml(java.io.InputStream)}.
     * @throws Throwable
     */
    @Test
    public void testDeserializeFromXml_vpath_res() throws Throwable {
        SdtncResponseDto resDto = SdtncTestUtil.readResFromXml(serdes, DATA_PATH, "sdtnc.vpath.res.xml");
        assertEquals("vObjectIndex", resDto.vpath.get(0).vObjectIndex);
        assertEquals("vObjectName", resDto.vpath.get(0).vObjectName);
        assertEquals(new Integer(126), resDto.vpath.get(0).vObjectStatus);
        assertEquals("vObjectDescription", resDto.vpath.get(0).vObjectDescription);
        assertEquals("resourceIndex", resDto.vpath.get(0).resourceIndex);
        assertNotNull(resDto.vpath.get(0).pathRoute);
        assertNotNull(resDto.vpath.get(0).pathInfo);
        assertNotNull(resDto.vpath.get(0).pathEndPoint);
        assertNotNull(resDto.vpath.get(0).lsp);
        assertNotNull(resDto.vpath.get(0).pw);
        assertNotNull(resDto.vpath.get(0).qos);
        assertNotNull(resDto.vpath.get(0).vlan);
    }
}
