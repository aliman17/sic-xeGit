package sic.asm.mnemonics;

import sic.asm.code.InstructionF2;
import sic.asm.code.Node;
import sic.asm.parsing.Parser;
import sic.asm.parsing.SyntaxError;
import sic.machine.Registers;

/**
 *  (ADDR, ...);
 * @author Ales
 *
 */
public class MnemonicF2rr extends Mnemonic {

	public MnemonicF2rr(String name, int opcode, String hint, String desc) {
		super(name, opcode, hint, desc);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Node parse(Parser parser) throws SyntaxError {
		this.line = parser.lexer.row;
		int reg1 = parser.parseRegister();
		// skip comma
		parser.parseComma();
		int reg2 = parser.parseRegister();
		
		return new InstructionF2(this, reg1,  reg2);
	}

	@Override
	public String operandToString(Node node){
		return (Registers.getRegStr(((InstructionF2)node).reg1) + ", " + Registers.getRegStr(((InstructionF2)node).reg2));
	}
}
