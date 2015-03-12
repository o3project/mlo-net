/**
 * SdtnclineDtoTest.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.rpc.dto;

import static org.junit.Assert.assertEquals;
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
 * SdtnclineDtoTest
 *
 */
public class SdtnclineDtoTest {
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
    public void testSerializeToXml_line_req() throws Throwable {
    	SdtncReqPostCreatePwDto reqDto = new SdtncReqPostCreatePwDto();
        SdtncVpathDto vpathDto = new SdtncVpathDto();
        SdtncPathRouteDto pathrouteDto = new SdtncPathRouteDto();
        SdtncLineDto linkDto = new SdtncLineDto();
        linkDto.elementIndex = "elementIndex";
        linkDto.lineSequence = new Integer(333);

        ArrayList<SdtncLineDto> linkDtoArray = new ArrayList<>();
        linkDtoArray.add(linkDto);

        ArrayList<SdtncPathRouteDto> pathRouteArray = new ArrayList<>();
        pathRouteArray.add(pathrouteDto);

        ArrayList<SdtncVpathDto> vpathArray = new ArrayList<>();
        vpathArray.add(vpathDto);

        pathrouteDto.line = linkDtoArray;
        vpathDto.pathRoute = pathRouteArray;
        reqDto.vpath = vpathArray;
        assertTrue(SdtncTestUtil.isSameXmlAs(serdes, reqDto, DATA_PATH, "sdtnc.line.req.xml"));
    }

    /**
     * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncSerdesImpl#deserializeFromXml(java.io.InputStream)}.
     * @throws Throwable
     */
    @Test
    public void testDeserializeFromXml_line_res() throws Throwable {
        SdtncResponseDto resDto = SdtncTestUtil.readResFromXml(serdes, DATA_PATH, "sdtnc.line.res.xml");
        assertEquals("elementIndex", resDto.vpath.get(0).pathRoute.get(0).line.get(0).elementIndex);
        assertEquals(new Integer(333), resDto.vpath.get(0).pathRoute.get(0).line.get(0).lineSequence);
    }
}
