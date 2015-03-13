/**
 * SdtncVlinkDtoTest.java
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
import org.o3project.mlo.server.rpc.dto.SdtncRequestDto;
import org.o3project.mlo.server.rpc.dto.SdtncResponseDto;
import org.o3project.mlo.server.rpc.dto.SdtncVlinkDto;

/**
 * SdtncVlinkDtoTest
 *
 */
public class SdtncVlinkDtoTest {
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
    public void testSerializeToXml_vlink_req() throws Throwable {
    	SdtncResponseDto resDto = new SdtncResponseDto();
        SdtncVlinkDto vlinkDto = new SdtncVlinkDto();
        vlinkDto.vObjectIndex = "123";
        vlinkDto.vObjectName = "234";
        vlinkDto.vObjectStatus = new Integer(345);
        vlinkDto.vObjectDescription = "456";
        vlinkDto.resourceIndex = "567";
        vlinkDto.vLineSource = "678";
        vlinkDto.vLineSink = "789";

        ArrayList<SdtncVlinkDto> vlinkList = new ArrayList<>();
        vlinkList.add(vlinkDto);

        resDto.vlink = vlinkList;
        assertTrue(SdtncTestUtil.isSameXmlAs(resDto, DATA_PATH, "sdtnc.vlink.req.xml"));
    }

    /**
     * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncSerdesImpl#deserializeFromXml(java.io.InputStream)}.
     * @throws Throwable
     */
    @Test
    public void testDeserializeFromXml_vlink_res() throws Throwable {
        SdtncResponseDto resDto = SdtncTestUtil.readResFromXml(serdes, DATA_PATH, "sdtnc.vlink.res.xml");
        assertEquals("123", resDto.vlink.get(0).vObjectIndex);
        assertEquals("234", resDto.vlink.get(0).vObjectName);
        assertEquals(new Integer(345), resDto.vlink.get(0).vObjectStatus);
        assertEquals("456", resDto.vlink.get(0).vObjectDescription);
        assertEquals("567", resDto.vlink.get(0).resourceIndex);
        assertEquals("678", resDto.vlink.get(0).vLineSource);
        assertEquals("789", resDto.vlink.get(0).vLineSink);
    }
}
