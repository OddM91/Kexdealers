package example;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;

import bus.Message;
import bus.MessageBus;
import bus.Recipients;
import ecs.AbstractSystem;
import ecs.Component;
import ecs.EntityController;
import loaders.BlueprintLoader;

public class NetworkSystem extends AbstractSystem implements Runnable{
	
	public static final int CONNECT = 0;
	public static final int DISCONNECT = 1;
	
	public volatile boolean running = false;
	
	private Socket socket = null;
	private InputStream inputStream;
	private OutputStream outputStream;
	
	private EncodeDelegator encodeDelegator;
	private DecodeDelegator decodeDelegator;
	
	private HashSet<Component> componentBuffer;
	
	public NetworkSystem(MessageBus messageBus, EntityController entityController) {
		super(messageBus, entityController);
		
		encodeDelegator = new EncodeDelegator();
		decodeDelegator = new DecodeDelegator();
		
		componentBuffer = new HashSet<Component>();
	}
	
	public void run() {
		running = true;
		
		try (DataInputStream stream = new DataInputStream(inputStream)) {
			while(running) {
				// Maybe check if stream is actually opened?
				decodeDelegator.delegate(stream, componentBuffer);
			}
		}catch(IOException x) {
			x.printStackTrace();
		}
		
		// If a thread is waiting on this thread to exit, it will get notified here
		// Acquiring this object's lock is required before calling notifyAll()
		synchronized(this) {
			this.notifyAll();
		}
	}
	
	public void update() {
		super.timeMarkStart();
		
		// message queue 
		Message message;
		while((message = messageBus.getNextMessage(Recipients.NETWORK_SYSTEM)) != null) {
			
			Object[] args = message.getArgs();
			
			switch(message.getBehaviorID()) {
			case CONNECT:
				boolean success = connectToServer(
						(String) args[0], 	// Host name
						(int) args[0], 		// Port
						(String) args[2]);	// Login name
				if(success) {
					message.setComplete();
				}
				break;
			case DISCONNECT: 
				disconnectFromServer();
				break;
			default: System.err.println("Network operation not implemented");
			}
		}
		
		super.timeMarkEnd();
	}
	
	public void cleanUp() {
		
	}
	
	public void loadBlueprint(ArrayList<String> blueprint) {
		// mostly useless here
	}
	
	// Returns false if connection failed
	private boolean connectToServer(String address, int port, String username) {
		try {
			System.out.println("Trying to connect to server...");
			socket = new Socket(address, port);
			System.out.println("...Connected to server: " +address +":" +port);
			// Setting up streams
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();
			// User name verification
			DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			System.out.println(bufferedReader.readLine());
			dataOutputStream.writeChars(username +"\n");
			// ===> close top level streams?
			// This socket will get rejected by the server if the user name is "bad" in any way
		}catch (ConnectException x) {
			System.err.println("Firewall blocking or no server listening");
		}catch (UnknownHostException e) {
			System.err.println("Server address unknown: " +e.getMessage());
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		return (socket != null) ? true : false;
	}
	
	private void disconnectFromServer() {
		try {
			running = false;
			this.wait();
		}catch(InterruptedException x) {
			System.err.println("Error while disconnecting from server (interrupted while waiting for network_system to exit)");
			x.printStackTrace();
		}
		try {
			inputStream.close();
			outputStream.close();
			socket.close();
		}catch(IOException x) {
			System.err.println("Error while disconnecting from server (while closing Socket/Streams)");
			x.printStackTrace();
		}
		
	}
	
	// TODO: Fetch data from message return?
	public ArrayList<String> loadInstanceFromServer() {
		return BlueprintLoader.splitIntoLines("example\nexample");
	}
	
}
