/**
 * SdtncQosDtoTest.java
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
import org.o3project.mlo.server.rpc.dto.SdtncQosDto;
import org.o3project.mlo.server.rpc.dto.SdtncReqPostCreatePwDto;
import org.o3project.mlo.server.rpc.dto.SdtncRequestDto;
import org.o3project.mlo.server.rpc.dto.SdtncResponseDto;
import org.o3project.mlo.server.rpc.dto.SdtncVpathDto;

/**
 * SdtncQosDtoTest
 *
 */
public class SdtncQosDtoTest {
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
    public void testSerializeToXml_qos_req() throws Throwable {
    	SdtncReqPostCreatePwDto reqDto = new SdtncReqPostCreatePwDto();
        SdtncVpathDto vpathDto = new SdtncVpathDto();
        SdtncQosDto qpsDto = new SdtncQosDto();
        qpsDto.vQoSPriority = new Integer(888);
        qpsDto.vQoSSla = new Integer(777);
        qpsDto.vQoSPir = new Integer(666);
        qpsDto.vQoSCir = new Integer(555);
        vpathDto.qos = qpsDto;
        ArrayList<SdtncVpathDto> vpathArray = new ArrayList<>();
        vpathArray.add(vpathDto);
        reqDto.vpath = vpathArray;
        assertTrue(SdtncTestUtil.isSameXmlAs(serdes, reqDto, DATA_PATH, "sdtnc.qos.req.xml"));
    }

    /**
     * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncSerdesImpl#deserializeFromXml(java.io.InputStream)}.
     * @throws Throwable
     */
    @Test
    public void testDeserializeFromXml_qos_res() throws Throwable {
        SdtncResponseDto resDto = SdtncTestUtil.readResFromXml(serdes, DATA_PATH, "sdtnc.qos.res.xml");
        assertEquals(new Integer(888), resDto.vpath.get(0).qos.vQoSPriority);
        assertEquals(new Integer(777), resDto.vpath.get(0).qos.vQoSSla);
        assertEquals(new Integer(666), resDto.vpath.get(0).qos.vQoSPir);
        assertEquals(new Integer(555), resDto.vpath.get(0).qos.vQoSCir);
    }
}

