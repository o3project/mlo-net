/**
 * SdtncServiceImplTest.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.rpc.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.bind.JAXB;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.o3project.mlo.server.impl.logic.SdtncInvokerImplDummy;
import org.o3project.mlo.server.impl.rpc.service.SdtncServiceImpl;
import org.o3project.mlo.server.logic.InternalException;
import org.o3project.mlo.server.logic.MloException;
import org.o3project.mlo.server.rpc.dto.SdtncReqPostCreatePwDto;
import org.o3project.mlo.server.rpc.dto.SdtncReqPostLoginDto;
import org.o3project.mlo.server.rpc.dto.SdtncReqPostLogoutDto;
import org.o3project.mlo.server.rpc.dto.SdtncRequestDto;
import org.o3project.mlo.server.rpc.dto.SdtncResponseDto;

public class SdtncServiceImplTest {

    private SdtncServiceImpl sdtncServiceImpl;
    private static final String DATA_PATH = "src/test/resources/org/o3project/mlo/server/logic/data/base";
    private SdtncInvokerImplDummy dummyInvoker;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        sdtncServiceImpl = new SdtncServiceImpl();
        dummyInvoker = new SdtncInvokerImplDummy();
    }

    @After
    public void tearDown() throws Exception {
        sdtncServiceImpl = null;
        dummyInvoker = null;
    }

    @Test
    public void loginTest() {
        try {
            dummyInvoker.setXmlName("login.res.xml");// 受信レスポンスファイル指定
            sdtncServiceImpl.setSdtncInvoker(dummyInvoker);// ダミークラス設定
            SdtncRequestDto requestDto = createTempDto("login.req.xml", SdtncReqPostLoginDto.class);//　リクエストXML DTO変換
            SdtncResponseDto responseDto = sdtncServiceImpl.login(requestDto); // 実行
            assertEquals(responseDto.head.errorDetail, "");
        } catch (MloException e) {
            fail();
        } catch (Throwable e) {
            fail();
        }
        assertEquals("", "");
    }

    @Test
    public void logoutTest() {
        try {
            dummyInvoker.setXmlName("logout.res.xml");// 受信レスポンスファイル指定
            sdtncServiceImpl.setSdtncInvoker(dummyInvoker);// ダミークラス設定
            SdtncRequestDto requestDto = createTempDto("logout.req.xml", SdtncReqPostLogoutDto.class);//　リクエストXML DTO変換
            SdtncResponseDto responseDto = sdtncServiceImpl.logout(requestDto);// 実行
            assertEquals(responseDto.head.errorDetail, "");
        } catch (MloException e) {
            fail();
        } catch (Throwable e) {
            fail();
        }
    }

    @Test
    public void getLspResourceTest() {
        try {
            HashMap<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("TEST_KEY", "TEST_VALUE");
            paramMap.put("TEST_KEY2", "TEST_VALUE2");
            paramMap.put("TEST_KEY3", "TEST_VALUE3");
            dummyInvoker.setXmlName("MultVLink.res.xml");// 受信レスポンスファイル指定
            sdtncServiceImpl.setSdtncInvoker(dummyInvoker);// ダミークラス設定
            SdtncResponseDto responseDto = sdtncServiceImpl.getLspResource(paramMap); // 実行
            assertEquals(responseDto.vlink.get(0).resourceIndex, "1");
        } catch (MloException e) {
            fail();
        } catch (Throwable e) {
            fail();
        }
    }

    @Test
    public void createPwTest() {
        try {
            dummyInvoker.setXmlName("createVpath.res.xml");// 受信レスポンスファイル指定
            sdtncServiceImpl.setSdtncInvoker(dummyInvoker);// ダミークラス設定
            SdtncRequestDto requestDto = createTempDto("createVpath.req.xml", SdtncReqPostCreatePwDto.class);//　リクエストXML DTO変換
            SdtncResponseDto responseDto = sdtncServiceImpl.createPw(requestDto); // 実行
            assertEquals(responseDto.vpath.get(0).vObjectIndex, "vObjectIndex");
        } catch (MloException e) {
            fail();
        } catch (Throwable e) {
            fail();
        }
    }

    @Test
    public void deletePwTest() {
        try {
            dummyInvoker.setXmlName("delVpath.res.xml");// 受信レスポンスファイル指定
            sdtncServiceImpl.setSdtncInvoker(dummyInvoker);// ダミークラス設定
            HashMap<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("TEST_KEY", "TEST_VALUE");
            paramMap.put("TEST_KEY2", "TEST_VALUE2");
            paramMap.put("TEST_KEY3", "TEST_VALUE3");
            SdtncResponseDto responseDto = sdtncServiceImpl.deletePw(paramMap); // 実行
            assertEquals(responseDto.vpath.get(0).vObjectIndex, "vObjectIndex");
        } catch (MloException e) {
            fail();
        } catch (Throwable e) {
            fail();
        }
    }

    @Test
    public void getPwTest() {
        try {
            HashMap<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("TEST_KEY", "TEST_VALUE");
            paramMap.put("TEST_KEY2", "TEST_VALUE2");
            paramMap.put("TEST_KEY3", "TEST_VALUE3");
            dummyInvoker.setXmlName("Vpath.res.xml");// 受信レスポンスファイル指定
            sdtncServiceImpl.setSdtncInvoker(dummyInvoker);// ダミークラス設定
            SdtncResponseDto responseDto = sdtncServiceImpl.getPw(paramMap); // 実行
            assertEquals(responseDto.vpath.get(0).vObjectIndex, "vObjectIndex");
        } catch (MloException e) {
            fail();
        } catch (Throwable e) {
            fail();
        }
    }
    
    /**
	 * テンプレートファイル(xml）を読み込み、リクエストDtoを作成する
	 * @param xmlFile
	 * @return リクエストDto
	 * @throws MloException 
	 */
	private static <T> T createTempDto(String xmlFileName, Class<T> type) throws MloException{
		T reqDto = null;
		InputStream istream = null;
		File xmlFile = new File(DATA_PATH, xmlFileName);
		try {
			istream = new FileInputStream(xmlFile);
			reqDto = JAXB.unmarshal(istream, type);
		} catch (FileNotFoundException e) {
			// 基本、このルートには入らない
			throw new InternalException("templete file is not found [file = " + xmlFile.getName() +"]");
		} finally {
			if (istream != null) {
				try {
					istream.close();
				} catch (IOException e) {
					reqDto = null;
					e.printStackTrace();
				}
			}
		}
		
		return reqDto;
	}
}
