package src.main.java.asm;

import src.main.java.drum.CA;
import src.main.java.drum.Carry;
import src.main.java.drum.Drum;
import src.main.java.drum.KA;

public class AddSubMechanism {

    public static final int MSB = 0;
    public static final int LSB = 49;

    public void operation(CA ca, KA ka, Carry carryDrum, boolean addSubMode, int selectedCoefficiennt) {
        while (!ca.isZero(selectedCoefficiennt)) {
            carryDrum.clear();
            boolean sign = ca.getData()[selectedCoefficiennt][0];
            addSubWithKA(ca, ka, carryDrum, addSubMode);
            if (sign != ca.getData()[selectedCoefficiennt][0]) {
                addSubMode = !addSubMode;
                if (ka.getShiftCount() == 50){
                    break;
                }
                ka.shiftRight();
            }
        }
        ka.resetShiftCount();
    }

    public static void addSubWithKA(CA ca, KA ka, Carry carryDrum, boolean addSubMode) {
        for (int band = 0; band < 30; ++band) {
            processBand(ca.getData()[band], ka.getData()[band], carryDrum, band, addSubMode);
        }
    }

    public void addSubWithCard(CA ca, boolean[] card, Carry carryDrum, int bandIndex, boolean addSubMode) {
        carryDrum.clear();
        processBand(ca.getData()[bandIndex], card, carryDrum, bandIndex, addSubMode);
    }


    public static void processBand(boolean[] caBand, boolean[] kaCardBand, Carry carryDrum, int bandIndex, boolean addSubMode) {
        boolean carryIn = carryDrum.getData()[bandIndex][0];
        for (int bit = 49; bit >= 0; --bit) {
            ASMResult result = addSubBit(caBand[bit], kaCardBand[bit], carryIn, addSubMode);
            caBand[bit] = result.sum;
            carryIn = result.carryOut;
        }
        carryDrum.getData()[bandIndex][0] = carryIn;
    }

    public static class ASMResult {
        boolean sum;
        boolean carryOut;
        ASMResult(boolean sum, boolean carryOut) {
            this.sum = sum;
            this.carryOut = carryOut;
        }
    }

    public static ASMResult addSubBit(boolean X, boolean Y, boolean carryIn, boolean addSub) {
        boolean carryOut;
        boolean sum = X ^ Y ^ carryIn;
        if (!addSub) {
            carryOut = (X && Y) || (X && carryIn) || (carryIn && Y);
        } else{
            carryOut = ((!X) && Y) || ((!X) && carryIn) || (carryIn && Y);
        }
        return new ASMResult(sum, carryOut);
    }
}
