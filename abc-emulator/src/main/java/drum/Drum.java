package src.main.java.drum;

public class Drum {
    protected final int bands;
    protected final int bits;
    protected final boolean[][] data;

    public Drum(int bands, int bits) {
        this.bands = bands;
        this.bits = bits;
        // Initialize all bits to false (0)
        this.data = new boolean[bands][bits];
    }

    public int getBandCount() {
        return bands;
    }

    public int getBitCount() {
        return bits;
    }

    public boolean getBit(int band, int position) {
        if (band < 0 || band >= bands || position < 0 || position >= bits) {
            throw new IndexOutOfBoundsException("Invalid band or position index");
        }
        return data[band][position];
    }

    public boolean[] getBand(int band) {
        return data[band];
    }

    public void setBit(int band, int position, boolean value) {
        if (band < 0 || band >= bands || position < 0 || position >= bits) {
            throw new IndexOutOfBoundsException("Invalid band or position index");
        }
        data[band][position] = value;
    }

    public void clear() {
        for (int i = 0; i < bands; i++) {
            for (int j = 0; j < bits; j++) {
                data[i][j] = false;
            }
        }
    }

    public boolean isZero(int band) {
        for (int i = 0; i < bits; ++i) {
            if (data[band][i]) return false;
        }
        return true;
    }

    public boolean[][] getData() {
        return data;
    }

    public void transfer(KA ka) {
        for (int band = 0; band < this.data.length; band++) {
            for (int bit = 0; bit < this.data[band].length; bit++) {
                ka.data[band][bit] = this.data[band][bit];
            }
        }
    }

    //LSB on the Right
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bands; i++) {
            sb.append(String.format("Band %2d: ", i));
            for (int j = bits - 1; j >= 0; j--) {
                sb.append(data[i][j] ? '1' : '0');
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public void printData() {
        final String RED = "\u001B[31m";
        final String GREEN = "\u001B[32m";
        final String RESET = "\u001B[0m";

        for (int i = 0; i < bands; i++) {
            for (int j = 0; j < bits; j++) {
                if (data[i][j]) {
                    // Print 1 in green
                    System.out.print(GREEN + "1" + RESET);
                } else {
                    // Print 0 in red
                    System.out.print(RED + "0" + RESET);
                }
            }
            System.out.println();
        }
    }
}