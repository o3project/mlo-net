/**
 * SshMessageHandler.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.logic;

/**
 * This interface designates the message handler for SSH. 
 */
public interface SshMessageHandler {
	
	/**
	 * Handles recieved message.
	 * @param line message handled.
	 */
	public void onMessage(String line);
}
