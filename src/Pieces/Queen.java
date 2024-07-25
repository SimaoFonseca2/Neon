package Pieces;

import main.Board;

import java.awt.image.BufferedImage;

public class Queen extends Piece{
    public Queen(Board board, int col, int row, boolean isBlack) {
        super(board);
        this.col = col;
        this.row = row;
        this.xPos = col * board.tile_size;
        this.yPos = row * board.tile_size;
        this.isBlack = isBlack;
        this.name = "Queen";
        this.sprite = sheet.getSubimage(sheetscale, isBlack ? sheetscale : 0, sheetscale, sheetscale).getScaledInstance(board.tile_size,board.tile_size, BufferedImage.SCALE_SMOOTH);
    }
    public boolean isValidMovement(int col, int row){
        return (this.col == col || this.row == row) || Math.abs(col - this.col) == Math.abs(row - this.row);
    }

    public boolean check_moveCollision(int col, int row){
        if(this.col == col || this.row == row){
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
        }else{
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
        }

        return false;
    }

}
