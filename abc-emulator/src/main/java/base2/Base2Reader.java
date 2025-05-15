package src.main.java.base2;

import src.main.java.ABCMachine;
import src.main.java.drum.Drum;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Base2Reader {
    private static final int ROWS = 30;
    private static final int COLS = 50;
    private static ABCMachine abc;

    public Base2Reader(ABCMachine abc) {
        this.abc = abc;
    }

    public static Drum readCard(Path file) throws IOException {
        // Create Drum to write to
        Drum drum = new Drum(ROWS, COLS);

        // Open CSV
        try (BufferedReader br = Files.newBufferedReader(file)) {
            String line;
            int row = 0;

            while ((line = br.readLine()) != null) {
                if (row >= ROWS)
                    throw new IOException("Too many rows (expected 30)");

                // Splits line
                String[] toks = line.split(",", -1);
                if (toks.length != COLS)
                    throw new IOException("Row " + row + " has " + toks.length + " columns (expected 50)");

                // Set bit (0: false, 1: true) at location on Drum
                for (int col = 0; col < COLS; ++col) {
                    boolean bit = "1".equals(toks[col].trim());
                    drum.setBit(row, col, bit);        // LSB‑left, MSB‑right
                }
                ++row;
            }
            if (row != ROWS)
                throw new IOException("Only " + row + " rows found (expected 30)");
        }

        // Put the new Drum onto CA
        for (int band = 0; band < ROWS; ++band) {
            abc.adder.addSubWithCard(abc.ca, drum.getData()[band], abc.carryDrum, band, abc.addSubMode);
        }

        return drum;
    }
}
