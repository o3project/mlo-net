/**
 * SdtncSliceDto.java
 * (C) 2013, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.rpc.dto;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * This class is the slice DTO for SDTNC.
 */
public class SdtncSliceDto {

	/** Slice ID. */
    @XmlAttribute(name = "groupIndex")
    public String groupIndex;

}

