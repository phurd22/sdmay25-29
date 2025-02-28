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

        //User has to 'Startup" Machine
        private boolean machineOn;

        //Linked with PB1. True: Addition, False: Subtraction
        private boolean additionMode;

        //ASM connection. True: Card Read, False: KA
        private boolean switch7;

        //AS relay control. True:Card, False: Overdrafts
        private boolean switch8;

        //Coefficient Banks
        private  boolean switch1, switch2, switch3, switch4, switch5, switch6;

        //Drums
        private BinaryRow CA, KA;
        private boolean CAClear, KAClear;
        private int selectedCoefficient;
        private int coefficientSign;

        public MachineController() {
            machineOn = false;
            additionMode = true;  // default mode is addition
            selectedCoefficient = 0;
            coefficientSign = 1;  // default to positive
            CA = new BinaryRow(new BigInteger[30]);
            KA = new BinaryRow(new BigInteger[30]);
            switch1 = false;
            switch2 = false;
            switch3 = false;
            switch4 = false;
            switch5 = false;
            switch6 = false;
            CAClear = false;
            KAClear = false;
        }

        public boolean isAdditionMode() {
            return additionMode;
        }

        //Used for machine startup
        public void setAdditionMode(boolean mode) {
            additionMode = mode;
        }

        //Machine On
        public void setMachineStatus(boolean mode) { machineOn = mode; }

        // SW7 : 68
        public void toggleSwitch7() {
            switch7 = !switch7;
            System.out.println("Inputs to ASM's are connected to: " + (switch7 ? "Card Reader" : "KA"));
        }

        // SW8 : 90
        public void toggleSwitch8() {
            switch8 = !switch8;
            System.out.println("Add-Subtract relay is controlled by: " + (switch8 ? "Card" : "Overdrafts"));
        }

        // PB1 : 84
        public void toggleAddSubtraction() {
            additionMode = !additionMode;
            System.out.println("Operation is now: " + (additionMode ? "Addition" : "Subtraction"));
        }

        // SW1-SW6 : 70
        public void toggleBank(int selection) {
            switch (selection) {
                case 1:
                    switch1 = !switch1;
                    System.out.println("Coefficient Inputs 1-5: " + (switch1 ? "Selected" : "Deselected"));
                    break;
                case 2:
                    switch2 = !switch2;
                    System.out.println("Coefficient Inputs 6-10: " + (switch2 ? "Selected" : "Deselected"));
                    break;
                case 3:
                    switch3 = !switch3;
                    System.out.println("Coefficient Inputs 11-15: " + (switch3 ? "Selected" : "Deselected"));
                    break;
                case 4:
                    switch4 = !switch4;
                    System.out.println("Coefficient Inputs 16-20: " + (switch4 ? "Selected" : "Deselected"));
                    break;
                case 5:
                    switch5 = !switch5;
                    System.out.println("Coefficient Inputs 21-25: " + (switch5 ? "Selected" : "Deselected"));
                    break;
                case 6:
                    switch6 = !switch6;
                    System.out.println("Coefficient Inputs 26-30: " + (switch6 ? "Selected" : "Deselected"));
                    break;
            }
        }

        // SW11 : 62
        public void clearCA() {
            CAClear = !CAClear;
            if (CAClear) {
                CA = new BinaryRow(new BigInteger[30]);
                System.out.println("CA Drum Cleared");
            }
        }
        // SW12 : 63
        public void clearKA() {
            KAClear = !KAClear;
            if (KAClear) {
                KA = new BinaryRow(new BigInteger[30]);
                System.out.println("KA Drum Cleared");
            }
        }

        // PB3 : 69
        public void transferCAtoKA() {
            // Deep copy the coefficients from CA to KA.
            KA = CA.clone();
        }

        public void setCoefficientSign(int signVal) {
            coefficientSign = signVal;
            String signStr = (coefficientSign == 1) ? "Positive" : "Negative";
            System.out.println("Step 29: Coefficient sign set to " + signStr + ".");
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
            machineController.setMachineStatus(true);
            machineController.setAdditionMode(true);
            L2.setSelected(true);
            L3.setSelected(false);
            System.out.println("Machine started.");
        });

        //3. Set switch SW7 so that inputs to ASM’s are connected to card reader.
        SW7.addActionListener(e -> {
            machineController.toggleSwitch7();
        });

        //4. Set switch SW8 so that add-subtract relay is controlled by card
        // (rather than by sensing overdrafts).
        SW8.addActionListener(e -> {
            machineController.toggleSwitch8();
        });

        //5. Select ADD with pushbutton PB1, by monitoring the lights L2 and L3.
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

        //6. Select field on CA into which card is read by closing one of the six switches SW1-SW6.
        SW1.addActionListener(e -> {
            machineController.toggleBank(1);
        });
        SW2.addActionListener(e -> {
            machineController.toggleBank(2);
        });
        SW3.addActionListener(e -> {
            machineController.toggleBank(3);
        });
        SW4.addActionListener(e -> {
            machineController.toggleBank(4);
        });
        SW5.addActionListener(e -> {
            machineController.toggleBank(5);
        });
        SW6.addActionListener(e -> {
            machineController.toggleBank(6);
        });

        //7. Clear CA and KA with switches SW11 and SW12 respectively.
        SW11.addActionListener(e -> {
            machineController.clearCA();
        });
        SW12.addActionListener(e -> {
            machineController.clearKA();
        });

        //9. PB2: Start base-10 read operation
        //Pushbutton 85

        //11. Transfer contents of CA to KA by pressing GO button PB3.
        PB3.addActionListener(e -> {

        });

        //PB3: Transfer CA-Drum to KA-Drum
        //Pushbutton 69


        //II: COMPUTING

        //ZD slider selected the coefficient to be eliminated.


        //PB4: Start Base-2 Punch
        //Pushbutton 88


        //PB6: Start Computation
        //Pushbutton 86


        //III: BASE-2 READING

        //PB5: Start Base-2 Read
        //Pushbutton 87


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

        //SW8: IBM Card Sign Control
        //Switch 90

        //SW9: Unused
        //No reference on Manual 1968

        //SW10: IBM Card 1's Output Limit
        //Switch 71, only powers of 10

        //SW11: Clear CA
        //Switch 62

        //SW12: Clear KA
        //Switch 63
    }

    // The main method to run your GUI.
    public static void main(String[] args) {
        JFrame frame = new JFrame("ABC Control Panel");
        frame.setContentPane(new ABCControlPanel().contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

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