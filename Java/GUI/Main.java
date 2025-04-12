import javax.swing.*;
import ui.PunchCardPanel;
import ui.ControlPanel;
import ui.CardSelectionListener;
import model.Card;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Atanasoff–Berry Computer Simulator");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);

            // Create a main panel using BorderLayout
            JPanel mainPanel = new JPanel(new BorderLayout());

            // Initialize the panels.
            PunchCardPanel punchPanel = new PunchCardPanel();
            ControlPanel controlPanel = new ControlPanel();

            // Set up the selection listener to update the odometer when a card is selected.
            punchPanel.setCardSelectionListener(new CardSelectionListener() {
                @Override
                public void cardSelected(Card card) {
                    controlPanel.updateOdometerValue(card.getValue());
                }
            });

            // Wrap the punch card panel in a JScrollPane so it can scroll.
            JScrollPane punchScrollPane = new JScrollPane(punchPanel);
            punchScrollPane.setPreferredSize(new Dimension(250, 600)); // Adjust width as needed

            // Add the punch card scroll pane on the left and the control panel in the center.
            mainPanel.add(punchScrollPane, BorderLayout.WEST);
            mainPanel.add(controlPanel, BorderLayout.CENTER);

            frame.add(mainPanel);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}