package src.main.java;

import src.main.java.gui.model.ControlPanel;

public class Main {
    public static void main(String[] args) {
        ABCMachine machine = new ABCMachine();
        System.out.println("ABC Emulator started.");
        machine.doSomething();
        ControlPanel panel = new ControlPanel();
    }
}