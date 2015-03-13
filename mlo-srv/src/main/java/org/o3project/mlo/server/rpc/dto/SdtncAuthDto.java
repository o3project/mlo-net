/**
 * SdtncAuthDto.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.rpc.dto;

import javax.xml.bind.annotation.XmlElement;

/**
 * This class is the authentication DTO class of SDTNC.
 */
public class SdtncAuthDto {

	/** Access token */
	@XmlElement(name = "token")
	public String token;
	
}
