import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Sudoku {
    class Tiles extends JButton {
        int r;
        int c;

        Tiles(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }

    int boardWidth = 600;
    int boardHeight = 650;

    String[] puzzle1 = {
            "--74916-5",
            "2---6-3-9",
            "-----7-1-", // Fixed: now has 9 characters
            "-586----4",
            "--3----9-",
            "--62--187",
            "9-4-7---2",
            "67-83----",
            "81--45---"
    };

    String[] solution1 = {
            "387491625",
            "241568379",
            "569327418",
            "758619234",
            "123784596",
            "496253187",
            "934176852",
            "675832941",
            "812945763"
    };

    JFrame frame = new JFrame("Sudoku");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JPanel buttonsPanel = new JPanel();

    JButton numSelect = null;
    int errors = 0;
    int countDash = 0;

    Sudoku() {
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        textLabel.setFont(new Font("Arial", Font.BOLD, 30));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Sudoku");

        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(9, 9));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setUpTiles();
        frame.add(boardPanel, BorderLayout.CENTER);

        buttonsPanel.setLayout(new GridLayout(1, 9));
        setUpButtons();
        frame.add(buttonsPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    public void setUpTiles() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                Tiles tile = new Tiles(r, c);
                char tileChar = puzzle1[r].charAt(c);
                if (tileChar != '-') {
                    tile.setFont(new Font("Arial", Font.BOLD, 20));
                    tile.setText(String.valueOf(tileChar));
                    tile.setBackground(Color.lightGray);
                } else {
                    countDash++;
                    tile.setFont(new Font("Arial", Font.PLAIN, 20));
                    tile.setBackground(Color.white);
                }

                // Custom border for 3x3 boxes
                if ((r == 2 && c == 2) || (r == 2 && c == 5) || (r == 5 && c == 2) || (r == 5 && c == 5)) {
                    tile.setBorder(BorderFactory.createMatteBorder(1, 1, 5, 5, Color.BLACK));
                } else if (r == 2 || r == 5) {
                    tile.setBorder(BorderFactory.createMatteBorder(1, 1, 5, 1, Color.BLACK));
                } else if (c == 2 || c == 5) {
                    tile.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 5, Color.BLACK));
                } else {
                    tile.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                }

                tile.setFocusable(false);
                boardPanel.add(tile);

                tile.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        Tiles tile = (Tiles) e.getSource();
                        int r = tile.r;
                        int c = tile.c;

                        if (numSelect != null) {
                            if (!tile.getText().equals("")) {
                                return;
                            }
                            String numSelectedText = numSelect.getText();
                            String tileSol = String.valueOf(solution1[r].charAt(c));

                            if (!isValidMove(r, c, numSelectedText)) {
                                errors++;
                                textLabel.setText("Invalid move!! Errors: " + errors);
                                return;
                            }

                            if (tileSol.equals(numSelectedText)) {
                                tile.setText(numSelectedText);
                                countDash--;
                                if (countDash == 0) {
                                    JOptionPane.showMessageDialog(frame, "Sudoku Completed!");
                                    System.exit(0);
                                }
                            } else {
                                errors++;
                                textLabel.setText("Wrong Value!! Errors: " + errors);
                            }
                        }
                    }
                });
            }
        }
    }

    public void setUpButtons() {
        for (int i = 1; i < 10; i++) {
            JButton button = new JButton();
            button.setFont(new Font("Arial", Font.BOLD, 20));
            button.setBackground(Color.white);
            button.setFocusable(false);
            button.setText(String.valueOf(i));
            buttonsPanel.add(button);

            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JButton button = (JButton) e.getSource();
                    if (numSelect != null) {
                        numSelect.setBackground(Color.WHITE);
                    }
                    numSelect = button;
                    numSelect.setBackground(Color.LIGHT_GRAY);
                }
            });
        }
    }

    public boolean isValidMove(int row, int col, String num) {
        // Check row
        for (int c = 0; c < 9; c++) {
            Component comp = boardPanel.getComponent(row * 9 + c);
            if (comp instanceof JButton && ((JButton) comp).getText().equals(num)) {
                return false;
            }
        }

        // Check column
        for (int r = 0; r < 9; r++) {
            Component comp = boardPanel.getComponent(r * 9 + col);
            if (comp instanceof JButton && ((JButton) comp).getText().equals(num)) {
                return false;
            }
        }

        return true;
    }
}
