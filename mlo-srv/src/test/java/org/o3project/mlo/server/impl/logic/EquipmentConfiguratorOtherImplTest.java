/**
 * EquipmentConfiguratorOtherImplTest.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.o3project.mlo.server.dto.FlowDto;
import org.o3project.mlo.server.dto.RestifCommonDto;
import org.o3project.mlo.server.dto.RestifComponentDto;
import org.o3project.mlo.server.dto.RestifRequestDto;
import org.o3project.mlo.server.dto.SliceDto;
import org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl;
import org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOptDeviceImpl;
import org.o3project.mlo.server.impl.rpc.service.OdenOSConfigImpl;
import org.o3project.mlo.server.impl.rpc.service.OdenOSTopology;
import org.o3project.mlo.server.impl.rpc.service.SdtncDtoOtherConfigImpl;
import org.o3project.mlo.server.logic.ApiCallException;
import org.o3project.mlo.server.logic.ConfigProvider;
import org.o3project.mlo.server.logic.DbAccessException;
import org.o3project.mlo.server.logic.InternalException;
import org.o3project.mlo.server.logic.MloException;
import org.o3project.mlo.server.logic.TopologyConfigConstants;
import org.o3project.mlo.server.logic.TopologyRepository;
import org.o3project.mlo.server.rpc.dto.SdtncAuthDto;
import org.o3project.mlo.server.rpc.dto.SdtncReqPostCreatePwDto;
import org.o3project.mlo.server.rpc.dto.SdtncReqPostLoginDto;
import org.o3project.mlo.server.rpc.dto.SdtncReqPostLogoutDto;
import org.o3project.mlo.server.rpc.dto.SdtncRequestDto;
import org.o3project.mlo.server.rpc.dto.SdtncResponseDto;
import org.o3project.mlo.server.rpc.dto.SdtncVlinkDto;
import org.o3project.mlo.server.rpc.dto.SdtncVpathDto;
import org.o3project.mlo.server.rpc.entity.PTFlowEntity;
import org.o3project.mlo.server.rpc.entity.PTLinkEntity;
import org.o3project.mlo.server.rpc.service.OdenOSAdapterService;
import org.o3project.mlo.server.rpc.service.SdtncService;

/**
 * DispatcherImplTest
 *
 */
public class EquipmentConfiguratorOtherImplTest implements TopologyConfigConstants {

	private String LOGIN_ID = "admin";
	private String LOGIN_PASS = "Admin";
	private String SLICE_ID = "4";
	private static final String HOP_LINK_ID = "521";
	private static final String CUT_LINK_ID = "221";
	private static final String LOGIN_IP_ADDRESS = "172.21.104.15";
	private static final String NE_ID_A = "1.1.1.0.3.0.1";
	private static final String NE_ID_Z = "1.1.2.0.3.0.1";
	private static final String HOP_PORT_ID_A = "1.1.1.3.5.1.3.1.0.1.1.2.0.3.0.2.1";
	private static final String HOP_PORT_ID_Z = "1.1.2.3.5.1.3.1.0.1.1.2.0.3.0.2.1";
	private static final String CUT_PORT_ID_A = "1.1.1.3.5.1.3.1.0.1.1.2.0.3.0.2.2";
	private static final String CUT_PORT_ID_Z = "1.1.2.3.5.1.3.1.0.1.1.2.0.3.0.2.2";
	private static final int VLANID_A = 421;
	private static final int DEF_SLA_MODE = 1;
	private static final int QOS_CIR = 1000;
	private static final int QOS_PIR = 1200;
	
	// リンク
	private int LINK_ID_TL1 = 50000012;
	private int LINK_ID_TL2 = 50000023;
	private int LINK_ID_TL3 = 50000013;
	private int LINK_ID_TL4 = 50000021;
	private int LINK_ID_TL5 = 50000032;
	private int LINK_ID_TL6 = 50000031;
	
	// Node
	String AMN64001_NODE_NAME = "AMN64001";
	String AMN64002_NODE_NAME = "AMN64002";
	String AMN64003_NODE_NAME = "AMN64003";
	
	// リクエスト
	RestifRequestDto restifRequestDto;
	EquipmentConfiguratorOtherImpl obj;
	
	// 定数
	public final String vPathId = "vPathId";
	
	public String vPathId2 = "vPathId2";
	
	private static final String DATA_PATH = "src/test/resources/org/o3project/mlo/server/rpc/service/data";
	private static final String DATA_FILE_001 = "sdtnc.dto.config.001.properties";
	private static final String DATA_FILE_002 = "sdtnc.dto.config.002.properties";
	private static final String DATA_FILE_003 = "sdtnc.dto.config.003.properties";
	
	// パラメータMapキー
	public String PARAM_KEY_TOKEN = "token";
	public String PARAM_KEY_SLICE_ID = "groupIndex";
	public String PARAM_KEY_VPATH_ID = "vObjectIndex";
	public String PARAM_KEY_VLINK_ID = "vObjectIndex";
	
	private PTLinkEntity linkEntityTl1;
	private PTLinkEntity linkEntityTl2;
	private PTLinkEntity linkEntityTl3;
	private PTLinkEntity linkEntityTl4;
	private PTLinkEntity linkEntityTl5;
	private PTLinkEntity linkEntityTl6;
	
	private PTFlowEntity flowEntityFlow1;
	private PTFlowEntity flowEntityFlow2;
	private PTFlowEntity flowEntityFlow3;
	private PTFlowEntity flowEntityFlow4;
	
	public EquipmentConfiguratorOtherImplTest() {
		super();
		initOdenOSTopologyEntities();
	}
	
	private void initOdenOSTopologyEntities() {
		ConfigProvider configProvider = createDefaultConfigProvider();
		OdenOSTopology odenOSTopology = new OdenOSTopology();
		String linkNameFmt = "link-%s-%s_%s";
		String linkNameTl1 = String.format(linkNameFmt, AMN6400_1_2_LINK_ID, AMN64001_NAME, AMN64002_NAME);
		String linkNameTl2 = String.format(linkNameFmt, AMN6400_2_3_LINK_ID, AMN64002_NAME, AMN64003_NAME);
		String linkNameTl3 = String.format(linkNameFmt, AMN6400_1_3_LINK_ID, AMN64001_NAME, AMN64003_NAME);
		String linkNameTl4 = String.format(linkNameFmt, AMN6400_2_1_LINK_ID, AMN64002_NAME, AMN64001_NAME);
		String linkNameTl5 = String.format(linkNameFmt, AMN6400_3_2_LINK_ID, AMN64003_NAME, AMN64002_NAME);
		String linkNameTl6 = String.format(linkNameFmt, AMN6400_3_1_LINK_ID, AMN64003_NAME, AMN64001_NAME);
		linkEntityTl1 = odenOSTopology.createPTLinkEntity(linkNameTl1, 10000, 9999, configProvider);
		linkEntityTl2 = odenOSTopology.createPTLinkEntity(linkNameTl2, 10000, 9999, configProvider);
		linkEntityTl3 = odenOSTopology.createPTLinkEntity(linkNameTl3, 10000, 8, configProvider);
		linkEntityTl4 = odenOSTopology.createPTLinkEntity(linkNameTl4, 10000, 9999, configProvider);
		linkEntityTl5 = odenOSTopology.createPTLinkEntity(linkNameTl5, 10000, 9999, configProvider);
		linkEntityTl6 = odenOSTopology.createPTLinkEntity(linkNameTl6, 10000, 8, configProvider);
		flowEntityFlow1 = odenOSTopology.createPTFlowEntity("flow1", 10000, 9999, configProvider);
		flowEntityFlow2 = odenOSTopology.createPTFlowEntity("flow2", 10000, 9999, configProvider);
		flowEntityFlow3 = odenOSTopology.createPTFlowEntity("flow3", 10000, 8, configProvider);
		flowEntityFlow4 = odenOSTopology.createPTFlowEntity("flow4", 10000, 8, configProvider);
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		obj = new EquipmentConfiguratorOtherImpl();
		
		ConfigProvider configProvider = createDefaultConfigProvider();
		
		EquipmentConfiguratorOptDeviceImpl equipmentConfiguratorOptDeviceImpl = new EquipmentConfiguratorOptDeviceImpl();
		equipmentConfiguratorOptDeviceImpl.setConfigProvider(configProvider);
		obj.setEquipmentConfiguratorOptDeviceImpl(equipmentConfiguratorOptDeviceImpl);
		SampleOdenOsAdp sampleAdp = new SampleOdenOsAdp();
		obj.getEquipmentConfiguratorOptDeviceImpl().setOdenOSAdapterService(sampleAdp);
		SampleSdtncService sampleSdtnc = new SampleSdtncService();
		obj.setSdtncService(sampleSdtnc);
		OdenOSConfigImpl odenOSConfig = new OdenOSConfigImpl();
		odenOSConfig.setConfigProvider(configProvider);
		
		SdtncDtoOtherConfigImpl dtoConf = new SdtncDtoOtherConfigImpl();
		dtoConf.setConfigProvider(configProvider);
		obj.setSdtncDtoOtherConfig(dtoConf);

		TopologyRepository topologyRepository = TopologyRepositoryDefaultImplTest.createObjLoaded(configProvider);
		obj.setTopologyRepository(topologyRepository);
		
		RestifComponentDto componentDto = new RestifComponentDto();
		componentDto.name = "demoApl";

		RestifCommonDto commonDto = new RestifCommonDto();
		commonDto.srcComponent = componentDto;
		SliceDto sliceDto = new SliceDto();
		sliceDto.id = 1;
		restifRequestDto = new RestifRequestDto();
		restifRequestDto.common = commonDto;
		restifRequestDto.slice = sliceDto;
	}

	/**
	 * @return
	 */
	ConfigProvider createDefaultConfigProvider() {
		File propFile = new File(DATA_PATH, DATA_FILE_001);
		ConfigProvider configProvider = ConfigProviderImplTest.createConfigProviderImpl(propFile.getAbsolutePath());
		return configProvider;
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		restifRequestDto = null;
		obj = null;
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl#addFlow()}.
	 * P1→P2→P3(リンク未設定)
	 */
	@Test
	public void testAddFlow1() {
		int flowId = 1;
		FlowDto addedFlowDto = createFlowDto(flowId, AMN64001_NODE_NAME, AMN64003_NODE_NAME, Arrays.asList(LINK_ID_TL1, LINK_ID_TL2));
		
		FlowDto reqDto = getCreateReqDto();
		
		try {
			obj.addFlow(addedFlowDto, reqDto, restifRequestDto);
			
			// bandwidthとdelayの上書き確認
			assertEquals(addedFlowDto.usedBandWidth, reqDto.reqBandWidth);
			assertEquals(addedFlowDto.delayTime, reqDto.reqDelay);
			
			// OdenOS メソッド呼び出し確認
			SampleOdenOsAdp resultSampleAdp = (SampleOdenOsAdp) obj.getEquipmentConfiguratorOptDeviceImpl().getOdenOSAdapterService(); 
			assertTrue(resultSampleAdp.resultGetLinkId == true);
			assertTrue(resultSampleAdp.resultRequestLink == true);
			assertTrue(resultSampleAdp.resultDeleteFlow == false);
			assertTrue(resultSampleAdp.resultPutFlow == true);
			
			// Sdtnc メソッド呼び出し確認
			SampleSdtncService resultSmpleSdt = (SampleSdtncService)obj.getSdtncService();
			assertTrue(resultSmpleSdt.callLogin == true);
			assertTrue(resultSmpleSdt.callLogout == true);
			assertTrue(resultSmpleSdt.callGetLspResource == true);
			assertTrue(resultSmpleSdt.callCreatePw == true);
			assertTrue(resultSmpleSdt.callDeleatPw == false);
			assertTrue(resultSmpleSdt.callGetPw == false);
			
			// getLink 引数確認
			assertEquals(resultSampleAdp.resultLinkIdList.size(), 4);
			assertTrue(resultSampleAdp.resultLinkIdList.contains(linkEntityTl1.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.contains(linkEntityTl4.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.contains(linkEntityTl2.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.contains(linkEntityTl5.linkId));
			
			// requestLink 引数確認
			assertEquals(resultSampleAdp.resultPtLinkList.size(), 4);
			compareLinkEntity(resultSampleAdp.resultPtLinkList.get(0), linkEntityTl1);
			compareLinkEntity(resultSampleAdp.resultPtLinkList.get(1), linkEntityTl2);
			compareLinkEntity(resultSampleAdp.resultPtLinkList.get(2), linkEntityTl5);
			compareLinkEntity(resultSampleAdp.resultPtLinkList.get(3), linkEntityTl4);
			
			// putFlow 引数確認
			assertEquals(resultSampleAdp.resultPutFlowEntity.size(), 2);
			compareFlowEntity(resultSampleAdp.resultPutFlowEntity.get(0), addedFlowDto.name + "_1", flowEntityFlow1);
			compareFlowEntity(resultSampleAdp.resultPutFlowEntity.get(1), addedFlowDto.name + "_2", flowEntityFlow2);
			assertTrue(obj.getEquipmentConfiguratorOptDeviceImpl().isFlowId(flowId) == true);
			
			// sdtncService.login 引数確認
			assertEquals(resultSmpleSdt.loginDto.login.loginId,LOGIN_ID);
			assertEquals(resultSmpleSdt.loginDto.login.loginPassword,LOGIN_PASS);
			assertEquals(resultSmpleSdt.loginDto.login.ipAddress, LOGIN_IP_ADDRESS);
			
			// sdtncService.getLspResource 引数確認
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_SLICE_ID),SLICE_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_VLINK_ID), HOP_LINK_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_TOKEN),"token");
			
			// sdtncService.createPw 引数確認
			assertEquals(resultSmpleSdt.addPwDto.slice.groupIndex,SLICE_ID);
			assertEquals(resultSmpleSdt.addPwDto.auth.token,"token");
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).vlan.uni.vVlanVLanIdNniClient == VLANID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).vObjectName, addedFlowDto.id + "_" + addedFlowDto.name);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(0).neId, NE_ID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(0).portId, HOP_PORT_ID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(1).neId, NE_ID_Z);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(1).portId, HOP_PORT_ID_Z);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSSla == DEF_SLA_MODE);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSPir == 0);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSCir == QOS_CIR);
			
			// sdtncService.logout 引数確認
			assertEquals(resultSmpleSdt.logoutDto.login.loginId,LOGIN_ID);
			
			// pathId登録確認
			assertEquals(obj.getPathIdMap().get(flowId), vPathId);
			
		} catch (MloException e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl#addFlow()}.
	 * P3→P2→P1(リンク未設定)
	 */
	@Test
	public void testAddFlow2() {
		
		int flowId = 2;
		FlowDto addedFlowDto = createFlowDto(flowId, AMN64003_NODE_NAME, AMN64001_NODE_NAME, Arrays.asList(LINK_ID_TL5, LINK_ID_TL4));
		
		FlowDto reqDto = getCreateReqDto();
		
		try {
			obj.addFlow(addedFlowDto, reqDto, restifRequestDto);
			
			// OdenOS メソッド呼び出し確認
			SampleOdenOsAdp resultSampleAdp = (SampleOdenOsAdp) obj.getEquipmentConfiguratorOptDeviceImpl().getOdenOSAdapterService(); 
			assertTrue(resultSampleAdp.resultGetLinkId == true);
			assertTrue(resultSampleAdp.resultRequestLink == true);
			assertTrue(resultSampleAdp.resultDeleteFlow == false);
			assertTrue(resultSampleAdp.resultPutFlow == true);
			
			// Sdtnc メソッド呼び出し確認
			SampleSdtncService resultSmpleSdt = (SampleSdtncService)obj.getSdtncService();
			assertTrue(resultSmpleSdt.callLogin == true);
			assertTrue(resultSmpleSdt.callLogout == true);
			assertTrue(resultSmpleSdt.callGetLspResource == true);
			assertTrue(resultSmpleSdt.callCreatePw == true);
			assertTrue(resultSmpleSdt.callDeleatPw == false);
			assertTrue(resultSmpleSdt.callGetPw == false);
			
			// getLink 引数確認
			assertEquals(resultSampleAdp.resultLinkIdList.size(), 4);
			assertTrue(resultSampleAdp.resultLinkIdList.get(0).equals(linkEntityTl5.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.get(1).equals(linkEntityTl4.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.get(2).equals(linkEntityTl1.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.get(3).equals(linkEntityTl2.linkId));
			
			// requestLink 引数確認
			assertEquals(resultSampleAdp.resultPtLinkList.size(), 4);
			compareLinkEntity(resultSampleAdp.resultPtLinkList.get(0), linkEntityTl5);
			compareLinkEntity(resultSampleAdp.resultPtLinkList.get(1), linkEntityTl4);
			compareLinkEntity(resultSampleAdp.resultPtLinkList.get(2), linkEntityTl1);
			compareLinkEntity(resultSampleAdp.resultPtLinkList.get(3), linkEntityTl2);

			// putFlow 引数確認
			assertEquals(resultSampleAdp.resultPutFlowEntity.size(), 2);
			compareFlowEntity(resultSampleAdp.resultPutFlowEntity.get(0), addedFlowDto.name + "_1", flowEntityFlow2);
			compareFlowEntity(resultSampleAdp.resultPutFlowEntity.get(1), addedFlowDto.name + "_2", flowEntityFlow1);
			assertTrue(obj.getEquipmentConfiguratorOptDeviceImpl().isFlowId(flowId) == true);
			
			// sdtncService.login 引数確認
			assertEquals(resultSmpleSdt.loginDto.login.loginId,LOGIN_ID);
			assertEquals(resultSmpleSdt.loginDto.login.loginPassword,LOGIN_PASS);
			assertEquals(resultSmpleSdt.loginDto.login.ipAddress, LOGIN_IP_ADDRESS);

			// sdtncService.getLspResource 引数確認
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_SLICE_ID),SLICE_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_VLINK_ID),HOP_LINK_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_TOKEN),"token");

			// sdtncService.createPw 引数確認
			assertEquals(resultSmpleSdt.addPwDto.slice.groupIndex,SLICE_ID);
			assertEquals(resultSmpleSdt.addPwDto.auth.token,"token");
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).vlan.uni.vVlanVLanIdNniClient == VLANID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).vObjectName, addedFlowDto.id + "_" + addedFlowDto.name);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(0).neId, NE_ID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(0).portId, HOP_PORT_ID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(1).neId, NE_ID_Z);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(1).portId, HOP_PORT_ID_Z);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathRoute.get(0).line.get(0).elementIndex, HOP_LINK_ID);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSSla == DEF_SLA_MODE);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSPir == 0);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSCir == QOS_CIR);

			// sdtncService.logout 引数確認
			assertEquals(resultSmpleSdt.logoutDto.login.loginId,LOGIN_ID);
			
			// pathId登録確認
			assertEquals(obj.getPathIdMap().get(flowId), vPathId);
			
		} catch (MloException e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl#addFlow()}.
	 * P1→P3(リンク未設定)
	 */
	@Test
	public void testAddFlow3() {
		
		int flowId = 3;
		FlowDto addedFlowDto = createFlowDto(flowId, AMN64001_NODE_NAME, AMN64003_NODE_NAME, Arrays.asList(LINK_ID_TL3));
		
		FlowDto reqDto = getCreateReqDto();
		
		try {
			obj.addFlow(addedFlowDto, reqDto, restifRequestDto);
			
			// OdenOS メソッド呼び出し確認
			SampleOdenOsAdp resultSampleAdp = (SampleOdenOsAdp) obj.getEquipmentConfiguratorOptDeviceImpl().getOdenOSAdapterService(); 
			assertTrue(resultSampleAdp.resultGetLinkId == true);
			assertTrue(resultSampleAdp.resultRequestLink == true);
			assertTrue(resultSampleAdp.resultDeleteFlow == false);
			assertTrue(resultSampleAdp.resultPutFlow == true);
			
			// Sdtnc メソッド呼び出し確認
			SampleSdtncService resultSmpleSdt = (SampleSdtncService)obj.getSdtncService();
			assertTrue(resultSmpleSdt.callLogin == true);
			assertTrue(resultSmpleSdt.callLogout == true);
			assertTrue(resultSmpleSdt.callGetLspResource == true);
			assertTrue(resultSmpleSdt.callCreatePw == true);
			assertTrue(resultSmpleSdt.callDeleatPw == false);
			assertTrue(resultSmpleSdt.callGetPw == false);
			
			// getLink 引数確認
			assertEquals(resultSampleAdp.resultLinkIdList.size(), 2);
			assertTrue(resultSampleAdp.resultLinkIdList.get(0).equals(linkEntityTl3.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.get(1).equals(linkEntityTl6.linkId));
			
			// requestLink 引数確認
			assertEquals(resultSampleAdp.resultPtLinkList.size(), 2);
			compareLinkEntity(resultSampleAdp.resultPtLinkList.get(0), linkEntityTl3);
			compareLinkEntity(resultSampleAdp.resultPtLinkList.get(1), linkEntityTl6);
			
			// putFlow 引数確認
			assertEquals(resultSampleAdp.resultPutFlowEntity.size(), 2);
			compareFlowEntity(resultSampleAdp.resultPutFlowEntity.get(0), addedFlowDto.name + "_1", flowEntityFlow3);
			compareFlowEntity(resultSampleAdp.resultPutFlowEntity.get(1), addedFlowDto.name + "_2", flowEntityFlow4);
			assertTrue(obj.getEquipmentConfiguratorOptDeviceImpl().isFlowId(flowId) == true);
			
			// sdtncService.login 引数確認
			assertEquals(resultSmpleSdt.loginDto.login.loginId,LOGIN_ID);
			assertEquals(resultSmpleSdt.loginDto.login.loginPassword,LOGIN_PASS);
			assertEquals(resultSmpleSdt.loginDto.login.ipAddress, LOGIN_IP_ADDRESS);

			// sdtncService.getLspResource 引数確認
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_SLICE_ID),SLICE_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_VLINK_ID),CUT_LINK_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_TOKEN),"token");

			// sdtncService.createPw 引数確認
			assertEquals(resultSmpleSdt.addPwDto.slice.groupIndex,SLICE_ID);
			assertEquals(resultSmpleSdt.addPwDto.auth.token,"token");
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).vlan.uni.vVlanVLanIdNniClient == VLANID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).vObjectName, addedFlowDto.id + "_" + addedFlowDto.name);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(0).neId, NE_ID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(0).portId, CUT_PORT_ID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(1).neId, NE_ID_Z);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(1).portId, CUT_PORT_ID_Z);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathRoute.get(0).line.get(0).elementIndex, CUT_LINK_ID);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSSla == DEF_SLA_MODE);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSPir == 0);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSCir == QOS_CIR);

			// sdtncService.logout 引数確認
			assertEquals(resultSmpleSdt.logoutDto.login.loginId,LOGIN_ID);
			
			// pathId登録確認
			assertEquals(obj.getPathIdMap().get(flowId), vPathId);
						
		} catch (MloException e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl#addFlow()}.
	 * P3→P1(リンク未設定)
	 */
	@Test
	public void testAddFlow4() {
		
		int flowId = 4;
		FlowDto addedFlowDto = createFlowDto(flowId, AMN64003_NODE_NAME, AMN64001_NODE_NAME, Arrays.asList(LINK_ID_TL6));
		FlowDto reqDto = getCreateReqDto();
		
		try {
			obj.addFlow(addedFlowDto, reqDto, restifRequestDto);
			
			// OdenOS メソッド呼び出し確認
			SampleOdenOsAdp resultSampleAdp = (SampleOdenOsAdp) obj.getEquipmentConfiguratorOptDeviceImpl().getOdenOSAdapterService(); 
			assertTrue(resultSampleAdp.resultGetLinkId == true);
			assertTrue(resultSampleAdp.resultRequestLink == true);
			assertTrue(resultSampleAdp.resultDeleteFlow == false);
			assertTrue(resultSampleAdp.resultPutFlow == true);
			
			// Sdtnc メソッド呼び出し確認
			SampleSdtncService resultSmpleSdt = (SampleSdtncService)obj.getSdtncService();
			assertTrue(resultSmpleSdt.callLogin == true);
			assertTrue(resultSmpleSdt.callLogout == true);
			assertTrue(resultSmpleSdt.callGetLspResource == true);
			assertTrue(resultSmpleSdt.callCreatePw == true);
			assertTrue(resultSmpleSdt.callDeleatPw == false);
			assertTrue(resultSmpleSdt.callGetPw == false);
			
			// getLink 引数確認
			assertEquals(resultSampleAdp.resultLinkIdList.size(), 2);
			assertTrue(resultSampleAdp.resultLinkIdList.get(0).equals(linkEntityTl6.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.get(1).equals(linkEntityTl3.linkId));
			
			// requestLink 引数確認
			assertEquals(resultSampleAdp.resultPtLinkList.size(), 2);
			compareLinkEntity(resultSampleAdp.resultPtLinkList.get(0), linkEntityTl6);
			compareLinkEntity(resultSampleAdp.resultPtLinkList.get(1), linkEntityTl3);
			
			// putFlow 引数確認
			assertEquals(resultSampleAdp.resultPutFlowEntity.size(), 2);
			compareFlowEntity(resultSampleAdp.resultPutFlowEntity.get(0), addedFlowDto.name + "_1", flowEntityFlow4);
			compareFlowEntity(resultSampleAdp.resultPutFlowEntity.get(1), addedFlowDto.name + "_2", flowEntityFlow3);
			assertTrue(obj.getEquipmentConfiguratorOptDeviceImpl().isFlowId(flowId) == true);
			
			// sdtncService.login 引数確認
			assertEquals(resultSmpleSdt.loginDto.login.loginId,LOGIN_ID);
			assertEquals(resultSmpleSdt.loginDto.login.loginPassword,LOGIN_PASS);
			assertEquals(resultSmpleSdt.loginDto.login.ipAddress, LOGIN_IP_ADDRESS);

			// sdtncService.getLspResource 引数確認
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_SLICE_ID),SLICE_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_VLINK_ID),CUT_LINK_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_TOKEN),"token");

			// sdtncService.createPw 引数確認
			assertEquals(resultSmpleSdt.addPwDto.slice.groupIndex,SLICE_ID);
			assertEquals(resultSmpleSdt.addPwDto.auth.token,"token");
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).vlan.uni.vVlanVLanIdNniClient == VLANID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).vObjectName, addedFlowDto.id + "_" + addedFlowDto.name);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(0).neId, NE_ID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(0).portId, CUT_PORT_ID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(1).neId, NE_ID_Z);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(1).portId, CUT_PORT_ID_Z);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathRoute.get(0).line.get(0).elementIndex, CUT_LINK_ID);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSSla == DEF_SLA_MODE);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSPir == 0);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSCir == QOS_CIR);

			// sdtncService.logout 引数確認
			assertEquals(resultSmpleSdt.logoutDto.login.loginId,LOGIN_ID);
			
			// pathId登録確認
			assertEquals(obj.getPathIdMap().get(flowId), vPathId);
			
		} catch (MloException e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl#addFlow()}.
	 * P1→P2→P3(リンク設定済)
	 */
	@Test
	public void testAddFlow5() {
		SampleOdenOsAdp sampleAdp = new SampleOdenOsAdp();
		sampleAdp.getLinkFlag = true;
		obj.getEquipmentConfiguratorOptDeviceImpl().setOdenOSAdapterService(sampleAdp);
		
		int flowId = 5;
		FlowDto addedFlowDto = createFlowDto(flowId, AMN64001_NODE_NAME, AMN64003_NODE_NAME, Arrays.asList(LINK_ID_TL1, LINK_ID_TL2));
		FlowDto reqDto = getCreateReqDto();
		
		try {
			obj.addFlow(addedFlowDto, reqDto, restifRequestDto);
			
			// OdenOS メソッド呼び出し確認
			SampleOdenOsAdp resultSampleAdp = (SampleOdenOsAdp) obj.getEquipmentConfiguratorOptDeviceImpl().getOdenOSAdapterService(); 
			assertTrue(resultSampleAdp.resultGetLinkId == true);
			assertTrue(resultSampleAdp.resultRequestLink == false);
			assertTrue(resultSampleAdp.resultDeleteFlow == false);
			assertTrue(resultSampleAdp.resultPutFlow == true);
			
			// Sdtnc メソッド呼び出し確認
			SampleSdtncService resultSmpleSdt = (SampleSdtncService)obj.getSdtncService();
			assertTrue(resultSmpleSdt.callLogin == true);
			assertTrue(resultSmpleSdt.callLogout == true);
			assertTrue(resultSmpleSdt.callGetLspResource == true);
			assertTrue(resultSmpleSdt.callCreatePw == true);
			assertTrue(resultSmpleSdt.callDeleatPw == false);
			
			// getLink 引数確認
			assertEquals(resultSampleAdp.resultLinkIdList.size(), 4);
			assertTrue(resultSampleAdp.resultLinkIdList.get(0).equals(linkEntityTl1.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.get(1).equals(linkEntityTl2.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.get(2).equals(linkEntityTl5.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.get(3).equals(linkEntityTl4.linkId));
			
			// putFlow 引数確認
			assertEquals(resultSampleAdp.resultPutFlowEntity.size(), 2);
			compareFlowEntity(resultSampleAdp.resultPutFlowEntity.get(0), addedFlowDto.name + "_1", flowEntityFlow1);
			compareFlowEntity(resultSampleAdp.resultPutFlowEntity.get(1), addedFlowDto.name + "_2", flowEntityFlow2);
			assertTrue(obj.getEquipmentConfiguratorOptDeviceImpl().isFlowId(flowId) == true);
			
			// sdtncService.login 引数確認
			assertEquals(resultSmpleSdt.loginDto.login.loginId,LOGIN_ID);
			assertEquals(resultSmpleSdt.loginDto.login.loginPassword,LOGIN_PASS);
			assertEquals(resultSmpleSdt.loginDto.login.ipAddress, LOGIN_IP_ADDRESS);

			// sdtncService.getLspResource 引数確認
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_SLICE_ID),SLICE_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_VLINK_ID),HOP_LINK_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_TOKEN),"token");

			// sdtncService.createPw 引数確認
			assertEquals(resultSmpleSdt.addPwDto.slice.groupIndex,SLICE_ID);
			assertEquals(resultSmpleSdt.addPwDto.auth.token,"token");
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).vlan.uni.vVlanVLanIdNniClient == VLANID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).vObjectName, addedFlowDto.id + "_" + addedFlowDto.name);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(0).neId, NE_ID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(0).portId, HOP_PORT_ID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(1).neId, NE_ID_Z);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(1).portId, HOP_PORT_ID_Z);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathRoute.get(0).line.get(0).elementIndex, HOP_LINK_ID);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSSla == DEF_SLA_MODE);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSPir == 0);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSCir == QOS_CIR);

			// sdtncService.logout 引数確認
			assertEquals(resultSmpleSdt.logoutDto.login.loginId,LOGIN_ID);
			
			// pathId登録確認
			assertEquals(obj.getPathIdMap().get(flowId), vPathId);
			
		} catch (MloException e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl#addFlow()}.
	 * P3→P2→P1(リンク設定済)
	 */
	@Test
	public void testAddFlow6() {
		SampleOdenOsAdp sampleAdp = new SampleOdenOsAdp();
		sampleAdp.getLinkFlag = true;
		obj.getEquipmentConfiguratorOptDeviceImpl().setOdenOSAdapterService(sampleAdp);
		
		int flowId = 6;
		FlowDto addedFlowDto = createFlowDto(flowId, AMN64003_NODE_NAME, AMN64001_NODE_NAME, Arrays.asList(LINK_ID_TL5, LINK_ID_TL4));
		
		FlowDto reqDto = getCreateReqDto();
		
		try {
			obj.addFlow(addedFlowDto, reqDto, restifRequestDto);
			
			// OdenOS メソッド呼び出し確認
			SampleOdenOsAdp resultSampleAdp = (SampleOdenOsAdp) obj.getEquipmentConfiguratorOptDeviceImpl().getOdenOSAdapterService(); 
			assertTrue(resultSampleAdp.resultGetLinkId == true);
			assertTrue(resultSampleAdp.resultRequestLink == false);
			assertTrue(resultSampleAdp.resultDeleteFlow == false);
			assertTrue(resultSampleAdp.resultPutFlow == true);
			
			// Sdtnc メソッド呼び出し確認
			SampleSdtncService resultSmpleSdt = (SampleSdtncService)obj.getSdtncService();
			assertTrue(resultSmpleSdt.callLogin == true);
			assertTrue(resultSmpleSdt.callLogout == true);
			assertTrue(resultSmpleSdt.callGetLspResource == true);
			assertTrue(resultSmpleSdt.callCreatePw == true);
			assertTrue(resultSmpleSdt.callDeleatPw == false);
			assertTrue(resultSmpleSdt.callGetPw == false);
			
			// getLink 引数確認
			assertEquals(resultSampleAdp.resultLinkIdList.size(), 4);
			assertTrue(resultSampleAdp.resultLinkIdList.get(0).equals(linkEntityTl5.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.get(1).equals(linkEntityTl4.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.get(2).equals(linkEntityTl1.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.get(3).equals(linkEntityTl2.linkId));
			
			// putFlow 引数確認
			assertEquals(resultSampleAdp.resultPutFlowEntity.size(), 2);
			compareFlowEntity(resultSampleAdp.resultPutFlowEntity.get(0), addedFlowDto.name + "_1", flowEntityFlow2);
			compareFlowEntity(resultSampleAdp.resultPutFlowEntity.get(1), addedFlowDto.name + "_2", flowEntityFlow1);
			assertTrue(obj.getEquipmentConfiguratorOptDeviceImpl().isFlowId(flowId) == true);
			
			// sdtncService.login 引数確認
			assertEquals(resultSmpleSdt.loginDto.login.loginId,LOGIN_ID);
			assertEquals(resultSmpleSdt.loginDto.login.loginPassword,LOGIN_PASS);
			assertEquals(resultSmpleSdt.loginDto.login.ipAddress, LOGIN_IP_ADDRESS);

			// sdtncService.getLspResource 引数確認
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_SLICE_ID),SLICE_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_VLINK_ID),HOP_LINK_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_TOKEN),"token");

			// sdtncService.createPw 引数確認
			assertEquals(resultSmpleSdt.addPwDto.slice.groupIndex,SLICE_ID);
			assertEquals(resultSmpleSdt.addPwDto.auth.token,"token");
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).vlan.uni.vVlanVLanIdNniClient == VLANID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).vObjectName, addedFlowDto.id + "_" + addedFlowDto.name);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(0).neId, NE_ID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(0).portId, HOP_PORT_ID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(1).neId, NE_ID_Z);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(1).portId, HOP_PORT_ID_Z);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathRoute.get(0).line.get(0).elementIndex, HOP_LINK_ID);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSSla == DEF_SLA_MODE);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSPir == 0);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSCir == QOS_CIR);

			// sdtncService.logout 引数確認
			assertEquals(resultSmpleSdt.logoutDto.login.loginId,LOGIN_ID);

			// pathId登録確認
			assertEquals(obj.getPathIdMap().get(flowId), vPathId);
			
		} catch (MloException e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl#addFlow()}.
	 * P1→P3(リンク未設定)
	 */
	@Test
	public void testAddFlow7() {
		SampleOdenOsAdp sampleAdp = new SampleOdenOsAdp();
		sampleAdp.getLinkFlag = true;
		obj.getEquipmentConfiguratorOptDeviceImpl().setOdenOSAdapterService(sampleAdp);
		
		int flowId = 7;
		FlowDto addedFlowDto = createFlowDto(flowId, AMN64001_NODE_NAME, AMN64003_NODE_NAME, Arrays.asList(LINK_ID_TL3));
		
		FlowDto reqDto = getCreateReqDto();
		
		try {
			obj.addFlow(addedFlowDto, reqDto, restifRequestDto);
			
			// OdenOS メソッド呼び出し確認
			SampleOdenOsAdp resultSampleAdp = (SampleOdenOsAdp) obj.getEquipmentConfiguratorOptDeviceImpl().getOdenOSAdapterService(); 
			assertTrue(resultSampleAdp.resultGetLinkId == true);
			assertTrue(resultSampleAdp.resultRequestLink == false);
			assertTrue(resultSampleAdp.resultDeleteFlow == false);
			assertTrue(resultSampleAdp.resultPutFlow == true);
			
			// Sdtnc メソッド呼び出し確認
			SampleSdtncService resultSmpleSdt = (SampleSdtncService)obj.getSdtncService();
			assertTrue(resultSmpleSdt.callLogin == true);
			assertTrue(resultSmpleSdt.callLogout == true);
			assertTrue(resultSmpleSdt.callGetLspResource == true);
			assertTrue(resultSmpleSdt.callCreatePw == true);
			assertTrue(resultSmpleSdt.callDeleatPw == false);
			assertTrue(resultSmpleSdt.callGetPw == false);

			// getLink 引数確認
			assertEquals(resultSampleAdp.resultLinkIdList.size(), 2);
			assertTrue(resultSampleAdp.resultLinkIdList.get(0).equals(linkEntityTl3.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.get(1).equals(linkEntityTl6.linkId));
			
			// putFlow 引数確認
			assertEquals(resultSampleAdp.resultPutFlowEntity.size(), 2);
			compareFlowEntity(resultSampleAdp.resultPutFlowEntity.get(0), addedFlowDto.name + "_1", flowEntityFlow3);
			compareFlowEntity(resultSampleAdp.resultPutFlowEntity.get(1), addedFlowDto.name + "_2", flowEntityFlow4);
			assertTrue(obj.getEquipmentConfiguratorOptDeviceImpl().isFlowId(flowId) == true);
			
			// sdtncService.login 引数確認
			assertEquals(resultSmpleSdt.loginDto.login.loginId,LOGIN_ID);
			assertEquals(resultSmpleSdt.loginDto.login.loginPassword,LOGIN_PASS);
			assertEquals(resultSmpleSdt.loginDto.login.ipAddress, LOGIN_IP_ADDRESS);
			// sdtncService.getLspResource 引数確認
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_SLICE_ID),SLICE_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_VLINK_ID),CUT_LINK_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_TOKEN),"token");

			// sdtncService.createPw 引数確認
			assertEquals(resultSmpleSdt.addPwDto.slice.groupIndex,SLICE_ID);
			assertEquals(resultSmpleSdt.addPwDto.auth.token,"token");
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).vlan.uni.vVlanVLanIdNniClient == VLANID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).vObjectName, addedFlowDto.id + "_" + addedFlowDto.name);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(0).neId, NE_ID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(0).portId, CUT_PORT_ID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(1).neId, NE_ID_Z);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(1).portId, CUT_PORT_ID_Z);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathRoute.get(0).line.get(0).elementIndex, CUT_LINK_ID);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSSla == DEF_SLA_MODE);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSPir == 0);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSCir == QOS_CIR);

			// sdtncService.logout 引数確認
			assertEquals(resultSmpleSdt.logoutDto.login.loginId,LOGIN_ID);

			// pathId登録確認
			assertEquals(obj.getPathIdMap().get(flowId), vPathId);
			
		} catch (MloException e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl#addFlow()}.
	 * P3→P1(リンク設定済)
	 */
	@Test
	public void testAddFlow8() {
		SampleOdenOsAdp sampleAdp = new SampleOdenOsAdp();
		sampleAdp.getLinkFlag = true;
		obj.getEquipmentConfiguratorOptDeviceImpl().setOdenOSAdapterService(sampleAdp);
		
		int flowId = 8;
		FlowDto addedFlowDto = createFlowDto(flowId, AMN64003_NODE_NAME, AMN64001_NODE_NAME, Arrays.asList(LINK_ID_TL6));
		
		FlowDto reqDto = getCreateReqDto();
		
		try {
			obj.addFlow(addedFlowDto, reqDto, restifRequestDto);
			
			// OdenOS メソッド呼び出し確認
			SampleOdenOsAdp resultSampleAdp = (SampleOdenOsAdp) obj.getEquipmentConfiguratorOptDeviceImpl().getOdenOSAdapterService(); 
			assertTrue(resultSampleAdp.resultGetLinkId == true);
			assertTrue(resultSampleAdp.resultRequestLink == false);
			assertTrue(resultSampleAdp.resultDeleteFlow == false);
			assertTrue(resultSampleAdp.resultPutFlow == true);
			
			// Sdtnc メソッド呼び出し確認
			SampleSdtncService resultSmpleSdt = (SampleSdtncService)obj.getSdtncService();
			assertTrue(resultSmpleSdt.callLogin == true);
			assertTrue(resultSmpleSdt.callLogout == true);
			assertTrue(resultSmpleSdt.callGetLspResource == true);
			assertTrue(resultSmpleSdt.callCreatePw == true);
			assertTrue(resultSmpleSdt.callDeleatPw == false);
			assertTrue(resultSmpleSdt.callGetPw == false);
			
			// getLink 引数確認
			assertEquals(resultSampleAdp.resultLinkIdList.size(), 2);
			assertTrue(resultSampleAdp.resultLinkIdList.get(0).equals(linkEntityTl6.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.get(1).equals(linkEntityTl3.linkId));
			
			// putFlow 引数確認
			assertEquals(resultSampleAdp.resultPutFlowEntity.size(), 2);
			compareFlowEntity(resultSampleAdp.resultPutFlowEntity.get(0), addedFlowDto.name + "_1", flowEntityFlow4);
			compareFlowEntity(resultSampleAdp.resultPutFlowEntity.get(1), addedFlowDto.name + "_2", flowEntityFlow3);
			assertTrue(obj.getEquipmentConfiguratorOptDeviceImpl().isFlowId(flowId) == true);
			
			// sdtncService.login 引数確認
			assertEquals(resultSmpleSdt.loginDto.login.loginId,LOGIN_ID);
			assertEquals(resultSmpleSdt.loginDto.login.loginPassword,LOGIN_PASS);
			assertEquals(resultSmpleSdt.loginDto.login.ipAddress, LOGIN_IP_ADDRESS);

			// sdtncService.getLspResource 引数確認
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_SLICE_ID),SLICE_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_VLINK_ID),CUT_LINK_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_TOKEN),"token");

			// sdtncService.createPw 引数確認
			assertEquals(resultSmpleSdt.addPwDto.slice.groupIndex,SLICE_ID);
			assertEquals(resultSmpleSdt.addPwDto.auth.token,"token");
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).vlan.uni.vVlanVLanIdNniClient == VLANID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).vObjectName, addedFlowDto.id + "_" + addedFlowDto.name);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(0).neId, NE_ID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(0).portId, CUT_PORT_ID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(1).neId, NE_ID_Z);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(1).portId, CUT_PORT_ID_Z);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathRoute.get(0).line.get(0).elementIndex, CUT_LINK_ID);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSSla == DEF_SLA_MODE);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSPir == 0);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSCir == QOS_CIR);

			// sdtncService.logout 引数確認
			assertEquals(resultSmpleSdt.logoutDto.login.loginId,LOGIN_ID);

			// pathId登録確認
			assertEquals(obj.getPathIdMap().get(flowId), vPathId);
			
		} catch (MloException e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl#addFlow()}.
	 * P1→P3(リンク未設定) LSP情報が取得できなかった場合
	 */
	@Test
	public void testAddFlow9() {
		SampleSdtncService sampleSdtnc = new SampleSdtncService();
		sampleSdtnc.successGetLspResource = false;								// LSP取得を失敗するように設定
		obj.setSdtncService(sampleSdtnc);
		
		int flowId = 3;
		FlowDto addedFlowDto = createFlowDto(flowId, AMN64001_NODE_NAME, AMN64003_NODE_NAME, Arrays.asList(LINK_ID_TL3));
		
		FlowDto reqDto = getCreateReqDto();
		
		try {
			obj.addFlow(addedFlowDto, reqDto, restifRequestDto);
			
			// OdenOS メソッド呼び出し確認
			SampleOdenOsAdp resultSampleAdp = (SampleOdenOsAdp) obj.getEquipmentConfiguratorOptDeviceImpl().getOdenOSAdapterService(); 
			assertTrue(resultSampleAdp.resultGetLinkId == false);
			assertTrue(resultSampleAdp.resultRequestLink == false);
			assertTrue(resultSampleAdp.resultDeleteFlow == false);
			
			// Sdtnc メソッド呼び出し確認
			SampleSdtncService resultSmpleSdt = (SampleSdtncService)obj.getSdtncService();
			assertTrue(resultSmpleSdt.callLogin == true);
			assertTrue(resultSmpleSdt.callLogout == true);
			assertTrue(resultSmpleSdt.callGetLspResource == true);
			assertTrue(resultSmpleSdt.callCreatePw == false);
			assertTrue(resultSmpleSdt.callDeleatPw == false);
			assertTrue(resultSmpleSdt.callGetPw == false);
			
			// sdtncService.login 引数確認
			assertEquals(resultSmpleSdt.loginDto.login.loginId,LOGIN_ID);
			assertEquals(resultSmpleSdt.loginDto.login.loginPassword,LOGIN_PASS);
			assertEquals(resultSmpleSdt.loginDto.login.ipAddress, LOGIN_IP_ADDRESS);

			// sdtncService.getLspResource 引数確認
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_SLICE_ID),SLICE_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_VLINK_ID),CUT_LINK_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_TOKEN),"token");

			// sdtncService.logout 引数確認
			assertEquals(resultSmpleSdt.logoutDto.login.loginId,LOGIN_ID);

			// pathId登録確認
			assertNull(obj.getPathIdMap().get(flowId));
			
		} catch (MloException e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl#addFlow()}.
	 * slaModeが1以外の場合
	 */
	@Test
	public void testAddFlow10() {
		
		File propFile = new File(DATA_PATH, DATA_FILE_002);
		ConfigProvider configProvider = ConfigProviderImplTest.createConfigProviderImpl(propFile.getAbsolutePath());
		SdtncDtoOtherConfigImpl dtoConf = new SdtncDtoOtherConfigImpl();
		dtoConf.setConfigProvider(configProvider);
		obj.setSdtncDtoOtherConfig(dtoConf);
		
		int flowId = 1;
		FlowDto addedFlowDto = createFlowDto(flowId, AMN64001_NODE_NAME, AMN64003_NODE_NAME, Arrays.asList(LINK_ID_TL1, LINK_ID_TL2));
		
		FlowDto reqDto = getCreateReqDto();
		
		try {
			obj.addFlow(addedFlowDto, reqDto, restifRequestDto);
			
			// OdenOS メソッド呼び出し確認
			SampleOdenOsAdp resultSampleAdp = (SampleOdenOsAdp) obj.getEquipmentConfiguratorOptDeviceImpl().getOdenOSAdapterService(); 
			assertTrue(resultSampleAdp.resultGetLinkId == true);
			assertTrue(resultSampleAdp.resultRequestLink == true);
			assertTrue(resultSampleAdp.resultDeleteFlow == false);
			assertTrue(resultSampleAdp.resultPutFlow == true);
			
			// Sdtnc メソッド呼び出し確認
			SampleSdtncService resultSmpleSdt = (SampleSdtncService)obj.getSdtncService();
			assertTrue(resultSmpleSdt.callLogin == true);
			assertTrue(resultSmpleSdt.callLogout == true);
			assertTrue(resultSmpleSdt.callGetLspResource == true);
			assertTrue(resultSmpleSdt.callCreatePw == true);
			assertTrue(resultSmpleSdt.callDeleatPw == false);
			assertTrue(resultSmpleSdt.callGetPw == false);
			
			// getLink 引数確認
			assertEquals(resultSampleAdp.resultLinkIdList.size(), 4);
			assertTrue(resultSampleAdp.resultLinkIdList.get(0).equals(linkEntityTl1.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.get(1).equals(linkEntityTl2.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.get(2).equals(linkEntityTl5.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.get(3).equals(linkEntityTl4.linkId));
			
			// requestLink 引数確認
			assertEquals(resultSampleAdp.resultPtLinkList.size(), 4);
			compareLinkEntity(resultSampleAdp.resultPtLinkList.get(0), linkEntityTl1);
			compareLinkEntity(resultSampleAdp.resultPtLinkList.get(1), linkEntityTl2);
			compareLinkEntity(resultSampleAdp.resultPtLinkList.get(2), linkEntityTl5);
			compareLinkEntity(resultSampleAdp.resultPtLinkList.get(3), linkEntityTl4);
			
			// putFlow 引数確認
			assertEquals(resultSampleAdp.resultPutFlowEntity.size(), 2);
			compareFlowEntity(resultSampleAdp.resultPutFlowEntity.get(0), addedFlowDto.name + "_1", flowEntityFlow1);
			compareFlowEntity(resultSampleAdp.resultPutFlowEntity.get(1), addedFlowDto.name + "_2", flowEntityFlow2);
			assertTrue(obj.getEquipmentConfiguratorOptDeviceImpl().isFlowId(flowId) == true);
			
			// sdtncService.login 引数確認
			assertEquals(resultSmpleSdt.loginDto.login.loginId,LOGIN_ID);
			assertEquals(resultSmpleSdt.loginDto.login.loginPassword,LOGIN_PASS);
			assertEquals(resultSmpleSdt.loginDto.login.ipAddress, LOGIN_IP_ADDRESS);
			
			// sdtncService.getLspResource 引数確認
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_SLICE_ID),SLICE_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_VLINK_ID), HOP_LINK_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_TOKEN),"token");
			
			// sdtncService.createPw 引数確認
			assertEquals(resultSmpleSdt.addPwDto.slice.groupIndex,SLICE_ID);
			assertEquals(resultSmpleSdt.addPwDto.auth.token,"token");
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).vlan.uni.vVlanVLanIdNniClient == VLANID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).vObjectName, addedFlowDto.id + "_" + addedFlowDto.name);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(0).neId, NE_ID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(0).portId, HOP_PORT_ID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(1).neId, NE_ID_Z);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(1).portId, HOP_PORT_ID_Z);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSSla == 5);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSPir == QOS_PIR);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSCir == QOS_CIR);
			
			// sdtncService.logout 引数確認
			assertEquals(resultSmpleSdt.logoutDto.login.loginId,LOGIN_ID);
			
			// pathId登録確認
			assertEquals(obj.getPathIdMap().get(flowId), vPathId);
			
		} catch (MloException e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl#addFlow()}.
	 * slaModeが1以外の場合
	 */
	@Test
	public void testAddFlow11_putVPath_workarround_vlanid421() {
		
		File propFile = new File(DATA_PATH, DATA_FILE_003);
		ConfigProvider configProvider = ConfigProviderImplTest.createConfigProviderImpl(propFile.getAbsolutePath());
		SdtncDtoOtherConfigImpl dtoConf = new SdtncDtoOtherConfigImpl();
		dtoConf.setConfigProvider(configProvider);
		obj.setSdtncDtoOtherConfig(dtoConf);
		
		final String expectedVlan421APortId = "1.1.1.3.5.1.3.1.0.1.1.1.0.3.0.2.5";
		final String expectedVlan421ZPortId = "1.1.3.3.5.1.3.1.0.1.1.1.0.3.0.2.5";
		
		int flowId = 1;
		FlowDto addedFlowDto = createFlowDto(flowId, AMN64001_NODE_NAME, AMN64003_NODE_NAME, Arrays.asList(LINK_ID_TL1, LINK_ID_TL2));
		
		FlowDto reqDto = getCreateReqDto();
		
		try {
			obj.addFlow(addedFlowDto, reqDto, restifRequestDto);
			
			// OdenOS メソッド呼び出し確認
			SampleOdenOsAdp resultSampleAdp = (SampleOdenOsAdp) obj.getEquipmentConfiguratorOptDeviceImpl().getOdenOSAdapterService(); 
			assertTrue(resultSampleAdp.resultGetLinkId == true);
			assertTrue(resultSampleAdp.resultRequestLink == true);
			assertTrue(resultSampleAdp.resultDeleteFlow == false);
			assertTrue(resultSampleAdp.resultPutFlow == true);
			
			// Sdtnc メソッド呼び出し確認
			SampleSdtncService resultSmpleSdt = (SampleSdtncService)obj.getSdtncService();
			assertTrue(resultSmpleSdt.callLogin == true);
			assertTrue(resultSmpleSdt.callLogout == true);
			assertTrue(resultSmpleSdt.callGetLspResource == true);
			assertTrue(resultSmpleSdt.callCreatePw == true);
			assertTrue(resultSmpleSdt.callDeleatPw == false);
			assertTrue(resultSmpleSdt.callGetPw == false);
			
			// getLink 引数確認
			assertEquals(resultSampleAdp.resultLinkIdList.size(), 4);
			assertTrue(resultSampleAdp.resultLinkIdList.get(0).equals(linkEntityTl1.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.get(1).equals(linkEntityTl2.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.get(2).equals(linkEntityTl5.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.get(3).equals(linkEntityTl4.linkId));
			
			// requestLink 引数確認
			assertEquals(resultSampleAdp.resultPtLinkList.size(), 4);
			compareLinkEntity(resultSampleAdp.resultPtLinkList.get(0), linkEntityTl1);
			compareLinkEntity(resultSampleAdp.resultPtLinkList.get(1), linkEntityTl2);
			compareLinkEntity(resultSampleAdp.resultPtLinkList.get(2), linkEntityTl5);
			compareLinkEntity(resultSampleAdp.resultPtLinkList.get(3), linkEntityTl4);
			
			// putFlow 引数確認
			assertEquals(resultSampleAdp.resultPutFlowEntity.size(), 2);
			compareFlowEntity(resultSampleAdp.resultPutFlowEntity.get(0), addedFlowDto.name + "_1", flowEntityFlow1);
			compareFlowEntity(resultSampleAdp.resultPutFlowEntity.get(1), addedFlowDto.name + "_2", flowEntityFlow2);
			assertTrue(obj.getEquipmentConfiguratorOptDeviceImpl().isFlowId(flowId) == true);
			
			// sdtncService.login 引数確認
			assertEquals(resultSmpleSdt.loginDto.login.loginId,LOGIN_ID);
			assertEquals(resultSmpleSdt.loginDto.login.loginPassword,LOGIN_PASS);
			assertEquals(resultSmpleSdt.loginDto.login.ipAddress, LOGIN_IP_ADDRESS);
			
			// sdtncService.getLspResource 引数確認
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_SLICE_ID),SLICE_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_VLINK_ID), HOP_LINK_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_TOKEN),"token");
			
			// sdtncService.createPw 引数確認
			assertEquals(resultSmpleSdt.addPwDto.slice.groupIndex,SLICE_ID);
			assertEquals(resultSmpleSdt.addPwDto.auth.token,"token");
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).vlan.uni.vVlanVLanIdNniClient == VLANID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).vObjectName, addedFlowDto.id + "_" + addedFlowDto.name);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(0).neId, NE_ID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(0).portId, expectedVlan421APortId);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(1).neId, NE_ID_Z);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(1).portId, expectedVlan421ZPortId);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSSla == 5);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSPir == QOS_PIR);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSCir == QOS_CIR);
			
			// sdtncService.logout 引数確認
			assertEquals(resultSmpleSdt.logoutDto.login.loginId,LOGIN_ID);
			
			// pathId登録確認
			assertEquals(obj.getPathIdMap().get(flowId), vPathId);
			
			
		} catch (MloException e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl#addFlow()}.
	 * slaModeが1以外の場合
	 */
	@Test
	public void testAddFlow11_putVPath_workarround_vlanid422() {
		
		File propFile = new File(DATA_PATH, DATA_FILE_003);
		ConfigProvider configProvider = ConfigProviderImplTest.createConfigProviderImpl(propFile.getAbsolutePath());
		SdtncDtoOtherConfigImpl dtoConf = new SdtncDtoOtherConfigImpl();
		dtoConf.setConfigProvider(configProvider);
		obj.setSdtncDtoOtherConfig(dtoConf);
		
		final String expectedVlan422APortId = "1.1.1.3.5.1.3.1.0.1.1.1.0.3.0.2.6";
		final String expectedVlan422ZPortId = "1.1.3.3.5.1.3.1.0.1.1.1.0.3.0.2.6";
		final Integer expectedVlanid = 422;
		
		int flowId = 1;
		FlowDto addedFlowDto = createFlowDto(flowId, AMN64001_NODE_NAME, AMN64003_NODE_NAME, Arrays.asList(LINK_ID_TL1, LINK_ID_TL2));
		
		FlowDto reqDto = getCreateReqDto();
		reqDto.srcCENodeName = "tokyo";
		reqDto.srcCEPortNo = "00000002";
		reqDto.dstCENodeName = "akashi";
		reqDto.dstCEPortNo = "00000005";
		
		try {
			obj.addFlow(addedFlowDto, reqDto, restifRequestDto);
			
			// OdenOS メソッド呼び出し確認
			SampleOdenOsAdp resultSampleAdp = (SampleOdenOsAdp) obj.getEquipmentConfiguratorOptDeviceImpl().getOdenOSAdapterService(); 
			assertTrue(resultSampleAdp.resultGetLinkId == true);
			assertTrue(resultSampleAdp.resultRequestLink == true);
			assertTrue(resultSampleAdp.resultDeleteFlow == false);
			assertTrue(resultSampleAdp.resultPutFlow == true);
			
			// Sdtnc メソッド呼び出し確認
			SampleSdtncService resultSmpleSdt = (SampleSdtncService)obj.getSdtncService();
			assertTrue(resultSmpleSdt.callLogin == true);
			assertTrue(resultSmpleSdt.callLogout == true);
			assertTrue(resultSmpleSdt.callGetLspResource == true);
			assertTrue(resultSmpleSdt.callCreatePw == true);
			assertTrue(resultSmpleSdt.callDeleatPw == false);
			assertTrue(resultSmpleSdt.callGetPw == false);
			
			// getLink 引数確認
			assertEquals(resultSampleAdp.resultLinkIdList.size(), 4);
			assertTrue(resultSampleAdp.resultLinkIdList.get(0).equals(linkEntityTl1.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.get(1).equals(linkEntityTl2.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.get(2).equals(linkEntityTl5.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.get(3).equals(linkEntityTl4.linkId));
			
			// requestLink 引数確認
			assertEquals(resultSampleAdp.resultPtLinkList.size(), 4);
			compareLinkEntity(resultSampleAdp.resultPtLinkList.get(0), linkEntityTl1);
			compareLinkEntity(resultSampleAdp.resultPtLinkList.get(1), linkEntityTl2);
			compareLinkEntity(resultSampleAdp.resultPtLinkList.get(2), linkEntityTl5);
			compareLinkEntity(resultSampleAdp.resultPtLinkList.get(3), linkEntityTl4);
			
			// putFlow 引数確認
			assertEquals(resultSampleAdp.resultPutFlowEntity.size(), 2);
			compareFlowEntity(resultSampleAdp.resultPutFlowEntity.get(0), addedFlowDto.name + "_1", flowEntityFlow1);
			compareFlowEntity(resultSampleAdp.resultPutFlowEntity.get(1), addedFlowDto.name + "_2", flowEntityFlow2);
			assertTrue(obj.getEquipmentConfiguratorOptDeviceImpl().isFlowId(flowId) == true);
			
			// sdtncService.login 引数確認
			assertEquals(resultSmpleSdt.loginDto.login.loginId,LOGIN_ID);
			assertEquals(resultSmpleSdt.loginDto.login.loginPassword,LOGIN_PASS);
			assertEquals(resultSmpleSdt.loginDto.login.ipAddress, LOGIN_IP_ADDRESS);
			
			// sdtncService.getLspResource 引数確認
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_SLICE_ID),SLICE_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_VLINK_ID), HOP_LINK_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_TOKEN),"token");
			
			// sdtncService.createPw 引数確認
			assertEquals(resultSmpleSdt.addPwDto.slice.groupIndex,SLICE_ID);
			assertEquals(resultSmpleSdt.addPwDto.auth.token,"token");
			assertEquals(expectedVlanid, resultSmpleSdt.addPwDto.vpath.get(0).vlan.uni.vVlanVLanIdNniClient);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).vObjectName, addedFlowDto.id + "_" + addedFlowDto.name);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(0).neId, NE_ID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(0).portId, expectedVlan422APortId);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(1).neId, NE_ID_Z);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(1).portId, expectedVlan422ZPortId);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSSla == 5);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSPir == QOS_PIR);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSCir == QOS_CIR);
			
			// sdtncService.logout 引数確認
			assertEquals(resultSmpleSdt.logoutDto.login.loginId,LOGIN_ID);
			
			// pathId登録確認
			assertEquals(obj.getPathIdMap().get(flowId), vPathId);
			
		} catch (MloException e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl#addFlow()}.
	 * LSPリソース取得：失敗 ログアウト：成功
	 */
	@Test
	public void testAddFlowError1() {
		
		int flowId = 4;
		FlowDto addedFlowDto = createFlowDto(flowId, AMN64003_NODE_NAME, AMN64001_NODE_NAME, Arrays.asList(LINK_ID_TL6));

		SampleSdtncService sampleSdtnc = new SampleSdtncService();
		sampleSdtnc.getLspFailFlag = true;										// LSP取得時にエラーを発生させる
		obj.setSdtncService(sampleSdtnc);
		
		FlowDto reqDto = getCreateReqDto();
		
		try {
			obj.addFlow(addedFlowDto, reqDto, restifRequestDto);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof InternalException);
			assertEquals(e.getMessage(), "getLspResource error");
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl#addFlow()}.
	 * LSPリソース取得：失敗 ログアウト：失敗
	 */
	@Test
	public void testAddFlowError2() {
		
		int flowId = 4;
		FlowDto addedFlowDto = createFlowDto(flowId, AMN64003_NODE_NAME, AMN64001_NODE_NAME, Arrays.asList(LINK_ID_TL6));

		SampleSdtncService sampleSdtnc = new SampleSdtncService();
		sampleSdtnc.getLspFailFlag = true;										// LSP取得時にエラーを発生させる
		sampleSdtnc.logoutFailFlag = true;										// ログアウト時にエラーを発生させる
		obj.setSdtncService(sampleSdtnc);
		
		FlowDto reqDto = getCreateReqDto();
		
		try {
			obj.addFlow(addedFlowDto, reqDto, restifRequestDto);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof InternalException);
			assertEquals(e.getMessage(), "getLspResource error");
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl#addFlow()}.
	 * ログイン：失敗
	 */
	@Test
	public void testAddFlowError3() {
		
		int flowId = 4;
		FlowDto addedFlowDto = createFlowDto(flowId, AMN64003_NODE_NAME, AMN64001_NODE_NAME, Arrays.asList(LINK_ID_TL6));

		SampleSdtncService sampleSdtnc = new SampleSdtncService();
		sampleSdtnc.loginFailFlag = true;										// ログイン時にエラーを発生させる
		obj.setSdtncService(sampleSdtnc);
		
		FlowDto reqDto = getCreateReqDto();
		
		
		try {
			obj.addFlow(addedFlowDto, reqDto, restifRequestDto);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof ApiCallException);
			assertEquals(e.getMessage(), "login error");
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl#addFlow()}.
	 * VlanIDエラー1
	 */
	@Test
	public void testAddFlowError4() {
		
		int flowId = 1;
		FlowDto addedFlowDto = createFlowDto(flowId, AMN64001_NODE_NAME, AMN64003_NODE_NAME, Arrays.asList(LINK_ID_TL3));
		
		FlowDto reqDto = getCreateReqDto();
		reqDto.srcCENodeName = "abc";
		reqDto.srcCEPortNo = "00000001";
		reqDto.dstCENodeName = "osaka";
		reqDto.dstCEPortNo = "00000004";
		
		try {
			obj.addFlow(addedFlowDto, reqDto, restifRequestDto);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof ApiCallException);
			assertEquals(e.getMessage(), "vlanId is null [srcCENodeName=" + reqDto.srcCENodeName + ", srcCEPortNo=" + reqDto.srcCEPortNo +"]");
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl#addFlow()}.
	 * VlanIDエラー2
	 */
	@Test
	public void testAddFlowError5() {
		
		int flowId = 1;
		FlowDto addedFlowDto = createFlowDto(flowId, AMN64001_NODE_NAME, AMN64003_NODE_NAME, Arrays.asList(LINK_ID_TL3));
		
		FlowDto reqDto = getCreateReqDto();
		reqDto.srcCENodeName = "tokyo";
		reqDto.srcCEPortNo = "00000001";
		reqDto.dstCENodeName = "osaka";
		reqDto.dstCEPortNo = "00000011";
		
		try {
			obj.addFlow(addedFlowDto, reqDto, restifRequestDto);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof ApiCallException);
			assertEquals(e.getMessage(), "vlanId is null [dstCENodeName=" + reqDto.dstCENodeName + ", dstCEPortNo=" + reqDto.dstCEPortNo +"]");
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl#addFlow()}.
	 * VlanIDエラー3
	 */
	@Test
	public void testAddFlowError6() {
		
		int flowId = 1;
		FlowDto addedFlowDto = createFlowDto(flowId, AMN64001_NODE_NAME, AMN64003_NODE_NAME, Arrays.asList(LINK_ID_TL3));
		
		FlowDto reqDto = getCreateReqDto();
		reqDto.srcCENodeName = "tokyo";
		reqDto.srcCEPortNo = "00000002";
		reqDto.dstCENodeName = "osaka";
		reqDto.dstCEPortNo = "00000004";
		
		try {
			obj.addFlow(addedFlowDto, reqDto, restifRequestDto);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof ApiCallException);
			assertEquals(e.getMessage(), "vlanId is different  [srcCENodeName=" + reqDto.srcCENodeName + 
					", srcCEPortNo=" + reqDto.srcCEPortNo + 
					", dstCENodeName=" + reqDto.dstCENodeName + 
					", dstCEPortNo=" + reqDto.dstCEPortNo +"]");
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl#modFlow()}.
	 * P1→P2→P3(リンク未設定)
	 */
	@Test
	public void testModFlow1() {
		
		int flowId = 9;
		FlowDto modFlowDto = createFlowDto(flowId, AMN64001_NODE_NAME, AMN64003_NODE_NAME, Arrays.asList(LINK_ID_TL1, LINK_ID_TL2));
		
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		map.put(flowId, vPathId2);
		obj.setPathIdMap(map);
		
		FlowDto reqDto = getUpdateReqDto();
		
		try {
			obj.modFlow(modFlowDto, reqDto, restifRequestDto);
			
			// bandwidthとdelayの上書き確認
			assertEquals(modFlowDto.usedBandWidth, reqDto.reqBandWidth);
			assertEquals(modFlowDto.delayTime, reqDto.reqDelay);
			
			// OdenOS メソッド呼び出し確認
			SampleOdenOsAdp resultSampleAdp = (SampleOdenOsAdp) obj.getEquipmentConfiguratorOptDeviceImpl().getOdenOSAdapterService(); 
			assertTrue(resultSampleAdp.resultGetLinkId == true);
			assertTrue(resultSampleAdp.resultRequestLink == true);
			assertTrue(resultSampleAdp.resultDeleteFlow == false);
			assertTrue(resultSampleAdp.resultPutFlow == true);
			
			// Sdtnc メソッド呼び出し確認
			SampleSdtncService resultSmpleSdt = (SampleSdtncService)obj.getSdtncService();
			assertTrue(resultSmpleSdt.callLogin == true);
			assertTrue(resultSmpleSdt.callLogout == true);
			assertTrue(resultSmpleSdt.callGetLspResource == true);
			assertTrue(resultSmpleSdt.callCreatePw == true);
			assertTrue(resultSmpleSdt.callDeleatPw == true);
			assertTrue(resultSmpleSdt.callGetPw == false);
			
			// getLink 引数確認
			assertEquals(resultSampleAdp.resultLinkIdList.size(), 4);
			assertTrue(resultSampleAdp.resultLinkIdList.get(0).equals(linkEntityTl1.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.get(1).equals(linkEntityTl2.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.get(2).equals(linkEntityTl5.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.get(3).equals(linkEntityTl4.linkId));
			
			// requestLink 引数確認
			assertEquals(resultSampleAdp.resultPtLinkList.size(), 4);
			compareLinkEntity(resultSampleAdp.resultPtLinkList.get(0), linkEntityTl1);
			compareLinkEntity(resultSampleAdp.resultPtLinkList.get(1), linkEntityTl2);
			compareLinkEntity(resultSampleAdp.resultPtLinkList.get(2), linkEntityTl5);
			compareLinkEntity(resultSampleAdp.resultPtLinkList.get(3), linkEntityTl4);
			
			// putFlow 引数確認
			assertEquals(resultSampleAdp.resultPutFlowEntity.size(), 2);
			compareFlowEntity(resultSampleAdp.resultPutFlowEntity.get(0), modFlowDto.name + "_1", flowEntityFlow1);
			compareFlowEntity(resultSampleAdp.resultPutFlowEntity.get(1), modFlowDto.name + "_2", flowEntityFlow2);
			assertTrue(obj.getEquipmentConfiguratorOptDeviceImpl().isFlowId(flowId) == true);
			
			// sdtncService.login 引数確認
			assertEquals(resultSmpleSdt.loginDto.login.loginId, LOGIN_ID);
			assertEquals(resultSmpleSdt.loginDto.login.loginPassword, LOGIN_PASS);
			assertEquals(resultSmpleSdt.loginDto.login.ipAddress, LOGIN_IP_ADDRESS);

			// sdtncService.getLspResource 引数確認
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_SLICE_ID),SLICE_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_VLINK_ID),HOP_LINK_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_TOKEN),"token");
						
			// sdtncService.deletePw 引数確認
			assertEquals(resultSmpleSdt.delPwMap.get(PARAM_KEY_SLICE_ID), SLICE_ID);
			assertEquals(resultSmpleSdt.delPwMap.get(PARAM_KEY_VPATH_ID), vPathId2);
			assertEquals(resultSmpleSdt.delPwMap.get(PARAM_KEY_TOKEN), "token");
			
			// sdtncService.createPw 引数確認
			assertEquals(resultSmpleSdt.addPwDto.slice.groupIndex,SLICE_ID);
			assertEquals(resultSmpleSdt.addPwDto.auth.token,"token");
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).vlan.uni.vVlanVLanIdNniClient == VLANID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).vObjectName, modFlowDto.id + "_" + modFlowDto.name);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(0).neId, NE_ID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(0).portId, HOP_PORT_ID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(1).neId, NE_ID_Z);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(1).portId, HOP_PORT_ID_Z);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathRoute.get(0).line.get(0).elementIndex, HOP_LINK_ID);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSSla == DEF_SLA_MODE);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSPir == 0);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSCir == QOS_CIR);

			// sdtncService.logout 引数確認
			assertEquals(resultSmpleSdt.logoutDto.login.loginId,LOGIN_ID);
			
			// pathId登録確認
			assertEquals(obj.getPathIdMap().get(flowId), vPathId);
			
		} catch (MloException e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl#modFlow()}.
	 * P3→P2→P1(リンク未設定)
	 */
	@Test
	public void testModFlow2() {
		int flowId = 10;
		FlowDto modFlowDto = createFlowDto(flowId, AMN64003_NODE_NAME, AMN64001_NODE_NAME, Arrays.asList(LINK_ID_TL5, LINK_ID_TL4));
		
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		map.put(flowId, vPathId2);
		obj.setPathIdMap(map);
		
		FlowDto reqDto = getUpdateReqDto();
		
		try {
			obj.modFlow(modFlowDto, reqDto, restifRequestDto);
			
			// OdenOS メソッド呼び出し確認
			SampleOdenOsAdp resultSampleAdp = (SampleOdenOsAdp) obj.getEquipmentConfiguratorOptDeviceImpl().getOdenOSAdapterService(); 
			assertTrue(resultSampleAdp.resultGetLinkId == true);
			assertTrue(resultSampleAdp.resultRequestLink == true);
			assertTrue(resultSampleAdp.resultDeleteFlow == false);
			assertTrue(resultSampleAdp.resultPutFlow == true);
			
			// Sdtnc メソッド呼び出し確認
			SampleSdtncService resultSmpleSdt = (SampleSdtncService)obj.getSdtncService();
			assertTrue(resultSmpleSdt.callLogin == true);
			assertTrue(resultSmpleSdt.callLogout == true);
			assertTrue(resultSmpleSdt.callGetLspResource == true);
			assertTrue(resultSmpleSdt.callCreatePw == true);
			assertTrue(resultSmpleSdt.callDeleatPw == true);
			assertTrue(resultSmpleSdt.callGetPw == false);
			
			// getLink 引数確認
			assertEquals(resultSampleAdp.resultLinkIdList.size(), 4);
			assertTrue(resultSampleAdp.resultLinkIdList.get(0).equals(linkEntityTl5.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.get(1).equals(linkEntityTl4.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.get(2).equals(linkEntityTl1.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.get(3).equals(linkEntityTl2.linkId));
			
			// requestLink 引数確認
			assertEquals(resultSampleAdp.resultPtLinkList.size(), 4);
			compareLinkEntity(resultSampleAdp.resultPtLinkList.get(0), linkEntityTl5);
			compareLinkEntity(resultSampleAdp.resultPtLinkList.get(1), linkEntityTl4);
			compareLinkEntity(resultSampleAdp.resultPtLinkList.get(2), linkEntityTl1);
			compareLinkEntity(resultSampleAdp.resultPtLinkList.get(3), linkEntityTl2);
			
			// putFlow 引数確認
			assertEquals(resultSampleAdp.resultPutFlowEntity.size(), 2);
			compareFlowEntity(resultSampleAdp.resultPutFlowEntity.get(0), modFlowDto.name + "_1", flowEntityFlow2);
			compareFlowEntity(resultSampleAdp.resultPutFlowEntity.get(1), modFlowDto.name + "_2", flowEntityFlow1);
			assertTrue(obj.getEquipmentConfiguratorOptDeviceImpl().isFlowId(flowId) == true);
			
			// sdtncService.login 引数確認
			assertEquals(resultSmpleSdt.loginDto.login.loginId, LOGIN_ID);
			assertEquals(resultSmpleSdt.loginDto.login.loginPassword, LOGIN_PASS);
			assertEquals(resultSmpleSdt.loginDto.login.ipAddress, LOGIN_IP_ADDRESS);

			// sdtncService.getLspResource 引数確認
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_SLICE_ID),SLICE_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_VLINK_ID),HOP_LINK_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_TOKEN),"token");
						
			// sdtncService.deletePw 引数確認
			assertEquals(resultSmpleSdt.delPwMap.get(PARAM_KEY_SLICE_ID), SLICE_ID);
			assertEquals(resultSmpleSdt.delPwMap.get(PARAM_KEY_VPATH_ID), vPathId2);
			assertEquals(resultSmpleSdt.delPwMap.get(PARAM_KEY_TOKEN), "token");
			
			// sdtncService.createPw 引数確認
			assertEquals(resultSmpleSdt.addPwDto.slice.groupIndex,SLICE_ID);
			assertEquals(resultSmpleSdt.addPwDto.auth.token,"token");
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).vlan.uni.vVlanVLanIdNniClient == VLANID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).vObjectName, modFlowDto.id + "_" + modFlowDto.name);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(0).neId, NE_ID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(0).portId, HOP_PORT_ID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(1).neId, NE_ID_Z);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(1).portId, HOP_PORT_ID_Z);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathRoute.get(0).line.get(0).elementIndex, HOP_LINK_ID);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSSla == DEF_SLA_MODE);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSPir == 0);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSCir == QOS_CIR);

			// sdtncService.logout 引数確認
			assertEquals(resultSmpleSdt.logoutDto.login.loginId,LOGIN_ID);
			
			// pathId登録確認
			assertEquals(obj.getPathIdMap().get(flowId), vPathId);
			
		} catch (MloException e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl#modFlow()}.
	 * P1→P3(リンク未設定)
	 */
	@Test
	public void testModFlow3() {
		int flowId = 11;
		FlowDto modFlowDto = createFlowDto(flowId, AMN64001_NODE_NAME, AMN64003_NODE_NAME, Arrays.asList(LINK_ID_TL3));
		
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		map.put(flowId, vPathId2);
		obj.setPathIdMap(map);
		
		FlowDto reqDto = getUpdateReqDto();
		
		try {
			obj.modFlow(modFlowDto, reqDto, restifRequestDto);
			
			// OdenOS メソッド呼び出し確認
			SampleOdenOsAdp resultSampleAdp = (SampleOdenOsAdp) obj.getEquipmentConfiguratorOptDeviceImpl().getOdenOSAdapterService(); 
			assertTrue(resultSampleAdp.resultGetLinkId == true);
			assertTrue(resultSampleAdp.resultRequestLink == true);
			assertTrue(resultSampleAdp.resultDeleteFlow == false);
			assertTrue(resultSampleAdp.resultPutFlow == true);
			
			// Sdtnc メソッド呼び出し確認
			SampleSdtncService resultSmpleSdt = (SampleSdtncService)obj.getSdtncService();
			assertTrue(resultSmpleSdt.callLogin == true);
			assertTrue(resultSmpleSdt.callLogout == true);
			assertTrue(resultSmpleSdt.callGetLspResource == true);
			assertTrue(resultSmpleSdt.callCreatePw == true);
			assertTrue(resultSmpleSdt.callDeleatPw == true);
			assertTrue(resultSmpleSdt.callGetPw == false);
			
			// getLink 引数確認
			assertEquals(resultSampleAdp.resultLinkIdList.size(), 2);
			assertTrue(resultSampleAdp.resultLinkIdList.get(0).equals(linkEntityTl3.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.get(1).equals(linkEntityTl6.linkId));
			
			// requestLink 引数確認
			assertEquals(resultSampleAdp.resultPtLinkList.size(), 2);
			compareLinkEntity(resultSampleAdp.resultPtLinkList.get(0), linkEntityTl3);
			compareLinkEntity(resultSampleAdp.resultPtLinkList.get(1), linkEntityTl6);
			
			// putFlow 引数確認
			assertEquals(resultSampleAdp.resultPutFlowEntity.size(), 2);
			compareFlowEntity(resultSampleAdp.resultPutFlowEntity.get(0), modFlowDto.name + "_1", flowEntityFlow3);
			compareFlowEntity(resultSampleAdp.resultPutFlowEntity.get(1), modFlowDto.name + "_2", flowEntityFlow4);
			assertTrue(obj.getEquipmentConfiguratorOptDeviceImpl().isFlowId(flowId) == true);
			
			// sdtncService.login 引数確認
			assertEquals(resultSmpleSdt.loginDto.login.loginId, LOGIN_ID);
			assertEquals(resultSmpleSdt.loginDto.login.loginPassword, LOGIN_PASS);
			assertEquals(resultSmpleSdt.loginDto.login.ipAddress, LOGIN_IP_ADDRESS);

			// sdtncService.getLspResource 引数確認
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_SLICE_ID),SLICE_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_VLINK_ID),CUT_LINK_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_TOKEN),"token");
						
			// sdtncService.deletePw 引数確認
			assertEquals(resultSmpleSdt.delPwMap.get(PARAM_KEY_SLICE_ID), SLICE_ID);
			assertEquals(resultSmpleSdt.delPwMap.get(PARAM_KEY_VPATH_ID), vPathId2);
			assertEquals(resultSmpleSdt.delPwMap.get(PARAM_KEY_TOKEN), "token");
			
			// sdtncService.createPw 引数確認
			assertEquals(resultSmpleSdt.addPwDto.slice.groupIndex,SLICE_ID);
			assertEquals(resultSmpleSdt.addPwDto.auth.token,"token");
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).vlan.uni.vVlanVLanIdNniClient == VLANID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).vObjectName, modFlowDto.id + "_" + modFlowDto.name);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(0).neId, NE_ID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(0).portId, CUT_PORT_ID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(1).neId, NE_ID_Z);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(1).portId, CUT_PORT_ID_Z);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathRoute.get(0).line.get(0).elementIndex, CUT_LINK_ID);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSSla == DEF_SLA_MODE);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSPir == 0);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSCir == QOS_CIR);

			// sdtncService.logout 引数確認
			assertEquals(resultSmpleSdt.logoutDto.login.loginId,LOGIN_ID);
			
			// pathId登録確認
			assertEquals(obj.getPathIdMap().get(flowId), vPathId);
			
		} catch (MloException e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl#modFlow()}.
	 * P3→P1(リンク未設定)
	 */
	@Test
	public void testModFlow4() {
		
		int flowId = 12;
		FlowDto modFlowDto = createFlowDto(flowId, AMN64003_NODE_NAME, AMN64001_NODE_NAME, Arrays.asList(LINK_ID_TL6));
		
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		map.put(flowId, vPathId2);
		obj.setPathIdMap(map);
		
		FlowDto reqDto = getUpdateReqDto();
		
		try {
			obj.modFlow(modFlowDto, reqDto, restifRequestDto);
			
			// OdenOS メソッド呼び出し確認
			SampleOdenOsAdp resultSampleAdp = (SampleOdenOsAdp) obj.getEquipmentConfiguratorOptDeviceImpl().getOdenOSAdapterService(); 
			assertTrue(resultSampleAdp.resultGetLinkId == true);
			assertTrue(resultSampleAdp.resultRequestLink == true);
			assertTrue(resultSampleAdp.resultDeleteFlow == false);
			assertTrue(resultSampleAdp.resultPutFlow == true);
			
			// Sdtnc メソッド呼び出し確認
			SampleSdtncService resultSmpleSdt = (SampleSdtncService)obj.getSdtncService();
			assertTrue(resultSmpleSdt.callLogin == true);
			assertTrue(resultSmpleSdt.callLogout == true);
			assertTrue(resultSmpleSdt.callGetLspResource == true);
			assertTrue(resultSmpleSdt.callCreatePw == true);
			assertTrue(resultSmpleSdt.callDeleatPw == true);
			assertTrue(resultSmpleSdt.callGetPw == false);
			
			// getLink 引数確認
			assertEquals(resultSampleAdp.resultLinkIdList.size(), 2);
			assertTrue(resultSampleAdp.resultLinkIdList.get(0).equals(linkEntityTl6.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.get(1).equals(linkEntityTl3.linkId));
			
			// requestLink 引数確認
			assertEquals(resultSampleAdp.resultPtLinkList.size(), 2);
			compareLinkEntity(resultSampleAdp.resultPtLinkList.get(0), linkEntityTl6);
			compareLinkEntity(resultSampleAdp.resultPtLinkList.get(1), linkEntityTl3);
			
			// putFlow 引数確認
			assertEquals(resultSampleAdp.resultPutFlowEntity.size(), 2);
			compareFlowEntity(resultSampleAdp.resultPutFlowEntity.get(0), modFlowDto.name + "_1", flowEntityFlow4);
			compareFlowEntity(resultSampleAdp.resultPutFlowEntity.get(1), modFlowDto.name + "_2", flowEntityFlow3);
			assertTrue(obj.getEquipmentConfiguratorOptDeviceImpl().isFlowId(flowId) == true);
			
			// sdtncService.login 引数確認
			assertEquals(resultSmpleSdt.loginDto.login.loginId, LOGIN_ID);
			assertEquals(resultSmpleSdt.loginDto.login.loginPassword, LOGIN_PASS);
			assertEquals(resultSmpleSdt.loginDto.login.ipAddress, LOGIN_IP_ADDRESS);

			// sdtncService.getLspResource 引数確認
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_SLICE_ID),SLICE_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_VLINK_ID),CUT_LINK_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_TOKEN),"token");
						
			// sdtncService.deletePw 引数確認
			assertEquals(resultSmpleSdt.delPwMap.get(PARAM_KEY_SLICE_ID), SLICE_ID);
			assertEquals(resultSmpleSdt.delPwMap.get(PARAM_KEY_VPATH_ID), vPathId2);
			assertEquals(resultSmpleSdt.delPwMap.get(PARAM_KEY_TOKEN), "token");
			
			// sdtncService.createPw 引数確認
			assertEquals(resultSmpleSdt.addPwDto.slice.groupIndex,SLICE_ID);
			assertEquals(resultSmpleSdt.addPwDto.auth.token,"token");
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).vlan.uni.vVlanVLanIdNniClient == VLANID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).vObjectName, modFlowDto.id + "_" + modFlowDto.name);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(0).neId, NE_ID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(0).portId, CUT_PORT_ID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(1).neId, NE_ID_Z);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(1).portId, CUT_PORT_ID_Z);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathRoute.get(0).line.get(0).elementIndex, CUT_LINK_ID);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSSla == DEF_SLA_MODE);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSPir == 0);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSCir == QOS_CIR);

			// sdtncService.logout 引数確認
			assertEquals(resultSmpleSdt.logoutDto.login.loginId,LOGIN_ID);
			
			// pathId登録確認
			assertEquals(obj.getPathIdMap().get(flowId), vPathId);
			
		} catch (MloException e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl#modFlow()}.
	 * P1→P2→P3(リンク設定済)
	 */
	@Test
	public void testModFlow5() {
		SampleOdenOsAdp sampleAdp = new SampleOdenOsAdp();
		sampleAdp.getLinkFlag = true;
		obj.getEquipmentConfiguratorOptDeviceImpl().setOdenOSAdapterService(sampleAdp);

		int flowId = 13;
		FlowDto modFlowDto = createFlowDto(flowId, AMN64001_NODE_NAME, AMN64003_NODE_NAME, Arrays.asList(LINK_ID_TL1, LINK_ID_TL2));
		
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		map.put(flowId, vPathId2);
		obj.setPathIdMap(map);
		
		FlowDto reqDto = getUpdateReqDto();
		
		try {
			obj.modFlow(modFlowDto, reqDto, restifRequestDto);
			
			// OdenOS メソッド呼び出し確認
			SampleOdenOsAdp resultSampleAdp = (SampleOdenOsAdp) obj.getEquipmentConfiguratorOptDeviceImpl().getOdenOSAdapterService(); 
			assertTrue(resultSampleAdp.resultGetLinkId == true);
			assertTrue(resultSampleAdp.resultRequestLink == false);
			assertTrue(resultSampleAdp.resultDeleteFlow == false);
			assertTrue(resultSampleAdp.resultPutFlow == true);
			
			// Sdtnc メソッド呼び出し確認
			SampleSdtncService resultSmpleSdt = (SampleSdtncService)obj.getSdtncService();
			assertTrue(resultSmpleSdt.callLogin == true);
			assertTrue(resultSmpleSdt.callLogout == true);
			assertTrue(resultSmpleSdt.callGetLspResource == true);
			assertTrue(resultSmpleSdt.callCreatePw == true);
			assertTrue(resultSmpleSdt.callDeleatPw == true);
			assertTrue(resultSmpleSdt.callGetPw == false);
			
			// getLink 引数確認
			assertEquals(resultSampleAdp.resultLinkIdList.size(), 4);
			assertTrue(resultSampleAdp.resultLinkIdList.get(0).equals(linkEntityTl1.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.get(1).equals(linkEntityTl2.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.get(2).equals(linkEntityTl5.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.get(3).equals(linkEntityTl4.linkId));
			
			// putFlow 引数確認
			assertEquals(resultSampleAdp.resultPutFlowEntity.size(), 2);
			compareFlowEntity(resultSampleAdp.resultPutFlowEntity.get(0), modFlowDto.name + "_1", flowEntityFlow1);
			compareFlowEntity(resultSampleAdp.resultPutFlowEntity.get(1), modFlowDto.name + "_2", flowEntityFlow2);
			assertTrue(obj.getEquipmentConfiguratorOptDeviceImpl().isFlowId(flowId) == true);
			
			// sdtncService.login 引数確認
			assertEquals(resultSmpleSdt.loginDto.login.loginId, LOGIN_ID);
			assertEquals(resultSmpleSdt.loginDto.login.loginPassword, LOGIN_PASS);
			assertEquals(resultSmpleSdt.loginDto.login.ipAddress, LOGIN_IP_ADDRESS);

			// sdtncService.getLspResource 引数確認
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_SLICE_ID),SLICE_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_VLINK_ID),HOP_LINK_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_TOKEN),"token");
						
			// sdtncService.deletePw 引数確認
			assertEquals(resultSmpleSdt.delPwMap.get(PARAM_KEY_SLICE_ID), SLICE_ID);
			assertEquals(resultSmpleSdt.delPwMap.get(PARAM_KEY_VPATH_ID), vPathId2);
			assertEquals(resultSmpleSdt.delPwMap.get(PARAM_KEY_TOKEN), "token");
			
			// sdtncService.createPw 引数確認
			assertEquals(resultSmpleSdt.addPwDto.slice.groupIndex,SLICE_ID);
			assertEquals(resultSmpleSdt.addPwDto.auth.token,"token");
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).vlan.uni.vVlanVLanIdNniClient == VLANID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).vObjectName, modFlowDto.id + "_" + modFlowDto.name);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(0).neId, NE_ID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(0).portId, HOP_PORT_ID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(1).neId, NE_ID_Z);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(1).portId, HOP_PORT_ID_Z);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathRoute.get(0).line.get(0).elementIndex, HOP_LINK_ID);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSSla == DEF_SLA_MODE);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSPir == 0);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSCir == QOS_CIR);

			// sdtncService.logout 引数確認
			assertEquals(resultSmpleSdt.logoutDto.login.loginId,LOGIN_ID);
			
			// pathId登録確認
			assertEquals(obj.getPathIdMap().get(flowId), vPathId);
			
		} catch (MloException e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl#modFlow()}.
	 * P3→P2→P1(リンク設定済)
	 */
	@Test
	public void testModFlow6() {
		SampleOdenOsAdp sampleAdp = new SampleOdenOsAdp();
		sampleAdp.getLinkFlag = true;
		obj.getEquipmentConfiguratorOptDeviceImpl().setOdenOSAdapterService(sampleAdp);
		
		int flowId = 14;
		FlowDto modFlowDto = createFlowDto(flowId, AMN64003_NODE_NAME, AMN64001_NODE_NAME, Arrays.asList(LINK_ID_TL5, LINK_ID_TL4));
		
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		map.put(flowId, vPathId2);
		obj.setPathIdMap(map);
		
		FlowDto reqDto = getUpdateReqDto();
		
		try {
			obj.modFlow(modFlowDto, reqDto, restifRequestDto);
			
			// OdenOS メソッド呼び出し確認
			SampleOdenOsAdp resultSampleAdp = (SampleOdenOsAdp) obj.getEquipmentConfiguratorOptDeviceImpl().getOdenOSAdapterService(); 
			assertTrue(resultSampleAdp.resultGetLinkId == true);
			assertTrue(resultSampleAdp.resultRequestLink == false);
			assertTrue(resultSampleAdp.resultDeleteFlow == false);
			assertTrue(resultSampleAdp.resultPutFlow == true);
			
			// Sdtnc メソッド呼び出し確認
			SampleSdtncService resultSmpleSdt = (SampleSdtncService)obj.getSdtncService();
			assertTrue(resultSmpleSdt.callLogin == true);
			assertTrue(resultSmpleSdt.callLogout == true);
			assertTrue(resultSmpleSdt.callGetLspResource == true);
			assertTrue(resultSmpleSdt.callCreatePw == true);
			assertTrue(resultSmpleSdt.callDeleatPw == true);
			assertTrue(resultSmpleSdt.callGetPw == false);
			
			// getLink 引数確認
			assertEquals(resultSampleAdp.resultLinkIdList.size(), 4);
			assertTrue(resultSampleAdp.resultLinkIdList.get(0).equals(linkEntityTl5.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.get(1).equals(linkEntityTl4.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.get(2).equals(linkEntityTl1.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.get(3).equals(linkEntityTl2.linkId));
			
			// putFlow 引数確認
			assertEquals(resultSampleAdp.resultPutFlowEntity.size(), 2);
			compareFlowEntity(resultSampleAdp.resultPutFlowEntity.get(0), modFlowDto.name + "_1", flowEntityFlow2);
			compareFlowEntity(resultSampleAdp.resultPutFlowEntity.get(1), modFlowDto.name + "_2", flowEntityFlow1);
			assertTrue(obj.getEquipmentConfiguratorOptDeviceImpl().isFlowId(flowId) == true);
			
			// sdtncService.login 引数確認
			assertEquals(resultSmpleSdt.loginDto.login.loginId, LOGIN_ID);
			assertEquals(resultSmpleSdt.loginDto.login.loginPassword, LOGIN_PASS);
			assertEquals(resultSmpleSdt.loginDto.login.ipAddress, LOGIN_IP_ADDRESS);

			// sdtncService.getLspResource 引数確認
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_SLICE_ID),SLICE_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_VLINK_ID),HOP_LINK_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_TOKEN),"token");
						
			// sdtncService.deletePw 引数確認
			assertEquals(resultSmpleSdt.delPwMap.get(PARAM_KEY_SLICE_ID), SLICE_ID);
			assertEquals(resultSmpleSdt.delPwMap.get(PARAM_KEY_VPATH_ID), vPathId2);
			assertEquals(resultSmpleSdt.delPwMap.get(PARAM_KEY_TOKEN), "token");
			
			// sdtncService.createPw 引数確認
			assertEquals(resultSmpleSdt.addPwDto.slice.groupIndex,SLICE_ID);
			assertEquals(resultSmpleSdt.addPwDto.auth.token,"token");
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).vlan.uni.vVlanVLanIdNniClient == VLANID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).vObjectName, modFlowDto.id + "_" + modFlowDto.name);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(0).neId, NE_ID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(0).portId, HOP_PORT_ID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(1).neId, NE_ID_Z);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(1).portId, HOP_PORT_ID_Z);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathRoute.get(0).line.get(0).elementIndex, HOP_LINK_ID);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSSla == DEF_SLA_MODE);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSPir == 0);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSCir == QOS_CIR);

			// sdtncService.logout 引数確認
			assertEquals(resultSmpleSdt.logoutDto.login.loginId,LOGIN_ID);
			
			// pathId登録確認
			assertEquals(obj.getPathIdMap().get(flowId), vPathId);
			
		} catch (MloException e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl#modFlow()}.
	 * P1→P3（リンク設定済）
	 */
	@Test
	public void testModFlow7() {
		SampleOdenOsAdp sampleAdp = new SampleOdenOsAdp();
		sampleAdp.getLinkFlag = true;
		obj.getEquipmentConfiguratorOptDeviceImpl().setOdenOSAdapterService(sampleAdp);

		int flowId = 15;
		FlowDto modFlowDto = createFlowDto(flowId, AMN64001_NODE_NAME, AMN64003_NODE_NAME, Arrays.asList(LINK_ID_TL3));
		
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		map.put(flowId, vPathId2);
		obj.setPathIdMap(map);
		
		FlowDto reqDto = getUpdateReqDto();
		
		try {
			obj.modFlow(modFlowDto, reqDto, restifRequestDto);
			
			// OdenOS メソッド呼び出し確認
			SampleOdenOsAdp resultSampleAdp = (SampleOdenOsAdp) obj.getEquipmentConfiguratorOptDeviceImpl().getOdenOSAdapterService(); 
			assertTrue(resultSampleAdp.resultGetLinkId == true);
			assertTrue(resultSampleAdp.resultRequestLink == false);
			assertTrue(resultSampleAdp.resultDeleteFlow == false);
			assertTrue(resultSampleAdp.resultPutFlow == true);
			
			// Sdtnc メソッド呼び出し確認
			SampleSdtncService resultSmpleSdt = (SampleSdtncService)obj.getSdtncService();
			assertTrue(resultSmpleSdt.callLogin == true);
			assertTrue(resultSmpleSdt.callLogout == true);
			assertTrue(resultSmpleSdt.callGetLspResource == true);
			assertTrue(resultSmpleSdt.callCreatePw == true);
			assertTrue(resultSmpleSdt.callDeleatPw == true);
			assertTrue(resultSmpleSdt.callGetPw == false);
			
			// getLink 引数確認
			assertEquals(resultSampleAdp.resultLinkIdList.size(), 2);
			assertTrue(resultSampleAdp.resultLinkIdList.get(0).equals(linkEntityTl3.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.get(1).equals(linkEntityTl6.linkId));
			
			// putFlow 引数確認
			assertEquals(resultSampleAdp.resultPutFlowEntity.size(), 2);
			compareFlowEntity(resultSampleAdp.resultPutFlowEntity.get(0), modFlowDto.name + "_1", flowEntityFlow3);
			compareFlowEntity(resultSampleAdp.resultPutFlowEntity.get(1), modFlowDto.name + "_2", flowEntityFlow4);
			assertTrue(obj.getEquipmentConfiguratorOptDeviceImpl().isFlowId(flowId) == true);
			
			// sdtncService.login 引数確認
			assertEquals(resultSmpleSdt.loginDto.login.loginId, LOGIN_ID);
			assertEquals(resultSmpleSdt.loginDto.login.loginPassword, LOGIN_PASS);
			assertEquals(resultSmpleSdt.loginDto.login.ipAddress, LOGIN_IP_ADDRESS);

			// sdtncService.getLspResource 引数確認
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_SLICE_ID),SLICE_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_VLINK_ID),CUT_LINK_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_TOKEN),"token");
						
			// sdtncService.deletePw 引数確認
			assertEquals(resultSmpleSdt.delPwMap.get(PARAM_KEY_SLICE_ID), SLICE_ID);
			assertEquals(resultSmpleSdt.delPwMap.get(PARAM_KEY_VPATH_ID), vPathId2);
			assertEquals(resultSmpleSdt.delPwMap.get(PARAM_KEY_TOKEN), "token");
			
			// sdtncService.createPw 引数確認
			assertEquals(resultSmpleSdt.addPwDto.slice.groupIndex,SLICE_ID);
			assertEquals(resultSmpleSdt.addPwDto.auth.token,"token");
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).vlan.uni.vVlanVLanIdNniClient == VLANID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).vObjectName, modFlowDto.id + "_" + modFlowDto.name);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(0).neId, NE_ID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(0).portId, CUT_PORT_ID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(1).neId, NE_ID_Z);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(1).portId, CUT_PORT_ID_Z);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathRoute.get(0).line.get(0).elementIndex, CUT_LINK_ID);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSSla == DEF_SLA_MODE);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSPir == 0);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSCir == QOS_CIR);

			// sdtncService.logout 引数確認
			assertEquals(resultSmpleSdt.logoutDto.login.loginId,LOGIN_ID);
			
			// pathId登録確認
			assertEquals(obj.getPathIdMap().get(flowId), vPathId);
			
		} catch (MloException e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl#modFlow()}.
	 * P3→P1(リンク設定済)
	 */
	@Test
	public void testModFlow8() {
		SampleOdenOsAdp sampleAdp = new SampleOdenOsAdp();
		sampleAdp.getLinkFlag = true;
		obj.getEquipmentConfiguratorOptDeviceImpl().setOdenOSAdapterService(sampleAdp);
		
		int flowId = 16;
		FlowDto modFlowDto = createFlowDto(flowId, AMN64003_NODE_NAME, AMN64001_NODE_NAME, Arrays.asList(LINK_ID_TL6));
		
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		map.put(flowId, vPathId2);
		obj.setPathIdMap(map);
		
		FlowDto reqDto = getUpdateReqDto();
		
		try {
			obj.modFlow(modFlowDto, reqDto, restifRequestDto);
			
			// OdenOS メソッド呼び出し確認
			SampleOdenOsAdp resultSampleAdp = (SampleOdenOsAdp) obj.getEquipmentConfiguratorOptDeviceImpl().getOdenOSAdapterService(); 
			assertTrue(resultSampleAdp.resultGetLinkId == true);
			assertTrue(resultSampleAdp.resultRequestLink == false);
			assertTrue(resultSampleAdp.resultDeleteFlow == false);
			assertTrue(resultSampleAdp.resultPutFlow == true);
			
			// Sdtnc メソッド呼び出し確認
			SampleSdtncService resultSmpleSdt = (SampleSdtncService)obj.getSdtncService();
			assertTrue(resultSmpleSdt.callLogin == true);
			assertTrue(resultSmpleSdt.callLogout == true);
			assertTrue(resultSmpleSdt.callGetLspResource == true);
			assertTrue(resultSmpleSdt.callCreatePw == true);
			assertTrue(resultSmpleSdt.callDeleatPw == true);
			assertTrue(resultSmpleSdt.callGetPw == false);

			// getLink 引数確認
			assertEquals(resultSampleAdp.resultLinkIdList.size(), 2);
			assertTrue(resultSampleAdp.resultLinkIdList.get(0).equals(linkEntityTl6.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.get(1).equals(linkEntityTl3.linkId));
			
			// putFlow 引数確認
			assertEquals(resultSampleAdp.resultPutFlowEntity.size(), 2);
			compareFlowEntity(resultSampleAdp.resultPutFlowEntity.get(0), modFlowDto.name + "_1", flowEntityFlow4);
			compareFlowEntity(resultSampleAdp.resultPutFlowEntity.get(1), modFlowDto.name + "_2", flowEntityFlow3);
			assertTrue(obj.getEquipmentConfiguratorOptDeviceImpl().isFlowId(flowId) == true);
			
			// sdtncService.login 引数確認
			assertEquals(resultSmpleSdt.loginDto.login.loginId, LOGIN_ID);
			assertEquals(resultSmpleSdt.loginDto.login.loginPassword, LOGIN_PASS);
			assertEquals(resultSmpleSdt.loginDto.login.ipAddress, LOGIN_IP_ADDRESS);

			// sdtncService.getLspResource 引数確認
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_SLICE_ID),SLICE_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_VLINK_ID),CUT_LINK_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_TOKEN),"token");
						
			// sdtncService.deletePw 引数確認
			assertEquals(resultSmpleSdt.delPwMap.get(PARAM_KEY_SLICE_ID), SLICE_ID);
			assertEquals(resultSmpleSdt.delPwMap.get(PARAM_KEY_VPATH_ID), vPathId2);
			assertEquals(resultSmpleSdt.delPwMap.get(PARAM_KEY_TOKEN), "token");
			
			// sdtncService.createPw 引数確認
			assertEquals(resultSmpleSdt.addPwDto.slice.groupIndex,SLICE_ID);
			assertEquals(resultSmpleSdt.addPwDto.auth.token,"token");
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).vlan.uni.vVlanVLanIdNniClient == VLANID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).vObjectName, modFlowDto.id + "_" + modFlowDto.name);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(0).neId, NE_ID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(0).portId, CUT_PORT_ID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(1).neId, NE_ID_Z);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(1).portId, CUT_PORT_ID_Z);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathRoute.get(0).line.get(0).elementIndex, CUT_LINK_ID);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSSla == DEF_SLA_MODE);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSPir == 0);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSCir == QOS_CIR);

			// sdtncService.logout 引数確認
			assertEquals(resultSmpleSdt.logoutDto.login.loginId,LOGIN_ID);
			
			// pathId登録確認
			assertEquals(obj.getPathIdMap().get(flowId), vPathId);
			
		} catch (MloException e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl#modFlow()}.
	 * P3→P2→P1(リンク設定済) 古いフロー情報あり
	 */
	@Test
	public void testModFlow9() {
		SampleOdenOsAdp sampleAdp = new SampleOdenOsAdp();
		sampleAdp.getLinkFlag = true;
		obj.getEquipmentConfiguratorOptDeviceImpl().setOdenOSAdapterService(sampleAdp);
		
		String odenOsFlowId1 = "odenOS_1";
		String odenOsFlowId2 = "odenOS_2";
		int flowId = 17;
		FlowDto delFlowDto = new FlowDto();
		delFlowDto.id = flowId;
		obj.getEquipmentConfiguratorOptDeviceImpl().addFlowId(flowId, odenOsFlowId1, odenOsFlowId2);
		
		FlowDto modFlowDto = createFlowDto(flowId, AMN64003_NODE_NAME, AMN64001_NODE_NAME, Arrays.asList(LINK_ID_TL5, LINK_ID_TL4));
		
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		map.put(flowId, vPathId2);
		obj.setPathIdMap(map);
		
		FlowDto reqDto = getUpdateReqDto();
		
		try {
			obj.modFlow(modFlowDto, reqDto, restifRequestDto);
			
			// OdenOS メソッド呼び出し確認
			SampleOdenOsAdp resultSampleAdp = (SampleOdenOsAdp) obj.getEquipmentConfiguratorOptDeviceImpl().getOdenOSAdapterService(); 
			assertTrue(resultSampleAdp.resultGetLinkId == true);
			assertTrue(resultSampleAdp.resultRequestLink == false);
			assertTrue(resultSampleAdp.resultDeleteFlow == true);
			assertTrue(resultSampleAdp.resultPutFlow == true);
			
			// Sdtnc メソッド呼び出し確認
			SampleSdtncService resultSmpleSdt = (SampleSdtncService)obj.getSdtncService();
			assertTrue(resultSmpleSdt.callLogin == true);
			assertTrue(resultSmpleSdt.callLogout == true);
			assertTrue(resultSmpleSdt.callGetLspResource == true);
			assertTrue(resultSmpleSdt.callCreatePw == true);
			assertTrue(resultSmpleSdt.callDeleatPw == true);
			assertTrue(resultSmpleSdt.callGetPw == false);
			
			// deleteFlow 引数確認
			assertEquals(2, resultSampleAdp.resultDeleteFlowId.size()); // deleteFlow が呼ばれていること
			
			// requestLink 引数確認
			assertEquals(resultSampleAdp.resultLinkIdList.size(), 4);
			assertTrue(resultSampleAdp.resultLinkIdList.get(0).equals(linkEntityTl5.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.get(1).equals(linkEntityTl4.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.get(2).equals(linkEntityTl1.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.get(3).equals(linkEntityTl2.linkId));
			
			// putFlow 引数確認
			assertEquals(resultSampleAdp.resultPutFlowEntity.size(), 2);
			compareFlowEntity(resultSampleAdp.resultPutFlowEntity.get(0), modFlowDto.name + "_1", flowEntityFlow2);
			compareFlowEntity(resultSampleAdp.resultPutFlowEntity.get(1), modFlowDto.name + "_2", flowEntityFlow1);
			assertTrue(obj.getEquipmentConfiguratorOptDeviceImpl().isFlowId(flowId) == true);
			
			// sdtncService.login 引数確認
			assertEquals(resultSmpleSdt.loginDto.login.loginId, LOGIN_ID);
			assertEquals(resultSmpleSdt.loginDto.login.loginPassword, LOGIN_PASS);
			assertEquals(resultSmpleSdt.loginDto.login.ipAddress, LOGIN_IP_ADDRESS);

			// sdtncService.getLspResource 引数確認
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_SLICE_ID),SLICE_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_VLINK_ID),HOP_LINK_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_TOKEN),"token");
						
			// sdtncService.deletePw 引数確認
			assertEquals(resultSmpleSdt.delPwMap.get(PARAM_KEY_SLICE_ID), SLICE_ID);
			assertEquals(resultSmpleSdt.delPwMap.get(PARAM_KEY_VPATH_ID), vPathId2);
			assertEquals(resultSmpleSdt.delPwMap.get(PARAM_KEY_TOKEN), "token");
			
			// sdtncService.createPw 引数確認
			assertEquals(resultSmpleSdt.addPwDto.slice.groupIndex,SLICE_ID);
			assertEquals(resultSmpleSdt.addPwDto.auth.token,"token");
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).vlan.uni.vVlanVLanIdNniClient == VLANID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).vObjectName, modFlowDto.id + "_" + modFlowDto.name);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(0).neId, NE_ID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(0).portId, HOP_PORT_ID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(1).neId, NE_ID_Z);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(1).portId, HOP_PORT_ID_Z);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathRoute.get(0).line.get(0).elementIndex, HOP_LINK_ID);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSSla == DEF_SLA_MODE);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSPir == 0);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSCir == QOS_CIR);

			// sdtncService.logout 引数確認
			assertEquals(resultSmpleSdt.logoutDto.login.loginId,LOGIN_ID);
			
			// pathId登録確認
			assertEquals(obj.getPathIdMap().get(flowId), vPathId);
			
		} catch (MloException e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl#modFlow()}.
	 * P3→P1 PW削除成功後、PW作成に失敗⇒再実行で成功
	 */
	@Test
	public void testModFlow11() {
		SampleOdenOsAdp sampleAdp = new SampleOdenOsAdp();
		sampleAdp.getLinkFlag = true;
		obj.getEquipmentConfiguratorOptDeviceImpl().setOdenOSAdapterService(sampleAdp);
		
		int flowId = 16;
		FlowDto modFlowDto = createFlowDto(flowId, AMN64003_NODE_NAME, AMN64001_NODE_NAME, Arrays.asList(LINK_ID_TL6));
		
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		map.put(flowId, vPathId2);
		obj.setPathIdMap(map);
		
		SampleSdtncService sampleSdtnc = new SampleSdtncService();
		sampleSdtnc.createPwFailFlag = true;									// PW作成時にエラーを発生させる
		obj.setSdtncService(sampleSdtnc);
		
		FlowDto reqDto = getUpdateReqDto();
		
		try{
			obj.modFlow(modFlowDto, reqDto, restifRequestDto);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof InternalException);
			assertEquals(e.getMessage(), "createPw error");
			// pathId登録確認
			assertNull(obj.getPathIdMap().get(flowId));
		}
		
		//再実行
		SampleOdenOsAdp sampleAdp2 = new SampleOdenOsAdp();
		sampleAdp2.getLinkFlag = true;
		obj.getEquipmentConfiguratorOptDeviceImpl().setOdenOSAdapterService(sampleAdp2);
		SampleSdtncService sampleSdtnc2 = new SampleSdtncService();
		sampleSdtnc2.createPwFailFlag = false;									// PW作成時にエラーを発生させる
		obj.setSdtncService(sampleSdtnc2);
		try {
			obj.modFlow(modFlowDto, reqDto, restifRequestDto);
			
			// OdenOS メソッド呼び出し確認
			SampleOdenOsAdp resultSampleAdp = (SampleOdenOsAdp) obj.getEquipmentConfiguratorOptDeviceImpl().getOdenOSAdapterService(); 
			assertTrue(resultSampleAdp.resultGetLinkId == true);
			assertTrue(resultSampleAdp.resultRequestLink == false);
			assertTrue(resultSampleAdp.resultDeleteFlow == false);
			assertTrue(resultSampleAdp.resultPutFlow == true);
			
			// Sdtnc メソッド呼び出し確認
			SampleSdtncService resultSmpleSdt = (SampleSdtncService)obj.getSdtncService();
			assertTrue(resultSmpleSdt.callLogin == true);
			assertTrue(resultSmpleSdt.callLogout == true);
			assertTrue(resultSmpleSdt.callGetLspResource == true);
			assertTrue(resultSmpleSdt.callCreatePw == true);
			assertTrue(resultSmpleSdt.callDeleatPw == false);
			assertTrue(resultSmpleSdt.callGetPw == false);

			// getLink 引数確認
			assertEquals(resultSampleAdp.resultLinkIdList.size(), 2);
			assertTrue(resultSampleAdp.resultLinkIdList.get(0).equals(linkEntityTl6.linkId));
			assertTrue(resultSampleAdp.resultLinkIdList.get(1).equals(linkEntityTl3.linkId));
			
			// putFlow 引数確認
			assertEquals(resultSampleAdp.resultPutFlowEntity.size(), 2);
			compareFlowEntity(resultSampleAdp.resultPutFlowEntity.get(0), modFlowDto.name + "_1", flowEntityFlow4);
			compareFlowEntity(resultSampleAdp.resultPutFlowEntity.get(1), modFlowDto.name + "_2", flowEntityFlow3);
			assertTrue(obj.getEquipmentConfiguratorOptDeviceImpl().isFlowId(flowId) == true);
			
			// sdtncService.login 引数確認
			assertEquals(resultSmpleSdt.loginDto.login.loginId, LOGIN_ID);
			assertEquals(resultSmpleSdt.loginDto.login.loginPassword, LOGIN_PASS);
			assertEquals(resultSmpleSdt.loginDto.login.ipAddress, LOGIN_IP_ADDRESS);

			// sdtncService.getLspResource 引数確認
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_SLICE_ID),SLICE_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_VLINK_ID),CUT_LINK_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_TOKEN),"token");
						
			// sdtncService.createPw 引数確認
			assertEquals(resultSmpleSdt.addPwDto.slice.groupIndex,SLICE_ID);
			assertEquals(resultSmpleSdt.addPwDto.auth.token,"token");
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).vlan.uni.vVlanVLanIdNniClient == VLANID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).vObjectName, modFlowDto.id + "_" + modFlowDto.name);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(0).neId, NE_ID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(0).portId, CUT_PORT_ID_A);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(1).neId, NE_ID_Z);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathEndPoint.get(1).portId, CUT_PORT_ID_Z);
			assertEquals(resultSmpleSdt.addPwDto.vpath.get(0).pathRoute.get(0).line.get(0).elementIndex, CUT_LINK_ID);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSSla == DEF_SLA_MODE);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSPir == 0);
			assertTrue(resultSmpleSdt.addPwDto.vpath.get(0).qos.vQoSCir == QOS_CIR);

			// sdtncService.logout 引数確認
			assertEquals(resultSmpleSdt.logoutDto.login.loginId,LOGIN_ID);
			
			// pathId登録確認
			assertEquals(obj.getPathIdMap().get(flowId), vPathId);
			
		} catch (MloException e) {
			fail();
		}
	}
	
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl#modFlow()}.
	 * LSPリソース取得：失敗 ログアウト：成功
	 */
	@Test
	public void testModFlowError1() {
		
		int flowId = 4;
		FlowDto modFlowDto = createFlowDto(flowId, AMN64003_NODE_NAME, AMN64001_NODE_NAME, Arrays.asList(LINK_ID_TL6));
		
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		map.put(flowId, vPathId2);
		obj.setPathIdMap(map);

		SampleSdtncService sampleSdtnc = new SampleSdtncService();
		sampleSdtnc.getLspFailFlag = true;										// LSP取得時にエラーを発生させる
		obj.setSdtncService(sampleSdtnc);
		
		FlowDto reqDto = getUpdateReqDto();
		
		try {
			obj.modFlow(modFlowDto, reqDto, restifRequestDto);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof InternalException);
			assertEquals(e.getMessage(), "getLspResource error");
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl#addFlow()}.
	 * LSPリソース取得：失敗 ログアウト：失敗
	 */
	@Test
	public void testModFlowError2() {
		
		int flowId = 4;
		FlowDto modFlowDto = createFlowDto(flowId, AMN64003_NODE_NAME, AMN64001_NODE_NAME, Arrays.asList(LINK_ID_TL6));
		
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		map.put(flowId, vPathId2);
		obj.setPathIdMap(map);

		SampleSdtncService sampleSdtnc = new SampleSdtncService();
		sampleSdtnc.getLspFailFlag = true;										// LSP取得時にエラーを発生させる
		sampleSdtnc.logoutFailFlag = true;										// ログアウト時にエラーを発生させる
		obj.setSdtncService(sampleSdtnc);
		
		FlowDto reqDto = getUpdateReqDto();
		
		try {
			obj.modFlow(modFlowDto, reqDto, restifRequestDto);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof InternalException);
			assertEquals(e.getMessage(), "getLspResource error");
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl#addFlow()}.
	 * ログイン：失敗
	 */
	@Test
	public void testModFlowError3() {
		
		int flowId = 4;
		FlowDto modFlowDto = createFlowDto(flowId, AMN64003_NODE_NAME, AMN64001_NODE_NAME, Arrays.asList(LINK_ID_TL6));
		
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		map.put(flowId, vPathId2);
		obj.setPathIdMap(map);

		SampleSdtncService sampleSdtnc = new SampleSdtncService();
		sampleSdtnc.loginFailFlag = true;										// ログイン時にエラーを発生させる
		obj.setSdtncService(sampleSdtnc);
		
		FlowDto reqDto = getUpdateReqDto();
		
		try {
			obj.modFlow(modFlowDto, reqDto, restifRequestDto);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof ApiCallException);
			assertEquals(e.getMessage(), "login error");
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl#modFlow()}.
	 * VlanIDエラー1
	 */
	@Test
	public void testModFlowError4() {
		
		int flowId = 1;
		FlowDto modFlowDto = createFlowDto(flowId, AMN64001_NODE_NAME, AMN64003_NODE_NAME, Arrays.asList(LINK_ID_TL3));
		
		FlowDto reqDto = getUpdateReqDto();
		reqDto.srcCENodeName = "tokyo";
		reqDto.srcCEPortNo = "00000011";
		reqDto.dstCENodeName = "osaka";
		reqDto.dstCEPortNo = "00000004";
		
		try {
			obj.modFlow(modFlowDto, reqDto, restifRequestDto);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof ApiCallException);
			assertEquals(e.getMessage(), "vlanId is null [srcCENodeName=" + reqDto.srcCENodeName + ", srcCEPortNo=" + reqDto.srcCEPortNo +"]");
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl#modFlow()}.
	 * VlanIDエラー2
	 */
	@Test
	public void testModFlowError5() {
		
		int flowId = 1;
		FlowDto modFlowDto = createFlowDto(flowId, AMN64001_NODE_NAME, AMN64003_NODE_NAME, Arrays.asList(LINK_ID_TL3));
		
		FlowDto reqDto = getUpdateReqDto();
		reqDto.srcCENodeName = "tokyo";
		reqDto.srcCEPortNo = "00000002";
		reqDto.dstCENodeName = "avb";
		reqDto.dstCEPortNo = "00000005";
		
		try {
			obj.modFlow(modFlowDto, reqDto, restifRequestDto);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof ApiCallException);
			assertEquals(e.getMessage(), "vlanId is null [dstCENodeName=" + reqDto.dstCENodeName + ", dstCEPortNo=" + reqDto.dstCEPortNo +"]");
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl#modFlow()}.
	 * VlanIDエラー3
	 */
	@Test
	public void testModFlowError6() {
		
		int flowId = 1;
		FlowDto modFlowDto = createFlowDto(flowId, AMN64001_NODE_NAME, AMN64003_NODE_NAME, Arrays.asList(LINK_ID_TL3));
		
		FlowDto reqDto = getUpdateReqDto();
		reqDto.srcCENodeName = "akashi";
		reqDto.srcCEPortNo = "00000005";
		reqDto.dstCENodeName = "osaka";
		reqDto.dstCEPortNo = "00000004";
		
		try {
			obj.modFlow(modFlowDto, reqDto, restifRequestDto);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof ApiCallException);
			assertEquals(e.getMessage(), "vlanId is different  [srcCENodeName=" + reqDto.srcCENodeName + 
					", srcCEPortNo=" + reqDto.srcCEPortNo + 
					", dstCENodeName=" + reqDto.dstCENodeName + 
					", dstCEPortNo=" + reqDto.dstCEPortNo +"]");
		}
	}

	private void compareLinkEntity(PTLinkEntity link1, PTLinkEntity link2) {
		assertTrue(link1.dstNode.equals(link2.dstNode));
		assertTrue(link1.dstPort.equals(link2.dstPort));
		assertTrue(link1.establishmentStatus.equals(link2.establishmentStatus));
		assertTrue(link1.linkId.equals(link2.linkId));
		assertTrue(link1.operStatus.equals(link2.operStatus));
		assertTrue(link1.reqBandwidth.equals(link2.reqBandwidth));
		assertTrue(link1.reqLatency.equals(link2.reqLatency));
		assertTrue(link1.srcNode.equals(link2.srcNode));
		assertTrue(link1.srcPort.equals(link2.srcPort));
	}
	
	private void compareFlowEntity(PTFlowEntity flow1, String flowId, PTFlowEntity flow2) {
		assertEquals(flow1.flowId, flowId);
		assertTrue(flow1.flowType.equals(flow2.flowType));
		assertTrue(flow1.basicFlowMatchInNode.equals(flow2.basicFlowMatchInNode));
		assertTrue(flow1.basicFlowMatchInPort.equals(flow2.basicFlowMatchInPort));
		assertEquals(flow1.flowPath.size(), flow2.flowPath.size());
		for (int i = 0; i < flow1.flowPath.size(); i++) {
			assertTrue(flow1.flowPath.get(i).equals(flow2.flowPath.get(i)));
		}
		assertTrue(flow1.basicFlowActionOutputNode.equals(flow2.basicFlowActionOutputNode));
		assertTrue(flow1.basicFlowActionOutputPort.equals(flow2.basicFlowActionOutputPort));
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl#modFlow()}.
	 * P1→P2→P3(リンク未設定) LSP情報取得失敗
	 */
	@Test
	public void testModFlow10() {
		SampleSdtncService sampleSdtnc = new SampleSdtncService();
		sampleSdtnc.successGetLspResource = false;								// LSP取得を失敗するように設定
		obj.setSdtncService(sampleSdtnc);
		
		int flowId = 100;
		FlowDto modFlowDto = createFlowDto(flowId, AMN64001_NODE_NAME, AMN64003_NODE_NAME, Arrays.asList(LINK_ID_TL1, LINK_ID_TL2));
		
		FlowDto reqDto = getUpdateReqDto();
		
		try {
			obj.modFlow(modFlowDto, reqDto, restifRequestDto);
			
			// OdenOS メソッド呼び出し確認
			SampleOdenOsAdp resultSampleAdp = (SampleOdenOsAdp) obj.getEquipmentConfiguratorOptDeviceImpl().getOdenOSAdapterService(); 
			assertTrue(resultSampleAdp.resultGetLinkId == false);
			assertTrue(resultSampleAdp.resultRequestLink == false);
			assertTrue(resultSampleAdp.resultDeleteFlow == false);
			
			// Sdtnc メソッド呼び出し確認
			SampleSdtncService resultSmpleSdt = (SampleSdtncService)obj.getSdtncService();
			assertTrue(resultSmpleSdt.callLogin == true);
			assertTrue(resultSmpleSdt.callLogout == true);
			assertTrue(resultSmpleSdt.callGetLspResource == true);
			assertTrue(resultSmpleSdt.callCreatePw == false);
			assertTrue(resultSmpleSdt.callDeleatPw == false);
			assertTrue(resultSmpleSdt.callGetPw == false);
			
			// sdtncService.login 引数確認
			assertEquals(resultSmpleSdt.loginDto.login.loginId, LOGIN_ID);
			assertEquals(resultSmpleSdt.loginDto.login.loginPassword, LOGIN_PASS);
			assertEquals(resultSmpleSdt.loginDto.login.ipAddress, LOGIN_IP_ADDRESS);

			// sdtncService.getLspResource 引数確認
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_SLICE_ID),SLICE_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_VLINK_ID),HOP_LINK_ID);
			assertEquals(resultSmpleSdt.getLspMap.get(PARAM_KEY_TOKEN),"token");

			// sdtncService.logout 引数確認
			assertEquals(resultSmpleSdt.logoutDto.login.loginId,LOGIN_ID);
			
			
		} catch (MloException e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl#delFlow()}.
	 * 未削除の状態
	 */
	@Test
	public void testDelFlow1() {

		String odenOsFlowId1 = "odenOS_1";
		String odenOsFlowId2 = "odenOS_2";
		int flowId = 18;
		FlowDto delFlowDto = new FlowDto();
		delFlowDto.id = flowId;
		obj.getEquipmentConfiguratorOptDeviceImpl().addFlowId(flowId, odenOsFlowId1, odenOsFlowId2);
		
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		map.put(flowId, vPathId2);
		obj.setPathIdMap(map);
		
		try {
			obj.delFlow(delFlowDto, restifRequestDto);

			// OdenOS メソッド呼び出し確認
			SampleOdenOsAdp resultSampleAdp = (SampleOdenOsAdp) obj.getEquipmentConfiguratorOptDeviceImpl().getOdenOSAdapterService(); 
			assertTrue(resultSampleAdp.resultGetLinkId == false);
			assertTrue(resultSampleAdp.resultDeleteFlow == true);
			assertTrue(resultSampleAdp.resultRequestLink == false);
			
			// Sdtnc メソッド呼び出し確認
			SampleSdtncService resultSmpleSdt = (SampleSdtncService)obj.getSdtncService();
			assertTrue(resultSmpleSdt.callLogin == true);
			assertTrue(resultSmpleSdt.callLogout == true);
			assertTrue(resultSmpleSdt.callGetLspResource == false);
			assertTrue(resultSmpleSdt.callCreatePw == false);
			assertTrue(resultSmpleSdt.callDeleatPw == true);
			assertTrue(resultSmpleSdt.callGetPw == false);
			
			// deleteFlow 引数確認
			assertEquals(resultSampleAdp.resultDeleteFlowId.size(), 2);
			assertTrue(resultSampleAdp.resultDeleteFlowId.get(0).equals(odenOsFlowId1));
			assertTrue(resultSampleAdp.resultDeleteFlowId.get(1).equals(odenOsFlowId2));
			assertTrue(obj.getEquipmentConfiguratorOptDeviceImpl().isFlowId(flowId) == false);
			
			// sdtncService.login 引数確認
			assertEquals(resultSmpleSdt.loginDto.login.loginId, LOGIN_ID);
			assertEquals(resultSmpleSdt.loginDto.login.loginPassword, LOGIN_PASS);
			assertEquals(resultSmpleSdt.loginDto.login.ipAddress, LOGIN_IP_ADDRESS);

			// sdtncService.deletePw 引数確認
			assertEquals(resultSmpleSdt.delPwMap.get(PARAM_KEY_SLICE_ID), SLICE_ID);
			assertEquals(resultSmpleSdt.delPwMap.get(PARAM_KEY_VPATH_ID), vPathId2);
			assertEquals(resultSmpleSdt.delPwMap.get(PARAM_KEY_TOKEN), "token");

			// sdtncService.logout 引数確認
			assertEquals(resultSmpleSdt.logoutDto.login.loginId,LOGIN_ID);
			
			// pathId登録確認
			assertNull(obj.getPathIdMap().get(flowId));
			
		} catch (MloException e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl#delFlow()}.
	 * 削除済
	 */
	@Test
	public void testDelFlow2() {
		
		int flowId = 19;
		FlowDto delFlowDto = new FlowDto();
		delFlowDto.id = flowId;
		
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		map.put(flowId, vPathId2);
		obj.setPathIdMap(map);
		
		try {
			obj.delFlow(delFlowDto, restifRequestDto);
			
			SampleOdenOsAdp resultSampleAdp = (SampleOdenOsAdp) obj.getEquipmentConfiguratorOptDeviceImpl().getOdenOSAdapterService(); 
			assertTrue(resultSampleAdp.resultGetLinkId == false);
			assertTrue(resultSampleAdp.resultDeleteFlow == false);
			assertTrue(resultSampleAdp.resultRequestLink == false);
			assertTrue(obj.getEquipmentConfiguratorOptDeviceImpl().isFlowId(flowId) == false);
			
			// Sdtnc メソッド呼び出し確認
			SampleSdtncService resultSmpleSdt = (SampleSdtncService)obj.getSdtncService();
			assertTrue(resultSmpleSdt.callLogin == true);
			assertTrue(resultSmpleSdt.callLogout == true);
			assertTrue(resultSmpleSdt.callGetLspResource == false);
			assertTrue(resultSmpleSdt.callCreatePw == false);
			assertTrue(resultSmpleSdt.callDeleatPw == true);
			assertTrue(resultSmpleSdt.callGetPw == false);
			
			// sdtncService.login 引数確認
			assertEquals(resultSmpleSdt.loginDto.login.loginId, LOGIN_ID);
			assertEquals(resultSmpleSdt.loginDto.login.loginPassword, LOGIN_PASS);
			assertEquals(resultSmpleSdt.loginDto.login.ipAddress, LOGIN_IP_ADDRESS);

			// sdtncService.deletePw 引数確認
			assertEquals(resultSmpleSdt.delPwMap.get(PARAM_KEY_SLICE_ID), SLICE_ID);
			assertEquals(resultSmpleSdt.delPwMap.get(PARAM_KEY_VPATH_ID), vPathId2);
			assertEquals(resultSmpleSdt.delPwMap.get(PARAM_KEY_TOKEN), "token");

			// sdtncService.logout 引数確認
			assertEquals(resultSmpleSdt.logoutDto.login.loginId,LOGIN_ID);
			
			// pathId登録確認
			assertNull(obj.getPathIdMap().get(flowId));
			
		} catch (MloException e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl#delFlow()}.
	 * PW削除済みのスキップ確認
	 */
	@Test
	public void testDelFlow3() {

		String odenOsFlowId1 = "odenOS_1";
		String odenOsFlowId2 = "odenOS_2";
		int flowId = 18;
		FlowDto delFlowDto = new FlowDto();
		delFlowDto.id = flowId;
		obj.getEquipmentConfiguratorOptDeviceImpl().addFlowId(flowId, odenOsFlowId1, odenOsFlowId2);
		
		try {
			obj.delFlow(delFlowDto, restifRequestDto);

			// OdenOS メソッド呼び出し確認
			SampleOdenOsAdp resultSampleAdp = (SampleOdenOsAdp) obj.getEquipmentConfiguratorOptDeviceImpl().getOdenOSAdapterService(); 
			assertTrue(resultSampleAdp.resultGetLinkId == false);
			assertTrue(resultSampleAdp.resultDeleteFlow == true);
			assertTrue(resultSampleAdp.resultRequestLink == false);
			
			// Sdtnc メソッド呼び出し確認
			SampleSdtncService resultSmpleSdt = (SampleSdtncService)obj.getSdtncService();
			assertTrue(resultSmpleSdt.callLogin == true);
			assertTrue(resultSmpleSdt.callLogout == true);
			assertTrue(resultSmpleSdt.callGetLspResource == false);
			assertTrue(resultSmpleSdt.callCreatePw == false);
			assertTrue(resultSmpleSdt.callDeleatPw == false);
			assertTrue(resultSmpleSdt.callGetPw == false);
			
			// deleteFlow 引数確認
			assertEquals(resultSampleAdp.resultDeleteFlowId.size(), 2);
			assertTrue(resultSampleAdp.resultDeleteFlowId.get(0).equals(odenOsFlowId1));
			assertTrue(resultSampleAdp.resultDeleteFlowId.get(1).equals(odenOsFlowId2));
			assertTrue(obj.getEquipmentConfiguratorOptDeviceImpl().isFlowId(flowId) == false);
			
			// sdtncService.login 引数確認
			assertEquals(resultSmpleSdt.loginDto.login.loginId, LOGIN_ID);
			assertEquals(resultSmpleSdt.loginDto.login.loginPassword, LOGIN_PASS);
			assertEquals(resultSmpleSdt.loginDto.login.ipAddress, LOGIN_IP_ADDRESS);

			// sdtncService.logout 引数確認
			assertEquals(resultSmpleSdt.logoutDto.login.loginId,LOGIN_ID);
			
		} catch (MloException e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl#delFlow()}.
	 * LSPリソース取得：失敗 ログアウト：成功
	 */
	@Test
	public void testDelFlowError1() {
		
		int flowId = 4;
		FlowDto delFlowDto = createFlowDto(flowId, AMN64003_NODE_NAME, AMN64001_NODE_NAME, Arrays.asList(LINK_ID_TL6));
		
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		map.put(flowId, vPathId2);
		obj.setPathIdMap(map);

		SampleSdtncService sampleSdtnc = new SampleSdtncService();
		sampleSdtnc.delPwFailFlag = true;										// PW取得時にエラーを発生させる
		obj.setSdtncService(sampleSdtnc);
		
		try {
			obj.delFlow(delFlowDto, restifRequestDto);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof InternalException);
			assertEquals(e.getMessage(), "deletePw error");
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl#delFlow()}.
	 * LSPリソース取得：失敗 ログアウト：失敗
	 */
	@Test
	public void testDelFlowError2() {
		
		int flowId = 4;
		FlowDto delFlowDto = createFlowDto(flowId, AMN64003_NODE_NAME, AMN64001_NODE_NAME, Arrays.asList(LINK_ID_TL6));
		
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		map.put(flowId, vPathId2);
		obj.setPathIdMap(map);

		SampleSdtncService sampleSdtnc = new SampleSdtncService();
		sampleSdtnc.delPwFailFlag = true;										// PW削除時にエラーを発生させる
		sampleSdtnc.logoutFailFlag = true;										// ログアウト時にエラーを発生させる
		obj.setSdtncService(sampleSdtnc);
		
		try {
			obj.delFlow(delFlowDto, restifRequestDto);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof InternalException);
			assertEquals(e.getMessage(), "deletePw error");
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl#delFlow()}.
	 * ログイン：失敗
	 */
	@Test
	public void testDelFlowError3() {
		
		int flowId = 4;
		FlowDto delFlowDto = createFlowDto(flowId, AMN64003_NODE_NAME, AMN64001_NODE_NAME, Arrays.asList(LINK_ID_TL6));

		SampleSdtncService sampleSdtnc = new SampleSdtncService();
		sampleSdtnc.loginFailFlag = true;										// ログイン時にエラーを発生させる
		obj.setSdtncService(sampleSdtnc);
		
		try {
			obj.delFlow(delFlowDto, restifRequestDto);
			fail();
		} catch (MloException e) {
			assertTrue(e instanceof ApiCallException);
			assertEquals(e.getMessage(), "login error");
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.impl.logic.EquipmentConfiguratorOtherImpl#createVPwLabel()}.
	 */
	@Test
	public void testCreateVPwLabel() {
		Integer pwLabelOffset = EquipmentConfiguratorOtherImpl.PW_LABEL_OFFSET;
		assertEquals(Integer.valueOf(0x50), pwLabelOffset);
		
		FlowDto flowDto = new FlowDto();
		flowDto.id = 5;
		assertEquals("000055", obj.createVPwLabel(flowDto));
		flowDto.id = 16;
		assertEquals("000060", obj.createVPwLabel(flowDto));
	}
	
	FlowDto createFlowDto(Integer flowId, String srcPtNodeName, String dstPtNodeName, List<Integer> linkInfoDtoIdList) {
		TopologyRepositoryDefaultImpl topologyRepositoryDefaultImpl = TopologyRepositoryDefaultImplTest.createObj();
		topologyRepositoryDefaultImpl.setTopologyProviderRemoteLdImpl(null);
		topologyRepositoryDefaultImpl.loadData();
		List<FlowDto> flowDtoTemplates = topologyRepositoryDefaultImpl.getFlowDtoTemplates();
		FlowDto newFlowDto = null;
		for (FlowDto flowDtoTemplate : flowDtoTemplates) {
			boolean isMatch = true;
			isMatch &= flowDtoTemplate.srcPTNodeName.equals(srcPtNodeName);
			isMatch &= flowDtoTemplate.dstPTNodeName.equals(dstPtNodeName);
			isMatch &= (flowDtoTemplate.linkInfoList.size() == linkInfoDtoIdList.size());
			for (int idx = 0; isMatch && idx < linkInfoDtoIdList.size(); idx += 1) {
				isMatch &= linkInfoDtoIdList.get(idx).equals(flowDtoTemplate.linkInfoList.get(idx).id);
			}
			if (isMatch) {
				newFlowDto = FlowDtoUtil.copyConcreteFlow(flowDtoTemplate);
				newFlowDto.id = flowId;
				break;
			}
		}
		return newFlowDto;
	}
	
	// 要求フロー情報作成(追加)
	public FlowDto getCreateReqDto(){
		FlowDto flowDto = new FlowDto();
		flowDto.name = "flow1";
		flowDto.srcCENodeName = "tokyo";
		flowDto.srcCEPortNo = "00000001";
		flowDto.dstCENodeName = "osaka";
		flowDto.dstCEPortNo = "00000004";
		flowDto.reqBandWidth = 100;
		flowDto.reqDelay = 8;
		flowDto.protectionLevel = "0";
		return flowDto;
	}
	
	// 要求フロー情報作成(変更)
	public FlowDto getUpdateReqDto(){
		FlowDto flowDto = new FlowDto();
		flowDto.type = "mod";
		flowDto.name = "flow2";
		flowDto.srcCENodeName = "tokyo";
		flowDto.srcCEPortNo = "00000001";
		flowDto.dstCENodeName = "osaka";
		flowDto.dstCEPortNo = "00000004";
		flowDto.reqBandWidth = 100;
		flowDto.reqDelay = 8;
		flowDto.protectionLevel = "0";
		return flowDto;
	}
	
	static PTFlowEntity createPTFlowEntity(Object flowName, Integer reqBandWidth, Integer reqDelay, ConfigProvider configProvider) {
		OdenOSTopology odenOSTopology = new OdenOSTopology();
		return odenOSTopology.createPTFlowEntity(flowName, reqBandWidth, reqDelay, configProvider);
	}
	
	static PTLinkEntity createPTLinkEntity(Object linkName, Integer bandWidth, Integer delay, ConfigProvider configProvider) {
		OdenOSTopology odenOSTopology = new OdenOSTopology();
		return odenOSTopology.createPTLinkEntity(linkName, bandWidth, delay, configProvider);
	}
	
	// JUnit用のOdenOSAdapterServiceImpl
	public class SampleOdenOsAdp implements OdenOSAdapterService{
		
		public boolean resultGetLinkId;
		public boolean resultRequestLink;
		public boolean resultDeleteFlow;
		public boolean resultPutFlow;
		public List<String> resultDeleteFlowId;
		public List<PTLinkEntity> resultPtLinkList;
		public PTFlowEntity resultPtFlowEntity;
		public List<PTFlowEntity> resultPutFlowEntity;
		public boolean getLinkFlag;
		public List<String> resultLinkIdList;
		
		SampleOdenOsAdp(){
			resultGetLinkId = false;
			resultRequestLink = false;
			resultPutFlow = false;
			resultPtLinkList = new ArrayList<PTLinkEntity>();
			resultPtFlowEntity = null;
			resultPutFlowEntity = new ArrayList<PTFlowEntity>();
			getLinkFlag = false;
			resultDeleteFlow = false;
			resultLinkIdList = new ArrayList<String>();
			resultDeleteFlowId = new ArrayList<String>();
		}

		@Override
		public void init() {
			
		}

		@Override
		public void dispose() {
			
		}

		@Override
		public PTLinkEntity getLink(String odenOSLinkId) throws MloException {
			
			resultGetLinkId = true;
			resultLinkIdList.add(odenOSLinkId);
			if(getLinkFlag){
				PTLinkEntity ptLink = null;
				
				if(odenOSLinkId.equals(linkEntityTl1.linkId)){
					ptLink = linkEntityTl1;
				}else if(odenOSLinkId.equals(linkEntityTl2.linkId)){
					ptLink = linkEntityTl2;
				}else if(odenOSLinkId.equals(linkEntityTl3.linkId)){
					ptLink = linkEntityTl3;
				}else if(odenOSLinkId.equals(linkEntityTl4.linkId)){
					ptLink = linkEntityTl4;
				}else if(odenOSLinkId.equals(linkEntityTl5.linkId)){
					ptLink = linkEntityTl5;
				}else if(odenOSLinkId.equals(linkEntityTl6.linkId)){
					ptLink = linkEntityTl6;
				}
				return ptLink;
			}else{
				return null;
			}
		}

		@Override
		public void requestLink(PTLinkEntity link) throws MloException {
			resultRequestLink = true;
			resultPtLinkList.add(link);
		}

		@Override
		public void deleteFlow(String odenosFlowId) throws MloException {
			resultDeleteFlow = true;
			resultDeleteFlowId.add(odenosFlowId);
		}

		/* (non-Javadoc)
		 * @see org.o3project.mlo.server.rpc.service.OdenOSAdapterService#putFlow(org.o3project.mlo.server.rpc.entity.PTFlowEntity)
		 */
		@Override
		public String putFlow(PTFlowEntity entity) throws MloException {
			resultPutFlow = true;
			resultPutFlowEntity.add(entity);
			
			return entity.flowId;
		}
		
	} 
	
	// JUnit用のSdtncServiceImpe
	public class SampleSdtncService implements SdtncService{

		public boolean callLogin = false;
		public boolean callLogout = false;
		public boolean callGetLspResource = false;
		public boolean callCreatePw = false;
		public boolean callDeleatPw = false;
		public boolean callGetPw = false;
		
		public boolean successGetLspResource = true;
		
		public SdtncReqPostLoginDto loginDto;
		public SdtncReqPostLogoutDto logoutDto;
		public SdtncReqPostCreatePwDto addPwDto;
		public Map<String, String> getLspMap;
		public Map<String, String> getPwMap;
		public Map<String, String> delPwMap;
		
		public boolean logoutFailFlag = false;
		public boolean loginFailFlag = false;
		public boolean getLspFailFlag = false;
		public boolean delPwFailFlag = false;
		public boolean createPwFailFlag = false;
		
		@Override
		public SdtncResponseDto login(SdtncRequestDto request) throws MloException {
			callLogin = true;
			loginDto = (SdtncReqPostLoginDto) request;
			SdtncResponseDto resDto = new SdtncResponseDto();
			SdtncAuthDto authDto = new SdtncAuthDto();
			authDto.token = "token";
			resDto.auth = authDto;
			if(loginFailFlag){
				throw new ApiCallException("login error");
			}
			return resDto;
		}

		@Override
		public SdtncResponseDto logout(SdtncRequestDto request) throws MloException {
			callLogout = true;
			logoutDto = (SdtncReqPostLogoutDto) request;
			SdtncResponseDto resDto = new SdtncResponseDto();
			if(logoutFailFlag){
				throw new DbAccessException("logout error");
			}
			return resDto;
		}

		@Override
		public SdtncResponseDto getLspResource(Map<String, String> paramMap) throws MloException {
			callGetLspResource = true;
			getLspMap = paramMap;
			SdtncResponseDto resDto = new SdtncResponseDto();
			if(successGetLspResource){
				SdtncVlinkDto vlinkDto = new SdtncVlinkDto();
				List<SdtncVlinkDto> vlinkList= new ArrayList<SdtncVlinkDto>();
				vlinkList.add(vlinkDto);
				resDto.vlink = vlinkList;
			}
			
			if(getLspFailFlag){
				throw new InternalException("getLspResource error");
			}
			
			return resDto;
		}

		@Override
		public SdtncResponseDto createPw(SdtncRequestDto request) throws MloException {
			callCreatePw = true;
			if(createPwFailFlag){
				throw new InternalException("createPw error");
			}
			addPwDto = (SdtncReqPostCreatePwDto) request;
			SdtncResponseDto resDto = new SdtncResponseDto();
			SdtncVpathDto vpathDto = new SdtncVpathDto();
			List<SdtncVpathDto> vpathList= new ArrayList<SdtncVpathDto>();
			vpathDto.vObjectIndex = vPathId;
			vpathList.add(vpathDto);
			resDto.vpath = vpathList;
			return resDto;
		}

		@Override
		public SdtncResponseDto deletePw(Map<String, String> paramMap) throws MloException {
			callDeleatPw = true;
			delPwMap = paramMap;
			SdtncResponseDto resDto = new SdtncResponseDto();
			if(delPwFailFlag){
				throw new InternalException("deletePw error");
			}
			return resDto;
		}

		@Override
		public SdtncResponseDto getPw(Map<String, String> paramMap) throws MloException {
			callGetPw = true;
			getPwMap = paramMap;
			SdtncResponseDto resDto = new SdtncResponseDto();
			return resDto;
		}
		
	}
}
