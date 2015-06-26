/**
 * HttpRequestInvokerException.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.logic;

/**
 * Exception class for SSH connection.
 */
public class SshConnectionException extends Exception {
	
	private static final long serialVersionUID = 9068814816903561208L;

	/**
	 * A constructor.
	 * @param message The message.
	 */
	public SshConnectionException(String message) {
		super(message);
	}
	
	
}
