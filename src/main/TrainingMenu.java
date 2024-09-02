package main;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Objects;

public class TrainingMenu extends JPanel {
    private Image neon_logo;

    public TrainingMenu(JFrame frame) {
        this.setPreferredSize(new Dimension(1000, 1000));
        GridBagLayout gridBagLayout = new GridBagLayout();
        this.setLayout(gridBagLayout);

        try {
            neon_logo = ImageIO.read(Objects.requireNonNull(getClass().getResource("/neon_logo.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JLabel imageLabel = new JLabel(new ImageIcon(neon_logo));

        JButton showBoardButton = new JButton("Custom positions") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                if (getModel().isRollover()) {
                    g2d.setColor(Color.LIGHT_GRAY);
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g2d);
            }

            @Override
            protected void paintBorder(Graphics g) {
                g.setColor(Color.BLACK);
                g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            }
        };

        showBoardButton.setFont(new Font("Arial", Font.BOLD, 16));
        showBoardButton.setForeground(Color.BLACK);
        showBoardButton.setBackground(Color.GRAY);
        showBoardButton.setOpaque(false);
        showBoardButton.setBorder(BorderFactory.createEmptyBorder());
        showBoardButton.setFocusPainted(false);
        showBoardButton.setToolTipText("Start a custom game");

        showBoardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fenString = JOptionPane.showInputDialog("Enter a FEN string");
                if (fenString != null) {
                    boolean isValid = isValidFEN(fenString);
                    if (isValid) {
                        JLabel eval = new JLabel("Score: 0");
                        Board board = new Board(fenString,eval);
                        frame.getContentPane().removeAll();
                        frame.add(board);
                        frame.revalidate();
                        frame.repaint();
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid FEN string.");
                    }
                }
            }
        });

        JButton trainingButton = new JButton("New game vsBot") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                if (getModel().isRollover()) {
                    g2d.setColor(Color.LIGHT_GRAY);
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g2d);
            }

            @Override
            protected void paintBorder(Graphics g) {
                g.setColor(Color.BLACK);
                g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            }
        };

        trainingButton.setFont(new Font("Arial", Font.BOLD, 16));
        trainingButton.setForeground(Color.BLACK);
        trainingButton.setBackground(Color.GRAY);
        trainingButton.setOpaque(false);
        trainingButton.setBorder(BorderFactory.createEmptyBorder());
        trainingButton.setFocusPainted(false);
        trainingButton.setToolTipText("Click for training options");

        trainingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JLabel eval = new JLabel("Score: 0");
                Board board = new Board(true, eval);
                GridBagConstraints gbc = new GridBagConstraints();
                frame.getContentPane().removeAll();

                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.anchor = GridBagConstraints.NORTHWEST;
                frame.add(eval, gbc);
                gbc.gridx = 1;
                gbc.gridy = 1;
                gbc.gridwidth = GridBagConstraints.REMAINDER;
                gbc.gridheight = GridBagConstraints.REMAINDER;
                gbc.anchor = GridBagConstraints.CENTER;
                frame.add(board, gbc);

                frame.revalidate();
                frame.repaint();
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 20, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(imageLabel, gbc);

        gbc.gridwidth = 2;
        gbc.gridy = 1;
        gbc.insets = new Insets(20, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.ipadx = 50;
        gbc.ipady = 20;
        this.add(showBoardButton, gbc);
        gbc.insets = new Insets(20, 10, 100, 10);
        gbc.gridy = 2;
        this.add(trainingButton, gbc);
    }

    public static boolean isValidFEN(String fen) {
        String[] fields = fen.split(" ");

        if (fields.length != 6) {
            return false;
        }

        if (!validatePiecePlacement(fields[0])) {
            return false;
        }

        if (!fields[1].equals("w") && !fields[1].equals("b")) {
            return false;
        }

        if (!fields[2].matches("[-KQkq]{1,4}")) {
            return false;
        }

        if (!fields[3].equals("-") && !fields[3].matches("^[a-h][36]$")) {
            return false;
        }

        if (!fields[4].matches("\\d+")) {
            return false;
        }

        if (!fields[5].matches("\\d+") || Integer.parseInt(fields[5]) <= 0) {
            return false;
        }

        return true;
    }

    private static boolean validatePiecePlacement(String piecePlacement) {
        String[] rows = piecePlacement.split("/");
        if (rows.length != 8) {
            return false;
        }

        for (String row : rows) {
            int count = 0;
            for (char c : row.toCharArray()) {
                if (Character.isDigit(c)) {
                    count += Character.getNumericValue(c);
                } else if ("prnbqkPRNBQK".indexOf(c) != -1) {
                    count++;
                } else {
                    return false;
                }
            }
            if (count != 8) {
                return false;
            }
        }

        return true;
    }

}