/**
 * SdtncDtoConfig.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.rpc.service;

import org.o3project.mlo.server.logic.MloException;

/**
 * This interface designates configurations of parameters in requesting SDTNC.
 */
public interface SdtncDtoConfig {

	/**
	 * Obtains login password.
	 * @return the login password. 
	 */
	String getLoginPassword();

	/**
	 * Obtains slice ID.
	 * @return the slice ID. 
	 */
	String getSliceId();
	
	/**
	 * Obtains login IP address.
	 * @return the login IP address. 
	 */
	String getLoginIpAddress();
	
	/**
	 * Obtains hop-by-hop path ID.
	 * @return the path ID. 
	 */
	String getHopPathId();
	
	/**
	 * Obtains cut-through link ID.
	 * @return the link ID. 
	 */
	String getCutLinkId();
	
	/**
	 * Obtains hop-by-hop link ID.
	 * @return the link ID. 
	 */
	String getHopLinkId();
	
	/**
	 * Obtains cut-through path ID.
	 * @return the path ID. 
	 */
	String getCutPathId();
	
	/**
	 * Obtains start-edge node ID.
	 * @return the node ID. 
	 */
	String getNeIdA();
	
	/**
	 * Obtains end-edge node ID.
	 * @return the node ID. 
	 */
	String getNeIdZ();
	
	/**
	 * Obtains hop-by-hop start-edge port ID.
	 * @return the port ID. 
	 */
	String getHopPortIdA();
	
	/**
	 * Obtains hop-by-hop end-edge port ID.
	 * @return the port ID. 
	 */
	String getHopPortIdZ();
	
	/**
	 * Obtains cut-through start-edge port ID.
	 * @return the port ID. 
	 */
	String getCutPortIdA();
	
	/**
	 * Obtains cut-through end-edge port ID.
	 * @return the port ID. 
	 */
	String getCutPortIdZ();
	
	/**
	 * Obtains Pir rate.
	 * @return the Pir rate. 
	 */
	String getPirRate();
	
	/**
	 * Obtains SLA mode.
	 * @return the SLA mode. 
	 */
	String getSlaMode();
	
	/**
	 * Designates whether workaround mode is used in creating path.
	 * If this returns true, MLO behaves in workaround mode.
	 * In workaround mode, MLO sets vlan specific port to port for pathEndPoint element.  
	 * @return workaround mode or not.
	 */
	Boolean isPutVPathWorkaroundMode();
	
	/**
	 * Obtains VLAN property.
	 * @param vlanId the VLAN ID.
	 * @param subKey the sub key of the property.
	 * @return the property value.
	 * @throws MloException Failed to retrieve values.
	 */
	String getVlanProperty(String vlanId, String subKey) throws MloException;
	
	/**
	 * Obtains the file path of VLAN extra mapping file, which includes VLAN information for 100 flow creation in SDTNC.
	 * @return the file path.
	 */
	String getVlanExtraMappingFilePath();
	
	/**
	 * Designates whether MLO can create 100 flows or not.
	 * @return  true, if possible.
	 */
	Boolean canCreateExtraFlows();
}
