package com.chess.engine.board;

import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

import static com.chess.engine.board.Board.*;
import static com.chess.engine.board.Move.CastleMove.*;

public abstract class Move {

    final Board board;
    final Piece movedPiece;
    final int destinationPosition;

    public static final Move INVALID_MOVE = new InvalidMove();

    private Move(final Board board, final Piece movedPiece,final int destinationPosition) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationPosition = destinationPosition;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + this.movedPiece.getPiecePosition();
        result = prime * result + this.destinationPosition;
        return result;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        } if (!(other instanceof Move)) {
            return false;
        }
        final Move otherMove = (Move) other;
        return getCurrentPosition() == otherMove.getCurrentPosition() &&
                getDestinationPosition() == otherMove.getDestinationPosition() &&
                getMovedPiece() == otherMove.getMovedPiece();
    }

    public int getCurrentPosition() {
        return this.movedPiece.getPiecePosition();
    }

    public int getDestinationPosition() {
        return this.destinationPosition;
    }

    public Piece getMovedPiece() {
        return movedPiece;
    }

    public boolean isCapture() {
        return false;
    }

    public boolean isCastlingMove() {
        return false;
    }

    public Piece getCapturedPiece() {
        return null;
    }

    public Board execute() {
        // works by making a new board where the move is made
        // not by mutating the word
        // to be worked on later

        final Builder builder = new Builder();

        for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
            if (!this.movedPiece.equals(piece)) {
                builder.setPiece(piece);
            }
        }

        for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
            builder.setPiece(piece);
        }
        // move the moved piece
        builder.setPiece(this.movedPiece.movePiece(this));
        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getColor());
        return builder.build();
    }

    // exclusive piece movements
    public static final class BigMove extends Move {
        public BigMove(final Board board, final Piece movedPiece, final int destinationPosition) {
            super(board, movedPiece, destinationPosition);
        }

    }

    // moves ending in a capture
    public static class CapturingMove extends Move {
        final Piece capturedPiece;
        public CapturingMove(final Board board, final Piece movedPiece, final int destinationPiece,
                      final Piece capturedPiece) {
            super(board, movedPiece, destinationPiece);
            this.capturedPiece = capturedPiece;
        }

        @Override
        public int hashCode() {
            return this.capturedPiece.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            } if (!(other instanceof CapturingMove)) {
                return false;
            }
            final CapturingMove otherCapturingMove = (CapturingMove) other;
            return super.equals(otherCapturingMove) && getCapturedPiece().equals(otherCapturingMove.getCapturedPiece());
        }

        @Override
        public Board execute() {
            // to be worked on later
            return null;
        }

        @Override
        public boolean isCapture() {
            return true;
        }

        @Override
        public Piece getCapturedPiece() {
            return this.capturedPiece;
        }
    }

    public static final class PawnMove extends Move {
        public PawnMove(final Board board, final Piece movedPiece, final int destinationPosition) {
            super(board, movedPiece, destinationPosition);
        }
    }

    public static class PawnCapturingMove extends CapturingMove {
        public PawnCapturingMove(final Board board, final Piece movedPiece, final int destinationPosition,
                                 final Piece capturedPiece) {
            super(board, movedPiece, destinationPosition, capturedPiece);
        }
    }

    public static final class PawnEnPassant extends PawnCapturingMove {
        public PawnEnPassant(final Board board, final Piece movedPiece, final int destinationPosition,
                        final Piece capturedPiece) {
            super(board, movedPiece, destinationPosition, capturedPiece);
        }
    }

    public static final class PawnJump extends Move {
        public PawnJump(final Board board, final Piece movedPiece, final int destinationPosition) {
            super(board, movedPiece, destinationPosition);
        }

        @Override
        public Board execute() {
            final Builder builder = new Builder();
            for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
                if (this.movedPiece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            final Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this);
            builder.setPiece(movedPiece);
            builder.setEnPassantPawn(movedPawn);
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getColor());
            return builder.build();
        }
    }

    static abstract class CastleMove extends Move {
        protected final Rook castleRook;
        protected final int castleRookStart;
        protected final int castleRookDestination;

        public CastleMove(final Board board, final Piece movedPiece, final int destinationPosition,
                          final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
            super(board, movedPiece, destinationPosition);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }

        public Rook getCastleRook() {
            return this.castleRook;
        }

        @Override
        public boolean isCastlingMove() {
            return true;
        }

        @Override
        // two pieces move with this execute method
        public Board execute() {
            final Builder builder = new Builder();
            for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
                if (this.movedPiece.equals(piece) && !this.castleRook.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            // look into this later
            builder.setPiece(new Rook(this.castleRook.getPiecePosition(), this.castleRook.getPieceColor()));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getColor());
            return builder.build();
        }
    }

    public static final class ShortCastleMove extends CastleMove {
        public ShortCastleMove(final Board board, final Piece movedPiece, final int destinationPosition,
                               final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
            super(board, movedPiece, destinationPosition, castleRook, castleRookStart, castleRookDestination);
        }

        @Override
        public String toString() {
            return "0-0";
        }
    }

    public static final class LongCastleMove extends CastleMove {
        public LongCastleMove(final Board board, final Piece movedPiece, final int destinationPosition,
                              final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
            super(board, movedPiece, destinationPosition, castleRook, castleRookStart, castleRookDestination);
        }

        @Override
        public String toString() {
            return "0-0-0";
        }
    }

    public static final class InvalidMove extends Move {
        public InvalidMove() {
            super(null, null, -1);
        }

        @Override
        public Board execute() {
            throw new RuntimeException("cannot execute this class! (invalid move)");
        }
    }

    // for convenience
    public static class MoveFactory {
        private MoveFactory() {
            throw new RuntimeException("Class not instantiable");
        }

        public static Move createMove(final Board board, final int currentPosition, final int destinationPosition) {
            for (final Move move : board.getAllLegalMoves()) {
                if(move.getCurrentPosition() == currentPosition && move.getDestinationPosition()
                        == destinationPosition) {
                    return move;
                }
            }
            return INVALID_MOVE;
        }
    }
}
