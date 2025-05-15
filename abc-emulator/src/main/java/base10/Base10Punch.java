package src.main.java.base10;

import src.main.java.ABCMachine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Base10Punch extends JFrame {
    private ABCMachine abc;
    // Directories
    private static final Path RES_ROOT = Paths.get("src", "main", "resources");

    private static final Path DECIMAL_DIR  = RES_ROOT.resolve("decimalcard");

    // Card Units
    private static final int ROWS = 10;
    private static final int SEGMENTS = 5;
    private static final int COLS_PER_SEG = 16;
    private static final int TOTAL_COLS = SEGMENTS * COLS_PER_SEG;

    private final boolean[][] punched = new boolean[ROWS][TOTAL_COLS];
    private int currentCol = 0;

    private static final int CELL_W = 12;
    private static final int CELL_H = 20;
    private static final int GAP = 2;         // Tiny space around each cell
    private static final int SEG_GAP = 8;     // Bold gap between segments

    private final CardCanvas canvas = new CardCanvas();

    // For Un-Do-ing
    private static final class Action { final int col; final Integer digit; Action(int c,Integer d){col=c;digit=d;}}
    private final java.util.Deque<Action> history = new java.util.ArrayDeque<>();

    private final JTextField filenameField = new JTextField(14);   // Shows default name
    private final JButton saveBtn = new JButton("Save");

    public Base10Punch(ABCMachine abc) {
        super("Punch Base-10 Card");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        // UI
        setLayout(new BorderLayout());
        add(canvas, BorderLayout.CENTER);

        // Footer with filename and save button
        JPanel south = new JPanel(new FlowLayout(FlowLayout.LEFT));
        south.add(new JLabel("Filename:"));
        south.add(filenameField);
        south.add(saveBtn);
        add(south, BorderLayout.SOUTH);

        filenameField.setText(nextDefaultFilename());

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        this.abc = abc;

        setFocusable(true);
        requestFocusInWindow();

        // User input keys
        KeyAdapter punchKeys = new KeyAdapter() {
            // Arrow keys need KeyCode
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    history.push(new Action(currentCol, null));   // record skip
                    currentCol++;
                    canvas.repaint();
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    if (history.isEmpty()) { beep(); return; }

                    Action act = history.pop();
                    currentCol = act.col;            // step back
                    if (act.digit != null)
                        punched[act.digit][currentCol] = false;

                    canvas.repaint();
                }
            }

            // Chars need KeyChar
            @Override
            public void keyTyped(KeyEvent e) {
                char ch = e.getKeyChar();
                if (!Character.isDigit(ch)) {            // allow only 0-9
                    e.consume();
                    return;
                }

                int digit = ch - '0';
                punched[digit][currentCol] = true;  // record punch
                history.push(new Action(currentCol, digit));
                currentCol++;
                canvas.repaint();
            }
        };

        //
        ActionListener saver = e -> saveCard(filenameField.getText().trim());
        saveBtn.addActionListener(saver);
        filenameField.addActionListener(saver);

        // For punchcard focus
        canvas.setFocusable(true);
        SwingUtilities.invokeLater(canvas::requestFocusInWindow);

        canvas.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mousePressed(java.awt.event.MouseEvent e) {
                canvas.requestFocusInWindow();
            }
        });

        canvas.addKeyListener(punchKeys);
        filenameField.addKeyListener(punchKeys);
    }

    private final class CardCanvas extends JPanel {
        // Init the punch card
        CardCanvas() {
            setPreferredSize(new Dimension(
                    TOTAL_COLS * (CELL_W + GAP) + (SEGMENTS - 1) * SEG_GAP + 20,
                    ROWS * (CELL_H + GAP) + GAP
            ));
            setBackground(Color.WHITE);
        }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            // Highlights current column
            if (currentCol < TOTAL_COLS) {
                int x = colToX(currentCol);
                g2.setColor(new Color(255, 255, 0, 60));     // Yellow
                g2.fillRect(x, 0, CELL_W, getHeight());
            }

            // Draws punches
            for (int r = 0; r < ROWS; ++r) {
                for (int c = 0; c < TOTAL_COLS; ++c) {
                    int x = c * (CELL_W + GAP) + (c / COLS_PER_SEG) * SEG_GAP;
                    int y = r * (CELL_H + GAP);
                    g2.setColor(Color.LIGHT_GRAY);
                    g2.drawRect(x, y, CELL_W, CELL_H);
                    if (punched[r][c]) {
                        g2.setColor(Color.BLACK);
                        g2.fillRect(x + 1, y + 1, CELL_W - 1, CELL_H - 1);
                    }
                }
            }

            // Solid line after each segment
            g2.setColor(Color.DARK_GRAY);
            g2.setStroke(new BasicStroke(3));
            for (int s = 1; s <= SEGMENTS; ++s) {
                int x = s * COLS_PER_SEG * (CELL_W + GAP) + (s - 1) * SEG_GAP - GAP / 2;
                g2.drawLine(x, 0, x, getHeight());
            }

            // Row index labels
            g2.setColor(Color.GRAY);
            g2.setFont(getFont().deriveFont(Font.PLAIN, 12f));
            FontMetrics fm = g2.getFontMetrics();
            for (int r = 0; r < ROWS; ++r) {
                int baseY = r * (CELL_H + GAP);
                int textY = baseY + (CELL_H + fm.getAscent() - fm.getDescent()) / 2;
                int textX = getWidth() - 14;
                g2.drawString(String.valueOf(r), textX, textY);
            }
        }

        private int colToX(int col) {
            return col * (CELL_W + GAP) + (col / COLS_PER_SEG) * SEG_GAP;
        }
    }

    // Saves card to Base10Storage
    private void saveCard(String rawName) {
        if (rawName.isEmpty()) { beep(); return; }

        String fileName = rawName.toLowerCase().endsWith(".csv") ? rawName : rawName + ".csv";
        Path target = DECIMAL_DIR.resolve(fileName);
        File file = target.toFile();

        if (file.exists()) {
            int ans = JOptionPane.showConfirmDialog(this,
                    "Overwrite existing file \"" + fileName + "\"?",
                    "Confirm overwrite", JOptionPane.OK_CANCEL_OPTION);
            if (ans != JOptionPane.OK_OPTION) return;
        }

        // Write the card
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (int row = 0; row < ROWS; ++row) {
                StringBuilder line = new StringBuilder();
                for (int seg = 0; seg < SEGMENTS; ++seg) {
                    for (int col = 0; col < COLS_PER_SEG; ++col) {
                        int globalCol = seg * COLS_PER_SEG + col;
                        line.append(punched[row][globalCol] ? '1' : ' ');
                        if (col < COLS_PER_SEG - 1) line.append(',');
                    }
                    line.append('|');
                }
                line.append(' ').append(row);
                bw.write(line.toString());
                bw.newLine();
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Unable to save card:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            abc.base10Storage.add(target);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JOptionPane.showMessageDialog(this, "Card saved:\n" + target,
                "Saved", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    // Creates dcardXX.csv with proper index
    private String nextDefaultFilename() {
        try {
            int next = Files.list(DECIMAL_DIR)
                    .filter(p -> p.getFileName().toString().matches("dcard\\d{2}\\.csv"))
                    .mapToInt(p -> Integer.parseInt(p.getFileName().toString().substring(5, 7)))
                    .max().orElse(-1) + 1;
            return String.format("dcard%02d.csv", next);
        } catch (IOException e) {
            return "dcard00.csv";
        }
    }

    private static void beep() { Toolkit.getDefaultToolkit().beep(); }
}
