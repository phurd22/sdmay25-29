package src.main.java.base2;

import src.main.java.ABCMachine;
import src.main.java.drum.Drum;

public class Base2Reader {
    private final ABCMachine abc;

    public Base2Reader(ABCMachine abc) {
        this.abc = abc;
    }

    public void readCard(Drum drum) {
        for (int band = 0; band < 30; ++band) {
            abc.adder.addSubWithCard(abc.ca, drum.getData()[band], abc.carryDrum, band, abc.addSubMode);
        }
    }
}
