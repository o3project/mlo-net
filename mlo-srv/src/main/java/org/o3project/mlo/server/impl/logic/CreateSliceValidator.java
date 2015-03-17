/**
 * CreateSliceValidator.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import org.o3project.mlo.server.dto.FlowDto;
import org.o3project.mlo.server.dto.RestifRequestDto;
import org.o3project.mlo.server.logic.ApiCallException;

/**
 * This is the implementation class of {@link org.o3project.mlo.server.logic.Validator}
 * which is used in creating slices.
 */
public class CreateSliceValidator extends AbstractSliceOperationValidator {
	
	@Override
	public void validate(RestifRequestDto dto) throws ApiCallException {

		// Common header part
		validateCommon(dto.common);

		// Slice part.
		if (dto.slice == null) {
			throw new ApiCallException(ERROR_DETAIL_PREFIX + "SliceIsUndefined");
		}
		
		// SliceName: up to 32 bytes.
		checkLength(dto.slice.name, MAX_LEN_32, "SliceName");

		// Slice part.
		if (dto.slice.flows == null) {
			throw new ApiCallException(ERROR_DETAIL_PREFIX + "FlowsIsUndefined");
		}
		
		// The number of flows [1:200]
		checkNumRange(dto.slice.flows.size(), 1, MAX_FLOW_LEN, "FlowsSizeOver");

		for (FlowDto flowDto : dto.slice.flows) {
			
			// FlowName: Up to 32 bytes.
			checkLength(flowDto.name, MAX_LEN_32, "FlowName");
			
			// SourceCENodeName: Up to 32 bytes.: 
			checkLength(flowDto.srcCENodeName, MAX_LEN_32, "SourceCENodeName");
			
			// SourceCEPortNo:  Up to 8 bytes. 
			//checkFixedLength8Num(flowDto.srcCEPortNo, "SourceCEPortNo");
			checkLength(flowDto.srcCEPortNo, MAX_LEN_8, "SourceCEPortNo");
			
			// DestCENodeName: up to 32 bytes.: 
			checkLength(flowDto.dstCENodeName, MAX_LEN_32, "DestCENodeName");
			
			// DestCEPortNo: Up to 8 bytes: 
			//checkFixedLength8Num(flowDto.dstCEPortNo, "DestCEPortNo");
			checkLength(flowDto.dstCEPortNo, MAX_LEN_8, "DestCEPortNo");
			
			// RequestBandWidth: Up to 8-digits.
			checkNumRange(flowDto.reqBandWidth, 0, MAX_BAND_WIDTH, "RequestBandWidth");
			
			// RequestDelay: Up to 4-digits.:
			checkNumRange(flowDto.reqDelay, 0, MAX_DELAY, "RequestDelay");
			
			// ProtectinonLevel:"0"or"1"
			if (!"0".equals(flowDto.protectionLevel) && !"1".equals(flowDto.protectionLevel)) {
				throw new ApiCallException(ERROR_DETAIL_PREFIX + "ProtectionLevel");
			}
		}
	}
}
