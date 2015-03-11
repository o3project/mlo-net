/**
 * UpdateSliceValidator.java
 * (C) 2013, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import org.o3project.mlo.server.dto.FlowDto;
import org.o3project.mlo.server.dto.RestifRequestDto;
import org.o3project.mlo.server.logic.ApiCallException;

/**
 * This class is the implementation class of {@link org.o3project.mlo.server.logic.Validator} interface.
 */
public class UpdateSliceValidator extends AbstractSliceOperationValidator {
	
	/** Request type: add */
	private static final String REQUEST_TYPE_ADD = "add";
	
	/** Request type: modify */
	private static final String REQUEST_TYPE_MOD = "mod";
	
	/** Request type: delete */
	private static final String REQUEST_TYPE_DEL = "del";

	@Override
	public void validate(RestifRequestDto dto) throws ApiCallException {

		// Validates common header.
		validateCommon(dto.common);

		// Slice part.
		if (dto.slice == null) {
			throw new ApiCallException(ERROR_DETAIL_PREFIX + "SliceIsUndefined");
		}
		
		// SliceId：Up to 8-digit.
		checkNumRange(dto.slice.id, 0, MAX_ID, "SliceId");

		// Slice part. 
		if (dto.slice.flows == null) {
			throw new ApiCallException(ERROR_DETAIL_PREFIX + "FlowsIsUndefined");
		}
		
		// The number of flows： between 1 and 200
		checkNumRange(dto.slice.flows.size(), 1, MAX_FLOW_LEN, "FlowsSizeOver");

		for (FlowDto flowDto : dto.slice.flows) {
			
			// RequestType:"add"or"mod"or"del"
			if (!REQUEST_TYPE_ADD.equals(flowDto.type) 
					&& !REQUEST_TYPE_MOD.equals(flowDto.type) 
					&& !REQUEST_TYPE_DEL.equals(flowDto.type)) {
				throw new ApiCallException(ERROR_DETAIL_PREFIX + "RequestType");
			}
			
			if (!REQUEST_TYPE_ADD.equals(flowDto.type)) {
				// FlowId： Up to 8-digit
				checkNumRange(flowDto.id, 0, MAX_ID, "FlowId");
			}
			
			if (!REQUEST_TYPE_DEL.equals(flowDto.type)) {

				// FlowName： Up to 32 bytes.
				checkLength(flowDto.name, MAX_LEN_32, "FlowName");
				
				// SourceCENodeName： Up to 32 bytes.可変英数字32byte	
				checkLength(flowDto.srcCENodeName, MAX_LEN_32, "SourceCENodeName");
				
				// SourceCEPortNo： Up to 8 bytes.	
				//checkFixedLength8Num(flowDto.srcCEPortNo, "SourceCEPortNo");
				checkLength(flowDto.srcCEPortNo, MAX_LEN_8, "SourceCEPortNo");
				
				// DestCENodeName： Up to 32 bytes.	
				checkLength(flowDto.dstCENodeName, MAX_LEN_32, "DestCENodeName");
				
				// DestCEPortNo： Up to 8 bytes.
				//checkFixedLength8Num(flowDto.dstCEPortNo, "DestCEPortNo");
				checkLength(flowDto.dstCEPortNo, MAX_LEN_8, "DestCEPortNo");

				// RequestBandWidth: 8-digits
				checkNumRange(flowDto.reqBandWidth, 0, MAX_BAND_WIDTH, "RequestBandWidth");
				
				// RequestDelay: 4-digits
				checkNumRange(flowDto.reqDelay, 0, MAX_DELAY, "RequestDelay");
				
				// ProtectinonLevel: "0"or"1"
				if (!"0".equals(flowDto.protectionLevel) && !"1".equals(flowDto.protectionLevel)) {
					throw new ApiCallException(ERROR_DETAIL_PREFIX + "ProtectionLevel");
				}
			}
		}
	}
}
