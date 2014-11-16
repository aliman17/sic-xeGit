package sic.asm.code;

import sic.asm.mnemonics.Mnemonic;

/*
 * n -> operand ali label
 * r -> r1
 * rn -> r1, secondOperand ali r1, label
 * rr
 */

public class InstructionF2 extends Node {
	
	public String symbol2;   // na drugem mestu
	public int operand;
	public int reg1;
	public int reg2;
	
	public InstructionF2(Mnemonic mnemonic, int operand) {
		super(mnemonic);
		this.reg1 = operand;
		this.reg2 = 0;
		this.length = 2;
	}
	
	public InstructionF2(Mnemonic mnemonic, int operand1, int operand2) {
		super(mnemonic);
		this.reg1 = operand1;
		this.reg2 = operand2;
		this.operand = reg1 << 4 & 0xf0 | reg2 & 0x0f;
		this.length = 2;
	}
	
	public InstructionF2(Mnemonic mnemonic, int register, String symbol) {
		super(mnemonic);
		this.reg1 = register;
		this.symbol2 = symbol;
		this.length = 2;
	}
	
	@Override
	public boolean resolve(Code code) throws SemanticError{
		if (symbol2 != null){
			int reg2 = code.simbTable.get(this.symbol2); 
			if (reg2 >= 0 && reg2 <= (1<<4)-1)
				this.reg2 = reg2;
			else
				throw new SemanticError("Line " + this.mnemonic.line + "; The number "+this.reg2+" is not in range of 4 bits");
		}
		this.operand = reg1 << 4 & 0xf0 | this.reg2 & 0x0f;
		return false;
	}

	@Override
	public void emitCode(byte[] data, int pos) {
		data[pos] = (byte) this.mnemonic.opcode;
		data[pos+1] = (byte) operand;
	}
	
	@Override
	public void emitText(StringBuffer buf){
		emitTextGeneral(buf, 2);
	}


}
