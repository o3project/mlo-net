/**
 * SdtncValidator.java
 * (C) 2013, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.impl.rpc.service;

import org.o3project.mlo.server.logic.InternalException;
import org.o3project.mlo.server.rpc.dto.SdtncResponseDto;
import org.o3project.mlo.server.rpc.dto.SdtncVlinkDto;
import org.o3project.mlo.server.rpc.dto.SdtncVpathDto;

/**
 * SdtncValidator
 */
public class SdtncValidator {
	
	private static final Integer MAX_LEN_32 = 32;
	
	private static final Integer MAX_LEN_64 = 64;
	
	private static final Integer MAX_LEN_512 = 512;
	
	private static final Integer MAX_VOBJECT_STATUS = 4;
	
	/** detail part in APICallError. */
	private static final String ERROR_DETAIL_PREFIX = "SDTNCBadResponse/InvalidData/";
	
	private SdtncValidator() {}

    /**
     * Checks null or not.
     * @param target Target object.
     * @param propName Property name.
     * @throws InternalException Validation error.
     */
    public static void checkNull(Object target, String propName) throws InternalException {
        if (target == null) {
            throw new InternalException(ERROR_DETAIL_PREFIX + propName);
        }
    }

    /**
     * Checks the length of string.
     * @param target Target object.
     * @param maxLength Maximum length.
     * @param propName Property name.
     * @throws InternalException Validation error.
     */
    public static void checkLength(String target, int maxLength, String propName) throws InternalException {
        if (target == null || (target.getBytes().length == 0 || target.getBytes().length > maxLength)) {
            throw new InternalException(ERROR_DETAIL_PREFIX + propName);
        }
    }

    /**
     * Checks the range of the number.
     * @param target Target object.
     * @param min The minimum number. 
     * @param max The maximum number.
     * @param propName Property name.
     * @throws InternalException Validation error.
     */
    public static void checkNumRange(Integer target, int min, int max, String propName) throws InternalException {
        if (target == null || (target < min || target > max)) {
            throw new InternalException(ERROR_DETAIL_PREFIX + propName);
        }
    }

    /**
     * Validates the login response.
     * @param res The response DTO. 
     * @throws InternalException Validation error.
     */
    static void validateLogin(SdtncResponseDto res) throws InternalException {

        //  Login login()
        //  root notNull
        //  root.head notNull
        //  root.auth notNull
        //  root.login notNull
        //  root.login.loginId SIZE[0..32]
        //  root.login.loginPassword SIZE[0..32]
        //  root.login.ipAddress SIZE[0..32]
        //  root.login.accountName SIZE[0..32]
        //  root.login.accountLoginDate SIZE[0..32]
        //  root.login.accountTimeZone SIZE[0..32]
        SdtncValidator.checkNull(res, "res");
        SdtncValidator.checkNull(res.head, "head");
        
        if (res.auth == null) {
        	throw new InternalException(ERROR_DETAIL_PREFIX + "Login failed : majorResponseCode=" + res.head.majorResponseCode 
                    + ", minorResponseCode=" + res.head.minorResponseCode
                    + ", errorDetail=" + res.head.errorDetail);
        }
        
        if (res.auth.token == null || (res.auth.token.getBytes().length == 0 || res.auth.token.getBytes().length > MAX_LEN_32)) {
            throw new InternalException(ERROR_DETAIL_PREFIX + "Login failed : majorResponseCode=" + res.head.majorResponseCode
                                       + ", minorResponseCode=" + res.head.minorResponseCode
                                       + ", errorDetail=" + res.head.errorDetail);
        }
        
        SdtncValidator.checkNull(res.login, "login");
        SdtncValidator.checkLength(res.login.loginId, MAX_LEN_32, "loginId");
        SdtncValidator.checkLength(res.login.loginPassword, MAX_LEN_32, "loginPassword");
        SdtncValidator.checkLength(res.login.ipAddress, MAX_LEN_32, "ipAddress");
        SdtncValidator.checkLength(res.login.accountName, MAX_LEN_32, "accountName");
        SdtncValidator.checkLength(res.login.accountLoginDate, MAX_LEN_32, "accountLoginDate");
        SdtncValidator.checkLength(res.login.accountTimeZone, MAX_LEN_32, "accountTimeZone");
        SdtncValidator.checkNull(res.login.accountLoginDate, "accountLoginDate");
    }

    /**
     * Validates the logout response.
     * @param res The response DTO. 
     * @throws InternalException Validation error. 
     */
    static void validateLogout(SdtncResponseDto res) throws InternalException {

        //  Logout logout()
        //  root notNull
        //  root.head notNull
        //  root.login notNull
        //  root.login.loginId SIZE[0..32]
        SdtncValidator.checkNull(res, "res");
        SdtncValidator.checkNull(res.head, "head");
        SdtncValidator.checkNull(res.login, "login");
        SdtncValidator.checkLength(res.login.loginId, MAX_LEN_32, "loginId");
    }

    /**
     * Validates the GET_LINK response.
     * @param res The response DTO.
     * @throws InternalException Validation error.
     */
    static void validateGetLspResource(SdtncResponseDto res) throws InternalException {

        //  GET_LINK getLspResource()
        //  root notNull
        //  root.head notNull
        //  root.slice notNull
        //  root.slice.groupIndex SIZE[0..64]
        //  root.vlink notNull
        //  root.vlink.vObjectIndex SIZE[0..64]
        //  root.vlink.vObjectName SIZE[0..32]
        //  root.vlink.vObjectStatus 0-4
        //  root.vlink.vObjectDescription SIZE[0..512]
        //  root.vlink.resourceIndex SIZE[0..64]
        //  root.vlink.vLineSource SIZE[0..64]
        //  root.vlink.vLineSink SIZE[0..64]
        SdtncValidator.checkNull(res, "res");
        SdtncValidator.checkNull(res.head, "head");
        SdtncValidator.checkNull(res.slice, "slice");
        SdtncValidator.checkLength(res.slice.groupIndex, MAX_LEN_64, "groupIndex");
        SdtncValidator.checkNull(res.vlink, "vlink");
        for(SdtncVlinkDto vlinkDto : res.vlink){
            SdtncValidator.checkLength(vlinkDto.vObjectIndex, MAX_LEN_64, "vObjectIndex");
            SdtncValidator.checkLength(vlinkDto.vObjectName, MAX_LEN_32, "vObjectName");
            SdtncValidator.checkNumRange(vlinkDto.vObjectStatus, 0, MAX_VOBJECT_STATUS, "vObjectStatus");
            SdtncValidator.checkLength(vlinkDto.vObjectDescription, MAX_LEN_512, "vObjectDescription");
            SdtncValidator.checkLength(vlinkDto.resourceIndex, MAX_LEN_64, "resourceIndex");
            SdtncValidator.checkLength(vlinkDto.vLineSource, MAX_LEN_64, "vLineSource");
            SdtncValidator.checkLength(vlinkDto.vLineSink, MAX_LEN_64, "vLineSink");
        }
    }

    /**
     * Validates POST_PW response.
     * @param res The response DTO.
     * @throws InternalException Validation error.
     */
    static void validateCreatePw(SdtncResponseDto res) throws InternalException {

        //  POST_PW createPw()
        //  root notNull
        //  root.head notNull
        //  root.vpath notNull
        //  root.vpath.vObjectIndex SIZE[0..64]
        SdtncValidator.checkNull(res, "res");
        SdtncValidator.checkNull(res.head, "head");
        
        if (res.vpath == null) {
            throw new InternalException(ERROR_DETAIL_PREFIX + "Creation of PW failed.");
        }

        for(SdtncVpathDto vpathDto : res.vpath){
            if (vpathDto.vObjectIndex == null || (vpathDto.vObjectIndex.getBytes().length == 0 || vpathDto.vObjectIndex.getBytes().length > MAX_LEN_64)) {
                throw new InternalException(ERROR_DETAIL_PREFIX + "Creation of PW failed.");
            }
        }
    }

    /**
     * Validates the DELETE_PW response.
     * @param res The response DTO.
     * @throws InternalException Validation error.
     */
    static void validateDeletePw(SdtncResponseDto res) throws InternalException {

        //  DELETE_PW deletePw()
        //  root notNull
        //  root.head notNull
        //  root.vpath notNull
        //  root.vpath.vObjectIndex SIZE[0..64]
        //  root.vpath.resourceIndex SIZE[0-64]
        SdtncValidator.checkNull(res, "res");
        SdtncValidator.checkNull(res.head, "head");
        SdtncValidator.checkNull(res.vpath, "vpath");
        for(SdtncVpathDto vpathDto : res.vpath){
            SdtncValidator.checkLength(vpathDto.vObjectIndex, MAX_LEN_64, "vObjectIndex");
        }
    }

    /**
     * Validates the GET_PW response.
     * @param res The response DTO.
     * @throws InternalException Validation error.n
     */
    static void validateGetPw(SdtncResponseDto res) throws InternalException {

        //  GET_PW getPw()
        //    root notNull
        //    root.head notNull
        //    root.slice notNull
        //    root.slice.groupIndex SIZE[0..64]
        //    root.vpath notNull
        //    root.vpath.vObjectIndex SIZE[0..64]
        //    root.vpath.vObjectName SIZE[0..32]
        //    root.vpath.vObjectStatus SIZE[0..4]
        //    root.vpath.vObjectDescription SIZE[0..512]
        //    root.vpath.resourceIndex SIZE[0..64]
        //    root.vpath.pathRoute notNull
        //    root.vpath.pathRoute.lineGroupWorkingProtection 1-2
        //    root.vpath.pathRoute.line notNull
        //    root.vpath.pathRoute.line.elementIndex SIZE(6)
        //    root.vpath.pathRoute.line.lineSequence 1-n
        //    root.vpath.vlan.uni.vVlanVLanIdNniClient notNULL
        SdtncValidator.checkNull(res, "res");
        SdtncValidator.checkNull(res.head, "head");
        SdtncValidator.checkNull(res.slice, "slice");
        SdtncValidator.checkLength(res.slice.groupIndex, MAX_LEN_64, "groupIndex");
        SdtncValidator.checkNull(res.vpath, "vpath");
        for (SdtncVpathDto vpathDto : res.vpath){
            SdtncValidator.checkLength(vpathDto.vObjectIndex, MAX_LEN_64, "vObjectIndex");
            SdtncValidator.checkLength(vpathDto.vObjectName, MAX_LEN_32, "vObjectName");
            SdtncValidator.checkNumRange(vpathDto.vObjectStatus, 0, MAX_VOBJECT_STATUS, "vObjectStatus");
            SdtncValidator.checkLength(vpathDto.vObjectDescription, MAX_LEN_512, "vObjectDescription");
            SdtncValidator.checkLength(vpathDto.resourceIndex, MAX_LEN_64, "resourceIndex");
            SdtncValidator.checkNull(vpathDto.vlan, "vlan");
            SdtncValidator.checkNull(vpathDto.vlan.uni, "uni");
            SdtncValidator.checkNull(vpathDto.vlan.uni.vVlanVLanIdNniClient, "vVlanVLanIdNniClient");
        }
    }
}
