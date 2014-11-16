package sic.machine;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Set;

import javax.rmi.CORBA.Util;

import device.Device;
import device.FileDevice;
import device.InputDevice;
import device.OutputDevice;

public class Machine {

	private final int NUM_DEVICES = 256;
	private final int stdin = 0;
	private final int stdout = 1;
	private final int stderr = 2;
	private Memory memory;
	private Registers registers;
	private Device[] array_of_devices;
	private TimerSic timerSic;
	private InputDevice inputDevice;

	public Machine() {
		super();

		memory = new Memory();
		registers = new Registers();
		timerSic = new TimerSic(this, 1000);

		array_of_devices = new Device[NUM_DEVICES];
		array_of_devices[stdin] = new InputDevice(System.in);
		array_of_devices[stdout] = new OutputDevice(System.out);
		array_of_devices[stderr] = new OutputDevice(System.err);
//		inputDevice = new InputDevice();

		for (int i = 3; i < NUM_DEVICES; i++)
			array_of_devices[i] = new FileDevice(Integer.toString(i)); // ime je
																		// kar
																		// stevilka
	}

	// GETTERS AND SETTERS---------------------------------------------

	public Device getDevice(int num) {
		if (validDevice(num - 3))
			return array_of_devices[num - 3];
		else
			return null;
	}

	public void setDevice(int num, FileDevice device) {
		if (validDevice(num))
			array_of_devices[num] = device; // 0,1,2 are taken
		else
			System.out.println("Not valid device number");

	}

	public Memory getMemory() {
		return memory;
	}

	public Registers getRegisters() {
		return registers;
	}

	// VALIDATION------------------------------------------------------

	public boolean validDevice(int num) {
		if (0 <= num && num < NUM_DEVICES) {
			return true;
		}
		return false;
	}

	// PC FETCH--------------------------------------------------------
	public int fetch() {
		// get PC
		int PC = registers.getPC();

		// get byte
		int b = memory.getByte(PC);

		// increase PC
		registers.setPC(PC + 1);

		// return value at address PC
		return b;
	}

	// IZVAJALNIK------------------------------------------------------
	public void execute() {
		TimerSic timer = new TimerSic(this, 1000);
		timer.start();
		System.out.println(timer.isRunning() ? "Is running ..."
				: "NOT running.");
	}

	public void executeInstruction() {

		// fetch1
		int opcode = fetch();

		if (execF1(opcode))
			return;

		// fetch2
		int operand = 0;
		operand = fetch();

		if (execF2(opcode, operand))
			return;

		// fetch3
		operand = operand << 8 | fetch() & 0xff;

		// get xbpe
		int xbpe = (operand & 0xf000) >> 12;

		// update operand
		operand = operand & 0x0fff;

		// extended?
		if ((xbpe & 0x1) == 1) {
			// fetch4 -> 20 bits operand
			operand = operand << 8 | (fetch() & 0xff);
			// 0000 0000|0000 ....|.... ....|XXXX XXXX
		}

		// ni, xbpe, operand
		operand = xbpe_operand(xbpe, operand);

		if (execSICF3F4(opcode, operand)) {
			return;
		}
		invalidOpcode(opcode);

	}

	public int xbpe_operand(int xbpe, int operand) {

		int address = operand;

		// index?
		if (xbpe == 8) {
			address += registers.getX();
		}
		if (xbpe == 4) {
			address += registers.getB();
		} else if (xbpe == 2) {
			address += registers.getPC();
			address = address & 0xfff;
		}
		
		return address;
	}
	public int ni_operand(int ni, int operand){
		
		int address = operand;
		
		switch (ni) {
		case 0:
			/*
			 * sic ukaz
			 */
			break;
		case 1:
			/*
			 * immediate addressing - done
			 */
			return operand;
		case 2:
			/*
			 * indirect addressing
			 */
			return memory.getWord(memory.getWord(address));

		case 3:
			/*
			 * easy addressing: get value on address operand
			 */
			return memory.getWord(address);
		default:
		}
		return operand;
	}

public void ni_operand_set(int ni, int address,  int value, boolean byt){
		
		switch (ni) {
		case 0:
			/*
			 * sic ukaz
			 */
			break;
		case 1:
			/*
			 * immediate addressing - done
			 */
			if (byt){
				memory.setByte(address, value);
				return;
			}
			memory.setWord(address, value);
			return;
		case 2:
			/*
			 * indirect addressing
			 */
			if (byt){
				memory.setByte(memory.getWord(address), value);
				return;
			}
			memory.setWord(memory.getWord(address), value);
			return;
		case 3:
			/*
			 * easy addressing: get value on address operand
			 */
		default:
		}
		if (byt){
			memory.setByte(address, value);
			return;
		}
		memory.setWord(address, value);
	}

	
	// PC FETCH--------------------------------------------------------

	public boolean execF1(int opcode) {

		switch (opcode) {
		case Opcode.FIX:
			notImplemented("FIX");
			break;
		case Opcode.FLOAT:
			notImplemented("FLOAT");
			break;
		case Opcode.HIO:
			notImplemented("HIO");
			break;
		case Opcode.NORM:
			notImplemented("NORM");
			break;
		case Opcode.SIO:
			notImplemented("SIO");
			break;
		case Opcode.TIO:
			notImplemented("TIO");
			break;
		default:
			return false;
		}
		return true;
	}

	public boolean execF2(int opcode, int operand) {

		/*
		 * Operand contains 2 registers
		 */

		int reg2 = operand & 0xf;
		int reg1 = (operand & 0xf0) >> 4;

		switch (opcode) {
		case Opcode.ADDR:
			registers.setReg(reg2,
					registers.getReg(reg1) + registers.getReg(reg2));
			break;
		case Opcode.CLEAR:
			System.out.println("CLEAR");
			registers.setReg(reg1, 0);
			break;
		case Opcode.COMPR:
			if (registers.getReg(reg1) == registers.getReg(reg2)) {
				registers.setSW(0x40);
				break;
			} else if (registers.getReg(reg1) > registers.getReg(reg2)) {
				registers.setSW(0x80);
			} else {
				registers.setSW(0);
			}
			break;
		case Opcode.DIVR:
			if (registers.getReg(reg1) == 0) {
				break;
			}
			registers.setReg(reg2,
					registers.getReg(reg2) / registers.getReg(reg1));
			break;
		case Opcode.MULR:
			registers.setReg(reg2,
					registers.getReg(reg2) * registers.getReg(reg1));
			break;
		case Opcode.RMO:
			registers.setReg(reg2, registers.getReg(reg1));
			break;
		case Opcode.SHIFTL:
			registers.setReg(reg1, registers.getReg(reg1) << reg2);
			break;
		case Opcode.SHIFTR:
			registers.setReg(reg1, registers.getReg(reg1) >> reg2);
			break;
		case Opcode.SUBR:
			registers.setReg(reg2,
					registers.getReg(reg2) - registers.getReg(reg1));
			break;
		default:
			return false;
		}

		return true;
	}

	public boolean execSICF3F4(int opcode, int operand) {

		int ni = opcode &0x3;
		opcode = opcode & 0xfc;
		int old_operand = operand;
		
		operand = ni_operand(ni, operand);
		
		int regA = registers.ST_A;

		switch (opcode) {
		case Opcode.ADD:
			System.out.println("ADD");
			registers.setReg(regA, registers.getReg(regA) + operand);
			break;

		case Opcode.SUB:
			System.out.println("SUB");
			registers.setReg(regA, registers.getReg(regA) - operand);
			break;

		case Opcode.MUL:
			System.out.println("MUL");
			registers.setReg(regA, registers.getReg(regA) * operand);
			break;

		case Opcode.DIV:
			System.out.println("DIV");
			if (operand == 0) {
				System.out.println("WARNING: Dividing by 0.");
				break;
			}
			registers.setReg(regA, registers.getReg(regA) / operand);
			break;
		case Opcode.J:
			System.out.println("J");
			registers.setPC(old_operand);
			break;
		case Opcode.LDA:
			System.out.println("LDA");
			registers.setA(operand);
			break;
		case Opcode.STA:
			System.out.println("STA");
			ni_operand_set(ni, old_operand, registers.getA(), false);
			break;
		case Opcode.COMP:
			System.out.println("COMP");
			if (registers.getA() == operand) {
				registers.setSW(0x40);
				break;
			} else if (registers.getA() > operand) {
				registers.setSW(0x80);
			} else {
				registers.setSW(0);
			}
			break;
		case Opcode.JLT:
			System.out.println("JLT");
			if (registers.getSW() == 0) {
				// jump
				registers.setPC(old_operand);
				break;
			} 
			break;
		case Opcode.JGT:
			System.out.println("JGT");
			if (registers.getSW() == 0x80) {
				// jump
				registers.setPC(old_operand);
				break;
			} 
			break;
		case Opcode.JEQ:
			System.out.println("JEQ");
			if (registers.getSW() == 0x40) {
				// jump
				registers.setPC(old_operand);
				break;
			} 
			break;
		case Opcode.JSUB:
			System.out.println("JSUB");
			registers.setL(registers.getPC());
			registers.setPC(old_operand);
			break;
		case Opcode.RSUB:
			System.out.println("RSUB");
			registers.setPC(registers.getL());
			break;
//		case Opcode.RD:
//			System.out.println("RD");
//			int device = operand & 0xff;
//			String dev = Integer.toHexString(device);
//			FileDevice input = new FileDevice(dev);
//			try {
//				registers.setA(input.read());
//			} catch (IOException e) {
//				System.out.println("Can not read from device.");
//			}
//			break;
//		case Opcode.WD:
//			System.out.println("WD");
//			if (registers.getSW() == 0x40) {
//				// jump
//				registers.setPC(old_operand);
//				break;
//			} 
//			break;
		default:
			return false;
		}

		return true;
	}

	// OBJ LOAD FILE---------------------------------------------------

	public boolean loadSection(Reader r) {
		/*
		 * TODO
		 */
		String progName;
		int address;
		int length;

		// System.out.println("Nalagam obj datoteko ... ");

		// start with H ***************************************************
		String readH = Utils.readString(r, 1);
		if (readH.compareTo("H") != 0){
			System.out.println("ERROR: Napacen format obj datoteke.");
			return false;
		}
		
		// program name: 2 - 7
		progName = Utils.readString(r, 6);
		// System.out.print(progName);

		// code address: 8 - 13
		address = Integer.parseInt(Utils.readString(r, 6), 16);

		// code length
		length = Integer.parseInt(Utils.readString(r, 6), 16);

		// start with T ***************************************************

		String read;
		while (true) {

			while ((read = Utils.readString(r, 1)).compareTo("T") != 0) {
//				 System.out.print(read);
				if (read.compareTo("E") == 0) {
					address = Utils.readWord(r);
					registers.setPC(address);
					// System.out.println(address);
					return true;
				}
			}

			// T is read

			// read address to store instructions
			address = Utils.readWord(r);

			int readWord;
			int readByte;
			int tLen = Utils.readByte(r);

			for (int i = 0; i < tLen; i++) {

				// read
				readByte = Utils.readByte(r);
//				System.out.println(readByte);
				// store
				memory.setByte(address, readByte);
				address ++;

			}

		}

	}

	// ERRORS HANDLERS-------------------------------------------------
	public void notImplemented(String mnemonic) {
		System.out.println("Not implemented mnemonic: " + mnemonic);
	}

	public void invalidOpcode(int opcode) {
		System.out.println("Invalid opcode.");
	}

	public void invalidAddressing() {
		System.out.println("Invalid addressing.");
	}

}
