package src.main.java.gui.model.Odometer;

import src.main.java.ABCMachine;
import src.main.java.base10.Base10Punch;

import javax.swing.*;
import java.awt.*;

/*
    1. Start at 10^14.
    2. Repeatedly add or subtract that value to CA and tick until sign change.
    3. Change operation after sign change and move to next column to the right.
            10^(i-1)
    4. Repeat
    5. Terminate if zero
 */
public class OdometerPanel extends JPanel {
    private JPanel digitBox;                // Inner Panel
    private static final int ROWS = 6;
    private static final int COLS = 15;
    private JLabel[][] digitLabels = new JLabel[ROWS][COLS];
    private JButton resetBtn;
    private JButton KAPrintButton;
    private JButton CAPrintButton;
    private JButton Base10Punch;
    private static final int CELL_SIDE_PX = 18;
    private static final int FONT_SIZE_PX = 14;
    private String[][] odometerStrings = {
            {"0","0","0","0","0","0","0","0","0","0","0","0","0","0","0"},
            {"0","9","0","9","0","9","0","9","0","9","0","9","0","9","1"},
            {"1","8","1","8","1","8","1","8","1","8","1","8","1","8","2"},
            {"2","7","2","7","2","7","2","7","2","7","2","7","2","7","3"},
            {"3","6","3","6","3","6","3","6","3","6","3","6","3","6","4"},
            {"4","5","4","5","4","5","4","5","4","5","4","5","4","5","5"},
            {"5","4","5","4","5","4","5","4","5","4","5","4","5","4","6"},
            {"6","3","6","3","6","3","6","3","6","3","6","3","6","3","7"},
            {"7","2","7","2","7","2","7","2","7","2","7","2","7","2","8"},
            {"8","1","8","1","8","1","8","1","8","1","8","1","8","1","9"},
            {"9","0","9","0","9","0","9","0","9","0","9","0","9","0","0"}
    };

    private static final String[][] INITIAL_TEMPLATE = {
            {"0","0","0","0","0","0","0","0","0","0","0","0","0","0","0"},
            {"0","9","0","9","0","9","0","9","0","9","0","9","0","9","1"},
            {"1","8","1","8","1","8","1","8","1","8","1","8","1","8","2"},
            {"2","7","2","7","2","7","2","7","2","7","2","7","2","7","3"},
            {"3","6","3","6","3","6","3","6","3","6","3","6","3","6","4"},
            {"4","5","4","5","4","5","4","5","4","5","4","5","4","5","5"},
            {"5","4","5","4","5","4","5","4","5","4","5","4","5","4","6"},
            {"6","3","6","3","6","3","6","3","6","3","6","3","6","3","7"},
            {"7","2","7","2","7","2","7","2","7","2","7","2","7","2","8"},
            {"8","1","8","1","8","1","8","1","8","1","8","1","8","1","9"},
            {"9","0","9","0","9","0","9","0","9","0","9","0","9","0","0"}
    };

    public OdometerPanel(ABCMachine abc) {

        // Outer Layer
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(8, 4, 8, 4));

        // Inner Digit Box
        digitBox = new JPanel(new GridLayout(ROWS, COLS, 0, 0));
        digitBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        digitBox.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        // Digit Grid
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                JLabel lbl = new JLabel(odometerStrings[r][c], SwingConstants.CENTER);
                lbl.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                lbl.setFont(new Font("Monospaced", Font.BOLD, FONT_SIZE_PX));
                lbl.setPreferredSize(new Dimension(CELL_SIDE_PX, CELL_SIDE_PX));
                digitLabels[r][c] = lbl;
                digitBox.add(lbl);
            }
        }
        add(digitBox);

        // Reset Button
        add(Box.createVerticalStrut(6));        // Vertical Gap
        resetBtn = new JButton("Reset Dials");
        resetBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        resetBtn.addActionListener(e -> resetOdometer());
        add(resetBtn);

        // CA Print
        add(Box.createVerticalStrut(20));
        CAPrintButton = new JButton("CAPrint");
        CAPrintButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        CAPrintButton.addActionListener(e -> {
            System.out.println("CA: ");
            abc.ca.printData();
        });
        CAPrintButton.setToolTipText("Print CA Drum Contents");
        add(CAPrintButton);

        // KA Print
        add(Box.createVerticalStrut(6));
        KAPrintButton = new JButton("KAPrint");
        KAPrintButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        KAPrintButton.addActionListener(e -> {
            System.out.println("KA: ");
            abc.ka.printData();
        });
        KAPrintButton.setToolTipText("Print KA Drum Contents");
        add(KAPrintButton);

        // Base10 Punch
        add(Box.createVerticalStrut(6));
        Base10Punch = new JButton(("10Punch"));
        Base10Punch.setAlignmentX(Component.CENTER_ALIGNMENT);
        Base10Punch.addActionListener(e -> new Base10Punch(abc));
        Base10Punch.setToolTipText("Punch a Base-10 Card");
        add(Base10Punch);

        // Glue to top
        add(Box.createVerticalGlue());
    }

    public void resetOdometer() {
        // Copy over
        for (int r = 0; r < INITIAL_TEMPLATE.length; r++) {
            System.arraycopy(INITIAL_TEMPLATE[r], 0,
                    odometerStrings[r], 0,
                    INITIAL_TEMPLATE[r].length);
        }

        // Update
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                digitLabels[row][col].setText(odometerStrings[row][col]);
            }
        }
    }

    public void tick(int column) {
        //System.out.println("Ticking column: " + column);
        String[] copy = new String[11];
        for (int i = 0; i < 11; ++i) {
            copy[i] = odometerStrings[i][column];
        }
        for (int i = 0; i < 11; ++i) {
            if (i == 10) {
                odometerStrings[i][column] = copy[0];
            } else {
                odometerStrings[i][column] = copy[i+1];
            }
        }

        for (int row = 0; row < ROWS; row++) {
            digitLabels[row][column].setText(odometerStrings[row][column]);
        }

        // Sleep so user can watch each step
        try {
            Thread.sleep(250);  //
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
