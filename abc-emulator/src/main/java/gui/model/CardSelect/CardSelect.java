package src.main.java.gui.model.CardSelect;

import src.main.java.base10.Base10Storage;
import src.main.java.base2.Base2Storage;
import src.main.java.mask.MaskStorage;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.util.Optional;

/*
    Lists all csv files (dcards, bcards, and masks).
    Lets user browse selection.
 */
public class CardSelect extends JPanel implements Base10Storage.Listener, Base2Storage.Listener, MaskStorage.Listener {
    // Stores the data
    private final DefaultListModel<String> decModel = new DefaultListModel<>();
    private final DefaultListModel<String> binModel = new DefaultListModel<>();
    private final DefaultListModel<String> maskModel = new DefaultListModel<>();

    // Displays data
    private final JList<String> decimalList = new JList<>(decModel);
    private final JList<String> binaryList  = new JList<>(binModel);
    private final JList<String> maskList    = new JList<>(maskModel);

    private final JTabbedPane tabs = new JTabbedPane();

    public CardSelect() {
        setLayout(new BorderLayout());

        // Populates the Decimal, Binary, and Mask tabs with files from respective folders under resources
        Base10Storage.getInstance().list().forEach(c -> decModel.addElement(c.name()));
        Base2Storage.getInstance().list().forEach(c -> binModel.addElement(c.name()));
        MaskStorage.getInstance().list().forEach(c -> maskModel.addElement(c.name()));

        // Hooks the GUI to the data
        Base10Storage.getInstance().addListener(this);
        Base2Storage.getInstance().addListener(this);
        MaskStorage.getInstance().addListener(this);

        // User can only select one card at a time
        decimalList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        binaryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        maskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tabs.addTab("Decimal", new JScrollPane(decimalList));
        tabs.addTab("Binary",  new JScrollPane(binaryList));
        tabs.addTab("Mask",    new JScrollPane(maskList));

        add(tabs, BorderLayout.CENTER);
        setPreferredSize(new Dimension(200, 350));
    }

    // Fetches the selected row index from the JList and converts into the corresponding Path
    public Optional<Path> selectedDecimalFile() {
        if (tabs.getSelectedIndex() != 0) return Optional.empty();
        int idx = decimalList.getSelectedIndex();
        return idx < 0 ? Optional.empty() : Optional.of(Base10Storage.getInstance().list().get(idx).file());
    }

    public Optional<Path> selectedBinaryFile() {
        if (tabs.getSelectedIndex() != 1) return Optional.empty();
        int idx = binaryList.getSelectedIndex();
        return idx < 0 ? Optional.empty() : Optional.of(Base2Storage.getInstance().list().get(idx).file());
    }

    public Optional<Path> selectedMaskFile() {
        if (tabs.getSelectedIndex() != 2) return Optional.empty();
        int idx = maskList.getSelectedIndex();
        return idx < 0 ? Optional.empty()
                : Optional.of(MaskStorage.getInstance().list().get(idx).file());
    }

    // Updates the GUI lists when new cards are added
    @Override public void cardAdded(Base10Storage.Card c) {
        SwingUtilities.invokeLater(() -> decModel.addElement(c.name()));
    }
    @Override public void cardAdded(Base2Storage.Card c){
        SwingUtilities.invokeLater(() -> binModel.addElement(c.name()));
    }
    @Override
    public void cardAdded(MaskStorage.Card c) {
        SwingUtilities.invokeLater(() -> maskModel.addElement(c.name()));
    }
}
