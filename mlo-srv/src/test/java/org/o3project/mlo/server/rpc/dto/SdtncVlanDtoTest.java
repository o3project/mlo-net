/**
 * SdtncVlanDtoTest.java
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
import org.o3project.mlo.server.rpc.dto.SdtncNniDto;
import org.o3project.mlo.server.rpc.dto.SdtncReqPostCreatePwDto;
import org.o3project.mlo.server.rpc.dto.SdtncRequestDto;
import org.o3project.mlo.server.rpc.dto.SdtncResponseDto;
import org.o3project.mlo.server.rpc.dto.SdtncUniDto;
import org.o3project.mlo.server.rpc.dto.SdtncVlanDto;
import org.o3project.mlo.server.rpc.dto.SdtncVpathDto;

/**
 * SdtncVlanDtoTest
 *
 */
public class SdtncVlanDtoTest {
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
    public void testSerializeToXml_vlan_req() throws Throwable {
    	SdtncReqPostCreatePwDto reqDto = new SdtncReqPostCreatePwDto();
        SdtncVpathDto vpathDto = new SdtncVpathDto();
        SdtncVlanDto vlanDto = new SdtncVlanDto();
        vlanDto.vVlanTagNum = new Integer(234);
        vlanDto.vVlanTagMode = new Integer(235);
        vlanDto.uni = new SdtncUniDto();
        vlanDto.nni = new SdtncNniDto();
        vpathDto.vlan = vlanDto;

        ArrayList<SdtncVpathDto> vpathArray = new ArrayList<>();
        vpathArray.add(vpathDto);

        reqDto.vpath = vpathArray;
        assertTrue(SdtncTestUtil.isSameXmlAs(serdes, reqDto, DATA_PATH, "sdtnc.vlan.req.xml"));
    }

    /**
     * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncSerdesImpl#deserializeFromXml(java.io.InputStream)}.
     * @throws Throwable
     */
    @Test
    public void testDeserializeFromXml_vlan_res() throws Throwable {
        SdtncResponseDto resDto = SdtncTestUtil.readResFromXml(serdes, DATA_PATH, "sdtnc.vlan.res.xml");
        assertEquals(new Integer(234), resDto.vpath.get(0).vlan.vVlanTagNum);
        assertEquals(new Integer(235), resDto.vpath.get(0).vlan.vVlanTagMode);
        assertNotNull(resDto.vpath.get(0).vlan.uni);
        assertNotNull(resDto.vpath.get(0).vlan.nni);
    }
}
