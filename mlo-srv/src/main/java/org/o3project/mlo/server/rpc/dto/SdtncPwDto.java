/**
 * SdtncPwDto.java
 * (C) 2013, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.rpc.dto;

import javax.xml.bind.annotation.XmlElement;

/**
 * This class is the PW DTO class for SDTNC.
 */
public class SdtncPwDto {

	/** Downstream PW label value. */
    @XmlElement(name = "vPwDownLabel")
    public String vPwDownLabel;

	/** Upstream PW label value. */
    @XmlElement(name = "vPwUpLabel")
    public String vPwUpLabel;

}

