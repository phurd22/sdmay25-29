package src.main.java;

import src.main.java.gui.model.ABCGUI;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("ABCGUI started");
        SwingUtilities.invokeLater(ABCGUI::new);
    }
}