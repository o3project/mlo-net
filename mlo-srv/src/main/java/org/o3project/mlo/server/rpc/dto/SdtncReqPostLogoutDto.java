/**
 * SdtncReqPostLogoutDto.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.rpc.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class is the logout request DTO for SDTNC.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "CpfIfParamLogin")
public class SdtncReqPostLogoutDto implements SdtncRequestDto {

	/** Header. */
    @XmlElement(name = "head")
    public SdtncHeadDto head;

	/** Authentication. */
    @XmlElement(name = "auth")
    public SdtncAuthDto auth;

	/** Login information. */
    @XmlElement(name = "login")
    public SdtncLoginDto login;

}

