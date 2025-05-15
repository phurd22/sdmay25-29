package src.main.java.drum;

/*
    Carry Borrow Drum, overflow buffer.
    Smaller drum with 1 bit for each of the 30 bands/coefficients.
    Is constantly set and read during operations.
    Is the carry propagation during the bit-wise arithmetic.
 */
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