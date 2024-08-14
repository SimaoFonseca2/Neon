package Pieces;

import main.Board;
import main.CheckFinder;
import main.Move;

import java.awt.image.BufferedImage;

public class King extends Piece{
    public King(Board board, int col, int row, boolean isBlack) {
        super(board);
        this.col = col;
        this.row = row;
        this.xPos = col * board.tile_size;
        this.yPos = row * board.tile_size;
        this.isBlack = isBlack;
        this.name = "King";
        this.value = 200;
        this.sprite = sheet.getSubimage(0, isBlack ? sheetscale : 0, sheetscale, sheetscale).getScaledInstance(board.tile_size,board.tile_size, BufferedImage.SCALE_SMOOTH);
    }
    public boolean isValidMovement(int col, int row){
        return Math.abs((col - this.col) * (row - this.row)) == 1 || Math.abs(row - this.row) + Math.abs(col - this.col) == 1 || canCastle(col,row);
    }

    public boolean canCastle(int col, int row) {
        if (this.row == row) {
            if (col == 6) {
                Piece rook = board.getPiece(7, row);
                    if (rook != null && rook.isFirstMove && isFirstMove) {
                        return board.getPiece(5, row) == null &&
                                board.getPiece(6, row) == null &&
                                !board.checkFinder.isKingChecked(new Move(board,this,4,row)) &&
                                !board.checkFinder.isKingChecked(new Move(board, this, 5, row));
                    }
                } else if (col == 2) {
                    Piece rook = board.getPiece(0, row);
                    if (rook != null && rook.isFirstMove && isFirstMove) {
                        return board.getPiece(3, row) == null &&
                                board.getPiece(2, row) == null &&
                                board.getPiece(1, row) == null &&
                                !board.checkFinder.isKingChecked(new Move(board,this,4,row)) &&
                                !board.checkFinder.isKingChecked(new Move(board, this, 3, row));
                    }
                }
            }
            return false;
        }
}
