/**
 * SdtncPathRouteDto.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.rpc.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * This class is the path route DTO for SDTNC.
 */
public class SdtncPathRouteDto {

	/** Line group working protection. */
    @XmlAttribute(name = "lineGroupWorkingProtection")
    public Integer lineGroupWorkingProtection;

	/** Tag of line. */
    @XmlElement(name = "line")
    public List<SdtncLineDto> line;

}

