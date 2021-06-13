package com.chess.engine.pieces;

import com.chess.engine.Color;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.Collection;

public abstract class Piece {
    protected final int piecePosition;
    protected final Color pieceColor;
    protected final boolean isFirstMove;
    protected final PieceType pieceType;
    private final int cachedHashCode;

    Piece(final PieceType pieceType, final int piecePosition, final Color pieceColor) {
        this.pieceType = pieceType;
        this.piecePosition = piecePosition;
        this.pieceColor = pieceColor;
        // continue this later
        // using first move to check pawns
        // because first move could also be used to check for king castling
        this.isFirstMove = false;
        this.cachedHashCode = computeHashCode();
    }

    private int computeHashCode() {
        // default computing of hashcode
        int result = pieceType.hashCode();
        result = 31 * result + pieceColor.hashCode();
        result = 31 * result + piecePosition;
        result = 31 * result + (isFirstMove ? 1 : 0);
        return result;
    }

    @Override
    // overriding to get object equality instead of reference equality
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        } if (!(other instanceof Piece)) {
            return false;
        }

        // casting the other object as piece
        final Piece otherPiece = (Piece) other;
        return piecePosition == otherPiece.getPiecePosition() && pieceType == otherPiece.getPieceType() &&
                pieceColor == otherPiece.getPieceColor() && isFirstMove == otherPiece.isFirstMove();
    }

    @Override
    public int hashCode() {
        // to not compute the hash everytime
        return this.cachedHashCode;
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

    public abstract Piece movePiece(Move move);

    public PieceType getPieceType() {
        return this.pieceType;
    }


    public enum PieceType {
        BISHOP("B") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KING("K") {
            @Override
            public boolean isKing() {
                return true;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KNIGHT("N") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        PAWN("P") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        QUEEN("Q") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        ROOK("R") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return true;
            }
        };


        private String pieceName;

        PieceType(final String pieceName) {
            this.pieceName = pieceName;
        }

        @Override
        public String toString() {
            return this.pieceName;
        }

        public abstract boolean isKing();

        public abstract boolean isRook();
    }
}
