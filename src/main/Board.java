package main;

import Pieces.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.Collectors;

public class Board extends JPanel{

    private Stack<Move> moveHistoryBlack = new Stack<>();
    private Stack<Move> moveHistoryWhite = new Stack<>();
    public String fenString = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    //8/P7/8/8/8/4K3/8/4k3 w - - 0 1
    //rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1
    public int tile_size = 85;
    int cols = 8;
    int rows = 8;

    public int eval_num = 0;

    ArrayList<Piece> pieceArrayList = new ArrayList<>();

    public Move promotedPieceMove;

    public Piece selectedPiece;

    public int enPessantTile = -1;
    public boolean blackTurn = false;
    public boolean isGameOver = false;
    public boolean vsBot = false;

    public JLabel eval;
    ChessBot bot = new ChessBot(this);
    Input input = new Input(this);
    public CheckFinder checkFinder = new CheckFinder(this);

    public Board(JLabel eval) {
        this.setPreferredSize(new Dimension(cols * tile_size, rows * tile_size));
        this.addMouseListener(input);
        this.addMouseMotionListener(input);
        this.eval = eval;
        LoadPiecesFromFen(fenString);
    }
    public Board(String fenString) {
        this.setPreferredSize(new Dimension(cols * tile_size, rows * tile_size));
        this.addMouseListener(input);
        this.addMouseMotionListener(input);
        this.fenString = fenString;
        LoadPiecesFromFen(fenString);
    }
    public Board(boolean vsBot, JLabel eval){
        this.setPreferredSize(new Dimension(cols * tile_size, rows * tile_size));
        this.addMouseListener(input);
        this.addMouseMotionListener(input);
        LoadPiecesFromFen(fenString);
        this.vsBot = vsBot;
        this.eval = eval;
    }




    private void LoadPiecesFromFen(String defaultfenString) {
        pieceArrayList.clear();
        String[] sections = defaultfenString.split(" ");
        //set up piece positions
        String position = sections[0];
        int row = 0;
        int col = 0;
        for(int i = 0; i < position.length(); i++){
            char ch = position.charAt(i);
            if(ch =='/'){
                row++;
                col = 0;
            }else if(Character.isDigit(ch)){
                col += Character.getNumericValue(ch);
            }else{
                boolean isBlack = Character.isLowerCase(ch);
                char pieceCh = Character.toUpperCase(ch);

                switch (pieceCh){
                    case 'R':
                        pieceArrayList.add(new Rook(this,col,row,isBlack));
                        break;
                    case 'N':
                        pieceArrayList.add(new Knight(this,col,row,isBlack));
                        break;
                    case 'B':
                        pieceArrayList.add(new Bishop(this,col,row,isBlack));
                        break;
                    case 'P':
                        pieceArrayList.add(new Pawn(this,col,row,isBlack));
                        break;
                    case 'K':
                        pieceArrayList.add(new King(this,col,row,isBlack));
                        break;
                    case 'Q':
                        pieceArrayList.add(new Queen(this,col,row,isBlack));
                        break;
                }
                col++;
            }
            //team to move
            blackTurn = sections[1].equals("b");

            //castling
            Piece b_queen_rook = getPiece(0,0);
            if(b_queen_rook instanceof Rook){
                b_queen_rook.isFirstMove = sections[2].contains("q");
            }
            Piece b_king_rook = getPiece(7,0);
            if(b_king_rook instanceof Rook){
                b_king_rook.isFirstMove = sections[2].contains("k");
            }
            Piece w_queen_rook = getPiece(0,7);
            if(w_queen_rook instanceof Rook){
                w_queen_rook.isFirstMove = sections[2].contains("Q");
            }
            Piece w_king_rook = getPiece(7,7);
            if(w_king_rook instanceof Rook){
                w_king_rook.isFirstMove = sections[2].contains("K");
            }

            //en passant position
            if(sections[3].equals("-")){
                enPessantTile = -1;
            }else{
                enPessantTile = (7 - (sections[3].charAt(1)- '1')) * 8 + (sections[3].charAt(0) - 'a');
            }
        }
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
                if(rook != null) {
                    rook.col = 5;
                    rook.xPos = rook.col * tile_size;
                }
            }else{
                rook = getPiece(0, move.piece.row);
                if(rook != null){
                    rook.col = 3;
                    rook.xPos = rook.col * tile_size;
                }
            }

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
        //moveHistory.push(move);
        if (vsBot && blackTurn) {
          //  bot.makeBotMove();
            bot.makeMove();
        }
    }
    public void makeTestMove(Move move){
        if(move.piece.name.equals("Pawn")){
            TestmovePawn(move);
        }else if(move.piece.name.equals("King")){
            moveKing(move);
        }
        move.piece.col = move.nextCol;
        move.piece.row = move.nextRow;
        move.piece.xPos = move.nextCol * tile_size;
        move.piece.yPos = move.nextRow * tile_size;
        //move.piece.isFirstMove = false;
        Testcapture(move.capture);
        blackTurn = !blackTurn;
        //updateGameState();
        if(move.capture != null){
            System.out.println(move.capture.name);
        }
    }

    public void undoMove(Move move) {
        if(promotedPieceMove != null){
            Piece piece = getPiece(move.nextCol, move.nextRow);
            pieceArrayList.remove(piece);
            pieceArrayList.add(move.piece);
        }
        makeTestMove(new Move(this,move.piece, move.prevCol, move.prevRow));
        if(Math.abs(move.piece.col - move.nextCol) == 2 && move.piece.name.equals("King")){
            Piece king_rook;
            Piece queen_rook;
            if(Math.abs(move.piece.col - move.nextCol) == 2) {
                king_rook = getPiece(5, move.piece.row);
                if (king_rook != null && king_rook.name.equals("Rook")) {
                    king_rook.col = 7;
                    king_rook.xPos = king_rook.col * tile_size;
                }
                queen_rook = getPiece(3, move.piece.row);
                if (queen_rook != null && queen_rook.name.equals("Rook")) {
                    queen_rook.col = 0;
                    queen_rook.xPos = queen_rook.col * tile_size;
                }
                move.piece.isFirstMove = true;
            }else{
                king_rook = getPiece(5, move.piece.row);
                if (king_rook != null && king_rook.name.equals("Rook")) {
                    king_rook.col = 7;
                    king_rook.xPos = king_rook.col * tile_size;
                }
                queen_rook = getPiece(3, move.piece.row);
                if (queen_rook != null && queen_rook.name.equals("Rook")) {
                    queen_rook.col = 0;
                    queen_rook.xPos = queen_rook.col * tile_size;
                }
                move.piece.isFirstMove = true;
            }
        }
//        if (!moveHistoryBlack.isEmpty() && isBlack) {
//            Move lastMove = moveHistoryBlack.pop();
//            makeTestMove(new Move(this, lastMove.piece, lastMove.prevCol, lastMove.prevRow));
//        }
//        if (!moveHistoryWhite.isEmpty() && !isBlack) {
//            Move lastMove = moveHistoryWhite.pop();
//            makeTestMove(new Move(this, lastMove.piece, lastMove.prevCol, lastMove.prevRow));
//        }
    }

    private String updateGameState() {
        Piece king = findKing(blackTurn);
        if (king == null) {
            System.out.println("Error: King is null during game state update.");
            return "Error!";
        }
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
    private void TestmovePawn(Move move){
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
            TestpromotePawn(move);
        }
    }

    private void promotePawn(Move move) {
        pieceArrayList.add(new Queen(this, move.nextCol, move.nextRow, move.piece.isBlack));
        capture(move.piece);
    }
    private void TestpromotePawn(Move move) {
        pieceArrayList.add(new Queen(this, move.nextCol, move.nextRow, move.piece.isBlack));
        capture(move.piece);
        promotedPieceMove = move;
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
        if(piece!=null){
            pieceArrayList.remove(piece);
            if(piece.isBlack){
                eval_num += piece.value;
            }else{
                eval_num -= piece.value;
            }
            eval.setText("Score: "+ eval_num);
        }
    }
    public void Testcapture(Piece piece){
        if(piece!=null){
            pieceArrayList.remove(piece);
        }
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

            int rectWidth = 300;
            int rectHeight = 100;
            int rectX = (this.getWidth() - rectWidth) / 2;
            int rectY = (this.getHeight() - rectHeight) / 2;

            g2d.setColor(new Color(87, 54, 43));
            g2d.drawRect(rectX, rectY, rectWidth, rectHeight);
            g2d.setColor(Color.BLACK);
            g2d.fillRect(rectX, rectY, rectWidth, rectHeight);
            g2d.setColor(Color.WHITE);
            g2d.drawString(Objects.requireNonNull(updateGameState()), x, y);
        }
    }
}
