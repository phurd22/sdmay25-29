package src.main.java.drum;

public class KA extends Drum {

    private int shiftCount;

    public KA() {
        super();
        this.shiftCount = 0;
    }

    // Shifts bands
    public void shiftRightBand(int bandIndex) {
        boolean[] band = data[bandIndex];
        for (int i = 0; i < BITS_PER_BAND - 1; i++) {
            band[i] = band[i + 1];
        }
        band[BITS_PER_BAND - 1] = false;
        shiftCount++;
    }

    // Shift every band in KA drum by 1
    public void shiftRightAllBands() {
        for (int band = 0; band < NUM_BANDS; band++) {
            shiftRightBand(band);
        }
    }

    // Reset shift counter
    public void resetShiftCount() {
        shiftCount = 0;
    }

    public int getShiftCount() {
        return shiftCount;
    }
}
