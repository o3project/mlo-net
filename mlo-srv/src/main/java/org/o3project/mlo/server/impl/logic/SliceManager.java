/**
 * SliceManager.java
 * (C) 2013, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.o3project.mlo.server.dto.FlowDto;
import org.o3project.mlo.server.dto.RestifCommonDto;
import org.o3project.mlo.server.dto.RestifRequestDto;
import org.o3project.mlo.server.dto.RestifResponseDto;
import org.o3project.mlo.server.dto.SliceDto;
import org.o3project.mlo.server.logic.ApiCallException;
import org.o3project.mlo.server.logic.EquipmentConfigurator;
import org.o3project.mlo.server.logic.MloException;
import org.o3project.mlo.server.logic.NbiConstants;
import org.o3project.mlo.server.logic.OtherException;
import org.o3project.mlo.server.logic.TopologyConfigConstants;
import org.o3project.mlo.server.logic.TopologyRepository;
import org.seasar.framework.container.annotation.tiger.Binding;


/**
 * This class is the slice operating class.
 */
public class SliceManager implements NbiConstants {
	
	private static final Log LOG = LogFactory.getLog(SliceManager.class);
	
	@Binding
	private DebugRestifChecker debugRestifChecker;
	
	private TopologyRepository topologyRepository;

	private final SliceService sliceService = new SliceService();
	
	/**
	 * Setter method (for DI setter injection).
	 * @param debugRestifChecker The instance. 
	 */
	public void setDebugRestifChecker(DebugRestifChecker debugRestifChecker) {
		this.debugRestifChecker = debugRestifChecker;
	}
	
	/**
	 * Setter method (for DI setter injection).
	 * @param topologyRepository the topologyRepository to set
	 */
	public void setTopologyRepository(TopologyRepository topologyRepository) {
		this.topologyRepository = topologyRepository;
	}
			
	/**
	 * Creates a slice from {@link RestifRequestDto}.
	 * @param reqDto Request DTO. 
	 * @param equConf Equipment configurator class.
	 * @return　Response DTO.
	 * @throws MloException An MLO exception. 
	 */
	final RestifResponseDto createSlice(RestifRequestDto reqDto, EquipmentConfigurator equConf) throws MloException {
		RestifResponseDto resDto = null;
		try {
			// Processes
			resDto = createSliceInternal(reqDto, equConf);
		} catch (MloException e) {
			// In CREATE operation case, both SliceName and SliceID exists in response XML.
			String sliceName = e.getSliceName();
			String sliceId = e.getSliceId();
			if (sliceName == null) {
				sliceName = reqDto.slice.name;
			}
			if (sliceId == null) {
				sliceId = "";
			}
			e.setSliceInfo(sliceName, sliceId);
			throw e;
		}
		return resDto;
	}
	
	
	/**
	 * Updates a slice.
	 * @param reqDto Request DTO. 
	 * @param equConf Equipment configurator class.
	 * @return　Response DTO.
	 * @throws MloException An MLO exception. 
	 */
	final RestifResponseDto updateSlice(RestifRequestDto reqDto, EquipmentConfigurator equConf) throws MloException {
		RestifResponseDto resDto = null;
		try {
			// Processes
			resDto = updateSliceInternal(reqDto, equConf);
		} catch (MloException e) {
			String sliceName = null;
			String sliceId = e.getSliceId();
			if (e.getSliceId() == null) {
				sliceId = getIdString(reqDto.slice.id, "");
			}
			e.setSliceInfo(sliceName, sliceId);
			throw e;
		}
		return resDto;
	}

	/**
	 * Deletes a slice.
	 * @param reqDto Request DTO. 
	 * @param equConf Equipment configurator class.
	 * @return　Response DTO.
	 * @throws MloException An MLO exception. 
	 */
	final RestifResponseDto deleteSlice(RestifRequestDto reqDto, EquipmentConfigurator equConf) throws MloException {
		RestifResponseDto resDto = null;
		try {
			// Processes 
			resDto = deleteSliceInternal(reqDto, equConf);
		} catch (MloException e) {
			e.setSliceInfo(null, null);
			throw e;
		}
		return resDto;
	}

	/**
	 * Reads a slice by slice ID.
	 * @param reqDto Request DTO. 
	 * @return　Response DTO.
	 * @throws MloException An MLO exception. 
	 */
	final RestifResponseDto readSlice(RestifRequestDto reqDto) throws MloException {
		RestifResponseDto resDto = null;
		try {
			// Processes
			resDto = readSliceInternal(reqDto);
		} catch (MloException e) {
			e.setSliceInfo(null, null);
			throw e;
		}
		return resDto;
	}
	
	/**
	 * Gets slice list.
	 * @param paramMap Request parameters.
	 * @return resDto Response DTO.
	 */
	final RestifResponseDto getSlices(Map<String, String> paramMap) {
		return getSlicesInternal(paramMap);
	}

	/**
	 * Creates a slice.
	 * @param reqDto Request DTO. 
	 * @param equConf Equipment configurator class.
	 * @return　Response DTO.
	 * @throws MloException An MLO exception. 
	 */
	private RestifResponseDto createSliceInternal(RestifRequestDto reqDto, EquipmentConfigurator equConf) throws MloException {
		final String owner = reqDto.common.srcComponent.name;
		
		// Duplication check of name.
		SliceEntity duplicated = sliceService.findSliceEntity(reqDto.slice.name, owner); // Search by name.
		if (duplicated != null) {
			// Slice of same name already exists
			ApiCallException e = new ApiCallException("AlreadyCreated");
			e.setSliceInfo(reqDto.slice.name, getIdString(duplicated.getSliceDto().id, null));
			throw e;
		}
		
		// Generates dummy error if the request matches to some pattern (for debug)
		checkDummyErrorBefore(reqDto.slice, "Create");
		
		SliceDto slice = new SliceDto();
		FlowDto flow = null;
		slice.flows = new ArrayList<FlowDto>();
		String flowName = null;
		try{
			for (FlowDto reqFlow : reqDto.slice.flows) {
				flow = createFlow(reqFlow);
				flowName = reqFlow.name;
				if (flow == null) {
					ApiCallException e = new ApiCallException(
							"BadRequest/FlowNotFound/" + flowName);
					e.setSliceInfo(reqDto.slice.name, null);
					throw e;
				}
				flow.id = sliceService.getNewFlowId();
				
				// Configures to equipment.
				equConf.addFlow(flow, reqFlow, reqDto);
				slice.flows.add(flow);
			}
			slice.name = reqDto.slice.name;
			slice.id = sliceService.getNewSliceId();
		}catch(MloException e){
			if(slice.flows.size() < 1){
				throw e;
			}else{
				slice.name = reqDto.slice.name;
				slice.id = sliceService.getNewSliceId();
				sliceService.addSliceEntity(new SliceEntity(slice, reqDto.common.srcComponent.name));
				OtherException otherException = new OtherException("NoResource/FlowName=" + flowName);
				otherException.setSliceInfo(reqDto.slice.name, String.valueOf(slice.id));
				throw otherException;
			}
		}
		
		// Commits data.
		sliceService.addSliceEntity(new SliceEntity(slice, reqDto.common.srcComponent.name));
		
		// Generates dummy error if the request matches to some pattern (for debug)
		checkDummyErrorAfter(slice, "Create");
		
		// Creates response DTO. 
		RestifResponseDto resDto = new RestifResponseDto();
		// Sets common header DTO.
		resDto.common = createResponseCommonDto(reqDto.common);
		// Creates a slice DTO.
		// Only sets response.slice.name and response.slice.id.
		SliceDto resSliceDto = createSimplifiedSliceDto(slice);
		resDto.slices = Arrays.asList(resSliceDto);

		return  resDto;
	}
	
	/**
	 * Updates a slice.
	 * @param reqDto Request DTO. 
	 * @param equConf Equipment configurator class.
	 * @return　Response DTO.
	 * @throws MloException An MLO exception. 
	 */
	private RestifResponseDto updateSliceInternal(RestifRequestDto reqDto, EquipmentConfigurator equConf) throws MloException {
		final String owner = reqDto.common.srcComponent.name;
		
		// Checks slice already exists for now.
		SliceEntity sliceEntity = sliceService.findSliceEntity(reqDto.slice.id, owner);
		if (sliceEntity == null) {
			// If not exists, throws exception.
			ApiCallException e = new ApiCallException("SliceIdDoesNotExist");
			e.setSliceInfo(null, getIdString(reqDto.slice.id, null));
			throw e;
		}
		
		final SliceDto sliceDto = sliceEntity.getSliceDto();
		
		// Generates dummy error if the request matches to some pattern (for debug)
		checkDummyErrorBefore(sliceDto, "Update");

		// Changes sliceDto.flows
		for (FlowDto reqFlow : reqDto.slice.flows) {
			if ("add".equals(reqFlow.type)) {
				FlowDto newFlow = createFlow(reqFlow);
				if (newFlow == null) {
					ApiCallException e = new ApiCallException(
							"BadRequest/InvalidData/FlowCanNotBeCreated/" + reqFlow.name);
					e.setSliceInfo(null, getIdString(sliceDto.id, null));
					throw e;
				}
				newFlow.id = sliceService.getNewFlowId();
				
				// Configures to equipments.
				equConf.addFlow(newFlow, reqFlow, reqDto);
				
				sliceDto.flows.add(newFlow);
			} else if ("mod".equals(reqFlow.type)) {
			
				FlowDto flow = getFlowDto(sliceDto, reqFlow.id);
				if (flow == null) {
					ApiCallException e = new ApiCallException(
							"AlreadyModified/FlowIdNotFound/" + reqFlow.id);
					e.setSliceInfo(null, getIdString(sliceDto.id, null));
					throw e;
				}
				
				FlowDto newFlow = createFlow(reqFlow);
				
				if (newFlow == null) {
					ApiCallException e = new ApiCallException("BadRequest/InvalidData/FlowCanNotBeCreated/" + reqFlow.name);
					e.setSliceInfo(null, getIdString(sliceDto.id, null));
					throw e;
				}
				
				int flowIdx = sliceDto.flows.indexOf(flow);
				newFlow.id = reqFlow.id;
				
				// Configures to equipments.
				equConf.modFlow(newFlow, reqFlow, reqDto);
				
				sliceDto.flows.set(flowIdx, newFlow);
			} else if ("del".equals(reqFlow.type)) {
				FlowDto flow = getFlowDto(sliceDto, reqFlow.id);
				if (flow == null) {
					ApiCallException e = new ApiCallException(
							"AlreadyModified/FlowIdNotFound/" + reqFlow.id);
					e.setSliceInfo(null, getIdString(sliceDto.id, null));
					throw e;
				}
				int flowIdx = sliceDto.flows.indexOf(flow);
				
				// Configures to equipments.
				equConf.delFlow(flow, reqDto);
				
				sliceDto.flows.remove(flowIdx);
			} else {
				LOG.warn("Unexpected reqFlow.type, which is ignored: " + reqFlow.type);
			}
		}
		
		// Generates dummy error if the request matches to some pattern (for debug)
		checkDummyErrorAfter(sliceDto, "Update");
		
		// Creates response DTO.
		RestifResponseDto resDto = new RestifResponseDto();
		resDto.common = createResponseCommonDto(reqDto.common);
		SliceDto resSliceDto = createSimplifiedSliceDto(sliceDto);
		resSliceDto.name = null; // update では slice.name は返さない。
		resDto.slices = Arrays.asList(resSliceDto);
		
		return  resDto;
	}

	/**
	 * Deletes a slice. 
	 * @param reqDto Request DTO. 
	 * @param equConf Equipment configurator class.
	 * @return　Response DTO.
	 * @throws MloException An MLO exception. 
	 */
	private RestifResponseDto deleteSliceInternal(RestifRequestDto reqDto, EquipmentConfigurator equConf) throws MloException {
		final String owner = reqDto.common.srcComponent.name;
		
		// Checks the slice exists nor not by Slice.Id.
		SliceEntity sliceEntity = sliceService.findSliceEntity(reqDto.slice.id, owner);
		if (sliceEntity == null) {
			// If not exists, throws exception.
			ApiCallException e = new ApiCallException("AlreadyDeleted");
			e.setSliceInfo(reqDto.slice.name, getIdString(reqDto.slice.id, null));
			throw e;
		}
		
		final SliceDto sliceDto = sliceEntity.getSliceDto();
		
		// Generates dummy error if the request matches to some pattern (for debug)
		checkDummyErrorBefore(sliceDto, "Delete");
		
		// Configures to equipments. 
		for(FlowDto reqFlow : sliceDto.flows){
			equConf.delFlow(reqFlow, reqDto);
		}
		
		// Deletes slice from data.
		sliceService.removeSliceEntity(sliceEntity);
		
		// Generates dummy error if the request matches to some pattern (for debug)
		checkDummyErrorAfter(sliceDto, "Delete");

		// Creates response DTO.
		RestifResponseDto resDto = new RestifResponseDto();		
		// Creates common header.
		resDto.common = createResponseCommonDto(reqDto.common);
		// Prepares response slice data.
		SliceDto resSliceDto = new SliceDto();
		// Sets only ID in slice DTO.
		resSliceDto.id = sliceDto.id;
		resDto.slices = Arrays.asList(resSliceDto);
		return resDto;
	}

	/**
	 * Reads a slice.
	 * @param reqDto Request DTO. 
	 * @return　Response DTO.
	 * @throws MloException An MLO exception. 
	 */
	private RestifResponseDto readSliceInternal(RestifRequestDto reqDto) throws MloException {
		final String owner = reqDto.common.srcComponent.name;
		
		// Obtains saved slice data from reqpested slice ID.
		SliceEntity sliceEntity = sliceService.findSliceEntity(reqDto.slice.id, owner);
		if (sliceEntity == null) {
			// If not found, throws exception.
			ApiCallException e = new ApiCallException("NoData");
			e.setSliceInfo(reqDto.slice.name, getIdString(reqDto.slice.id, null));
			throw e;
		}
		
		final SliceDto sliceDto = sliceEntity.getSliceDto();
		
		// Generates dummy error if the request matches to some pattern (for debug)
		checkDummyErrorBefore(sliceDto, "Read");
		
		// Obtains saved lice data.
		SliceDto resSliceDto = copySliceDto(sliceDto);
		
		// Sets flowTypeName
		for (FlowDto flow : resSliceDto.flows) {
			Object oFlowTypeName = flow.attributes.get(TopologyConfigConstants.FLOW_ATTR_KEY_SDTNC_LINK_ID);
			if (oFlowTypeName != null) {
				flow.flowTypeName = oFlowTypeName.toString();
			}
		}
		
		// Generates dummy error if the request matches to some pattern (for debug)
		checkDummyErrorAfter(sliceDto, "Read");
		
		// Creates response DTO.
		RestifResponseDto resDto = new RestifResponseDto();
		// Creates common header. 
		resDto.common = createResponseCommonDto(reqDto.common);
		resDto.slices = Arrays.asList(resSliceDto);
		return resDto;
	}
	
	/**
	 * Obtains slice list.
	 * @param paramMap Requested query parameters. 
	 * @return Response DTO.
	 */
	private RestifResponseDto getSlicesInternal(Map<String, String> paramMap) {
		final String owner = paramMap.get(REQPARAM_KEY_OWNER);
		
		List<SliceEntity> sliceEntities = sliceService.findSliceEntities(owner);
		
		RestifResponseDto resDto = new RestifResponseDto();
		resDto.common = RestifCommonDto.createInstance(1, 
				DEFAULT_COMPONENT_NAME, owner,
				"Response");
		resDto.slices = new ArrayList<SliceDto>();
		for (SliceEntity sliceEntity : sliceEntities) {
			SliceDto resSliceDto = new SliceDto();
			resSliceDto.id = sliceEntity.getSliceDto().id;
			resSliceDto.name = sliceEntity.getSliceDto().name;
			resDto.slices.add(resSliceDto);
		}
		return resDto;
	}
	
	/**
	 * Obtains slice ID.
	 * @param slice Slice. 
	 * @return Slice ID.
	 */
	private String getIdString(Integer id, String defaultValue) {
		return (id != null) ? String.format("%08d", id) : defaultValue;
	}
	
	/**
	 * Creates flow from requested flow information.
	 * The name field is copied from requested data, but id and type fields are not.
	 * @param reqFlowDto Requested flow DTO. 
	 * @return Created flow DTO.
	 * @throws MloException Failure in processing. 
	 */
	private FlowDto createFlow(FlowDto reqFlowDto) throws MloException {
		LOG.info("InputData reqestBandWidth=" + reqFlowDto.reqBandWidth +", requestDelay=" + reqFlowDto.reqDelay);
		FlowDto tmpl = topologyRepository.queryFlowTemplate(reqFlowDto);
		FlowDto flow = null;
		if (tmpl != null) {
			flow = FlowDtoUtil.copyConcreteFlow(tmpl);
			flow.name = reqFlowDto.name;
		}
		return flow;
	}

	/**
	 * Copies the slice.
	 * @param sliceDto Original slice DTO.
	 * @return Copied slice DTO.
	 */
	private SliceDto copySliceDto(SliceDto sliceDto) {
		SliceDto dto = new SliceDto();
		dto.id = sliceDto.id;
		dto.name = sliceDto.name;
		dto.flows = new ArrayList<FlowDto>();
		for (FlowDto flowDto : sliceDto.flows) {
			dto.flows.add(FlowDtoUtil.copyConcreteFlow(flowDto));
		}
		return dto;
	}

	/**
	 * Creates common header DTO.
	 * @param reqCommonDto　
	 * @return　RestifCommonDto
	 * @throws ApiCallException
	 */
	private RestifCommonDto createResponseCommonDto(RestifCommonDto reqCommonDto) throws ApiCallException {
		if (reqCommonDto == null) {
			throw new ApiCallException("CommonIsNull");
		}
		if (!"mlo".equals(reqCommonDto.dstComponent.name)) {
			throw new ApiCallException("InvalidDstComponent");
		}
		return RestifCommonDto.createInstance(1, 
				"mlo",
				reqCommonDto.srcComponent.name,
				"Response");
	}
	
	/**
	 * Creates simplified slice DTO.
	 * New slice DTO has only name and id fields.
	 * @param sliceDto　
	 * @return　SliceDto
	 */
	private SliceDto createSimplifiedSliceDto(SliceDto sliceDto) {
		SliceDto dto = new SliceDto();
		dto.name = sliceDto.name;
		dto.id = sliceDto.id;
		if (sliceDto.flows != null) {
			dto.flows = new ArrayList<FlowDto>();
			FlowDto flowAppended = null;
			for (FlowDto flow : sliceDto.flows) {
				flowAppended = new FlowDto();
				flowAppended.id = flow.id;
				flowAppended.name = flow.name;
				dto.flows.add(flowAppended);
			}
		}
		return dto;
	}
	
	private FlowDto getFlowDto(SliceDto slice, int id) {
		FlowDto flowDto = null;
		if (slice != null && slice.flows != null) {
			for (FlowDto dto : slice.flows) {
				if (id == dto.id) {
					flowDto = dto;
					break;
				}
			}
		}
		return flowDto;
	}
	
	/**
	 * Generates dummy exception for debug if slice matches specified condition.
	 * @param slice Slice DTO
	 * @param opeName Operation name (Create/Update/Delete/Read)
	 * @throws MloException Dummy exception.
	 */
	private void checkDummyErrorBefore(SliceDto slice, String opeName) throws MloException {
		if (debugRestifChecker != null) {
			debugRestifChecker.checkBefore(slice.name, opeName);
		}
	}
	
	/**
	 * Generates dummy exception for debug if slice matches specified condition.
	 * @param slice Slice DTO
	 * @param opeName Operation name (Create/Update/Delete/Read)
	 * @throws MloException Dummy exception.
	 */
	private void checkDummyErrorAfter(SliceDto slice, String opeName) throws MloException {
		if (debugRestifChecker != null) {
			debugRestifChecker.checkAfter(slice.name, opeName);
		}
	}
	
}
