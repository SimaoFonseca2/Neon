package Pieces;

import main.Board;

import java.awt.image.BufferedImage;

public class Bishop extends Piece{
    public Bishop(Board board, int col, int row, boolean isBlack) {
        super(board);
        this.col = col;
        this.row = row;
        this.xPos = col * board.tile_size;
        this.yPos = row * board.tile_size;
        this.isBlack = isBlack;
        this.value = 3;
        this.name = "Bishop";

        this.sprite = sheet.getSubimage(2 * sheetscale, isBlack ? sheetscale : 0, sheetscale, sheetscale).getScaledInstance(board.tile_size,board.tile_size, BufferedImage.SCALE_SMOOTH);
    }

    public boolean isValidMovement(int col, int row){
        return Math.abs(col - this.col) == Math.abs(row - this.row);
    }
    public boolean check_moveCollision(int col, int row){
        //check up&left
        if(this.col > col && this.row > row){
            for(int i = 1; i<Math.abs(this.col-col); i++){
                if(board.getPiece(this.col-i, this.row - i)!=null){
                    return true;
                }
            }
        }
        //check up&right
        if(this.col < col && this.row > row){
            for(int i = 1; i<Math.abs(this.col-col); i++){
                if(board.getPiece(this.col+i, this.row - i)!=null){
                    return true;
                }
            }
        }
        //check down&right
        if(this.col < col && this.row < row){
            for(int i = 1; i<Math.abs(this.col-col); i++){
                if(board.getPiece(this.col+i, this.row + i)!=null){
                    return true;
                }
            }
        }
        //check down&left
        if(this.col > col && this.row < row){
            for(int i = 1; i<Math.abs(this.col-col); i++){
                if(board.getPiece(this.col-i, this.row + i)!=null){
                    return true;
                }
            }
        }
        return false;
    }
}
