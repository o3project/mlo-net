/**
 * EventRepositoryImpl.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.o3project.mlo.server.dto.EventDto;
import org.o3project.mlo.server.logic.EventRepository;
import org.o3project.mlo.server.logic.Notification;
import org.o3project.mlo.server.logic.NotificationCenter;

/**
 * EventRepositoryImpl
 */
public class EventRepositoryImpl implements EventRepository {
	
	private static final Integer EVENTS_MAX_SIZE = 256;

	private final List<EventDto> allUsersEvents = new ArrayList<>();
	
	private final Map<String, List<EventDto>> userEventsMap = new HashMap<>();
	
	private NotificationCenter notificationCenter;
	
	/**
	 * @param notificationCenter the notificationCenter to set
	 */
	public void setNotificationCenter(NotificationCenter notificationCenter) {
		this.notificationCenter = notificationCenter;
	}
	

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.EventRepository#addEventToAllUsers(org.o3project.mlo.server.dto.EventDto)
	 */
	@Override
	public EventDto addEventToAllUsers(EventDto eventDto) {
		eventDto.userid = null;
		synchronized (allUsersEvents) {
			return addEvent(eventDto, allUsersEvents);
		}
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.EventRepository#addEventToUser(org.o3project.mlo.server.dto.EventDto, java.lang.String)
	 */
	@Override
	public EventDto addEventToUser(EventDto eventDto, String userid) {
		eventDto.userid = userid;
		synchronized (userEventsMap) {
			if (!userEventsMap.containsKey(userid)) {
				userEventsMap.put(userid, new ArrayList<EventDto>());
			}
			List<EventDto> events = userEventsMap.get(userid);
			return addEvent(eventDto, events);
		}
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.EventRepository#getEventsByUserid(java.lang.String)
	 */
	@Override
	public EventDto[] getEventsByUserid(String userid) {
		List<EventDto> events = new ArrayList<>();
		synchronized (allUsersEvents) {
			events.addAll(allUsersEvents);
		}
		synchronized (userEventsMap) {
			if (userEventsMap.containsKey(userid)) {
				events.addAll(userEventsMap.get(userid));
			}
		}
		Collections.sort(events, new Comparator<EventDto>() {
			@Override
			public int compare(EventDto evt1, EventDto evt2) {
				return evt1.timestamp.compareTo(evt2.timestamp);
			}
		});
		return events.toArray(new EventDto[0]);
	}
	
	private final EventDto addEvent(EventDto eventDto, List<EventDto> events) {
		eventDto.id = generateEventId(eventDto);
		events.add(eventDto);
		while (events.size() > EVENTS_MAX_SIZE) {
			events.remove(0);
		}
		notificationCenter.postNotification(createNotification(eventDto));
		return eventDto;
	}
	
	private final Notification createNotification(EventDto eventDto) {
		Notification notification = new Notification();
		notification.notificationName = eventDto.getClass().getName();
		notification.data = eventDto;
		notification.sender = this;
		return notification;
	}

	private static final String generateEventId(EventDto eventDto) {
		return String.format("%08x", eventDto.hashCode());
	}
}
