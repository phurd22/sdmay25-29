package src.main.java.gui.model;

import src.main.java.ABCMachine;
import src.main.java.gui.model.CardSelect.CardSelect;
import src.main.java.gui.model.Odometer.OdometerPanel;
import src.main.java.mask.MaskReader;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import javax.swing.Timer;

/*
    Main user engagement.
    Java Swing Panel.
    Replicates the original ABC control panel.
 */
public class ControlPanel extends JPanel {
    private final ABCMachine abc;
    private final CardSelect selector;
    private JPanel contentPane;
    private JCheckBox SD0;
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
    private JCheckBox ZD0;
    private JCheckBox ZD1;
    private JCheckBox ZD2;
    private JCheckBox ZD3;
    private JCheckBox ZD4;
    private JCheckBox ZD5;
    private JCheckBox ZD6;
    private JCheckBox ZD7;
    private JCheckBox ZD8;
    private JCheckBox ZD9;
    private JCheckBox ZD10;
    private JCheckBox ZD11;
    private JCheckBox ZD12;
    private JCheckBox ZD13;
    private JCheckBox ZD14;
    private JCheckBox ZD15;
    private JCheckBox ZD16;
    private JCheckBox ZD17;
    private JCheckBox ZD18;
    private JCheckBox ZD19;
    private JCheckBox ZD20;
    private JCheckBox ZD21;
    private JCheckBox ZD22;
    private JCheckBox ZD23;
    private JCheckBox ZD24;
    private JCheckBox ZD25;
    private JCheckBox ZD26;
    private JCheckBox ZD27;
    private JCheckBox ZD28;
    private JCheckBox SD29;
    private JCheckBox ZD29;
    private JButton PB1;
    private JButton PB2;
    private JButton PB3;
    private JRadioButton SW1;
    private JRadioButton SW2;
    private JRadioButton SW3;
    private JRadioButton SW4;
    private JRadioButton SW5;
    private JRadioButton SW6;
    private JRadioButton SW7;
    private JRadioButton SW8;
    private JRadioButton SW9;
    private JRadioButton SW10;
    private JRadioButton SW11;
    private JRadioButton SW12;
    private JButton PB4;
    private JButton PB5;
    private JButton PB6;
    private JCheckBox L1;
    private JCheckBox L3;
    private JCheckBox L2;
    private JCheckBox L4;
    private JCheckBox L5;
    private JCheckBox L6;
    private JCheckBox L7;
    private JCheckBox L8;
    private JRadioButton MS;
    private JButton base10Punch;
    private JButton odoClearButton;
    private final OdometerPanel odometer;

    public ControlPanel(ABCMachine abc, CardSelect selector, OdometerPanel odo) {
        this.abc = abc;
        this.selector = selector;
        this.odometer = odo;

        setLayout(new BorderLayout());
        add(contentPane, BorderLayout.CENTER);

        /**---------------MACHINE START-------------**/
        MS.setToolTipText("Machine Start");
        MS.addActionListener(e -> {
            if (MS.isSelected()) {
                System.out.println("Machine Started");
                if (abc.addSubMode) {
                    L3.setSelected(true);
                    L2.setSelected(false);
                } else {
                    L3.setSelected(false);
                    L2.setSelected(true);
                }
            }
        });

        /**------------------SWITCHES---------------------------**/
        /*--------------------BANK SELECT---------------------*/
        SW1.setToolTipText("1-5 Coefficients");
        SW1.addActionListener(e -> abc.toggleFieldSelect(0));
        SW2.setToolTipText("6-10 Coefficients");
        SW2.addActionListener(e -> abc.toggleFieldSelect(5));
        SW3.setToolTipText("11-15 Coefficients");
        SW3.addActionListener(e -> abc.toggleFieldSelect(2));
        SW4.setToolTipText("16-20 Coefficients");
        SW4.addActionListener(e -> abc.toggleFieldSelect(3));
        SW5.setToolTipText("21-25 Coefficients");
        SW5.addActionListener(e -> abc.toggleFieldSelect(4));
        SW6.setToolTipText("26-30 Coefficients");
        SW6.addActionListener(e -> abc.toggleFieldSelect(5));

        /*----KA/CARD_READER & CARD/OVERDRAFT-----*/
        SW7.setToolTipText("KA/CARD_READER");
        SW7.addActionListener(e -> {
            abc.toggleSW7();
            System.out.println("SW7 is now connected to " + abc.sw7.toString());
        });
        SW8.setToolTipText("OVERDRAFT/CARD");
        SW8.addActionListener(e -> {
            abc.toggleSW8();
            System.out.println("SW8 is now controlled by " + abc.sw8.toString());
        });
        SW9.setToolTipText("Unused");
        SW10.setToolTipText("Powers of 10 brush");
        SW10.addActionListener(e -> {
            abc.toggleSW10();
            System.out.println("SW10 is now " + abc.sw10.toString());
        });
        /*---------------CLEARS------------------*/
        SW11.setToolTipText("Clear CA");
        SW11.addActionListener(e -> {
            if(SW11.isSelected()){
                abc.clearCA();
                System.out.println("CA Cleared");
            }
        });
        SW12.setToolTipText("Klear KA");
        SW12.addActionListener(e -> {
            if (SW12.isSelected()) {
                abc.clearKA();
                System.out.println("KA Kleared");
            }
        });

        /**---------------------PUSH-BUTTONS-----------------**/
        PB1.setToolTipText("Toggle ADD or SUB Mode");
        PB1.addActionListener(e -> {
            abc.toggleAddSubMode();
            if (abc.addSubMode) {
                L3.setSelected(true);
                L2.setSelected(false);
                System.out.println("Subtract Mode");
            } else {
                L3.setSelected(false);
                L2.setSelected(true);
                System.out.println("Addition Mode");
            }
        });
        PB2.setToolTipText("Base 10 Read");
        PB2.addActionListener(e -> {
            if (abc.sw7 != ABCMachine.Switch7.CARD_READER) {
                System.out.println("ASM's are connected to KA, change to Card Read");
                return;
            } else if (abc.sw8 != ABCMachine.Switch8.CARD) {
                System.out.println("Add-Subtract Relay is controlled by sensing Overdrafts, change to Card.");
                return;
            } else if (abc.addSubMode) {
                System.out.println("Please put machine into ADD mode");
                return;
            } else if (abc.fieldSelect.index == -1) {
                System.out.println("Please select a bank to load onto using SW1-SW6");
                return;
            } else if (!selector.selectedDecimalFile().isPresent()) {
                System.out.println("Please select a Base10 Card to read.");
                return;
            }
            selector.selectedDecimalFile().ifPresent(file -> {
                flashCheckBox(L4, 2000);
                try { abc.base10Reader.readCard(file.toString(), abc.fieldSelect.index); }
                catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Load error", JOptionPane.ERROR_MESSAGE);
                }
            });
            System.out.println("Base 10 Read!");
        });
        PB3.setToolTipText("Transfer CA to KA");
        PB3.addActionListener(e -> {
            abc.ca.transfer(abc.ka);
            System.out.println("CA transferred over to KA");
        });
        PB4.setToolTipText("Base 2 Punch");
        PB4.addActionListener(e -> {
            try {
                abc.base2Punch.punchCard(abc.ca.deepCopy());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println("Base 2 Punched");
        });
        PB5.setToolTipText("Base 2 Read");
        PB5.addActionListener(e -> {
            if (abc.sw7 != ABCMachine.Switch7.CARD_READER) {
                System.out.println("ASM's are connected to KA, change to Card Read");
                return;
            } else if (abc.sw8 != ABCMachine.Switch8.CARD) {
                System.out.println("Add-Subtract Relay is controlled by sensing Overdrafts, change to Card.");
                return;
            } else if (abc.addSubMode) {
                System.out.println("Please put machine into ADD mode");
                return;
            } else if (!selector.selectedBinaryFile().isPresent()) {
                System.out.println("Please select a Base2 Card to read.");
                return;
            }
            selector.selectedBinaryFile().ifPresent(file -> {
                flashCheckBox(L1, 2000);
                try {
                    abc.base2Reader.readCard(file.toAbsolutePath());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            System.out.println("Base 2 Read!");
        });
        PB6.setToolTipText("Start Computation");
        PB6.addActionListener(e -> {
            if (abc.sw7 == ABCMachine.Switch7.CARD_READER
                    && abc.sw8 == ABCMachine.Switch8.OVERDRAFT
                    && abc.sw10 == ABCMachine.Switch10.ON) {
                Path card = selector.selectedMaskFile().orElseThrow();
                L6.setSelected(true);
                new SwingWorker<>() {
                    @Override protected Void doInBackground() throws Exception {
                        new MaskReader(abc).readMask(card);
                        return null;
                    }
                }.execute();
                L6.setSelected(false);
                return;
            }
            if (abc.sw7 != ABCMachine.Switch7.KA) {
                System.out.println("ASM's are connected to Card Reader, change to KA");
                return;
            } else if (abc.zeroDetection == -1) {
                System.out.println("Coefficient is not selected. Please select one.");
                return;
            } else if (abc.sw8 != ABCMachine.Switch8.OVERDRAFT && abc.sw10 == ABCMachine.Switch10.ON) {
                System.out.println("Add-Subtract Relay is controlled by Card, change to Overdrafts");
                return;
            }
            flashCheckBox(L5, 2000);
            abc.adder.operation(abc.ca, abc.ka, abc.carryDrum, abc.addSubMode, abc.zeroDetection);
            System.out.println("Operation Complete");
        });

            /**--------------------JUMPERS-------------------------**/
            /*-----------------ZERO DETECTION----------------------*/
            JCheckBox[] zd = {
                    ZD0, ZD1, ZD2, ZD3, ZD4, ZD5, ZD6, ZD7, ZD8, ZD9,
                    ZD10, ZD11, ZD12, ZD13, ZD14, ZD15, ZD16, ZD17, ZD18, ZD19,
                    ZD20, ZD21, ZD22, ZD23, ZD24, ZD25, ZD26, ZD27, ZD28, ZD29};


            for (int i = 0; i < zd.length; ++i) {
                zd[i].setToolTipText("ZD" + (i+1));
                int idx = i;
                zd[i].addActionListener(e -> {
                    JCheckBox src = (JCheckBox) e.getSource();
                    if (src.isSelected()) {
                        for (JCheckBox other : zd) if (other != src) other.setSelected(false);
                        abc.setZeroDetection(idx);
                    } else {
                        abc.clearZeroDetection();
                    }
                });
            }
            /*------------------SIGN DETECTION------------------------*/
            JCheckBox[] sd = {
                    SD0, SD1, SD2, SD3, SD4, SD5, SD6, SD7, SD8, SD9,
                    SD10, SD11, SD12, SD13, SD14, SD15, SD16, SD17, SD18, SD19,
                    SD20, SD21, SD22, SD23, SD24, SD25, SD26, SD27, SD28, SD29};

        for (int i = 0; i < sd.length; ++i) {
            int idx = i;
            sd[i].setToolTipText("SD" + (i+1));
            sd[i].addActionListener(e -> {
                JCheckBox src = (JCheckBox) e.getSource();

                if (src.isSelected()) {
                    for (JCheckBox other : sd) {
                        if (other != src) other.setSelected(false);
                    }
                    abc.setSignDetection(idx);
                    boolean signBit = abc.ca.getData()[idx][0];
                    L7.setSelected(!signBit);
                    L8.setSelected(signBit);
                } else {
                    abc.clearSignDetection();
                    L7.setSelected(false);
                    L8.setSelected(false);
                }
            });
        }

        /**---------------------LIGHTS--------------------------**/
        L1.setToolTipText("Base-2 Read Operation");
        L2.setToolTipText("Add Operation");
        L3.setToolTipText("Subtract Operation");
        L4.setToolTipText("Read Base-10 Card Operation");
        L5.setToolTipText("Coefficient Elimination Operation");
        L6.setToolTipText("Decimal Output Operation");
        L7.setToolTipText("Positive Number Indicator");
        L8.setToolTipText("Negative Number Indicator");

    }
    public void flashCheckBox(JCheckBox box, int secs) {
        SwingUtilities.invokeLater(() -> {
            box.setSelected(true);
            new Timer(secs, e -> box.setSelected(false)) {{
                setRepeats(false);
                start();
            }};
        });
    }

}
