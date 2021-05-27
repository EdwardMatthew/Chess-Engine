package com.chess.engine.pieces;

import com.chess.engine.Color;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.Collection;

public abstract class Piece {
    protected final int piecePosition;
    protected final Color pieceColor;
    protected final boolean isFirstMove;

    Piece(final int piecePosition, final Color pieceColor) {
        this.piecePosition = piecePosition;
        this.pieceColor = pieceColor;
        // continue this later
        // using first move to check pawns
        // because first move could also be used to check for king castling
        this.isFirstMove = false;
    }

    public int getPiecePosition() {
        return this.piecePosition;
    }

    public boolean isFirstMove() {
        return this.isFirstMove;
    }

    public Color getPieceColor() {
        return this.pieceColor;
    }

    // calculating legal moves
    public abstract Collection<Move> findLegalMove(final Board board);

    public enum pieceType {

        PAWN("P"),
        KNIGHT("N"),
        BISHOP("B"),
        ROOK("R"),
        QUEEN("Q"),
        KING("K");


        private String pieceName;

        pieceType(final String pieceName) {
            this.pieceName = pieceName;
        }

        @Override public String toString() {
            return this.pieceName;
        }
    }
}
