/**
 * SdtncGetMethodImpl.java
 * (C) 2013, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.impl.rpc.service;

/**
 * This class is the implementation class of {@link org.o3project.mlo.server.rpc.service.SdtncMethod} interface for get method.
 */
public class SdtncGetMethodImpl extends SdtncBaseMethod {

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.SdtncMethod#getName()
	 */
	@Override
	public String getName() {
		return "GET";
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.SdtncMethod#isSetDoOutput()
	 */
	@Override
	public boolean isSetDoOutput() {
		return false;
	}
}
