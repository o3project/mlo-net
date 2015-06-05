/**
 * NotificationCenter.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.logic;

/**
 * NotificationCenter
 */
public interface NotificationCenter {
	
	void addObserver(NotificationObserver observer, String notificationName);
	
	void removeObserver(NotificationObserver observer, String notificationName);
	
	void postNotification(Notification notification);

}
