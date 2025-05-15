package src.main.java.base2;

import src.main.java.ABCMachine;
import src.main.java.drum.Drum;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Base2Punch {
    // All punched binary cards land in the binarycard folder
    private static final Path BIN_DIR = Paths.get("src", "main", "resources", "binarycard");

    // Saves as bcardXX.csv
    private static final Pattern NUMBER = Pattern.compile("bcard(\\d+)\\.csv");
    private final ABCMachine abc;
    public Base2Punch(ABCMachine abc) {
        this.abc = abc;
    }

    public Path punchCard(Drum drum) throws IOException {
        // Make sure the Drum is modified
        if (drum.getBandCount() != 30 || drum.getBitCount() != 50) {
            throw new IllegalArgumentException("Expected 30×50 drum, got " + drum.getBandCount() + "×" + drum.getBitCount());
        }

        // Output folder
        Files.createDirectories(BIN_DIR);

        // Figure out the next filename
        int next = Files.list(BIN_DIR)
                .map(p -> NUMBER.matcher(p.getFileName().toString()))
                .filter(Matcher::matches)
                .map(m -> Integer.parseInt(m.group(1)))
                .max(Comparator.naturalOrder())
                .orElse(-1) + 1;

        // Build the path
        Path out = BIN_DIR.resolve(String.format("bcard%02d.csv", next));

        // Writes the Drum
        try (BufferedWriter bw = Files.newBufferedWriter(out)) {

            for (int row = 0; row < drum.getBandCount(); ++row) {
                StringBuilder line = new StringBuilder();

                // LSB column first, MSB last
                for (int col = 0; col < drum.getBitCount(); ++col) {
                    line.append(drum.getBit(row, col) ? '1' : '0');
                    if (col < drum.getBitCount() - 1) line.append(',');
                }
                bw.write(line.toString());
                bw.newLine();
            }
        }
        // Add card to the memory storage
        abc.base2Storage.add(out, drum);
        return out;
    }
}
