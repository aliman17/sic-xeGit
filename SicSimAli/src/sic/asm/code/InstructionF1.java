package sic.asm.code;

import sic.asm.mnemonics.Mnemonic;

public class InstructionF1 extends Node {
	
	// no operand
	public InstructionF1(Mnemonic mnemonic) {
		super(mnemonic);
		this.length = 1;
	}
	
	public boolean resolve(Code code){
		return false;
	}

	@Override
	public void emitCode(byte[] data, int pos) {		
		data[pos] = (byte) this.mnemonic.opcode;
	}
	
	// obj datoteka
	@Override
	public void emitText(StringBuffer buf){
		emitTextGeneral(buf, 1);
	}

}
