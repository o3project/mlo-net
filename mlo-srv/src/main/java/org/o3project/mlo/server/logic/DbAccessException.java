/**
 * DbAccessException.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.logic;

/**
 * Signals that DBAccessError occurs in calling MLO Web API.
 */
public class DbAccessException extends MloException {

	private static final long serialVersionUID = -1076108675962607800L;
	
	/**
	 * Constructs an instance with the specified detail message.
	 * The string is returned in MLO Web API response.
	 * @param msg the detail message.
	 */
	public DbAccessException(String msg) {
		super(msg);
	}

	/**
	 * Constructs an instance with the specified cause and a detail message.
	 * The string is returned in MLO Web API response.
	 * The cause is used by MLO.
	 * @param msg the detail message.
	 * @param cause the cause.
	 */
	public DbAccessException(String msg, Throwable cause) {
		super(msg, cause);
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.MloException#getErrorName()
	 */
	@Override
	public String getErrorName() {
		return "DBAccessError";
	}
}
