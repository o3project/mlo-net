/**
 * AlarmProcessorImpl.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import org.o3project.mlo.server.dto.AlarmDto;
import org.o3project.mlo.server.logic.AlarmProcessor;
import org.o3project.mlo.server.logic.MloException;
import org.o3project.mlo.server.logic.Notification;
import org.o3project.mlo.server.logic.NotificationCenter;

/**
 * AlarmProcessorImpl
 */
public class AlarmProcessorImpl implements AlarmProcessor {
	private static final Integer ALARMID_ALARM_DTO_MAP_MAX_SIZE = 128;
	
	final LinkedHashMap<String, AlarmDto> alarmidAlarmDtoMap = new LinkedHashMap<>();
	
	Integer alarmidAlarmDtoMapMaxSize = ALARMID_ALARM_DTO_MAP_MAX_SIZE;
	
	NotificationCenter notificationCenter;
	
	/**
	 * @param notificationCenter the notificationCenter to set
	 */
	public void setNotificationCenter(NotificationCenter notificationCenter) {
		this.notificationCenter = notificationCenter;
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.AlarmProcessor#getAlarms()
	 */
	@Override
	public List<AlarmDto> getAlarms() {
		List<AlarmDto> alarmDtoList = null;
		synchronized (alarmidAlarmDtoMap) {
			alarmDtoList = new ArrayList<>(alarmidAlarmDtoMap.values());
			alarmDtoList = Collections.unmodifiableList(alarmDtoList);
		}
		return alarmDtoList;
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.AlarmProcessor#postAlarms(java.util.List)
	 */
	@Override
	public List<AlarmDto> postAlarms(List<AlarmDto> alarmDtos) throws MloException {
		List<AlarmDto> newAlarmDtos = new ArrayList<>();
		synchronized (alarmidAlarmDtoMap) {
			for (AlarmDto alarmDto : alarmDtos) {
				alarmDto.id = String.format("%08x", alarmDto.hashCode());
				if (!alarmidAlarmDtoMap.containsKey(alarmDto.id)) {
					alarmidAlarmDtoMap.put(alarmDto.id, alarmDto);
					newAlarmDtos.add(alarmDto);
					
					notifyAlarm(alarmDto);
				}
			}
			
			if (alarmidAlarmDtoMap.size() > alarmidAlarmDtoMapMaxSize) {
				int numOfDeleted = (alarmidAlarmDtoMap.size() - alarmidAlarmDtoMapMaxSize);
				for (String key : alarmidAlarmDtoMap.keySet()) {
					if (numOfDeleted < 1) {
						break;
					}
					alarmidAlarmDtoMap.remove(key);
					numOfDeleted -= 1;
				}
			}
		}
		return newAlarmDtos;
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.AlarmProcessor#deleteAlarm(java.lang.String)
	 */
	@Override
	public AlarmDto deleteAlarm(String alarmId) throws MloException {
		AlarmDto deletedAlarmDto = null;
		synchronized (alarmidAlarmDtoMap) {
			deletedAlarmDto = alarmidAlarmDtoMap.get(alarmId);
			if (deletedAlarmDto != null) {
				alarmidAlarmDtoMap.remove(alarmId);
			}
		}
		return deletedAlarmDto;
	}

	/**
	 * @param alarmDto
	 */
	void notifyAlarm(AlarmDto alarmDto) {
		Notification notification = new Notification();
		notification.notificationName = alarmDto.getClass().getName();
		notification.sender = this;
		notification.data = alarmDto;
		notificationCenter.postNotification(notification);
	}
}
