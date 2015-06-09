/**
 * EventManager.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.logic;

import org.o3project.mlo.server.dto.EventDto;

/**
 * EventManager
 */
public interface EventRepository {
	
	EventDto addEventToAllUsers(EventDto eventDto);
	
	EventDto addEventToUser(EventDto eventDto, String userid);
	
	EventDto[] getEventsByUserid(String userid);

}
