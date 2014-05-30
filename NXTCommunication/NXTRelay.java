import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.robotics.Color;
import lejos.util.Delay;


public class NXTRelay {
	
	private USBComNXT usb;
	private volatile String toNXT;
	private volatile String toPC;
	private ColorSensor cs = new ColorSensor(SensorPort.S1);
	
	public NXTRelay(){
		toPC = "";
		toNXT = "";
		handleUSB();
		handleBT();
		while(true){
			if(usb.isConnected() && BTCom.getIsConnected()){
				cs.setFloodlight(Color.BLUE);
			} else if(usb.isConnected() || BTCom.getIsConnected()){
				cs.setFloodlight(Color.GREEN);
			} else {
				cs.setFloodlight(Color.RED);
			}
			Delay.msDelay(1000);
		}
	}
	
	private void handleUSB(){
		Runnable r = new Runnable(){
			public void run() {
				usb = USBComNXT.getInstance();
				while(true){
					while(usb.isConnected()){
						usb.sendString(toPC);
						toNXT = usb.receiveString();
						LCD.drawString("To NXT", 0, 4);
						LCD.drawString(toNXT, 0, 5);
					}
				}
			}
		};
		Thread t = new Thread(r);
		t.start();
	}

	private void handleBT(){
		Runnable r = new Runnable(){
			public void run() {
				BTCom.connect();
				while(true){
					while(BTCom.getIsConnected()){
						toPC = BTCom.recieveData();
						LCD.drawString("To PC", 0, 2);
						LCD.drawString(toPC, 0, 3);
						BTCom.sendData(toNXT);
					}
				}				
			}
		};
		Thread t = new Thread(r);
		t.start();
	}
	
	public static void main(String[] args) {
		new NXTRelay();
	}
}
