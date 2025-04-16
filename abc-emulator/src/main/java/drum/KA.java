package src.main.java.drum;

public class KA extends Drum {
    public static final int BAND_COUNT = 30;
    public static final int BIT_COUNT = 50;
    private int shiftCount;

    public KA() {
        super(BAND_COUNT, BIT_COUNT);
        this.shiftCount = 0;
    }

    public void shiftRight() {
        for (int j = 0; j < data.length; j++) {
            boolean oldSign = data[j][49];  // save the current sign bit
            // Shift bits [0..48] to the right by 1 (LSB is dropped, bit i+1 becomes bit i)
            for (int i = 0; i < 49; i++) {
                data[j][i] = data[j][i + 1];
            }
            // Fill the MSB with the old sign bit to preserve sign (arithmetic shift)
            data[j][49] = oldSign;
        }
        ++shiftCount;
    }

    public void shiftLeft() {
        for (int j = 0; j < data.length; j++) {
            // Save the current sign bit from this band
            boolean oldSign = data[j][49];

            // Shift bits [1..49] to the left by 1
            // so bit i-1 moves into bit i
            for (int i = 49; i > 0; i--) {
                data[j][i] = data[j][i - 1];
            }

            // Typically we fill the new LSB (bit 0) with false (0)
            // but if you have special rules, you could do that here
            data[j][0] = false;

            // Restore the original sign bit (arithmetic shift)
            data[j][49] = oldSign;
        }
        ++shiftCount;
    }

    // Reset shift counter
    public void resetShiftCount() {
        shiftCount = 0;
    }

    public int getShiftCount() {
        return shiftCount;
    }
}
