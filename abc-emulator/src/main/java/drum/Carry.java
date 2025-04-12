package src.main.java.drum;

import java.util.Arrays;

public class Carry {
    private boolean[] carryBits;  // e.g. length 30
    private static final int NUM_BANDS = 30;

    public Carry() {
        carryBits = new boolean[NUM_BANDS];
    }

    // Clear all carry bits to false
    public void clearAll() {
        Arrays.fill(carryBits, false);
    }

    // Accessors
    public boolean getCarry(int bandIndex) {
        return carryBits[bandIndex];
    }
    public void setCarry(int bandIndex, boolean value) {
        carryBits[bandIndex] = value;
    }
}