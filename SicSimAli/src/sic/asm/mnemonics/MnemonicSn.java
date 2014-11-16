package sic.asm.mnemonics;

import sic.asm.code.Code;
import sic.asm.code.Node;
import sic.asm.code.Storage;
import sic.asm.parsing.Parser;
import sic.asm.parsing.SyntaxError;

/**
 *  (RESB, RESW).
 * @author Ales
 *
 */
public class MnemonicSn extends Mnemonic {

	public MnemonicSn(String name, int opcode, String hint, String desc) {
		super(name, opcode, hint, desc);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Node parse(Parser parser) throws SyntaxError {
		this.line = parser.lexer.row;
		// number
		if (Character.isDigit(parser.lexer.peek()))
			if (this.name.compareTo("RESW") == 0)
				return new Storage(this, parser.parseNumber(0, Code.MAX_ADDR));
			else if (this.name.compareTo("RESB") == 0)
				return new Storage(this, parser.parseNumber(0, Code.MAX_ADDR));
			else
				throw new SyntaxError(String.format("Number too big."), parser.lexer.row, parser.lexer.col);

		// otherwise: error
		else
			throw new SyntaxError(String.format("Invalid character '%c", parser.lexer.peek()), parser.lexer.row, parser.lexer.col);

	}

	@Override
	public String operandToString(Node node){
		Storage instr = (Storage)node;
		return Integer.toString(instr.value);
	}
}
