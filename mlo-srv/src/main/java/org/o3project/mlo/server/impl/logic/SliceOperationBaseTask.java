/**
 * SliceOperationBaseTask.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import org.o3project.mlo.server.dto.RestifResponseDto;
import org.o3project.mlo.server.logic.InternalException;
import org.o3project.mlo.server.logic.MloException;
import org.o3project.mlo.server.logic.SliceOperationTask;

/**
 * This class is the base implementation class of {@link SliceOperationTask} interface.
 */
public abstract class SliceOperationBaseTask 
		extends AbstractSliceOperationTask implements SliceOperationTask {
	
	protected SliceManager sliceManager;

	/**
	 * Setter method (for DI setter injection).
	 * @param sliceManager The instance. 
	 */
	public void setSliceManager(SliceManager sliceManager) {
		this.sliceManager = sliceManager;
	}

	/*
	 * (non-Javadoc)
	 * @see org.o3project.mlo.server.impl.logic.AbstractSliceOperationTask#checkPreparation()
	 */
	@Override
	protected final void checkPreparation() throws MloException {
		super.checkPreparation();
		
		if(sliceManager == null) {
			throw new InternalException("SliceManager is null.");
		}
	};

	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public RestifResponseDto call() throws Exception {
		throw new InternalException("NotImplemented");
	}
}
