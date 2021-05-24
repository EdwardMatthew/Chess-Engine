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

public class Queen extends Piece {

    private static final int[] POSSIBLE_LEGAL_MOVES_DIRECTION = {-9, -8, -7, -1, 1, 7, 8, 9};

    Queen(final int piecePosition, final Color pieceColor) {
        super(piecePosition, pieceColor);
    }

    @Override
    public Collection<Move> findLegalMove(final Board board) {

        int possibleDestinationPosition;
        final List<Move> legalMoves = new ArrayList<>();

        for (final int possibleDestinationOffset : POSSIBLE_LEGAL_MOVES_DIRECTION) {
            possibleDestinationPosition = this.piecePosition + possibleDestinationOffset;

            // looping to move the bishop to one of the four possible diagonals
            // continue loop while tile is still valid
            // stop loop when tile is not valid anymore
            while (Utilities.isValidSquarePosition(possibleDestinationPosition)) {
                // break the loop where the algorithm breaks
                if (firstColumn(this.piecePosition, possibleDestinationOffset) ||
                        eightColumn(this.piecePosition, possibleDestinationOffset)) {
                    break;
                }

                possibleDestinationPosition += possibleDestinationOffset;

                if (Utilities.isValidSquarePosition(possibleDestinationPosition)) {
                    final Square possibleDestinationSquare = board.getSquare(possibleDestinationPosition);

                    // if the destination square is not occupied by an enemy piece
                    if (!possibleDestinationSquare.isSquareFilled()) {
                        legalMoves.add(new Move.BigMove(board, this, possibleDestinationPosition));
                    } else {
                        final Piece pieceAtDestination = possibleDestinationSquare.getPiece();
                        final Color pieceColor = pieceAtDestination.getPieceColor();

                        // checking if the piece is capturing an enemy piece
                        if (this.pieceColor != pieceColor) {
                            legalMoves.add(new Move.BigMove(board, this, possibleDestinationPosition));
                        }
                    }
                    break;
                }
            }
        }

        return ImmutableList.copyOf(legalMoves);
    }

    // check where algorithm breaks
    private static boolean firstColumn(final int currentPosition, final int possibleOffset) {
        return Utilities.FIRST_COLUMN[currentPosition] && (possibleOffset == -9 || possibleOffset == 7 ||
                possibleOffset == -1);
    }

    private static boolean eightColumn(final int currentPosition, final int possibleOffset) {
        return Utilities.EIGHT_COLUMN[currentPosition] && (possibleOffset == -7 || possibleOffset == 9 ||
                possibleOffset == 1);
    }
}
