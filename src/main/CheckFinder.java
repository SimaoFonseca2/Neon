package main;

import Pieces.Piece;

public class CheckFinder {
    Board board;
    public CheckFinder(Board board){
        this.board = board;
    }

    public boolean isKingChecked(Move move){
        Piece king = board.findKing(move.piece.isBlack);
        assert king != null;
        int kingCol = king.col;
        int kingRow = king.row;
        if(board.selectedPiece != null && board.selectedPiece.name.equals("King")){
            kingCol = move.nextCol;
            kingRow = move.nextRow;
        }
        return HitByRook(move.nextCol, move.nextRow, king, kingCol, kingRow,0, 1)|| //up
                HitByRook(move.nextCol, move.nextRow, king, kingCol, kingRow,1, 0)|| //right
                HitByRook(move.nextCol, move.nextRow, king, kingCol, kingRow,0, -1)|| //down
                HitByRook(move.nextCol, move.nextRow, king, kingCol, kingRow,-1,0)||   //left

                HitByBishop(move.nextCol, move.nextRow, king, kingCol, kingRow,-1,-1)|| //up left
                HitByBishop(move.nextCol, move.nextRow, king, kingCol, kingRow,1,-1)||  // up right
                HitByBishop(move.nextCol, move.nextRow, king, kingCol, kingRow,1,1)||   // down right
                HitByBishop(move.nextCol, move.nextRow, king, kingCol, kingRow,-1,1)||  //down left

                HitByKnight(move.nextCol, move.nextRow, king, kingCol, kingRow)||
                HitByPawn(king, move.nextCol, move.nextRow, kingCol, kingRow)||
                HitByKing(king, kingCol,kingRow);
    }


    private boolean HitByRook(int col, int row, Piece king, int kingCol, int kingRow, int colVal, int rowVal){
        for(int i = 1; i< 8; i++){
            if(kingCol + (i * colVal) == col && kingRow + (i * rowVal) == row){
                break;
            }
            Piece piece = board.getPiece(kingCol + (i * colVal), kingRow + (i * rowVal));
            if(piece != null && piece != board.selectedPiece){
                if(!board.isSameTeam(piece, king) && (piece.name.equals("Rook") || (piece.name.equals("Queen")))){
                    return true;
                }
                break;
            }
        }

        return false;
    }

    private boolean HitByBishop(int col, int row, Piece king, int kingCol, int kingRow, int colVal, int rowVal){
        for(int i = 1; i< 8; i++){
            if(kingCol - (i * colVal) == col && kingRow - (i * rowVal) == row){
                break;
            }
            Piece piece = board.getPiece(kingCol - (i * colVal), kingRow - (i * rowVal));
            if(piece != null && piece != board.selectedPiece){
                if(!board.isSameTeam(piece, king) && (piece.name.equals("Bishop") || (piece.name.equals("Queen")))){
                    return true;
                }
                break;
            }
        }

        return false;
    }


    private boolean HitByKnight(int col, int row, Piece king, int kingCol, int kingRow){
        return checkKnight(board.getPiece(kingCol - 1, kingRow - 2), king, col, row) ||
                checkKnight(board.getPiece(kingCol + 1, kingRow - 2), king, col, row) ||
                checkKnight(board.getPiece(kingCol + 2, kingRow - 1), king, col, row) ||
                checkKnight(board.getPiece(kingCol + 2, kingRow + 1), king, col, row) ||
                checkKnight(board.getPiece(kingCol + 1, kingRow + 2), king, col, row) ||
                checkKnight(board.getPiece(kingCol - 1, kingRow + 2), king, col, row) ||
                checkKnight(board.getPiece(kingCol - 2, kingRow + 1), king, col, row) ||
                checkKnight(board.getPiece(kingCol - 2, kingRow - 1), king, col, row);
    }

    private boolean checkKnight(Piece piece, Piece king, int col, int row ){
        return piece != null && !board.isSameTeam(piece, king) && piece.name.equals("Knight") && !(piece.col == col && piece.row == row);
    }


    private boolean HitByKing(Piece king, int kingCol, int kingRow){
        return checkKing(board.getPiece(kingCol - 1, kingRow - 1), king) ||
                checkKing(board.getPiece(kingCol + 1, kingRow - 1), king) ||
                checkKing(board.getPiece(kingCol, kingRow - 1), king) ||
                checkKing(board.getPiece(kingCol - 1, kingRow), king) ||
                checkKing(board.getPiece(kingCol + 1, kingRow), king) ||
                checkKing(board.getPiece(kingCol - 1, kingRow + 1), king) ||
                checkKing(board.getPiece(kingCol + 1, kingRow + 1), king) ||
                checkKing(board.getPiece(kingCol, kingRow + 1), king);
    }

    private boolean checkKing(Piece piece, Piece king){
        return piece != null && !board.isSameTeam(piece, king) && piece.name.equals("King");
    }


    private boolean HitByPawn(Piece king, int col, int row, int kingCol, int kingRow){
        int teamindex = king.isBlack ? 1 : -1;
        return checkPawn(board.getPiece(kingCol + 1, kingRow + teamindex), king, col, row) ||
                checkPawn(board.getPiece(kingCol - 1, kingRow + teamindex), king, col, row);
    }

    private boolean checkPawn(Piece piece, Piece king, int col, int row){
        return piece != null && !board.isSameTeam(piece,king) && piece.name.equals("Pawn");
    }

    public boolean isCheckmate(Piece king){
        for(Piece piece : board.pieceArrayList){
            if(board.isSameTeam(piece,king)){
                board.selectedPiece = piece == king ? king : null;
                for(int row = 0; row < board.rows; row++){
                    for(int col = 0; col < board.cols; col++){
                        Move move = new Move(board, piece, col, row);
                        if(board.isValidMove(move)){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}
