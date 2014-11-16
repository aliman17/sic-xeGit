package sic.asm.code;

import sic.asm.mnemonics.Mnemonic;
import sic.asm.parsing.SyntaxError;
import sic.machine.Registers;

public class Directive extends Node {

	/**
	 * Opcode for instructions Directive
	 */
	//TODO: set real values
	public static final int NOBASE = 1;
	public static final int LTORG = 2;
	public static final int START = 3;
	public static final int END = 4;
	public static final int BASE = 5;
	public static final int EQU = 6;
	public static final int ORG = 7;
	
	/**
	 * Operand
	 */
	
	public int value;
	public String symbol;
	
	public Directive(Mnemonic mnemonic){
		super(mnemonic);
	}

	public Directive(Mnemonic mnemonic, int operand) {
		super(mnemonic);
		this.value = operand;
	}
	
	public Directive(Mnemonic mnemonic, String operand) {
		super(mnemonic);
		this.symbol = operand;
	}

	@Override
	public boolean resolve(Code code) throws SemanticError{
		if (symbol != null) 
			value = code.simbTable.get(symbol);
		
		// resolve additional attributes
		switch (this.mnemonic.name) {
		case "START":
			code.programStartAddress = value;
			code.programName = this.label;
			break;
		case "ORG":
			code.locCounterNext = value;
			break;
		case "END":
			code.programBegin = value;
			break;
		case "BASE":
			code.getRegisters().setB(value);
			break;
		case "EQU":
			if (symbol == null)
				break;
			if (symbol.compareTo("*") == 0){
				value = code.locCounterCurrent;
				code.simbTable.put(this.label, value);
			}
			else if (code.simbTable.get(symbol) != null)
				value = code.simbTable.get(symbol);
			else
				throw new SemanticError("EQU error " + mnemonic.line);
				
			break;
		default:
			break;
		}
		return false;
	}

	@Override
	public void emitCode(byte[] data, int pos) {
		// does nothing
	}
}


