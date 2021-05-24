package com.chess.engine.pieces;

import com.chess.engine.Color;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Utilities;
import com.chess.engine.board.Square;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chess.engine.board.Move.*;

public class Knight extends Piece {

    // offset for the maximum possible legal moves of the knight
    private final static int[] POSSIBLE_LEGAL_MOVES = {-17, -15, -10, -6, 6, 10, 15, 17};

    Knight(final int piecePosition, final Color pieceColor) {
        super(piecePosition, pieceColor);
    }

    @Override
    public Collection<Move> findLegalMove(final Board board) {

        int possibleDestinationPosition;
        final List<Move> legalMoves = new ArrayList<>();

        // looping through all of the possible legal moves
        for (final int PossibleDestinationOffset : POSSIBLE_LEGAL_MOVES) {
            possibleDestinationPosition = this.piecePosition + PossibleDestinationOffset;

            // move is possible
            if (Utilities.isValidSquarePosition(possibleDestinationPosition)) {

                // skip the problematic position
                if (firstColumn(this.piecePosition, PossibleDestinationOffset) ||
                    secondColumn(this.piecePosition, PossibleDestinationOffset) ||
                    seventhColumn(this.piecePosition, PossibleDestinationOffset) ||
                    eightColumn(this.piecePosition, PossibleDestinationOffset)) {
                    continue;
                }

                final Square possibleDestinationSquare = board.getSquare(possibleDestinationPosition);

                // if the destination square is not occupied by an enemy piece
                if (!possibleDestinationSquare.isSquareFilled()) {
                    legalMoves.add(new BigMove(board, this, possibleDestinationPosition));
                } else {
                    final Piece pieceAtDestination = possibleDestinationSquare.getPiece();
                    final Color pieceColor = pieceAtDestination.getPieceColor();

                    // checking if the piece is capturing an enemy piece
                    if (this.pieceColor != pieceColor) {
                        legalMoves.add(new BigMove(board, this, possibleDestinationPosition));
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    // check where the algorithm breaks
    // check where the offset breaks the algorithm and skip counting it as legal move
    private static boolean firstColumn(final int currentPosition, final int possibleOffset) {
        return Utilities.FIRST_COLUMN[currentPosition] && (possibleOffset == -17 || possibleOffset == -10 ||
                possibleOffset == 6 || possibleOffset == 15);
    }

    private static boolean secondColumn(final int currentPosition, final int possibleOffset) {
        return Utilities.SECOND_COLUMN[currentPosition] && (possibleOffset == -10 || possibleOffset == 6);

    }

    private static boolean seventhColumn(final int currentPosition, final int possibleOffset) {
        return Utilities.SEVENTH_COLUMN[currentPosition] && (possibleOffset == -6 || possibleOffset == 10);
    }

    private static boolean eightColumn(final int currentPosition, final int possibleOffset) {
        return Utilities.EIGHT_COLUMN[currentPosition] && (possibleOffset == -15 || possibleOffset == -6 ||
                possibleOffset == 10 || possibleOffset == 17);
    }
}

