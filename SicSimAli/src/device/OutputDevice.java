package device;

import java.io.IOException;
import java.io.OutputStream;

public class OutputDevice extends Device {

	private OutputStream output;

	public OutputDevice(OutputStream output) {
		// TODO Auto-generated constructor stub
		this.output = output;
	}

	public void write(byte b) throws IOException {
		output.write(b);
	}
}
