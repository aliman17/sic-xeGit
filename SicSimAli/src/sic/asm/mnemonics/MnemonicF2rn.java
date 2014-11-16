package sic.asm.mnemonics;

import sic.asm.code.InstructionF2;
import sic.asm.code.Node;
import sic.asm.parsing.Parser;
import sic.asm.parsing.SyntaxError;
import sic.machine.Registers;

/**
 * 
 * @author Ales
 * (SHIFTL, SHIFTR);
 */

public class MnemonicF2rn extends Mnemonic {

	public MnemonicF2rn(String name, int opcode, String hint, String desc) {
		super(name, opcode, hint, desc);
	}
	
	@Override
	public Node parse(Parser parser) throws SyntaxError {
		this.line = parser.lexer.row;
		int register = parser.parseRegister();
		// skip comma
		parser.parseComma();

		// number
		if (Character.isDigit(parser.lexer.peek()))
			return new InstructionF2(this, register, parser.parseNumber(0, (1<<4)-1));
//		// symbol
		else if (Character.isLetter(parser.lexer.peek()))
			return new InstructionF2(this, register, parser.parseSymbol());
		else
			throw new SyntaxError(String.format("Invalid character '%c", parser.lexer.peek()), parser.lexer.row, parser.lexer.col);
	
	}
	
	@Override
	public String operandToString(Node node){
		if (((InstructionF2)node).symbol2 == null)
			return (Registers.getRegStr(((InstructionF2)node).reg1) + ", " + Integer.toString(((InstructionF2)node).reg2));
		else
			return (Registers.getRegStr(((InstructionF2)node).reg1) + ", " + ((InstructionF2)node).symbol2);
	}

}
