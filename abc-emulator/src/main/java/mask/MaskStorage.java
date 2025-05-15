package src.main.java.mask;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;

public class MaskStorage {
    // Listeners
    public interface Listener { void cardAdded(Card c); }
    private final List<Listener> listeners = new CopyOnWriteArrayList<>();
    public void addListener(Listener l){ listeners.add(l); }
    public void removeListener(Listener l){ listeners.remove(l); }

    // Card record
    public record Card(String name, Path file){}

    // File names and location
    private static final Path ROOT = Paths.get("src","main","resources","mask");
    private static final Pattern NUMBER = Pattern.compile("mask\\d+\\.csv");
    private static final MaskStorage INSTANCE = new MaskStorage();
    public static MaskStorage getInstance(){ return INSTANCE; }

    // Catalog
    private final List<Card> cards = new ArrayList<>();

    private MaskStorage(){ reload();}

    // Populates tab
    public synchronized void reload(){
        cards.clear();
        if (!Files.isDirectory(ROOT)) return;
        try (var stream = Files.list(ROOT)){
            stream.filter(p -> NUMBER.matcher(p.getFileName().toString()).matches())
                    .sorted()
                    .forEach(p -> cards.add(new Card(p.getFileName().toString(), p)));
        }catch(IOException ex){ System.err.println("Cannot read mask dir: "+ ex);}
    }

    public synchronized void add(Path file){
        Card c = new Card(file.getFileName().toString(), file);
        cards.add(c);
        listeners.forEach(l -> l.cardAdded(c));
    }

    public synchronized List<Card> list(){ return List.copyOf(cards); }
}