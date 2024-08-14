package main;

import Pieces.Knight;
import Pieces.Piece;

import java.util.Objects;

public class Move {

    int prevCol;
    int prevRow;
    int nextCol;
    int nextRow;
    Piece piece;
    Piece capture;

    public Move(Board board, Piece piece, int newCol, int newRow){
        this.prevCol = piece.col;
        this.prevRow = piece.row;
        this.nextCol = newCol;
        this.nextRow = newRow;
        this.piece = piece;
        this.capture = board.getPiece(newCol,newRow);
    }
}
