/**
 * SdtncValidatorTest.java
 * (C) 2013, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.impl.rpc.service;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.o3project.mlo.server.impl.rpc.service.SdtncSerdesImpl;
import org.o3project.mlo.server.impl.rpc.service.SdtncValidator;
import org.o3project.mlo.server.logic.InternalException;
import org.o3project.mlo.server.logic.MloException;
import org.o3project.mlo.server.rpc.dto.SdtncResponseDto;

/**
 * SdtncValidatorTest
 *
 */
public class SdtncValidatorTest {

    private static final String DATA_PATH = "src/test/resources/org/o3project/mlo/server/logic/data/base";

    private SdtncSerdesImpl serdes;

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

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
     * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncValidator#checkLength(String, String)}.
     */
    @Test
    public void checkLengthTest001() {

        try {
            SdtncValidator.checkLength("a", 5, "test1");
        } catch (InternalException e) {
            fail();
        }

    }

    /**
     * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncValidator#checkLength(String, String)}.
     */
    @Test
    public void checkLengthTest002() {

        String name_of_properties = "test";

        try {
            SdtncValidator.checkLength("abcdef", 5, name_of_properties);
            fail("Error");
        } catch (InternalException e) {
            assertTrue(e instanceof InternalException);
            assertEquals(e.getMessage(), "SDTNCBadResponse/InvalidData/" + name_of_properties);
        }
    }
    
    /**
     * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncValidator#checkLength(String, String)}.
     */
    @Test
    public void checkLengthTest003() {

        String name_of_properties = "test";

        try {
            SdtncValidator.checkLength("abcde", 5, name_of_properties);
        } catch (InternalException e) {
            fail("Error");
        }
    }

    /**
     * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncValidator#checkLength(String, String)}.
     */
    @Test
    public void checkLengthTest004() {

        String name_of_properties = "test";

        try {
            SdtncValidator.checkLength(null, 5, name_of_properties);
            fail("Error");
        } catch (InternalException e) {
            assertTrue(e instanceof InternalException);
            assertEquals(e.getMessage(), "SDTNCBadResponse/InvalidData/" + name_of_properties);
        }
    }
    
    /**
     * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncValidator#checkLength(String, String)}.
     */
    @Test
    public void checkLengthTest005() {

        String name_of_properties = "test";

        try {
            SdtncValidator.checkLength("", 5, name_of_properties);
            fail("Error");
        } catch (InternalException e) {
            assertTrue(e instanceof InternalException);
            assertEquals(e.getMessage(), "SDTNCBadResponse/InvalidData/" + name_of_properties);
        }
    }
    
    /**
     * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncValidator#checkNull(Object, String)}.
     */
    @Test
    public void checkNullTest_001() {
        try {
            SdtncValidator.checkNull(null, "unitTestError");
            fail("Error");
        } catch (MloException e) {
            assertTrue(e instanceof InternalException);
            assertEquals(e.getMessage(), "SDTNCBadResponse/InvalidData/" + "unitTestError");
        }
    }

    /**
     * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncValidator#checkNull(Object, jString)}.
     */
    @Test
    public void checkNullTest_002() {
        try {
            SdtncValidator.checkNull("123", "unitTestError");
        } catch (MloException e) {
            fail("Error");
        }
    }

    /**
     * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncValidator#checkNumRange(Integer, int, int, String)}.
     */
    @Test
    public void checkNumRangeTest_001() {
        int target = 9;
        try {
            SdtncValidator.checkNumRange(target, 0, 8, "sample");
            fail("Error");
        } catch (MloException e) {
            assertEquals(e.getMessage(), "SDTNCBadResponse/InvalidData/" + "sample");
            assertTrue(e instanceof InternalException);
        }
    }

    /**
     * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncValidator#checkNumRange(Integer, int, int, String)}.
     */
    @Test
    public void checkNumRangeTest_002() {
        int target = 8;
        try {
            SdtncValidator.checkNumRange(target, 0, 8, "sample");
        } catch (MloException e) {
            fail("Error");
        }
    }
    
    /**
     * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncValidator#checkNumRange(Integer, int, int, String)}.
     */
    @Test
    public void checkNumRangeTest_003() {
        int target = 0;
        try {
            SdtncValidator.checkNumRange(target, 0, 8, "sample");
        } catch (MloException e) {
            fail("Error");
        }
    }
    
    /**
     * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncValidator#checkNumRange(Integer, int, int, String)}.
     */
    @Test
    public void checkNumRangeTest_004() {
        int target = -1;
        try {
            SdtncValidator.checkNumRange(target, 0, 8, "sample");
            fail("Error");
        } catch (MloException e) {
            assertTrue(e instanceof InternalException);
        }
    }

    /**
     * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncValidator#checkNumRange(Integer, int, int, String)}.
     */
    @Test
    public void checkNumRangeTest_005() {
        try {
            SdtncValidator.checkNumRange(null, 0, 8, "sample");
            fail("Error");
        } catch (MloException e) {
            assertEquals(e.getMessage(), "SDTNCBadResponse/InvalidData/" + "sample");
            assertTrue(e instanceof InternalException);
        }
    }
    
    /**
     * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncValidator#validateLogin(SdtncResponseDto)}.
     */
    @Test
    public void validateLoginTest_001() {
        try {
            SdtncResponseDto res = SdtncTestUtil.readResFromXml(serdes, DATA_PATH, "login.res.xml");
            SdtncValidator.validateLogin(res);
        } catch (InternalException e) {
            fail("Error");
        } catch (Throwable e1) {
            fail("Error");
        }
    }

    /**
     * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncValidator#validateLogin(SdtncResponseDto)}.
     */
    @Test
    public void validateLoginTest_Error_001() {
    	SdtncResponseDto res = null;
        try {
            res = SdtncTestUtil.readResFromXml(serdes, DATA_PATH, "login.res.xml");
            res.head.majorResponseCode = 31;
            res.head.minorResponseCode = 2;
            res.head.errorDetail = "login error";
            res.auth.token = null;
            SdtncValidator.validateLogin(res);
            fail("Error");
        } catch (InternalException e) {
            assertTrue(e instanceof InternalException);
            assertEquals(e.getMessage(), "SDTNCBadResponse/InvalidData/" + "Login failed : majorResponseCode=" + res.head.majorResponseCode + 
                                         ", minorResponseCode=" + res.head.minorResponseCode + 
                                         ", errorDetail=" + res.head.errorDetail);
        } catch (Throwable e1) {
            fail("Error");
        }
    }
    
    /**
     * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncValidator#validateLogin(SdtncResponseDto)}.
     */
    @Test
    public void validateLoginTest_Error_002() {
    	SdtncResponseDto res = null;
        try {
            res = SdtncTestUtil.readResFromXml(serdes, DATA_PATH, "login.res.xml");
            res.head.majorResponseCode = 31;
            res.head.minorResponseCode = 2;
            res.head.errorDetail = "login error";
            res.auth.token = "111111111022222222203333333330444";
            SdtncValidator.validateLogin(res);
            fail("Error");
        } catch (InternalException e) {
            assertTrue(e instanceof InternalException);
            assertEquals(e.getMessage(), "SDTNCBadResponse/InvalidData/" + "Login failed : majorResponseCode=" + res.head.majorResponseCode + 
                    ", minorResponseCode=" + res.head.minorResponseCode + 
                    ", errorDetail=" + res.head.errorDetail);
        } catch (Throwable e1) {
            fail("Error");
        }
    }
    
    /**
     * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncValidator#validateLogin(SdtncResponseDto)}.
     */
    @Test
    public void validateLoginTest_Error_003() {
    	SdtncResponseDto res = null;
        try {
            res = SdtncTestUtil.readResFromXml(serdes, DATA_PATH, "login.res.xml");
            res.head.majorResponseCode = 31;
            res.head.minorResponseCode = 2;
            res.head.errorDetail = "login error";
            res.auth = null;
            SdtncValidator.validateLogin(res);
            fail("Error");
        } catch (InternalException e) {
            assertTrue(e instanceof InternalException);
            assertEquals(e.getMessage(), "SDTNCBadResponse/InvalidData/" + "Login failed : majorResponseCode=" + res.head.majorResponseCode + 
                    ", minorResponseCode=" + res.head.minorResponseCode + 
                    ", errorDetail=" + res.head.errorDetail);
        } catch (Throwable e1) {
            fail("Error");
        }
    }

    /**
     * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncValidator#validateLogin(SdtncResponseDto)}.
     */
    @Test
    public void validateLoginTest_Error_004() {
    	SdtncResponseDto res = null;
        try {
            res = SdtncTestUtil.readResFromXml(serdes, DATA_PATH, "login.res.xml");
            res.head.majorResponseCode = 31;
            res.head.minorResponseCode = 2;
            res.head.errorDetail = "login error";
            res.auth.token = "";
            SdtncValidator.validateLogin(res);
            fail("Error");
        } catch (InternalException e) {
            assertTrue(e instanceof InternalException);
            assertEquals(e.getMessage(), "SDTNCBadResponse/InvalidData/" + "Login failed : majorResponseCode=" + res.head.majorResponseCode + 
                    ", minorResponseCode=" + res.head.minorResponseCode + 
                    ", errorDetail=" + res.head.errorDetail);
        } catch (Throwable e1) {
            fail("Error");
        }
    }

    /**
     * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncValidator#validateLogout(SdtncResponseDto)}.
     */
    @Test
    public void validateLogoutTest_001() {
        try {
            SdtncResponseDto res = SdtncTestUtil.readResFromXml(serdes, DATA_PATH, "logout.res.xml");
            SdtncValidator.validateLogout(res);
        } catch (InternalException e) {
            fail("Error");
        } catch (Throwable e1) {
            fail("Error");
        }
    }

    /**
     * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncValidator#validateLogout(SdtncResponseDto)}.
     */
    @Test
    public void validateLogoutTest_Error_001() {
        try {
            SdtncResponseDto res = SdtncTestUtil.readResFromXml(serdes, DATA_PATH, "logout.res.xml");
            res.login.loginId = null;
            SdtncValidator.validateLogout(res);
            fail("Error");
        } catch (InternalException e) {
            assertTrue(e instanceof InternalException);
            assertEquals(e.getMessage(), "SDTNCBadResponse/InvalidData/" + "loginId");
        } catch (Throwable e1) {
            fail("Error");
        }
    }

    /**
     * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncValidator#validateGetLspResource(SdtncResponseDto)}.
     */
    @Test
    public void validateGetLspResourceTest_001() {
        try {
            SdtncResponseDto res = SdtncTestUtil.readResFromXml(serdes, DATA_PATH, "MultVLink.res.xml");
            SdtncValidator.validateGetLspResource(res);
        } catch (InternalException e) {
            fail("Error");
        } catch (Throwable e1) {
            fail("Error");
        }
    }

    /**
     * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncValidator#validateGetLspResource(SdtncResponseDto)}.
     */
    @Test
    public void validateGetLspResourceTest_002() {
        try {
            SdtncResponseDto res = SdtncTestUtil.readResFromXml(serdes, DATA_PATH, "MultVLink.res.xml");
            res.slice.groupIndex = null;
            SdtncValidator.validateGetLspResource(res);
            fail("Error");
        } catch (InternalException e) {
            assertTrue(e instanceof InternalException);
            assertEquals(e.getMessage(), "SDTNCBadResponse/InvalidData/" + "groupIndex");
        } catch (Throwable e1) {
            fail("Error");
        }
    }
    
    /**
     * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncValidator#validateGetLspResource(SdtncResponseDto)}.
     */
    @Test
    public void validateGetLspResourceTest_003() {
        try {
            SdtncResponseDto res = SdtncTestUtil.readResFromXml(serdes, DATA_PATH, "MultVLink.res.xml");
            res.slice.groupIndex = "11111111102222222220333333333044444444405555555550666666666077777";
            SdtncValidator.validateGetLspResource(res);
            fail("Error");
        } catch (InternalException e) {
            assertTrue(e instanceof InternalException);
            assertEquals(e.getMessage(), "SDTNCBadResponse/InvalidData/" + "groupIndex");
        } catch (Throwable e1) {
            fail("Error");
        }
    }

    /**
     * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncValidator#validateGetPw(SdtncResponseDto)}.
     */
    @Test
    public void validateGetPwTest_001() {
        try {
            SdtncResponseDto res = SdtncTestUtil.readResFromXml(serdes, DATA_PATH, "Vpath.res.xml");
            SdtncValidator.validateGetPw(res);
        } catch (InternalException e) {
            fail("Error");
        } catch (Throwable e1) {
            fail("Error");
        }
    }
    
    /**
     * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncValidator#validateGetPw(SdtncResponseDto)}.
     */
    @Test
    public void validateGetPwTest_Error_001() {
        try {
            SdtncResponseDto res = SdtncTestUtil.readResFromXml(serdes, DATA_PATH, "Vpath.res.xml");
            res.slice = null;
            SdtncValidator.validateGetPw(res);
            fail("Error");
        } catch (InternalException e) {
            assertTrue(e instanceof InternalException);
            assertEquals(e.getMessage(), "SDTNCBadResponse/InvalidData/" + "slice");
        } catch (Throwable e1) {
            fail("Error");
        }
    }
    
    /**
     * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncValidator#validateGetPw(SdtncResponseDto)}.
     */
    @Test
    public void validateGetPwTest_Error_002() {
        try {
            SdtncResponseDto res = SdtncTestUtil.readResFromXml(serdes, DATA_PATH, "Vpath.res.xml");
            res.vpath.get(0).vObjectIndex = null;
            SdtncValidator.validateGetPw(res);
            fail("Error");
        } catch (InternalException e) {
            assertTrue(e instanceof InternalException);
            assertEquals(e.getMessage(), "SDTNCBadResponse/InvalidData/" + "vObjectIndex");
        } catch (Throwable e1) {
            fail("Error");
        }
    }

    /**
     * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncValidator#validateGetPw(SdtncResponseDto)}.
     */
    @Test
    public void validateGetPwTest_Error_003() {
        try {
            SdtncResponseDto res = SdtncTestUtil.readResFromXml(serdes, DATA_PATH, "Vpath.res.xml");
            res.vpath.get(0).vObjectIndex = "11111111102222222220333333333044444444405555555550666666666077777";
            SdtncValidator.validateGetPw(res);
            fail("Error");
        } catch (InternalException e) {
            assertTrue(e instanceof InternalException);
            assertEquals(e.getMessage(), "SDTNCBadResponse/InvalidData/" + "vObjectIndex");
        } catch (Throwable e1) {
            fail("Error");
        }
    }

    /**
     * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncValidator#validateCreatePw(SdtncResponseDto)}.
     */
    @Test
    public void validateCreatePwTest_001() {
        try {
            SdtncResponseDto res = SdtncTestUtil.readResFromXml(serdes, DATA_PATH, "createVpath.res.xml");
            SdtncValidator.validateCreatePw(res);
        } catch (InternalException e) {
            fail("Error");
        } catch (Throwable e1) {
            fail("Error");
        }
    }

    /**
     * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncValidator#validateCreatePw(SdtncResponseDto)}.
     */
    @Test
    public void validateCreatePwTest_Error_001() {
    	try {
            SdtncResponseDto res = SdtncTestUtil.readResFromXml(serdes, DATA_PATH, "createVpath.res.xml");
            res.vpath.get(0).vObjectIndex = null;
            SdtncValidator.validateCreatePw(res);
            fail("Error");
        } catch (InternalException e) {
            assertTrue(e instanceof InternalException);
            assertEquals(e.getMessage(), "SDTNCBadResponse/InvalidData/Creation of PW failed.");
        } catch (Throwable e1) {
            fail("Error");
        }
    }
    
    /**
     * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncValidator#validateCreatePw(SdtncResponseDto)}.
     */
    @Test
    public void validateCreatePwTest_Error_002() {
        try {
            SdtncResponseDto res = SdtncTestUtil.readResFromXml(serdes, DATA_PATH, "Vpath.res.xml");
            res.vpath.get(0).vObjectIndex = "11111111102222222220333333333044444444405555555550666666666077777";
            SdtncValidator.validateCreatePw(res);
            fail("Error");
        } catch (InternalException e) {
            assertTrue(e instanceof InternalException);
            assertEquals(e.getMessage(), "SDTNCBadResponse/InvalidData/Creation of PW failed.");
        } catch (Throwable e1) {
            fail("Error");
        }
    }

    /**
     * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncValidator#validateCreatePw(SdtncResponseDto)}.
     */
    @Test
    public void validateCreatePwTest_Error_003() {
        try {
            SdtncResponseDto res = SdtncTestUtil.readResFromXml(serdes, DATA_PATH, "Vpath.res.xml");
            res.vpath.get(0).vObjectIndex = "";
            SdtncValidator.validateCreatePw(res);
            fail("Error");
        } catch (InternalException e) {
            assertTrue(e instanceof InternalException);
            assertEquals(e.getMessage(), "SDTNCBadResponse/InvalidData/Creation of PW failed.");
        } catch (Throwable e1) {
            fail("Error");
        }
    }

    /**
     * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncValidator#validateCreatePw(SdtncResponseDto)}.
     */
    @Test
    public void validateCreatePwTest_Error_004() {
        try {
            SdtncResponseDto res = SdtncTestUtil.readResFromXml(serdes, DATA_PATH, "Vpath.res.xml");
            res.vpath = null;
            SdtncValidator.validateCreatePw(res);
            fail("Error");
        } catch (InternalException e) {
            assertTrue(e instanceof InternalException);
            assertEquals(e.getMessage(), "SDTNCBadResponse/InvalidData/Creation of PW failed.");
        } catch (Throwable e1) {
            fail("Error");
        }
    }

    /**
     * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncValidator#validateDeletePw(SdtncResponseDto)}.
     */
    @Test
    public void validateDeletePwTest_001() {
        try {
            SdtncResponseDto res = SdtncTestUtil.readResFromXml(serdes, DATA_PATH, "delVpath.res.xml");
            SdtncValidator.validateDeletePw(res);
        } catch (InternalException e) {
            fail("Error");
        } catch (Throwable e1) {
            fail("Error");
        }
    }

    /**
     * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncValidator#validateDeletePw(SdtncResponseDto)}.
     */
    @Test
    public void validateDeletePwTest_Error_001() {
        try {
            SdtncResponseDto res = SdtncTestUtil.readResFromXml(serdes, DATA_PATH, "delVpath.res.xml");
            res.vpath.get(0).vObjectIndex = null;
            SdtncValidator.validateDeletePw(res);
            fail("Error");
        } catch (InternalException e) {
            assertTrue(e instanceof InternalException);
            assertEquals(e.getMessage(), "SDTNCBadResponse/InvalidData/" + "vObjectIndex");
        } catch (Throwable e1) {
            fail("Error");
        }
    }
    
    /**
     * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncValidator#validateDeletePw(SdtncResponseDto)}.
     */
    @Test
    public void validateDeletePwTest_Error_002() {
        try {
            SdtncResponseDto res = SdtncTestUtil.readResFromXml(serdes, DATA_PATH, "delVpath.res.xml");
            res.vpath.get(0).vObjectIndex = "11111111102222222220333333333044444444405555555550666666666077777";
            SdtncValidator.validateDeletePw(res);
            fail("Error");
        } catch (InternalException e) {
            assertTrue(e instanceof InternalException);
            assertEquals(e.getMessage(), "SDTNCBadResponse/InvalidData/" + "vObjectIndex");
        } catch (Throwable e1) {
            fail("Error");
        }
    }
}
