/**
 * AbstractSliceOperationValidator.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.o3project.mlo.server.dto.RestifCommonDto;
import org.o3project.mlo.server.logic.ApiCallException;
import org.o3project.mlo.server.logic.Validator;


/**
 * This is the base class of {@link Validator}.
 */
public abstract class AbstractSliceOperationValidator implements Validator {

	/** Error detail prefix string of APICallError. */
	protected static final String ERROR_DETAIL_PREFIX = "BadRequest/InvalidData/";
	
	protected static final int MAX_LEN_8 = 8;
	
	protected static final int MAX_LEN_32 = 32;
	
	protected static final int MAX_FLOW_LEN = 200;
	
	protected static final int MAX_ID = 99999999;
	
	protected static final int MAX_VERSION = 255;

	protected static final int MAX_BAND_WIDTH = 99999999;
	
	protected static final int MAX_DELAY = 9999;
	
	/** The pattern of the 8-digit number */
	private Pattern fixedLength8Numeric = Pattern.compile("^\\d{8}$");

	/**
	 * Validates common header.
	 * @param dto Common header DTO.
	 * @throws ApiCallException Failed to validate.
	 */
	protected void validateCommon(RestifCommonDto dto) throws ApiCallException {
	
		if (dto == null) {
			throw new ApiCallException(ERROR_DETAIL_PREFIX + "CommonIsUndefined");
		}

		// Version:1～255
		checkNumRange(dto.version, 1, MAX_VERSION, "Version");

		// SourceComponent
		if (dto.srcComponent == null) {
			throw new ApiCallException(ERROR_DETAIL_PREFIX + "SourceComponent");
		} else {
			// SourceComponent_ComponentName: up to 32 bytes.
			checkLength(dto.srcComponent.name, MAX_LEN_32, "SourceComponent_ComponentName");
		}

		// DestComponent
		if (dto.dstComponent == null) {
			throw new ApiCallException(ERROR_DETAIL_PREFIX + "DestComponent");
		} else {
			// DestComponent_ComponentName: up to 32 bytes.
			checkLength(dto.dstComponent.name, MAX_LEN_32, "DestComponent_ComponentName");
		}
		
		// Operation:"Request"or"Responce"
		if (!"Request".equals(dto.operation) && !"Responce".equals(dto.operation)) {
			throw new ApiCallException(ERROR_DETAIL_PREFIX + "Operation");
		}
	}
	
	/**
	 * Validates the length of the string.
	 * @param target A validation target. 
	 * @param maxLength The maximum length.
	 * @param propName The property name.
	 * @throws ApiCallException Failed to validate.
	 */
	protected void checkLength(String target, int maxLength, String propName) throws ApiCallException {
		if (target == null || (target.getBytes().length == 0 || target.getBytes().length > maxLength)) {
			throw new ApiCallException(ERROR_DETAIL_PREFIX + propName);
		}
	}
	
	/**
	 * 数値項目の範囲チェック
	 * Validates numeric range.
	 * @param target the validation target.
	 * @param min the minimum value.
	 * @param max The maximum value.
	 * @param propName The property name.
	 * @throws ApiCallException Failed to validate.
	 */
	protected void checkNumRange(Integer target, int min, int max, String propName) throws ApiCallException {
		if (target == null || (target < min || target > max)) {
			throw new ApiCallException(ERROR_DETAIL_PREFIX + propName);
		}
	}
	
	/**
	 * Validates the 8-digit number.
	 * @param target The validation target.t
	 * @param propName The property name. 
	 * @throws ApiCallException Failed to validate.
	 */
	protected void checkFixedLength8Num(String target, String propName) throws ApiCallException {
	    Matcher m = fixedLength8Numeric.matcher(target);
		if (!m.matches()) {
			throw new ApiCallException(ERROR_DETAIL_PREFIX + propName);
		}
	}
}
