package org.o3project.mlo.server.logic;

import static org.junit.Assert.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.o3project.mlo.server.endpoint.RemoteEndpoint;

public class SshShellPipeTest {

	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshShellPipe#SshShellPipe()}.
	 */
	@Test
	public void testSshShellPipe() {
		try{
			SshShellPipe obj = new SshShellPipe();
			obj.close();
		} catch (Throwable th) {
			fail();
		} finally {
		}
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
		final CountDownLatch countDownLatch = new CountDownLatch(1);
		obj.setSshMessageHandler(new SshMessageHandler() {
			@Override
			public void onMessage(String line) {
				countDownLatch.countDown();
			}
		});
			obj.handleMessage("test");
			try {
				assertTrue(countDownLatch.await(30 , TimeUnit.SECONDS));
			} catch (InterruptedException e) {
				fail();
			} finally{
				obj.close();
			}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshShellPipe#handleMessage(java.lang.String)}.
	 */
	@Test
	public void testHandleMessage_null() {
		SshShellPipe obj = new SshShellPipe();
		final CountDownLatch countDownLatch = new CountDownLatch(1);
		obj.setSshMessageHandler(new SshMessageHandler() {
			@Override
			public void onMessage(String line) {
				countDownLatch.countDown();
			}
		});
		try{
			obj.setSshMessageHandler(null);
			obj.handleMessage("test");
			assertFalse(countDownLatch.await(30 , TimeUnit.SECONDS));
		} catch (InterruptedException e) {
			fail();
		} finally {
			obj.close();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshShellPipe#handleException(java.lang.Throwable)}.
	 */
	@Test
	public void testHandleException() {
		SshShellPipe obj = new SshShellPipe();
		final CountDownLatch countDownLatch = new CountDownLatch(1);
		obj.setSshExceptionHandler(new SshExceptionHandler() {
			@Override
			public void onException(Throwable throwable) {
				countDownLatch.countDown();
			}
		});
		try{
			obj.handleException(new Exception());
			assertTrue(countDownLatch.await(30 , TimeUnit.SECONDS));
		} catch (InterruptedException e) {
			fail();
		} finally {
			obj.close();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshShellPipe#handleException(java.lang.Throwable)}.
	 */
	@Test
	public void testHandleException_null() {
		SshShellPipe obj = new SshShellPipe();
		final CountDownLatch countDownLatch = new CountDownLatch(1);
		obj.setSshExceptionHandler(new SshExceptionHandler() {
			@Override
			public void onException(Throwable throwable) {
				countDownLatch.countDown();
			}
		});
		try{
			obj.setSshExceptionHandler(null);
			obj.handleException(new Exception());
			assertFalse(countDownLatch.await(30 , TimeUnit.SECONDS));
		} catch (InterruptedException e) {
			fail();
		} finally {
			obj.close();
		}
	}

}
