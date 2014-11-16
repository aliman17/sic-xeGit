package sic.asm.code;

import sic.asm.mnemonics.Mnemonic;

/**
 * BYTE, WORD, RESB, RESW + size
 * @author Ales
 *
 */

public class Storage extends Node {

	public static final int RESB = 0;
	public static final int RESW = 0;
	public static final int BYTE = 0;
	public static final int WORD = 0;
	
	public int value;
	public byte[] data;
	
	public Storage(Mnemonic mnemonic, int operand) {
		super(mnemonic);
		this.value = operand;
		this.length = length();
		switch (this.mnemonic.name) {
		case "BYTE":
				data = new byte[1];
				data[0]=(byte)(value&0xff);
				break;
		case "WORD":
				data = new byte[3];
				data[0]=(byte)((value>>16)&0xff);
				data[1]=(byte)((value>>8)&0xff);
				data[2]=(byte)((value)&0xff);
				break;
		default:
			break;
		}
	}
	
	public Storage(Mnemonic mnemonic, byte[] data) {
		super(mnemonic);
		this.data = data;
		this.length = length();
	}
	
	public int length(){
		
		switch (this.mnemonic.name) {
		case "RESB":
			return value;
		case "RESW":
			return value*3;
		case "BYTE":
			if (data == null)
				return 1;
			return data.length;
		case "WORD":
			if (data == null)
				return 3;
			return data.length;
		default:
			break;
		}
		
		return 0;
	}

	// for the whole program ... appending
	@Override
	public void emitCode(byte[] data, int pos) {
		switch (this.mnemonic.name) {
		case "RESB":
			break;
		case "RESW":
			break;
		case "BYTE":
		case "WORD":
			for (int i = 0; i < this.length; i++)
				data[pos+i] = this.data[i];
			break;
		default:
			break;
		}
	}

	@Override
	public boolean resolve(Code code) throws SemanticError {
		return false;
	}
	
	@Override
	public void emitText(StringBuffer buf){
		switch (this.mnemonic.name) {
		case "RESB":
//			for (int i = 0; i<value; i++)
//				buf.append("00");
			return;
		case "RESW":
//			for (int i = 0; i<value; i++)
//				buf.append("000000");
			return;
		case "BYTE":
			buf.append(Node.byteArrayToHex(data));
			return;
		case "WORD":
			buf.append(Node.byteArrayToHex(data));
			return;
		default:
			break;
		}
	}

}
