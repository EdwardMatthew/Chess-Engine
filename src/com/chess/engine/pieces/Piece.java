package com.chess.engine.pieces;

import com.chess.engine.Color;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.Collection;

public abstract class Piece {
    protected final int piecePosition;
    protected final Color pieceColor;

    Piece(final int piecePosition, final Color pieceColor) {
        this.piecePosition = piecePosition;
        this.pieceColor = pieceColor;
    }

    public Color getPieceColor() {
        return this.pieceColor;
    }

    // calculating legal moves
    public abstract Collection<Move> findLegalMove(final Board board);

}
