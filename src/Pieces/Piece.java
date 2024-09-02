package Pieces;

import main.Board;
import main.Move;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Piece implements Cloneable{

    public int col, row;
    public int xPos, yPos;
    public boolean isBlack;
    public String name;
    public int value;
    public Move promo;
    public int relativeValue;
    public boolean isFirstMove = true;
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

    @Override
    public Piece clone() {
        try {
            Piece cloned = (Piece) super.clone();
            cloned.board = this.board;

            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
    public boolean isValidMovement(int col, int row){
        return true;
    }

    public boolean check_moveCollision(int col, int row){
        return false;
    }

    public void paint(Graphics2D g2d){
        g2d.drawImage(sprite, xPos, yPos, null);
    }
}
