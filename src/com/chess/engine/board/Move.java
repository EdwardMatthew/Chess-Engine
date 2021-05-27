package com.chess.engine.board;

import com.chess.engine.pieces.Piece;

public abstract class Move {

    final Board board;
    final Piece movedPiece;
    final int destinationPosition;

    private Move(final Board board, final Piece movedPiece,final int destinationPosition) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationPosition = destinationPosition;
    }

    // exclusive piece movements
    public static final class BigMove extends Move {
        public BigMove(final Board board, final Piece movedPiece, final int destinationPosition) {
            super(board, movedPiece, destinationPosition);
        }
    }


    // moves ending in a capture
    public static final class CapturingMove extends Move {
        final Piece capturedPiece;
        public CapturingMove(final Board board, final Piece movedPiece, final int destinationPiece,
                      final Piece capturedPiece) {
            super(board, movedPiece, destinationPiece);
            this.capturedPiece = capturedPiece;
        }
    }
}
