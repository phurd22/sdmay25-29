package src.main.java.gui.model;

import src.main.java.ABCMachine;
import src.main.java.gui.model.CardSelect.CardSelect;

import javax.swing.*;
import java.awt.*;

public class ABCGUI extends JFrame {

    public ABCGUI () {
        super("ABC Emulator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        ABCMachine abc = new ABCMachine();

        // Card Select
        CardSelect selector = new CardSelect();
        selector.setPreferredSize(new Dimension(190, 450));


        ControlPanel control = new ControlPanel(abc, selector, abc.odometerPanel);
        control.setPreferredSize(new Dimension(965, 450));


        JSplitPane rightSplit = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                control,
                abc.odometerPanel
        );
        rightSplit.setDividerLocation(control.getPreferredSize().width);
        rightSplit.setResizeWeight(0.0);

        JSplitPane mainSplit = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                selector,
                rightSplit
        );
        mainSplit.setDividerLocation(selector.getPreferredSize().width);
        mainSplit.setResizeWeight(0.0);

        add(mainSplit);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
