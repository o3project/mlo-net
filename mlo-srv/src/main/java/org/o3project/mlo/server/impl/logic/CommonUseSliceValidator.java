/**
 * CommonUseSliceValidator.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import org.o3project.mlo.server.dto.RestifRequestDto;
import org.o3project.mlo.server.logic.ApiCallException;

/**
 * This is the implementation class of {@link org.o3project.mlo.server.logic.Validator}.
 */
public class CommonUseSliceValidator extends AbstractSliceOperationValidator {

	@Override
	public void validate(RestifRequestDto dto) throws ApiCallException {

		// Common header.
		validateCommon(dto.common);

		// Slice part.
		if (dto.slice == null) {
			throw new ApiCallException(ERROR_DETAIL_PREFIX + "SliceIsUndefined");
		}
		
		// Slice ID.
		checkNumRange(dto.slice.id, 0, MAX_ID, "SliceId");
	}
}
