/**
 * LdException.java
 * (C) 2015, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.psdtnc.logic;

/**
 * LdException
 *
 */
public class LdOperationException extends Exception {

	private static final long serialVersionUID = -3684498304263402051L;

	public LdOperationException(String message) {
		super(message);
	}
	
	public LdOperationException(String message, Throwable cause) {
		super(message, cause);
	}
}
