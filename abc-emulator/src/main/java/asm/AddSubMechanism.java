package src.main.java.asm;

import src.main.java.drum.CA;
import src.main.java.drum.Carry;
import src.main.java.drum.KA;

public class AddSubMechanism {
    public static final int MSB = 0;
    public static final int LSB = 49;

    /*
    Key Steps - Derived by Alexander Stoytchev
    1. Pick two equations                                   - CA and KA
    2. Pick a variable to eliminate                         - selectedCoefficient
    3. Is the coefficient for the variable that             - ca.isZero(selectedCoefficient)
        we try to eliminate in the second equation
           equal to 0? If yes, goto step 7 and
           exit the loop. If no, go to the next step.
    4. Check the signs of the coefficients that multiply    - addSubMode
        this variable in the two equations
    - if they are the same (+,+) or (-,-) then use
        subtraction in step 5
    - if they are different (+,-) or (-,+) then use
        addition in step 5
    5. Perform the operation from step 4                    - Add       KA to CA
    (addition or subtraction), treating the coefficients            or
    of the two equations as vectors. For example, subtract - Subtract  KA from CA
    the first equation form the second. Or add the first
    equation to the second. Always update only the
    coefficients of the second equation.
    6. Did the sign of the variable (e.g., x) change in     - ca.getData()[selectedCoefficient][MSB]
    the **second** equation? If yes, divide all             - ka.shiftRight();
    coefficients in the **first** equation by 2
    (shift right). Don't do anything to the second equation.
    GOTO Step 3.
    7. End Loop
     */
    public void operation(CA ca, KA ka, Carry carryDrum, boolean addSubMode, int selectedCoefficient) {
        while (!ca.isZero(selectedCoefficient)) {
            carryDrum.clear();
            boolean sign = ca.getData()[selectedCoefficient][MSB];
            addSubWithKA(ca, ka, carryDrum, addSubMode);
            if (sign != ca.getData()[selectedCoefficient][MSB]) {
                addSubMode = !addSubMode;
                if (ka.getShiftCount() == 50){
                    break;
                }
                ka.shiftRight();
            }
        }
        ka.resetShiftCount();
    }

    /*
        Step 5 helper. Uses 3 Drums with the selected mode to process all 30 Coefficients
     */
    public static void addSubWithKA(CA ca, KA ka, Carry carryDrum, boolean addSubMode) {
        for (int band = 0; band < 30; ++band) {
            processBand(ca.getData()[band], ka.getData()[band], carryDrum, band, addSubMode);
        }
    }

    /*
        Called from Conversion, "Puts On" one Coefficient at a time
     */
    public void addSubWithCard(CA ca, boolean[] card, Carry carryDrum, int bandIndex, boolean addSubMode) {
        carryDrum.clear();
        processBand(ca.getData()[bandIndex], card, carryDrum, bandIndex, addSubMode);
    }

    /*
        Processes all 50 bits of a band (the 50 bit boolean array).
     */
    public static void processBand(boolean[] caBand, boolean[] kaCardBand, Carry carryDrum, int bandIndex, boolean addSubMode) {
        boolean carryIn = carryDrum.getData()[bandIndex][0];
        for (int bit = LSB; bit >= MSB; --bit) {
            ASMResult result = addSubBit(caBand[bit], kaCardBand[bit], carryIn, addSubMode);
            caBand[bit] = result.sum;
            carryIn = result.carryOut;
        }
        carryDrum.getData()[bandIndex][0] = carryIn;
    }

    /*
        Helps the processBand function. In boolean arithmetic there are Sum and Carry bits.
        The ASMResult helps carry both of those for each addition of subtraction of a bit.
     */
    public static class ASMResult {
        boolean sum;
        boolean carryOut;
        ASMResult(boolean sum, boolean carryOut) {
            this.sum = sum;
            this.carryOut = carryOut;
        }
    }

    /*
        Taken from Clifford Berry Manual Late 41' Early 42'
     */
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
