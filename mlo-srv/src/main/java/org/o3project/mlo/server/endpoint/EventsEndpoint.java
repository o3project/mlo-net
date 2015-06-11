package org.o3project.mlo.server.endpoint;

import java.io.IOException;
import java.util.ArrayList;

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
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;

public class EventsEndpoint extends Endpoint implements NotificationObserver {
	private static final Log LOG = LogFactory.getLog(EventsEndpoint.class);
	
	private final Object oMutex = new Object();
	private static ArrayList<Session> sessionList = new ArrayList<Session>();
	private boolean isInit = false;
	
	S2Container container;
	NotificationCenter notificationCenter;
	
	@Override
	public void onOpen(final Session session, EndpointConfig ec) {
		sessionList.add(session);
		
		//AliveCheckThread pushTestThread = new AliveCheckThread(session);
		//pushTestThread.start();
		
		session.addMessageHandler(new MessageHandler.Whole<String>() {
			  @Override
			  public void onMessage(String text) {
				/*
				try {
				session.getBasicRemote().sendText(text);
				} catch (IOException ex) {
				Logger.getLogger(MyEndpoint.class.getName()).log(Level.SEVERE, null, ex);
				}
				 */
			  }
		});
		this.
		LOG.debug("WebSocket Connection is opened.");
		
		if (!isInit) {
			init();
		}
	}

	private void init(){
		container = SingletonS2ContainerFactory.getContainer();
		notificationCenter = (NotificationCenter) container.getComponent("notificationCenter");
		notificationCenter.addObserver(this, EventDto.class.getName());
		isInit = true;
	}
	
	@Override
    public void onClose(Session session, CloseReason closeReason){
		notificationCenter.removeObserver(this, EventDto.class.getName());
        sessionList.remove(session);
        LOG.debug("WebSocket Connection is closed.");
    }
	
	
	@Override
	public void notificationObserved(Notification notification){
		
		synchronized (oMutex) {
			EventDto eventDto = (EventDto) notification.data;
			  
			// JSON化し、メッセージ送信
			for(Session session : sessionList){
				try {
					session.getBasicRemote().sendText(JSON.encode(eventDto));
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
		}
	}
	
}