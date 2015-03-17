/**
 * EquipmentConfiguratorOtherImpl.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import java.util.HashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.o3project.mlo.server.dto.FlowDto;
import org.o3project.mlo.server.dto.RestifRequestDto;
import org.o3project.mlo.server.impl.rpc.service.SdtncDtoUtil;
import org.o3project.mlo.server.logic.ApiCallException;
import org.o3project.mlo.server.logic.ConfigConstants;
import org.o3project.mlo.server.logic.EquipmentConfigurator;
import org.o3project.mlo.server.logic.MloException;
import org.o3project.mlo.server.logic.TopologyRepository;
import org.o3project.mlo.server.rpc.dto.SdtncRequestDto;
import org.o3project.mlo.server.rpc.dto.SdtncResponseDto;
import org.o3project.mlo.server.rpc.service.SdtncConstants;
import org.o3project.mlo.server.rpc.service.SdtncDtoConfig;
import org.o3project.mlo.server.rpc.service.SdtncService;
import org.seasar.framework.container.annotation.tiger.Aspect;
import org.seasar.framework.container.annotation.tiger.Binding;


/**
 *  This class is the sample implementation class of {@link EquipmentConfigurator} interface.
 */
@Aspect("traceInterceptor")
public class EquipmentConfiguratorOtherImpl implements EquipmentConfigurator, SdtncConstants, ConfigConstants {
	
	/** Logger **/
	private static final Log LOG = LogFactory.getLog(EquipmentConfiguratorOtherImpl.class);
	
	/** Login ID **/
	private static final String LOGIN_ID = "admin";

	/** The offset value of PW label (upstream-downstream common). **/
	static final Integer PW_LABEL_OFFSET = 0x50;
	
	/** The conversion factor of band width. **/
	private static final int CHANGE_BANDWIDTH_TO_QOS = 10;
	
	/** PW path ID map. **/
	private HashMap<Integer, String> pathIdMap = new HashMap<Integer, String>();

	@Binding
	private EquipmentConfiguratorOptDeviceImpl equipmentConfiguratorOptDeviceImpl;
	
	@Binding
	private SdtncService sdtncService;
	
	@Binding
	private SdtncDtoConfig sdtncDtoOtherConfig;
	
	private TopologyRepository topologyRepository;

	/**
	 * Setter method (for DI setter injection).
	 * @param equipmentConfiguratorOptDeviceImpl The instance.
	 */
	public void setEquipmentConfiguratorOptDeviceImpl(EquipmentConfiguratorOptDeviceImpl equipmentConfiguratorOptDeviceImpl) {
		this.equipmentConfiguratorOptDeviceImpl = equipmentConfiguratorOptDeviceImpl;
	}
	
	/**
	 * Setter method (for DI setter injection).
	 * @param sdtncDtoOtherConfig the sdtncDtoOtherConfig to set
	 */
	public void setSdtncDtoOtherConfig(SdtncDtoConfig sdtncDtoOtherConfig) {
		this.sdtncDtoOtherConfig = sdtncDtoOtherConfig;
	}
	
	/**
	 * Setter method (for DI setter injection).
	 * @param topologyRepository the topologyRepository to set
	 */
	public void setTopologyRepository(TopologyRepository topologyRepository) {
		this.topologyRepository = topologyRepository;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.EquipmentConfigurator#addFlow(org.o3project.mlo.server.dto.FlowDto)
	 */
	@Override
	public void addFlow(FlowDto newFlowDto, FlowDto reqFlowDto, RestifRequestDto restifRequestDto) throws MloException {
		
		// Defines vars.
		String loginPassword = sdtncDtoOtherConfig.getLoginPassword();
		String loginIpAddress = sdtncDtoOtherConfig.getLoginIpAddress();
		
		// SDTNC login
		String token = doSdtncPostLogin(LOGIN_ID, loginPassword, loginIpAddress);
		
		try{
			String sliceId = sdtncDtoOtherConfig.getSliceId();
			
			// Obtains SDTNC vlink (LSP))
			String vLinkId = getLinkId(newFlowDto);
			SdtncResponseDto resLspDto = doSdtncGetVLink(token, sliceId, vLinkId);
			
			// If LSP information exists.
			if(resLspDto != null && resLspDto.vlink != null){
				
				// Obtains VLAN ID
				String vlanId = getVlanId(reqFlowDto);
				
				// Request OdenOS link.
				equipmentConfiguratorOptDeviceImpl.requestOdenOSLink(newFlowDto);
				
				// Obtains SDTNC vpath (PW)
				String vPwLabel = createVPwLabel(newFlowDto);
				SdtncResponseDto resAddPwDto = doSdtncPostVPath(newFlowDto, token, sliceId, vPwLabel, vlanId, reqFlowDto);
				pathIdMap.put(newFlowDto.id, resAddPwDto.vpath.get(0).vObjectIndex);

				// Requests to put OdenOS flow.
				equipmentConfiguratorOptDeviceImpl.putOdenOSFlow(newFlowDto, reqFlowDto);
				
				// Overwrites bandwidth and delay.
				newFlowDto.usedBandWidth = reqFlowDto.reqBandWidth;
				newFlowDto.delayTime = reqFlowDto.reqDelay;

//			} else {
//				// Never pass here.
			}
		}finally{
			try{
				// SDTNC logout. 
				doSdtncPostLogout(LOGIN_ID);
			}catch(Exception e){
				// Kills logout failure here.
				LOG.error("logout failed loginId");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.EquipmentConfigurator#delFlow(org.o3project.mlo.server.dto.FlowDto)
	 */
	@Override
	public void delFlow(FlowDto deletedFlowDto, RestifRequestDto restifRequestDto) throws MloException {
		
		String loginPassword = sdtncDtoOtherConfig.getLoginPassword();
		String loginIpAddress = sdtncDtoOtherConfig.getLoginIpAddress();
		
		// SDTNC login..
		String token = doSdtncPostLogin(LOGIN_ID, loginPassword, loginIpAddress);
		
		try{
			String sliceId = sdtncDtoOtherConfig.getSliceId();
			
			// Skips if PW already deleted.
			if(pathIdMap.get(deletedFlowDto.id) != null){
				// Deletes SDTNC vpath (PW).
				String pathId = pathIdMap.get(deletedFlowDto.id);
				doSdtncDeleteVPath(deletedFlowDto, token, sliceId, pathId);
				pathIdMap.remove(deletedFlowDto.id);
			}
		}finally{
			try{
				// SDTNC logout.
				doSdtncPostLogout(LOGIN_ID);
			}catch(Exception e){
				// Kills logout failure here.
				LOG.error("logout failed loginId");
			}
		}
		// Deletes Odenos flow.
		equipmentConfiguratorOptDeviceImpl.delFlow(deletedFlowDto, restifRequestDto);
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.EquipmentConfigurator#modFlow(org.o3project.mlo.server.dto.FlowDto)
	 */
	@Override
	public void modFlow(FlowDto newFlowDto, FlowDto reqFlowDto, RestifRequestDto restifRequestDto) throws MloException {
		
		String loginPassword = sdtncDtoOtherConfig.getLoginPassword();
		String loginIpAddress = sdtncDtoOtherConfig.getLoginIpAddress();
	
		// SDTNC login
		String token = doSdtncPostLogin(LOGIN_ID, loginPassword, loginIpAddress);
		
		try{
			String sliceId = sdtncDtoOtherConfig.getSliceId();
			
			// Obtains SDTNC vlink (LSP) 
			String vLinkId = getLinkId(newFlowDto);
			SdtncResponseDto resLspDto = doSdtncGetVLink(token, sliceId, vLinkId);
			
			// If LSP resource exists.
			if(resLspDto != null && resLspDto.vlink != null){
				
				// Obtains VLAN ID
				String vlanId = getVlanId(reqFlowDto);

				// Requests OdenOS link 
				equipmentConfiguratorOptDeviceImpl.requestOdenOSLink(newFlowDto);

				// Skips if PW already deleted.
				if(pathIdMap.get(newFlowDto.id) != null){
					// Deletes SDTNC vpath (PW)
					String pathId = pathIdMap.get(newFlowDto.id);
					doSdtncDeleteVPath(newFlowDto, token, sliceId, pathId);
					pathIdMap.remove(newFlowDto.id);
				}
				// Deletes OdenOS flow. 
				equipmentConfiguratorOptDeviceImpl.delFlow(newFlowDto, restifRequestDto);
				
				// Puts SDTNC vpath (PW)
				String vPwLabel = createVPwLabel(newFlowDto);
				SdtncResponseDto resAddPwDto = doSdtncPostVPath(newFlowDto, token, sliceId, vPwLabel, vlanId, reqFlowDto);
				pathIdMap.put(newFlowDto.id, resAddPwDto.vpath.get(0).vObjectIndex);
				
				// Puts OdenOS flow.S
				equipmentConfiguratorOptDeviceImpl.putOdenOSFlow(newFlowDto, reqFlowDto);
				
				// Overwrites bandwidth and delayy
				newFlowDto.usedBandWidth = reqFlowDto.reqBandWidth;
				newFlowDto.delayTime = reqFlowDto.reqDelay;
				
//			} else {
//				// Never pass here.
			}
		}finally{
			try{
				// SDTNC  logout.
				doSdtncPostLogout(LOGIN_ID);
			}catch(Exception e){
				// Kills logout failure here.
				LOG.error("logout failed loginId");
			}
		}
	}

	/**
	 * Requests to login to SDTNC.
	 * @param loginId Login ID.
	 * @param loginPassword Login password. 
	 * @param loginIpAddress Login IP address. 
	 * @return Access token. 
	 * @throws MloException Failure in processing. 
	 */
	private String doSdtncPostLogin(String loginId, String loginPassword, String loginIpAddress)
			throws MloException {
		String token = null;
		HashMap<String, String> loginDtoPrmMap = new HashMap<String, String>();
		loginDtoPrmMap.put(SdtncDtoUtil.DTO_PRM_KEY_LOGIN_ID, loginId);
		loginDtoPrmMap.put(SdtncDtoUtil.DTO_PRM_KEY_LOGIN_PASSWORD, loginPassword);
		loginDtoPrmMap.put(SdtncDtoUtil.DTO_PRM_KEY_LOGIN_IP_ADDRESS, loginIpAddress);
		SdtncRequestDto loginDto = SdtncDtoUtil.createLoginReqDto(loginDtoPrmMap);
		SdtncResponseDto resLoginDto = sdtncService.login(loginDto);
		token = resLoginDto.auth.token;
		return token;
	}

	/**
	 * Requests to logout from SDTNC.
	 * @param loginId Login ID.
	 * @throws MloException Failure in processing. 
	 */
	private void doSdtncPostLogout(String loginId) throws MloException {
		HashMap<String, String> logoutDtoPrmMap = new HashMap<String, String>();
		logoutDtoPrmMap.put(SdtncDtoUtil.DTO_PRM_KEY_LOGIN_ID, loginId);
		SdtncRequestDto logoutDto = SdtncDtoUtil.createLogoutReqDto(logoutDtoPrmMap);
		sdtncService.logout(logoutDto);
	}

	/**
	 * Requests to obtain vlink to SDTNC.
	 * @param token Access token. 
	 * @param sliceId Slice ID.
	 * @param vLinkId Vlink ID.
	 * @return vlink object.
	 * @throws MloException Failed in processing.
	 */
	private SdtncResponseDto doSdtncGetVLink(String token, String sliceId, String vLinkId) throws MloException {
		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put(PARAM_KEY_SLICE_ID, sliceId);
		paramMap.put(PARAM_KEY_VLINK_ID, vLinkId);
		paramMap.put(PARAM_KEY_TOKEN, token);
		SdtncResponseDto resLspDto = sdtncService.getLspResource(paramMap);
		return resLspDto;
	}

	/**
	 * Requests to put vpath to SDTNC.
	 * @param newFlowDto New flow DTO.
	 * @param sliceId Slice ID.
	 * @param vPwLabel PW label (upstream-downstream common).
	 * @param vlanId Vlan ID.
	 * @param token Access token. 
	 * @return Vpath object.
	 * @throws MloException Failed in processing. 
	 */
	private SdtncResponseDto doSdtncPostVPath(FlowDto newFlowDto, String token, String sliceId, String vPwLabel, String vlanId, FlowDto reqFlowDto) throws MloException {
		HashMap<String, String> addPwDtoPrmMap = new HashMap<String, String>();
		addPwDtoPrmMap.put(SdtncDtoUtil.DTO_PRM_KEY_SLICEID, sliceId);
		addPwDtoPrmMap.put(SdtncDtoUtil.DTO_PRM_KEY_TOKEN, token);
		addPwDtoPrmMap.put(SdtncDtoUtil.DTO_PRM_KEY_VLANID, vlanId);
		addPwDtoPrmMap.put(SdtncDtoUtil.DTO_PRM_KEY_PATH_NAME, getPathName(newFlowDto));
		addPwDtoPrmMap.put(SdtncDtoUtil.DTO_PRM_KEY_NEID_A, sdtncDtoOtherConfig.getNeIdA());
		addPwDtoPrmMap.put(SdtncDtoUtil.DTO_PRM_KEY_NEID_Z, sdtncDtoOtherConfig.getNeIdZ());
		if (sdtncDtoOtherConfig.isPutVPathWorkaroundMode()) {
			addPwDtoPrmMap.put(SdtncDtoUtil.DTO_PRM_KEY_PORTID_A, 
					sdtncDtoOtherConfig.getVlanProperty(vlanId, PROP_SUBKEY_PATH_END_POINT_A_PORT_ID));
			addPwDtoPrmMap.put(SdtncDtoUtil.DTO_PRM_KEY_PORTID_Z, 
					sdtncDtoOtherConfig.getVlanProperty(vlanId, PROP_SUBKEY_PATH_END_POINT_Z_PORT_ID));
		} else {
			addPwDtoPrmMap.put(SdtncDtoUtil.DTO_PRM_KEY_PORTID_A, getPortIdA(newFlowDto));
			addPwDtoPrmMap.put(SdtncDtoUtil.DTO_PRM_KEY_PORTID_Z, getPortIdZ(newFlowDto));
		}
		addPwDtoPrmMap.put(SdtncDtoUtil.DTO_PRM_KEY_ELEMENT_INDEX, getLinkId(newFlowDto));
		addPwDtoPrmMap.put(SdtncDtoUtil.DTO_PRM_KEY_V_PW_DOWN_LABEL, vPwLabel);
		addPwDtoPrmMap.put(SdtncDtoUtil.DTO_PRM_KEY_V_PW_UP_LABEL, vPwLabel);
		
		// Converts used band width from Mbps to QoS (100kbps)
		int bandWidth = reqFlowDto.reqBandWidth;
		
		int slaMode = Integer.valueOf(sdtncDtoOtherConfig.getSlaMode());
		
		int vQosPir = 0;
		
		// Sets pir value unless slaMode is complete band insure (1).
		if(slaMode != 1){
			double pirRate = Double.valueOf(sdtncDtoOtherConfig.getPirRate());
			vQosPir = (int) (bandWidth * pirRate * CHANGE_BANDWIDTH_TO_QOS);
		}
		int vQosCir = bandWidth * CHANGE_BANDWIDTH_TO_QOS;
		addPwDtoPrmMap.put(SdtncDtoUtil.DTO_PRM_KEY_V_QOS_SLA, String.valueOf(slaMode));
		addPwDtoPrmMap.put(SdtncDtoUtil.DTO_PRM_KEY_V_QOS_PIR, String.valueOf(vQosPir));
		addPwDtoPrmMap.put(SdtncDtoUtil.DTO_PRM_KEY_V_QOS_CIR, String.valueOf(vQosCir));
		
		SdtncRequestDto addPwDto = SdtncDtoUtil.creatAddPwReqDto(addPwDtoPrmMap);
		SdtncResponseDto resAddPwDto = sdtncService.createPw(addPwDto);
		return resAddPwDto;
	}

	/**
	 * Requests to delete vpath to SDTNC.
	 * @param flowDto Deleted flow DTO.
	 * @param token Access token. 
	 * @param sliceId Slice ID.
	 * @param pathId Vpath ID.
	 * @throws MloException Failed in processing. 
	 */
	private void doSdtncDeleteVPath(FlowDto flowDto, String token, String sliceId, String pathId) throws MloException {
		HashMap<String, String> deletePrmMap = new HashMap<String, String>();
		deletePrmMap.put(PARAM_KEY_SLICE_ID, sliceId);
		deletePrmMap.put(PARAM_KEY_VPATH_ID, pathId);
		deletePrmMap.put(PARAM_KEY_TOKEN, token);
		sdtncService.deletePw(deletePrmMap);
	}

	/**
	 * Obtains PW label (upstream-downstream common) from FlowDto.id.
	 * @param flowDto Flow DTO.
	 * @return PW label.
	 */
	String createVPwLabel(FlowDto flowDto) {
		return String.format("%06x", (PW_LABEL_OFFSET + flowDto.id));
	}

	/**
	 * Obtains link ID from link information in flow DTO.
	 * @param flow  Flow..
	 * @return Link ID.
	 */
	private String getLinkId(FlowDto flow){
		String linkId = null;
		if(flow.linkInfoList.size() > 1){
			linkId = sdtncDtoOtherConfig.getHopLinkId();
		}else{
			linkId = sdtncDtoOtherConfig.getCutLinkId();
		}
		return linkId;
	}
	
	/**
	 * Obtains start-edge port ID.
	 * @param flow Flow. 
	 * @return Port ID.
	 */
	private String getPortIdA(FlowDto flow){
		String portIdA = null;
		if(flow.linkInfoList.size() > 1){
			portIdA = sdtncDtoOtherConfig.getHopPortIdA();
		}else{
			portIdA = sdtncDtoOtherConfig.getCutPortIdA();
		}
		return portIdA;
	}
	
	/**
	 * Obtains end-edge port ID.
	 * @param flow Flow.
	 * @return Port ID. 
	 */
	private String getPortIdZ(FlowDto flow){
		String portIdZ = null;
		if(flow.linkInfoList.size() > 1){
			portIdZ = sdtncDtoOtherConfig.getHopPortIdZ();
		}else{
			portIdZ = sdtncDtoOtherConfig.getCutPortIdZ();
		}
		return portIdZ;
	}
	
	/**
	 * Obtains flow path name.
	 * @param flow Flow.
	 * @return Path name.
	 */
	private String getPathName(FlowDto flow){
		return String.valueOf(flow.id) + "_" + flow.name;
	}
	
	/**
	 * Obtains VLAN ID.
	 * @param reqFlowDto Flow.
	 * @throws MloException Error in checking VLAN ID.I
	 */
	private String getVlanId(FlowDto reqFlowDto) throws MloException{
		String srcVlanId = topologyRepository.queryVlanId(reqFlowDto.srcCENodeName, reqFlowDto.srcCEPortNo);
		String dstVlanId = topologyRepository.queryVlanId(reqFlowDto.dstCENodeName, reqFlowDto.dstCEPortNo);
		
		if(srcVlanId == null){
			throw new ApiCallException("vlanId is null [srcCENodeName=" + reqFlowDto.srcCENodeName + ", srcCEPortNo=" + reqFlowDto.srcCEPortNo +"]");
		}
		
		if(dstVlanId == null){
			throw new ApiCallException("vlanId is null [dstCENodeName=" + reqFlowDto.dstCENodeName + ", dstCEPortNo=" + reqFlowDto.dstCEPortNo +"]");
		}
		
		if(!(srcVlanId.equals(dstVlanId))){
			throw new ApiCallException("vlanId is different  [srcCENodeName=" + reqFlowDto.srcCENodeName
										+ ", srcCEPortNo=" + reqFlowDto.srcCEPortNo
										+ ", dstCENodeName=" + reqFlowDto.dstCENodeName 
										+ ", dstCEPortNo=" + reqFlowDto.dstCEPortNo +"]");
		}
		
		return srcVlanId;
	}
	
	/**
	 * Setter method (For DI setter injection).
	 * @param sdtncService The instance. 
	 */
	public void setSdtncService(SdtncService sdtncService){
		this.sdtncService = sdtncService;
	}
	
	/**
	 * Obtains SDTNC adapter service.
	 * @return sdtncService
	 */
	public SdtncService getSdtncService(){
		return sdtncService;
	}
	
	/**
	 * Obtains vpath ID map.
	 * @return pathIdMap
	 */
	public HashMap<Integer, String> getPathIdMap(){
		return pathIdMap;
	}
	
	/**
	 * Sets vpath ID map.
	 * @param map vPathId map.
	 */
	public void setPathIdMap(HashMap<Integer, String> map){
		pathIdMap = map;
	}
	
	/**
	 * Obtains {@link EquipmentConfiguratorOptDeviceImpl} instance.
	 * @return the instance. 
	 */
	public EquipmentConfiguratorOptDeviceImpl getEquipmentConfiguratorOptDeviceImpl() {
		return equipmentConfiguratorOptDeviceImpl;
	}
}
