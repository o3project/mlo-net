/**
 * Validator.java
 * (C) 2013, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.logic;

import org.o3project.mlo.server.dto.RestifRequestDto;

/**
 * This interface designates the validation feature for MLO web API.
 */
public interface Validator {

	/**
	 * Validates the specified request DTO.
	 * @param dto the request DTO. 
	 * @throws ApiCallException Validation error. 
	 */
	void validate(RestifRequestDto dto) throws ApiCallException;
}
