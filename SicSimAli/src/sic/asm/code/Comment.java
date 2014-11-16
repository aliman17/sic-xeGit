package sic.asm.code;

/**
 * Podporni razred za predmet Sistemska programska oprema.
 * @author jure
 */
public class Comment extends Node {
	
	public Comment(String comment) {
		super(null);
		setComment(comment);
	}

	@Override
	public String toString() {
		return comment;
	}

	@Override
	public void emitCode(byte[] data, int pos) {
		// does nothing
	}

	@Override
	public boolean resolve(Code code) {
		return false;
	}

}
