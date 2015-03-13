/**
 * InternalException.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.logic;

/**
 * Signals that InternalError occurs in calling MLO Web API.
 */
public class InternalException extends MloException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4714293328947831238L;
	
	/**
	 * Constructs an instance with the specified detail message.
	 * The string is returned in MLO Web API response.
	 * @param msg the detail message.
	 */
	public InternalException(String msg) {
		super(msg);
	}

	/**
	 * Constructs an instance with the specified cause and a detail message.
	 * The string is returned in MLO Web API response.
	 * The cause is used by MLO.
	 * @param msg the detail message.
	 * @param cause the cause.
	 */
	public InternalException(String msg, Throwable cause) {
		super(msg, cause);
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.MloException#getErrorName()
	 */
	@Override
	public String getErrorName() {
		return "InternalError";
	}

}
