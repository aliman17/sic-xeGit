package sic.machine;
import java.io.IOException;
import java.io.Reader;

public class Utils {

	public Utils() {
		// TODO Auto-generated constructor stub
	}

	public static String readString(Reader r, int len) {

		String niz = "";
		for (int i = 0; i < len; i++) {
			try {
				niz += (char) r.read();
			} catch (IOException e) {
				System.out.println("ERROR: Utils.readString.");
			}
			;
		}
		
		return niz;
	}

	public static int readByte(Reader r) {
		try {
			return Character.getNumericValue((char) r.read()) << 4
					| Character.getNumericValue((char) r.read());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR: Utils.readByte.");
			return -1;
		}
	}

	public static int readWord(Reader r) {
		try {
			int w = Character.getNumericValue((char) r.read()) << 20;
			w = w | Character.getNumericValue((char) r.read()) << 16;
			w = w | Character.getNumericValue((char) r.read()) << 12;
			w = w | Character.getNumericValue((char) r.read()) << 8;
			w = w | Character.getNumericValue((char) r.read()) << 4;
			w = w | Character.getNumericValue((char) r.read()) ;
			return w;
		} catch (IOException e) {
			System.out.println("ERROR: Utils.readWord.");
			return -1;
		}
	}
}
