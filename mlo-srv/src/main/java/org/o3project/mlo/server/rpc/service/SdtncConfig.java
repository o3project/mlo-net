/**
 * SdtncConfig.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.rpc.service;

/**
 * This interface designates the configuration of SDTNC adapter service.
 */
public interface SdtncConfig {

	/**
	 * Obtains the base URI of SDTNC VRM NBI.
	 * @return the base URI.
	 */
	String getNbiBaseUri();

	/**
	 * Obtains the connection timeout to connect to SDTNC.
	 * The unit is seconds.
	 * If zero is retrieved, it means no timeout.
	 * @return the timeout.
	 */
	Integer getConnectionTimeoutSec();
	
	/**
	 * Obtains the read timeout to connect to SDTNC.
	 * The unit is seconds.
	 * If zero is retrieved, it means no timeout.
	 * @return the timeout.
	 */
	Integer getReadTimeoutSec();
	
	/**
	 * Obtains the flag value which designates whether dummy invoker is used or not.
	 * If true, MLO uses dummy invoker.
	 * Dummy invoker is for debugging MLO.
	 * @return the flag.
	 */
	boolean getDummyInvokerSetFlag();
}
