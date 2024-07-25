package Pieces;

import main.Board;

import java.awt.image.BufferedImage;

public class Rook extends Piece{
    public Rook(Board board, int col, int row, boolean isBlack) {
        super(board);
        this.col = col;
        this.row = row;
        this.xPos = col * board.tile_size;
        this.yPos = row * board.tile_size;
        this.isBlack = isBlack;
        this.name = "Rook";

        this.sprite = sheet.getSubimage(4 * sheetscale, isBlack ? sheetscale : 0, sheetscale, sheetscale).getScaledInstance(board.tile_size,board.tile_size, BufferedImage.SCALE_SMOOTH);
    }
}
