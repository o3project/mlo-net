/**
 * SdtncDtoUtilTest.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.rpc.service;

import static org.junit.Assert.*;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.impl.rpc.service.SdtncDtoUtil;
import org.o3project.mlo.server.logic.MloException;
import org.o3project.mlo.server.rpc.dto.SdtncReqPostCreatePwDto;
import org.o3project.mlo.server.rpc.dto.SdtncReqPostLoginDto;
import org.o3project.mlo.server.rpc.dto.SdtncReqPostLogoutDto;

/**
 * SdtncDtoUtilTest
 *
 */
public class SdtncDtoUtilTest {

	public String OTHER_LOGIN_ID = "demoApl";
	public String HITACHI_LOGIN_ID = "hitachi";
	
	public String OTHER_LOGIN_PASSWORD = "other_password";
	public String HITACHI_LOGIN_PASSWORD = "hitachi_password";
	public String LOGIN_IP_ADDRESS = "127.0.0.1";
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncDtoUtil#createLoginReqDto()}.
	 * 他社連携ログイン
	 */
	@Test
	public void createLoginReqDtoTest1(){
		SdtncReqPostLoginDto dto;
		try {
			HashMap<String,String> prmMap = new HashMap<String,String>();
			prmMap.put(SdtncDtoUtil.DTO_PRM_KEY_LOGIN_ID, OTHER_LOGIN_ID);
			prmMap.put(SdtncDtoUtil.DTO_PRM_KEY_LOGIN_PASSWORD, OTHER_LOGIN_PASSWORD);
			prmMap.put(SdtncDtoUtil.DTO_PRM_KEY_LOGIN_IP_ADDRESS, LOGIN_IP_ADDRESS);
			dto = (SdtncReqPostLoginDto) SdtncDtoUtil.createLoginReqDto(prmMap);
			assertEquals(dto.login.loginId, OTHER_LOGIN_ID);
			assertEquals(dto.login.loginPassword, OTHER_LOGIN_PASSWORD);
			assertEquals(dto.login.ipAddress, LOGIN_IP_ADDRESS);
			assertEquals(dto.login.accountTimeZone, "JST");
		} catch (MloException e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncDtoUtil#createLoginReqDto()}.
	 * 日立連携ログイン
	 */
	@Test
	public void createLoginReqDtoTest2(){
		SdtncReqPostLoginDto dto;
		try {
			HashMap<String,String> prmMap = new HashMap<String,String>();
			prmMap.put(SdtncDtoUtil.DTO_PRM_KEY_LOGIN_ID, HITACHI_LOGIN_ID);
			prmMap.put(SdtncDtoUtil.DTO_PRM_KEY_LOGIN_PASSWORD, HITACHI_LOGIN_PASSWORD);
			prmMap.put(SdtncDtoUtil.DTO_PRM_KEY_LOGIN_IP_ADDRESS, LOGIN_IP_ADDRESS);
			dto = (SdtncReqPostLoginDto) SdtncDtoUtil.createLoginReqDto(prmMap);
			assertEquals(dto.login.loginId, HITACHI_LOGIN_ID);
			assertEquals(dto.login.loginPassword, HITACHI_LOGIN_PASSWORD);
			assertEquals(dto.login.ipAddress, LOGIN_IP_ADDRESS);
			assertEquals(dto.login.accountTimeZone, "JST");
		} catch (MloException e) {
			fail();
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncDtoUtil#createLogoutReqDto()}.
	 * ログアウト
	 */
	@Test
	public void createLogoutReqDtoTest(){
		SdtncReqPostLogoutDto dto;
		try {
			HashMap<String,String> prmMap = new HashMap<String,String>();
			prmMap.put(SdtncDtoUtil.DTO_PRM_KEY_LOGIN_ID, OTHER_LOGIN_ID);
			dto = (SdtncReqPostLogoutDto) SdtncDtoUtil.createLogoutReqDto(prmMap);
			assertEquals(dto.login.loginId, OTHER_LOGIN_ID);
		} catch (MloException e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.rpc.service.SdtncDtoUtil#creatAddPwReqDto()}.
	 * PW生成
	 */
	@Test
	public void creatAddPwReqDtoTest(){
		SdtncReqPostCreatePwDto dto;
		try {
			String sliceId = "slcieId";
			String token = "token";
			String vLanId = "421";
			String vObjectName = "pathName";
			String neIdA = "neIdA";
			String portIdA = "1";
			String neIdZ = "neIdZ";
			String portIdZ = "9";
			String elementIndex = "elementIndex";
			String vPwDownLabel = "000051";
			String vPwUpLabel = "000052";
			int vQosPir = 120;
			int vQosCir = 100;
			int vQosSla = 4;
			HashMap<String,String> prmMap = new HashMap<String,String>();
			prmMap.put(SdtncDtoUtil.DTO_PRM_KEY_SLICEID, sliceId);
			prmMap.put(SdtncDtoUtil.DTO_PRM_KEY_TOKEN, token);
			prmMap.put(SdtncDtoUtil.DTO_PRM_KEY_VLANID, vLanId);
			prmMap.put(SdtncDtoUtil.DTO_PRM_KEY_PATH_NAME, vObjectName);
			prmMap.put(SdtncDtoUtil.DTO_PRM_KEY_NEID_A, neIdA);
			prmMap.put(SdtncDtoUtil.DTO_PRM_KEY_PORTID_A, portIdA);
			prmMap.put(SdtncDtoUtil.DTO_PRM_KEY_NEID_Z, neIdZ);
			prmMap.put(SdtncDtoUtil.DTO_PRM_KEY_PORTID_Z, portIdZ);
			prmMap.put(SdtncDtoUtil.DTO_PRM_KEY_ELEMENT_INDEX, elementIndex);
			prmMap.put(SdtncDtoUtil.DTO_PRM_KEY_V_PW_DOWN_LABEL, vPwDownLabel);
			prmMap.put(SdtncDtoUtil.DTO_PRM_KEY_V_PW_UP_LABEL, vPwUpLabel);
			prmMap.put(SdtncDtoUtil.DTO_PRM_KEY_V_QOS_SLA, String.valueOf(vQosSla));
			prmMap.put(SdtncDtoUtil.DTO_PRM_KEY_V_QOS_PIR, String.valueOf(vQosPir));
			prmMap.put(SdtncDtoUtil.DTO_PRM_KEY_V_QOS_CIR, String.valueOf(vQosCir));
			dto = (SdtncReqPostCreatePwDto) SdtncDtoUtil.creatAddPwReqDto(prmMap);
			
			assertEquals(dto.auth.token, token);
			assertEquals(dto.slice.groupIndex, sliceId);
			assertEquals(dto.vpath.get(0).vObjectName, vObjectName);
			assertEquals(dto.vpath.get(0).vObjectDescription, "demo");
			assertTrue(dto.vpath.get(0).pathInfo.vGrade == 0);
			assertEquals(dto.vpath.get(0).pathEndPoint.get(0).startEndPoint, "A");
			assertEquals(dto.vpath.get(0).pathEndPoint.get(0).neId, neIdA);
			assertEquals(dto.vpath.get(0).pathEndPoint.get(0).portId, "1");
			assertEquals(dto.vpath.get(0).pathEndPoint.get(1).startEndPoint, "Z");
			assertEquals(dto.vpath.get(0).pathEndPoint.get(1).neId, neIdZ);
			assertEquals(dto.vpath.get(0).pathEndPoint.get(1).portId, "9");
			assertEquals("000051", dto.vpath.get(0).pw.vPwDownLabel);
			assertEquals("000052", dto.vpath.get(0).pw.vPwUpLabel);
			assertTrue(dto.vpath.get(0).qos.vQoSPriority == 1);
			assertTrue(dto.vpath.get(0).qos.vQoSSla == vQosSla);
			assertTrue(dto.vpath.get(0).qos.vQoSPir == vQosPir);
			assertTrue(dto.vpath.get(0).qos.vQoSCir == vQosCir);
			assertTrue(dto.vpath.get(0).vlan.vVlanTagNum == 1);
			assertTrue(dto.vpath.get(0).vlan.vVlanTagMode == 1);
			assertTrue(dto.vpath.get(0).vlan.uni.vVlanVLanIdNniClient == 421);
			assertTrue(dto.vpath.get(0).pathRoute.get(0).lineGroupWorkingProtection == 1);
			assertEquals(dto.vpath.get(0).pathRoute.get(0).line.get(0).elementIndex, "elementIndex");
		} catch (MloException e) {
			fail();
		}

	}
}
