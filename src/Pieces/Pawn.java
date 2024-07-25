package Pieces;

import main.Board;

import java.awt.image.BufferedImage;

public class Pawn extends Piece{
    public Pawn(Board board, int col, int row, boolean isBlack) {
        super(board);
        this.col = col;
        this.row = row;
        this.xPos = col * board.tile_size;
        this.yPos = row * board.tile_size;
        this.isBlack = isBlack;
        this.name = "Pawn";
        this.sprite = sheet.getSubimage(5 * sheetscale, isBlack ? sheetscale : 0, sheetscale, sheetscale).getScaledInstance(board.tile_size,board.tile_size, BufferedImage.SCALE_SMOOTH);
    }
    public boolean isValidMovement(int col, int row){
        int teamIndex = isBlack ? -1 : 1;

        if(this.col == col && row == this.row - teamIndex && board.getPiece(col,row)==null){
            return true;
        }
        if(isFirstMove&&this.col == col && row == this.row - teamIndex * 2 && board.getPiece(col,row)==null && board.getPiece(col,row+teamIndex)==null){
            return true;
        }
        if(col == this.col - 1 && row == this.row - teamIndex && board.getPiece(col,row) != null){
            return true;
        }
        if(col == this.col + 1 && row == this.row - teamIndex && board.getPiece(col,row) != null){
            return true;
        }
        return false;
    }
}
