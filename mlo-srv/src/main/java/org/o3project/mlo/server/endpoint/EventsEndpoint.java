package org.o3project.mlo.server.endpoint;

import java.io.IOException;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

import net.arnx.jsonic.JSON;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.o3project.mlo.server.dto.EventDto;
import org.o3project.mlo.server.logic.Notification;
import org.o3project.mlo.server.logic.NotificationCenter;
import org.o3project.mlo.server.logic.NotificationObserver;
import org.seasar.framework.container.SingletonS2Container;

public class EventsEndpoint extends Endpoint implements NotificationObserver {
	private static final Log LOG = LogFactory.getLog(EventsEndpoint.class);
	
	private Session session;;
	private NotificationCenter notificationCenter;
	private boolean isInitialized = false;
	
	@Override
	public void onOpen(final Session session, EndpointConfig ec) {
		this.session = session;
		
		session.addMessageHandler(new MessageHandler.Whole<String>() {
			@Override
			public void onMessage(String text) {
			}
		});
		EventsEndpoint.LOG.debug("WebSocket Connection is opened.");
		
		if (!isInitialized) {
			init();
		}
	}

	private void init(){
		notificationCenter = (NotificationCenter) SingletonS2Container.getComponent("notificationCenter");
		notificationCenter.addObserver(this, EventDto.class.getName());
		isInitialized = true;
	}
	
	@Override
    public void onClose(Session session, CloseReason closeReason){
		notificationCenter.removeObserver(this, EventDto.class.getName());
		LOG.debug("WebSocket Connection is closed.");
    }

	@Override
	public void notificationObserved(Notification notification){
		
		EventDto eventDto = (EventDto) notification.data;

		// JSON化し、メッセージ送信
		try {
			this.session.getBasicRemote().sendText(JSON.encode(eventDto));
		} catch (IOException e) {
			LOG.error("Failed to send eventDto.",e);
		}
	}
	
}