package src.main.java.mask;

import src.main.java.ABCMachine;

import java.io.*;
import java.nio.file.*;
import java.util.Objects;

/*
    Mask is used to help the Odometer add or subtract powers of 10 to tick.
    Masks are 1-5 for each bank select and respective coefficient index.

 */
public class MaskReader {
    private static final int SEGS= 5;
    private static final int ROWS = 10;
    private static final int COLS = 16;
    private final ABCMachine abc;


    public MaskReader(ABCMachine abc) {
        this.abc = abc;
    }

    // Help Odometer tick for each punch
    // This is the math behind Odometers ticking
    protected void onPunch(int band, int digit, int decade, int col){
        if (abc.ca.isZero(band)) return;
        boolean sign = abc.ca.getData()[band][0];        //CA's sign

        while (abc.ca.getData()[band][0] == sign) {
            abc.conv.convert(decade, digit, abc.addSubMode, band);
            abc.odometerPanel.tick(col);
            if (abc.ca.isZero(band)) return;
        }
        abc.toggleAddSubMode();
    }

    // Parse the csv
    // Same as base 10 read but with different calls
    public void readMask(Path file) throws IOException {

        try (InputStream in = Files.exists(file)
                ? Files.newInputStream(file)
                : getClass().getClassLoader()
                .getResourceAsStream("mask/"+file.getFileName());
             BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(in)))
        ){
            String line; int row = 0;
            while ((line = br.readLine()) != null && row < ROWS){
                String[] segParts = line.split("\\|");
                for(int s = 0; s< SEGS && s < segParts.length; ++s){
                    String[] cols = segParts[s].split(",");
                    for(int c = 1; c < COLS && c < cols.length; ++c){
                        if("1".equals(cols[c].trim())){
                            onPunch(s, row, COLS - 1 - c, c - 1);
                        }
                    }
                }
                ++row;
            }
            if(row!=ROWS) throw new IOException("Expected 10 rows, got " + row);
        }
    }
}
