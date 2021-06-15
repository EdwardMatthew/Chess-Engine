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

import static com.chess.engine.board.Move.*;

public class WhitePlayer extends Player {
    public WhitePlayer(final Board board, final Collection<Move> whiteLegalMoves, final Collection<Move> blackLegalMoves) {
        super(board, whiteLegalMoves, blackLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    @Override
    public Color getColor() {
        return Color.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.blackPlayer();
    }

    @Override
    protected Collection<Move> calculateKingCastling(final Collection<Move> playerLegals,
                                                     final Collection<Move> opponentLegals) {
        final List<Move> kingCastles = new ArrayList<>();

        if (this.playerKing.isFirstMove() && !this.isInCheck()) {
            // white castle short
            // square 61 and 62 is the square beside the king in the king side of the board
            if (!this.board.getSquare(61).isSquareFilled() &&
                    !this.board.getSquare(62).isSquareFilled()) {
                // square 63 is the rook square
                final Square rookSquare = this.board.getSquare(63);

                if (rookSquare.isSquareFilled() && rookSquare.getPiece().isFirstMove()) {
                    if (Player.findAttackOnSquare(61, opponentLegals).isEmpty() &&
                    Player.findAttackOnSquare(62, opponentLegals).isEmpty() &&
                    rookSquare.getPiece().getPieceType().isRook()) {
                        // add king castle move later
                        kingCastles.add(new ShortCastleMove(this.board, this.playerKing, 62,
                                       (Rook)rookSquare.getPiece(), rookSquare.getSquarePosition(), 61));
                    }
                }
            }

            // white castle long
            if (!this.board.getSquare(59).isSquareFilled() &&
                    !this.board.getSquare(58).isSquareFilled() &&
                    !this.board.getSquare(57).isSquareFilled()) {
                // square 56 is the queen side square
                final Square rookSquare = this.board.getSquare(56);

                if (rookSquare.isSquareFilled() && rookSquare.getPiece().isFirstMove()) {
                    if (Player.findAttackOnSquare(59, opponentLegals).isEmpty() &&
                            Player.findAttackOnSquare(58, opponentLegals).isEmpty() &&
                            rookSquare.getPiece().getPieceType().isRook()) {
                        // implement long castle move later
                        kingCastles.add(new LongCastleMove(this.board, this.playerKing, 58,
                                (Rook)rookSquare.getPiece(), rookSquare.getSquarePosition(), 59));
                    }
                }
            }
        }

        return ImmutableList.copyOf(kingCastles);
    }
}
