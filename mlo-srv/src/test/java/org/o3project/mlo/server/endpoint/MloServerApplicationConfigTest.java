package org.o3project.mlo.server.endpoint;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.websocket.server.ServerEndpointConfig;

import org.junit.Test;

public class MloServerApplicationConfigTest {

	/**
	 * Test method for {@link org.o3project.mlo.server.logic.EventsApplicationConfig#getEndpointConfigs(java.util.Set<Class<? extends Endpoint>>)}.
	 */
	@Test
	public void testGetEndpointConfigs() {
		MloServerApplicationConfig obj = new MloServerApplicationConfig();
		HashSet<ServerEndpointConfig> serverEndpointConfigSet;
		
		serverEndpointConfigSet = (HashSet<ServerEndpointConfig>) obj.getEndpointConfigs(null);
		ServerEndpointConfig serverEndpointConfig;
		assertEquals(2, serverEndpointConfigSet.size());
		Iterator<ServerEndpointConfig> it = serverEndpointConfigSet.iterator();
		serverEndpointConfig = it.next();
		
		if (RemoteEndpoint.class.equals(serverEndpointConfig.getEndpointClass())){
			assertEquals(RemoteEndpoint.class, serverEndpointConfig.getEndpointClass());
			assertEquals("/remote/{nodeName}", serverEndpointConfig.getPath());
			serverEndpointConfig = it.next();
			assertEquals(EventsEndpoint.class, serverEndpointConfig.getEndpointClass());
			assertEquals("/events", serverEndpointConfig.getPath());
		} else {
			assertEquals(EventsEndpoint.class, serverEndpointConfig.getEndpointClass());
			assertEquals("/events", serverEndpointConfig.getPath());
			serverEndpointConfig = it.next();
			assertEquals(RemoteEndpoint.class, serverEndpointConfig.getEndpointClass());
			assertEquals("/remote/{nodeName}", serverEndpointConfig.getPath());
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.logic.MloServerApplicationConfig#getAnnotatedEndpointClasses(getAnnotatedEndpointClasses)}.
	 */
	@Test
	public void testGetAnnotatedEndpointClasses() {
		MloServerApplicationConfig obj = new MloServerApplicationConfig();
		
		Set<Class<?>> emptySet = obj.getAnnotatedEndpointClasses(null);
		assertEquals(0, emptySet.size());
	}
}
