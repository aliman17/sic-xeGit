package sic.asm.code;

import sic.asm.mnemonics.Mnemonic;

public class InstructionF4 extends Node {

	public String symbol;
	public int value;
	
	public InstructionF4(Mnemonic mnemonic) {
		super(mnemonic);
		this.length = 4;
	}

	public InstructionF4(Mnemonic mnemonic, int operand, int ni, boolean x) {
		super(mnemonic);
		this.value = operand;
		this.ni = ni;
		this.length = 4;
		if (x)
			xbpe = 8;
	}
	
	public InstructionF4(Mnemonic mnemonic, String operand, int ni, boolean x) {
		super(mnemonic);
		this.symbol = operand;
		this.ni = ni;
		this.length = 4;
		if (x)
			xbpe = 8;
	}

	public boolean resolve(Code code) throws SemanticError{
		if (symbol == null)
			return false;
		value = code.simbTable.get(symbol); 
		if(!(value >= 0 && value < (1<<20)))
			throw new SemanticError("The number is not in range of 20 bits");
		return false;
	}
	
	@Override
	public void emitCode(byte[] data, int pos) {
		data[pos] = (byte) (this.mnemonic.opcode + ni);
		data[pos+1] = (byte) (xbpe << 4 | (value & 0xf0000) >> 16);
		data[pos+2] = (byte) ((value & 0xff00) >> 8);
		data[pos+3] = (byte) (value & 0xff);

	}
	
//	@Override
//	public void emitText(StringBuffer buf){
//		byte[] data = new byte[4];
//		String str = "";
//		emitCode(data, 0);
//		for (byte i : data){
//			int j = i & 0xff;
//			str += (Integer.toHexString(j));
//			str = Code.paddingWord(str, "0", 8);
//		}
//		buf.append(str);
//	}
//	
	@Override
	public void emitText(StringBuffer buf){
		emitTextGeneral(buf, 4);
	}

}
