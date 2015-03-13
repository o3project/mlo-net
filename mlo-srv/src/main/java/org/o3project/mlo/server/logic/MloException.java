/**
 * MloException.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.logic;

/**
 * This exception class designates an error caused in MLO.
 * This is the base exception class.
 */
public abstract class MloException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7675619198185750129L;
	
	private String sliceName = null;
	
	private String sliceId = null;

	/**
	 * Constructs an instance with null message.
	 */
	MloException() {
		super();
	}

	/**
	 * Constructs an instance with the specified detail message.
	 * The string is returned in MLO Web API response.
	 * @param msg the detail message.
	 */
	MloException(String msg) {
		super(msg);
	}

	/**
	 * Constructs an instance with the specified cause and a detail message.
	 * The string is returned in MLO Web API response.
	 * The cause is used by MLO.
	 * @param msg the detail message.
	 * @param cause the cause.
	 */
	MloException(String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * Constructs an instance with the specified cause.
	 * The cause is used by MLO.
	 * @param cause the cause.
	 */
	MloException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Obtains the error type.
	 * @return  the error type name.
	 */
	public abstract String getErrorName();
	
	/**
	 * Obtains the slice name.
	 * @return the slice name.
	 */
	public String getSliceName() {
		return sliceName;
	}
	
	/**
	 * Obtains the slice ID.
	 * @return the slice ID.
	 */
	public String getSliceId() {
		return sliceId;
	}
	
	/**
	 * Sets the slice name and slice iD.
	 * @param name the slice name. 
	 * @param id the slice ID.
	 */
	public void setSliceInfo(String name, String id) {
		sliceName = name;
		sliceId = id;
	}
}
