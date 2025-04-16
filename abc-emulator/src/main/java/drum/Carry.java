package src.main.java.drum;

import java.util.Arrays;

public class Carry extends Drum {
    public static final int BAND_COUNT = 30;
    public static final int BIT_COUNT = 1;

    public Carry() {
        super(BAND_COUNT, BIT_COUNT);
    }

    public boolean getCarry(int i) {
        return data[i][0];
    }
}