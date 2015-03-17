/**
 * {@link EquipmentConfiguratorOptDeviceImpl}.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.o3project.mlo.server.dto.FlowDto;
import org.o3project.mlo.server.dto.LinkInfoDto;
import org.o3project.mlo.server.dto.RestifRequestDto;
import org.o3project.mlo.server.impl.rpc.service.OdenOSTopology;
import org.o3project.mlo.server.logic.ConfigProvider;
import org.o3project.mlo.server.logic.EquipmentConfigurator;
import org.o3project.mlo.server.logic.MloException;
import org.o3project.mlo.server.logic.TopologyConfigConstants;
import org.o3project.mlo.server.rpc.entity.PTFlowEntity;
import org.o3project.mlo.server.rpc.entity.PTLinkEntity;
import org.o3project.mlo.server.rpc.service.OdenOSAdapterService;
import org.o3project.mlo.server.rpc.service.SdtncConstants;
import org.seasar.framework.container.annotation.tiger.Aspect;


/**
 * This class is the implementation class of {@link EquipmentConfigurator} instance for OPT device via Odenos.
 */
@Aspect("traceInterceptor")
public class EquipmentConfiguratorOptDeviceImpl implements EquipmentConfigurator, SdtncConstants, TopologyConfigConstants {
	
	private static final Log LOG = LogFactory.getLog(EquipmentConfiguratorOptDeviceImpl.class);
	
	private HashMap<Integer, List<String>> flowIdMap = new HashMap<Integer, List<String>>();
	
	private ConfigProvider configProvider;
	
	// odenOSAdapter
	private OdenOSAdapterService odenOSAdapterService;
	
	private final OdenOSTopology odenOSTopology = new OdenOSTopology();
	
	/**
	 * Setter method (for DI setter injection).
	 * @param configProvider the configProvider to set
	 */
	public void setConfigProvider(ConfigProvider configProvider) {
		this.configProvider = configProvider;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.EquipmentConfigurator#addFlow(org.o3project.mlo.server.dto.FlowDto)
	 */
	@Override
	public void addFlow(FlowDto newFlowDto, FlowDto reqFlowDto, RestifRequestDto restifRequestDto) throws MloException {
		// Requests to put link.
		requestOdenOSLink(newFlowDto);

		// Requests to put flow.
		putOdenOSFlow(newFlowDto, reqFlowDto);
	}

	/*
	 * (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.EquipmentConfigurator#delFlow(org.o3project.mlo.server.dto.FlowDto)
	 */
	@Override
	public void delFlow(FlowDto deletedFlowDto, RestifRequestDto restifRequestDto) throws MloException {

		// Obtains odenos flow ID from flow ID.
		List<String> idList = getFlowId(deletedFlowDto.id);
		
		if(null != idList){
			for (String id : idList) {
				// Deletes flow.
				odenOSAdapterService.deleteFlow(id);
			}
			// Deletes flow ID from ID map.
			deleteFlowId(deletedFlowDto.id);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.EquipmentConfigurator#modFlow(org.o3project.mlo.server.dto.FlowDto)
	 */
	@Override
	public void modFlow(FlowDto newFlowDto, FlowDto reqFlowDto, RestifRequestDto restifRequestDto) throws MloException {
		// Requests to put link.
		requestOdenOSLink(newFlowDto);

		// Requests to put flow.
		putOdenOSFlow(newFlowDto, reqFlowDto);
	}	

	/**
	 * Requests to put odenos link.
	 * @param newFlowDto New flow DTO.
	 * @throws MloException Failed in processing.
	 */
	void requestOdenOSLink(FlowDto newFlowDto) throws MloException {
		FlowDto fwFlowDto = newFlowDto;
		requestOdenOSLink(fwFlowDto.linkInfoList);
		
		FlowDto bwFlowDto = getBackwardFlowDto(fwFlowDto);
		if (bwFlowDto != null) {
			requestOdenOSLink(bwFlowDto.linkInfoList);
		}
	}		

	/**
	 * Puts or updates odenos flow.
	 * @param newFlowDto New flow DTO. 
	 * @param reqFlowDto Requested flow DTO. 
	 * @throws MloException Failure in processing. For example, odenos task is not run.
	 */
	void putOdenOSFlow(FlowDto newFlowDto, FlowDto reqFlowDto) throws MloException {
		
		// Creates flow information for OdenOS (Forward direction)
		FlowDto fwFlowDto = newFlowDto;
		PTFlowEntity fwFlowEntity = getPTFlowEntity(fwFlowDto, reqFlowDto);
		
		// Creates flow information for OdenOS (Backward direction)
		FlowDto bwFlowDto = getBackwardFlowDto(fwFlowDto);
		PTFlowEntity bwFlowEntity = getPTFlowEntity(bwFlowDto, reqFlowDto);

		String fwOdenOSFlowId = null;
		if (fwFlowEntity != null) {
			// Puts flow of forward direction.
			fwFlowEntity.flowId = getOdenOSFlowId(newFlowDto, 1);
			fwOdenOSFlowId = odenOSAdapterService.putFlow(fwFlowEntity);
			// Registers to flow ID map.
			addFlowId(newFlowDto.id, fwOdenOSFlowId, null);
		} else {
			LOG.debug("OdenOS putFlow skipped. No flow entity. Forward direction flow of flowDto.id = " + fwFlowDto.id);
		}
		
		String bwOdenOSFlowId = null;
		if (bwFlowEntity != null) {
			// Puts flow of backward direction.
			bwFlowEntity.flowId = getOdenOSFlowId(newFlowDto, 2);
			bwOdenOSFlowId = odenOSAdapterService.putFlow(bwFlowEntity);
			// Registers to flow ID map.
			addFlowId(newFlowDto.id, fwOdenOSFlowId, bwOdenOSFlowId);
		} else {
			LOG.debug("OdenOS putFlow skipped. No flow entity. Backward direction flow of flowDto.id = " + fwFlowDto.id);
		}
	}
	
	void requestOdenOSLink(List<LinkInfoDto> linkInfoDtoList) throws MloException {
		for(LinkInfoDto linkInfoDto : linkInfoDtoList){
			PTLinkEntity linkEntity = createPTLinkEntity(linkInfoDto);
			
			if (linkEntity != null) {
				PTLinkEntity currentLinkEntity = odenOSAdapterService.getLink(linkEntity.linkId);
				if (currentLinkEntity == null) {
					odenOSAdapterService.requestLink(linkEntity);
				} else {
					LOG.info("Link already exists. linkId = " + linkEntity.linkId);
				}
			} else {
				LOG.debug("OdenOS getLink skipped. No link entity.");
			}
			
		}
	}

	/**
	 * Obtains flow ID for odenos.
	 * @param dto Flow DTO.
	 * @param flowSerial 1:Forward-direction, 2:Backward-direction.
	 * @return Flow ID. 
	 */
	private String getOdenOSFlowId(FlowDto dto, int flowSerial) {
		// The format of odenos flow ID is "<flow name>_n", 
		// which n is 1 for forward direction and 2 for backward direction.
		return String.format("%s_%d", dto.name, flowSerial);
	}
	
	/**
	 * Crates flow entity. 
	 * @param newFlowDto New flow DTO. 
	 * @param reqFlowDto Requested flow DTO.
	 * @return Flow entity.
	 */
	private PTFlowEntity getPTFlowEntity(FlowDto newFlowDto, FlowDto reqFlowDto){
		Object flowName = newFlowDto.attributes.get(FLOW_ATTR_KEY_FLOW_NAME);
		Integer bandWidth = reqFlowDto.reqBandWidth;
		Integer delay = reqFlowDto.reqDelay;
		return odenOSTopology.createPTFlowEntity(flowName, bandWidth, delay, configProvider);
	}
	
	/**
	 * Appends odenos flow ID to map.
	 * @param flowId Flow ID.
	 * @param odenOsFlowId1 Forward-direction flow ID.
	 * @param odenOsFlowId2 Backward-direction flow ID.
	 */
	public void addFlowId(int flowId, String odenOsFlowId1, String odenOsFlowId2){
		List<String> list = new ArrayList<String>();
		if (null != odenOsFlowId1) {
			list.add(odenOsFlowId1);
		}
		if (null != odenOsFlowId2) {
			list.add(odenOsFlowId2);
		}
		flowIdMap.put(flowId, list);
	}
	
	/**
	 * Obtains flow ID from map.
	 * @param flowId Flow ID. 
	 * @return Odenos flow ID.
	 */
	public List<String> getFlowId(int flowId){
		return flowIdMap.get(flowId);
	}
	
	/**
	 * Deletes flow ID from map.
	 * @param flowId Flow ID.
	 */
	public void deleteFlowId(int flowId){
		flowIdMap.remove(flowId);
	}
	
	/**
	 * Checks whether the flow ID exists or not.
	 * @param flowId Flow ID. 
	 * @return True if exists.
	 */
	public boolean isFlowId(int flowId){
		return flowIdMap.containsKey(flowId);
	}
	
	/**
	 * Setter method (for DI setter injection).
	 * @param odenOSAdapterService The instance. 
	 */
	public void setOdenOSAdapterService(OdenOSAdapterService odenOSAdapterService){
		this.odenOSAdapterService = odenOSAdapterService;
	}
	
	/**
	 * Obtains Odenos adapter service.
	 * @return odenOSAdapterService
	 */
	public OdenOSAdapterService getOdenOSAdapterService(){
		return odenOSAdapterService;
	}
	
	private FlowDto getBackwardFlowDto(FlowDto flowDto) {
		FlowDto bwFlowDto = null;
		String reverseKey = FLOW_ATTR_KEY_REVERSE;
		if (flowDto.attributes.containsKey(reverseKey)) {
			bwFlowDto = (FlowDto) flowDto.attributes.get(reverseKey);
		}
		return bwFlowDto;
	}

	/**
	 * @param linkInfoDto
	 * @return
	 */
	private PTLinkEntity createPTLinkEntity(LinkInfoDto linkInfoDto) {
		Object linkName = linkInfoDto.attributes.get(LINK_ATTR_KEY_LINK_NAME);
		Object bandWidth = linkInfoDto.attributes.get(LINK_ATTR_KEY_BAND_WIDTH);
		Object delay = linkInfoDto.attributes.get(LINK_ATTR_KEY_DELAY);
		LOG.debug(String.format("creating PT link entity. (linkName, bandWidth, delay) = (%s, %s, %s)", linkName, bandWidth, delay));
		return odenOSTopology.createPTLinkEntity(linkName, bandWidth, delay, configProvider);
	}		
}
