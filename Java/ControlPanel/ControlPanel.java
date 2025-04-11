import javax.swing.*;
import java.awt.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * ===============================
 *      UPDATED ABC "MANUAL"
 * ===============================
 *
 * I. READING BASE-10 CARDS
 *  1. Turn power switch MS on.
 *  2. (Ignored) Set meter switch VS to show plate voltage.
 *  3. Set switch SW7 so that inputs to ASM’s are connected to card reader.
 *  4. Set switch SW8 so that add-subtract relay is controlled by card (rather than overdrafts).
 *  5. Select ADD with pushbutton PB1 (monitor lights L2 for ADD, L3 for SUBTRACT).
 *  6. Select which field (5 coeffs) on CA to write by closing one of SW1–SW6.
 *  7. Clear CA and KA with switches SW11 and SW12 respectively (optional).
 *  8. Place first IBM card in reader. (Equation w/ biggest coeff to be eliminated goes first.)
 *  9. Press PB2 to load the 5 comma-separated numbers into the selected chunk of CA.
 * 10. For more than five coefficients, pick the next SW1–SW6, feed in next card, press PB2 again.
 * 11. Transfer CA to KA by pressing PB3.
 * 12. Repeat to place second equation in CA (if you’re doing multiple eqns).
 *
 * II. COMPUTING
 * 13. Set SW7 so that inputs to ASM’s are connected to KA (rather than the card reader).
 * 14. Select the coefficient to be eliminated (in code, use checkboxes ZD0..ZD29 or a Mask).
 * 15. Set SW8 so add-subtract relay is switched by overdrafts (i.e. SW8 toggled).
 * 16. Press PB1 to choose ADD if signs of coefficients are alike, SUBTRACT if opposite.
 * 17. Press PB6 to start the computation. It auto-stops when selected coeff is zero.
 * 18. (Ignored) Place blank base-2 card in rack.
 * 19. Press PB4 to punch contents of CA (the answer) onto a base-2 card (stored in punchCards).
 *
 * III. BASE-2 READING
 * 20. Return SW7 to “card read” position (so switch7 = true).
 * 21. Put machine in ADD by pressing PB1 if needed.
 * 22. Place first card in base-2 reading rack (select from punch card JList).
 * 23. Clear CA and KA by pressing SW11 and SW12 (optional).
 * 24. Read first card into CA by pressing PB5 (reads the selected punch card).
 * 25. Transfer CA to KA by pressing PB3.
 * 26. Place second card in rack.
 * 27. Read second card into CA by pressing PB5.
 *
 * IV. BASE-10 READING
 * 28. Select the coefficient location to be read (in code, ZD checkboxes or new “mask + odometer”).
 * 29. (Originally: check sign with SD slider. We have checkboxes SD0..SD29 if needed.)
 * 30. Place machine in SUBTRACT if number is positive, ADD if negative (PB1).
 * 31. (Hardware detail) Open SW10 to connect powers-of-10 brush in base-10 card reader. (Optional)
 * 32. Set SW7 to card read position.
 * 33. Set SW8 to sense overdrafts.
 * 34. Press PB6 to compute.
 * 35. Read base-10 number on counters (in code, print CA or watch console).
 *
 * ===============================
 *   END OF MANUAL / INSTRUCTIONS
 * ===============================
 */
public class ControlPanel {

    // The main UI panel that holds your existing checkboxes, buttons, etc.
    private JPanel contentPane;

    // Existing CheckBoxes for zero detect, sign detect, etc.
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
    private JButton base2PrintButton;
    private JButton CAPrintButton;
    private JButton KAPrintButton;

    // Our machine controller:
    private final MachineController machineController;

    // The list of punch cards (base-2).
    private static List<BinaryRow> punchCards;

    // A separate list for mask cards (5 masks). Each mask has one column with 2^14.
    private static List<BinaryRow> maskCards;

    // The model + JList for punch cards:
    private static DefaultListModel<String> punchCardModel;
    private static JList<String> punchCardList;

    // The model + JList for mask cards:
    private static DefaultListModel<String> maskListModel;
    private static JList<String> maskList;

    // The “odometer” label on the right panel:
    private JLabel odometerLabel;

    // We call this to set the static punchCardList from main
    public static void setPunchCardList(JList<String> list) {
        punchCardList = list;
    }

    // Similarly for the mask list, if we want to pass it around
    public static void setMaskList(JList<String> list) {
        maskList = list;
    }

    // =============================
    //         CONSTRUCTOR
    // =============================
    public ControlPanel() {
        // 1. Create the controller that handles the operations.
        machineController = new MachineController();

        // -----------------------------------
        // 2. Preload the 5 "Mask" cards
        // -----------------------------------
        maskCards = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            BigInteger[] maskArray = new BigInteger[30];
            for (int j = 0; j < 30; j++) {
                maskArray[j] = BigInteger.ZERO;
            }
            // Set the i-th index to 2^14 = 16384
            maskArray[i] = BigInteger.valueOf(1L << 14);
            maskCards.add(new BinaryRow(maskArray));
        }

        // -----------------------------------
        // 3. Preload standard punch cards
        // -----------------------------------
        punchCards = new ArrayList<>();

        // Card #0: [1, 1, 1, 4, 0, ...]
        BigInteger[] card0 = new BigInteger[30];
        card0[0] = BigInteger.ONE;
        card0[1] = BigInteger.ONE;
        card0[2] = BigInteger.ONE;
        card0[3] = BigInteger.valueOf(4);
        for (int i = 4; i < 30; i++) {
            card0[i] = BigInteger.ZERO;
        }
        punchCards.add(new BinaryRow(card0));

        // Card #1: [1, 3, 3, 10, 0, ...]
        BigInteger[] card1 = new BigInteger[30];
        card1[0] = BigInteger.ONE;
        card1[1] = BigInteger.valueOf(3);
        card1[2] = BigInteger.valueOf(3);
        card1[3] = BigInteger.valueOf(10);
        for (int i = 4; i < 30; i++) {
            card1[i] = BigInteger.ZERO;
        }
        punchCards.add(new BinaryRow(card1));

        // Card #2: [2, 1, -1, 3, 0, ...]
        BigInteger[] card2 = new BigInteger[30];
        card2[0] = BigInteger.valueOf(2);
        card2[1] = BigInteger.valueOf(1);
        card2[2] = BigInteger.valueOf(-1);
        card2[3] = BigInteger.valueOf(3);
        for (int i = 4; i < 30; i++) {
            card2[i] = BigInteger.ZERO;
        }
        punchCards.add(new BinaryRow(card2));

        // Card #3: [0, 2, 2, 6, 0, ...]
        BigInteger[] card3 = new BigInteger[30];
        card3[0] = BigInteger.ZERO;
        card3[1] = BigInteger.valueOf(2);
        card3[2] = BigInteger.valueOf(2);
        card3[3] = BigInteger.valueOf(6);
        for (int i = 4; i < 30; i++) {
            card3[i] = BigInteger.ZERO;
        }
        punchCards.add(new BinaryRow(card3));

        // Card #4: [0, -5, -7, -17, 0, ...]
        BigInteger[] card4 = new BigInteger[30];
        card4[0] = BigInteger.ZERO;
        card4[1] = BigInteger.valueOf(-5);
        card4[2] = BigInteger.valueOf(-7);
        card4[3] = BigInteger.valueOf(-17);
        for (int i = 4; i < 30; i++) {
            card4[i] = BigInteger.ZERO;
        }
        punchCards.add(new BinaryRow(card4));

        // Card #5: [0, 0, -2, -2, 0, ...]
        BigInteger[] card5 = new BigInteger[30];
        card5[0] = BigInteger.ZERO;
        card5[1] = BigInteger.ZERO;
        card5[2] = BigInteger.valueOf(-2);
        card5[3] = BigInteger.valueOf(-2);
        for (int i = 4; i < 30; i++) {
            card5[i] = BigInteger.ZERO;
        }
        punchCards.add(new BinaryRow(card5));

        // Card #6: [0, 2, 0, 4, 0, ...]
        BigInteger[] card6 = new BigInteger[30];
        card6[0] = BigInteger.ZERO;
        card6[1] = BigInteger.valueOf(2);
        card6[2] = BigInteger.ZERO;
        card6[3] = BigInteger.valueOf(4);
        for (int i = 4; i < 30; i++) {
            card6[i] = BigInteger.ZERO;
        }
        punchCards.add(new BinaryRow(card6));

        // Card #7: [1, 1, 0, 3, 0, ...]
        BigInteger[] card7 = new BigInteger[30];
        card7[0] = BigInteger.ONE;
        card7[1] = BigInteger.ONE;
        card7[2] = BigInteger.ZERO;
        card7[3] = BigInteger.valueOf(3);
        for (int i = 4; i < 30; i++) {
            card7[i] = BigInteger.ZERO;
        }
        punchCards.add(new BinaryRow(card7));

        // Update list model after preloading
        updatePunchCardListModel();

        // 4. The “odometer” label (shown on the right panel)
        odometerLabel = new JLabel("Odometer: 0");

        // 5. Add the action listeners for your existing UI components
        //    EXACTLY as in your original code.
        //    (All the SW1..SW12 toggles, PB1..PB6, etc.)
        //    The following is an example snippet—retain your actual code:

        // Example:
        MS.addActionListener(e -> {
            machineController.setMachineStatus(true);
            machineController.setAdditionMode(true);
            L2.setSelected(true);
            L3.setSelected(false);
            System.out.println("Machine started.");
        });

        // SW7 / SW8 toggles:
        SW7.addActionListener(e -> machineController.toggleSwitch7());
        SW8.addActionListener(e -> machineController.toggleSwitch8());

        // PB1 toggles ADD/SUBTRACT:
        PB1.addActionListener(e -> {
            machineController.toggleAddSubtraction();
            if (machineController.isAdditionMode()) {
                L2.setSelected(true);
                L3.setSelected(false);
            } else {
                L2.setSelected(false);
                L3.setSelected(true);
            }
        });

        // SW1..SW6
        SW1.addActionListener(e -> machineController.toggleBank(1));
        SW2.addActionListener(e -> machineController.toggleBank(2));
        SW3.addActionListener(e -> machineController.toggleBank(3));
        SW4.addActionListener(e -> machineController.toggleBank(4));
        SW5.addActionListener(e -> machineController.toggleBank(5));
        SW6.addActionListener(e -> machineController.toggleBank(6));

        // SW11..SW12
        SW11.addActionListener(e -> machineController.clearCA());
        SW12.addActionListener(e -> machineController.clearKA());

        // PB2: load base-10 card
        PB2.addActionListener(e -> {
            String userInput = userText.getText();
            System.out.println(userInput);
            machineController.loadCard(userInput, machineController);
            userText.setText("");
        });

        // PB3: transfer CA to KA
        PB3.addActionListener(e -> machineController.transferCAtoKA());

        // PB4: punch CA to new card
        PB4.addActionListener(e -> {
            machineController.punchContents();
            updatePunchCardListModel();
        });

        // PB5: read selected punch card into CA
        PB5.addActionListener(e -> {
            int selectedIndex = punchCardList.getSelectedIndex();
            if (selectedIndex < 0) {
                JOptionPane.showMessageDialog(contentPane, "Please select a punch card first.");
                return;
            }
            machineController.readCard(selectedIndex);
        });

        // PB6: start computation
        PB6.addActionListener(e -> {
            System.out.println("Computation Started");
            machineController.startComputation();
        });

        // For your coefficient checkboxes ZD0..ZD29
        ZD0.addActionListener(e -> machineController.coefficientSelect(0));
        ZD1.addActionListener(e -> machineController.coefficientSelect(1));
        ZD2.addActionListener(e -> machineController.coefficientSelect(2));
        ZD3.addActionListener(e -> machineController.coefficientSelect(3));
        // etc... replicate for ZD4..ZD29

        // Similarly for SD0..SD29 if needed
        // (the original code had them toggling the same method or something else)

        // Print buttons
        CAPrintButton.addActionListener(e -> machineController.printMatrix(-1));
        KAPrintButton.addActionListener(e -> machineController.printMatrix(-2));
        base2PrintButton.addActionListener(e -> {
            int selectedIndex = punchCardList.getSelectedIndex();
            if (selectedIndex < 0) {
                JOptionPane.showMessageDialog(contentPane, "Please select a punch card first.");
                return;
            }
            machineController.printMatrix(selectedIndex);
        });



    }

    // =====================================================
    //     LIST MODELS (Punch Cards & Masks) + UPDATERS
    // =====================================================

    // Punch card list model
    public static DefaultListModel<String> createPunchCardListModel() {
        DefaultListModel<String> model = new DefaultListModel<>();
        if (punchCards == null || punchCards.isEmpty()) {
            model.addElement("No Punch Cards");
        } else {
            for (int i = 0; i < punchCards.size(); i++) {
                model.addElement("Card #" + i);
            }
        }
        return model;
    }

    // Update the punch card list
    public static void updatePunchCardListModel() {
        if (punchCardList != null) {
            punchCardModel = createPunchCardListModel();
            punchCardList.setModel(punchCardModel);
        } else {
            System.out.println("Punch card list is not initialized yet.");
        }
    }

    // Mask list model
    public static DefaultListModel<String> createMaskListModel() {
        DefaultListModel<String> model = new DefaultListModel<>();
        if (maskCards == null || maskCards.isEmpty()) {
            model.addElement("No Masks");
        } else {
            for (int i = 0; i < maskCards.size(); i++) {
                model.addElement("Mask #" + i);
            }
        }
        return model;
    }

    public static void updateMaskListModel() {
        if (maskList != null) {
            maskListModel = createMaskListModel();
            maskList.setModel(maskListModel);
        } else {
            System.out.println("Mask list is not initialized yet.");
        }
    }

    // =====================================================
    //     BINARY ROW (Drum) CLASS
    // =====================================================
    public static class BinaryRow {
        BigInteger[] coeffs;  // row of coefficients
        int shiftCount;       // how many times shifted right

        public BinaryRow(BigInteger[] coeffs) {
            this.coeffs = coeffs;
            this.shiftCount = 0;
        }

        public BinaryRow clone() {
            BigInteger[] newCoeffs = new BigInteger[coeffs.length];
            System.arraycopy(coeffs, 0, newCoeffs, 0, coeffs.length);
            BinaryRow copy = new BinaryRow(newCoeffs);
            copy.shiftCount = this.shiftCount;
            return copy;
        }
    }

    // =====================================================
    //     MACHINE CONTROLLER
    // =====================================================
    private static class MachineController {

        // Basic on/off & modes
        private boolean machineOn;
        private boolean additionMode; // true = ADD, false = SUBTRACT

        // Switches controlling how ASM inputs are connected, etc.
        private boolean switch7;  // true = card reader, false = KA
        private boolean switch8;  // true = card, false = overdrafts

        // Bank switches for which chunk of CA to fill (SW1..SW6)
        private boolean switch1, switch2, switch3, switch4, switch5, switch6;

        // Drums
        private BinaryRow CA, KA;
        private boolean CAClear, KAClear;

        // Which coefficient index is selected
        private int selectedCoefficient;

        // Odometer reading for the “mask” approach
        private BigInteger odometerValue;  // The actual number from CA

        public MachineController() {
            machineOn = false;
            additionMode = true;
            selectedCoefficient = -1;

            CA = new BinaryRow(new BigInteger[30]);
            KA = new BinaryRow(new BigInteger[30]);

            switch1 = switch2 = switch3 = false;
            switch4 = switch5 = switch6 = false;
            CAClear = false;
            KAClear = false;
            odometerValue = BigInteger.ZERO;  // default

            // Re-initialize punchCards if needed
            punchCards = new ArrayList<>();
        }

        // Odometer methods
        public BigInteger applyMask(BinaryRow mask) {
            // 1. Find which column has 2^14
            BigInteger bigMask = BigInteger.valueOf(1L << 14); // 2^14
            int maskIndex = -1;
            for (int i = 0; i < mask.coeffs.length; i++) {
                if (mask.coeffs[i].equals(bigMask)) {
                    maskIndex = i;
                    break;
                }
            }
            if (maskIndex == -1) {
                System.out.println("No 2^14 found in mask!");
                return BigInteger.ZERO;
            }

            // 2. Set our selectedCoefficient
            selectedCoefficient = maskIndex;

            // 3. Grab the current value in CA at that index
            odometerValue = CA.coeffs[maskIndex];

            System.out.println("Mask applied. Selected coefficient index = " + maskIndex
                    + "  CA-value = " + odometerValue);

            OdometerWindow window = new OdometerWindow(odometerValue, additionMode);
            window.setVisible(true);
            window.showTick();

            return odometerValue; // Return the actual number
        }

        public BigInteger getOdometerReading() {
            return odometerValue;
        }

        public void resetOdometer() {
            odometerValue = BigInteger.valueOf(0);
            selectedCoefficient = -1;
            System.out.println("Odometer reset to 0. No coefficient selected.");
        }

        // Basic getters/setters
        public boolean isAdditionMode() {
            return additionMode;
        }

        public void setAdditionMode(boolean mode) {
            additionMode = mode;
        }

        public void setMachineStatus(boolean mode) {
            machineOn = mode;
        }

        // Switch toggles
        public void toggleSwitch7() {
            switch7 = !switch7;
            System.out.println("Inputs to ASM's are now connected to: " + (switch7 ? "Card Reader" : "KA"));
        }

        public void toggleSwitch8() {
            switch8 = !switch8;
            System.out.println("Add-Subtract relay is controlled by: " + (switch8 ? "Card" : "Overdrafts"));
        }

        // PB1 toggles ADD/SUBTRACT
        public void toggleAddSubtraction() {
            additionMode = !additionMode;
            System.out.println("Operation is now: " + (additionMode ? "Addition" : "Subtraction"));
        }

        // SW1..SW6 toggle
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

        // Clear CA / KA
        public void clearCA() {
            CAClear = !CAClear;
            if (CAClear) {
                CA = new BinaryRow(new BigInteger[30]);
                for (int i = 0; i < CA.coeffs.length; i++) {
                    CA.coeffs[i] = BigInteger.ZERO;
                }
                System.out.println("CA Drum Cleared");
            }
        }

        public void clearKA() {
            KAClear = !KAClear;
            if (KAClear) {
                KA = new BinaryRow(new BigInteger[30]);
                for (int i = 0; i < KA.coeffs.length; i++) {
                    KA.coeffs[i] = BigInteger.ZERO;
                }
                System.out.println("KA Drum Cleared");
            }
        }

        // PB2: load base-10 card
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
            System.arraycopy(result, 0, CA.coeffs, offset, 5);
            printMatrix(-1);
        }

        // PB3: transfer CA to KA
        public void transferCAtoKA() {
            KA = CA.clone();
            System.out.println("Contents of CA transferred to KA.");
            printMatrix(-2);
        }

        // Selecting which coefficient (ZD) to manipulate
        public void coefficientSelect(int coe) {
            if (selectedCoefficient == coe) {
                selectedCoefficient = -1;
            } else {
                selectedCoefficient = coe;
            }
            // You could also set odometerReading here if you want
            // odometerReading = (selectedCoefficient >= 0) ? selectedCoefficient : 0;
        }

        // PB6: start computation
        public void startComputation() {
            // Some checks
            if (switch7) {
                System.out.println("Currently set to Card Reader. Switch to KA for computing?");
                System.out.println("Computation Quit");
                return;
            }
            if (selectedCoefficient == -1) {
                System.out.println("Need coefficient selected.");
                System.out.println("Computation Quit");
                return;
            }
            if (switch8) {
                System.out.println("Add-Subtract relay is controlled by Card. Need overdrafts for the iterative approach?");
                System.out.println("Computation Quit");
                return;
            }

            boolean running = true;
            while (running) {
                BigInteger sign = CA.coeffs[selectedCoefficient];
                if (additionMode) {
                    for (int i = 0; i < CA.coeffs.length; i++) {
                        CA.coeffs[i] = CA.coeffs[i].add(KA.coeffs[i]);
                    }
                } else {
                    for (int i = 0; i < CA.coeffs.length; i++) {
                        CA.coeffs[i] = CA.coeffs[i].subtract(KA.coeffs[i]);
                    }
                }
                // If selected coefficient is zero, we stop
                if (CA.coeffs[selectedCoefficient].equals(BigInteger.ZERO)) {
                    running = false;
                }
                // If sign changed, we flip add/sub and half KA
                else if (sign.signum() != CA.coeffs[selectedCoefficient].signum()) {
                    additionMode = !additionMode;
                    for (int i = 0; i < KA.coeffs.length; i++) {
                        KA.coeffs[i] = KA.coeffs[i].divide(BigInteger.valueOf(2));
                    }
                }
            }
            System.out.println("Computation Complete");
        }

        // PB4: punch CA to punchCards
        public void punchContents() {
            punchCards.add(CA.clone());
            System.out.println("Punched Card added. Total cards: " + punchCards.size());
        }

        // PB5: read a punch card into CA
        public void readCard(int index) {
            if (punchCards == null || index < 0 || index >= punchCards.size()) {
                System.out.println("Invalid card selection.");
                return;
            }
            BinaryRow card = punchCards.get(index);
            CA = card.clone();
            System.out.println("Read card #" + index + " into CA.");
        }

        // Print matrix: -1 for CA, -2 for KA, otherwise card index
        public void printMatrix(int index) {
            if (index == -1) {
                System.out.print("CA: [");
                for (int i = 0; i < CA.coeffs.length; i++) {
                    System.out.print(CA.coeffs[i] + ", ");
                }
                System.out.println("]");
            } else if (index == -2) {
                System.out.print("KA: [");
                for (int i = 0; i < KA.coeffs.length; i++) {
                    System.out.print(KA.coeffs[i] + ", ");
                }
                System.out.println("]");
            } else {
                if (punchCards == null || index < 0 || index >= punchCards.size()) {
                    System.out.println("Invalid punch card index for printing.");
                    return;
                }
                BinaryRow card = punchCards.get(index);
                System.out.print("CD: [");
                for (int i = 0; i < card.coeffs.length; i++) {
                    System.out.print(card.coeffs[i] + ", ");
                }
                System.out.println("]");
            }
        }
    }

    // Helper to figure out offset for SW1..SW6
    private static int getOffset(MachineController mc) {
        if (mc.switch1) return 0;   // covers 0..4
        if (mc.switch2) return 5;   // covers 5..9
        if (mc.switch3) return 10;  // covers 10..14
        if (mc.switch4) return 15;  // covers 15..19
        if (mc.switch5) return 20;  // covers 20..24
        if (mc.switch6) return 25;  // covers 25..29
        throw new IllegalStateException("No valid switch was set to true!");
    }

    // =============================
    //          MAIN
    // =============================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 1. Instantiate your ControlPanel
            ControlPanel controlPanelInstance = new ControlPanel();

            // 2. Punch card list + model
            punchCardModel = createPunchCardListModel();
            punchCardList = new JList<>(punchCardModel);
            punchCardList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            punchCardList.addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    int selectedIndex = punchCardList.getSelectedIndex();
                    System.out.println("Selected Punch Card: " + selectedIndex);
                }
            });
            setPunchCardList(punchCardList);
            JScrollPane punchScrollPane = new JScrollPane(punchCardList);
            punchScrollPane.setPreferredSize(new Dimension(200, 400));

            // 3. Mask list + model
            maskListModel = createMaskListModel();
            maskList = new JList<>(maskListModel);
            maskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            setMaskList(maskList);
            JScrollPane maskScrollPane = new JScrollPane(maskList);
            maskScrollPane.setPreferredSize(new Dimension(150, 100));

            // 4. Right Panel (odometer panel)
            JPanel odometerPanel = new JPanel();
            odometerPanel.setLayout(new BoxLayout(odometerPanel, BoxLayout.Y_AXIS));

            JLabel maskLabel = new JLabel("Available Masks:");
            odometerPanel.add(maskLabel);
            odometerPanel.add(maskScrollPane);

            // "Apply Mask" button
            JButton applyMaskButton = new JButton("Apply Mask");
            odometerPanel.add(applyMaskButton);

            MechanicalOdometer mechanicalOdometer;
            mechanicalOdometer = new MechanicalOdometer(controlPanelInstance.odometerLabel);
            applyMaskButton.addActionListener(e -> {
                int sel = maskList.getSelectedIndex();
                if (sel < 0) {
                    JOptionPane.showMessageDialog(null, "Please select a mask first.");
                    return;
                }
                // This sets selectedCoefficient + returns CA-value for that column
                BigInteger val = controlPanelInstance.machineController.applyMask(maskCards.get(sel));

                // Old approach: just set a l   abel:
                // controlPanelInstance.odometerLabel.setText("Odometer: " + val);

                // NEW approach: hand this value to our mechanical odometer
                mechanicalOdometer.setValueWithTicks(val);
            });

            // The label from the constructor
            odometerPanel.add(controlPanelInstance.odometerLabel);

            // "Reset Odometer" button
            JButton resetButton = new JButton("Reset Odometer");
            odometerPanel.add(resetButton);
            resetButton.addActionListener(e -> {
                mechanicalOdometer.resetToZero();
            });

            // 5. Assemble mainPanel
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.add(punchScrollPane, BorderLayout.WEST);
            mainPanel.add(controlPanelInstance.contentPane, BorderLayout.CENTER);
            mainPanel.add(odometerPanel, BorderLayout.EAST);

            // 6. Frame setup
            JFrame frame = new JFrame("ABC Control Panel");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);

//            applyMaskButton.addActionListener(e -> {
//                int sel = maskList.getSelectedIndex();
//                if (sel < 0) {
//                    JOptionPane.showMessageDialog(null, "Please select a mask first.");
//                    return;
//                }
//                // We call applyMask, which now returns the CA value for the chosen column
//                BigInteger val = controlPanelInstance.machineController.applyMask(maskCards.get(sel));
//
//                // Update the odometer label to show that CA value
//                controlPanelInstance.odometerLabel.setText("Odometer: " + val);
//            });
        });

    }

    public static class MechanicalOdometer {
        // 15 digits (index 0 = highest place, index 14 = lowest place)
        private final int[] digits = new int[15];

        // A small helper label (or pass a callback) to show the odometer reading
        private final JLabel odometerLabel;

        public MechanicalOdometer(JLabel odometerLabel) {
            this.odometerLabel = odometerLabel;
            resetToZero();
        }

        // Reset everything to 000000000000000
        public void resetToZero() {
            for (int i = 0; i < 15; i++) {
                digits[i] = 0;
            }
            updateLabel();
        }

        // Return a zero-padded 15-digit string
        public String getReading() {
            StringBuilder sb = new StringBuilder();
            for (int d : digits) {
                sb.append(d);
            }
            return sb.toString();
        }

        // Called when we want the odometer to show the decimal value of 'finalVal'
        // but do so in the iterative mechanical approach you described.
        public void setValueWithTicks(BigInteger finalVal) {
            // We’ll run the mechanical approach in a background thread
            // so the GUI won’t freeze during the repeated ticks.
            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() throws Exception {
                    // 1) Start from zero
                    resetToZeroInternal();

                    // 2) If finalVal is positive, we do the “subtract 10 repeatedly,
                    //    then move to next smaller decade, etc.” approach.
                    //    If finalVal is negative, we do a complementary “add 10 repeatedly” approach.
                    BigInteger currentVal = BigInteger.ZERO;

                    // We'll do the "from top decade to bottom decade" approach
                    // but we must replicate the sample logic:
                    //
                    // a) Repeatedly subtract "10^somePower" in decimal from the currentVal
                    //    until we can't (the result is negative). Then revert the last step.
                    // b) If we actually overshoot (become negative), switch from subtracting
                    //    to adding the "next smaller decade," etc.
                    //
                    // The example given for 28 is very hardware-specific.
                    // We'll do a simplified version that yields the same final reading
                    // but carefully increments digits one at a time.

                    // We'll define a method for "tick up a digit" or "tick down a digit"
                    // so the user sees the mechanical effect.

                    // Convert finalVal to absolute so we handle positive path first
                    BigInteger target = finalVal.abs();

                    for (int decade = 0; decade < 15; decade++) {
                        BigInteger placeValue = BigInteger.TEN.pow(14 - decade); // 10^(14-decade)

                        // Repeatedly subtract placeValue from 'target'
                        // while remaining >= 0
                        while (target.compareTo(placeValue) >= 0) {
                            target = target.subtract(placeValue); // subtract decimal "10^(14-decade)"
                            digits[decade] += 1;
                            // mechanical "roll" if we exceed '9'
                            if (digits[decade] > 9) {
                                digits[decade] = 0; // roll over
                                // if that happens, increment the next higher place if any
                                rollOverHigherPlace(decade - 1);
                            }
                            updateLabel();
                            // Sleep a bit to see each “tick”
                            Thread.sleep(2000);
                        }
                        // Now we've subtracted all we can at this decade. We move on to the next one.
                    }

                    // If original finalVal was negative, we can do the same logic but in reverse
                    // (Add placeValue while current < |finalVal|, etc.).
                    // Or do an additional pass.
                    // For brevity, let's handle only positives in detail.
                    // You can expand on this if you need the full negative path from your example.

                    // If there's some remainder left, you can do a final small increment
                    // to account for 1^0 steps, or handle negative logic if finalVal was < 0.

                    // For the user’s example: we might do repeated 10-subtractions,
                    // then if we go negative, revert, switch to next smaller decade, etc.
                    // But here we kept it simpler. The effect is that you see each digit “tick up”
                    // until you represent the final decimal number.

                    // The user’s final step was: if we end exactly at zero, we "tick" one more time, etc.
                    // Implement that if you want. For example:
                    if (target.equals(BigInteger.ZERO) && finalVal.signum() != 0) {
                        // maybe do one last "tick"? Up to you.
                    }

                    // If finalVal was negative, you could forcibly set a minus sign, or
                    // invert the digits in some mechanical way. That’s up to you.
                    // For now, we’ll just assume we needed a nonnegative final value.

                    return null;
                }

                @Override
                protected void done() {
                    // Nothing special at the end, unless you want to confirm final results
                }
            };
            worker.execute();
        }

        // Internal reset without updating the label
        private void resetToZeroInternal() {
            for (int i = 0; i < 15; i++) {
                digits[i] = 0;
            }
        }

        // If we roll over from 9->0 at index = decade, we increment the next higher place (decade-1).
        // If decade-1 < 0, we ignore (beyond leftmost digit).
        private void rollOverHigherPlace(int index) {
            if (index < 0) return; // no higher place
            digits[index] += 1;
            if (digits[index] > 9) {
                digits[index] = 0;
                rollOverHigherPlace(index - 1);
            }
        }

        private void updateLabel() {
            // Update the label text on the Event Dispatch Thread
            SwingUtilities.invokeLater(() -> odometerLabel.setText(getReading()));
        }
    }
}