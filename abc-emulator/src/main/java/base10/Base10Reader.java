package src.main.java.base10;

import src.main.java.ABCMachine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Base10Reader {
    private static final int SEGS= 5;
    private static final int ROWS = 10;
    private static final int COLS = 16;
    private final ABCMachine abc;
    public Base10Reader(ABCMachine abc) {
        this.abc = abc;
    }

    public void readCard(int[] card) {
        for (int i = 0; i < card.length; ++i) {
            abc.conv.convert(String.valueOf(card[i]), i);
        }
    }

    public void readCard(String filePath, int band) throws IOException {

        final boolean[][][] card = new boolean[SEGS][ROWS][COLS];

        FileReader csv = new FileReader(filePath);
        try (BufferedReader br = new BufferedReader(csv)){
            String line;
            int row = 0;

            while((line = br.readLine()) != null && row < 10) {
                String[] parts = line.split("\\|");
                if (parts.length < 6) {
                    throw new IOException("Incorrect Line: " + line);
                }

                for (int seg = 0; seg < SEGS; ++seg) {
                    String[] cols = parts[seg].split(",");
                    for (int col = 0; col < COLS && col < cols.length; ++col) {
                        if ("1".equals(cols[col].trim())) {
                            card[seg][row][col] = true;
                        }
                    }
                }
                ++row;
            }
            if (row != 10) {
                throw new IOException("Expected 10 rows, there are: " + row);
            }
        }

        for (int seg = 0; seg < SEGS; ++seg) {
            boolean addSubMode = false;             // Addition
            boolean isStarted = false;              // Used for negative numbers

            for (int col = 0; col < COLS; ++col) {
                int punchedRow = -1;
                for (int row = 0; row < ROWS; ++row) {
                    if (card[seg][row][col]) {
                        punchedRow = row;
                        break;
                    }
                }
                if (punchedRow == -1) continue;
                if (!isStarted) {
                    if (punchedRow == 0) {
                        addSubMode = true;          // Subtraction for leading 0
                        continue;                   // Don't send this digit
                    } else {
                        isStarted = true;
                    }
                }
                int decade = COLS - 1 - col;
                int digit = punchedRow;
                abc.conv.convert(decade, digit, addSubMode, band + seg);
            }
        }
    }
}