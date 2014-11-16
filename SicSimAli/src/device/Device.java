package device;

import java.io.IOException;


public class Device {
	
	public byte[] devices;
	
	public Device() {
		super();
		devices = new byte[255];
	}

	public boolean test(){
		/*
		 * TD
		 */
		return true;
	}
	
	public byte read() throws IOException{
		/*
		 * WD
		 */
		return 0;
	}
	
	public void write(byte value) throws IOException{
		/*
		 * WD
		 */
		
	}
	
	public Device getDevice(int num){
		return null;
	}
	
	public void setDevice(int num, Device device){
		
	}

}
