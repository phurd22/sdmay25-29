import java.util.*;
import java.math.BigInteger;

public class GaussianBinarySolver {

    private static final int SCALE = 1;       // multiply every coefficient by this factor
    private static final int BIT_WIDTH = 50;      // simulate 50-bit registers (2's complement)
    private static final int SHIFT_LIMIT = 49;    // detect if 49 shifts (i.e. 50-bit capacity reached)

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Main loop: keep asking for a system until the user quits.
        while (true) {
            System.out.println("Enter a linear system in MATLAB format (or 'q' to quit):");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("q")) {
                System.out.println("Exiting.");
                break;
            }

            BinaryRow[] matrix = parseSystem(input);
            if (matrix == null) {
                System.out.println("Invalid input format. Please try again.\n");
                continue;
            }

            // Check dimensions: Expect n equations and n+1 columns (augmented matrix)
            int n = matrix.length;
            if (matrix[0].coeffs.length != n + 1) {
                System.out.println("Invalid system dimensions. Expecting n equations with n+1 columns.");
                continue;
            }

            // Scale the matrix to help precision
            scaleMatrix(matrix, SCALE);

            System.out.println("\nOriginal System (scaled) in binary:");
            printFormattedMatrix(matrix);

            // Solve the system using our modified Gaussian elimination.
            BinaryRow[] solvedRows = solveSystem(matrix);

            // Extract solution: for each solved row, solution = - (constant) / (pivot)
            BigInteger[] solution = extractSolution(solvedRows);

            System.out.println("\nSolution (in decimal):");
            printSolution(solution);
            System.out.println("\n-----------------------------\n");
        }
        scanner.close();
    }

    //===========================================================================
    // Helper Class: BinaryRow
    //===========================================================================
    // Features a row (drum) of coefficients (as BigIntegers) and a shift counter.
    public static class BinaryRow {
        BigInteger[] coeffs;  // the row’s coefficients (each column/band; last element is constant)
        int shiftCount;       // number of times this row has been shifted right

        public BinaryRow(BigInteger[] coeffs) {
            this.coeffs = coeffs;
            this.shiftCount = 0;
        }

        public BinaryRow clone() {
            BigInteger[] newCoeffs = new BigInteger[coeffs.length];
            for (int i = 0; i < coeffs.length; i++) {
                newCoeffs[i] = coeffs[i];
            }
            BinaryRow copy = new BinaryRow(newCoeffs);
            copy.shiftCount = this.shiftCount;
            return copy;
        }
    }

    //===========================================================================
    // Input and Formatting Methods
    //===========================================================================

    // Parses a system from a MATLAB-like string.
    // Example input: [1, 2, 3, 4; 5, 6, 7, 8; 9, 10, 11, 12]
    // Returns an array of BinaryRow, one per equation.
    private static BinaryRow[] parseSystem(String input) {
        try {
            input = input.replace("[", "").replace("]", "");
            String[] rowStrings = input.split(";");
            int n = rowStrings.length;
            BinaryRow[] matrix = new BinaryRow[n];
            for (int i = 0; i < n; i++) {
                String[] elems = rowStrings[i].trim().split(",");
                int m = elems.length;
                BigInteger[] coeffs = new BigInteger[m];
                for (int j = 0; j < m; j++) {
                    int val = Integer.parseInt(elems[j].trim());
                    coeffs[j] = BigInteger.valueOf(val);
                }
                matrix[i] = new BinaryRow(coeffs);
            }
            return matrix;
        } catch (Exception e) {
            return null;
        }
    }

    // Multiply every coefficient in the matrix by 'factor'
    private static void scaleMatrix(BinaryRow[] matrix, int factor) {
        BigInteger f = BigInteger.valueOf(factor);
        for (BinaryRow row : matrix) {
            for (int i = 0; i < row.coeffs.length; i++) {
                row.coeffs[i] = row.coeffs[i].multiply(f);
            }
        }
    }

    // Print the matrix in nicely formatted, right‐justified columns.
    // Each band is printed as: (Decimal)'50-bitBinary'variable.
    // For variable columns, the variable is a, b, c, …; the constant column is labeled C.
    private static void printFormattedMatrix(BinaryRow[] matrix) {
        for (BinaryRow row : matrix) {
            printRow(row);
        }
    }

    // Converts a BigInteger to a fixed-width (BIT_WIDTH) two's complement binary string.
    private static String toBinaryString(BigInteger value, int width) {
        // Compute value mod 2^width to simulate a register of width 'width'
        BigInteger modVal = value.mod(BigInteger.TWO.pow(width));
        String s = modVal.toString(2);
        while (s.length() < width) {
            s = "0" + s;
        }
        if (s.length() > width) {
            s = s.substring(s.length() - width);
        }
        return s;
    }

    // Print a BinaryRow using the desired format.
    private static void printRow(BinaryRow row) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < row.coeffs.length; i++) {
            String var;
            if (i < row.coeffs.length - 1) {
                var = String.valueOf((char)('a' + i));
            } else {
                var = "C";
            }
            sb.append("(").append(row.coeffs[i].toString()).append(")")
                    .append("'").append(toBinaryString(row.coeffs[i], BIT_WIDTH)).append("'")
                    .append(var).append(" ");
        }
        sb.append("= 0   (shifts: ").append(row.shiftCount).append(")");
        System.out.println(sb.toString());
    }

    // Print the solution (in decimal) using variable names (a, b, c, …)
    private static void printSolution(BigInteger[] solution) {
        char var = 'a';
        StringBuilder sb = new StringBuilder();
        for (BigInteger sol : solution) {
            sb.append(var).append(" = ").append(sol).append("   ");
            var++;
        }
        System.out.println(sb.toString());
    }

    //===========================================================================
    // Elimination and Combination Methods
    //===========================================================================

    // Solves the system using the modified (Atanasoff) Gaussian elimination.
    // Returns an array of solved BinaryRows (one per variable).
    private static BinaryRow[] solveSystem(BinaryRow[] originalMatrix) {
        int n = originalMatrix.length;  // number of equations/unknowns

        // Build a list of batches.
        // Batch 0 is the original system.
        List<List<BinaryRow>> batches = new ArrayList<>();
        List<BinaryRow> batch0 = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            batch0.add(originalMatrix[i].clone());
        }
        batches.add(batch0);

        // Forward elimination:
        // For each elimination step (targeting one column per batch) combine
        // consecutive pairs from the current batch using keyStepCombineForward.
        int pivotCol = 0;
        while (batches.get(batches.size() - 1).size() > 1 && pivotCol < n) {
            List<BinaryRow> currentBatch = batches.get(batches.size() - 1);
            List<BinaryRow> newBatch = new ArrayList<>();
            for (int i = 0; i < currentBatch.size() - 1; i++) {
                BinaryRow rowA = currentBatch.get(i);
                if (rowA.coeffs[pivotCol].equals(BigInteger.ZERO)) {
                    BinaryRow alt = findVariable(currentBatch, pivotCol, i);
                    if (alt != null) {
                        rowA = alt;
                    }
                }
                BinaryRow rowB = currentBatch.get(i + 1);
                BinaryRow combined = keyStepCombineForward(rowA, rowB, pivotCol);
                newBatch.add(combined);
                System.out.println("\nAfter combining batch " + (batches.size() - 1) +
                        " rows " + i + " and " + (i + 1) +
                        " targeting column " + pivotCol + ":");
                printRow(combined);
            }
            batches.add(newBatch);
            pivotCol++; // move to the next variable/column
        }

        System.out.println("\n=== Forward Elimination Batches ===");
        for (int b = 0; b < batches.size(); b++) {
            System.out.println("Batch " + b + ":");
            for (BinaryRow row : batches.get(b)) {
                printRow(row);
            }
            System.out.println();
        }

        // --- Backward Elimination ---
        // We assume batches.size() == n.
        int totalBatches = batches.size();
        BinaryRow[] solved = new BinaryRow[totalBatches];
        // The final forward elimination row (the only row in the last batch) is solved for variable n-1.
        solved[totalBatches - 1] = batches.get(totalBatches - 1).get(0);

        int solvedCount = totalBatches - 2;
        // Backward part: For each earlier batch, combine the final solved row with a row from that batch.
        for (int i = 2; i <= totalBatches; i++) {
            int currentPivot = pivotCol - 1; // pivotCol was incremented in forward elimination
            List<BinaryRow> currentBatch = batches.get(totalBatches - i);
            BinaryRow rowB;
            if (currentBatch.get(0).coeffs[currentPivot].equals(BigInteger.ZERO)) {
                rowB = findVariableBackward(currentBatch, currentPivot, 0);
                if (rowB == null) {
                    if (totalBatches - i - 1 >= 0) {
                        rowB = findVariableSpecial(batches.get(totalBatches - i - 1), currentPivot - 1, -1);
                    } else {
                        rowB = currentBatch.get(0);
                    }
                }
            } else {
                rowB = currentBatch.get(0);
            }
            System.out.println("\nBackward elimination step: Combining solved drum from final batch with drum from batch " + (totalBatches - i)
                    + " at pivot column " + currentPivot);
            BinaryRow temp = keyStepCombineBackward(solved[totalBatches - 1], rowB, totalBatches - 1);
            for (int j = 2; j < i; j++) {
                System.out.println("Backward elimination inner step: Combining solved drum from batch " + (totalBatches - j)
                        + " with intermediate result at pivot column " + (totalBatches - j));
                temp = keyStepCombineBackward(solved[totalBatches - j], temp, totalBatches - j);
            }
            solved[solvedCount] = temp;
            solvedCount--;
            pivotCol--;
        }

        System.out.println("\n=== Backward Elimination (Solved Rows) ===");
        for (int i = 0; i < solved.length; i++) {
            printRow(solved[i]);
        }

        return solved;
    }

    // Extracts the final solution from the solved rows.
    // For each row i, the pivot is at coeffs[i] and the constant is at coeffs[n].
    private static BigInteger[] extractSolution(BinaryRow[] solved) {
        int n = solved.length;
        BigInteger[] solution = new BigInteger[n];
        for (int i = 0; i < n; i++) {
            BigInteger pivot = solved[i].coeffs[i];
            BigInteger constant = solved[i].coeffs[n];
            if (pivot.equals(BigInteger.ZERO)) {
                System.out.println("Error: Zero pivot encountered for variable " + i);
                solution[i] = BigInteger.ZERO;
            } else {
                solution[i] = constant.negate().divide(pivot);
            }
        }
        return solution;
    }

    //===========================================================================
    // Error Handling / Variable-Finding Methods
    //===========================================================================

    private static BinaryRow findVariable(List<BinaryRow> currentBatch, int pivotCol, int i) {
        for (int j = 0; j < currentBatch.size(); j++) {
            if ((j != i && j != i + 1) && !currentBatch.get(j).coeffs[pivotCol].equals(BigInteger.ZERO)) {
                return currentBatch.get(j);
            }
        }
        return null;
    }

    private static BinaryRow findVariableBackward(List<BinaryRow> currentBatch, int pivotCol, int i) {
        for (int j = 0; j < currentBatch.size(); j++) {
            if ((j != i) && !currentBatch.get(j).coeffs[pivotCol].equals(BigInteger.ZERO)) {
                return currentBatch.get(j);
            }
        }
        return null;
    }

    private static BinaryRow findVariableSpecial(List<BinaryRow> currentBatch, int pivotCol, int i) {
        for (int j = 0; j < currentBatch.size(); j++) {
            if ((j != i) && currentBatch.get(j).coeffs[pivotCol].equals(BigInteger.ZERO)) {
                return currentBatch.get(j);
            }
        }
        return null;
    }

    //===========================================================================
    // Key Steps: Binary Combination Methods with Detailed Visualization
    //===========================================================================

    // Helper method to print a detailed view of two drums (KA and CA)
    // For each band (column), prints: (decimal)'50-bit binary'variable.
    private static void printDrumPair(String kaLabel, BinaryRow KA, String caLabel, BinaryRow CA, int pivotCol, String message) {
        System.out.println(message);
        for (int i = 0; i < KA.coeffs.length; i++) {
            String var;
            if (i < KA.coeffs.length - 1) {
                var = String.valueOf((char)('a' + i));
            } else {
                var = "C";
            }
            String bandStr = "Band " + i + ": ";
            String kaStr = "(" + KA.coeffs[i].toString() + ")'" + toBinaryString(KA.coeffs[i], BIT_WIDTH) + "'" + kaLabel;
            String caStr = "(" + CA.coeffs[i].toString() + ")'" + toBinaryString(CA.coeffs[i], BIT_WIDTH) + "'" + caLabel;
            System.out.println(bandStr + kaStr);
            System.out.println("       " + caStr);
        }
        System.out.println();
    }

    // Forward elimination key step: combining two rows (drums) with detailed visualization.
    // KA is the source drum; CA is the drum being affected.
    private static BinaryRow keyStepCombineForward(BinaryRow rowA, BinaryRow rowB, int pivotCol) {
        BinaryRow KA = rowA.clone();
        BinaryRow CA = rowB.clone();
        printDrumPair("KA", KA, "CA", CA, pivotCol, "Initial state before combining:");
        while (!CA.coeffs[pivotCol].equals(BigInteger.ZERO)) {
            int signBefore = CA.coeffs[pivotCol].signum();
            String op;
            if ((KA.coeffs[pivotCol].signum() >= 0 && CA.coeffs[pivotCol].signum() >= 0) ||
                    (KA.coeffs[pivotCol].signum() < 0 && CA.coeffs[pivotCol].signum() < 0)) {
                op = "Subtracting KA from CA";
                for (int i = 0; i < KA.coeffs.length; i++) {
                    CA.coeffs[i] = CA.coeffs[i].subtract(KA.coeffs[i]);
                }
            } else {
                op = "Adding KA to CA";
                for (int i = 0; i < KA.coeffs.length; i++) {
                    CA.coeffs[i] = CA.coeffs[i].add(KA.coeffs[i]);
                }
            }
            printDrumPair("KA", KA, "CA", CA, pivotCol, "After " + op + ":");
            int signAfter = CA.coeffs[pivotCol].signum();
            if (signAfter != signBefore) {
                // Shift right KA (divide each coefficient by 2) and increment its shift counter.
                for (int i = 0; i < KA.coeffs.length; i++) {
                    KA.coeffs[i] = shiftRight(KA.coeffs[i]);
                }
                KA.shiftCount++;
                printDrumPair("KA", KA, "CA", CA, pivotCol, "After shifting right KA (shift count: " + KA.shiftCount + "):");
                if (KA.shiftCount >= SHIFT_LIMIT) {
                    System.out.println("Warning: Shift limit (" + SHIFT_LIMIT + ") reached for KA during forward elimination.");
                    break;
                }
            }
        }
        return CA;
    }

    // Backward elimination key step: similar to forward but used in the reverse phase.
    private static BinaryRow keyStepCombineBackward(BinaryRow rowA, BinaryRow rowB, int pivotCol) {
        BinaryRow KA = rowA.clone();
        BinaryRow CA = rowB.clone();
        printDrumPair("KA", KA, "CA", CA, pivotCol, "Initial state before backward combining:");
        while (!CA.coeffs[pivotCol].equals(BigInteger.ZERO)) {
            int signBefore = CA.coeffs[pivotCol].signum();
            String op;
            if ((KA.coeffs[pivotCol].signum() >= 0 && CA.coeffs[pivotCol].signum() >= 0) ||
                    (KA.coeffs[pivotCol].signum() < 0 && CA.coeffs[pivotCol].signum() < 0)) {
                op = "Subtracting KA from CA";
                for (int i = 0; i < KA.coeffs.length; i++) {
                    CA.coeffs[i] = CA.coeffs[i].subtract(KA.coeffs[i]);
                }
            } else {
                op = "Adding KA to CA";
                for (int i = 0; i < KA.coeffs.length; i++) {
                    CA.coeffs[i] = CA.coeffs[i].add(KA.coeffs[i]);
                }
            }
            printDrumPair("KA", KA, "CA", CA, pivotCol, "After " + op + " (backward):");
            int signAfter = CA.coeffs[pivotCol].signum();
            if (signAfter != signBefore) {
                for (int i = 0; i < KA.coeffs.length; i++) {
                    KA.coeffs[i] = shiftRight(KA.coeffs[i]);
                }
                KA.shiftCount++;
                printDrumPair("KA", KA, "CA", CA, pivotCol, "After shifting right KA (backward, shift count: " + KA.shiftCount + "):");
                if (KA.shiftCount >= SHIFT_LIMIT) {
                    System.out.println("Warning: Shift limit (" + SHIFT_LIMIT + ") reached for KA during backward elimination.");
                    break;
                }
            }
        }
        return CA;
    }

    // A helper method to right shift (divide by 2) on a BigInteger.
    private static BigInteger shiftRight(BigInteger value) {
        return value.shiftRight(1);
    }
}
