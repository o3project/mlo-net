/**
 * CreatePwFailSdtncServiceImpl.java
 * (C) 2014,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import org.o3project.mlo.server.impl.rpc.service.SdtncServiceImpl;
import org.o3project.mlo.server.logic.InternalException;
import org.o3project.mlo.server.logic.MloException;
import org.o3project.mlo.server.rpc.dto.SdtncReqPostCreatePwDto;
import org.o3project.mlo.server.rpc.dto.SdtncRequestDto;
import org.o3project.mlo.server.rpc.dto.SdtncResponseDto;

/**
 * CreatePwFailSdtncServiceImpl
 *
 */
public class CreatePwFailSdtncServiceImpl extends SdtncServiceImpl {

	/*
	 * (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.SDTNCService#createPw(org.o3project.mlo.server.rpc.dto.SdtncRequestDto)
	 */
	@Override
	public SdtncResponseDto createPw(SdtncRequestDto reqDto) throws MloException {
		SdtncReqPostCreatePwDto reqDtoCreatePw = (SdtncReqPostCreatePwDto)reqDto;
		if(reqDtoCreatePw.vpath.get(0).qos.vQoSCir == 77770){
			throw new InternalException("SDTNCBadResponse/InvalidData/Creation of PW failed.");
		}
		SdtncResponseDto resDto = super.createPw(reqDto);
		return resDto;
		
	}
}
