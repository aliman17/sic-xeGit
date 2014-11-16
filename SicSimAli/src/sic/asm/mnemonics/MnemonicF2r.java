package sic.asm.mnemonics;

import sic.asm.code.InstructionF2;
import sic.asm.code.Node;
import sic.asm.parsing.Parser;
import sic.asm.parsing.SyntaxError;
import sic.machine.Registers;

/**
 * 
 * @author Ales
 * (CLEAR r, TIXR);
 */

public class MnemonicF2r extends Mnemonic {

	public MnemonicF2r(String name, int opcode, String hint, String desc) {
		super(name, opcode, hint, desc);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Method reads r1, we have only one register to read
	 */
	@Override
	public Node parse(Parser parser) throws SyntaxError {	
		this.line = parser.lexer.row;
		return new InstructionF2(this, parser.parseRegister());
	}
	
	@Override
	public String operandToString(Node node) {
		// register to string
		return Registers.getRegStr(((InstructionF2)node).reg1);
	}

}
