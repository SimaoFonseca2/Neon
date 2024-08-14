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
        this.value = 5;
        this.name = "Rook";

        this.sprite = sheet.getSubimage(4 * sheetscale, isBlack ? sheetscale : 0, sheetscale, sheetscale).getScaledInstance(board.tile_size,board.tile_size, BufferedImage.SCALE_SMOOTH);
    }
    public boolean isValidMovement(int col, int row){
        return this.col == col || this.row == row;
    }
    public boolean check_moveCollision(int col, int row){
        //check left
        if(this.col > col){
            for(int c = this.col - 1; c > col; c--){
                if(board.getPiece(c,this.row) != null){
                    return true;
                }
            }
        }
        //check right
        if(this.col < col){
            for(int c = this.col + 1; c < col; c++){
                if(board.getPiece(c,this.row) != null){
                    return true;
                }
            }
        }
        //check up
        if(this.row > row){
            for(int r = this.row - 1; r > row; r--){
                if(board.getPiece(this.col,r) != null){
                    return true;
                }
            }
        }
        //check down
        if(this.row < row){
            for(int r = this.row + 1; r < row; r++){
                if(board.getPiece(this.col,r) != null){
                    return true;
                }
            }
        }
        return false;
    }
}
