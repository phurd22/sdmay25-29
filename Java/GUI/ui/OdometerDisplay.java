package ui;

import javax.swing.*;
import java.awt.*;

public class OdometerDisplay extends JPanel {
    private JLabel displayLabel;

    public OdometerDisplay() {
        setLayout(new GridBagLayout());
        displayLabel = new JLabel("0000");
        displayLabel.setFont(new Font("Monospaced", Font.BOLD, 48));
        add(displayLabel);
    }

    // Update the displayed value.
    public void updateValue(int newValue) {
        displayLabel.setText(String.format("%04d", newValue));
    }
}