/**
 * SdtncVpathDto.java
 * (C) 2013, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.rpc.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * This class is the path DTO for SDTNC.
 */
public class SdtncVpathDto {

	/** Path ID. */
    @XmlAttribute(name = "vObjectIndex")
    public String vObjectIndex;

	/** Path name. */
    @XmlElement(name = "vObjectName")
    public String vObjectName;

	/** Status. */
    @XmlElement(name = "vObjectStatus")
    public Integer vObjectStatus;

	/** Description. */
    @XmlElement(name = "vObjectDescription")
    public String vObjectDescription;

	/** Resource ID. */
    @XmlAttribute(name = "resourceIndex")
    public String resourceIndex;

	/** Path route. */
    @XmlElement(name = "pathRoute")
    public List<SdtncPathRouteDto> pathRoute;

	/** Path information. */
    @XmlElement(name = "pathInfo")
    public SdtncPathInfoDto pathInfo;

	/** Start/end point. */
    @XmlElement(name = "pathEndPoint")
    public List<SdtncPathEndPointDto> pathEndPoint;

	/** LSP. */
    @XmlElement(name = "lsp")
    public SdtncLspDto lsp;

	/** PW. */
    @XmlElement(name = "pw")
    public SdtncPwDto pw;

	/** QoS. */
    @XmlElement(name = "qos")
    public SdtncQosDto qos;

	/** VLAN. */
    @XmlElement(name = "vlan")
    public SdtncVlanDto vlan;

}

