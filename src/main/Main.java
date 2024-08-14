package main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Neon");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.black);
        frame.setLayout(new GridBagLayout());
        frame.setMinimumSize(new Dimension(1000, 1000));
        frame.setLocationRelativeTo(null);
        Image originalIcon = null;
        try {
            originalIcon = ImageIO.read(Objects.requireNonNull(Main.class.getResource("/icon.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        BufferedImage icon = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = icon.createGraphics();
        g2d.drawImage(originalIcon,0,0,null);
        g2d.dispose();
        frame.setIconImage(icon);
        Menu menu = new Menu(frame);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        frame.add(menu, gbc);
        frame.pack();
        frame.setVisible(true);
    }
}