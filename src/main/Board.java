package main;

import Pieces.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Board extends JPanel {
    public int tile_size = 85;
    int cols = 8;
    int rows = 8;

    ArrayList<Piece> pieceArrayList = new ArrayList<>();

    public Piece selectedPiece;

    Input input = new Input(this);

    public Board() {
        this.setPreferredSize(new Dimension(cols * tile_size, rows * tile_size));
        this.addMouseListener(input);
        this.addMouseMotionListener(input);
        AddPieces();
    }

    public Piece getPiece(int col, int row){
        for(Piece piece : pieceArrayList){
            if(piece.col == col && piece.row == row){
                return piece;
            }
        }
        return null;
    }

    public void makeMove(Move move){
        move.piece.col = move.nextCol;
        move.piece.row = move.nextRow;
        move.piece.xPos = move.nextCol * tile_size;
        move.piece.yPos = move.nextRow * tile_size;
        capture(move);
    }

    public void capture(Move move){
        pieceArrayList.remove(move.capture);
    }

    public boolean isValidMove(Move move){
        if(isSameTeam(move.piece, move.capture)){
            return false;
        }
        return true;
    }

    public boolean isSameTeam(Piece p1, Piece p2){
        if(p1 == null || p2 == null){
            return false;
        }
        return p1.isBlack == p2.isBlack;
    }

    public void AddPieces(){
        pieceArrayList.add(new Knight(this, 1, 0, true));
        pieceArrayList.add(new Knight(this, 6, 0, true));
        pieceArrayList.add(new Rook(this, 0, 0, true));
        pieceArrayList.add(new Rook(this, 7, 0, true));
        pieceArrayList.add(new Queen(this, 3, 0, true));
        pieceArrayList.add(new Bishop(this, 2, 0, true));
        pieceArrayList.add(new Bishop(this, 5, 0, true));
        pieceArrayList.add(new King(this, 4, 0, true));
        for (int col = 0; col < 8; col++) {
            pieceArrayList.add(new Pawn(this, col, 1, true));
        }
        pieceArrayList.add(new Rook(this, 0, 7, false));
        pieceArrayList.add(new Rook(this, 7, 7, false));
        pieceArrayList.add(new Knight(this, 1, 7, false));
        pieceArrayList.add(new Knight(this, 6, 7, false));
        pieceArrayList.add(new Bishop(this, 2, 7, false));
        pieceArrayList.add(new Bishop(this, 5, 7, false));
        pieceArrayList.add(new Queen(this, 3, 7, false));
        pieceArrayList.add(new King(this, 4, 7, false));
        for (int col = 0; col < 8; col++) {
            pieceArrayList.add(new Pawn(this, col, 6, false));
        }
    }

    public void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //paint board
        for(int row = 0; row<rows; row++){
            for(int col = 0; col<cols; col++){
                g2d.setColor((col+row)%2==0 ? new Color(220, 204, 183) : new Color(85, 52, 43));
                g2d.fillRect(col * tile_size, row * tile_size, tile_size, tile_size);
            }
        }
        //paint piece possible moves
        if(selectedPiece != null){
            for(int row = 0; row<rows; row++){
                for(int col = 0; col<cols; col++){
                    if(isValidMove(new Move(this, selectedPiece, col,row))){
                        g2d.setColor(new Color(0, 0, 0, 179));
                        int offset = (tile_size - tile_size / 2) / 2;

                        if(getPiece(col, row) != null){
                            g2d.setStroke(new BasicStroke(3)); // Adjust the width as needed
                            g2d.drawOval(col * tile_size, row * tile_size, tile_size, tile_size);
                        }else{
                            g2d.fillOval(col * tile_size+offset, row * tile_size+offset, tile_size/2, tile_size/2);
                        }
                    }
                }
            }
        }
        //paint pieces
        for(Piece piece : pieceArrayList){
            piece.paint(g2d);
        }
    }
}
