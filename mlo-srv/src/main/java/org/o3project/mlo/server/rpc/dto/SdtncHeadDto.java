/**
 * SdtncHeadDto.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.rpc.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.o3project.mlo.server.logic.XmlIdAdapter;


/**
 * This class is the header DTO class of SDTNC.
 */
public class SdtncHeadDto {

	/** Sequence number. */
    @XmlElement(name = "sequenceNumber")
    @XmlJavaTypeAdapter(XmlIdAdapter.class)
    public Integer sequenceNumber;

    /** Time.  */
    @XmlElement(name = "time")
    public String time;

    /** Major response code.  */
    @XmlElement(name = "majorResponseCode")
    @XmlJavaTypeAdapter(XmlIdAdapter.class)
    public Integer majorResponseCode;

    /** Minor response code. */
    @XmlElement(name = "minorResponseCode")
    @XmlJavaTypeAdapter(XmlIdAdapter.class)
    public Integer minorResponseCode;

    /** Error details. */
    @XmlElement(name = "errorDetail")
    public String errorDetail;
}
