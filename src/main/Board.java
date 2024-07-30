package main;

import Pieces.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class Board extends JPanel {
    public int tile_size = 85;
    int cols = 8;
    int rows = 8;

    ArrayList<Piece> pieceArrayList = new ArrayList<>();

    public Piece selectedPiece;

    public int enPessantTile = -1;
    private boolean blackTurn = false;
    private boolean isGameOver = false;

    Input input = new Input(this);
    public CheckFinder checkFinder = new CheckFinder(this);

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

    private void moveKing(Move move){
        if(Math.abs(move.piece.col - move.nextCol) == 2){
            Piece rook;
            if(move.piece.col < move.nextCol){
                rook = getPiece(7, move.piece.row);
                rook.col = 5;
            }else{
                rook = getPiece(0, move.piece.row);
                rook.col = 3;
            }
            rook.xPos = rook.col * tile_size;
        }
    }

    public void makeMove(Move move){
        if(move.piece.name.equals("Pawn")){
            movePawn(move);
        }else if(move.piece.name.equals("King")){
            moveKing(move);
        }
        move.piece.col = move.nextCol;
        move.piece.row = move.nextRow;
        move.piece.xPos = move.nextCol * tile_size;
        move.piece.yPos = move.nextRow * tile_size;
        move.piece.isFirstMove = false;
        capture(move.capture);
        blackTurn = !blackTurn;
        updateGameState();
    }

    private String updateGameState() {
        Piece king = findKing(blackTurn);
        if(checkFinder.isCheckmate(king)){
            isGameOver = true;
            if(checkFinder.isKingChecked(new Move(this, king, king.col, king.row))){
                return blackTurn ? "White Wins!" : "Black Wins!";
            }else{
                return "Stalemate!";
            }
        } else if (noPieces(true) && noPieces(false)) {
            isGameOver = true;
            return "Stalemate!";
        }
        return null;
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
    }

    private void promotePawn(Move move) {
        pieceArrayList.add(new Queen(this, move.nextCol, move.nextRow, move.piece.isBlack));
        capture(move.piece);
    }

    Piece findKing(boolean isBlack){
        for (Piece piece : pieceArrayList){
            if (isBlack == piece.isBlack && Objects.equals(piece.name, "King")){
                return piece;
            }
        }
        return null;
    }

    private boolean noPieces(boolean isBlack){
        ArrayList<String> names = pieceArrayList.stream()
                .filter(piece -> piece.isBlack == isBlack)
                .map(piece -> piece.name)
                .collect(Collectors.toCollection(ArrayList::new));
        if(names.contains("Queen") || names.contains("Pawn") || names.contains("Rook")){
            return false;
        }
        return names.size() < 3;
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
        if(checkFinder.isKingChecked(move)){
            return false;
        }
        if(move.piece.isBlack != blackTurn){
            return false;
        }
        if(isGameOver){
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
//        pieceArrayList.add(new Knight(this, 1, 0, true));
//        pieceArrayList.add(new Knight(this, 6, 0, true));
//        pieceArrayList.add(new Rook(this, 0, 0, true));
//        pieceArrayList.add(new Rook(this, 7, 0, true));
//        pieceArrayList.add(new Queen(this, 3, 0, true));
//        pieceArrayList.add(new Bishop(this, 2, 0, true));
//        pieceArrayList.add(new Bishop(this, 5, 0, true));
        pieceArrayList.add(new King(this, 4, 0, true));
//        for (int col = 0; col < 8; col++) {
//            pieceArrayList.add(new Pawn(this, col, 1, true));
//        }
//        pieceArrayList.add(new Rook(this, 0, 7, false));
//        pieceArrayList.add(new Rook(this, 7, 7, false));
//        pieceArrayList.add(new Knight(this, 1, 7, false));
//        pieceArrayList.add(new Knight(this, 6, 7, false));
//        pieceArrayList.add(new Bishop(this, 2, 7, false));
//        pieceArrayList.add(new Bishop(this, 5, 7, false));
        pieceArrayList.add(new Queen(this, 3, 7, false));
        pieceArrayList.add(new King(this, 4, 7, false));
//        for (int col = 0; col < 8; col++) {
//            pieceArrayList.add(new Pawn(this, col, 6, false));
//        }
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
        //Game over screen
        if(isGameOver){
            Font font = new Font("Arial", Font.BOLD, 40);
            g2d.setFont(font);
            FontMetrics metrics = g.getFontMetrics(font);
            int x = (this.getWidth() - metrics.stringWidth(Objects.requireNonNull(updateGameState()))) / 2;
            int y = ((this.getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();

            int rectWidth = 300; // Rectangle width
            int rectHeight = 100; // Rectangle height
            int rectX = (this.getWidth() - rectWidth) / 2; // Calculate top-left x coordinate
            int rectY = (this.getHeight() - rectHeight) / 2; // Calculate top-left y coordinate

            g2d.setColor(new Color(87, 54, 43));
            g2d.drawRect(rectX, rectY, rectWidth, rectHeight);
            g2d.setColor(Color.BLACK);
            g2d.fillRect(rectX, rectY, rectWidth, rectHeight);
            g2d.setColor(Color.WHITE);
            g2d.drawString(Objects.requireNonNull(updateGameState()), x, y);
        }
    }
}
