package sic.machine;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import sic.asm.code.Code;
import device.FileDevice;
import device.InputDevice;
import device.OutputDevice;

public class Test {
	public static Memory memory;
	public static Machine machine;
	public static Registers registers;

	public Test(){
		this.machine = new Machine();
		this.registers = machine.getRegisters();
		this.memory = machine.getMemory();
	}
	
	// ok
	public void getByteTest() {
		int b = 3;
		int zapisi = 10;
		memory.setByte(b, 10);
		if (memory.getByte(b) == zapisi) {
			System.out.println("OK: getByteTest Zapisovanje in branje iz pomnilnika.");
		} else
			System.out.println("ERROR: getByteTest Zapisovanje in branje iz pomnilnika.");
	}

	public void testFileDevice() throws IOException {
		FileDevice file = new FileDevice("aliman");
		byte testByte = 0x41;
		// write byte into file
		file.write(testByte);

		// read byte from file
		if (file.read() == testByte) {
			System.out.println("OK: testFileDevice Pisanje in branje v datoteko.");
		} else
			System.out.println("ERROR: testFileDevice Pisanje in branje v datoteko.");
	}

	public void testInputOutputDevice() throws IOException {
		InputDevice input = new InputDevice(System.in);
		System.out.print("Testiraj vhod - vpisi znak: ");

		// read from stdin
		byte stdin = input.read();

		// write to stdout
		OutputDevice output = new OutputDevice(System.out);
		System.out.print("Echo:> ");
		output.write(stdin);
		System.out.println();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out
					.println("ERROR: Sleepanje v testInputOutputDevice metodi ne deluje.");
		}
	}

	public void testEcexF2() {
		registers.setA(6);
		machine.execF2(Opcode.CLEAR, registers.ST_A << 4);
		if (registers.getA() == 0)
			System.out.println("OK: testEcexF2 CLEAR deluje. output "
					+ registers.getReg(registers.ST_A));
		else
			System.out.println("ERROR: CLEAR.");

		// ADDR

		// choose registers
		int reg1 = registers.ST_A;
		int reg2 = registers.ST_X;

		// numbers for sum
		int num1 = 3;
		int num2 = 7;

		// set registers
		registers.setReg(reg1, num1);
		registers.setReg(reg2, num2);

		// set operand
		int operand1 = (reg1 & 0xf) << 4;
		int operand2 = operand1 | reg2 & 0xf;

		// execute
		machine.execF2(Opcode.ADDR, operand2);

		if ((num1 + num2) == registers.getReg(reg2)) {
			System.out.println("OK: testEcexF2 ADDR deluje. " + num2 + "+" + num1 + "="
					+ registers.getReg(reg2));
		} else
			System.out.println("ERROR: testEcexF2 ADDR.");

		// SUBSTRACT

		// numbers for sum
		num1 = 3;
		num2 = 7;

		// set registers
		registers.setReg(reg1, num1);
		registers.setReg(reg2, num2);

		// set operand
		operand1 = (reg1 & 0xf) << 4;
		operand2 = operand1 | reg2 & 0xf;

		// execute
		machine.execF2(Opcode.SUBR, operand2);

		if ((num2 - num1) == registers.getReg(reg2)) {
			System.out.println("OK: testEcexF2 SUBR deluje. " + num2 + "-" + num1 + "="
					+ registers.getReg(reg2));
		} else
			System.out.println("ERROR: testEcexF2 SUBR.");

		// COMPR

		// reset SW
		registers.setSW(0);

		// set registers
		registers.setReg(reg1, num1);
		registers.setReg(reg2, num2);

		// execute comparision
		machine.execF2(Opcode.COMPR, operand2);

		if (registers.getReg(reg1) > registers.getReg(reg2)
				&& registers.getSW() == 0x80) {
			System.out.println("OK: testEcexF2 COMPR deluje.");
		} else if (registers.getReg(reg1) == registers.getReg(reg2)
				&& registers.getSW() == 0x40) {
			System.out.println("OK: testEcexF2 COMPR deluje.");
		} else if (registers.getReg(reg1) < registers.getReg(reg2)
				&& registers.getSW() == 0) {
			System.out.println("OK: testEcexF2 COMPR deluje. Input: " + num1 + " " + num2);
		} else {
			System.out.println("ERROR: testEcexF2 COMPR.");
		}

		// DIVR

		// set registers
		registers.setReg(reg1, num1);
		registers.setReg(reg2, num2);

		// set operand
		operand1 = (reg1 & 0xf) << 4;
		operand2 = operand1 | reg2 & 0xf;

		// execute division
		machine.execF2(Opcode.DIVR, operand2);

		if (num1 == 0 && registers.getReg(reg2) == num2) {
			System.out.println("WARNING: Imenovalec je 0.");
		} else if (num1 != 0 && registers.getReg(reg2) == (num2 / num1)) {
			System.out.println("OK: testEcexF2 DIVR deluje. " + num2 + "/" + num1 + "="
					+ registers.getReg(reg2));
		} else {
			System.out.println("ERROR: DIVR.");
		}

		// MULR

		// set registers
		registers.setReg(reg1, num1);
		registers.setReg(reg2, num2);

		// set operand
		operand1 = (reg1 & 0xf) << 4;
		operand2 = operand1 | reg2 & 0xf;

		// execute multiplication
		machine.execF2(Opcode.MULR, operand2);

		if (registers.getReg(reg2) == (num2 * num1)) {
			System.out.println("OK: testEcexF2 MULR deluje. " + num2 + "*" + num1 + "="
					+ registers.getReg(reg2));
		} else {
			System.out.println("ERROR: testEcexF2 MULR.");
		}

		// RMO
		// execute
		machine.execF2(Opcode.RMO, operand2);

		if (registers.getReg(reg2) == registers.getReg(reg1)) {
			System.out.println("OK: testEcexF2 RMO deluje.");
		} else {
			System.out.println("ERROR: testEcexF2 RMO.");
		}

		// SHIFTL r1,n

		reg1 = 0;
		num1 = 2;
		num2 = 1; // shift

		// set registers
		registers.setReg(reg1, num1);

		// set operand
		operand1 = (reg1 & 0xf) << 4;
		operand2 = operand1 | num2 & 0xf;

		// execute multiplication
		machine.execF2(Opcode.SHIFTL, operand2);

		if (registers.getReg(reg1) == (num1 << num2)) {
			System.out.println("OK: testEcexF2 SHIFTL deluje. Input: " + num1
					+ " shiftl: " + registers.getReg(reg1));
		} else {
			System.out.println("ERROR: testEcexF2 SHIFTL.");
		}

		// SHIFTL r1,n

		reg1 = 0;
		num1 = 4;
		num2 = 1; // shift

		// set registers
		registers.setReg(reg1, num1);

		// set operand
		operand1 = (reg1 & 0xf) << 4;
		operand2 = operand1 | num2 & 0xf;

		// execute multiplication
		machine.execF2(Opcode.SHIFTR, operand2);

		if (registers.getReg(reg1) == (num1 >> num2)) {
			System.out.println("OK: testEcexF2 SHIFTR deluje. Input: " + num1
					+ " shiftr: " + registers.getReg(reg1));
		} else {
			System.out.println("ERROR: testEcexF2 SHIFTL.");
		}

	}

	public void testEcexF3F4() {

		int numInRegA = 10;
		int operand = 5;

		// ADD
		// set regA
		registers.setA(numInRegA);

		// execute
		machine.execSICF3F4(Opcode.ADD, operand);

		if (registers.getA() == numInRegA + operand) {
			System.out.println("OK: testEcexF3F4 ADD deluje. " + numInRegA + "+" + operand
					+ "=" + registers.getA());
		} else
			System.out.println("ERROR: testEcexF3F4 ADD.");

		// SUB
		// set regA
		registers.setA(numInRegA);
		// execute
		machine.execSICF3F4(Opcode.SUB, operand);

		if (registers.getA() == (numInRegA - operand)) {
			System.out.println("OK: testEcexF3F4 SUB deluje. " + numInRegA + "-" + operand
					+ "=" + registers.getA());
		} else
			System.out.println("ERROR: testEcexF3F4 SUB.");

		// MUL
		// set regA
		registers.setA(numInRegA);
		// execute
		machine.execSICF3F4(Opcode.MUL, operand);

		if (registers.getA() == (numInRegA * operand)) {
			System.out.println("OK: testEcexF3F4 MUL deluje. " + numInRegA + "*" + operand
					+ "=" + registers.getA());
		} else
			System.out.println("ERROR: testEcexF3F4 MUL.");

		// DIV
		// set regA
		registers.setA(numInRegA);
		// execute
		machine.execSICF3F4(Opcode.DIV, operand);

		if (registers.getA() == (numInRegA / operand)) {
			System.out.println("OK: testEcexF3F4 DIV deluje. " + numInRegA + "/" + operand
					+ "=" + registers.getA());
		} else
			System.out.println("ERROR: testEcexF3F4 DIV.");

	}

	public void testAddressing() {

		int numInRegA = 10;
		int operand = 5;

		// IMMEDIATE - is obviously working

		// EASY ADDRESSING

		// ADD

		registers.setA(numInRegA);

		// execute
		machine.execSICF3F4(Opcode.ADD + 3, operand);

		if (registers.getA() == numInRegA + memory.getWord(operand)) {
			System.out.println("OK: testAddressing EASY ADDRESSING deluje. " + numInRegA + "+"
					+ memory.getWord(operand) + "=" + registers.getA());
		} else
			System.out.println("ERROR: testAddressing EASY ADDRESSING.");

		// INDIRECT ADDRESSING

		registers.setA(numInRegA);

		// execute
		machine.execSICF3F4(Opcode.ADD + 2, operand);

		if (registers.getA() == numInRegA
				+ memory.getWord(memory.getWord(operand))) {
			System.out.println("OK: testAddressing INDIRECT ADDRESSING deluje. " + numInRegA
					+ "+" + memory.getWord(memory.getWord(operand)) + "="
					+ registers.getA());
		} else
			System.out.println("ERROR: testAddressing INDIRECT ADDRESSING.");

	}

	public void testExecute() {
		/*
		 * read instruction from address 10 and execute it -> we have ADD it to
		 * registerA
		 */

		// set instruction to address 10
		int address = 10;
		int operand = 9;
		int op_addr = 1;
		int opcode = Opcode.ADD;
		int regA = 5;

		// opcode
		int ni = 3;
		opcode = opcode + ni;
		memory.setByte(address, opcode);

		// special bits - WARNING: it's made a bit superficial
		memory.setByte(address + 1, 0);

		// put operand to addr op_addr
		memory.setWord(op_addr, operand);
		memory.setByte(address + 2, op_addr);

		// set register A
		registers.setA(regA);
		// set PC
		registers.setPC(address);

		machine.executeInstruction();

		if (registers.getA() == operand + regA) {
			System.out.println("OK: testExecute DIRECT ADDRESSING deluje " + regA + "+"
					+ operand + "=" + registers.getA());
		} else
			System.out.println("ERROR: testExecute DIRECT ADDRESSING.");

		// INDIRECT ADDRESSING

		int op_op_addr = 4;

		// opcode
		ni = 2; // indirect addressing
		opcode = opcode & 0xfc | ni;
		memory.setByte(address, opcode);

		// special bits - WARNING: it's made a bit superficial
		memory.setByte(address + 1, 0);

		// put operand to addr op_addr
		memory.setWord(op_addr, operand);
		memory.setWord(op_op_addr, op_addr);
		memory.setByte(address + 2, op_op_addr);

		// set register A
		registers.setA(regA);
		// set PC
		registers.setPC(address);

		machine.executeInstruction();

		if (registers.getA() == operand + regA) {
			System.out.println("OK: testExecute INDIRECT ADDRESSING deluje");
		} else
			System.out.println("ERROR: testExecute INDIRECT ADDRESSING.");

		// INDEX addressing

		address = 10;
		int offset = 4;
		int xbpe = 0x8;
		ni = 3;
		int value = 9;
		int instruction = Opcode.ADD + ni << 16 | xbpe << 12 | address;

		registers.setX(offset);

		memory.setWord(0, instruction);
		memory.setWord(address + offset, value);

		// set register A
		registers.setA(2);
		// set PC
		registers.setPC(0);

		machine.executeInstruction();

		// value + regA before
		if (registers.getA() == value + 2) {
			System.out.println("OK: testExecute BASE deluje");
		} else
			System.out.println("ERROR: testExecute BASE.");

	}

	public void testReadByteObj() throws FileNotFoundException {
		int r = Utils.readByte(new FileReader(
				"C:/Users/Ales/workspace/SicSimAli/src/my_file.txt"));
		System.out.println("OK: testReadByteObj Utils.readByte: " + r);

		String niz = Utils.readString(new FileReader(
				"C:/Users/Ales/workspace/SicSimAli/src/my_file.txt"), 3);
		System.out.println("OK: testReadByteObj Utils.readString: " + niz);

		int w = Utils.readWord(new FileReader(
				"C:/Users/Ales/workspace/SicSimAli/src/my_file.txt"));
		System.out.println("OK: testReadByteObj Utils.readWord: " + Integer.toHexString(w));

//		machine.loadSection(new FileReader(
//				"C:/Users/Ales/workspace/SicSimAli/src/my_file.txt"));
	}

	public void loadExecute() throws FileNotFoundException {
		machine.loadSection(new FileReader(
				"C:/Users/Ales/workspace/SicSimAli/src/sic/machine/load&execute.txt"));
		machine.getRegisters().setA(9);
		machine.execute();
		if (machine.getRegisters().getA() == 14)
			System.out.println("OK: load&execute");
		else
			System.out.println("ERROR: load&execute");
	}

	public void checkTimer() {
		TimerSic timer = new TimerSic(machine, 1000);
		timer.start();
		System.out.println(timer.isRunning() ? "Is running ..."
				: "NOT running.");
		timer.stop();
		System.out.println("OK: checkTimer TimerSic deluje.");
	}
	
	public void executeInstruction() throws FileNotFoundException{
		machine.loadSection(new FileReader(
				"C:/Users/Ales/workspace/SicSimAli/src/sic/machine/load&execute.txt"));

		machine.executeInstruction();
		machine.executeInstruction();
		machine.executeInstruction();
		machine.executeInstruction();
		machine.executeInstruction();
		machine.executeInstruction();
		machine.executeInstruction();
	}
	
	public void printMemory(int from, int to){
		System.out.print("Memory");
		for (int i = 0; i < to - from; i++){
			if (i%16 == 0)
				System.out.println();
			System.out.print(" " + Code.paddingWord(Integer.toHexString(machine.getMemory().getByte(i + from)), "0", 2));
		}
		System.out.println();
		System.out.flush();
	}
	
	public static void executionTimer(Test test) throws FileNotFoundException {
		test = new Test();
		Scanner scanner = new Scanner(System.in);
		String input = "";
		test.machine.loadSection(new FileReader(
				"./load&execute.txt"));
		int delay = 1000;
		TimerSic timer = new TimerSic(test.machine, delay);
		boolean start = false;
		
		while(true){
			input = scanner.next();
			switch (input){
			case "s":
				if (start){
					timer.stop();
					start = false;
				}
				else{	
					timer = new TimerSic(test.machine, delay);
					timer.start();
					start = true;
				}
				break;
			case "r":
				test.machine.getRegisters().setPC(0);
				break;
			case "k":
				test.machine.executeInstruction();
				TimerSic.printRegs(test.machine.getRegisters());
				break;
			case "h":
				if (start)
					timer.stop();
				delay = scanner.nextInt();
				timer = new TimerSic(test.machine, delay);
				if (start)
					timer.start();
			break;
			case "pc":
				int st = 0;
				if (scanner.hasNextInt())
					st = scanner.nextInt();
				test.machine.getRegisters().setPC(st);
				break;
			case "mm":
				test.printMemory(0, 100);
				break;
			case "m":
				int from = scanner.nextInt();
				int to = scanner.nextInt();
				test.printMemory(from, to);
				break;
			case "q":
				return;
			case "help":
			default:
				System.out.println("Ukazi:");
				System.out.println("h <hitrost> - speed");
				System.out.println("k           - korak");
				System.out.println("m <od><do>  - memory");
				System.out.println("mm          - memory from 0 to 100");
				System.out.println("q           - izhod");
				System.out.println("pc <int>    - nastavi c");
				System.out.println("r           - reset program -> pc = 0");
				System.out.println("s           - start/stop");
			}
		}
	}
	
	public void execute(){
		machine.execute();
	}
	
	//**************************************************************
	
	public static void nodePrint() {
		System.out.printf("%-10s %-10s %-10s %-10s\n", "Aliman", "Omi", "operand", "No comment");
	}
	
	public static void test(Test test) throws IOException{
		test.getByteTest();
		test.testFileDevice();
		// testInputOutputDevice();
		test.testEcexF2();
		test.testEcexF3F4();
		test.testAddressing();
		test.testExecute();
		test.testReadByteObj();
		//loadExecute();
		//nodePrint();
//		executeInstruction();
//		checkTimer();
//		execute();
		// System.out.println("v execute smo popravili prenos operanda -> popravi to");


	}

	public static void main(String[] args) throws IOException, InterruptedException {
		//Thread.sleep(1000);
		Test test = new Test();
		test(test);
		Thread.sleep(500);
		System.out.print("\nTestiranje koncano. Za zagon vnesi 's', za pomoc vnesi 'help':\n>");
		test.executionTimer(test);
	}

}
