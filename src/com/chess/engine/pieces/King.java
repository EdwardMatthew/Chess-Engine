package com.chess.engine.pieces;

import com.chess.engine.Color;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Square;
import com.chess.engine.board.Utilities;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chess.engine.board.Move.*;

public class King extends Piece {

    private static final int[] POSSIBLE_LEGAL_MOVES_DIRECTION = {-9, -8, -7, -1, 1, 7, 8, 9};
    private final boolean isCastled;
    private final boolean canShortCastle;
    private final boolean canLongCastle;


    public King(final int piecePosition, final Color pieceColor,
                final boolean canShortCastle, final boolean canLongCastle) {
        super(PieceType.KING, piecePosition, pieceColor, true);
        this.isCastled = false;
        this.canShortCastle = canShortCastle;
        this.canLongCastle = canLongCastle;
    }

    public King(final int piecePosition, final Color pieceColor, final boolean isCastled,
                final boolean canShortCastle, final boolean canLongCastle,
                final boolean isFirstMove) {
        super(PieceType.KING, piecePosition, pieceColor, isFirstMove);
        this.isCastled = isCastled;
        this.canShortCastle = canShortCastle;
        this.canLongCastle = canLongCastle;
    }

    public boolean isCastled() {
        return this.isCastled;
    }

    public boolean isCanShortCastle() {
        return this.canShortCastle;
    }

    public boolean isCanLongCastle() {
        return this.canLongCastle;
    }

    @Override
    public Collection<Move> findLegalMove(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();
        
        for (final int possibleDestinationOffset : POSSIBLE_LEGAL_MOVES_DIRECTION) {
            final int possibleDestinationPosition = this.piecePosition + possibleDestinationOffset;

            if (Utilities.isValidSquarePosition(possibleDestinationPosition)) {

                // skip the problematic position
                if (firstColumn(this.piecePosition, possibleDestinationOffset) ||
                        eightColumn(this.piecePosition, possibleDestinationOffset)) {
                    continue;
                }

                final Square possibleDestinationSquare = board.getSquare(possibleDestinationPosition);

                // check if square is occupied, if occupied, check the color
                // when color differs, capture the piece
                if (!possibleDestinationSquare.isSquareFilled()) {
                    legalMoves.add(new BigMove(board, this, possibleDestinationPosition));
                } else {
                    final Piece pieceAtDestination = possibleDestinationSquare.getPiece();
                    final Color pieceColor = pieceAtDestination.getPieceColor();
                    if (this.pieceColor != pieceColor) {
                        legalMoves.add(new MajorCapturingMove(board,this, possibleDestinationPosition,
                                pieceAtDestination));
                    }
                }
            }
        }

        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public King movePiece(Move move) {
        return new King(move.getDestinationPosition(), move.getMovedPiece().getPieceColor(),
                false, move.isCastlingMove(), false, false);
    }

    @Override
    public String toString() {
        return pieceType.toString();
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof King)) {
            return false;
        }
        if (!super.equals(other)) {
            return false;
        }
        final King king = (King) other;
        return isCastled == king.isCastled;
    }

    @Override
    public int hashCode() {
        return (31 * super.hashCode() + (isCastled ? 1 : 0));
    }

    private static boolean firstColumn(final int currentPosition, final int possibleOffset) {
        return Utilities.FIRST_COLUMN[currentPosition] && (possibleOffset == -9 || possibleOffset == -1 ||
                possibleOffset == 7);
    }

    private static boolean eightColumn(final int currentPosition, final int possibleOffset) {
        return Utilities.EIGHT_COLUMN[currentPosition] && (possibleOffset == -7 || possibleOffset == 1 ||
                possibleOffset == 9);
    }
}
