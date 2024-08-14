package Pieces;

import main.Board;

import java.awt.image.BufferedImage;

public class Knight extends Piece{
    public Knight(Board board, int col, int row, boolean isBlack) {
        super(board);
        this.col = col;
        this.row = row;
        this.xPos = col * board.tile_size;
        this.yPos = row * board.tile_size;
        this.isBlack = isBlack;
        this.value = 3;
        this.relativeValue = this.value;
        this.name = "Knight";
        this.sprite = sheet.getSubimage(3 * sheetscale, isBlack ? sheetscale : 0, sheetscale, sheetscale).getScaledInstance(board.tile_size,board.tile_size, BufferedImage.SCALE_SMOOTH);
    }

    public boolean isValidMovement(int col, int row){
        return Math.abs(col - this.col) * Math.abs(row - this.row) == 2;
    }

//    public int getRelativeValue(){
//        if(col>=2 && col <=5 && row>=2 && row <= 5){
//            relativeValue = value*8;
//        }
//        if(col>=2 && col <=5 && row == 1){
//            relativeValue = value*6;
//        }
//        if(col>=2 && col <=5 && row == 6){
//            relativeValue = value*6;
//        }
//        if(col==1 && row>=2 && row<=5){
//            this.relativeValue = value*6;
//        }
//        if(col==6 && row>=2 && row<=5){
//            relativeValue = value*6;
//        }
//        if(col == 0 && row == 0 || col==7 && row == 0 || col == 0 && row == 7 || col ==7 && row == 7){
//            relativeValue = value*2;
//        }
//        if(col == 0 && row == 1 || col==1 && row == 0  || col == 0 && row == 6 || col == 1 && row == 7){
//            relativeValue = value*2;
//        }
//        if(col == 7 && row == 1 || col==6 && row == 0  || col == 7 && row == 6 || col == 6 && row == 7){
//            relativeValue = value*2;
//        }
//        if(col==0 && row >=2 && row <= 5 || col==7 && row >=2 && row <= 5 || row==0 && col >=2 && col <= 5 || row==7 && col >=2 && col <= 5 || col == 1 && row == 6 || col == 6 && row == 6 || col == 1 && row == 1 || col == 6 && row == 1){
//            relativeValue = value*3;
//        }
//        return relativeValue;
//    }

}
