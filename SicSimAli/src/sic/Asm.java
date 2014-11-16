package sic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Scanner;

import sic.asm.code.Code;
import sic.asm.code.SemanticError;
import sic.asm.parsing.Parser;
import sic.asm.parsing.SyntaxError;
import sic.machine.Test;

/**
 * Podporni razred za predmet Sistemska programska oprema.
 * @author jure
 */
public class Asm {

	/**
	 * Read file
	 */
	public static String readFile(File file) {
	    byte[] buf = new byte[(int) file.length()];
	    try {
	    	InputStream s = new FileInputStream(file);
	    	try {
	    		s.read(buf);
			} finally {
	    		s.close();
	    	}
    	} catch (IOException e) {
    		return "";
	    }
	    return new String(buf);
	}

	public static void printByteData(Code code) {
		String data = "";
		System.out.println(data);
		for (byte i : code.emitCode()){
			System.out.printf("%c",(char)i);
		}
		System.out.println();
	}
	
	public static void writeFile(Code code, String file){
		Writer writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file), "utf-8"));
			writer.write(code.emitText());
			writer.flush();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} finally {
			try {writer.close();} catch (Exception ex) {}
		}
	}
	/**
	 * MAIN
	 * @throws InterruptedException 
	 */

	public static void main(String[] args) throws InterruptedException {
		Scanner sc = new Scanner(System.in);

		while (true){
		String input = "";
		
		System.out.print("Izberi primer, ki ga želiš assemblat: (/q)\n>");
		String primer = sc.next();
		
		switch (primer){
		case "1":
			input = readFile(new File("./src/sic/my_file.txt"));
			break;
		case "q":
			return;
		default:
		case "2":
			input = readFile(new File("./src/sic/easy.txt"));
			break;
		}
		
		// prvi prehod
		Parser parser = new Parser();
		Code code;
		try {
			code = parser.parse(input);
		} catch (SyntaxError e) {
			System.err.println(e);
			System.exit(1);
			return;
		} catch (SemanticError e) {
			System.err.println(e);
			System.exit(1);
			return;
		}
		
		// drugi prehod
		try {
			code.resolve();
		} catch (SemanticError e) {
			System.err.println(e);
			System.exit(1);
			return;
		}
		
		System.out.print("\nIzpisem log datoteko? (y/n)\n>");
		if(sc.next().compareTo("n") == 0)
			continue;
		Thread.sleep(300);
		System.out.println("Izpisujem ...");
		Thread.sleep(500);
		
		// print log
		code.print();
		
		System.out.print("\nIzpisem labele? (y/n)\n>");
		if(sc.next().compareTo("n") == 0)
			continue;
		Thread.sleep(300);
		System.out.println("Izpisujem ...");
		Thread.sleep(500);
		// print labels
		System.out.println(code.dumpSymbols());
		
		System.out.print("\nIzpisem surovo kodo? (y/n)\n>");
		if(sc.next().compareTo("n") == 0)
			continue;
		Thread.sleep(300);
		System.out.println("Izpisujem ...");
		Thread.sleep(500);
		// raw bytes
		printByteData(code);
		Thread.sleep(500);
		System.out.print("\nIzpisem objektno kodo? (y/n)\n>");
		if(sc.next().compareTo("n") == 0)
			return;
		Thread.sleep(300);
		System.out.println("Izpisujem ...");
		Thread.sleep(500);
		// print obj
		System.out.println("\nObject code\n"+code.emitText());
		
		Thread.sleep(500);
		System.out.println("\nZapisujem obj code v datoteko ...");
		Thread.sleep(500);
		writeFile(code, "./load&execute.txt");
		
		while(true){
			System.out.print("\nNadaljujem z izvedbo tega programa?(y/n/q)\n>");
			String inputS = sc.next();
			
			if(inputS.compareTo("n") == 0)
				break;
			else if(inputS.compareTo("q") == 0){
				Thread.sleep(500);
				System.out.println("\nNasvidenje ... see you :D");
				return;
			}
			else{
				Test test = new Test();
				String[] str = null;
				try {
					test.main(str);
				} catch (IOException e) {
					System.out.println("Test error");
				}
			}
		}
	}
		
	}

}
