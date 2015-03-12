/**
 * IOdenOSListener.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.rpc.service;

/**
 * This interface designates the listener of Odenos driver to communicate information between MLO and Odenos.
 */
public interface OdenOSListener {

	/**
	 * Sets Odenos driver instance to MLO.
	 * @param driver the PT driver.
	 */
	void setDriver(OdenOSDriver driver);

	/**
	 * Notifies LINK_CHANGE event to MLO.
	 * If the argument linkId is set to null, 
	 * it means the failure of link request.
	 * @param linkId the link ID.
	 */
	void notifyLinkChanged(String linkId);

	/**
	 * Notifies FLOW_CHANGE event of deletion request to MLO.
	 * If the argument odenosFlowId is set to null, 
	 * it means the failure of deletion request.
	 * @param odenosFlowId the Odenos flow ID.
	 */
	void notifyFlowDeleted(String odenosFlowId);

	/**
	 * Notifies FLOW_CHANGE event of creation or updating request to MLO.
	 * If the argument odenosFlowId is set to null, 
	 * it means the failure of request.
	 * @param odenosFlowId the Odenos flow ID.
	 */
	void notifyFlowChanged(String odenosFlowId);

}
