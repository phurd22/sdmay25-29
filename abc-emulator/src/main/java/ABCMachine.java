package src.main.java;

import src.main.java.asm.AddSubMechanism;
import src.main.java.drum.*;

import static src.main.java.drum.Conversion.build50BitsFromConversion;

public class ABCMachine {
    private final CA ca;
    private final KA ka;
    private final Carry carry;
    private final AddSubMechanism asm;

    public ABCMachine() {
        this.ca = new CA();
        this.ka = new KA();
        this.carry = new Carry();
        this.asm = new AddSubMechanism(carry);
    }

    // Zero out the entire CA or KA
    public void zeroCA() {
        ca.clearAll();
    }
    public void zeroKA() {
        ka.clearAll();
        ka.resetShiftCount();
    }

    // COPY KA, CA => "KA = KA + CA"
    // Usually we'd zero KA first to get an exact copy
    public void copyCAtoKA() {
        ka.clearAll();
        asm.doOperation(ka, ca, false); // add
    }

    // "Add CA, KA" => CA <- CA + KA
    public void addCA_KA() {
        asm.doOperation(ca, ka, false);
    }

    // "Sub CA, KA" => CA <- CA - KA
    public void subCA_KA() {
        asm.doOperation(ca, ka, true);
    }

    // "ADDSR" => CA <- CA + (KA >> 1)
    public void addsrCA_KA() {
        // Make a temp copy of KA
        KA temp = new KA();
        temp.transfer(ka);
        for (int b = 0; b < Drum.NUM_BANDS; b++) {
            temp.shiftRightBand(b);
        }
        asm.doOperation(ca, temp, false);
    }

    // "SUBBSR" => CA <- CA - (KA >> 1)
    public void subbsrCA_KA() {
        KA temp = new KA();
        temp.transfer(ka);
        for (int b = 0; b < Drum.NUM_BANDS; b++) {
            temp.shiftRightBand(b);
        }
        asm.doOperation(ca, temp, true);
    }

    // DLOAD / BLOAD would be similar:
    //   clear CA, then doOperation(CA, cardDrum, false) to add the data in
    public void dload(Drum decimalCard) {
        ca.clearAll();
        asm.doOperation(ca, decimalCard, false);
    }

    public void bload(Drum binaryCard) {
        ca.clearAll();
        asm.doOperation(ca, binaryCard, false);
    }

    // BSTORE => produce a new drum with the same bits as CA
    public Drum bstoreCA() {
        Drum result = new Drum();
        result.transfer(ca);
        return result;
    }

    // Accessors
    public CA getCA() {
        return ca;
    }
    public KA getKA() {
        return ka;
    }
    public Carry getCarry() {
        return carry;
    }

    public void loadDecimalIntoCABand(int bandIndex, long decimalValue) {
        // 1) Convert decimalValue to a 15-digit zero-padded string
        //    so that index 0 => most significant digit
        //    If you prefer another approach, e.g. repeated % 10, that’s fine too.
        String decStr = String.format("%015d", decimalValue); // zero-pad to length 15

        // 2) For each digit i in [0..14],
        //    decade = 14 - i  (i=0 => decade=14, i=14 => decade=0)
        for (int i = 0; i < 15; i++) {
            char c = decStr.charAt(i);
            int digit = c - '0'; // 0..9
            int decade = 14 - i;

            if (digit != 0) {
                // 3) Build the 50-bit pattern from the conversion drum
                boolean[] pattern = build50BitsFromConversion(digit, decade);

                // 4) Add that pattern into CA’s selected band via ASM
                asm.doOperationBand(ca, bandIndex, pattern, false /* add */);
            }
        }
    }

}
