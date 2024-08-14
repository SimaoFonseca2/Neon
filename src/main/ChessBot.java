package main;

import Pieces.Piece;

import java.util.ArrayList;

public class ChessBot {
    private final Board board;
    Move chosenMove;
    public ChessBot(Board board){
        this.board = board;
    }

    public void makeMove(){
        Minimax(board,3,board.blackTurn);
        if(chosenMove!=null){
            board.makeMove(chosenMove);
        }
    }
    private int EvaluateBoard() {
        int material = 0;
        for (Piece piece : board.pieceArrayList) {
            int teamMultiplier = piece.isBlack ? -1 : 1;
            material += piece.value * teamMultiplier;
        }
        return material;
    }
    public ArrayList<Move> findValidMoves(){
        ArrayList<Move> validMoves = new ArrayList<>();
        for (Piece piece : board.pieceArrayList) {
            for (int row = 0; row < board.rows; row++) {
                for (int col = 0; col < board.cols; col++) {
                    Move move = new Move(board, piece, col, row);
                    if (board.isValidMove(move)) {
                        validMoves.add(move);
                    }
                }
            }
        }
        return validMoves;
    }

    int Minimax(Board board, int depth, boolean isMinimizing){
        if(depth == 0 || findValidMoves().isEmpty()){
            return  EvaluateBoard();
        }
        ArrayList<Move> validMoves = findValidMoves();
        if(isMinimizing){
            int bestEval = Integer.MAX_VALUE;
            Move bestMove = validMoves.getFirst();
            if(!validMoves.isEmpty()){
                for(Move move : validMoves){
                    board.makeTestMove(move);
                    int eval = Minimax(board,depth - 1, false);
                    if(move.capture != null){
                        board.pieceArrayList.add(move.capture);
                    }
                    board.undoMove(move);
                    if(bestEval>eval && board.blackTurn){
                        bestMove = move;
                        bestEval = eval;
                    }
                }
            }
            chosenMove = bestMove;
            return bestEval;
        }else {
            int bestEval = Integer.MIN_VALUE;
            Move bestMove = validMoves.getFirst();
            if(!validMoves.isEmpty()){
                for(Move move : validMoves){
                    board.makeTestMove(move);
                    int eval = Minimax(board,depth - 1, true);
                    if(move.capture != null){
                        board.pieceArrayList.add(move.capture);
                    }
                    board.undoMove(move);
                    if(bestEval<eval && !board.blackTurn){
                        bestMove = move;
                        bestEval = eval;
                    }
                }
            }
            chosenMove = bestMove;
            return bestEval;
        }
    }
}
