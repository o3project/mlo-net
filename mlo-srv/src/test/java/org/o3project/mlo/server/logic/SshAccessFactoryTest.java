package org.o3project.mlo.server.logic;

import static org.junit.Assert.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.o3project.mlo.server.impl.logic.ConfigProviderImpl;

public class SshAccessFactoryTest {

	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshAccessFactory#setConfigProvider(org.o3project.mlo.server.logic.ConfigProvider)}.
	 */
	@Test
	public void testSetConfigProvider() {
		SshAccessFactory obj = new SshAccessFactory();
		try{
			obj.setConfigProvider(new ConfigProviderImpl("default.mlo-srv.properties", "/etc/mlo/mlo-srv.properties"));
		} catch (Throwable th) {
			fail();
		}
	}

	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshAccessFactory#init()}.
	 */
	@Test
	public void testInit() {
		SshAccessFactory obj = new SshAccessFactory();
		obj.init();
		assertNotNull(obj.executorService);
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshAccessFactory#destroy()}.
	 * @throws InterruptedException 
	 */
	@Test
	public void testDestroy() throws InterruptedException {
		SshAccessFactory obj = new SshAccessFactory();
		obj.init();
		
		final CountDownLatch latch = new CountDownLatch(1);
		SshShellTask sshShellTask = new SshShellTask() {
			@Override
			public Void call() {
				while(!Thread.currentThread().isInterrupted()) {
					latch.countDown();
				}
				return null;
			}
		};
		
		Future<Void> future = obj.executorService.submit(sshShellTask);
		
		// wait until the task is started.
		latch.await(60, TimeUnit.SECONDS);

		// execute
		obj.destroy();
	
		// test
		assertNull(obj.executorService);
		assertEquals(true, future.isDone());
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshAccessFactory#destroy()}.
	 */
	@Test
	public void testDestroy_null() {
		SshAccessFactory obj = new SshAccessFactory();
		try{
			obj.destroy();
			assertNull(obj.executorService);
		} catch (Exception e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshAccessFactory#destroy()}.
	 * @throws InterruptedException 
	 */
	@Test
	public void testDestroy_intereputed() throws InterruptedException {
		final SshAccessFactory obj = new SshAccessFactory();
		obj.init();
		
		final CountDownLatch taskBeginLatch = new CountDownLatch(1);
		SshShellTask sshShellTask = new SshShellTask() {
			@Override
			public Void call() {
				while (!Thread.currentThread().isInterrupted()) {
					taskBeginLatch.countDown();
				}
				
				long start = System.currentTimeMillis();
				long duration = 10 * 1000L;
				long current = start;
				do {
					current = System.currentTimeMillis();
				} while (current < start + duration);
				System.out.println("task ends.");
				return null;
			}
		};
	
		final CountDownLatch destroyBeginLatch = new CountDownLatch(1);
		final CountDownLatch destroyEndLatch = new CountDownLatch(1);
		Runnable destroyTask = new Runnable() {
			@Override
			public void run() {
				try {
					destroyBeginLatch.countDown();
					obj.destroy();
				} finally {
					destroyEndLatch.countDown();
				}
			}
		};

		Future<Void> future = obj.executorService.submit(sshShellTask);
		
		// wait until the task is started.
		taskBeginLatch.await(60, TimeUnit.SECONDS);

		// execute
		Thread destroyThread = new Thread(destroyTask, "destroy task");
		destroyThread.start();
		
		// wait until the destroy task is started.
		destroyBeginLatch.await(60, TimeUnit.SECONDS);
		
		// interrupt awaitTermination
		destroyThread.interrupt();
		
		// wait until the destroy task is ended.
		destroyEndLatch.await(60, TimeUnit.SECONDS);
	
		// test
		assertNull(obj.executorService);
		assertEquals(false, future.isDone());
	}
	
	/**
	 * Test method for {@link org.o3project.mlo.server.logic.SshAccessFactory#create()}.
	 */
	@Test
	public void testCreate() {
		SshAccessFactory obj = new SshAccessFactory();
		
		SshAccess sshAccess = obj.create();
		assertNotNull(sshAccess);
	}

}
