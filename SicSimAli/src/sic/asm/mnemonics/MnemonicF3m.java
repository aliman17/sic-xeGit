package sic.asm.mnemonics;

import sic.asm.code.InstructionF2;
import sic.asm.code.InstructionF3;
import sic.asm.code.Node;
import sic.asm.parsing.Parser;
import sic.asm.parsing.SyntaxError;

/**
 *  (LDA, ...);
 * @author Ales
 *
 */
public class MnemonicF3m extends Mnemonic {

	public MnemonicF3m(String name, int opcode, String hint, String desc) {
		super(name, opcode, hint, desc);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Node parse(Parser parser) throws SyntaxError {
		this.line = parser.lexer.row;
		int ni = niBits(parser);
		byte[] data;
		// number
		if (Character.isDigit(parser.lexer.peek()))
			return new InstructionF3(this, parser.parseNumber(0, (1<<12)-1 ), ni, parser.parseIndexed());
		
		//else
			int dataInt;
			try {
				data = parser.parseData();
				dataInt = data[0] << 8 | data[1];
				return new InstructionF2(this, dataInt);
			} catch (Exception e) {	}
		// symbol
		if (Character.isLetter(parser.lexer.peek())){
			return new InstructionF3(this, parser.parseSymbol(), ni, parser.parseIndexed());
		}
		else
			throw new SyntaxError(String.format("Invalid character '%c", parser.lexer.peek()), parser.lexer.row, parser.lexer.col);

	}

	@Override
	public String operandToString(Node node){
		InstructionF3 instr = (InstructionF3)node;
		if (instr.symbol != null)
			return instr.symbol;
		else
			return Integer.toString(instr.value);
	}
}
