/**
 * SdtncQosDto.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.rpc.dto;

import javax.xml.bind.annotation.XmlElement;

/**
 * This class is the QoS DTO class for SDTNC.
 */
public class SdtncQosDto {

	/** QoS priority. */
    @XmlElement(name = "vQoSPriority")
    public Integer vQoSPriority;

	/** SLA mode. */
    @XmlElement(name = "vQoSSla")
    public Integer vQoSSla;

	/** QoS Pir. */
    @XmlElement(name = "vQoSPir")
    public Integer vQoSPir;

	/** QoS Cir. */
    @XmlElement(name = "vQoSCir")
    public Integer vQoSCir;

}

