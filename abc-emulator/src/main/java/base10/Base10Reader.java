package src.main.java.base10;

import src.main.java.ABCMachine;
import src.main.java.asm.AddSubMechanism;
import src.main.java.drum.CA;
import src.main.java.drum.Conversion;
import src.main.java.drum.KA;

public class Base10Reader {
    private static ABCMachine abc;

    public Base10Reader(ABCMachine abc) {
        Base10Reader.abc = abc;
    }

    public void readCard(int[] card) {
        for (int i = 0; i < card.length; ++i) {
            abc.conv.convert(String.valueOf(card[i]), i);
        }
    }
}
