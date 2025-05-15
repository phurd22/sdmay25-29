package src.main.java.drum;

import src.main.java.ABCMachine;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/*
    Built in decimal to binary ROM.
    Read only drum with 50 bit patterns for digits {1, 5, 3, 7, 9} times each power of 10.
    Loads CA with converted values.
    Very odd and mind tangling.
 */
public class Conversion {
    private static boolean[][] lookUpTable;
    private static ABCMachine abc;

    public Conversion(ABCMachine abc) {
        try (InputStream in = Conversion.class.getClassLoader().getResourceAsStream("src/main/resources/lookup/convdrum.csv")) {
            if (in == null) {
                throw new IllegalArgumentException("lookup/convdrum.csv not found");
            }
            lookUpTable = readCsvToBooleanArray(new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)));
        } catch (IOException ex) {
            throw new RuntimeException("Unable to load lookup table", ex);
        }
        this.abc = abc;
    }
    //------------------------------------------USING CSV FILE----------------------------------------------------------
    // Imports the hardcoded csv lookup table for decimal to binary
    private static boolean[][] readCsvToBooleanArray(BufferedReader br) throws IOException {
        List<boolean[]> rows = new ArrayList<>();

        String line;
        while ((line = br.readLine()) != null) {
            // Split on commas
            String[] cells = line.split(",", -1);

            boolean[] row = new boolean[cells.length];
            for (int i = 0; i < cells.length; ++i) {
                row[i] = !cells[i].isEmpty() && cells[i].trim().charAt(0) == '1';
            }
            rows.add(row);
        }

        // Convert List<boolean[]> to boolean[][]
        return rows.toArray(new boolean[rows.size()][]);
    }

    // Calculates column index and brush offset for correct number (digit in certain decade)
    public void convert(int decade, int digit, boolean addSubMode, int bandIndex) {
        boolean[] band = new boolean[50];
        int colIndex = 0;
        int brushOffset = 0;
        long value = (long) (digit * (Math.pow(10, decade)));

        // Very specific calculation for proper brush offset and column index in looktable
        if (decade > 0) {
            if (value % 9 == 0) {
                colIndex = 32 + (decade * 2);
                brushOffset = 4;
            } else if (value % 7 == 0) {
                colIndex = 31 + (decade * 2);
                brushOffset = 4;
            } else if (value % 3 == 0) {
                colIndex = 16 + decade;
                brushOffset = 5 - (digit / 3);
            } else if (value % 5 == 0) {
                int tempValue = 5;
                colIndex = 1 + decade;
                if (digit != 5) {
                    tempValue = digit * 10;
                    colIndex = decade;
                }
                brushOffset = (int) (4 - (Math.log(tempValue / 5.0) / Math.log(2)));
            }
        } else {
            if (value % 9 == 0) {           // 9
                colIndex = 32;
                brushOffset = 4;
            } else if (value % 7 == 0) {    // 7
                colIndex = 31;
                brushOffset = 4;
            } else if (value % 3 == 0) {    // 3, 6
                colIndex = 16;
                brushOffset = 5 - (digit / 3);
            } else if (value % 5 == 0) {    // 5
                int tempValue = 5;
                if (digit != 5) {
                    tempValue = digit * 10;
                }
                colIndex = 1;
                brushOffset = (int) (4 - (Math.log(tempValue / 5.0) / Math.log(2)));
            } else {                        // 1, 2, 4, 8
                colIndex = 0;
                brushOffset = (int) (4 - (Math.log(digit) / Math.log(2)));
            }
        }
        tableLookup(colIndex, brushOffset, band);
        // Adds each column at a time
        abc.adder.addSubWithCard(abc.ca, band, abc.carryDrum, bandIndex, addSubMode);
    }

    // Searches the csv convert boolean[][] for correct column to copy onto the temp vector
    public void tableLookup(int colIndex, int brushOffset, boolean[] temp) {
        for (int timeStep = 0; timeStep < 50; ++timeStep) {
            temp[timeStep] = lookUpTable[49 - timeStep + brushOffset][colIndex];
        }
    }

    // Extra print function
    public static void printBooleanAsOneNZero(boolean[] row) {
        String ANSI_RESET = "\u001B[0m";
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_RED = "\u001B[31m";

            for (int i = 0; i < row.length; ++i) {
                if (row[i]) {
                    System.out.print(ANSI_GREEN + "1" + ANSI_RESET);
                } else {
                    System.out.print(ANSI_RED + "0" + ANSI_RESET);
                }
            }
            System.out.println();
    }
}
