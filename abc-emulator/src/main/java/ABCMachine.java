package src.main.java;

import src.main.java.asm.AddSubMechanism;
import src.main.java.base10.Base10Punch;
import src.main.java.base10.Base10Storage;
import src.main.java.drum.*;
import src.main.java.base2.*;
import src.main.java.base10.Base10Reader;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ABCMachine {
    public CA ca;
    public KA ka;
    public Carry carryDrum;
    public Conversion conv;
    public Base10Reader base10Reader;
    public Base10Storage base10Storage;
    public Base2Reader base2Reader;
    public Base2Punch base2Punch;
    public Base2Storage base2Storage;
    public AddSubMechanism adder = new AddSubMechanism();

    private static final Path RES_ROOT =
            Paths.get("src", "main", "resources");

    private static final Path DECIMAL_DIR  = RES_ROOT.resolve("decimalcard");
    private static final Path BINARY_DIR   = RES_ROOT.resolve("binarycard");
    private static final Path MASK_DIR     = RES_ROOT.resolve("mask");

    public boolean addSubMode;
    public Switch7 sw7 = Switch7.KA;
    public Switch8 sw8 = Switch8.CARD;
    public FieldSelect fieldSelect = FieldSelect.F0;
    public int zeroDetection = -1;
    public int signDetection = -1;

    public ABCMachine() {
        ca = new CA();
        ka = new KA();
        carryDrum = new Carry();
        conv = new Conversion(this);
        base10Reader = new Base10Reader(this);
        base2Reader = new Base2Reader(this);
        base2Punch = new Base2Punch(this);
        base10Storage = Base10Storage.getInstance();
        base2Storage = Base2Storage.getInstance();
    }

    public void doSomething(){
        String filePath =
                "C:\\Users\\willi\\OneDrive\\Desktop\\ABC\\GitHub\\sdmay25-29\\abc-emulator"
                        + "\\src\\main\\resources\\decimalcard\\dcard01.csv";
        try {
            base10Reader.readCard(filePath, 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ca.printData();

        ca.clear();
        Base10Punch punch = new Base10Punch();


        ca.printData();

    }


    /*--------------------SWITCHES---------------------------*/
    //SW1-6
    public void toggleFieldSelect(int i) {
        switch (i) {
            case  0: fieldSelect = FieldSelect.F1_5;
            case  1: fieldSelect = FieldSelect.F6_10;
            case  2: fieldSelect = FieldSelect.F11_15;
            case  3: fieldSelect = FieldSelect.F16_20;
            case  4: fieldSelect = FieldSelect.F21_25;
            case  5: fieldSelect = FieldSelect.F26_30;
            default: fieldSelect = FieldSelect.F0;
        }
    }

    //SW7
    public void toggleSW7() {
        sw7 = (sw7 == Switch7.KA ? Switch7.CARD_READER : Switch7.KA);
    }
    //SW8
    public void toggleSW8() {
        sw8 = (sw8 == Switch8.CARD ? Switch8.OVERDRAFT : Switch8.CARD);
    }
    //SW11
    public void clearCA() {
        ca.clear();
    }
    //SW12
    public void clearKA() {
        ka.clear();
    }

    /*------------------------PUSH-BUTTONS---------------------*/
    //PB1
    public void toggleAddSubMode() {
        addSubMode = !addSubMode;
    }
    //PB2


    /*-------------------ENUMS--------------------------*/
    public enum Switch7 { KA, CARD_READER}
    public enum Switch8 { CARD, OVERDRAFT}
    public enum FieldSelect {
        F0 (-1, "none"),
        F1_5 (0, "1‑5"),
        F6_10(5, "6‑10"),
        F11_15(10, "11‑15"),
        F16_20(15, "16‑20"),
        F21_25(20, "21‑25"),
        F26_30(25, "26‑30");

        public final int index;
        public final String label;
        FieldSelect(int idx, String lbl){ index = idx; label = lbl; }

        @Override public String toString(){ return label; }
    }

    public void setZeroDetection(int index) { zeroDetection = index;}
    public void clearZeroDetection(){ zeroDetection = -1; }
    public void setSignDetection(int index) { signDetection = index;}
    public void clearSignDetection(){ signDetection = -1; }
}