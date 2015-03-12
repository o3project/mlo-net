/**
 * OtherException.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.logic;

/**
 * Signals that OtherError occurs in calling MLO Web API.
 */
public class OtherException extends MloException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7378169734181874147L;
	
	/**
	 * Constructs an instance with the specified detail message.
	 * The string is returned in MLO Web API response.
	 * @param msg the detail message.
	 */
	public OtherException(String msg) {
		super(msg);
	}	

	/**
	 * Constructs an instance with the specified cause and a detail message.
	 * The string is returned in MLO Web API response.
	 * The cause is used by MLO.
	 * @param msg the detail message.
	 * @param cause the cause.
	 */
	public OtherException(String msg, Throwable cause) {
		super(msg, cause);
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.MloException#getErrorName()
	 */
	@Override
	public String getErrorName() {
		return "OtherError";
	}
}
