/**
 * OdenOSDriver.java
 * (C) 2013, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.impl.rpc.service;

import java.io.IOException;

import org.o3project.mlo.server.logic.InternalException;
import org.o3project.mlo.server.rpc.entity.PTFlowEntity;
import org.o3project.mlo.server.rpc.entity.PTLinkEntity;
import org.o3project.mlo.server.rpc.service.OdenOSDriver;
import org.o3project.mlo.server.rpc.service.OdenOSListener;


/**
 * OdenOSDriver
 *
 */
public class OdenOSDriverStub implements OdenOSDriver {

	private PTLinkEntity getLinkResponse = null;
	
	private int requestLinkWait = 0;

	private int deleteFlowWait = 0;

	private int putFlowWait = 0;

	private OdenOSListener listener = null;

	class NotifyLinkThread extends Thread {
		
		private String elements;

		public NotifyLinkThread(String elements) {
			this.elements = elements;
		}
		
		@Override
		public void run() {
			try {
				Thread.sleep((requestLinkWait * 1000));
				listener.notifyLinkChanged(elements);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	class NotifyDeleteFlowThread extends Thread {
		
		private String elements;

		public NotifyDeleteFlowThread(String elements) {
			this.elements = elements;
		}
		
		@Override
		public void run() {
			try {
				Thread.sleep((deleteFlowWait * 1000));
				listener.notifyFlowDeleted(elements);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	class NotifyFlowChangedThread extends Thread {
		
		private String flowId;

		public NotifyFlowChangedThread(String flowId) {
			this.flowId = flowId;
		}
		
		@Override
		public void run() {
			try {
				Thread.sleep((putFlowWait * 1000));
				listener.notifyFlowChanged(flowId);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.IOdenOSDriver#getLink(java.lang.String)
	 */
	@Override
	public PTLinkEntity getLink(String linkId) throws Exception {
		if (linkId.equals("test")) {
			throw new IOException("test");
		}
		if (getLinkResponse == null) {
			throw new InternalException("test");
		}
		return getLinkResponse;
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.IOdenOSDriver#requestLink(org.o3project.mlo.server.rpc.entity.PTLinkEntity)
	 */
	@Override
	public void requestLink(PTLinkEntity entity) throws Exception {
		if (requestLinkWait < 0) {
			throw new InternalException("test");
		}
		NotifyLinkThread thread = new NotifyLinkThread(entity.linkId);
		thread.start();
	}

	public void setGetLinkResponse(PTLinkEntity getLinkResponse) {
		this.getLinkResponse = getLinkResponse;
	}

	public void setRequestLinkWait(int requestLinkWait) {
		this.requestLinkWait = requestLinkWait;
	}

	public void setDeleteFlowWait(int deleteFlowWait) {
		this.deleteFlowWait = deleteFlowWait;
	}

	public void setPutFlowWait(int putFlowWait) {
		this.putFlowWait = putFlowWait;
	}

	public void setListener(OdenOSListener listener) {
		this.listener = listener;
	}

	@Override
	public void deleteFlow(String odenosFlowId) throws Exception {
		if (deleteFlowWait < 0) {
			throw new InternalException("test");
		}
		NotifyDeleteFlowThread thread = new NotifyDeleteFlowThread(odenosFlowId);
		thread.start();
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.rpc.service.IOdenOSDriver#putFlow(org.o3project.mlo.server.rpc.entity.PTFlowEntity)
	 */
	@Override
	public void putFlow(PTFlowEntity entity) throws Exception {
		if (putFlowWait < 0) {
			throw new InternalException("test");
		}
		NotifyFlowChangedThread thread = new NotifyFlowChangedThread(entity.flowId);
		thread.start();
	}
}
