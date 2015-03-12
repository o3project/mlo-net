/**
 * SdtncPathRouteDtoTest.java
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
import org.o3project.mlo.server.rpc.dto.SdtncLineDto;
import org.o3project.mlo.server.rpc.dto.SdtncPathRouteDto;
import org.o3project.mlo.server.rpc.dto.SdtncReqPostCreatePwDto;
import org.o3project.mlo.server.rpc.dto.SdtncRequestDto;
import org.o3project.mlo.server.rpc.dto.SdtncResponseDto;
import org.o3project.mlo.server.rpc.dto.SdtncVpathDto;

/**
 * SdtncPathRouteDtoTest
 *
 */
public class SdtncPathRouteDtoTest {
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
    public void testSerializeToXml_pathroute_req() throws Throwable {
    	SdtncReqPostCreatePwDto reqDto = new SdtncReqPostCreatePwDto();
        SdtncVpathDto vpathDto = new SdtncVpathDto();
        SdtncLineDto line = new SdtncLineDto();
        SdtncPathRouteDto pathrouteDto = new SdtncPathRouteDto();
        pathrouteDto.lineGroupWorkingProtection = new Integer(444);

        ArrayList<SdtncLineDto> lineArrray = new ArrayList<>();
        lineArrray.add(line);

        ArrayList<SdtncPathRouteDto> pathrouteArray = new ArrayList<>();
        pathrouteArray.add(pathrouteDto);

        ArrayList<SdtncVpathDto> vpathArray = new ArrayList<>();
        vpathArray.add(vpathDto);

        pathrouteDto.line = lineArrray;
        vpathDto.pathRoute = pathrouteArray;
        reqDto.vpath = vpathArray;
        assertTrue(SdtncTestUtil.isSameXmlAs(serdes, reqDto, DATA_PATH, "sdtnc.pathroute.req.xml"));
    }

    /**
     * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncSerdesImpl#deserializeFromXml(java.io.InputStream)}.
     * @throws Throwable
     */
    @Test
    public void testDeserializeFromXml_pathroute_res() throws Throwable {
        SdtncResponseDto resDto = SdtncTestUtil.readResFromXml(serdes, DATA_PATH, "sdtnc.pathroute.res.xml");
        assertEquals(new Integer(444), resDto.vpath.get(0).pathRoute.get(0).lineGroupWorkingProtection);
        assertNotNull(resDto.vpath.get(0).pathRoute.get(0).line);
    }
}
