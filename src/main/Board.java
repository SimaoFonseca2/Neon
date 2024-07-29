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

    public int enPessantTile = -1;

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

    public int getTile_num(int col, int row){
        return row * rows + col;
    }

    public void makeMove(Move move){
        if(move.piece.name.equals("Pawn")){
            movePawn(move);
        }else{
            move.piece.col = move.nextCol;
            move.piece.row = move.nextRow;
            move.piece.xPos = move.nextCol * tile_size;
            move.piece.yPos = move.nextRow * tile_size;
            move.piece.isFirstMove = false;
            capture(move.capture);
        }
    }

    private void movePawn(Move move) {
        //en pessant
        int teamIndex = move.piece.isBlack ? -1 : 1;
        if(getTile_num(move.nextCol, move.nextRow)==enPessantTile){
            move.capture = getPiece(move.nextCol,move.nextRow+teamIndex);
        }
        if(Math.abs((move.piece.row - move.nextRow))==2){
            enPessantTile = getTile_num(move.nextCol, move.nextRow + teamIndex);
        }else{
            enPessantTile = -1;
        }

        //promotions
        teamIndex = move.piece.isBlack ? 7 : 0;
        if(move.nextRow == teamIndex){
            promotePawn(move);
        }

        move.piece.col = move.nextCol;
        move.piece.row = move.nextRow;
        move.piece.xPos = move.nextCol * tile_size;
        move.piece.yPos = move.nextRow * tile_size;
        move.piece.isFirstMove = false;
        capture(move.capture);
    }

    private void promotePawn(Move move) {
        pieceArrayList.add(new Queen(this, move.nextCol, move.nextRow, move.piece.isBlack));
        capture(move.piece);
    }

    public void capture(Piece piece){
        pieceArrayList.remove(piece);
    }

    public boolean isValidMove(Move move){
        if(isSameTeam(move.piece, move.capture)){
            return false;
        }
        if(!move.piece.isValidMovement(move.nextCol, move.nextRow)){
            return false;
        }
        if(move.piece.check_moveCollision(move.nextCol, move.nextRow)){
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
                            g2d.setStroke(new BasicStroke(3));
                            g2d.drawOval(col * tile_size, row * tile_size, tile_size, tile_size);
                        }else{
                            g2d.fillOval(col * tile_size+offset, row * tile_size+offset, tile_size/2, tile_size/2);
                        }
                    }
                }
            }
        }

        if(selectedPiece != null && !input.isDragged){
            g2d.setColor(new Color(0, 0, 0, 179));
            g2d.setStroke(new BasicStroke(3));
            g2d.drawOval(selectedPiece.col * tile_size, selectedPiece.row * tile_size, tile_size, tile_size);
            g2d.fillOval(selectedPiece.col * tile_size, selectedPiece.row * tile_size, tile_size, tile_size);
        }
        //paint pieces
        for(Piece piece : pieceArrayList){
            piece.paint(g2d);
        }
    }
}
