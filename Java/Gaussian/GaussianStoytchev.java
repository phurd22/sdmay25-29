import java.util.Scanner;

// 1 [1, 2, 3, -4; 2, 5, 3, -5; 1, 0, 8, -9]
// 1 [2, 5, 3, -5; 1, 2, 3, -4; 1, 0, 8, -9]

// 17 1 [1, 2, 1, -3; 0, 1, -1, 4; -1, -2, 3, -9]       ∴ x=2, y=-1, z=3    True True
// 19 1 [1, -3, -2, -5; 3, -2, 1, -8; 0, 1, -3, 1]      ∴ x=2, y=-1, z=0    True True
// 21 1 [1, 1, 1, -4; 1, 3, 3, -10; 2, 1, -1, -3]       ∴ x=1, y=2, z=1     True True
// 23 1 [1, 0, -4, -1; 2, -1, -6, -4; 2, 3, -2, -8]     ∴ x=5, y=0, z=1     True True
// 25 1 [2, 4, -1, -2; 1, 2, -3, 4; 3, -1, 1, -1]       ∴ x=0, y=1, z=2     True True
// 27 1 [0, 2, 4, 1; -2, 1, 2, 1; 4, -2, 0, 0]          x=1/4, y=1/2, z=-1/2
//Scl 1 [0, 2, 4, 4; -2, 1, 2, 4; 4, -2, 0, 0]          x=1, y=2, z=-2
//Ori 1 [1, 3, -2, -1; 2, 5, -2, -6; 4, 13, -9, -3]     ∴ x=1, y=2, z=3     True True
// [1, 2, -1, 1, -6; -1, 1, 2, -1, -3; 2, -1, 2, 2, -14; 1, 1, -1, 2, -8]   a=1, b=2, c=3, d=4
// [-1, 1, 2, -1, -3; 2, -1, 2, 2, -14; 1, 1, -1, 2, -8; 1, 2, -1, 1, -6]
// [-1, 1, 2, -1, -3; 1, 2, -1, 1, -6; 2, -1, 2, 2, -14; 1, 1, -1, 2, -8]
// [2, -1, 2, 2, -14; 1, 2, -1, 1, -6; 1, 1, -1, 2, -8; -1, 1, 2, -1, -3]
//
// [2, 1, 1, 1, 1, -4; 1, 2, 1, 1, 1, -5; 1, 1, 2, 1, 1, -6; 1, 1, 1, 2, 1, -7; 1, 1, 1, 1, 2, -8] a = -1, b = 0, c = 1, d = 2, e = 3
// [1, 1, 1, 1, 1, 1, 1, 28; 1, -1, 1, -1, 1, -1, 1, 4; 2, 3, -1, 4, -2, 1, -3, -4; 0, 1, 2, 3, 4, 5, 6, 112; -1, 0, 1, 0, -1, 0, 1, 4; 1, -1, 1, -1, 1, -1, 1, 4; 1, -2, 3, -4, 5, -6, 7, 28] a = 1 ... g = 7

public class GaussianStoytchev {
    private static int mainCol;
    private static int mainRow;
    private static int mainSize;
    private static final int SCALE = 100;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int rows, cols, defaultrows, defaultcols, choice;
        int i, j;
        int[] answers;

        //Give matrix size option
        System.out.println("Choose the matrix size:");
        System.out.println("0. 2x3 Matrix");
        System.out.println("1. 3x4 Matrix");
        System.out.println("2. 4x5 Matrix");
        choice = scanner.nextInt();


        if (choice == 1) {
            rows = 3;
            cols = 4;
            defaultrows = 9;
            defaultcols = 4;
            mainCol = 4;
            mainRow = 9;
            mainSize = 3;
            answers = new int[3];
        } else if (choice == 2) {
            rows = 4;
            cols = 5;
            defaultrows = 16;
            defaultcols = 5;
            mainCol = 5;
            mainRow = 16;
            mainSize = 4;
            answers = new int[4];
        } else if (choice == 0) {
            rows = 2;
            cols = 3;
            defaultrows = 4;
            defaultcols = 3;
            mainCol = 3;
            mainRow = 4;
            mainSize = 2;
            answers = new int[2];
        } else {
            System.out.println("Invalid choice. Exiting program.");
            scanner.close();
            return;
        }

        int[][] matrix = new int[defaultrows][defaultcols];

        // Read the matrix in MATLAB-like format
        System.out.println("Enter the coefficients in MATLAB format:");
        System.out.println("Example: [1, 2, 3, 4; 5, 6, 7, 8; 9, 10, 11, 12]");
        String input = scanner.nextLine();

        // Remove the brackets and split the input
        input = input.replace("[", "").replace("]", "");
        String[] rowsData = input.split(";");

        // Parse the rows and fill the matrix
        try {
            for (i = 0; i < rows; i++) {
                String[] elements = rowsData[i].trim().split(",");
                for (j = 0; j < cols; j++) {
                    matrix[i][j] = Integer.parseInt(elements[j].trim());
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid input format. Please try again.");
            scanner.close();
            return;
        }
        scanner.close();

        // Display the entered matrix
        System.out.println("You entered the following matrix:");
        printMatrix(matrix, rows, cols);
        System.out.println("-----------------");

        //Different solving based off number of equations
        if (choice == 1) {
            answers = threeEquations(matrix);
        } else if (choice == 2){
            answers = fourEquations(matrix);
        } else if (choice == 0) {
            answers = null;
        }

        //Display answers
        printMatrix(matrix, defaultrows, defaultcols);
        String[] variables = new String[]{"x","y","z","w"};
        System.out.print("∴ ");
        for (i = 0; i < answers.length; ++i) {
            System.out.print(variables[i] + "=" + answers[i]);
            if (i != answers.length - 1) {
                System.out.print(", ");
            }
        }
    }

    // Function to print the matrix
    // Method to print a matrix given its dimensions
    public static void printMatrix(int[][] matrix, int rows, int cols) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void scale(int[][] matrix) {
        for (int i = 0; i < mainRow; ++i) {
            for (int j = 0; j < mainCol; ++j) {
                matrix[i][j] = matrix[i][j] * SCALE;
            }
        }
    }

    public static int[][] deepCopy(int[][] original) {
        // Initialize a new 3x4 matrix for the copy
        int[][] copy = new int[mainRow][mainCol];

        // Copy each element from the original to the new matrix
        for (int i = 0; i < mainRow; i++) {
            for (int j = 0; j < mainCol; j++) {
                copy[i][j] = original[i][j];
            }
        }
        return copy;
    }

    public static int[] threeEquations(int[][] matrix){
        int i;

        scale(matrix);

        //The First Electronic Computer - The Atanasoff Story pg. 11-12
        //Atanasoff's modified Gaussian method with "operator interaction"
        for (i = 1; i < 7; ++i) {
            switch (i) {
                case 1 -> keySteps(matrix, 0, 1, 3, 0);
                case 2 -> keySteps(matrix, 1, 2, 4, 0);
                case 3 -> keySteps(matrix, 3, 4, 5, 1);
                case 4 -> keySteps(matrix, 5, 3, 6, 2);
                case 5 -> keySteps(matrix, 5, 0, 7, 2);
                case 6 -> keySteps(matrix, 6, 7, 8, 1);
            }
        }

        //Gather answer from specific rows
        int[] answer = new int[3];
        if (matrix[8][3] % matrix[8][0] != 0) {
            answer[0] = -((matrix[8][3]))/(matrix[8][0]);
            answer[1] = -((matrix[6][3]))/(matrix[6][1]);
            answer[2] = -((matrix[5][3]))/(matrix[5][2]);
        } else {
            answer[0] = -(matrix[8][3])/(matrix[8][0]);
            answer[1] = -(matrix[6][3])/(matrix[6][1]);
            answer[2] = -(matrix[5][3])/(matrix[5][2]);
        }


        return answer;
    }
    public static int[] fourEquations(int[][] matrix){
        int i;

        //My interpretation of Atanasoff's modified Gaussian
        //For a four equation linear system
        for (i = 1; i < 13; ++i) {
            switch (i) {
                case  1 -> keySteps(matrix,  0,  1,  4, 0);
                case  2 -> keySteps(matrix,  1,  2,  5, 0);
                case  3 -> keySteps(matrix,  2,  3,  6, 0);
                case  4 -> keySteps(matrix,  4,  5,  7, 1);
                case  5 -> keySteps(matrix,  5,  6,  8, 1);
                case  6 -> keySteps(matrix,  7,  8,  9, 2);
                case  7 -> keySteps(matrix,  9,  7, 10, 3);
                case  8 -> keySteps(matrix,  9,  4, 11, 3);
                case  9 -> keySteps(matrix, 10, 11, 12, 2);
                case 10 -> keySteps(matrix, 12,  0, 13, 1);
                case 11 -> keySteps(matrix, 10, 13, 14, 2);
                case 12 -> keySteps(matrix,  9, 14, 15, 3);
            }
        }

        //Gather answer from specific rows
        int[] answer = new int[4];
        answer[0] = -(matrix[15][4])/(matrix[15][0]);
        answer[1] = -(matrix[12][4])/(matrix[12][1]);
        answer[2] = -(matrix[10][4])/(matrix[10][2]);
        answer[3] = -(matrix[ 9][4])/(matrix[ 9][3]);

        return answer;
    }

    //NEED TO FIND A VARIBALE FOR 0 X

    public static int findInt(int[][] matrix, int col, int start, int eq2) {
        int i;

        for (i = start; i < mainRow; ++i) {
            if ((i != eq2) && (matrix[i][col] != 0)) {
                return i;
            }
        }

        return -1;
    }

    public static void keySteps(int [][] originalmatrix, int eq1, int eq2, int neweq, int col) {
        boolean step7 = false;
        int signchange;

        //Create Copy to Properly edit rows
        int[][] matrix = deepCopy(originalmatrix);

        while (!step7) {

            if (matrix[eq2][col] == 0){                                     //Step 3.
                step7 = true;
            } else {                                                            //Step 4.

                /////////////////////////Error Handling////////////////////////////////////
                if (matrix[eq1][col] == 0 && (eq1 == 0 || eq1 == 1)) {        //Need an equation with a variable to add/sub
                    eq1 = findInt(matrix, col, 0, eq2);                          //0 variable error solve
                }
                if ((eq1 == 3)) {
                    if (matrix[eq1][col] == 0) {
                        eq2 = 3;
                        break;
                    } else if (matrix[eq2][col] == 0) {
                        break;
                    }
                }
                if ((eq1 == 5) && (eq2 == 3) && (matrix[eq2][1] == 0)) {
                    if (matrix[4][1] == 0) {
                        for (int i = 0; i < 2; ++ i) {
                            if (matrix[i][0] == 0) {
                                eq2 = i;
                            }
                        }
                    } else {
                        eq2 = 4;
                    }
                }
                if ((eq1 == 5) && (eq2 == 0) && (matrix[eq2][0] == 0)) {
                    eq2 = findInt(matrix, 0, 0, 0);
                }

                ///////////////////////End Error Handling///////////////////////////////////

                signchange = matrix[eq2][col];
                if ((matrix[eq1][col] < 0 && matrix[eq2][col] < 0) ||           //Same
                        matrix[eq1][col] > 0 && matrix[eq2][col] > 0){          //(+,+) or (-,-)
                    for (int i = 0; i < mainCol; ++i) {                     //Step 5.
                        matrix[eq2][i] = matrix[eq2][i] - matrix[eq1][i];
                    }
                } else {                                                        //Different
                    for (int i = 0; i < mainCol; ++i) {                         //(+,-) or (-,+)
                        matrix[eq2][i] = matrix[eq2][i] + matrix[eq1][i];   //Step 5.
                    }
                }

                if ((signchange < 0 && matrix[eq2][col] > 0) ||             //Step 6.
                        (signchange > 0 && matrix[eq2][col] < 0)) {             //Signs Different
                    for (int i = 0; i < mainCol; ++i) {
                        matrix[eq1][i] = matrix[eq1][i] / 2;                    //Shift Right
                    }
                }
            }

        }

        for (int i = 0; i < mainCol; ++i) {                         //Step 7.
            originalmatrix[neweq][i] = matrix[eq2][i];
        }
    }
}