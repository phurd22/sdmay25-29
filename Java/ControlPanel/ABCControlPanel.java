import javax.swing.*;

public class ABCControlPanel {
    private JPanel contentPane;
    private JButton PB1;
    private JButton PB2;
    private JButton PB3;
    private JCheckBox L1;
    private JCheckBox L2;
    private JButton PB6;
    private JButton PB5;
    private JButton PB4;
    private JRadioButton SW4;
    private JRadioButton SW3;
    private JRadioButton SW2;
    private JRadioButton SW1;
    private JRadioButton SW5;
    private JRadioButton SW6;
    private JRadioButton SW10;
    private JRadioButton SW9;
    private JRadioButton SW8;
    private JRadioButton SW7;
    private JRadioButton SW11;
    private JRadioButton SW12;
    private JCheckBox L3;
    private JCheckBox L4;
    private JCheckBox L5;
    private JCheckBox L6;
    private JCheckBox L7;
    private JCheckBox L8;
    private JRadioButton MS;
    private JRadioButton VS;
    private JSlider ZD;
    private JSlider SD;

    private MachineController machineController;

    public class MachineController {
        private boolean additionMode;

        public MachineController() {
            additionMode = true;
        }
    }

}
