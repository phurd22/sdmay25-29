import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.*;
import java.math.BigInteger;
import java.util.Scanner;


public class ABCControlPanel {
    private JPanel contentPane;
    private JButton PB1;            //Add/Subtract Selection Push-Button
    private JButton PB2;            //Start Base-10 Read Operation
    private JButton PB3;            //Transfer CA-Drum to KA-Drum
    private JButton PB4;            //Start Base-2 Punch
    private JButton PB5;            //Start Base-2 Read
    private JButton PB6;            //Start Computation
    private JRadioButton SW1;       //Coefficients Input Selection: 1-5
    private JRadioButton SW2;       //Coefficients Input Selection: 6-10
    private JRadioButton SW3;       //Coefficients Input Selection: 11-15
    private JRadioButton SW4;       //Coefficients Input Selection: 16-20
    private JRadioButton SW5;       //Coefficients Input Selection: 21-25
    private JRadioButton SW6;       //Coefficients Input Selection: 26-30
    private JRadioButton SW7;       //Card Read Switch
    private JRadioButton SW8;       //IBM Card Sign Control
    private JRadioButton SW9;       //Unused
    private JRadioButton SW10;      //IBM Card 1's Output Limit
    private JRadioButton SW11;      //Clear-CA Switch
    private JRadioButton SW12;      //Clear-KA Switch
    private JCheckBox L1;           //Base-2 Read Operation
    private JCheckBox L2;           //Add Operation
    private JCheckBox L3;           //Subtract Operation
    private JCheckBox L4;           //Read IBM Card Operation
    private JCheckBox L5;           //Coefficient Elimination Operation
    private JCheckBox L6;           //Decimal Output Operation
    private JCheckBox L7;           //Positive Number Indicator
    private JCheckBox L8;           //Negative Number Indicator
    private JRadioButton MS;        //Motor Switch
    private JRadioButton VS;        //Voltage Switch
    private JSlider ZD;             //Zero Detection Coefficient Selection
    private JSlider SD;             //Sign Detection Coefficient Selection

    private final MachineController machineController;
    private BigInteger[] pendingCoefficients;

    //Machine Controller, contains functions for the buttons
    public static class MachineController {
        private boolean machineOn;
        private boolean additionMode;
        private int selectedCoefficient;
        private int coefficientSign;

        public MachineController() {
            machineOn = false;
            additionMode = true;  // default mode is addition
            selectedCoefficient = 0;
            coefficientSign = 1;  // default to positive
        }

        public boolean isAdditionMode() {
            return additionMode;
        }
        //Used for machine startup
        public void setAdditionMode(boolean mode) {
            additionMode = mode;
        }

        public void setCoefficientSign(int signVal) {
            coefficientSign = signVal;
            String signStr = (coefficientSign == 1) ? "Positive" : "Negative";
            System.out.println("Step 29: Coefficient sign set to " + signStr + ".");
        }

        // PB1: Toggle the add/subtract mode.
        public void toggleAddSubtraction() {
            additionMode = !additionMode;
            System.out.println("Operation is now: " + (additionMode ? "Addition" : "Subtraction"));
        }

        public void readBase10Card() {
            if (!machineOn) {
                System.out.println("Error: Machine is off. Please press MS first.");
                return;
            }
            System.out.println("Step 9: Initiating base-10 card read operation. Reading IBM card...");
            // Simulate reading a card
        }

        public void transferCAtoKA() {
            if (!machineOn) {
                System.out.println("Error: Machine is off. Cannot transfer CA to KA.");
                return;
            }
            System.out.println("Step 11: Transferring contents of CA to KA.");
        }

        public void setCoefficientToEliminate(int coef) {
            selectedCoefficient = coef;
            System.out.println("Step 14: Coefficient " + selectedCoefficient + " selected for elimination.");
        }

        public void compute() {
            if (!machineOn) {
                System.out.println("Error: Machine is off. Cannot compute.");
                return;
            }
            System.out.println("Step 17/34: Computation initiated...");
            // Simulate computation (Gaussian elimination, etc.)
            try {
                Thread.sleep(1000); // simulate processing delay
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            System.out.println("Computation complete. Equation reduced to zero.");
        }

        public void punchBase2Card() {
            if (!machineOn) {
                System.out.println("Error: Machine is off. Cannot punch card.");
                return;
            }
            System.out.println("Step 19: Punching contents of CA onto a base-2 card.");
        }

        public void readBase2Card() {
            if (!machineOn) {
                System.out.println("Error: Machine is off. Cannot read base-2 card.");
                return;
            }
            System.out.println("Step 24/27: Reading base-2 card into CA.");
        }

        public void setInputSourceToCardReader() {
            System.out.println("Step 3/32: SW7 set to card read position. Inputs now from card reader.");
        }

        public void setRelayControlledByOverdraft() {
            System.out.println("Step 4/15/33: SW8 set so that add-subtract relay is controlled by overdrafts.");
        }

        public void clearCA() {
            System.out.println("Step 7/23: Clearing CA drum.");
        }

        public void clearKA() {
            System.out.println("Step 7/23: Clearing KA drum.");
        }
    }

    //Contains listeners for the buttons
    public ABCControlPanel() {
        // Create the controller which handles the operations.
        machineController = new MachineController();

        //I: READING BASE-10 CARDS

        //1. Turn power switch MS on.
        //Switch 1
        MS.addActionListener(e -> {
            machineController.setAdditionMode(true);
            L2.setSelected(true);
            L3.setSelected(false);
            System.out.println("Machine started.");
        });

        //5. Select ADD with PB1
        //Pushbutton 84
        PB1.addActionListener(e -> {
            machineController.toggleAddSubtraction();
            //Light change for toggle
            if (machineController.isAdditionMode()) {
                L2.setSelected(true);
                L3.setSelected(false);
            } else {
                L2.setSelected(false);
                L3.setSelected(true);
            }
        });

        //9. PB2: Start base-10 read operation
        //Pushbutton 85
        PB2.addActionListener(e -> {
            machineController.readBase10Card();
        });

        //PB3: Transfer CA-Drum to KA-Drum
        //Pushbutton 69
        PB3.addActionListener(e -> {
            machineController.transferCAtoKA();
        });

        //II: COMPUTING

        //ZD slider selected the coefficient to be eliminated.
        ZD.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int coef = ZD.getValue();
                machineController.setCoefficientToEliminate(coef);
                System.out.println("ZD Slider: Coefficient " + coef + " selected for elimination.");
            }
        });

        //PB4: Start Base-2 Punch
        //Pushbutton 88
        PB4.addActionListener(e -> {
            machineController.punchBase2Card();
        });

        //PB6: Start Computation
        //Pushbutton 86
        PB6.addActionListener(e -> {
            machineController.compute();
        });

        //III: BASE-2 READING

        //PB5: Start Base-2 Read
        //Pushbutton 87
        PB5.addActionListener(e -> {
            machineController.readBase2Card();
        });

        //IV: BASE-10 READING

        // SD slider for checking sign.
        SD.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int signValue = SD.getValue();  // Assume -1 for negative, +1 for positive.
                machineController.setCoefficientSign(signValue);
                System.out.println("SD Slider: Coefficient sign set to " + (signValue == 1 ? "Positive" : "Negative"));
            }
        });

        //SW1: Coefficients Input Selection: 1-5
        //Switch 70

        //SW2: Coefficients Input Selection: 6-10
        //Switch 70

        //SW3: Coefficients Input Selection 11-15
        //Switch 70

        //SW4: Coefficients Input Selection 16-20
        //Switch 70

        //SW5: Coefficients Input Selection 21-25
        //Switch 70

        //SW6: Coefficients Input Selection 26-30
        //Switch 70

        //SW7: Card Read Switch
        //Switch 68
        SW7.addActionListener(e -> {
            // SW7: Set input source (e.g., card reader vs. KA)
            machineController.setInputSourceToCardReader();
        });


        //SW8: IBM Card Sign Control
        //Switch 90
        SW8.addActionListener(e -> {
            // SW8: Configure add-subtract relay to be controlled by overdrafts.
            machineController.setRelayControlledByOverdraft();
        });

        //SW9: Unused
        //No reference on Manual 1968

        //SW10: IBM Card 1's Output Limit
        //Switch 71, only powers of 10

        //SW11: Clear CA
        //Switch 62
        SW11.addActionListener(e -> {
            machineController.clearCA();
        });

        //SW12: Clear KA
        //Switch 63
        SW12.addActionListener(e -> {
            machineController.clearKA();
        });
    }

    // The main method to run your GUI.
    public static void main(String[] args) {
        JFrame frame = new JFrame("ABC Control Panel");
        frame.setContentPane(new ABCControlPanel().contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    //Helper Methods
    private void promptForCoefficients() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter up to 5 coefficients (space-separated, last one is the constant): ");
        String line = scanner.nextLine();
        String[] tokens = line.trim().split("\\s+");
        if(tokens.length > 5) {
            System.out.println("Error: Please enter no more than 5 coefficients.");
            return;
        }
        BigInteger[] cardCoefficients = new BigInteger[5];
        for (int i = 0; i < tokens.length; i++) {
            // Assuming coefficients fit in 50 bits; you might want to check this.
            cardCoefficients[i] = new BigInteger(tokens[i]);
        }
        // Store the coefficients in a temporary location until PB2 is pressed
        // For example, you could save cardCoefficients into a field.
        this.pendingCoefficients = cardCoefficients;
        System.out.println("Coefficients entered. Press PB2 to load into the CA drum.");
    }

    private String to50BitBinary(BigInteger number) {
        // Convert to a two's complement binary string
        String bin = number.toString(2);
        // For positive numbers, pad with leading zeros.
        // For negative numbers, get the last 50 bits of the two's complement representation.
        if (number.signum() >= 0) {
            return String.format("%50s", bin).replace(' ', '0');
        } else {
            // Ensure the binary string is at least 50 characters; if not, pad appropriately.
            if (bin.length() < 50) {
                bin = String.format("%50s", bin).replace(' ', '0');
            }
            return bin.substring(bin.length() - 50);
        }
    }
}