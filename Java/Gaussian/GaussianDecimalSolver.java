import java.util.*;

public class GaussianDecimalSolver {

    private static final int SCALE = 100; // multiply every coefficient by this factor

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

            int[][] matrix = parseSystem(input);
            if (matrix == null) {
                System.out.println("Invalid input format. Please try again.\n");
                continue;
            }

            // Check dimensions: Expect n equations and n+1 columns (augmented matrix)
            int n = matrix.length;
            if (matrix[0].length != n + 1) {
                System.out.println("Invalid system dimensions. Expecting n equations with n+1 columns.");
                continue;
            }

            // Scale the matrix to help preserve precision when shifting right.
            scaleMatrix(matrix, SCALE);

            System.out.println("\nOriginal System (scaled):");
            printFormattedMatrix(matrix);

            // Solve the system using our modified Gaussian elimination.
            int[] solution = solveSystem(matrix);

            System.out.println("\nSolution:");
            printSolution(solution);
            System.out.println("\n-----------------------------\n");
        }
        scanner.close();
    }

    //===========================================================================
    // Input and Formatting Methods
    //===========================================================================

    // Parse a system from a MATLAB-like string.
    // Example input: [1, 2, 3, 4; 5, 6, 7, 8; 9, 10, 11, 12]
    private static int[][] parseSystem(String input) {
        try {
            input = input.replace("[", "").replace("]", "");
            String[] rowStrings = input.split(";");
            int n = rowStrings.length;
            String[] firstRowElements = rowStrings[0].trim().split(",");
            int m = firstRowElements.length;
            int[][] matrix = new int[n][m];
            for (int i = 0; i < n; i++) {
                String[] elems = rowStrings[i].trim().split(",");
                if (elems.length != m) {
                    return null;
                }
                for (int j = 0; j < m; j++) {
                    matrix[i][j] = Integer.parseInt(elems[j].trim());
                }
            }
            return matrix;
        } catch (Exception e) {
            return null;
        }
    }

    // Multiply every coefficient in the matrix by 'factor'
    private static void scaleMatrix(int[][] matrix, int factor) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] *= factor;
            }
        }
    }

    // Print the matrix in nicely formatted, right‐justified columns.
    // Also appends " = 0" to each printed equation.
    private static void printFormattedMatrix(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        // Determine a width for each column
        int[] widths = new int[cols];
        for (int j = 0; j < cols; j++) {
            int maxWidth = 0;
            for (int i = 0; i < rows; i++) {
                int len = String.valueOf(matrix[i][j]).length();
                if (len > maxWidth) {
                    maxWidth = len;
                }
            }
            widths[j] = maxWidth;
        }
        // Print each row with each coefficient right-justified
        for (int i = 0; i < rows; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < cols; j++) {
                sb.append(String.format("%" + widths[j] + "d ", matrix[i][j]));
            }
            sb.append("= 0");
            System.out.println(sb.toString());
        }
    }

    // Print the solution using variable names (a, b, c, …)
    private static void printSolution(int[] solution) {
        char var = 'a';
        StringBuilder sb = new StringBuilder();
        for (int sol : solution) {
            sb.append(var).append(" = ").append(sol).append("   ");
            var++;
        }
        System.out.println(sb.toString());
    }

    //===========================================================================
    // Elimination and Combination Methods
    //===========================================================================

    // Solve the system using the modified (Atanasoff) Gaussian elimination.
    // Returns an int[] of solution values.
    private static int[] solveSystem(int[][] originalMatrix) {
        int n = originalMatrix.length;  // number of equations/unknowns

        // We'll build a list of batches. Each batch is a List<int[]>
        // Batch 0 is the original system.
        List<List<int[]>> batches = new ArrayList<>();
        List<int[]> batch0 = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            batch0.add(originalMatrix[i].clone());
        }
        batches.add(batch0);

        // Forward elimination:
        // For each elimination step (targeting one column per batch) combine
        // consecutive pairs from the current batch using keyStepCombine.
        int pivotCol = 0;
        while (batches.get(batches.size() - 1).size() > 1 && pivotCol < n) {
            List<int[]> currentBatch = batches.get(batches.size() - 1);
            List<int[]> newBatch = new ArrayList<>();
            int[] rowA, rowB;
            for (int i = 0; i < currentBatch.size() - 1; i++) {
                if (currentBatch.get(i)[pivotCol] == 0) {
                    rowA = findVariable(currentBatch, pivotCol, i);
                } else {
                    rowA = currentBatch.get(i);
                }
                if (rowA == null) {
                    rowB = currentBatch.get(i);
                    rowA = rowB;
                } else {
                    rowB = currentBatch.get (i + 1);
                }

                int[] combined = keyStepCombineForward(rowA, rowB, pivotCol);
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
            for (int[] row : batches.get(b)) {
                printRow(row);
            }
            System.out.println();
        }

        // --- Backward Elimination Revised ---
// We assume that the forward elimination has stored each batch in the list 'batches'
// where batches.get(0) is the original system, batches.get(1) contains the results
// from combining rows in batch 0, and so on. For an n-equation system, batches.size() == n.

// Create an array to hold the "solved" rows for each variable.
        int totalBatches = batches.size();  // For an n-equation system, totalBatches == n.
        int[][] solved = new int[totalBatches][];

// The final forward elimination row (the only row in the last batch) is solved for variable n-1.
        solved[totalBatches - 1] = batches.get(totalBatches - 1).get(0);

        int solvedCount = totalBatches - 2;
        //Backward Part
        int[] rowB;
        for (int i = 2; i <= totalBatches; ++i) {
            if (batches.get(totalBatches - i).get(0)[pivotCol - 1] == 0) {
                rowB = findVariableBackward(batches.get(totalBatches - i), pivotCol - 1, 0);
                if (rowB == null) {
                    rowB = findVariableSpecial(batches.get(totalBatches - i - 1), pivotCol - 2, -1);
                }
            } else {
                rowB = batches.get(totalBatches - i).get(0);
            }
            int[] temp = keyStepCombineBackward(solved[totalBatches - 1], rowB, totalBatches - 1);

            for (int j = 2; j < i; ++j) {

                temp = keyStepCombineBackward(solved[totalBatches - j], temp, totalBatches - j);
            }

            solved[solvedCount] = temp;
            --solvedCount;
            --pivotCol;
        }

// --- Extract the final solution ---
// We assume that the augmented matrix has n+1 columns (columns 0..n-1 for coefficients and column n for constants).
        n = solved.length;  // Number of variables/equations.
        int[] solution = new int[n];
        for (int i = 0; i < n; i++) {
            int pivot = solved[i][i];    // The pivot (solved) coefficient for variable i.
            int constant = solved[i][n];   // The constant from the augmented column.
            if (pivot == 0) {
                System.out.println("Error: Zero pivot encountered for variable " + i);
                solution[i] = 0;
            } else {
                // As before, we assume the solution is given by -constant/pivot.
                solution[i] = -constant / pivot;
            }
        }

// (For debugging, you might want to print the solved rows:)
        System.out.println("\n=== Backward Elimination (Solved Rows) ===");
        for (int i = 0; i < solved.length; i++) {
            printRow(solved[i]);
        }

        return solution;
    }

    // Print a single row with fixed width formatting.
    private static void printRow(int[] row) {
        for (int num : row) {
            System.out.printf("%8d", num);
        }
        System.out.println("   = 0");
    }

    private static int[] findVariable(List<int[]> currentBatch, int pivotCol, int i) {

        for (int j = 0; j < currentBatch.size(); ++ j) {
            if ((j != i && j != i+1) && currentBatch.get(j)[pivotCol] != 0) {
                return currentBatch.get(j);
            }
        }

        return null;
    }
    private static int[] findVariableBackward(List<int[]> currentBatch, int pivotCol, int i) {

        for (int j = 0; j < currentBatch.size(); ++ j) {
            if ((j != i) && currentBatch.get(j)[pivotCol] != 0) {
                return currentBatch.get(j);
            }
        }

        return null;
    }

    private static int[] findVariableSpecial(List<int[]> currentBatch, int pivotCol, int i) {
        for (int j = 0; j < currentBatch.size(); ++ j) {
            if ((j != i) && currentBatch.get(j)[pivotCol] == 0) {
                return currentBatch.get(j);
            }
        }

        return null;
    }

    // Forward elimination “key step” combination.
    // Given two rows (A and B) and a pivot column, repeatedly update B until
    // the coefficient at pivotCol becomes 0.
    private static int[] keyStepCombineForward(int[] rowA, int[] rowB, int pivotCol) {
        int len = rowA.length;
        int[] A = rowA.clone();
        int[] B = rowB.clone();

        while (B[pivotCol] != 0) {
            int signBefore = Integer.compare(B[pivotCol], 0);
            // Step 4: Check the signs in the pivot column.
            if ((A[pivotCol] >= 0 && B[pivotCol] >= 0) || (A[pivotCol] < 0 && B[pivotCol] < 0)) {
                // Same signs: subtract A from B.
                for (int i = 0; i < len; i++) {
                    B[i] = B[i] - A[i];
                }
            } else {
                // Different signs: add A to B.
                for (int i = 0; i < len; i++) {
                    B[i] = B[i] + A[i];
                }
            }
            int signAfter = Integer.compare(B[pivotCol], 0);
            if (signAfter != signBefore) {
                // Step 6: If the sign changed in B, divide all entries of A by 2.
                for (int i = 0; i < len; i++) {
                    A[i] = A[i] / 2;
                }
            }
            // Loop continues until B[pivotCol] becomes 0.
        }
//        for (int i = 0; i < B.length; ++i) {
//            if (B[i] % 10 != 0) {
//                roundToNearestHundred(B[i]);
//            }
//        }
        return B;
    }

    private static int[] keyStepCombineBackward(int[] rowA, int[] rowB, int pivotCol) {
        int len = rowA.length;
        int[] A = rowA.clone();
        int[] B = rowB.clone();

        while (B[pivotCol] != 0) {
            int signBefore = Integer.compare(B[pivotCol], 0);
            // Step 4: Check the signs in the pivot column.
            if ((A[pivotCol] >= 0 && B[pivotCol] >= 0) || (A[pivotCol] < 0 && B[pivotCol] < 0)) {
                // Same signs: subtract A from B.
                for (int i = 0; i < len; i++) {
                    B[i] = B[i] - A[i];
                }
            } else {
                // Different signs: add A to B.
                for (int i = 0; i < len; i++) {
                    B[i] = B[i] + A[i];
                }
            }
            int signAfter = Integer.compare(B[pivotCol], 0);
            if (signAfter != signBefore) {
                // Step 6: If the sign changed in B, divide all entries of A by 2.
                for (int i = 0; i < len; i++) {
                    A[i] = A[i] / 2;
                }
            }
            // Loop continues until B[pivotCol] becomes 0.
        }
        return B;
    }

    // A single-step combination method (used in backward elimination).
    // It applies one addition/subtraction rule based on the signs at the pivot column.
    private static int[] combineRows(int[] rowA, int[] rowB, int pivotCol) {
        int len = rowA.length;
        int[] result = rowB.clone();
        if ((rowA[pivotCol] >= 0 && rowB[pivotCol] >= 0) || (rowA[pivotCol] < 0 && rowB[pivotCol] < 0)) {
            for (int i = 0; i < len; i++) {
                result[i] = rowB[i] - rowA[i];
            }
        } else {
            for (int i = 0; i < len; i++) {
                result[i] = rowB[i] + rowA[i];
            }
        }
        return result;
    }
}