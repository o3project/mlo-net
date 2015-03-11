/**
 * SdtncLoginDto.java
 * (C) 2013, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.rpc.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * This class is the login DTO class for SDTNC.
 */
public class SdtncLoginDto {
	
	/** Login ID. */
	@XmlAttribute(name = "loginId")
	public String loginId;

	/** Login password. */
	@XmlElement(name = "loginPassword")
	public String loginPassword;

	/** IP address. */
	@XmlElement(name = "ipAddress")
	public String ipAddress;
	
	/** Account name. */
	@XmlElement(name = "accountName")
	public String accountName;
	
	/** Last login date. */
	@XmlElement(name = "accountLoginDate")
	public String accountLoginDate;
	
	/** Time zone. */
	@XmlElement(name = "accountTimeZone")
	public String accountTimeZone;
	
	/**
	 * Creates the login DTO.
	 * @param logidId Login ID.
	 * @param loginPasswd Login password.
	 * @param ipAddress Login IP address.
	 * @param accountTimeZone Time zone.
	 * @return Login DTO.
	 */
	public static SdtncLoginDto createInstance(
			String logidId, String loginPasswd, 
			String ipAddress, String accountTimeZone) {
		SdtncLoginDto dto = new SdtncLoginDto();
		dto.loginId = logidId;
		dto.loginPassword = loginPasswd;
		dto.ipAddress = ipAddress;
		dto.accountTimeZone = accountTimeZone;
		return dto;
	}

}
