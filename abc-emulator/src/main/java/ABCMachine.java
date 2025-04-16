package src.main.java;

import src.main.java.asm.AddSubMechanism;
import src.main.java.drum.*;
import src.main.java.base2.*;
import src.main.java.base10.Base10Reader;

import java.util.Scanner;


public class ABCMachine {
    public CA ca;
    public KA ka;
    public Carry carryDrum;
    public Conversion conv;
    public Base10Reader base10Reader;
    public Base2Reader base2Reader;
    public Base2Punch base2Punch;
    public AddSubMechanism adder = new AddSubMechanism();

    public ABCMachine() {
        ca = new CA();
        ka = new KA();
        carryDrum = new Carry();
        conv = new Conversion(this);
        base10Reader = new Base10Reader(this);
        base2Reader = new Base2Reader();
        base2Punch = new Base2Punch();
    }

    public void doSomething(){
        int[] card = new int[] {
                56
        };
        base10Reader.readCard(card);
        ca.transfer(ka);
        System.out.println("KA");
        ka.printData();
        card = new int[] {
                28
        };
        ca.clear();
        base10Reader.readCard(card);
        System.out.println("CA");
        ca.printData();

//        adder.addSubWithKA(ca, ka, carryDrum, true);
//        System.out.println("CA product");
//        ca.printData();

        System.out.println("Going to preform operation");
        adder.operation(ca, ka, carryDrum, true, 0);
        System.out.println("CA product");
        ca.printData();
    }
}
