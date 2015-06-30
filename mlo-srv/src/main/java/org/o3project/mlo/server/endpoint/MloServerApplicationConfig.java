/**
 * EventsApplicationConfig.java
 * (C) 2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.endpoint;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpointConfig;

/**
 * This class is the application config class.
 */
public class MloServerApplicationConfig implements ServerApplicationConfig {

	/*
	 * (non-Javadoc)
	 * @see javax.websocket.server.ServerApplicationConfig#getEndpointConfigs(java.util.Set)
	 */
	@Override
	public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> set) {
		HashSet<ServerEndpointConfig> serverEndpointConfigSet = new HashSet<ServerEndpointConfig>();
		serverEndpointConfigSet.add(ServerEndpointConfig.Builder.create(EventsEndpoint.class, "/events").build());
		serverEndpointConfigSet.add(ServerEndpointConfig.Builder.create(RemoteEndpoint.class, "/remote/{nodeName}").build());
		
		return serverEndpointConfigSet;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.websocket.server.ServerApplicationConfig#getAnnotatedEndpointClasses(java.util.Set)
	 */
	@Override
	public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> set) {
		return Collections.emptySet();
	}
}