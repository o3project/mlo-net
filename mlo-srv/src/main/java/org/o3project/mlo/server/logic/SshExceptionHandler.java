/**
 * SshExceptionHandler.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.logic;

/**
 * This interface designates the exception handler for SSH.
 */
public interface SshExceptionHandler {

	/**
	 * Processes exception handled.
	 * @param throwable exception handled.
	 */
	public void onException(Throwable throwable);
	
}
