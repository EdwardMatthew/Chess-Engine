package com.chess.engine.pieces;

import com.chess.engine.Color;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Utilities;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pawn extends Piece {

    private static final int[] POSSIBLE_LEGAL_MOVES_DIRECTION = {8};

    Pawn(final int piecePosition, final Color pieceColor) {
        super(piecePosition, pieceColor);
    }

    @Override
    public Collection<Move> findLegalMove(final Board board) {

        int possibleDestinationPosition;
        final List<Move> legalMoves = new ArrayList<>();

        for (final int possibleDestinationOffset : POSSIBLE_LEGAL_MOVES_DIRECTION) {
            possibleDestinationPosition = this.piecePosition + (this.getPieceColor().getDirection() *
                    possibleDestinationOffset);

            if (!Utilities.isValidSquarePosition(possibleDestinationPosition)) {
                continue;
            }

            if (possibleDestinationOffset == 8 && board.getSquare(possibleDestinationPosition).isSquareFilled()) {
                // replace with PawnMove later
                legalMoves.add(new Move.BigMove(board, this, possibleDestinationPosition));
            }
        }

        return ImmutableList.copyOf(legalMoves);
    }
}
