import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.LCD;
import lejos.nxt.comm.USB;
import lejos.nxt.comm.USBConnection;


public class USBCom {
	
	private static USBCom instance;
	private USBConnection con;
	private DataOutputStream dos;
	private DataInputStream dis;
	private boolean isConnected;
	
	private USBCom(){
		connect();
	}
	
	public static USBCom getInstance(){
		if(instance == null) instance = new USBCom();
		return instance;
	}
	
	public boolean isConnected(){
		return isConnected;
	}
	
	public synchronized void sendString(String output){
		try {
			dos.writeUTF(output);
			dos.flush();
		} catch (IOException e) {
			isConnected = false;
			LCD.drawString("Failed to send", 0, 0);
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
			LCD.drawString("Failed to receive", 0, 0);
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
			LCD.drawString("Failed to send", 0, 0);
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
			LCD.drawString("Failed to receive", 0, 0);
			stopConnection();
			connect();
			return -1;
		}
	}
	
	private synchronized void connect(){
		LCD.drawString("waiting", 0, 0);
		con = USB.waitForConnection();
		dos = con.openDataOutputStream();
		dis = con.openDataInputStream();
		LCD.drawString("Connected", 0, 0);
		isConnected = true;
	}
	
	private synchronized void stopConnection(){
		try {
			dos.close();
			dis.close();
			con.close();
		} catch (IOException e) {
			LCD.drawString("Failed to close", 0, 0);
		}
	}
}
