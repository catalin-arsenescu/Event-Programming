package com.eventprogramming.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.eventprogramming.constants.Constants;

public class MultiThreadedServer implements Runnable {

	private ServerSocket serverSocket;
	private int port;
	private boolean isStopped;
    private ExecutorService threadPool = Executors.newFixedThreadPool(10);

	public MultiThreadedServer(int port) {
		this.port = port;
	}

	@Override
	public void run() {
		openServerSocket();
		while (!isStopped()) {
			Socket connectionSocket = null;
			try {
				connectionSocket = serverSocket.accept();
			} catch (IOException e) {
				if (isStopped()) {
					System.out.println("Server Stopped.");
					return;
				}
				throw new RuntimeException("Error accepting client connection", e);
			}
			
			threadPool.execute(new ServerWorker(connectionSocket));
		}
		
		threadPool.shutdown();
		System.out.println("Server Stopped.");
	}

	private synchronized boolean isStopped() {
		return this.isStopped;
	}

	public synchronized void stop() {
		this.isStopped = true;
		try {
			this.serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void openServerSocket() {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		MultiThreadedServer server = new MultiThreadedServer(Constants.MAIN_SERVER_PORT);
		new Thread(server).start();
	}
}
