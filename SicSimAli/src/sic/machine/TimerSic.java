package sic.machine;

import java.beans.Transient;
import java.util.Timer;
import java.util.TimerTask;

import sic.asm.code.Code;

public class TimerSic extends TimerTask{
	
	private Timer timer;
	private boolean isRunning;
	private int delay;
	private Machine machine;

	public TimerSic(Machine m, int delay) {
		isRunning = false;
		machine = m;
		this.delay = delay;
	}

	public void start(){	
		if (isRunning) this.cancel();
		try {
			isRunning = true;
			timer = new Timer();
			timer.schedule(this, 0, delay);
		} catch (Exception e) {
			System.out.println("ERROR: Timer start.");
			e.printStackTrace();
		}		
	}
	
	public void stop() {
		try {
			timer.cancel();
			isRunning = false;
		} catch (Exception e) {
			System.out.println("ERROR: Timer cancle");
			e.printStackTrace();
		}
	}
	
	public boolean isRunning(){
		return isRunning;
	}
	
	public long getSpeed() {
		return delay;
	}
	
	public void setSpeed(int speed) {
		delay = speed;
		if (isRunning){
			this.start();
		}
	}

	@Override
	public void run() {
		machine.executeInstruction();
		printRegs(machine.getRegisters());
	}
	
	public static void printRegs(Registers registers){
		String str = String.format("A:%-8s S:%-8s T:%-8s  L:%-8s \nB:%-8s X:%-8s SW:%-8s PC:%-8s", 
				Code.paddingWord(Integer.toHexString(registers.getA()), "0", 6), 
				Code.paddingWord(Integer.toHexString(registers.getS()), "0", 6),
				Code.paddingWord(Integer.toHexString(registers.getT()), "0", 6),
				Code.paddingWord(Integer.toHexString(registers.getL()), "0", 6),
				Code.paddingWord(Integer.toHexString(registers.getB()), "0", 6),
				Code.paddingWord(Integer.toHexString(registers.getX()), "0", 6),
				Code.paddingWord(Integer.toHexString(registers.getSW()), "0", 6),
				Code.paddingWord(Integer.toHexString(registers.getPC()), "0", 6));
		System.out.println(str.toUpperCase());
	}
	
}
