/**
 * FlowDtoUtil.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import java.util.ArrayList;
import java.util.List;


import org.o3project.mlo.server.dto.FlowDto;
import org.o3project.mlo.server.dto.LinkInfoDto;
import org.o3project.mlo.server.logic.TopologyConfigConstants;
import org.seasar.framework.beans.util.Beans;

/**
 * This class is the utility class for {@link FlowDto}.
 *
 */
public final class FlowDtoUtil implements TopologyConfigConstants {
	/**
	 * A constructor.
	 */
	private FlowDtoUtil() {
		super();
	}
	
	/**
	 * Creates a link info instance.
	 * @param id Link ID.
	 * @param srcNodeName start-edge PT node name.
	 * @param srcNodeId start-edge PT node ID.
	 * @param dstNodeName end-edge PT node name.
	 * @param dstNodeId end-edge PT node ID.
	 * @return Link info.
	 */
	public static LinkInfoDto createLinkInfo(Integer id, String srcNodeName, Integer srcNodeId, String dstNodeName, Integer dstNodeId) {
		LinkInfoDto link = new LinkInfoDto();
		link.id = id;
		link.srcPTNodeName = srcNodeName;
		link.srcPTNodeId = srcNodeId;
		link.dstPTNodeName = dstNodeName;
		link.dstPTNodeId = dstNodeId;
		return link;
	}

	/**
	 * Copies {@link FlowDto} instance.
	 * Requested parameter and type are not copied.
	 * @param flowDto Original flow DTO. 
	 * @return Copied flow DTO. 
	 */
	public static FlowDto copyConcreteFlow(FlowDto flowDto) {
		FlowDto fd = createConcreteFlow(flowDto.linkInfoList, flowDto.usedBandWidth, flowDto.delayTime);
		fd.attributes.putAll(flowDto.attributes);
		fd.name = flowDto.name;
		fd.id = flowDto.id;
		return fd;
	}

	/**
	 * Creates an instance of {@link FlowDto}.
	 * Requested parameters, id, name and type are not copied.
	 * @param links Link info list. 
	 * @param width Band width of this flow.
	 * @param delay Delay of this flow.
	 * @return An intance.
	 */
	public static FlowDto createConcreteFlow(List<LinkInfoDto> links, int width, int delay) {
		FlowDto flow = null;
		int lastIdx = links.size() - 1;
		if (lastIdx > -1) {
			flow = new FlowDto();
			flow.usedBandWidth = width;
			flow.delayTime = delay;
			flow.srcPTNodeName = links.get(0).srcPTNodeName;
			flow.srcPTNodeId = links.get(0).srcPTNodeId;
			flow.dstPTNodeName = links.get(lastIdx).dstPTNodeName;
			flow.dstPTNodeId = links.get(lastIdx).dstPTNodeId;
			flow.overlayLogicalList = String.format("%08d_%08d", flow.srcPTNodeId, flow.dstPTNodeId);
			flow.underlayLogicalList = String.format("%08d", flow.srcPTNodeId);
			flow.linkInfoList = new ArrayList<LinkInfoDto>();
			for (LinkInfoDto lid : links) {
				flow.underlayLogicalList = String.format("%s_%08d", flow.underlayLogicalList, lid.dstPTNodeId);
				LinkInfoDto newLinkInfoDto = Beans.createAndCopy(LinkInfoDto.class, lid).execute();
				newLinkInfoDto.attributes.putAll(lid.attributes);
				flow.linkInfoList.add(newLinkInfoDto);
			}
		} else {
			flow = new FlowDto();
			flow.usedBandWidth = width;
			flow.delayTime = delay;
			flow.srcPTNodeName = null;
			flow.srcPTNodeId = null;
			flow.dstPTNodeName = null;
			flow.dstPTNodeId = null;
			flow.overlayLogicalList = "";
			flow.underlayLogicalList = "";
			flow.linkInfoList = new ArrayList<LinkInfoDto>();
		}
		return flow;
	}
}
