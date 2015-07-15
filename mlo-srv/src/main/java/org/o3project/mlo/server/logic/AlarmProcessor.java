/**
 * AlarmProcessor.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.logic;

import java.util.List;

import org.o3project.mlo.server.dto.AlarmDto;

/**
 * AlarmProcessor
 */
public interface AlarmProcessor {
	List<AlarmDto> getAlarms();
	
	List<AlarmDto> postAlarms(List<AlarmDto> alarmDtos) throws MloException;
	
	AlarmDto deleteAlarm(String alarmId) throws MloException;
}
