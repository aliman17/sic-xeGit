package sic.asm.mnemonics;

import sic.asm.code.InstructionF1;
import sic.asm.code.Node;
import sic.asm.parsing.Parser;
import sic.asm.parsing.SyntaxError;

public class MnemonicF1 extends Mnemonic {

	public MnemonicF1(String name, int opcode, String hint, String desc) {
		super(name, opcode, hint, desc);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Node parse(Parser parser) throws SyntaxError {
		this.line = parser.lexer.row;
		return new InstructionF1(this);
	}

}
