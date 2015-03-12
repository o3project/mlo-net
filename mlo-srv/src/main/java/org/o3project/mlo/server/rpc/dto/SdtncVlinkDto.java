/**
 * SdtncVlinkDto.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.rpc.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * This class is the VLINK DTO for SDTNC.
 */
public class SdtncVlinkDto {

	/** VLINK ID. */
    @XmlAttribute(name = "vObjectIndex")
    public String vObjectIndex;

	/** Name. */
    @XmlElement(name = "vObjectName")
    public String vObjectName;

	/** Object status. */
    @XmlElement(name = "vObjectStatus")
    public Integer vObjectStatus;

	/** Description. */
    @XmlElement(name = "vObjectDescription")
    public String vObjectDescription;

	/** Resource index. */
    @XmlElement(name = "resourceIndex")
    public String resourceIndex;

	/** Start point. */
    @XmlElement(name = "vLineSource")
    public String vLineSource;

	/** End point. */
    @XmlElement(name = "vLineSink")
    public String vLineSink;

}
