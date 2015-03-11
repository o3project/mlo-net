/**
 * LinkInfoDto.java
 * (C) 2013, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.dto;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.o3project.mlo.server.logic.XmlIdAdapter;


/**
 * This class is the DTO class of link information.
 */
public class LinkInfoDto {
	
	/** Link ID. */
	@XmlElement(name = "LinkId")
	@XmlJavaTypeAdapter(XmlIdAdapter.class)
	public Integer id;
	
	/** Source packet transport node name. */
	@XmlElement(name = "SourcePTNodeName")
	public String srcPTNodeName;
	
	/** Source packet transport node ID. */
	@XmlElement(name = "SourcePTNodeId")
	@XmlJavaTypeAdapter(XmlIdAdapter.class)
	public Integer srcPTNodeId;
	
	/** Destination packet transport node name. */
	@XmlElement(name = "DestPTNodeName")
	public String dstPTNodeName;
	
	/** Destination packet transport node ID. */
	@XmlElement(name = "DestPTNodeId")
	@XmlJavaTypeAdapter(XmlIdAdapter.class)
	public Integer dstPTNodeId;
	
	/** Attributes of the link. */
	@XmlTransient
	public final Map<String, Object> attributes = new HashMap<String, Object>();
}
