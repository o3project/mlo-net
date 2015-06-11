package org.o3project.mlo.server.endpoint;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpointConfig;

public class EventsApplicationConfig implements ServerApplicationConfig {

	@Override
	public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> set) {
		HashSet<ServerEndpointConfig> serverEndpointConfigSet = new HashSet<ServerEndpointConfig>();
		serverEndpointConfigSet.add(ServerEndpointConfig.Builder.create(EventsEndpoint.class, "/events").build());
		
		return serverEndpointConfigSet;
	}

	@Override
	public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> set) {
		return Collections.emptySet();
	}
}