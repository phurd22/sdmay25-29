package src.main.java.base10;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Base10Punch extends JFrame {
    // Directories
    private static final Path RES_ROOT =
            Paths.get("src", "main", "resources");

    private static final Path DECIMAL_DIR  = RES_ROOT.resolve("decimalcard");
    private static final Path MASK_DIR     = RES_ROOT.resolve("mask");

    // CARD UNITS
    private static final int ROWS = 10;
    private static final int SEGMENTS = 5;
    private static final int COLS_PER_SEG = 16;
    private static final int TOTAL_COLS = SEGMENTS * COLS_PER_SEG;

    private final boolean[][] punched = new boolean[ROWS][TOTAL_COLS];
    private int currentCol = 0;

    private static final int CELL_W = 12;     // tweak to taste
    private static final int CELL_H = 20;
    private static final int GAP = 2;         // tiny space around each cell
    private static final int SEG_GAP = 8;     // bold gap between segments

    private final CardCanvas canvas = new CardCanvas();

    // For Un-Do-ing
    private static final class Action { final int col; final Integer digit; Action(int c,Integer d){col=c;digit=d;}}
    private final java.util.Deque<Action> history = new java.util.ArrayDeque<>();

    public Base10Punch() {
        super("Punch Base-10 Card");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        add(canvas);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        addKeyListener(new KeyAdapter() {
            @Override public void keyTyped(KeyEvent e) {
                char ch = e.getKeyChar();

                /* ---- save & quit ---- */
                if (ch == '\n') { saveAndClose(); return; }

                if (currentCol >= TOTAL_COLS) {
                    Toolkit.getDefaultToolkit().beep();
                    return;
                }

                /* --- skip column '.' --- */
                if (ch == '.') {
                    history.push(new Action(currentCol, null));   // record skip
                    currentCol++;
                    canvas.repaint();
                    return;
                }

                /* --- punch digit --- */
                if (Character.isDigit(ch)) {
                    int digit = ch - '0';
                    punched[digit][currentCol] = true;
                    history.push(new Action(currentCol, digit));  // record punch
                    currentCol++;
                    canvas.repaint();
                    return;
                }

                /* --- undo last action ( 'u' or 'U' ) --- */
                if (ch == 'u' || ch == 'U') {
                    if (history.isEmpty()) { beep(); return; }

                    Action act = history.pop();
                    currentCol = act.col;                      // step back
                    if (act.digit != null)                     // was a punch – unpunch it
                        punched[act.digit][currentCol] = false;

                    canvas.repaint();
                    return;
                }
            }
        });

    }

    private final class CardCanvas extends JPanel {
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

            /* ---- highlight current column ---- */
            if (currentCol < TOTAL_COLS) {
                int x = colToX(currentCol);
                g2.setColor(new Color(255, 255, 0, 60));     // translucent yellow
                g2.fillRect(x, 0, CELL_W, getHeight());
            }

            /* draw cells & punches */
            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < TOTAL_COLS; c++) {
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

            /* bold lines after each segment */
            g2.setColor(Color.DARK_GRAY);
            g2.setStroke(new BasicStroke(3));
            for (int s = 1; s <= SEGMENTS; s++) {
                int x = s * COLS_PER_SEG * (CELL_W + GAP) + (s - 1) * SEG_GAP - GAP / 2;
                g2.drawLine(x, 0, x, getHeight());
            }

            /* ---- row index labels ---- */
            g2.setColor(Color.GRAY);
            g2.setFont(getFont().deriveFont(Font.PLAIN, 12f));
            FontMetrics fm = g2.getFontMetrics();
            for (int r = 0; r < ROWS; r++) {
                int baseY   = r * (CELL_H + GAP);          // top of this row
                int textY   = baseY + (CELL_H + fm.getAscent() - fm.getDescent()) / 2;  // vertical centre
                int textX   = getWidth() - 14;             // just right of last segment line
                g2.drawString(String.valueOf(r), textX, textY);
            }
        }

        private int colToX(int col) {
            return col * (CELL_W + GAP) + (col / COLS_PER_SEG) * SEG_GAP;
        }
    }

    private void saveAndClose() {
        /* ensure directory exists */
        DECIMAL_DIR.toFile().mkdirs();


        String defaultName = "dcard" + System.currentTimeMillis() + ".csv";
        File defaultFile   = DECIMAL_DIR.resolve(defaultName).toFile();

        JFileChooser fc = new JFileChooser(DECIMAL_DIR.toFile());
        fc.setDialogTitle("Save punched card");
        fc.setSelectedFile(defaultFile);

        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;                                    // cancelled
        }

        File file = fc.getSelectedFile();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {

            for (int row = 0; row < ROWS; row++) {
                StringBuilder line = new StringBuilder();

                for (int seg = 0; seg < SEGMENTS; seg++) {
                    for (int col = 0; col < COLS_PER_SEG; col++) {
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

            JOptionPane.showMessageDialog(this,
                    "Card saved:\n" + file.getAbsolutePath(),
                    "Saved", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Unable to save card:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        dispose();
    }

    /* convenience */
    private static void beep() { Toolkit.getDefaultToolkit().beep(); }
}
