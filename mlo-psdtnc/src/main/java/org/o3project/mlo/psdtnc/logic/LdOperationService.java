/**
 * LdOperationService.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.psdtnc.logic;

import org.o3project.mlo.server.dto.LdTopoDto;

/**
 * LdOperationService
 *
 */
public interface LdOperationService {
	
	void doStart() throws LdOperationException;
	
	void doStop() throws LdOperationException;
	
	Integer doStatus() throws LdOperationException;
	
	Integer doExecuteSingleCommand(String shellCommand) throws LdOperationException;
	
	LdTopoDto doLoadTopoConf() throws LdOperationException;
}
