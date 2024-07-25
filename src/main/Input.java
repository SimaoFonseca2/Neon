package main;

import Pieces.Piece;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Input extends MouseAdapter {
    Board board;
    public Input(Board board){
        this.board = board;
    }
    @Override
    public void mousePressed(MouseEvent e) {
        int col = e.getX() / board.tile_size;
        int row = e.getY() / board.tile_size;
        Piece piece = board.getPiece(col, row);
        if(piece != null){
            board.selectedPiece = piece;
        }
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        if(board.selectedPiece != null){
            board.selectedPiece.xPos = e.getX() - board.tile_size / 2;
            board.selectedPiece.yPos = e.getY() - board.tile_size / 2;
            board.repaint();
        }

    }
    @Override
    public void mouseReleased(MouseEvent e) {
        int col = e.getX() / board.tile_size;
        int row = e.getY() / board.tile_size;
        if(board.selectedPiece != null){
            Move move = new Move(board, board.selectedPiece, col, row);
            if(board.isValidMove(move)){
                board.makeMove(move);
            }else{
                board.selectedPiece.xPos = board.selectedPiece.col * board.tile_size;
                board.selectedPiece.yPos = board.selectedPiece.row * board.tile_size;
            }
        }
        board.selectedPiece = null;
        board.repaint();
    }
}
