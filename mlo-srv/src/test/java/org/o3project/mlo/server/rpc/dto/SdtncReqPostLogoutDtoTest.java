/**
 * SdtncReqPostLogoutDtoTest.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.rpc.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import javax.xml.bind.JAXB;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.impl.rpc.service.SdtncSerdesImpl;
import org.o3project.mlo.server.impl.rpc.service.SdtncTestUtil;
import org.o3project.mlo.server.rpc.dto.SdtncAuthDto;
import org.o3project.mlo.server.rpc.dto.SdtncHeadDto;
import org.o3project.mlo.server.rpc.dto.SdtncLoginDto;
import org.o3project.mlo.server.rpc.dto.SdtncReqPostLoginDto;
import org.o3project.mlo.server.rpc.dto.SdtncReqPostLogoutDto;
import org.o3project.mlo.server.rpc.dto.SdtncRequestDto;

/**
 * SdtncReqPostLogoutDtoTest
 *
 */
public class SdtncReqPostLogoutDtoTest {
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
        //		<CpfIfParamLogin>
        //		    <login loginId="loginId">
        //		        <loginPassword>loginPassword</loginPassword>
        //		        <ipAddress>ipAddress</ipAddress>
        //		        <accountName>accountName</accountName>
        //		        <accountLoginDate>accountLoginDate</accountLoginDate>
        //		        <accountTimeZone>accountTimeZone</accountTimeZone>
        //		    </login>
        //		</CpfIfParamLogin>

        // DTO
        SdtncLoginDto logIn = new SdtncLoginDto();
        logIn.loginId = "loginId";
        logIn.loginPassword = "loginPassword";
        logIn.ipAddress = "ipAddress";
        logIn.accountName = "accountName";
        logIn.accountLoginDate = "accountLoginDate";
        logIn.accountTimeZone = "accountTimeZone";

        SdtncReqPostLogoutDto obj = new SdtncReqPostLogoutDto();
        obj.login = logIn;

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
        SdtncReqPostLogoutDto request_unmarshaled = JAXB.unmarshal(istream, SdtncReqPostLogoutDto.class);

        // 確認　（前DTO　= 後DTO）
        assertEquals(request_unmarshaled.login.loginId, obj.login.loginId);
        assertEquals(request_unmarshaled.login.loginPassword, obj.login.loginPassword);
        assertEquals(request_unmarshaled.login.ipAddress, obj.login.ipAddress);
        assertEquals(request_unmarshaled.login.accountName, obj.login.accountName);
        assertEquals(request_unmarshaled.login.accountLoginDate, obj.login.accountLoginDate);
        assertEquals(request_unmarshaled.login.accountTimeZone, obj.login.accountTimeZone);

    }

    /**
     * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncSerdesImpl#serializeToXml(SdtncRequestDto, java.io.OutputStream)}.
     * @throws Throwable
     */
    @Test
    public void testSerializeToXml_req_req() throws Throwable {
    	SdtncReqPostLoginDto reqDto = new SdtncReqPostLoginDto();
        reqDto.head = new SdtncHeadDto();
        reqDto.auth = new SdtncAuthDto();
        reqDto.login = new SdtncLoginDto();
        assertTrue(SdtncTestUtil.isSameXmlAs(serdes, reqDto, DATA_PATH, "sdtnc.post.logout.req.xml"));
    }

}
