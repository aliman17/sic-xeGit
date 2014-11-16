package sic.asm.mnemonics;

import sic.asm.code.Code;
import sic.asm.code.InstructionF3;
import sic.asm.code.Node;
import sic.asm.code.Storage;
import sic.asm.parsing.Parser;
import sic.asm.parsing.SyntaxError;

/**
 * (BYTE, WORD);
 * @author Ales
 *
 */
public class MnemonicSd extends Mnemonic {

	public MnemonicSd(String name, int opcode, String hint, String desc) {
		super(name, opcode, hint, desc);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Node parse(Parser parser) throws SyntaxError {
		this.line = parser.lexer.row;
		// number
		if (Character.isDigit(parser.lexer.peek()))
			return new Storage(this, parser.parseNumber(0, Code.MAX_WORD));
		
		byte[] data = parser.parseData();

		if (this.name.compareTo("WORD") == 0){
			byte[] tmp = new byte[((data.length-1)/3 + 1)*3];
			System.arraycopy(data, 0, tmp, 0, data.length);
		}
		return new Storage(this, data);
	}
	
	@Override
	public String operandToString(Node node){
		Storage instr = (Storage)node;
		return Integer.toString(instr.value);
	}

}
