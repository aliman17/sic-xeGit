package device;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;


public class FileDevice extends Device {
	
	private String name;

	public FileDevice(String name) {
		super();
		this.name = name;
	}
	
	public byte read() throws IOException{
		RandomAccessFile input = new RandomAccessFile(name, "r");	// read only
		int input1 = input.read();
		byte b = (byte)(input1 & 0xff);
		if (b == -1)
			return 0;
		return b;
		
	}
	
	public void write(byte b) throws IOException{
		RandomAccessFile output = new RandomAccessFile(name, "rw");	// create file in not existed
		output.write(b);
	}
}
