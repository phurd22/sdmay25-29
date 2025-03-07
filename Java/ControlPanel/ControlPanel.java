import javax.swing.*;
import java.math.BigInteger;

public class ControlPanel {
    private JPanel contentPane;
    private JCheckBox SD0;
    private JCheckBox ZD0;
    private JCheckBox ZD2;
    private JCheckBox ZD24;
    private JCheckBox ZD3;
    private JCheckBox ZD23;
    private JCheckBox ZD25;
    private JCheckBox ZD26;
    private JCheckBox ZD29;
    private JCheckBox ZD1;
    private JCheckBox ZD4;
    private JCheckBox ZD28;
    private JCheckBox ZD27;
    private JCheckBox ZD5;
    private JCheckBox ZD6;
    private JCheckBox ZD7;
    private JCheckBox ZD8;
    private JCheckBox ZD22;
    private JCheckBox ZD21;
    private JCheckBox ZD20;
    private JCheckBox ZD19;
    private JCheckBox ZD18;
    private JCheckBox ZD17;
    private JCheckBox ZD16;
    private JCheckBox ZD15;
    private JCheckBox ZD14;
    private JCheckBox ZD13;
    private JCheckBox ZD12;
    private JCheckBox ZD11;
    private JCheckBox ZD10;
    private JCheckBox ZD9;
    private JCheckBox SD1;
    private JCheckBox SD2;
    private JCheckBox SD3;
    private JCheckBox SD4;
    private JCheckBox SD5;
    private JCheckBox SD6;
    private JCheckBox SD7;
    private JCheckBox SD8;
    private JCheckBox SD9;
    private JCheckBox SD10;
    private JCheckBox SD11;
    private JCheckBox SD12;
    private JCheckBox SD13;
    private JCheckBox SD14;
    private JCheckBox SD15;
    private JCheckBox SD16;
    private JCheckBox SD17;
    private JCheckBox SD18;
    private JCheckBox SD19;
    private JCheckBox SD20;
    private JCheckBox SD21;
    private JCheckBox SD22;
    private JCheckBox SD23;
    private JCheckBox SD24;
    private JCheckBox SD25;
    private JCheckBox SD26;
    private JCheckBox SD27;
    private JCheckBox SD28;
    private JCheckBox SD29;
    private JButton PB1;
    private JButton PB2;
    private JButton PB3;
    private JButton PB4;
    private JButton PB5;
    private JButton PB6;
    private JCheckBox L1;
    private JCheckBox L2;
    private JCheckBox L3;
    private JRadioButton SW1;
    private JRadioButton SW2;
    private JRadioButton SW3;
    private JRadioButton SW4;
    private JRadioButton SW5;
    private JRadioButton SW6;
    private JRadioButton SW10;
    private JRadioButton SW9;
    private JRadioButton SW8;
    private JRadioButton SW7;
    private JCheckBox L4;
    private JCheckBox L5;
    private JCheckBox L6;
    private JCheckBox L7;
    private JCheckBox L8;
    private JRadioButton MS;
    private JRadioButton VS;
    private JRadioButton SW12;
    private JRadioButton SW11;
    private JTextField userText;
    private JTextField a1TextField;

    private final MachineController machineController;
    private BigInteger[] pendingCoefficients;

    //Machine Controller, contains functions for the buttons
    private static class MachineController {

        //User has to 'Startup" Machine
        private boolean machineOn;

        //Linked with PB1. True: Addition, False: Subtraction
        private boolean additionMode;

        //ASM connection. True: Card Read, False: KA
        private boolean switch7;

        //AS relay control. True:Card, False: Overdrafts
        private boolean switch8;

        //Coefficient Banks
        private boolean switch1, switch2, switch3, switch4, switch5, switch6;

        //Drums
        private ABCControlPanel.BinaryRow CA, KA;
        private boolean CAClear, KAClear;
        private int selectedCoefficient;
        private int coefficientSign;

        public MachineController() {

            machineOn = false;
            additionMode = true;  // default mode is addition
            selectedCoefficient = 0;
            coefficientSign = 1;  // default to positive
            CA = new ABCControlPanel.BinaryRow(new BigInteger[30]);
            KA = new ABCControlPanel.BinaryRow(new BigInteger[30]);
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
                CA = new ABCControlPanel.BinaryRow(new BigInteger[30]);
                System.out.println("CA Drum Cleared");
            }
        }
        // SW12 : 63
        public void clearKA() {
            KAClear = !KAClear;
            if (KAClear) {
                KA = new ABCControlPanel.BinaryRow(new BigInteger[30]);
                System.out.println("KA Drum Cleared");
            }
        }

        // PB2 : 85
        public void loadCard(String userInput, MachineController machineController) {
            String[] parts = userInput.split(",");

            if (parts.length != 5) {
                throw new IllegalArgumentException("Input must contain exactly 5 numbers");
            }

            BigInteger[] result = new BigInteger[5];

            for (int i = 0; i < 5; i++) {
                result[i] = new BigInteger(parts[i].trim());
            }

            int offset = getOffset(machineController);

            for (int i = 0; i < 5; i++) {
                CA.coeffs[offset + i] = result[i];
            }

            // Show that the correct positions were updated
            for (int i = 0; i < CA.coeffs.length; i++) {
                System.out.println("CA[" + i + "] = " + CA.coeffs[i]);
            }
        }


        // PB3 : 69
        public void transferCAtoKA() {
            // Deep copy the coefficients from CA to KA.
            KA = CA.clone();
            System.out.println("Contents of CA transferred to KA.");
            for (int i = 0; i < KA.coeffs.length; i++) {
                System.out.println("KA[" + i + "] = " + KA.coeffs[i]);
            }
        }

        // ZD : 66 & 72
        public void coefficientSelect(int coe) {
            
        }


        public void setCoefficientSign(int signVal) {
            coefficientSign = signVal;
            String signStr = (coefficientSign == 1) ? "Positive" : "Negative";
            System.out.println("Step 29: Coefficient sign set to " + signStr + ".");
        }
    }

    //Contains listeners for the buttons
    public ControlPanel() {
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

        //8. Place first IBM card in reader. (User input).
        //User types in the text field

        //9. PB2: Start base-10 read operation
        PB2.addActionListener(e -> {
            String userInput = userText.getText();
            System.out.println(userInput);
            machineController.loadCard(userInput, machineController);
            userText.setText("");
        });

        //11. Transfer contents of CA to KA by pressing GO button PB3.
        PB3.addActionListener(e -> {
            machineController.transferCAtoKA();
        });

        //II: COMPUTING

        //13. Set switch SW7 so that inputs to ASM’s are connected to KA.
        //SW7 is listening above

        //14. Select coefficient to be eliminated by selecting checkbox.
        ZD0.addActionListener(e -> {

        });


        //PB4: Start Base-2 Punch
        //Pushbutton 88


        //PB6: Start Computation
        //Pushbutton 86


        //III: BASE-2 READING

        //PB5: Start Base-2 Read
        //Pushbutton 87


        //IV: BASE-10 READING

        // SD slider for checking sign.
//        SD.addChangeListener(new ChangeListener() {
//            @Override
//            public void stateChanged(ChangeEvent e) {
//                int signValue = SD.getValue();  // Assume -1 for negative, +1 for positive.
//                machineController.setCoefficientSign(signValue);
//                System.out.println("SD Slider: Coefficient sign set to " + (signValue == 1 ? "Positive" : "Negative"));
//            }
//        });

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
        frame.setContentPane(new ControlPanel().contentPane);
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

        public ABCControlPanel.BinaryRow clone() {
            BigInteger[] newCoeffs = new BigInteger[coeffs.length];
            for (int i = 0; i < coeffs.length; i++) {
                newCoeffs[i] = coeffs[i];
            }
            ABCControlPanel.BinaryRow copy = new ABCControlPanel.BinaryRow(newCoeffs);
            copy.shiftCount = this.shiftCount;
            return copy;
        }
    }

    private static int getOffset(MachineController machineController) {
        if (machineController.switch1) return 0;   // covers indexes 0..4
        if (machineController.switch2) return 5;   // covers indexes 5..9
        if (machineController.switch3) return 10;  // covers indexes 10..14
        if (machineController.switch4) return 15;  // covers indexes 15..19
        if (machineController.switch5) return 20;  // covers indexes 20..24
        if (machineController.switch6) return 25;  // covers indexes 25..29

        throw new IllegalStateException("No valid switch was set to true");
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
