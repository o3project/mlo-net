/**
 * OdenOSConfig.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.rpc.service;

/**
 * This interface designates the configuration of Odenos.
 */
public interface OdenOSConfig {

	/**
	 * Obtains the object ID of the remote system manager.
	 * @return ID.
	 */
	String getRemoteSystemManagerId();

	/**
	 * Obtains the host of the remote system manager.
	 * @return the host name.
	 */
	String getRemoteSystemManagerHost();
	
	/**
	 * Obtains the port number of the remote system manager.
	 * @return the port number.
	 */
	Integer getRemoteSystemManagerPort();
	
	/**
	 * Obtains the object ID of the component manager (remote object manager).
	 * @return ID.
	 */
	String getComponentManagerId();
	
	/**
	 * Obtains the host of the component manager (remote object manager).
	 * This is not used for Odenos-1.0.0.
	 * @return the host name.
	 */
	String getComponentManagerHost();
	
	/**
	 * Obtains the port number of the component manager (remote object manager).
	 * This is not used for Odenos-1.0.0.
	 * @return the port number. 
	 */
	Integer getComponentManagerPort();
	
	/**
	 * Obtains the base URI of the component manager (remote object manager).
	 * This is not used for Odenos-1.0.0.
	 * @return the base URI. 
	 */
	String getComponentManagerBaseUri();
	
	/**
	 * Obtains the launcher host name.
	 * This is not used for Odenos-1.0.0.
	 * @return the host name.
	 */
	String getLauncherHost();
	
	/**
	 * Obtains the launcher port number.
	 * This is not used for Odenos-1.0.0.
	 * @return the port number.
	 */
	Integer getLauncherPort();
	
	/**
	 * Obtains the object ID of the L2 network component.
	 * @return ID.
	 */
	String getNetworkComponentIdL2();
	
	/**
	 * Specifies whether MLO creates the L2 network component or not.
	 * @return true, if MLO creates the L2 network component.
	 */
	Boolean isAvailableCreateL2();
	
	
	/**
	 * Obtains the object ID of th L012 network component.
	 * @return ID.
	 */
	String getNetworkComponentIdL012();
	
	/**
	 * Obtains the network component path.
	 * This is not used for Odenos-1.0.0.
	 * @return the path.
	 */
	String getNetworkComponentPath();

	/**
	 * Obtains the object ID of the packet transport driver.
	 * @return ID.
	 */
	String getPTDriverId();

	/**
	 * Obtains the PT driver path.
	 * This is not used for Odenos-1.0.0.
	 * @return the path.
	 */
	String getPTDriverPath();

	/**
	 * Obtains the object ID of the connection between network component and PT driver.
	 * @return ID.
	 */
	String getConnectionId();
	
	/**
	 * Obtains the connection type of the connection between network component and PT driver.
	 * @return the connection type.
	 */
	String getConnectionType();

	/**
	 * Obtains the connection state of the connection between network component and PT driver.
	 * This is not used for Odenos-1.0.0.
	 * @return the state.
	 */
	String getConnectionState();

	/**
	 * Obtains the path of the connection between network component and PT driver.
	 * This is not used for Odenos-1.0.0.
	 * @return the path.
	 */
	String getConnectionPath();

	/**
	 * Obtains the timeout of the response from Odenos.
	 * The unit is seconds.
	 * @return the timeout.
	 */
	Integer getResponseTimeout();

	/**
	 * Designates whether the completion condition of link request is decided by "established" in establishment_status attribute.
	 * @return returns true if decided by "established".
	 */
	Boolean isAvailableReqLinkEstablishedCompletion();
}
