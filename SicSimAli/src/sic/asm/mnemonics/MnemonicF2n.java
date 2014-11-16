package sic.asm.mnemonics;

import sic.asm.code.InstructionF2;
import sic.asm.code.Node;
import sic.asm.parsing.Parser;
import sic.asm.parsing.SyntaxError;

/*
 * SVC
 */

public class MnemonicF2n extends Mnemonic {

	public MnemonicF2n(String name, int opcode, String hint, String desc) {
		super(name, opcode, hint, desc);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Node parse(Parser parser) throws SyntaxError {
		this.line = parser.lexer.row;
		byte[] data;
		if (Character.isDigit(parser.lexer.peek()))
			return new InstructionF2(this, parser.parseNumber(0, (1<<4)-1));
		// symbol
//		else if (Character.isLetter(parser.lexer.peek()))
//			return new InstructionF2(this, parser.parseSymbol());
//		// otherwise: error
		else{
			data = parser.parseData();
			int dataInt = data[0] << 8 | data[1];
			return new InstructionF2(this, dataInt);
		}
	}
	
	@Override
	public String operandToString(Node node) {
		return Integer.toString(((InstructionF2) node).reg2);
	}
}
