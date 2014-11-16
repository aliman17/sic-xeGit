package sic.machine;
import java.nio.ByteBuffer;

/**
 * 
 */

/**
 * @author Ales
 *
 */
public class Memory {
	private static final int MAX_ADDRESS = 1048576; // 2^20
	private byte[] pomnilnik = new byte[MAX_ADDRESS];
	
	public int getByte(int addr){
		/*
		 * AND s & 0xff - we want unsigned numbers
		 */
		if (inMemRange(addr)){
			return (int)pomnilnik[addr] & 0xff;  
		}
		else 
			System.out.println("Not valid address");
		return -1;
	}
	
	public void setByte(int addr, int val){
		
		// valid addr?
		if (inMemRange(addr)){
			
			// int -> 4 bytes
			byte[] bytes = ByteBuffer.allocate(4).putInt(val).array();
			
			// set last byte in int -> byte 4
			pomnilnik[addr] = bytes[3];	
		}
		else 
			System.out.println("Not valid address");
			
	}
	
	public int getWord(int addr){
		/*
		 * call getByte 3 times
		 * concatenate 4 pieces into one int
		 * 
		 * << is shift
		 */
		if(inMemRange(addr)){
			return 0x00 << 24 | getByte(addr) << 16 | getByte(addr+1) << 8 | getByte(addr+2);
		}
		else 
			System.out.println("Not valid address");
		
		// NOT DONE CORRECTLY JET
		return -1;
	}
	
	public void setWord(int addr, int value) {
		
		if (inMemRange(addr)){
		
			// int -> byte array
			byte[] bytes = ByteBuffer.allocate(4).putInt(value).array();
			
			// last 3 bytes write into memory
			for (int i = 0; i < 3; i++) {
				pomnilnik[addr+i] = bytes[i+1];		// i+1, cuz first byte in int is useless -> we have 3byte word
			}
		}
		else
			System.out.println("Not valid address");
	}
	
	public boolean inMemRange(int addr) {
		
		if (addr >= MAX_ADDRESS || addr < 0){
			// if not valid address
			return false;
		}
		
		// if valid address
		return true;
	}
	
	/*
	 * We could add floating points
	 */
	
}
