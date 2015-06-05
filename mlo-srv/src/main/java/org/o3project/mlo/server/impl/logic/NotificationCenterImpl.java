/**
 * NotificationCenterImpl.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.o3project.mlo.server.logic.Notification;
import org.o3project.mlo.server.logic.NotificationCenter;
import org.o3project.mlo.server.logic.NotificationObserver;

/**
 * NotificationCenterImpl
 */
public class NotificationCenterImpl implements NotificationCenter {
	private static final Log LOG = LogFactory.getLog(NotificationCenterImpl.class);
	
	private static final Integer THREAD_POOL_SIZE = 32;
	
	private static final Integer EXECUTOR_SERVICE_TERMINATION_TIMEOUT_SEC = 20;
	
	private final Object oMutex = new Object();

	private ExecutorService executorService;
	
	Map<String, Set<NotificationObserver>> observerMap = new HashMap<String, Set<NotificationObserver>>();
	
	public void init() {
		synchronized (oMutex) {
			if (executorService == null) {
				executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
			}
		}
	}
	
	public void destroy() {
		synchronized (oMutex) {
			if (executorService != null) {
				Integer timeoutSec = EXECUTOR_SERVICE_TERMINATION_TIMEOUT_SEC;
				executorService.shutdownNow();
				try {
					executorService.awaitTermination(timeoutSec, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					LOG.warn("Interruption occurs.", e);
				} finally {
					executorService = null;
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.NotificationCenter#addObserver(org.o3project.mlo.server.logic.NotificationObserver, java.lang.String)
	 */
	@Override
	public void addObserver(NotificationObserver observer, String notificationName) {
		synchronized (oMutex) {
			if (!observerMap.containsKey(notificationName)) {
				observerMap.put(notificationName, new HashSet<NotificationObserver>());
			}
			Set<NotificationObserver> observerSet = observerMap.get(notificationName);
			if (!observerSet.contains(observer)) {
				observerSet.add(observer);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.NotificationCenter#removeObserver(org.o3project.mlo.server.logic.NotificationObserver, java.lang.String)
	 */
	@Override
	public void removeObserver(NotificationObserver observer, String notificationName) {
		synchronized (oMutex) {
			if (observerMap.containsKey(notificationName) && observerMap.get(notificationName).contains(observer)) {
				observerMap.get(notificationName).remove(observer);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.NotificationCenter#postNotification(org.o3project.mlo.server.logic.Notification)
	 */
	@Override
	public void postNotification(Notification notification) {
		Set<NotificationObserver> observerSet = null;
		synchronized (oMutex) {
			if (observerMap.containsKey(notification.notificationName)) {
				observerSet = new HashSet<>(observerMap.get(notification.notificationName));
			} else {
				observerSet = new HashSet<>();
			}
			
			if (executorService != null) {
				for (NotificationObserver observer : observerSet) {
					executorService.submit(createNotificationTask(notification, observer));
				}
			}
		}
	}

	Runnable createNotificationTask(final Notification notification, final NotificationObserver observer) {
		return new Runnable() {
			@Override
			public void run() {
				observer.notificationObserved(notification);
			}
		};
	}
}
