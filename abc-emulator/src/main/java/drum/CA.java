package src.main.java.drum;

/*
    CA Drum (Counter Abaci), one of the two identical rotating memory drums.
    32 bands - 30 active and 2 spare.
    50 bits per band.
    Has cards loaded into it. Either Base-2 or Base-10.
    Adds or Subtracts KA to itself.
 */
public class CA extends Drum {
    public static final int BAND_COUNT = 30;
    public static final int BIT_COUNT = 50;
    public CA() {
        super(BAND_COUNT, BIT_COUNT);
    }
}
