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
        this.setLayout(null);
        try {
            neon_logo = ImageIO.read(Objects.requireNonNull(getClass().getResource("/neon_logo.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        showBoardButton.setBounds(425, 575, 150, 50);
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
                if(fenString!=null){
                    boolean isValid = isValidFEN(fenString);
                    if (isValid) {
                        Board board = new Board(fenString);
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
        trainingButton.setBounds(425, 635, 150, 50);
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
                Board board = new Board(true,eval);
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
        this.add(showBoardButton);
        this.add(trainingButton);
    }

    public static boolean isValidFEN(String fen) {
        String[] fields = fen.split(" ");

        if (fields.length != 6) {
            return false;
        }

        // Validate piece placement
        if (!validatePiecePlacement(fields[0])) {
            return false;
        }

        // Validate active color
        if (!fields[1].equals("w") && !fields[1].equals("b")) {
            return false;
        }

        // Validate castling availability
        if (!fields[2].matches("[-KQkq]{1,4}")) {
            return false;
        }

        // Validate en passant target square
        if (!fields[3].equals("-") && !fields[3].matches("^[a-h][36]$")) {
            return false;
        }

        // Validate halfmove clock
        if (!fields[4].matches("\\d+")) {
            return false;
        }

        // Validate fullmove number
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(250, 250, 500, 500);
        if (neon_logo != null) {
            g.drawImage(neon_logo, 380, 330, 250, 250, this);
        }
    }
}
