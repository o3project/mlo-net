/**
 * EventsEndpoint.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.endpoint;

import java.io.IOException;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;

import net.arnx.jsonic.JSON;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.o3project.mlo.server.dto.EventDto;
import org.o3project.mlo.server.logic.Notification;
import org.o3project.mlo.server.logic.NotificationCenter;
import org.o3project.mlo.server.logic.NotificationObserver;
import org.seasar.framework.container.SingletonS2Container;

/**
 * This class is the endpoint class for events.
 */
public class EventsEndpoint extends Endpoint implements NotificationObserver {
	private static final Log LOG = LogFactory.getLog(EventsEndpoint.class);
	
	private Session session;
	private NotificationCenter notificationCenter;
	
	/*
	 * (non-Javadoc)
	 * @see javax.websocket.Endpoint#onOpen(javax.websocket.Session, javax.websocket.EndpointConfig)
	 */
	@Override
	public void onOpen(final Session session, EndpointConfig ec) {
		this.session = session;

		LOG.debug("WebSocket Connection is opened.");
		
		notificationCenter = (NotificationCenter) SingletonS2Container.getComponent("notificationCenter");
		notificationCenter.addObserver(this, EventDto.class.getName());
	}

	/*
	 * (non-Javadoc)
	 * @see javax.websocket.Endpoint#onClose(javax.websocket.Session, javax.websocket.CloseReason)
	 */
	@Override
    public void onClose(Session session, CloseReason closeReason){
		notificationCenter.removeObserver(this, EventDto.class.getName());
		LOG.debug("WebSocket Connection is closed.");
    }

	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.NotificationObserver#notificationObserved( org.o3project.mlo.server.logic.Notification)
	 */
	@Override
	public void notificationObserved(Notification notification){
		EventDto eventDto = (EventDto) notification.data;

		if (this.session.isOpen()){
			// JSON化し、メッセージ送信
			try {
				this.session.getBasicRemote().sendText(JSON.encode(eventDto));
			} catch (IOException e) {
				LOG.error("Failed to send a eventDto.",e);
			}
		} else {
			LOG.warn("Session is close. Cancelled transmission of a eventDto.");
		}
	}
}