package src.main.java.drum;

import java.util.Arrays;

public class Drum {

    public static final int NUM_BANDS = 30;                         // Coefficients
    public static final int BITS_PER_BAND = 50;               // Bits
    protected final boolean[][] data;                    // 2D array representing contacts

    // Constructor
    public Drum() {
        data = new boolean[NUM_BANDS][BITS_PER_BAND];
    }

    // Returns entire 2D array
    public boolean[][] getData() {
        return data;
    }

    // Returns 50-bit array for specific band
    public boolean[] getBand(int bandIndex) {
        return data[bandIndex];
    }

    // Clears entire drum to false (0)
    public void clearAll() {
        for (int band = 0; band < NUM_BANDS; band++) {
            Arrays.fill(data[band], false);
        }
    }

    // Clears one band to false (0)
    public void clearBand(int bandIndex) {
        Arrays.fill(data[bandIndex], false);
    }

    // Returns true if every bit is false (0)
    public boolean bandIsZero(int bandIndex) {
        for (boolean bit : data[bandIndex]) {
            if (bit) return false;
        }
        return true;
    }

    // Returns true if MSB is set, meaning negative
    public boolean bandIsNegative(int bandIndex) {
        return data[bandIndex][BITS_PER_BAND - 1]; // index 49
    }

    // Transfer CA to KA
    public void transfer(Drum KA) {
        boolean[][] src = KA.data;
        for (int b = 0; b < NUM_BANDS; b++) {
            System.arraycopy(src[b], 0, this.data[b], 0, BITS_PER_BAND);
        }
    }
}
