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
        for (int band = 0; band < data.length; ++band) {
            boolean sign = data[band][MSB];
            for (int bit = LSB; bit > MSB; --bit) {
                data[band][bit] = data[band][bit - 1];
            }
            data[band][MSB] = sign;
        }
        ++shiftCount;
    }

    public void shiftLeft() {
        for (int band = 0; band < data.length; ++band) {
            for (int bit = MSB; bit < LSB; ++bit) {
                data[band][bit] = data[band][bit + 1];
            }
            data[band][LSB] = false;
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
