package ui;

import model.Card;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class PunchCardPanel extends JPanel {
    private DefaultListModel<Card> cardModel;
    private JList<Card> cardList;
    private CardSelectionListener selectionListener;

    public PunchCardPanel() {
        setLayout(new BorderLayout());

        // Initialize the model.
        cardModel = new DefaultListModel<>();

        // Create a dummy thumbnail image (replace with your actual image).
        ImageIcon sampleIcon = new ImageIcon(new BufferedImage(50, 80, BufferedImage.TYPE_INT_RGB));

        // Add sample cards.
        cardModel.addElement(new Card(12, sampleIcon));
        cardModel.addElement(new Card(25, sampleIcon));
        cardModel.addElement(new Card(37, sampleIcon));

        // Create and configure the JList.
        cardList = new JList<>(cardModel);
        cardList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Card) {
                    Card card = (Card) value;
                    label.setText(card.toString());
                    label.setIcon(card.getThumbnail());
                }
                return label;
            }
        });
        cardList.setDragEnabled(true);
        cardList.setDropMode(DropMode.INSERT);

        // Listen for selection changes and notify the listener.
        cardList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && selectionListener != null) {
                    Card selectedCard = cardList.getSelectedValue();
                    if (selectedCard != null) {
                        selectionListener.cardSelected(selectedCard);
                    }
                }
            }
        });

        // Right-click context menu for deletion.
        cardList.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) showPopup(e);
            }
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) showPopup(e);
            }
            private void showPopup(MouseEvent e) {
                int index = cardList.locationToIndex(e.getPoint());
                if (index >= 0) {
                    cardList.setSelectedIndex(index);
                    JPopupMenu popup = new JPopupMenu();
                    JMenuItem deleteItem = new JMenuItem("Delete Card");
                    deleteItem.addActionListener(ae -> cardModel.remove(index));
                    popup.add(deleteItem);
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        // Place the list in the center of this panel.
        add(new JScrollPane(cardList), BorderLayout.CENTER);
    }

    // Setter for the selection listener.
    public void setCardSelectionListener(CardSelectionListener listener) {
        this.selectionListener = listener;
    }
}