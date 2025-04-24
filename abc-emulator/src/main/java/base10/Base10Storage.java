package src.main.java.base10;

import src.main.java.ABCMachine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Base10Storage {
    public record Card(String name, Path file, boolean[][][] data) {}
    private static final Base10Storage INSTANCE = new Base10Storage();
    public static Base10Storage getInstance() { return INSTANCE; }

    private static final int SEGS = 5;
    private static final int ROWS = 10;
    private static final int COLS = 16;

    private final List<Card> cards = new ArrayList<>();
    private final Path rootDir;

    private Base10Storage() {
        rootDir = Paths.get("src", "main", "resources", "decimalcard");
        reload();
    }

    public synchronized void reload() {
        cards.clear();
        if (!Files.isDirectory(rootDir)) return;
        try (var stream = Files.list(rootDir).filter(p -> p.toString().endsWith(".csv"))) {
            stream.forEach(p -> {
                try { cards.add(parseFile(p)); }
                catch (IOException ex) { System.err.println("Bad card " + p + ": " + ex); }
            });
        } catch (IOException io) {
            System.err.println("Unable to read " + rootDir + ": " + io);
        }
    }

    public synchronized List<Card> list() { return List.copyOf(cards); }

    public synchronized boolean[][][] get(int index) {
        return (index < 0 || index >= cards.size()) ? null : cloneCard(cards.get(index).data);
    }

    public synchronized void add(Path file) throws IOException {
        cards.add(parseFile(file));
    }


    public synchronized Card delete(int index, boolean deleteFile) {
        if (index < 0 || index >= cards.size()) return null;
        Card removed = cards.remove(index);
        if (deleteFile) try { Files.deleteIfExists(removed.file()); } catch (IOException ignored) {}
        return removed;
    }

    public synchronized int size() { return cards.size(); }

    private Card parseFile(Path file) throws IOException {
        boolean[][][] bits = new boolean[SEGS][ROWS][COLS];

        // Support ClassLoader resources too (jar scenario)
        try (InputStream in = Files.exists(file) ?
                Files.newInputStream(file) :
                getClass().getClassLoader().getResourceAsStream("decimalcard/" + file.getFileName());
             BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(in)))) {

            String line; int row = 0;
            while ((line = br.readLine()) != null && row < ROWS) {
                String[] segParts = line.split("\\|");
                for (int s = 0; s < SEGS && s < segParts.length; ++s) {
                    String[] cols = segParts[s].split(",");
                    for (int c = 0; c < COLS && c < cols.length; ++c)
                        bits[s][row][c] = "1".equals(cols[c].trim());
                }
                row++;
            }
            if (row != ROWS) throw new IOException("Expected 10 rows, got " + row);
        }

        return new Card(file.getFileName().toString(), file, bits);
    }

    private static boolean[][][] cloneCard(boolean[][][] src) {
        return Arrays.stream(src).map(seg ->
                Arrays.stream(seg).map(boolean[]::clone).toArray(boolean[][]::new)
        ).toArray(boolean[][][]::new);
    }
}
