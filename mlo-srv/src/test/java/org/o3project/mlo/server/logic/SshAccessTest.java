package org.o3project.mlo.server.logic;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.PipedOutputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.o3project.mlo.server.impl.logic.ConfigProviderImpl;

public class SshAccessTest {

	
	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshAccess#setSshShellTask(org.o3project.mlo.server.logic.SshShellTask)}.
	 */
	@Test
	public void testSetSshShellTask() {
		SshAccess obj = new SshAccess();
		SshShellTask sshShellTask = new SshShellTask();
		try{
			obj.setSshShellTask(sshShellTask);
		} catch (Throwable th) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshAccess#setConfigProvider(org.o3project.mlo.server.logic.ConfigProvider)}.
	 */
	@Test
	public void testSetConfigProvider() {
		SshAccess obj = new SshAccess();
		ConfigProvider configProvider = new ConfigProviderImpl("", "");
		try{
			obj.setConfigProvider(configProvider);
		} catch (Throwable th) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshAccess#setExecutor(java.util.concurrent.ExecutorService)}.
	 */
	@Test
	public void testSetExecutor() {
		SshAccess obj = new SshAccess();
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		try{
			obj.setExecutor(executorService);
		} catch (Throwable th) {
			fail();
		} finally {
			executorService.shutdownNow();
			executorService = null;
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshAccess#setExecutor(java.util.concurrent.ExecutorService)}.
	 */
	@Test
	public void testSetSshShellPipe() {
		SshAccess obj = new SshAccess();
		SshShellPipe sshShellPipe = new SshShellPipe();
		try{
			obj.setSshShellPipe(sshShellPipe);
		} catch (Throwable th) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshAccess#open()}.
	 */
	@Test
	public void testOpen() {
		SshAccess obj = new SshAccess(); 
		final CountDownLatch countDownLatch = new CountDownLatch(1);
		SshShellTask sshShellTask = new SshShellTask() {
			
			@Override
			public Void call() {
				countDownLatch.countDown();
				return null;
			}
		};
		
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		ConfigProvider configProvider = new ConfigProviderImpl("default.mlo-srv.properties", "/etc/mlo/mlo-srv.properties");
		obj.setSshShellTask(sshShellTask);
		obj.setExecutor(executorService);
		obj.setConfigProvider(configProvider);
		obj.setSshShellPipe(new SshShellPipe());
		
		assertTrue(obj.open("s1", null, null));
		
		try {
			boolean isCalled = countDownLatch.await(30 , TimeUnit.SECONDS);
			assertTrue(isCalled);
		} catch (InterruptedException e) {
			fail();
		} finally {
			executorService.shutdown();
			obj.close();
			executorService = null;
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshAccess#open()}.
	 */
	@Test
	public void testOpen_exception() {
		SshAccess obj = new SshAccess(); 
		final CountDownLatch countDownLatch = new CountDownLatch(1);
		SshShellTask sshShellTask = new SshShellTask() {
			@Override
			public Void call() {
				return null;
			}
		};
		
		final SshExceptionHandler sshExceptionHandler = new SshExceptionHandler() {
			@Override
			public void onException(Throwable e) {
				countDownLatch.countDown();
			}
		};
		
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		ConfigProvider configProvider = new ConfigProviderImpl("default.mlo-srv.properties", "/etc/mlo/mlo-srv.properties");
		obj.setSshShellTask(sshShellTask);
		obj.setExecutor(executorService);
		obj.setConfigProvider(configProvider);
		obj.setSshShellPipe(new SshShellPipe(){
			public void connect() throws IOException {
				throw new IOException();
			}
		});
		
		assertFalse(obj.open("s1", null, sshExceptionHandler));
		
		try {
			boolean isCalled = countDownLatch.await(30 , TimeUnit.SECONDS);
			assertTrue(isCalled);
		} catch (InterruptedException e) {
			fail();
		} finally {
			executorService.shutdown();
			obj.close();
			executorService = null;
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshAccess#open()}.
	 */
	@Test
	public void testOpen_twice() {
		SshAccess obj = new SshAccess(); 
		SshShellTask sshShellTask = new SshShellTask() {
			@Override
			public Void call() {
				return null;
			}
		};
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		ConfigProvider configProvider = new ConfigProviderImpl("default.mlo-srv.properties", "/etc/mlo/mlo-srv.properties");
		obj.setSshShellTask(sshShellTask);
		obj.setExecutor(executorService);
		obj.setConfigProvider(configProvider);
		obj.setSshShellPipe(new SshShellPipe());

		try {
			assertTrue(obj.open("s1", null, null));
			assertFalse(obj.open("s1", null, null));
		} finally {
			executorService.shutdown();
			obj.close();
			executorService = null;
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshAccess#close()}.
	 */
	@Test
	public void testClose() {
		SshAccess obj = new SshAccess(); 
		final CountDownLatch countDownLatch = new CountDownLatch(10);
		SshShellTask sshShellTask = new SshShellTask() {
			
			@Override
			public Void call() {
				while (!Thread.interrupted()) {
					countDownLatch.countDown();
				}
				return null;
			}
	
		};
		
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		ConfigProvider configProvider = new ConfigProviderImpl("default.mlo-srv.properties", "/etc/mlo/mlo-srv.properties");
		obj.setSshShellTask(sshShellTask);
		obj.setExecutor(executorService);
		obj.setConfigProvider(configProvider);
		obj.setSshShellPipe(new SshShellPipe());

		obj.open("s1", null, null);
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			fail();
		}
		
		executorService.shutdown();
		assertTrue(obj.close());
		
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshAccess#close()}.
	 */
	@Test
	public void testClose_twice() {
		SshAccess obj = new SshAccess(); 
		final CountDownLatch countDownLatch = new CountDownLatch(10);
		SshShellTask sshShellTask = new SshShellTask() {
			
			@Override
			public Void call() {
				while (!Thread.interrupted()) {
					countDownLatch.countDown();
					System.out.println("Test thread is running.");
				}
				System.out.println("Test thread is intereputed!");
				return null;
			}
	
		};
		
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		ConfigProvider configProvider = new ConfigProviderImpl("default.mlo-srv.properties", "/etc/mlo/mlo-srv.properties");
		obj.setSshShellTask(sshShellTask);
		obj.setExecutor(executorService);
		obj.setConfigProvider(configProvider);
		obj.setSshShellPipe(new SshShellPipe());

		obj.open("s1", null, null);
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			fail();
		}
		executorService.shutdown();
		assertTrue(obj.close());
		assertFalse(obj.close());
		executorService = null;
		
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshAccess#close()}.
	 */
	@Test
	public void testClose_not_open() {
		SshAccess obj = new SshAccess(); 
		
		assertFalse(obj.close());
		
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshAccess#send(java.lang.String)}.
	 */
	@Test
	public void testSend() {
		SshAccess obj = new SshAccess(); 
		SshShellTask sshShellTask = new SshShellTask() {
			
			@Override
			public Void call() {
				while (!Thread.interrupted()) {
				}
				return null;
			}
		};
		
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		ConfigProvider configProvider = new ConfigProviderImpl("default.mlo-srv.properties", "/etc/mlo/mlo-srv.properties");
		obj.setSshShellTask(sshShellTask);
		obj.setExecutor(executorService);
		obj.setConfigProvider(configProvider);
		obj.setSshShellPipe(new SshShellPipe());

		assertTrue(obj.open("s1", null, null));
		assertTrue(obj.send("test"));
		assertTrue(obj.close());
		
		executorService.shutdownNow();
		executorService = null;
		
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshAccess#send(java.lang.String)}.
	 */
	@Test
	public void testSend_exception() {
		SshAccess obj = new SshAccess(); 
		final CountDownLatch countDownLatch = new CountDownLatch(1);
		SshShellTask sshShellTask = new SshShellTask() {
			@Override
			public Void call() {
				while (!Thread.interrupted()) {
				}
				return null;
			}
		};
		
		final SshExceptionHandler sshExceptionHandler = new SshExceptionHandler() {
			@Override
			public void onException(Throwable e) {
				countDownLatch.countDown();
			}
		};
		
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		ConfigProvider configProvider = new ConfigProviderImpl("default.mlo-srv.properties", "/etc/mlo/mlo-srv.properties");
		obj.setSshShellTask(sshShellTask);
		obj.setExecutor(executorService);
		obj.setConfigProvider(configProvider);
		obj.setSshShellPipe(new SshShellPipe(){
			public PipedOutputStream getAppStdinOutputStream() {
				return new PipedOutputStream();
			}
		});
		
		assertTrue(obj.open("s1", null, sshExceptionHandler));
		assertFalse(obj.send("test"));
		assertTrue(obj.close());
		
		boolean isCalled;
		try {
			isCalled = countDownLatch.await(30 , TimeUnit.SECONDS);
			assertTrue(isCalled);
		} catch (InterruptedException e1) {
			fail();
		}

		executorService.shutdownNow();
		executorService = null;
		
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshAccess#send(java.lang.String)}.
	 */
	@Test
	public void testSend_not_open() {
		SshAccess obj = new SshAccess(); 

		assertFalse(obj.send("test"));
		assertFalse(obj.close());
		
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshAccess#send(java.lang.String)}.
	 */
	@Test
	public void testSend_close() {
		SshAccess obj = new SshAccess(); 
		SshShellTask sshShellTask = new SshShellTask() {
			
			@Override
			public Void call() {
				while (!Thread.interrupted()) {
					System.out.println("Test thread is running.");
				}
				System.out.println("Test thread is intereputed!");
				return null;
			}
		};
		
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		ConfigProvider configProvider = new ConfigProviderImpl("default.mlo-srv.properties", "/etc/mlo/mlo-srv.properties");
		obj.setSshShellTask(sshShellTask);
		obj.setExecutor(executorService);
		obj.setConfigProvider(configProvider);
		obj.setSshShellPipe(new SshShellPipe());

		assertTrue(obj.open("s1", null, null));
		assertTrue(obj.close());
		assertFalse(obj.send("test"));
		assertFalse(obj.close());
		
		executorService.shutdownNow();
		executorService = null;
		
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshAccess#createSshNodeConfig()}.
	 */
	@Test
	public void testCreateSshNodeHost() {
		SshAccess obj = new SshAccess(); 
		ConfigProvider configProvider = new ConfigProviderImpl("default.mlo-srv.properties", "/etc/mlo/mlo-srv.properties");
		obj.setConfigProvider(configProvider);
		SshNodeConfig sshNodeConfig = obj.createSshNodeConfig();
		assertEquals("127.0.0.1", sshNodeConfig.getHost());
		assertEquals(22, sshNodeConfig.getSshPort());
		assertEquals("developer", sshNodeConfig.getUserid());
		assertEquals("developer", sshNodeConfig.getPassword());
		assertEquals(120000, sshNodeConfig.getSshSessionTimeout());
		assertEquals(120000, sshNodeConfig.getSshChannelTimeout());
		assertEquals("mlo-net/lagopus-docker/workspace", sshNodeConfig.getLdWorkspaceDirpash());
	}
}
