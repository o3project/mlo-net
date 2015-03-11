/**
 * FlowDto.java
 * (C) 2013, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.o3project.mlo.server.logic.XmlIdAdapter;


/**
 * This class is the DTO class of flow.
 */
public class FlowDto {
	
	/** Request type. */
	@XmlElement(name = "RequestType")
	public String type;

	/** Flow name. */
	@XmlElement(name = "FlowName")
	public String name;

	/** Flow type name. */
	@XmlElement(name = "FlowTypeName")
	public String flowTypeName;

	/** ID. */
	@XmlElement(name = "FlowId")
	@XmlJavaTypeAdapter(XmlIdAdapter.class)
	public Integer id;
	
	/** Start edge CE node name of the flow. */
	@XmlElement(name = "SourceCENodeName")
	public String srcCENodeName;
	
	/** Start edge CE port of the flow. */
	@XmlElement(name = "SourceCEPortNo")
	public String srcCEPortNo;
	
	/** End edge CE node name of the flow. */
	@XmlElement(name = "DestCENodeName")
	public String dstCENodeName;
	
	/** End edge CE port of the flow. */
	@XmlElement(name = "DestCEPortNo")
	public String dstCEPortNo;
	
	/** Band width requested to the flow. The unit is Mbps. */
	@XmlElement(name = "RequestBandWidth")
	public Integer reqBandWidth;
	
	/** Delay requested to the flow. The unit is millisecond. */
	@XmlElement(name = "RequestDelay")
	public Integer reqDelay;
	
	/** Protection level, which always should be zero. */
	@XmlElement(name = "ProtectionLevel")
	public String protectionLevel;
	
	/** Start edge packet transport node name of the flow. */
	@XmlElement(name = "SourcePTNodeName")
	public String srcPTNodeName;
	
	/** Start edge packet transport node ID of the flow. */
	@XmlElement(name = "SourcePTNodeId")
	@XmlJavaTypeAdapter(XmlIdAdapter.class)
	public Integer srcPTNodeId;
	
	/** End edge packet transport node name of the flow. */
	@XmlElement(name = "DestPTNodeName")
	public String dstPTNodeName;
	
	/** End edge packet transport node ID of the flow. */
	@XmlElement(name = "DestPTNodeId")
	@XmlJavaTypeAdapter(XmlIdAdapter.class)
	public Integer dstPTNodeId;
	
	/** Used band width. The unit is Mbps. */
	@XmlElement(name = "UsedBandWidth")
	public Integer usedBandWidth;
	
	/** Used delay. The unit is milliseconds. */
	@XmlElement(name = "DelayTime")
	public Integer delayTime;
	
	/** The list string of relaying packet transport nodes. */
	@XmlElement(name = "UnderlayLogicalList")
	public String underlayLogicalList;
	
	/** The list string of endpoint packet transport nodes. */
	@XmlElement(name = "OverlayLogicalList")
	public String overlayLogicalList;
	
	/** Link information */
	@XmlElement(name = "LinkInfoList")
	public List<LinkInfoDto> linkInfoList;

	/** Attributes of the flow. */
	@XmlTransient
	public final Map<String, Object> attributes = new HashMap<String, Object>();
}
