/**
 * IOdenOSDriver.java
 * (C) 2013, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.rpc.service;

import org.o3project.mlo.server.rpc.entity.PTFlowEntity;
import org.o3project.mlo.server.rpc.entity.PTLinkEntity;

/**
 * This interface designates the Odenos driver to communicate packet transport network information between MLO and Odenos.
 */
public interface OdenOSDriver {

	/**
	 * Gets link information.
	 * If link with specified link ID does not exist, null is returned.
	 * @param linkId the link ID.
	 * @return Link entity.
	 * @throws Exception Error occurs in Odenos.
	 */
	PTLinkEntity getLink(String linkId) throws Exception;
	
	/**
	 * Requests to create link.
	 * @param entity the link entity. 
	 * @throws Exception Error occurs in Odenos.
	 */
	void requestLink(PTLinkEntity entity) throws Exception;
	
	/**
	 * Deletes flow.
	 * @param odenosFlowId Odenos flow ID.n
	 * @throws Exception Error occurs in Odenos.
	 */
	void deleteFlow(String odenosFlowId) throws Exception;
	
	/**
	 * Puts or updates flow.
	 * @param entity the flow entity.
	 * @throws Exception Error occurs in Odenos.
	 */
	void putFlow(PTFlowEntity entity) throws Exception;
}
