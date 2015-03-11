/**
 * DeletePwFailSdtncServiceImpl.java
 * (C) 2014, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import java.util.Map;

import org.o3project.mlo.server.impl.rpc.service.SdtncServiceImpl;
import org.o3project.mlo.server.logic.InternalException;
import org.o3project.mlo.server.logic.MloException;
import org.o3project.mlo.server.rpc.dto.SdtncResponseDto;


/**
 * DeletePwFailSdtncServiceImpl
 *
 */
public class DeletePwFailSdtncServiceImpl extends SdtncServiceImpl {

	/*
	 * (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.SDTNCService#deletePw(org.o3project.mlo.server.rpc.dto.SdtncRequestDto)
	 */
	@Override
	public SdtncResponseDto deletePw(Map<String, String> paramMap) throws MloException {
		throw new InternalException("SDTNCBadResponse/InvalidData/vObjectIndex");
	}
}
