package src.main.java.drum;

public class Drum {
    protected boolean[][] bands;                    // 2D array representing contacts
    protected int numBands;                         // Coefficients
    protected int numContactsPerBand;               // Bits

    // Constructor
    public Drum(int numBands, int numContactsPerBand) {
        this.numBands = numBands;
        this.numContactsPerBand = numContactsPerBand;
        bands = new boolean[numBands][numContactsPerBand];
    }

    // Get a state of specific contact
    public boolean getContact(int band, int position) { return bands[band][position]; }

    // Set a state of a specific contact
    public void setContact(int band, int position, boolean value) {
        bands[band][position] = value;
    }
}
