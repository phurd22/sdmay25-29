package src.main.java.drum;

import src.main.java.ABCMachine;
import src.main.java.asm.AddSubMechanism;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static src.main.java.asm.AddSubMechanism.addSubWithCard;

public class Conversion {
    private static boolean[][] lookUpTable;
    private static ABCMachine abc;

    public Conversion(ABCMachine abc) {
        String filePath = "C:\\Users\\willi\\OneDrive\\Desktop\\convdrum.csv";
        lookUpTable = readCsvToBooleanArray(filePath);
        Conversion.abc = abc;
    }
    //------------------------------------------USING CSV FILE----------------------------------------------------------
    public static boolean[][] readCsvToBooleanArray(String filePath) {
        List<boolean[]> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Use split with limit -1 to include trailing empty fields.
                String[] tokens = line.split(",", -1);
                boolean[] boolRow = new boolean[tokens.length];
                for (int i = 0; i < tokens.length; i++) {
                    // "1" represents true, everything else (or empty) represents false.
                    boolRow[i] = tokens[i].trim().equals("1");
                }
                rows.add(boolRow);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Convert the list to a 2D boolean array.
        int numRows = rows.size();
        if (numRows != 60) {
            System.err.println("Warning: Expected 60 rows (indices from -4 to 55) but found " + numRows);
        }
        boolean[][] dataArray = new boolean[numRows][];
        for (int i = 0; i < numRows; i++) {
            dataArray[i] = rows.get(i);
        }
        return dataArray;
    }

    public void convert(int decade, int digit, boolean addSubMode, int bandIndex) {
        boolean[] band = new boolean[50];
        int colIndex = 0;
        int brushOffset = 0;
        long value = (long) (digit * (Math.pow(10, decade)));

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
        addSubWithCard(abc.ca, band, abc.carryDrum, bandIndex, addSubMode);
    }



    public boolean[] convert(String decimal, int startBandIndex) {
        //--------------------------PADDING-----------------------------------
        // check if number is negative
        boolean isNegative = decimal.startsWith("-");

        // remove negative sign if it is
        String number = isNegative ? decimal.substring(1) : decimal;

        // parse number
        java.math.BigInteger pieced = new java.math.BigInteger(number);

        // pad with 0's making it 15 digits long
        String padded = String.format("%015d", pieced);

        //----------------------PADDING DONE-----------------------------------
        //----------------------START LOOKUP-----------------------------------\
        boolean[] band = new boolean[50];
        int colIndex = 0;
        int brushOffset = 0;
        for (int decade = 14; decade >= 0; --decade) {
            band = new boolean[50];
            int digit = Character.getNumericValue(padded.charAt(14 - decade));
            long value = (long) (Character.getNumericValue(padded.charAt(14 - decade)) * (Math.pow(10, decade)));

            // start lookup on csv file
            if (digit != 0) {
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
                addSubWithCard(abc.ca, band, abc.carryDrum, startBandIndex, isNegative);
            }
        }
        return band;
    }

    public void tableLookup(int colIndex, int brushOffset, boolean[] temp) {
        for (int timeStep = 0; timeStep < 50; ++timeStep) {
            temp[timeStep] = lookUpTable[49 - timeStep + brushOffset][colIndex];
        }
    }

    public static void printBooleanArrayAsNumbers(boolean[][] data) {
        String ANSI_RESET = "\u001B[0m";
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_RED = "\u001B[31m";
        int j = -4;
        for (boolean[] row : data) {
            System.out.print(j + ": ");
            ++j;
            for (int i = 0; i < row.length; i++) {
                // Choose color based on the boolean value
                if (row[i]) {
                    System.out.print(ANSI_GREEN + "1" + ANSI_RESET);
                } else {
                    System.out.print(ANSI_RED + "0" + ANSI_RESET);
                }
                // Print comma separator if not the last element of the row
                if (i < row.length - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println();
        }
    }
}
