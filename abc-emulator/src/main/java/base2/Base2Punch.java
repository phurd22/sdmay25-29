package src.main.java.base2;

import src.main.java.drum.CA;

/**
 * The Base-2 Punch of the ABC.
 * This component simulates punching the results onto a card in binary form.
 * It captures the final state of the CA drum as output data.
 */
public class Base2Punch {
    private boolean[][] outputData;
    private int bands;
    private int bits;

    /**
     * Punches (captures) the current content of the given CA drum.
     * This simulates writing the binary results to an output medium (e.g., a card).
     *
     * @param ca the CA drum containing the final results to punch out
     */
    public void punch(CA ca) {
        bands = ca.getBandCount();
        bits = ca.getBitCount();
        // Initialize storage for output data
        outputData = new boolean[bands][bits];
        // Copy each bit from CA into the output storage
        for (int i = 0; i < bands; i++) {
            for (int j = 0; j < bits; j++) {
                outputData[i][j] = ca.getBit(i, j);
            }
        }
    }

    /**
     * Returns the output data captured by this punch as a 2D boolean array.
     * Each [i][j] entry corresponds to bit j of band i (false = 0, true = 1).
     */
    public boolean[][] getOutputData() {
        return outputData;
    }

    /**
     * Returns a formatted string of the output binary data (similar to Drum's representation).
     */
    public String toString() {
        if (outputData == null) {
            return "No data punched yet.";
        }
        StringBuilder sb = new StringBuilder("Punched Output:\n");
        for (int i = 0; i < bands; i++) {
            sb.append(String.format("Band %2d: ", i));
            for (int j = bits - 1; j >= 0; j--) {
                sb.append(outputData[i][j] ? '1' : '0');
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
