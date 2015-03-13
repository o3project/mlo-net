/**
 * PseudoSdtncVrmServiceImpl.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.psdtnc.impl.logic;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.o3project.mlo.psdtnc.dto.SdtncVrmifLoginDto;
import org.o3project.mlo.psdtnc.dto.SdtncVrmifVlinkListDto;
import org.o3project.mlo.psdtnc.dto.SdtncVrmifVpathDto;
import org.o3project.mlo.psdtnc.dto.SdtncVrmifVpathListDto;
import org.o3project.mlo.psdtnc.form.SdtncVrmifForm;
import org.o3project.mlo.psdtnc.logic.ConfigConstants;
import org.o3project.mlo.psdtnc.logic.LdOperationException;
import org.o3project.mlo.psdtnc.logic.LdOperationService;
import org.o3project.mlo.psdtnc.logic.PseudoSdtncVrmService;
import org.o3project.mlo.server.dto.LdFlowDto;
import org.o3project.mlo.server.logic.ConfigProvider;
import org.o3project.mlo.server.logic.LdTopologyRepository;
import org.o3project.mlo.server.logic.MloException;
import org.o3project.mlo.server.rpc.dto.SdtncAuthDto;
import org.o3project.mlo.server.rpc.dto.SdtncHeadDto;
import org.o3project.mlo.server.rpc.dto.SdtncLoginDto;
import org.o3project.mlo.server.rpc.dto.SdtncSliceDto;
import org.o3project.mlo.server.rpc.dto.SdtncVlinkDto;
import org.o3project.mlo.server.rpc.dto.SdtncVpathDto;
import org.seasar.framework.beans.util.Beans;

/**
 * PseudoSdtncVrmServiceImpl
 *
 */
public class PseudoSdtncVrmServiceImpl implements PseudoSdtncVrmService, ConfigConstants {
	private static final Log LOG = LogFactory.getLog(PseudoSdtncVrmServiceImpl.class);
	
	private static final SimpleDateFormat DTO_HEAD_TIME_FMT = new SimpleDateFormat("yyyyMMddHHmmssZ");
	
	private final Object oMutex = new Object();
	
	private ConfigProvider configProvider;
	
	private LdTopologyRepository ldTopologyRepository;
	
	private LdOperationService ldOperationService;
	
	private LdOperationService ldOperationServiceDummyLocalImpl;
	
	private final Map<String, String> loginIdTokenMap = new LinkedHashMap<String, String>();
	
	private final Map<String, SdtncLoginDto> tokenLoginDtoMap = new LinkedHashMap<String, SdtncLoginDto>();
	
	private final Map<String, SdtncVpathDto> vpathIdVpathDtoMap = new LinkedHashMap<String, SdtncVpathDto>();
	
	private int currentVpathIdIndex = 0;
	
	/**
	 * @param configProvider the configProvider to set
	 */
	public void setConfigProvider(ConfigProvider configProvider) {
		this.configProvider = configProvider;
	}
	
	/**
	 * @param ldTopologyRepository the ldTopologyRepository to set
	 */
	public void setLdTopologyRepository(LdTopologyRepository ldTopologyRepository) {
		this.ldTopologyRepository = ldTopologyRepository;
	}
	
	/**
	 * @param ldOperationService the ldOperationService to set
	 */
	public void setLdOperationService(LdOperationService ldOperationService) {
		this.ldOperationService = ldOperationService;
	}
	
	/**
	 * @param ldOperationServiceDummyLocalImpl the ldOperationServiceDummyLocalImpl to set
	 */
	public void setLdOperationServiceDummyLocalImpl(LdOperationService ldOperationServiceDummyLocalImpl) {
		this.ldOperationServiceDummyLocalImpl = ldOperationServiceDummyLocalImpl;
	}
	
	public void init() {
		
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.psdtnc.logic.PseudoSdtncVrmService#doPostLogin(org.o3project.mlo.psdtnc.dto.SdtncVrmifLoginDto)
	 */
	@Override
	public SdtncVrmifLoginDto doPostLogin(SdtncVrmifLoginDto reqDto) {
		SdtncVrmifLoginDto resDto = null;
		synchronized (oMutex) {
			resDto = doPostLoginInternal(reqDto);
		}
		return resDto;
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.psdtnc.logic.PseudoSdtncVrmService#doPostLogout(org.o3project.mlo.psdtnc.dto.SdtncVrmifLoginDto)
	 */
	@Override
	public SdtncVrmifLoginDto doPostLogout(SdtncVrmifLoginDto reqDto) {
		SdtncVrmifLoginDto resDto = null;
		synchronized (oMutex) {
			resDto = doPostLogoutInternal(reqDto);
		}
		return resDto;
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.psdtnc.logic.PseudoSdtncVrmService#doGetLink(org.o3project.mlo.psdtnc.form.SdtncVrmifForm)
	 */
	@Override
	public SdtncVrmifVlinkListDto doGetLink(SdtncVrmifForm reqParams) {
		SdtncVrmifVlinkListDto resDto = null;
		synchronized (oMutex) {
			resDto = doGetLinkInternal(reqParams);
		}
		return resDto;
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.psdtnc.logic.PseudoSdtncVrmService#doPostPath(org.o3project.mlo.psdtnc.dto.SdtncVrmifVpathDto)
	 */
	@Override
	public SdtncVrmifVpathDto doPostPath(SdtncVrmifVpathDto reqDto) {
		SdtncVrmifVpathDto resDto = null;
		synchronized (oMutex) {
			resDto = doPostPathInternal(reqDto);
		}
		return resDto;
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.psdtnc.logic.PseudoSdtncVrmService#doDeletePath(org.o3project.mlo.psdtnc.form.SdtncVrmifForm)
	 */
	@Override
	public SdtncVrmifVpathDto doDeletePath(SdtncVrmifForm reqParams) {
		SdtncVrmifVpathDto resDto = null;
		synchronized (oMutex) {
			resDto = doDeletePathInternal(reqParams);
		}
		return resDto;
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.psdtnc.logic.PseudoSdtncVrmService#doGetPath(org.o3project.mlo.psdtnc.form.SdtncVrmifForm)
	 */
	@Override
	public SdtncVrmifVpathListDto doGetPath(SdtncVrmifForm reqParams) {
		SdtncVrmifVpathListDto resDto = null;
		synchronized (oMutex) {
			resDto = doGetPathInternal(reqParams);
		}
		return resDto;
	}

	private SdtncVrmifLoginDto doPostLoginInternal(SdtncVrmifLoginDto reqDto) {
		String loginId = reqDto.login.loginId;
		
		SdtncVrmifLoginDto resDto = new SdtncVrmifLoginDto();
		resDto.head = createResponseHeadDto();
		if (loginIdTokenMap.containsKey(loginId)) {
			resDto.head.majorResponseCode = 40;
			resDto.head.minorResponseCode = 000;
			resDto.head.errorDetail = "Invalid userid or passwd.";
			resDto.auth = null;
			resDto.login = null;
		} else if (checkCredentials(reqDto)) {
			String token = generateToken(loginId);
			resDto.auth = new SdtncAuthDto();
			resDto.auth.token = token;
			resDto.login = Beans.createAndCopy(SdtncLoginDto.class, reqDto.login).execute();
			resDto.login.accountName = "Admin user";
			resDto.login.accountLoginDate = getCurrentDateString();
			
			loginIdTokenMap.put(loginId, token);
			tokenLoginDtoMap.put(token, resDto.login);
		} else {
			resDto.head.majorResponseCode = 90;
			resDto.head.minorResponseCode = 900;
			resDto.head.errorDetail = "Invalid userid or passwd.";
			resDto.auth = null;
			resDto.login = null;
		}
		return resDto;
	}
	
	private SdtncVrmifLoginDto doPostLogoutInternal(SdtncVrmifLoginDto reqDto) {
		String loginId = reqDto.login.loginId;
		
		SdtncVrmifLoginDto resDto = new SdtncVrmifLoginDto();
		resDto.head = createResponseHeadDto();
		if (loginIdTokenMap.containsKey(loginId)) {
			String token = loginIdTokenMap.get(loginId);
			loginIdTokenMap.remove(loginId);
			tokenLoginDtoMap.remove(token);
			
			resDto.login = Beans.createAndCopy(SdtncLoginDto.class, reqDto.login).execute();
		} else {
			resDto.head.majorResponseCode = 91;
			resDto.head.minorResponseCode = 901;
			resDto.head.errorDetail = "Not logged in. : " + loginId;
			resDto.auth = null;
			resDto.login = null;
		}
		return resDto;
	}
	
	private SdtncVrmifVlinkListDto doGetLinkInternal(SdtncVrmifForm reqParams) {
		String token = reqParams.token;
		String sliceId = reqParams.groupIndex;
		String linkId = reqParams.vObjectIndex;
		
		SdtncVrmifVlinkListDto resDto = new SdtncVrmifVlinkListDto();
		resDto.head = createResponseHeadDto();
		if (!isTokenAvailable(token)) {
			resDto.head.majorResponseCode = 92;
			resDto.head.minorResponseCode = 902;
			resDto.head.errorDetail = "Token is not available. : " + token;
		} else if (!isSliceIdAvailable(token, sliceId)) {
			resDto.head.majorResponseCode = 93;
			resDto.head.minorResponseCode = 903;
			resDto.head.errorDetail = "Invalid slice ID. : " + sliceId;
		} else {
			List<LdFlowDto> ldFlows = null;
			try {
				resDto.slice = new SdtncSliceDto();
				resDto.slice.groupIndex = sliceId;
				ldFlows = ldTopologyRepository.getLdTopo().flows;
				resDto.vlink = new ArrayList<SdtncVlinkDto>();
				for (LdFlowDto ldFlow : ldFlows) {
					if (linkId == null) {
						resDto.vlink.add(convertToVlink(ldFlow));
					} else if (linkId.equals(ldFlow.name)) {
						resDto.vlink.add(convertToVlink(ldFlow));
						break;
					}
				}
			} catch (MloException e) {
				LOG.warn("Failed to get Ld topology.", e);
				resDto.head.majorResponseCode = 94;
				resDto.head.minorResponseCode = 904;
				resDto.head.errorDetail = "Unexpected situation. : "  + e.getMessage();
			}
		}
		
		return resDto;
	}
	
	private SdtncVrmifVpathDto doPostPathInternal(SdtncVrmifVpathDto reqDto) {
		String token = reqDto.auth.token;
		String sliceId = reqDto.slice.groupIndex;
		SdtncVpathDto sdtncVpathDto = reqDto.vpath.get(0);
		String linkId = getLinkId(sdtncVpathDto); 
		
		SdtncVrmifVpathDto resDto = new SdtncVrmifVpathDto();
		resDto.head = createResponseHeadDto();
		if (!isTokenAvailable(token)) {
			resDto.head.majorResponseCode = 92;
			resDto.head.minorResponseCode = 902;
			resDto.head.errorDetail = "Token is not available. : " + token;
		} else if (!isSliceIdAvailable(token, sliceId)) {
			resDto.head.majorResponseCode = 93;
			resDto.head.minorResponseCode = 903;
			resDto.head.errorDetail = "Invalid slice ID. : " + sliceId;
		} else {
			List<LdFlowDto> ldFlows = null;
			try {
				ldFlows = ldTopologyRepository.getLdTopo().flows;
				LdFlowDto selectedLdFlowDto = null;
				for (LdFlowDto ldFlow : ldFlows) {
					if (linkId.equals(ldFlow.name)) {
						selectedLdFlowDto = ldFlow;
						break;
					}
				}
				if (selectedLdFlowDto != null) {
					String pathId = generateVpathId(sliceId, linkId);
					sdtncVpathDto.vObjectIndex = pathId;
					
					// invoke here
					invokeToPutFlows(Arrays.asList(sdtncVpathDto));
					
					vpathIdVpathDtoMap.put(pathId, sdtncVpathDto);
					resDto.vpath = new ArrayList<SdtncVpathDto>();
					resDto.vpath.add(new SdtncVpathDto());
					resDto.vpath.get(0).vObjectIndex = pathId;
				} else {
					resDto.head.majorResponseCode = 95;
					resDto.head.minorResponseCode = 905;
					resDto.head.errorDetail = "Invalid link ID. : "  + linkId;
				}
			} catch (MloException e) {
				LOG.warn("Failed to put a flow.", e);
				resDto.head.majorResponseCode = 94;
				resDto.head.minorResponseCode = 904;
				resDto.head.errorDetail = "Unexpected situation. : "  + e.getMessage();
			} catch (LdOperationException e) {
				LOG.warn("Failed to put a flow.", e);
				resDto.head.majorResponseCode = 94;
				resDto.head.minorResponseCode = 904;
				resDto.head.errorDetail = "Unexpected situation. : "  + e.getMessage();
			}
		}		
		return resDto;
	}

	/**
	 * @param reqParams
	 * @return
	 */
	private SdtncVrmifVpathDto doDeletePathInternal(SdtncVrmifForm reqParams) {
		String token = reqParams.token;
		String sliceId = reqParams.groupIndex;
		String pathId = reqParams.vObjectIndex;
		
		SdtncVrmifVpathDto resDto = new SdtncVrmifVpathDto();
		resDto.head = createResponseHeadDto();
		if (!isTokenAvailable(token)) {
			resDto.head.majorResponseCode = 92;
			resDto.head.minorResponseCode = 902;
			resDto.head.errorDetail = "Token is not available. : " + token;
		} else if (!isSliceIdAvailable(token, sliceId)) {
			resDto.head.majorResponseCode = 93;
			resDto.head.minorResponseCode = 903;
			resDto.head.errorDetail = "Invalid slice ID. : " + sliceId;
		} else {
			SdtncVpathDto vpathDto = vpathIdVpathDtoMap.get(pathId);
			if (vpathDto != null) {
				try {
					// invoke here
					invokeToDeleteFlows(Arrays.asList(vpathDto));
					
					resDto.vpath = new ArrayList<SdtncVpathDto>();
					resDto.vpath.add(new SdtncVpathDto());
					resDto.vpath.get(0).vObjectIndex = vpathDto.vObjectIndex;
					resDto.vpath.get(0).resourceIndex = vpathDto.resourceIndex;
					
					vpathIdVpathDtoMap.remove(pathId);
				} catch (MloException e) {
					LOG.warn("Failed to delete a flow.", e);
					resDto.head.majorResponseCode = 94;
					resDto.head.minorResponseCode = 904;
					resDto.head.errorDetail = "Unexpected situation. : "  + e.getMessage();
				} catch (LdOperationException e) {
					LOG.warn("Failed to delete a flow.", e);
					resDto.head.majorResponseCode = 94;
					resDto.head.minorResponseCode = 904;
					resDto.head.errorDetail = "Unexpected situation. : "  + e.getMessage();
				}
			} else {
				resDto.head.majorResponseCode = 11;
				resDto.head.minorResponseCode = 400;
				resDto.head.errorDetail = "4430-11400 The specified interface might have been modified or deleted by another user. : " + pathId;
			}
		}		
		return resDto;
	}
	
	/**
	 * @param reqParams
	 * @return
	 */
	private SdtncVrmifVpathListDto doGetPathInternal(SdtncVrmifForm reqParams) {
		String token = reqParams.token;
		String sliceId = reqParams.groupIndex;
		String pathId = reqParams.vObjectIndex;
		
		SdtncVrmifVpathListDto resDto = new SdtncVrmifVpathListDto();
		resDto.head = createResponseHeadDto();
		if (!isTokenAvailable(token)) {
			resDto.head.majorResponseCode = 92;
			resDto.head.minorResponseCode = 902;
			resDto.head.errorDetail = "Token is not available. : " + token;
		} else if (!isSliceIdAvailable(token, sliceId)) {
			resDto.head.majorResponseCode = 93;
			resDto.head.minorResponseCode = 903;
			resDto.head.errorDetail = "Invalid slice ID. : " + sliceId;
		} else {
			List<SdtncVpathDto> vpathDtoList = null;
			if (pathId != null) {
				SdtncVpathDto vpathDto = vpathIdVpathDtoMap.get(pathId);
				if (vpathDto != null) {
					vpathDtoList = Arrays.asList(vpathDto);
				}
			} else {
				vpathIdVpathDtoMap.values();
				vpathDtoList = new ArrayList<SdtncVpathDto>(vpathIdVpathDtoMap.values());
			}
			
			if (vpathDtoList != null) {
				resDto.vpath = vpathDtoList;
			} else {
				resDto.head.majorResponseCode = 11;
				resDto.head.minorResponseCode = 400;
				resDto.head.errorDetail = "4430-11400 The specified interface might have been modified or deleted by another user. : " + pathId;
			}
		}		
		return resDto;
	}

	/**
	 * @param sdtncVpathDto
	 * @return
	 */
	String getLinkId(SdtncVpathDto sdtncVpathDto) {
		// Supports only 1 LSP.
		return sdtncVpathDto.pathRoute.get(0).line.get(0).elementIndex;
	}

	/**
	 * @param vpathDtoList
	 * @throws LdOperationException 
	 */
	private void invokeToPutFlows(List<SdtncVpathDto> vpathDtoList) throws LdOperationException, MloException {
		String shellCommand = null;
		Integer exitStatus = null;
		
		for (SdtncVpathDto vpathDto : vpathDtoList) {
			String flowName = getLinkId(vpathDto);
			shellCommand = getShellCommandPutFlow(flowName);
			exitStatus = getAvailableLdOperationService().doExecuteSingleCommand(shellCommand);
			LOG.info(String.format("Executed putting a flow. : (shellCommand, exitStatus) = (%s, %s)", shellCommand, exitStatus));
		}
	}

	/**
	 * @param vpathDtoList
	 * @throws LdOperationException 
	 */
	private void invokeToDeleteFlows(List<SdtncVpathDto> vpathDtoList) throws LdOperationException, MloException {
		String shellCommand = null;
		Integer exitStatus = null;
		
		for (SdtncVpathDto vpathDto : vpathDtoList) {
			String flowName = getLinkId(vpathDto);
			shellCommand = getShellCommandDeleteFlow(flowName);
			exitStatus = getAvailableLdOperationService().doExecuteSingleCommand(shellCommand);
			LOG.info(String.format("Executed deleting a flow. : (shellCommand, exitStatus) = (%s, %s)", shellCommand, exitStatus));
		}
	}

	/**
	 * @return
	 */
	private LdOperationService getAvailableLdOperationService() {
		boolean isDebugLocal = configProvider.getBooleanProperty(PROP_KEY_LD_DEBUG_ENABLE_LOCAL_SAMPLE);
		if (!isDebugLocal) {
			return ldOperationService;
		} else {
			return ldOperationServiceDummyLocalImpl;
		}
	}

	/**
	 * @param flowName
	 * @return
	 */
	private String getShellCommandPutFlow(String flowName) {
		String fmt = configProvider.getProperty(PROP_KEY_LD_OPERATION_SHELL_COMMAND_FMT_PUT_FLOW);
		return String.format(fmt, flowName);
	}

	/**
	 * @param flowName
	 * @return
	 */
	private String getShellCommandDeleteFlow(String flowName) {
		String fmt = configProvider.getProperty(PROP_KEY_LD_OPERATION_SHELL_COMMAND_FMT_DELETE_FLOW);
		return String.format(fmt, flowName);
	}

	private SdtncHeadDto createResponseHeadDto() {
		SdtncHeadDto dto = new SdtncHeadDto();
		dto.sequenceNumber = 0;
		dto.time = getCurrentDateString();
		dto.majorResponseCode = 0;
		dto.minorResponseCode = 0;
		dto.errorDetail = "";
		return dto;
	}
	
	private boolean checkCredentials(SdtncVrmifLoginDto reqDto) {
		boolean canLogin = false;
		List<String> ids = getLoginIds();
		String loginId = reqDto.login.loginId;
		if (ids.contains(loginId)) {
			String passwd = getPasswd(loginId);
			canLogin = (passwd != null) && (passwd.equals(reqDto.login.loginPassword));
		}
		return canLogin;
	}

	private List<String> getLoginIds() {
		return Arrays.asList(configProvider.getCommaSplitedProperty(PROP_KEY_VRM_LOGIN_IDS));
	}
	
	private String getPasswd(String loginId) {
		return getLoginIdSubProperty(loginId, PROP_SUBKEY_VRM_LOGIN_ID_PASSWORD);
	}
	
	private String getSliceId(String loginId) {
		return getLoginIdSubProperty(loginId, PROP_SUBKEY_VRM_SLICE_ID);
	}
	
	private String getLoginIdSubProperty(String loginId, String subkey) {
		String passwd = null;
		if (loginId != null) {
			Map<String, String> props = configProvider.getSubProperties(PROP_KEY_VRM_LOGIN_ID_PREFIX + loginId + ".");
			if (props != null) {
				passwd = props.get(subkey);
			}
		}
		return passwd;
	}
	
	private String generateToken(String seed) {
		return String.format("%08x", seed.hashCode());
	}
	
	private boolean isTokenAvailable(String token) {
		return tokenLoginDtoMap.containsKey(token);
	}
	
	/**
	 * @param token
	 * @param sliceId
	 * @return
	 */
	private boolean isSliceIdAvailable(String token, String sliceId) {
		boolean isAvailable = false;
		SdtncLoginDto loginDto = tokenLoginDtoMap.get(token);
		if (loginDto != null) {
			String availableSliceId = getSliceId(loginDto.loginId);
			isAvailable = availableSliceId.equals(sliceId);
		}
		return isAvailable;
	}
	
	private SdtncVlinkDto convertToVlink(LdFlowDto ldFlowDto) {
		int brNamesLen = ldFlowDto.brNames.size();
		SdtncVlinkDto vlinkDto = new SdtncVlinkDto();
		vlinkDto.vObjectIndex = ldFlowDto.name;
		vlinkDto.vObjectName = ldFlowDto.name;
		vlinkDto.vObjectStatus = 2; // Normal
		vlinkDto.vObjectDescription = ldFlowDto.name;
		vlinkDto.resourceIndex = ldFlowDto.name;
		vlinkDto.vLineSource = ldFlowDto.brNames.get(0);
		vlinkDto.vLineSink = ldFlowDto.brNames.get(brNamesLen - 1);
		return vlinkDto;
	}
	
	private String generateVpathId(String sliceId, String linkId) {
		int vpathIdIndex = generateVpathIdIndex();
		return String.format("%s-%s-%08x", sliceId, linkId, vpathIdIndex );
	}
	
	private int generateVpathIdIndex() {
		int idx = currentVpathIdIndex;
		currentVpathIdIndex += 1;
		return idx;
	}
	
	private String getCurrentDateString() {
		synchronized (DTO_HEAD_TIME_FMT) {
			return DTO_HEAD_TIME_FMT.format(new Date());
		}
	}
}
