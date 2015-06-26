/**
 * RemoteEndpoint.java
 * (C) 2015, Hitachi, Ltd.
 */
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
import org.o3project.mlo.server.dto.RemoteAccessRequestDto;
import org.o3project.mlo.server.dto.RemoteAccessResponseDto;
import org.o3project.mlo.server.logic.SshAccess;
import org.o3project.mlo.server.logic.SshAccessFactory;
import org.o3project.mlo.server.logic.SshExceptionHandler;
import org.o3project.mlo.server.logic.SshMessageHandler;
import org.seasar.framework.container.SingletonS2Container;

/**
 * This class is the endpoint class for remote node access.
 */
public class RemoteEndpoint extends Endpoint implements SshMessageHandler, SshExceptionHandler {
	private static final Log LOG = LogFactory.getLog(RemoteEndpoint.class);
	
	private static final String RESPONSE_STATUS_OK = "ok";
	private static final String RESPONSE_STATUS_NG = "ng";
	
	private Session session;
	private SshAccess sshAccess;

	/*
	 * (non-Javadoc)
	 * @see javax.websocket.Endpoint#onOpen(javax.websocket.Session, javax.websocket.EndpointConfig)
	 */
	@Override
	public void onOpen(final Session session, EndpointConfig ec) {
		LOG.debug("Webscoket is opened.");
		
		this.session = session;
		// Gets name of target node from path parameter.
		String targetId = this.session.getPathParameters().get("nodeName") ;
		
		SshAccessFactory sshAccessFactory = (SshAccessFactory) SingletonS2Container.getComponent("sshAccessFactory");
		sshAccess = sshAccessFactory.create();
		
		sshAccess.open(targetId, this, this);
		
		session.addMessageHandler(new MessageHandler.Whole<String>() {
			@Override
			public void onMessage(String message) {
				LOG.debug("Recieved a message. : " + message + ":");
				RemoteAccessRequestDto remoteAccessRequestDto = JSON.decode(message, RemoteAccessRequestDto.class);
				sshAccess.send(remoteAccessRequestDto.commandString);
			}
		});
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.websocket.Endpoint#onClose(javax.websocket.Session, javax.websocket.CloseReason)
	 */
	@Override
    public void onClose(Session session, CloseReason closeReason){
		LOG.debug("WebSocket is closed.");
		sshAccess.close();
    }
	
	/*
	 * (non-Javadoc)
	 * @see javax.websocket.Endpoint#onError(javax.websocket.Session, java.lang.Throwable)
	 */
	@Override
    public void onError(Session session, Throwable throwable) {
		LOG.error("WebSocket is closed by error." ,throwable);
		sshAccess.close();
    }
	
	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.SshMessageHandler#onMessage(java.lang.String)
	 */
	@Override
	public void onMessage(final String line) {
		RemoteAccessResponseDto remoteAccessResponseDto = new RemoteAccessResponseDto();
		remoteAccessResponseDto.status = RESPONSE_STATUS_OK;
		remoteAccessResponseDto.result = line + "\n";
		try {
			session.getBasicRemote().sendText(JSON.encode(remoteAccessResponseDto));
		} catch (IOException e) {
			LOG.error("Failed to send response message", e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.SshExceptionHandler#onException(java.lang.Throwable)
	 */
	@Override
	public void onException(Throwable throwable) {
		RemoteAccessResponseDto remoteAccessResponseDto = new RemoteAccessResponseDto();
		remoteAccessResponseDto.status = RESPONSE_STATUS_NG;
		remoteAccessResponseDto.exception = throwable.getMessage();
		try {
			session.getBasicRemote().sendText(JSON.encode(remoteAccessResponseDto));
		} catch (IOException e) {
			LOG.error("Failed to send response exception message", e);
		}
	}
}