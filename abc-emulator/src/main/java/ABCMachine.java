package src.main.java;

import src.main.java.asm.AddSubMechanism;
import src.main.java.base10.Base10Punch;
import src.main.java.base10.Base10Storage;
import src.main.java.drum.*;
import src.main.java.base2.*;
import src.main.java.base10.Base10Reader;
import src.main.java.gui.model.Odometer.OdometerPanel;
import src.main.java.mask.MaskStorage;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/*
    Holds everything together, allows access.

 */
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
    public MaskStorage maskStorage;
    public AddSubMechanism adder = new AddSubMechanism();
    public OdometerPanel odometerPanel;

    private static final Path RES_ROOT = Paths.get("src", "main", "resources");

    private static final Path DECIMAL_DIR = RES_ROOT.resolve("decimalcard");
    private static final Path BINARY_DIR = RES_ROOT.resolve("binarycard");
    private static final Path MASK_DIR = RES_ROOT.resolve("mask");

    public boolean addSubMode;
    public Switch7 sw7 = Switch7.KA;
    public Switch8 sw8 = Switch8.OVERDRAFT;
    public Switch10 sw10 = Switch10.OFF;
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
        maskStorage = MaskStorage.getInstance();
        odometerPanel = new OdometerPanel(this);
        odometerPanel.setPreferredSize(new Dimension(240, 50));
    }


    /*--------------------SWITCHES---------------------------*/
    //SW1-6
    public void toggleFieldSelect(int i) {
        if (i == -1) {
            fieldSelect = FieldSelect.F0;
        } else if (i == 0) {
            fieldSelect = FieldSelect.F1_5;
        } else if (i == 1) {
            fieldSelect = FieldSelect.F6_10;
        } else if (i == 2) {
            fieldSelect = FieldSelect.F11_15;
        } else if (i == 3) {
            fieldSelect = FieldSelect.F16_20;
        } else if (i == 4) {
            fieldSelect = FieldSelect.F21_25;
        } else if (i == 5) {
            fieldSelect = FieldSelect.F26_30;
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
    //SW10
    public void toggleSW10() { sw10 = (sw10 == Switch10.ON ? Switch10.OFF : Switch10.ON);}
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
    public enum Switch7 { KA, CARD_READER }
    public enum Switch8 { CARD, OVERDRAFT }
    public enum Switch10 { OFF, ON }
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