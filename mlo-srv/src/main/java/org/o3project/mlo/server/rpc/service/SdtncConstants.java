/**
 * SdtncConstants.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.rpc.service;

import org.o3project.mlo.server.impl.rpc.service.SdtncDtoUtil;

/**
 * This interface defines constants used by {@link SdtncService} interface.
 */
public interface SdtncConstants {
	
	/** Parameter key: Access token. */
	String PARAM_KEY_TOKEN = SdtncDtoUtil.DTO_PRM_KEY_TOKEN;

	/** Parameter key: Slice ID. */
	String PARAM_KEY_SLICE_ID = SdtncDtoUtil.DTO_PRM_KEY_SLICEID;
	
	/** Parameter key: VPATH ID. */
	String PARAM_KEY_VPATH_ID = SdtncDtoUtil.DTO_PRM_KEY_PATHID;
	
	/** Parameter key: VLINK ID. */
	String PARAM_KEY_VLINK_ID = "vObjectIndex";
	
}
