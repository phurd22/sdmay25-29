package src.main.java.drum;

import java.util.HashMap;
import java.util.Map;

public class Conversion {

    private static final long[] POW10 = {
            1L,
            10L,
            100L,
            1000L,
            10000L,
            100000L,
            1000000L,
            10000000L,
            100000000L,
            1000000000L,
            10000000000L,
            100000000000L,
            1000000000000L,
            10000000000000L,
            100000000000000L
    };

    /**
     * Returns the bit (0 or 1) from the "conversion drum"
     * for the given digit, decade, and timestep.
     *
     * @param digit   0..9 (the decimal digit)
     * @param decade  0..14 (10^decade is the multiplier)
     * @param timestep 0..59 (bit position in the 60-step rotation)
     * @return 0 or 1, the bit for (digit * 10^decade) at the given timestep
     *         (LSB = timestep 0). Returns 0 for timesteps >= 50.
     */
    public static int getBit(int digit, int decade, int timestep) {
        // If the drum is only 50 bits wide, timesteps 50..59 are empty => return 0
        if (timestep >= 50) {
            return 0;
        }

        // Validate input ranges (digit in 0..9, decade in 0..14).
        // You could throw an exception or just clamp:
        if (digit < 0 || digit > 9) return 0;
        if (decade < 0 || decade > 14) return 0;
        if (timestep < 0 || timestep > 59) return 0; // out of normal range

        // Compute the long value for (digit * 10^decade).
        long value = digit * POW10[decade];

        // Extract the bit at position 'timestep' (LSB = index 0).
        // If that bit is set, return 1; otherwise 0.
        long mask = 1L << timestep;
        return ((value & mask) != 0) ? 1 : 0;
    }

    public static boolean[] build50BitsFromConversion(int digit, int decade) {
        // 50 bits for the ABC’s representation
        boolean[] bits = new boolean[50];
        // The conversion drum function returns 0 or 1 for each timestep in [0..59],
        // but we only need [0..49] for the 50 actual bits (timesteps ≥ 50 are always 0).
        for (int t = 0; t < 50; t++) {
            int bitVal = getBit(digit, decade, t); // 0 or 1
            bits[t] = (bitVal == 1);
        }
        return bits;
    }

}
