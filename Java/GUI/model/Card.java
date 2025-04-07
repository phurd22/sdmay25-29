package model;

import javax.swing.*;

public class Card {
    private int value;
    private ImageIcon thumbnail;

    public Card(int value, ImageIcon thumbnail) {
        this.value = value;
        this.thumbnail = thumbnail;
    }

    public int getValue() {
        return value;
    }

    public String getBinaryValue() {
        return Integer.toBinaryString(value);
    }

    public ImageIcon getThumbnail() {
        return thumbnail;
    }

    @Override
    public String toString() {
        return "Decimal: " + value + " | Binary: " + getBinaryValue();
    }
}
