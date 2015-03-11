/**
 * EmptySliceOperationTask.java
 * (C) 2013, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import org.o3project.mlo.server.dto.RestifResponseDto;
import org.o3project.mlo.server.impl.logic.AbstractSliceOperationTask;
import org.o3project.mlo.server.logic.MloException;

/**
 * EmptySliceOperationTask
 *
 */
class EmptySliceOperationTask extends AbstractSliceOperationTask {
	
	private final MloException sampleMloException;
	
	private final Exception sampleException;
	
	/**
	 * 
	 */
	public EmptySliceOperationTask(MloException sampleMloException, Exception sampleException) {
		super();
		this.sampleMloException = sampleMloException;
		this.sampleException = sampleException;
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public RestifResponseDto call() throws Exception {
		if (sampleMloException != null) {
			throw sampleMloException;
		} else if (sampleException != null) {
			throw sampleException;
		}
		return null;
	}
}
