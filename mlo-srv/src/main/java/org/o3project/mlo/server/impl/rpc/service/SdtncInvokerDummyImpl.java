/**
 * SdtncInvokerDummyImpl.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.rpc.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.xml.bind.JAXB;

import org.o3project.mlo.server.logic.InternalException;
import org.o3project.mlo.server.logic.MloException;
import org.o3project.mlo.server.rpc.dto.SdtncRequestDto;
import org.o3project.mlo.server.rpc.dto.SdtncResponseDto;
import org.o3project.mlo.server.rpc.service.SdtncInvoker;
import org.o3project.mlo.server.rpc.service.SdtncMethod;
import org.seasar.framework.util.ResourceUtil;


/**
 * This class is the dummy implementation class of {@link SdtncInvoker} interface.
 * This class is only for debugging.
 */
public class SdtncInvokerDummyImpl implements SdtncInvoker {

	private static final String PATH_LOGIN = "login";
	private static final String PATH_LOGOUT = "logout";
	private static final String PATH_LINK = "link";
	private static final String PATH_PATH = "path";
	private static final String DATA_BASE_PATH = "org/o3project/mlo/server/rpc/service";
	
	private static final String LOGIN_XML = "dummyInvoker.login.res.xml";
	private static final String LOGOUT_XML = "dummyInvoker.logout.res.xml";
	private static final String PW_ADD_XML = "dummyInvoker.createPw.res.xml";
	private static final String PW_DELETE_XML = "dummyInvoker.deletePw.res.xml";
	private static final String PW_GET_XML = "dummyInvoker.getPw.res.xml";
	private static final String LSP_GET_XML = "dummyInvoker.getLspResource.res.xml";
	/*
	 * (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.SdtncInvoker#invoke(org.o3project.mlo.server.rpc.service.SdtncMethod, org.o3project.mlo.server.rpc.dto.SdtncRequestDto, java.lang.String, java.util.Map)
	 */
	@Override
	public SdtncResponseDto invoke(SdtncMethod method,
			SdtncRequestDto reqDto, String path, Map<String, String> params)
			throws MloException {
		
		SdtncResponseDto resDto = null;
		
		if("POST".equals(method.getName())){
			if(PATH_LOGIN.equals(path)){
				// Login response.
				resDto = createDto(LOGIN_XML);
			}else if(PATH_LOGOUT.equals(path)){
				// Logout response.
				resDto = createDto(LOGOUT_XML);
			}else if(PATH_PATH.equals(path)){
				// Create PW response.
				resDto = createDto(PW_ADD_XML);
			}
		}else if ("GET".equals(method.getName())){
			if(PATH_LINK.equals(path)){
				// Get LSP resource response.
				resDto = createDto(LSP_GET_XML);
			}else if(PATH_PATH.equals(path)){
				// Get PW response
				resDto = createDto(PW_GET_XML);
				
				String vpathId = params.get(SdtncDtoUtil.DTO_PRM_KEY_PATHID);
				
				
				final int hopVlanId = 425;
				final int cutVlanId = 426;
				if("testHBHPathId".equals(vpathId)){
					resDto.vpath.get(0).vlan.uni.vVlanVLanIdNniClient = hopVlanId;
				}
				
				if("testCThPathId".equals(vpathId)){
					resDto.vpath.get(0).vlan.uni.vVlanVLanIdNniClient = cutVlanId;
				}
				
			}
		}else if ("DELETE".equals(method.getName())){
			if(PATH_PATH.equals(path)){
				// Delete PW response.
				resDto = createDto(PW_DELETE_XML);
			}
		}
		
		return resDto;
	}
	
	/**
	 * Creates response DTO.
	 * @param xmlFileName Template XML file name.
	 * @return Response DTO. 
	 */
	private SdtncResponseDto createDto(String xmlFileName) throws MloException{
		SdtncResponseDto resDto = null;
		InputStream istream = null;
		File xmlFile = ResourceUtil.getResourceAsFile(DATA_BASE_PATH + "/" + xmlFileName);
		try {
			istream = new FileInputStream(xmlFile);
			resDto = JAXB.unmarshal(istream, SdtncResponseDto.class);
		} catch (FileNotFoundException e) {
			// Never pass here.
			throw new InternalException("templete response file is not found [file = " + xmlFile.getName() +"]");
		} finally {
			if (istream != null) {
				try {
					istream.close();
				} catch (IOException e) {
					resDto = null;
				}
			}
		}
		
		return resDto;
	}
}
