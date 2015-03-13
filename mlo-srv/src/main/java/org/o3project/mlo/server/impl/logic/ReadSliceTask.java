/**
 * ReadSliceTask.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import org.o3project.mlo.server.dto.RestifRequestDto;
import org.o3project.mlo.server.dto.RestifResponseDto;

/**
 * This class is the implementation class to read slice called in {@link SliceManager}.
 */
public class ReadSliceTask extends SliceOperationBaseTask {

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.impl.logic.SliceOperationBaseTask#call()
	 */
	@Override
	public RestifResponseDto call() throws Exception {
		RestifRequestDto reqDto = getRequestDto(); 
		synchronized (sliceManager) {
			return sliceManager.readSlice(reqDto);
		}
	}
}
