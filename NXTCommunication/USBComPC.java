import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

import lejos.pc.comm.NXTCommLogListener;
import lejos.pc.comm.NXTConnector;


public class USBCom {
	
	private static volatile USBCom instance;
	private NXTConnector con;
	private DataInputStream dis;
	private DataOutputStream dos;
	private boolean isConnected = false;
	
	
	private USBCom(){
		con = new NXTConnector();
		con.addLogListener(new NXTCommLogListener(){
			public void logEvent(String message) {
				System.out.println("USBCom Log.listener: " + message);
			}

			public void logEvent(Throwable throwable) {
				System.out.println("USBCom Log.listener - stack trace: ");
				throwable.printStackTrace();
			}
		});
		//connect();
	}
	
	public synchronized void sendString(String output){
		try {
			dos.writeUTF(output);
			dos.flush();
		} catch (IOException e) {
			isConnected = false;
			System.out.println("Failed to send" + output);
			stopConnection();
			connect();
		}
	}
	
	public synchronized String receiveString(){
		String s = " ";
		try {
			s = dis.readUTF();
			return s;
		} catch (IOException e) {
			isConnected = false;
			System.out.println("Failed to receive");
			stopConnection();
			connect();
			return "FAIL";
		}
	}
	
	public synchronized void sendInt(int output){
		try {
			dos.writeInt(output);
			dos.flush();
		} catch (IOException e) {
			isConnected = false;
			System.out.println("Failed to send" + output);
			stopConnection();
			connect();
		}
	}
	
	public synchronized int receiveInt(){
		int i = -1;
		try {
			i = dis.readInt();
			return i;
		} catch (IOException e) {
			isConnected = false;
			System.out.println("Failed to receive");
			stopConnection();
			connect();
			return -1;
		}
	}
	
	public synchronized void sendFloat(float output){
		try {
			dos.writeFloat(output);
			dos.flush();
		} catch (IOException e) {
			isConnected = false;
			System.out.println("Failed to send" + output);
			stopConnection();
			connect();
		}
	}
	
	public synchronized float receiveFloat(){
		float i = -1;
		try {
			i = dis.readInt();
			return i;
		} catch (IOException e) {
			isConnected = false;
			System.out.println("Failed to receive");
			stopConnection();
			connect();
			return -1;
		}
	}
	
	private static ReentrantLock getInstanceLock = new ReentrantLock();
	
	public static USBCom getInstance(){
		if(instance == null){
			getInstanceLock.lock();
				if(instance == null){
					instance = new USBCom();
				}
			getInstanceLock.unlock();
		}
		return instance;
	}
	
	public boolean isConnected(){
		return isConnected;
	}
	
	public synchronized void connect(){
		if (!con.connectTo("usb://")){
			System.err.println("No NXT found using USB");
			isConnected = false;
			return;
		}
		dis = new DataInputStream(con.getInputStream());
		dos = new DataOutputStream(con.getOutputStream());
		isConnected = true;
	}
	
	public synchronized void stopConnection(){
		try {
			dis.close();
			dos.close();
			System.out.println("Closed data streams");
		} catch (IOException ioe) {
			System.err.println("IO Exception Closing connection");
		}
		try {
			con.close();
			System.out.println("Closed connection");
		} catch (IOException ioe) {
			System.err.println("IO Exception Closing connection");
		}
	}

}
