package Pieces;

import main.Board;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Piece {

    public int col, row;
    public int xPos, yPos;

    public boolean isBlack;
    public String name;
    public int value;
    Image sprite;
    Board board;
    BufferedImage sheet;
    {
        try {
            sheet = ImageIO.read(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("pieces.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    protected int sheetscale = sheet.getWidth()/6;
    public Piece(Board board){
        this.board = board;
    }

    public void paint(Graphics2D g2d){
        g2d.drawImage(sprite, xPos, yPos, null);
    }
}
