package sic.asm.code;

import sic.asm.mnemonics.Mnemonic;

/**
 * Abstract class Node.
 * Includes label, mnemonic and comment.
 * Podporni razred za predmet Sistemska programska oprema.
 * @author jure
 */
public abstract class Node {

	public String label;
	public Mnemonic mnemonic;
	public String comment;
	public int length;
	public int ni;
	public int xbpe;
	
	public Node(Mnemonic mnemonic) {
		this.mnemonic = mnemonic;
		this.length = 0;
		this.ni = 0;
		this.xbpe = 0;
	}

	public String getLabel() {
		return label == null ? "" : label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Return comment as a string.
	 */
	public String getComment() {
		return comment == null ? "" : comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	// pred zacetkom obiska
	public void enter(Code code) {
		code.locCounterNext += this.length; 
	}
	
	// po koncu obiska
	public void leave(Code code) {
		code.locCounterCurrent = code.locCounterNext;
	}
	
	// definicija label
	public boolean activate(Code code) throws SemanticError {
		if (label == null) {
			return false;
		}
		
		// if label exists -> error
		if(code.simbTable.get(label) != null){
			throw new SemanticError("Duplicate label");
		}
		code.simbTable.put(label, code.locCounterCurrent);
		return true;
	}
	
	// 2. prehod - razresi simbole
	public abstract boolean resolve(Code code) throws SemanticError;
	
	public abstract void emitCode(byte[] data, int pos);

	/**
	 * Return string representation of the node.
	 * Label and comment are not included.
	 */
	@Override
	public String toString() {
		return mnemonic.toString() + " " + operandToString();
	}

	public String operandToString() {
		// na podlagi mnemonika tocno vemo, kaksna je bila oblika
		return mnemonic.operandToString(this);
	}
	
	// byte v hex zapis -> string
	public void emitText(StringBuffer buf){}
	
	public static String byteArrayToHex(byte[] data) {
	   StringBuilder str = new StringBuilder();
	   for(byte b: data)
		   str.append(String.format("%02x", b&0xff));
	   return str.toString();
	}
	
	public void emitTextGeneral(StringBuffer buf, int len){
		byte[] data = new byte[len];
		emitCode(data, 0);
		String str = byteArrayToHex(data);
		buf.append(str);
	}
}
