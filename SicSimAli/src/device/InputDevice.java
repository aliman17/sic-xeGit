package device;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;


public class InputDevice extends Device {
	
	private InputStream stream;

	public InputDevice(InputStream stream) {
		// TODO Auto-generated constructor stub
		
		this.stream = stream;
	}

	public byte read() throws IOException{	
		byte b = (byte)(stream.read() & 0xff);		// AND must be because in failure it returns -1 which is 1111 1111 
		if (b == -1)
			return 0;	// like end of string
		return b;
	}
}
