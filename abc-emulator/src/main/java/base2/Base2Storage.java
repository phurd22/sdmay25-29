package src.main.java.base2;

import src.main.java.drum.Drum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;

public class Base2Storage {
    // The path to the binary cards
    private static final Path ROOT = Paths.get("src","main","resources","binarycard");
    // Each are saved as bcardXX.csv
    private static final Pattern NUMBER = Pattern.compile("bcard(\\d{2})\\.csv");

    // Shows the list of binarycard folder on startup
    private Base2Storage() { reload();}

    private void reload() {
        // Clear the memory and checks for correct path
        cards.clear();
        if (!Files.isDirectory(ROOT)) {
            return;
        }
        // Streams the cards under binarycard folder and checks if named bcardXX.csv
        try (var stream = Files.list(ROOT)) {
            stream.filter(p -> NUMBER.matcher(p.getFileName().toString()).matches()).sorted().forEach(p -> {
                try {
                    Drum d = Base2Reader.readCard(p);
                    cards.add(new Card(p.getFileName().toString(), p, d));
                } catch (IOException ex) {
                    System.err.println("Bad binary card " + p + ": " + ex);
                }
            });
        } catch (IOException ex) {
            System.err.println("Cannot scan " + ROOT + ": " + ex);
        }
    }

    // Observer List
    private final List<Listener> listeners = new CopyOnWriteArrayList<>();
    public void addListener(Listener l){ listeners.add(l); }
    public void removeListener(Listener l){ listeners.remove(l); }

    // Data object
    public record Card(String name, Path file, Drum drum) { }

    // Updates the window
    public interface Listener { void cardAdded(Card c); }

    private static final Base2Storage INSTANCE = new Base2Storage();
    public static Base2Storage getInstance() { return INSTANCE; }

    // In-memory
    private final List<Card> cards = new ArrayList<>();

    // Called by Base2Punch, deepCopies and calls cardAdded to update
    public synchronized void add(Path file, Drum drum) {
        Card c = new Card(file.getFileName().toString(), file, drum.deepCopy());
        cards.add(c);
        listeners.forEach(l -> l.cardAdded(c));
    }

    // Read-only of list
    public synchronized List<Card> list() { return List.copyOf(cards); }

}
