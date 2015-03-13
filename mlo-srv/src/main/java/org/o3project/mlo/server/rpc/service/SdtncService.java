/**
 * SdtncService.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.rpc.service;

import java.util.Map;

import org.o3project.mlo.server.logic.MloException;
import org.o3project.mlo.server.rpc.dto.SdtncRequestDto;
import org.o3project.mlo.server.rpc.dto.SdtncResponseDto;


/**
 * This interface designates the adapter feature of SDTNC.
 */
public interface SdtncService {

    /**
     * Does login to SDTNC.
     * This method calls LOGIN interface of SDTNC.
     * @param reqDto the request DTO.
     * @return the response DTO.
     * @throws MloException Failed to access to SDTNC.
     */
    SdtncResponseDto login(SdtncRequestDto reqDto) throws MloException;

    /**
     * Does logout from SDTNC.
     * This method calls LOGOUT interface of SDTNC.
     * @param reqDto the request DTO.
     * @return the response DTO.
     * @throws MloException Failed to access to SDTNC.
     */
    SdtncResponseDto logout(SdtncRequestDto reqDto) throws MloException;

    /**
     * Obtains link (LSP resource).
     * This method calls GET_LINK interface of SDTNC.
     * @param paramMap query parameter.
     * @return the response DTO.
     * @throws MloException Failed to access to SDTNC.
     */
    SdtncResponseDto getLspResource(
            Map<String, String> paramMap) throws MloException;

    /**
     * Creates path (PW).
     * This method calls PUT_PATH interface of SDTNC.
     * @param reqDto the request DTO.
     * @return the response DTO.
     * @throws MloException Failed to access to SDTNC.
     */
    SdtncResponseDto createPw(SdtncRequestDto reqDto) throws MloException;

    /**
     * Deletes path (PW).
     * This method calls DELETE_PATH interface of SDTNC.
     * @param paramMap query parameter.
     * @return the response DTO.
     * @throws MloException Failed to access to SDTNC.
     */
    SdtncResponseDto deletePw(Map<String, String> paramMap) throws MloException;

    /**
     * Obtains path (PW).
     * This method calls GET_PATH interface of SDTNC.
     * @param paramMap query parameter.
     * @return the response DTO.
     * @throws MloException Failed to access to SDTNC.
     */
    SdtncResponseDto getPw(Map<String, String> paramMap) throws MloException;
}
