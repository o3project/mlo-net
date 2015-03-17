/**
 * SliceDto.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.o3project.mlo.server.logic.XmlIdAdapter;


/**
 * This class is the DTO class of slice.
 */
public class SliceDto {
	
	/** Slice name, named by user. */
	@XmlElement(name = "SliceName")
	public String name;
	
	/** Slice ID. */
	@XmlElement(name = "SliceId")
	@XmlJavaTypeAdapter(XmlIdAdapter.class)
	public Integer id;
	
	/** Flows. */
	@XmlElementWrapper(name = "Flows")
	@XmlElement(name = "Flow")
	public List<FlowDto> flows;

}
