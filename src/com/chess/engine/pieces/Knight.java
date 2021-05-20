package com.chess.engine.pieces;

import com.chess.engine.Color;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Square;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {

    // offset for the maximum possible legal moves of the knight
    private final static int[] POSSIBLE_LEGAL_MOVES = {-17, -15, -10, -6, 6, 10, 15, 17}

    Knight(final int piecePosition, final Color pieceColor) {
        super(piecePosition, pieceColor);
    }

    @Override
    public List<Move> findLegalMove(Board board) {

        int possibleDestinationPosition;
        final List<Move> legalMoves = new ArrayList<>();

        // looping through all of the possible legal moves
        for (final int currentPossible : POSSIBLE_LEGAL_MOVES) {
            possibleDestinationPosition = this.piecePosition + currentPossible;

            // move is possible
            if (true /* isValidSquarePosition*/) {
                final Square possibleDestinationSquare = board.getSquare(possibleDestinationPosition);

                // if the destination square is not occupied by an enemy piece
                if (!possibleDestinationSquare.isSquareFilled()) {
                    legalMoves.add(new Move());
                } else {
                    final Piece pieceAtDestination = possibleDestinationSquare.getPiece();
                    final Color pieceColor = pieceAtDestination.getPieceColor();

                    // checking if the piece is capturing an enemy piece
                    if (this.pieceColor != pieceColor) {
                        legalMoves.add(new Move());
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }
}

