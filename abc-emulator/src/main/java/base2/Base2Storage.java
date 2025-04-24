package src.main.java.base2;

import src.main.java.drum.Drum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Base2Storage {
    private static final Base2Storage INSTANCE = new Base2Storage();
    public static Base2Storage getInstance() { return INSTANCE; }

    private final List<Drum> cards = new ArrayList<>();

    public synchronized void add(Drum card) { cards.add(card.deepCopy()); }

    public synchronized Drum delete(int idx) {
        return (idx < 0 || idx >= cards.size()) ? null : cards.remove(idx);
    }

    public synchronized List<Drum> list() { return Collections.unmodifiableList(cards); }

    public synchronized Drum get(int idx) {
        return (idx < 0 || idx >= cards.size()) ? null : cards.get(idx).deepCopy();
    }

    public synchronized int size() { return cards.size(); }

    private Base2Storage() { }
}
