/**
 * SdtncDeleteMethodImpl.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.rpc.service;

/**
 * This class is the implementation class of {@link org.o3project.mlo.server.rpc.service.SdtncMethod} interface for delete method.
 */
public class SdtncDeleteMethodImpl extends SdtncBaseMethod {

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.SdtncMethod#getName()
	 */
	@Override
	public String getName() {
		return "DELETE";
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.SdtncMethod#isSetDoOutput()
	 */
	@Override
	public boolean isSetDoOutput() {
		return false;
	}
}
