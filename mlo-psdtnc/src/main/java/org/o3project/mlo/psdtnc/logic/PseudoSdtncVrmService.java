/**
 * PseudoSdtncVrmService.java
 * (C) 2015, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.psdtnc.logic;

import org.o3project.mlo.psdtnc.dto.SdtncVrmifLoginDto;
import org.o3project.mlo.psdtnc.dto.SdtncVrmifVlinkListDto;
import org.o3project.mlo.psdtnc.dto.SdtncVrmifVpathDto;
import org.o3project.mlo.psdtnc.dto.SdtncVrmifVpathListDto;
import org.o3project.mlo.psdtnc.form.SdtncVrmifForm;

/**
 * 
 *
 */
public interface PseudoSdtncVrmService {

	SdtncVrmifLoginDto doPostLogin(SdtncVrmifLoginDto reqDto);
	
	SdtncVrmifLoginDto doPostLogout(SdtncVrmifLoginDto reqDto);
	
	SdtncVrmifVlinkListDto doGetLink(SdtncVrmifForm reqParams);
	
	SdtncVrmifVpathDto doPostPath(SdtncVrmifVpathDto reqDto);
	
	SdtncVrmifVpathDto doDeletePath(SdtncVrmifForm reqParams);
	
	SdtncVrmifVpathListDto doGetPath(SdtncVrmifForm reqParams);
}
