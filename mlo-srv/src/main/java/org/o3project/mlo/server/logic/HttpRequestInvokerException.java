/**
 * HttpRequestInvokerException.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.logic;

/**
 * Exception class for HttpRequestInvoker.
 */
public class HttpRequestInvokerException extends Exception {

	private static final long serialVersionUID = -3031842876416082525L;

	/**
	 * The status code of http response.
	 */
	final Integer statusCode;

	/**
	 * A constructor.
	 * @param statusCode The status code.
	 * @param message The message.
	 */
	public HttpRequestInvokerException(Integer statusCode, String message) {
		super(message);
		this.statusCode = statusCode;
	}
	
	/**
	 * A constructor.
	 * @param statusCode The status code.
	 * @param message The message.
	 * @param cause The cause object for this exception.
	 */
	public HttpRequestInvokerException(Integer statusCode, String message, Throwable cause) {
		super(message, cause);
		this.statusCode = statusCode;
	}
	
	/**
	 * Obtains the status code.
	 * @return The status code.
	 */
	public Integer getStatusCode() {
		return statusCode;
	}
}
