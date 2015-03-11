/**
 * OdenosTaskDummy.java
 * (C) 2015, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.impl.rpc.service;

import java.util.concurrent.CountDownLatch;

public class OdenosTaskDummy extends OdenosTask {
	Thread thread = null;
	
	boolean isStarted = false;
	
	boolean isFinished = false;
	
	final CountDownLatch latch;
	/**
	 * 
	 */
	public OdenosTaskDummy(CountDownLatch latch) {
		this.latch = latch;
	}
	
	/**
	 * 
	 */
	public OdenosTaskDummy() {
		this(null);
	}
	
	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.impl.rpc.service.OdenosTask#run()
	 */
	@Override
	public void run() {
		isStarted = true;
		thread = Thread.currentThread();
		try {
			if (latch != null) {
				latch.countDown();
			}
			
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			thread = null;
			isFinished = true;
		}
	}
	
	Thread getRunningThread() {
		return thread;
	}
}