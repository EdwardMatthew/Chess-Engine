package com.chess.engine.player;

import com.chess.engine.Color;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Square;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BlackPlayer extends Player {
    public BlackPlayer(final Board board, final Collection<Move> whiteLegalMoves, final Collection<Move> blackLegalMoves) {
        super(board, blackLegalMoves, whiteLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

    @Override
    public Color getColor() {
        return Color.BLACK;
    }

    @Override
    public Player getOpponent() {
        return this.board.whitePlayer();
    }

    @Override
    protected Collection<Move> calculateKingCastling(final Collection<Move> playerLegals,
                                                     final Collection<Move> opponentLegals) {
        final List<Move> kingCastles = new ArrayList<>();

        if (this.playerKing.isFirstMove() && !this.isInCheck()) {
            // black castle short
            // square 5 and 6 is the square beside the king in the black king side of the board
            if (!this.board.getSquare(5).isSquareFilled() &&
                    !this.board.getSquare(6).isSquareFilled()) {
                // square 63 is the rook square
                final Square rookSquare = this.board.getSquare(7);

                // check to see if there's a checked square before castling
                if (rookSquare.isSquareFilled() && rookSquare.getPiece().isFirstMove()) {
                    if (Player.findAttackOnSquare(5, opponentLegals).isEmpty() &&
                            Player.findAttackOnSquare(6, opponentLegals).isEmpty() &&
                            rookSquare.getPiece().getPieceType().isRook()) {
                        // add king castle move later
                        kingCastles.add(new Move.ShortCastleMove(this.board, this.playerKing, 6,
                                       (Rook)rookSquare.getPiece(), rookSquare.getSquarePosition(), 5));
                    }
                }
            }

            // black castle long
            // check if there is no obstructing piece before castling long
            if (!this.board.getSquare(1).isSquareFilled() &&
                    !this.board.getSquare(2).isSquareFilled() &&
                    !this.board.getSquare(3).isSquareFilled()) {
                // square 0 is the queen side square for the black rook
                final Square rookSquare = this.board.getSquare(0);

                // check for any attacks before castling
                if (rookSquare.isSquareFilled() && rookSquare.getPiece().isFirstMove()) {
                    if (Player.findAttackOnSquare(1, opponentLegals).isEmpty() &&
                            Player.findAttackOnSquare(2, opponentLegals).isEmpty() &&
                            rookSquare.getPiece().getPieceType().isRook()) {
                        // implement long castle move later
                        kingCastles.add(new Move.LongCastleMove(this.board, this.playerKing, 2,
                                       (Rook)rookSquare.getPiece(), rookSquare.getSquarePosition(), 3));
                    }
                }
            }
        }
        return ImmutableList.copyOf(kingCastles);
    }
}

