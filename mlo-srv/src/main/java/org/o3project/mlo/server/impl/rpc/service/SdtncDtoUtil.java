/**
 * SdtncDtoUtil.java
 * (C) 2013, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.impl.rpc.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.bind.JAXB;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.o3project.mlo.server.logic.InternalException;
import org.o3project.mlo.server.logic.MloException;
import org.o3project.mlo.server.rpc.dto.SdtncReqPostCreatePwDto;
import org.o3project.mlo.server.rpc.dto.SdtncReqPostLoginDto;
import org.o3project.mlo.server.rpc.dto.SdtncReqPostLogoutDto;
import org.o3project.mlo.server.rpc.dto.SdtncRequestDto;
import org.seasar.framework.util.ResourceUtil;


/**
 * This class is the utility class to create request DTO in connecting to SDTNC.
 */
public class SdtncDtoUtil {

	private static final Log LOG = LogFactory.getLog(SdtncDtoUtil.class);
	
	// Path setting.
	private static final String DATA_BASE_PATH = "org/o3project/mlo/server/rpc/service";
	private static final String TEMP_LOGIN_XML = "login.req.xml";
	private static final String TEMP_LOGOUT_XML = "logout.req.xml";
	private static final String TEMP_PW_ADD_XML = "createVpath.req.xml";
	
	/** Parameter key: Login ID. */
	public static final String DTO_PRM_KEY_LOGIN_ID = "loginId";
	/** Parameter key: Login password. */
	public static final String DTO_PRM_KEY_LOGIN_PASSWORD = "loginPassword";
	/** Parameter key: IP address. */
	public static final String DTO_PRM_KEY_LOGIN_IP_ADDRESS = "ipAddress";
	/** Parameter key: Access token. */
	public static final String DTO_PRM_KEY_TOKEN = "token";
	/** Parameter key: Slice ID. */
	public static final String DTO_PRM_KEY_SLICEID = "groupIndex";
	/** Parameter key: Path ID. */
	public static final String DTO_PRM_KEY_PATHID = "vObjectIndex";
	/** Parameter key: Vlan ID. */
	public static final String DTO_PRM_KEY_VLANID = "vVlanVLanIdNniClient";
	/** Parameter key: Path name. */
	public static final String DTO_PRM_KEY_PATH_NAME = "vObjectName";
	/** Parameter key: Node ID (start-edge). */
	public static final String DTO_PRM_KEY_NEID_A = "neId_A";
	/** Parameter key: Port ID (start-edge). */
	public static final String DTO_PRM_KEY_PORTID_A = "portId_A";
	/** Parameter key: Node ID (end-edge). */
	public static final String DTO_PRM_KEY_NEID_Z = "neId_Z";
	/** Parameter key: Port ID (end-edge). */
	public static final String DTO_PRM_KEY_PORTID_Z = "portId_Z";
	/** Parameter key: Path element ID. */
	public static final String DTO_PRM_KEY_ELEMENT_INDEX = "elementIndex";
	/** Parameter key: PW label (downstream). */
	public static final String DTO_PRM_KEY_V_PW_DOWN_LABEL = "vPwDownLabel";
	/** Parameter key: PW label (upstream). */
	public static final String DTO_PRM_KEY_V_PW_UP_LABEL = "vPwUpLabel";
	/** Parameter key: SLA mode. */
	public static final String DTO_PRM_KEY_V_QOS_SLA = "vQoSSla";
	/** Parameter key: vQoS Pir. */
	public static final String DTO_PRM_KEY_V_QOS_PIR = "vQoSPir";
	/** Parameter key: vQoS Cir. */
	public static final String DTO_PRM_KEY_V_QOS_CIR = "vQoSCir";
	
	/**
	 * Creates request DTO to login.
	 * @param prmMap Parameter map.
	 * @return Request DTO.
	 * @throws MloException Failed to create DTO.
	 */
	public static SdtncRequestDto createLoginReqDto(HashMap<String, String> prmMap) throws MloException{
		
		// Creates DTO for response.
		SdtncReqPostLoginDto reqDto = createTempDto(TEMP_LOGIN_XML, SdtncReqPostLoginDto.class);
		
		String loginId = prmMap.get(DTO_PRM_KEY_LOGIN_ID);
		// Overwrites login ID.
		reqDto.login.loginId = loginId;
		
		// Overwrites login password.
		reqDto.login.loginPassword = prmMap.get(DTO_PRM_KEY_LOGIN_PASSWORD);
		
		// Overwrites IP address.
		reqDto.login.ipAddress = prmMap.get(DTO_PRM_KEY_LOGIN_IP_ADDRESS);
		
		return reqDto;
	}
	
	/**
	 * Creates request DTO to logout.
	 * @param prmMap Parameter map. 
	 * @return Request DTO.
	 * @throws MloException Failed to create DTO.
	 */
	public static SdtncRequestDto createLogoutReqDto(HashMap<String, String> prmMap) throws MloException{
		
		// Creates DTO for response.
		SdtncReqPostLogoutDto reqDto = createTempDto(TEMP_LOGOUT_XML, SdtncReqPostLogoutDto.class);
		
		// Overwrites login ID.
		reqDto.login.loginId = prmMap.get(DTO_PRM_KEY_LOGIN_ID);
		
		return reqDto;
		
	}
	
	/**
	 * Creates request DTO to set vpath information.
	 * @param prmMap Parameter map.
	 * @return Request DTO.q
	 * @throws MloException Failed to create DTO.
	 */
	public static SdtncRequestDto creatAddPwReqDto(HashMap<String, String> prmMap) throws MloException{
		
		// Creates DTO for response.
		SdtncReqPostCreatePwDto reqDto = createTempDto(TEMP_PW_ADD_XML, SdtncReqPostCreatePwDto.class);
		
		// Overwrites slice ID.
		reqDto.slice.groupIndex = prmMap.get(DTO_PRM_KEY_SLICEID);
		
		// Sets access token
		reqDto.auth.token = prmMap.get(DTO_PRM_KEY_TOKEN);
		
		// Sets vlan ID.
		reqDto.vpath.get(0).vlan.uni.vVlanVLanIdNniClient = Integer.valueOf(prmMap.get(DTO_PRM_KEY_VLANID));
		
		// Sets path name.
		reqDto.vpath.get(0).vObjectName = prmMap.get(DTO_PRM_KEY_PATH_NAME);
		
		// neId(start-edge)
		reqDto.vpath.get(0).pathEndPoint.get(0).neId = prmMap.get(DTO_PRM_KEY_NEID_A);
		
		// portId(start-edge)
		reqDto.vpath.get(0).pathEndPoint.get(0).portId = prmMap.get(DTO_PRM_KEY_PORTID_A);
		
		// neId(end-edge)
		reqDto.vpath.get(0).pathEndPoint.get(1).neId = prmMap.get(DTO_PRM_KEY_NEID_Z);
		
		// portId(end-edge)
		reqDto.vpath.get(0).pathEndPoint.get(1).portId = prmMap.get(DTO_PRM_KEY_PORTID_Z);
		
		// pathRoute element ID
		reqDto.vpath.get(0).pathRoute.get(0).line.get(0).elementIndex = prmMap.get(DTO_PRM_KEY_ELEMENT_INDEX);
		
		// PW label (downstream)
		reqDto.vpath.get(0).pw.vPwDownLabel = prmMap.get(DTO_PRM_KEY_V_PW_DOWN_LABEL);
		
		// PW label (upstream)
		reqDto.vpath.get(0).pw.vPwUpLabel = prmMap.get(DTO_PRM_KEY_V_PW_UP_LABEL);
		
		// SLA mode
		reqDto.vpath.get(0).qos.vQoSSla = Integer.valueOf(prmMap.get(DTO_PRM_KEY_V_QOS_SLA));
				
		// vQoS Pir
		reqDto.vpath.get(0).qos.vQoSPir = Integer.valueOf(prmMap.get(DTO_PRM_KEY_V_QOS_PIR));
		
		// vQoS Cir
		reqDto.vpath.get(0).qos.vQoSCir = Integer.valueOf(prmMap.get(DTO_PRM_KEY_V_QOS_CIR));
		
		return reqDto;
	}
	
	/**
	 * Loads template xml file, and then creates request DTO.
	 * @param xmlFile Template XML file name.
	 * @return Request DTO.
	 * @throws MloException Failed to load template file.
	 */
	private static <T> T createTempDto(String xmlFileName, Class<T> type) throws MloException{
		T reqDto = null;
		InputStream istream = null;
		File xmlFile = ResourceUtil.getResourceAsFile(DATA_BASE_PATH + "/" + xmlFileName);
		try {
			istream = new FileInputStream(xmlFile);
			reqDto = JAXB.unmarshal(istream, type);
		} catch (FileNotFoundException e) {
			// Never pass here.
			throw new InternalException("templete file is not found [file = " + xmlFile.getName() +"]");
		} finally {
			if (istream != null) {
				try {
					istream.close();
				} catch (IOException e) {
					reqDto = null;
					LOG.error("FileInputStream close failed", e);
				}
			}
		}
		
		return reqDto;
	}
	
	private SdtncDtoUtil() {
		super();
	}
}
