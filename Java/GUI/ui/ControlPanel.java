package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ControlPanel extends JPanel {
    private OdometerDisplay odometer;

    public ControlPanel() {
        setLayout(new BorderLayout());

        // Create the odometer display component.
        odometer = new OdometerDisplay();
        add(odometer, BorderLayout.CENTER);

        // Optional: a button panel for manual testing.
        JPanel buttonPanel = new JPanel();
        JButton loadButton = new JButton("Load Card (Random)");
        loadButton.addActionListener(e -> {
            int value = (int)(Math.random() * 10000);
            odometer.updateValue(value);
        });
        buttonPanel.add(loadButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Public method to update the odometer based on a card's value.
    public void updateOdometerValue(int value) {
        odometer.updateValue(value);
    }
}