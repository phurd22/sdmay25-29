package src.main.java;

import src.main.java.asm.AddSubMechanism;
import src.main.java.drum.Carry;
import src.main.java.drum.Drum;

public class Main {
    public static void main(String[] args) {
        ABCMachine machine = new ABCMachine();
        System.out.println("ABC Emulator started.");
        machine.doSomething();
    }
}