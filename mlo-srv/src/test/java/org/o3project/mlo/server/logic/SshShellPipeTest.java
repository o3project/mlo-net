package org.o3project.mlo.server.logic;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.o3project.mlo.server.endpoint.RemoteEndpoint;

public class SshShellPipeTest {

	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshShellPipe#SshShellPipe()}.
	 */
	@Test
	public void testSshShellPipe() {
		SshShellPipe obj = null; 
		try{
			obj = new SshShellPipe();
		} finally {
			obj.close();
		}
		assertNotNull(obj);
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshShellPipe#setSshMessageHandler(org.o3project.mlo.server.endpoint.RemoteEndpoint)}.
	 */
	@Test
	public void testSetSshMessageHandler() {
		SshShellPipe obj = new SshShellPipe();
		try{
			obj.setSshMessageHandler(new RemoteEndpoint());
		} catch (Throwable th) {
			fail();
		} finally {
			obj.close();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshShellPipe#setSshExceptionHandler(org.o3project.mlo.server.endpoint.RemoteEndpoint)}.
	 */
	@Test
	public void testSetSshExceptionHandler() {
		SshShellPipe obj = new SshShellPipe();
		try{
			obj.setSshExceptionHandler(new RemoteEndpoint());
		} catch (Throwable th) {
			fail();
		} finally {
			obj.close();
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshShellPipe#connect()}.
	 */
	@Test
	public void testConnect() {
		SshShellPipe obj = new SshShellPipe();
		try{
			obj.connect();
		} catch (Throwable th) {
			fail();
		} finally {
			obj.close();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshShellPipe#getSshStdInputStream()}.
	 */
	@Test
	public void testGetSshStdInputStream() {
		SshShellPipe obj = new SshShellPipe();
		
		assertNotNull(obj.getSshStdInputStream());
		obj.close();
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshShellPipe#getSshStdOutputStream()}.
	 */
	@Test
	public void testGetSshStdOutputStream() {
		SshShellPipe obj = new SshShellPipe();
		
		assertNotNull(obj.getSshStdOutputStream());
		obj.close();
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshShellPipe#getAppStdinOutputStream()}.
	 */
	@Test
	public void testGetAppStdinOutputStream() {
		SshShellPipe obj = new SshShellPipe();
		
		assertNotNull(obj.getAppStdinOutputStream());
		obj.close();
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshShellPipe#getAppStdoutInputStream()}.
	 */
	@Test
	public void testGetAppStdoutInputStream() {
		SshShellPipe obj = new SshShellPipe();
		
		assertNotNull(obj.getAppStdoutInputStream());
		obj.close();
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshShellPipe#close()}.
	 */
	@Test
	public void testClose() {
		SshShellPipe obj = new SshShellPipe();
		try{
			obj.close();
		} catch (Throwable th) {
			fail();
		} 
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshShellPipe#handleMessage(java.lang.String)}.
	 */
	@Test
	public void testHandleMessage() {
		SshShellPipe obj = new SshShellPipe();
		
		DummySshMessageHandler handler = new DummySshMessageHandler();
		obj.setSshMessageHandler(handler);
		String line = "dummy line";
	
		// execute
		try {
			obj.handleMessage(line);
		} finally {
			obj.close();
		}
		
		// test
		assertEquals(1, handler.lines.size());
		assertEquals("dummy line", handler.lines.get(0));
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshShellPipe#handleMessage(java.lang.String)}.
	 */
	@Test
	public void testHandleMessage_null() {
		SshShellPipe obj = new SshShellPipe();
		
		obj.setSshMessageHandler(null);
		
		NullPointerException npe = null;
		try{
			obj.handleMessage("test");
		} catch (NullPointerException e) {
			npe = e;
		} finally {
			obj.close();
		}
		
		// Null pointer exception must not be thrown.
		assertNull(npe);
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshShellPipe#handleException(java.lang.Throwable)}.
	 */
	@Test
	public void testHandleException() {
		SshShellPipe obj = new SshShellPipe();
	
		DummySshExceptionHandler handler = new DummySshExceptionHandler();
		obj.setSshExceptionHandler(handler);
		Exception exception = new Exception("dummy exception");

		// execute
		try {
			obj.handleException(exception);
		} finally {
			obj.close();
		}

		// test
		assertEquals(1, handler.throwables.size());
		assertEquals("dummy exception", handler.throwables.get(0).getMessage());
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshShellPipe#handleException(java.lang.Throwable)}.
	 */
	@Test
	public void testHandleException_null() {
		SshShellPipe obj = new SshShellPipe();
		
		obj.setSshExceptionHandler(null);
		
		NullPointerException npe = null;
		try{
			obj.handleException(new Exception());
		} catch (NullPointerException e) {
			npe = e;
		} finally {
			obj.close();
		}
		
		// Null pointer exception must not be thrown.
		assertNull(npe);
	}
}

class DummySshMessageHandler implements SshMessageHandler {
	public List<String> lines = new ArrayList<>();
	
	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.SshMessageHandler#onMessage(java.lang.String)
	 */
	@Override
	public void onMessage(String line) {
		lines.add(line);
	}
}

class DummySshExceptionHandler implements SshExceptionHandler {
	public List<Throwable> throwables = new ArrayList<>();
	/* (non-Javadoc)
	 * @see org.o3project.mlo.server.logic.SshExceptionHandler#onException(java.lang.Throwable)
	 */
	@Override
	public void onException(Throwable throwable) {
		throwables.add(throwable);
	}
}

