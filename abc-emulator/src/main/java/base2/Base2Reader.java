package src.main.java.base2;

import src.main.java.drum.Drum;

import java.util.Random;

/**
 * The Base-2 Reader of the ABC.
 * This component simulates reading binary (base-2) encoded coefficients from a punched card or another source.
 * It uses mock data to fill a drum (e.g., CA or KA) with 30 coefficients of 50 bits each.
 */
public class Base2Reader {
    private Random random;

    public Base2Reader() {
        // Initialize a random number generator for mock data
        this.random = new Random();
    }

    /**
     * Reads 30 binary coefficients (50-bit each) as mock data and stores them into the given drum.
     * Each band's 50-bit pattern is randomly generated.
     *
     * @param drum the Drum (e.g., CA or KA) to fill with binary coefficients
     */
    public void readCoefficients(Drum drum) {
        int bands = drum.getBandCount();
        int bits = drum.getBitCount();
        // We expect 50 bits per coefficient as per ABC design
        if (bits < 50) {
            throw new IllegalArgumentException("Target drum must support 50-bit coefficients");
        }
        // Fill each band with a random 50-bit pattern
        for (int band = 0; band < bands; band++) {
            for (int bit = 0; bit < bits; bit++) {
                boolean bitVal = random.nextBoolean();
                drum.setBit(band, bit, bitVal);
            }
        }
    }
}
