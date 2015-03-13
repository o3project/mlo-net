/**
 * OdenOSAdapterService.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.rpc.service;

import org.o3project.mlo.server.logic.MloException;
import org.o3project.mlo.server.rpc.entity.PTFlowEntity;
import org.o3project.mlo.server.rpc.entity.PTLinkEntity;

/**
 * This interface designates the adapter service for Odenos.
 */
public interface OdenOSAdapterService {

	/**
	 * Initializes this component.
	 * This method is called only at once when the application is launched.
	 */
	void init();
	
	/**
	 * Disposes this component.
	 * This method is called only at once when the application is stopped.
	 */
	void dispose();

	/**
	 * Gets link entity.
	 * @param odenOSLinkId Odenos link ID.
	 * @return Link entity, or null if not exists.
	 * @throws MloException Failed to request.
	 */
	PTLinkEntity getLink(String odenOSLinkId) throws MloException;
	
	/**
	 * Requests to put link.
	 * This method may take long time to process.
	 * When this method completes, the link has been registered.
	 * @param link Requested link.
	 * @throws MloException Failed to process.
	 */
	void requestLink(PTLinkEntity link) throws MloException;
	

	/**
	 * Requests to delete the flow.
	 * This method may take long time to process.
	 * @param odenosFlowId Odenos flow ID.
	 * @throws MloException Failed to process.
	 */
	void deleteFlow(String odenosFlowId) throws MloException;
	
	/**
	 * Requests to create or update the flow.
	 * This method may take long time to process.
	 * @param entity Flow entity.
	 * @return Odenos flow ID. 
	 * @throws MloException Failed to process. 
	 */
	String putFlow(PTFlowEntity entity) throws MloException;
}
