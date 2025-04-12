import javax.swing.*;
import java.awt.*;
import java.math.BigInteger;
import java.util.Arrays;

public class OdometerWindow extends JFrame {
    private static final int ROWS = 6;
    private static final int COLS = 15;
    private JLabel[][] digitLabels = new JLabel[ROWS][COLS];
    private enum ColumnType {
        EVEN, ODD, LAST
    }
    String[][] odometerStrings = {
            {"0","0","0","0","0","0","0","0","0","0","0","0","0","0","0"},
            {"0","9","0","9","0","9","0","9","0","9","0","9","0","9","1"},
            {"1","8","1","8","1","8","1","8","1","8","1","8","1","8","2"},
            {"2","7","2","7","2","7","2","7","2","7","2","7","2","7","3"},
            {"3","6","3","6","3","6","3","6","3","6","3","6","3","6","4"},
            {"4","5","4","5","4","5","4","5","4","5","4","5","4","5","5"},
            {"5","4","5","4","5","4","5","4","5","4","5","4","5","4","6"},
            {"6","3","6","3","6","3","6","3","6","3","6","3","6","3","7"},
            {"7","2","7","2","7","2","7","2","7","2","7","2","7","2","8"},
            {"8","1","8","1","8","1","8","1","8","1","8","1","8","1","9"},
            {"9","0","9","0","9","0","9","0","9","0","9","0","9","0","0"}
    };
    private ColumnType[] columnTypes = new ColumnType[COLS];
    private BigInteger currentValue;
    private boolean additionMode;
    public OdometerWindow(BigInteger startValue, boolean addMode) {
        super("Mechanical Odometer");

        this.currentValue = startValue;
        this.additionMode = addMode;

        // 1) Decide the type for each column, from left=col0 => decade=14, to right=col14 => decade=0
        //    decade = 14 - col
        for (int col = 0; col < COLS; col++) {
            int decade = 14 - col;
            if (decade == 0) {
                columnTypes[col] = ColumnType.LAST;
            } else if (decade % 2 == 0) {
                columnTypes[col] = ColumnType.EVEN;
            } else {
                columnTypes[col] = ColumnType.ODD;
            }
        }

        // Build the GUI layout
        setLayout(new GridLayout(ROWS, COLS));
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                digitLabels[row][col] = new JLabel(
                        odometerStrings[row][col],
                        SwingConstants.CENTER
                );
                digitLabels[row][col].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                add(digitLabels[row][col]);
            }
        }

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    public void tick(int column) {
        System.out.println("Ticking column: " + column);
        String[] copy = new String[11];
        for (int i = 0; i < 11; ++i) {
            copy[i] = odometerStrings[i][column];
        }
        for (int i = 0; i < 11; ++i) {
            if (i == 10) {
                odometerStrings[i][column] = copy[0];
            } else {
                odometerStrings[i][column] = copy[i+1];
            }
        }

        for (int row = 0; row < ROWS; row++) {
            digitLabels[row][column].setText(odometerStrings[row][column]);
        }
        // Sleep so user can watch each step
        try {
            Thread.sleep(250);  //
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void showTick(){
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                for (int col = 0; col < COLS; col++) {
                    int decade = 14 - col;  // 10^decade
                    BigInteger power = BigInteger.TEN.pow(decade);
                    BigInteger sign = currentValue;
                    // Keep “adding” or “subtracting” until sign changes or we hit zero
                    while (sign.signum() == currentValue.signum()) {

                        // Perform add/sub
                        if (additionMode) {
                            currentValue = currentValue.add(power);
                        } else {

                            currentValue = currentValue.subtract(power);
                        }
                        tick(col);

                        if (sign.signum() != currentValue.signum()) {
                            additionMode = !additionMode;
                            break;
                        }
                    }
                    // If we’re at zero, we’re done.
                    if (currentValue.equals(BigInteger.ZERO)) {
                        break;
                    }
                }
                return null;
            }
            @Override
            protected void done() {
                // Optionally do something when the odometer is fully done.
                System.out.println("Odometer ticking complete.");
            }
        };
        worker.execute();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            //POSITIVE NUMBER
            //OdometerWindow window = new OdometerWindow(BigInteger.valueOf(28), false);

            //NEGATIVE NUMBER
            OdometerWindow window = new OdometerWindow(BigInteger.valueOf(300), false);

            window.setVisible(true);
            window.showTick();
        });
    }
}
