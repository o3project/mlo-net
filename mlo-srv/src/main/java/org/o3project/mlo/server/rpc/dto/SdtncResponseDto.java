/**
 * SdtncResponseDto.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.rpc.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * This class is the response DTO from SDTNC.
 */
@XmlAccessorType(XmlAccessType.FIELD)
//@XmlRootElement(name = "root")
public class SdtncResponseDto {

	/** Header. */
    @XmlElement(name = "head")
    public SdtncHeadDto head;

	/** Authetication. */
    @XmlElement(name = "auth")
    public SdtncAuthDto auth;

	/** Login. */
    @XmlElement(name = "login")
    public SdtncLoginDto login;
    
	/** Slice. */
    @XmlElement(name = "slice")
    public SdtncSliceDto slice;

	/** Path. */
    @XmlElement(name = "vpath")
    public List<SdtncVpathDto> vpath;

	/** Link. */
    @XmlElement(name = "vlink")
    public List<SdtncVlinkDto> vlink;

}

