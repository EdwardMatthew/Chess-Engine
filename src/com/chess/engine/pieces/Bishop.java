package com.chess.engine.pieces;

import com.chess.engine.Color;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Square;
import com.chess.engine.board.Utilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Bishop extends Piece {

    private static final int[] POSSIBLE_LEGAL_MOVES_DIRECTION_DIRECTION = {-9, -7, 7, 9};

    Bishop(final int piecePosition, final Color pieceColor) {
        super(piecePosition, pieceColor);
    }

    @Override
    public Collection<Move> findLegalMove(final Board board) {

        int possibleDestinationPosition;
        final List<Move> legalMoves = new ArrayList<>();

        for (final int possibleDestinationOffset : POSSIBLE_LEGAL_MOVES_DIRECTION_DIRECTION) {
            possibleDestinationPosition = this.piecePosition;

            // looping to move the bishop to one of the four possible diagonals
            // continue loop while tile is still valid
            // stop loop when tile is not valid anymore
            while (Utilities.isValidSquarePosition(possibleDestinationPosition)) {
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
        return null;
    }
}
