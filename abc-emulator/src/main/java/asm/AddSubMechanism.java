package src.main.java.asm;

import src.main.java.drum.CA;
import src.main.java.drum.Carry;
import src.main.java.drum.Drum;
import src.main.java.drum.KA;

public class AddSubMechanism {
    private final Carry carryDrum;

    public AddSubMechanism(Carry carryDrum) {
        this.carryDrum = carryDrum;
    }

    public void doOperation(Drum target, Drum source, boolean subtract) {
        carryDrum.clearAll();

        for (int band = 0; band < Drum.NUM_BANDS; band++) {
            processBand(target.getBand(band), source.getBand(band), band, subtract);
        }
    }

    private void processBand(boolean[] targetBand, boolean[] sourceBand, int bandIndex, boolean subtract) {
        boolean carryIn = false;

        for (int bit = 0; bit < Drum.BITS_PER_BAND; bit++) {
            int a = targetBand[bit] ? 1 : 0;
            int b = sourceBand[bit] ? 1 : 0;
            int c = carryIn ? 1 : 0;

            int sum;
            if (!subtract) {
                // add => a + b + c
                sum = a + b + c;
            } else {
                // sub => a - (b + c)
                sum = a - (b + c);
            }

            boolean resultBit;
            boolean carryOut;
            if (!subtract) {
                // sum in [0..3]
                resultBit = (sum % 2) == 1;
                carryOut = (sum >= 2);
            } else {
                // sum in [-2..1]
                switch (sum) {
                    case 1:
                        resultBit = true;
                        carryOut = false;
                        break;
                    case 0:
                        resultBit = false;
                        carryOut = false;
                        break;
                    case -1:
                        resultBit = true;
                        carryOut = true;  // borrow
                        break;
                    default: // -2
                        resultBit = false;
                        carryOut = true;
                        break;
                }
            }

            targetBand[bit] = resultBit;
            carryIn = carryOut;
        }
        // end of 50 bits => store final carryOut in carryDrum if you want
        carryDrum.setCarry(bandIndex, carryIn);
    }

    public void doOperationBand(Drum targetDrum, int bandIndex, boolean[] sourceBits, boolean subtract) {
        // Typically clear carry bit for this band if you want a fresh operation:
        carryDrum.setCarry(bandIndex, false);

        boolean[] targetBand = targetDrum.getBand(bandIndex);

        boolean carryIn = false;
        for (int bit = 0; bit < Drum.BITS_PER_BAND; bit++) {
            int a = targetBand[bit] ? 1 : 0;
            int b = sourceBits[bit] ? 1 : 0;
            int c = carryIn ? 1 : 0;

            int sum;
            if (!subtract) {
                sum = a + b + c;
            } else {
                sum = a - (b + c);
            }

            boolean result;
            boolean carryOut;
            if (!subtract) {
                // sum = 0..3
                result = (sum % 2) != 0;
                carryOut = (sum >= 2);
            } else {
                // sum = -2..1
                switch (sum) {
                    case 1:  result = true;  carryOut = false; break;
                    case 0:  result = false; carryOut = false; break;
                    case -1: result = true;  carryOut = true;  break;
                    default: // -2
                        result = false; carryOut = true;  break;
                }
            }

            targetBand[bit] = result;
            carryIn = carryOut;
        }
        // If you wish to store the final carryOut in the carry drum:
        carryDrum.setCarry(bandIndex, carryIn);
    }
}
