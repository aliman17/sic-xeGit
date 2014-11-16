package sic.asm.code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sic.machine.Registers;

/**
 * Podporni razred za predmet Sistemska programska oprema.
 * @author jure
 */
public class Code {
	
	public static final int MAX_ADDR = 100000;
	public static final int MAX_WORD = (1<<24)-1;  // TODO popravi, kjer to uporabljaš
	public static final int MAX_BYTE = (1<<8)-1;
	private Registers registers;
	
	protected String programName;
	protected int programStartAddress;
	public int programBegin;
	protected List<Node> program;  // each line in text file is one instruction in this list
	protected int programLength;
	protected Map<String, Integer> simbTable;
	
	protected int locCounterCurrent;
	protected int locCounterNext;
	
	public Code(){
		programName = "";
		program = new ArrayList<Node>();
		simbTable = new HashMap<String, Integer>();
		programBegin = 0;
		registers = new Registers();
	}
	
	public Registers getRegisters() {
		return registers;
	}

	public void setRegisters(Registers registers) {
		this.registers = registers;
	}

	public void begin() {
		locCounterNext = programStartAddress;
		locCounterCurrent = programStartAddress;
		
		// Reset base register
		//registers.setB(0);  TODO
	}
	
	public void end() {
	}
	
	// 2. prehod - razreševanje simbolov
	public void resolve() throws SemanticError {
		begin();
		for (Node node : program) {
			node.enter(this);   // povecaj next
			node.resolve(this);
			node.leave(this);   // posodobi current
		}
		programLength = locCounterNext - programStartAddress;
		end();
	}
	
	public void append(Node node) throws SemanticError {
		node.enter(this);
		program.add(node);
		node.activate(this);
		node.leave(this);
	}

	public void print() {
		if (program == null){
			System.out.println("Program je prazen.");
			return;
		}
		System.out.println(dumpCode());
	}
	
	public void defineSymbol(String symbol, int value){
		simbTable.put(symbol, value);
	}
	
	public int resolveSymbol (String symbol){
		return simbTable.get(symbol);
	}
	
	// generating raw code
	public byte[] emitCode(){
		byte[] code = new byte[programLength];
		int pos = 0;
		for(Node i : program){
			i.emitCode(code, pos);
			pos += i.length;
		}
		return code;
	}
	
	// generating OBJ file
	public void dumpText() throws SemanticError {
		begin();
		for (Node node : program) {
			node.enter(this);
			node.resolve(this);
			node.leave(this);
		}
		programLength = locCounterNext - programStartAddress;
		end();
	}
	
	// return all labels
	public String dumpSymbols(){
		String str = "\nSymbol Table";
		for (String i : simbTable.keySet()){
			str += "\n" + String.format("%-10s %-10s", i, Integer.toHexString(simbTable.get(i)));
		}
		return str.toUpperCase();
	}
	
	// Log datoteka
	public String dumpCode(){
		begin();
		StringBuffer buf = new StringBuffer();
		buf.append("\nCode");
		for (Node node : program) {
			node.enter(this);
			//memory position
			buf.append("\n" + String.format("%6s", Integer.toHexString(locCounterCurrent)).replace(' ', '0').toUpperCase() );
			buf.append("  ");
			// append code
			StringBuffer b = new StringBuffer();
			node.emitText(b);
			buf.append(paddingWord(b.toString(), " ", 6));
			// append text
			buf.append(String.format("   %-8s %s", (node.label != null) ? node.label : "", node.toString()));
			node.leave(this);
		}
		end();
		return buf.toString().toUpperCase();
	}

	// Object file
	public String emitText(){
		begin();
		// header
		String obj = textHeader();
		
		// body
		obj += textBody();
		
		// footer
		obj += textFooter();
		end();
		return obj.toUpperCase();
	}
	
	public String textHeader(){
		int prLen = programName.length();
		
		// name
		String obj = "H" + programName;
		for (int i = 0; i < 6 - prLen; i++)
			obj += " ";
		
		// starting address
		String start = Integer.toHexString(this.programStartAddress);
		obj += paddingWord(start, "0", 6);
		
		// length
		String codelen = Integer.toHexString(this.programLength);
		obj += paddingWord(codelen, "0", 6);
		
		return obj;
	}
	
	public String textBody(){
		this.begin();
		
		StringBuffer objectCode = new StringBuffer();
		int recordLenPosition = 0;
		String record = ""; 
		boolean beginT = true;
		int taddress = this.locCounterCurrent;
		for (Node i : program){
			if (i instanceof Storage){
				switch (i.mnemonic.name){
				case "RESW":
				case "RESB":
					continue;
				default:
				}
			}
			i.enter(this);
			if (record.length() + 2*i.length >= 60){
				// staro popravimo
				writeRecordT(objectCode, taddress, record);
				beginT = true;
			}
			if (beginT){
				taddress = this.locCounterCurrent;
				// zapisemo T+address
				beginT = false;
				record = "";
			}
			StringBuffer b = new StringBuffer(); 
			i.emitText(b);
			record += b.toString();
			i.leave(this);
		}
		if(record.length() > 0){
			// staro popravimo
			writeRecordT(objectCode, taddress, record);
		}
		this.end();
		return objectCode.toString();
	}
	
	public void writeRecordT(StringBuffer objectCode, int taddress, String record){
		// priprava na novo
		objectCode.append("\nT" 
				+ paddingWord(Integer.toHexString(taddress), "0", 6) 
				+ paddingWord(Integer.toHexString(record.length()/2), "0", 2)); 
		objectCode.append(record);
	}
	
	public String textFooter() {
		String obj = "\nE";
		String begin = Integer.toHexString(this.programBegin);
		obj += paddingWord(begin, "0", 6);
		return obj;
	}

	public static String paddingWord(String str, String fill, int length) {
		String obj = "";
		int len = str.length();
		for (int i = 0; i < length-len;i++)
			obj += fill;
		return obj + str;
	}
	
}
