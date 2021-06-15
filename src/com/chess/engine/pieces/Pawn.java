package com.chess.engine.pieces;

import com.chess.engine.Color;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Utilities;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chess.engine.board.Move.*;

public class Pawn extends Piece {
    // 7 and 9 is due to the pawn capturing diagonally
    private static final int[] POSSIBLE_LEGAL_MOVES_DIRECTION = {7, 8, 9, 16};

    public Pawn(final int piecePosition, final Color pieceColor) {
        super(PieceType.PAWN, piecePosition, pieceColor);
    }

    @Override
    public Collection<Move> findLegalMove(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for (final int possibleDestinationOffset : POSSIBLE_LEGAL_MOVES_DIRECTION) {
            final int possibleDestinationPosition = this.piecePosition + (this.pieceColor.getDirection() *
                    possibleDestinationOffset);

            if (!Utilities.isValidSquarePosition(possibleDestinationPosition)) {
                continue;
            }

            if (possibleDestinationOffset == 8 && !board.getSquare(possibleDestinationPosition).isSquareFilled()) {
                // replace with PawnMove later to deal with promotions
                legalMoves.add(new BigMove(board, this, possibleDestinationPosition));
            } else if (possibleDestinationOffset == 16 && this.isFirstMove() &&
                    (Utilities.SEVENTH_RANK[this.piecePosition] && this.pieceColor.isBlack()) ||
                    (Utilities.SECOND_RANK[this.piecePosition] && this.pieceColor.isWhite())) {
                // check to see move unobstructed
                final int behindPossibleDestinationPosition = this.piecePosition +
                        (this.pieceColor.getDirection() * 8);
                if (!board.getSquare(behindPossibleDestinationPosition).isSquareFilled() &&
                        !board.getSquare(possibleDestinationPosition).isSquareFilled()) {
                    legalMoves.add(new BigMove(board, this, possibleDestinationPosition));
                }
                // capturing diagonally with the edge cases
            } else if (possibleDestinationOffset == 7 &&
                    !((Utilities.EIGHT_COLUMN[this.piecePosition] && this.pieceColor.isWhite() ||
                            (Utilities.FIRST_COLUMN[this.piecePosition] && this.pieceColor.isBlack())))) {
                if (board.getSquare(possibleDestinationPosition).isSquareFilled()) {
                    final Piece pieceAtDestination = board.getSquare(possibleDestinationPosition).getPiece();
                    if (this.pieceColor != pieceAtDestination.getPieceColor()) {
                        legalMoves.add(new CapturingMove(board, this, possibleDestinationPosition,
                                pieceAtDestination));
                    }
                }

            } else if (possibleDestinationOffset == 9 &&
                    !((Utilities.FIRST_COLUMN[this.piecePosition] && this.pieceColor.isBlack() ||
                            (Utilities.EIGHT_COLUMN[this.piecePosition] && this.pieceColor.isWhite())))) {
                if (board.getSquare(possibleDestinationPosition).isSquareFilled()) {
                    final Piece pieceAtDestination = board.getSquare(possibleDestinationPosition).getPiece();
                    if (this.pieceColor != pieceAtDestination.getPieceColor()) {
                        legalMoves.add(new CapturingMove(board, this, possibleDestinationPosition,
                                pieceAtDestination));
                    }
                }
            }
        }

        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Pawn movePiece(Move move) {
        return new Pawn(move.getDestinationPosition(), move.getMovedPiece().getPieceColor());
    }

    @Override
    public String toString() {
        return pieceType.toString();
    }
}
