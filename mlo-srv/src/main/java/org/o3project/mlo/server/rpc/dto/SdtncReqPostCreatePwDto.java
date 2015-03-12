/**
 * SdtncReqPostCreatePwDto.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.rpc.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class is the POST request DTO class to create PW for SDTNC.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "CpfIfParamVpath")
public class SdtncReqPostCreatePwDto implements SdtncRequestDto {

	/** Header. */
    @XmlElement(name = "head")
    public SdtncHeadDto head;

	/** Authentication. */
    @XmlElement(name = "auth")
    public SdtncAuthDto auth;

	/** Slice. */
    @XmlElement(name = "slice")
    public SdtncSliceDto slice;

	/** Path information. */
    @XmlElement(name = "vpath")
    public List<SdtncVpathDto> vpath;

}

