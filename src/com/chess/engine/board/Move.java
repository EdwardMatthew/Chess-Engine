package com.chess.engine.board;

import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;
import com.sun.xml.internal.bind.annotation.OverrideAnnotationOf;

import static com.chess.engine.board.Board.*;

public abstract class Move {

    protected final Board board;
    protected final Piece movedPiece;
    protected final int destinationPosition;
    protected final boolean isFirstMove;

    public static final Move INVALID_MOVE = new InvalidMove();

    private Move(final Board board, final Piece movedPiece, final int destinationPosition) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationPosition = destinationPosition;
        this.isFirstMove = movedPiece.isFirstMove();
    }

    private Move(final Board board, final int destinationPosition) {
        this.board = board;
        this.destinationPosition = destinationPosition;
        this.movedPiece = null;
        this.isFirstMove = false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.destinationPosition;
        result = prime * result + this.movedPiece.hashCode();
        result = prime * result + this.movedPiece.getPiecePosition();
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
        return  getCurrentPosition() == otherMove.getCurrentPosition() &&
                getDestinationPosition() == otherMove.getDestinationPosition() &&
                getMovedPiece().equals(otherMove.getMovedPiece());
    }

    public Board getBoard() {
        return this.board;
    }

    public int getCurrentPosition() {
        return this.getMovedPiece().getPiecePosition();
    }

    public int getDestinationPosition() {
        return this.destinationPosition;
    }

    public Piece getMovedPiece() {
        return this.movedPiece;
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

    public static class MajorCapturingMove extends CapturingMove {
        public MajorCapturingMove(final Board board, final Piece pieceMoved,
                                  final int destinationPosition, final Piece capturedPiece) {
            super(board, pieceMoved, destinationPosition, capturedPiece);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof MajorCapturingMove && super.equals(other);
        }

        @Override
        public String toString() {
            return movedPiece.getPieceType() + Utilities.getPositionAtCoordinate(this.destinationPosition);
        }
    }

    // exclusive piece movements
    public static final class BigMove extends Move {
        public BigMove(final Board board, final Piece movedPiece, final int destinationPosition) {
            super(board, movedPiece, destinationPosition);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof BigMove && super.equals(other);
        }

        @Override
        public String toString() {
            return movedPiece.getPieceType().toString() + Utilities.getPositionAtCoordinate(this.destinationPosition);
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

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof PawnMove && super.equals(other);
        }

        @Override
        public String toString() {
            return Utilities.getPositionAtCoordinate(this.destinationPosition);
        }
    }

    public static class PawnCapturingMove extends CapturingMove {
        public PawnCapturingMove(final Board board, final Piece movedPiece, final int destinationPosition,
                                 final Piece capturedPiece) {
            super(board, movedPiece, destinationPosition, capturedPiece);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof PawnCapturingMove && super.equals(other);
        }

        @Override
        public String toString() {
            return Utilities.getPositionAtCoordinate(this.movedPiece.getPiecePosition()).substring(0,1) + "x" +
                    Utilities.getPositionAtCoordinate(this.destinationPosition);
        }
    }

    public static final class PawnEnPassant extends PawnCapturingMove {
        public PawnEnPassant(final Board board, final Piece movedPiece, final int destinationPosition,
                        final Piece capturedPiece) {
            super(board, movedPiece, destinationPosition, capturedPiece);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof PawnEnPassant && super.equals(other);
        }

        @Override
        public Board execute() {
            final Builder builder = new Builder();
            for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
                if (!this.movedPiece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
                if (!piece.equals(this.getCapturedPiece())) {
                    builder.setPiece(piece);
                }
            }

            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getColor());
            return builder.build();
        }
    }

    public static class PawnPromotion extends Move {

        final Move decoratedMove;
        final Pawn promotedPawn;

        public PawnPromotion(final Move decoratedMove) {
            super(decoratedMove.getBoard(), decoratedMove.getMovedPiece(), decoratedMove.getDestinationPosition());
            this.decoratedMove = decoratedMove;
            this.promotedPawn = (Pawn)decoratedMove.getMovedPiece();
        }

        @Override
        public int hashCode() {
            return decoratedMove.hashCode() + (31 * promotedPawn.hashCode());
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof PawnPromotion && super.equals(other);
        }

        @Override
        public Board execute() {
            final Board pawnMovedBoard = this.decoratedMove.execute();
            final Builder builder = new Builder();
            for (final Piece piece : pawnMovedBoard.currentPlayer().getActivePieces()) {
                if (!this.promotedPawn.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece : pawnMovedBoard.currentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            builder.setPiece(this.promotedPawn.getPromotionPiece().movePiece(this));
            builder.setMoveMaker(pawnMovedBoard.currentPlayer().getColor());
            return builder.build();
        }

        @Override
        public boolean isCapture() {
            return this.decoratedMove.isCapture();
        }

        @Override
        public Piece getCapturedPiece() {
            return this.decoratedMove.getCapturedPiece();
        }

        @Override
        public String toString() {
            return "";
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
                if (!this.movedPiece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            final Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this);
            builder.setPiece(movedPiece);
            builder.setEnPassantPawn(movedPawn); // set jump pawn to be the en passant pawn
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getColor());
            return builder.build();
        }

        @Override
        public String toString() {
            return Utilities.getPositionAtCoordinate(this.destinationPosition);
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
                if (!this.movedPiece.equals(piece) && !this.castleRook.equals(piece)) {
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

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime + result + this.castleRook.hashCode();
            result = prime * result + this.castleRookDestination;
            return result;
        }

        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof CastleMove)) {
                return false;
            }
            final CastleMove otherCastleMove = (CastleMove) other;
            return super.equals(otherCastleMove) && this.castleRook.equals(otherCastleMove.getCastleRook());
        }
    }

    public static final class ShortCastleMove extends CastleMove {
        public ShortCastleMove(final Board board, final Piece movedPiece, final int destinationPosition,
                               final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
            super(board, movedPiece, destinationPosition, castleRook, castleRookStart, castleRookDestination);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof ShortCastleMove && super.equals(other);
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
        public boolean equals(final Object other) {
            return this == other || other instanceof LongCastleMove && super.equals(other);
        }

        @Override
        public String toString() {
            return "0-0-0";
        }
    }

    public static final class InvalidMove extends Move {

        public InvalidMove() {
            super(null, 65); // 65 invalid destination
        }

        @Override
        public Board execute() {
            throw new RuntimeException("cannot execute this class! (invalid move)");
        }

        @Override
        public int getCurrentPosition() {
            return -1;
        }
    }

    // for convenience
    public static class MoveFactory {

        private MoveFactory() {
            throw new RuntimeException("Class not instantiable");
        }

        public static Move getInvalidMove() {
            return INVALID_MOVE;
        }

        public static Move createMove(final Board board, final int currentPosition, final int destinationPosition) {
            for (final Move move : board.getAllLegalMoves()) {
                if (move.getCurrentPosition() == currentPosition &&
                    move.getDestinationPosition() == destinationPosition) {
                    return move;
                }
            }
            return INVALID_MOVE;
        }
    }
}
