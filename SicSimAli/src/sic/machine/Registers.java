package sic.machine;
public class Registers {

	/*
	 * 1) Mapping konstant za stevilke registrov je se treba narediti.
	 */

	private int A;
	private int X;
	private int L;
	private int B;
	private int S;
	private int T;
	private double F;

	public static final int ST_A = 0;
	public static final int ST_X = 1;
	public static final int ST_L = 2;
	public static final int ST_B = 3;
	public static final int ST_S = 4;
	public static final int ST_T = 5;
	public static final int ST_F = 6;

	private int PC;
	private int CC;

	public Registers() {
		super();
		A = 0;
		X = 0;
		L = 0;
		B = 0;
		S = 0;
		T = 0;
		F = 0;
		PC = 0;
		CC = 0;
	}

	public int getA() {
		return A;
	}

	public void setA(int a) {
		A = a;
	}

	public int getX() {
		return X;
	}

	public void setX(int x) {
		X = x;
	}

	public int getL() {
		return L;
	}

	public void setL(int l) {
		L = l;
	}

	public int getB() {
		return B;
	}

	public void setB(int b) {
		B = b;
	}

	public int getS() {
		return S;
	}

	public void setS(int s) {
		S = s;
	}

	public int getT() {
		return T;
	}

	public void setT(int t) {
		T = t;
	}

	public double getF() {
		return F;
	}

	public void setF(double f) {
		F = f;
	}

	public int getPC() {
		return PC;
	}

	public void setPC(int pC) {
		PC = pC;
	}

	public int getSW() {
		if (CC < 0)
			return 0;
		else if (CC == 0)
			return 0x40;
		else
			return 0x80;
	}

	public void setSW(int sW) {
		if (sW == 0)
			CC = -1;
		else if (sW == 0x40)
			CC = 0;
		else
			CC = 1;
	}

	public int getReg(int regno) {
		switch (regno) {
		case ST_A:
			return A;
		case ST_X:
			return X;
		case ST_L:
			return L;
		case ST_B:
			return B;
		case ST_S:
			return S;
		case ST_T:
			return T;
		case ST_F:
			return (int) F; // POZOR -> F JE CASTAN NA INT!!!! NI DOUBLE
							// REALIZIRAN
		default:
			System.out.print("Register number invalid.");
			return -1;
		}
	}
	
	public static String getRegStr(int regno) {
		switch (regno) {
		case ST_A:
			return "A";
		case ST_X:
			return "X";
		case ST_L:
			return "L";
		case ST_B:
			return "B";
		case ST_S:
			return "S";
		case ST_T:
			return "T";
		case ST_F:
			return "F"; 
			
		default:
			System.out.print("Register number invalid.");
			return "";
		}
	}

	public void setReg(int regno, int value) {
		switch (regno) {
		case ST_A:
			A = value;
			break;
		case ST_X:
			X = value;
			break;
		case ST_L:
			L = value;
			break;
		case ST_B:
			B = value;
			break;
		case ST_S:
			S = value;
			break;
		case ST_T:
			T = value;
			break;
		case ST_F:
			F = value;
			break;
		default:
			System.out.print("Register number invalid.");
			break;
		}
	}

}
