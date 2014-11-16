package sic.asm.code;

import sic.asm.mnemonics.Mnemonic;
import sic.asm.parsing.SyntaxError;
import sic.machine.Registers;

public class InstructionF3 extends Node {

	public String symbol;
	public int value;
	public boolean index = false;

	/**
	 * Doesn't support SIC jet
	 */
	
	public InstructionF3(Mnemonic mnemonic) {
		/*
		 * RSUB je tukaj
		 */
		super(mnemonic);
		this.length = 3;
	}

	public InstructionF3(Mnemonic mnemonic, String operand, int ni, boolean x) {
		super(mnemonic);
		this.symbol = operand;
		this.ni = ni;
		this.index = x;
		this.length = 3;
	}
	
	public InstructionF3(Mnemonic mnemonic, int number, int ni, boolean x) {
		super(mnemonic);
		this.value = number;
		this.ni = ni;
		this.index = x;
		this.length = 3;
	}

	@Override
	public boolean resolve(Code code) throws SemanticError{
		if (symbol == null)
			return false;
		
		if(code.simbTable.get(symbol) == null)
			throw new SemanticError("Line "+ this.mnemonic.line +": Label " + symbol + "  not specified");
		value = code.simbTable.get(symbol); 
		
		if (pcRelative(code))
			return false;
		
		if (baseRelative(code))
			return false;
		
		if (absolute(code))
			return false;
		throw new SemanticError("Can not address it relative nor absolute");

	}
	
	public boolean pcRelative(Code code){
		int tmp = value - code.locCounterNext;
		if (ni == 0)
			return betweenBounds(tmp, -16384, 16383, (tmp>>12)&0x7);
		else	
			return betweenBounds(tmp, -2048, 2047, 2);
	}
	
	public boolean baseRelative(Code code){
		int tmp = value - code.getRegisters().getB();
		if (ni == 0)
			return betweenBounds(tmp, 0, 16383, (tmp>>12)&0x7);
		else	
			return betweenBounds(tmp, 0, 32767, 4);
	}

	public boolean betweenBounds(int tmp, int l, int h, int xbpe1){
		if (tmp >= l && tmp <= h){
			value = tmp & 0xfff;
			xbpe = xbpe1 + (index ? 0x8 : 0);
			return true;
		}
		return false;
	}
	
	public boolean absolute(Code code) throws SemanticError{
		int absBound = (1<<12);
		if (ni == 0)
			absBound = absBound << 3;
		if (!(value >= 0 && value < absBound))
			throw new SemanticError("Absolute addressing ... error");
		xbpe = (index ? 0x8 : 0);
		return true;
	}
	
	@Override
	public void emitCode(byte[] data, int pos) {
		data[pos] = (byte) (this.mnemonic.opcode + ni);
		data[pos+1] = (byte) (xbpe << 4 | (value & 0xf00) >> 8);
		data[pos+2] = (byte) (value & 0xff);
	}
	
//	@Override
//	public void emitText(StringBuffer buf){
//		byte[] data = new byte[3];
//		String str = "";
//		emitCode(data, 0);
//		for (byte i : data){
//			int j = i & 0xff;
//			str += (Integer.toHexString(j));
//			str = Code.paddingWord(str, "0", 6);
//		}
//		buf.append(str);
//	}
	@Override
	public void emitText(StringBuffer buf){
		emitTextGeneral(buf, 3);
	}

	
	
}
