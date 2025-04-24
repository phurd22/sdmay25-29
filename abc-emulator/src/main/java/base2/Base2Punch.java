package src.main.java.base2;

import src.main.java.ABCMachine;
import src.main.java.drum.Drum;

public class Base2Punch {
    private final ABCMachine abc;

    public Base2Punch(ABCMachine abc) {
        this.abc = abc;
    }

    public void punchCard() {
        Drum card = abc.ca.deepCopy();
        abc.base2Storage.add(card);
    }
}
