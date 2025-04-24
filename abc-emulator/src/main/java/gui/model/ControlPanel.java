package src.main.java.gui.model;

import src.main.java.ABCMachine;
import src.main.java.base10.Base10Punch;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ControlPanel {
    private final ABCMachine abc;
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
    private JRadioButton L7;
    private JCheckBox L8;
    private JRadioButton MS;
    private JButton KAPrintButton;
    private JButton CAPrintButton;
    private JButton base10Punch;

    public ControlPanel() {
        abc = new ABCMachine();
        JList<String> cardList;          // visible list of card names
        DefaultListModel<String> cardModel;


        /**------------------SWITCHES---------------------------**/
        /*--------------------BANK SELECT---------------------*/
        SW1.addActionListener(e -> abc.toggleFieldSelect(0));
        SW2.addActionListener(e -> abc.toggleFieldSelect(1));
        SW3.addActionListener(e -> abc.toggleFieldSelect(2));
        SW4.addActionListener(e -> abc.toggleFieldSelect(3));
        SW5.addActionListener(e -> abc.toggleFieldSelect(4));
        SW6.addActionListener(e -> abc.toggleFieldSelect(5));

        /*----KA/CARD_READER & CARD/OVERDRAFT-----*/
        SW7.addActionListener(e -> abc.toggleSW7());
        SW8.addActionListener(e -> abc.toggleSW8());

        /*---------------CLEARS------------------*/
        SW11.addActionListener(e -> abc.clearCA());
        SW12.addActionListener(e -> abc.clearKA());

        /**---------------------PUSH-BUTTONS-----------------**/
        PB1.addActionListener(e -> abc.toggleAddSubMode());
        PB2.addActionListener(e -> {
            if (abc.sw7 != ABCMachine.Switch7.CARD_READER) {
                System.out.println("ASM's are connected to KA, change to Card Read");
            } else if (abc.sw8 != ABCMachine.Switch8.CARD) {
                System.out.println("Add-Subtract Relay is controlled by sensing Overdrafts, change to Card.");
            } else if (abc.addSubMode) {
                System.out.println("Please put machine into ADD mode");
            } else if (abc.fieldSelect.index == -1) {
                System.out.println("Please select a bank to load onto using SW1-SW6");
            } else if (true) {
                System.out.println("Please select a Base10 Card to read.");
            }

            String filepath = "";

            try {
                abc.base10Reader.readCard(filepath, abc.fieldSelect.index);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        });
        PB3.addActionListener(e -> abc.ca.transfer(abc.ka));
        //PB4
        //PB5
        PB6.addActionListener(e -> {
            if (abc.sw7 != ABCMachine.Switch7.KA) {
                System.out.println("ASM's are connected to Card Reader, change to KA");
                return;
            } else if (abc.zeroDetection == -1) {
                System.out.println("Coefficient is not selected. Please select one.");
                return;
            } else if (abc.sw8 != ABCMachine.Switch8.OVERDRAFT) {
                System.out.println("Add-Subtract Relay is controlled by Card, change to Overdrafts");
                return;
            }
            abc.adder.operation(abc.ca, abc.ka, abc.carryDrum, abc.addSubMode, abc.zeroDetection);
        });

        /**--------------------JUMPERS-------------------------**/
        /*-----------------ZERO DETECTION----------------------*/
        JCheckBox[] zd = {
                ZD0,  ZD1,  ZD2,  ZD3,  ZD4,  ZD5,  ZD6,  ZD7,  ZD8,  ZD9,
                ZD10, ZD11, ZD12, ZD13, ZD14, ZD15, ZD16, ZD17, ZD18, ZD19,
                ZD20, ZD21, ZD22, ZD23, ZD24, ZD25, ZD26, ZD27, ZD28, ZD29 };

        for (int i = 0; i < zd.length; i++) {
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
                SD0,  SD1,  SD2,  SD3,  SD4,  SD5,  SD6,  SD7,  SD8,  SD9,
                SD10, SD11, SD12, SD13, SD14, SD15, SD16, SD17, SD18, SD19,
                SD20, SD21, SD22, SD23, SD24, SD25, SD26, SD27, SD28, SD29 };

        for (int i = 0; i < sd.length; i++) {
            int idx = i;
            sd[i].addActionListener(e -> {
                JCheckBox src = (JCheckBox) e.getSource();
                if (src.isSelected()) {
                    for (JCheckBox other : sd) if (other != src) other.setSelected(false);
                    abc.setSignDetection(idx);
                } else {
                    abc.clearSignDetection();
                }
            });
        }

        /**--------------------EXTRAS------------------------**/
        base10Punch.addActionListener(e -> new Base10Punch());


    }
}
