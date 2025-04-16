package src.main.java.asm;

import src.main.java.drum.CA;
import src.main.java.drum.Carry;
import src.main.java.drum.KA;

public class AddSubMechanism {

    public void operation(CA ca, KA ka, Carry carryDrum, boolean addSubMode, int selectedCoefficiennt) {
        carryDrum.clear();
        while (!ca.isZero(selectedCoefficiennt)) {
            boolean sign = ca.getData()[selectedCoefficiennt][0];
            addSubWithKA(ca, ka, carryDrum, addSubMode);
            if (sign != ca.getData()[selectedCoefficiennt][0]) {
                System.out.println("Swap");
                addSubMode = !addSubMode;
                if (ka.getShiftCount() == 50){
                    break;
                }
                ka.shiftLeft();
            }
        }
    }

    public static void addSubWithKA(CA ca, KA ka, Carry carryDrum, boolean addSubMode) {
        for (int band = 0; band < 30; ++band) {
            carryDrum.clear();
            processBand(ca.getData()[band], ka.getData()[band], carryDrum, band, addSubMode);
            if (addSubMode) {
                boolean[] extraOne = new boolean[50];
                extraOne[49] = true;
                processBand(ca.getData()[band], extraOne, carryDrum, band, !addSubMode);
            }
        }
    }

    public static void addSubWithCard(CA ca, boolean[] card, Carry carryDrum, int bandIndex, boolean addSubMode) {
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
        // Invert Y if we are doing subtraction
        boolean yPrime = Y ^ addSub;

        // Perform the usual 3-input full-adder on X, yPrime, and carryIn
        boolean sum = X ^ yPrime ^ carryIn;
        boolean carryOut = (X && yPrime) || (X && carryIn) || (yPrime && carryIn);

        return new ASMResult(sum, carryOut);
    }
}
